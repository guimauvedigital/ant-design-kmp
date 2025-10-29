package com.antdesign.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

// ============ Enums ============

/**
 * Layout mode for Descriptions.
 *
 * - **Horizontal**: Label and content are displayed side by side (default in React)
 * - **Vertical**: Label is displayed above content
 */
enum class DescriptionsLayout {
    /** Label and content side by side */
    Horizontal,
    /** Label above content */
    Vertical
}

/**
 * Size presets for Descriptions.
 * Matches React Ant Design size prop.
 */
enum class DescriptionsSize {
    /** Default size with standard padding */
    Default,
    /** Medium size (middle in React) */
    Middle,
    /** Small size with compact padding */
    Small
}

/**
 * Breakpoint definitions for responsive column configuration.
 * Matches React Ant Design breakpoints: xs, sm, md, lg, xl, xxl
 */
enum class DescriptionsBreakpoint {
    /** Extra small: < 576px */
    XS,
    /** Small: >= 576px */
    SM,
    /** Medium: >= 768px */
    MD,
    /** Large: >= 992px */
    LG,
    /** Extra large: >= 1200px */
    XL,
    /** Extra extra large: >= 1600px */
    XXL
}

// ============ Data Classes ============

/**
 * Semantic style names for Descriptions components.
 * Matches React interface with: root, header, title, extra, label, content
 * @since v5.23.0
 */
data class DescriptionsStyles(
    val root: Modifier = Modifier,
    val header: Modifier = Modifier,
    val title: Modifier = Modifier,
    val extra: Modifier = Modifier,
    val label: Modifier = Modifier,
    val content: Modifier = Modifier
)

/**
 * Semantic className mappings for Descriptions components.
 * Matches React's classNames prop.
 * @since v5.23.0
 */
data class DescriptionsClassNames(
    val root: String = "",
    val header: String = "",
    val title: String = "",
    val extra: String = "",
    val label: String = "",
    val content: String = ""
)

/**
 * Semantic styles for individual description item.
 * Matches React's DescriptionsItemProps.styles
 * @since v5.23.0
 */
data class DescriptionItemStyles(
    val label: Modifier = Modifier,
    val content: Modifier = Modifier
)

/**
 * Semantic className mappings for individual description item.
 * Matches React's DescriptionsItemProps.classNames
 * @since v5.23.0
 */
data class DescriptionItemClassNames(
    val label: String = "",
    val content: String = ""
)

/**
 * Sealed class representing different span configurations for a description item.
 * Matches React's span prop which accepts: number | 'filled' | Partial<Record<Breakpoint, number>>
 * @since v5.13.0 (responsive span added in v5.13.0)
 */
sealed class DescriptionItemSpan {
    /** Fixed number of columns to span */
    data class Fixed(val value: Int) : DescriptionItemSpan()

    /** Fill the entire row */
    data object Filled : DescriptionItemSpan()

    /** Responsive span based on breakpoints */
    data class Responsive(val breakpoints: Map<DescriptionsBreakpoint, Int>) : DescriptionItemSpan()
}

/**
 * Configuration for a single description item.
 * This is the Kotlin equivalent of React's DescriptionsItemType.
 *
 * @param key Unique identifier for the item
 * @param label Label text (simple string version)
 * @param labelComposable Label content (composable version, takes priority over label)
 * @param content Content text (simple string version)
 * @param contentComposable Content (composable version, takes priority over content)
 * @param span Column span configuration: Fixed(n), Filled, or Responsive(map). Can also accept Int directly.
 * @param labelStyle Deprecated: use styles.label instead
 * @param contentStyle Deprecated: use styles.content instead
 * @param styles Semantic styles for label and content (added v5.23.0)
 * @param classNames Semantic class names for label and content (added v5.23.0)
 * @param className Additional CSS class name (root level)
 * @param style Additional inline styles (root level)
 */
data class DescriptionItem(
    val key: String? = null,
    val label: String? = null,
    val labelComposable: (@Composable () -> Unit)? = null,
    val content: String? = null,
    val contentComposable: (@Composable () -> Unit)? = null,
    val span: DescriptionItemSpan = DescriptionItemSpan.Fixed(1),
    @Deprecated(
        message = "Use styles.label instead",
        replaceWith = ReplaceWith("styles = DescriptionItemStyles(label = labelStyle)")
    )
    val labelStyle: Modifier? = null,
    @Deprecated(
        message = "Use styles.content instead",
        replaceWith = ReplaceWith("styles = DescriptionItemStyles(content = contentStyle)")
    )
    val contentStyle: Modifier? = null,
    val styles: DescriptionItemStyles? = null,
    val classNames: DescriptionItemClassNames? = null,
    val className: String? = null,
    val style: Modifier? = null
)

