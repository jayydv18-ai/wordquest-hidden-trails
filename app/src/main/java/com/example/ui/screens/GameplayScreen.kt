package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameState
import com.example.game.GeneratedPuzzle
import com.example.game.SoundManager
import com.example.game.WordSearchGenerator
import com.example.model.GridPos
import com.example.model.Level
import com.example.model.WordPlacement
import com.example.ui.components.GameButton
import com.example.ui.components.GameButtonStyle
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WordHighlightColors
import kotlinx.coroutines.delay

@Composable
fun GameplayScreen(
    level: Level,
    gameState: GameState,
    soundManager: SoundManager,
    onBackToMap: () -> Unit,
    onLevelCompleted: (stars: Int, timeSeconds: Int, wordsFound: Int) -> Unit,
    onUseHintToken: () -> Boolean,
    onBuyHintWithCoins: () -> Boolean = { false },
    onWatchAdForHint: () -> Unit = {},
    onTutorialFinished: () -> Unit
) {
    // Generate the puzzle once per level
    val puzzle = remember(level.id) {
        WordSearchGenerator.generate(level.words, level.gridSize)
    }

    val foundWords = remember { mutableStateListOf<String>() }
    var currentSelection by remember { mutableStateOf<List<GridPos>>(emptyList()) }
    var dragStartPos by remember { mutableStateOf<GridPos?>(null) }
    var highlightedHintPositions by remember { mutableStateOf<List<GridPos>>(emptyList()) }
    var showPauseMenu by remember { mutableStateOf(false) }
    var showRestartConfirm by remember { mutableStateOf(false) }
    var showHintMenu by remember { mutableStateOf(false) }
    var boardPixelSize by remember { mutableStateOf(0f) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var mistakesCount by remember { mutableIntStateOf(0) }
    var hintsUsedCount by remember { mutableIntStateOf(0) }
    var isTutorialActive by remember {
        mutableStateOf(!gameState.tutorialCompleted && level.id == 1)
    }

    // Shake animation on wrong selection
    val shakeOffset = remember { Animatable(0f) }

    // Timer loop
    LaunchedEffect(showPauseMenu) {
        while (!showPauseMenu) {
            delay(1000)
            elapsedSeconds++
        }
    }

    // Check level completion
    LaunchedEffect(foundWords.size) {
        if (foundWords.size == level.words.size && level.words.isNotEmpty()) {
            soundManager.playLevelComplete()
            delay(400)
            // Calculate stars
            val stars = when {
                hintsUsedCount == 0 && mistakesCount <= 2 && elapsedSeconds <= level.targetTimeSeconds -> 3
                hintsUsedCount <= 1 && mistakesCount <= 5 -> 2
                else -> 1
            }
            onLevelCompleted(stars, elapsedSeconds, foundWords.size)
        }
    }

    // Animated tutorial finger bounce
    val infiniteTransition = rememberInfiniteTransition(label = "tut_trans")
    val tutorialSwipeProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tut_swipe"
    )

    // Current target category label: shows next unfound word
    val nextTargetWord = remember(foundWords.toList()) {
        level.words.firstOrNull { it !in foundWords } ?: level.words.first()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdventureBlueDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("gameplay_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Action Bar
            TopGameplayHeader(
                level = level,
                targetWord = nextTargetWord,
                hintsAvailable = gameState.hints,
                onBackClick = onBackToMap,
                onPauseClick = { showPauseMenu = true },
                onHintClick = { showHintMenu = true }
            )

            // Center: Word Search Puzzle Board with drag interaction
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .offset(x = shakeOffset.value.dp),
                contentAlignment = Alignment.Center
            ) {
                PuzzleBoardCard(
                    puzzle = puzzle,
                    currentSelection = currentSelection,
                    foundPlacements = puzzle.wordPlacements.filter { it.word in foundWords },
                    hintPositions = highlightedHintPositions,
                    isTutorialActive = isTutorialActive,
                    tutorialSwipeProgress = tutorialSwipeProgress,
                    onBoardPixelSizeMeasured = { boardPixelSize = it },
                    onDragUpdate = { touchOffset ->
                        if (boardPixelSize > 0) {
                            val cellSize = boardPixelSize / puzzle.gridSize
                            val row = (touchOffset.y / cellSize).toInt().coerceIn(0, puzzle.gridSize - 1)
                            val col = (touchOffset.x / cellSize).toInt().coerceIn(0, puzzle.gridSize - 1)
                            val currentPos = GridPos(row, col)

                            if (dragStartPos == null) {
                                dragStartPos = currentPos
                                currentSelection = listOf(currentPos)
                                soundManager.playLetterSelect(1)
                            } else {
                                val line = WordSearchGenerator.computeLineBetween(dragStartPos!!, currentPos)
                                if (line != null && line != currentSelection) {
                                    currentSelection = line
                                    soundManager.playLetterSelect(line.size)
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        val sel = currentSelection
                        if (sel.size >= 2) {
                            val formedWord = sel.map { puzzle.grid[it.row][it.col] }.joinToString("")
                            val reversedWord = formedWord.reversed()

                            val matchedWord = level.words.find { target ->
                                (target.equals(formedWord, ignoreCase = true) || target.equals(reversedWord, ignoreCase = true)) &&
                                        target !in foundWords
                            }

                            if (matchedWord != null) {
                                foundWords.add(matchedWord)
                                soundManager.playWordFound()
                                highlightedHintPositions = emptyList()

                                if (isTutorialActive) {
                                    isTutorialActive = false
                                    onTutorialFinished()
                                }
                            } else {
                                soundManager.playError()
                                mistakesCount++
                                // Gentle shake effect
                                kotlinx.coroutines.GlobalScope.apply {
                                    // Trigger shake animation
                                }
                            }
                        }
                        currentSelection = emptyList()
                        dragStartPos = null
                    }
                )
            }

            // Bottom Section: Words to Find List
            WordListPanel(
                targetWords = level.words,
                foundWords = foundWords.toSet(),
                wordPlacements = puzzle.wordPlacements
            )
        }

        // Pause Menu Dialog
        if (showPauseMenu) {
            PauseMenuDialog(
                onResume = { showPauseMenu = false },
                onRestart = {
                    showPauseMenu = false
                    showRestartConfirm = true
                },
                onQuitToMap = {
                    showPauseMenu = false
                    onBackToMap()
                }
            )
        }

        // Restart Confirmation Dialog
        if (showRestartConfirm) {
            AlertDialog(
                onDismissRequest = { showRestartConfirm = false },
                title = { Text("Restart Level?", fontWeight = FontWeight.Bold) },
                text = { Text("Your current progress in this level will be reset.") },
                confirmButton = {
                    GameButton(
                        text = "RESTART",
                        onClick = {
                            showRestartConfirm = false
                            foundWords.clear()
                            currentSelection = emptyList()
                            elapsedSeconds = 0
                            mistakesCount = 0
                        },
                        style = GameButtonStyle.ORANGE,
                        height = 42.dp,
                        fontSize = 14
                    )
                },
                dismissButton = {
                    GameButton(
                        text = "CANCEL",
                        onClick = { showRestartConfirm = false },
                        style = GameButtonStyle.WOOD,
                        height = 42.dp,
                        fontSize = 14
                    )
                },
                containerColor = ParchmentCream
            )
        }

        // Hint System Dialog
        if (showHintMenu) {
            HintChoiceDialog(
                hintsAvailable = gameState.hints,
                coins = gameState.coins,
                onBuyHint = { onBuyHintWithCoins() },
                onWatchAdForHint = {
                    showHintMenu = false
                    onWatchAdForHint()
                },
                onDismiss = { showHintMenu = false },
                onChooseHint = { hintType ->
                    showHintMenu = false
                    if (onUseHintToken()) {
                        hintsUsedCount++
                        soundManager.playHint()

                        val unfound = puzzle.wordPlacements.filter { it.word !in foundWords }
                        val target = unfound.firstOrNull()

                        if (target != null) {
                            when (hintType) {
                                1 -> {
                                    // Reveal first letter
                                    highlightedHintPositions = listOf(target.positions.first())
                                }
                                2 -> {
                                    // Highlight start & direction (first two letters)
                                    highlightedHintPositions = target.positions.take(2)
                                }
                                3 -> {
                                    // Reveal full word
                                    foundWords.add(target.word)
                                    soundManager.playWordFound()
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun TopGameplayHeader(
    level: Level,
    targetWord: String,
    hintsAvailable: Int,
    onBackClick: () -> Unit,
    onPauseClick: () -> Unit,
    onHintClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B3252))
                    .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                    .clickable(onClick = onBackClick)
                    .testTag("gameplay_back_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Level Title & Theme
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL ${level.id.toString().padStart(2, '0')}",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = level.theme,
                    color = GoldYellowLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Right Action Buttons (Hint & Pause)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hint Button with badge counter
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(GoldYellowLight, GoldYellow, Color(0xFFE65100))
                            )
                        )
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable(onClick = onHintClick)
                        .testTag("gameplay_hint_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Hint",
                        tint = Color(0xFF3E2723),
                        modifier = Modifier.size(24.dp)
                    )
                    // Badge count
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 3.dp, y = 3.dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0288D1))
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hintsAvailable.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Pause Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B3252))
                        .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .clickable(onClick = onPauseClick)
                        .testTag("gameplay_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Target Word Badge Chip
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF66BB6A), MeadowGreenDark)
                    )
                )
                .border(1.5.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 5.dp)
        ) {
            Text(
                text = targetWord,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun PuzzleBoardCard(
    puzzle: GeneratedPuzzle,
    currentSelection: List<GridPos>,
    foundPlacements: List<WordPlacement>,
    hintPositions: List<GridPos>,
    isTutorialActive: Boolean,
    tutorialSwipeProgress: Float,
    onBoardPixelSizeMeasured: (Float) -> Unit,
    onDragUpdate: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(ParchmentWhite)
            .border(3.dp, BoardBorder, RoundedCornerShape(20.dp))
            .onGloballyPositioned { coordinates ->
                onBoardPixelSizeMeasured(coordinates.size.width.toFloat())
            }
            .pointerInput(puzzle.gridSize) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onDragUpdate(offset)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        onDragUpdate(change.position)
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDragCancel = {
                        onDragEnd()
                    }
                )
            }
    ) {
        val boardSize = maxWidth
        val cellSize = boardSize / puzzle.gridSize

        // Background canvas to draw smooth rounded pill highlights for found words & current drag selection
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellPx = size.width / puzzle.gridSize

            // 1. Draw Found Words (solid color pill lines)
            for (placement in foundPlacements) {
                if (placement.positions.size >= 2) {
                    val color = WordHighlightColors[placement.colorIndex % WordHighlightColors.size]
                    val start = placement.positions.first()
                    val end = placement.positions.last()

                    val startOffset = Offset((start.col + 0.5f) * cellPx, (start.row + 0.5f) * cellPx)
                    val endOffset = Offset((end.col + 0.5f) * cellPx, (end.row + 0.5f) * cellPx)

                    drawLine(
                        color = color.copy(alpha = 0.55f),
                        start = startOffset,
                        end = endOffset,
                        strokeWidth = cellPx * 0.82f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // 2. Draw Current Active Drag Selection
            if (currentSelection.size >= 2) {
                val start = currentSelection.first()
                val end = currentSelection.last()

                val startOffset = Offset((start.col + 0.5f) * cellPx, (start.row + 0.5f) * cellPx)
                val endOffset = Offset((end.col + 0.5f) * cellPx, (end.row + 0.5f) * cellPx)

                drawLine(
                    color = Color(0xFFFFB300).copy(alpha = 0.65f),
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = cellPx * 0.85f,
                    cap = StrokeCap.Round
                )
            } else if (currentSelection.size == 1) {
                val single = currentSelection.first()
                drawCircle(
                    color = Color(0xFFFFB300).copy(alpha = 0.65f),
                    radius = cellPx * 0.42f,
                    center = Offset((single.col + 0.5f) * cellPx, (single.row + 0.5f) * cellPx)
                )
            }

            // 3. Draw Hint highlights (glowing pulse around target positions)
            for (hintPos in hintPositions) {
                drawCircle(
                    color = Color(0xFF00E5FF).copy(alpha = 0.7f),
                    radius = cellPx * 0.45f,
                    center = Offset((hintPos.col + 0.5f) * cellPx, (hintPos.row + 0.5f) * cellPx),
                    style = Stroke(width = 6f)
                )
            }
        }

        // Grid of Letters
        Column(modifier = Modifier.fillMaxSize()) {
            for (r in 0 until puzzle.gridSize) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (c in 0 until puzzle.gridSize) {
                        val char = puzzle.grid[r][c]
                        val isSelected = currentSelection.contains(GridPos(r, c))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                color = if (isSelected) Color.White else TextDark,
                                fontSize = if (puzzle.gridSize <= 8) 22.sp else if (puzzle.gridSize <= 10) 19.sp else 16.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Interactive Tutorial Gesture Animation
        if (isTutorialActive) {
            val firstWord = puzzle.wordPlacements.firstOrNull()
            if (firstWord != null && firstWord.positions.size >= 2) {
                val start = firstWord.positions.first()
                val end = firstWord.positions.last()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x22000000))
                ) {
                    // Tutorial Helper Banner
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xE6000000))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "👉 DRAG TO FIND: ${firstWord.word}",
                            color = GoldYellowLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Floating Hand Icon moving from start to end
                    val interpX = (start.col + (end.col - start.col) * tutorialSwipeProgress) * (this@BoxWithConstraints.maxWidth.value / puzzle.gridSize)
                    val interpY = (start.row + (end.row - start.row) * tutorialSwipeProgress) * (this@BoxWithConstraints.maxWidth.value / puzzle.gridSize)

                    Text(
                        text = "👆",
                        fontSize = 32.sp,
                        modifier = Modifier
                            .offset(x = interpX.dp + 10.dp, y = interpY.dp + 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WordListPanel(
    targetWords: List<String>,
    foundWords: Set<String>,
    wordPlacements: List<WordPlacement>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(ParchmentCream)
            .border(2.dp, BoardBorder, RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(115.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(targetWords) { word ->
                val isFound = word in foundWords
                val placement = wordPlacements.find { it.word == word }
                val highlightColor = if (placement != null) {
                    WordHighlightColors[placement.colorIndex % WordHighlightColors.size]
                } else MeadowGreen

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isFound) highlightColor.copy(alpha = 0.15f) else Color.Transparent
                        )
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                ) {
                    if (isFound) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Found",
                            tint = highlightColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                    Text(
                        text = word,
                        color = if (isFound) highlightColor else TextDark,
                        fontSize = 13.sp,
                        fontWeight = if (isFound) FontWeight.Bold else FontWeight.Black,
                        textDecoration = if (isFound) TextDecoration.LineThrough else TextDecoration.None,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PauseMenuDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuitToMap: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
            .clickable(onClick = onResume),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(ParchmentCream)
                .border(3.dp, BoardBorder, RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PAUSED",
                color = TextDark,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            GameButton(
                text = "RESUME",
                onClick = onResume,
                style = GameButtonStyle.GREEN,
                height = 50.dp,
                fontSize = 16
            )

            Spacer(modifier = Modifier.height(12.dp))

            GameButton(
                text = "RESTART",
                onClick = onRestart,
                style = GameButtonStyle.ORANGE,
                height = 50.dp,
                fontSize = 16
            )

            Spacer(modifier = Modifier.height(12.dp))

            GameButton(
                text = "QUIT TO MAP",
                onClick = onQuitToMap,
                style = GameButtonStyle.BLUE,
                height = 50.dp,
                fontSize = 16
            )
        }
    }
}

@Composable
fun HintChoiceDialog(
    hintsAvailable: Int,
    coins: Int,
    onBuyHint: () -> Unit,
    onWatchAdForHint: () -> Unit = {},
    onDismiss: () -> Unit,
    onChooseHint: (type: Int) -> Unit
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HINT SELECTION",
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8EAF6))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hints: $hintsAvailable 💡",
                    color = MeadowGreenDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Coins: $coins 🪙",
                    color = Color(0xFFE65100),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Buy Hint for 25 Coins section
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
                    onClick = onBuyHint,
                    style = GameButtonStyle.GOLD,
                    height = 36.dp,
                    fontSize = 12,
                    enabled = coins >= 25,
                    testTag = "buy_hint_25_button"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Free Hint with Rewarded Video Ad section
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Free Hint (💡)",
                            color = TextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2E7D32))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "REWARD",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Watch short video for +1 Hint",
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
                    testTag = "watch_ad_for_hint_button"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HintOptionCard(
                icon = "🔤",
                title = "First Letter",
                desc = "Highlight the first letter of a hidden word.",
                enabled = hintsAvailable > 0,
                onClick = { onChooseHint(1) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            HintOptionCard(
                icon = "🧭",
                title = "Start & Direction",
                desc = "Highlight starting position and direction.",
                enabled = hintsAvailable > 0,
                onClick = { onChooseHint(2) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            HintOptionCard(
                icon = "✨",
                title = "Reveal Complete Word",
                desc = "Instantly discover a full word path.",
                enabled = hintsAvailable > 0,
                onClick = { onChooseHint(3) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            GameButton(
                text = "CLOSE",
                onClick = onDismiss,
                style = GameButtonStyle.WOOD,
                height = 42.dp,
                fontSize = 14
            )
        }
    }
}

@Composable
fun HintOptionCard(
    icon: String,
    title: String,
    desc: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) ParchmentWhite else Color(0xFFE0E0E0))
            .border(1.5.dp, if (enabled) Color(0xFF81D4FA) else Color.Gray, RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (enabled) TextDark else Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = desc,
                color = if (enabled) TextMuted else Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}
