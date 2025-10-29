package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tree Examples - Demonstrating 100% React Ant Design parity
 *
 * This file demonstrates all Tree features:
 * 1. Basic Tree
 * 2. Controlled/Uncontrolled states
 * 3. Checkable Tree with cascading
 * 4. Multiple selection
 * 5. Draggable Tree
 * 6. Lazy loading
 * 7. Virtual scrolling
 * 8. Custom icons and rendering
 * 9. Show lines
 * 10. Filtering
 */

// ============================================================================
// EXAMPLE 1: BASIC TREE
// ============================================================================

@Composable
fun BasicTreeExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(
                        key = "0-0-0",
                        title = "parent 1-0",
                        children = listOf(
                            TreeNode<Unit>(key = "0-0-0-0", title = "leaf"),
                            TreeNode<Unit>(key = "0-0-0-1", title = "leaf")
                        )
                    ),
                    TreeNode<Unit>(
                        key = "0-0-1",
                        title = "parent 1-1",
                        children = listOf(
                            TreeNode<Unit>(key = "0-0-1-0", title = "leaf")
                        )
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Basic Tree", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            defaultExpandAll = true,
            onSelect = { keys, info ->
                println("Selected: $keys")
            }
        )
    }
}

// ============================================================================
// EXAMPLE 2: CHECKABLE TREE WITH CASCADING
// ============================================================================

@Composable
fun CheckableTreeExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(
                        key = "0-0-0",
                        title = "parent 1-0",
                        children = listOf(
                            TreeNode<Unit>(key = "0-0-0-0", title = "leaf"),
                            TreeNode<Unit>(key = "0-0-0-1", title = "leaf"),
                            TreeNode<Unit>(key = "0-0-0-2", title = "leaf")
                        )
                    ),
                    TreeNode<Unit>(
                        key = "0-0-1",
                        title = "parent 1-1",
                        children = listOf(
                            TreeNode<Unit>(key = "0-0-1-0", title = "leaf"),
                            TreeNode<Unit>(key = "0-0-1-1", title = "leaf")
                        )
                    )
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "leaf"),
                    TreeNode<Unit>(key = "0-1-1", title = "leaf")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Checkable Tree (with cascading)", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            checkable = true,
            defaultExpandAll = true,
            defaultCheckedKeys = listOf("0-0-0-0", "0-0-1-0"),
            onCheck = { keys, info ->
                println("Checked: $keys")
                println("Half checked: ${info.halfCheckedKeys}")
            }
        )
    }
}

// ============================================================================
// EXAMPLE 3: CONTROLLED TREE
// ============================================================================

@Composable
fun ControlledTreeExample() {
    var expandedKeys by remember { mutableStateOf(listOf("0-0")) }
    var selectedKeys by remember { mutableStateOf(listOf<String>()) }
    var checkedKeys by remember { mutableStateOf(listOf<String>()) }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2"),
                    TreeNode<Unit>(key = "0-0-2", title = "child 3")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-1-1", title = "child 2")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Controlled Tree", fontSize = 16.sp, color = Color.Black)
        Text("Expanded: $expandedKeys", fontSize = 12.sp)
        Text("Selected: $selectedKeys", fontSize = 12.sp)
        Text("Checked: $checkedKeys", fontSize = 12.sp)

        AntTree(
            treeData = treeData,
            checkable = true,
            expandedKeys = expandedKeys,
            selectedKeys = selectedKeys,
            checkedKeys = checkedKeys,
            onExpand = { keys, info ->
                expandedKeys = keys
            },
            onSelect = { keys, info ->
                selectedKeys = keys
            },
            onCheck = { keys, info ->
                checkedKeys = keys
            }
        )
    }
}

// ============================================================================
// EXAMPLE 4: MULTIPLE SELECTION
// ============================================================================

@Composable
fun MultipleSelectionTreeExample() {
    var selectedKeys by remember { mutableStateOf(listOf<String>()) }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2"),
                    TreeNode<Unit>(key = "0-0-2", title = "child 3")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-1-1", title = "child 2")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Multiple Selection", fontSize = 16.sp, color = Color.Black)
        Text("Selected: $selectedKeys", fontSize = 12.sp)

        AntTree(
            treeData = treeData,
            multiple = true,
            defaultExpandAll = true,
            onSelect = { keys, info ->
                selectedKeys = keys
            }
        )
    }
}

// ============================================================================
// EXAMPLE 5: TREE WITH LINES
// ============================================================================

