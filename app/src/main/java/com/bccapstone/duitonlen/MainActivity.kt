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
        composable("left_liveness") {
            LivenessScreenContainer(navController = navController, modelName = "all-pos", onHumanDetected = {
                navController.navigate("right_liveness") {
                    popUpTo("instruction") { inclusive = true }
                }
            }, instruction = "Turn Left", nextBtn = "right_liveness", headMotion = "left")
        }
        composable("right_liveness") {
            LivenessScreenContainer(navController = navController, modelName = "all-pos", onHumanDetected = {
                navController.navigate("result") {
                    popUpTo("instruction") { inclusive = true }
                }
            }, instruction = "Turn Right", nextBtn = "result", headMotion = "right")
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
