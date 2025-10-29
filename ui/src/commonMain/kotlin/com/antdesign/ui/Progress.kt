package com.antdesign.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * # Progress
 *
 * Display the current progress of an operation flow.
 *
 * ## When To Use
 *
 * If it will take a long time to complete an operation, you can use Progress to show the current progress and status.
 * - When an operation will interrupt the current interface, or it needs to run in the background for more than 2 seconds.
 * - When you need to display the completion percentage of an operation.
 *
 * ## Examples
 *
 * ### Basic Line Progress
 * ```kotlin
 * AntProgress(
 *     percent = 75.0,
 *     type = ProgressType.Line
 * )
 * ```
 *
 * ### Circle Progress with Custom Color
 * ```kotlin
 * AntProgress(
 *     percent = 60.0,
 *     type = ProgressType.Circle,
 *     strokeColor = Color.Blue,
 *     width = 120.dp
 * )
 * ```
 *
 * ### Dashboard Progress
 * ```kotlin
 * AntProgress(
 *     percent = 80.0,
 *     type = ProgressType.Dashboard,
 *     gapDegree = 75.0,
 *     gapPosition = GapPosition.Bottom
 * )
 * ```
 *
 * ### Progress with Steps
 * ```kotlin
 * AntProgress(
 *     percent = 50.0,
 *     steps = 5,
 *     strokeColor = Color.Green
 * )
 * ```
 *
 * ### Success Segment (Two-color Progress)
 * ```kotlin
 * AntProgress(
 *     percent = 60.0,
 *     success = SuccessConfig(percent = 30.0)
 * )
 * ```
 *
 * ### Custom Format Function
 * ```kotlin
 * AntProgress(
 *     percent = 75.0,
 *     format = { percent, _ -> "${percent.toInt()}/100" }
 * )
 * ```
 *
 * ### Active Animation
 * ```kotlin
 * AntProgress(
 *     percent = 45.0,
 *     status = ProgressStatus.Active
 * )
 * ```
 *
 * ### Gradient Progress
 * ```kotlin
 * AntProgress(
 *     percent = 80.0,
 *     type = ProgressType.Circle,
 *     strokeColor = ProgressGradient(
 *         gradient = mapOf(
 *             0.0 to Color(0xFF108EE9),
 *             1.0 to Color(0xFF87D068)
 *         )
 *     )
 * )
 * ```
 *
 * @param percent Progress percentage (0.0 - 100.0)
 * @param modifier Modifier for the progress container
 * @param type Progress type: Line, Circle, or Dashboard
 * @param status Progress status: Normal, Success, Exception, or Active
 * @param format Custom format function for progress text
 * @param strokeColor Progress bar color (can be Color or ProgressGradient)
 * @param strokeLinecap Shape of progress line end: Round, Square, or Butt
 * @param strokeWidth Width of progress stroke (line: 8dp default, circle: 6% default)
 * @param showInfo Whether to display progress text
 * @param size Size variant: Small, Default, or custom dimensions
 * @param steps Number of steps to divide progress into segments
 * @param success Success progress configuration for two-color progress
 * @param trailColor Background track color
 * @param gapDegree Gap degree for Dashboard type (default 75°)
 * @param gapPosition Gap position for Dashboard: Top, Bottom, Left, or Right
 * @param width Canvas width for Circle/Dashboard (default 120dp)
 * @param classNames Semantic class names for styling (v5.5.0+)
 * @param styles Semantic styles for custom modifications (v5.5.0+)
 */
