package digital.guimauve.antdesign

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

// ============ Data Classes ============

/**
 * Configuration for a table column.
 * Provides comprehensive control over column appearance and behavior.
 *
 * @param title Column title (header text or function)
 * @param dataIndex Property name to access data in record (supports nested paths)
 * @param key Unique key for the column
 * @param width Fixed width for the column
 * @param minWidth Minimum width (auto table-layout only, v5.21.0+)
 * @param maxWidth Maximum width when resizing
 * @param fixed Fix column to left or right when scrolling horizontally
 * @param align Text alignment in the column
 * @param className Column CSS class name
 * @param colSpan Column span for title
 * @param ellipsis Enable text ellipsis (forces tableLayout: fixed)
 * @param ellipsisTooltip Show tooltip when text is ellipsized
 * @param ellipsisShowTitle Show title attribute on ellipsized text (v4.3.0+)
 * @param sorter Sorting configuration
 * @param sortOrder Current sort order (controlled)
 * @param defaultSortOrder Default sort order (uncontrolled)
 * @param sortDirections Available sort directions
 * @param showSorterTooltip Show tooltip on sorter icon (boolean or TooltipProps)
 * @param sortIcon Custom sort icon renderer (v5.6.0+)
 * @param filters Filter menu items
 * @param filteredValue Current filtered values (controlled)
 * @param defaultFilteredValue Default filtered values (uncontrolled)
 * @param filterResetToDefaultFilteredValue Reset to default on filter clear
 * @param filtered Whether dataSource is currently filtered (state indicator)
 * @param filterMultiple Allow multiple filter values
 * @param filterSearch Enable search in filter dropdown (v4.17.0+)
 * @param filterMode Filter mode: Menu or Tree (v4.17.0+)
 * @param filterIcon Custom filter icon renderer
 * @param filterDropdown Custom filter overlay content
 * @param filterOnClose Trigger filter on menu close (v5.15.0+)
 * @param filterDropdownProps Custom dropdown props (v5.22.0+)
 * @param onFilter Filter function to apply
 * @param render Custom render function for cell content
 * @param children Child columns for column groups
 * @param onCell Function to get cell properties
 * @param onHeaderCell Function to get header cell properties
 * @param resizable Enable column resizing
 * @param responsive Responsive breakpoints to show/hide column (v4.2.0+)
 * @param rowScope Set scope attribute for accessibility (v5.1.0+)
 * @param shouldCellUpdate Optimization: only re-render cell when this returns true (v4.3.0+)
 * @param hidden Hide this column completely (v5.13.0+)
 */
data class TableColumn<T>(
    val title: Any, // String or @Composable function
    val dataIndex: String? = null,
    val key: String? = null,
    val width: Dp? = null,
    val minWidth: Dp? = null,
    val maxWidth: Dp? = null,
    val fixed: TableColumnFixed? = null,
    val align: TableColumnAlign = TableColumnAlign.Left,
    val className: String? = null,
    val colSpan: Int = 1,
    val ellipsis: Boolean = false,
    val ellipsisTooltip: Boolean = true,
    val ellipsisShowTitle: Boolean = true,
    // Sorter
    val sorter: TableColumnSorter<T>? = null,
    val sortOrder: SortOrder? = null,
    val defaultSortOrder: SortOrder? = null,
    val sortDirections: List<SortOrder> = listOf(SortOrder.Ascend, SortOrder.Descend),
    val showSorterTooltip: ShowSorterTooltip = ShowSorterTooltip.Enabled(),
    val sortIcon: (@Composable (sortOrder: SortOrder?) -> Unit)? = null,
    // Filters
    val filters: List<TableFilter>? = null,
    val filteredValue: List<String>? = null,
    val defaultFilteredValue: List<String>? = null,
    val filterResetToDefaultFilteredValue: Boolean = false,
    val filtered: Boolean = false,
    val filterMultiple: Boolean = true,
    val filterSearch: Boolean = false,
    val filterMode: FilterMode = FilterMode.Menu,
    val filterIcon: (@Composable (filtered: Boolean) -> Unit)? = null,
    val filterDropdown: (@Composable (FilterDropdownProps) -> Unit)? = null,
    val filterOnClose: Boolean = true,
    val filterDropdownProps: Map<String, Any>? = null,
    val onFilter: ((value: String, record: T) -> Boolean)? = null,
    // Render
    val render: (@Composable (T, Int) -> Unit)? = null,
    // Column groups
    val children: List<TableColumn<T>>? = null,
    // Cell props
    val onCell: ((record: T, rowIndex: Int) -> CellProps)? = null,
    val onHeaderCell: ((column: TableColumn<T>) -> CellProps)? = null,
    // Resizable
    val resizable: Boolean = false,
    // Responsive
    val responsive: List<String>? = null,
    // Accessibility
    val rowScope: RowScope? = null,
    // Performance optimization
    val shouldCellUpdate: ((record: T, prevRecord: T) -> Boolean)? = null,
    // Visibility
    val hidden: Boolean = false,
)

/**
 * Configuration for showing sorter tooltip.
 */
sealed class ShowSorterTooltip {
    data class Enabled(
        val target: SorterTooltipTarget = SorterTooltipTarget.FullHeader,
        val title: String? = null,
        val placement: String = "top",
    ) : ShowSorterTooltip()

    object Disabled : ShowSorterTooltip()
}

enum class SorterTooltipTarget {
    FullHeader,
    SorterIcon
}

enum class FilterMode {
    Menu,
    Tree
}

/**
 * Row scope attribute for accessibility (v5.1.0+)
 */
enum class RowScope {
    Row,
    Rowgroup
}

/**
 * Props passed to custom filter dropdown components
 */
data class FilterDropdownProps(
    val filters: List<TableFilter>,
    val selectedKeys: List<String>,
    val setSelectedKeys: (List<String>) -> Unit,
    val confirm: (param: FilterConfirmParam?) -> Unit,
    val clearFilters: (() -> Unit)? = null,
    val visible: Boolean = false,
)

/**
 * Parameter for filter confirm action
 */
data class FilterConfirmParam(
    val closeDropdown: Boolean = true,
)

data class CellProps(
    val colSpan: Int = 1,
    val rowSpan: Int = 1,
    val onClick: (() -> Unit)? = null,
    val className: String? = null,
    val style: String? = null,
)

data class TableColumnSorter<T>(
    val compare: ((T, T) -> Int)?,
    val multiple: Int? = null,
)

enum class TableColumnFixed {
    Left,
    Right
}

enum class TableColumnAlign {
    Left,
    Center,
    Right
}

enum class SortOrder {
    Ascend,
    Descend
}

data class TableFilter(
    val text: String,
    val value: String,
    val children: List<TableFilter>? = null,
)

// ============ Scroll & Virtual Props ============

data class TableScroll(
    val x: Dp? = null,
    val y: Dp? = null,
    val scrollToFirstRowOnChange: Boolean = true,
)

// ============ Sticky Props ============

/**
 * Configuration for sticky headers and columns.
 * Sticky headers remain visible when scrolling vertically.
 */
sealed class StickyConfig {
    object False : StickyConfig()
    object True : StickyConfig()
    data class Config(
        val offsetHeader: Dp = 0.dp,
        val offsetScroll: Dp = 0.dp,
        val getContainer: (() -> Any)? = null,
    ) : StickyConfig()
}

// ============ Row Event Handlers ============

/**
 * Event handlers for table rows.
 * Supports click, double-click, context menu, mouse enter/leave events.
 */
data class RowEventHandlers(
    val onClick: (() -> Unit)? = null,
    val onDoubleClick: (() -> Unit)? = null,
    val onContextMenu: (() -> Unit)? = null,
    val onMouseEnter: (() -> Unit)? = null,
    val onMouseLeave: (() -> Unit)? = null,
)

// ============ Pagination ============

data class TablePagination(
    val current: Int = 1,
    val pageSize: Int = 10,
    val total: Int = 0,
    val showSizeChanger: Boolean = false,
    val showQuickJumper: Boolean = false,
    val showTotal: ((total: Int) -> String)? = null,
    val pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    val onChange: ((page: Int, pageSize: Int) -> Unit)? = null,
    val position: List<PaginationPosition> = listOf(PaginationPosition.BottomRight),
)

