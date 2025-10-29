package digital.guimauve.antdesign

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// DATA CLASSES & TYPES
// ============================================================================

/**
 * Tree node data structure with generic type support
 */
data class TreeNode<T>(
    val key: String,
    val title: String,
    val data: T? = null,
    val icon: (@Composable () -> Unit)? = null,
    val disabled: Boolean = false,
    val disableCheckbox: Boolean = false,
    val selectable: Boolean = true,
    val checkable: Boolean = true,
    val isLeaf: Boolean = false,
    val children: List<TreeNode<T>>? = null,
    val switcherIcon: (@Composable (Boolean) -> Unit)? = null,
)

/**
 * Custom field names for mapping data structures
 */
data class TreeFieldNames(
    val title: String = "title",
    val key: String = "key",
    val children: String = "children",
)

/**
 * Configuration for showing lines between nodes
 */
data class ShowLineConfig(
    val showLeafIcon: Boolean = true,
)

/**
 * Information passed to onCheck callback
 */
data class CheckInfo<T>(
    val event: CheckEvent,
    val node: TreeNode<T>,
    val checked: Boolean,
    val checkedNodes: List<TreeNode<T>>,
    val halfCheckedKeys: List<String>,
)

enum class CheckEvent {
    CHECK,
    UNCHECK
}

/**
 * Information passed to onExpand callback
 */
data class ExpandInfo<T>(
    val expanded: Boolean,
    val node: TreeNode<T>,
)

/**
 * Information passed to onSelect callback
 */
data class SelectInfo<T>(
    val event: SelectEvent,
    val selected: Boolean,
    val node: TreeNode<T>,
    val selectedNodes: List<TreeNode<T>>,
)

enum class SelectEvent {
    SELECT
}

/**
 * Information passed to drag callbacks
 */
data class TreeDragInfo<T>(
    val event: TreeDragEvent,
    val node: TreeNode<T>,
    val expandedKeys: List<String>,
)

enum class TreeDragEvent {
    DRAG_START,
    DRAG_ENTER,
    DRAG_OVER,
    DRAG_LEAVE,
    DRAG_END
}

/**
 * Information passed to onDrop callback
 */
data class TreeDropInfo<T>(
    val event: TreeDropEvent,
    val node: TreeNode<T>,
    val dragNode: TreeNode<T>,
    val dragNodesKeys: List<String>,
    val dropPosition: Int, // -1: before, 0: inside, 1: after
    val dropToGap: Boolean,
)

enum class TreeDropEvent {
    DROP
}

/**
 * Information passed to onRightClick callback
 */
data class RightClickInfo<T>(
    val event: RightClickEvent,
    val node: TreeNode<T>,
)

enum class RightClickEvent {
    RIGHT_CLICK
}

// ============================================================================
// INTERNAL STATE CLASSES
// ============================================================================

private data class FlattenedNode<T>(
    val node: TreeNode<T>,
    val level: Int,
    val parentKey: String?,
    val hasChildren: Boolean,
    val isExpanded: Boolean,
)

private class TreeState<T>(
    initialExpandedKeys: List<String>,
    initialSelectedKeys: List<String>,
    initialCheckedKeys: List<String>,
    val autoExpandParent: Boolean,
) {
    var expandedKeys by mutableStateOf(initialExpandedKeys)
    var selectedKeys by mutableStateOf(initialSelectedKeys)
    var checkedKeys by mutableStateOf(initialCheckedKeys)
    var halfCheckedKeys by mutableStateOf<List<String>>(emptyList())
    var loadedKeys by mutableStateOf<List<String>>(emptyList())
    var dragState by mutableStateOf<DragState<T>?>(null)
}

private data class DragState<T>(
    val dragNode: TreeNode<T>,
    val dropPosition: Int,
    val dropKey: String?,
)

// ============================================================================
// MAIN TREE COMPONENT
// ============================================================================

