package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

// ============ Enums & Data Classes ============

/**
 * Size variant for pagination component
 */
enum class PaginationSize {
    Default,
    Small
}

/**
 * Alignment options for pagination component
 * @since v5.19.0
 */
enum class PaginationAlign {
    Start,
    Center,
    End
}

/**
 * Types of pagination items that can be rendered
 * Used with [itemRender] to identify which element is being customized
 */
enum class PaginationItemType {
    /** Regular page number button (1, 2, 3, ...) */
    Page,
    /** Previous page button */
    Prev,
    /** Next page button */
    Next,
    /** Jump backward button (jumps 5 pages back, shows «) */
    JumpPrev,
    /** Jump forward button (jumps 5 pages forward, shows ») */
    JumpNext
}

/**
 * Configuration for quick jumper with optional go button
 * @property goButton Custom go button to trigger jump action
 */
data class QuickJumperConfig(
    val goButton: (@Composable () -> Unit)? = null
)

/**
 * Semantic class names for Pagination component (v5.4.0+)
 * Allows customization of CSS classes for different parts of the pagination
 *
 * @property root Class name for the root pagination container
 * @property item Class name for pagination items (page numbers, prev/next buttons)
 * @property prev Class name for the previous button
 * @property next Class name for the next button
 * @property jump Class name for jump buttons (jump-prev, jump-next)
 * @property page Class name for page number buttons
 */
data class PaginationClassNames(
    val root: String? = null,
    val item: String? = null,
    val prev: String? = null,
    val next: String? = null,
    val jump: String? = null,
    val page: String? = null
)

/**
 * Semantic styles for Pagination component (v5.4.0+)
 * Allows customization of Compose Modifiers for different parts of the pagination
 *
 * @property root Modifier for the root pagination container
 * @property item Modifier for pagination items (page numbers, prev/next buttons)
 * @property prev Modifier for the previous button
 * @property next Modifier for the next button
 * @property jump Modifier for jump buttons (jump-prev, jump-next)
 * @property page Modifier for page number buttons
 */
data class PaginationStyles(
    val root: Modifier? = null,
    val item: Modifier? = null,
    val prev: Modifier? = null,
    val next: Modifier? = null,
    val jump: Modifier? = null,
    val page: Modifier? = null
)

/**
 * Internationalization configuration for pagination text
 * All strings can be customized to support different languages
 *
 * @property items_per_page Text shown in page size selector (e.g., "/ page")
 * @property jump_to Text for quick jumper input label (e.g., "Go to")
 * @property jump_to_confirm Text for quick jumper confirmation (e.g., "confirm")
 * @property page General page text (rarely used, can be empty)
 * @property prev_page ARIA label for previous page button
 * @property next_page ARIA label for next page button
 * @property prev_5 ARIA label for jump backward 5 pages button
 * @property next_5 ARIA label for jump forward 5 pages button
 * @property prev_3 ARIA label for jump backward 3 pages button
 * @property next_3 ARIA label for jump forward 3 pages button
 */
data class PaginationLocale(
    val items_per_page: String = "/ page",
    val jump_to: String = "Go to",
    val jump_to_confirm: String = "confirm",
    val page: String = "",
    val prev_page: String = "Previous Page",
    val next_page: String = "Next Page",
    val prev_5: String = "Previous 5 Pages",
    val next_5: String = "Next 5 Pages",
    val prev_3: String = "Previous 3 Pages",
    val next_3: String = "Next 3 Pages"
)

// ============ Main Component ============

