package digital.guimauve.antdesign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Size variants for the Spin component
 * Matches React Ant Design v5.x size specifications
 *
 * @since 1.0.0
 */
enum class SpinSize {
    /** Small spinner - 14dp diameter */
    Small,

    /** Default/Middle spinner - 20dp diameter */
    Default,

    /** Large spinner - 32dp diameter */
    Large
}

/**
 * Semantic class names for Spin sub-elements
 * Ant Design v5.7.0+ feature for fine-grained styling control
 *
 * This allows you to apply custom CSS class names to specific parts of the spinner.
 * Note: In Compose/KMP, class names are informational only and don't apply CSS.
 * Use [SpinStyles] for actual styling in Compose.
 *
 * @property root Custom class name for the root container (informational)
 * @property dot Custom class name for the spinning indicator (informational)
 * @property text Custom class name for the tip text (informational)
 * @since 5.7.0
 * @see SpinStyles for functional styling with Compose modifiers
 */
data class SpinClassNames(
    val root: String = "",
    val dot: String = "",
    val text: String = "",
)

/**
 * Semantic modifiers for Spin sub-elements
 * Allows targeted styling of spinner components using Compose modifiers
 *
 * This is the Compose equivalent of React's semantic styles feature in Ant Design v5.
 * Apply custom modifiers to specific parts of the spinner for fine-grained control.
 *
 * @property root Custom modifier for the root container
 * @property dot Custom modifier for the spinning indicator
 * @property text Custom modifier for the tip text
 * @since 5.7.0
 * @see SpinClassNames for informational class names
 */
data class SpinStyles(
    val root: Modifier = Modifier,
    val dot: Modifier = Modifier,
    val text: Modifier = Modifier,
)

/**
 * Ant Design Spin component for Compose Multiplatform
 * Full feature parity with React Ant Design v5.21.0+
 *
 * A spinner component for displaying loading state of a page or section.
 *
 * **When to use:**
 * - When content is being fetched or processed
 * - When a page/section needs a loading indicator
 * - When wrapping content that may take time to load
 *
 * **Features:**
 * - Default rotating dots indicator
 * - Custom indicator support
 * - Overlay mode for wrapping content
 * - Fullscreen loading mode (v5.11.0+)
 * - Progress ring with percentage (v5.21.0+)
 * - Delay to prevent flash for quick operations
 * - Tip text for describing the loading state
 * - Size variants (small, default, large)
 * - Semantic styles for fine-grained control (v5.7.0+)
 *
 * **Examples:**
 * ```kotlin
 * // Basic spinner
 * AntSpin(spinning = true)
 *
 * // With tip text
 * AntSpin(
 *     spinning = true,
 *     tip = "Loading..."
 * )
 *
 * // Overlay mode - wrapping content
 * AntSpin(
 *     spinning = isLoading,
 *     tip = "Loading data..."
 * ) {
 *     YourContent()
 * }
 *
 * // Fullscreen mode
 * AntSpin(
 *     spinning = isLoading,
 *     fullscreen = true,
 *     tip = "Processing..."
 * )
 *
 * // With progress
 * AntSpin(
 *     spinning = true,
 *     percent = 75.0,
 *     tip = "Uploading..."
 * )
 *
 * // Delayed appearance (prevents flash)
 * AntSpin(
 *     spinning = isLoading,
 *     delay = 500 // Show spinner only after 500ms
 * )
 *
 * // Custom indicator
 * AntSpin(
 *     spinning = true,
 *     indicator = {
 *         Icon(Icons.Default.Refresh, "Loading")
 *     }
 * )
 *
 * // Different sizes
 * AntSpin(spinning = true, size = SpinSize.Small)
 * AntSpin(spinning = true, size = SpinSize.Large)
 * ```
 *
 * @param spinning Whether Spin is visible. When false, only children are shown.
 * @param modifier Modifier to be applied to the component
 * @param size Size of the spinning indicator (Small: 14dp, Default: 20dp, Large: 32dp)
 * @param delay Delay in milliseconds before showing spinner. Prevents flash for quick operations.
 * @param indicator Custom spinning indicator. Replaces the default dots indicator.
 * @param tip Descriptive text shown below the spinner. Provides context about the loading state.
 * @param wrapperClassName CSS class name for wrapper element (informational only in Compose)
 * @param fullscreen Display spinner in fullscreen mode covering entire screen (v5.11.0+)
 * @param percent Progress percentage (0.0 to 100.0). Shows circular progress ring around spinner (v5.21.0+)
 * @param children Content to be wrapped by the spinner. When provided, spinner becomes an overlay.
 * @param classNames Semantic class names for sub-elements (v5.7.0+, informational in Compose)
 * @param styles Semantic styles (modifiers) for sub-elements (v5.7.0+)
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/spin">Ant Design Spin</a>
 */
