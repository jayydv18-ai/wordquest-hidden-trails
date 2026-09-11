package com.example.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameState
import com.example.data.LevelRepository
import com.example.game.SoundManager
import com.example.model.DailyRewardDay
import com.example.ui.components.GameButton
import com.example.ui.components.GameButtonStyle
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.BoardBorder
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun DailyRewardDialog(
    gameState: GameState,
    canClaim: Boolean,
    onClaimReward: (day: Int, coins: Int, hints: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentDayCycle = if (gameState.dailyRewardClaimedDay >= 7) 1 else gameState.dailyRewardClaimedDay + (if (canClaim) 1 else 0)
    val todayReward = LevelRepository.dailyRewards.find { it.day == currentDayCycle } ?: LevelRepository.dailyRewards.first()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDD000000))
            .clickable(onClick = onDismiss)
            .testTag("daily_reward_dialog"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {}
                .shadow(24.dp, RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF153B64), AdventureBlueDark)
                    )
                )
                .border(2.5.dp, GoldYellow, RoundedCornerShape(26.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY REWARD",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6 days grid (Days 1 to 6)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(LevelRepository.dailyRewards.take(6)) { reward ->
                    val isClaimed = reward.day < currentDayCycle || (reward.day == currentDayCycle && !canClaim)
                    val isToday = reward.day == currentDayCycle && canClaim

                    Box(
                        modifier = Modifier
                            .height(82.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (isToday) Modifier.background(Brush.verticalGradient(listOf(Color(0xFF0288D1), Color(0xFF01579B))))
                                else Modifier.background(Color(0xFF1E3A5F))
                            )
                            .border(
                                width = if (isToday) 2.dp else 1.dp,
                                color = if (isToday) GoldYellow else Color(0xFF375A7F),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Day ${reward.day}",
                                color = if (isToday) GoldYellowLight else Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = if (reward.coins > 0) "🪙" else "💡",
                                fontSize = 20.sp
                            )
                            Text(
                                text = if (reward.coins > 0) "${reward.coins}" else "${reward.hints}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Claimed Checkmark Badge
                        if (isClaimed) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x88000000)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Claimed",
                                    tint = MeadowGreenDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Day 7 Special Chest Card
            val isDay7Today = currentDayCycle == 7 && canClaim
            val isDay7Claimed = currentDayCycle == 7 && !canClaim
            val day7Reward = LevelRepository.dailyRewards[6]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00695C), Color(0xFF00897B), Color(0xFF00695C))
                        )
                    )
                    .border(2.dp, GoldYellow, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "🎁", fontSize = 34.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DAY 7 - SPECIAL REWARD",
                            color = GoldYellowLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "🪙 ${day7Reward.coins} Coins  +  💡 ${day7Reward.hints} Hints",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Claim Button
            GameButton(
                text = if (canClaim) "CLAIM" else "CLAIMED TODAY",
                enabled = canClaim,
                onClick = {
                    onClaimReward(todayReward.day, todayReward.coins, todayReward.hints)
                },
                style = if (canClaim) GameButtonStyle.GREEN else GameButtonStyle.WOOD,
                height = 52.dp,
                fontSize = 18,
                testTag = "claim_reward_button"
            )
        }
    }
}

@Composable
fun DailyPuzzleScreen(
    gameState: GameState,
    soundManager: SoundManager,
    onBackClick: () -> Unit,
    onPuzzleCompleted: () -> Unit,
    onUseHintToken: () -> Boolean
) {
    val dailyLevel = remember { LevelRepository.getDailyPuzzleLevel() }
    var isCompleted by remember { mutableStateOf(false) }

    if (isCompleted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AdventureBlueDark)
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .shadow(16.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(ParchmentCream)
                    .border(3.dp, BoardBorder, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DAILY PUZZLE COMPLETE!",
                    color = TextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Streak: ${gameState.dailyStreak} Days 🔥",
                    color = GoldYellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                GameButton(
                    text = "CLAIM REWARD (+50 🪙)",
                    onClick = onPuzzleCompleted,
                    style = GameButtonStyle.GREEN,
                    height = 50.dp,
                    fontSize = 16
                )
            }
        }
    } else {
        GameplayScreen(
            level = dailyLevel,
            gameState = gameState,
            soundManager = soundManager,
            onBackToMap = onBackClick,
            onLevelCompleted = { _, _, _ ->
                isCompleted = true
            },
            onUseHintToken = onUseHintToken,
            onTutorialFinished = {}
        )
    }
}