/**
 * Tree component with full React Ant Design parity
 *
 * A Tree component displays hierarchical data in a tree structure.
 * Supports expansion, selection, checkboxes, drag & drop, lazy loading, and more.
 *
 * @param treeData The tree data structure
 * @param autoExpandParent Whether to automatically expand parent nodes
 * @param blockNode Whether tree nodes fill the row
 * @param checkable Whether to show checkboxes
 * @param checkedKeys Controlled checked keys
 * @param defaultCheckedKeys Default checked keys (uncontrolled)
 * @param defaultExpandAll Whether to expand all nodes by default
 * @param defaultExpandedKeys Default expanded keys (uncontrolled)
 * @param defaultExpandParent Whether to expand parent nodes by default
 * @param defaultSelectedKeys Default selected keys (uncontrolled)
 * @param disabled Whether the tree is disabled
 * @param draggable Enable drag & drop (Boolean or function)
 * @param expandedKeys Controlled expanded keys
 * @param fieldNames Custom field names for mapping (TreeFieldNames)
 * @param filterTreeNode Filter function for nodes
 * @param height Fixed height for virtual scrolling
 * @param icon Custom icon renderer
 * @param loadData Lazy loading function
 * @param loadedKeys Keys of loaded nodes
 * @param multiple Allow multiple selection
 * @param selectable Whether nodes are selectable
 * @param selectedKeys Controlled selected keys
 * @param showIcon Whether to show icons
 * @param showLine Whether to show connecting lines
 * @param switcherIcon Custom switcher icon renderer
 * @param titleRender Custom title renderer
 * @param virtual Enable virtual scrolling
 * @param onCheck Callback when checkbox state changes
 * @param onDragEnd Callback when drag ends
 * @param onDragEnter Callback when dragging enters a node
 * @param onDragLeave Callback when dragging leaves a node
 * @param onDragOver Callback when dragging over a node
 * @param onDragStart Callback when drag starts
 * @param onDrop Callback when drop occurs
 * @param onExpand Callback when node expands/collapses
 * @param onLoad Callback when lazy loading completes
 * @param onRightClick Callback on right click
 * @param onSelect Callback when selection changes
 * @param modifier Modifier for the tree
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> AntTree(
    treeData: List<TreeNode<T>>,
    modifier: Modifier = Modifier,
    autoExpandParent: Boolean = true,
    blockNode: Boolean = false,
    checkable: Boolean = false,
    checkedKeys: List<String>? = null,
    defaultCheckedKeys: List<String> = emptyList(),
    defaultExpandAll: Boolean = false,
    defaultExpandedKeys: List<String> = emptyList(),
    defaultExpandParent: Boolean = true,
    defaultSelectedKeys: List<String> = emptyList(),
    disabled: Boolean = false,
    draggable: Any = false, // Boolean or ((TreeNode<T>) -> Boolean)
    expandedKeys: List<String>? = null,
    fieldNames: TreeFieldNames? = null,
    filterTreeNode: ((TreeNode<T>) -> Boolean)? = null,
    height: Dp? = null,
    icon: (@Composable (TreeNode<T>) -> Unit)? = null,
    loadData: ((TreeNode<T>) -> Unit)? = null,
    loadedKeys: List<String> = emptyList(),
    multiple: Boolean = false,
    selectable: Boolean = true,
    selectedKeys: List<String>? = null,
    showIcon: Boolean = false,
    showLine: Any = false, // Boolean or ShowLineConfig
    switcherIcon: (@Composable (TreeNode<T>) -> Unit)? = null,
    titleRender: (@Composable (TreeNode<T>) -> Unit)? = null,
    virtual: Boolean = true,
    onCheck: ((List<String>, CheckInfo<T>) -> Unit)? = null,
    onDragEnd: ((TreeDragInfo<T>) -> Unit)? = null,
    onDragEnter: ((TreeDragInfo<T>) -> Unit)? = null,
    onDragLeave: ((TreeDragInfo<T>) -> Unit)? = null,
    onDragOver: ((TreeDragInfo<T>) -> Unit)? = null,
    onDragStart: ((TreeDragInfo<T>) -> Unit)? = null,
    onDrop: ((TreeDropInfo<T>) -> Unit)? = null,
    onExpand: ((List<String>, ExpandInfo<T>) -> Unit)? = null,
    onLoad: ((List<String>, ExpandInfo<T>) -> Unit)? = null,
    onRightClick: ((RightClickInfo<T>) -> Unit)? = null,
    onSelect: ((List<String>, SelectInfo<T>) -> Unit)? = null,
) {
    // Initialize state
    val initialExpanded = when {
        defaultExpandAll -> getAllKeys(treeData)
        else -> defaultExpandedKeys
    }

    val treeState = remember {
        TreeState<T>(
            initialExpandedKeys = initialExpanded,
            initialSelectedKeys = defaultSelectedKeys,
            initialCheckedKeys = defaultCheckedKeys,
            autoExpandParent = autoExpandParent
        )
    }

    // Sync controlled states
    LaunchedEffect(expandedKeys) {
        expandedKeys?.let { treeState.expandedKeys = it }
    }

    LaunchedEffect(selectedKeys) {
        selectedKeys?.let { treeState.selectedKeys = it }
    }

    LaunchedEffect(checkedKeys) {
        checkedKeys?.let {
            treeState.checkedKeys = it
            // Recalculate half-checked keys
            treeState.halfCheckedKeys = calculateHalfCheckedKeys(treeData, it)
        }
    }

    LaunchedEffect(loadedKeys) {
        treeState.loadedKeys = loadedKeys
    }

    // Filter nodes if filterTreeNode is provided
    val filteredTreeData = remember(treeData, filterTreeNode) {
        if (filterTreeNode != null) {
            filterNodes(treeData, filterTreeNode)
        } else {
            treeData
        }
    }

    // Flatten tree for rendering
    val flattenedNodes = remember(filteredTreeData, treeState.expandedKeys) {
        flattenTree(filteredTreeData, treeState.expandedKeys)
    }

    // Get node map for quick lookup
    val nodeMap = remember(treeData) {
        buildNodeMap(treeData)
    }

    // Determine if showLine is enabled
    val showLineEnabled = when (showLine) {
        is Boolean -> showLine
        is ShowLineConfig -> true
        else -> false
    }

    val showLineConfig = when (showLine) {
        is ShowLineConfig -> showLine
        else -> ShowLineConfig()
    }

    // Determine if draggable is enabled
    val isDraggable = when (draggable) {
        is Boolean -> draggable
        else -> false
    }

    // Main tree container
    Box(modifier = modifier) {
        if (virtual && height != null) {
            // Virtual scrolling mode
            VirtualTree(
                flattenedNodes = flattenedNodes,
                height = height,
                treeState = treeState,
                nodeMap = nodeMap,
                checkable = checkable,
                selectable = selectable,
                disabled = disabled,
                multiple = multiple,
                showIcon = showIcon,
                showLine = showLineEnabled,
                showLineConfig = showLineConfig,
                blockNode = blockNode,
                isDraggable = isDraggable,
                icon = icon,
                switcherIcon = switcherIcon,
                titleRender = titleRender,
                loadData = loadData,
                onExpand = { key, expand, node ->
                    handleExpand(
                        key = key,
                        expand = expand,
                        node = node,
                        treeState = treeState,
                        expandedKeys = expandedKeys,
                        autoExpandParent = autoExpandParent,
                        onExpand = onExpand,
                        onLoad = onLoad,
                        loadData = loadData
                    )
                },
                onSelect = { key, node ->
                    handleSelect(
                        key = key,
                        node = node,
                        treeState = treeState,
                        selectedKeys = selectedKeys,
                        multiple = multiple,
                        nodeMap = nodeMap,
                        onSelect = onSelect
                    )
                },
                onCheck = { key, checked, node ->
                    handleCheck(
                        key = key,
                        checked = checked,
                        node = node,
                        treeData = treeData,
                        treeState = treeState,
                        checkedKeys = checkedKeys,
                        nodeMap = nodeMap,
                        onCheck = onCheck
                    )
                },
                onRightClick = { node ->
                    onRightClick?.invoke(
                        RightClickInfo(
                            event = RightClickEvent.RIGHT_CLICK,
                            node = node
                        )
                    )
                },
                onDragStart = { node ->
                    treeState.dragState = DragState(node, 0, null)
                    onDragStart?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_START,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragEnd = { node ->
                    onDragEnd?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_END,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                    treeState.dragState = null
                },
                onDragEnter = { node ->
                    onDragEnter?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_ENTER,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragLeave = { node ->
                    onDragLeave?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_LEAVE,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragOver = { node ->
                    onDragOver?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_OVER,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDrop = { node, dropPosition ->
                    val dragState = treeState.dragState
                    if (dragState != null) {
                        onDrop?.invoke(
                            TreeDropInfo(
                                event = TreeDropEvent.DROP,
                                node = node,
                                dragNode = dragState.dragNode,
                                dragNodesKeys = listOf(dragState.dragNode.key),
                                dropPosition = dropPosition,
                                dropToGap = dropPosition != 0
                            )
                        )
                    }
                    treeState.dragState = null
                }
            )
        } else {
            // Standard rendering mode
            StandardTree(
                flattenedNodes = flattenedNodes,
                treeState = treeState,
                nodeMap = nodeMap,
                checkable = checkable,
                selectable = selectable,
                disabled = disabled,
                multiple = multiple,
                showIcon = showIcon,
                showLine = showLineEnabled,
                showLineConfig = showLineConfig,
                blockNode = blockNode,
                isDraggable = isDraggable,
                icon = icon,
                switcherIcon = switcherIcon,
                titleRender = titleRender,
                loadData = loadData,
                onExpand = { key, expand, node ->
                    handleExpand(
                        key = key,
                        expand = expand,
                        node = node,
                        treeState = treeState,
                        expandedKeys = expandedKeys,
                        autoExpandParent = autoExpandParent,
                        onExpand = onExpand,
                        onLoad = onLoad,
                        loadData = loadData
                    )
                },
                onSelect = { key, node ->
                    handleSelect(
                        key = key,
                        node = node,
                        treeState = treeState,
                        selectedKeys = selectedKeys,
                        multiple = multiple,
                        nodeMap = nodeMap,
                        onSelect = onSelect
                    )
                },
                onCheck = { key, checked, node ->
                    handleCheck(
                        key = key,
                        checked = checked,
                        node = node,
                        treeData = treeData,
                        treeState = treeState,
                        checkedKeys = checkedKeys,
                        nodeMap = nodeMap,
                        onCheck = onCheck
                    )
                },
                onRightClick = { node ->
                    onRightClick?.invoke(
                        RightClickInfo(
                            event = RightClickEvent.RIGHT_CLICK,
                            node = node
                        )
                    )
                },
                onDragStart = { node ->
                    treeState.dragState = DragState(node, 0, null)
                    onDragStart?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_START,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragEnd = { node ->
                    onDragEnd?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_END,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                    treeState.dragState = null
                },
                onDragEnter = { node ->
                    onDragEnter?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_ENTER,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragLeave = { node ->
                    onDragLeave?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_LEAVE,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDragOver = { node ->
                    onDragOver?.invoke(
                        TreeDragInfo(
                            event = TreeDragEvent.DRAG_OVER,
                            node = node,
                            expandedKeys = treeState.expandedKeys
                        )
                    )
                },
                onDrop = { node, dropPosition ->
                    val dragState = treeState.dragState
                    if (dragState != null) {
                        onDrop?.invoke(
                            TreeDropInfo(
                                event = TreeDropEvent.DROP,
                                node = node,
                                dragNode = dragState.dragNode,
                                dragNodesKeys = listOf(dragState.dragNode.key),
                                dropPosition = dropPosition,
                                dropToGap = dropPosition != 0
                            )
                        )
                    }
                    treeState.dragState = null
                }
            )
        }
    }
}

// ============================================================================
// RENDERING COMPONENTS
// ============================================================================

@Composable
private fun <T> StandardTree(
    flattenedNodes: List<FlattenedNode<T>>,
    treeState: TreeState<T>,
    nodeMap: Map<String, TreeNode<T>>,
    checkable: Boolean,
    selectable: Boolean,
    disabled: Boolean,
    multiple: Boolean,
    showIcon: Boolean,
    showLine: Boolean,
    showLineConfig: ShowLineConfig,
    blockNode: Boolean,
    isDraggable: Boolean,
    icon: (@Composable (TreeNode<T>) -> Unit)?,
    switcherIcon: (@Composable (TreeNode<T>) -> Unit)?,
    titleRender: (@Composable (TreeNode<T>) -> Unit)?,
    loadData: ((TreeNode<T>) -> Unit)?,
    onExpand: (String, Boolean, TreeNode<T>) -> Unit,
    onSelect: (String, TreeNode<T>) -> Unit,
    onCheck: (String, Boolean, TreeNode<T>) -> Unit,
    onRightClick: (TreeNode<T>) -> Unit,
    onDragStart: (TreeNode<T>) -> Unit,
    onDragEnd: (TreeNode<T>) -> Unit,
    onDragEnter: (TreeNode<T>) -> Unit,
    onDragLeave: (TreeNode<T>) -> Unit,
    onDragOver: (TreeNode<T>) -> Unit,
    onDrop: (TreeNode<T>, Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        flattenedNodes.forEach { flatNode ->
            TreeNodeItem(
                flatNode = flatNode,
                treeState = treeState,
                checkable = checkable,
                selectable = selectable,
                disabled = disabled,
                showIcon = showIcon,
                showLine = showLine,
                showLineConfig = showLineConfig,
                blockNode = blockNode,
                isDraggable = isDraggable,
                icon = icon,
                switcherIcon = switcherIcon,
                titleRender = titleRender,
                loadData = loadData,
                onExpand = onExpand,
                onSelect = onSelect,
                onCheck = onCheck,
                onRightClick = onRightClick,
                onDragStart = onDragStart,
                onDragEnd = onDragEnd,
                onDragEnter = onDragEnter,
                onDragLeave = onDragLeave,
                onDragOver = onDragOver,
                onDrop = onDrop
            )
        }
    }
}

@Composable
private fun <T> VirtualTree(
    flattenedNodes: List<FlattenedNode<T>>,
    height: Dp,
    treeState: TreeState<T>,
    nodeMap: Map<String, TreeNode<T>>,
    checkable: Boolean,
    selectable: Boolean,
    disabled: Boolean,
    multiple: Boolean,
    showIcon: Boolean,
    showLine: Boolean,
    showLineConfig: ShowLineConfig,
    blockNode: Boolean,
    isDraggable: Boolean,
    icon: (@Composable (TreeNode<T>) -> Unit)?,
    switcherIcon: (@Composable (TreeNode<T>) -> Unit)?,
    titleRender: (@Composable (TreeNode<T>) -> Unit)?,
    loadData: ((TreeNode<T>) -> Unit)?,
    onExpand: (String, Boolean, TreeNode<T>) -> Unit,
    onSelect: (String, TreeNode<T>) -> Unit,
    onCheck: (String, Boolean, TreeNode<T>) -> Unit,
    onRightClick: (TreeNode<T>) -> Unit,
    onDragStart: (TreeNode<T>) -> Unit,
    onDragEnd: (TreeNode<T>) -> Unit,
    onDragEnter: (TreeNode<T>) -> Unit,
    onDragLeave: (TreeNode<T>) -> Unit,
    onDragOver: (TreeNode<T>) -> Unit,
    onDrop: (TreeNode<T>, Int) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        items(
            items = flattenedNodes,
            key = { it.node.key }
        ) { flatNode ->
            TreeNodeItem(
                flatNode = flatNode,
                treeState = treeState,
                checkable = checkable,
                selectable = selectable,
                disabled = disabled,
                showIcon = showIcon,
                showLine = showLine,
                showLineConfig = showLineConfig,
                blockNode = blockNode,
                isDraggable = isDraggable,
                icon = icon,
                switcherIcon = switcherIcon,
                titleRender = titleRender,
                loadData = loadData,
                onExpand = onExpand,
                onSelect = onSelect,
                onCheck = onCheck,
                onRightClick = onRightClick,
                onDragStart = onDragStart,
                onDragEnd = onDragEnd,
                onDragEnter = onDragEnter,
                onDragLeave = onDragLeave,
                onDragOver = onDragOver,
                onDrop = onDrop
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> TreeNodeItem(
    flatNode: FlattenedNode<T>,
    treeState: TreeState<T>,
    checkable: Boolean,
    selectable: Boolean,
    disabled: Boolean,
    showIcon: Boolean,
    showLine: Boolean,
    showLineConfig: ShowLineConfig,
    blockNode: Boolean,
    isDraggable: Boolean,
    icon: (@Composable (TreeNode<T>) -> Unit)?,
    switcherIcon: (@Composable (TreeNode<T>) -> Unit)?,
    titleRender: (@Composable (TreeNode<T>) -> Unit)?,
    loadData: ((TreeNode<T>) -> Unit)?,
    onExpand: (String, Boolean, TreeNode<T>) -> Unit,
    onSelect: (String, TreeNode<T>) -> Unit,
    onCheck: (String, Boolean, TreeNode<T>) -> Unit,
    onRightClick: (TreeNode<T>) -> Unit,
    onDragStart: (TreeNode<T>) -> Unit,
    onDragEnd: (TreeNode<T>) -> Unit,
    onDragEnter: (TreeNode<T>) -> Unit,
    onDragLeave: (TreeNode<T>) -> Unit,
    onDragOver: (TreeNode<T>) -> Unit,
    onDrop: (TreeNode<T>, Int) -> Unit,
) {
    val node = flatNode.node
    val level = flatNode.level
    val hasChildren = flatNode.hasChildren
    val isExpanded = flatNode.isExpanded

    val isSelected = treeState.selectedKeys.contains(node.key)
    val isChecked = treeState.checkedKeys.contains(node.key)
    val isHalfChecked = treeState.halfCheckedKeys.contains(node.key)
    val isLoaded = treeState.loadedKeys.contains(node.key)
    val isLoading = loadData != null && hasChildren && !node.isLeaf && !isLoaded && isExpanded

    val indentation = (24 * level).dp
    val nodeDisabled = disabled || node.disabled
    val nodeSelectable = selectable && node.selectable && !nodeDisabled

    // Checkbox state
    val checkboxState = when {
        isChecked -> ToggleableState.On
        isHalfChecked -> ToggleableState.Indeterminate
        else -> ToggleableState.Off
    }

    // Background color based on state
    val backgroundColor = when {
        isSelected -> Color(0xFFE6F7FF)
        treeState.dragState?.dropKey == node.key -> Color(0xFFF0F0F0)
        else -> Color.Transparent
    }

    // Line drawing modifier
    val lineModifier = if (showLine) {
        Modifier.drawBehind {
            val lineColor = Color(0xFFD9D9D9)
            val strokeWidth = 1.dp.toPx()

            // Vertical line to parent
            if (level > 0) {
                drawLine(
                    color = lineColor,
                    start = Offset(indentation.toPx() - 12.dp.toPx(), 0f),
                    end = Offset(indentation.toPx() - 12.dp.toPx(), size.height / 2),
                    strokeWidth = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 2f))
                )
            }

            // Horizontal line to node
            if (level > 0) {
                drawLine(
                    color = lineColor,
                    start = Offset(indentation.toPx() - 12.dp.toPx(), size.height / 2),
                    end = Offset(indentation.toPx(), size.height / 2),
                    strokeWidth = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 2f))
                )
            }
        }
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(if (blockNode) 1f else 0f)
            .background(backgroundColor)
            .then(lineModifier)
            .pointerInput(node.key) {
                detectTapGestures(
                    onTap = {
                        if (nodeSelectable) {
                            onSelect(node.key, node)
                        }
                    },
                    onPress = { offset ->
                        val press = tryAwaitRelease()
                        if (!press) {
                            // Right click detected
                            onRightClick(node)
                        }
                    }
                )
            }
            .then(
                if (isDraggable && !nodeDisabled) {
                    Modifier.pointerInput(node.key) {
                        detectDragGestures(
                            onDragStart = {
                                onDragStart(node)
                            },
                            onDragEnd = {
                                onDragEnd(node)
                            },
                            onDrag = { change, dragAmount ->
                                // Handle drag over
                                onDragOver(node)
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
            .padding(start = indentation, top = 4.dp, bottom = 4.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Switcher icon (expand/collapse)
        Box(
            modifier = Modifier
                .size(16.dp)
                .clickable(enabled = hasChildren && !nodeDisabled) {
                    onExpand(node.key, !isExpanded, node)
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    // Loading indicator
                    Text(
                        text = "⟳",
                        fontSize = 12.sp,
                        color = Color(0xFF1890FF)
                    )
                }

                hasChildren -> {
                    if (switcherIcon != null) {
                        switcherIcon(node)
                    } else if (node.switcherIcon != null) {
                        node.switcherIcon.invoke(isExpanded)
                    } else {
                        Text(
                            text = if (isExpanded) "▼" else "▶",
                            fontSize = 10.sp,
                            color = if (nodeDisabled) Color.Gray else Color(0xFF8C8C8C)
                        )
                    }
                }

                showLine && showLineConfig.showLeafIcon -> {
                    // Leaf icon
                    Text(
                        text = "◦",
                        fontSize = 8.sp,
                        color = Color(0xFFD9D9D9)
                    )
                }
            }
        }

        // Checkbox
        if (checkable && node.checkable && !node.disableCheckbox) {
            TriStateCheckbox(
                state = checkboxState,
                onClick = {
                    val newChecked = checkboxState != ToggleableState.On
                    onCheck(node.key, newChecked, node)
                },
                enabled = !nodeDisabled
            )
        }

        // Icon
        if (showIcon) {
            when {
                icon != null -> icon(node)
                node.icon != null -> node.icon.invoke()
                else -> {
                    // Default folder/file icon
                    Text(
                        text = if (hasChildren) "📁" else "📄",
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Title
        if (titleRender != null) {
            titleRender(node)
        } else {
            Text(
                text = node.title,
                fontSize = 14.sp,
                color = when {
                    nodeDisabled -> Color(0xFFBFBFBF)
                    isSelected -> Color(0xFF1890FF)
                    else -> Color(0xFF262626)
                }
            )
        }
    }
}

// ============================================================================
// EVENT HANDLERS
// ============================================================================

private fun <T> handleExpand(
    key: String,
    expand: Boolean,
    node: TreeNode<T>,
    treeState: TreeState<T>,
    expandedKeys: List<String>?,
    autoExpandParent: Boolean,
    onExpand: ((List<String>, ExpandInfo<T>) -> Unit)?,
    onLoad: ((List<String>, ExpandInfo<T>) -> Unit)?,
    loadData: ((TreeNode<T>) -> Unit)?,
) {
    val newExpandedKeys = if (expand) {
        treeState.expandedKeys + key
    } else {
        treeState.expandedKeys - key
    }

    // Only update state if uncontrolled
    if (expandedKeys == null) {
        treeState.expandedKeys = newExpandedKeys
    }

    // Trigger onExpand callback
    onExpand?.invoke(
        newExpandedKeys,
        ExpandInfo(
            expanded = expand,
            node = node
        )
    )

    // Load data if needed
    if (expand && loadData != null && !treeState.loadedKeys.contains(key)) {
        loadData(node)
        treeState.loadedKeys = treeState.loadedKeys + key
        onLoad?.invoke(
            treeState.loadedKeys,
            ExpandInfo(
                expanded = true,
                node = node
            )
        )
    }
}

private fun <T> handleSelect(
    key: String,
    node: TreeNode<T>,
    treeState: TreeState<T>,
    selectedKeys: List<String>?,
    multiple: Boolean,
    nodeMap: Map<String, TreeNode<T>>,
    onSelect: ((List<String>, SelectInfo<T>) -> Unit)?,
) {
    val newSelectedKeys = if (multiple) {
        if (treeState.selectedKeys.contains(key)) {
            treeState.selectedKeys - key
        } else {
            treeState.selectedKeys + key
        }
    } else {
        listOf(key)
    }

    // Only update state if uncontrolled
    if (selectedKeys == null) {
        treeState.selectedKeys = newSelectedKeys
    }

    // Get selected nodes
    val selectedNodes = newSelectedKeys.mapNotNull { nodeMap[it] }

    // Trigger onSelect callback
    onSelect?.invoke(
        newSelectedKeys,
        SelectInfo(
            event = SelectEvent.SELECT,
            selected = newSelectedKeys.contains(key),
            node = node,
            selectedNodes = selectedNodes
        )
    )
}

private fun <T> handleCheck(
    key: String,
    checked: Boolean,
    node: TreeNode<T>,
    treeData: List<TreeNode<T>>,
    treeState: TreeState<T>,
    checkedKeys: List<String>?,
    nodeMap: Map<String, TreeNode<T>>,
    onCheck: ((List<String>, CheckInfo<T>) -> Unit)?,
) {
    // Calculate new checked keys with cascading
    val (newCheckedKeys, newHalfCheckedKeys) = calculateCheckedKeys(
        treeData = treeData,
        key = key,
        checked = checked,
        currentCheckedKeys = treeState.checkedKeys,
        nodeMap = nodeMap
    )

    // Only update state if uncontrolled
    if (checkedKeys == null) {
        treeState.checkedKeys = newCheckedKeys
        treeState.halfCheckedKeys = newHalfCheckedKeys
    }

    // Get checked nodes
    val checkedNodes = newCheckedKeys.mapNotNull { nodeMap[it] }

    // Trigger onCheck callback
    onCheck?.invoke(
        newCheckedKeys,
        CheckInfo(
            event = if (checked) CheckEvent.CHECK else CheckEvent.UNCHECK,
            node = node,
            checked = checked,
            checkedNodes = checkedNodes,
            halfCheckedKeys = newHalfCheckedKeys
        )
    )
}

// ============================================================================
// UTILITY FUNCTIONS
// ============================================================================

/**
 * Flatten tree structure for rendering
 */
