package com.example.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GameState
import com.example.ui.components.GameButton
import com.example.ui.components.GameButtonStyle
import com.example.ui.components.TopStatsBar
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GoldYellowLight

@Composable
fun HomeScreen(
    gameState: GameState,
    onPlayClick: () -> Unit,
    onMapClick: () -> Unit,
    onDailyPuzzleClick: () -> Unit,
    onCollectionClick: () -> Unit,
    onDailyRewardClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit,
    onAddCoinsClick: () -> Unit,
    onAddHintsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Landscape Background
        Image(
            painter = painterResource(id = R.drawable.img_splash_hero),
            contentDescription = "Home background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Vignette Gradients for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AdventureBlueDark.copy(alpha = 0.65f),
                            Color.Transparent,
                            AdventureBlueDark.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Stats Bar
            TopStatsBar(
                playerName = gameState.playerName,
                playerLevel = gameState.playerLevel,
                coins = gameState.coins,
                hints = gameState.hints,
                onAddCoinsClick = onAddCoinsClick,
                onAddHintsClick = onAddHintsClick,
                onAvatarClick = onDailyRewardClick
            )

            // Center Section: Logo & Mascot Companion
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Compass Logo
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(GoldYellowLight, GoldYellow, Color(0xFFE65100))
                            )
                        )
                        .border(2.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🧭", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // WORDQUEST Title
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "WORDQUEST",
                        color = Color(0xFF1B2838),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.offset(y = 3.dp)
                    )
                    Text(
                        text = "WORDQUEST",
                        color = GoldYellowLight,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                }

                // Subtitle: HIDDEN TRAILS
                Box(
                    modifier = Modifier
                        .offset(y = (-4).dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF4E342E), Color(0xFF795548), Color(0xFF4E342E))
                            )
                        )
                        .border(1.2.dp, GoldYellow, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "HIDDEN TRAILS",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Explorer Mascot & Puppy Companion Card
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF64B5F6).copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                        .border(3.dp, GoldYellowLight.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_companion),
                        contentDescription = "Jay & Puppy Adventure Mascot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Bottom Buttons Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Primary PLAY button (large, juicy green 3D button)
                GameButton(
                    text = if (gameState.unlockedLevel > 1) "CONTINUE" else "PLAY",
                    onClick = onPlayClick,
                    style = GameButtonStyle.GREEN,
                    icon = Icons.Default.PlayArrow,
                    height = 60.dp,
                    fontSize = 22,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    testTag = "home_play_button"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary Navigation Buttons (MAP, DAILY PUZZLE, COLLECTION)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SecondaryFeatureButton(
                        label = "MAP",
                        icon = Icons.Default.Map,
                        color = Color(0xFF0288D1),
                        onClick = onMapClick,
                        testTag = "home_map_button"
                    )
                    SecondaryFeatureButton(
                        label = "DAILY PUZZLE",
                        icon = Icons.Default.Today,
                        color = Color(0xFF00897B),
                        onClick = onDailyPuzzleClick,
                        testTag = "home_daily_puzzle_button"
                    )
                    SecondaryFeatureButton(
                        label = "COLLECTION",
                        icon = Icons.Default.EmojiEvents,
                        color = Color(0xFFF57C00),
                        onClick = onCollectionClick,
                        testTag = "home_collection_button"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Utility Buttons Row (Sound, Music, How to Play, Settings)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UtilityIconButton(
                        icon = if (gameState.soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        label = "Sound",
                        active = gameState.soundEnabled,
                        onClick = onToggleSound,
                        testTag = "home_sound_toggle"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    UtilityIconButton(
                        icon = if (gameState.musicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        label = "Music",
                        active = gameState.musicEnabled,
                        onClick = onToggleMusic,
                        testTag = "home_music_toggle"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    UtilityIconButton(
                        icon = Icons.Default.HelpOutline,
                        label = "How to Play",
                        active = true,
                        onClick = onHowToPlayClick,
                        testTag = "home_how_to_play"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    UtilityIconButton(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        active = true,
                        onClick = onSettingsClick,
                        testTag = "home_settings"
                    )
                }
            }
        }
    }
}

@Composable
fun SecondaryFeatureButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(color.copy(alpha = 0.9f), color)
                    )
                )
                .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun UtilityIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF102A45).copy(alpha = 0.85f))
                .border(
                    1.dp,
                    if (active) Color(0xFF64B5F6).copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.4f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (active) Color.White else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (active) Color.White.copy(alpha = 0.85f) else Color.Gray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
