package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

/**
 * Transfer Item data class
 * Represents a single item in the Transfer component
 *
 * @property key Unique identifier for the item (required)
 * @property title Display title for the item
 * @property description Optional description text shown below the title
 * @property disabled Whether this item is disabled and cannot be selected
 *
 * @since 1.0.0
 * @see AntTransfer
 */
data class TransferItem(
    val key: String,
    val title: String,
    val description: String? = null,
    val disabled: Boolean = false
)

/**
 * Transfer Direction enum
 * Indicates the direction of item transfer between lists
 *
 * @since 1.0.0
 */
enum class TransferDirection {
    /** Transfer from left (source) to right (target) */
    Left,
    /** Transfer from right (target) to left (source) */
    Right
}

/**
 * Transfer Status enum
 * Validation status for the Transfer component
 *
 * @since 4.19.0
 */
enum class TransferStatus {
    /** Default state, no validation styling */
    Default,
    /** Error state, shows red borders and accents */
    Error,
    /** Warning state, shows yellow/orange borders and accents */
    Warning
}

/**
 * Transfer Select All Info
 * Information passed to selectAllLabels render function
 *
 * @property selectedCount Number of items currently selected
 * @property totalCount Total number of items in the list
 *
 * @since 1.0.0
 */
data class TransferSelectAllInfo(
    val selectedCount: Int,
    val totalCount: Int
)

/**
 * Pagination Configuration for Transfer lists
 * Controls how pagination is displayed in Transfer lists
 *
 * @property pageSize Number of items per page (default: 10)
 * @property simple Use simple pagination controls
 * @property showSizeChanger Show page size selector
 * @property showLessItems Show fewer page numbers
 *
 * @since 4.3.0
 */
data class PaginationConfig(
    val pageSize: Int = 10,
    val simple: Boolean = false,
    val showSizeChanger: Boolean = false,
    val showLessItems: Boolean = false
)

/**
 * Semantic class names for Transfer sub-elements
 * Ant Design v5.5.0+ feature for fine-grained styling control
 *
 * Note: In Compose/KMP, class names are informational only and don't apply CSS.
 * Use [TransferStyles] for actual styling in Compose.
 *
 * @property list Custom class name for list container (informational)
 * @property listHeader Custom class name for list header (informational)
 * @property listBody Custom class name for list body/content area (informational)
 * @property listItem Custom class name for individual list items (informational)
 *
 * @since 5.5.0
 * @see TransferStyles for functional styling with Compose modifiers
 */
data class TransferClassNames(
    val list: String = "",
    val listHeader: String = "",
    val listBody: String = "",
    val listItem: String = ""
)

/**
 * Semantic modifiers for Transfer sub-elements
 * Allows targeted styling of Transfer components using Compose modifiers
 *
 * This is the Compose equivalent of React's semantic styles feature in Ant Design v5.
 * Apply custom modifiers to specific parts of the Transfer for fine-grained control.
 *
 * @property list Custom modifier for list container
 * @property listHeader Custom modifier for list header
 * @property listBody Custom modifier for list body/content area
 * @property listItem Custom modifier for individual list items
 *
 * @since 5.5.0
 * @see TransferClassNames for informational class names
 */
data class TransferStyles(
    val list: Modifier = Modifier,
    val listHeader: Modifier = Modifier,
    val listBody: Modifier = Modifier,
    val listItem: Modifier = Modifier
)

/**
 * Transfer locale configuration
 * Internationalization strings for the Transfer component
 *
 * @property itemUnit Singular unit text (e.g., "item")
 * @property itemsUnit Plural unit text (e.g., "items")
 * @property searchPlaceholder Placeholder text for search input
 * @property notFoundContent Content to show when no items found
 */
data class TransferLocale(
    val itemUnit: String = "item",
    val itemsUnit: String = "items",
    val searchPlaceholder: String = "Search here",
    val notFoundContent: String = "No data"
)

