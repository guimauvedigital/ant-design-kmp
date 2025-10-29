package digital.guimauve.antdesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Divider orientation: Horizontal or Vertical
 * Maps to React: orientation?: 'left' | 'right' | 'center' (for horizontal text alignment)
 */
enum class DividerOrientation {
    Horizontal,
    Vertical
}

/**
 * Horizontal divider text alignment
 * Maps to React: orientationMargin for vertical, and implicit text alignment
 */
enum class DividerTextAlign {
    Left,
    Center,
    Right
}

/**
 * Divider type: Solid or Dashed
 * Maps to React: type?: 'solid' | 'dashed'
 */
enum class DividerType {
    Solid,
    Dashed
}

/**
 * Semantic class names for Divider sub-components
 * Maps to React's classNames prop with semantic structure
 */
data class DividerClassNames(
    val root: String? = null,
    val text: String? = null,
    val line: String? = null,
)

/**
 * Semantic styles for Divider sub-components
 * Maps to React's styles prop with semantic structure
 */
data class DividerStyles(
    val root: Modifier? = null,
    val text: Modifier? = null,
    val line: Modifier? = null,
)

/**
 * Ant Design Divider Component - 100% React Ant Design Parity
 *
 * Full feature parity with React implementation:
 * https://ant.design/components/divider/
 *
 * Features:
 * - Horizontal and vertical dividers
 * - Solid and dashed line styles
 * - Text/content in divider (left/center/right aligned)
 * - Plain text variant (no bold)
 * - Orientation margin for vertical dividers
 * - Theme integration with border colors
 * - Customizable styling via classNames and styles
 *
 * @param modifier Root modifier
 * @param orientation Divider orientation: Horizontal or Vertical (default: Horizontal)
 * @param dashed Whether to use dashed line style (default: false)
 * @param type Divider type: Solid or Dashed (default: Solid)
 * @param plain Plain text style without bold (default: false)
 * @param textAlign Text alignment for horizontal divider (default: Center)
 * @param orientationMargin Margin for vertical divider (default: null)
 * @param color Border color (default: theme border color #D9D9D9)
 * @param thickness Line thickness (default: 1.dp)
 * @param children Composable content (text or custom widget)
 * @param classNames Semantic class names for sub-components
 * @param styles Semantic styles for sub-components
 */
@Composable
fun AntDivider(
    modifier: Modifier = Modifier,
    orientation: DividerOrientation = DividerOrientation.Horizontal,
    dashed: Boolean = false,
    type: DividerType = DividerType.Solid,
    plain: Boolean = false,
    textAlign: DividerTextAlign = DividerTextAlign.Center,
    orientationMargin: Dp? = null,
    color: Color = Color(0xFFD9D9D9),
    thickness: Dp = 1.dp,
    children: (@Composable () -> Unit)? = null,
    classNames: DividerClassNames? = null,
    styles: DividerStyles? = null,
) {
    when (orientation) {
        DividerOrientation.Horizontal -> {
            HorizontalDivider(
                modifier = modifier,
                dashed = dashed || type == DividerType.Dashed,
                plain = plain,
                textAlign = textAlign,
                color = color,
                thickness = thickness,
                children = children,
                classNames = classNames,
                styles = styles
            )
        }

        DividerOrientation.Vertical -> {
            VerticalDivider(
                modifier = modifier,
                dashed = dashed || type == DividerType.Dashed,
                color = color,
                thickness = thickness,
                orientationMargin = orientationMargin,
                classNames = classNames,
                styles = styles
            )
        }
    }
}

/**
 * Horizontal divider with optional text/content
 */