@Composable
fun TreeWithLinesExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(
                        key = "0-0-0",
                        title = "parent 1-0",
                        children = listOf(
                            TreeNode<Unit>(key = "0-0-0-0", title = "leaf"),
                            TreeNode<Unit>(key = "0-0-0-1", title = "leaf")
                        )
                    ),
                    TreeNode<Unit>(key = "0-0-1", title = "leaf")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "leaf")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Tree with Lines", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            showLine = true,
            defaultExpandAll = true
        )
    }
}

// ============================================================================
// EXAMPLE 6: TREE WITH ICONS
// ============================================================================

@Composable
fun TreeWithIconsExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "Documents",
                icon = { Text("📁", fontSize = 16.sp) },
                children = listOf(
                    TreeNode<Unit>(
                        key = "0-0-0",
                        title = "Work",
                        icon = { Text("📁", fontSize = 16.sp) },
                        children = listOf(
                            TreeNode<Unit>(
                                key = "0-0-0-0",
                                title = "Report.pdf",
                                icon = { Text("📄", fontSize = 16.sp) },
                                isLeaf = true
                            ),
                            TreeNode<Unit>(
                                key = "0-0-0-1",
                                title = "Presentation.pptx",
                                icon = { Text("📊", fontSize = 16.sp) },
                                isLeaf = true
                            )
                        )
                    ),
                    TreeNode<Unit>(
                        key = "0-0-1",
                        title = "Personal",
                        icon = { Text("📁", fontSize = 16.sp) },
                        children = listOf(
                            TreeNode<Unit>(
                                key = "0-0-1-0",
                                title = "Photo.jpg",
                                icon = { Text("🖼", fontSize = 16.sp) },
                                isLeaf = true
                            )
                        )
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Tree with Icons", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            showIcon = true,
            defaultExpandAll = true
        )
    }
}

// ============================================================================
// EXAMPLE 7: LAZY LOADING TREE
// ============================================================================

@Composable
fun LazyLoadingTreeExample() {
    var loadedKeys by remember { mutableStateOf(listOf<String>()) }
    var treeData by remember {
        mutableStateOf(
            listOf(
                TreeNode<Unit>(
                    key = "0-0",
                    title = "parent 1 (click to load children)",
                    children = emptyList(),
                    isLeaf = false
                ),
                TreeNode<Unit>(
                    key = "0-1",
                    title = "parent 2 (click to load children)",
                    children = emptyList(),
                    isLeaf = false
                )
            )
        )
    }

    fun loadChildren(node: TreeNode<Unit>) {
        // Simulate async loading
        if (!loadedKeys.contains(node.key)) {
            loadedKeys = loadedKeys + node.key

            // Add children to the node
            val newChildren = listOf(
                TreeNode<Unit>(
                    key = "${node.key}-0",
                    title = "child 1 of ${node.title}",
                    isLeaf = true
                ),
                TreeNode<Unit>(
                    key = "${node.key}-1",
                    title = "child 2 of ${node.title}",
                    isLeaf = true
                )
            )

            // Update tree data
            treeData = treeData.map {
                if (it.key == node.key) {
                    it.copy(children = newChildren)
                } else {
                    it
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Lazy Loading Tree", fontSize = 16.sp, color = Color.Black)
        Text("Loaded: $loadedKeys", fontSize = 12.sp)

        AntTree(
            treeData = treeData,
            loadedKeys = loadedKeys,
            loadData = { node ->
                loadChildren(node)
            }
        )
    }
}

// ============================================================================
// EXAMPLE 8: VIRTUAL SCROLLING TREE
// ============================================================================

@Composable
fun VirtualScrollingTreeExample() {
    val treeData = remember {
        // Generate large tree
        (0 until 20).map { i ->
            TreeNode<Unit>(
                key = "0-$i",
                title = "parent $i",
                children = (0 until 10).map { j ->
                    TreeNode<Unit>(
                        key = "0-$i-$j",
                        title = "child $j"
                    )
                }
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Virtual Scrolling (Large Tree)", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            virtual = true,
            height = 300.dp
        )
    }
}

// ============================================================================
// EXAMPLE 9: CUSTOM TITLE RENDER
// ============================================================================

@Composable
fun CustomTitleRenderTreeExample() {
    val treeData = remember {
        listOf(
            TreeNode<String>(
                key = "0-0",
                title = "Important",
                data = "high",
                children = listOf(
                    TreeNode(key = "0-0-0", title = "Task 1", data = "high"),
                    TreeNode(key = "0-0-1", title = "Task 2", data = "medium")
                )
            ),
            TreeNode<String>(
                key = "0-1",
                title = "Normal",
                data = "low",
                children = listOf(
                    TreeNode(key = "0-1-0", title = "Task 3", data = "low")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Custom Title Render", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            defaultExpandAll = true,
            titleRender = { node ->
                val color = when (node.data) {
                    "high" -> Color(0xFFFF4D4F)
                    "medium" -> Color(0xFFFFA940)
                    else -> Color(0xFF52C41A)
                }
                Text(
                    text = "${node.title} [${node.data}]",
                    fontSize = 14.sp,
                    color = color
                )
            }
        )
    }
}

// ============================================================================
// EXAMPLE 10: FILTER TREE
// ============================================================================

@Composable
fun FilterTreeExample() {
    var searchValue by remember { mutableStateOf("") }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "Documents",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "Report.pdf"),
                    TreeNode<Unit>(key = "0-0-1", title = "Presentation.pptx"),
                    TreeNode<Unit>(key = "0-0-2", title = "Spreadsheet.xlsx")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "Images",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "Photo1.jpg"),
                    TreeNode<Unit>(key = "0-1-1", title = "Photo2.png")
                )
            ),
            TreeNode<Unit>(
                key = "0-2",
                title = "Videos",
                children = listOf(
                    TreeNode<Unit>(key = "0-2-0", title = "Movie.mp4")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Filter Tree", fontSize = 16.sp, color = Color.Black)

        // Search input would go here
        Text("Search: $searchValue", fontSize = 12.sp)

        AntTree(
            treeData = treeData,
            defaultExpandAll = searchValue.isNotEmpty(),
            filterTreeNode = { node ->
                searchValue.isEmpty() || node.title.contains(searchValue, ignoreCase = true)
            }
        )
    }
}

// ============================================================================
// EXAMPLE 11: DRAGGABLE TREE
// ============================================================================

@Composable
fun DraggableTreeExample() {
    var dragInfo by remember { mutableStateOf<String?>(null) }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "child 1")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Draggable Tree", fontSize = 16.sp, color = Color.Black)
        dragInfo?.let {
            Text(it, fontSize = 12.sp, color = Color.Gray)
        }

        AntTree(
            treeData = treeData,
            draggable = true,
            defaultExpandAll = true,
            onDragStart = { info ->
                dragInfo = "Dragging: ${info.node.title}"
            },
            onDragEnd = { info ->
                dragInfo = "Drag ended"
            },
            onDrop = { info ->
                dragInfo = "Dropped ${info.dragNode.title} onto ${info.node.title}"
            }
        )
    }
}

// ============================================================================
// EXAMPLE 12: DISABLED NODES
// ============================================================================

@Composable
fun DisabledNodesTreeExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1 (enabled)",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1 (enabled)"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2 (disabled)", disabled = true),
                    TreeNode<Unit>(key = "0-0-2", title = "child 3 (enabled)")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2 (disabled)",
                disabled = true,
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "child 1")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Tree with Disabled Nodes", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            checkable = true,
            defaultExpandAll = true
        )
    }
}