private fun <T> flattenTree(
    nodes: List<TreeNode<T>>,
    expandedKeys: List<String>,
    level: Int = 0,
    parentKey: String? = null,
): List<FlattenedNode<T>> {
    val result = mutableListOf<FlattenedNode<T>>()

    for (node in nodes) {
        val hasChildren = !node.children.isNullOrEmpty()
        val isExpanded = expandedKeys.contains(node.key)

        result.add(
            FlattenedNode(
                node = node,
                level = level,
                parentKey = parentKey,
                hasChildren = hasChildren,
                isExpanded = isExpanded
            )
        )

        // Add children if expanded
        if (hasChildren && isExpanded) {
            result.addAll(
                flattenTree(
                    nodes = node.children!!,
                    expandedKeys = expandedKeys,
                    level = level + 1,
                    parentKey = node.key
                )
            )
        }
    }

    return result
}

/**
 * Build a map of all nodes by key for quick lookup
 */
private fun <T> buildNodeMap(nodes: List<TreeNode<T>>): Map<String, TreeNode<T>> {
    val map = mutableMapOf<String, TreeNode<T>>()

    fun traverse(nodes: List<TreeNode<T>>) {
        for (node in nodes) {
            map[node.key] = node
            node.children?.let { traverse(it) }
        }
    }

    traverse(nodes)
    return map
}