@Composable
fun AntSpin(
    spinning: Boolean = true,
    modifier: Modifier = Modifier,
    size: SpinSize = SpinSize.Default,
    delay: Int? = null,
    indicator: (@Composable () -> Unit)? = null,
    tip: String? = null,
    wrapperClassName: String = "",
    fullscreen: Boolean = false,
    percent: Double? = null,
    children: (@Composable () -> Unit)? = null,
    classNames: SpinClassNames? = null,
    styles: SpinStyles? = null,
) {
    // Get theme from ConfigProvider
    val theme = useTheme()

    // Handle delay mechanism
    var showSpin by remember(spinning) { mutableStateOf(delay == null || delay == 0) }

    LaunchedEffect(spinning) {
        if (spinning && delay != null && delay > 0) {
            delay(delay.toLong())
            showSpin = true
        } else {
            showSpin = spinning
        }
    }

    // Determine actual visibility
    val isVisible = spinning && showSpin

    // Fullscreen mode uses Popup to overlay entire screen
    if (fullscreen) {
        if (isVisible) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.8f))
                        .then(styles?.root ?: Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    SpinnerContent(
                        size = size,
                        indicator = indicator,
                        tip = tip,
                        percent = percent,
                        theme = theme,
                        dotModifier = styles?.dot ?: Modifier,
                        textModifier = styles?.text ?: Modifier
                    )
                }
            }
        }
        // Children still render below fullscreen spinner
        children?.invoke()
        return
    }

    // Overlay mode (when children provided)
    if (children != null) {
        Box(
            modifier = modifier.then(styles?.root ?: Modifier),
            contentAlignment = Alignment.Center
        ) {
            // Content
            Box(
                modifier = if (isVisible) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier
                }
            ) {
                children()
            }

            // Overlay spinner
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    SpinnerContent(
                        size = size,
                        indicator = indicator,
                        tip = tip,
                        percent = percent,
                        theme = theme,
                        dotModifier = styles?.dot ?: Modifier,
                        textModifier = styles?.text ?: Modifier
                    )
                }
            }
        }
    } else {
        // Standalone spinner (no children)
        AnimatedVisibility(
            visible = isVisible,
            modifier = modifier.then(styles?.root ?: Modifier),
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                SpinnerContent(
                    size = size,
                    indicator = indicator,
                    tip = tip,
                    percent = percent,
                    theme = theme,
                    dotModifier = styles?.dot ?: Modifier,
                    textModifier = styles?.text ?: Modifier
                )
            }
        }
    }
}

/**
 * Internal component that renders the actual spinner content
 * Includes the indicator (default or custom), tip text, and optional progress ring
 */
@Composable
private fun SpinnerContent(
    size: SpinSize,
    indicator: (@Composable () -> Unit)?,
    tip: String?,
    percent: Double?,
    theme: AntThemeConfig,
    dotModifier: Modifier,
    textModifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(
                current = percent?.toFloat() ?: 0f,
                range = 0f..100f
            )
        }
    ) {
        // Spinner indicator with optional progress ring
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Progress ring (if percent is provided)
            if (percent != null) {
                ProgressRing(
                    percent = percent,
                    size = size,
                    theme = theme
                )
            }

            // Spinner indicator
            Box(modifier = dotModifier) {
                if (indicator != null) {
                    indicator()
                } else {
                    DefaultSpinIndicator(
                        size = size,
                        color = theme.token.colorPrimary
                    )
                }
            }
        }

        // Tip text
        if (tip != null) {
            Text(
                text = tip,
                fontSize = 14.sp,
                color = ColorPalette.gray7,
                modifier = textModifier
            )
        }
    }
}

/**
 * Default spinning dots indicator
 * Creates the classic Ant Design rotating dots pattern
 *
 * Uses 8 dots arranged in a circle, each fading in sequence to create
 * the spinning effect. The dots rotate continuously at 1200ms per rotation.
 */
