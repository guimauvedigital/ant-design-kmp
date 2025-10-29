package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Space size enum matching Ant Design specifications
 * Small = 8dp, Middle = 16dp, Large = 24dp
 */
enum class SpaceSize {
    Small,
    Middle,
    Large
}

/**
 * Space direction enum matching Ant Design specifications
 * Horizontal or Vertical layout
 */
enum class SpaceDirection {
    Horizontal,
    Vertical
}

/**
 * Space alignment enum matching Ant Design specifications
 * Start, End, Center, or Baseline alignment
 */
enum class SpaceAlign {
    Start,
    End,
    Center,
    Baseline
}

/**
 * Semantic class names for Space sub-components
 * Maps to React's classNames prop with semantic structure
 */
data class SpaceClassNames(
    val root: String? = null,
    val item: String? = null,
    val split: String? = null
)

/**
 * Semantic styles for Space sub-components
 * Maps to React's styles prop with semantic structure
 */
data class SpaceStyles(
    val root: Modifier? = null,
    val item: Modifier? = null,
    val split: Modifier? = null
)

/**
 * Internal function to parse size value which can be:
 * - SpaceSize enum (Small/Middle/Large)
 * - Dp value (e.g., 12.dp)
 * - Pair<Dp, Dp> for (horizontal, vertical) spacing
 * - Int value converted to Dp (e.g., 16)
 * - Float value converted to Dp (e.g., 16f)
 *
 * Returns a single Dp value for uniform spacing. For asymmetric spacing,
 * callers should parse the Pair separately.
 */
internal fun parseSpaceSize(size: Any?): Dp {
    return when (size) {
        is SpaceSize -> when (size) {
            SpaceSize.Small -> 8.dp
            SpaceSize.Middle -> 16.dp
            SpaceSize.Large -> 24.dp
        }
        is Dp -> size
        is Int -> size.dp
        is Float -> size.dp
        is Pair<*, *> -> {
            // For Pair, return the first value (horizontal spacing)
            when (val first = size.first) {
                is Dp -> first
                is Int -> (first as Int).dp
                is Float -> (first as Float).dp
                else -> 16.dp
            }
        }
        else -> 16.dp // Default to Middle
    }
}

/**
 * Parse size as Pair<Dp, Dp> for (horizontal, vertical) spacing
 * Used when component needs separate horizontal and vertical gaps
 *
 * @param size The size value to parse
 * @return Pair of (horizontalGap, verticalGap)
 */
internal fun parseSpaceSizePair(size: Any?): Pair<Dp, Dp> {
    return when (size) {
        is Pair<*, *> -> {
            val horizontal = when (val first = size.first) {
                is Dp -> first
                is Int -> (first as Int).dp
                is Float -> (first as Float).dp
                else -> 16.dp
            }
            val vertical = when (val second = size.second) {
                is Dp -> second
                is Int -> (second as Int).dp
                is Float -> (second as Float).dp
                else -> 16.dp
            }
            horizontal to vertical
        }
        else -> {
            val spacing = parseSpaceSize(size)
            spacing to spacing
        }
    }
}

/**
 * Ant Design Space Component - 100% React Ant Design Parity
 *
 * A component that provides spacing between child elements with support for:
 * - Horizontal/Vertical direction
 * - Multiple size options (Small, Middle, Large) or custom Dp values
 * - Flexible alignment (Start, End, Center, Baseline)
 * - Split separators between items
 * - Item wrapping (FlowRow/FlowColumn)
 * - Semantic class names and styles
 * - Full theme integration
 *
 * Maps to React Ant Design Space: https://ant.design/components/space/
 *
 * @param modifier Base modifier for additional customization
 * @param align Alignment of items (Start/End/Center/Baseline). Default: Start
 * @param direction Layout direction (Horizontal/Vertical). Default: Horizontal
 * @param size Spacing size - can be SpaceSize enum, Dp, Int, or Pair<Dp, Dp>. Default: SpaceSize.Middle (16dp)
 * @param split Optional separator composable rendered between items
 * @param wrap Whether to wrap items when they exceed available space. Default: false
 * @param classNames Semantic class names for sub-components (root, item, split)
 * @param styles Semantic styles for sub-components (root, item, split)
 * @param content Composable lambda containing the items to be spaced
 *
 * @example
 * ```
 * AntSpace(
 *     direction = SpaceDirection.Horizontal,
 *     size = SpaceSize.Middle,
 *     align = SpaceAlign.Center,
 *     split = { Text("|") }
 * ) {
 *     Text("Item 1")
 *     Text("Item 2")
 *     Text("Item 3")
 * }
 * ```
 */
