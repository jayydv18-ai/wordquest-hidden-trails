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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameState
import com.example.ui.components.GameButton
import com.example.ui.components.GameButtonStyle
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.BoardBorder
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun SettingsScreen(
    gameState: GameState,
    onBackClick: () -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onResetProgress: () -> Unit
) {
    var showHowToPlay by remember { mutableStateOf(false) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdventureBlueDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("settings_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B3252))
                        .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .clickable(onClick = onBackClick)
                        .testTag("settings_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "SETTINGS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Audio & Feedback Card
            SettingsGroupCard(title = "AUDIO & CONTROLS") {
                SettingToggleRow(
                    icon = Icons.Default.VolumeUp,
                    title = "Sound Effects",
                    checked = gameState.soundEnabled,
                    onCheckedChange = onToggleSound
                )
                SettingToggleRow(
                    icon = Icons.Default.MusicNote,
                    title = "Music",
                    checked = gameState.musicEnabled,
                    onCheckedChange = onToggleMusic
                )
                SettingToggleRow(
                    icon = Icons.Default.Vibration,
                    title = "Haptic Vibration",
                    checked = gameState.vibrationEnabled,
                    onCheckedChange = onToggleVibration
                )
                SettingToggleRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    checked = gameState.notificationsEnabled,
                    onCheckedChange = onToggleNotifications
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // General Card
            SettingsGroupCard(title = "GENERAL") {
                SettingClickableRow(
                    icon = Icons.Default.Language,
                    title = "Language",
                    subtitle = "English",
                    onClick = {}
                )
                SettingClickableRow(
                    icon = Icons.Default.HelpOutline,
                    title = "How to Play",
                    onClick = { showHowToPlay = true }
                )
                SettingClickableRow(
                    icon = Icons.Default.Security,
                    title = "Privacy Policy",
                    onClick = { showPrivacyPolicy = true }
                )
                SettingClickableRow(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    onClick = { showTerms = true }
                )
                SettingClickableRow(
                    icon = Icons.Default.Info,
                    title = "About WordQuest",
                    onClick = { showAbout = true }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reset Progress Button
            GameButton(
                text = "RESTORE / RESET PROGRESS",
                onClick = { showResetConfirm = true },
                style = GameButtonStyle.ORANGE,
                height = 46.dp,
                fontSize = 14,
                testTag = "reset_progress_button"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Dialogs
        if (showHowToPlay) {
            InfoDialog(
                title = "HOW TO PLAY",
                content = "1. DRAG ACROSS CONNECTED LETTERS:\nTouch and drag across adjacent letters horizontally, vertically, or diagonally in any direction (forward or reverse) to form a word.\n\n2. DISCOVER ALL HIDDEN WORDS:\nFind every target word listed at the bottom to complete the level.\n\n3. EARN STARS & COINS:\nClear levels fast and without using hints to earn 3 stars and bonus coins!\n\n4. USE HINTS WISELY:\nTap the Hint button to reveal starting letters or entire word paths.",
                onDismiss = { showHowToPlay = false }
            )
        }

        if (showPrivacyPolicy) {
            InfoDialog(
                title = "PRIVACY POLICY",
                content = "WordQuest: Hidden Trails values your privacy. All game progression, stars, coins, and settings are stored locally on your device.\n\nNo personal identifying information is transmitted or sold to third parties. AdMob advertising services may use standard non-sensitive advertising identifiers in accordance with Google Play Developer policies.",
                onDismiss = { showPrivacyPolicy = false }
            )
        }

        if (showTerms) {
            InfoDialog(
                title = "TERMS OF SERVICE",
                content = "By playing WordQuest: Hidden Trails, you agree to enjoy the game for personal, non-commercial amusement. All levels, word lists, artwork, and music are property of JayLabs.\n\nAll 20 core adventure levels are completely playable offline without mandatory purchases.",
                onDismiss = { showTerms = false }
            )
        }

        if (showAbout) {
            InfoDialog(
                title = "ABOUT WORDQUEST",
                content = "WORDQUEST: HIDDEN TRAILS\nVersion 1.0\nPackage: com.jaylabs.wordquest.hiddentrails\n\nAn original casual word-search adventure journey crafted by JayLabs. Thank you for exploring the trails with Jay and his faithful adventure puppy companion!",
                onDismiss = { showAbout = false }
            )
        }

        if (showResetConfirm) {
            AlertDialog(
                onDismissRequest = { showResetConfirm = false },
                title = { Text("Reset All Progress?", fontWeight = FontWeight.Bold) },
                text = { Text("This will reset your completed levels, stars, coins, and achievements to default. Are you sure?") },
                confirmButton = {
                    GameButton(
                        text = "RESET",
                        onClick = {
                            showResetConfirm = false
                            onResetProgress()
                        },
                        style = GameButtonStyle.ORANGE,
                        height = 40.dp,
                        fontSize = 13
                    )
                },
                dismissButton = {
                    GameButton(
                        text = "CANCEL",
                        onClick = { showResetConfirm = false },
                        style = GameButtonStyle.WOOD,
                        height = 40.dp,
                        fontSize = 13
                    )
                },
                containerColor = ParchmentCream
            )
        }
    }
}

@Composable
fun SettingsGroupCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(ParchmentCream)
            .border(2.dp, BoardBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
fun SettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF0288D1),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = TextDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFB0BEC5)
            )
        )
    }
}

@Composable
fun SettingClickableRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF0288D1),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = TextDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun InfoDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(ParchmentCream)
                .border(2.5.dp, BoardBorder, RoundedCornerShape(24.dp))
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = content,
                color = TextDark,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            GameButton(
                text = "GOT IT",
                onClick = onDismiss,
                style = GameButtonStyle.GREEN,
                height = 46.dp,
                fontSize = 15
            )
        }
    }
}
