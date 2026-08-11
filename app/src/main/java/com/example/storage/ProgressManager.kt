package com.example.storage

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "word_adventure_progress")

data class UserProgress(
    val unlockedLevel: Int = 1,
    val starsPerLevel: Map<Int, Int> = emptyMap(),
    val totalStars: Int = 0,
    val totalXP: Int = 0,
    val totalCoins: Int = 50, // Starting coins
    val dailyStreak: Int = 0,
    val hasCompletedToday: Boolean = false,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val voiceEnabled: Boolean = true,
    val reduceMotion: Boolean = false,
    val completedWords: Set<String> = emptySet()
)

class ProgressManager(private val context: Context) {

    companion object {
        private val UNLOCKED_LEVEL = intPreferencesKey("unlocked_level")
        private val TOTAL_STARS = intPreferencesKey("total_stars")
        private val TOTAL_XP = intPreferencesKey("total_xp")
        private val TOTAL_COINS = intPreferencesKey("total_coins")
        private val DAILY_STREAK = intPreferencesKey("daily_streak")
        private val LAST_ACTIVE_EPOCH_DAY = longPreferencesKey("last_active_epoch_day")
        private val LAST_STREAK_BONUS_EPOCH_DAY = longPreferencesKey("last_streak_bonus_epoch_day")
        private val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        private val VOICE_ENABLED = booleanPreferencesKey("voice_enabled")
        private val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val COMPLETED_WORDS = stringSetPreferencesKey("completed_words")
    }

    val progressFlow: Flow<UserProgress> = context.dataStore.data.map { prefs ->
        val unlocked = prefs[UNLOCKED_LEVEL] ?: 1
        val totalStars = prefs[TOTAL_STARS] ?: 0
        val totalXp = prefs[TOTAL_XP] ?: 0
        val totalCoins = prefs[TOTAL_COINS] ?: 50
        val savedStreak = prefs[DAILY_STREAK] ?: 0
        val lastActive = prefs[LAST_ACTIVE_EPOCH_DAY] ?: 0L
        val sound = prefs[SOUND_ENABLED] ?: true
        val music = prefs[MUSIC_ENABLED] ?: true
        val voice = prefs[VOICE_ENABLED] ?: true
        val reduceMotion = prefs[REDUCE_MOTION] ?: false
        val words = prefs[COMPLETED_WORDS] ?: emptySet()

        val today = System.currentTimeMillis() / 86_400_000L
        val hasCompletedToday = (lastActive == today)
        
        // Calculate effective streak based on days elapsed
        val effectiveStreak = when {
            lastActive == today -> if (savedStreak == 0) 1 else savedStreak
            lastActive == today - 1 -> if (savedStreak == 0) 1 else savedStreak
            else -> 0 // Streak reset if missed a day
        }

        // Reconstruct star map from keys "level_stars_X"
        val starMap = mutableMapOf<Int, Int>()
        for (i in 1..1000) {
            val key = intPreferencesKey("level_stars_$i")
            val stars = prefs[key] ?: 0
            if (stars > 0) {
                starMap[i] = stars
            }
        }

        UserProgress(
            unlockedLevel = unlocked,
            starsPerLevel = starMap,
            totalStars = totalStars,
            totalXP = totalXp,
            totalCoins = totalCoins,
            dailyStreak = effectiveStreak,
            hasCompletedToday = hasCompletedToday,
            soundEnabled = sound,
            musicEnabled = music,
            voiceEnabled = voice,
            reduceMotion = reduceMotion,
            completedWords = words
        )
    }

    suspend fun saveLevelCompletion(levelNum: Int, starsEarned: Int, xpEarned: Int, coinsEarned: Int, word: String) {
        context.dataStore.edit { prefs ->
            val currentUnlocked = prefs[UNLOCKED_LEVEL] ?: 1
            if (levelNum >= currentUnlocked && currentUnlocked < 1000) {
                prefs[UNLOCKED_LEVEL] = levelNum + 1
            }

            val levelStarKey = intPreferencesKey("level_stars_$levelNum")
            val previousStars = prefs[levelStarKey] ?: 0
            if (starsEarned > previousStars) {
                val starDiff = starsEarned - previousStars
                prefs[levelStarKey] = starsEarned
                val currentTotalStars = prefs[TOTAL_STARS] ?: 0
                prefs[TOTAL_STARS] = currentTotalStars + starDiff
            }

            val currentXp = prefs[TOTAL_XP] ?: 0
            prefs[TOTAL_XP] = currentXp + xpEarned

            // Handle Daily Streak & Bonus
            val today = System.currentTimeMillis() / 86_400_000L
            val lastActive = prefs[LAST_ACTIVE_EPOCH_DAY] ?: 0L
            val currentStreak = prefs[DAILY_STREAK] ?: 0
            val lastBonusDay = prefs[LAST_STREAK_BONUS_EPOCH_DAY] ?: 0L

            var newStreak = currentStreak
            var bonusCoinsEarned = 0

            if (lastActive == today) {
                newStreak = if (currentStreak == 0) 1 else currentStreak
            } else if (lastActive == today - 1) {
                newStreak = if (currentStreak == 0) 2 else currentStreak + 1
            } else {
                newStreak = 1
            }

            prefs[DAILY_STREAK] = newStreak
            prefs[LAST_ACTIVE_EPOCH_DAY] = today

            // Award daily bonus if not awarded today yet
            if (lastBonusDay != today) {
                val dailyBonus = 20 + (newStreak.coerceAtMost(10) * 10) // 30, 40 ... up to 120 bonus coins
                bonusCoinsEarned = dailyBonus
                prefs[LAST_STREAK_BONUS_EPOCH_DAY] = today
            }

            val currentCoins = prefs[TOTAL_COINS] ?: 50
            prefs[TOTAL_COINS] = currentCoins + coinsEarned + bonusCoinsEarned

            val currentWords = prefs[COMPLETED_WORDS] ?: emptySet()
            prefs[COMPLETED_WORDS] = currentWords + word
        }
    }

    suspend fun addCoins(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[TOTAL_COINS] ?: 50
            prefs[TOTAL_COINS] = (current + amount).coerceAtLeast(0)
        }
    }

    suspend fun updateSettings(sound: Boolean, music: Boolean, voice: Boolean, reduceMotion: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SOUND_ENABLED] = sound
            prefs[MUSIC_ENABLED] = music
            prefs[VOICE_ENABLED] = voice
            prefs[REDUCE_MOTION] = reduceMotion
        }
    }

    suspend fun resetProgress() {
        context.dataStore.edit { prefs ->
            prefs.clear()
            prefs[UNLOCKED_LEVEL] = 1
            prefs[TOTAL_COINS] = 50
            prefs[SOUND_ENABLED] = true
            prefs[MUSIC_ENABLED] = true
            prefs[VOICE_ENABLED] = true
        }
    }
}