/**
 * Ant Design Pagination component for Kotlin Multiplatform
 *
 * A complete pagination component with support for page navigation, size changing,
 * quick jumping, and full customization capabilities.
 *
 * @param current Current page number (controlled mode)
 * @param total Total number of items
 * @param pageSize Number of items per page
 * @param onChange Callback when page or pageSize changes
 * @param onShowSizeChange Callback when page size changes
 * @param defaultCurrent Default initial page number (uncontrolled mode)
 * @param defaultPageSize Default initial page size (uncontrolled mode)
 * @param disabled Disable all pagination interactions
 * @param hideOnSinglePage Hide pagination when there's only one page
 * @param showSizeChanger Show page size selector (can be Boolean or custom select props)
 * @param showQuickJumper Show quick jump input (can be Boolean or QuickJumperConfig for custom go button)
 * @param showTotal Function to display total items and current range
 * @param showTitle Show title attribute on page items (default true)
 * @param simple Use simple pagination (prev/next only)
 * @param size Size variant (Default or Small)
 * @param pageSizeOptions Available page size options
 * @param align Horizontal alignment of pagination (v5.19.0+)
 * @param responsive Enable responsive behavior for mobile
 * @param showLessItems Show fewer page numbers (3 instead of 5 around current)
 * @param totalBoundaryShowSizeChanger Total threshold to show size changer (default 50)
 * @param locale Internationalization configuration for all text labels
 * @param itemRender Custom renderer for pagination items (pages, prev/next, jumpers)
 * @param prevIcon Custom icon for previous button
 * @param nextIcon Custom icon for next button
 * @param jumpPrevIcon Custom icon for jump previous button
 * @param jumpNextIcon Custom icon for jump next button
 * @param selectComponentClass Custom select component class for page size selector
 * @param selectPrefixCls Prefix class name for select element
 * @param prefixCls Prefix class name for pagination
 * @param rootClassName Root class name for the pagination container
 * @param classNames Semantic class names for different parts (v5.4.0+)
 * @param styles Semantic styles (Modifiers) for different parts (v5.4.0+)
 * @param modifier Compose modifier for the pagination container
 */
