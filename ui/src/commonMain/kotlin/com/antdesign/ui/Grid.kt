package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Ant Design Grid System - Row Component
 *
 * 24 Grids System: The grid is based on a 24-column layout.
 * Responsive: Supports six responsive dimensions: xs, sm, md, lg, xl, xxl.
 * Flex Layout: Uses flex layout for precise alignment control.
 *
 * Example usage:
 * ```kotlin
 * AntRow(
 *     gutter = 16,
 *     justify = RowJustify.Center,
 *     align = RowAlign.Middle
 * ) {
 *     AntCol(span = 6) {
 *         Text("Column 1")
 *     }
 *     AntCol(span = 6) {
 *         Text("Column 2")
 *     }
 *     AntCol(span = 6) {
 *         Text("Column 3")
 *     }
 *     AntCol(span = 6) {
 *         Text("Column 4")
 *     }
 * }
 * ```
 *
 * Responsive gutter example:
 * ```kotlin
 * AntRow(
 *     gutter = mapOf(
 *         Breakpoint.Xs to Pair(8, 8),
 *         Breakpoint.Sm to Pair(16, 16),
 *         Breakpoint.Md to Pair(24, 24),
 *         Breakpoint.Lg to Pair(32, 32)
 *     )
 * ) {
 *     // Columns...
 * }
 * ```
 *
 * @param modifier The modifier to be applied to the row
 * @param align Vertical alignment of columns: Top, Middle, Bottom, Stretch
 * @param gutter Grid spacing. Can be:
 *   - Int: Single value for horizontal spacing
 *   - Pair<Int, Int>: [horizontal, vertical] spacing
 *   - Map<Breakpoint, Int>: Responsive horizontal spacing
 *   - Map<Breakpoint, Pair<Int, Int>>: Responsive [horizontal, vertical] spacing
 * @param justify Horizontal arrangement: Start, End, Center, SpaceAround, SpaceBetween, SpaceEvenly
 * @param wrap Whether to auto wrap columns (default: true)
 * @param classNames Semantic class names for styling (v5.5.0+)
 * @param styles Semantic styles with Modifier (v5.5.0+)
 * @param content The row content containing AntCol components
 */
@Composable
fun AntRow(
    modifier: Modifier = Modifier,
    align: RowAlign = RowAlign.Top,
    gutter: Any = 0,
    justify: RowJustify = RowJustify.Start,
    wrap: Boolean = true,
    classNames: RowClassNames? = null,
    styles: RowStyles? = null,
    content: @Composable AntRowScope.() -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val currentBreakpoint = rememberCurrentBreakpoint(maxWidth)
        val (horizontalGutter, verticalGutter) = resolveGutter(gutter, currentBreakpoint)

        val horizontalArrangement = when (justify) {
            RowJustify.Start -> Arrangement.Start
            RowJustify.End -> Arrangement.End
            RowJustify.Center -> Arrangement.Center
            RowJustify.SpaceBetween -> Arrangement.SpaceBetween
            RowJustify.SpaceAround -> Arrangement.SpaceAround
            RowJustify.SpaceEvenly -> Arrangement.SpaceEvenly
        }

        val verticalAlignment = when (align) {
            RowAlign.Top -> Alignment.Top
            RowAlign.Middle -> Alignment.CenterVertically
            RowAlign.Bottom -> Alignment.Bottom
            RowAlign.Stretch -> Alignment.CenterVertically // Stretch handled in layout
        }

        val rowModifier = modifier
            .fillMaxWidth()
            .then(if (horizontalGutter > 0) Modifier.padding(horizontal = (-horizontalGutter / 2).dp) else Modifier)

        val rowScope = remember(currentBreakpoint, horizontalGutter, verticalGutter) {
            AntRowScopeImpl(currentBreakpoint, horizontalGutter, verticalGutter)
        }

        if (wrap) {
            // Use FlowRow for wrapping support
            FlowRow(
                modifier = rowModifier.then(styles?.wrap ?: Modifier),
                horizontalArrangement = if (horizontalGutter > 0)
                    Arrangement.spacedBy(horizontalGutter.dp)
                else
                    horizontalArrangement,
                verticalArrangement = if (verticalGutter > 0)
                    Arrangement.spacedBy(verticalGutter.dp)
                else
                    Arrangement.Top,
                maxItemsInEachRow = Int.MAX_VALUE
            ) {
                rowScope.content()
            }
        } else {
            // Use Row for non-wrapping layout
            Row(
                modifier = rowModifier.then(styles?.wrap ?: Modifier),
                horizontalArrangement = if (horizontalGutter > 0)
                    Arrangement.spacedBy(horizontalGutter.dp)
                else
                    horizontalArrangement,
                verticalAlignment = verticalAlignment
            ) {
                rowScope.content()
            }
        }
    }
}