/**
 * Internal representation of a description item after processing.
 */
internal data class InternalDescriptionItem(
    val key: String?,
    val label: String?,
    val labelComposable: (@Composable () -> Unit)?,
    val content: String?,
    val contentComposable: (@Composable () -> Unit)?,
    val span: Int,
    val filled: Boolean,
    val labelStyle: Modifier?,
    val contentStyle: Modifier?,
    val styles: DescriptionItemStyles?,
    val classNames: DescriptionItemClassNames?,
    val className: String?,
    val style: Modifier?
)

/**
 * Default column map for responsive breakpoints.
 * Matches React's DEFAULT_COLUMN_MAP from constant.ts
 */
private val DEFAULT_COLUMN_MAP = mapOf(
    DescriptionsBreakpoint.XXL to 3,
    DescriptionsBreakpoint.XL to 3,
    DescriptionsBreakpoint.LG to 3,
    DescriptionsBreakpoint.MD to 3,
    DescriptionsBreakpoint.SM to 2,
    DescriptionsBreakpoint.XS to 1
)

// ============ Main Component ============

/**
 * Display multiple read-only fields in groups.
 *
 * Descriptions is a component for displaying key-value pairs in a structured format,
 * commonly used for property sheets, detail pages, or displaying structured data.
 *
 * ## Features
 * - Responsive column layout with breakpoint support
 * - Horizontal and vertical layout modes
 * - Bordered and non-bordered styles
 * - Column span support
 * - Customizable label and content styles
 * - Title and extra content support
 * - Three size presets
 *
 * ## Props (matching React Ant Design)
 *
 * @param items List of description items to display
 * @param modifier Modifier for the root container
 * @param prefixCls Custom prefix for CSS classes (default: "ant-descriptions")
 * @param className Additional CSS class name for root
 * @param rootClassName Root container class name
 * @param style Inline styles for root container
 * @param bordered Whether to display borders (default: false)
 * @param size Size preset: Default, Middle, or Small (default: Default)
 * @param title Title content (string version)
 * @param titleComposable Title content (composable version, takes priority)
 * @param extra Extra content displayed on the right side of the header
 * @param column Number of columns OR map of breakpoint to column count (default: 3)
 * @param layout Layout mode: Horizontal or Vertical (default: Horizontal)
 * @param colon Whether to display colon after label (default: true)
 * @param labelStyle [Deprecated] Use styles.label instead. Direct label styling modifier (deprecated in v4.21.0)
 * @param contentStyle [Deprecated] Use styles.content instead. Direct content styling modifier (deprecated in v4.21.0)
 * @param styles Semantic styles for different parts of the component (added v5.23.0)
 * @param classNames Semantic class names for different parts of the component (added v5.23.0)
 * @param id HTML id attribute
 *
 * ## Examples
 *
 * ### Basic Usage
 * ```kotlin
 * AntDescriptions(
 *     items = listOf(
 *         DescriptionItem(
 *             label = "Product",
 *             content = "Cloud Database"
 *         ),
 *         DescriptionItem(
 *             label = "Billing Mode",
 *             content = "Prepaid"
 *         ),
 *         DescriptionItem(
 *             label = "Creation Time",
 *             content = "2018-04-24 18:00:00"
 *         )
 *     )
 * )
 * ```
 *
 * ### With Title and Border
 * ```kotlin
 * AntDescriptions(
 *     title = "User Info",
 *     bordered = true,
 *     items = listOf(
 *         DescriptionItem(label = "Name", content = "Zhou Maomao"),
 *         DescriptionItem(label = "Telephone", content = "1810000000"),
 *         DescriptionItem(label = "Address", content = "Hangzhou, Zhejiang")
 *     )
 * )
 * ```
 *
 * ### Responsive Columns
 * ```kotlin
 * AntDescriptions(
 *     column = mapOf(
 *         DescriptionsBreakpoint.XXL to 4,
 *         DescriptionsBreakpoint.XL to 3,
 *         DescriptionsBreakpoint.LG to 3,
 *         DescriptionsBreakpoint.MD to 3,
 *         DescriptionsBreakpoint.SM to 2,
 *         DescriptionsBreakpoint.XS to 1
 *     ),
 *     items = items
 * )
 * ```
 *
 * ### Vertical Layout
 * ```kotlin
 * AntDescriptions(
 *     layout = DescriptionsLayout.Vertical,
 *     bordered = true,
 *     items = items
 * )
 * ```
 *
 * ### Column Span
 * ```kotlin
 * AntDescriptions(
 *     bordered = true,
 *     items = listOf(
 *         DescriptionItem(label = "Product", content = "Cloud Database", span = 2),
 *         DescriptionItem(label = "Billing", content = "Prepaid"),
 *         DescriptionItem(label = "Time", content = "2018-04-24", span = 3)
 *     )
 * )
 * ```
 *
 * ### Custom Styles
 * ```kotlin
 * AntDescriptions(
 *     items = items,
 *     styles = DescriptionsStyles(
 *         label = Modifier.background(Color(0xFFF5F5F5)),
 *         content = Modifier.padding(horizontal = 16.dp)
 *     )
 * )
 * ```
 *
 * @see DescriptionItem
 * @see DescriptionsLayout
 * @see DescriptionsSize
 * @see DescriptionsBreakpoint
 */
