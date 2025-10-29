package com.antdesign.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.launch
import kotlin.math.sqrt

/**
 * Wave effect configuration matching React's ConfigProvider wave options
 */
data class WaveConfig(
    val disabled: Boolean = false,
    val color: Color? = null,
    val duration: Int = 600,
    val borderRadius: Dp? = null,
    val showEffect: ((WaveInfo) -> Unit)? = null
)

/**
 * Wave information passed during animation
 */
data class WaveInfo(
    val position: Offset,
    val targetSize: Size,
    val color: Color,
    val borderRadius: Dp
)

/**
 * Internal state for a single wave animation
 */
private data class WaveAnimation(
    val id: Int,
    val position: Offset,
    val progress: Animatable<Float, AnimationVector1D>
)

/**
 * Wave click effect component matching Ant Design React implementation
 *
 * Creates a ripple/wave animation effect on click, expanding outward from the click position
 * with a fade-out animation. Used internally by buttons and interactive components.
 *
 * React equivalent:
 * ```jsx
 * <Wave disabled={false}>
 *   <Button>Click me</Button>
 * </Wave>
 * ```
 *
 * Kotlin usage:
 * ```kotlin
 * AntWave {
 *     AntButton(onClick = {}) {
 *         Text("Click me")
 *     }
 * }
 * ```
 *
 * @param modifier Modifier to be applied to the container
 * @param disabled If true, wave effect is disabled
 * @param color Wave color (defaults to primary color with low opacity)
 * @param duration Animation duration in milliseconds (default 600ms)
 * @param borderRadius Border radius to inherit from parent (default 0.dp)
 * @param component Component type (Button, Tag, Checkbox, Radio, Switch)
 * @param className CSS class equivalent for styling
 * @param config Advanced wave configuration
 * @param content The content to wrap with wave effect
 */
@Composable
fun AntWave(
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    color: Color = Color(0xFF1890FF).copy(alpha = 0.2f),
    duration: Int = 600,
    borderRadius: Dp = 0.dp,
    component: WaveComponent? = null,
    className: String = "",
    config: WaveConfig? = null,
    content: @Composable BoxScope.() -> Unit
) {
    // Apply config if provided
    val effectiveDisabled = config?.disabled ?: disabled
    val effectiveColor = config?.color ?: color
    val effectiveDuration = config?.duration ?: duration
    val effectiveBorderRadius = config?.borderRadius ?: borderRadius

    // State for managing multiple simultaneous waves
    var waves by remember { mutableStateOf<List<WaveAnimation>>(emptyList()) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var waveIdCounter by remember { mutableStateOf(0) }
    var clickPosition by remember { mutableStateOf<Offset?>(null) }

    // Check if this is a quick component (Checkbox, Radio)
    val isQuickComponent = component == WaveComponent.Checkbox ||
                           component == WaveComponent.Radio
    val actualDuration = if (isQuickComponent) (effectiveDuration * 0.7f).toInt() else effectiveDuration

    // Handle wave animation when click position changes
    LaunchedEffect(clickPosition) {
        clickPosition?.let { position ->
            // Create animated progress value
            val progress = Animatable(0f)
            val waveId = waveIdCounter++

            // Add new wave animation
            val newWave = WaveAnimation(
                id = waveId,
                position = position,
                progress = progress
            )
            waves = waves + newWave

            // Custom effect callback
            config?.showEffect?.invoke(
                WaveInfo(
                    position = position,
                    targetSize = containerSize.toSize(),
                    color = effectiveColor,
                    borderRadius = effectiveBorderRadius
                )
            )

            // Animate wave
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = actualDuration,
                    easing = FastOutSlowInEasing
                )
            )
            // Remove wave after animation completes
            waves = waves.filterNot { it.id == waveId }
            clickPosition = null
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { containerSize = it }
            .pointerInput(effectiveDisabled) {
                if (!effectiveDisabled) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()?.position
                            if (position != null) {
                                clickPosition = position
                            }
                        }
                    }
                }
            }
            .drawWithContent {
                drawContent()

                // Draw all active waves
                if (!effectiveDisabled) {
                    waves.forEach { wave ->
                        drawWaveEffect(
                            wave = wave,
                            color = effectiveColor,
                            duration = actualDuration,
                            borderRadius = effectiveBorderRadius
                        )
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Component types that support wave effects
 */
enum class WaveComponent {
    Button,
    Tag,
    Checkbox,
    Radio,
    Switch
}

/**
 * Draws a single wave effect animation
 * Matches React's box-shadow expansion animation:
 * - Starts with box-shadow: 0 0 0 0
 * - Expands to box-shadow: 0 0 0 6px
 * - Opacity fades from 0.2 to 0
 * - Uses ease-out-circ easing
 */
private fun DrawScope.drawWaveEffect(
    wave: WaveAnimation,
    color: Color,
    duration: Int,
    borderRadius: Dp
) {
    val progress = wave.progress.value

    // Ease-out-circ easing function for smoother animation
    val eased = sqrt(1 - (1 - progress) * (1 - progress))

    // Calculate maximum radius (from click point to furthest corner)
    val maxRadius = calculateMaxRadius(wave.position, size)

    // Scale from 0 to full size
    val currentRadius = maxRadius * eased

    // Opacity animation: fade from initial opacity to 0
    val alpha = (1f - progress) * color.alpha

    // Draw expanding circle (simulating box-shadow expansion)
    drawCircle(
        color = color.copy(alpha = alpha),
        radius = currentRadius,
        center = wave.position
    )

    // Optional: draw border ring effect (like box-shadow border)
    if (progress < 0.9f) {
        val borderWidth = 6.dp.toPx() * eased
        val borderAlpha = (1f - progress) * 0.3f

        drawCircle(
            color = color.copy(alpha = borderAlpha),
            radius = currentRadius + borderWidth / 2,
            center = wave.position,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = borderWidth)
        )
    }
}

/**
 * Calculate maximum radius from click position to furthest corner
 */
private fun calculateMaxRadius(position: Offset, size: Size): Float {
    val corners = listOf(
        Offset(0f, 0f),
        Offset(size.width, 0f),
        Offset(0f, size.height),
        Offset(size.width, size.height)
    )

    return corners.maxOf { corner ->
        val dx = corner.x - position.x
        val dy = corner.y - position.y
        sqrt(dx * dx + dy * dy)
    }
}

/**
 * Legacy wave loading effect component (sine wave animation)
 * Kept for backward compatibility
 *
 * @deprecated Use AntWave for click ripple effects
 */
@Deprecated(
    message = "Use AntWave for click ripple effects. This is a legacy loading animation.",
    replaceWith = ReplaceWith("AntWave")
)
@Composable
fun AntWaveLegacy(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1890FF).copy(alpha = 0.2f),
    children: @Composable () -> Unit
) {
    var isAnimating by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition(label = "wave")

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave-progress"
    )

    Box(modifier = modifier) {
        children()

        if (isAnimating) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, height / 2)

                    for (x in 0..width.toInt() step 10) {
                        val y = height / 2 + 20 * kotlin.math.sin(
                            (x / width) * 2 * kotlin.math.PI + animationProgress * 2 * kotlin.math.PI
                        ).toFloat()
                        lineTo(x.toFloat(), y)
                    }

                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = path,
                    color = color,
                    style = androidx.compose.ui.graphics.drawscope.Fill
                )
            }
        }
    }
}
