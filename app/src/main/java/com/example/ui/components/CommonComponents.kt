package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WordLevel
import com.example.data.repository.LevelRepository
import com.example.storage.UserProgress
import com.example.ui.theme.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun HeartBar(
    currentHearts: Int,
    maxHearts: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxHearts) {
            val isFilled = i <= currentHearts
            val scale by animateFloatAsState(
                targetValue = if (isFilled) 1f else 0.85f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "heartScale"
            )

            Icon(
                imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Heart $i",
                tint = if (isFilled) DangerRed else TextMuted.copy(alpha = 0.4f),
                modifier = Modifier
                    .size(28.dp)
                    .scale(scale)
            )
        }
    }
}

@Composable
fun CoinPill(
    coins: Int,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = OrangeCoin.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, OrangeCoin.copy(alpha = 0.4f)),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("🪙", fontSize = 16.sp)
            Text(
                text = coins.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextDark
            )
        }
    }
}

@Composable
fun StarPill(
    stars: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = GoldStar.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldStar.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Stars",
                tint = GoldStar,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stars.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextDark
            )
        }
    }
}

@Composable
fun StreakPill(
    streakDays: Int,
    hasCompletedToday: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (hasCompletedToday) Color(0xFFFF6D00).copy(alpha = 0.15f) else TextMuted.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (hasCompletedToday) Color(0xFFFF6D00).copy(alpha = 0.5f) else TextMuted.copy(alpha = 0.3f)
        ),
        modifier = modifier.testTag("streak_pill")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(if (hasCompletedToday) "🔥" else "⚡", fontSize = 16.sp)
            Text(
                text = "$streakDays ${if (streakDays == 1) "Day" else "Days"}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = if (hasCompletedToday) Color(0xFFD84315) else TextMuted
            )
        }
    }
}

@Composable
fun DailyStreakCard(
    progress: UserProgress,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (progress.hasCompletedToday) Color(0xFFFFF3E0) else Color(0xFFEDE7F6)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (progress.hasCompletedToday) Color(0xFFFFB74D) else PurplePrimary.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_streak_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (progress.hasCompletedToday) Color(0xFFFF6D00) else PurplePrimary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (progress.hasCompletedToday) "🔥" else "⚡",
                        fontSize = 24.sp
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${progress.dailyStreak} Day Streak!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        if (progress.hasCompletedToday) {
                            Surface(
                                shape = CircleShape,
                                color = MintSuccess,
                                modifier = Modifier.clip(CircleShape)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = if (progress.hasCompletedToday)
                            "Great job! You claimed today's daily streak bonus! 🪙"
                        else
                            "Solve 1 level today to extend streak & earn bonus coins! 🪙",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted,
                        lineHeight = 16.sp
                    )
                }
            }

            if (!progress.hasCompletedToday) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("PLAY ⚡", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun XpProgressBar(
    currentXp: Int,
    modifier: Modifier = Modifier
) {
    val levelNumber = (currentXp / 100) + 1
    val xpInCurrentLevel = currentXp % 100
    val progress = xpInCurrentLevel / 100f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "xpProgress"
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "LEVEL $levelNumber",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = PurplePrimary
            )
            Text(
                text = "$xpInCurrentLevel / 100 XP",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(PurplePrimary.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(PurplePrimary, PinkSecondary)
                        )
                    )
            )
        }
    }
}

private val KidPaletteColors = listOf(
    Color(0xFFFF5252), // Coral Red
    Color(0xFFFF9800), // Vibrant Orange
    Color(0xFF4CAF50), // Fresh Green
    Color(0xFF00BCD4), // Cyan Sky
    Color(0xFF3F51B5), // Royal Blue
    Color(0xFF9C27B0), // Bright Violet
    Color(0xFFE91E63), // Hot Pink
    Color(0xFFFFB300)  // Golden Amber
)

private val KidPaletteLightBg = listOf(
    Color(0xFFFFF0F0),
    Color(0xFFFFF5E6),
    Color(0xFFF0F9F0),
    Color(0xFFE6FAFC),
    Color(0xFFEEF0FA),
    Color(0xFFF8EEFA),
    Color(0xFFFCE8F0),
    Color(0xFFFFFDF0)
)