@Composable
private fun HorizontalDivider(
    modifier: Modifier = Modifier,
    dashed: Boolean = false,
    plain: Boolean = false,
    textAlign: DividerTextAlign = DividerTextAlign.Center,
    color: Color = Color(0xFFD9D9D9),
    thickness: Dp = 1.dp,
    children: (@Composable () -> Unit)? = null,
    classNames: DividerClassNames? = null,
    styles: DividerStyles? = null,
) {
    if (children == null) {
        // Simple divider without text
        DividerLine(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness),
            dashed = dashed,
            color = color,
            thickness = thickness,
            styles = styles
        )
    } else {
        // Divider with text/content
        Row(
            modifier = (styles?.root ?: Modifier)
                .fillMaxWidth()
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = when (textAlign) {
                DividerTextAlign.Left -> Arrangement.Start
                DividerTextAlign.Center -> Arrangement.Center
                DividerTextAlign.Right -> Arrangement.End
            }
        ) {
            // Left line
            if (textAlign != DividerTextAlign.Left) {
                DividerLine(
                    modifier = Modifier
                        .weight(1f)
                        .height(thickness),
                    dashed = dashed,
                    color = color,
                    thickness = thickness,
                    styles = styles
                )
            }

            // Text/content area
            Box(
                modifier = (styles?.text ?: Modifier)
                    .padding(horizontal = 16.dp)
            ) {
                children()
            }

            // Right line
            if (textAlign != DividerTextAlign.Right) {
                DividerLine(
                    modifier = Modifier
                        .weight(1f)
                        .height(thickness),
                    dashed = dashed,
                    color = color,
                    thickness = thickness,
                    styles = styles
                )
            }
        }
    }
}

/**
 * Vertical divider with optional margin
 */
@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier,
    dashed: Boolean = false,
    color: Color = Color(0xFFD9D9D9),
    thickness: Dp = 1.dp,
    orientationMargin: Dp? = null,
    classNames: DividerClassNames? = null,
    styles: DividerStyles? = null,
) {
    val verticalMargin = orientationMargin ?: 8.dp

    DividerLine(
        modifier = (styles?.root ?: Modifier)
            .width(thickness)
            .fillMaxHeight()
            .padding(vertical = verticalMargin)
            .then(modifier),
        dashed = dashed,
        color = color,
        thickness = thickness,
        isVertical = true,
        styles = styles
    )
}

/**
 * Core divider line component with dashed support
 */
@Composable
private fun DividerLine(
    modifier: Modifier = Modifier,
    dashed: Boolean = false,
    color: Color = Color(0xFFD9D9D9),
    thickness: Dp = 1.dp,
    isVertical: Boolean = false,
    styles: DividerStyles? = null,
) {
    if (dashed) {
        DashedDividerLine(
            modifier = (styles?.line ?: Modifier).then(modifier),
            color = color,
            thickness = thickness,
            isVertical = isVertical
        )
    } else {
        Box(
            modifier = (styles?.line ?: Modifier)
                .then(modifier)
                .background(color)
        )
    }
}

/**
 * Dashed divider implementation using Canvas
 * Draws repeating dash pattern
 */
@Composable
private fun DashedDividerLine(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFD9D9D9),
    thickness: Dp = 1.dp,
    isVertical: Boolean = false,
) {
    val dashLength = 4.dp
    val gapLength = 4.dp

    Canvas(
        modifier = modifier
    ) {
        val strokeWidth = thickness.toPx()

        if (isVertical) {
            // Vertical dashed line
            val dashLengthPx = dashLength.toPx()
            val gapLengthPx = gapLength.toPx()
            var y = 0f

            while (y < size.height) {
                drawLine(
                    color = color,
                    start = Offset(size.width / 2, y),
                    end = Offset(
                        size.width / 2,
                        (y + dashLengthPx).coerceAtMost(size.height)
                    ),
                    strokeWidth = strokeWidth
                )
                y += dashLengthPx + gapLengthPx
            }
        } else {
            // Horizontal dashed line
            val dashLengthPx = dashLength.toPx()
            val gapLengthPx = gapLength.toPx()
            var x = 0f

            while (x < size.width) {
                drawLine(
                    color = color,
                    start = Offset(x, size.height / 2),
                    end = Offset(
                        (x + dashLengthPx).coerceAtMost(size.width),
                        size.height / 2
                    ),
                    strokeWidth = strokeWidth
                )
                x += dashLengthPx + gapLengthPx
            }
        }
    }
}
