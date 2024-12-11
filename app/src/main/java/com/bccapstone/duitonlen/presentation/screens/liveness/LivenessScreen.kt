package com.bccapstone.duitonlen.presentation.screens.liveness

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bccapstone.duitonlen.R
import com.bccapstone.duitonlen.ui.composable.AnimatedArrow
import com.bccapstone.duitonlen.ui.composable.CircularProgressIndicatorWithBorder
import com.bccapstone.duitonlen.ui.composable.HeadOutline


@Composable
fun LivenessScreenContainer(
    modelName: String,
    onHumanDetected: () -> Unit,
    onHumanNotDetected: () -> Unit,
    instruction: String,
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
                onHumanDetected = onHumanDetected,
                onHumanNotDetected = onHumanNotDetected,
                modelName = modelName,
                instruction = instruction,
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
    viewModel: LivenessViewModel = viewModel(),
    onHumanDetected: () -> Unit = {},
    onHumanNotDetected: () -> Unit = {},
    modelName: String,
    instruction: String = "Please show your face",
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
            onHumanNotDetected,
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

                // show animated arrow only if headMotion is "left" or "right"
                if (headMotion == "left" || headMotion == "right" || headMotion == "up") {
                    AnimatedArrow(
                        headMotion = headMotion,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp)
                    )
                }

                // show head outline if headMotion is "face"
                if (headMotion == "face") {
                    // Head outline
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(170.dp)
                    ) {
                        HeadOutline(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
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
                    color = Color.White,
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
                    text = AnnotatedString.Builder().apply {
                        append("Put your ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("face")
                        }
                        append(" in the circle, follow the ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("instructions")
                        }
                        append(" and fill the ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )
                        ) {
                            append("green indicator")
                        }
                    }.toAnnotatedString(), fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(y = 130.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Please Remove all COSMETICS and stay in bright room.
                Text(
                    text = AnnotatedString.Builder().apply {
                        append("Please ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.colorSecondary))) {
                            append("remove")
                        }
                        append(" all ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.colorSecondary))) {
                            append("cosmetics")
                        }
                        append(", stay in ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.colorSecondary))) {
                            append("bright room")
                        }
                        append(" and ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colorResource(id = R.color.colorSecondary))) {
                            append("clean background.")
                        }
                    }.toAnnotatedString(),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(y = 150.dp)
                )
            }
        }
    }
}