package com.example.data

import com.example.model.Achievement
import com.example.model.Chapter
import com.example.model.DailyRewardDay
import com.example.model.Difficulty
import com.example.model.Level
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LevelRepository {

    val chapters = listOf(
        Chapter(
            id = 1,
            title = "Chapter 1",
            subtitle = "Whispering Meadows",
            quote = "A gentle breeze carries the first whispers of your adventure.",
            startLevel = 1,
            endLevel = 5,
            terrainZone = "Meadows"
        ),
        Chapter(
            id = 2,
            title = "Chapter 2",
            subtitle = "Misty Woods",
            quote = "Mysterious paths, new words, new stories await beyond the ancient trees.",
            startLevel = 6,
            endLevel = 10,
            terrainZone = "Forest"
        ),
        Chapter(
            id = 3,
            title = "Chapter 3",
            subtitle = "Crystal Valley",
            quote = "Luminous crystals light the trail across shining streams and bridges.",
            startLevel = 11,
            endLevel = 15,
            terrainZone = "Crystal"
        ),
        Chapter(
            id = 4,
            title = "Chapter 4",
            subtitle = "Ancient Summit",
            quote = "Climb the heights of discovery where the ultimate word treasure lies.",
            startLevel = 16,
            endLevel = 20,
            terrainZone = "Summit"
        ),
        Chapter(
            id = 5,
            title = "Chapter 5",
            subtitle = "Sunset Coast",
            quote = "Warm coastal sands and sunset sea breezes beckon the word explorer.",
            startLevel = 21,
            endLevel = 25,
            terrainZone = "Coast"
        ),
        Chapter(
            id = 6,
            title = "Chapter 6",
            subtitle = "Enchanted Forest",
            quote = "Tall ancient canopies, glowing mushrooms, and firefly-lit trail secrets.",
            startLevel = 26,
            endLevel = 30,
            terrainZone = "Enchanted"
        ),
        Chapter(
            id = 7,
            title = "Chapter 7",
            subtitle = "Frozen Peaks",
            quote = "Glacial summits, crystal caves, and sparkling diamond treasures.",
            startLevel = 31,
            endLevel = 35,
            terrainZone = "Frozen"
        ),
        Chapter(
            id = 8,
            title = "Chapter 8",
            subtitle = "Desert Ruins",
            quote = "Ancient temple pillars and pharaoh relics hidden beneath golden dunes.",
            startLevel = 36,
            endLevel = 40,
            terrainZone = "Desert"
        ),
        Chapter(
            id = 9,
            title = "Chapter 9",
            subtitle = "Sky Islands",
            quote = "Floating aerial archipelagos, soaring airships, and cosmic heights.",
            startLevel = 41,
            endLevel = 45,
            terrainZone = "Sky"
        ),
        Chapter(
            id = 10,
            title = "Chapter 10",
            subtitle = "Ancient Kingdom",
            quote = "The grand royal castle bathed in starlight, where epic destinies culminate.",
            startLevel = 46,
            endLevel = 50,
            terrainZone = "Kingdom"
        )
    )

    val levels = listOf(
        // CHAPTER 1 — WHISPERING MEADOWS
        Level(
            id = 1,
            chapterId = 1,
            theme = "FRIENDSHIP",
            difficulty = Difficulty.EASY,
            gridSize = 8,
            words = listOf("FRIEND", "TRUST", "CHAT", "SMILE", "LOVE", "PARTY", "MUSIC", "TRIP", "DANCE"),
            targetTimeSeconds = 90,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 2,
            chapterId = 1,
            theme = "SWEETS",
            difficulty = Difficulty.EASY,
            gridSize = 8,
            words = listOf("GELATO", "SWEET", "TASTE", "TREAT", "COOKIE", "CAKE", "CANDY", "CREAM", "SUGAR"),
            targetTimeSeconds = 90,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 3,
            chapterId = 1,
            theme = "HOUSE",
            difficulty = Difficulty.EASY,
            gridSize = 10,
            words = listOf("BOOKCASE", "TOWEL", "STOOL", "DESK", "MIRROR", "HANGER", "MIXER", "ESPRESSO", "DISHWASHER"),
            targetTimeSeconds = 110,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 4,
            chapterId = 1,
            theme = "NATURE",
            difficulty = Difficulty.EASY,
            gridSize = 8,
            words = listOf("FOREST", "FLOWER", "RIVER", "LEAF", "STONE", "CLOUD", "GRASS", "TREE", "SUN"),
            targetTimeSeconds = 90,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 5,
            chapterId = 1,
            theme = "TRAVEL",
            difficulty = Difficulty.EASY,
            gridSize = 8,
            words = listOf("TRAIN", "ROAD", "TRIP", "MAP", "HOTEL", "TICKET", "BAG", "PLANE", "CAMP"),
            targetTimeSeconds = 100,
            rewardCoins = 100,
            rewardHints = 1
        ),

        // CHAPTER 2 — MISTY WOODS
        Level(
            id = 6,
            chapterId = 2,
            theme = "ANIMALS",
            difficulty = Difficulty.EASY,
            gridSize = 8,
            words = listOf("TIGER", "HORSE", "RABBIT", "PANDA", "MONKEY", "FOX", "BEAR", "DEER", "WOLF"),
            targetTimeSeconds = 100,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 7,
            chapterId = 2,
            theme = "OCEAN",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("OCEAN", "WHALE", "SHARK", "CORAL", "SHELL", "WAVE", "FISH", "DOLPHIN", "ISLAND"),
            targetTimeSeconds = 110,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 8,
            chapterId = 2,
            theme = "KITCHEN",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("PLATE", "SPOON", "KNIFE", "GLASS", "OVEN", "PAN", "BOWL", "FORK", "STOVE"),
            targetTimeSeconds = 110,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 9,
            chapterId = 2,
            theme = "MUSIC",
            difficulty = Difficulty.MEDIUM,
            gridSize = 10,
            words = listOf("GUITAR", "PIANO", "DRUM", "SINGER", "MELODY", "RHYTHM", "SONG", "STAGE", "MICROPHONE"),
            targetTimeSeconds = 120,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 10,
            chapterId = 2,
            theme = "SPACE",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("PLANET", "ROCKET", "MOON", "STAR", "COMET", "ORBIT", "GALAXY", "SPACE", "ASTEROID"),
            targetTimeSeconds = 120,
            rewardCoins = 100,
            rewardHints = 2
        ),

        // CHAPTER 3 — CRYSTAL VALLEY
        Level(
            id = 11,
            chapterId = 3,
            theme = "ADVENTURE",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("JOURNEY", "EXPLORE", "CAMP", "TRAIL", "TREASURE", "CAVE", "BRIDGE", "COMPASS", "MOUNTAIN"),
            targetTimeSeconds = 120,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 12,
            chapterId = 3,
            theme = "SCHOOL",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("TEACHER", "STUDENT", "BOOK", "PENCIL", "CLASS", "DESK", "LESSON", "EXAM", "SCHOOL"),
            targetTimeSeconds = 120,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 13,
            chapterId = 3,
            theme = "SPORTS",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("FOOTBALL", "CRICKET", "TENNIS", "RUNNER", "PLAYER", "GOAL", "TEAM", "MATCH", "TROPHY"),
            targetTimeSeconds = 130,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 14,
            chapterId = 3,
            theme = "TECHNOLOGY",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("COMPUTER", "MOBILE", "SCREEN", "KEYBOARD", "CAMERA", "INTERNET", "ROBOT", "DIGITAL", "SOFTWARE"),
            targetTimeSeconds = 130,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 15,
            chapterId = 3,
            theme = "FESTIVAL",
            difficulty = Difficulty.HARD,
            gridSize = 11,
            words = listOf("FESTIVAL", "LIGHT", "MUSIC", "DANCE", "COLOR", "CELEBRATION", "FLOWER", "LANTERN", "FAMILY"),
            targetTimeSeconds = 140,
            rewardCoins = 100,
            rewardHints = 2
        ),

        // CHAPTER 4 — ANCIENT SUMMIT
        Level(
            id = 16,
            chapterId = 4,
            theme = "OCEAN LIFE",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("SEASHELL", "OCTOPUS", "DOLPHIN", "CORAL", "TURTLE", "WHALE", "SEAHORSE", "STARFISH", "JELLYFISH"),
            targetTimeSeconds = 140,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 17,
            chapterId = 4,
            theme = "EXPLORER",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("DISCOVER", "JOURNEY", "EXPLORE", "ADVENTURE", "COMPASS", "TREASURE", "ANCIENT", "TEMPLE", "RUINS"),
            targetTimeSeconds = 140,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 18,
            chapterId = 4,
            theme = "SCIENCE",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("SCIENCE", "ENERGY", "ATOM", "MATTER", "FORCE", "GRAVITY", "MOLECULE", "PLANET", "EXPERIMENT"),
            targetTimeSeconds = 140,
            rewardCoins = 100,
            rewardHints = 1
        ),
        Level(
            id = 19,
            chapterId = 4,
            theme = "MYSTERY",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("MYSTERY", "SECRET", "CLUE", "SHADOW", "PUZZLE", "HIDDEN", "CODE", "KEY", "DISCOVER"),
            targetTimeSeconds = 140,
            rewardCoins = 100,
            rewardHints = 2
        ),
        Level(
            id = 20,
            chapterId = 4,
            theme = "DESTINATION",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("ADVENTURE", "JOURNEY", "TREASURE", "DISCOVERY", "FRIENDSHIP", "COURAGE", "MYSTERY", "DESTINATION", "VICTORY"),
            targetTimeSeconds = 160,
            rewardCoins = 100,
            rewardHints = 3
        ),

        // CHAPTER 5 — SUNSET COAST (LEVELS 21–25)
        Level(
            id = 21,
            chapterId = 5,
            theme = "BEACH",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("BEACH", "SAND", "WAVE", "SHELL", "SUNSET", "PALM", "OCEAN", "ISLAND", "BREEZE"),
            targetTimeSeconds = 120,
            rewardCoins = 35,
            rewardHints = 1
        ),
        Level(
            id = 22,
            chapterId = 5,
            theme = "CAMPING",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("TENT", "CAMP", "FIRE", "TRAIL", "ROPE", "LANTERN", "BACKPACK", "FOREST", "ADVENTURE"),
            targetTimeSeconds = 120,
            rewardCoins = 35,
            rewardHints = 1
        ),
        Level(
            id = 23,
            chapterId = 5,
            theme = "WEATHER",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("RAIN", "STORM", "CLOUD", "THUNDER", "LIGHTNING", "WIND", "SUNSHINE", "RAINBOW", "BREEZE"),
            targetTimeSeconds = 120,
            rewardCoins = 35,
            rewardHints = 1
        ),
        Level(
            id = 24,
            chapterId = 5,
            theme = "FRUIT",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("APPLE", "BANANA", "ORANGE", "MANGO", "PAPAYA", "GRAPE", "CHERRY", "PEACH", "LEMON"),
            targetTimeSeconds = 120,
            rewardCoins = 40,
            rewardHints = 1
        ),
        Level(
            id = 25,
            chapterId = 5,
            theme = "SEA ADVENTURE",
            difficulty = Difficulty.MEDIUM,
            gridSize = 10,
            words = listOf("SAILOR", "ANCHOR", "CAPTAIN", "OCEAN", "VOYAGE", "COMPASS", "SHIP", "ISLAND", "TREASURE"),
            targetTimeSeconds = 130,
            rewardCoins = 40,
            rewardHints = 1
        ),

        // CHAPTER 6 — ENCHANTED FOREST (LEVELS 26–30)
        Level(
            id = 26,
            chapterId = 6,
            theme = "FOREST",
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = listOf("FOREST", "TREE", "MOSS", "LEAF", "ROOT", "BRANCH", "MUSHROOM", "PINE", "WOODLAND"),
            targetTimeSeconds = 125,
            rewardCoins = 45,
            rewardHints = 1
        ),
        Level(
            id = 27,
            chapterId = 6,
            theme = "BIRDS",
            difficulty = Difficulty.MEDIUM,
            gridSize = 10,
            words = listOf("EAGLE", "PARROT", "SPARROW", "OWL", "FALCON", "PENGUIN", "SWALLOW", "FEATHER", "NEST"),
            targetTimeSeconds = 130,
            rewardCoins = 45,
            rewardHints = 1
        ),
        Level(
            id = 28,
            chapterId = 6,
            theme = "MAGIC",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("MAGIC", "SPELL", "WIZARD", "POTION", "CRYSTAL", "ENCHANT", "WAND", "MYSTERY", "CHARM"),
            targetTimeSeconds = 140,
            rewardCoins = 50,
            rewardHints = 1
        ),
        Level(
            id = 29,
            chapterId = 6,
            theme = "NIGHT",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("MOON", "STARS", "NIGHT", "SHADOW", "DREAM", "DARKNESS", "OWL", "MIDNIGHT", "SILENCE"),
            targetTimeSeconds = 140,
            rewardCoins = 50,
            rewardHints = 1
        ),
        Level(
            id = 30,
            chapterId = 6,
            theme = "FOREST TREASURE",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("TREASURE", "SECRET", "ANCIENT", "FOREST", "CRYSTAL", "CHEST", "MAP", "KEY", "MYSTERY"),
            targetTimeSeconds = 140,
            rewardCoins = 50,
            rewardHints = 2
        ),

        // CHAPTER 7 — FROZEN PEAKS (LEVELS 31–35)
        Level(
            id = 31,
            chapterId = 7,
            theme = "WINTER",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("WINTER", "SNOW", "ICE", "FROST", "COLD", "SCARF", "GLOVES", "COAT", "SNOWFLAKE"),
            targetTimeSeconds = 140,
            rewardCoins = 55,
            rewardHints = 1
        ),
        Level(
            id = 32,
            chapterId = 7,
            theme = "MOUNTAINS",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("MOUNTAIN", "SUMMIT", "CLIFF", "VALLEY", "ROCK", "PEAK", "TRAIL", "CLIMB", "EVEREST"),
            targetTimeSeconds = 140,
            rewardCoins = 55,
            rewardHints = 1
        ),
        Level(
            id = 33,
            chapterId = 7,
            theme = "ARCTIC ANIMALS",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("POLAR", "BEAR", "WALRUS", "SEAL", "PENGUIN", "ARCTIC", "FOX", "WHALE", "TUNDRA"),
            targetTimeSeconds = 140,
            rewardCoins = 60,
            rewardHints = 1
        ),
        Level(
            id = 34,
            chapterId = 7,
            theme = "ICE CAVE",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("CAVE", "ICE", "CRYSTAL", "TUNNEL", "FROZEN", "ECHO", "COLD", "GLOW", "STONE"),
            targetTimeSeconds = 140,
            rewardCoins = 60,
            rewardHints = 1
        ),
        Level(
            id = 35,
            chapterId = 7,
            theme = "FROZEN TREASURE",
            difficulty = Difficulty.HARD,
            gridSize = 11,
            words = listOf("TREASURE", "DIAMOND", "CRYSTAL", "FROZEN", "ANCIENT", "CROWN", "CHEST", "ICEBERG", "SECRET"),
            targetTimeSeconds = 150,
            rewardCoins = 60,
            rewardHints = 2
        ),

        // CHAPTER 8 — DESERT RUINS (LEVELS 36–40)
        Level(
            id = 36,
            chapterId = 8,
            theme = "DESERT",
            difficulty = Difficulty.HARD,
            gridSize = 10,
            words = listOf("DESERT", "SAND", "DUNE", "OASIS", "CAMEL", "SUN", "HEAT", "MIRAGE", "SCORCH"),
            targetTimeSeconds = 140,
            rewardCoins = 65,
            rewardHints = 1
        ),
        Level(
            id = 37,
            chapterId = 8,
            theme = "ANCIENT EGYPT",
            difficulty = Difficulty.HARD,
            gridSize = 11,
            words = listOf("PYRAMID", "PHARAOH", "TEMPLE", "MUMMY", "SCARAB", "SPHINX", "GOLD", "TOMB", "DESERT"),
            targetTimeSeconds = 150,
            rewardCoins = 65,
            rewardHints = 1
        ),
        Level(
            id = 38,
            chapterId = 8,
            theme = "ARCHAEOLOGY",
            difficulty = Difficulty.HARD,
            gridSize = 11,
            words = listOf("ARCHAEOLOGY", "ARTIFACT", "EXCAVATE", "RUINS", "ANCIENT", "RELIC", "DISCOVER", "HISTORY", "TEMPLE"),
            targetTimeSeconds = 150,
            rewardCoins = 70,
            rewardHints = 1
        ),
        Level(
            id = 39,
            chapterId = 8,
            theme = "ANCIENT CITY",
            difficulty = Difficulty.HARD,
            gridSize = 11,
            words = listOf("KINGDOM", "PALACE", "CASTLE", "TEMPLE", "MARKET", "GATE", "TOWER", "STREET", "STATUE"),
            targetTimeSeconds = 150,
            rewardCoins = 70,
            rewardHints = 1
        ),
        Level(
            id = 40,
            chapterId = 8,
            theme = "LOST TEMPLE",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("TEMPLE", "TREASURE", "ANCIENT", "SECRET", "CHAMBER", "PUZZLE", "STATUE", "RELIC", "KINGDOM"),
            targetTimeSeconds = 160,
            rewardCoins = 70,
            rewardHints = 2
        ),

        // CHAPTER 9 — SKY ISLANDS (LEVELS 41–45)
        Level(
            id = 41,
            chapterId = 9,
            theme = "SKY",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("CLOUD", "SKY", "WIND", "BREEZE", "SUN", "RAIN", "STORM", "RAINBOW", "THUNDER"),
            targetTimeSeconds = 150,
            rewardCoins = 75,
            rewardHints = 2
        ),
        Level(
            id = 42,
            chapterId = 9,
            theme = "FLYING",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("FLIGHT", "WINGS", "EAGLE", "AIRSHIP", "CLOUD", "PILOT", "GLIDER", "SKY", "JOURNEY"),
            targetTimeSeconds = 150,
            rewardCoins = 75,
            rewardHints = 2
        ),
        Level(
            id = 43,
            chapterId = 9,
            theme = "SPACE TRAVEL",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("ROCKET", "PLANET", "GALAXY", "ASTEROID", "ORBIT", "COMET", "STAR", "COSMOS", "UNIVERSE"),
            targetTimeSeconds = 160,
            rewardCoins = 80,
            rewardHints = 2
        ),
        Level(
            id = 44,
            chapterId = 9,
            theme = "STARS",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("STAR", "CONSTELLATION", "MOON", "GALAXY", "NEBULA", "METEOR", "COSMIC", "ORBIT", "NIGHT"),
            targetTimeSeconds = 170,
            rewardCoins = 80,
            rewardHints = 2
        ),
        Level(
            id = 45,
            chapterId = 9,
            theme = "CELESTIAL TREASURE",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("CELESTIAL", "TREASURE", "CRYSTAL", "STARLIGHT", "GALAXY", "MYSTERY", "ANCIENT", "DISCOVERY", "DESTINY"),
            targetTimeSeconds = 170,
            rewardCoins = 80,
            rewardHints = 3
        ),

        // CHAPTER 10 — ANCIENT KINGDOM (LEVELS 46–50)
        Level(
            id = 46,
            chapterId = 10,
            theme = "CASTLE",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("CASTLE", "KINGDOM", "TOWER", "PALACE", "CROWN", "KNIGHT", "GATE", "THRONE", "KING"),
            targetTimeSeconds = 160,
            rewardCoins = 90,
            rewardHints = 2
        ),
        Level(
            id = 47,
            chapterId = 10,
            theme = "KNIGHTS",
            difficulty = Difficulty.EXPERT,
            gridSize = 11,
            words = listOf("KNIGHT", "ARMOR", "SWORD", "SHIELD", "HORSE", "BANNER", "CASTLE", "GUARD", "BATTLE"),
            targetTimeSeconds = 160,
            rewardCoins = 90,
            rewardHints = 2
        ),
        Level(
            id = 48,
            chapterId = 10,
            theme = "LEGEND",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("LEGEND", "HERO", "COURAGE", "DESTINY", "POWER", "QUEST", "VICTORY", "HONOR", "ADVENTURE"),
            targetTimeSeconds = 170,
            rewardCoins = 100,
            rewardHints = 2
        ),
        Level(
            id = 49,
            chapterId = 10,
            theme = "FINAL MYSTERY",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("MYSTERY", "SECRET", "TREASURE", "ANCIENT", "KINGDOM", "CRYSTAL", "TEMPLE", "DISCOVERY", "DESTINY"),
            targetTimeSeconds = 170,
            rewardCoins = 100,
            rewardHints = 2
        ),
        Level(
            id = 50,
            chapterId = 10,
            theme = "THE FINAL DESTINATION",
            difficulty = Difficulty.EXPERT,
            gridSize = 12,
            words = listOf("ADVENTURE", "FRIENDSHIP", "COURAGE", "TREASURE", "DISCOVERY", "KINGDOM", "VICTORY", "JOURNEY", "DESTINATION"),
            targetTimeSeconds = 180,
            rewardCoins = 500,
            rewardHints = 5
        )
    )

    val dailyRewards = listOf(
        DailyRewardDay(day = 1, coins = 100, hints = 0),
        DailyRewardDay(day = 2, coins = 0, hints = 1),
        DailyRewardDay(day = 3, coins = 150, hints = 0),
        DailyRewardDay(day = 4, coins = 0, hints = 1),
        DailyRewardDay(day = 5, coins = 200, hints = 0),
        DailyRewardDay(day = 6, coins = 0, hints = 2),
        DailyRewardDay(day = 7, coins = 350, hints = 3, isSpecialChest = true)
    )

    val achievements = listOf(
        Achievement(
            id = "first_word",
            title = "First Word",
            description = "Find your very first hidden word.",
            iconType = "star",
            requirement = 1,
            category = "Badges"
        ),
        Achievement(
            id = "first_perfect",
            title = "Perfect Scout",
            description = "Earn 3 stars on any level without using hints.",
            iconType = "trophy",
            requirement = 1,
            category = "Badges"
        ),
        Achievement(
            id = "levels_10",
            title = "10 Levels",
            description = "Complete 10 journey levels.",
            iconType = "map",
            requirement = 10,
            category = "Badges"
        ),
        Achievement(
            id = "levels_20",
            title = "Trail Master",
            description = "Complete all 20 levels in the saga!",
            iconType = "crown",
            requirement = 20,
            category = "Badges"
        ),
        Achievement(
            id = "words_50",
            title = "50 Words",
            description = "Discover a total of 50 hidden words.",
            iconType = "search",
            requirement = 50,
            category = "Trophies"
        ),
        Achievement(
            id = "words_100",
            title = "100 Words",
            description = "Discover a total of 100 hidden words.",
            iconType = "book",
            requirement = 100,
            category = "Trophies"
        ),
        Achievement(
            id = "no_hints",
            title = "No Hints",
            description = "Clear 3 levels consecutively without hints.",
            iconType = "eye",
            requirement = 3,
            category = "Trophies"
        ),
        Achievement(
            id = "three_star_master",
            title = "Three Star Master",
            description = "Earn 3 stars on at least 15 levels.",
            iconType = "gold_star",
            requirement = 15,
            category = "Trophies"
        ),
        Achievement(
            id = "chapter_1_clear",
            title = "Meadow Pioneer",
            description = "Complete Chapter 1: Whispering Meadows.",
            iconType = "tree",
            requirement = 5,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_2_clear",
            title = "Forest Pathfinder",
            description = "Complete Chapter 2: Misty Woods.",
            iconType = "fog",
            requirement = 10,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_3_clear",
            title = "Crystal Seeker",
            description = "Complete Chapter 3: Crystal Valley.",
            iconType = "crystal",
            requirement = 15,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_4_clear",
            title = "Summit Conqueror",
            description = "Complete Chapter 4: Ancient Summit.",
            iconType = "mountain",
            requirement = 20,
            category = "Worlds"
        ),
        // CHAPTER 5–10 WORLD BADGES
        Achievement(
            id = "chapter_5_clear",
            title = "Sunset Coast",
            description = "Complete Chapter 5: Sunset Coast.",
            iconType = "beach",
            requirement = 25,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_6_clear",
            title = "Enchanted Forest",
            description = "Complete Chapter 6: Enchanted Forest.",
            iconType = "forest",
            requirement = 30,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_7_clear",
            title = "Frozen Peaks",
            description = "Complete Chapter 7: Frozen Peaks.",
            iconType = "snow",
            requirement = 35,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_8_clear",
            title = "Desert Ruins",
            description = "Complete Chapter 8: Desert Ruins.",
            iconType = "pyramid",
            requirement = 40,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_9_clear",
            title = "Sky Islands",
            description = "Complete Chapter 9: Sky Islands.",
            iconType = "cloud",
            requirement = 45,
            category = "Worlds"
        ),
        Achievement(
            id = "chapter_10_clear",
            title = "Ancient Kingdom",
            description = "Complete Chapter 10: Ancient Kingdom.",
            iconType = "castle",
            requirement = 50,
            category = "Worlds"
        ),
        // EXPANSION BADGES
        Achievement(
            id = "sunset_explorer",
            title = "Sunset Explorer",
            description = "Complete Level 25 at the coastal shore.",
            iconType = "sunset",
            requirement = 25,
            category = "Badges"
        ),
        Achievement(
            id = "forest_walker",
            title = "Forest Walker",
            description = "Complete Level 30 deep in the woods.",
            iconType = "forest",
            requirement = 30,
            category = "Badges"
        ),
        Achievement(
            id = "ice_climber",
            title = "Ice Climber",
            description = "Complete Level 35 atop the glaciers.",
            iconType = "mountain",
            requirement = 35,
            category = "Badges"
        ),
        Achievement(
            id = "temple_seeker",
            title = "Temple Seeker",
            description = "Complete Level 40 in the lost ruins.",
            iconType = "temple",
            requirement = 40,
            category = "Badges"
        ),
        Achievement(
            id = "sky_explorer",
            title = "Sky Explorer",
            description = "Complete Level 45 in the floating clouds.",
            iconType = "airship",
            requirement = 45,
            category = "Badges"
        ),
        Achievement(
            id = "kingdom_discoverer",
            title = "Kingdom Discoverer",
            description = "Complete Level 50 and discover the kingdom.",
            iconType = "crown",
            requirement = 50,
            category = "Badges"
        ),
        // EXPANSION TROPHIES
        Achievement(
            id = "fifty_wordquest",
            title = "Fifty WordQuest",
            description = "Complete all 50 levels of the great expedition.",
            iconType = "trophy",
            requirement = 50,
            category = "Trophies"
        ),
        Achievement(
            id = "perfect_journey",
            title = "Perfect Journey",
            description = "Earn 3 stars on Levels 21–50.",
            iconType = "gold_star",
            requirement = 30,
            category = "Trophies"
        ),
        Achievement(
            id = "no_hint_master",
            title = "No Hint Master",
            description = "Complete Levels 21–50 without using hints.",
            iconType = "eye",
            requirement = 30,
            category = "Trophies"
        )
    )

    fun getLevelById(id: Int): Level {
        if (id in 1..levels.size) {
            return levels[id - 1]
        }
        val safeId = id.coerceAtLeast(1)
        val chapterId = ((safeId - 1) / 5) + 1
        val themeData = proceduralThemes[(safeId - 1) % proceduralThemes.size]
        val difficulty = when {
            safeId <= 15 -> Difficulty.EASY
            safeId <= 40 -> Difficulty.MEDIUM
            safeId <= 80 -> Difficulty.HARD
            else -> Difficulty.EXPERT
        }
        val gridSize = when (difficulty) {
            Difficulty.EASY -> 8
            Difficulty.MEDIUM -> 9
            Difficulty.HARD -> 10
            Difficulty.EXPERT -> 11
        }
        val targetTime = 80 + (gridSize * 5)

        return Level(
            id = safeId,
            chapterId = chapterId,
            theme = themeData.first,
            difficulty = difficulty,
            gridSize = gridSize,
            words = themeData.second,
            targetTimeSeconds = targetTime,
            rewardCoins = 100,
            rewardHints = 1
        )
    }

    fun getLevelsForChunk(chunkIndex: Int): List<Level> {
        val startId = chunkIndex * 5 + 1
        return (startId until startId + 5).map { getLevelById(it) }
    }

    fun getChapterForLevel(levelId: Int): Chapter {
        val safeId = levelId.coerceAtLeast(1)
        val chapterId = ((safeId - 1) / 5) + 1
        if (chapterId <= chapters.size) {
            return chapters[chapterId - 1]
        }
        val startLevel = (chapterId - 1) * 5 + 1
        val endLevel = chapterId * 5
        val meta = chapterThemes[(chapterId - 1) % chapterThemes.size]
        return Chapter(
            id = chapterId,
            title = "Chapter $chapterId",
            subtitle = meta.second,
            quote = meta.third,
            startLevel = startLevel,
            endLevel = endLevel,
            terrainZone = meta.first
        )
    }

    private val chapterThemes = listOf(
        Triple("Meadows", "Whispering Meadows", "A gentle breeze carries the first whispers of your adventure."),
        Triple("Forest", "Misty Woods", "Mysterious paths and ancient trees await discovery."),
        Triple("Crystal", "Crystal Valley", "Luminous crystals light the trail across shining streams."),
        Triple("Summit", "Ancient Summit", "Climb the heights where the ultimate word treasure lies."),
        Triple("Desert", "Golden Oasis", "Golden dunes whispering ancient legends under a blazing sun."),
        Triple("Caverns", "Emerald Caverns", "Gleaming gemstones sparkle along subterranean paths."),
        Triple("Ocean", "Sunken Reefs", "Deep oceanic secrets buried beneath coral formations."),
        Triple("Tundra", "Mystic Aurora", "Dancing lights shimmer across the frozen wilderness."),
        Triple("Volcano", "Molten Peaks", "Fierce volcanic paths testing the bravest explorers."),
        Triple("Sky", "Celestial Sanctuary", "Float among clouds where celestial words resonate."),
        Triple("Jungle", "Forgotten Rainforest", "Thick emerald canopies hiding legendary temple ruins."),
        Triple("Citadel", "Dragon Citadel", "Ancient stone fortress overlooking boundless horizons."),
        Triple("Cosmos", "Starfall Frontier", "Venture into the cosmic expanse of infinite galaxies."),
        Triple("Islands", "Pirate Archipelago", "Hidden islands with chests of sparkling treasures."),
        Triple("Magic", "Enchanted Realm", "Arcane sorcery weaving secrets into every puzzle.")
    )

    private val proceduralThemes = listOf(
        "SPACE" to listOf("GALAXY", "PLANET", "COMET", "ORBIT", "ROCKET", "NEBULA", "COSMOS", "SOLAR", "ASTEROID"),
        "JUNGLE" to listOf("TIGER", "JAGUAR", "PARROT", "CANOPY", "LIANA", "MONKEY", "BAMBOO", "PYTHON", "TOUCAN"),
        "DESERT" to listOf("OASIS", "CAMEL", "DUNE", "PYRAMID", "MIRAGE", "CACTUS", "SAHARA", "CANYON", "SCORPION"),
        "ARCTIC" to listOf("PENGUIN", "ICEBERG", "POLAR", "BLIZZARD", "GLACIER", "IGLOO", "WALRUS", "AURORA", "TUNDRA"),
        "MAGIC" to listOf("WIZARD", "SPELL", "POTION", "WAND", "CRYSTAL", "MYSTIC", "SCROLL", "ENCHANT", "DRAGON"),
        "CASTLE" to listOf("KNIGHT", "THRONE", "CROWN", "SHIELD", "SWORD", "ARMOR", "DUNGEON", "KINGDOM", "BRIDGE"),
        "OCEAN DEPTHS" to listOf("DOLPHIN", "OCTOPUS", "CORAL", "SUBMARINE", "TRENCH", "JELLYFISH", "PEARL", "ANCHOR", "WHALE"),
        "PIRATES" to listOf("TREASURE", "GALLEON", "COMPASS", "ISLAND", "PARROT", "CANNON", "SILVER", "CUTLASS", "CHEST"),
        "VOLCANO" to listOf("MAGMA", "CRATER", "ASHES", "ERUPT", "LAVA", "OBSIDIAN", "BASALT", "PUMICE", "VENT"),
        "WEATHER" to listOf("RAINBOW", "THUNDER", "STORM", "CYCLONE", "BREEZE", "LIGHTNING", "MONSOON", "TORNADO", "CLOUD"),
        "GARDEN" to listOf("BLOSSOM", "PETAL", "BUTTERFLY", "TULIP", "ORCHID", "SUNFLOWER", "JASMINE", "LOTUS", "ROSE"),
        "MUSIC" to listOf("MELODY", "HARMONY", "RHYTHM", "GUITAR", "VIOLIN", "SYMPHONY", "CONCERT", "PIANO", "CHORUS"),
        "SPORTS" to listOf("SOCCER", "TENNIS", "RUNNER", "BASKET", "CRICKET", "TROPHY", "STADIUM", "CHAMPION", "ATHLETE"),
        "SAFARI" to listOf("ELEPHANT", "GIRAFFE", "ZEBRA", "LION", "CHEETAH", "RHINO", "SAVANNA", "GAZELLE", "HYENA"),
        "COOKING" to listOf("RECIPE", "FLAVOR", "SPICE", "BAKERY", "ROAST", "GOURMET", "PASTRY", "CHEF", "KITCHEN"),
        "HEROES" to listOf("COURAGE", "BRAVE", "LEGEND", "VALOR", "QUEST", "GLORY", "VICTORY", "HONOR", "TRIUMPH"),
        "MYTHOLOGY" to listOf("PHOENIX", "GRIFFIN", "PEGASUS", "TITAN", "HYDRA", "SPHINX", "CENTAUR", "CHIMERA", "KRAKEN"),
        "DETECTIVE" to listOf("MYSTERY", "CLUE", "CIPHER", "SECRET", "EVIDENCE", "SHADOW", "MAGNIFY", "SUSPECT", "SOLVE"),
        "GEMS" to listOf("DIAMOND", "RUBY", "EMERALD", "SAPPHIRE", "TOPAZ", "AMETHYST", "OPAL", "GARNET", "JADE"),
        "SKY" to listOf("CLOUDS", "EAGLE", "HORIZON", "BREEZE", "SUNSET", "GLIDER", "CELESTIAL", "ZENITH", "FEATHER"),
        "FARM" to listOf("HARVEST", "TRACTOR", "BARN", "MEADOW", "WHEAT", "ORCHARD", "CROPS", "PASTURE", "WINDMILL"),
        "ANCIENT" to listOf("TEMPLE", "RUINS", "RELIC", "PHARAOH", "TOMB", "MONUMENT", "SCULPTURE", "DYNASTY", "SCROLL"),
        "WINTER" to listOf("SNOWFLAKE", "SLEIGH", "MITTENS", "FROST", "ICICLE", "SKATING", "FIREPLACE", "SCARF", "CABIN"),
        "ISLAND" to listOf("LAGOON", "PALM", "SHORE", "REEF", "ATOLL", "TROPICAL", "COCONUT", "SAND", "BEACH"),
        "TIME" to listOf("FUTURE", "PAST", "CENTURY", "CLOCK", "HOURGLASS", "ERA", "MOMENT", "INFINITY", "CHRONICLE"),
        "ENERGY" to listOf("LIGHTNING", "SOLAR", "POWER", "VOLT", "CURRENT", "ATOM", "PULSE", "SPARK", "CIRCUIT"),
        "EXPLORER" to listOf("EXPEDITION", "VOYAGE", "TRAIL", "FRONTIER", "SUMMIT", "COMPASS", "DISCOVER", "PATHFIND", "MAP")
    )

    fun getDailyPuzzleLevel(): Level {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val dateString = dateFormat.format(Date())
        val dayIndex = (dateString.hashCode() and 0x7FFFFFFF) % dailyThemes.size
        val themeData = dailyThemes[dayIndex]
        return Level(
            id = 999,
            chapterId = 1,
            theme = themeData.first,
            difficulty = Difficulty.MEDIUM,
            gridSize = 9,
            words = themeData.second,
            targetTimeSeconds = 120,
            rewardCoins = 100,
            rewardHints = 1
        )
    }

    private val dailyThemes = listOf(
        "ASTRONOMY" to listOf("STAR", "PLANET", "GALAXY", "NEBULA", "COSMOS", "SOLAR", "MOON", "TELESCOPE", "ORBIT"),
        "JUNGLE" to listOf("TIGER", "JAGUAR", "PARROT", "LIANA", "CANOPY", "RIVER", "ORCHID", "MONKEY", "FERN"),
        "HARBOR" to listOf("SHIP", "ANCHOR", "LIGHTHOUSE", "SAIL", "DOCK", "WAVE", "BREEZE", "SEAGULL", "CAPTAIN"),
        "CAMPFIRE" to listOf("FLAME", "TENT", "STARS", "EMBER", "WOODS", "NIGHT", "LANTERN", "MARSHMALLOW", "STORY"),
        "TREASURE" to listOf("GOLD", "CHEST", "JEWEL", "PIRATE", "ISLAND", "MAP", "SILVER", "COMPASS", "SECRET")
    )
}
