package com.bccapstone.duitonlen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bccapstone.duitonlen.ui.screen.InstructionScreenContainer
import com.bccapstone.duitonlen.ui.screen.liveness.LivenessScreenContainer
import com.bccapstone.duitonlen.ui.screen.ResultScreenContainer
import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuitOnlenTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "instruction") {
        composable("instruction") {
            InstructionScreenContainer(navController = navController)
        }
        composable("front_liveness") {
            LivenessScreenContainer(navController = navController, modelName = "front-facing", onHumanDetected = {
                navController.navigate("blink_liveness") {
                    popUpTo("instruction") { inclusive = true }
                }
            }, instruction = "Front Face", nextBtn = "blink_liveness")
        }
        composable("blink_liveness") {
            LivenessScreenContainer(navController = navController, modelName = "blink-detect", onHumanDetected = {
                navController.navigate("left_liveness") {
                    popUpTo("instruction") { inclusive = true }
                }
            }, instruction = "Blink", nextBtn = "left_liveness")
        }
        composable("left_liveness") {
            LivenessScreenContainer(navController = navController, modelName = "left-detect", onHumanDetected = {
                navController.navigate("result") {
                    popUpTo("instruction") { inclusive = true }
                }
            }, instruction = "Turn Left", nextBtn = "result")
        }
        composable(
            "result",
        ) {
            ResultScreenContainer(
                navController = navController
            )
        }
    }
}
