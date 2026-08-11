package com.example.game

import com.example.data.model.WordLevel

data class GameState(
    val currentLevel: WordLevel,
    val currentRoundNumber: Int = 1,
    val totalRounds: Int = 3,
    val currentPuzzle: PuzzleRound? = null,
    val hearts: Int = 3,
    val hintUsed: Boolean = false,
    val highlightedHintLetter: Char? = null,
    val mistakesInLevel: Int = 0,
    val isLevelCompleted: Boolean = false,
    val isGameOver: Boolean = false,
    val earnedStars: Int = 3,
    val earnedXP: Int = 0,
    val earnedCoins: Int = 0,
    val feedbackMessage: String? = null,
    val feedbackIsSuccess: Boolean = true,
    val showWordExplanation: Boolean = false,
    val isProcessingSelection: Boolean = false,
    val shakeTriggerKey: Int = 0
)