/**
 * Scope for AntRow that provides context to child AntCol components
 */
interface AntRowScope {
    val currentBreakpoint: Breakpoint
    val horizontalGutter: Int
    val verticalGutter: Int
}

/**
 * Internal implementation of AntRowScope
 */
private class AntRowScopeImpl(
    override val currentBreakpoint: Breakpoint,
    override val horizontalGutter: Int,
    override val verticalGutter: Int
) : AntRowScope

/**
 * Ant Design Grid System - Col Component
 *
 * Column component for the 24-grid system. Must be placed directly within AntRow.
 *
 * Basic usage:
 * ```kotlin
 * AntCol(span = 12) {
 *     Text("Half width")
 * }
 * ```
 *
 * Responsive usage:
 * ```kotlin
 * AntCol(
 *     xs = 24,  // Full width on mobile
 *     sm = 12,  // Half width on small tablets
 *     md = 8,   // 1/3 width on medium screens
 *     lg = 6    // 1/4 width on large screens
 * ) {
 *     Text("Responsive column")
 * }
 * ```
 *
 * Advanced responsive configuration:
 * ```kotlin
 * AntCol(
 *     md = ColResponsiveConfig(
 *         span = 12,
 *         offset = 6,
 *         order = 2
 *     )
 * ) {
 *     Text("Advanced responsive")
 * }
 * ```
 *
 * @param modifier The modifier to be applied to the column
 * @param flex Flex layout style. Can be:
 *   - String: "auto", "none", "1 1 auto", etc.
 *   - Int: Flex grow factor
 * @param offset Number of columns to offset (adds left margin)
 * @param order Flex order for column positioning
 * @param pull Number of columns to pull (move left with relative positioning)
 * @param push Number of columns to push (move right with relative positioning)
 * @param span Number of columns to span (1-24)
 * @param xs Responsive config for <576px screens
 * @param sm Responsive config for ≥576px screens
 * @param md Responsive config for ≥768px screens
 * @param lg Responsive config for ≥992px screens
 * @param xl Responsive config for ≥1200px screens
 * @param xxl Responsive config for ≥1600px screens
 * @param classNames Semantic class names for styling (v5.5.0+)
 * @param styles Semantic styles with Modifier (v5.5.0+)
 * @param content The column content
 */