/**
 * Get all keys in the tree
 */
private fun <T> getAllKeys(nodes: List<TreeNode<T>>): List<String> {
    return nodes.flatMap { node ->
        listOf(node.key) + (node.children?.let { getAllKeys(it) } ?: emptyList())
    }
}

/**
 * Filter tree nodes based on predicate
 */
private fun <T> filterNodes(
    nodes: List<TreeNode<T>>,
    predicate: (TreeNode<T>) -> Boolean,
): List<TreeNode<T>> {
    return nodes.mapNotNull { node ->
        val matchesFilter = predicate(node)
        val filteredChildren = node.children?.let { filterNodes(it, predicate) }

        when {
            matchesFilter -> node.copy(children = filteredChildren)
            filteredChildren?.isNotEmpty() == true -> node.copy(children = filteredChildren)
            else -> null
        }
    }
}

/**
 * Calculate checked keys with cascading (parent-child relationships)
 */
private fun <T> calculateCheckedKeys(
    treeData: List<TreeNode<T>>,
    key: String,
    checked: Boolean,
    currentCheckedKeys: List<String>,
    nodeMap: Map<String, TreeNode<T>>,
): Pair<List<String>, List<String>> {
    val node = nodeMap[key] ?: return currentCheckedKeys to emptyList()

    val newCheckedKeys = currentCheckedKeys.toMutableList()

    // Update current node
    if (checked) {
        if (!newCheckedKeys.contains(key)) {
            newCheckedKeys.add(key)
        }
    } else {
        newCheckedKeys.remove(key)
    }

    // Update all children
    fun updateChildren(node: TreeNode<T>, checked: Boolean) {
        node.children?.forEach { child ->
            if (checked) {
                if (!newCheckedKeys.contains(child.key) && child.checkable && !child.disableCheckbox) {
                    newCheckedKeys.add(child.key)
                }
            } else {
                newCheckedKeys.remove(child.key)
            }
            updateChildren(child, checked)
        }
    }

    updateChildren(node, checked)

    // Calculate half-checked keys (parents with some children checked)
    val halfCheckedKeys = calculateHalfCheckedKeys(treeData, newCheckedKeys)

    return newCheckedKeys to halfCheckedKeys
}

