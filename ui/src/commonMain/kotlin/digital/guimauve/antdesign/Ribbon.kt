package digital.guimauve.antdesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ribbon placement positions - corners of the wrapped content
 * Matches React Ant Design Ribbon API
 */
enum class RibbonPlacement {
    TopStart,
    TopEnd,
    BottomStart,
    BottomEnd
}

/**
 * Ant Design Ribbon component for Compose Multiplatform
 * Full feature parity with React Ant Design v5+
 *
 * The Ribbon component wraps content and displays a decorative ribbon
 * in one of four corner positions. Supports custom colors with theme
 * integration and includes a triangle fold effect.
 *
 * @param text Ribbon text content (required)
 * @param children Wrapped content to display
 * @param modifier Modifier to be applied to the root container
 * @param color Ribbon color (defaults to theme primary color)
 * @param placement Corner position for the ribbon (TopStart/TopEnd/BottomStart/BottomEnd)
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/ribbon">Ant Design Ribbon</a>
 */
@Composable
fun AntRibbon(
    text: String,
    children: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color? = null,
    placement: RibbonPlacement = RibbonPlacement.TopStart,
) {
    // Get theme from ConfigProvider for default color
    val theme = useTheme()
    val ribbonColor = color ?: theme.token.colorPrimary

    Box(modifier = modifier.fillMaxWidth()) {
        // Wrapped content - positioned relatively
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            children()
        }

        // Ribbon positioned at specified corner with triangle fold effect
        RibbonOverlay(
            text = text,
            color = ribbonColor,
            placement = placement,
            modifier = Modifier.matchParentSize()
        )
    }
}

/**
 * Ribbon overlay component with corner positioning and triangle fold
 * Internal component used by AntRibbon
 */
@Composable
private fun RibbonOverlay(
    text: String,
    color: Color,
    placement: RibbonPlacement,
    modifier: Modifier = Modifier,
) {
    // Ribbon dimensions
    val ribbonWidth = 100.dp
    val ribbonHeight = 30.dp
    val triangleSize = 12.dp

    Box(modifier = modifier) {
        // Ribbon container positioned at corner
        Box(
            modifier = Modifier
                .align(
                    when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.BottomStart -> Alignment.TopStart
                        RibbonPlacement.TopEnd, RibbonPlacement.BottomEnd -> Alignment.TopEnd
                    }
                )
                .offset(
                    x = when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.BottomStart -> (-ribbonWidth / 3)
                        RibbonPlacement.TopEnd, RibbonPlacement.BottomEnd -> (ribbonWidth / 3)
                    },
                    y = when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.TopEnd -> (-ribbonHeight / 2.5f)
                        RibbonPlacement.BottomStart, RibbonPlacement.BottomEnd -> (ribbonHeight / 2.5f)
                    }
                )
                .rotate(
                    when (placement) {
                        RibbonPlacement.TopStart -> -45f
                        RibbonPlacement.TopEnd -> 45f
                        RibbonPlacement.BottomStart -> 45f
                        RibbonPlacement.BottomEnd -> -45f
                    }
                )
                .width(ribbonWidth)
                .height(ribbonHeight)
                .background(color)
        ) {
            // Ribbon text
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            // Triangle fold effect in bottom-right corner of ribbon
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(triangleSize)
            ) {
                drawRibbonFold(
                    baseColor = color,
                    triangleSize = triangleSize.toPx()
                )
            }
        }
    }
}

/**
 * Draw the triangle fold effect at the corner of the ribbon
 * Creates a shadow/depth effect for the ribbon corner
 */
private fun DrawScope.drawRibbonFold(
    baseColor: Color,
    triangleSize: Float,
) {
    // Create a darker shade of the base color for the fold effect
    val foldColor = baseColor.copy(
        red = (baseColor.red * 0.8f).coerceIn(0f, 1f),
        green = (baseColor.green * 0.8f).coerceIn(0f, 1f),
        blue = (baseColor.blue * 0.8f).coerceIn(0f, 1f)
    )

    // Draw right-pointing triangle (fold at bottom-right corner)
    val trianglePath = Path().apply {
        moveTo(triangleSize, 0f) // Top-right
        lineTo(triangleSize, triangleSize) // Bottom-right
        lineTo(0f, 0f) // Top-left
        close()
    }

    drawPath(trianglePath, color = foldColor)

    // Optional: Draw subtle shadow line for depth
    drawLine(
        color = Color.Black.copy(alpha = 0.1f),
        start = Offset(triangleSize, 0f),
        end = Offset(0f, 0f),
        strokeWidth = 0.5f
    )
}

/**
 * Alternative Ribbon variant that displays above content
 * Useful for decorative purposes on cards or containers
 *
 * @param text Ribbon text content
 * @param color Ribbon color (defaults to theme primary color)
 * @param placement Corner position for the ribbon
 *
 * @since 1.0.0
 */
@Composable
fun AntRibbonSimple(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    placement: RibbonPlacement = RibbonPlacement.TopStart,
) {
    // Get theme from ConfigProvider for default color
    val theme = useTheme()
    val ribbonColor = color ?: theme.token.colorPrimary

    val ribbonWidth = 100.dp
    val ribbonHeight = 30.dp

    Box(modifier = modifier) {
        // Simple ribbon without triangle fold - positioned at corner
        Box(
            modifier = Modifier
                .align(
                    when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.BottomStart -> Alignment.TopStart
                        RibbonPlacement.TopEnd, RibbonPlacement.BottomEnd -> Alignment.TopEnd
                    }
                )
                .offset(
                    x = when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.BottomStart -> (-ribbonWidth / 3)
                        RibbonPlacement.TopEnd, RibbonPlacement.BottomEnd -> (ribbonWidth / 3)
                    },
                    y = when (placement) {
                        RibbonPlacement.TopStart, RibbonPlacement.TopEnd -> (-ribbonHeight / 2.5f)
                        RibbonPlacement.BottomStart, RibbonPlacement.BottomEnd -> (ribbonHeight / 2.5f)
                    }
                )
                .rotate(
                    when (placement) {
                        RibbonPlacement.TopStart -> -45f
                        RibbonPlacement.TopEnd -> 45f
                        RibbonPlacement.BottomStart -> 45f
                        RibbonPlacement.BottomEnd -> -45f
                    }
                )
                .width(ribbonWidth)
                .height(ribbonHeight)
                .background(ribbonColor)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