enum class PaginationPosition {
    TopLeft, TopCenter, TopRight,
    BottomLeft, BottomCenter, BottomRight,
    None
}

// ============ Expandable ============

/**
 * Configuration for expandable rows in the table.
 * Provides comprehensive control over row expansion behavior and rendering.
 *
 * @param childrenColumnName Property name for nested children data
 * @param columnTitle Expand column title (v4.23.0+)
 * @param columnWidth Width of the expand icon column
 * @param defaultExpandAllRows Expand all rows initially
 * @param defaultExpandedRowKeys Initial expanded row keys (uncontrolled mode)
 * @param expandedRowClassName Custom class name for expanded rows (v5.22.0+)
 * @param expandedRowKeys Controlled list of expanded row keys
 * @param expandedRowRender Render function for expanded content, receives record, index, indent level, and expanded state
 * @param expandIcon Custom expand icon renderer
 * @param expandRowByClick If true, clicking anywhere on the row will expand/collapse it
 * @param fixed Fix expansion icon column (v4.16.0+)
 * @param indentSize Indentation size for nested rows
 * @param rowExpandable Function to determine if a row can be expanded
 * @param showExpandColumn Show/hide expand column (v4.18.0+)
 * @param onExpand Callback when a row is expanded/collapsed
 * @param onExpandedRowsChange Callback when expanded rows change
 * @param expandedRowOffset Offset for expanded row (DEPRECATED v5.26.0+)
 */
data class TableExpandable<T>(
    val childrenColumnName: String = "children",
    val columnTitle: String? = null,
    val columnWidth: Dp = 48.dp,
    val defaultExpandAllRows: Boolean = false,
    val defaultExpandedRowKeys: List<String> = emptyList(),
    val expandedRowClassName: ((record: T, index: Int, indent: Int) -> String)? = null,
    val expandedRowKeys: List<String> = emptyList(),
    val expandedRowRender: (@Composable (record: T, index: Int, indent: Int, expanded: Boolean) -> Unit)? = null,
    val expandIcon: (@Composable (expanded: Boolean, record: T, onExpand: (T) -> Unit) -> Unit)? = null,
    val expandRowByClick: Boolean = false,
    val fixed: TableColumnFixed? = null,
    val indentSize: Dp = 15.dp,
    val rowExpandable: ((record: T) -> Boolean)? = null,
    val showExpandColumn: Boolean = true,
    val onExpand: ((expanded: Boolean, record: T) -> Unit)? = null,
    val onExpandedRowsChange: ((expandedKeys: List<String>) -> Unit)? = null,
    @Deprecated("Use Table.EXPAND_COLUMN instead", ReplaceWith("Table.EXPAND_COLUMN"), level = DeprecationLevel.WARNING)
    val expandedRowOffset: Int? = null,
)

// ============ Tree Data ============

/**
 * Interface for tree-structured data in tables.
 * Implement this interface to enable hierarchical data display.
 */
interface TableTreeNode {
    val children: List<TableTreeNode>?
}

/**
 * Data class representing a flattened tree node for rendering
 */
private data class FlattenedTreeNode<T>(
    val data: T,
    val key: String,
    val level: Int,
    val hasChildren: Boolean,
    val parentKey: String?,
    val index: Int,
)

// ============ Table Props ============

enum class TableSize {
    Small,
    Default,
    Middle,
    Large
}

enum class TableLayout {
    Auto,    // CSS table-layout: auto (adapts to content)
    Fixed    // CSS table-layout: fixed (better performance, equal columns)
}

/**
 * Configuration for row selection in the table.
 * Provides comprehensive control over row selection behavior.
 *
 * @param align Selection column alignment (v5.25.0+)
 * @param checkStrictly Enable strict checking (parent-child independence, v4.4.0+)
 * @param columnTitle Custom title for selection column
 * @param columnWidth Width of the selection column
 * @param defaultSelectedRowKeys Initial selected row keys (uncontrolled mode)
 * @param fixed Fix the selection column left
 * @param getCheckboxProps Function to get checkbox properties for specific rows (e.g., to disable certain rows)
 * @param getTitleCheckboxProps Function to get title checkbox properties
 * @param hideSelectAll Hide the select all checkbox in header (v4.3.0+)
 * @param preserveSelectedRowKeys Keep selection even when key doesn't exist in dataSource (v4.4.0+)
 * @param renderCell Custom render function for selection cells (v4.1.0+)
 * @param selectedRowKeys Controlled list of selected row keys
 * @param selections Custom selection config or true for defaults
 * @param type Selection type: Checkbox or Radio
 * @param onChange Callback when selection changes (includes info.type)
 * @param onSelect Individual row selection callback
 * @param onSelectAll Select all callback
 * @param onSelectInvert Invert selection callback
 * @param onSelectNone Clear selection callback
 */
data class TableRowSelection<T>(
    val align: TableColumnAlign = TableColumnAlign.Left,
    val checkStrictly: Boolean = true,
    val columnTitle: Any? = null, // String or @Composable
    val columnWidth: Dp = 32.dp,
    val defaultSelectedRowKeys: List<Int> = emptyList(),
    val fixed: Boolean = false,
    val getCheckboxProps: ((record: T) -> CheckboxProps)? = null,
    val getTitleCheckboxProps: (() -> CheckboxProps)? = null,
    val hideSelectAll: Boolean = false,
    val preserveSelectedRowKeys: Boolean = false,
    val renderCell: (@Composable (checked: Boolean, record: T, index: Int, originNode: @Composable () -> Unit) -> Unit)? = null,
    val selectedRowKeys: List<Int> = emptyList(),
    val selections: Any? = null, // List<SelectionConfig> or Boolean
    val type: TableRowSelectionType = TableRowSelectionType.Checkbox,
    val onChange: ((selectedRowKeys: List<Int>, selectedRows: List<T>, info: SelectionInfo) -> Unit)? = null,
    val onSelect: ((record: T, selected: Boolean, selectedRows: List<T>) -> Unit)? = null,
    val onSelectAll: ((selected: Boolean, selectedRows: List<T>, changeRows: List<T>) -> Unit)? = null,
    val onSelectInvert: ((selectedRowKeys: List<Int>) -> Unit)? = null,
    val onSelectNone: (() -> Unit)? = null,
)

/**
 * Selection information passed to onChange callback
 */
data class SelectionInfo(
    val type: SelectionType,
)

enum class SelectionType {
    Single,
    Multiple,
    All,
    Invert,
    None
}

/**
 * Custom selection configuration
 */
data class SelectionConfig(
    val key: String,
    val text: String,
    val onSelect: (changeableRowKeys: List<Int>) -> Unit,
)

/**
 * Properties for configuring checkbox behavior in row selection.
 *
 * @param disabled Whether the checkbox is disabled
 * @param indeterminate Whether the checkbox is in indeterminate state
 * @param title Tooltip title for the checkbox
 */
data class CheckboxProps(
    val disabled: Boolean = false,
    val indeterminate: Boolean = false,
    val title: String? = null,
)

enum class TableRowSelectionType {
    Checkbox,
    Radio
}

data class TableSorterInfo(
    val columnKey: String? = null,
    val order: SortOrder? = null,
    val field: String? = null,
)

/**
 * Extra information passed to onChange callback
 */
data class TableChangeExtra<T>(
    val currentDataSource: List<T>,
    val action: TableAction,
)

enum class TableAction {
    Paginate,
    Sort,
    Filter
}

data class TableComponents(
    val table: (@Composable (content: @Composable () -> Unit) -> Unit)? = null,
    val header: (@Composable (content: @Composable () -> Unit) -> Unit)? = null,
    val body: (@Composable (content: @Composable () -> Unit) -> Unit)? = null,
)

// ============ Semantic Styling (v5.4.0+) ============

/**
 * Semantic class names for table parts (v5.4.0+)
 * Allows granular styling of table components
 */
