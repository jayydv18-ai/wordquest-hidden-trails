package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GameColorScheme = lightColorScheme(
    primary = AdventureBlue,
    onPrimary = Color.White,
    primaryContainer = AdventureBlueLight,
    onPrimaryContainer = Color.White,
    secondary = AdventureTurquoise,
    onSecondary = Color.White,
    secondaryContainer = AdventureTurquoiseLight,
    onSecondaryContainer = Color.White,
    tertiary = GoldYellow,
    onTertiary = Color.Black,
    background = AdventureBlueDark,
    onBackground = Color.White,
    surface = ParchmentWhite,
    onSurface = TextDark,
    surfaceVariant = ParchmentCream,
    onSurfaceVariant = TextMuted
)

@Composable
fun WordQuestTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameColorScheme,
        typography = Typography,
        content = content
    )
}