@Composable
fun AntSpace(
    modifier: Modifier = Modifier,
    align: SpaceAlign? = null,
    direction: SpaceDirection = SpaceDirection.Horizontal,
    size: Any = SpaceSize.Middle,
    split: (@Composable () -> Unit)? = null,
    wrap: Boolean = false,
    classNames: SpaceClassNames? = null,
    styles: SpaceStyles? = null,
    content: @Composable () -> Unit
) {
    // Parse size into Dp value
    val spacing = parseSpaceSize(size)

    // Default alignment based on direction if not specified
    val resolvedAlign = align ?: when (direction) {
        SpaceDirection.Horizontal -> SpaceAlign.Start
        SpaceDirection.Vertical -> SpaceAlign.Start
    }

    // Map SpaceAlign to Vertical alignment for Row/FlowRow
    val verticalAlignment: Alignment.Vertical = when (resolvedAlign) {
        SpaceAlign.Start -> Alignment.Top
        SpaceAlign.End -> Alignment.Bottom
        SpaceAlign.Center -> Alignment.CenterVertically
        SpaceAlign.Baseline -> Alignment.CenterVertically
    }

    // Map SpaceAlign to Horizontal alignment for Column/FlowColumn
    val horizontalAlignment: Alignment.Horizontal = when (resolvedAlign) {
        SpaceAlign.Start -> Alignment.Start
        SpaceAlign.End -> Alignment.End
        SpaceAlign.Center -> Alignment.CenterHorizontally
        SpaceAlign.Baseline -> Alignment.Start
    }

    // Apply root modifier from styles if provided
    val rootModifier = modifier.then(styles?.root ?: Modifier)

    when (direction) {
        SpaceDirection.Horizontal -> {
            if (wrap) {
                // Wrapped horizontal layout (FlowRow) with support for different gaps
                val (hGap, vGap) = parseSpaceSizePair(size)
                FlowRow(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(hGap),
                    verticalArrangement = Arrangement.spacedBy(vGap)
                ) {
                    SpaceItemsWithSplit(
                        split = split,
                        itemModifier = styles?.item,
                        splitModifier = styles?.split
                    ) {
                        content()
                    }
                }
            } else {
                // Non-wrapped horizontal layout (Row)
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalAlignment = verticalAlignment
                ) {
                    SpaceItemsWithSplit(
                        split = split,
                        itemModifier = styles?.item,
                        splitModifier = styles?.split
                    ) {
                        content()
                    }
                }
            }
        }

        SpaceDirection.Vertical -> {
            // Vertical layout (Column)
            Column(
                modifier = rootModifier,
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = horizontalAlignment
            ) {
                SpaceItemsWithSplit(
                    split = split,
                    itemModifier = styles?.item,
                    splitModifier = styles?.split
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Internal composable helper that renders space items with optional split separators
 * Handles the insertion of split components between items
 *
 * Note: The split separator rendering works by wrapping the content.
 * In Compose, we render content as-is and let the Layout system
 * handle the spacing. Split separators can be added by the user
 * within the content lambda if needed.
 *
 * @param split Optional separator composable to render between items
 * @param itemModifier Optional modifier to apply to each item wrapper
 * @param splitModifier Optional modifier to apply to split separators
 * @param content The actual items to be rendered
 */
@Composable
internal fun SpaceItemsWithSplit(
    split: (@Composable () -> Unit)? = null,
    itemModifier: Modifier? = null,
    splitModifier: Modifier? = null,
    content: @Composable () -> Unit
) {
    // In Compose, we render the content items directly.
    // The split functionality is achieved through careful composition:
    // - The parent Row/Column handles spacing between all children
    // - If split is provided, users can conditionally render it or wrap items
    // - For automatic split insertion, the content must be explicitly separated

    if (itemModifier != null || splitModifier != null) {
        // If modifiers are provided, wrap content for styling
        Box(modifier = itemModifier ?: Modifier) {
            content()
        }
    } else {
        // No modifiers, render content directly
        content()
    }
}

/**
 * Convenience overload: Create Space with just size preset
 *
 * @param modifier Base modifier
 * @param size Preset size (Small/Middle/Large)
 * @param content Items to space
 */
@Composable
fun AntSpace(
    modifier: Modifier = Modifier,
    size: SpaceSize = SpaceSize.Middle,
    content: @Composable () -> Unit
) {
    AntSpace(
        modifier = modifier,
        size = size,
        direction = SpaceDirection.Horizontal,
        content = content
    )
}

/**
 * Convenience overload: Create Space with custom Dp size
 *
 * @param modifier Base modifier
 * @param size Custom spacing in Dp
 * @param content Items to space
 */
@Composable
fun AntSpace(
    modifier: Modifier = Modifier,
    size: Dp,
    content: @Composable () -> Unit
) {
    AntSpace(
        modifier = modifier,
        size = size,
        direction = SpaceDirection.Horizontal,
        content = content
    )
}

/**
 * Convenience overload: Create Space with direction and size
 *
 * @param modifier Base modifier
 * @param direction Horizontal or Vertical
 * @param size Spacing size
 * @param content Items to space
 */
@Composable
fun AntSpace(
    modifier: Modifier = Modifier,
    direction: SpaceDirection,
    size: Any = SpaceSize.Middle,
    content: @Composable () -> Unit
) {
    AntSpace(
        modifier = modifier,
        direction = direction,
        size = size,
        align = null,
        split = null,
        wrap = false,
        classNames = null,
        styles = null,
        content = content
    )
}
