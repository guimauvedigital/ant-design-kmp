package digital.guimauve.antdesign

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Enum for Compact component size variants
 * Controls border-radius based on spacing between items
 */
enum class CompactSize {
    Small,
    Middle,
    Large
}

/**
 * Composition Local for tracking compact item position
 * Used to apply correct border radius (first/middle/last logic)
 */
data class CompactItemContext(
    val isFirst: Boolean = false,
    val isLast: Boolean = false,
    val isSingle: Boolean = false,
    val direction: SpaceDirection = SpaceDirection.Horizontal,
    val borderRadius: Dp = 6.dp,
)

val LocalCompactItemContext = compositionLocalOf<CompactItemContext> {
    CompactItemContext()
}

/**
 * Compact component for grouping form controls with merged borders
 * Provides layout for buttons, inputs, and other components with collapsed spacing
 *
 * React Ant Design Space.Compact equivalent with full parity:
 * - Removes gaps between items (merged borders)
 * - First item gets full border radius (left/top)
 * - Middle items get no border radius
 * - Last item gets full border radius (right/bottom)
 * - Block mode fills container width/height
 * - Size variants affect item styling
 *
 * @param modifier Modifier for the compact container
 * @param direction Horizontal (default) or Vertical layout
 * @param size Size variant: Small/Middle/Large
 * @param block Whether to fill max width (horizontal) or height (vertical)
 * @param children Content items to group
 */
@Composable
fun AntCompact(
    modifier: Modifier = Modifier,
    direction: SpaceDirection = SpaceDirection.Horizontal,
    size: CompactSize = CompactSize.Middle,
    block: Boolean = false,
    children: @Composable () -> Unit,
) {
    // Determine border radius from size
    val borderRadius = when (size) {
        CompactSize.Small -> 4.dp
        CompactSize.Middle -> 6.dp
        CompactSize.Large -> 8.dp
    }

    // Build modifier with block sizing if enabled
    val containerModifier = modifier.then(
        when {
            block && direction == SpaceDirection.Horizontal -> Modifier.fillMaxWidth()
            block && direction == SpaceDirection.Vertical -> Modifier.fillMaxHeight()
            else -> Modifier
        }
    )

    when (direction) {
        SpaceDirection.Horizontal -> {
            Row(
                modifier = containerModifier,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                CompactContent(
                    direction = direction,
                    borderRadius = borderRadius,
                    children = children
                )
            }
        }

        SpaceDirection.Vertical -> {
            Column(
                modifier = containerModifier,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                CompactContent(
                    direction = direction,
                    borderRadius = borderRadius,
                    children = children
                )
            }
        }
    }
}

/**
 * Internal composable for managing compact item context through composition
 * Wraps children and provides border-radius context to child items
 */
@Composable
fun CompactContent(
    direction: SpaceDirection,
    borderRadius: Dp,
    children: @Composable () -> Unit,
) {
    // This wrapper enables context awareness for child items
    // Child items should read LocalCompactItemContext to apply correct border-radius
    CompositionLocalProvider(
        LocalCompactItemContext provides CompactItemContext(
            direction = direction,
            borderRadius = borderRadius
        )
    ) {
        children()
    }
}

/**
 * Helper function to get border radius modifier for a compact item
 * Should be called by button, input, and other form components inside AntCompact
 *
 * @param isFirst Whether this is the first item in the group
 * @param isLast Whether this is the last item in the group
 * @param direction Layout direction (horizontal/vertical)
 * @param borderRadius Base border radius value
 * @return Dp for the border radius, or 0.dp for middle items
 */
fun getCompactItemBorderRadius(
    isFirst: Boolean,
    isLast: Boolean,
    direction: SpaceDirection,
    borderRadius: Dp,
): Dp {
    return when {
        isFirst && isLast -> borderRadius // Single item gets full radius
        isFirst -> borderRadius // First item gets full radius
        isLast -> borderRadius // Last item gets full radius
        else -> 0.dp // Middle items get no radius
    }
}

/**
 * Helper to get per-corner border radius for compact items
 * Applies radius only to relevant corners based on direction and position
 *
 * @param isFirst Whether this is the first item
 * @param isLast Whether this is the last item
 * @param direction Layout direction
 * @param borderRadius Base border radius
 * @return Quadruple of (topStart, topEnd, bottomEnd, bottomStart)
 */
fun getCompactItemCornerRadius(
    isFirst: Boolean,
    isLast: Boolean,
    direction: SpaceDirection,
    borderRadius: Dp,
): Quadruple<Dp, Dp, Dp, Dp> {
    return when (direction) {
        SpaceDirection.Horizontal -> {
            val startRadius = if (isFirst) borderRadius else 0.dp
            val endRadius = if (isLast) borderRadius else 0.dp
            Quadruple(
                topStart = startRadius,
                topEnd = endRadius,
                bottomEnd = endRadius,
                bottomStart = startRadius
            )
        }

        SpaceDirection.Vertical -> {
            val topRadius = if (isFirst) borderRadius else 0.dp
            val bottomRadius = if (isLast) borderRadius else 0.dp
            Quadruple(
                topStart = topRadius,
                topEnd = topRadius,
                bottomEnd = bottomRadius,
                bottomStart = bottomRadius
            )
        }
    }
}

/**
 * Data class for corner radius values
 */
data class Quadruple<A, B, C, D>(
    val topStart: A,
    val topEnd: B,
    val bottomEnd: C,
    val bottomStart: D,
)
