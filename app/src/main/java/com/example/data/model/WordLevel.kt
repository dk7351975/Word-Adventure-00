package com.example.data.model

data class WordLevel(
    val id: Int,
    val levelNumber: Int,
    val word: String,
    val category: String,
    val categoryEmoji: String,
    val pronunciation: String,
    val meaning: String,
    val simpleExplanation: String,
    val exampleSentence: String,
    val hint: String,
    val difficulty: Int, // 1 (Very Easy), 2 (Easy), 3 (Medium), 4 (Advanced), 5 (Master)
    val roundCount: Int = 3
) {
    val letterCount: Int get() = word.length
}