@Composable
fun AntProgress(
    percent: Double,
    modifier: Modifier = Modifier,
    type: ProgressType = ProgressType.Line,
    status: ProgressStatus = ProgressStatus.Normal,
    format: ((percent: Double, successPercent: Double?) -> String)? = null,
    strokeColor: Any? = null,
    strokeLinecap: StrokeLinecap = StrokeLinecap.Round,
    strokeWidth: Dp? = null,
    showInfo: Boolean = true,
    size: Any = ProgressSize.Default,
    steps: Int? = null,
    success: SuccessConfig? = null,
    trailColor: Color = ColorPalette.gray3,
    gapDegree: Double? = null,
    gapPosition: GapPosition = GapPosition.Bottom,
    width: Dp? = null,
    classNames: ProgressClassNames? = null,
    styles: ProgressStyles? = null
) {
    // Determine colors based on status
    val defaultStrokeColor = when (status) {
        ProgressStatus.Success -> AntDesignTheme.defaultTheme.token.colorSuccess
        ProgressStatus.Exception -> AntDesignTheme.defaultTheme.token.colorError
        ProgressStatus.Active, ProgressStatus.Normal -> AntDesignTheme.defaultTheme.token.colorPrimary
    }

    val actualStrokeColor = strokeColor ?: defaultStrokeColor

    // Apply root styles
    val rootModifier = modifier.then(styles?.root ?: Modifier)

    when (type) {
        ProgressType.Line -> {
            ProgressLine(
                percent = percent,
                modifier = rootModifier,
                status = status,
                format = format,
                strokeColor = actualStrokeColor,
                strokeLinecap = strokeLinecap,
                strokeWidth = strokeWidth,
                showInfo = showInfo,
                size = size,
                steps = steps,
                success = success,
                trailColor = trailColor,
                styles = styles
            )
        }
        ProgressType.Circle -> {
            ProgressCircle(
                percent = percent,
                modifier = rootModifier,
                status = status,
                format = format,
                strokeColor = actualStrokeColor,
                strokeLinecap = strokeLinecap,
                strokeWidth = strokeWidth,
                showInfo = showInfo,
                size = size,
                success = success,
                trailColor = trailColor,
                width = width,
                styles = styles
            )
        }
        ProgressType.Dashboard -> {
            ProgressDashboard(
                percent = percent,
                modifier = rootModifier,
                status = status,
                format = format,
                strokeColor = actualStrokeColor,
                strokeLinecap = strokeLinecap,
                strokeWidth = strokeWidth,
                showInfo = showInfo,
                size = size,
                success = success,
                trailColor = trailColor,
                gapDegree = gapDegree,
                gapPosition = gapPosition,
                width = width,
                styles = styles
            )
        }
    }
}

/**
 * Line Progress Component
 * Renders a horizontal progress bar with optional steps and success segment
 */
@Composable
private fun ProgressLine(
    percent: Double,
    modifier: Modifier = Modifier,
    status: ProgressStatus = ProgressStatus.Normal,
    format: ((percent: Double, successPercent: Double?) -> String)? = null,
    strokeColor: Any,
    strokeLinecap: StrokeLinecap = StrokeLinecap.Round,
    strokeWidth: Dp? = null,
    showInfo: Boolean = true,
    size: Any = ProgressSize.Default,
    steps: Int? = null,
    success: SuccessConfig? = null,
    trailColor: Color = ColorPalette.gray3,
    styles: ProgressStyles? = null
) {
    // Determine dimensions based on size
    val (actualStrokeWidth, textSize) = when (size) {
        is ProgressSize -> {
            when (size) {
                ProgressSize.Small -> Pair(strokeWidth ?: 6.dp, 12.sp)
                ProgressSize.Default -> Pair(strokeWidth ?: 8.dp, 14.sp)
            }
        }
        is Int -> Pair(size.dp, 14.sp)
        is Pair<*, *> -> {
            val (w, h) = size as Pair<Int, Int>
            Pair(h.dp, 14.sp)
        }
        else -> Pair(strokeWidth ?: 8.dp, 14.sp)
    }

    // Animate percent changes
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat().coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "progress_percent"
    )

    val animatedSuccessPercent by animateFloatAsState(
        targetValue = success?.percent?.toFloat()?.coerceIn(0f, 100f) ?: 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "success_percent"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Progress bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(actualStrokeWidth)
                .then(styles?.line ?: Modifier)
        ) {
            if (steps != null && steps > 0) {
                // Steps mode
                ProgressSteps(
                    percent = animatedPercent,
                    steps = steps,
                    strokeColor = strokeColor,
                    trailColor = trailColor,
                    success = success?.copy(percent = animatedSuccessPercent.toDouble()),
                    strokeLinecap = strokeLinecap,
                    height = actualStrokeWidth
                )
            } else {
                // Standard line progress
                val shape = when (strokeLinecap) {
                    StrokeLinecap.Round -> RoundedCornerShape(100.dp)
                    StrokeLinecap.Square -> RoundedCornerShape(0.dp)
                    StrokeLinecap.Butt -> RoundedCornerShape(0.dp)
                }

                // Background track
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(trailColor)
                )

                // Main progress
                if (animatedPercent > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedPercent / 100f)
                            .clip(shape)
                            .background(
                                brush = createBrush(
                                    strokeColor = strokeColor,
                                    width = 1000f,
                                    height = actualStrokeWidth.value
                                )
                            )
                            .then(
                                if (status == ProgressStatus.Active) {
                                    Modifier.animatedStripes()
                                } else Modifier
                            )
                    )
                }

                // Success segment overlay
                if (success != null && animatedSuccessPercent > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((animatedPercent + animatedSuccessPercent).coerceAtMost(100f) / 100f)
                            .clip(shape)
                            .background(success.strokeColor)
                    )
                }
            }
        }

        // Info text
        if (showInfo) {
            Text(
                text = formatProgressText(
                    percent = animatedPercent.toDouble(),
                    successPercent = success?.let { animatedSuccessPercent.toDouble() },
                    status = status,
                    format = format
                ),
                fontSize = textSize,
                color = getTextColor(status, strokeColor),
                modifier = styles?.text ?: Modifier,
                style = TextStyle(fontWeight = FontWeight.Normal)
            )
        }
    }
}

