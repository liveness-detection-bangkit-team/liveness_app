package com.bccapstone.duitonlen.ui.screen

import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bccapstone.duitonlen.R
//import com.bccapstone.duitonlen.presentation.theme.CobaComposeTheme

class ResultScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultScreenContainer()
                }
            }
        }
    }
}

@Composable
fun ResultScreenContainer() {
        Result()
}

@Composable
fun Result(){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Red,

                )
    ){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp, bottom = 16.dp)
            .padding(20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        Image(
            painter = painterResource(id = R.drawable.succes),
            contentDescription = "Banner" ,
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
            text = "98,99% Human",
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
                ),
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

@Preview(showBackground = true)
@Composable
fun ResultPreview() {
    DuitOnlenTheme {
        Result()
    }
}
