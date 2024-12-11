package com.bccapstone.duitonlen.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun HeadOutline(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3/4f)  // This maintains a good head-shape proportion
    ) {
        val ovalPath = Path().apply {
            // Create an oval that's slightly taller than it is wide
            addOval(
                Rect(
                    left = size.width * 0.15f,
                    top = size.height * 0.1f,
                    right = size.width * 0.85f,
                    bottom = size.height * 0.9f
                )
            )
        }

        drawPath(
            path = ovalPath,
            color = Color.White.copy(alpha = 0.6f),
            style = Stroke(
                width = (size.width * 0.01f).coerceAtLeast(8f),  // Thicker stroke for better visibility
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}