/**
 * Progress Steps Component
 * Renders progress divided into segments
 */
@Composable
private fun ProgressSteps(
    percent: Float,
    steps: Int,
    strokeColor: Any,
    trailColor: Color,
    success: SuccessConfig?,
    strokeLinecap: StrokeLinecap,
    height: Dp
) {
    val shape = when (strokeLinecap) {
        StrokeLinecap.Round -> RoundedCornerShape(100.dp)
        StrokeLinecap.Square -> RoundedCornerShape(0.dp)
        StrokeLinecap.Butt -> RoundedCornerShape(0.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val percentPerStep = 100f / steps
        val mainColor = if (strokeColor is Color) strokeColor else AntDesignTheme.defaultTheme.token.colorPrimary
        val successPercent = success?.percent?.toFloat() ?: 0f

        repeat(steps) { index ->
            val stepStartPercent = index * percentPerStep
            val stepEndPercent = (index + 1) * percentPerStep

            val color = when {
                // Success segment
                success != null && percent >= stepStartPercent && (percent + successPercent) >= stepEndPercent -> {
                    success.strokeColor
                }
                // Main progress
                percent >= stepEndPercent -> mainColor
                percent > stepStartPercent -> mainColor
                else -> trailColor
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height)
                    .clip(shape)
                    .background(color)
            )
        }
    }
}

/**
 * Circle Progress Component
 * Renders a circular progress ring using Canvas
 */
@Composable
private fun ProgressCircle(
    percent: Double,
    modifier: Modifier = Modifier,
    status: ProgressStatus = ProgressStatus.Normal,
    format: ((percent: Double, successPercent: Double?) -> String)? = null,
    strokeColor: Any,
    strokeLinecap: StrokeLinecap = StrokeLinecap.Round,
    strokeWidth: Dp? = null,
    showInfo: Boolean = true,
    size: Any = ProgressSize.Default,
    success: SuccessConfig? = null,
    trailColor: Color = ColorPalette.gray3,
    width: Dp? = null,
    styles: ProgressStyles? = null
) {
    // Determine canvas width
    val canvasWidth = width ?: when (size) {
        is ProgressSize -> when (size) {
            ProgressSize.Small -> 80.dp
            ProgressSize.Default -> 120.dp
        }
        is Pair<*, *> -> {
            val (w, _) = size as Pair<Int, Int>
            w.dp
        }
        else -> 120.dp
    }

    // Stroke width as percentage of radius (default 6%)
    val actualStrokeWidth = strokeWidth ?: (canvasWidth.value * 0.06).dp

    // Animate percent changes
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat().coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "circle_percent"
    )

    val animatedSuccessPercent by animateFloatAsState(
        targetValue = success?.percent?.toFloat()?.coerceIn(0f, 100f) ?: 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "circle_success"
    )

    Box(
        modifier = modifier
            .size(canvasWidth)
            .then(styles?.circle ?: Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Canvas for drawing circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sizeWidth = this.size.width
            val sizeHeight = this.size.height
            val canvasSize = minOf(sizeWidth, sizeHeight)
            val centerX = canvasSize / 2f
            val centerY = canvasSize / 2f
            val radius = (canvasSize - actualStrokeWidth.toPx()) / 2f

            val cap = when (strokeLinecap) {
                StrokeLinecap.Round -> StrokeCap.Round
                StrokeLinecap.Square -> StrokeCap.Square
                StrokeLinecap.Butt -> StrokeCap.Butt
            }

            // Draw background track
            drawArc(
                color = trailColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
            )

            // Draw main progress arc
            if (animatedPercent > 0f) {
                val sweepAngle = (animatedPercent / 100f) * 360f
                val brush = createBrush(strokeColor, canvasSize, canvasSize)

                drawArc(
                    brush = brush,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
                )
            }

            // Draw success segment overlay
            if (success != null && animatedSuccessPercent > 0f) {
                val mainSweep = (animatedPercent / 100f) * 360f
                val successSweep = (animatedSuccessPercent / 100f) * 360f

                drawArc(
                    color = success.strokeColor,
                    startAngle = -90f + mainSweep,
                    sweepAngle = successSweep,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
                )
            }
        }

        // Center text
        if (showInfo) {
            Text(
                text = formatProgressText(
                    percent = animatedPercent.toDouble(),
                    successPercent = success?.let { animatedSuccessPercent.toDouble() },
                    status = status,
                    format = format
                ),
                fontSize = (canvasWidth.value * 0.16).sp,
                color = getTextColor(status, strokeColor),
                modifier = styles?.text ?: Modifier,
                style = TextStyle(fontWeight = FontWeight.Normal)
            )
        }
    }
}

