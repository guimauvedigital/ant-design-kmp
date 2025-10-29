package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============ Enums & Data Classes ============

/**
 * Layout orientation for list items
 */
enum class ListItemLayout {
    /** Horizontal layout - avatar/meta on left, actions on right */
    Horizontal,

    /** Vertical layout - all content stacked vertically */
    Vertical
}

/**
 * Size variant for list component
 */
enum class ListSize {
    /** Default size with standard padding */
    Default,

    /** Large size with increased padding */
    Large,

    /** Small size with reduced padding */
    Small
}

/**
 * Grid configuration for list display
 *
 * Supports responsive breakpoints for different screen sizes:
 * - xs: Extra small (<576px)
 * - sm: Small (≥576px)
 * - md: Medium (≥768px)
 * - lg: Large (≥992px)
 * - xl: Extra large (≥1200px)
 * - xxl: Extra extra large (≥1600px)
 *
 * @property gutter Spacing between grid items
 * @property column Default number of columns
 * @property xs Number of columns for extra small screens
 * @property sm Number of columns for small screens
 * @property md Number of columns for medium screens
 * @property lg Number of columns for large screens
 * @property xl Number of columns for extra large screens
 * @property xxl Number of columns for extra extra large screens
 */
data class ListGrid(
    val gutter: Dp = 0.dp,
    val column: Int = 1,
    val xs: Int? = null,
    val sm: Int? = null,
    val md: Int? = null,
    val lg: Int? = null,
    val xl: Int? = null,
    val xxl: Int? = null,
)

/**
 * Internationalization configuration for list empty state
 *
 * @property emptyText Text to display when list is empty
 * @property emptyTextComposable Composable to display when list is empty (overrides emptyText)
 */
data class ListLocale(
    val emptyText: String = "No Data",
    val emptyTextComposable: (@Composable () -> Unit)? = null,
)

/**
 * Semantic class names for List component parts (v5.4.0+)
 *
 * Allows customization of CSS classes for specific semantic parts of the List.
 *
 * @property header Class name for the header section
 * @property footer Class name for the footer section
 * @property empty Class name for the empty state section
 * @property item Class name for list items container
 * @property pagination Class name for the pagination section
 */
data class ListClassNames(
    val header: String? = null,
    val footer: String? = null,
    val empty: String? = null,
    val item: String? = null,
    val pagination: String? = null,
)

/**
 * Semantic styles for List component parts (v5.4.0+)
 *
 * Allows customization of Modifiers for specific semantic parts of the List.
 *
 * @property header Modifier for the header section
 * @property footer Modifier for the footer section
 * @property empty Modifier for the empty state section
 * @property item Modifier for list items container
 * @property pagination Modifier for the pagination section
 */
data class ListStyles(
    val header: Modifier? = null,
    val footer: Modifier? = null,
    val empty: Modifier? = null,
    val item: Modifier? = null,
    val pagination: Modifier? = null,
)

/**
 * Semantic class names for List.Item component parts (v5.4.0+)
 *
 * @property actions Class name for the actions section
 * @property extra Class name for the extra content section
 */
data class ListItemClassNames(
    val actions: String? = null,
    val extra: String? = null,
)

/**
 * Semantic styles for List.Item component parts (v5.4.0+)
 *
 * @property actions Modifier for the actions section
 * @property extra Modifier for the extra content section
 */
data class ListItemStyles(
    val actions: Modifier? = null,
    val extra: Modifier? = null,
)

/**
 * Loading state configuration for list
 */
sealed class ListLoading {
    /** List is not loading */
    object False : ListLoading()

    /** List is loading with default spinner */
    object True : ListLoading()

    /**
     * List is loading with custom configuration
     *
     * @property spinning Whether to show the loading indicator
     * @property indicator Custom loading indicator composable
     * @property tip Loading text to display below the spinner
     * @property delay Delay in milliseconds before showing loading indicator
     */
    data class Config(
        val spinning: Boolean = true,
        val indicator: (@Composable () -> Unit)? = null,
        val tip: String? = null,
        val delay: Long = 0,
    ) : ListLoading()
}

