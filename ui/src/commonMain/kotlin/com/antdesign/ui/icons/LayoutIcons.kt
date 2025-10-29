package com.antdesign.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Vector icons for Layout component (Sider triggers)
 */

@Composable
fun MenuIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val lineLength = this.size.width * 0.6f
        val strokeWidth = this.size.height * 0.1f
        val spacing = this.size.height * 0.2f

        // Top line
        drawLine(
            color = color,
            start = Offset(center.x - lineLength / 2, center.y - spacing),
            end = Offset(center.x + lineLength / 2, center.y - spacing),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Middle line
        drawLine(
            color = color,
            start = Offset(center.x - lineLength / 2, center.y),
            end = Offset(center.x + lineLength / 2, center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Bottom line
        drawLine(
            color = color,
            start = Offset(center.x - lineLength / 2, center.y + spacing),
            end = Offset(center.x + lineLength / 2, center.y + spacing),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LeftArrowIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val arrowSize = this.size.minDimension * 0.4f
        val strokeWidth = this.size.minDimension * 0.1f

        val arrowPath = Path().apply {
            // Arrow pointing left
            moveTo(center.x + arrowSize * 0.3f, center.y - arrowSize)
            lineTo(center.x - arrowSize * 0.3f, center.y)
            lineTo(center.x + arrowSize * 0.3f, center.y + arrowSize)
        }

        drawPath(
            path = arrowPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun RightArrowIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val arrowSize = this.size.minDimension * 0.4f
        val strokeWidth = this.size.minDimension * 0.1f

        val arrowPath = Path().apply {
            // Arrow pointing right
            moveTo(center.x - arrowSize * 0.3f, center.y - arrowSize)
            lineTo(center.x + arrowSize * 0.3f, center.y)
            lineTo(center.x - arrowSize * 0.3f, center.y + arrowSize)
        }

        drawPath(
            path = arrowPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
