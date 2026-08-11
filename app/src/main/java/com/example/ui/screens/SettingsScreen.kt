package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.GameEngine
import com.example.storage.UserProgress
import com.example.ui.theme.*

// Default policy & legal URLs
const val PRIVACY_POLICY_URL = "https://dknews00.blogspot.com/p/privacy-policy.html"
const val TERMS_CONDITIONS_URL = "https://dknews00.blogspot.com/p/terms-conditions.html"
const val DISCLAIMER_URL = "https://dknews00.blogspot.com/p/disclaimer_01573111366.html?m=1&zx=ccde42dcdc70a367"
const val CONTACT_US_URL = "mailto:support@example.com"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    progress: UserProgress,
    gameEngine: GameEngine,
    onBack: () -> Unit
) {
    var soundEnabled by remember(progress) { mutableStateOf(progress.soundEnabled) }
    var musicEnabled by remember(progress) { mutableStateOf(progress.musicEnabled) }
    var voiceEnabled by remember(progress) { mutableStateOf(progress.voiceEnabled) }
    var reduceMotion by remember(progress) { mutableStateOf(progress.reduceMotion) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    var showResetDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings ⚙️", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        containerColor = LightBackground,
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Audio & Notifications
            Text(text = "Sound Settings & Notifications 🔊🔔", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PurplePrimary)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingToggleRow(
                        title = "Sound Effects 🔊",
                        subtitle = "Play game audio effects",
                        checked = soundEnabled,
                        onCheckedChange = {
                            soundEnabled = it
                            gameEngine.updateSettings(it, musicEnabled, voiceEnabled, reduceMotion)
                        }
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingToggleRow(
                        title = "Background Music 🎵",
                        subtitle = "Play ambient music melodies",
                        checked = musicEnabled,
                        onCheckedChange = {
                            musicEnabled = it
                            gameEngine.updateSettings(soundEnabled, it, voiceEnabled, reduceMotion)
                        }
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingToggleRow(
                        title = "Voice Pronunciation 🗣️",
                        subtitle = "Speak letters and complete words",
                        checked = voiceEnabled,
                        onCheckedChange = {
                            voiceEnabled = it
                            gameEngine.updateSettings(soundEnabled, musicEnabled, it, reduceMotion)
                        }
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingToggleRow(
                        title = "Notifications 🔔",
                        subtitle = "Daily play reminders and streak alerts",
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                        }
                    )
                }
            }

            // Legal & Information
            Text(text = "Legal & Support 🔒📄", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PurplePrimary)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingLinkRow(
                        title = "🔒 Privacy Policy",
                        subtitle = "Read our privacy policy & data protection",
                        onClick = {
                            try {
                                uriHandler.openUri(PRIVACY_POLICY_URL)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        testTag = "privacy_policy_button"
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingLinkRow(
                        title = "📄 Terms & Conditions",
                        subtitle = "Read terms of service",
                        onClick = {
                            try {
                                uriHandler.openUri(TERMS_CONDITIONS_URL)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        testTag = "terms_conditions_button"
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingLinkRow(
                        title = "⚠️ Disclaimer",
                        subtitle = "Educational content disclaimer",
                        onClick = {
                            try {
                                uriHandler.openUri(DISCLAIMER_URL)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        testTag = "disclaimer_button"
                    )
                    HorizontalDivider(color = CardBorder)
                    SettingLinkRow(
                        title = "📧 Contact Us",
                        subtitle = "Get support or send feedback",
                        onClick = {
                            try {
                                uriHandler.openUri(CONTACT_US_URL)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        testTag = "contact_us_button"
                    )
                }
            }

            // Data & Reset
            Text(text = "Data & Reset ⚠️", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DangerRed)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Reset Progress", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                        Text(text = "Clear stars, coins and level progress", fontSize = 12.sp, color = TextMuted)
                    }

                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                        modifier = Modifier.testTag("reset_progress_button")
                    ) {
                        Text("Reset")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Word Adventure v1.0.0", fontSize = 13.sp, color = TextMuted)
                Text(text = "Made with ❤️ for young learners", fontSize = 12.sp, color = TextMuted)
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to reset all stars, coins, and level progress? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        gameEngine.resetProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Yes, Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
            Text(text = subtitle, fontSize = 12.sp, color = TextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = LightSurface, checkedTrackColor = PurplePrimary)
        )
    }
}

@Composable
private fun SettingLinkRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
            Text(text = subtitle, fontSize = 12.sp, color = TextMuted)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = PurplePrimary
        )
    }
}

