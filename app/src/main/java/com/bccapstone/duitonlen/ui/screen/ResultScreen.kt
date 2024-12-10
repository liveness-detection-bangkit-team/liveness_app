package com.bccapstone.duitonlen.ui.screen

import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bccapstone.duitonlen.R

class ResultScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ResultScreenContainer()
                }
            }
        }
    }
}

/*
@Composable
fun ResultScreenContainer(navController: NavController, verificationResult: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Red,

                )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 65.dp, bottom = 16.dp)
                .padding(20.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.succes),
                contentDescription = "Banner",
                modifier = Modifier
                    .padding(top = 50.dp)
                    .align(alignment = Alignment.TopCenter)
            )
            Text(
                text = "Congrats!",
                color = Color.Red,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 35.dp)
            )
            Text(
                text = "Your Are",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 135.dp)
            )
            Text(
                text = verificationResult ?: "Unknown",
                color = Color.Green,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 235.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 520.dp)
                    .padding(16.dp)
                    .background(
                        color = Color.Red,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        navController.navigate("instruction")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Go to Home",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}
*/

@Composable
fun ResultScreenContainer(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Liveness Check Success!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Human presence confirmed",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("instruction") {
                        popUpTo("instruction") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )
            ) {
                Text(
                    "Try Again",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ResultPreview() {
    DuitOnlenTheme {
//        Result()
    }
}
