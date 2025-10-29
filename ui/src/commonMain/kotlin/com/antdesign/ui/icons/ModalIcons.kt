package com.antdesign.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Professional vector icons for Modal component
 */

@Composable
fun InfoCircleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    color: Color = Color(0xFF1677ff)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension / 2

        // Circle
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )

        // Info "i" symbol
        val infoColor = Color.White

        // Dot on top
        drawCircle(
            color = infoColor,
            radius = this.size.minDimension * 0.08f,
            center = Offset(center.x, center.y - radius * 0.35f)
        )

        // Vertical line
        val lineStartY = center.y - radius * 0.1f
        val lineEndY = center.y + radius * 0.45f
        val lineWidth = this.size.minDimension * 0.14f

        drawLine(
            color = infoColor,
            start = Offset(center.x, lineStartY),
            end = Offset(center.x, lineEndY),
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CheckCircleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    color: Color = Color(0xFF52c41a)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension / 2

        // Circle
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )

        // Checkmark
        val checkColor = Color.White
        val strokeWidth = this.size.minDimension * 0.08f

        val checkPath = Path().apply {
            val scale = radius * 0.65f
            moveTo(center.x - scale * 0.4f, center.y)
            lineTo(center.x - scale * 0.1f, center.y + scale * 0.35f)
            lineTo(center.x + scale * 0.5f, center.y - scale * 0.35f)
        }

        drawPath(
            path = checkPath,
            color = checkColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun CloseCircleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    color: Color = Color(0xFFff4d4f)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension / 2

        // Circle
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )

        // X mark
        val xColor = Color.White
        val strokeWidth = this.size.minDimension * 0.08f
        val xSize = radius * 0.5f

        // Top-left to bottom-right
        drawLine(
            color = xColor,
            start = Offset(center.x - xSize, center.y - xSize),
            end = Offset(center.x + xSize, center.y + xSize),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Top-right to bottom-left
        drawLine(
            color = xColor,
            start = Offset(center.x + xSize, center.y - xSize),
            end = Offset(center.x - xSize, center.y + xSize),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun WarningIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    color: Color = Color(0xFFfaad14)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension / 2

        // Triangle path
        val trianglePath = Path().apply {
            val height = radius * 1.8f
            val width = radius * 1.6f

            moveTo(center.x, center.y - height * 0.55f) // Top
            lineTo(center.x + width, center.y + height * 0.45f) // Bottom right
            lineTo(center.x - width, center.y + height * 0.45f) // Bottom left
            close()
        }

        drawPath(
            path = trianglePath,
            color = color
        )

        // Exclamation mark
        val exclamationColor = Color.White
        val lineWidth = this.size.minDimension * 0.1f

        // Vertical line
        val lineStartY = center.y - radius * 0.35f
        val lineEndY = center.y + radius * 0.05f

        drawLine(
            color = exclamationColor,
            start = Offset(center.x, lineStartY),
            end = Offset(center.x, lineEndY),
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )

        // Dot at bottom
        drawCircle(
            color = exclamationColor,
            radius = this.size.minDimension * 0.06f,
            center = Offset(center.x, center.y + radius * 0.25f)
        )
    }
}

@Composable
fun QuestionCircleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    color: Color = Color(0xFFfaad14)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension / 2

        // Circle
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )

        // Question mark
        val questionColor = Color.White
        val strokeWidth = this.size.minDimension * 0.1f

        // Question mark curve
        val questionPath = Path().apply {
            val scale = radius * 0.5f

            // Start at top-left
            moveTo(center.x - scale * 0.4f, center.y - scale * 0.5f)

            // Curve to top-right
            cubicTo(
                center.x - scale * 0.4f, center.y - scale * 0.85f,
                center.x + scale * 0.4f, center.y - scale * 0.85f,
                center.x + scale * 0.4f, center.y - scale * 0.5f
            )

            // Curve down to center
            cubicTo(
                center.x + scale * 0.4f, center.y - scale * 0.15f,
                center.x, center.y - scale * 0.1f,
                center.x, center.y + scale * 0.15f
            )
        }

        drawPath(
            path = questionPath,
            color = questionColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Dot at bottom
        drawCircle(
            color = questionColor,
            radius = this.size.minDimension * 0.07f,
            center = Offset(center.x, center.y + radius * 0.5f)
        )
    }
}

@Composable
fun CloseOutlinedIcon(
    modifier: Modifier = Modifier,
    size: Dp = 14.dp,
    color: Color = Color(0xFF00000073)
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.1f
        val xSize = this.size.minDimension * 0.35f

        // Top-left to bottom-right
        drawLine(
            color = color,
            start = Offset(center.x - xSize, center.y - xSize),
            end = Offset(center.x + xSize, center.y + xSize),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Top-right to bottom-left
        drawLine(
            color = color,
            start = Offset(center.x + xSize, center.y - xSize),
            end = Offset(center.x - xSize, center.y + xSize),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