@Composable
fun AntPagination(
    current: Int = 1,
    total: Int,
    pageSize: Int = 10,
    onChange: ((page: Int, pageSize: Int) -> Unit)? = null,
    onShowSizeChange: ((current: Int, size: Int) -> Unit)? = null,
    defaultCurrent: Int = 1,
    defaultPageSize: Int = 10,
    disabled: Boolean = false,
    hideOnSinglePage: Boolean = false,
    showSizeChanger: Boolean = false,
    showQuickJumper: Any = false, // Boolean or QuickJumperConfig
    showTotal: ((total: Int, range: IntRange) -> String)? = null,
    showTitle: Boolean = true,
    simple: Boolean = false,
    size: PaginationSize = PaginationSize.Default,
    pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    align: PaginationAlign = PaginationAlign.Start,
    responsive: Boolean = true,
    showLessItems: Boolean = false,
    totalBoundaryShowSizeChanger: Int = 50,
    locale: PaginationLocale? = null,
    itemRender: ((page: Int, type: PaginationItemType, originalElement: @Composable () -> Unit) -> @Composable () -> Unit)? = null,
    prevIcon: (@Composable () -> Unit)? = null,
    nextIcon: (@Composable () -> Unit)? = null,
    jumpPrevIcon: (@Composable () -> Unit)? = null,
    jumpNextIcon: (@Composable () -> Unit)? = null,
    // Deprecated: Will be removed in future versions - use custom rendering patterns instead
    selectComponentClass: Any? = null,
    selectPrefixCls: String? = null,
    prefixCls: String = "ant-pagination",
    rootClassName: String? = null,
    classNames: PaginationClassNames? = null,
    styles: PaginationStyles? = null,
    modifier: Modifier = Modifier
) {
    val config = useConfig()
    val contextLocale = useLocale()

    // Merge custom locale with default English locale
    val effectiveLocale = locale ?: PaginationLocale()

    // Parse showQuickJumper (can be Boolean or QuickJumperConfig)
    val quickJumperEnabled = when (showQuickJumper) {
        is Boolean -> showQuickJumper
        is QuickJumperConfig -> true
        else -> false
    }
    val quickJumperConfig = when (showQuickJumper) {
        is QuickJumperConfig -> showQuickJumper
        else -> null
    }

    // Determine if showSizeChanger should be enabled based on totalBoundaryShowSizeChanger
    val effectiveShowSizeChanger = if (showSizeChanger) {
        true
    } else if (total > totalBoundaryShowSizeChanger) {
        true
    } else {
        false
    }

    // State management for uncontrolled mode
    var internalCurrent by remember { mutableStateOf(defaultCurrent) }
    var internalPageSize by remember { mutableStateOf(defaultPageSize) }

    val actualCurrent = if (onChange != null) current else internalCurrent
    val actualPageSize = if (onChange != null) pageSize else internalPageSize

    val totalPages = (total + actualPageSize - 1) / actualPageSize

    // Hide pagination if only one page
    if (hideOnSinglePage && totalPages <= 1) {
        return
    }

    val handlePageChange = { page: Int, newPageSize: Int ->
        if (!disabled) {
            val validPage = page.coerceIn(1, totalPages)
            if (onChange != null) {
                onChange(validPage, newPageSize)
            } else {
                internalCurrent = validPage
                internalPageSize = newPageSize
            }
        }
    }

    val handleSizeChange = { newSize: Int ->
        if (!disabled) {
            val newTotalPages = (total + newSize - 1) / newSize
            val newPage = minOf(actualCurrent, newTotalPages)

            onShowSizeChange?.invoke(actualCurrent, newSize)
            handlePageChange(newPage, newSize)
        }
    }

    // Apply root styles and classNames
    val rootModifier = modifier
        .fillMaxWidth()
        .then(styles?.root ?: Modifier)

    Row(
        modifier = rootModifier,
        horizontalArrangement = when (align) {
            PaginationAlign.Start -> Arrangement.Start
            PaginationAlign.Center -> Arrangement.Center
            PaginationAlign.End -> Arrangement.End
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (simple) {
            SimplePagination(
                current = actualCurrent,
                totalPages = totalPages,
                disabled = disabled,
                size = size,
                showTitle = showTitle,
                config = config,
                itemRender = itemRender,
                prevIcon = prevIcon,
                nextIcon = nextIcon,
                classNames = classNames,
                styles = styles,
                onPageChange = { page -> handlePageChange(page, actualPageSize) }
            )
        } else {
            FullPagination(
                current = actualCurrent,
                total = total,
                pageSize = actualPageSize,
                totalPages = totalPages,
                disabled = disabled,
                size = size,
                showSizeChanger = effectiveShowSizeChanger,
                showQuickJumper = quickJumperEnabled,
                quickJumperConfig = quickJumperConfig,
                showTotal = showTotal,
                showTitle = showTitle,
                pageSizeOptions = pageSizeOptions,
                showLessItems = showLessItems,
                config = config,
                locale = effectiveLocale,
                itemRender = itemRender,
                prevIcon = prevIcon,
                nextIcon = nextIcon,
                jumpPrevIcon = jumpPrevIcon,
                jumpNextIcon = jumpNextIcon,
                classNames = classNames,
                styles = styles,
                onPageChange = { page -> handlePageChange(page, actualPageSize) },
                onSizeChange = handleSizeChange,
                responsive = responsive
            )
        }
    }
}

// ============ Simple Pagination ============

@Composable
private fun SimplePagination(
    current: Int,
    totalPages: Int,
    disabled: Boolean,
    size: PaginationSize,
    showTitle: Boolean,
    config: ConfigProviderProps,
    itemRender: ((page: Int, type: PaginationItemType, originalElement: @Composable () -> Unit) -> @Composable () -> Unit)?,
    prevIcon: (@Composable () -> Unit)?,
    nextIcon: (@Composable () -> Unit)?,
    classNames: PaginationClassNames?,
    styles: PaginationStyles?,
    onPageChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous button
        val prevButton: @Composable () -> Unit = {
            PaginationButton(
                content = prevIcon ?: { Text("<") },
                enabled = !disabled && current > 1,
                isPrimary = false,
                size = size,
                showTitle = showTitle,
                title = "Previous Page",
                config = config,
                itemModifier = styles?.prev,
                onClick = { onPageChange(current - 1) }
            )
        }
        val prevRendered = itemRender?.invoke(current - 1, PaginationItemType.Prev, prevButton) ?: prevButton
        prevRendered()

        // Current / Total display
        Text(
            text = "$current / $totalPages",
            fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
            color = if (disabled) Color(0xFF00000040) else Color(0xFF000000D9)
        )

        // Next button
        val nextButton: @Composable () -> Unit = {
            PaginationButton(
                content = nextIcon ?: { Text(">") },
                enabled = !disabled && current < totalPages,
                isPrimary = false,
                size = size,
                showTitle = showTitle,
                title = "Next Page",
                config = config,
                itemModifier = styles?.next,
                onClick = { onPageChange(current + 1) }
            )
        }
        val nextRendered = itemRender?.invoke(current + 1, PaginationItemType.Next, nextButton) ?: nextButton
        nextRendered()
    }
}

