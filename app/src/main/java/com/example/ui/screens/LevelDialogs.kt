package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Chapter
import com.example.model.Level
import com.example.ui.components.ConfettiOverlay
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
import com.example.ui.theme.WarmOrange

fun getChapterArtworkResId(chapterId: Int): Int {
    return when (chapterId) {
        5 -> R.drawable.img_sunset_coast
        6 -> R.drawable.img_enchanted_forest
        7 -> R.drawable.img_frozen_peaks
        8 -> R.drawable.img_desert_ruins
        9 -> R.drawable.img_sky_islands
        10 -> R.drawable.img_ancient_kingdom
        3 -> R.drawable.img_ocean_preview
        else -> R.drawable.img_splash_hero
    }
}

@Composable
fun LevelIntroDialog(
    level: Level,
    onStartPuzzle: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable(onClick = onDismiss)
            .testTag("level_intro_dialog"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .clickable(enabled = false) {}
                .shadow(16.dp, RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
                .background(ParchmentCream)
                .border(3.dp, BoardBorder, RoundedCornerShape(26.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Level Title
            Text(
                text = "LEVEL ${level.id.toString().padStart(2, '0')}",
                color = TextMuted,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = level.theme,
                color = TextDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Level Artwork Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, BoardBorder, RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(
                        id = getChapterArtworkResId(level.chapterId)
                    ),
                    contentDescription = "Level preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Difficulty Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(level.difficulty.badgeColorHex))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = level.difficulty.label.uppercase(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Find ${level.words.size} hidden words.",
                color = TextDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Rewards Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ParchmentWhite)
                    .border(1.5.dp, Color(0xFFE0D7C6), RoundedCornerShape(14.dp))
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Rewards: ",
                    color = TextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "🪙", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "+${level.rewardCoins}",
                    color = GoldYellow,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "💡", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "+${level.rewardHints}",
                    color = Color(0xFF0288D1),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Start Puzzle Button
            GameButton(
                text = "START PUZZLE",
                onClick = onStartPuzzle,
                style = GameButtonStyle.GREEN,
                icon = Icons.Default.PlayArrow,
                height = 54.dp,
                fontSize = 18,
                testTag = "start_puzzle_button"
            )
        }
    }
}

@Composable
fun LevelCompleteDialog(
    level: Level,
    earnedStars: Int,
    timeSeconds: Int,
    wordsFound: Int,
    totalPlayerStars: Int = 0,
    onNextLevel: () -> Unit,
    onMap: () -> Unit,
    onHome: () -> Unit,
    onViewCollection: () -> Unit = onMap,
    onWatchAdForBonusCoins: (() -> Unit)? = null
) {
    val starScale = remember { Animatable(0.2f) }
    var hasClaimedBonus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        starScale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
    }

    val isGrandFinale = level.id == 50

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDD000000))
            .testTag("level_complete_dialog"),
        contentAlignment = Alignment.Center
    ) {
        // Falling celebration confetti
        ConfettiOverlay()

        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .shadow(24.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(ParchmentCream)
                .border(3.dp, if (isGrandFinale) GoldYellow else BoardBorder, RoundedCornerShape(28.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            if (isGrandFinale)
                                listOf(Color(0xFFFFD54F), Color(0xFFFF8F00), Color(0xFFFF6F00))
                            else
                                listOf(Color(0xFFFFB300), Color(0xFFFFA000))
                        )
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isGrandFinale) "JOURNEY COMPLETE!" else "LEVEL COMPLETE!",
                    color = Color.White,
                    fontSize = if (isGrandFinale) 19.sp else 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            if (isGrandFinale) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "THE ANCIENT KINGDOM HAS BEEN DISCOVERED",
                    color = WarmOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated 3 Stars
            Row(
                modifier = Modifier.scale(starScale.value),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 1..3) {
                    val isEarned = i <= earnedStars
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star $i",
                        tint = if (isEarned) GoldYellow else Color(0xFFCFD8DC),
                        modifier = Modifier
                            .size(if (isGrandFinale) 48.dp else 44.dp)
                            .padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isGrandFinale) {
                // Grand Castle Artwork
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, GoldYellow, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_ancient_kingdom),
                        contentDescription = "The Ancient Kingdom",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                // Companion character celebration mascot
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9))
                        .border(2.dp, MeadowGreenDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_companion),
                        contentDescription = "Celebrating Mascot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stats summary card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ParchmentWhite)
                    .border(1.5.dp, Color(0xFFE0D7C6), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                StatRow("Words Found", "$wordsFound / ${level.words.size}")
                val minutes = timeSeconds / 60
                val seconds = timeSeconds % 60
                StatRow("Time", String.format("%02d:%02d", minutes, seconds))
                if (isGrandFinale) {
                    StatRow("Total Levels", "50 / 50")
                    if (totalPlayerStars > 0) {
                        StatRow("Total Stars", "$totalPlayerStars ⭐")
                    }
                    StatRow("Grand Reward", "🪙 +500 Coins  💡 +5 Hints")
                    StatRow("Badges Won", "👑 Kingdom & 🏆 Fifty WordQuest")
                } else {
                    StatRow("Reward", "🪙 +${level.rewardCoins}  💡 +${level.rewardHints}")
                }
            }

            if (isGrandFinale) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✨ MORE TRAILS AWAIT... Adventure continues! ✨",
                    color = MeadowGreenDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            GameButton(
                text = if (isGrandFinale) "CONTINUE ADVENTURE" else "NEXT LEVEL",
                onClick = onNextLevel,
                style = GameButtonStyle.GREEN,
                height = 50.dp,
                fontSize = 17,
                testTag = "next_level_button"
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (onWatchAdForBonusCoins != null && !hasClaimedBonus) {
                GameButton(
                    text = "CLAIM +50 BONUS COINS 🎬",
                    onClick = {
                        hasClaimedBonus = true
                        onWatchAdForBonusCoins()
                    },
                    style = GameButtonStyle.GOLD,
                    height = 44.dp,
                    fontSize = 13,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "bonus_coins_ad_button"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isGrandFinale) {
                    GameButton(
                        text = "COLLECTION",
                        onClick = onViewCollection,
                        icon = Icons.Default.Star,
                        style = GameButtonStyle.BLUE,
                        height = 44.dp,
                        fontSize = 14,
                        modifier = Modifier.weight(1f),
                        testTag = "complete_collection_button"
                    )
                } else {
                    GameButton(
                        text = "MAP",
                        onClick = onMap,
                        icon = Icons.Default.Map,
                        style = GameButtonStyle.BLUE,
                        height = 44.dp,
                        fontSize = 14,
                        modifier = Modifier.weight(1f),
                        testTag = "complete_map_button"
                    )
                }
                GameButton(
                    text = "HOME",
                    onClick = onHome,
                    icon = Icons.Default.Home,
                    style = GameButtonStyle.WOOD,
                    height = 44.dp,
                    fontSize = 14,
                    modifier = Modifier.weight(1f),
                    testTag = "complete_home_button"
                )
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = TextDark, fontSize = 13.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun ChapterIntroDialog(
    chapter: Chapter,
    onEnterChapter: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDD000000))
            .clickable(onClick = onDismiss)
            .testTag("chapter_intro_dialog"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .clickable(enabled = false) {}
                .shadow(20.dp, RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1E3C72), AdventureBlueDark)
                    )
                )
                .border(2.5.dp, GoldYellow, RoundedCornerShape(26.dp))
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = chapter.title.uppercase(),
                color = GoldYellowLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chapter.subtitle,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Chapter artwork landscape
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, GoldYellowLight.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = getChapterArtworkResId(chapter.id)),
                    contentDescription = "Chapter scenery",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "\"${chapter.quote}\"",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            GameButton(
                text = "ENTER",
                onClick = onEnterChapter,
                style = GameButtonStyle.GREEN,
                height = 52.dp,
                fontSize = 18,
                testTag = "chapter_enter_button"
            )
        }
    }
}
