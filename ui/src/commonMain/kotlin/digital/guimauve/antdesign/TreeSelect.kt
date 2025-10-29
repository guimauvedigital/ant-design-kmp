package digital.guimauve.antdesign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

// ============================================================================
// DATA CLASSES & ENUMS
// ============================================================================

/**
 * TreeNode data structure for TreeSelect
 *
 * @param value The unique value identifier for the node
 * @param title The display text for the node
 * @param children Child nodes (null if leaf)
 * @param disabled Whether the node is disabled
 * @param disableCheckbox Whether the checkbox is disabled (for checkable mode)
 * @param selectable Whether the node can be selected
 * @param checkable Whether the node shows a checkbox
 * @param isLeaf Whether this is a leaf node (for lazy loading)
 * @param icon Custom icon for the node
 */
data class TreeSelectNode(
    val value: String,
    val title: String,
    val children: List<TreeSelectNode>? = null,
    val disabled: Boolean = false,
    val disableCheckbox: Boolean = false,
    val selectable: Boolean = true,
    val checkable: Boolean = true,
    val isLeaf: Boolean = false,
    val icon: (@Composable () -> Unit)? = null,
)

/**
 * Placement options for the dropdown
 */
enum class TreeSelectPlacement {
    BottomLeft,
    BottomRight,
    TopLeft,
    TopRight
}

/**
 * Strategy for displaying checked nodes in the selector
 */
enum class TreeSelectCheckedStrategy {
    /**
     * Show all checked nodes (A, B, C)
     */
    ShowAll,

    /**
     * Show only parent when all children are checked (A instead of A1, A2)
     */
    ShowParent,

    /**
     * Show only child nodes (A1, A2, not A) - Default
     */
    ShowChild
}

/**
 * Tree expand behavior
 */
enum class TreeSelectExpandAction {
    /**
     * Expand on click anywhere on the node
     */
    Click,

    /**
     * Expand on double click
     */
    DoubleClick,

    /**
     * Only expand via icon click
     */
    False
}

/**
 * Extra information passed to onChange callback
 *
 * @param triggerValue The value that triggered the change
 * @param triggerNode The node that triggered the change
 * @param allCheckedNodes All currently checked nodes
 */
data class TreeSelectExtra(
    val triggerValue: Any?,
    val triggerNode: TreeSelectNode?,
    val allCheckedNodes: List<TreeSelectNode>,
)

/**
 * Label-value pair for labelInValue mode
 *
 * @param label The display label
 * @param value The underlying value
 */
data class TreeSelectLabelValue(
    val label: String,
    val value: Any,
)

/**
 * Semantic class names for TreeSelect parts
 *
 * @param root Class name for the root container
 * @param selector Class name for the selector
 * @param dropdown Class name for the dropdown
 * @param tree Class name for the tree
 */
data class TreeSelectClassNames(
    val root: String = "",
    val selector: String = "",
    val dropdown: String = "",
    val tree: String = "",
)

/**
 * Semantic styles for TreeSelect parts
 *
 * @param root Modifier for the root container
 * @param selector Modifier for the selector
 * @param dropdown Modifier for the dropdown
 * @param tree Modifier for the tree
 */
data class TreeSelectStyles(
    val root: Modifier = Modifier,
    val selector: Modifier = Modifier,
    val dropdown: Modifier = Modifier,
    val tree: Modifier = Modifier,
)

// ============================================================================
// MAIN TREESELECT COMPONENT
// ============================================================================