@Composable
fun AntDescriptions(
    items: List<DescriptionItem>,
    modifier: Modifier = Modifier,
    prefixCls: String = "ant-descriptions",
    className: String? = null,
    rootClassName: String? = null,
    style: Modifier = Modifier,
    bordered: Boolean = false,
    size: DescriptionsSize = DescriptionsSize.Default,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
    column: Any = 3, // Int or Map<DescriptionsBreakpoint, Int>
    layout: DescriptionsLayout = DescriptionsLayout.Horizontal,
    colon: Boolean = true,
    labelStyle: Modifier = Modifier,
    contentStyle: Modifier = Modifier,
    styles: DescriptionsStyles? = null,
    classNames: DescriptionsClassNames? = null,
    id: String? = null
) {
    // Calculate effective column count based on current breakpoint
    val effectiveColumn = remember(column) {
        when (column) {
            is Int -> column
            is Map<*, *> -> {
                // For now, we'll use a simple approach
                // In a real implementation, you'd detect the actual screen size
                @Suppress("UNCHECKED_CAST")
                val columnMap = column as? Map<DescriptionsBreakpoint, Int> ?: mapOf()
                val mergedMap = DEFAULT_COLUMN_MAP + columnMap
                // Default to MD breakpoint behavior (3 columns)
                mergedMap[DescriptionsBreakpoint.MD] ?: 3
            }
            else -> 3
        }
    }

    // Convert items to internal representation
    val internalItems = remember(items, effectiveColumn) {
        items.map { item ->
            // Resolve span based on type and current breakpoint
            val resolvedSpan = when (val spanConfig = item.span) {
                is DescriptionItemSpan.Fixed -> spanConfig.value
                is DescriptionItemSpan.Filled -> effectiveColumn
                is DescriptionItemSpan.Responsive -> {
                    // For responsive span, we need to resolve based on current breakpoint
                    // For now, default to MD breakpoint behavior
                    // In a real implementation, you'd use actual screen size detection
                    spanConfig.breakpoints[DescriptionsBreakpoint.MD]
                        ?: spanConfig.breakpoints[DescriptionsBreakpoint.LG]
                        ?: spanConfig.breakpoints[DescriptionsBreakpoint.XL]
                        ?: spanConfig.breakpoints[DescriptionsBreakpoint.XXL]
                        ?: spanConfig.breakpoints[DescriptionsBreakpoint.SM]
                        ?: spanConfig.breakpoints[DescriptionsBreakpoint.XS]
                        ?: 1
                }
            }

            val isFilled = item.span is DescriptionItemSpan.Filled

            InternalDescriptionItem(
                key = item.key,
                label = item.label,
                labelComposable = item.labelComposable,
                content = item.content,
                contentComposable = item.contentComposable,
                span = resolvedSpan,
                filled = isFilled,
                labelStyle = item.labelStyle,
                contentStyle = item.contentStyle,
                styles = item.styles,
                classNames = item.classNames,
                className = item.className,
                style = item.style
            )
        }
    }

    // Calculate rows based on column count and item spans
    // This matches React's useRow hook logic
    val rows = remember(internalItems, effectiveColumn) {
        calculateRows(internalItems, effectiveColumn)
    }

    // Determine padding based on size
    val cellPadding = when (size) {
        DescriptionsSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        DescriptionsSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        DescriptionsSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    }

    val headerPadding = when (size) {
        DescriptionsSize.Small -> 12.dp
        DescriptionsSize.Middle -> 16.dp
        DescriptionsSize.Default -> 20.dp
    }

    // Colors
    val borderColor = Color(0xFFD9D9D9)
    val labelBgColor = Color(0xFFFAFAFA)
    val contentBgColor = Color.White
    val labelTextColor = Color(0xFF595959)
    val contentTextColor = Color(0xFF000000D9)

    // Root container
    Column(
        modifier = modifier
            .then(style)
            .then(styles?.root ?: Modifier)
            .fillMaxWidth()
    ) {
        // Header (title + extra)
        if (title != null || titleComposable != null || extra != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = headerPadding)
                    .then(styles?.header ?: Modifier),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title
                if (titleComposable != null) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .then(styles?.title ?: Modifier)
                    ) {
                        titleComposable()
                    }
                } else if (title != null) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000D9),
                        modifier = Modifier
                            .weight(1f)
                            .then(styles?.title ?: Modifier)
                    )
                }

                // Extra
                if (extra != null) {
                    Box(
                        modifier = Modifier.then(styles?.extra ?: Modifier)
                    ) {
                        extra()
                    }
                }
            }
        }

        // Content: Descriptions view (table-like structure)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            rows.forEachIndexed { rowIndex, row ->
                when (layout) {
                    DescriptionsLayout.Horizontal -> {
                        DescriptionsRowHorizontal(
                            row = row,
                            bordered = bordered,
                            colon = colon,
                            cellPadding = cellPadding,
                            effectiveColumn = effectiveColumn,
                            borderColor = borderColor,
                            labelBgColor = labelBgColor,
                            contentBgColor = contentBgColor,
                            labelTextColor = labelTextColor,
                            contentTextColor = contentTextColor,
                            labelStyle = labelStyle,
                            contentStyle = contentStyle,
                            globalStyles = styles,
                            isFirstRow = rowIndex == 0,
                            isLastRow = rowIndex == rows.size - 1
                        )
                    }
                    DescriptionsLayout.Vertical -> {
                        DescriptionsRowVertical(
                            row = row,
                            bordered = bordered,
                            colon = colon,
                            cellPadding = cellPadding,
                            effectiveColumn = effectiveColumn,
                            borderColor = borderColor,
                            labelBgColor = labelBgColor,
                            contentBgColor = contentBgColor,
                            labelTextColor = labelTextColor,
                            contentTextColor = contentTextColor,
                            labelStyle = labelStyle,
                            contentStyle = contentStyle,
                            globalStyles = styles,
                            isFirstRow = rowIndex == 0,
                            isLastRow = rowIndex == rows.size - 1
                        )
                    }
                }
            }
        }
    }
}

