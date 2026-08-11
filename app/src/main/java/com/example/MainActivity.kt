package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ads.AdManager
import com.example.data.repository.LevelRepository
import com.example.game.GameEngine
import com.example.ui.screens.*
import com.example.ui.theme.WordAdventureTheme

class MainActivity : ComponentActivity() {

    private val adManager = AdManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize AdMob SDK
        adManager.initialize(this)

        setContent {
            WordAdventureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordAdventureApp(adManager = adManager)
                }
            }
        }
    }
}

@Composable
fun WordAdventureApp(
    adManager: AdManager,
    gameEngine: GameEngine = viewModel()
) {
    val navController = rememberNavController()
    val userProgress by gameEngine.userProgress.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                progress = userProgress,
                onPlayCurrentLevel = {
                    val levelToPlay = userProgress.unlockedLevel.coerceIn(1, LevelRepository.getTotalLevelCount())
                    gameEngine.startLevel(levelToPlay)
                    navController.navigate("game/$levelToPlay")
                },
                onSelectLevel = { levelNum ->
                    gameEngine.startLevel(levelNum)
                    navController.navigate("game/$levelNum")
                },
                onNavigateLevels = { navController.navigate("levels") },
                onNavigateProgress = { navController.navigate("progress") },
                onNavigateRewards = { navController.navigate("rewards") },
                onNavigateSettings = { navController.navigate("settings") }
            )
        }

        composable("levels") {
            LevelMapScreen(
                progress = userProgress,
                onSelectLevel = { levelNum ->
                    gameEngine.startLevel(levelNum)
                    navController.navigate("game/$levelNum")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "game/{levelNumber}",
            arguments = listOf(navArgument("levelNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelNumber = backStackEntry.arguments?.getInt("levelNumber") ?: userProgress.unlockedLevel
            GameScreen(
                gameEngine = gameEngine,
                adManager = adManager,
                onNavigateNextLevel = {
                    val nextLevel = (levelNumber + 1).coerceAtMost(LevelRepository.getTotalLevelCount())
                    gameEngine.startLevel(nextLevel)
                    navController.navigate("game/$nextLevel") {
                        popUpTo("game/$levelNumber") { inclusive = true }
                    }
                },
                onBackToLevels = {
                    navController.popBackStack("levels", inclusive = false)
                        .takeIf { it } ?: navController.navigate("home")
                }
            )
        }

        composable("progress") {
            ProgressScreen(
                progress = userProgress,
                onBack = { navController.popBackStack() }
            )
        }

        composable("rewards") {
            RewardsScreen(
                progress = userProgress,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                progress = userProgress,
                gameEngine = gameEngine,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
