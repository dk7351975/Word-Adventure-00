package com.example.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioManager
import com.example.data.repository.LevelRepository
import com.example.speech.SpeechManager
import com.example.storage.ProgressManager
import com.example.storage.UserProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameEngine(application: Application) : AndroidViewModel(application) {

    val audioManager = AudioManager()
    val speechManager = SpeechManager(application)
    val progressManager = ProgressManager(application)

    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

    private val _state = MutableStateFlow(
        GameState(currentLevel = LevelRepository.getLevel(1))
    )
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressManager.progressFlow.collect { progress ->
                _userProgress.value = progress
                audioManager.soundEnabled = progress.soundEnabled
                audioManager.musicEnabled = progress.musicEnabled
                speechManager.isEnabled = progress.voiceEnabled
            }
        }
    }

    fun startLevel(levelNumber: Int) {
        val level = LevelRepository.getLevel(levelNumber)
        val firstRound = PuzzleGenerator.createRound(level, 1)

        _state.value = GameState(
            currentLevel = level,
            currentRoundNumber = 1,
            totalRounds = level.roundCount,
            currentPuzzle = firstRound,
            hearts = 3,
            hintUsed = false,
            highlightedHintLetter = null,
            mistakesInLevel = 0,
            isLevelCompleted = false,
            isGameOver = false,
            earnedStars = 3,
            earnedXP = 0,
            earnedCoins = 0,
            feedbackMessage = null,
            showWordExplanation = false,
            isProcessingSelection = false
        )
    }

    fun selectLetter(selectedChar: Char) {
        val current = _state.value
        val puzzle = current.currentPuzzle ?: return
        if (current.isProcessingSelection || current.isLevelCompleted || current.isGameOver) return

        if (selectedChar == puzzle.correctLetter) {
            handleCorrectSelection(selectedChar)
        } else {
            handleWrongSelection()
        }
    }

    private fun handleCorrectSelection(selectedChar: Char) {
        val current = _state.value
        val puzzle = current.currentPuzzle ?: return

        _state.value = current.copy(
            isProcessingSelection = true,
            highlightedHintLetter = null,
            feedbackMessage = "Awesome! 🎉",
            feedbackIsSuccess = true
        )

        audioManager.playCorrect()
        speechManager.speakLetter(selectedChar)

        viewModelScope.launch {
            delay(500) // Brief animation time

            if (current.currentRoundNumber < current.totalRounds) {
                // Advance to next mini round
                val nextRoundNum = current.currentRoundNumber + 1
                val nextPuzzle = PuzzleGenerator.createRound(current.currentLevel, nextRoundNum)
                _state.value = _state.value.copy(
                    currentRoundNumber = nextRoundNum,
                    currentPuzzle = nextPuzzle,
                    isProcessingSelection = false,
                    feedbackMessage = null
                )
            } else {
                // Completed all mini rounds for this level!
                completeLevel()
            }
        }
    }

    private fun handleWrongSelection() {
        val current = _state.value
        val newHearts = (current.hearts - 1).coerceAtLeast(0)
        val newMistakes = current.mistakesInLevel + 1

        val encouragementMessages = listOf("Try again! 😊", "Almost! Keep going! 💪", "Good try! You can do it! ⭐")
        val randomMessage = encouragementMessages.random()

        audioManager.playWrong()

        if (newHearts == 0) {
            audioManager.playHeartLost()
            _state.value = current.copy(
                hearts = 0,
                mistakesInLevel = newMistakes,
                isGameOver = true,
                feedbackMessage = "Don't worry! Let's try again.",
                feedbackIsSuccess = false,
                shakeTriggerKey = current.shakeTriggerKey + 1
            )
        } else {
            audioManager.playHeartLost()
            _state.value = current.copy(
                hearts = newHearts,
                mistakesInLevel = newMistakes,
                feedbackMessage = randomMessage,
                feedbackIsSuccess = false,
                shakeTriggerKey = current.shakeTriggerKey + 1
            )
        }
    }

    private fun completeLevel() {
        val current = _state.value
        val stars = when {
            current.hintUsed -> 1
            current.mistakesInLevel == 0 -> 3
            current.mistakesInLevel in 1..2 -> 2
            else -> 1
        }

        val baseXP = 20
        val bonusXP = if (stars == 3) 10 else 0
        val earnedXP = baseXP + bonusXP

        val baseCoins = 10
        val bonusCoins = if (stars == 3) 5 else 0
        val earnedCoins = baseCoins + bonusCoins

        audioManager.playLevelComplete()
        speechManager.speakWord(current.currentLevel.word)

        _state.value = current.copy(
            isLevelCompleted = true,
            earnedStars = stars,
            earnedXP = earnedXP,
            earnedCoins = earnedCoins,
            feedbackMessage = "GREAT JOB! 🎉",
            showWordExplanation = true,
            isProcessingSelection = false
        )

        viewModelScope.launch {
            progressManager.saveLevelCompletion(
                levelNum = current.currentLevel.levelNumber,
                starsEarned = stars,
                xpEarned = earnedXP,
                coinsEarned = earnedCoins,
                word = current.currentLevel.word
            )
        }
    }

    fun applyHint() {
        val current = _state.value
        val puzzle = current.currentPuzzle ?: return
        audioManager.playHint()

        _state.value = current.copy(
            hintUsed = true,
            highlightedHintLetter = puzzle.correctLetter,
            feedbackMessage = "Hint activated! Tap the glowing letter 💡"
        )
    }

    fun restoreOneHeartWithAd(): Boolean {
        val current = _state.value
        if (current.hearts >= 3) {
            return false
        }
        val newHearts = (current.hearts + 1).coerceAtMost(3)
        audioManager.playClick()
        _state.value = current.copy(
            hearts = newHearts,
            isGameOver = false,
            feedbackMessage = "+1 Heart restored! ❤️",
            feedbackIsSuccess = true
        )
        return true
    }

    fun retryWithRewardedAd() {
        val current = _state.value
        audioManager.playClick()
        _state.value = current.copy(
            hearts = 3,
            isGameOver = false,
            feedbackMessage = "Hearts restored! Let's play! ❤️"
        )
    }

    fun replayWordPronunciation() {
        val current = _state.value
        speechManager.speakWord(current.currentLevel.word)
    }

    fun updateSettings(sound: Boolean, music: Boolean, voice: Boolean, reduceMotion: Boolean) {
        viewModelScope.launch {
            progressManager.updateSettings(sound, music, voice, reduceMotion)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            progressManager.resetProgress()
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.shutdown()
    }
}