/**
 * Ant Design Transfer Component for Compose Multiplatform
 * Full feature parity with React Ant Design v5.x
 *
 * A transfer component allows users to move items between two lists.
 * It provides a clear interface for selecting and transferring items with support
 * for search, pagination, custom rendering, and validation states.
 *
 * ## Basic Usage
 * ```kotlin
 * val items = listOf(
 *     TransferItem(key = "1", title = "Item 1"),
 *     TransferItem(key = "2", title = "Item 2"),
 *     TransferItem(key = "3", title = "Item 3")
 * )
 * var targetKeys by remember { mutableStateOf(listOf<String>()) }
 *
 * AntTransfer(
 *     dataSource = items,
 *     targetKeys = targetKeys,
 *     onChange = { newTargetKeys, direction, moveKeys ->
 *         targetKeys = newTargetKeys
 *         println("Moved $moveKeys to $direction")
 *     }
 * )
 * ```
 *
 * ## With Search
 * ```kotlin
 * AntTransfer(
 *     dataSource = items,
 *     targetKeys = targetKeys,
 *     onChange = { newTargetKeys, _, _ -> targetKeys = newTargetKeys },
 *     showSearch = true,
 *     filterOption = { input, item ->
 *         item.title.contains(input, ignoreCase = true)
 *     }
 * )
 * ```
 *
 * ## One-Way Transfer
 * ```kotlin
 * AntTransfer(
 *     dataSource = items,
 *     targetKeys = targetKeys,
 *     onChange = { newTargetKeys, _, _ -> targetKeys = newTargetKeys },
 *     oneWay = true
 * )
 * ```
 *
 * ## With Pagination
 * ```kotlin
 * AntTransfer(
 *     dataSource = largeItemList,
 *     targetKeys = targetKeys,
 *     onChange = { newTargetKeys, _, _ -> targetKeys = newTargetKeys },
 *     pagination = PaginationConfig(pageSize = 10)
 * )
 * ```
 *
 * @param dataSource The data source for transfer items (required)
 * @param targetKeys Keys of items in the target (right) list - can be controlled or uncontrolled
 * @param onChange Callback when transfer occurs (newTargetKeys, direction, movedKeys)
 * @param modifier Modifier to be applied to the component
 * @param selectedKeys Controlled selected keys for both lists [sourceKeys, targetKeys]
 * @param onSelectChange Callback when selection changes (sourceSelectedKeys, targetSelectedKeys)
 * @param disabled Disable all transfer operations
 * @param titles Custom titles for left and right lists [leftTitle, rightTitle]
 * @param operations Custom operation button labels [toRightLabel, toLeftLabel]
 * @param showSearch Show search input box in lists
 * @param filterOption Custom filter function for search (inputValue, item) -> Boolean
 * @param onSearch Callback when search value changes (direction, searchValue)
 * @param searchPlaceholder Placeholder text for search input (deprecated, use locale)
 * @param notFoundContent Custom content when list is empty
 * @param render Custom item title renderer, returns String for display
 * @param oneWay One-way transfer mode (only left to right)
 * @param showSelectAll Show select all checkbox in header
 * @param selectAllLabels Custom labels for select all checkboxes with count info
 * @param pagination Enable pagination (Boolean for default, PaginationConfig for custom)
 * @param status Validation status (Default, Error, Warning)
 * @param classNames Semantic class names for sub-elements (v5.5.0+, informational)
 * @param styles Semantic styles for sub-elements (v5.5.0+)
 * @param listStyle Custom list styling - can be single Modifier or Pair<Modifier, Modifier> for left/right
 * @param locale Internationalization configuration
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/transfer">Ant Design Transfer</a>
 */