// ============ Full Pagination ============

@Composable
private fun FullPagination(
    current: Int,
    total: Int,
    pageSize: Int,
    totalPages: Int,
    disabled: Boolean,
    size: PaginationSize,
    showSizeChanger: Boolean,
    showQuickJumper: Boolean,
    quickJumperConfig: QuickJumperConfig?,
    showTotal: ((total: Int, range: IntRange) -> String)?,
    showTitle: Boolean,
    pageSizeOptions: List<Int>,
    showLessItems: Boolean,
    config: ConfigProviderProps,
    locale: PaginationLocale,
    itemRender: ((page: Int, type: PaginationItemType, originalElement: @Composable () -> Unit) -> @Composable () -> Unit)?,
    prevIcon: (@Composable () -> Unit)?,
    nextIcon: (@Composable () -> Unit)?,
    jumpPrevIcon: (@Composable () -> Unit)?,
    jumpNextIcon: (@Composable () -> Unit)?,
    classNames: PaginationClassNames?,
    styles: PaginationStyles?,
    onPageChange: (Int) -> Unit,
    onSizeChange: (Int) -> Unit,
    responsive: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Show total
        if (showTotal != null) {
            val start = (current - 1) * pageSize + 1
            val end = minOf(current * pageSize, total)
            val range = start..end

            Text(
                text = showTotal(total, range),
                fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
                color = Color(0xFF00000073),
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        // Size changer
        if (showSizeChanger) {
            PageSizeChanger(
                pageSize = pageSize,
                pageSizeOptions = pageSizeOptions,
                disabled = disabled,
                size = size,
                locale = locale,
                onSizeChange = onSizeChange
            )
        }

        // Previous button
        val prevButton: @Composable () -> Unit = {
            PaginationButton(
                content = prevIcon ?: { Text("<") },
                enabled = !disabled && current > 1,
                isPrimary = false,
                size = size,
                showTitle = showTitle,
                title = locale.prev_page,
                config = config,
                itemModifier = styles?.prev,
                onClick = { onPageChange(current - 1) }
            )
        }
        val prevRendered = itemRender?.invoke(current - 1, PaginationItemType.Prev, prevButton) ?: prevButton
        prevRendered()

        // Page numbers
        val pageRange = getPageRange(current, totalPages, showLessItems)
        pageRange.forEach { page ->
            when {
                page == -1 -> {
                    // Ellipsis (not customizable via itemRender)
                    Text(
                        text = "•••",
                        fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
                        color = Color(0xFF00000040),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                page == -2 -> {
                    // Jump prev (5 pages back)
                    val jumpPrevButton: @Composable () -> Unit = {
                        JumpButton(
                            content = jumpPrevIcon ?: { Text("«") },
                            enabled = !disabled,
                            size = size,
                            showTitle = showTitle,
                            title = locale.prev_5,
                            config = config,
                            itemModifier = styles?.jump,
                            onClick = { onPageChange(maxOf(1, current - 5)) }
                        )
                    }
                    val jumpPrevRendered = itemRender?.invoke(
                        maxOf(1, current - 5),
                        PaginationItemType.JumpPrev,
                        jumpPrevButton
                    ) ?: jumpPrevButton
                    jumpPrevRendered()
                }
                page == -3 -> {
                    // Jump next (5 pages forward)
                    val jumpNextButton: @Composable () -> Unit = {
                        JumpButton(
                            content = jumpNextIcon ?: { Text("»") },
                            enabled = !disabled,
                            size = size,
                            showTitle = showTitle,
                            title = locale.next_5,
                            config = config,
                            itemModifier = styles?.jump,
                            onClick = { onPageChange(minOf(totalPages, current + 5)) }
                        )
                    }
                    val jumpNextRendered = itemRender?.invoke(
                        minOf(totalPages, current + 5),
                        PaginationItemType.JumpNext,
                        jumpNextButton
                    ) ?: jumpNextButton
                    jumpNextRendered()
                }
                else -> {
                    val pageButton: @Composable () -> Unit = {
                        PaginationButton(
                            content = { Text("$page") },
                            enabled = !disabled,
                            isPrimary = page == current,
                            size = size,
                            showTitle = showTitle,
                            title = "$page",
                            config = config,
                            itemModifier = styles?.page,
                            onClick = { onPageChange(page) }
                        )
                    }
                    val pageRendered = itemRender?.invoke(page, PaginationItemType.Page, pageButton) ?: pageButton
                    pageRendered()
                }
            }
        }

        // Next button
        val nextButton: @Composable () -> Unit = {
            PaginationButton(
                content = nextIcon ?: { Text(">") },
                enabled = !disabled && current < totalPages,
                isPrimary = false,
                size = size,
                showTitle = showTitle,
                title = locale.next_page,
                config = config,
                itemModifier = styles?.next,
                onClick = { onPageChange(current + 1) }
            )
        }
        val nextRendered = itemRender?.invoke(current + 1, PaginationItemType.Next, nextButton) ?: nextButton
        nextRendered()

        // Quick jumper
        if (showQuickJumper) {
            QuickJumper(
                totalPages = totalPages,
                disabled = disabled,
                size = size,
                locale = locale,
                goButton = quickJumperConfig?.goButton,
                onJump = onPageChange
            )
        }
    }
}

// ============ Page Size Changer ============

@Composable
private fun PageSizeChanger(
    pageSize: Int,
    pageSizeOptions: List<Int>,
    disabled: Boolean,
    size: PaginationSize,
    locale: PaginationLocale,
    onSizeChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Trigger button
        Box(
            modifier = Modifier
                .height(if (size == PaginationSize.Small) 24.dp else 32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (disabled) Color(0xFFF5F5F5) else Color.White)
                .clickable(enabled = !disabled) { expanded = !expanded }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$pageSize ${locale.items_per_page}",
                    fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
                    color = if (disabled) Color(0xFF00000040) else Color(0xFF000000D9)
                )
                Text(
                    text = "▼",
                    fontSize = 10.sp,
                    color = if (disabled) Color(0xFF00000040) else Color(0xFF000000D9)
                )
            }
        }

        // Dropdown
        if (expanded && !disabled) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                Card(
                    modifier = Modifier.width(150.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        pageSizeOptions.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSizeChange(option)
                                        expanded = false
                                    }
                                    .background(
                                        if (option == pageSize) Color(0xFFF0F5FF) else Color.Transparent
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "$option ${locale.items_per_page}",
                                    fontSize = 14.sp,
                                    color = if (option == pageSize) Color(0xFF1890FF) else Color(0xFF000000D9)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============ Quick Jumper ============

@Composable
private fun QuickJumper(
    totalPages: Int,
    disabled: Boolean,
    size: PaginationSize,
    locale: PaginationLocale,
    goButton: (@Composable () -> Unit)?,
    onJump: (Int) -> Unit
) {
    var jumpValue by remember { mutableStateOf("") }

    val performJump = {
        jumpValue.toIntOrNull()?.let { page ->
            if (page in 1..totalPages) {
                onJump(page)
                jumpValue = ""
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = locale.jump_to,
            fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
            color = if (disabled) Color(0xFF00000040) else Color(0xFF00000073)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(if (size == PaginationSize.Small) 24.dp else 32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (disabled) Color(0xFFF5F5F5) else Color.White)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = jumpValue,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        jumpValue = newValue
                    }
                },
                enabled = !disabled,
                textStyle = TextStyle(
                    fontSize = if (size == PaginationSize.Small) 12.sp else 14.sp,
                    color = if (disabled) Color(0xFF00000040) else Color(0xFF000000D9),
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { performJump() }
                ),
                cursorBrush = SolidColor(Color(0xFF1890FF)),
                singleLine = true
            )
        }

        // Custom go button if provided
        if (goButton != null) {
            Box(modifier = Modifier.clickable(enabled = !disabled) { performJump() }) {
                goButton()
            }
        }
    }
}

// ============ Pagination Button ============

@Composable
private fun PaginationButton(
    content: @Composable () -> Unit,
    enabled: Boolean,
    isPrimary: Boolean,
    size: PaginationSize,
    showTitle: Boolean,
    title: String,
    config: ConfigProviderProps,
    itemModifier: Modifier?,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !enabled -> Color(0xFFF5F5F5)
        isPrimary -> config.theme.token.colorPrimary
        else -> Color.White
    }

    val borderColor = when {
        !enabled -> Color(0xFFD9D9D9)
        isPrimary -> config.theme.token.colorPrimary
        else -> Color(0xFFD9D9D9)
    }

    Box(
        modifier = Modifier
            .size(if (size == PaginationSize.Small) 24.dp else 32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .then(itemModifier ?: Modifier)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

// ============ Jump Button (for quick navigation) ============

@Composable
private fun JumpButton(
    content: @Composable () -> Unit,
    enabled: Boolean,
    size: PaginationSize,
    showTitle: Boolean,
    title: String,
    config: ConfigProviderProps,
    itemModifier: Modifier?,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(if (size == PaginationSize.Small) 24.dp else 32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isHovered && enabled) Color(0xFF1890FF) else Color.White)
            .then(itemModifier ?: Modifier)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

// ============ Utility Functions ============

/**
 * Calculates which page numbers to display in the pagination
 *
 * @param current Current page number
 * @param total Total number of pages
 * @param showLessItems If true, shows fewer pages (current ± 1), otherwise shows current ± 2
 * @return List of page numbers and special markers:
 *         - Positive numbers: page numbers to display
 *         - -1: ellipsis (•••)
 *         - -2: jump prev button (jumps 5 pages back)
 *         - -3: jump next button (jumps 5 pages forward)
 */
private fun getPageRange(current: Int, total: Int, showLessItems: Boolean = false): List<Int> {
    if (total <= 0) return emptyList()

    // Buffer size: how many pages to show around current page
    // showLessItems=true: show 1 page on each side (3 total: current-1, current, current+1)
    // showLessItems=false: show 2 pages on each side (5 total: current-2, current-1, current, current+1, current+2)
    val pageBufferSize = if (showLessItems) 1 else 2

    // Threshold for showing all pages without ellipsis
    val allPagesThreshold = if (showLessItems) 5 else 7

    // If total pages is small enough, show all pages
    if (total <= allPagesThreshold) return (1..total).toList()

    val range = mutableListOf<Int>()

    // Always show first page
    range.add(1)

    // Calculate the range of pages to show around current
    val leftBound = maxOf(2, current - pageBufferSize)
    val rightBound = minOf(total - 1, current + pageBufferSize)

    when {
        current <= pageBufferSize + 2 -> {
            // Near the beginning: show first few pages
            // Example (showLessItems=false): [1] 2 3 4 5 ... 20
            // Example (showLessItems=true): [1] 2 3 ... 20
            val endPage = if (showLessItems) 3 else 5
            range.addAll(2..minOf(endPage, total - 1))

            if (total > allPagesThreshold) {
                range.add(-3) // jump next
            }
        }
        current >= total - pageBufferSize - 1 -> {
            // Near the end: show last few pages
            // Example (showLessItems=false): 1 ... 16 17 18 19 [20]
            // Example (showLessItems=true): 1 ... 18 19 [20]
            range.add(-2) // jump prev

            val startPage = if (showLessItems) total - 2 else total - 4
            range.addAll(maxOf(2, startPage)..(total - 1))
        }
        else -> {
            // In the middle: show jump buttons and pages around current
            // Example (showLessItems=false): 1 ... 8 9 [10] 11 12 ... 20
            // Example (showLessItems=true): 1 ... 9 [10] 11 ... 20
            range.add(-2) // jump prev
            range.addAll(leftBound..rightBound)
            range.add(-3) // jump next
        }
    }

    // Always show last page
    if (total > 1) {
        range.add(total)
    }

    return range
}

// ============ Examples ============

@Composable
fun PaginationBasicExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 100,
        onChange = { page, _ -> current = page }
    )
}

@Composable
fun PaginationWithSizeChangerExample() {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(10) }

    AntPagination(
        current = current,
        total = 500,
        pageSize = pageSize,
        showSizeChanger = true,
        onChange = { page, size ->
            current = page
            pageSize = size
        },
        onShowSizeChange = { _, size ->
            println("Page size changed to: $size")
        }
    )
}

@Composable
fun PaginationWithQuickJumperExample() {
    var current by remember { mutableStateOf(2) }

    AntPagination(
        current = current,
        total = 500,
        showQuickJumper = true,
        onChange = { page, _ -> current = page }
    )
}

@Composable
fun PaginationWithTotalExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 100,
        showTotal = { total, range ->
            "Showing ${range.first}-${range.last} of $total items"
        },
        onChange = { page, _ -> current = page }
    )
}

