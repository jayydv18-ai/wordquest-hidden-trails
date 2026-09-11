package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import com.example.ads.UnityBannerAd
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GameState
import com.example.data.LevelRepository
import com.example.model.Chapter
import com.example.model.Level
import com.example.ui.theme.AdventureBlueDark
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GoldYellowLight
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    gameState: GameState,
    onBackClick: () -> Unit,
    onLevelSelected: (Level) -> Unit,
    onChapterIntroClick: (Chapter) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val activeChunkIndex = (gameState.unlockedLevel - 1) / 5

    // Dynamic chunk count supporting continuous infinite scrolling
    var loadedChunkCount by remember {
        mutableIntStateOf(maxOf(12, activeChunkIndex + 6))
    }

    val listState = rememberLazyListState()

    // Scroll to current active level chunk on launch
    LaunchedEffect(Unit) {
        if (activeChunkIndex in 0 until loadedChunkCount) {
            listState.scrollToItem(activeChunkIndex)
        }
    }

    // Infinite scroll detection: as the user scrolls down, load more chunks
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= loadedChunkCount - 2) {
                    loadedChunkCount += 6 // Append 30 more levels seamlessly
                }
            }
    }

    // Pulse animation for current active node
    val infiniteTransition = rememberInfiniteTransition(label = "map_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "active_pulse"
    )

    // Check if the current level chunk is currently visible
    val isCurrentLevelVisible by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.any { it.index == activeChunkIndex }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("map_screen")
    ) {
        // Map World Landscape Background
        Image(
            painter = painterResource(id = R.drawable.img_map_background),
            contentDescription = "Adventure map background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Soft ambient parchment overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x221B2838))
        )

        // Infinite Scrollable Levels Trail
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(top = 70.dp, bottom = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = loadedChunkCount,
                key = { chunkIndex -> "trail_chunk_$chunkIndex" }
            ) { chunkIndex ->
                val chunkLevels = LevelRepository.getLevelsForChunk(chunkIndex)
                val chapter = LevelRepository.getChapterForLevel(chunkLevels.first().id)

                MapTrailChunk(
                    chunkIndex = chunkIndex,
                    chapter = chapter,
                    levels = chunkLevels,
                    gameState = gameState,
                    pulseScale = pulseScale,
                    onLevelSelected = onLevelSelected,
                    onChapterIntroClick = onChapterIntroClick
                )
            }

            // Infinite Loading indicator at bottom of trail
            item(key = "trail_infinite_loader") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xCC263238))
                            .border(1.5.dp, GoldYellow, RoundedCornerShape(20.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            color = GoldYellow,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "Unfolding infinite trails...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Floating Top Header (Navigation + Stats)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back to Home Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(AdventureBlueDark.copy(alpha = 0.90f))
                    .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    .clickable(onClick = onBackClick)
                    .testTag("map_back_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Title & Current Level badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF3E2723), Color(0xFF5D4037), Color(0xFF3E2723))
                        )
                    )
                    .border(2.dp, GoldYellow, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "🧭 WORLD TRAIL",
                    color = GoldYellowLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0288D1))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Lvl ${gameState.unlockedLevel}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Resource Balances
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Coins
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1B2838).copy(alpha = 0.9f))
                        .border(1.dp, GoldYellow.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🪙", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = gameState.coins.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }

                // Hints
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1B2838).copy(alpha = 0.9f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = gameState.hints.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Floating "Jump to Current Level" Button (appears when scrolled away)
        AnimatedVisibility(
            visible = !isCurrentLevelVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 60.dp)
        ) {
            Box(
                modifier = Modifier
                    .shadow(10.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0288D1), Color(0xFF01579B))
                        )
                    )
                    .border(2.dp, GoldYellowLight, RoundedCornerShape(24.dp))
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(activeChunkIndex)
                        }
                    }
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📍", fontSize = 16.sp)
                    Text(
                        text = "JUMP TO LEVEL ${gameState.unlockedLevel}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Unity Banner Ad anchored at the bottom of Map screen
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            UnityBannerAd()
        }
    }
}

