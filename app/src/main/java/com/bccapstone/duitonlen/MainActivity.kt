package com.bccapstone.duitonlen

import LoginScreen
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
import com.bccapstone.duitonlen.presentation.theme.DuitOnlenTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.bccapstone.duitonlen.presentation.screens.auth.register.RegisterScreen
import com.bccapstone.duitonlen.presentation.screens.home.Greeting

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuitOnlenTheme {

                MainNavigation()

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("home") {
                        Greeting(
                            onLogoutSuccess = {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
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