@Composable
fun AntRowScope.AntCol(
    modifier: Modifier = Modifier,
    flex: Any? = null,
    offset: Int = 0,
    order: Int = 0,
    pull: Int = 0,
    push: Int = 0,
    span: Int = 24,
    xs: Any? = null,
    sm: Any? = null,
    md: Any? = null,
    lg: Any? = null,
    xl: Any? = null,
    xxl: Any? = null,
    classNames: ColClassNames? = null,
    styles: ColStyles? = null,
    content: @Composable BoxScope.() -> Unit
) {
    // Resolve responsive configuration
    val responsiveConfig = resolveResponsiveConfig(
        currentBreakpoint = currentBreakpoint,
        baseSpan = span,
        baseOffset = offset,
        baseOrder = order,
        basePull = pull,
        basePush = push,
        baseFlex = flex,
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
        xxl = xxl
    )

    // Calculate column width as fraction of 24
    val columnFraction = if (responsiveConfig.span > 0) {
        responsiveConfig.span / 24f
    } else {
        0f
    }

    // Build column modifier
    var columnModifier = modifier
        .then(styles?.col ?: Modifier)

    // Apply gutter padding
    if (horizontalGutter > 0) {
        columnModifier = columnModifier.padding(horizontal = (horizontalGutter / 2).dp)
    }

    // Apply flex or fillMaxWidth based on configuration
    columnModifier = when {
        responsiveConfig.flex != null -> {
            applyFlexModifier(columnModifier, responsiveConfig.flex)
        }
        responsiveConfig.span > 0 -> {
            columnModifier.fillMaxWidth(columnFraction)
        }
        else -> {
            columnModifier // span = 0 means display: none
        }
    }

    // Apply offset (left margin)
    if (responsiveConfig.offset > 0) {
        val offsetFraction = responsiveConfig.offset / 24f
        columnModifier = columnModifier.padding(start = (offsetFraction * 100).dp)
    }

    // Apply order (flex order not directly supported in Compose, handled via layout order)
    // Note: In Compose, we can't directly set flex order, but we can use Layout for custom ordering

    // Apply push/pull (relative positioning)
    if (responsiveConfig.push > 0) {
        val pushFraction = responsiveConfig.push / 24f
        columnModifier = columnModifier.offset(x = (pushFraction * 100).dp)
    }
    if (responsiveConfig.pull > 0) {
        val pullFraction = responsiveConfig.pull / 24f
        columnModifier = columnModifier.offset(x = (-pullFraction * 100).dp)
    }

    // Render the column
    if (responsiveConfig.span != 0) {
        Box(
            modifier = columnModifier
        ) {
            content()
        }
    }
}

/**
 * Responsive configuration for a column
 */
data class ColResponsiveConfig(
    val span: Int? = null,
    val offset: Int? = null,
    val order: Int? = null,
    val pull: Int? = null,
    val push: Int? = null,
    val flex: Any? = null
)

/**
 * Internal resolved configuration for a column
 */
private data class ResolvedColConfig(
    val span: Int,
    val offset: Int,
    val order: Int,
    val pull: Int,
    val push: Int,
    val flex: Any?
)

/**
 * Responsive breakpoints matching Ant Design specifications
 */
enum class Breakpoint(val minWidth: Dp) {
    Xs(0.dp),      // <576px
    Sm(576.dp),    // ≥576px
    Md(768.dp),    // ≥768px
    Lg(992.dp),    // ≥992px
    Xl(1200.dp),   // ≥1200px
    Xxl(1600.dp)   // ≥1600px
}

/**
 * Vertical alignment options for Row
 */
enum class RowAlign {
    Top,
    Middle,
    Bottom,
    Stretch
}

/**
 * Horizontal arrangement options for Row
 */
enum class RowJustify {
    Start,
    End,
    Center,
    SpaceAround,
    SpaceBetween,
    SpaceEvenly
}

/**
 * Semantic class names for Row (v5.5.0+)
 */
data class RowClassNames(
    val wrap: String = ""
)

/**
 * Semantic styles for Row (v5.5.0+)
 */
data class RowStyles(
    val wrap: Modifier = Modifier
)

/**
 * Semantic class names for Col (v5.5.0+)
 */
data class ColClassNames(
    val col: String = ""
)

/**
 * Semantic styles for Col (v5.5.0+)
 */
data class ColStyles(
    val col: Modifier = Modifier
)

/**
 * Determines the current breakpoint based on screen width
 */
