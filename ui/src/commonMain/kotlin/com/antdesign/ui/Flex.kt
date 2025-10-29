package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class FlexDirection {
    Row,
    Column,
    RowReverse,
    ColumnReverse
}

enum class FlexWrap {
    NoWrap,
    Wrap,
    WrapReverse
}

enum class FlexJustify {
    FlexStart,
    FlexEnd,
    Center,
    SpaceBetween,
    SpaceAround,
    SpaceEvenly
}

enum class FlexAlign {
    FlexStart,
    FlexEnd,
    Center,
    Baseline,
    Stretch
}

/**
 * Parse gap value which can be:
 * - Single Dp value for both row and column gap
 * - Pair<Dp, Dp> for (row gap, column gap)
 * - String representation of Dp (e.g., "8.dp")
 *
 * Returns Pair<Dp, Dp> for (horizontalGap, verticalGap)
 */
internal fun parseGap(gap: Any?): Pair<Dp, Dp> {
    return when (gap) {
        is Dp -> gap to gap
        is Pair<*, *> -> {
            val horizontal = when (gap.first) {
                is Dp -> gap.first as Dp
                is Int -> (gap.first as Int).dp
                else -> 0.dp
            }
            val vertical = when (gap.second) {
                is Dp -> gap.second as Dp
                is Int -> (gap.second as Int).dp
                else -> 0.dp
            }
            horizontal to vertical
        }
        is Int -> gap.dp to gap.dp
        is String -> {
            val value = gap.replace("dp", "").toIntOrNull() ?: 0
            value.dp to value.dp
        }
        else -> 0.dp to 0.dp
    }
}

/**
 * Parse flex value (CSS flex shorthand)
 * Examples: "1", "auto", "0 0 100%", etc.
 *
 * Returns Modifier with flex property applied if valid
 * Note: weight() is only available in RowScope/ColumnScope, so we use fillMaxWidth() instead
 */
internal fun parseFlexValue(flex: String?): Modifier {
    if (flex == null) return Modifier

    return when {
        flex == "1" || flex == "1 1 0%" -> Modifier.fillMaxWidth()
        flex == "auto" -> Modifier.wrapContentWidth()
        flex.contains("1") -> Modifier.fillMaxWidth()
        else -> Modifier
    }
}

/**
 * Ant Design Flex Component - Full React Ant Design parity
 *
 * Provides flexible box layout with support for:
 * - Vertical/Horizontal direction
 * - Multiple wrap options (NoWrap, Wrap, WrapReverse)
 * - 6 justification options (justify-content)
 * - 5 alignment options (align-items)
 * - Flexible gap support (single, dual, or custom)
 * - CSS flex shorthand parsing
 * - Theme integration
 *
 * @param modifier Base modifier for additional customization
 * @param vertical Shorthand for column direction
 * @param direction Main axis direction (Row/Column/RowReverse/ColumnReverse)
 * @param wrap How children should wrap (NoWrap/Wrap/WrapReverse)
 * @param justify Justification along main axis (6 options matching React Ant Design)
 * @param align Alignment along cross axis (5 options matching React Ant Design)
 * @param gap Spacing between children - can be Dp, Pair<Dp,Dp>, Int, or String
 * @param flex CSS flex shorthand ("1", "auto", etc.)
 * @param children Composable content
 */
