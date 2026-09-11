package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AdventureBlue
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.AdventureTurquoise
import com.example.ui.theme.BoardBorder
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmOrange

enum class GameButtonStyle {
    GREEN,
    BLUE,
    GOLD,
    ORANGE,
    WOOD,
    MINI_BLUE
}

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: GameButtonStyle = GameButtonStyle.GREEN,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    fontSize: Int = 18,
    testTag: String = "game_button"
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "btn_scale"
    )

    val (topColor, bottomColor, shadowColor, textColor) = when (style) {
        GameButtonStyle.GREEN -> Quadruple(
            Color(0xFF66BB6A),
            MeadowGreenDark,
            Color(0xFF1B5E20),
            Color.White
        )
        GameButtonStyle.BLUE -> Quadruple(
            Color(0xFF29B6F6),
            Color(0xFF0277BD),
            Color(0xFF01579B),
            Color.White
        )
        GameButtonStyle.GOLD -> Quadruple(
            GoldYellowLight,
            Color(0xFFF57F17),
            Color(0xFFE65100),
            Color(0xFF3E2723)
        )
        GameButtonStyle.ORANGE -> Quadruple(
            Color(0xFFFF9E80),
            WarmOrange,
            Color(0xFFBF360C),
            Color.White
        )
        GameButtonStyle.WOOD -> Quadruple(
            Color(0xFFA1887F),
            Color(0xFF5D4037),
            Color(0xFF3E2723),
            Color.White
        )
        GameButtonStyle.MINI_BLUE -> Quadruple(
            Color(0xFF4FC3F7),
            Color(0xFF0288D1),
            Color(0xFF01579B),
            Color.White
        )
    }

    val finalTopColor = if (enabled) topColor else Color(0xFFB0BEC5)
    val finalBottomColor = if (enabled) bottomColor else Color(0xFF78909C)
    val finalShadowColor = if (enabled) shadowColor else Color(0xFF546E7A)

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .height(height + 4.dp)
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                            onClick()
                        }
                    )
                }
            },
        contentAlignment = Alignment.TopCenter
    ) {
        // 3D Bottom Edge / Bevel Shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = 4.dp)
                .clip(RoundedCornerShape(height / 2))
                .background(finalShadowColor)
        )

        // Main Button Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = if (isPressed) 2.dp else 0.dp)
                .clip(RoundedCornerShape(height / 2))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(finalTopColor, finalBottomColor)
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(height / 2)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Subtle top highlight sheen
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(height * 0.45f)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(topStart = height / 2, topEnd = height / 2))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.White.copy(alpha = 0.30f), Color.Transparent)
                        )
                    )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = textColor,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun TopStatsBar(
    playerName: String,
    playerLevel: Int,
    coins: Int,
    hints: Int,
    onAddCoinsClick: () -> Unit = {},
    onAddHintsClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player Avatar Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AdventureBlueDark.copy(alpha = 0.85f))
                .border(1.5.dp, Color(0xFF64B5F6).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .clickable(onClick = onAvatarClick)
                .padding(end = 12.dp, top = 3.dp, bottom = 3.dp, start = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFFFFB74D), Color(0xFFE65100))))
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🧭",
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = playerName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lv. $playerLevel",
                    color = GoldYellowLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Coins and Hints Chips
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Coin Chip (Click + to earn free coins by watching rewarded video ad)
            ResourcePill(
                icon = "🪙",
                amount = coins.toString(),
                pillBg = Color(0xFF1B2838),
                accentColor = GoldYellow,
                onAddClick = onAddCoinsClick
            )

            // Hint Chip (Click + to buy 1 hint for 25 coins)
            ResourcePill(
                icon = "💡",
                amount = hints.toString(),
                pillBg = Color(0xFF1B2838),
                accentColor = Color(0xFF00E5FF),
                onAddClick = onAddHintsClick
            )
        }
    }
}

@Composable
fun ResourcePill(
    icon: String,
    amount: String,
    pillBg: Color,
    accentColor: Color,
    onAddClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(pillBg.copy(alpha = 0.88f))
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = icon, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = amount,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp
        )
        if (onAddClick != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(accentColor)
                    .clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Buy",
                    tint = Color.Black,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun StarRatingDisplay(
    stars: Int,
    modifier: Modifier = Modifier,
    starSize: Dp = 32.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..3) {
            val isFilled = i <= stars
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $i",
                tint = if (isFilled) GoldYellow else Color(0xFFB0BEC5),
                modifier = Modifier
                    .size(starSize)
                    .padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun ParchmentCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(ParchmentCream)
            .border(2.5.dp, BoardBorder, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        content()
    }
}
