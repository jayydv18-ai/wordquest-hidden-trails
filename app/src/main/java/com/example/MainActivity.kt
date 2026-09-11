package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.UnityAdsManager
import com.example.data.GamePreferences
import com.example.data.LevelRepository
import com.example.game.SoundManager
import com.example.model.Chapter
import com.example.model.Level
import com.example.ui.components.GameButton
import com.example.ui.components.GameButtonStyle
import com.example.ui.screens.ChapterIntroDialog
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.DailyPuzzleScreen
import com.example.ui.screens.DailyRewardDialog
import com.example.ui.screens.GameplayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelCompleteDialog
import com.example.ui.screens.LevelIntroDialog
import com.example.ui.screens.MapScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.BoardBorder
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WordQuestTheme

enum class ScreenState {
    SPLASH,
    HOME,
    MAP,
    GAMEPLAY,
    DAILY_PUZZLE,
    COLLECTION,
    SETTINGS
}

class MainActivity : ComponentActivity() {

    private lateinit var gamePrefs: GamePreferences
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Unity Ads SDK with Game ID: 6189204
        UnityAdsManager.initialize(this, testMode = false)

        gamePrefs = GamePreferences(this)
        soundManager = SoundManager(this).apply {
            soundEnabled = gamePrefs.state.value.soundEnabled
            musicEnabled = gamePrefs.state.value.musicEnabled
            vibrationEnabled = gamePrefs.state.value.vibrationEnabled
        }

        setContent {
            WordQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F284E)
                ) {
                    WordQuestApp(
                        activity = this,
                        gamePrefs = gamePrefs,
                        soundManager = soundManager
                    )
                }
            }
        }
    }
}