@Composable
fun AnimatedLetterTile(
    letter: Char,
    isHighlighted: Boolean = false,
    tileIndex: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else if (isHighlighted) 1.10f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tileScale"
    )

    // Pulse animation for hint highlight
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val colorAccent = KidPaletteColors[tileIndex % KidPaletteColors.size]
    val bgLightColor = KidPaletteLightBg[tileIndex % KidPaletteLightBg.size]

    val backgroundColor = if (isHighlighted) YellowAccent else bgLightColor
    val borderColor = if (isHighlighted) GoldStar.copy(alpha = pulseAlpha) else colorAccent
    val textColor = if (isHighlighted) TextDark else colorAccent

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .shadow(
                elevation = if (isHighlighted) 8.dp else 4.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .border(
                width = if (isHighlighted) 3.5.dp else 2.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .testTag("tile_${letter}"),
        contentAlignment = Alignment.Center
    ) {
        // 3D bottom shade accent inside tile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .align(Alignment.BottomCenter)
                .background(borderColor.copy(alpha = 0.25f))
        )

        Text(
            text = letter.toString(),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(120)
            isPressed = false
        }
    }
}

@Composable
fun CustomPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    gradientColors: List<Color> = listOf(PurplePrimary, DeepPurple)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (enabled) Brush.horizontalGradient(gradientColors)
                else Brush.horizontalGradient(listOf(TextMuted, TextMuted))
            )
            .clickable(
                enabled = enabled,
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .padding(vertical = 14.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = LightSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun WordExplanationSheet(
    wordLevel: WordLevel,
    onSpeakWord: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("word_explanation_card")
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${wordLevel.categoryEmoji} ${wordLevel.category}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = wordLevel.word,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )

                IconButton(
                    onClick = onSpeakWord,
                    modifier = Modifier
                        .size(40.dp)
                        .background(PurplePrimary.copy(alpha = 0.12f), CircleShape)
                        .testTag("speak_word_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak word",
                        tint = PurplePrimary
                    )
                }
            }

            Text(
                text = "/${wordLevel.pronunciation}/",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )

            HorizontalDivider(color = CardBorder, thickness = 1.dp)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Meaning:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PurplePrimary
                )
                Text(
                    text = wordLevel.meaning,
                    fontSize = 15.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Example:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PinkSecondary
                )
                Text(
                    text = "\"${wordLevel.exampleSentence}\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomPrimaryButton(
                text = "Continue 🚀",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun VisualLevelMapRow(
    progress: UserProgress,
    onSelectLevel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val levels = remember { LevelRepository.getAllLevels() }
    val listState = rememberLazyListState()

    LaunchedEffect(progress.unlockedLevel) {
        val targetIndex = (progress.unlockedLevel - 1).coerceIn(0, levels.size - 1)
        listState.animateScrollToItem(targetIndex)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🗺️ 1000 LEVELS MAP",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PurplePrimary
            )
            Text(
                text = "${progress.unlockedLevel}/1000 Unlocked",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )
        }

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("visual_level_map_row")
        ) {
            itemsIndexed(levels) { index, level ->
                val isUnlocked = level.levelNumber <= progress.unlockedLevel
                val isCurrent = level.levelNumber == progress.unlockedLevel
                val starsEarned = progress.starsPerLevel[level.levelNumber] ?: if (level.levelNumber < progress.unlockedLevel) 3 else 0

                LevelMapItemNode(
                    level = level,
                    isUnlocked = isUnlocked,
                    isCurrent = isCurrent,
                    starsEarned = starsEarned,
                    onClick = {
                        if (isUnlocked) {
                            onSelectLevel(level.levelNumber)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LevelMapItemNode(
    level: WordLevel,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    starsEarned: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "mapCurrentNodePulse")
    val currentPulseScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mapPulseScale"
    )

    val scale = if (isCurrent) currentPulseScale else 1f

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 8.dp else if (isUnlocked) 4.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrent) 3.dp else 1.5.dp,
            color = if (isCurrent) YellowAccent else if (isUnlocked) PurplePrimary.copy(alpha = 0.4f) else CardBorder
        ),
        modifier = modifier
            .width(135.dp)
            .scale(scale)
            .clip(RoundedCornerShape(22.dp))
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("map_item_node_${level.levelNumber}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status Indicator Badge at Top
            if (isCurrent) {
                Surface(
                    shape = CircleShape,
                    color = YellowAccent,
                    modifier = Modifier.shadow(4.dp, CircleShape)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text("📍", fontSize = 11.sp)
                        Text(
                            text = "HERE!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )
                    }
                }
            } else if (isUnlocked) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (s in 1..3) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (s <= starsEarned) GoldStar else CardBorder,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            } else {
                Surface(
                    shape = CircleShape,
                    color = TextMuted.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = TextMuted,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                    )
                }
            }

            // Central Emoji Icon Box
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = when {
                            isCurrent -> PurplePrimary
                            isUnlocked -> SkyBlue.copy(alpha = 0.15f)
                            else -> LightBackground
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) level.categoryEmoji else "🔒",
                    fontSize = if (isUnlocked) 26.sp else 18.sp
                )
            }

            // Level Title & Word
            Text(
                text = "LEVEL ${level.levelNumber}",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isCurrent) PurplePrimary else if (isUnlocked) TextDark else TextMuted
            )

            Text(
                text = if (isUnlocked) level.word else "Locked",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) TextDark else TextMuted
            )
        }
    }
}

