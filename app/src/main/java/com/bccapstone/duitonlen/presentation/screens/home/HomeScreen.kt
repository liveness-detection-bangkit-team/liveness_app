package com.bccapstone.duitonlen.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bccapstone.duitonlen.data.Result
import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.presentation.theme.DuitOnlenTheme
import com.bccapstone.duitonlen.ui.composable.BalanceCard
import com.bccapstone.duitonlen.ui.composable.BottomNavigation
import com.bccapstone.duitonlen.ui.composable.FinancialRecords
import com.bccapstone.duitonlen.ui.composable.MenuButtons
import com.bccapstone.duitonlen.ui.composable.PaymentSection
import com.bccapstone.duitonlen.ui.composable.TopBar

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit = {}
) {

    var greeting by remember { mutableStateOf("") }

    val greetingState by viewModel.homeState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState()

    val context = LocalContext.current

    // hit the getHome API every time the screen is launched
    LaunchedEffect(Unit) {
        viewModel.getHome()
    }

    LaunchedEffect(greetingState) {
        when (greetingState) {
            is Result.Success -> {
                greeting = (greetingState as Result.Success).data.message
            }

            is Result.Error -> {
                Toast.makeText(context, (greetingState as Result.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }

    LaunchedEffect(logoutState) {
        when (logoutState) {
            is Result.Success -> {
                onLogoutSuccess()
                Toast.makeText(
                    context,
                    (logoutState as Result.Success).data.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Result.Error -> {
                Toast.makeText(context, (logoutState as Result.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }


    EWalletScreen(greeting, viewModel, logoutState, navController)
}


@Composable
fun EWalletScreen(greeting: String, viewModel: HomeViewModel, logoutState: Result<ApiResponse>, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // TopBar
                        Box(
                            modifier = Modifier

                        ) {
                            TopBar(greeting)
                        }

                        // BalanceCard yang overlap dengan TopBar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 85.dp)
                        ) {
                            BalanceCard()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset(y = (-30).dp)  // Nilai negatif untuk efek overlap ke atas
                    ) {
                        Column {
                            MenuButtons()
                            PaymentSection()
                            FinancialRecords()
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // logout button
                    Button(
                        onClick = {
                            viewModel.logout()
                        }, modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (logoutState is Result.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Logout",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // Bottom Navigation dengan fixed posisition di bawah
            BottomNavigation(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DuitOnlenTheme {
        HomeScreen(
            navController = NavController(LocalContext.current)
        )
    }
}