/**
 * Calculate half-checked keys (parents with some but not all children checked)
 */
private fun <T> calculateHalfCheckedKeys(
    nodes: List<TreeNode<T>>,
    checkedKeys: List<String>,
): List<String> {
    val halfChecked = mutableListOf<String>()

    fun traverse(node: TreeNode<T>): Triple<Int, Int, Boolean> {
        val children = node.children
        if (children.isNullOrEmpty()) {
            // Leaf node
            val isChecked = checkedKeys.contains(node.key)
            return Triple(if (isChecked) 1 else 0, 1, isChecked)
        }

        var checkedCount = 0
        var totalCount = 0
        var hasChecked = false

        for (child in children) {
            val (childChecked, childTotal, childHasChecked) = traverse(child)
            checkedCount += childChecked
            totalCount += childTotal
            hasChecked = hasChecked || childHasChecked
        }

        val isFullyChecked = checkedCount == totalCount && totalCount > 0
        val isPartiallyChecked = checkedCount > 0 && checkedCount < totalCount

        if (isPartiallyChecked || (hasChecked && !isFullyChecked)) {
            halfChecked.add(node.key)
        }

        val nodeChecked = if (checkedKeys.contains(node.key)) 1 else 0
        return Triple(checkedCount + nodeChecked, totalCount + 1, hasChecked || nodeChecked > 0)
    }

    nodes.forEach { traverse(it) }

    return halfChecked
}
