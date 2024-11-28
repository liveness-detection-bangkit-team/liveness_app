package com.bccapstone.duitonlen.ui.screen.liveness

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bccapstone.duitonlen.ui.theme.DuitOnlenTheme


class LivenessScreen() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DuitOnlenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    LivenessScreenContainer()
                }
            }
        }
    }
}

@Composable
fun LivenessScreenContainer(navController: NavController, modelName: String, onHumanDetected: () -> Unit, instruction: String, nextBtn: String) {
    var hasCameraPermission by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        if (hasCameraPermission) {
            Liveness(navController, modelName = modelName, onHumanDetected = onHumanDetected, instruction = instruction, nextBtn = nextBtn)
        } else {
            Text(
                text = "Aplikasi membutuhkan izin kamera",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Liveness(
    navController: NavController,
    viewModel: LivenessViewModel = viewModel(),
    onHumanDetected: () -> Unit = {},
    modelName: String,
    instruction: String = "Please show your face",
    nextBtn: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        viewModel.initializeDetection(
            context,
            lifecycleOwner,
            previewView,
            onHumanDetected,
            modelName
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Red background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
        )

        // White container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 260.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            // Camera Preview
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-150).dp)
            ) {




                if (!viewModel.isModelReady.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(350.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                } else {
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier
                            .size(350.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )

                    // Show detection status
                    if (viewModel.isHumanDetected.value) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .border(1.dp, Color.Green, CircleShape)
                        )
                    }
                }
            }

            // Error message if any
            viewModel.errorMessage.value?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = instruction,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .offset(y = 220.dp)
                        .background(
                            color = Color.Yellow,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)

                )
                Text(
                    text = if (viewModel.isHumanDetected.value) "Human Detected" else "Unidentified",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .offset(y = 230.dp)
                )
                Text(
                    text = viewModel.countdownTime.value.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .offset(y = 230.dp)
                )
                Text(
                    text = viewModel.onHumanDetected.value.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .offset(y = 230.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 240.dp)
                        .padding(16.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate(nextBtn) {
                                popUpTo("instruction") {
                                    inclusive = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Next",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }


            // next button go to result screen

        }
    }
}