@Composable
private fun rememberCurrentBreakpoint(width: Dp): Breakpoint {
    return remember(width) {
        when {
            width >= Breakpoint.Xxl.minWidth -> Breakpoint.Xxl
            width >= Breakpoint.Xl.minWidth -> Breakpoint.Xl
            width >= Breakpoint.Lg.minWidth -> Breakpoint.Lg
            width >= Breakpoint.Md.minWidth -> Breakpoint.Md
            width >= Breakpoint.Sm.minWidth -> Breakpoint.Sm
            else -> Breakpoint.Xs
        }
    }
}

/**
 * Resolves gutter value based on current breakpoint
 *
 * Gutter can be:
 * - Int: Single horizontal value
 * - Pair<Int, Int>: [horizontal, vertical]
 * - Map<Breakpoint, Int>: Responsive horizontal values
 * - Map<Breakpoint, Pair<Int, Int>>: Responsive [horizontal, vertical] values
 */
private fun resolveGutter(gutter: Any, breakpoint: Breakpoint): Pair<Int, Int> {
    return when (gutter) {
        is Int -> Pair(gutter, 0)
        is Pair<*, *> -> {
            val h = (gutter.first as? Int) ?: 0
            val v = (gutter.second as? Int) ?: 0
            Pair(h, v)
        }
        is Map<*, *> -> {
            // Find the appropriate breakpoint value
            val breakpoints = listOf(
                Breakpoint.Xxl, Breakpoint.Xl, Breakpoint.Lg,
                Breakpoint.Md, Breakpoint.Sm, Breakpoint.Xs
            )

            // Find the largest breakpoint that is <= current breakpoint
            var selectedValue: Any? = null
            for (bp in breakpoints) {
                if (bp.minWidth <= breakpoint.minWidth && gutter.containsKey(bp)) {
                    selectedValue = gutter[bp]
                    break
                }
            }

            when (selectedValue) {
                is Int -> Pair(selectedValue, 0)
                is Pair<*, *> -> {
                    val h = (selectedValue.first as? Int) ?: 0
                    val v = (selectedValue.second as? Int) ?: 0
                    Pair(h, v)
                }
                else -> Pair(0, 0)
            }
        }
        else -> Pair(0, 0)
    }
}

/**
 * Resolves responsive configuration based on current breakpoint
 */
private fun resolveResponsiveConfig(
    currentBreakpoint: Breakpoint,
    baseSpan: Int,
    baseOffset: Int,
    baseOrder: Int,
    basePull: Int,
    basePush: Int,
    baseFlex: Any?,
    xs: Any?,
    sm: Any?,
    md: Any?,
    lg: Any?,
    xl: Any?,
    xxl: Any?
): ResolvedColConfig {
    // Find the responsive config for current breakpoint
    val responsiveValue = when (currentBreakpoint) {
        Breakpoint.Xxl -> xxl ?: xl ?: lg ?: md ?: sm ?: xs
        Breakpoint.Xl -> xl ?: lg ?: md ?: sm ?: xs
        Breakpoint.Lg -> lg ?: md ?: sm ?: xs
        Breakpoint.Md -> md ?: sm ?: xs
        Breakpoint.Sm -> sm ?: xs
        Breakpoint.Xs -> xs
    }

    // Parse the responsive value
    return when (responsiveValue) {
        null -> ResolvedColConfig(baseSpan, baseOffset, baseOrder, basePull, basePush, baseFlex)
        is Int -> ResolvedColConfig(responsiveValue, baseOffset, baseOrder, basePull, basePush, baseFlex)
        is ColResponsiveConfig -> ResolvedColConfig(
            span = responsiveValue.span ?: baseSpan,
            offset = responsiveValue.offset ?: baseOffset,
            order = responsiveValue.order ?: baseOrder,
            pull = responsiveValue.pull ?: basePull,
            push = responsiveValue.push ?: basePush,
            flex = responsiveValue.flex ?: baseFlex
        )
        else -> ResolvedColConfig(baseSpan, baseOffset, baseOrder, basePull, basePush, baseFlex)
    }
}