@Composable
fun MapTrailChunk(
    chunkIndex: Int,
    chapter: Chapter,
    levels: List<Level>,
    gameState: GameState,
    pulseScale: Float,
    onLevelSelected: (Level) -> Unit,
    onChapterIntroClick: (Chapter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chapter Banner Header
        Row(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF4E342E), Color(0xFF6D4C41), Color(0xFF4E342E))
                    )
                )
                .border(2.dp, GoldYellow, RoundedCornerShape(16.dp))
                .clickable { onChapterIntroClick(chapter) }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${chapter.title.uppercase()} • ${chapter.terrainZone}",
                    color = GoldYellowLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = chapter.subtitle,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x44000000))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Lvl ${chapter.startLevel} - ${chapter.endLevel}",
                    color = GoldYellowLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5 Levels laid out along a winding trail
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
        ) {
            val width = maxWidth.value

            // 5 Node Positions inside this 580dp chunk
            // Winding coordinates (from top of chunk to bottom of chunk)
            val nodeOffsets = listOf(
                Offset(width * 0.50f, 60f),  // Level 1 of chunk
                Offset(width * 0.74f, 170f), // Level 2 of chunk
                Offset(width * 0.44f, 280f), // Level 3 of chunk
                Offset(width * 0.24f, 390f), // Level 4 of chunk
                Offset(width * 0.56f, 500f)  // Level 5 of chunk (checkpoint/chest)
            )

            // Canvas drawing the winding path
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path()
                path.moveTo(nodeOffsets[0].x, nodeOffsets[0].y)
                for (i in 0 until nodeOffsets.size - 1) {
                    val p1 = nodeOffsets[i]
                    val p2 = nodeOffsets[i + 1]
                    val midY = (p1.y + p2.y) / 2
                    path.cubicTo(
                        p1.x, midY,
                        p2.x, midY,
                        p2.x, p2.y
                    )
                }

                // Connector line to next chunk
                val lastNode = nodeOffsets.last()
                path.cubicTo(
                    lastNode.x, lastNode.y + 40f,
                    width * 0.50f, 560f,
                    width * 0.50f, 580f
                )

                // Outer Shadow
                drawPath(
                    path = path,
                    color = Color(0x553E2723),
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                // Cobblestone/dirt layer
                drawPath(
                    path = path,
                    color = Color(0xFFD7CCC8),
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )
                // Golden center line
                drawPath(
                    path = path,
                    color = Color(0xFFFDD835),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
            }

            // Render the 5 nodes
            levels.forEachIndexed { index, level ->
                if (index < nodeOffsets.size) {
                    val pos = nodeOffsets[index]
                    val isCompleted = gameState.completedLevels.contains(level.id)
                    val isUnlocked = level.id <= gameState.unlockedLevel
                    val isCurrent = level.id == gameState.unlockedLevel
                    val stars = gameState.levelStars[level.id] ?: 0
                    val isCheckpoint = level.id % 5 == 0

                    val isGrandFinale = level.id == 50
                    val boxSize = if (isGrandFinale) 100 else 76
                    val halfBox = boxSize / 2

                    Box(
                        modifier = Modifier
                            .offset(x = (pos.x - halfBox).dp, y = (pos.y - halfBox).dp)
                            .size(boxSize.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LevelNode(
                            level = level,
                            isCompleted = isCompleted,
                            isUnlocked = isUnlocked,
                            isCurrent = isCurrent,
                            stars = stars,
                            isCheckpoint = isCheckpoint,
                            pulseScale = pulseScale,
                            onClick = {
                                if (isUnlocked) {
                                    onLevelSelected(level)
                                }
                            }
                        )

                        // Mascot avatar standing on the active level node
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-30).dp)
                                    .size(42.dp)
                                    .shadow(6.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF8E1))
                                    .border(2.dp, GoldYellow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_companion),
                                    contentDescription = "Current position companion",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

        // Chapter Landmark Showcase for Chapters 5 to 10
        if (chapter.id in 5..10) {
            Spacer(modifier = Modifier.height(10.dp))
            ChapterLandmarkDecor(
                chapterId = chapter.id,
                isUnlocked = gameState.unlockedLevel >= chapter.startLevel
            )
        }
    }
}

@Composable
fun ChapterLandmarkDecor(chapterId: Int, isUnlocked: Boolean) {
    val (label, iconRes, desc) = when (chapterId) {
        5 -> Triple("Sunset Coast Landmark", R.drawable.img_sunset_coast, "Warm coastal dunes & shimmering sunset waters")
        6 -> Triple("Enchanted Forest Landmark", R.drawable.img_enchanted_forest, "Ancient mossy trees & glowing fairy lights")
        7 -> Triple("Frozen Peaks Landmark", R.drawable.img_frozen_peaks, "Glittering crystal summits & frozen caverns")
        8 -> Triple("Desert Ruins Landmark", R.drawable.img_desert_ruins, "Ancient temple columns & golden pyramids")
        9 -> Triple("Sky Islands Landmark", R.drawable.img_sky_islands, "Floating celestial aeries & drifting airships")
        10 -> Triple("The Ancient Kingdom Citadel", R.drawable.img_ancient_kingdom, "Grand Royal Castle Finale Destination")
        else -> return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF263238))
            .border(2.dp, if (isUnlocked) GoldYellow else Color(0xFF546E7A), RoundedCornerShape(18.dp))
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xE6101720))
                    )
                )
                .padding(10.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "⭐ $label",
                        color = GoldYellowLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = desc,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (chapterId == 10) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD84315))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "👑 GRAND FINALE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelNode(
    level: Level,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    stars: Int,
    isCheckpoint: Boolean,
    pulseScale: Float,
    onClick: () -> Unit
) {
    val isGrandFinale = level.id == 50
    val nodeSize = if (isGrandFinale) 82.dp else if (isCheckpoint) 68.dp else if (isCurrent) 62.dp else 56.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = isUnlocked, onClick = onClick)
            .testTag("level_node_${level.id}")
    ) {
        if (isGrandFinale) {
            Text(text = "👑", fontSize = 18.sp, modifier = Modifier.offset(y = 4.dp))
        }

        Box(
            modifier = Modifier
                .size(nodeSize)
                .scale(if (isCurrent || isGrandFinale) pulseScale else 1f)
                .shadow(
                    elevation = if (isGrandFinale) 16.dp else if (isCurrent) 12.dp else 6.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    when {
                        isGrandFinale -> Brush.radialGradient(
                            listOf(Color(0xFFFFD54F), Color(0xFFFF8F00), Color(0xFFBF360C))
                        )
                        isCurrent -> Brush.radialGradient(
                            listOf(Color(0xFF81D4FA), Color(0xFF0288D1), Color(0xFF01579B))
                        )
                        isCompleted -> Brush.radialGradient(
                            listOf(Color(0xFF64B5F6), Color(0xFF1976D2), Color(0xFF0D47A1))
                        )
                        isUnlocked -> Brush.radialGradient(
                            listOf(Color(0xFFFFF176), GoldYellow, Color(0xFFE65100))
                        )
                        else -> Brush.radialGradient(
                            listOf(Color(0xFF78909C), Color(0xFF455A64), Color(0xFF263238))
                        )
                    }
                )
                .border(
                    width = if (isGrandFinale) 5.dp else if (isCheckpoint) 4.dp else 3.dp,
                    color = when {
                        isGrandFinale -> Color(0xFFFFD700)
                        isCheckpoint -> GoldYellow
                        isCurrent -> Color.White
                        isCompleted -> Color(0xFFBBDEFB)
                        isUnlocked -> Color.White
                        else -> Color(0xFF90A4AE)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFFB0BEC5),
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = level.id.toString(),
                    color = Color.White,
                    fontSize = if (isGrandFinale) 24.sp else if (isCheckpoint) 22.sp else 18.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Stars earned underneath completed node
        if (isCompleted) {
            Row(
                modifier = Modifier
                    .offset(y = (-4).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xCC000000))
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (s in 1..3) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (s <= stars) GoldYellow else Color.Gray,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}
