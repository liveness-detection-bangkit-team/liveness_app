package com.bccapstone.duitonlen.ui.composable

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AnimatedArrow(headMotion: String, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "arrowAnimation")

    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetAnimation"
    )

    // Determine whether to apply offset to X or Y based on direction
    val offsetX = when (headMotion) {
        "left" -> -animatedOffset
        "right" -> animatedOffset
        else -> 0f
    }

    val offsetY = when (headMotion) {
        "up" -> -animatedOffset
        "down" -> animatedOffset
        else -> 0f
    }

    Canvas(
        modifier = modifier
            .size(80.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
    ) {
        val arrowWidth = size.width
        val arrowHeight = size.height
        val strokeWidth = 8.dp.toPx()

        val path = Path().apply {
            if (headMotion == "left") {
                // Left-pointing arrow
                moveTo(arrowWidth * 0.4f, arrowHeight * 0.3f)
                lineTo(arrowWidth * 0.2f, arrowHeight * 0.5f)
                lineTo(arrowWidth * 0.4f, arrowHeight * 0.7f)
                moveTo(arrowWidth * 0.2f, arrowHeight * 0.5f)
                lineTo(arrowWidth * 0.8f, arrowHeight * 0.5f)
            } else if (headMotion == "right") {
                // Right-pointing arrow
                moveTo(arrowWidth * 0.6f, arrowHeight * 0.3f)
                lineTo(arrowWidth * 0.8f, arrowHeight * 0.5f)
                lineTo(arrowWidth * 0.6f, arrowHeight * 0.7f)
                moveTo(arrowWidth * 0.8f, arrowHeight * 0.5f)
                lineTo(arrowWidth * 0.2f, arrowHeight * 0.5f)
            } else if (headMotion == "up") {
                // Up-pointing arrow
                moveTo(arrowWidth * 0.3f, arrowHeight * 0.4f)
                lineTo(arrowWidth * 0.5f, arrowHeight * 0.2f)
                lineTo(arrowWidth * 0.7f, arrowHeight * 0.4f)
                moveTo(arrowWidth * 0.5f, arrowHeight * 0.2f)
                lineTo(arrowWidth * 0.5f, arrowHeight * 0.8f)
            } else if (headMotion == "down") {
                // Down-pointing arrow
                moveTo(arrowWidth * 0.3f, arrowHeight * 0.6f)
                lineTo(arrowWidth * 0.5f, arrowHeight * 0.8f)
                lineTo(arrowWidth * 0.7f, arrowHeight * 0.6f)
                moveTo(arrowWidth * 0.5f, arrowHeight * 0.8f)
                lineTo(arrowWidth * 0.5f, arrowHeight * 0.2f)
            }
        }

        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.6f),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
