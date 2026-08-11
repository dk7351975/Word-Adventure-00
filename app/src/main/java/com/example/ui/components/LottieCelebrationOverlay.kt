package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.R
import com.example.ui.theme.*

@Composable
fun LottieCelebrationOverlay(
    isTriggered: Boolean,
    earnedStars: Int = 3,
    earnedXP: Int = 50,
    earnedCoins: Int = 20,
    levelNumber: Int = 1,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isTriggered,
        enter = fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.8f),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f),
        modifier = modifier
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.celebration_level_complete)
        )
        val lottieProgress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = isTriggered,
            speed = 1.0f
        )

        // Pulsing bounce animation for trophy
        val infiniteTransition = rememberInfiniteTransition(label = "trophyBounce")
        val trophyScale by infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(650, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "trophyScale"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .padding(20.dp)
                .testTag("lottie_celebration_overlay"),
            contentAlignment = Alignment.Center
        ) {
            // Lottie Animation Canvas
            LottieAnimation(
                composition = composition,
                progress = { lottieProgress },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .testTag("lottie_celebration_animation")
            )

            // Celebratory Dialog Card
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = LightSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                border = androidx.compose.foundation.BorderStroke(3.dp, GoldStar),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .shadow(24.dp, RoundedCornerShape(32.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Banner
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = YellowAccent,
                        modifier = Modifier.shadow(6.dp, RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            text = "🎉 LEVEL $levelNumber COMPLETED! 🎉",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }

                    // Trophy / Winner Mascot Box with Lottie pulse effect
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(trophyScale)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(YellowAccent, OrangeCoin, GoldStar)
                                )
                            )
                            .border(3.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏆", fontSize = 60.sp)
                    }

                    // Stars Earned Row
                    AnimatedCelebrationStarsRow(
                        earnedStars = earnedStars,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "SUPER JOB, CHAMP! 🌟",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurplePrimary,
                        textAlign = TextAlign.Center
                    )

                    // Rewards Stats Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightBackground, RoundedCornerShape(20.dp))
                            .border(1.5.dp, CardBorder, RoundedCornerShape(20.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🪙 +$earnedCoins", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = OrangeCoin)
                            Text("Coins", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        }

                        VerticalDivider(modifier = Modifier.height(30.dp), color = CardBorder, thickness = 1.dp)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚡ +$earnedXP", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                            Text("XP Earned", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        }
                    }

                    // Large Vibrant Continue Button
                    CustomPrimaryButton(
                        text = "NEXT LEVEL 🚀",
                        onClick = onContinueClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("celebration_continue_button"),
                        gradientColors = listOf(Color(0xFF00C853), Color(0xFF00E676))
                    )
                }
            }
        }
    }
}