// ============================================================================
// EXAMPLE 13: BLOCK NODE
// ============================================================================

@Composable
fun BlockNodeTreeExample() {
    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2")
                )
            ),
            TreeNode<Unit>(
                key = "0-1",
                title = "parent 2",
                children = listOf(
                    TreeNode<Unit>(key = "0-1-0", title = "child 1")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Block Node (Full width)", fontSize = 16.sp, color = Color.Black)

        AntTree(
            treeData = treeData,
            blockNode = true,
            defaultExpandAll = true
        )
    }
}

// ============================================================================
// EXAMPLE 14: RIGHT CLICK CONTEXT MENU
// ============================================================================

@Composable
fun RightClickTreeExample() {
    var contextMenuInfo by remember { mutableStateOf<String?>(null) }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "parent 1",
                children = listOf(
                    TreeNode<Unit>(key = "0-0-0", title = "child 1"),
                    TreeNode<Unit>(key = "0-0-1", title = "child 2")
                )
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Right Click Context Menu", fontSize = 16.sp, color = Color.Black)
        contextMenuInfo?.let {
            Text(it, fontSize = 12.sp, color = Color(0xFF1890FF))
        }

        AntTree(
            treeData = treeData,
            defaultExpandAll = true,
            onRightClick = { info ->
                contextMenuInfo = "Right clicked on: ${info.node.title}"
            }
        )
    }
}

// ============================================================================
// ALL EXAMPLES SHOWCASE
// ============================================================================

@Composable
fun AllTreeExamples() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        BasicTreeExample()
        CheckableTreeExample()
        ControlledTreeExample()
        MultipleSelectionTreeExample()
        TreeWithLinesExample()
        TreeWithIconsExample()
        LazyLoadingTreeExample()
        VirtualScrollingTreeExample()
        CustomTitleRenderTreeExample()
        FilterTreeExample()
        DraggableTreeExample()
        DisabledNodesTreeExample()
        BlockNodeTreeExample()
        RightClickTreeExample()
    }
}