@Composable
fun AntTransfer(
    dataSource: List<TransferItem>,
    modifier: Modifier = Modifier,
    targetKeys: List<String>? = null,
    onChange: ((targetKeys: List<String>, direction: TransferDirection, moveKeys: List<String>) -> Unit)? = null,
    selectedKeys: List<String>? = null,
    onSelectChange: ((sourceSelectedKeys: List<String>, targetSelectedKeys: List<String>) -> Unit)? = null,
    disabled: Boolean = false,
    titles: List<String> = listOf("Source", "Target"),
    operations: List<String> = listOf("→", "←"),
    showSearch: Boolean = false,
    filterOption: ((inputValue: String, item: TransferItem) -> Boolean)? = null,
    onSearch: ((direction: TransferDirection, value: String) -> Unit)? = null,
    searchPlaceholder: String = "Search here",
    notFoundContent: (@Composable () -> Unit)? = null,
    render: ((item: TransferItem) -> String)? = null,
    oneWay: Boolean = false,
    showSelectAll: Boolean = true,
    selectAllLabels: List<(@Composable (info: TransferSelectAllInfo) -> Unit)>? = null,
    pagination: Any = false, // Boolean or PaginationConfig
    status: TransferStatus = TransferStatus.Default,
    classNames: TransferClassNames? = null,
    styles: TransferStyles? = null,
    listStyle: Any? = null, // Modifier or Pair<Modifier, Modifier>
    locale: TransferLocale = TransferLocale()
) {
    // Get theme from ConfigProvider
    val theme = useTheme()
    val config = useConfig()

    // Internal state for uncontrolled mode
    var internalTargetKeys by remember { mutableStateOf(emptyList<String>()) }
    var internalLeftSelectedKeys by remember { mutableStateOf(emptyList<String>()) }
    var internalRightSelectedKeys by remember { mutableStateOf(emptyList<String>()) }

    // Use controlled values if provided, otherwise use internal state
    val actualTargetKeys = targetKeys ?: internalTargetKeys
    val actualSelectedKeys = selectedKeys
    val actualLeftSelectedKeys = if (actualSelectedKeys != null) {
        actualSelectedKeys.filter { !actualTargetKeys.contains(it) }
    } else {
        internalLeftSelectedKeys
    }
    val actualRightSelectedKeys = if (actualSelectedKeys != null) {
        actualSelectedKeys.filter { actualTargetKeys.contains(it) }
    } else {
        internalRightSelectedKeys
    }

    // Search state
    var leftSearchText by remember { mutableStateOf("") }
    var rightSearchText by remember { mutableStateOf("") }

    // Pagination state
    var leftCurrentPage by remember { mutableStateOf(1) }
    var rightCurrentPage by remember { mutableStateOf(1) }

    // Determine pagination config
    val paginationConfig = when (pagination) {
        is PaginationConfig -> pagination
        true -> PaginationConfig()
        else -> null
    }

    // Split items into source and target
    val sourceItems = remember(dataSource, actualTargetKeys) {
        dataSource.filter { !actualTargetKeys.contains(it.key) }
    }

    val targetItems = remember(dataSource, actualTargetKeys) {
        dataSource.filter { actualTargetKeys.contains(it.key) }
    }

    // Apply filtering
    val filteredSourceItems = remember(sourceItems, leftSearchText, filterOption) {
        if (leftSearchText.isEmpty()) {
            sourceItems
        } else {
            sourceItems.filter { item ->
                filterOption?.invoke(leftSearchText, item)
                    ?: item.title.contains(leftSearchText, ignoreCase = true)
            }
        }
    }

    val filteredTargetItems = remember(targetItems, rightSearchText, filterOption) {
        if (rightSearchText.isEmpty()) {
            targetItems
        } else {
            targetItems.filter { item ->
                filterOption?.invoke(rightSearchText, item)
                    ?: item.title.contains(rightSearchText, ignoreCase = true)
            }
        }
    }

    // Apply pagination
    val paginatedSourceItems = remember(filteredSourceItems, leftCurrentPage, paginationConfig) {
        if (paginationConfig != null) {
            val start = (leftCurrentPage - 1) * paginationConfig.pageSize
            val end = (start + paginationConfig.pageSize).coerceAtMost(filteredSourceItems.size)
            filteredSourceItems.subList(start.coerceIn(0, filteredSourceItems.size), end)
        } else {
            filteredSourceItems
        }
    }

    val paginatedTargetItems = remember(filteredTargetItems, rightCurrentPage, paginationConfig) {
        if (paginationConfig != null) {
            val start = (rightCurrentPage - 1) * paginationConfig.pageSize
            val end = (start + paginationConfig.pageSize).coerceAtMost(filteredTargetItems.size)
            filteredTargetItems.subList(start.coerceIn(0, filteredTargetItems.size), end)
        } else {
            filteredTargetItems
        }
    }

    // Calculate list style modifiers
    val (leftListStyle, rightListStyle) = when (listStyle) {
        is Pair<*, *> -> (listStyle.first as? Modifier ?: Modifier) to (listStyle.second as? Modifier ?: Modifier)
        is Modifier -> listStyle to listStyle
        else -> Modifier to Modifier
    }

    // Determine border color based on status
    val borderColor = when (status) {
        TransferStatus.Error -> theme.token.colorError
        TransferStatus.Warning -> theme.token.colorWarning
        TransferStatus.Default -> Color(0xFFD9D9D9)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Source list (left)
        TransferList(
            title = titles.getOrElse(0) { "Source" },
            items = paginatedSourceItems,
            allItems = filteredSourceItems,
            selectedKeys = actualLeftSelectedKeys,
            onSelectedKeysChange = { newKeys ->
                if (selectedKeys == null) {
                    internalLeftSelectedKeys = newKeys
                }
                onSelectChange?.invoke(newKeys, actualRightSelectedKeys)
            },
            searchText = leftSearchText,
            onSearchTextChange = { newText ->
                leftSearchText = newText
                onSearch?.invoke(TransferDirection.Left, newText)
            },
            showSearch = showSearch,
            searchPlaceholder = searchPlaceholder,
            showSelectAll = showSelectAll,
            disabled = disabled,
            render = render,
            notFoundContent = notFoundContent,
            selectAllLabel = selectAllLabels?.getOrNull(0),
            borderColor = borderColor,
            classNames = classNames,
            styles = styles,
            listStyleModifier = leftListStyle,
            paginationConfig = paginationConfig,
            currentPage = leftCurrentPage,
            onPageChange = { leftCurrentPage = it },
            locale = locale,
            theme = theme,
            modifier = Modifier.weight(1f)
        )

        // Operation buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // To right button
            AntButton(
                onClick = {
                    val movedKeys = actualLeftSelectedKeys
                    val newTargetKeys = actualTargetKeys + movedKeys
                    if (targetKeys == null) {
                        internalTargetKeys = newTargetKeys
                        internalLeftSelectedKeys = emptyList()
                    }
                    onChange?.invoke(newTargetKeys, TransferDirection.Right, movedKeys)

                    // Clear selections after transfer
                    if (selectedKeys == null) {
                        internalLeftSelectedKeys = emptyList()
                    }
                    onSelectChange?.invoke(emptyList(), actualRightSelectedKeys)
                },
                disabled = disabled || actualLeftSelectedKeys.isEmpty(),
                size = ButtonSize.Small
            ) {
                Text(operations.getOrElse(0) { "→" })
            }

            // To left button (only if not one-way)
            if (!oneWay) {
                AntButton(
                    onClick = {
                        val movedKeys = actualRightSelectedKeys
                        val newTargetKeys = actualTargetKeys.filter { !movedKeys.contains(it) }
                        if (targetKeys == null) {
                            internalTargetKeys = newTargetKeys
                            internalRightSelectedKeys = emptyList()
                        }
                        onChange?.invoke(newTargetKeys, TransferDirection.Left, movedKeys)

                        // Clear selections after transfer
                        if (selectedKeys == null) {
                            internalRightSelectedKeys = emptyList()
                        }
                        onSelectChange?.invoke(actualLeftSelectedKeys, emptyList())
                    },
                    disabled = disabled || actualRightSelectedKeys.isEmpty(),
                    size = ButtonSize.Small
                ) {
                    Text(operations.getOrElse(1) { "←" })
                }
            }
        }

        // Target list (right)
        TransferList(
            title = titles.getOrElse(1) { "Target" },
            items = paginatedTargetItems,
            allItems = filteredTargetItems,
            selectedKeys = actualRightSelectedKeys,
            onSelectedKeysChange = { newKeys ->
                if (selectedKeys == null) {
                    internalRightSelectedKeys = newKeys
                }
                onSelectChange?.invoke(actualLeftSelectedKeys, newKeys)
            },
            searchText = rightSearchText,
            onSearchTextChange = { newText ->
                rightSearchText = newText
                onSearch?.invoke(TransferDirection.Right, newText)
            },
            showSearch = showSearch,
            searchPlaceholder = searchPlaceholder,
            showSelectAll = showSelectAll,
            disabled = disabled,
            render = render,
            notFoundContent = notFoundContent,
            selectAllLabel = selectAllLabels?.getOrNull(1),
            borderColor = borderColor,
            classNames = classNames,
            styles = styles,
            listStyleModifier = rightListStyle,
            paginationConfig = paginationConfig,
            currentPage = rightCurrentPage,
            onPageChange = { rightCurrentPage = it },
            locale = locale,
            theme = theme,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Transfer List Component
 * Internal component that renders a single list panel (source or target)
 *
 * @param title List title displayed in header
 * @param items Items to display (after pagination)
 * @param allItems All items (before pagination, for select all)
 * @param selectedKeys Currently selected item keys
 * @param onSelectedKeysChange Callback when selection changes
 * @param searchText Current search text
 * @param onSearchTextChange Callback when search text changes
 * @param showSearch Whether to show search input
 * @param searchPlaceholder Search input placeholder
 * @param showSelectAll Whether to show select all checkbox
 * @param disabled Whether list is disabled
 * @param render Custom item renderer
 * @param notFoundContent Custom empty state content
 * @param selectAllLabel Custom select all label with count info
 * @param borderColor Border color for the list
 * @param classNames Semantic class names
 * @param styles Semantic styles
 * @param listStyleModifier Custom list style modifier
 * @param paginationConfig Pagination configuration
 * @param currentPage Current page number
 * @param onPageChange Callback when page changes
 * @param locale Locale configuration
 * @param theme Theme configuration
 * @param modifier Modifier for the component
 */
@Composable
private fun TransferList(
    title: String,
    items: List<TransferItem>,
    allItems: List<TransferItem>,
    selectedKeys: List<String>,
    onSelectedKeysChange: (List<String>) -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    showSearch: Boolean,
    searchPlaceholder: String,
    showSelectAll: Boolean,
    disabled: Boolean,
    render: ((TransferItem) -> String)?,
    notFoundContent: (@Composable () -> Unit)?,
    selectAllLabel: (@Composable (info: TransferSelectAllInfo) -> Unit)?,
    borderColor: Color,
    classNames: TransferClassNames?,
    styles: TransferStyles?,
    listStyleModifier: Modifier,
    paginationConfig: PaginationConfig?,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    locale: TransferLocale,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .then(listStyleModifier)
            .then(styles?.list ?: Modifier)
            .clip(RoundedCornerShape(theme.token.borderRadiusSM))
            .border(1.dp, borderColor, RoundedCornerShape(theme.token.borderRadiusSM))
            .background(theme.token.colorBgBase)
    ) {
        // Header
        TransferListHeader(
            title = title,
            items = items,
            allItems = allItems,
            selectedKeys = selectedKeys,
            onSelectedKeysChange = onSelectedKeysChange,
            showSelectAll = showSelectAll,
            disabled = disabled,
            selectAllLabel = selectAllLabel,
            styles = styles,
            locale = locale,
            theme = theme
        )

        // Search
        if (showSearch) {
            AntInput(
                value = searchText,
                onValueChange = onSearchTextChange,
                placeholder = searchPlaceholder,
                disabled = disabled,
                size = InputSize.Small,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        // Items list
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .then(styles?.listBody ?: Modifier)
        ) {
            if (items.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (notFoundContent != null) {
                        notFoundContent()
                    } else {
                        AntEmpty(description = locale.notFoundContent)
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items, key = { it.key }) { item ->
                        TransferListItem(
                            item = item,
                            selected = selectedKeys.contains(item.key),
                            onSelectedChange = { selected ->
                                val newKeys = if (selected) {
                                    selectedKeys + item.key
                                } else {
                                    selectedKeys.filter { it != item.key }
                                }
                                onSelectedKeysChange(newKeys)
                            },
                            disabled = disabled,
                            render = render,
                            styles = styles,
                            theme = theme
                        )
                    }
                }
            }
        }

        // Pagination
        if (paginationConfig != null && allItems.isNotEmpty()) {
            TransferPagination(
                totalItems = allItems.size,
                pageSize = paginationConfig.pageSize,
                currentPage = currentPage,
                onPageChange = onPageChange,
                simple = paginationConfig.simple,
                theme = theme
            )
        }
    }
}

/**
 * Transfer List Header Component
 * Displays the list title, select all checkbox, and item counts
 */
@Composable
private fun TransferListHeader(
    title: String,
    items: List<TransferItem>,
    allItems: List<TransferItem>,
    selectedKeys: List<String>,
    onSelectedKeysChange: (List<String>) -> Unit,
    showSelectAll: Boolean,
    disabled: Boolean,
    selectAllLabel: (@Composable (info: TransferSelectAllInfo) -> Unit)?,
    styles: TransferStyles?,
    locale: TransferLocale,
    theme: AntThemeConfig
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(styles?.listHeader ?: Modifier)
            .background(ColorPalette.gray2)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSelectAll) {
            // Get all selectable keys (non-disabled items from current page)
            val selectableKeys = items.filter { !it.disabled }.map { it.key }
            val allSelected = selectableKeys.isNotEmpty() && selectableKeys.all { selectedKeys.contains(it) }
            val indeterminate = !allSelected && selectableKeys.any { selectedKeys.contains(it) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = allSelected,
                    onCheckedChange = { checked ->
                        val newKeys = if (checked) {
                            (selectedKeys + selectableKeys).distinct()
                        } else {
                            selectedKeys.filter { !selectableKeys.contains(it) }
                        }
                        onSelectedKeysChange(newKeys)
                    },
                    enabled = !disabled && items.isNotEmpty()
                )

                if (selectAllLabel != null) {
                    // Use custom select all label with count info
                    val selectedCount = selectedKeys.filter { key ->
                        allItems.any { it.key == key }
                    }.size
                    val info = TransferSelectAllInfo(
                        selectedCount = selectedCount,
                        totalCount = allItems.size
                    )
                    selectAllLabel(info)
                } else {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.token.colorTextBase
                    )
                }
            }
        } else {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = theme.token.colorTextBase
            )
        }

        // Count display
        val selectedCount = selectedKeys.filter { key ->
            allItems.any { it.key == key }
        }.size
        val unit = if (allItems.size == 1) locale.itemUnit else locale.itemsUnit
        Text(
            text = "$selectedCount/${allItems.size} $unit",
            fontSize = 12.sp,
            color = ColorPalette.gray7
        )
    }
}