/**
 * TreeSelect - Combines Tree and Select for hierarchical data selection
 *
 * A TreeSelect displays hierarchical data in a dropdown selector, allowing users
 * to select items from a tree structure. Supports single/multiple selection,
 * checkboxes, search, lazy loading, and virtual scrolling.
 *
 * ## Examples
 *
 * ### Basic Usage
 * ```kotlin
 * var value by remember { mutableStateOf<String?>(null) }
 * AntTreeSelect(
 *     value = value,
 *     onValueChange = { value = it },
 *     treeData = listOf(
 *         TreeSelectNode(
 *             value = "parent1",
 *             title = "Parent 1",
 *             children = listOf(
 *                 TreeSelectNode(value = "child1", title = "Child 1"),
 *                 TreeSelectNode(value = "child2", title = "Child 2")
 *             )
 *         )
 *     )
 * )
 * ```
 *
 * ### Multiple Selection with Checkboxes
 * ```kotlin
 * var values by remember { mutableStateOf<List<String>>(emptyList()) }
 * AntTreeSelect(
 *     value = values,
 *     onValueChange = { values = it as List<String> },
 *     treeData = treeData,
 *     multiple = true,
 *     treeCheckable = true,
 *     showCheckedStrategy = TreeSelectCheckedStrategy.ShowChild
 * )
 * ```
 *
 * ### With Search
 * ```kotlin
 * AntTreeSelect(
 *     value = value,
 *     onValueChange = { value = it },
 *     treeData = treeData,
 *     showSearch = true,
 *     filterTreeNode = { inputValue, node ->
 *         node.title.contains(inputValue, ignoreCase = true)
 *     }
 * )
 * ```
 *
 * ### Lazy Loading
 * ```kotlin
 * AntTreeSelect(
 *     value = value,
 *     onValueChange = { value = it },
 *     treeData = treeData,
 *     loadData = { node ->
 *         // Load children asynchronously
 *         scope.launch {
 *             val children = fetchChildren(node.value)
 *             // Update treeData with children
 *         }
 *     }
 * )
 * ```
 *
 * @param value Current selected value (String for single, List<String> for multiple)
 * @param onValueChange Callback when value changes
 * @param treeData Tree data source
 * @param modifier Modifier for the component
 * @param defaultValue Default uncontrolled value
 * @param onChange Advanced callback with extra information
 * @param allowClear Show clear button
 * @param autoFocus Auto focus on mount
 * @param bordered Show border (deprecated, use variant)
 * @param disabled Disable the selector
 * @param dropdownStyle Custom dropdown container style
 * @param filterTreeNode Custom filter function (inputValue, treeNode) -> Boolean
 * @param labelInValue Return {value, label} object instead of value
 * @param loadData Async data loading callback
 * @param maxTagCount Maximum number of tags to show in multiple mode
 * @param maxTagPlaceholder Custom placeholder when maxTagCount exceeded
 * @param multiple Enable multiple selection
 * @param notFoundContent Custom content when no data
 * @param placeholder Placeholder text
 * @param placement Dropdown placement position
 * @param showCheckedStrategy How to display checked nodes (ShowAll/ShowParent/ShowChild)
 * @param showSearch Enable search functionality
 * @param size Component size variant
 * @param status Validation status (error/warning)
 * @param suffixIcon Custom suffix icon
 * @param switcherIcon Custom tree expand/collapse icon
 * @param treeCheckable Show checkboxes on tree nodes
 * @param treeCheckStrictly Checkbox selection without parent-child cascade
 * @param treeDefaultExpandAll Expand all tree nodes by default
 * @param treeDefaultExpandedKeys Default expanded node keys
 * @param treeExpandAction Expand behavior (Click/DoubleClick/False)
 * @param treeExpandedKeys Controlled expanded keys
 * @param treeIcon Show tree node icons
 * @param treeLine Show connecting lines between nodes
 * @param treeNodeFilterProp Property to filter by
 * @param treeNodeLabelProp Property to use as display label
 * @param virtual Enable virtual scrolling for large trees
 * @param listHeight Height for virtual scrolling
 * @param variant Component variant (Outlined/Filled/Borderless)
 * @param classNames Semantic class names for component parts
 * @param styles Semantic styles for component parts
 * @param popupMatchSelectWidth Match dropdown width to selector
 * @param onDropdownVisibleChange Callback when dropdown visibility changes
 * @param onTreeExpand Callback when tree node expands
 * @param onSearch Callback when search input changes
 * @param open Controlled dropdown visibility
 * @param defaultOpen Default dropdown visibility
 */