@Composable
fun WordQuestApp(
    activity: Activity,
    gamePrefs: GamePreferences,
    soundManager: SoundManager
) {
    val gameState by gamePrefs.state.collectAsState()

    var currentScreen by remember { mutableStateOf(ScreenState.SPLASH) }
    var activeLevel by remember {
        mutableStateOf(LevelRepository.getLevelById(gameState.unlockedLevel) ?: LevelRepository.levels.first())
    }
    var showLevelIntro by remember { mutableStateOf(false) }
    var showChapterIntro by remember { mutableStateOf<Chapter?>(null) }
    var showDailyReward by remember { mutableStateOf(false) }
    var showRewardAdDialog by remember { mutableStateOf(false) }

    // Level Complete Dialog state
    var completedLevelData by remember {
        mutableStateOf<Triple<Level, Int, Pair<Int, Int>>?>(null) // Level, stars, (time, words)
    }

    // System Back Button Handling
    BackHandler(enabled = currentScreen != ScreenState.HOME && currentScreen != ScreenState.SPLASH) {
        soundManager.playTap()
        when {
            showRewardAdDialog -> showRewardAdDialog = false
            showDailyReward -> showDailyReward = false
            showChapterIntro != null -> showChapterIntro = null
            showLevelIntro -> showLevelIntro = false
            completedLevelData != null -> {
                completedLevelData = null
                currentScreen = ScreenState.MAP
            }
            currentScreen == ScreenState.GAMEPLAY -> currentScreen = ScreenState.MAP
            else -> currentScreen = ScreenState.HOME
        }
    }

    when (currentScreen) {
        ScreenState.SPLASH -> {
            SplashScreen(
                onSplashFinished = {
                    currentScreen = ScreenState.HOME
                }
            )
        }

        ScreenState.HOME -> {
            HomeScreen(
                gameState = gameState,
                onPlayClick = {
                    soundManager.playTap()
                    val targetLevel = LevelRepository.getLevelById(gameState.unlockedLevel) ?: LevelRepository.levels.first()
                    activeLevel = targetLevel
                    showLevelIntro = true
                    currentScreen = ScreenState.MAP
                },
                onMapClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.MAP
                },
                onDailyPuzzleClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.DAILY_PUZZLE
                },
                onCollectionClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.COLLECTION
                },
                onDailyRewardClick = {
                    soundManager.playTap()
                    showDailyReward = true
                },
                onHowToPlayClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.SETTINGS
                },
                onSettingsClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.SETTINGS
                },
                onToggleSound = {
                    val newVal = !gameState.soundEnabled
                    gamePrefs.toggleSound(newVal)
                    soundManager.soundEnabled = newVal
                    if (newVal) soundManager.playTap()
                },
                onToggleMusic = {
                    val newVal = !gameState.musicEnabled
                    gamePrefs.toggleMusic(newVal)
                    soundManager.musicEnabled = newVal
                    soundManager.playTap()
                },
                onAddCoinsClick = {
                    soundManager.playTap()
                    showRewardAdDialog = true
                },
                onAddHintsClick = {
                    soundManager.playTap()
                    showRewardAdDialog = true
                }
            )
        }

        ScreenState.MAP -> {
            MapScreen(
                gameState = gameState,
                onBackClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.HOME
                },
                onLevelSelected = { level ->
                    soundManager.playTap()
                    activeLevel = level
                    showLevelIntro = true
                },
                onChapterIntroClick = { chapter ->
                    soundManager.playTap()
                    showChapterIntro = chapter
                }
            )

            // Overlaid Level Intro Dialog
            if (showLevelIntro) {
                LevelIntroDialog(
                    level = activeLevel,
                    onStartPuzzle = {
                        soundManager.playTap()
                        showLevelIntro = false
                        currentScreen = ScreenState.GAMEPLAY
                    },
                    onDismiss = {
                        showLevelIntro = false
                    }
                )
            }

            // Overlaid Chapter Intro Dialog
            if (showChapterIntro != null) {
                ChapterIntroDialog(
                    chapter = showChapterIntro!!,
                    onEnterChapter = {
                        soundManager.playTap()
                        showChapterIntro = null
                    },
                    onDismiss = {
                        showChapterIntro = null
                    }
                )
            }
        }

        ScreenState.GAMEPLAY -> {
            GameplayScreen(
                level = activeLevel,
                gameState = gameState,
                soundManager = soundManager,
                onBackToMap = {
                    soundManager.playTap()
                    currentScreen = ScreenState.MAP
                },
                onLevelCompleted = { stars, timeSeconds, wordsFound ->
                    gamePrefs.recordLevelCompletion(
                        levelId = activeLevel.id,
                        stars = stars,
                        timeSeconds = timeSeconds,
                        wordsFoundInLevel = wordsFound
                    )
                    val coinsToAward = if (activeLevel.id == 50) 500 else maxOf(100, activeLevel.rewardCoins)
                    val hintsToAward = if (activeLevel.id == 50) 5 else activeLevel.rewardHints
                    gamePrefs.addCoins(coinsToAward)
                    gamePrefs.addHints(hintsToAward)
                    completedLevelData = Triple(activeLevel, stars, Pair(timeSeconds, wordsFound))
                },
                onUseHintToken = {
                    gamePrefs.useHint()
                },
                onBuyHintWithCoins = {
                    if (gamePrefs.spendCoins(25)) {
                        gamePrefs.addHints(1)
                        soundManager.playCoin()
                        true
                    } else {
                        soundManager.playError()
                        false
                    }
                },
                onWatchAdForHint = {
                    UnityAdsManager.showRewarded(activity, onRewardEarned = {
                        gamePrefs.addHints(1)
                        soundManager.playCoin()
                    })
                },
                onTutorialFinished = {
                    gamePrefs.setTutorialCompleted()
                }
            )

            // Overlaid Level Complete Dialog
            if (completedLevelData != null) {
                val (lvl, stars, stats) = completedLevelData!!
                val totalStars = gameState.levelStars.values.sum()
                LevelCompleteDialog(
                    level = lvl,
                    earnedStars = stars,
                    timeSeconds = stats.first,
                    wordsFound = stats.second,
                    totalPlayerStars = totalStars,
                    onWatchAdForBonusCoins = {
                        UnityAdsManager.showRewarded(activity, onRewardEarned = {
                            gamePrefs.addCoins(50)
                            soundManager.playCoin()
                        })
                    },
                    onNextLevel = {
                        soundManager.playTap()
                        completedLevelData = null
                        val nextId = lvl.id + 1
                        val nextLevel = LevelRepository.getLevelById(nextId) ?: LevelRepository.levels.first()
                        UnityAdsManager.onLevelFinished(activity, levelId = lvl.id) {
                            activeLevel = nextLevel
                            showLevelIntro = true
                            currentScreen = ScreenState.MAP
                        }
                    },
                    onMap = {
                        soundManager.playTap()
                        completedLevelData = null
                        UnityAdsManager.onLevelFinished(activity, levelId = lvl.id) {
                            currentScreen = ScreenState.MAP
                        }
                    },
                    onHome = {
                        soundManager.playTap()
                        completedLevelData = null
                        UnityAdsManager.onLevelFinished(activity, levelId = lvl.id) {
                            currentScreen = ScreenState.HOME
                        }
                    },
                    onViewCollection = {
                        soundManager.playTap()
                        completedLevelData = null
                        UnityAdsManager.onLevelFinished(activity, levelId = lvl.id) {
                            currentScreen = ScreenState.COLLECTION
                        }
                    }
                )
            }
        }

        ScreenState.DAILY_PUZZLE -> {
            DailyPuzzleScreen(
                gameState = gameState,
                soundManager = soundManager,
                onBackClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.HOME
                },
                onPuzzleCompleted = {
                    soundManager.playCoin()
                    gamePrefs.addCoins(50)
                    gamePrefs.addHints(1)
                    currentScreen = ScreenState.HOME
                },
                onUseHintToken = {
                    gamePrefs.useHint()
                }
            )
        }

        ScreenState.COLLECTION -> {
            CollectionScreen(
                gameState = gameState,
                onBackClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.HOME
                }
            )
        }

        ScreenState.SETTINGS -> {
            SettingsScreen(
                gameState = gameState,
                onBackClick = {
                    soundManager.playTap()
                    currentScreen = ScreenState.HOME
                },
                onToggleSound = {
                    gamePrefs.toggleSound(it)
                    soundManager.soundEnabled = it
                },
                onToggleMusic = {
                    gamePrefs.toggleMusic(it)
                    soundManager.musicEnabled = it
                },
                onToggleVibration = {
                    gamePrefs.toggleVibration(it)
                    soundManager.vibrationEnabled = it
                },
                onToggleNotifications = {
                    gamePrefs.toggleNotifications(it)
                },
                onResetProgress = {
                    gamePrefs.resetProgress()
                    currentScreen = ScreenState.HOME
                }
            )
        }
    }

    // Global Daily Reward Dialog Modal
    if (showDailyReward) {
        DailyRewardDialog(
            gameState = gameState,
            canClaim = gamePrefs.canClaimDailyReward(),
            onClaimReward = { day, coins, hints ->
                soundManager.playCoin()
                gamePrefs.claimDailyReward(day, coins, hints)
                showDailyReward = false
            },
            onDismiss = {
                showDailyReward = false
            }
        )
    }

    // Global Rewarded Ad Choice Dialog Modal
    if (showRewardAdDialog) {
        RewardAdChoiceDialog(
            coins = gameState.coins,
            hints = gameState.hints,
            onWatchAdForCoins = {
                showRewardAdDialog = false
                UnityAdsManager.showRewarded(
                    activity = activity,
                    onRewardEarned = {
                        gamePrefs.addCoins(50)
                        soundManager.playCoin()
                    }
                )
            },
            onWatchAdForHint = {
                showRewardAdDialog = false
                UnityAdsManager.showRewarded(
                    activity = activity,
                    onRewardEarned = {
                        gamePrefs.addHints(1)
                        soundManager.playCoin()
                    }
                )
            },
            onBuyHintWithCoins = {
                if (gamePrefs.spendCoins(25)) {
                    gamePrefs.addHints(1)
                    soundManager.playCoin()
                } else {
                    soundManager.playError()
                }
            },
            onDismiss = {
                showRewardAdDialog = false
            }
        )
    }
}

