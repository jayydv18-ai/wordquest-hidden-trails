package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GameState(
    val unlockedLevel: Int = 1,
    val completedLevels: Set<Int> = emptySet(),
    val levelStars: Map<Int, Int> = emptyMap(),
    val levelBestTimes: Map<Int, Int> = emptyMap(),
    val coins: Int = 850,
    val hints: Int = 5,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val tutorialCompleted: Boolean = false,
    val dailyRewardClaimedDay: Int = 0,
    val lastClaimDate: String = "",
    val dailyStreak: Int = 1,
    val unlockedAchievements: Set<String> = emptySet(),
    val wordsFoundCount: Int = 0,
    val playerName: String = "Jay",
    val playerLevel: Int = 5
)

class GamePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("wordquest_prefs", Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(loadInitialState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private fun loadInitialState(): GameState {
        val unlockedLevel = prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
        val completedStr = prefs.getString(KEY_COMPLETED_LEVELS, "") ?: ""
        val completedLevels = if (completedStr.isBlank()) emptySet() else {
            completedStr.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }

        val starsStr = prefs.getString(KEY_LEVEL_STARS, "") ?: ""
        val levelStars = mutableMapOf<Int, Int>()
        if (starsStr.isNotBlank()) {
            starsStr.split(";").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    val k = parts[0].toIntOrNull()
                    val v = parts[1].toIntOrNull()
                    if (k != null && v != null) levelStars[k] = v
                }
            }
        }

        val timesStr = prefs.getString(KEY_LEVEL_TIMES, "") ?: ""
        val levelTimes = mutableMapOf<Int, Int>()
        if (timesStr.isNotBlank()) {
            timesStr.split(";").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    val k = parts[0].toIntOrNull()
                    val v = parts[1].toIntOrNull()
                    if (k != null && v != null) levelTimes[k] = v
                }
            }
        }

        val achievementsStr = prefs.getString(KEY_ACHIEVEMENTS, "") ?: ""
        val achievements = if (achievementsStr.isBlank()) emptySet() else {
            achievementsStr.split(",").toSet()
        }

        return GameState(
            unlockedLevel = unlockedLevel,
            completedLevels = completedLevels,
            levelStars = levelStars,
            levelBestTimes = levelTimes,
            coins = prefs.getInt(KEY_COINS, 850),
            hints = prefs.getInt(KEY_HINTS, 5),
            soundEnabled = prefs.getBoolean(KEY_SOUND, true),
            musicEnabled = prefs.getBoolean(KEY_MUSIC, true),
            vibrationEnabled = prefs.getBoolean(KEY_VIBRATION, true),
            notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true),
            tutorialCompleted = prefs.getBoolean(KEY_TUTORIAL, false),
            dailyRewardClaimedDay = prefs.getInt(KEY_DAILY_DAY, 0),
            lastClaimDate = prefs.getString(KEY_LAST_CLAIM_DATE, "") ?: "",
            dailyStreak = prefs.getInt(KEY_DAILY_STREAK, 1),
            unlockedAchievements = achievements,
            wordsFoundCount = prefs.getInt(KEY_WORDS_FOUND, 0),
            playerName = prefs.getString(KEY_PLAYER_NAME, "Jay") ?: "Jay",
            playerLevel = prefs.getInt(KEY_PLAYER_LEVEL, 5)
        )
    }

    fun addCoins(amount: Int) {
        val newCoins = (_state.value.coins + amount).coerceAtLeast(0)
        prefs.edit().putInt(KEY_COINS, newCoins).apply()
        _state.value = _state.value.copy(coins = newCoins)
    }

    fun spendCoins(amount: Int): Boolean {
        if (_state.value.coins >= amount) {
            val newCoins = _state.value.coins - amount
            prefs.edit().putInt(KEY_COINS, newCoins).apply()
            _state.value = _state.value.copy(coins = newCoins)
            return true
        }
        return false
    }

    fun addHints(count: Int) {
        val newHints = (_state.value.hints + count).coerceAtLeast(0)
        prefs.edit().putInt(KEY_HINTS, newHints).apply()
        _state.value = _state.value.copy(hints = newHints)
    }

    fun useHint(): Boolean {
        if (_state.value.hints > 0) {
            val newHints = _state.value.hints - 1
            prefs.edit().putInt(KEY_HINTS, newHints).apply()
            _state.value = _state.value.copy(hints = newHints)
            return true
        }
        return false
    }

    fun recordLevelCompletion(levelId: Int, stars: Int, timeSeconds: Int, wordsFoundInLevel: Int) {
        val current = _state.value
        val newCompleted = current.completedLevels + levelId
        val newUnlocked = maxOf(current.unlockedLevel, levelId + 1)
        val oldStars = current.levelStars[levelId] ?: 0
        val newStars = current.levelStars + (levelId to maxOf(oldStars, stars))
        val oldTime = current.levelBestTimes[levelId] ?: Int.MAX_VALUE
        val newTimes = current.levelBestTimes + (levelId to minOf(oldTime, timeSeconds))
        val totalWords = current.wordsFoundCount + wordsFoundInLevel
        val newPlayerLevel = 1 + (newCompleted.size / 2)

        // Check achievements
        val unlockedAch = current.unlockedAchievements.toMutableSet()
        unlockedAch.add("first_word")
        if (stars == 3) unlockedAch.add("first_perfect")
        if (newCompleted.size >= 10) unlockedAch.add("levels_10")
        if (newCompleted.size >= 20) unlockedAch.add("levels_20")
        if (totalWords >= 50) unlockedAch.add("words_50")
        if (totalWords >= 100) unlockedAch.add("words_100")
        if (newStars.values.count { it == 3 } >= 15) unlockedAch.add("three_star_master")
        if (newCompleted.containsAll(listOf(1, 2, 3, 4, 5))) unlockedAch.add("chapter_1_clear")
        if (newCompleted.containsAll(listOf(6, 7, 8, 9, 10))) unlockedAch.add("chapter_2_clear")
        if (newCompleted.containsAll(listOf(11, 12, 13, 14, 15))) unlockedAch.add("chapter_3_clear")
        if (newCompleted.containsAll(listOf(16, 17, 18, 19, 20))) unlockedAch.add("chapter_4_clear")

        // Chapter 5-10 clear checks
        if (newCompleted.containsAll((21..25).toList())) unlockedAch.add("chapter_5_clear")
        if (newCompleted.containsAll((26..30).toList())) unlockedAch.add("chapter_6_clear")
        if (newCompleted.containsAll((31..35).toList())) unlockedAch.add("chapter_7_clear")
        if (newCompleted.containsAll((36..40).toList())) unlockedAch.add("chapter_8_clear")
        if (newCompleted.containsAll((41..45).toList())) unlockedAch.add("chapter_9_clear")
        if (newCompleted.containsAll((46..50).toList())) unlockedAch.add("chapter_10_clear")

        // Specific Level landmark achievements
        if (newCompleted.contains(25)) unlockedAch.add("sunset_explorer")
        if (newCompleted.contains(30)) unlockedAch.add("forest_walker")
        if (newCompleted.contains(35)) unlockedAch.add("ice_climber")
        if (newCompleted.contains(40)) unlockedAch.add("temple_seeker")
        if (newCompleted.contains(45)) unlockedAch.add("sky_explorer")
        if (newCompleted.contains(50)) unlockedAch.add("kingdom_discoverer")

        // 50 levels completed
        if (newCompleted.containsAll((1..50).toList())) unlockedAch.add("fifty_wordquest")

        // Perfect journey: 3 stars on levels 21-50
        val expansion3Stars = (21..50).all { lvlId -> (newStars[lvlId] ?: 0) == 3 }
        if (expansion3Stars) unlockedAch.add("perfect_journey")

        prefs.edit()
            .putInt(KEY_UNLOCKED_LEVEL, newUnlocked)
            .putString(KEY_COMPLETED_LEVELS, newCompleted.joinToString(","))
            .putString(KEY_LEVEL_STARS, newStars.entries.joinToString(";") { "${it.key}:${it.value}" })
            .putString(KEY_LEVEL_TIMES, newTimes.entries.joinToString(";") { "${it.key}:${it.value}" })
            .putString(KEY_ACHIEVEMENTS, unlockedAch.joinToString(","))
            .putInt(KEY_WORDS_FOUND, totalWords)
            .putInt(KEY_PLAYER_LEVEL, newPlayerLevel)
            .apply()

        _state.value = current.copy(
            unlockedLevel = newUnlocked,
            completedLevels = newCompleted,
            levelStars = newStars,
            levelBestTimes = newTimes,
            unlockedAchievements = unlockedAch,
            wordsFoundCount = totalWords,
            playerLevel = newPlayerLevel
        )
    }

    fun setTutorialCompleted() {
        prefs.edit().putBoolean(KEY_TUTORIAL, true).apply()
        _state.value = _state.value.copy(tutorialCompleted = true)
    }

    fun toggleSound(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
        _state.value = _state.value.copy(soundEnabled = enabled)
    }

    fun toggleMusic(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MUSIC, enabled).apply()
        _state.value = _state.value.copy(musicEnabled = enabled)
    }

    fun toggleVibration(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATION, enabled).apply()
        _state.value = _state.value.copy(vibrationEnabled = enabled)
    }

    fun toggleNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
        _state.value = _state.value.copy(notificationsEnabled = enabled)
    }

    fun claimDailyReward(day: Int, coins: Int, hints: Int) {
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val nextDay = if (day >= 7) 1 else day
        val newCoins = _state.value.coins + coins
        val newHints = _state.value.hints + hints
        val newStreak = _state.value.dailyStreak + 1

        prefs.edit()
            .putInt(KEY_DAILY_DAY, nextDay)
            .putString(KEY_LAST_CLAIM_DATE, todayStr)
            .putInt(KEY_DAILY_STREAK, newStreak)
            .putInt(KEY_COINS, newCoins)
            .putInt(KEY_HINTS, newHints)
            .apply()

        _state.value = _state.value.copy(
            dailyRewardClaimedDay = nextDay,
            lastClaimDate = todayStr,
            dailyStreak = newStreak,
            coins = newCoins,
            hints = newHints
        )
    }

    fun canClaimDailyReward(): Boolean {
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        return _state.value.lastClaimDate != todayStr
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
        _state.value = GameState()
    }

    companion object {
        private const val KEY_UNLOCKED_LEVEL = "unlocked_level"
        private const val KEY_COMPLETED_LEVELS = "completed_levels"
        private const val KEY_LEVEL_STARS = "level_stars"
        private const val KEY_LEVEL_TIMES = "level_times"
        private const val KEY_COINS = "coins"
        private const val KEY_HINTS = "hints"
        private const val KEY_SOUND = "sound"
        private const val KEY_MUSIC = "music"
        private const val KEY_VIBRATION = "vibration"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_TUTORIAL = "tutorial_done"
        private const val KEY_DAILY_DAY = "daily_day"
        private const val KEY_LAST_CLAIM_DATE = "last_claim_date"
        private const val KEY_DAILY_STREAK = "daily_streak"
        private const val KEY_ACHIEVEMENTS = "achievements"
        private const val KEY_WORDS_FOUND = "words_found"
        private const val KEY_PLAYER_NAME = "player_name"
        private const val KEY_PLAYER_LEVEL = "player_level"
    }
}
