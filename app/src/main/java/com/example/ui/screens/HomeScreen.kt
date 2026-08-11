package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.LevelRepository
import com.example.storage.UserProgress
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    progress: UserProgress,
    onPlayCurrentLevel: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onNavigateLevels: () -> Unit,
    onNavigateProgress: () -> Unit,
    onNavigateRewards: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val currentLevelObj = LevelRepository.getLevel(progress.unlockedLevel)

    Scaffold(
        containerColor = LightBackground,
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WORD ADVENTURE",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurplePrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Learn • Spell • Play",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StreakPill(streakDays = progress.dailyStreak, hasCompletedToday = progress.hasCompletedToday)
                    StarPill(stars = progress.totalStars)
                    CoinPill(coins = progress.totalCoins)
                }
            }

            // XP Progress Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = YellowAccent,
                            modifier = Modifier.clip(CircleShape)
                        ) {
                            Text(
                                text = "⭐ LEVEL ${progress.unlockedLevel}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = TextDark,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = "${progress.unlockedLevel} / 1000 Levels",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PurplePrimary
                        )
                    }

                    // XP Bar
                    val currentXpLevel = (progress.totalXP / 100) + 1
                    val xpInCurrentLevel = progress.totalXP % 100
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "XP Progress",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "$xpInCurrentLevel / 100 XP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurplePrimary
                            )
                        }
                        LinearProgressIndicator(
                            progress = { xpInCurrentLevel / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = PurplePrimary,
                            trackColor = PurplePrimary.copy(alpha = 0.15f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥 ${progress.dailyStreak} Day Streak",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD84315)
                        )
                        Text(
                            text = "Total XP: ${progress.totalXP}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                }
            }

            // Hero Play Card: Continue Adventure
            Card(
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(PurplePrimary, DeepPurple, SkyBlue)
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = YellowAccent,
                            modifier = Modifier.clip(CircleShape)
                        ) {
                            Text(
                                text = "LEVEL ${progress.unlockedLevel} • ${currentLevelObj.word}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = TextDark,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = if (progress.unlockedLevel > 1) "READY FOR YOUR NEXT SPELLING ADVENTURE?" else "START YOUR SPELLING ADVENTURE",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightSurface,
                            lineHeight = 26.sp
                        )

                        CustomPrimaryButton(
                            text = if (progress.unlockedLevel > 1) "▶ CONTINUE ADVENTURE" else "▶ START ADVENTURE",
                            onClick = onPlayCurrentLevel,
                            gradientColors = listOf(YellowAccent, OrangeCoin),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("play_button")
                        )
                    }
                }
            }

            // 1000 Level Adventure Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗺️ 1000 LEVEL ADVENTURE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Text(
                    text = "${progress.unlockedLevel} / 1000 Unlocked",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )
            }

            // Level Map Row (Preview of nearby levels)
            VisualLevelMapRow(
                progress = progress,
                onSelectLevel = onSelectLevel
            )

            // Your Journey Progress by World/Difficulty
            JourneyProgressCard(progress = progress)

            // Explore & Learn Menu
            Text(
                text = "Explore & Learn",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeMenuCard(
                    title = "LEVELS",
                    subtitle = "${progress.unlockedLevel}/1000 Unlocked",
                    icon = Icons.Default.Book,
                    accentColor = SkyBlue,
                    onClick = onNavigateLevels,
                    modifier = Modifier.weight(1f).testTag("levels_menu_button")
                )

                HomeMenuCard(
                    title = "PROGRESS",
                    subtitle = "${progress.completedWords.size} Words",
                    icon = Icons.Default.EmojiEvents,
                    accentColor = MintSuccess,
                    onClick = onNavigateProgress,
                    modifier = Modifier.weight(1f).testTag("progress_menu_button")
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeMenuCard(
                    title = "REWARDS",
                    subtitle = "Badges & Fun",
                    icon = Icons.Default.CardGiftcard,
                    accentColor = PinkSecondary,
                    onClick = onNavigateRewards,
                    modifier = Modifier.weight(1f).testTag("rewards_menu_button")
                )

                HomeMenuCard(
                    title = "SETTINGS",
                    subtitle = "Audio & Options",
                    icon = Icons.Default.Settings,
                    accentColor = YellowAccent,
                    onClick = onNavigateSettings,
                    modifier = Modifier.weight(1f).testTag("settings_menu_button")
                )
            }
        }
    }
}

@Composable
private fun JourneyProgressCard(
    progress: UserProgress,
    modifier: Modifier = Modifier
) {
    val unlocked = progress.unlockedLevel

    // Dynamic calculations for each world difficulty tier
    val beginnerCount = (unlocked - 1).coerceIn(0, 100)
    val easyCount = (unlocked - 101).coerceIn(0, 200)
    val mediumCount = (unlocked - 301).coerceIn(0, 200)
    val hardCount = (unlocked - 501).coerceIn(0, 200)
    val advancedCount = (unlocked - 701).coerceIn(0, 200)
    val masterCount = (unlocked - 901).coerceIn(0, 100)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🏆 YOUR JOURNEY",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            DifficultyProgressItem("🌱 Beginner (World 1)", beginnerCount, 100, MintSuccess)
            DifficultyProgressItem("🌿 Easy (Worlds 2-3)", easyCount, 200, SkyBlue)
            DifficultyProgressItem("🌳 Medium (Worlds 4-5)", mediumCount, 200, PurplePrimary)
            DifficultyProgressItem("🔥 Hard (Worlds 6-7)", hardCount, 200, OrangeCoin)
            DifficultyProgressItem("💎 Advanced (Worlds 8-9)", advancedCount, 200, PinkSecondary)
            DifficultyProgressItem("👑 Master (World 10)", masterCount, 100, GoldStar)
        }
    }
}

@Composable
private fun DifficultyProgressItem(
    title: String,
    completed: Int,
    total: Int,
    barColor: Color
) {
    val fraction = completed.toFloat() / total.toFloat()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "$completed / $total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )
        }
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = barColor.copy(alpha = 0.15f)
        )
    }
}

@Composable
private fun HomeMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(accentColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }
        }
    }
}