data class TableClassNames(
    val root: String? = null,
    val wrapper: String? = null,
    val header: String? = null,
    val headerRow: String? = null,
    val headerCell: String? = null,
    val body: String? = null,
    val bodyRow: String? = null,
    val bodyCell: String? = null,
    val footer: String? = null,
    val pagination: String? = null,
    val expandIcon: String? = null,
    val expandedRow: String? = null,
    val selection: String? = null,
    val sorter: String? = null,
    val filter: String? = null,
)

/**
 * Semantic styles for table parts (v5.4.0+)
 * Allows granular Modifier styling of table components
 */
data class TableStyles(
    val root: Modifier? = null,
    val wrapper: Modifier? = null,
    val header: Modifier? = null,
    val headerRow: Modifier? = null,
    val headerCell: Modifier? = null,
    val body: Modifier? = null,
    val bodyRow: Modifier? = null,
    val bodyCell: Modifier? = null,
    val footer: Modifier? = null,
    val pagination: Modifier? = null,
    val expandIcon: Modifier? = null,
    val expandedRow: Modifier? = null,
    val selection: Modifier? = null,
    val sorter: Modifier? = null,
    val filter: Modifier? = null,
)

/**
 * Table ref interface for programmatic control (v5.11.0+)
 */
interface TableRef {
    /**
     * Get the native wrapper element
     */
    val nativeElement: Any?

    /**
     * Scroll to specific position, row key, or index
     * @param config Scroll configuration
     */
    fun scrollTo(config: ScrollToConfig)
}

/**
 * Scroll configuration for programmatic scrolling (v5.11.0+)
 */
data class ScrollToConfig(
    val index: Int? = null,
    val key: String? = null,
    val top: Int? = null,
)

// ============ Internal State ============

internal data class SortState<T>(
    val columnKey: String,
    val order: SortOrder?,
    val sorter: TableColumnSorter<T>?,
    val column: TableColumn<T>,
)

internal data class FilterState(
    val columnKey: String,
    val filteredValues: List<String>,
)

internal data class ColumnWidth(
    val key: String,
    var width: Dp,
)

// ============ Main Component ============

/**
 * A comprehensive data table component with advanced features.
 *
 * Supports:
 * - Sorting (single and multiple columns)
 * - Filtering with search
 * - Pagination with multiple positions
 * - Row selection (checkbox/radio) with advanced callbacks
 * - Expandable rows with custom content
 * - Virtual scrolling for large datasets (v5.9.0+)
 * - Column resizing
 * - Fixed columns (left/right)
 * - Sticky headers with configurable offsets (v4.6.0+)
 * - Tree/hierarchical data
 * - Column groups
 * - Custom cell rendering
 * - Responsive columns
 * - Summary rows
 * - Row event handlers (click, hover, context menu)
 * - Column transformation
 * - Semantic styling (classNames, styles) - v5.4.0+
 * - Programmatic control via ref (v5.11.0+)
 *
 * @param columns List of column configurations
 * @param dataSource Data to display in the table
 * @param modifier Modifier for the table container
 * @param bordered Show borders around table and cells
 * @param childrenColumnName Property name for nested children in tree data
 * @param classNames Semantic class names for table parts (v5.4.0+)
 * @param components Custom component overrides
 * @param emptyText Custom empty state content
 * @param expandable Expandable rows configuration
 * @param footer Table footer renderer
 * @param getPopupContainer Dropdown render container function
 * @param indentSize Indentation size for tree/nested data
 * @param loading Show loading skeleton or spinner
 * @param locale Custom locale configuration for i18n
 * @param onChange Callback when pagination, filters, or sorting changes
 * @param onHeaderRow Callback to attach event handlers to header rows
 * @param onRow Callback to attach event handlers to rows
 * @param onScroll Body scroll event handler (v5.16.0+)
 * @param pagination Pagination configuration, false to disable
 * @param ref Table ref for programmatic control (v5.11.0+)
 * @param rowClassName Function to generate dynamic class names for rows
 * @param rowHoverable Enable row hover effect (v5.16.0+)
 * @param rowKey Function to extract unique key from each row
 * @param rowSelection Row selection configuration
 * @param scroll Scroll configuration (x/y dimensions)
 * @param showHeader Show/hide table header
 * @param showSorterTooltip Show tooltip on sorter (v5.16.0+)
 * @param size Table size (affects padding)
 * @param sortDirections Supported sort directions
 * @param sticky Enable sticky header configuration
 * @param styles Semantic styles for table parts (v5.4.0+)
 * @param summary Summary row renderer (for totals, averages, etc.)
 * @param tableLayout Table layout mode (Auto or Fixed)
 * @param title Table title renderer
 * @param transformColumns Transform columns before rendering
 * @param virtual Enable virtual scrolling (v5.9.0+)
 */
