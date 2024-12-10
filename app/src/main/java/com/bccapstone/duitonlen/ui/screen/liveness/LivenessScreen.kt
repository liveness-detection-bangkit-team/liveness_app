package com.bccapstone.duitonlen.ui.screen.liveness

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bccapstone.duitonlen.R


/*
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

 */

@Composable
fun LivenessScreenContainer(
    navController: NavController,
    modelName: String,
    onHumanDetected: () -> Unit,
    instruction: String,
    nextBtn: String,
    headMotion: String
) {
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
            Liveness(
                navController,
                modelName = modelName,
                onHumanDetected = onHumanDetected,
                instruction = instruction,
                nextBtn = nextBtn,
                headMotion = headMotion
            )
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
    nextBtn: String,
    headMotion: String
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
            modelName,
            headMotion
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releaseCamera()
        }
    }

    // Re-initialize the camera when the composable is recomposed
    LaunchedEffect(previewView) {
        viewModel.reinitializeCamera(previewView)
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
                    .background(
                        color = Color.Black,
                        shape = CircleShape
                    )
            ) {


                if (viewModel.cameraManager == null || !viewModel.isModelReady.value) {
                    CircularProgressIndicator(
                        color = Color.Green,
                        strokeWidth = 8.dp,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                } else {
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                    )

                }
                // Circular Progress Indicator outside the border
                if (viewModel.isModelReady.value && viewModel.countdownTime.value > 0) {
                    CircularProgressIndicatorWithBorder(
                        countdownTime = viewModel.countdownTime.value,
                        totalTime = 3000,
                        color = Color.Green,
                        modifier = Modifier
                            .size(300.dp) // Adjust size to be outside the border
                    )
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
                        .offset(y = (-175).dp)
                        .background(
                            color = colorResource(id = R.color.colorSecondary),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)

                )
                Text(
                    text = if (viewModel.isDirectionDetected.value) "Human Detected" else "Unidentified",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .offset(y = 120.dp)
                )
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorWithBorder(
    countdownTime: Int,
    totalTime: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = (totalTime - countdownTime).toFloat() / totalTime

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 8.dp.toPx()
            size.minDimension / 2 - strokeWidth / 2
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(strokeWidth)
            )
        }
    }
}