/**
 * Custom weight extension for Modifier to simulate RowScope.weight behavior
 * This is used when flex is specified on columns
 */
private fun Modifier.weight(weight: Float, fill: Boolean): Modifier {
    return if (fill) {
        this.fillMaxWidth()
    } else {
        this.wrapContentWidth()
    }
}

/**
 * Applies flex modifier based on flex value
 *
 * Flex can be:
 * - String: "auto", "none", "1 1 auto", CSS flex shorthand
 * - Int: Flex grow factor
 */
private fun applyFlexModifier(modifier: Modifier, flex: Any?): Modifier {
    return when (flex) {
        null -> modifier
        is Int -> {
            // Int represents flex grow factor
            if (flex > 0) {
                modifier.weight(flex.toFloat(), fill = true)
            } else {
                modifier
            }
        }
        is String -> {
            when (flex.lowercase()) {
                "auto" -> modifier.weight(1f, fill = false)
                "none" -> modifier.wrapContentWidth()
                else -> {
                    // Parse CSS flex shorthand like "1 1 auto"
                    val parts = flex.split(" ")
                    if (parts.isNotEmpty()) {
                        val grow = parts[0].toFloatOrNull() ?: 1f
                        modifier.weight(grow, fill = true)
                    } else {
                        modifier
                    }
                }
            }
        }
        else -> modifier
    }
}

/**
 * Example usage demonstrating all features:
 *
 * ```kotlin
 * // Basic grid
 * AntRow(gutter = 16) {
 *     AntCol(span = 8) { Text("1/3") }
 *     AntCol(span = 8) { Text("1/3") }
 *     AntCol(span = 8) { Text("1/3") }
 * }
 *
 * // Responsive grid
 * AntRow(gutter = Pair(16, 24)) {
 *     AntCol(xs = 24, sm = 12, md = 8, lg = 6) {
 *         Text("Responsive")
 *     }
 * }
 *
 * // Offset and order
 * AntRow {
 *     AntCol(span = 6, offset = 6) { Text("Offset 6") }
 *     AntCol(span = 6, order = 2) { Text("Order 2") }
 *     AntCol(span = 6, order = 1) { Text("Order 1") }
 * }
 *
 * // Push and pull
 * AntRow {
 *     AntCol(span = 18, push = 6) { Text("Push 6") }
 *     AntCol(span = 6, pull = 18) { Text("Pull 18") }
 * }
 *
 * // Flex layout
 * AntRow {
 *     AntCol(flex = "auto") { Text("Auto flex") }
 *     AntCol(flex = 2) { Text("Flex 2") }
 *     AntCol(flex = 1) { Text("Flex 1") }
 * }
 *
 * // Responsive gutter
 * AntRow(
 *     gutter = mapOf(
 *         Breakpoint.Xs to Pair(8, 8),
 *         Breakpoint.Sm to Pair(16, 16),
 *         Breakpoint.Md to Pair(24, 24),
 *         Breakpoint.Lg to Pair(32, 32)
 *     ),
 *     align = RowAlign.Middle,
 *     justify = RowJustify.SpaceBetween
 * ) {
 *     AntCol(
 *         xs = 24,
 *         sm = ColResponsiveConfig(span = 12, offset = 0),
 *         md = ColResponsiveConfig(span = 8, offset = 2),
 *         lg = ColResponsiveConfig(span = 6, offset = 3)
 *     ) {
 *         Text("Advanced responsive")
 *     }
 * }
 *
 * // Alignment options
 * AntRow(align = RowAlign.Middle, justify = RowJustify.Center) {
 *     AntCol(span = 4) { Text("Centered") }
 * }
 *
 * // No wrap
 * AntRow(wrap = false) {
 *     AntCol(span = 8) { Text("No wrap 1") }
 *     AntCol(span = 8) { Text("No wrap 2") }
 *     AntCol(span = 8) { Text("No wrap 3") }
 * }
 * ```
 */