@Composable
fun <T> AntTable(
    columns: List<TableColumn<T>>,
    dataSource: List<T>,
    modifier: Modifier = Modifier,
    bordered: Boolean = true,
    childrenColumnName: String = "children",
    classNames: TableClassNames? = null,
    components: TableComponents? = null,
    emptyText: Any = "No data",
    expandable: TableExpandable<T>? = null,
    footer: (@Composable (currentPageData: List<T>) -> Unit)? = null,
    getPopupContainer: ((triggerNode: Any) -> Any)? = null,
    indentSize: Dp = 15.dp,
    loading: Boolean = false,
    locale: Map<String, String>? = null,
    onChange: ((
        pagination: TablePagination?,
        filters: Map<String, List<String>>,
        sorter: TableSorterInfo,
        extra: TableChangeExtra<T>,
    ) -> Unit)? = null,
    onHeaderRow: ((columns: List<TableColumn<T>>, index: Int) -> RowEventHandlers)? = null,
    onRow: ((record: T, index: Int) -> RowEventHandlers)? = null,
    onScroll: ((event: Any) -> Unit)? = null,
    pagination: TablePagination? = TablePagination(),
    ref: TableRef? = null,
    rowClassName: ((record: T, index: Int) -> String)? = null,
    rowHoverable: Boolean = true,
    rowKey: ((T) -> String)? = null,
    rowSelection: TableRowSelection<T>? = null,
    scroll: TableScroll? = null,
    showHeader: Boolean = true,
    showSorterTooltip: ShowSorterTooltip = ShowSorterTooltip.Enabled(target = SorterTooltipTarget.FullHeader),
    size: TableSize = TableSize.Default,
    sortDirections: List<SortOrder> = listOf(SortOrder.Ascend, SortOrder.Descend),
    sticky: StickyConfig = StickyConfig.False,
    styles: TableStyles? = null,
    summary: (@Composable (data: List<T>) -> Unit)? = null,
    tableLayout: TableLayout = TableLayout.Auto,
    title: (@Composable (currentPageData: List<T>) -> Unit)? = null,
    transformColumns: ((columns: List<TableColumn<T>>) -> List<TableColumn<T>>)? = null,
    virtual: Boolean = false,
) {
    val config = useConfig()
    val localeConfig = useLocale()

    // State management
    var sortStates by remember {
        mutableStateOf(
            columns.mapIndexedNotNull { index, col ->
                if (col.sorter != null) {
                    SortState(
                        columnKey = col.key ?: col.dataIndex ?: "column_$index",
                        order = col.sortOrder ?: col.defaultSortOrder,
                        sorter = col.sorter,
                        column = col
                    )
                } else null
            }
        )
    }

    var filterStates by remember {
        mutableStateOf(
            columns.mapIndexedNotNull { index, col ->
                if (col.filters != null) {
                    FilterState(
                        columnKey = col.key ?: col.dataIndex ?: "column_$index",
                        filteredValues = col.filteredValue ?: col.defaultFilteredValue ?: emptyList()
                    )
                } else null
            }
        )
    }

    var expandedKeys by remember {
        mutableStateOf(
            if (expandable?.expandedRowKeys?.isNotEmpty() == true) {
                expandable.expandedRowKeys
            } else {
                expandable?.defaultExpandedRowKeys ?: emptyList()
            }
        )
    }

    // Update expanded keys callback
    LaunchedEffect(expandedKeys) {
        expandable?.onExpandedRowsChange?.invoke(expandedKeys)
    }

    var currentPage by remember { mutableStateOf(pagination?.current ?: 1) }
    var currentPageSize by remember { mutableStateOf(pagination?.pageSize ?: 10) }

    // Sync internal state with external pagination props (for controlled mode)
    LaunchedEffect(pagination?.current, pagination?.pageSize) {
        pagination?.current?.let { currentPage = it }
        pagination?.pageSize?.let { currentPageSize = it }
    }

    // Resizable columns state
    val resizableColumnWidths = remember {
        mutableStateMapOf<String, Dp>().apply {
            columns.forEachIndexed { index, col ->
                val key = col.key ?: col.dataIndex ?: "column_$index"
                col.width?.let { this[key] = it }
            }
        }
    }

    // Apply column transformation
    val transformedColumns = remember(columns, transformColumns) {
        transformColumns?.invoke(columns) ?: columns
    }

    // Flatten columns (handle column groups) and filter hidden columns
    val flattenedColumns = remember(transformedColumns) {
        flattenColumns(transformedColumns).filter { !it.hidden }
    }

    // Apply sorting
    val sortedData = remember(dataSource, sortStates) {
        applySorting(dataSource, sortStates)
    }

    // Apply filtering
    val filteredData = remember(sortedData, filterStates, flattenedColumns) {
        applyFiltering(sortedData, filterStates, flattenedColumns)
    }

    // Apply pagination
    // Note: If pagination.total is provided, we assume server-side pagination and skip client-side pagination
    val paginatedData = remember(filteredData, currentPage, currentPageSize, pagination) {
        if (pagination != null && pagination.total == 0) {
            // Client-side pagination: total not provided, paginate the data locally
            val start = (currentPage - 1) * currentPageSize
            val end = minOf(start + currentPageSize, filteredData.size)
            if (start < filteredData.size) {
                filteredData.subList(start, end)
            } else {
                emptyList()
            }
        } else {
            // Server-side pagination: data is already paginated, just display it
            filteredData
        }
    }

    val padding = when (size) {
        TableSize.Small -> 8.dp
        TableSize.Middle, TableSize.Default -> 12.dp
        TableSize.Large -> 16.dp
    }

    // Scroll state for virtual scrolling
    val listState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = config.theme.token.colorBgBase
        ),
        border = if (bordered) BorderStroke(1.dp, Color(0xFFD9D9D9)) else null
    ) {
        Column {
            // Title
            if (title != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(config.theme.components.table.headerBg)
                        .padding(padding)
                ) {
                    title(paginatedData)
                }
            }

            // Table content with horizontal scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (scroll?.x != null) {
                            Modifier.horizontalScroll(horizontalScrollState)
                        } else Modifier
                    )
            ) {
                Column(
                    modifier = if (scroll?.y != null) {
                        Modifier.heightIn(max = scroll.y)
                    } else Modifier
                ) {
                    // Header
                    if (showHeader) {
                        TableHeader(
                            columns = transformedColumns,
                            flattenedColumns = flattenedColumns,
                            padding = padding,
                            sortStates = sortStates,
                            filterStates = filterStates,
                            rowSelection = rowSelection,
                            dataSource = paginatedData,
                            config = config,
                            locale = localeConfig,
                            sticky = sticky,
                            resizableColumnWidths = resizableColumnWidths,
                            onSortChange = { columnKey, nextOrder ->
                                sortStates = updateSortStates(sortStates, columnKey, nextOrder, flattenedColumns)

                                val sorterInfo = sortStates.firstOrNull { it.columnKey == columnKey }?.let {
                                    TableSorterInfo(
                                        columnKey = it.columnKey,
                                        order = it.order,
                                        field = it.column.dataIndex
                                    )
                                } ?: TableSorterInfo()

                                onChange?.invoke(
                                    pagination?.copy(current = currentPage, pageSize = currentPageSize),
                                    filterStates.associate { it.columnKey to it.filteredValues },
                                    sorterInfo,
                                    TableChangeExtra(filteredData, TableAction.Sort)
                                )
                            },
                            onFilterChange = { columnKey, values ->
                                filterStates = filterStates.map {
                                    if (it.columnKey == columnKey) it.copy(filteredValues = values) else it
                                }

                                onChange?.invoke(
                                    pagination?.copy(current = currentPage, pageSize = currentPageSize),
                                    filterStates.associate { it.columnKey to it.filteredValues },
                                    TableSorterInfo(),
                                    TableChangeExtra(filteredData, TableAction.Filter)
                                )
                            },
                            onColumnResize = { key, newWidth ->
                                resizableColumnWidths[key] = newWidth
                            },
                            onHeaderRow = onHeaderRow
                        )
                    }

                    // Body
                    if (loading) {
                        repeat(5) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(padding)
                            ) {
                                AntSkeleton(loading = true)
                            }
                        }
                    } else if (paginatedData.isEmpty()) {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding * 4),
                            contentAlignment = Alignment.Center
                        ) {
                            if (emptyText is String) {
                                Text(
                                    text = emptyText,
                                    fontSize = 14.sp,
                                    color = Color(0xFF000000).copy(alpha = 0.45f)
                                )
                            } else {
                                // Default empty text
                                Text(
                                    text = "No data",
                                    fontSize = 14.sp,
                                    color = Color(0xFF000000).copy(alpha = 0.45f)
                                )
                            }
                        }
                    } else {
                        // Virtual scrolling or regular
                        if (virtual && scroll?.y != null) {
                            VirtualTableBody(
                                data = paginatedData,
                                columns = flattenedColumns,
                                padding = padding,
                                rowKey = rowKey,
                                rowSelection = rowSelection,
                                expandable = expandable,
                                expandedKeys = expandedKeys,
                                onRow = onRow,
                                onExpandClick = { itemKey, item, isExpanded ->
                                    val newExpanded = !isExpanded
                                    expandedKeys = if (newExpanded) {
                                        expandedKeys + itemKey
                                    } else {
                                        expandedKeys - itemKey
                                    }
                                    expandable?.onExpand?.invoke(newExpanded, item)
                                },
                                listState = listState,
                                resizableColumnWidths = resizableColumnWidths,
                                rowClassName = rowClassName
                            )
                        } else {
                            RegularTableBody(
                                data = paginatedData,
                                columns = flattenedColumns,
                                padding = padding,
                                rowKey = rowKey,
                                rowSelection = rowSelection,
                                expandable = expandable,
                                expandedKeys = expandedKeys,
                                onRow = onRow,
                                onExpandClick = { itemKey, item, isExpanded ->
                                    val newExpanded = !isExpanded
                                    expandedKeys = if (newExpanded) {
                                        expandedKeys + itemKey
                                    } else {
                                        expandedKeys - itemKey
                                    }
                                    expandable?.onExpand?.invoke(newExpanded, item)
                                },
                                resizableColumnWidths = resizableColumnWidths,
                                rowClassName = rowClassName
                            )
                        }

                        // Summary row
                        if (summary != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(config.theme.components.table.headerBg)
                                    .padding(padding)
                            ) {
                                summary(paginatedData)
                            }
                        }
                    }
                }
            }

            // Footer
            if (footer != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(config.theme.components.table.headerBg)
                        .padding(padding)
                ) {
                    footer(paginatedData)
                }
            }

            // Pagination
            if (pagination != null && filteredData.isNotEmpty() && !pagination.position.contains(PaginationPosition.None)) {
                val primaryPosition = pagination.position.firstOrNull() ?: PaginationPosition.BottomRight
                val paginationAlign = when (primaryPosition) {
                    PaginationPosition.TopLeft, PaginationPosition.BottomLeft -> PaginationAlign.Start
                    PaginationPosition.TopCenter, PaginationPosition.BottomCenter -> PaginationAlign.Center
                    PaginationPosition.TopRight, PaginationPosition.BottomRight, PaginationPosition.None -> PaginationAlign.End
                }

                Box(modifier = Modifier.padding(16.dp)) {
                    AntPagination(
                        current = currentPage,
                        total = pagination.total,
                        pageSize = currentPageSize,
                        showSizeChanger = pagination.showSizeChanger,
                        showQuickJumper = pagination.showQuickJumper,
                        showTotal = pagination.showTotal?.let { showTotalFn ->
                            { total, range -> showTotalFn(total) }
                        },
                        pageSizeOptions = pagination.pageSizeOptions,
                        align = paginationAlign,
                        onChange = { page, pageSize ->
                            currentPage = page
                            currentPageSize = pageSize
                            pagination.onChange?.invoke(page, pageSize)

                            onChange?.invoke(
                                pagination.copy(current = page, pageSize = pageSize, total = filteredData.size),
                                filterStates.associate { it.columnKey to it.filteredValues },
                                TableSorterInfo(),
                                TableChangeExtra(filteredData, TableAction.Paginate)
                            )
                        }
                    )
                }
            }
        }
    }
}

