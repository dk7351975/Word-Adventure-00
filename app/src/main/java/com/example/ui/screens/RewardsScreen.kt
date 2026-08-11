package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storage.UserProgress
import com.example.ui.components.CoinPill
import com.example.ui.components.StarPill
import com.example.ui.theme.*

data class RewardBadge(
    val id: String,
    val emoji: String,
    val title: String,
    val requiredStars: Int,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    progress: UserProgress,
    onBack: () -> Unit
) {
    val badges = listOf(
        RewardBadge("1", "🐣", "Word Beginner", 3, "Complete your first 3-letter word!"),
        RewardBadge("2", "🦊", "Animal Explorer", 15, "Master 5 animal spellings!"),
        RewardBadge("3", "🚀", "Space Star", 30, "Reach 30 total stars!"),
        RewardBadge("4", "👑", "Spelling Wizard", 60, "Reach 60 total stars!"),
        RewardBadge("5", "🌈", "Rainbow Champion", 100, "Earn 100 stars!"),
        RewardBadge("6", "💎", "Master Scholar", 150, "Earn 150 stars!"),
        RewardBadge("7", "🏆", "300 Level Titan", 300, "Reach 300 total stars!"),
        RewardBadge("8", "🌠", "500 Star Conqueror", 500, "Earn 500 stars!"),
        RewardBadge("9", "🌌", "Galactic Master", 1000, "Earn 1000 stars!"),
        RewardBadge("10", "⚡", "1000 Level Legend", 2500, "Reach 2500 total stars!")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rewards & Badges 🎁", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    StarPill(stars = progress.totalStars)
                    Spacer(modifier = Modifier.width(8.dp))
                    CoinPill(coins = progress.totalCoins)
                    Spacer(modifier = Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        containerColor = LightBackground,
        modifier = Modifier
            .fillMaxSize()
            .testTag("rewards_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Trophy & Badge Collection",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge ->
                    val isUnlocked = progress.totalStars >= badge.requiredStars

                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        if (isUnlocked) PinkSecondary.copy(alpha = 0.2f) else CardBorder,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isUnlocked) badge.emoji else "🔒",
                                    fontSize = 28.sp
                                )
                            }

                            Text(
                                text = badge.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isUnlocked) TextDark else TextMuted
                            )

                            Text(
                                text = if (isUnlocked) badge.description else "Requires ⭐ ${badge.requiredStars}",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