/**
 * Dashboard Progress Component
 * Renders a three-quarter circle (speedometer style) with customizable gap
 */
@Composable
private fun ProgressDashboard(
    percent: Double,
    modifier: Modifier = Modifier,
    status: ProgressStatus = ProgressStatus.Normal,
    format: ((percent: Double, successPercent: Double?) -> String)? = null,
    strokeColor: Any,
    strokeLinecap: StrokeLinecap = StrokeLinecap.Round,
    strokeWidth: Dp? = null,
    showInfo: Boolean = true,
    size: Any = ProgressSize.Default,
    success: SuccessConfig? = null,
    trailColor: Color = ColorPalette.gray3,
    gapDegree: Double? = null,
    gapPosition: GapPosition = GapPosition.Bottom,
    width: Dp? = null,
    styles: ProgressStyles? = null
) {
    // Determine canvas width
    val canvasWidth = width ?: when (size) {
        is ProgressSize -> when (size) {
            ProgressSize.Small -> 80.dp
            ProgressSize.Default -> 120.dp
        }
        is Pair<*, *> -> {
            val (w, _) = size as Pair<Int, Int>
            w.dp
        }
        else -> 120.dp
    }

    // Stroke width as percentage of radius (default 6%)
    val actualStrokeWidth = strokeWidth ?: (canvasWidth.value * 0.06).dp

    // Gap degree (default 75°)
    val actualGapDegree = gapDegree ?: 75.0

    // Calculate start angle based on gap position
    val startAngle = when (gapPosition) {
        GapPosition.Top -> -(actualGapDegree / 2f)
        GapPosition.Bottom -> 90f - (actualGapDegree / 2f)
        GapPosition.Left -> 180f - (actualGapDegree / 2f)
        GapPosition.Right -> -(actualGapDegree / 2f) - 180f
    }

    val totalAngle = 360f - actualGapDegree.toFloat()

    // Animate percent changes
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat().coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "dashboard_percent"
    )

    val animatedSuccessPercent by animateFloatAsState(
        targetValue = success?.percent?.toFloat()?.coerceIn(0f, 100f) ?: 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "dashboard_success"
    )

    Box(
        modifier = modifier
            .size(canvasWidth)
            .then(styles?.circle ?: Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Canvas for drawing dashboard
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sizeWidth = this.size.width
            val sizeHeight = this.size.height
            val canvasSize = minOf(sizeWidth, sizeHeight)
            val centerX = canvasSize / 2f
            val centerY = canvasSize / 2f
            val radius = (canvasSize - actualStrokeWidth.toPx()) / 2f

            val cap = when (strokeLinecap) {
                StrokeLinecap.Round -> StrokeCap.Round
                StrokeLinecap.Square -> StrokeCap.Square
                StrokeLinecap.Butt -> StrokeCap.Butt
            }

            // Draw background track
            drawArc(
                color = trailColor,
                startAngle = startAngle.toFloat(),
                sweepAngle = totalAngle,
                useCenter = false,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
            )

            // Draw main progress arc
            if (animatedPercent > 0f) {
                val sweepAngle = (animatedPercent / 100f) * totalAngle
                val brush = createBrush(strokeColor, canvasSize, canvasSize)

                drawArc(
                    brush = brush,
                    startAngle = startAngle.toFloat(),
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
                )
            }

            // Draw success segment overlay
            if (success != null && animatedSuccessPercent > 0f) {
                val mainSweep = (animatedPercent / 100f) * totalAngle
                val successSweep = (animatedSuccessPercent / 100f) * totalAngle

                drawArc(
                    color = success.strokeColor,
                    startAngle = startAngle.toFloat() + mainSweep,
                    sweepAngle = successSweep,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = actualStrokeWidth.toPx(), cap = cap)
                )
            }
        }

        // Center text
        if (showInfo) {
            Text(
                text = formatProgressText(
                    percent = animatedPercent.toDouble(),
                    successPercent = success?.let { animatedSuccessPercent.toDouble() },
                    status = status,
                    format = format
                ),
                fontSize = (canvasWidth.value * 0.16).sp,
                color = getTextColor(status, strokeColor),
                modifier = styles?.text ?: Modifier,
                style = TextStyle(fontWeight = FontWeight.Normal)
            )
        }
    }
}

