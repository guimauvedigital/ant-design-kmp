package digital.guimauve.antdesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Configuration for Watermark text styling.
 * Provides granular control over font appearance in watermarks.
 *
 * @param color The color of the watermark text (default: semi-transparent black)
 * @param fontSize The size of the watermark text in SP units (default: 16)
 * @param fontWeight The weight of the watermark text (default: Normal)
 * @param fontFamily The font family to use (default: "sans-serif")
 * @param fontStyle The style of the font (normal or italic, default: Normal)
 */
data class WatermarkFont(
    val color: Color = Color(0x40000000),
    val fontSize: Int = 16,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontFamily: String = "sans-serif",
    val fontStyle: FontStyle = FontStyle.Normal,
)

/**
 * Ant Design Watermark Component
 *
 * A composable that adds a repeating watermark pattern to wrapped content.
 * Supports text watermarks with multiline content and optional image watermarks.
 * The watermark overlay is non-blocking, allowing pointer events to pass through.
 *
 * React Ant Design Reference: https://ant.design/components/watermark/
 *
 * @param modifier Modifier for the root container
 * @param content Content to display as watermark. Can be:
 *   - String for single-line watermark
 *   - List<String> for multiline watermark
 * @param width Width of the watermark element (default: 120.dp)
 * @param height Height of the watermark element (default: 64.dp)
 * @param rotate Rotation angle in degrees for the watermark (default: -22f)
 * @param zIndex Z-index for layering the watermark overlay (default: 9)
 * @param gap Spacing between watermarks as Pair<Dp, Dp> for (x, y) (default: 100.dp, 100.dp)
 * @param offset Optional offset from the gap position as Pair<Dp, Dp> for (x, y)
 * @param font Font configuration for text watermarks
 * @param image Optional image URL for image-based watermarks (currently placeholder)
 * @param children The content to be wrapped with the watermark overlay
 *
 * Example usage:
 * ```
 * AntWatermark(
 *   content = "Watermark Text",
 *   font = WatermarkFont(
 *     color = Color.Black.copy(alpha = 0.15f),
 *     fontSize = 16
 *   ),
 *   gap = Pair(100.dp, 100.dp),
 *   rotate = -22f
 * ) {
 *   // Your content here
 *   AntButton(text = "Click me")
 * }
 * ```
 */
@Composable
fun AntWatermark(
    modifier: Modifier = Modifier,
    content: Any? = "Ant Design",
    width: Dp = 120.dp,
    height: Dp = 64.dp,
    rotate: Float = -22f,
    zIndex: Int = 9,
    gap: Pair<Dp, Dp> = Pair(100.dp, 100.dp),
    offset: Pair<Dp, Dp>? = null,
    font: WatermarkFont? = null,
    image: String? = null,
    children: @Composable () -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val watermarkFont = font ?: WatermarkFont()

    // Convert font config to TextStyle
    val textStyle = TextStyle(
        color = watermarkFont.color,
        fontSize = watermarkFont.fontSize.sp,
        fontWeight = watermarkFont.fontWeight,
        fontFamily = FontFamily.SansSerif,
        fontStyle = watermarkFont.fontStyle
    )

    // Extract gap values
    val gapXDp = gap.first
    val gapYDp = gap.second
    val offsetXDp = offset?.first ?: 0.dp
    val offsetYDp = offset?.second ?: 0.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // Allow pointer events to pass through to underlying content
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(PointerEventPass.Initial)
                    }
                }
            }
    ) {
        // Render wrapped content
        children()

        // Watermark overlay layer
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Allow pointer events to pass through
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Initial)
                        }
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Convert gap and offset to pixels
            val gapXPx = gapXDp.toPx()
            val gapYPx = gapYDp.toPx()
            val offsetXPx = offsetXDp.toPx()
            val offsetYPx = offsetYDp.toPx()

            // Handle content as string or list
            val watermarkTexts = when (content) {
                is String -> listOf(content)
                is List<*> -> (content as? List<String>) ?: emptyList()
                else -> listOf(content.toString())
            }

            if (watermarkTexts.isEmpty()) return@Canvas

            // Measure each text line
            val textLayoutResults = watermarkTexts.map { text ->
                textMeasurer.measure(text, textStyle)
            }

            // Calculate dimensions based on measured content
            val maxTextWidth = textLayoutResults.maxOfOrNull { it.size.width.toFloat() } ?: 0f
            val textHeight = textLayoutResults.firstOrNull()?.size?.height?.toFloat() ?: 0f
            val totalHeight = if (watermarkTexts.size > 1) {
                (textHeight * watermarkTexts.size) + (8f * (watermarkTexts.size - 1))
            } else {
                textHeight
            }

            // Calculate pattern dimensions
            val patternWidth = maxTextWidth + gapXPx
            val patternHeight = totalHeight + gapYPx

            if (maxTextWidth <= 0f || textHeight <= 0f) return@Canvas

            // Calculate grid for watermarks
            val cols = ((canvasWidth + gapXPx) / patternWidth).toInt() + 2
            val rows = ((canvasHeight + gapYPx) / patternHeight).toInt() + 2

            // Draw watermarks in grid pattern
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val baseX = col * patternWidth + offsetXPx
                    val baseY = row * patternHeight + offsetYPx

                    // Draw rotated watermark group
                    rotate(
                        degrees = rotate,
                        pivot = Offset(
                            baseX + maxTextWidth / 2,
                            baseY + totalHeight / 2
                        )
                    ) {
                        var yOffset = 0f

                        // Draw each line of text
                        for ((index, textLayoutResult) in textLayoutResults.withIndex()) {
                            if (index > 0) yOffset += 8f // Line spacing

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(baseX, baseY + yOffset)
                            )

                            yOffset += textLayoutResult.size.height.toFloat()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Simplified Watermark composable with string content.
 * Uses default WatermarkFont configuration.
 *
 * @param text The watermark text to display
 * @param modifier Modifier for styling
 * @param rotate Rotation angle in degrees (default: -22f)
 * @param gap Spacing between watermarks (default: 100.dp for both x and y)
 * @param children The content to wrap with the watermark
 */
@Composable
fun AntWatermarkText(
    text: String,
    modifier: Modifier = Modifier,
    rotate: Float = -22f,
    gap: Pair<Dp, Dp> = Pair(100.dp, 100.dp),
    children: @Composable () -> Unit,
) {
    AntWatermark(
        modifier = modifier,
        content = text,
        rotate = rotate,
        gap = gap,
        children = children
    )
}

/**
 * Watermark composable with multiline text content.
 * Automatically arranges multiple lines with proper spacing.
 *
 * @param lines List of text lines to display as watermark
 * @param modifier Modifier for styling
 * @param rotate Rotation angle in degrees (default: -22f)
 * @param gap Spacing between watermarks (default: 100.dp for both x and y)
 * @param children The content to wrap with the watermark
 */
@Composable
fun AntWatermarkMultiline(
    lines: List<String>,
    modifier: Modifier = Modifier,
    rotate: Float = -22f,
    gap: Pair<Dp, Dp> = Pair(100.dp, 100.dp),
    children: @Composable () -> Unit,
) {
    AntWatermark(
        modifier = modifier,
        content = lines,
        rotate = rotate,
        gap = gap,
        children = children
    )
}