/**
 * Transfer List Item Component
 * Renders a single item in the transfer list
 */
@Composable
private fun TransferListItem(
    item: TransferItem,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    disabled: Boolean,
    render: ((TransferItem) -> String)?,
    styles: TransferStyles?,
    theme: AntThemeConfig
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(styles?.listItem ?: Modifier)
            .clickable(
                enabled = !disabled && !item.disabled,
                onClick = { onSelectedChange(!selected) }
            )
            .background(if (selected) ColorPalette.blue1 else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = onSelectedChange,
            enabled = !disabled && !item.disabled
        )

        Column {
            val displayTitle = render?.invoke(item) ?: item.title
            Text(
                text = displayTitle,
                fontSize = 14.sp,
                color = when {
                    item.disabled -> ColorPalette.gray7
                    else -> theme.token.colorTextBase
                }
            )

            if (item.description != null) {
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = ColorPalette.gray7
                )
            }
        }
    }
}

/**
 * Transfer Pagination Component
 * Displays pagination controls for the transfer list
 */
@Composable
private fun TransferPagination(
    totalItems: Int,
    pageSize: Int,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    simple: Boolean,
    theme: AntThemeConfig
) {
    val totalPages = ceil(totalItems.toDouble() / pageSize).toInt()

    if (totalPages <= 1) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorPalette.gray2)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (simple) {
            // Simple pagination: Previous / Next
            AntButton(
                onClick = { onPageChange(currentPage - 1) },
                disabled = currentPage <= 1,
                size = ButtonSize.Small,
                variant = ButtonVariant.Text
            ) {
                Text("<")
            }

            Text(
                text = "$currentPage / $totalPages",
                fontSize = 12.sp,
                color = theme.token.colorTextBase
            )

            AntButton(
                onClick = { onPageChange(currentPage + 1) },
                disabled = currentPage >= totalPages,
                size = ButtonSize.Small,
                variant = ButtonVariant.Text
            ) {
                Text(">")
            }
        } else {
            // Full pagination with page numbers
            AntButton(
                onClick = { onPageChange(currentPage - 1) },
                disabled = currentPage <= 1,
                size = ButtonSize.Small,
                variant = ButtonVariant.Text
            ) {
                Text("<")
            }

            // Show page numbers (simplified - show first, last, and current +/- 1)
            val pagesToShow = mutableListOf<Int>()

            // Always show first page
            pagesToShow.add(1)

            // Show current page and neighbors
            if (currentPage > 2) {
                if (currentPage > 3) {
                    pagesToShow.add(-1) // Ellipsis
                }
                pagesToShow.add(currentPage - 1)
            }

            if (currentPage != 1 && currentPage != totalPages) {
                pagesToShow.add(currentPage)
            }

            if (currentPage < totalPages - 1) {
                pagesToShow.add(currentPage + 1)
                if (currentPage < totalPages - 2) {
                    pagesToShow.add(-1) // Ellipsis
                }
            }

            // Always show last page
            if (totalPages > 1) {
                pagesToShow.add(totalPages)
            }

            pagesToShow.distinct().forEach { page ->
                if (page == -1) {
                    Text(
                        text = "...",
                        fontSize = 12.sp,
                        color = ColorPalette.gray7
                    )
                } else {
                    AntButton(
                        onClick = { onPageChange(page) },
                        size = ButtonSize.Small,
                        variant = if (page == currentPage) ButtonVariant.Solid else ButtonVariant.Text,
                        type = if (page == currentPage) ButtonType.Primary else ButtonType.Default
                    ) {
                        Text(page.toString())
                    }
                }
            }

            AntButton(
                onClick = { onPageChange(currentPage + 1) },
                disabled = currentPage >= totalPages,
                size = ButtonSize.Small,
                variant = ButtonVariant.Text
            ) {
                Text(">")
            }
        }
    }
}