// ============ Helper Functions ============

private fun <T> flattenColumns(columns: List<TableColumn<T>>): List<TableColumn<T>> {
    val result = mutableListOf<TableColumn<T>>()

    fun flatten(cols: List<TableColumn<T>>) {
        cols.forEach { col ->
            if (col.children != null && col.children.isNotEmpty()) {
                flatten(col.children)
            } else {
                result.add(col)
            }
        }
    }

    flatten(columns)
    return result
}

private fun <T> getColumnDepth(columns: List<TableColumn<T>>): Int {
    var maxDepth = 1

    fun calculateDepth(cols: List<TableColumn<T>>, currentDepth: Int) {
        cols.forEach { col ->
            if (col.children != null && col.children.isNotEmpty()) {
                maxDepth = maxOf(maxDepth, currentDepth + 1)
                calculateDepth(col.children, currentDepth + 1)
            }
        }
    }

    calculateDepth(columns, 1)
    return maxDepth
}

// ============ Helper Components ============

@Composable
private fun <T> TableHeader(
    columns: List<TableColumn<T>>,
    flattenedColumns: List<TableColumn<T>>,
    padding: Dp,
    sortStates: List<SortState<T>>,
    filterStates: List<FilterState>,
    rowSelection: TableRowSelection<T>?,
    dataSource: List<T>,
    config: ConfigProviderProps,
    locale: LocaleConfig,
    sticky: StickyConfig,
    resizableColumnWidths: Map<String, Dp>,
    onSortChange: (String, SortOrder?) -> Unit,
    onFilterChange: (String, List<String>) -> Unit,
    onColumnResize: (String, Dp) -> Unit,
    onHeaderRow: ((columns: List<TableColumn<T>>, index: Int) -> RowEventHandlers)? = null,
) {
    val hasColumnGroups = columns.any { it.children != null && it.children.isNotEmpty() }

    val stickyModifier = when (sticky) {
        is StickyConfig.True -> Modifier.zIndex(10f)
        is StickyConfig.Config -> Modifier.zIndex(10f)
        else -> Modifier
    }

    if (hasColumnGroups) {
        // Multi-level header
        val depth = getColumnDepth(columns)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(stickyModifier)
                .background(config.theme.components.table.headerBg)
        ) {
            repeat(depth) { level ->
                TableHeaderLevel(
                    columns = columns,
                    level = level,
                    padding = padding,
                    sortStates = sortStates,
                    filterStates = filterStates,
                    rowSelection = if (level == 0) rowSelection else null,
                    dataSource = dataSource,
                    config = config,
                    locale = locale,
                    resizableColumnWidths = resizableColumnWidths,
                    onSortChange = onSortChange,
                    onFilterChange = onFilterChange,
                    onColumnResize = onColumnResize
                )
            }
        }
    } else {
        // Single level header
        TableHeaderRow(
            columns = flattenedColumns,
            padding = padding,
            sortStates = sortStates,
            filterStates = filterStates,
            rowSelection = rowSelection,
            dataSource = dataSource,
            config = config,
            locale = locale,
            sticky = sticky,
            resizableColumnWidths = resizableColumnWidths,
            onSortChange = onSortChange,
            onFilterChange = onFilterChange,
            onColumnResize = onColumnResize,
            onHeaderRow = onHeaderRow
        )
    }
}

@Composable
private fun <T> TableHeaderLevel(
    columns: List<TableColumn<T>>,
    level: Int,
    padding: Dp,
    sortStates: List<SortState<T>>,
    filterStates: List<FilterState>,
    rowSelection: TableRowSelection<T>?,
    dataSource: List<T>,
    config: ConfigProviderProps,
    locale: LocaleConfig,
    resizableColumnWidths: Map<String, Dp>,
    onSortChange: (String, SortOrder?) -> Unit,
    onFilterChange: (String, List<String>) -> Unit,
    onColumnResize: (String, Dp) -> Unit,
    currentLevel: Int = 0,
) {
    if (currentLevel == level) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(config.theme.components.table.headerBg)
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection column
            if (rowSelection != null && !rowSelection.hideSelectAll && level == 0) {
                Box(
                    modifier = Modifier.width(rowSelection.columnWidth),
                    contentAlignment = Alignment.Center
                ) {
                    if (rowSelection.type == TableRowSelectionType.Checkbox) {
                        AntCheckbox(
                            checked = rowSelection.selectedRowKeys.size == dataSource.size && dataSource.isNotEmpty(),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    val allKeys = dataSource.indices.toList()
                                    rowSelection.onChange?.invoke(allKeys, dataSource, SelectionInfo(SelectionType.All))
                                } else {
                                    rowSelection.onChange?.invoke(
                                        emptyList(),
                                        emptyList(),
                                        SelectionInfo(SelectionType.None)
                                    )
                                }
                            }
                        )
                    }
                }
            }

            // Columns
            columns.forEach { column ->
                if (column.children != null && column.children.isNotEmpty()) {
                    // Group header
                    val theme = useTheme()
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = column.title.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.token.colorText
                        )
                    }
                } else {
                    // Leaf column
                    TableHeaderCell(
                        column = column,
                        sortStates = sortStates,
                        filterStates = filterStates,
                        locale = locale,
                        resizableColumnWidths = resizableColumnWidths,
                        onSortChange = onSortChange,
                        onFilterChange = onFilterChange,
                        onColumnResize = onColumnResize,
                        modifier = Modifier.weight(column.width?.value ?: 1f)
                    )
                }
            }
        }
    } else if (currentLevel < level) {
        columns.forEach { column ->
            if (column.children != null && column.children.isNotEmpty()) {
                TableHeaderLevel(
                    columns = column.children,
                    level = level,
                    padding = padding,
                    sortStates = sortStates,
                    filterStates = filterStates,
                    rowSelection = null,
                    dataSource = dataSource,
                    config = config,
                    locale = locale,
                    resizableColumnWidths = resizableColumnWidths,
                    onSortChange = onSortChange,
                    onFilterChange = onFilterChange,
                    onColumnResize = onColumnResize,
                    currentLevel = currentLevel + 1
                )
            }
        }
    }
}