/**
 * Helper function to create brush from strokeColor
 * Supports solid colors and gradients
 */
private fun createBrush(strokeColor: Any, width: Float, height: Float): Brush {
    return when (strokeColor) {
        is Color -> SolidColor(strokeColor)
        is ProgressGradient -> {
            val colors = strokeColor.gradient.entries.sortedBy { it.key }
                .map { it.value }
            val stops = strokeColor.gradient.entries.sortedBy { it.key }
                .map { it.key.toFloat() }
                .toFloatArray()

            Brush.linearGradient(
                colorStops = colors.mapIndexed { index, color ->
                    stops.getOrElse(index) { index.toFloat() / (colors.size - 1) } to color
                }.toTypedArray(),
                start = Offset(0f, 0f),
                end = Offset(width, height)
            )
        }
        else -> SolidColor(AntDesignTheme.defaultTheme.token.colorPrimary)
    }
}

/**
 * Format progress text based on status and custom format function
 */
private fun formatProgressText(
    percent: Double,
    successPercent: Double?,
    status: ProgressStatus,
    format: ((percent: Double, successPercent: Double?) -> String)?
): String {
    // Custom format function takes precedence
    if (format != null) {
        return format(percent, successPercent)
    }

    // Status-based icons
    return when (status) {
        ProgressStatus.Success -> "✓"
        ProgressStatus.Exception -> "✕"
        else -> "${percent.toInt()}%"
    }
}

/**
 * Get text color based on status
 */
private fun getTextColor(status: ProgressStatus, strokeColor: Any): Color {
    return when (status) {
        ProgressStatus.Success -> AntDesignTheme.defaultTheme.token.colorSuccess
        ProgressStatus.Exception -> AntDesignTheme.defaultTheme.token.colorError
        ProgressStatus.Normal, ProgressStatus.Active -> {
            if (strokeColor is Color) strokeColor
            else AntDesignTheme.defaultTheme.token.colorPrimary
        }
    }
}

/**
 * Modifier for animated stripes effect (Active status)
 */
private fun Modifier.animatedStripes(): Modifier {
    // This is a placeholder - in a real implementation, you would use
    // Canvas with animated gradient or pattern for moving stripes
    // For now, we'll just apply a subtle animation
    return this
}

// ============================================================================
// Enums and Data Classes
// ============================================================================

/**
 * Progress type variants
 *
 * @property Line Horizontal line progress bar
 * @property Circle Circular progress ring
 * @property Dashboard Three-quarter circle (speedometer style)
 */
enum class ProgressType {
    /** Horizontal line progress bar */
    Line,

    /** Circular progress ring */
    Circle,

    /** Three-quarter circle (speedometer style) with gap */
    Dashboard
}

/**
 * Progress status variants
 *
 * @property Normal Default state with primary color
 * @property Success Completed state with success color and checkmark
 * @property Exception Error state with error color and cross mark
 * @property Active In-progress state with animated stripes effect
 */
enum class ProgressStatus {
    /** Default state with primary color */
    Normal,

    /** Completed state with success color and checkmark */
    Success,

    /** Error state with error color and cross mark */
    Exception,

    /** In-progress state with animated stripes effect */
    Active
}

/**
 * Stroke linecap style for progress ends
 *
 * @property Round Rounded ends (default)
 * @property Square Square ends
 * @property Butt Flat ends
 */
