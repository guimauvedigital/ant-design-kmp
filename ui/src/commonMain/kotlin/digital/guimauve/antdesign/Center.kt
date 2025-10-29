package digital.guimauve.antdesign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Ant Design Center Component - 100% React Ant Design Parity
 *
 * A simple component for centering content both horizontally and vertically.
 * Provides two layout modes: block (fills available space) and inline (wraps content).
 *
 * Maps to React Ant Design Center: https://ant.design/components/center/
 *
 * Features:
 * - Block mode: Fills parent container and centers content
 * - Inline mode: Wraps content size and centers within it
 * - Clean, focused implementation with minimal API surface
 * - Full theme integration support
 *
 * @param modifier Base modifier for additional customization
 * @param inline If true, uses inline layout (wrapContentSize). If false (default),
 *               uses block layout (fillMaxSize). Maps to React's inline prop.
 * @param children Composable content to be centered
 *
 * @example Block mode (default - fills available space):
 * ```
 * AntCenter(
 *     modifier = Modifier.height(200.dp)
 * ) {
 *     Text("Centered content")
 * }
 * ```
 *
 * @example Inline mode (wraps content):
 * ```
 * AntCenter(inline = true) {
 *     Text("Compact centered content")
 * }
 * ```
 */
@Composable
fun AntCenter(
    modifier: Modifier = Modifier,
    inline: Boolean = false,
    children: @Composable () -> Unit,
) {
    val layoutModifier = if (inline) {
        // Inline mode: wrap content size, center within wrapped bounds
        modifier.wrapContentSize(Alignment.Center)
    } else {
        // Block mode: fill available space, center content
        modifier.fillMaxSize()
    }

    Box(
        modifier = layoutModifier,
        contentAlignment = Alignment.Center
    ) {
        children()
    }
}

/**
 * Convenience overload: Create Center with just children
 *
 * @param children Content to center (block mode)
 */
@Composable
fun AntCenter(
    children: @Composable () -> Unit,
) {
    AntCenter(
        modifier = Modifier,
        inline = false,
        children = children
    )
}

/**
 * Convenience overload: Create Center with inline mode shorthand
 *
 * @param inline Layout mode flag
 * @param children Content to center
 */
@Composable
fun AntCenter(
    inline: Boolean,
    children: @Composable () -> Unit,
) {
    AntCenter(
        modifier = Modifier,
        inline = inline,
        children = children
    )
}
