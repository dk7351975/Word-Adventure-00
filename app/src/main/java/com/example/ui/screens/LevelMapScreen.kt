package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WordLevel
import com.example.data.repository.LevelRepository
import com.example.storage.UserProgress
import com.example.ui.components.CoinPill
import com.example.ui.components.StarPill
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelMapScreen(
    progress: UserProgress,
    onSelectLevel: (Int) -> Unit,
    onBack: () -> Unit
) {
    val levels = remember { LevelRepository.getAllLevels() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var selectedRangeIndex by remember { mutableIntStateOf(((progress.unlockedLevel - 1) / 100).coerceIn(0, 9)) }
    var jumpInputText by remember { mutableStateOf("") }
    var showJumpDialog by remember { mutableStateOf(false) }

    val rangeList = listOf(
        "1-100", "101-200", "201-300", "301-400", "401-500",
        "501-600", "601-700", "701-800", "801-900", "901-1000"
    )

    LaunchedEffect(progress.unlockedLevel) {
        val targetIndex = (progress.unlockedLevel - 1).coerceIn(0, levels.size - 1)
        listState.animateScrollToItem(targetIndex)
        selectedRangeIndex = ((progress.unlockedLevel - 1) / 100).coerceIn(0, 9)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "1000 Levels Map 🗺️",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = PurplePrimary
                        )
                        Text(
                            text = "Level ${progress.unlockedLevel} / 1000 Unlocked",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PurplePrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showJumpDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Jump to level",
                            tint = PurplePrimary
                        )
                    }
                    StarPill(stars = progress.totalStars)
                    Spacer(modifier = Modifier.width(6.dp))
                    CoinPill(coins = progress.totalCoins)
                    Spacer(modifier = Modifier.width(10.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        containerColor = LightBackground,
        modifier = Modifier
            .fillMaxSize()
            .testTag("level_map_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Level Range Selector Tabs (1-100, 101-200 ... 901-1000)
            Surface(
                color = LightSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "SELECT LEVEL RANGE (1 TO 1000):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurplePrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(rangeList) { idx, rangeStr ->
                            val isSelected = idx == selectedRangeIndex
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedRangeIndex = idx
                                    val targetLevelIndex = idx * 100
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(targetLevelIndex)
                                    }
                                },
                                label = {
                                    Text(
                                        text = rangeStr,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurplePrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = LightBackground,
                                    labelColor = TextDark
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(levels) { index, level ->
                    val isUnlocked = level.levelNumber <= progress.unlockedLevel
                    val isCurrent = level.levelNumber == progress.unlockedLevel
                    val starsEarned = progress.starsPerLevel[level.levelNumber] ?: if (level.levelNumber < progress.unlockedLevel) 3 else 0

                    // Tier Header every 100 levels
                    if (index % 100 == 0) {
                        val tierNum = (index / 100) + 1
                        val startLvl = index + 1
                        val endLvl = (index + 100).coerceAtMost(1000)
                        
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepPurple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "🏆 TIER $tierNum",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = YellowAccent
                                    )
                                    Text(
                                        text = "Levels $startLvl to $endLvl",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = if (progress.unlockedLevel >= startLvl) "UNLOCKING" else "LOCKED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (progress.unlockedLevel >= startLvl) MintSuccess else Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Alternate horizontal alignment to form a winding level path!
                    val alignment = when (index % 4) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        2 -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = alignment
                    ) {
                        LevelNodeCard(
                            level = level,
                            isUnlocked = isUnlocked,
                            isCurrent = isCurrent,
                            starsEarned = starsEarned,
                            onClick = {
                                if (isUnlocked) {
                                    onSelectLevel(level.levelNumber)
                                } else {
                                    // Also allow testing/playing any level directly
                                    onSelectLevel(level.levelNumber)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Jump to level dialog
    if (showJumpDialog) {
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = {
                Text("Jump to Level (1 - 1000) 🎯", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter any level number from 1 to 1000:", fontSize = 13.sp, color = TextMuted)
                    OutlinedTextField(
                        value = jumpInputText,
                        onValueChange = { jumpInputText = it.filter { char -> char.isDigit() } },
                        placeholder = { Text("e.g. 250, 500, 1000") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(onGo = {
                            val targetNum = jumpInputText.toIntOrNull()
                            if (targetNum != null && targetNum in 1..1000) {
                                onSelectLevel(targetNum)
                                showJumpDialog = false
                            }
                        }),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetNum = jumpInputText.toIntOrNull()
                        if (targetNum != null && targetNum in 1..1000) {
                            onSelectLevel(targetNum)
                            showJumpDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Go to Level")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LevelNodeCard(
    level: WordLevel,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    starsEarned: Int,
    onClick: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "pulseTransition")
    val currentPulseScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val scale = if (isCurrent) currentPulseScale else 1f
    val bgColor = when {
        isCurrent -> PurplePrimary
        isUnlocked -> SkyBlue
        else -> TextMuted.copy(alpha = 0.25f)
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) LightSurface else if (isUnlocked) LightSurface else LightBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 10.dp else if (isUnlocked) 4.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrent) 3.dp else 1.5.dp,
            color = if (isCurrent) YellowAccent else if (isUnlocked) SkyBlue else CardBorder
        ),
        modifier = Modifier
            .width(235.dp)
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("level_node_${level.levelNumber}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Distinct Current Level Location Badge Header
            if (isCurrent) {
                Surface(
                    shape = CircleShape,
                    color = YellowAccent,
                    modifier = Modifier.shadow(4.dp, CircleShape)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("📍", fontSize = 12.sp)
                        Text(
                            text = "CURRENT LEVEL",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            color = TextDark
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Level Circle badge or Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(bgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isUnlocked) {
                        Text(
                            text = level.categoryEmoji,
                            fontSize = 26.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = LightSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "LEVEL ${level.levelNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurplePrimary
                    )
                    Text(
                        text = if (isUnlocked) level.word else "Locked",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) TextDark else TextMuted
                    )

                    // Stars for completed level
                    if (isUnlocked && starsEarned > 0) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            for (s in 1..3) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (s <= starsEarned) GoldStar else CardBorder,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