@Composable
fun RewardAdChoiceDialog(
    coins: Int,
    hints: Int,
    onWatchAdForCoins: () -> Unit,
    onWatchAdForHint: () -> Unit,
    onBuyHintWithCoins: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(ParchmentCream)
                .border(3.dp, BoardBorder, RoundedCornerShape(24.dp))
                .clickable(enabled = false) {}
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "REWARD VAULT 🎁",
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Wallet: $coins 🪙  |  Hints: $hints 💡",
                color = MeadowGreenDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Option 1: Free Coins (Watch Ad)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFF8E1))
                    .border(1.5.dp, GoldYellow, RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "+50 Free Coins 🪙",
                        color = TextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Watch a quick video ad",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                GameButton(
                    text = "WATCH 🎬",
                    onClick = onWatchAdForCoins,
                    style = GameButtonStyle.GOLD,
                    height = 36.dp,
                    fontSize = 11,
                    testTag = "vault_watch_ad_coins"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Option 2: Free Hint (Watch Ad)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9))
                    .border(1.5.dp, MeadowGreenDark, RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "+1 Free Hint 💡",
                        color = TextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Watch a quick video ad",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                GameButton(
                    text = "WATCH 🎬",
                    onClick = onWatchAdForHint,
                    style = GameButtonStyle.GREEN,
                    height = 36.dp,
                    fontSize = 11,
                    testTag = "vault_watch_ad_hint"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Option 3: Buy Hint for 25 Coins
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE3F2FD))
                    .border(1.5.dp, Color(0xFF42A5F5), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Buy 1 Hint (💡)",
                        color = TextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Costs 25 coins from wallet",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                GameButton(
                    text = "25 🪙",
                    onClick = onBuyHintWithCoins,
                    style = GameButtonStyle.BLUE,
                    height = 36.dp,
                    fontSize = 11,
                    enabled = coins >= 25,
                    testTag = "vault_buy_hint_coins"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            GameButton(
                text = "CLOSE",
                onClick = onDismiss,
                style = GameButtonStyle.WOOD,
                height = 40.dp,
                fontSize = 13,
                modifier = Modifier.fillMaxWidth(),
                testTag = "vault_close_button"
            )
        }
    }
}