@Composable
fun SimplePaginationExample() {
    var current by remember { mutableStateOf(3) }

    AntPagination(
        current = current,
        total = 100,
        simple = true,
        onChange = { page, _ -> current = page }
    )
}

@Composable
fun SmallPaginationExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 50,
        size = PaginationSize.Small,
        onChange = { page, _ -> current = page }
    )
}

@Composable
fun DisabledPaginationExample() {
    AntPagination(
        current = 1,
        total = 50,
        disabled = true
    )
}

@Composable
fun ControlledPaginationExample() {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(10) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Current page: $current, Page size: $pageSize")

        AntPagination(
            current = current,
            total = 100,
            pageSize = pageSize,
            showSizeChanger = true,
            showQuickJumper = true,
            showTotal = { total, range ->
                "Total $total items"
            },
            onChange = { page, size ->
                current = page
                pageSize = size
            }
        )
    }
}

@Composable
fun PaginationWithItemRenderExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 500,
        showSizeChanger = true,
        onChange = { page, _ -> current = page },
        itemRender = { page, type, originalElement ->
            @Composable {
                when (type) {
                    PaginationItemType.Prev -> {
                        Text(
                            text = "← Previous",
                            fontSize = 14.sp,
                            color = Color(0xFF1890FF),
                            modifier = Modifier
                                .clickable { current = maxOf(1, current - 1) }
                                .padding(8.dp)
                        )
                    }
                    PaginationItemType.Next -> {
                        Text(
                            text = "Next →",
                            fontSize = 14.sp,
                            color = Color(0xFF1890FF),
                            modifier = Modifier
                                .clickable { current = minOf((500 + 9) / 10, current + 1) }
                                .padding(8.dp)
                        )
                    }
                    PaginationItemType.Page -> {
                        // Use custom page number rendering
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (page == current) Color(0xFF52C41A) else Color.White)
                                .clickable { current = page }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$page",
                                fontSize = 14.sp,
                                color = if (page == current) Color.White else Color(0xFF000000D9)
                            )
                        }
                    }
                    else -> originalElement() // Use default for jump buttons
                }
            }
        }
    )
}