/**
 * Pagination configuration for list
 */
sealed class ListPagination {
    /** No pagination */
    object False : ListPagination()

    /** Default pagination */
    object True : ListPagination()

    /**
     * Custom pagination configuration
     *
     * @property current Current page number (controlled mode)
     * @property total Total number of items
     * @property pageSize Number of items per page
     * @property onChange Callback when page or pageSize changes
     * @property onShowSizeChange Callback when page size changes
     * @property defaultCurrent Default initial page number
     * @property defaultPageSize Default initial page size
     * @property disabled Disable pagination
     * @property hideOnSinglePage Hide pagination when there's only one page
     * @property showSizeChanger Show page size selector
     * @property showQuickJumper Show quick jump input
     * @property showTotal Function to display total items and current range
     * @property simple Use simple pagination (prev/next only)
     * @property size Size variant
     * @property pageSizeOptions Available page size options
     * @property align Horizontal alignment
     * @property responsive Enable responsive behavior
     * @property showLessItems Show fewer page numbers
     * @property locale Internationalization configuration
     * @property position Position of pagination (top, bottom, both)
     */
    data class Config(
        val current: Int = 1,
        val total: Int = 0,
        val pageSize: Int = 10,
        val onChange: ((page: Int, pageSize: Int) -> Unit)? = null,
        val onShowSizeChange: ((current: Int, size: Int) -> Unit)? = null,
        val defaultCurrent: Int = 1,
        val defaultPageSize: Int = 10,
        val disabled: Boolean = false,
        val hideOnSinglePage: Boolean = false,
        val showSizeChanger: Boolean = false,
        val showQuickJumper: Boolean = false,
        val showTotal: ((total: Int, range: IntRange) -> String)? = null,
        val simple: Boolean = false,
        val size: PaginationSize = PaginationSize.Default,
        val pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
        val align: PaginationAlign = PaginationAlign.End,
        val responsive: Boolean = true,
        val showLessItems: Boolean = false,
        val locale: PaginationLocale? = null,
        val position: ListPaginationPosition = ListPaginationPosition.Bottom,
    ) : ListPagination()
}

/**
 * Position of pagination in list
 */
enum class ListPaginationPosition {
    /** Pagination at top */
    Top,

    /** Pagination at bottom (default) */
    Bottom,

    /** Pagination at both top and bottom */
    Both
}

/**
 * Internal context for list component
 * Passed to child items to maintain consistent styling
 */
internal data class ListContextData(
    val grid: ListGrid? = null,
    val itemLayout: ListItemLayout = ListItemLayout.Horizontal,
)

// ============ Main List Component ============

/**
 * Ant Design List component for Kotlin Multiplatform
 *
 * A versatile list component for displaying series of content with support for
 * pagination, loading states, grid layout, and full customization.
 *
 * ## Key Features
 * - Multiple layouts: horizontal, vertical, grid
 * - Built-in pagination support
 * - Loading states with custom indicators
 * - Responsive grid with breakpoints
 * - Rich item metadata (avatar, title, description)
 * - Actions and extra content support
 * - Full internationalization
 *
 * ## Usage Examples
 *
 * ### Basic List
 * ```kotlin
 * val data = listOf("Item 1", "Item 2", "Item 3")
 * AntList(
 *     dataSource = data,
 *     renderItem = { item, index ->
 *         AntListItem {
 *             Text(item)
 *         }
 *     }
 * )
 * ```
 *
 * ### List with Pagination
 * ```kotlin
 * var currentPage by remember { mutableStateOf(1) }
 * AntList(
 *     dataSource = largeDataset,
 *     pagination = ListPagination.Config(
 *         current = currentPage,
 *         pageSize = 10,
 *         total = largeDataset.size,
 *         onChange = { page, _ -> currentPage = page }
 *     ),
 *     renderItem = { item, index -> ... }
 * )
 * ```
 *
 * ### Grid Layout
 * ```kotlin
 * AntList(
 *     dataSource = data,
 *     grid = ListGrid(
 *         gutter = 16.dp,
 *         column = 4,
 *         xs = 1,
 *         sm = 2,
 *         md = 3,
 *         lg = 4
 *     ),
 *     renderItem = { item, index -> ... }
 * )
 * ```
 *
 * @param dataSource List of items to display
 * @param renderItem Function to render each item
 * @param bordered Add border around the list
 * @param footer Footer content
 * @param grid Grid layout configuration (enables grid mode)
 * @param header Header content
 * @param itemLayout Layout orientation for items
 * @param loading Loading state configuration
 * @param loadMore Load more button/content
 * @param locale Internationalization configuration
 * @param pagination Pagination configuration
 * @param rowKey Function to extract unique key from each item
 * @param size Size variant (affects padding)
 * @param split Show split lines between items
 * @param modifier Compose modifier
 * @param id HTML id attribute for the root element (v5.0+)
 * @param rootClassName Class name for the root element (v5.0+)
 * @param rootStyle Style modifier for the root element (v5.0+)
 * @param classNames Semantic class names for different parts of the component (v5.4.0+)
 * @param styles Semantic style modifiers for different parts of the component (v5.4.0+)
 * @param virtual Enable virtual scrolling for large datasets (uses LazyColumn internally)
 */
