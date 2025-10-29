package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ant Design Container component for responsive content layout
 *
 * The Container component provides a fixed-width content area with optional centering,
 * padding, and background customization. It follows Ant Design's responsive layout patterns.
 *
 * Key Features:
 * - Responsive max-width constraint (default 1200.dp matching React Ant Design)
 * - Horizontal centering with optional disable
 * - Flexible padding configuration (Dp or PaddingValues)
 * - Customizable background color
 * - Full width parent constraint with centered inner content
 *
 * @param modifier Modifier for the container's outer Box
 * @param maxWidth Maximum width of the contained content (default: 1200.dp from Ant Design theme)
 * @param centerContent Whether to horizontally center the content (default: true)
 * @param padding Inner padding for the content (default: 24.dp)
 * @param paddingValues Alternative to padding parameter, using PaddingValues for fine-grained control
 * @param background Background color of the container (default: transparent)
 * @param children Composable content to be displayed within the container
 *
 * Example usage:
 * ```
 * AntContainer(
 *     maxWidth = 1200.dp,
 *     centerContent = true,
 *     padding = 24.dp
 * ) {
 *     Text("Container content")
 * }
 * ```
 *
 * Example with PaddingValues:
 * ```
 * AntContainer(
 *     maxWidth = 1000.dp,
 *     paddingValues = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
 * ) {
 *     Text("Content with custom padding")
 * }
 * ```
 */
@Composable
fun AntContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 1200.dp,
    centerContent: Boolean = true,
    padding: Dp? = null,
    paddingValues: PaddingValues? = null,
    background: Color = Color.Transparent,
    children: @Composable () -> Unit,
) {
    // Resolve padding: paddingValues takes precedence, fallback to padding Dp, then default
    val resolvedPaddingValues = paddingValues ?: (
            padding?.let { PaddingValues(it) } ?: PaddingValues(24.dp)
            )

    // Outer container: fills max width, centers content horizontally
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(background),
        contentAlignment = if (centerContent) Alignment.Center else Alignment.TopStart
    ) {
        // Inner container: constrained width with padding
        Box(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .padding(resolvedPaddingValues)
        ) {
            children()
        }
    }
}

/**
 * Convenience overload for Container with single Dp padding value
 *
 * @param modifier Modifier for the container
 * @param maxWidth Maximum width constraint
 * @param centerContent Whether to center content horizontally
 * @param padding Uniform padding for all sides
 * @param background Background color
 * @param children Content
 */
@Composable
fun AntContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 1200.dp,
    centerContent: Boolean = true,
    padding: Dp = 24.dp,
    background: Color = Color.Transparent,
    children: @Composable () -> Unit,
) {
    AntContainer(
        modifier = modifier,
        maxWidth = maxWidth,
        centerContent = centerContent,
        padding = padding,
        paddingValues = null,
        background = background,
        children = children
    )
}