@Composable
private fun <T> TableHeaderRow(
    columns: List<TableColumn<T>>,
    padding: Dp,
    sortStates: List<SortState<T>>,
    filterStates: List<FilterState>,
    rowSelection: TableRowSelection<T>?,
    dataSource: List<T>,
    config: ConfigProviderProps,
    locale: LocaleConfig,
    sticky: StickyConfig,
    resizableColumnWidths: Map<String, Dp>,
    onSortChange: (String, SortOrder?) -> Unit,
    onFilterChange: (String, List<String>) -> Unit,
    onColumnResize: (String, Dp) -> Unit,
    onHeaderRow: ((columns: List<TableColumn<T>>, index: Int) -> RowEventHandlers)? = null,
) {
    val stickyModifier = when (sticky) {
        is StickyConfig.True -> Modifier.zIndex(10f)
        is StickyConfig.Config -> Modifier.zIndex(10f)
        else -> Modifier
    }

    val headerRowHandlers = onHeaderRow?.invoke(columns, 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(stickyModifier)
            .background(config.theme.components.table.headerBg)
            .padding(padding)
            .then(
                if (headerRowHandlers != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { headerRowHandlers.onClick?.invoke() },
                            onDoubleTap = { headerRowHandlers.onDoubleClick?.invoke() },
                            onLongPress = { headerRowHandlers.onContextMenu?.invoke() }
                        )
                    }
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection column
        if (rowSelection != null && !rowSelection.hideSelectAll) {
            Box(
                modifier = Modifier.width(rowSelection.columnWidth),
                contentAlignment = Alignment.Center
            ) {
                if (rowSelection.type == TableRowSelectionType.Checkbox) {
                    AntCheckbox(
                        checked = rowSelection.selectedRowKeys.size == dataSource.size && dataSource.isNotEmpty(),
                        onCheckedChange = { checked ->
                            if (checked) {
                                val allKeys = dataSource.indices.toList()
                                rowSelection.onChange?.invoke(allKeys, dataSource, SelectionInfo(SelectionType.All))
                            } else {
                                rowSelection.onChange?.invoke(
                                    emptyList(),
                                    emptyList(),
                                    SelectionInfo(SelectionType.None)
                                )
                            }
                        }
                    )
                }
            }
        }

        // Data columns
        columns.forEach { column ->
            val columnKey = column.key ?: column.dataIndex ?: ""
            val columnWidth = resizableColumnWidths[columnKey] ?: column.width

            TableHeaderCell(
                column = column,
                sortStates = sortStates,
                filterStates = filterStates,
                locale = locale,
                resizableColumnWidths = resizableColumnWidths,
                onSortChange = onSortChange,
                onFilterChange = onFilterChange,
                onColumnResize = onColumnResize,
                modifier = if (columnWidth != null) {
                    Modifier.width(columnWidth)
                } else {
                    Modifier.weight(1f)
                }
            )
        }
    }
}