@Composable
fun AntTreeSelect(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    treeData: List<TreeSelectNode>,
    modifier: Modifier = Modifier,
    defaultValue: Any? = null,
    onChange: ((value: Any, label: Any, extra: TreeSelectExtra) -> Unit)? = null,
    allowClear: Boolean = true,
    autoFocus: Boolean = false,
    bordered: Boolean = true,
    disabled: Boolean = false,
    dropdownStyle: Modifier = Modifier,
    filterTreeNode: ((inputValue: String, treeNode: TreeSelectNode) -> Boolean)? = null,
    labelInValue: Boolean = false,
    loadData: ((treeNode: TreeSelectNode) -> Unit)? = null,
    maxTagCount: Int = Int.MAX_VALUE,
    maxTagPlaceholder: (@Composable (omittedCount: Int) -> Unit)? = null,
    multiple: Boolean = false,
    notFoundContent: (@Composable () -> Unit)? = null,
    placeholder: String = "Please select",
    placement: TreeSelectPlacement = TreeSelectPlacement.BottomLeft,
    showCheckedStrategy: TreeSelectCheckedStrategy = TreeSelectCheckedStrategy.ShowChild,
    showSearch: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    suffixIcon: (@Composable () -> Unit)? = null,
    switcherIcon: (@Composable (expanded: Boolean) -> Unit)? = null,
    treeCheckable: Boolean = false,
    treeCheckStrictly: Boolean = false,
    treeDefaultExpandAll: Boolean = false,
    treeDefaultExpandedKeys: List<String>? = null,
    treeExpandAction: TreeSelectExpandAction = TreeSelectExpandAction.Click,
    treeExpandedKeys: List<String>? = null,
    treeIcon: Boolean = false,
    treeLine: Boolean = false,
    treeNodeFilterProp: String = "value",
    treeNodeLabelProp: String = "title",
    virtual: Boolean = true,
    listHeight: Dp = 256.dp,
    variant: SelectVariant? = null,
    classNames: TreeSelectClassNames? = null,
    styles: TreeSelectStyles? = null,
    popupMatchSelectWidth: Boolean = true,
    onDropdownVisibleChange: ((visible: Boolean) -> Unit)? = null,
    onTreeExpand: ((expandedKeys: List<String>) -> Unit)? = null,
    onSearch: ((value: String) -> Unit)? = null,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
) {
    val theme = useTheme()
    val config = useConfig()

    // State management
    var internalValue by remember { mutableStateOf(defaultValue) }
    val effectiveValue = value ?: internalValue

    var expanded by remember { mutableStateOf(defaultOpen) }
    val isExpanded = open ?: expanded

    var searchQuery by remember { mutableStateOf("") }
    var internalExpandedKeys by remember {
        mutableStateOf(
            when {
                treeDefaultExpandAll -> getAllNodeKeys(treeData)
                treeDefaultExpandedKeys != null -> treeDefaultExpandedKeys
                else -> emptyList()
            }
        )
    }
    val effectiveExpandedKeys = treeExpandedKeys ?: internalExpandedKeys

    var loadedKeys by remember { mutableStateOf<Set<String>>(emptySet()) }

    // Focus management
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Get selected values as list
    val selectedValues = when (effectiveValue) {
        is List<*> -> effectiveValue.map { it.toString() }
        null -> emptyList()
        else -> listOf(effectiveValue.toString())
    }

    // Get all checked nodes
    val allCheckedNodes = remember(selectedValues, treeData) {
        findNodesByValues(treeData, selectedValues)
    }

    // Apply checked strategy for display
    val displayNodes = remember(allCheckedNodes, showCheckedStrategy, treeData) {
        when (showCheckedStrategy) {
            TreeSelectCheckedStrategy.ShowAll -> allCheckedNodes
            TreeSelectCheckedStrategy.ShowParent -> filterToParents(allCheckedNodes, treeData)
            TreeSelectCheckedStrategy.ShowChild -> filterToChildren(allCheckedNodes)
        }
    }

    // Display text
    val displayText = when {
        displayNodes.isEmpty() -> placeholder
        displayNodes.size > maxTagCount -> {
            displayNodes.take(maxTagCount).joinToString(", ") {
                getNodeLabel(it, treeNodeLabelProp)
            } + "..."
        }

        else -> displayNodes.joinToString(", ") { getNodeLabel(it, treeNodeLabelProp) }
    }

    // Effective variant
    val effectiveVariant = variant ?: if (bordered) SelectVariant.Outlined else SelectVariant.Borderless

    // Colors based on status and theme
    val borderColor = when {
        disabled -> Color(0xFFD9D9D9)
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        isExpanded -> theme.token.colorPrimary
        else -> Color(0xFFD9D9D9)
    }

    val backgroundColor = when {
        disabled -> Color(0xFFF5F5F5)
        effectiveVariant == SelectVariant.Filled -> Color(0xFFFAFAFA)
        effectiveVariant == SelectVariant.Borderless -> Color.Transparent
        else -> Color.White
    }

    // Padding based on size
    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        InputSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    Box(modifier = modifier.then(styles?.root ?: Modifier)) {
        // Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(styles?.selector ?: Modifier)
                .clickable(enabled = !disabled) {
                    if (open == null) {
                        expanded = !expanded
                    }
                    onDropdownVisibleChange?.invoke(!isExpanded)
                },
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            border = if (effectiveVariant == SelectVariant.Borderless) null
            else BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(theme.token.borderRadius)
        ) {
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Display area
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (multiple && displayNodes.isNotEmpty()) {
                        // Display tags for multiple selection
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            displayNodes.take(maxTagCount).forEach { node ->
                                AntTag(
                                    text = getNodeLabel(node, treeNodeLabelProp),
                                    closable = !disabled,
                                    onClose = {
                                        handleValueChange(
                                            newValues = selectedValues - node.value,
                                            multiple = multiple,
                                            labelInValue = labelInValue,
                                            treeNodeLabelProp = treeNodeLabelProp,
                                            allCheckedNodes = allCheckedNodes,
                                            onValueChange = onValueChange,
                                            onChange = onChange,
                                            triggerNode = node
                                        )
                                    }
                                )
                            }

                            if (displayNodes.size > maxTagCount) {
                                if (maxTagPlaceholder != null) {
                                    maxTagPlaceholder(displayNodes.size - maxTagCount)
                                } else {
                                    Text(
                                        text = "+${displayNodes.size - maxTagCount}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = displayText,
                            fontSize = 14.sp,
                            color = if (displayNodes.isEmpty()) Color.Gray else Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (allowClear && displayNodes.isNotEmpty() && !disabled) {
                        IconButton(
                            onClick = {
                                handleValueChange(
                                    newValues = emptyList(),
                                    multiple = multiple,
                                    labelInValue = labelInValue,
                                    treeNodeLabelProp = treeNodeLabelProp,
                                    allCheckedNodes = emptyList(),
                                    onValueChange = onValueChange,
                                    onChange = onChange,
                                    triggerNode = null
                                )
                                if (open == null) {
                                    expanded = false
                                }
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Text("✕", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    if (suffixIcon != null) {
                        suffixIcon()
                    } else {
                        Text(
                            text = if (isExpanded) "▲" else "▼",
                            fontSize = 12.sp,
                            color = if (disabled) Color.Gray else Color(0xFF8C8C8C)
                        )
                    }
                }
            }
        }

        // Dropdown
        if (isExpanded) {
            Popup(
                alignment = when (placement) {
                    TreeSelectPlacement.BottomLeft -> Alignment.TopStart
                    TreeSelectPlacement.BottomRight -> Alignment.TopEnd
                    TreeSelectPlacement.TopLeft -> Alignment.BottomStart
                    TreeSelectPlacement.TopRight -> Alignment.BottomEnd
                },
                onDismissRequest = {
                    if (open == null) {
                        expanded = false
                    }
                    onDropdownVisibleChange?.invoke(false)
                }
            ) {
                Card(
                    modifier = Modifier
                        .then(if (popupMatchSelectWidth) Modifier.fillMaxWidth() else Modifier.widthIn(min = 200.dp))
                        .heightIn(max = listHeight)
                        .then(dropdownStyle)
                        .then(styles?.dropdown ?: Modifier),
                    shape = RoundedCornerShape(theme.token.borderRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        // Search input
                        if (showSearch) {
                            AntInput(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    onSearch?.invoke(it)
                                },
                                placeholder = "Search",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                size = InputSize.Small
                            )
                        }

                        // Tree
                        TreeContent(
                            treeData = treeData,
                            selectedValues = selectedValues,
                            expandedKeys = effectiveExpandedKeys,
                            loadedKeys = loadedKeys,
                            searchQuery = searchQuery,
                            treeCheckable = treeCheckable,
                            treeCheckStrictly = treeCheckStrictly,
                            treeIcon = treeIcon,
                            treeLine = treeLine,
                            treeExpandAction = treeExpandAction,
                            filterTreeNode = filterTreeNode,
                            treeNodeFilterProp = treeNodeFilterProp,
                            treeNodeLabelProp = treeNodeLabelProp,
                            switcherIcon = switcherIcon,
                            loadData = loadData,
                            virtual = virtual,
                            listHeight = listHeight,
                            multiple = multiple,
                            notFoundContent = notFoundContent,
                            styles = styles,
                            theme = theme,
                            onExpandToggle = { key ->
                                val newKeys = if (effectiveExpandedKeys.contains(key)) {
                                    effectiveExpandedKeys - key
                                } else {
                                    effectiveExpandedKeys + key
                                }
                                if (treeExpandedKeys == null) {
                                    internalExpandedKeys = newKeys
                                }
                                onTreeExpand?.invoke(newKeys)
                            },
                            onSelect = { node ->
                                val newValues = if (multiple || treeCheckable) {
                                    if (selectedValues.contains(node.value)) {
                                        selectedValues - node.value
                                    } else {
                                        selectedValues + node.value
                                    }
                                } else {
                                    listOf(node.value)
                                }

                                // Handle cascade if checkable and not strict
                                val finalValues = if (treeCheckable && !treeCheckStrictly) {
                                    handleCascadeSelection(
                                        node,
                                        newValues.contains(node.value),
                                        treeData,
                                        selectedValues
                                    )
                                } else {
                                    newValues
                                }

                                handleValueChange(
                                    newValues = finalValues,
                                    multiple = multiple,
                                    labelInValue = labelInValue,
                                    treeNodeLabelProp = treeNodeLabelProp,
                                    allCheckedNodes = findNodesByValues(treeData, finalValues),
                                    onValueChange = onValueChange,
                                    onChange = onChange,
                                    triggerNode = node
                                )

                                // Close dropdown for single selection
                                if (!multiple && !treeCheckable && open == null) {
                                    expanded = false
                                    onDropdownVisibleChange?.invoke(false)
                                }
                            },
                            onLoadData = { node ->
                                loadData?.invoke(node)
                                loadedKeys = loadedKeys + node.value
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// TREE CONTENT RENDERING
// ============================================================================

@Composable
private fun TreeContent(
    treeData: List<TreeSelectNode>,
    selectedValues: List<String>,
    expandedKeys: List<String>,
    loadedKeys: Set<String>,
    searchQuery: String,
    treeCheckable: Boolean,
    treeCheckStrictly: Boolean,
    treeIcon: Boolean,
    treeLine: Boolean,
    treeExpandAction: TreeSelectExpandAction,
    filterTreeNode: ((String, TreeSelectNode) -> Boolean)?,
    treeNodeFilterProp: String,
    treeNodeLabelProp: String,
    switcherIcon: (@Composable (Boolean) -> Unit)?,
    loadData: ((TreeSelectNode) -> Unit)?,
    virtual: Boolean,
    listHeight: Dp,
    multiple: Boolean,
    notFoundContent: (@Composable () -> Unit)?,
    styles: TreeSelectStyles?,
    theme: AntThemeConfig,
    onExpandToggle: (String) -> Unit,
    onSelect: (TreeSelectNode) -> Unit,
    onLoadData: (TreeSelectNode) -> Unit,
) {
    // Filter tree data
    val filteredData = remember(treeData, searchQuery, filterTreeNode) {
        if (searchQuery.isNotEmpty()) {
            filterTree(treeData, searchQuery, filterTreeNode, treeNodeFilterProp)
        } else {
            treeData
        }
    }

    // Flatten tree for rendering
    val flattenedNodes = remember(filteredData, expandedKeys) {
        flattenTreeNodes(filteredData, expandedKeys)
    }

    if (flattenedNodes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            notFoundContent?.invoke() ?: Text(
                text = "No data",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    } else if (virtual) {
        // Virtual scrolling
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = listHeight)
                .then(styles?.tree ?: Modifier)
        ) {
            items(flattenedNodes, key = { it.node.value }) { flatNode ->
                TreeNodeItem(
                    flatNode = flatNode,
                    selectedValues = selectedValues,
                    expandedKeys = expandedKeys,
                    loadedKeys = loadedKeys,
                    treeCheckable = treeCheckable,
                    treeCheckStrictly = treeCheckStrictly,
                    treeIcon = treeIcon,
                    treeLine = treeLine,
                    treeExpandAction = treeExpandAction,
                    treeNodeLabelProp = treeNodeLabelProp,
                    switcherIcon = switcherIcon,
                    loadData = loadData,
                    theme = theme,
                    onExpandToggle = onExpandToggle,
                    onSelect = onSelect,
                    onLoadData = onLoadData
                )
            }
        }
    } else {
        // Standard scrolling
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(styles?.tree ?: Modifier)
        ) {
            flattenedNodes.forEach { flatNode ->
                TreeNodeItem(
                    flatNode = flatNode,
                    selectedValues = selectedValues,
                    expandedKeys = expandedKeys,
                    loadedKeys = loadedKeys,
                    treeCheckable = treeCheckable,
                    treeCheckStrictly = treeCheckStrictly,
                    treeIcon = treeIcon,
                    treeLine = treeLine,
                    treeExpandAction = treeExpandAction,
                    treeNodeLabelProp = treeNodeLabelProp,
                    switcherIcon = switcherIcon,
                    loadData = loadData,
                    theme = theme,
                    onExpandToggle = onExpandToggle,
                    onSelect = onSelect,
                    onLoadData = onLoadData
                )
            }
        }
    }
}

// ============================================================================
// TREE NODE ITEM
// ============================================================================

private data class FlatTreeNode(
    val node: TreeSelectNode,
    val level: Int,
    val hasChildren: Boolean,
    val isExpanded: Boolean,
)

@Composable
private fun TreeNodeItem(
    flatNode: FlatTreeNode,
    selectedValues: List<String>,
    expandedKeys: List<String>,
    loadedKeys: Set<String>,
    treeCheckable: Boolean,
    treeCheckStrictly: Boolean,
    treeIcon: Boolean,
    treeLine: Boolean,
    treeExpandAction: TreeSelectExpandAction,
    treeNodeLabelProp: String,
    switcherIcon: (@Composable (Boolean) -> Unit)?,
    loadData: ((TreeSelectNode) -> Unit)?,
    theme: AntThemeConfig,
    onExpandToggle: (String) -> Unit,
    onSelect: (TreeSelectNode) -> Unit,
    onLoadData: (TreeSelectNode) -> Unit,
) {
    val node = flatNode.node
    val level = flatNode.level
    val hasChildren = flatNode.hasChildren
    val isExpanded = flatNode.isExpanded

    val isSelected = selectedValues.contains(node.value)
    val isLoading = loadData != null && hasChildren && !node.isLeaf &&
            !loadedKeys.contains(node.value) && isExpanded

    val indentation = (24 * level).dp
    val nodeDisabled = node.disabled

    // Checkbox state
    val checkboxState = if (treeCheckable && !treeCheckStrictly) {
        calculateCheckboxState(node, selectedValues)
    } else {
        if (isSelected) ToggleableState.On else ToggleableState.Off
    }

    val backgroundColor = if (isSelected && !treeCheckable) {
        Color(0xFFE6F7FF)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(enabled = !nodeDisabled && node.selectable) {
                when (treeExpandAction) {
                    TreeSelectExpandAction.Click -> {
                        if (hasChildren) {
                            onExpandToggle(node.value)
                        }
                        if (!treeCheckable || !hasChildren) {
                            onSelect(node)
                        }
                    }

                    TreeSelectExpandAction.False -> {
                        if (!treeCheckable) {
                            onSelect(node)
                        }
                    }

                    TreeSelectExpandAction.DoubleClick -> {
                        // TODO: Implement double-click detection
                        if (!treeCheckable) {
                            onSelect(node)
                        }
                    }
                }
            }
            .padding(start = indentation, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Switcher icon
        Box(
            modifier = Modifier
                .size(16.dp)
                .clickable(enabled = hasChildren && !nodeDisabled) {
                    onExpandToggle(node.value)
                    if (isExpanded && !loadedKeys.contains(node.value) && loadData != null) {
                        onLoadData(node)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Text(
                        text = "⟳",
                        fontSize = 12.sp,
                        color = theme.token.colorPrimary
                    )
                }

                hasChildren -> {
                    if (switcherIcon != null) {
                        switcherIcon(isExpanded)
                    } else {
                        Text(
                            text = if (isExpanded) "▼" else "▶",
                            fontSize = 10.sp,
                            color = if (nodeDisabled) Color.Gray else Color(0xFF8C8C8C)
                        )
                    }
                }

                treeLine -> {
                    Text(
                        text = "◦",
                        fontSize = 8.sp,
                        color = Color(0xFFD9D9D9)
                    )
                }
            }
        }

        // Checkbox
        if (treeCheckable && node.checkable && !node.disableCheckbox) {
            TriStateCheckbox(
                state = checkboxState,
                onClick = { onSelect(node) },
                enabled = !nodeDisabled
            )
        }

        // Icon
        if (treeIcon) {
            if (node.icon != null) {
                node.icon.invoke()
            } else {
                Text(
                    text = if (hasChildren) "📁" else "📄",
                    fontSize = 14.sp
                )
            }
        }

        // Title
        Text(
            text = getNodeLabel(node, treeNodeLabelProp),
            fontSize = 14.sp,
            color = when {
                nodeDisabled -> Color(0xFFBFBFBF)
                isSelected && !treeCheckable -> theme.token.colorPrimary
                else -> Color(0xFF262626)
            }
        )
    }
}

// ============================================================================
// UTILITY FUNCTIONS
// ============================================================================

/**
 * Get all node keys recursively
 */
private fun getAllNodeKeys(nodes: List<TreeSelectNode>): List<String> {
    return nodes.flatMap { node ->
        listOf(node.value) + (node.children?.let { getAllNodeKeys(it) } ?: emptyList())
    }
}

/**
 * Find nodes by their values
 */
private fun findNodesByValues(
    nodes: List<TreeSelectNode>,
    values: List<String>,
): List<TreeSelectNode> {
    val result = mutableListOf<TreeSelectNode>()

    fun search(currentNodes: List<TreeSelectNode>) {
        for (node in currentNodes) {
            if (values.contains(node.value)) {
                result.add(node)
            }
            node.children?.let { search(it) }
        }
    }

    search(nodes)
    return result
}

/**
 * Get node label based on label property
 */
private fun getNodeLabel(node: TreeSelectNode, labelProp: String): String {
    return when (labelProp) {
        "title" -> node.title
        "value" -> node.value
        else -> node.title
    }
}

/**
 * Filter to parent nodes only (for ShowParent strategy)
 */
private fun filterToParents(
    nodes: List<TreeSelectNode>,
    treeData: List<TreeSelectNode>,
): List<TreeSelectNode> {
    val nodeValues = nodes.map { it.value }.toSet()
    val result = mutableListOf<TreeSelectNode>()

    fun traverse(currentNodes: List<TreeSelectNode>) {
        for (node in currentNodes) {
            val children = node.children
            if (children != null) {
                val allChildrenSelected = children.all { child ->
                    nodeValues.contains(child.value) ||
                            (child.children?.all { nodeValues.contains(it.value) } == true)
                }

                if (allChildrenSelected && nodeValues.contains(node.value)) {
                    result.add(node)
                } else {
                    traverse(children)
                }
            } else if (nodeValues.contains(node.value)) {
                result.add(node)
            }
        }
    }

    traverse(treeData)
    return result
}

/**
 * Filter to child (leaf) nodes only (for ShowChild strategy)
 */
private fun filterToChildren(nodes: List<TreeSelectNode>): List<TreeSelectNode> {
    return nodes.filter { it.children == null || it.children.isEmpty() }
}

/**
 * Filter tree nodes based on search query
 */
private fun filterTree(
    nodes: List<TreeSelectNode>,
    query: String,
    filterFn: ((String, TreeSelectNode) -> Boolean)?,
    filterProp: String,
): List<TreeSelectNode> {
    if (query.isEmpty()) return nodes

    return nodes.mapNotNull { node ->
        val matchesFilter = if (filterFn != null) {
            filterFn(query, node)
        } else {
            when (filterProp) {
                "value" -> node.value.contains(query, ignoreCase = true)
                "title" -> node.title.contains(query, ignoreCase = true)
                else -> node.title.contains(query, ignoreCase = true)
            }
        }

        val filteredChildren = node.children?.let {
            filterTree(it, query, filterFn, filterProp)
        }

        when {
            matchesFilter -> node.copy(children = filteredChildren)
            filteredChildren?.isNotEmpty() == true -> node.copy(children = filteredChildren)
            else -> null
        }
    }
}

/**
 * Flatten tree nodes for rendering
 */
private fun flattenTreeNodes(
    nodes: List<TreeSelectNode>,
    expandedKeys: List<String>,
    level: Int = 0,
): List<FlatTreeNode> {
    val result = mutableListOf<FlatTreeNode>()

    for (node in nodes) {
        val hasChildren = !node.children.isNullOrEmpty()
        val isExpanded = expandedKeys.contains(node.value)

        result.add(
            FlatTreeNode(
                node = node,
                level = level,
                hasChildren = hasChildren,
                isExpanded = isExpanded
            )
        )

        if (hasChildren && isExpanded) {
            result.addAll(
                flattenTreeNodes(node.children!!, expandedKeys, level + 1)
            )
        }
    }

    return result
}

/**
 * Calculate checkbox state considering parent-child relationships
 */
private fun calculateCheckboxState(
    node: TreeSelectNode,
    selectedValues: List<String>,
): ToggleableState {
    if (node.children == null || node.children.isEmpty()) {
        return if (selectedValues.contains(node.value)) {
            ToggleableState.On
        } else {
            ToggleableState.Off
        }
    }

    val allChildrenKeys = getAllNodeKeys(node.children)
    val selectedChildrenCount = allChildrenKeys.count { selectedValues.contains(it) }

    return when {
        selectedChildrenCount == 0 -> ToggleableState.Off
        selectedChildrenCount == allChildrenKeys.size -> ToggleableState.On
        else -> ToggleableState.Indeterminate
    }
}

/**
 * Handle cascade selection (parent-child)
 */
private fun handleCascadeSelection(
    node: TreeSelectNode,
    checked: Boolean,
    treeData: List<TreeSelectNode>,
    currentValues: List<String>,
): List<String> {
    val result = currentValues.toMutableList()

    // Update current node
    if (checked) {
        if (!result.contains(node.value)) {
            result.add(node.value)
        }
    } else {
        result.remove(node.value)
    }

    // Update all descendants
    fun updateDescendants(node: TreeSelectNode, checked: Boolean) {
        node.children?.forEach { child ->
            if (checked) {
                if (!result.contains(child.value) && child.checkable && !child.disableCheckbox) {
                    result.add(child.value)
                }
            } else {
                result.remove(child.value)
            }
            updateDescendants(child, checked)
        }
    }

    updateDescendants(node, checked)

    return result
}

/**
 * Handle value change with all callbacks
 */
private fun handleValueChange(
    newValues: List<String>,
    multiple: Boolean,
    labelInValue: Boolean,
    treeNodeLabelProp: String,
    allCheckedNodes: List<TreeSelectNode>,
    onValueChange: (Any?) -> Unit,
    onChange: ((value: Any, label: Any, extra: TreeSelectExtra) -> Unit)?,
    triggerNode: TreeSelectNode?,
) {
    val finalValue = if (multiple) {
        if (labelInValue) {
            newValues.map { value ->
                val node = allCheckedNodes.find { it.value == value }
                TreeSelectLabelValue(
                    label = node?.let { getNodeLabel(it, treeNodeLabelProp) } ?: value,
                    value = value
                )
            }
        } else {
            newValues
        }
    } else {
        val singleValue = newValues.firstOrNull()
        if (singleValue != null && labelInValue) {
            val node = allCheckedNodes.find { it.value == singleValue }
            TreeSelectLabelValue(
                label = node?.let { getNodeLabel(it, treeNodeLabelProp) } ?: singleValue,
                value = singleValue
            )
        } else {
            singleValue
        }
    }

    onValueChange(finalValue)

    // Call advanced onChange callback
    if (onChange != null) {
        val label = if (multiple) {
            newValues.map { value ->
                allCheckedNodes.find { it.value == value }?.let {
                    getNodeLabel(it, treeNodeLabelProp)
                } ?: value
            }
        } else {
            allCheckedNodes.firstOrNull()?.let { getNodeLabel(it, treeNodeLabelProp) } ?: ""
        }

        onChange(
            finalValue ?: (if (multiple) emptyList<String>() else ""),
            label,
            TreeSelectExtra(
                triggerValue = triggerNode?.value,
                triggerNode = triggerNode,
                allCheckedNodes = allCheckedNodes
            )
        )
    }
}