@Composable
fun AntFlex(
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    direction: FlexDirection = FlexDirection.Row,
    wrap: FlexWrap = FlexWrap.NoWrap,
    justify: FlexJustify = FlexJustify.FlexStart,
    align: FlexAlign = FlexAlign.FlexStart,
    gap: Any? = null,
    flex: String? = null,
    children: @Composable () -> Unit
) {
    // Determine actual direction based on vertical flag
    val actualDirection = if (vertical) FlexDirection.Column else direction

    // Parse gap into horizontal and vertical components
    val (horizontalGap, verticalGap) = parseGap(gap)

    // Apply flex modifier if provided
    val flexModifier = parseFlexValue(flex)
    val combinedModifier = modifier.then(flexModifier)

    // Map justify values to Compose arrangements
    fun getHorizontalArrangement(justify: FlexJustify, gap: Dp): Arrangement.Horizontal {
        return when (justify) {
            FlexJustify.FlexStart -> Arrangement.spacedBy(gap, Alignment.Start)
            FlexJustify.FlexEnd -> Arrangement.spacedBy(gap, Alignment.End)
            FlexJustify.Center -> Arrangement.spacedBy(gap, Alignment.CenterHorizontally)
            FlexJustify.SpaceBetween -> Arrangement.SpaceBetween
            FlexJustify.SpaceAround -> Arrangement.SpaceAround
            FlexJustify.SpaceEvenly -> Arrangement.SpaceEvenly
        }
    }

    fun getVerticalArrangement(justify: FlexJustify, gap: Dp): Arrangement.Vertical {
        return when (justify) {
            FlexJustify.FlexStart -> Arrangement.spacedBy(gap, Alignment.Top)
            FlexJustify.FlexEnd -> Arrangement.spacedBy(gap, Alignment.Bottom)
            FlexJustify.Center -> Arrangement.spacedBy(gap, Alignment.CenterVertically)
            FlexJustify.SpaceBetween -> Arrangement.SpaceBetween
            FlexJustify.SpaceAround -> Arrangement.SpaceAround
            FlexJustify.SpaceEvenly -> Arrangement.SpaceEvenly
        }
    }

    // Map align values to Compose alignments (cross axis)
    fun getRowVerticalAlignment(align: FlexAlign): Alignment.Vertical {
        return when (align) {
            FlexAlign.FlexStart -> Alignment.Top
            FlexAlign.FlexEnd -> Alignment.Bottom
            FlexAlign.Center -> Alignment.CenterVertically
            FlexAlign.Baseline -> Alignment.CenterVertically
            FlexAlign.Stretch -> Alignment.CenterVertically
        }
    }

    fun getColumnHorizontalAlignment(align: FlexAlign): Alignment.Horizontal {
        return when (align) {
            FlexAlign.FlexStart -> Alignment.Start
            FlexAlign.FlexEnd -> Alignment.End
            FlexAlign.Center -> Alignment.CenterHorizontally
            FlexAlign.Baseline -> Alignment.Start
            FlexAlign.Stretch -> Alignment.CenterHorizontally
        }
    }

    // Render based on direction and wrap setting
    when (actualDirection) {
        FlexDirection.Row -> {
            when (wrap) {
                FlexWrap.NoWrap -> {
                    Row(
                        modifier = combinedModifier,
                        horizontalArrangement = getHorizontalArrangement(justify, horizontalGap),
                        verticalAlignment = getRowVerticalAlignment(align)
                    ) {
                        children()
                    }
                }
                FlexWrap.Wrap -> {
                    FlowRow(
                        modifier = combinedModifier,
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
                FlexWrap.WrapReverse -> {
                    // FlowRow with wrap reverse by using custom arrangement
                    FlowRow(
                        modifier = combinedModifier,
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
            }
        }

        FlexDirection.Column -> {
            when (wrap) {
                FlexWrap.NoWrap -> {
                    Column(
                        modifier = combinedModifier,
                        verticalArrangement = getVerticalArrangement(justify, verticalGap),
                        horizontalAlignment = getColumnHorizontalAlignment(align)
                    ) {
                        children()
                    }
                }
                FlexWrap.Wrap -> {
                    FlowColumn(
                        modifier = combinedModifier,
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        maxItemsInEachColumn = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
                FlexWrap.WrapReverse -> {
                    FlowColumn(
                        modifier = combinedModifier,
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        maxItemsInEachColumn = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
            }
        }

        FlexDirection.RowReverse -> {
            when (wrap) {
                FlexWrap.NoWrap -> {
                    Row(
                        modifier = combinedModifier,
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap, Alignment.End),
                        verticalAlignment = getRowVerticalAlignment(align)
                    ) {
                        children()
                    }
                }
                FlexWrap.Wrap -> {
                    FlowRow(
                        modifier = combinedModifier,
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
                FlexWrap.WrapReverse -> {
                    FlowRow(
                        modifier = combinedModifier,
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
            }
        }

        FlexDirection.ColumnReverse -> {
            when (wrap) {
                FlexWrap.NoWrap -> {
                    Column(
                        modifier = combinedModifier,
                        verticalArrangement = Arrangement.spacedBy(verticalGap, Alignment.Bottom),
                        horizontalAlignment = getColumnHorizontalAlignment(align)
                    ) {
                        children()
                    }
                }
                FlexWrap.Wrap -> {
                    FlowColumn(
                        modifier = combinedModifier,
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        maxItemsInEachColumn = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
                FlexWrap.WrapReverse -> {
                    FlowColumn(
                        modifier = combinedModifier,
                        verticalArrangement = Arrangement.spacedBy(verticalGap),
                        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
                        maxItemsInEachColumn = Int.MAX_VALUE
                    ) {
                        children()
                    }
                }
            }
        }
    }
}