@Composable
fun PaginationWithLocaleExample() {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(10) }

    // French locale
    val frenchLocale = PaginationLocale(
        items_per_page = "/ page",
        jump_to = "Aller à",
        jump_to_confirm = "confirmer",
        page = "",
        prev_page = "Page Précédente",
        next_page = "Page Suivante",
        prev_5 = "5 Pages Précédentes",
        next_5 = "5 Pages Suivantes",
        prev_3 = "3 Pages Précédentes",
        next_3 = "3 Pages Suivantes"
    )

    AntPagination(
        current = current,
        total = 500,
        pageSize = pageSize,
        showSizeChanger = true,
        showQuickJumper = true,
        locale = frenchLocale,
        onChange = { page, size ->
            current = page
            pageSize = size
        }
    )
}

@Composable
fun PaginationWithShowLessItemsExample() {
    var current by remember { mutableStateOf(10) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Normal pagination (shows 5 pages):")
        AntPagination(
            current = current,
            total = 500,
            showLessItems = false,
            onChange = { page, _ -> current = page }
        )

        Text("Compact pagination (shows 3 pages):")
        AntPagination(
            current = current,
            total = 500,
            showLessItems = true,
            onChange = { page, _ -> current = page }
        )
    }
}

// ============ New v5+ Examples ============

