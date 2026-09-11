package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.AdventureTurquoise
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GoldYellowLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val logoScale = remember { Animatable(0.7f) }
    val logoAlpha = remember { Animatable(0f) }
    val compassRotation = remember { Animatable(0f) }
    val loadingProgress = remember { Animatable(0.05f) }
    val letterFloat = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Smooth entrance animations
        logoAlpha.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        logoScale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        compassRotation.animateTo(360f, animationSpec = tween(1200, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(Unit) {
        loadingProgress.animateTo(1f, animationSpec = tween(1600, easing = LinearEasing))
        delay(150)
        onSplashFinished()
    }

    LaunchedEffect(Unit) {
        letterFloat.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen")
    ) {
        // Hero Background Image with Gradient Overlay
        Image(
            painter = painterResource(id = R.drawable.img_splash_hero),
            contentDescription = "Splash Hero Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Atmospheric Blue-Turquoise Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AdventureBlueDark.copy(alpha = 0.55f),
                            Color.Transparent,
                            AdventureBlueDark.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Floating Mystical Letters in Background
        val letters = listOf("W", "O", "R", "D", "Q", "U", "E", "S", "T")
        letters.forEachIndexed { i, char ->
            val angle = i * 40f
            val rad = Math.toRadians(angle.toDouble())
            val dist = 140f + (i % 3) * 35f
            val xOff = (kotlin.math.cos(rad) * dist).toFloat()
            val yOff = (kotlin.math.sin(rad) * dist).toFloat() - 70f + (letterFloat.value * 12f * if (i % 2 == 0) 1 else -1)

            Text(
                text = char,
                color = Color.White.copy(alpha = 0.25f + (i % 3) * 0.15f),
                fontSize = (22 + (i % 4) * 6).sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = xOff.dp, y = yOff.dp)
                    .rotate((i * 15f) - 30f)
            )
        }

        // Center Brand Logo & Compass
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp)
                .scale(logoScale.value)
                .alpha(logoAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Compass Badge
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(GoldYellowLight, GoldYellow, Color(0xFFD84315))
                        )
                    )
                    .border(3.dp, Color.White, CircleShape)
                    .rotate(compassRotation.value),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🧭",
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Logo: WORDQUEST
            Box(contentAlignment = Alignment.Center) {
                // Shadow Text
                Text(
                    text = "WORDQUEST",
                    color = Color(0xFF1B2838),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    modifier = Modifier.offset(y = 4.dp)
                )
                // Gold Gradient Text
                Text(
                    text = "WORDQUEST",
                    color = GoldYellowLight,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }

            // Subtitle Banner: HIDDEN TRAILS
            Box(
                modifier = Modifier
                    .offset(y = (-4).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF5D4037), Color(0xFF8D6E63), Color(0xFF5D4037))
                        )
                    )
                    .border(1.5.dp, GoldYellow, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "HIDDEN TRAILS",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp
                )
            }
        }

        // Bottom Loading Bar & Label
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 48.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Loading Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF0F2035).copy(alpha = 0.8f))
                    .border(1.dp, Color(0xFF4FC3F7).copy(alpha = 0.5f), RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(loadingProgress.value)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(AdventureTurquoise, Color(0xFF00E5FF), GoldYellow)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading trails...",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}