@Composable
fun <T> AntList(
    dataSource: List<T>,
    renderItem: @Composable (item: T, index: Int) -> Unit,
    bordered: Boolean = false,
    footer: (@Composable () -> Unit)? = null,
    grid: ListGrid? = null,
    header: (@Composable () -> Unit)? = null,
    itemLayout: ListItemLayout = ListItemLayout.Horizontal,
    loading: ListLoading = ListLoading.False,
    loadMore: (@Composable () -> Unit)? = null,
    locale: ListLocale = ListLocale(),
    pagination: ListPagination = ListPagination.False,
    rowKey: ((T) -> String)? = null,
    size: ListSize = ListSize.Default,
    split: Boolean = true,
    modifier: Modifier = Modifier,
    id: String? = null,
    rootClassName: String? = null,
    rootStyle: Modifier? = null,
    classNames: ListClassNames? = null,
    styles: ListStyles? = null,
    virtual: Boolean = false,
) {
    val config = useConfig()

    // Pagination state management
    val paginationConfig = when (pagination) {
        is ListPagination.Config -> pagination
        is ListPagination.True -> ListPagination.Config(total = dataSource.size)
        is ListPagination.False -> null
    }

    var internalCurrent by remember { mutableStateOf(paginationConfig?.defaultCurrent ?: 1) }
    var internalPageSize by remember { mutableStateOf(paginationConfig?.defaultPageSize ?: 10) }

    val actualCurrent = paginationConfig?.current ?: internalCurrent
    val actualPageSize = paginationConfig?.pageSize ?: internalPageSize

    // Calculate paginated data
    val displayData = if (paginationConfig != null && pagination !is ListPagination.False) {
        val totalPages = (paginationConfig.total + actualPageSize - 1) / actualPageSize
        val validCurrent = actualCurrent.coerceIn(1, maxOf(1, totalPages))
        val startIndex = (validCurrent - 1) * actualPageSize
        val endIndex = minOf(startIndex + actualPageSize, dataSource.size)

        if (startIndex < dataSource.size) {
            dataSource.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
    } else {
        dataSource
    }

    // Loading state
    val isLoading = when (loading) {
        is ListLoading.True -> true
        is ListLoading.Config -> loading.spinning
        else -> false
    }

    val loadingConfig = when (loading) {
        is ListLoading.Config -> loading
        is ListLoading.True -> ListLoading.Config(spinning = true)
        else -> null
    }

    // Context for child items
    val contextData = remember(grid, itemLayout) {
        ListContextData(grid = grid, itemLayout = itemLayout)
    }

    CompositionLocalProvider(LocalListContext provides contextData) {
        Column(
            modifier = modifier
                .then(rootStyle ?: Modifier)
                .then(
                    if (bordered) {
                        Modifier.border(
                            width = 1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            // Pagination at top
            if (paginationConfig != null &&
                (paginationConfig.position == ListPaginationPosition.Top ||
                        paginationConfig.position == ListPaginationPosition.Both)
            ) {
                Box(modifier = styles?.pagination ?: Modifier) {
                    PaginationContent(
                        config = paginationConfig,
                        dataSource = dataSource,
                        actualCurrent = actualCurrent,
                        actualPageSize = actualPageSize,
                        onPageChange = { page, pageSize ->
                            if (paginationConfig.onChange != null) {
                                paginationConfig.onChange.invoke(page, pageSize)
                            } else {
                                internalCurrent = page
                                internalPageSize = pageSize
                            }
                        }
                    )
                }

                if (bordered) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }

            // Header
            if (header != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(styles?.header ?: Modifier)
                        .padding(
                            horizontal = 24.dp,
                            vertical = when (size) {
                                ListSize.Small -> 8.dp
                                ListSize.Default -> 12.dp
                                ListSize.Large -> 16.dp
                            }
                        )
                ) {
                    header()
                }

                if (bordered || split) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }

            // Main content with loading overlay
            Box {
                // Content
                when {
                    displayData.isEmpty() && !isLoading -> {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(styles?.empty ?: Modifier)
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (locale.emptyTextComposable != null) {
                                locale.emptyTextComposable.invoke()
                            } else {
                                Text(
                                    text = locale.emptyText,
                                    fontSize = 14.sp,
                                    color = Color(0xFF00000040)
                                )
                            }
                        }
                    }

                    grid != null -> {
                        // Grid layout
                        GridContent(
                            dataSource = displayData,
                            grid = grid,
                            renderItem = renderItem,
                            rowKey = rowKey,
                            size = size
                        )
                    }

                    else -> {
                        // List layout
                        ListContent(
                            dataSource = displayData,
                            renderItem = renderItem,
                            rowKey = rowKey,
                            split = split,
                            bordered = bordered,
                            size = size,
                            isLoading = isLoading,
                            virtual = virtual
                        )
                    }
                }

                // Loading overlay
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 53.dp)
                            .background(Color(0x80FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (loadingConfig != null) {
                            AntSpin(
                                spinning = loadingConfig.spinning,
                                indicator = loadingConfig.indicator,
                                tip = loadingConfig.tip,
                                delay = loadingConfig.delay?.toInt()
                            )
                        }
                    }
                }
            }

            // Footer
            if (footer != null) {
                if (bordered || split) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(styles?.footer ?: Modifier)
                        .padding(
                            horizontal = 24.dp,
                            vertical = when (size) {
                                ListSize.Small -> 8.dp
                                ListSize.Default -> 12.dp
                                ListSize.Large -> 16.dp
                            }
                        )
                ) {
                    footer()
                }
            }

            // Load more
            if (loadMore != null) {
                if (bordered || split) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    loadMore()
                }
            }

            // Pagination at bottom
            if (paginationConfig != null &&
                (paginationConfig.position == ListPaginationPosition.Bottom ||
                        paginationConfig.position == ListPaginationPosition.Both) &&
                loadMore == null
            ) {
                if (bordered) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }

                Box(modifier = styles?.pagination ?: Modifier) {
                    PaginationContent(
                        config = paginationConfig,
                        dataSource = dataSource,
                        actualCurrent = actualCurrent,
                        actualPageSize = actualPageSize,
                        onPageChange = { page, pageSize ->
                            if (paginationConfig.onChange != null) {
                                paginationConfig.onChange.invoke(page, pageSize)
                            } else {
                                internalCurrent = page
                                internalPageSize = pageSize
                            }
                        }
                    )
                }
            }
        }
    }
}