/**
 * Example: Custom icon pagination (v5+)
 * Demonstrates prevIcon, nextIcon, jumpPrevIcon, jumpNextIcon props
 */
@Composable
fun PaginationWithCustomIconsExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 500,
        onChange = { page, _ -> current = page },
        prevIcon = { Text("◀") },
        nextIcon = { Text("▶") },
        jumpPrevIcon = { Text("⏪") },
        jumpNextIcon = { Text("⏩") }
    )
}

/**
 * Example: Semantic styling with classNames and styles (v5.4.0+)
 * Demonstrates PaginationClassNames and PaginationStyles
 */
@Composable
fun PaginationWithSemanticStylingExample() {
    var current by remember { mutableStateOf(1) }

    val customStyles = PaginationStyles(
        root = Modifier.padding(16.dp),
        item = Modifier.padding(4.dp),
        page = Modifier.background(Color(0xFFF0F5FF)),
        prev = Modifier.padding(horizontal = 8.dp),
        next = Modifier.padding(horizontal = 8.dp)
    )

    AntPagination(
        current = current,
        total = 100,
        styles = customStyles,
        onChange = { page, _ -> current = page }
    )
}

/**
 * Example: Quick jumper with custom go button (v5+)
 * Demonstrates QuickJumperConfig with goButton
 */
