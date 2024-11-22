package com.bccapstone.duitonlen.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bccapstone.duitonlen.presentation.theme.DuitOnlenTheme
import com.bccapstone.duitonlen.data.Result

@Composable
fun Greeting(
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
                Toast.makeText(context, (logoutState as Result.Success).data.message, Toast.LENGTH_SHORT).show()
            }
            is Result.Error -> {
                Toast.makeText(context, (logoutState as Result.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { // 1
        Text(
            text = greeting,
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(16.dp))
        // logout button
        Button(onClick = {
            viewModel.logout()
        }) {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DuitOnlenTheme {
        Greeting()
    }
}