// ============ List Content (Non-Grid) ============

@Composable
private fun <T> ListContent(
    dataSource: List<T>,
    renderItem: @Composable (item: T, index: Int) -> Unit,
    rowKey: ((T) -> String)?,
    split: Boolean,
    bordered: Boolean,
    size: ListSize,
    isLoading: Boolean,
    virtual: Boolean = false,
) {
    if (virtual) {
        // Virtual scrolling with LazyColumn
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(
                items = dataSource,
                key = { index, item -> rowKey?.invoke(item) ?: "list-item-$index" }
            ) { index, item ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    renderItem(item, index)
                }

                // Divider between items
                if (split && index < dataSource.size - 1) {
                    Divider(
                        color = Color(0xFFF0F0F0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = if (bordered) 24.dp else 0.dp)
                    )
                }
            }
        }
    } else {
        // Standard Column layout
        Column(modifier = Modifier.fillMaxWidth()) {
            dataSource.forEachIndexed { index, item ->
                key(rowKey?.invoke(item) ?: "list-item-$index") {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        renderItem(item, index)
                    }

                    // Divider between items
                    if (split && index < dataSource.size - 1) {
                        Divider(
                            color = Color(0xFFF0F0F0),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = if (bordered) 24.dp else 0.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============ Grid Content ============

@Composable
private fun <T> GridContent(
    dataSource: List<T>,
    grid: ListGrid,
    renderItem: @Composable (item: T, index: Int) -> Unit,
    rowKey: ((T) -> String)?,
    size: ListSize,
) {
    // Use default column count for now (responsive breakpoints would require platform-specific code)
    // For full responsive support, we'd need BoxWithConstraints or platform-specific width detection
    val columnCount = grid.column

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(grid.gutter / 2),
        verticalArrangement = Arrangement.spacedBy(grid.gutter)
    ) {
        dataSource.chunked(columnCount).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(grid.gutter)
            ) {
                rowItems.forEachIndexed { colIndex, item ->
                    val itemIndex = rowIndex * columnCount + colIndex
                    key(rowKey?.invoke(item) ?: "grid-item-$itemIndex") {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(grid.gutter / 2)
                        ) {
                            renderItem(item, itemIndex)
                        }
                    }
                }

                // Fill remaining columns
                repeat(columnCount - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ============ Pagination Content ============

@Composable
private fun <T> PaginationContent(
    config: ListPagination.Config,
    dataSource: List<T>,
    actualCurrent: Int,
    actualPageSize: Int,
    onPageChange: (Int, Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = when (config.align) {
            PaginationAlign.Start -> Alignment.CenterStart
            PaginationAlign.Center -> Alignment.Center
            PaginationAlign.End -> Alignment.CenterEnd
        }
    ) {
        AntPagination(
            current = actualCurrent,
            total = config.total.takeIf { it > 0 } ?: dataSource.size,
            pageSize = actualPageSize,
            onChange = { page, pageSize ->
                onPageChange(page, pageSize)
                config.onChange?.invoke(page, pageSize)
            },
            onShowSizeChange = config.onShowSizeChange,
            disabled = config.disabled,
            hideOnSinglePage = config.hideOnSinglePage,
            showSizeChanger = config.showSizeChanger,
            showQuickJumper = config.showQuickJumper,
            showTotal = config.showTotal,
            simple = config.simple,
            size = config.size,
            pageSizeOptions = config.pageSizeOptions,
            align = config.align,
            responsive = config.responsive,
            showLessItems = config.showLessItems,
            locale = config.locale
        )
    }
}

// ============ List Item ============

/**
 * Ant Design List.Item component
 *
 * Represents a single item in the list with support for actions, extra content,
 * and custom styling.
 *
 * ## Usage
 *
 * ### Basic Item
 * ```kotlin
 * AntListItem {
 *     Text("List item content")
 * }
 * ```
 *
 * ### Item with Actions
 * ```kotlin
 * AntListItem(
 *     actions = listOf(
 *         { Text("Edit", color = Color(0xFF1890FF)) },
 *         { Text("Delete", color = Color(0xFFFF4D4F)) }
 *     )
 * ) {
 *     Text("Item with actions")
 * }
 * ```
 *
 * ### Item with Extra Content
 * ```kotlin
 * AntListItem(
 *     extra = { Image(...) }
 * ) {
 *     AntListItemMeta(
 *         title = "Item Title",
 *         description = "Item description"
 *     )
 * }
 * ```
 *
 * @param actions List of action composables (shown on the right)
 * @param extra Extra content composable (shown on the right in vertical layout)
 * @param onClick Click handler for the entire item
 * @param colStyle Custom modifier for grid layout styling
 * @param modifier Compose modifier
 * @param classNames Semantic class names for different parts (actions, extra) (v5.4.0+)
 * @param styles Semantic style modifiers for different parts (actions, extra) (v5.4.0+)
 * @param content Main content of the item
 */
@Composable
fun AntListItem(
    actions: List<@Composable () -> Unit> = emptyList(),
    extra: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    colStyle: Modifier = Modifier,
    modifier: Modifier = Modifier,
    classNames: ListItemClassNames? = null,
    styles: ListItemStyles? = null,
    content: @Composable () -> Unit,
) {
    val context = LocalListContext.current
    val isGrid = context.grid != null
    val itemLayout = context.itemLayout

    val itemModifier = modifier
        .fillMaxWidth()
        .then(colStyle)
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
            } else {
                Modifier
            }
        )

    when {
        // Vertical layout with extra content
        itemLayout == ListItemLayout.Vertical && extra != null -> {
            Row(
                modifier = itemModifier.padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main content (left side)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    content()

                    // Actions below content in vertical layout
                    if (actions.isNotEmpty()) {
                        Box(modifier = styles?.actions ?: Modifier) {
                            ActionsContent(actions = actions)
                        }
                    }
                }

                // Extra content (right side)
                Box(modifier = styles?.extra ?: Modifier) {
                    extra()
                }
            }
        }

        // Horizontal layout or vertical without extra
        else -> {
            Row(
                modifier = itemModifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Main content (left side)
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }

                // Actions and extra (right side)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (actions.isNotEmpty()) {
                        Box(modifier = styles?.actions ?: Modifier) {
                            ActionsContent(actions = actions)
                        }
                    }

                    if (extra != null) {
                        Box(modifier = styles?.extra ?: Modifier) {
                            extra()
                        }
                    }
                }
            }
        }
    }
}

