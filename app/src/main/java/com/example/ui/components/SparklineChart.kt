package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ApexCrimson
import com.example.ui.theme.ApexEmerald

@Composable
fun SparklineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true,
    strokeWidth: Float = 4f,
    showGradientFill: Boolean = true
) {
    if (data.size < 2) return

    val lineColor = if (isPositive) ApexEmerald else ApexCrimson
    val fillColor = if (isPositive) {
        listOf(ApexEmerald.copy(alpha = 0.25f), Color.Transparent)
    } else {
        listOf(ApexCrimson.copy(alpha = 0.25f), Color.Transparent)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val minVal = data.minOrNull() ?: 0f
        val maxVal = data.maxOrNull() ?: 1f
        val range = (maxVal - minVal).coerceAtLeast(0.001f)

        val stepX = width / (data.size - 1)

        val path = Path()
        val fillPath = Path()

        data.forEachIndexed { index, value ->
            val x = index * stepX
            // Invert y because canvas (0,0) is top-left
            val normalized = (value - minVal) / range
            val y = height - (normalized * (height * 0.75f)) - (height * 0.12f)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                val prevX = (index - 1) * stepX
                val prevNormalized = (data[index - 1] - minVal) / range
                val prevY = height - (prevNormalized * (height * 0.75f)) - (height * 0.12f)

                // Smooth cubic bezier curve
                val cx1 = prevX + (stepX / 2f)
                val cy1 = prevY
                val cx2 = prevX + (stepX / 2f)
                val cy2 = y
                path.cubicTo(cx1, cy1, cx2, cy2, x, y)
                fillPath.cubicTo(cx1, cy1, cx2, cy2, x, y)
            }
        }

        if (showGradientFill) {
            fillPath.lineTo(width, height)
            fillPath.close()
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = fillColor,
                    startY = 0f,
                    endY = height
                )
            )
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}
