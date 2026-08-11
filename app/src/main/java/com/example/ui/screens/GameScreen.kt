package com.example.ui.screens

import com.example.data.repository.LevelRepository

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdManager
import com.example.game.GameEngine
import com.example.game.GameState
import com.example.ui.components.*
import com.example.ui.theme.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.ui.components.AdBannerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameEngine: GameEngine,
    adManager: AdManager,
    onNavigateNextLevel: () -> Unit,
    onBackToLevels: () -> Unit
) {
    val state by gameEngine.state.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    var showHintDialog by remember { mutableStateOf(false) }
    var showHeartsFullDialog by remember { mutableStateOf(false) }
    var showAddHeartAdDialog by remember { mutableStateOf(false) }

    // Shake offset animation for wrong selection
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(state.shakeTriggerKey) {
        if (state.shakeTriggerKey > 0) {
            repeat(3) {
                shakeOffset.animateTo(20f, tween(40))
                shakeOffset.animateTo(-20f, tween(40))
            }
            shakeOffset.animateTo(0f, tween(40))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "⭐ LEVEL ${state.currentLevel.levelNumber}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = PurplePrimary
                        )
                        Text(
                            text = "Round ${state.currentRoundNumber} of ${state.totalRounds}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackToLevels() },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PurplePrimary)
                    }
                },
                actions = {
                    HeartBar(currentHearts = state.hearts)
                    Spacer(modifier = Modifier.width(6.dp))
                    CoinPill(coins = gameEngine.userProgress.collectAsState().value.totalCoins)
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        containerColor = LightBackground,
        modifier = Modifier
            .fillMaxSize()
            .testTag("game_screen")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .offset(x = shakeOffset.value.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PurplePrimary,
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "🐾 ${state.currentLevel.category.uppercase()} • LEVEL ${state.currentLevel.levelNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LightSurface,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                    )
                }

                // VERY LARGE Word Illustration Card
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, PurplePrimary.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // VERY LARGE ANIMAL / EMOJI
                        Text(
                            text = state.currentLevel.categoryEmoji,
                            fontSize = 135.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 135.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Spell the word!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )

                            // Hear Word Audio Button
                            IconButton(
                                onClick = { gameEngine.replayWordPronunciation() },
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(PurplePrimary.copy(alpha = 0.12f), CircleShape)
                                    .testTag("audio_hear_word")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Hear Word",
                                    tint = PurplePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // VERY LARGE Word Missing Letter Slots Box
                val puzzle = state.currentPuzzle
                if (puzzle != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        puzzle.displayedLetters.forEachIndexed { index, letter ->
                            WordLetterSlot(
                                letter = letter,
                                isMissingSlot = index == puzzle.missingIndex
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                // Feedback Message Banner
                AnimatedVisibility(
                    visible = state.feedbackMessage != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    val msg = state.feedbackMessage ?: ""
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (state.feedbackIsSuccess) MintSuccess.copy(alpha = 0.18f) else DangerRed.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (state.feedbackIsSuccess) MintSuccess else DangerRed
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = msg,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = if (state.feedbackIsSuccess) MintSuccess else DangerRed,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                // Premium Colorful Keyboard & Taskbar Container Card
                Card(
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF4A00E0), // Royal Deep Violet
                                        Color(0xFF8E2DE2), // Electric Purple
                                        Color(0xFF6C5CE7)  // Vibrant Indigo
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✨ TAP THE MISSING LETTER ✨",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White.copy(alpha = 0.95f),
                                letterSpacing = 1.sp
                            )

                            // Alphabet Letter Board Grid with Colorful Tiles
                            if (puzzle != null) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 220.dp)
                                        .testTag("alphabet_board")
                                ) {
                                    itemsIndexed(puzzle.letterOptions) { index, charOption ->
                                        val isHighlighted = state.highlightedHintLetter == charOption
                                        AnimatedLetterTile(
                                            letter = charOption,
                                            isHighlighted = isHighlighted,
                                            tileIndex = index,
                                            onClick = {
                                                gameEngine.selectLetter(charOption)
                                            }
                                        )
                                    }
                                }
                            }

                            // Banner Ad directly under the Alphabet letters
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "📢 SPONSORED AD",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    AdBannerView()
                                }
                            }

                            // Bottom Action Taskbar with Left (+1 ❤️ Add Heart) and Right (💡 Hint)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Bottom-Left Corner: +1 Heart Button (Vibrant Pink Gradient)
                                Box(
                                    modifier = Modifier
                                        .shadow(8.dp, RoundedCornerShape(22.dp))
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFFFF416C), Color(0xFFFF4B2B))
                                            )
                                        )
                                        .border(2.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
                                        .clickable {
                                            if (state.hearts >= 3) {
                                                showHeartsFullDialog = true
                                            } else {
                                                showAddHeartAdDialog = true
                                            }
                                        }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .testTag("add_heart_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("❤️", fontSize = 16.sp)
                                        Text(
                                            text = "+1 HEART",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                }

                                // Bottom-Right Corner: Hint Button (Vibrant Golden Yellow Gradient)
                                Box(
                                    modifier = Modifier
                                        .shadow(8.dp, RoundedCornerShape(22.dp))
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFFFFD200), Color(0xFFF7971E))
                                            )
                                        )
                                        .border(2.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
                                        .clickable { showHintDialog = true }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .testTag("hint_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = "Hint",
                                            tint = TextDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "HINT 💡",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = TextDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Persistent AdMob Display Banner Ad
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ADVERTISEMENT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        AdBannerView()
                    }
                }
            }

            // Confetti and Lottie Celebration Overlay on Level Completion
            if (state.isLevelCompleted) {
                ConfettiCanvas()
                LottieCelebrationOverlay(
                    isTriggered = state.isLevelCompleted,
                    earnedStars = state.earnedStars,
                    earnedXP = state.earnedXP,
                    earnedCoins = state.earnedCoins,
                    levelNumber = state.currentLevel.levelNumber,
                    onContinueClick = {
                        if (state.currentLevel.levelNumber < LevelRepository.getTotalLevelCount()) {
                            onNavigateNextLevel()
                        } else {
                            onBackToLevels()
                        }
                    }
                )
            }
        }
    }

    // Add Heart Ad Confirmation Dialog
    if (showAddHeartAdDialog) {
        AlertDialog(
            onDismissRequest = { showAddHeartAdDialog = false },
            icon = { Text("❤️", fontSize = 36.sp) },
            title = { Text("Get +1 Heart! ❤️", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text("Watch a quick short video to restore +1 heart to your game!", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = {
                        showAddHeartAdDialog = false
                        if (activity != null) {
                            adManager.showRewardedAd(
                                activity = activity,
                                onRewardEarned = { gameEngine.restoreOneHeartWithAd() },
                                onAdClosedOrFailed = { gameEngine.restoreOneHeartWithAd() }
                            )
                        } else {
                            gameEngine.restoreOneHeartWithAd()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkSecondary, contentColor = LightSurface),
                    modifier = Modifier.testTag("watch_heart_ad_button")
                ) {
                    Text("WATCH & GET +1 HEART 🎬", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHeartAdDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Hearts Full Dialog
    if (showHeartsFullDialog) {
        AlertDialog(
            onDismissRequest = { showHeartsFullDialog = false },
            icon = { Text("❤️", fontSize = 36.sp) },
            title = { Text("Hearts Full! (3/3 ❤️)", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text("You already have maximum 3 hearts! Keep playing!", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = { showHeartsFullDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("OK, LET'S PLAY! 💪", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Hint Confirmation Dialog
    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            icon = { Icon(Icons.Default.Lightbulb, contentDescription = null, tint = YellowAccent, modifier = Modifier.size(36.dp)) },
            title = { Text("Need a little help?", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text("Watch a short video to highlight the correct letter!", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = {
                        showHintDialog = false
                        if (activity != null) {
                            adManager.showRewardedAd(
                                activity = activity,
                                onRewardEarned = { gameEngine.applyHint() },
                                onAdClosedOrFailed = { gameEngine.applyHint() }
                            )
                        } else {
                            gameEngine.applyHint()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowAccent, contentColor = TextDark),
                    modifier = Modifier.testTag("watch_hint_button")
                ) {
                    Text("WATCH & GET HINT 🎬", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showHintDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Failure / Game Over Screen
    if (state.isGameOver) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Don't worry! Let's try again. 💪", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text("You lost all hearts, but you can restore them instantly by watching a short video!", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = {
                        if (activity != null) {
                            adManager.showRewardedAd(
                                activity = activity,
                                onRewardEarned = { gameEngine.retryWithRewardedAd() },
                                onAdClosedOrFailed = { gameEngine.retryWithRewardedAd() }
                            )
                        } else {
                            gameEngine.retryWithRewardedAd()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkSecondary),
                    modifier = Modifier.testTag("watch_retry_button")
                ) {
                    Text("WATCH AD & RETRY ❤️", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onBackToLevels) {
                    Text("Level Map")
                }
            }
        )
    }

    // Level Complete Dialog with Word Explanation
    if (state.isLevelCompleted && state.showWordExplanation) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉 GREAT JOB!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                    AnimatedCelebrationStarsRow(
                        earnedStars = state.earnedStars,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WordExplanationSheet(
                        wordLevel = state.currentLevel,
                        onSpeakWord = { gameEngine.replayWordPronunciation() },
                        onDismiss = {
                            if (state.currentLevel.levelNumber < LevelRepository.getTotalLevelCount()) {
                                onNavigateNextLevel()
                            } else {
                                onBackToLevels()
                            }
                        }
                    )
                }
            },
            confirmButton = { }
        )
    }
}

@Composable
private fun WordLetterSlot(
    letter: Char?,
    isMissingSlot: Boolean
) {
    val isFilled = letter != null

    // Pulse animation for missing slot focus
    val infiniteTransition = rememberInfiniteTransition(label = "slotGlow")
    val pulseBorderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isMissingSlot && !isFilled) YellowAccent.copy(alpha = 0.28f) else LightSurface,
        border = androidx.compose.foundation.BorderStroke(
            if (isMissingSlot) 3.5.dp else 2.dp,
            if (isMissingSlot) PurplePrimary.copy(alpha = if (!isFilled) pulseBorderAlpha else 1f) else CardBorder
        ),
        shadowElevation = if (isMissingSlot) 8.dp else 3.dp,
        modifier = Modifier.size(70.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = letter?.toString() ?: "_",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isMissingSlot) PurplePrimary else TextDark
            )
        }
    }
}
