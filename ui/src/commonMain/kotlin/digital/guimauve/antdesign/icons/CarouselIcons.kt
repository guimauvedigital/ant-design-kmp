package digital.guimauve.antdesign.icons

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
 * Arrow icons for Carousel component navigation
 */

@Composable
fun ArrowLeftIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White,
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f
        val arrowSize = this.size.minDimension * 0.35f

        val arrowPath = Path().apply {
            // Arrow pointing left
            moveTo(center.x + arrowSize * 0.3f, center.y - arrowSize)
            lineTo(center.x - arrowSize * 0.5f, center.y)
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
fun ArrowRightIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White,
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f
        val arrowSize = this.size.minDimension * 0.35f

        val arrowPath = Path().apply {
            // Arrow pointing right
            moveTo(center.x - arrowSize * 0.3f, center.y - arrowSize)
            lineTo(center.x + arrowSize * 0.5f, center.y)
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

@Composable
fun ArrowUpIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White,
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f
        val arrowSize = this.size.minDimension * 0.35f

        val arrowPath = Path().apply {
            // Arrow pointing up
            moveTo(center.x - arrowSize, center.y + arrowSize * 0.3f)
            lineTo(center.x, center.y - arrowSize * 0.5f)
            lineTo(center.x + arrowSize, center.y + arrowSize * 0.3f)
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
fun ArrowDownIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White,
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val strokeWidth = this.size.minDimension * 0.08f
        val arrowSize = this.size.minDimension * 0.35f

        val arrowPath = Path().apply {
            // Arrow pointing down
            moveTo(center.x - arrowSize, center.y - arrowSize * 0.3f)
            lineTo(center.x, center.y + arrowSize * 0.5f)
            lineTo(center.x + arrowSize, center.y - arrowSize * 0.3f)
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
