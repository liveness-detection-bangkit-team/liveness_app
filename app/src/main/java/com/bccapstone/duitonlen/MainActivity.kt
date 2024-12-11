package com.bccapstone.duitonlen

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bccapstone.duitonlen.presentation.screens.auth.register.RegisterScreen
import com.bccapstone.duitonlen.presentation.theme.DuitOnlenTheme
import com.bccapstone.duitonlen.presentation.screens.InstructionScreenContainer
import com.bccapstone.duitonlen.presentation.screens.result.SuccessScreen
import com.bccapstone.duitonlen.presentation.screens.home.HomeScreen
import com.bccapstone.duitonlen.presentation.screens.liveness.LivenessScreenContainer
import com.bccapstone.duitonlen.presentation.screens.result.FailedScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuitOnlenTheme {
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
                        HomeScreen(
                            navController = navController,
                            onLogoutSuccess = {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("instruction") {
                        InstructionScreenContainer(
                            navController = navController,
                            navigateTo = "up_liveness"
                        )
                    }

                    composable("up_liveness") {
                        LivenessScreenContainer(
                            modelName = "all-pos",
                            onHumanDetected = {
                                navController.navigate("spoof_detection/left_liveness") {
                                    popUpTo("instruction") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onHumanNotDetected = {
                                navController.navigate("result_failed") {
                                    popUpTo("instruction") { inclusive = false }
                                }
                            },
                            instruction = "Look Up",
                            headMotion = "up"
                        )
                    }

                    // Liveness check
                    composable("left_liveness") {
                        LivenessScreenContainer(
                            modelName = "all-pos",
                            onHumanDetected = {
                                navController.navigate("spoof_detection/right_liveness") {
                                    popUpTo("instruction") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onHumanNotDetected = {
                                navController.navigate("result_failed") {
                                    popUpTo("instruction") { inclusive = false }
                                }
                            },
                            instruction = "Turn Left",
                            headMotion = "left"
                        )
                    }


                    composable("right_liveness") {
                        LivenessScreenContainer(
                            modelName = "all-pos",
                            onHumanDetected = {
                                navController.navigate("spoof_detection/result_success") {
                                    popUpTo("instruction") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onHumanNotDetected = {
                                navController.navigate("result_failed") {
                                    popUpTo("instruction") { inclusive = false }
                                }
                            },
                            instruction = "Turn Right",
                            headMotion = "right"
                        )
                    }



                    composable("spoof_detection/{nextDestination}") { backStackEntry ->
                        val nextDestination =
                            backStackEntry.arguments?.getString("nextDestination")
                                ?: "result_failed"
                        LivenessScreenContainer(
                            modelName = "spoof-detect",
                            onHumanDetected = {
                                navController.navigate(nextDestination) {
                                    popUpTo("instruction") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onHumanNotDetected = {
                                navController.navigate("result_failed") {
                                    popUpTo("instruction") { inclusive = false }
                                }
                            },
                            instruction = "Face the camera",
                            headMotion = "face"
                        )
                    }

                    composable("result_success") {
                        SuccessScreen(navController = navController)
                    }

                    composable("result_failed") {
                        FailedScreen(navController = navController)
                    }
                }
            }
        }
    }
}