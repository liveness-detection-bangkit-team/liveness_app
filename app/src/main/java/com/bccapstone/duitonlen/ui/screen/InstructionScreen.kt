package com.bccapstone.duitonlen.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme
import com.bccapstone.duitonlen.R

class InstructionScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InstructionScreenContainer()
                }
            }
        }
    }
}

@Composable
fun InstructionScreenContainer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White,
            )
    ){
        Instruction()
    }
}
@Composable
fun Instruction() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(
                color = Color.Red,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 170.dp)
//            .padding(top = 170.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ){
        Image(
            painter = painterResource(id = R.drawable.img_instruction),
            contentDescription = "Banner" ,
            modifier = Modifier
                .padding(top = 50.dp)
                .align(alignment = Alignment.TopCenter)

        )
        Column(
            modifier = Modifier
                .padding(top = 270.dp)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "You Will be given a set of instruction, to scan your biometrics, so please follow the instruction carefully.",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "as soon as you are ready click the button below!",
                fontSize = 18.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .padding(16.dp)
                    .background(
                        color = Color.Red,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "I'm Ready!",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewInstruction() {
    DuitOnlenTheme {
        InstructionScreenContainer()
    }
}