@Composable
fun AnimatedCelebrationStarsRow(
    earnedStars: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.testTag("animated_celebration_stars")
    ) {
        for (starIndex in 1..3) {
            val isEarned = starIndex <= earnedStars
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(earnedStars) {
                isVisible = false
                kotlinx.coroutines.delay((starIndex * 180).toLong())
                isVisible = true
            }

            val scale by animateFloatAsState(
                targetValue = if (isVisible && isEarned) 1f else if (!isEarned) 0.85f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "starScale_$starIndex"
            )

            val rotation by animateFloatAsState(
                targetValue = if (isVisible && isEarned) 0f else -30f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "starRotation_$starIndex"
            )

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (isEarned) GoldStar else CardBorder,
                modifier = Modifier
                    .size(34.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
            )
        }
    }
}

private enum class ParticleShape { CIRCLE, RIBBON, STAR }

private class CelebrationParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    val shape: ParticleShape,
    var rotation: Float,
    val rotationSpeed: Float,
    var alpha: Float = 1f
)

private class FloatingEmojiParticle(
    val emoji: String,
    var x: Float,
    var y: Float,
    val vy: Float,
    val amplitude: Float,
    val frequency: Float,
    val phase: Float,
    val fontSizeSp: Float
)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStarPath(
    center: Offset,
    radius: Float,
    color: Color,
    alpha: Float = 1f,
    rotationDegrees: Float = 0f
) {
    val path = Path()
    val points = 5
    val innerRadius = radius * 0.42f
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = i * Math.PI / points - Math.PI / 2 + Math.toRadians(rotationDegrees.toDouble())
        val x = center.x + r * cos(angle).toFloat()
        val y = center.y + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color, alpha = alpha)
}