@Composable
private fun DefaultSpinIndicator(
    size: SpinSize,
    color: Color,
) {
    val spinnerSize = when (size) {
        SpinSize.Small -> 14.dp
        SpinSize.Default -> 20.dp
        SpinSize.Large -> 32.dp
    }

    val dotSize = when (size) {
        SpinSize.Small -> 2.dp
        SpinSize.Default -> 3.dp
        SpinSize.Large -> 4.dp
    }

    // Rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "spin_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(spinnerSize)
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = this.size.width / 2
            val centerY = this.size.height / 2
            val radius = this.size.minDimension / 2 - dotSize.toPx()
            val dotRadius = dotSize.toPx() / 2

            // Draw 8 dots in a circle
            for (i in 0 until 8) {
                val angle = (i * 45.0) * PI / 180.0
                val x = centerX + (radius * cos(angle)).toFloat()
                val y = centerY + (radius * sin(angle)).toFloat()

                // Calculate alpha for fade effect (each dot has different opacity)
                val alpha = 0.3f + (i / 8f) * 0.7f

                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = dotRadius,
                    center = Offset(x, y)
                )
            }
        }
    }
}

/**
 * Progress ring component for showing percentage around the spinner
 * Displays a circular progress ring with animated progress
 *
 * @param percent Progress percentage (0.0 to 100.0)
 * @param size Size of the progress ring (matches spinner size)
 * @param theme Theme configuration for colors
 * @since 5.21.0
 */
@Composable
private fun ProgressRing(
    percent: Double,
    size: SpinSize,
    theme: AntThemeConfig,
) {
    val ringSize = when (size) {
        SpinSize.Small -> 28.dp
        SpinSize.Default -> 40.dp
        SpinSize.Large -> 64.dp
    }

    val strokeWidth = when (size) {
        SpinSize.Small -> 2.dp
        SpinSize.Default -> 3.dp
        SpinSize.Large -> 4.dp
    }

    // Animate progress changes
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat(),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progress_animation"
    )

    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val diameter = this.size.minDimension - strokeWidthPx
            val radius = diameter / 2
            val topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
            val arcSize = Size(diameter, diameter)

            // Background ring (light gray)
            drawArc(
                color = ColorPalette.gray4,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )

            // Progress ring (primary color)
            val sweepAngle = (animatedPercent / 100f) * 360f
            drawArc(
                color = theme.token.colorPrimary,
                startAngle = -90f, // Start from top
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
        }

        // Optional: Show percentage text in center
        // Uncomment if you want to display percentage number
        /*
        Text(
            text = "${animatedPercent.toInt()}%",
            fontSize = when (size) {
                SpinSize.Small -> 8.sp
                SpinSize.Default -> 10.sp
                SpinSize.Large -> 12.sp
            },
            color = theme.token.colorPrimary
        )
        */
    }
}

/**
 * Alternative line-based spinning indicator
 * Uses 8 lines arranged in a circle for a different visual style
 *
 * This is an alternative to the dots indicator and can be used
 * as a custom indicator if preferred.
 *
 * Example:
 * ```kotlin
 * AntSpin(
 *     spinning = true,
 *     indicator = {
 *         SpinLinesIndicator(size = SpinSize.Default)
 *     }
 * )
 * ```
 */
@Composable
fun SpinLinesIndicator(
    size: SpinSize = SpinSize.Default,
    color: Color? = null,
) {
    val theme = useTheme()
    val spinColor = color ?: theme.token.colorPrimary

    val spinnerSize = when (size) {
        SpinSize.Small -> 14.dp
        SpinSize.Default -> 20.dp
        SpinSize.Large -> 32.dp
    }

    val lineLength = when (size) {
        SpinSize.Small -> 4.dp
        SpinSize.Default -> 6.dp
        SpinSize.Large -> 8.dp
    }

    val lineWidth = when (size) {
        SpinSize.Small -> 1.5.dp
        SpinSize.Default -> 2.dp
        SpinSize.Large -> 3.dp
    }

    // Rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "lines_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(spinnerSize)
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = this.size.width / 2
            val centerY = this.size.height / 2
            val radius = this.size.minDimension / 2 - lineLength.toPx() / 2
            val lineLengthPx = lineLength.toPx()
            val lineWidthPx = lineWidth.toPx()

            // Draw 8 lines in a circle
            for (i in 0 until 8) {
                val angle = (i * 45.0) * PI / 180.0
                val startX = centerX + ((radius - lineLengthPx / 2) * cos(angle)).toFloat()
                val startY = centerY + ((radius - lineLengthPx / 2) * sin(angle)).toFloat()
                val endX = centerX + ((radius + lineLengthPx / 2) * cos(angle)).toFloat()
                val endY = centerY + ((radius + lineLengthPx / 2) * sin(angle)).toFloat()

                // Calculate alpha for fade effect
                val alpha = 0.3f + (i / 8f) * 0.7f

                drawLine(
                    color = spinColor.copy(alpha = alpha),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = lineWidthPx,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