@Composable
private fun <T> TableHeaderCell(
    column: TableColumn<T>,
    sortStates: List<SortState<T>>,
    filterStates: List<FilterState>,
    locale: LocaleConfig,
    resizableColumnWidths: Map<String, Dp>,
    onSortChange: (String, SortOrder?) -> Unit,
    onFilterChange: (String, List<String>) -> Unit,
    onColumnResize: (String, Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = useTheme()
    val columnKey = column.key ?: column.dataIndex ?: ""
    val sortState = sortStates.firstOrNull { it.columnKey == columnKey }
    val filterState = filterStates.firstOrNull { it.columnKey == columnKey }
    val headerCellProps = column.onHeaderCell?.invoke(column)

    Box(
        modifier = modifier
            .then(
                if (column.resizable) {
                    Modifier.pointerInput(columnKey) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            val currentWidth = resizableColumnWidths[columnKey] ?: column.width ?: 100.dp
                            val newWidth = (currentWidth.value + dragAmount).coerceIn(
                                column.minWidth?.value ?: 50f,
                                column.maxWidth?.value ?: 1000f
                            ).dp
                            onColumnResize(columnKey, newWidth)
                        }
                    }
                } else Modifier
            ),
        contentAlignment = when (column.align) {
            TableColumnAlign.Left -> Alignment.CenterStart
            TableColumnAlign.Center -> Alignment.Center
            TableColumnAlign.Right -> Alignment.CenterEnd
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                enabled = column.sorter != null,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (column.sorter != null) {
                    val currentOrder = sortState?.order
                    val nextOrder = getNextSortOrder(currentOrder, column.sortDirections)
                    onSortChange(columnKey, nextOrder)
                }
            }
        ) {
            Text(
                text = column.title.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = theme.token.colorText,
                maxLines = if (column.ellipsis) 1 else Int.MAX_VALUE,
                overflow = if (column.ellipsis) TextOverflow.Ellipsis else TextOverflow.Clip
            )

            // Sorter icons
            if (column.sorter != null) {
                Spacer(modifier = Modifier.width(4.dp))
                if (column.sortIcon != null) {
                    column.sortIcon.invoke(sortState?.order)
                } else {
                    SorterIcon(
                        sortOrder = sortState?.order,
                        sortDirections = column.sortDirections
                    )
                }
            }

            // Filter icon
            if (column.filters != null) {
                Spacer(modifier = Modifier.width(4.dp))
                FilterDropdownButton(
                    column = column,
                    filterState = filterState,
                    locale = locale,
                    onFilterChange = { values ->
                        onFilterChange(columnKey, values)
                    }
                )
            }

            // Resize handle
            if (column.resizable) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(16.dp)
                        .background(Color(0xFFD9D9D9))
                        .pointerInput(columnKey) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                val currentWidth = resizableColumnWidths[columnKey] ?: column.width ?: 100.dp
                                val newWidth = (currentWidth.value + dragAmount).coerceIn(
                                    column.minWidth?.value ?: 50f,
                                    column.maxWidth?.value ?: 1000f
                                ).dp
                                onColumnResize(columnKey, newWidth)
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun SorterIcon(
    sortOrder: SortOrder?,
    sortDirections: List<SortOrder>,
) {
    Column(
        modifier = Modifier.size(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (sortDirections.contains(SortOrder.Ascend)) {
            Text(
                text = "▲",
                fontSize = 8.sp,
                color = if (sortOrder == SortOrder.Ascend) Color(0xFF1890FF) else Color(0xFFBFBFBF),
                modifier = Modifier.offset(y = 2.dp)
            )
        }
        if (sortDirections.contains(SortOrder.Descend)) {
            Text(
                text = "▼",
                fontSize = 8.sp,
                color = if (sortOrder == SortOrder.Descend) Color(0xFF1890FF) else Color(0xFFBFBFBF),
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
    }
}

@Composable
private fun <T> FilterDropdownButton(
    column: TableColumn<T>,
    filterState: FilterState?,
    locale: LocaleConfig,
    onFilterChange: (List<String>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val hasActiveFilters = filterState?.filteredValues?.isNotEmpty() == true

    Box {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(20.dp)
        ) {
            Text(
                text = "▼",
                fontSize = 10.sp,
                color = if (hasActiveFilters) Color(0xFF1890FF) else Color(0xFF000000).copy(alpha = 0.45f)
            )
        }

        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                FilterDropdown(
                    filters = column.filters ?: emptyList(),
                    selectedValues = filterState?.filteredValues ?: emptyList(),
                    filterMultiple = column.filterMultiple,
                    filterSearch = column.filterSearch,
                    locale = locale,
                    onConfirm = { values ->
                        onFilterChange(values)
                        expanded = false
                    },
                    onReset = {
                        onFilterChange(emptyList())
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterDropdown(
    filters: List<TableFilter>,
    selectedValues: List<String>,
    filterMultiple: Boolean,
    filterSearch: Boolean,
    locale: LocaleConfig,
    onConfirm: (List<String>) -> Unit,
    onReset: () -> Unit,
) {
    var tempSelected by remember { mutableStateOf(selectedValues) }
    var searchText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Search box
            if (filterSearch) {
                AntInput(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = "Search...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            // Filter items
            val filteredFilters = if (searchText.isNotEmpty()) {
                filters.filter { it.text.contains(searchText, ignoreCase = true) }
            } else {
                filters
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                filteredFilters.forEach { filter ->
                    val isSelected = tempSelected.contains(filter.value)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelected = if (filterMultiple) {
                                    if (isSelected) {
                                        tempSelected - filter.value
                                    } else {
                                        tempSelected + filter.value
                                    }
                                } else {
                                    listOf(filter.value)
                                }
                            }
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AntCheckbox(
                            checked = isSelected,
                            onCheckedChange = {
                                tempSelected = if (filterMultiple) {
                                    if (isSelected) {
                                        tempSelected - filter.value
                                    } else {
                                        tempSelected + filter.value
                                    }
                                } else {
                                    listOf(filter.value)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = filter.text,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            AntDivider()

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AntButton(
                    onClick = onReset,
                    type = ButtonType.Link,
                    size = ButtonSize.Small
                ) {
                    Text(locale.filterReset)
                }

                AntButton(
                    onClick = { onConfirm(tempSelected) },
                    type = ButtonType.Primary,
                    size = ButtonSize.Small
                ) {
                    Text(locale.filterConfirm)
                }
            }
        }
    }
}

// ============ Table Body Components ============

@Composable
private fun <T> RegularTableBody(
    data: List<T>,
    columns: List<TableColumn<T>>,
    padding: Dp,
    rowKey: ((T) -> String)?,
    rowSelection: TableRowSelection<T>?,
    expandable: TableExpandable<T>?,
    expandedKeys: List<String>,
    onRow: ((record: T, index: Int) -> RowEventHandlers)?,
    onExpandClick: (String, T, Boolean) -> Unit,
    resizableColumnWidths: Map<String, Dp>,
    rowClassName: ((record: T, index: Int) -> String)? = null,
) {
    Column {
        data.forEachIndexed { index, item ->
            val itemKey = rowKey?.invoke(item) ?: index.toString()
            val isSelected = rowSelection?.selectedRowKeys?.contains(index) == true
            val isExpanded = expandedKeys.contains(itemKey)
            val canExpand = expandable?.rowExpandable?.invoke(item) ?: true
            val rowEventHandlers = onRow?.invoke(item, index)
            val customRowClassName = rowClassName?.invoke(item, index)

            Column {
                TableRow(
                    item = item,
                    index = index,
                    columns = columns,
                    padding = padding,
                    isSelected = isSelected,
                    rowSelection = rowSelection,
                    expandable = expandable,
                    isExpanded = isExpanded,
                    canExpand = canExpand,
                    onRowClick = {},
                    onExpandClick = { onExpandClick(itemKey, item, isExpanded) },
                    resizableColumnWidths = resizableColumnWidths,
                    rowEventHandlers = rowEventHandlers,
                    customRowClassName = customRowClassName,
                    allData = data
                )

                // Expanded content
                if (isExpanded && canExpand && expandable?.expandedRowRender != null) {
                    val theme = useTheme()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(theme.token.colorBgLayout)
                            .padding(
                                start = padding + expandable.indentSize,
                                top = padding,
                                end = padding,
                                bottom = padding
                            )
                    ) {
                        expandable.expandedRowRender.invoke(item, index, 1, isExpanded)
                    }
                }
            }

            if (index < data.lastIndex) {
                AntDivider()
            }
        }
    }
}

@Composable
private fun <T> VirtualTableBody(
    data: List<T>,
    columns: List<TableColumn<T>>,
    padding: Dp,
    rowKey: ((T) -> String)?,
    rowSelection: TableRowSelection<T>?,
    expandable: TableExpandable<T>?,
    expandedKeys: List<String>,
    onRow: ((record: T, index: Int) -> RowEventHandlers)?,
    onExpandClick: (String, T, Boolean) -> Unit,
    listState: LazyListState,
    resizableColumnWidths: Map<String, Dp>,
    rowClassName: ((record: T, index: Int) -> String)? = null,
) {
    LazyColumn(state = listState) {
        itemsIndexed(data, key = { index, item -> rowKey?.invoke(item) ?: index }) { index, item ->
            val itemKey = rowKey?.invoke(item) ?: index.toString()
            val isSelected = rowSelection?.selectedRowKeys?.contains(index) == true
            val isExpanded = expandedKeys.contains(itemKey)
            val canExpand = expandable?.rowExpandable?.invoke(item) ?: true
            val rowEventHandlers = onRow?.invoke(item, index)
            val customRowClassName = rowClassName?.invoke(item, index)

            Column {
                TableRow(
                    item = item,
                    index = index,
                    columns = columns,
                    padding = padding,
                    isSelected = isSelected,
                    rowSelection = rowSelection,
                    expandable = expandable,
                    isExpanded = isExpanded,
                    canExpand = canExpand,
                    onRowClick = {},
                    onExpandClick = { onExpandClick(itemKey, item, isExpanded) },
                    resizableColumnWidths = resizableColumnWidths,
                    rowEventHandlers = rowEventHandlers,
                    customRowClassName = customRowClassName,
                    allData = data
                )

                // Expanded content
                if (isExpanded && canExpand && expandable?.expandedRowRender != null) {
                    val theme = useTheme()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(theme.token.colorBgLayout)
                            .padding(
                                start = padding + expandable.indentSize,
                                top = padding,
                                end = padding,
                                bottom = padding
                            )
                    ) {
                        expandable.expandedRowRender.invoke(item, index, 1, isExpanded)
                    }
                }
            }

            if (index < data.lastIndex) {
                AntDivider()
            }
        }
    }
}

@Composable
private fun <T> TableRow(
    item: T,
    index: Int,
    columns: List<TableColumn<T>>,
    padding: Dp,
    isSelected: Boolean,
    rowSelection: TableRowSelection<T>?,
    expandable: TableExpandable<T>?,
    isExpanded: Boolean,
    canExpand: Boolean,
    onRowClick: () -> Unit,
    onExpandClick: () -> Unit,
    resizableColumnWidths: Map<String, Dp>,
    rowEventHandlers: RowEventHandlers? = null,
    customRowClassName: String? = null,
    allData: List<T>,
) {
    val theme = useTheme()
    val tableToken = theme.components.table

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                when {
                    isSelected -> tableToken.rowHoverBg
                    else -> theme.token.colorBgBase
                }
            )
            .clickable {
                if (expandable?.expandRowByClick == true && canExpand) {
                    onExpandClick()
                } else {
                    onRowClick()
                }
                rowEventHandlers?.onClick?.invoke()
            }
            .then(
                if (rowEventHandlers?.onDoubleClick != null || rowEventHandlers?.onContextMenu != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { rowEventHandlers.onDoubleClick?.invoke() },
                            onLongPress = { rowEventHandlers.onContextMenu?.invoke() }
                        )
                    }
                } else Modifier
            )
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection column
        if (rowSelection != null) {
            Box(
                modifier = Modifier.width(rowSelection.columnWidth),
                contentAlignment = Alignment.Center
            ) {
                val checkboxProps = rowSelection.getCheckboxProps?.invoke(item)
                val isDisabled = checkboxProps?.disabled ?: false
                val isIndeterminate = checkboxProps?.indeterminate ?: false

                if (rowSelection.renderCell != null) {
                    rowSelection.renderCell.invoke(isSelected, item, index) {
                        // Default origin node
                    }
                } else {
                    when (rowSelection.type) {
                        TableRowSelectionType.Checkbox -> {
                            AntCheckbox(
                                checked = isSelected,
                                disabled = isDisabled,
                                indeterminate = isIndeterminate,
                                onCheckedChange = { checked ->
                                    if (!isDisabled) {
                                        val newSelection = if (checked) {
                                            rowSelection.selectedRowKeys + index
                                        } else {
                                            rowSelection.selectedRowKeys - index
                                        }
                                        val selectedRows = allData.filterIndexed { idx, _ -> idx in newSelection }
                                        rowSelection.onChange?.invoke(
                                            newSelection,
                                            selectedRows,
                                            SelectionInfo(SelectionType.Single)
                                        )
                                        rowSelection.onSelect?.invoke(item, checked, selectedRows)
                                    }
                                }
                            )
                        }

                        TableRowSelectionType.Radio -> {
                            AntRadio(
                                value = index,
                                checked = isSelected,
                                disabled = isDisabled,
                                onChange = { event ->
                                    if (!isDisabled) {
                                        rowSelection.onChange?.invoke(
                                            listOf(index),
                                            listOf(item),
                                            SelectionInfo(SelectionType.Single)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Expandable column
        if (expandable != null) {
            Box(
                modifier = Modifier.width(expandable.columnWidth),
                contentAlignment = Alignment.Center
            ) {
                if (canExpand) {
                    if (expandable.expandIcon != null) {
                        expandable.expandIcon.invoke(isExpanded, item) { record ->
                            onExpandClick()
                        }
                    } else {
                        DefaultExpandIcon(
                            expanded = isExpanded,
                            onClick = onExpandClick
                        )
                    }
                }
            }
        }

        // Data columns
        columns.forEach { column ->
            val columnKey = column.key ?: column.dataIndex ?: ""
            val cellProps = column.onCell?.invoke(item, index)
            val columnWidth = resizableColumnWidths[columnKey] ?: column.width

            // Skip cells with colSpan = 0
            if (cellProps?.colSpan == 0 || cellProps?.rowSpan == 0) {
                return@forEach
            }

            Box(
                modifier = if (columnWidth != null) {
                    Modifier.width(columnWidth)
                } else {
                    Modifier.weight(1f)
                }
                    .then(
                        if (cellProps?.onClick != null) {
                            Modifier.clickable { cellProps.onClick.invoke() }
                        } else Modifier
                    ),
                contentAlignment = when (column.align) {
                    TableColumnAlign.Left -> Alignment.CenterStart
                    TableColumnAlign.Center -> Alignment.Center
                    TableColumnAlign.Right -> Alignment.CenterEnd
                }
            ) {
                if (column.render != null) {
                    column.render.invoke(item, index)
                } else {
                    val text = item.toString()

                    if (column.ellipsis && column.ellipsisTooltip) {
                        // Ellipsis with tooltip
                        var showTooltip by remember { mutableStateOf(false) }

                        Box {
                            Text(
                                text = text,
                                fontSize = 14.sp,
                                color = Color(0xFF000000).copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = { showTooltip = true }
                                    )
                                }
                            )

                            if (showTooltip) {
                                Popup(
                                    onDismissRequest = { showTooltip = false }
                                ) {
                                    Card(
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Text(
                                            text = text,
                                            modifier = Modifier.padding(8.dp),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            color = Color(0xFF000000).copy(alpha = 0.85f),
                            maxLines = if (column.ellipsis) 1 else Int.MAX_VALUE,
                            overflow = if (column.ellipsis) TextOverflow.Ellipsis else TextOverflow.Clip
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultExpandIcon(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        label = "expand_icon_rotation"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(24.dp)
    ) {
        Text(
            text = "▶",
            fontSize = 12.sp,
            color = Color(0xFF1890FF),
            modifier = Modifier.rotate(rotation)
        )
    }
}


// ============ Utility Functions ============

/**
 * Helper data class to track fixed column positioning and shadows
 */
private data class FixedColumnInfo(
    val leftFixedColumns: List<String> = emptyList(),
    val rightFixedColumns: List<String> = emptyList(),
    val leftWidths: Map<String, Dp> = emptyMap(),
    val rightWidths: Map<String, Dp> = emptyMap(),
)

/**
 * Calculate fixed column information from columns list
 */
private fun <T> calculateFixedColumns(
    columns: List<TableColumn<T>>,
    resizableColumnWidths: Map<String, Dp>,
): FixedColumnInfo {
    val leftFixed = mutableListOf<String>()
    val rightFixed = mutableListOf<String>()
    val leftWidths = mutableMapOf<String, Dp>()
    val rightWidths = mutableMapOf<String, Dp>()

    var leftOffset = 0.dp
    var rightOffset = 0.dp

    columns.forEachIndexed { index, col ->
        val key = col.key ?: col.dataIndex ?: "column_$index"
        val width = resizableColumnWidths[key] ?: col.width ?: 100.dp

        when (col.fixed) {
            TableColumnFixed.Left -> {
                leftFixed.add(key)
                leftWidths[key] = leftOffset
                leftOffset += width
            }

            TableColumnFixed.Right -> {
                rightFixed.add(0, key) // Prepend for right-to-left ordering
                rightWidths[key] = rightOffset
                rightOffset += width
            }

            null -> {}
        }
    }

    return FixedColumnInfo(leftFixed, rightFixed, leftWidths, rightWidths)
}

/**
 * Get modifier for fixed column with shadow effect
 * Shows shadow on the last left fixed column or first right fixed column when scrolled
 */
private fun getFixedColumnModifier(
    columnKey: String,
    fixedInfo: FixedColumnInfo,
    isScrolled: Boolean,
): Modifier {
    return when {
        columnKey in fixedInfo.leftFixedColumns -> {
            val isLastLeftFixed = columnKey == fixedInfo.leftFixedColumns.lastOrNull()
            Modifier
                .zIndex(2f)
                .then(
                    if (isScrolled && isLastLeftFixed) {
                        Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RectangleShape,
                                clip = false
                            )
                            .background(Color.White.copy(alpha = 0.9f))
                    } else if (isLastLeftFixed) {
                        Modifier.background(Color.White)
                    } else Modifier
                )
        }

        columnKey in fixedInfo.rightFixedColumns -> {
            val isFirstRightFixed = columnKey == fixedInfo.rightFixedColumns.firstOrNull()
            Modifier
                .zIndex(2f)
                .then(
                    if (isScrolled && isFirstRightFixed) {
                        Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RectangleShape,
                                clip = false
                            )
                            .background(Color.White.copy(alpha = 0.9f))
                    } else if (isFirstRightFixed) {
                        Modifier.background(Color.White)
                    } else Modifier
                )
        }

        else -> Modifier
    }
}

private fun getNextSortOrder(current: SortOrder?, directions: List<SortOrder>): SortOrder? {
    return when {
        current == null -> directions.firstOrNull()
        current == SortOrder.Ascend && directions.contains(SortOrder.Descend) -> SortOrder.Descend
        current == SortOrder.Descend && directions.contains(SortOrder.Ascend) -> null
        else -> null
    }
}

private fun <T> updateSortStates(
    currentStates: List<SortState<T>>,
    columnKey: String,
    order: SortOrder?,
    columns: List<TableColumn<T>>,
): List<SortState<T>> {
    val targetColumn = columns.firstOrNull { (it.key ?: it.dataIndex ?: "") == columnKey }
    val targetSorter = targetColumn?.sorter

    // Check if multiple sorting is allowed
    val hasMultiple = targetSorter?.multiple != null

    return if (hasMultiple) {
        // Multiple sort: keep other sorts, update this one
        val newStates = currentStates.filter { it.columnKey != columnKey }.toMutableList()
        if (order != null && targetColumn != null && targetSorter != null) {
            newStates.add(
                SortState(
                    columnKey = columnKey,
                    order = order,
                    sorter = targetSorter,
                    column = targetColumn
                )
            )
        }
        newStates.sortedByDescending { it.sorter?.multiple ?: 0 }
    } else {
        // Single sort: replace all
        if (order != null && targetColumn != null && targetSorter != null) {
            listOf(
                SortState(
                    columnKey = columnKey,
                    order = order,
                    sorter = targetSorter,
                    column = targetColumn
                )
            )
        } else {
            emptyList()
        }
    }
}

private fun <T> applySorting(data: List<T>, sortStates: List<SortState<T>>): List<T> {
    if (sortStates.isEmpty()) return data

    val activeSorters = sortStates.filter { it.order != null }
    if (activeSorters.isEmpty()) return data

    return data.sortedWith { a, b ->
        var result = 0
        for (sorter in activeSorters) {
            val compare = sorter.sorter?.compare?.invoke(a, b) ?: 0
            result = when (sorter.order) {
                SortOrder.Ascend -> compare
                SortOrder.Descend -> -compare
                null -> 0
            }
            if (result != 0) break
        }
        result
    }
}

private fun <T> applyFiltering(
    data: List<T>,
    filterStates: List<FilterState>,
    columns: List<TableColumn<T>>,
): List<T> {
    if (filterStates.isEmpty()) return data

    var filtered = data

    filterStates.forEach { filterState ->
        if (filterState.filteredValues.isNotEmpty()) {
            val column = columns.firstOrNull { (it.key ?: it.dataIndex ?: "") == filterState.columnKey }
            val onFilter = column?.onFilter

            if (onFilter != null) {
                filtered = filtered.filter { record ->
                    filterState.filteredValues.any { value ->
                        onFilter(value, record)
                    }
                }
            }
        }
    }

    return filtered
}
