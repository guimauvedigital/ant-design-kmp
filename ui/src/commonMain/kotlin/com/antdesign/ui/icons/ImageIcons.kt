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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Professional vector icons for Image component preview controls
 */

@Composable
fun ZoomInIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension * 0.35f
        val strokeWidth = this.size.minDimension * 0.08f

        // Magnifying glass circle
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(center.x - this.size.width * 0.1f, center.y - this.size.height * 0.1f),
            style = Stroke(width = strokeWidth)
        )

        // Handle
        val angle = 45f * (PI / 180f).toFloat()
        val handleStart = Offset(
            center.x - this.size.width * 0.1f + radius * cos(angle),
            center.y - this.size.height * 0.1f + radius * sin(angle)
        )
        val handleEnd = Offset(
            handleStart.x + this.size.width * 0.18f,
            handleStart.y + this.size.height * 0.18f
        )

        drawLine(
            color = color,
            start = handleStart,
            end = handleEnd,
            strokeWidth = strokeWidth * 1.2f,
            cap = StrokeCap.Round
        )

        // Plus sign
        val plusSize = radius * 0.5f
        val plusCenter = Offset(center.x - this.size.width * 0.1f, center.y - this.size.height * 0.1f)

        // Horizontal line
        drawLine(
            color = color,
            start = Offset(plusCenter.x - plusSize, plusCenter.y),
            end = Offset(plusCenter.x + plusSize, plusCenter.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Vertical line
        drawLine(
            color = color,
            start = Offset(plusCenter.x, plusCenter.y - plusSize),
            end = Offset(plusCenter.x, plusCenter.y + plusSize),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ZoomOutIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension * 0.35f
        val strokeWidth = this.size.minDimension * 0.08f

        // Magnifying glass circle
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(center.x - this.size.width * 0.1f, center.y - this.size.height * 0.1f),
            style = Stroke(width = strokeWidth)
        )

        // Handle
        val angle = 45f * (PI / 180f).toFloat()
        val handleStart = Offset(
            center.x - this.size.width * 0.1f + radius * cos(angle),
            center.y - this.size.height * 0.1f + radius * sin(angle)
        )
        val handleEnd = Offset(
            handleStart.x + this.size.width * 0.18f,
            handleStart.y + this.size.height * 0.18f
        )

        drawLine(
            color = color,
            start = handleStart,
            end = handleEnd,
            strokeWidth = strokeWidth * 1.2f,
            cap = StrokeCap.Round
        )

        // Minus sign
        val minusSize = radius * 0.5f
        val minusCenter = Offset(center.x - this.size.width * 0.1f, center.y - this.size.height * 0.1f)

        // Horizontal line
        drawLine(
            color = color,
            start = Offset(minusCenter.x - minusSize, minusCenter.y),
            end = Offset(minusCenter.x + minusSize, minusCenter.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun RotateLeftIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension * 0.35f
        val strokeWidth = this.size.minDimension * 0.08f

        // Circular arrow path (counter-clockwise)
        val path = Path().apply {
            val startAngle = 60f
            val sweepAngle = 270f

            moveTo(
                center.x + radius * cos(startAngle * PI.toFloat() / 180f),
                center.y + radius * sin(startAngle * PI.toFloat() / 180f)
            )

            for (angle in startAngle.toInt()..(startAngle + sweepAngle).toInt() step 10) {
                val rad = angle * PI.toFloat() / 180f
                lineTo(
                    center.x + radius * cos(rad),
                    center.y + radius * sin(rad)
                )
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Arrow head (pointing left)
        val arrowSize = this.size.minDimension * 0.12f
        val arrowAngle = 330f * PI.toFloat() / 180f
        val arrowTip = Offset(
            center.x + radius * cos(arrowAngle),
            center.y + radius * sin(arrowAngle)
        )

        val arrowPath = Path().apply {
            moveTo(arrowTip.x - arrowSize * 0.7f, arrowTip.y - arrowSize * 0.5f)
            lineTo(arrowTip.x, arrowTip.y)
            lineTo(arrowTip.x + arrowSize * 0.3f, arrowTip.y - arrowSize)
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
fun RotateRightIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = this.size.minDimension * 0.35f
        val strokeWidth = this.size.minDimension * 0.08f

        // Circular arrow path (clockwise)
        val path = Path().apply {
            val startAngle = 120f
            val sweepAngle = -270f

            moveTo(
                center.x + radius * cos(startAngle * PI.toFloat() / 180f),
                center.y + radius * sin(startAngle * PI.toFloat() / 180f)
            )

            for (angle in startAngle.toInt() downTo (startAngle + sweepAngle).toInt() step 10) {
                val rad = angle * PI.toFloat() / 180f
                lineTo(
                    center.x + radius * cos(rad),
                    center.y + radius * sin(rad)
                )
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Arrow head (pointing right)
        val arrowSize = this.size.minDimension * 0.12f
        val arrowAngle = 210f * PI.toFloat() / 180f
        val arrowTip = Offset(
            center.x + radius * cos(arrowAngle),
            center.y + radius * sin(arrowAngle)
        )

        val arrowPath = Path().apply {
            moveTo(arrowTip.x + arrowSize * 0.7f, arrowTip.y - arrowSize * 0.5f)
            lineTo(arrowTip.x, arrowTip.y)
            lineTo(arrowTip.x - arrowSize * 0.3f, arrowTip.y - arrowSize)
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
fun FlipHorizontalIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val width = this.size.width * 0.6f
        val height = this.size.height * 0.6f
        val strokeWidth = this.size.minDimension * 0.08f

        // Left half (darker/filled)
        val leftPath = Path().apply {
            moveTo(center.x - width / 2, center.y - height / 2)
            lineTo(center.x, center.y - height / 2)
            lineTo(center.x, center.y + height / 2)
            lineTo(center.x - width / 2, center.y + height / 2)
            close()
        }

        drawPath(
            path = leftPath,
            color = color.copy(alpha = 0.5f)
        )

        // Right half (outline)
        drawLine(
            color = color,
            start = Offset(center.x, center.y - height / 2),
            end = Offset(center.x + width / 2, center.y - height / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(center.x + width / 2, center.y - height / 2),
            end = Offset(center.x + width / 2, center.y + height / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(center.x + width / 2, center.y + height / 2),
            end = Offset(center.x, center.y + height / 2),
            strokeWidth = strokeWidth
        )

        // Center divider line
        drawLine(
            color = color,
            start = Offset(center.x, center.y - height / 2),
            end = Offset(center.x, center.y + height / 2),
            strokeWidth = strokeWidth * 1.5f,
            cap = StrokeCap.Round
        )

        // Arrows
        val arrowSize = this.size.minDimension * 0.08f

        // Left arrow
        val leftArrowPath = Path().apply {
            moveTo(center.x - width / 4, center.y - arrowSize)
            lineTo(center.x - width / 4 - arrowSize, center.y)
            lineTo(center.x - width / 4, center.y + arrowSize)
        }

        drawPath(
            path = leftArrowPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Right arrow
        val rightArrowPath = Path().apply {
            moveTo(center.x + width / 4, center.y - arrowSize)
            lineTo(center.x + width / 4 + arrowSize, center.y)
            lineTo(center.x + width / 4, center.y + arrowSize)
        }

        drawPath(
            path = rightArrowPath,
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
fun FlipVerticalIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val width = this.size.width * 0.6f
        val height = this.size.height * 0.6f
        val strokeWidth = this.size.minDimension * 0.08f

        // Top half (darker/filled)
        val topPath = Path().apply {
            moveTo(center.x - width / 2, center.y - height / 2)
            lineTo(center.x + width / 2, center.y - height / 2)
            lineTo(center.x + width / 2, center.y)
            lineTo(center.x - width / 2, center.y)
            close()
        }

        drawPath(
            path = topPath,
            color = color.copy(alpha = 0.5f)
        )

        // Bottom half (outline)
        drawLine(
            color = color,
            start = Offset(center.x - width / 2, center.y),
            end = Offset(center.x - width / 2, center.y + height / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(center.x - width / 2, center.y + height / 2),
            end = Offset(center.x + width / 2, center.y + height / 2),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(center.x + width / 2, center.y + height / 2),
            end = Offset(center.x + width / 2, center.y),
            strokeWidth = strokeWidth
        )

        // Center divider line
        drawLine(
            color = color,
            start = Offset(center.x - width / 2, center.y),
            end = Offset(center.x + width / 2, center.y),
            strokeWidth = strokeWidth * 1.5f,
            cap = StrokeCap.Round
        )

        // Arrows
        val arrowSize = this.size.minDimension * 0.08f

        // Top arrow
        val topArrowPath = Path().apply {
            moveTo(center.x - arrowSize, center.y - height / 4)
            lineTo(center.x, center.y - height / 4 - arrowSize)
            lineTo(center.x + arrowSize, center.y - height / 4)
        }

        drawPath(
            path = topArrowPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Bottom arrow
        val bottomArrowPath = Path().apply {
            moveTo(center.x - arrowSize, center.y + height / 4)
            lineTo(center.x, center.y + height / 4 + arrowSize)
            lineTo(center.x + arrowSize, center.y + height / 4)
        }

        drawPath(
            path = bottomArrowPath,
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
fun DownloadIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f

        // Arrow shaft
        val shaftLength = this.size.height * 0.4f
        drawLine(
            color = color,
            start = Offset(center.x, center.y - shaftLength / 2),
            end = Offset(center.x, center.y + shaftLength / 2),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Arrow head
        val arrowSize = this.size.minDimension * 0.15f
        val arrowPath = Path().apply {
            moveTo(center.x - arrowSize, center.y + shaftLength / 2 - arrowSize * 0.7f)
            lineTo(center.x, center.y + shaftLength / 2)
            lineTo(center.x + arrowSize, center.y + shaftLength / 2 - arrowSize * 0.7f)
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

        // Base line
        val baseWidth = this.size.width * 0.5f
        drawLine(
            color = color,
            start = Offset(center.x - baseWidth / 2, center.y + this.size.height * 0.35f),
            end = Offset(center.x + baseWidth / 2, center.y + this.size.height * 0.35f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun EyeIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    color: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f

        // Eye outline (ellipse-like shape)
        val eyePath = Path().apply {
            moveTo(center.x - this@Canvas.size.width * 0.4f, center.y)

            // Top curve
            cubicTo(
                center.x - this@Canvas.size.width * 0.3f, center.y - this@Canvas.size.height * 0.25f,
                center.x + this@Canvas.size.width * 0.3f, center.y - this@Canvas.size.height * 0.25f,
                center.x + this@Canvas.size.width * 0.4f, center.y
            )

            // Bottom curve
            cubicTo(
                center.x + this@Canvas.size.width * 0.3f, center.y + this@Canvas.size.height * 0.25f,
                center.x - this@Canvas.size.width * 0.3f, center.y + this@Canvas.size.height * 0.25f,
                center.x - this@Canvas.size.width * 0.4f, center.y
            )

            close()
        }

        drawPath(
            path = eyePath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Pupil
        drawCircle(
            color = color,
            radius = this.size.minDimension * 0.12f,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // Inner dot
        drawCircle(
            color = color,
            radius = this.size.minDimension * 0.05f,
            center = center
        )
    }
}
