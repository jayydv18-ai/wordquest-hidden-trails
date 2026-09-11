package com.example.model

enum class Difficulty(val label: String, val badgeColorHex: Long) {
    EASY("Easy", 0xFF4CAF50),
    MEDIUM("Medium", 0xFF2196F3),
    HARD("Hard", 0xFFFF9800),
    EXPERT("Expert", 0xFF9C27B0)
}

data class Level(
    val id: Int,
    val chapterId: Int,
    val theme: String,
    val difficulty: Difficulty,
    val gridSize: Int,
    val words: List<String>,
    val targetTimeSeconds: Int = 120,
    val rewardCoins: Int = 100,
    val rewardHints: Int = 1
)

data class Chapter(
    val id: Int,
    val title: String,
    val subtitle: String,
    val quote: String,
    val startLevel: Int,
    val endLevel: Int,
    val terrainZone: String
)

data class GridPos(val row: Int, val col: Int)

data class WordPlacement(
    val word: String,
    val positions: List<GridPos>,
    val colorIndex: Int = 0
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconType: String,
    val requirement: Int,
    val category: String = "Badges" // Badges, Trophies, Worlds
)

data class DailyRewardDay(
    val day: Int,
    val coins: Int,
    val hints: Int,
    val isSpecialChest: Boolean = false
)