// ============ Actions Content ============

@Composable
private fun ActionsContent(actions: List<@Composable () -> Unit>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        actions.forEachIndexed { index, action ->
            action()

            // Separator between actions
            if (index < actions.size - 1) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(14.dp)
                        .background(Color(0xFFF0F0F0))
                )
            }
        }
    }
}

// ============ List Item Meta ============

/**
 * Ant Design List.Item.Meta component
 *
 * Provides a standardized way to display item metadata with avatar, title, and description.
 *
 * ## Usage
 *
 * ### Basic Meta
 * ```kotlin
 * AntListItemMeta(
 *     title = "Item Title",
 *     description = "Item description"
 * )
 * ```
 *
 * ### Meta with Avatar
 * ```kotlin
 * AntListItemMeta(
 *     avatar = {
 *         Box(
 *             modifier = Modifier
 *                 .size(40.dp)
 *                 .clip(CircleShape)
 *                 .background(Color(0xFF1890FF))
 *         )
 *     },
 *     title = "John Doe",
 *     description = "Software Engineer"
 * )
 * ```
 *
 * ### Meta with Composable Title
 * ```kotlin
 * AntListItemMeta(
 *     titleComposable = {
 *         Row {
 *             Text("Title", fontWeight = FontWeight.Bold)
 *             Badge(count = 5)
 *         }
 *     },
 *     description = "Description text"
 * )
 * ```
 *
 * @param avatar Avatar composable (shown on the left)
 * @param description Description text (shown below title)
 * @param descriptionComposable Description as composable (overrides description)
 * @param title Title text
 * @param titleComposable Title as composable (overrides title)
 * @param modifier Compose modifier
 */
