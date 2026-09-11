package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

data class ConfettiParticle(
    val startX: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val shapeType: Int // 0: rect, 1: circle
)

@Composable
fun ConfettiOverlay(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "confetti_anim")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_progress"
    )

    val colors = listOf(
        Color(0xFFFFD54F),
        Color(0xFFFF7043),
        Color(0xFF4CAF50),
        Color(0xFF29B6F6),
        Color(0xFFAB47BC),
        Color(0xFFFF4081)
    )

    val particles = remember {
        List(45) {
            ConfettiParticle(
                startX = Random.nextFloat(),
                speed = Random.nextFloat() * 0.6f + 0.7f,
                size = Random.nextFloat() * 12f + 8f,
                color = colors[Random.nextInt(colors.size)],
                rotationSpeed = Random.nextFloat() * 360f,
                shapeType = Random.nextInt(2)
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (p in particles) {
            val y = ((progress.value * p.speed + p.startX) % 1.0f) * h
            val x = (p.startX * w) + (kotlin.math.sin((progress.value * 4 + p.startX * 10).toDouble()) * 30).toFloat()
            val angle = (progress.value * p.rotationSpeed) % 360f

            rotate(angle, pivot = Offset(x, y)) {
                if (p.shapeType == 0) {
                    drawRect(
                        color = p.color,
                        topLeft = Offset(x - p.size / 2, y - p.size / 2),
                        size = Size(p.size, p.size * 0.6f)
                    )
                } else {
                    drawCircle(
                        color = p.color,
                        radius = p.size / 2.5f,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}
