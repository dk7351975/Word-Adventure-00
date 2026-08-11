package com.example.game

import com.example.data.model.WordLevel
import kotlin.random.Random

data class PuzzleRound(
    val word: String,
    val missingIndex: Int,
    val correctLetter: Char,
    val displayedLetters: List<Char?>, // null represents missing position
    val letterOptions: List<Char>
)

object PuzzleGenerator {

    fun createRound(wordLevel: WordLevel, roundNumber: Int): PuzzleRound {
        val word = wordLevel.word.uppercase()
        val length = word.length

        // Choose missing index deterministically or pseudo-randomly per round
        val missingIndex = when (roundNumber % 3) {
            1 -> (length / 2).coerceIn(0, length - 1)
            2 -> 0
            else -> (length - 1).coerceIn(0, length - 1)
        }

        val correctLetter = word[missingIndex]

        val displayedLetters = word.mapIndexed { index, c ->
            if (index == missingIndex) null else c
        }

        val options = generateLetterOptions(correctLetter, wordLevel.difficulty)

        return PuzzleRound(
            word = word,
            missingIndex = missingIndex,
            correctLetter = correctLetter,
            displayedLetters = displayedLetters,
            letterOptions = options
        )
    }

    private fun generateLetterOptions(correctLetter: Char, difficulty: Int): List<Char> {
        val targetCount = when (difficulty) {
            1, 2 -> 8
            3, 4 -> 12
            else -> 16
        }

        val allAlphabet = ('A'..'Z').toList()
        val distractors = allAlphabet.filter { it != correctLetter }.shuffled()

        val options = mutableListOf<Char>()
        options.add(correctLetter)
        options.addAll(distractors.take(targetCount - 1))

        return options.shuffled()
    }
}