@Composable
fun AntListItemMeta(
    avatar: (@Composable () -> Unit)? = null,
    description: String? = null,
    descriptionComposable: (@Composable () -> Unit)? = null,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        if (avatar != null) {
            Box {
                avatar()
            }
        }

        // Content (title and description)
        if (title != null || titleComposable != null || description != null || descriptionComposable != null) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title
                if (titleComposable != null) {
                    titleComposable()
                } else if (title != null) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF000000D9)
                    )
                }

                // Description
                if (descriptionComposable != null) {
                    descriptionComposable()
                } else if (description != null) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color(0xFF00000073)
                    )
                }
            }
        }
    }
}

// ============ Context ============

internal val LocalListContext = compositionLocalOf { ListContextData() }

// ============ Examples ============

@Composable
fun ListBasicExample() {
    val data = listOf(
        "Racing car sprays burning fuel into crowd.",
        "Japanese princess to wed commoner.",
        "Australian walks 100km after outback crash.",
        "Man charged over missing wedding girl.",
        "Los Angeles battles huge wildfires."
    )

    AntList(
        dataSource = data,
        bordered = true,
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListWithMetaExample() {
    data class User(
        val name: String,
        val avatar: String,
        val description: String,
    )

    val data = listOf(
        User("John Doe", "J", "Software Engineer"),
        User("Jane Smith", "J", "Product Manager"),
        User("Bob Johnson", "B", "Designer")
    )

    AntList(
        dataSource = data,
        bordered = true,
        renderItem = { item, index ->
            AntListItem {
                AntListItemMeta(
                    avatar = {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF1890FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.avatar,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    title = item.name,
                    description = item.description
                )
            }
        }
    )
}

@Composable
fun ListWithActionsExample() {
    val data = listOf("Item 1", "Item 2", "Item 3")

    AntList(
        dataSource = data,
        bordered = true,
        renderItem = { item, index ->
            AntListItem(
                actions = listOf(
                    {
                        Text(
                            text = "Edit",
                            color = Color(0xFF1890FF),
                            modifier = Modifier.clickable { println("Edit $item") }
                        )
                    },
                    {
                        Text(
                            text = "Delete",
                            color = Color(0xFFFF4D4F),
                            modifier = Modifier.clickable { println("Delete $item") }
                        )
                    }
                )
            ) {
                Text(item)
            }
        }
    )
}

@Composable
fun ListGridExample() {
    val data = (1..12).map { "Item $it" }

    AntList(
        dataSource = data,
        grid = ListGrid(
            gutter = 16.dp,
            column = 4,
            xs = 1,
            sm = 2,
            md = 3,
            lg = 4
        ),
        renderItem = { item, index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(item)
            }
        }
    )
}

@Composable
fun ListLoadingExample() {
    var loading by remember { mutableStateOf(true) }
    val data = listOf("Item 1", "Item 2", "Item 3")

    AntList(
        dataSource = data,
        bordered = true,
        loading = if (loading) ListLoading.Config(
            spinning = true,
            tip = "Loading..."
        ) else ListLoading.False,
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListPaginationExample() {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(5) }
    val data = (1..50).map { "Item $it" }

    AntList(
        dataSource = data,
        bordered = true,
        pagination = ListPagination.Config(
            current = current,
            pageSize = pageSize,
            total = data.size,
            showSizeChanger = true,
            onChange = { page, size ->
                current = page
                pageSize = size
            }
        ),
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListLoadMoreExample() {
    var visibleCount by remember { mutableStateOf(5) }
    val allData = (1..50).map { "Item $it" }
    val displayData = allData.take(visibleCount)

    AntList(
        dataSource = displayData,
        bordered = true,
        loadMore = {
            if (visibleCount < allData.size) {
                AntButton(
                    onClick = { visibleCount += 5 },
                    type = ButtonType.Link
                ) {
                    Text("Load More")
                }
            }
        },
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListVerticalLayoutExample() {
    data class Article(
        val title: String,
        val description: String,
        val content: String,
        val image: String,
    )

    val data = listOf(
        Article(
            "Ant Design Title 1",
            "Ant Design, a design language for background applications",
            "We supply a series of design principles, practical patterns...",
            "img1"
        ),
        Article(
            "Ant Design Title 2",
            "Ant Design, a design language for background applications",
            "We supply a series of design principles, practical patterns...",
            "img2"
        )
    )

    AntList(
        dataSource = data,
        itemLayout = ListItemLayout.Vertical,
        bordered = true,
        renderItem = { item, index ->
            AntListItem(
                actions = listOf(
                    { Text("156", color = Color(0xFF00000073)) },
                    { Text("156", color = Color(0xFF00000073)) },
                    { Text("2", color = Color(0xFF00000073)) }
                ),
                extra = {
                    Box(
                        modifier = Modifier
                            .size(272.dp, 150.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    )
                }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = item.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000D9)
                    )
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = Color(0xFF00000073)
                    )
                    Text(
                        text = item.content,
                        fontSize = 14.sp,
                        color = Color(0xFF00000073)
                    )
                }
            }
        }
    )
}

@Composable
fun ListWithHeaderFooterExample() {
    val data = (1..5).map { "Item $it" }

    AntList(
        dataSource = data,
        bordered = true,
        header = {
            Text(
                text = "Header",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        footer = {
            Text(
                text = "Footer",
                fontSize = 14.sp,
                color = Color(0xFF00000073)
            )
        },
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListSizesExample() {
    val data = listOf("Item 1", "Item 2", "Item 3")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Large Size:")
        AntList(
            dataSource = data,
            bordered = true,
            size = ListSize.Large,
            renderItem = { item, _ ->
                AntListItem { Text(item) }
            }
        )

        Text("Default Size:")
        AntList(
            dataSource = data,
            bordered = true,
            size = ListSize.Default,
            renderItem = { item, _ ->
                AntListItem { Text(item) }
            }
        )

        Text("Small Size:")
        AntList(
            dataSource = data,
            bordered = true,
            size = ListSize.Small,
            renderItem = { item, _ ->
                AntListItem { Text(item) }
            }
        )
    }
}

@Composable
fun ListInfiniteScrollExample() {
    var items by remember { mutableStateOf((1..20).map { "Item $it" }) }
    var buttonLoading by remember { mutableStateOf(false) }

    AntList(
        dataSource = items,
        bordered = true,
        loadMore = {
            AntButton(
                onClick = {
                    // In a real app, you would load more items asynchronously
                    buttonLoading = true
                    // Simulate immediate addition for this example
                    items = items + ((items.size + 1)..(items.size + 20)).map { "Item $it" }
                    buttonLoading = false
                },
                loading = if (buttonLoading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Load More")
            }
        },
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListSemanticStylingExample() {
    val data = listOf("Item 1", "Item 2", "Item 3")

    // v5.4.0+ Semantic styling support
    AntList(
        dataSource = data,
        bordered = true,
        header = { Text("Custom Header") },
        footer = { Text("Custom Footer") },
        rootStyle = Modifier.padding(16.dp),
        styles = ListStyles(
            header = Modifier.background(Color(0xFFF0F0F0)),
            footer = Modifier.background(Color(0xFFFAFAFA)),
            item = Modifier.background(Color.White)
        ),
        renderItem = { item, index ->
            AntListItem(
                actions = listOf(
                    { Text("Edit", color = Color(0xFF1890FF)) }
                ),
                extra = { Text("Extra") },
                styles = ListItemStyles(
                    actions = Modifier.background(Color(0xFFF5F5F5).copy(alpha = 0.5f)),
                    extra = Modifier.padding(8.dp)
                )
            ) {
                Text(item)
            }
        }
    )
}

@Composable
fun ListVirtualScrollExample() {
    val largeDataset = (1..10000).map { "Item $it" }

    // Virtual scrolling for large datasets
    AntList(
        dataSource = largeDataset,
        bordered = true,
        virtual = true, // Enable virtual scrolling
        renderItem = { item, index ->
            AntListItem {
                Text(item)
            }
        }
    )
}

@Composable
fun ListCustomEmptyStateExample() {
    val emptyData = emptyList<String>()

    AntList(
        dataSource = emptyData,
        bordered = true,
        locale = ListLocale(
            emptyText = "No data available",
            emptyTextComposable = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("No Data", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Try adding some items", fontSize = 12.sp, color = Color.Gray)
                }
            }
        ),
        renderItem = { item, index ->
            AntListItem { Text(item) }
        }
    )
}