// ============ Internal Helper Components ============

/**
 * Renders a row in horizontal layout mode.
 * In horizontal mode, label and content are side by side.
 * When bordered, label uses th and content uses td (like a table).
 */
@Composable
private fun DescriptionsRowHorizontal(
    row: List<InternalDescriptionItem>,
    bordered: Boolean,
    colon: Boolean,
    cellPadding: PaddingValues,
    effectiveColumn: Int,
    borderColor: Color,
    labelBgColor: Color,
    contentBgColor: Color,
    labelTextColor: Color,
    contentTextColor: Color,
    labelStyle: Modifier,
    contentStyle: Modifier,
    globalStyles: DescriptionsStyles?,
    isFirstRow: Boolean,
    isLastRow: Boolean
) {
    if (bordered) {
        // Bordered horizontal: Each item has separate label and content cells
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor
                )
        ) {
            row.forEach { item ->
                // Label cell (th-like)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(labelBgColor)
                        .border(width = 1.dp, color = borderColor)
                        .padding(cellPadding)
                        .then(labelStyle)
                        .then(item.labelStyle ?: Modifier)
                        .then(item.styles?.label ?: Modifier)
                        .then(globalStyles?.label ?: Modifier)
                ) {
                    if (item.labelComposable != null) {
                        item.labelComposable.invoke()
                    } else if (item.label != null) {
                        Text(
                            text = item.label + if (colon) ":" else "",
                            fontSize = 14.sp,
                            color = labelTextColor,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Content cell (td-like)
                Box(
                    modifier = Modifier
                        .weight(item.span.toFloat())
                        .background(contentBgColor)
                        .border(width = 1.dp, color = borderColor)
                        .padding(cellPadding)
                        .then(contentStyle)
                        .then(item.contentStyle ?: Modifier)
                        .then(item.styles?.content ?: Modifier)
                        .then(globalStyles?.content ?: Modifier)
                ) {
                    if (item.contentComposable != null) {
                        item.contentComposable.invoke()
                    } else if (item.content != null) {
                        Text(
                            text = item.content,
                            fontSize = 14.sp,
                            color = contentTextColor
                        )
                    }
                }
            }
        }
    } else {
        // Non-bordered horizontal: Label and content in same container
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            row.forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(item.span.toFloat())
                        .padding(cellPadding)
                        .then(item.style ?: Modifier)
                ) {
                    // Label
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(labelStyle)
                            .then(item.labelStyle ?: Modifier)
                            .then(item.styles?.label ?: Modifier)
                            .then(globalStyles?.label ?: Modifier)
                    ) {
                        if (item.labelComposable != null) {
                            Row {
                                item.labelComposable.invoke()
                                if (colon) {
                                    Text(
                                        text = ":",
                                        fontSize = 14.sp,
                                        color = labelTextColor
                                    )
                                }
                            }
                        } else if (item.label != null) {
                            Text(
                                text = item.label + if (colon) ":" else "",
                                fontSize = 14.sp,
                                color = labelTextColor,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(contentStyle)
                            .then(item.contentStyle ?: Modifier)
                            .then(item.styles?.content ?: Modifier)
                            .then(globalStyles?.content ?: Modifier)
                    ) {
                        if (item.contentComposable != null) {
                            item.contentComposable.invoke()
                        } else if (item.content != null) {
                            Text(
                                text = item.content,
                                fontSize = 14.sp,
                                color = contentTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Renders a row in vertical layout mode.
 * In vertical mode, label is above content.
 * Renders two rows: one for labels, one for contents.
 */
@Composable
private fun DescriptionsRowVertical(
    row: List<InternalDescriptionItem>,
    bordered: Boolean,
    colon: Boolean,
    cellPadding: PaddingValues,
    effectiveColumn: Int,
    borderColor: Color,
    labelBgColor: Color,
    contentBgColor: Color,
    labelTextColor: Color,
    contentTextColor: Color,
    labelStyle: Modifier,
    contentStyle: Modifier,
    globalStyles: DescriptionsStyles?,
    isFirstRow: Boolean,
    isLastRow: Boolean
) {
    if (bordered) {
        // Label row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = borderColor)
        ) {
            row.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(item.span.toFloat())
                        .background(labelBgColor)
                        .border(width = 1.dp, color = borderColor)
                        .padding(cellPadding)
                        .then(labelStyle)
                        .then(item.labelStyle ?: Modifier)
                        .then(item.styles?.label ?: Modifier)
                        .then(globalStyles?.label ?: Modifier)
                ) {
                    if (item.labelComposable != null) {
                        item.labelComposable.invoke()
                    } else if (item.label != null) {
                        Text(
                            text = item.label + if (colon) ":" else "",
                            fontSize = 14.sp,
                            color = labelTextColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Content row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = borderColor)
        ) {
            row.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(item.span.toFloat())
                        .background(contentBgColor)
                        .border(width = 1.dp, color = borderColor)
                        .padding(cellPadding)
                        .then(contentStyle)
                        .then(item.contentStyle ?: Modifier)
                        .then(item.styles?.content ?: Modifier)
                        .then(globalStyles?.content ?: Modifier)
                ) {
                    if (item.contentComposable != null) {
                        item.contentComposable.invoke()
                    } else if (item.content != null) {
                        Text(
                            text = item.content,
                            fontSize = 14.sp,
                            color = contentTextColor
                        )
                    }
                }
            }
        }
    } else {
        // Non-bordered vertical
        Column {
            row.forEach { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cellPadding)
                        .then(item.style ?: Modifier)
                ) {
                    // Label
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .then(labelStyle)
                            .then(item.labelStyle ?: Modifier)
                            .then(item.styles?.label ?: Modifier)
                            .then(globalStyles?.label ?: Modifier)
                    ) {
                        if (item.labelComposable != null) {
                            item.labelComposable.invoke()
                        } else if (item.label != null) {
                            Text(
                                text = item.label + if (colon) ":" else "",
                                fontSize = 14.sp,
                                color = labelTextColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(contentStyle)
                            .then(item.contentStyle ?: Modifier)
                            .then(item.styles?.content ?: Modifier)
                            .then(globalStyles?.content ?: Modifier)
                    ) {
                        if (item.contentComposable != null) {
                            item.contentComposable.invoke()
                        } else if (item.content != null) {
                            Text(
                                text = item.content,
                                fontSize = 14.sp,
                                color = contentTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============ Row Calculation Logic ============

/**
 * Calculate rows based on column count and item spans.
 * This matches React's useRow hook logic from useRow.ts
 *
 * Algorithm:
 * 1. Iterate through items
 * 2. For each item, check if it fits in the current row
 * 3. If it doesn't fit or exceeds column count, start a new row
 * 4. Handle 'filled' items (take entire row)
 * 5. Fill incomplete rows by extending the last item's span
 */
private fun calculateRows(
    items: List<InternalDescriptionItem>,
    columnCount: Int
): List<List<InternalDescriptionItem>> {
    val rows = mutableListOf<List<InternalDescriptionItem>>()
    var currentRow = mutableListOf<InternalDescriptionItem>()
    var currentCount = 0

    items.forEach { item ->
        if (item.filled) {
            // Filled items take the entire row
            if (currentRow.isNotEmpty()) {
                rows.add(currentRow.toList())
                currentRow.clear()
                currentCount = 0
            }
            rows.add(listOf(item.copy(span = columnCount)))
            return@forEach
        }

        val itemSpan = item.span.coerceAtLeast(1)
        val restSpan = columnCount - currentCount

        if (currentCount + itemSpan > columnCount) {
            // Item doesn't fit in current row
            if (currentCount > 0) {
                // Adjust the last item's span to fill the row
                val lastItem = currentRow.removeAt(currentRow.size - 1)
                val adjustedSpan = lastItem.span + restSpan
                currentRow.add(lastItem.copy(span = adjustedSpan))
                rows.add(currentRow.toList())
                currentRow.clear()
            }
            currentRow.add(item.copy(span = min(itemSpan, columnCount)))
            currentCount = min(itemSpan, columnCount)
        } else if (currentCount + itemSpan == columnCount) {
            // Item exactly fills the row
            currentRow.add(item)
            rows.add(currentRow.toList())
            currentRow.clear()
            currentCount = 0
        } else {
            // Item fits in current row with space left
            currentRow.add(item)
            currentCount += itemSpan
        }
    }

    // Handle remaining items in incomplete row
    if (currentRow.isNotEmpty()) {
        val lastItem = currentRow.removeAt(currentRow.size - 1)
        val remainingSpan = columnCount - (currentCount - lastItem.span)
        currentRow.add(lastItem.copy(span = remainingSpan))
        rows.add(currentRow.toList())
    }

    return rows
}

// ============ Preview Examples ============

/**
 * Example 1: Basic Descriptions
 */
@Composable
fun DescriptionsBasicExample() {
    AntDescriptions(
        items = listOf(
            DescriptionItem(
                label = "Product",
                content = "Cloud Database"
            ),
            DescriptionItem(
                label = "Billing Mode",
                content = "Prepaid"
            ),
            DescriptionItem(
                label = "Automatic Renewal",
                content = "YES"
            ),
            DescriptionItem(
                label = "Order time",
                content = "2018-04-24 18:00:00"
            ),
            DescriptionItem(
                label = "Usage Time",
                content = "2019-04-24 18:00:00",
                span = DescriptionItemSpan.Fixed(2)
            ),
            DescriptionItem(
                label = "Status",
                content = "Running",
                span = DescriptionItemSpan.Fixed(3)
            ),
            DescriptionItem(
                label = "Negotiated Amount",
                content = "$80.00"
            ),
            DescriptionItem(
                label = "Discount",
                content = "$20.00"
            ),
            DescriptionItem(
                label = "Official Receipts",
                content = "$60.00"
            )
        )
    )
}

/**
 * Example 2: Descriptions with title and border
 */
@Composable
fun DescriptionsBorderedExample() {
    AntDescriptions(
        title = "User Info",
        bordered = true,
        items = listOf(
            DescriptionItem(
                label = "Product",
                content = "Cloud Database"
            ),
            DescriptionItem(
                label = "Billing Mode",
                content = "Prepaid"
            ),
            DescriptionItem(
                label = "Automatic Renewal",
                content = "YES"
            ),
            DescriptionItem(
                label = "Order time",
                content = "2018-04-24 18:00:00",
                span = DescriptionItemSpan.Fixed(2)
            ),
            DescriptionItem(
                label = "Usage Time",
                content = "2019-04-24 18:00:00",
                span = DescriptionItemSpan.Fixed(3)
            )
        )
    )
}

/**
 * Example 3: Vertical layout
 */
@Composable
fun DescriptionsVerticalExample() {
    AntDescriptions(
        title = "User Info",
        layout = DescriptionsLayout.Vertical,
        bordered = true,
        items = listOf(
            DescriptionItem(
                label = "UserName",
                content = "Zhou Maomao"
            ),
            DescriptionItem(
                label = "Telephone",
                content = "1810000000"
            ),
            DescriptionItem(
                label = "Live",
                content = "Hangzhou, Zhejiang"
            ),
            DescriptionItem(
                label = "Address",
                content = "No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China"
            ),
            DescriptionItem(
                label = "Remark",
                content = "empty"
            )
        )
    )
}

/**
 * Example 4: Different sizes
 */
@Composable
fun DescriptionsSizesExample() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Small
        Text("Small size:", fontWeight = FontWeight.Bold)
        AntDescriptions(
            bordered = true,
            size = DescriptionsSize.Small,
            items = listOf(
                DescriptionItem(label = "Product", content = "Cloud Database"),
                DescriptionItem(label = "Billing", content = "Prepaid"),
                DescriptionItem(label = "Time", content = "2018-04-24 18:00:00"),
                DescriptionItem(label = "Amount", content = "$80.00", span = DescriptionItemSpan.Fixed(2))
            )
        )

        // Middle
        Text("Middle size:", fontWeight = FontWeight.Bold)
        AntDescriptions(
            bordered = true,
            size = DescriptionsSize.Middle,
            items = listOf(
                DescriptionItem(label = "Product", content = "Cloud Database"),
                DescriptionItem(label = "Billing", content = "Prepaid"),
                DescriptionItem(label = "Time", content = "2018-04-24 18:00:00"),
                DescriptionItem(label = "Amount", content = "$80.00", span = DescriptionItemSpan.Fixed(2))
            )
        )

        // Default
        Text("Default size:", fontWeight = FontWeight.Bold)
        AntDescriptions(
            bordered = true,
            size = DescriptionsSize.Default,
            items = listOf(
                DescriptionItem(label = "Product", content = "Cloud Database"),
                DescriptionItem(label = "Billing", content = "Prepaid"),
                DescriptionItem(label = "Time", content = "2018-04-24 18:00:00"),
                DescriptionItem(label = "Amount", content = "$80.00", span = DescriptionItemSpan.Fixed(2))
            )
        )
    }
}

/**
 * Example 5: Custom content with composables
 */
@Composable
fun DescriptionsCustomContentExample() {
    AntDescriptions(
        title = "Custom Content",
        bordered = true,
        extra = {
            Text(
                text = "Edit",
                color = Color(0xFF1890FF),
                fontSize = 14.sp
            )
        },
        items = listOf(
            DescriptionItem(
                label = "Product",
                contentComposable = {
                    Text(
                        text = "Cloud Database",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1890FF)
                    )
                }
            ),
            DescriptionItem(
                label = "Status",
                contentComposable = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF52C41A).copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Running",
                            color = Color(0xFF52C41A),
                            fontSize = 14.sp
                        )
                    }
                }
            ),
            DescriptionItem(
                label = "Price",
                contentComposable = {
                    Text(
                        text = "$80.00",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFFFF4D4F)
                    )
                }
            )
        )
    )
}

/**
 * Example 6: Responsive columns (simulated)
 */
@Composable
fun DescriptionsResponsiveExample() {
    AntDescriptions(
        title = "Responsive Columns",
        column = mapOf(
            DescriptionsBreakpoint.XXL to 4,
            DescriptionsBreakpoint.XL to 3,
            DescriptionsBreakpoint.LG to 3,
            DescriptionsBreakpoint.MD to 3,
            DescriptionsBreakpoint.SM to 2,
            DescriptionsBreakpoint.XS to 1
        ),
        bordered = true,
        items = listOf(
            DescriptionItem(label = "Product", content = "Cloud Database"),
            DescriptionItem(label = "Billing", content = "Prepaid"),
            DescriptionItem(label = "Time", content = "2018-04-24 18:00:00"),
            DescriptionItem(label = "Amount", content = "$80.00"),
            DescriptionItem(label = "Discount", content = "$20.00"),
            DescriptionItem(label = "Official", content = "$60.00")
        )
    )
}

/**
 * Example 7: Semantic styles (v5.23.0+)
 * Demonstrates the new styles and classNames props for granular styling control
 */
@Composable
fun DescriptionsSemanticStylesExample() {
    AntDescriptions(
        title = "Semantic Styles Example",
        bordered = true,
        styles = DescriptionsStyles(
            label = Modifier.background(Color(0xFFF0F5FF)),
            content = Modifier.background(Color(0xFFFFFBE6))
        ),
        items = listOf(
            DescriptionItem(
                label = "Product",
                content = "Cloud Database",
                styles = DescriptionItemStyles(
                    label = Modifier.padding(8.dp),
                    content = Modifier.padding(8.dp)
                )
            ),
            DescriptionItem(
                label = "Status",
                content = "Running"
            ),
            DescriptionItem(
                label = "Price",
                content = "$80.00"
            )
        )
    )
}

/**
 * Example 8: Responsive item span (v5.13.0+)
 * Demonstrates responsive span configuration for individual items
 */
@Composable
fun DescriptionsResponsiveSpanExample() {
    AntDescriptions(
        title = "Responsive Item Spans",
        bordered = true,
        column = 4,
        items = listOf(
            DescriptionItem(
                label = "Product",
                content = "Cloud Database",
                span = DescriptionItemSpan.Responsive(
                    mapOf(
                        DescriptionsBreakpoint.XXL to 2,
                        DescriptionsBreakpoint.XL to 2,
                        DescriptionsBreakpoint.LG to 2,
                        DescriptionsBreakpoint.MD to 2,
                        DescriptionsBreakpoint.SM to 1,
                        DescriptionsBreakpoint.XS to 1
                    )
                )
            ),
            DescriptionItem(
                label = "Billing",
                content = "Prepaid",
                span = DescriptionItemSpan.Fixed(1)
            ),
            DescriptionItem(
                label = "Status",
                content = "Running",
                span = DescriptionItemSpan.Fixed(1)
            ),
            DescriptionItem(
                label = "Description",
                content = "This is a full-width description that spans the entire row",
                span = DescriptionItemSpan.Filled
            ),
            DescriptionItem(
                label = "Amount",
                content = "$80.00",
                span = DescriptionItemSpan.Fixed(2)
            ),
            DescriptionItem(
                label = "Discount",
                content = "$20.00",
                span = DescriptionItemSpan.Fixed(2)
            )
        )
    )
}

/**
 * Example 9: Advanced semantic customization (v5.23.0+)
 * Shows comprehensive use of classNames and styles at both global and item levels
 */
@Composable
fun DescriptionsAdvancedCustomizationExample() {
    AntDescriptions(
        title = "Advanced Customization",
        bordered = true,
        styles = DescriptionsStyles(
            root = Modifier.padding(16.dp),
            header = Modifier.background(Color(0xFFF0F2F5)),
            title = Modifier.padding(12.dp),
            label = Modifier.background(Color(0xFFFAFAFA)),
            content = Modifier.background(Color.White)
        ),
        classNames = DescriptionsClassNames(
            root = "custom-descriptions",
            header = "custom-header",
            title = "custom-title",
            label = "custom-label",
            content = "custom-content"
        ),
        items = listOf(
            DescriptionItem(
                label = "Product",
                content = "Cloud Database",
                styles = DescriptionItemStyles(
                    label = Modifier
                        .background(Color(0xFF1890FF).copy(alpha = 0.1f))
                        .padding(8.dp),
                    content = Modifier
                        .background(Color(0xFFF0F5FF))
                        .padding(8.dp)
                ),
                classNames = DescriptionItemClassNames(
                    label = "product-label",
                    content = "product-content"
                )
            ),
            DescriptionItem(
                label = "Status",
                content = "Running",
                styles = DescriptionItemStyles(
                    content = Modifier
                        .background(Color(0xFF52C41A).copy(alpha = 0.1f))
                        .padding(8.dp)
                )
            ),
            DescriptionItem(
                label = "Price",
                content = "$80.00",
                styles = DescriptionItemStyles(
                    content = Modifier
                        .background(Color(0xFFFFE58F))
                        .padding(8.dp)
                )
            )
        )
    )
}