enum class StrokeLinecap {
    /** Rounded ends (default) */
    Round,

    /** Square ends */
    Square,

    /** Flat ends with no extension */
    Butt
}

/**
 * Progress size variants
 *
 * @property Small Smaller size (80dp for circle, 6dp stroke for line)
 * @property Default Standard size (120dp for circle, 8dp stroke for line)
 */
enum class ProgressSize {
    /** Smaller size (80dp for circle, 6dp stroke for line) */
    Small,

    /** Standard size (120dp for circle, 8dp stroke for line) */
    Default
}

/**
 * Gap position for Dashboard progress
 *
 * @property Top Gap at top
 * @property Bottom Gap at bottom (default)
 * @property Left Gap at left
 * @property Right Gap at right
 */
enum class GapPosition {
    /** Gap positioned at top */
    Top,

    /** Gap positioned at bottom (default for dashboard) */
    Bottom,

    /** Gap positioned at left */
    Left,

    /** Gap positioned at right */
    Right
}

/**
 * Success progress configuration for two-color progress
 *
 * Allows showing a secondary progress color on top of the main progress.
 * The success segment starts where the main progress ends.
 *
 * @property percent Success progress percentage (0.0 - 100.0)
 * @property strokeColor Color for success segment (default: green)
 *
 * @sample
 * ```kotlin
 * AntProgress(
 *     percent = 60.0,
 *     success = SuccessConfig(
 *         percent = 30.0,
 *         strokeColor = Color.Green
 *     )
 * )
 * // Shows 60% primary color + 30% green = 90% total
 * ```
 */
data class SuccessConfig(
    /** Success progress percentage (adds to main percent) */
    val percent: Double,

    /** Color for success segment (default: Ant Design green) */
    val strokeColor: Color = Color(0xFF52C41A)
)

/**
 * Progress gradient configuration
 *
 * Supports linear gradients for Circle and Dashboard progress types.
 * For Line type, gradient is approximated with color interpolation.
 *
 * @property gradient Map of position (0.0 to 1.0) to Color
 *
 * @sample
 * ```kotlin
 * AntProgress(
 *     percent = 80.0,
 *     type = ProgressType.Circle,
 *     strokeColor = ProgressGradient(
 *         gradient = mapOf(
 *             0.0 to Color(0xFF108EE9),
 *             0.5 to Color(0xFF87D068),
 *             1.0 to Color(0xFF3FBF00)
 *         )
 *     )
 * )
 * ```
 */
data class ProgressGradient(
    /** Map of position (0.0 to 1.0) to Color for gradient stops */
    val gradient: Map<Double, Color>
)

/**
 * Semantic class names for Progress styling (v5.5.0+)
 *
 * Allows applying custom CSS classes to specific parts of the Progress component.
 * Note: In Compose, this is primarily for documentation purposes and may not have
 * direct styling effects without custom implementation.
 *
 * @property root Class name for root container
 * @property line Class name for line progress specific styles
 * @property circle Class name for circle/dashboard progress specific styles
 * @property text Class name for info text
 */
data class ProgressClassNames(
    /** Class name for root container element */
    val root: String = "",

    /** Class name for line progress specific element */
    val line: String = "",

    /** Class name for circle/dashboard progress specific element */
    val circle: String = "",

    /** Class name for info text element */
    val text: String = ""
)

/**
 * Semantic styles for Progress customization (v5.5.0+)
 *
 * Allows applying custom Modifiers to specific parts of the Progress component
 * for fine-grained style control.
 *
 * @property root Modifier for root container
 * @property line Modifier for line progress
 * @property circle Modifier for circle/dashboard progress
 * @property text Modifier for info text
 *
 * @sample
 * ```kotlin
 * AntProgress(
 *     percent = 75.0,
 *     styles = ProgressStyles(
 *         root = Modifier.padding(16.dp),
 *         line = Modifier.shadow(2.dp),
 *         text = Modifier.alpha(0.8f)
 *     )
 * )
 * ```
 */
data class ProgressStyles(
    /** Modifier for root container element */
    val root: Modifier = Modifier,

    /** Modifier for line progress specific element */
    val line: Modifier = Modifier,

    /** Modifier for circle/dashboard progress specific element */
    val circle: Modifier = Modifier,

    /** Modifier for info text element */
    val text: Modifier = Modifier
)