@Composable
fun PaginationWithCustomGoButtonExample() {
    var current by remember { mutableStateOf(1) }

    AntPagination(
        current = current,
        total = 500,
        showQuickJumper = QuickJumperConfig(
            goButton = {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color(0xFF1890FF), RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("GO", color = Color.White, fontSize = 12.sp)
                }
            }
        ),
        onChange = { page, _ -> current = page }
    )
}

/**
 * Example: Pagination with alignment (v5.19.0+)
 * Demonstrates align prop with Start, Center, End options
 */
@Composable
fun PaginationWithAlignmentExample() {
    var current by remember { mutableStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Left aligned (Start):")
        AntPagination(
            current = current,
            total = 100,
            align = PaginationAlign.Start,
            onChange = { page, _ -> current = page }
        )

        Text("Center aligned:")
        AntPagination(
            current = current,
            total = 100,
            align = PaginationAlign.Center,
            onChange = { page, _ -> current = page }
        )

        Text("Right aligned (End):")
        AntPagination(
            current = current,
            total = 100,
            align = PaginationAlign.End,
            onChange = { page, _ -> current = page }
        )
    }
}

/**
 * Example: Auto show size changer based on totalBoundaryShowSizeChanger (v5+)
 * Demonstrates totalBoundaryShowSizeChanger prop
 */
@Composable
fun PaginationWithTotalBoundaryExample() {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(10) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Total: 30 items (below boundary of 50, no size changer):")
        AntPagination(
            current = current,
            total = 30,
            pageSize = pageSize,
            totalBoundaryShowSizeChanger = 50,
            onChange = { page, size ->
                current = page
                pageSize = size
            }
        )

        Text("Total: 100 items (above boundary of 50, shows size changer):")
        AntPagination(
            current = current,
            total = 100,
            pageSize = pageSize,
            totalBoundaryShowSizeChanger = 50,
            onChange = { page, size ->
                current = page
                pageSize = size
            }
        )
    }
}

/**
 * Example: Complete v5+ feature showcase
 * Demonstrates all new props working together
 */
@Composable
fun PaginationCompleteV5Example() {
    var current by remember { mutableStateOf(5) }
    var pageSize by remember { mutableStateOf(20) }

    val customStyles = PaginationStyles(
        root = Modifier.padding(vertical = 16.dp),
        page = Modifier.padding(2.dp)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Complete v5+ Pagination with all features:", fontWeight = FontWeight.Bold)

        AntPagination(
            current = current,
            total = 500,
            pageSize = pageSize,
            align = PaginationAlign.Center,
            showSizeChanger = true,
            showQuickJumper = QuickJumperConfig(
                goButton = {
                    Text("→", color = Color(0xFF1890FF), fontSize = 16.sp)
                }
            ),
            showTotal = { total, range ->
                "${range.first}-${range.last} of $total items"
            },
            showTitle = true,
            totalBoundaryShowSizeChanger = 50,
            prevIcon = { Text("‹", fontSize = 18.sp) },
            nextIcon = { Text("›", fontSize = 18.sp) },
            jumpPrevIcon = { Text("«", fontSize = 16.sp) },
            jumpNextIcon = { Text("»", fontSize = 16.sp) },
            styles = customStyles,
            rootClassName = "custom-pagination",
            prefixCls = "ant-pagination",
            onChange = { page, size ->
                current = page
                pageSize = size
            }
        )
    }
}