@Composable
fun ConfettiCanvas(
    modifier: Modifier = Modifier
) {
    val palette = listOf(PurplePrimary, PinkSecondary, YellowAccent, MintSuccess, SkyBlue, GoldStar, OrangeCoin)

    val burstParticles = remember {
        List(35) {
            val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
            val speed = Random.nextFloat() * 0.022f + 0.008f
            CelebrationParticle(
                x = 0.5f,
                y = 0.35f,
                vx = (cos(angle) * speed).toFloat(),
                vy = (sin(angle) * speed).toFloat() - 0.005f,
                color = palette.random(),
                size = Random.nextFloat() * 14f + 8f,
                shape = ParticleShape.entries.toTypedArray().random(),
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 12f
            )
        }
    }

    val rainParticles = remember {
        List(45) {
            CelebrationParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -0.5f,
                vx = (Random.nextFloat() - 0.5f) * 0.004f,
                vy = Random.nextFloat() * 0.012f + 0.006f,
                color = palette.random(),
                size = Random.nextFloat() * 12f + 6f,
                shape = ParticleShape.entries.toTypedArray().random(),
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 8f
            )
        }
    }

    val emojiList = listOf("🎉", "⭐", "🏆", "🎈", "✨", "🥳", "🌟", "👏")
    val floatingEmojis = remember {
        List(8) { index ->
            FloatingEmojiParticle(
                emoji = emojiList[index % emojiList.size],
                x = Random.nextFloat() * 0.85f + 0.08f,
                y = 1.1f + (index * 0.15f),
                vy = Random.nextFloat() * 0.004f + 0.003f,
                amplitude = Random.nextFloat() * 0.03f + 0.015f,
                frequency = Random.nextFloat() * 0.05f + 0.03f,
                phase = Random.nextFloat() * 6.28f,
                fontSizeSp = Random.nextFloat() * 12f + 24f
            )
        }
    }

    var frame by remember { mutableIntStateOf(0) }
    val gravity = 0.00035f

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(16)
            frame++

            burstParticles.forEach { p ->
                p.x += p.vx
                p.y += p.vy
                p.vy += gravity
                p.rotation += p.rotationSpeed
            }

            rainParticles.forEach { p ->
                p.y += p.vy
                p.x += p.vx + (sin(frame * 0.05f + p.rotation) * 0.0015f)
                p.rotation += p.rotationSpeed
                if (p.y > 1.2f) {
                    p.y = -0.1f
                    p.x = Random.nextFloat()
                    p.vy = Random.nextFloat() * 0.012f + 0.006f
                }
            }

            floatingEmojis.forEach { e ->
                e.y -= e.vy
                e.x += sin(frame * e.frequency + e.phase) * e.amplitude * 0.1f
                if (e.y < -0.15f) {
                    e.y = 1.15f
                    e.x = Random.nextFloat() * 0.85f + 0.08f
                }
            }
        }
    }

    val transition = rememberInfiniteTransition(label = "shockwave")
    val shockwaveRadius by transition.animateFloat(
        initialValue = 20f,
        targetValue = 240f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shockwaveR"
    )
    val shockwaveAlpha = (1f - (shockwaveRadius / 240f)).coerceIn(0f, 1f)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width * 0.5f, size.height * 0.35f)

            if (shockwaveAlpha > 0.05f) {
                drawCircle(
                    color = GoldStar,
                    radius = shockwaveRadius * density,
                    center = centerOffset,
                    style = Stroke(width = 4.dp.toPx()),
                    alpha = shockwaveAlpha * 0.6f
                )
            }

            burstParticles.forEach { p ->
                val pos = Offset(p.x * size.width, p.y * size.height)
                if (p.x in -0.2f..1.2f && p.y in -0.2f..1.2f) {
                    when (p.shape) {
                        ParticleShape.CIRCLE -> {
                            drawCircle(color = p.color, radius = p.size, center = pos, alpha = p.alpha)
                        }
                        ParticleShape.RIBBON -> {
                            rotate(p.rotation, pos) {
                                drawRect(
                                    color = p.color,
                                    topLeft = Offset(pos.x - p.size, pos.y - (p.size * 0.4f)),
                                    size = Size(p.size * 2f, p.size * 0.8f),
                                    alpha = p.alpha
                                )
                            }
                        }
                        ParticleShape.STAR -> {
                            drawStarPath(center = pos, radius = p.size, color = p.color, alpha = p.alpha, rotationDegrees = p.rotation)
                        }
                    }
                }
            }

            rainParticles.forEach { p ->
                val pos = Offset(p.x * size.width, p.y * size.height)
                when (p.shape) {
                    ParticleShape.CIRCLE -> {
                        drawCircle(color = p.color, radius = p.size, center = pos, alpha = p.alpha)
                    }
                    ParticleShape.RIBBON -> {
                        rotate(p.rotation, pos) {
                            drawRect(
                                color = p.color,
                                topLeft = Offset(pos.x - p.size, pos.y - (p.size * 0.4f)),
                                size = Size(p.size * 2f, p.size * 0.8f),
                                alpha = p.alpha
                            )
                        }
                    }
                    ParticleShape.STAR -> {
                        drawStarPath(center = pos, radius = p.size, color = p.color, alpha = p.alpha, rotationDegrees = p.rotation)
                    }
                }
            }
        }

        floatingEmojis.forEach { e ->
            Text(
                text = e.emoji,
                fontSize = e.fontSizeSp.sp,
                modifier = Modifier.graphicsLayer {
                    translationX = e.x * widthPx
                    translationY = e.y * heightPx
                }
            )
        }
    }
}
