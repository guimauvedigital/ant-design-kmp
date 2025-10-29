package com.antdesign.ui.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antdesign.ui.*
import org.jetbrains.compose.storytale.story

// ============================================================================
// TABLE - Complete Story with ALL Parameters
// ============================================================================

val TableComplete by story {
    // Table parameters
    val bordered by parameter(true)
    val loading by parameter(false)
    val showHeader by parameter(true)
    val pagination by parameter(true)
    val size by parameter("default") // small, default, large
    val sticky by parameter(false)
    val tableLayout by parameter("auto") // auto, fixed

    // Column parameters
    val showEllipsis by parameter(false)
    val showSorting by parameter(true)
    val showFiltering by parameter(true)
    val columnAlign by parameter("left") // left, center, right

    // Row selection parameters
    val selectionType by parameter("checkbox") // checkbox, radio, none
    val showRowSelection by parameter(true)

    // Data
    data class Person(
        val key: String,
        val name: String,
        val age: Int,
        val address: String,
        val salary: Int,
        val department: String,
        val email: String
    )

    val dataSource = remember {
        listOf(
            Person("1", "John Brown", 32, "New York No. 1", 50000, "Engineering", "john@example.com"),
            Person("2", "Jim Green", 42, "London No. 1", 65000, "Marketing", "jim@example.com"),
            Person("3", "Joe Black", 32, "Sidney No. 1", 48000, "Sales", "joe@example.com"),
            Person("4", "Jane Smith", 28, "Toronto No. 2", 52000, "Engineering", "jane@example.com"),
            Person("5", "Bob Wilson", 35, "Paris No. 3", 58000, "HR", "bob@example.com"),
            Person("6", "Alice Johnson", 29, "Seattle No. 5", 55000, "Design", "alice@example.com"),
            Person("7", "Charlie Davis", 38, "Austin No. 7", 62000, "Engineering", "charlie@example.com"),
            Person("8", "Diana Miller", 31, "Boston No. 9", 54000, "Marketing", "diana@example.com")
        )
    }

    // State
    var selectedKeys by remember { mutableStateOf<List<Int>>(emptyList()) }
    var sortedData by remember { mutableStateOf(dataSource) }
    var filteredData by remember { mutableStateOf(dataSource) }
    var activeFilter by remember { mutableStateOf<String?>(null) }

    // Columns configuration
    val columns = remember {
        listOf(
            TableColumn<Person>(
                title = "Name",
                dataIndex = "name",
                key = "name",
                width = if (showEllipsis) 150.dp else null,
                fixed = TableColumnFixed.Left,
                align = when (columnAlign) {
                    "center" -> TableColumnAlign.Center
                    "right" -> TableColumnAlign.Right
                    else -> TableColumnAlign.Left
                },
                ellipsis = showEllipsis,
                ellipsisTooltip = true,
                sorter = if (showSorting) TableColumnSorter<Person>(compare = { a, b -> a.name.compareTo(b.name) }) else null,
                render = { person, _ ->
                    Text(person.name, fontWeight = FontWeight.Bold)
                }
            ),
            TableColumn<Person>(
                title = "Age",
                dataIndex = "age",
                key = "age",
                width = 80.dp,
                align = TableColumnAlign.Center,
                sorter = if (showSorting) TableColumnSorter<Person>(compare = { a, b -> a.age.compareTo(b.age) }) else null,
                filters = if (showFiltering) listOf(
                    TableFilter("Under 30", "under30"),
                    TableFilter("30-35", "30-35"),
                    TableFilter("Over 35", "over35")
                ) else null,
                render = { person, _ -> Text(person.age.toString()) }
            ),
            TableColumn<Person>(
                title = "Address",
                dataIndex = "address",
                key = "address",
                ellipsis = showEllipsis,
                render = { person, _ -> Text(person.address) }
            ),
            TableColumn<Person>(
                title = "Department",
                dataIndex = "department",
                key = "department",
                filters = if (showFiltering) listOf(
                    TableFilter("Engineering", "Engineering"),
                    TableFilter("Marketing", "Marketing"),
                    TableFilter("Sales", "Sales"),
                    TableFilter("HR", "HR"),
                    TableFilter("Design", "Design")
                ) else null,
                render = { person, _ -> Text(person.department) }
            ),
            TableColumn<Person>(
                title = "Salary",
                dataIndex = "salary",
                key = "salary",
                fixed = TableColumnFixed.Right,
                align = TableColumnAlign.Right,
                sorter = if (showSorting) TableColumnSorter<Person>(compare = { a, b -> a.salary.compareTo(b.salary) }) else null,
                render = { person, _ -> Text("$${person.salary}") }
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Table - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            AntButton(
                onClick = { sortedData = sortedData.sortedBy { it.age } },
                size = ButtonSize.Small,
                type = ButtonType.Default
            ) {
                Text("Sort by Age")
            }
            AntButton(
                onClick = {
                    filteredData = dataSource.filter { it.age < 30 }
                    activeFilter = "Under 30"
                },
                size = ButtonSize.Small,
                type = if (activeFilter == "Under 30") ButtonType.Primary else ButtonType.Default
            ) {
                Text("Filter < 30")
            }
            AntButton(
                onClick = {
                    filteredData = dataSource
                    activeFilter = null
                },
                size = ButtonSize.Small,
                type = ButtonType.Default
            ) {
                Text("Clear Filter")
            }
        }

        if (activeFilter != null) {
            Text("Active filter: $activeFilter", color = Color(0xFF1890FF), fontSize = 12.sp)
        }

        // Main table
        AntTable(
            columns = columns,
            dataSource = filteredData,
            bordered = bordered,
            loading = loading,
            showHeader = showHeader,
            pagination = if (pagination) TablePagination() else null,
            size = when (size) {
                "small" -> TableSize.Small
                "large" -> TableSize.Large
                else -> TableSize.Default
            },
            rowSelection = if (showRowSelection) {
                TableRowSelection(
                    selectedRowKeys = selectedKeys,
                    onChange = { keys, rows, info -> selectedKeys = keys },
                    type = when (selectionType) {
                        "radio" -> TableRowSelectionType.Radio
                        "checkbox" -> TableRowSelectionType.Checkbox
                        else -> TableRowSelectionType.Checkbox
                    }
                )
            } else null,
            rowKey = { person -> person.key },
            onRow = { person, index ->
                RowEventHandlers(
                    onClick = { println("Clicked row $index: ${person.name}") }
                )
            },
            sticky = if (sticky) StickyConfig.True else StickyConfig.False
        )

        if (selectedKeys.isNotEmpty()) {
            Text(
                "Selected ${selectedKeys.size} row(s)",
                color = Color(0xFF1890FF),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// ============================================================================
// LIST - Complete Story with ALL Parameters
// ============================================================================

val ListComplete by story {
    val bordered by parameter(false)
    val loading by parameter(false)
    val split by parameter(true)
    val size by parameter("default") // small, default, large
    val grid by parameter(false)
    val gridColumns by parameter(3)
    val gridGutter by parameter(16)

    data class ListItemData(
        val id: String,
        val title: String,
        val description: String,
        val avatar: String,
        val status: String
    )

    val dataSource = remember {
        listOf(
            ListItemData("1", "Ant Design Title 1", "Ant Design, a design language for background applications", "A", "active"),
            ListItemData("2", "Ant Design Title 2", "Ant Design, a design language for background applications", "B", "inactive"),
            ListItemData("3", "Ant Design Title 3", "Ant Design, a design language for background applications", "C", "active"),
            ListItemData("4", "Ant Design Title 4", "Ant Design, a design language for background applications", "D", "pending"),
            ListItemData("5", "Ant Design Title 5", "Ant Design, a design language for background applications", "E", "active")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("List - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        AntList(
            dataSource = dataSource,
            bordered = bordered,
            loading = if (loading) ListLoading.True else ListLoading.False,
            split = split,
            size = when (size) {
                "small" -> ListSize.Small
                "large" -> ListSize.Large
                else -> ListSize.Default
            },
            header = {
                Text("Header", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            footer = {
                Text("Footer - Total: ${dataSource.size} items", fontSize = 12.sp, color = Color.Gray)
            },
            grid = if (grid) {
                ListGrid(
                    column = gridColumns,
                    gutter = gridGutter.dp
                )
            } else null,
            renderItem = { item, _ ->
                AntListItem(
                    actions = listOf(
                        {
                            AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                Text("Edit")
                            }
                        },
                        {
                            AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                Text("More")
                            }
                        }
                    ),
                    extra = {
                        Text("Extra content", fontSize = 12.sp, color = Color.Gray)
                    }
                ) {
                    AntListItemMeta(
                        avatar = {
                            AntAvatar(
                                text = item.avatar,
                                size = AvatarSize.Default,
                                backgroundColor = when (item.status) {
                                    "active" -> Color(0xFF52C41A)
                                    "inactive" -> Color(0xFFD9D9D9)
                                    "pending" -> Color(0xFF1890FF)
                                    else -> Color(0xFF1890FF)
                                },
                                textColor = Color.White
                            )
                        },
                        titleComposable = {
                            Text(item.title, fontWeight = FontWeight.Bold)
                        },
                        descriptionComposable = {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(item.description, fontSize = 12.sp, color = Color.Gray)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    AntTag(
                                        text = item.status,
                                        color = when (item.status) {
                                            "active" -> TagColor.Success
                                            "inactive" -> TagColor.Default
                                            "pending" -> TagColor.Processing
                                            else -> TagColor.Default
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

// ============================================================================
// CARD - Complete Story with ALL Parameters
// ============================================================================

val CardComplete by story {
    val bordered by parameter(true)
    val hoverable by parameter(false)
    val loading by parameter(false)
    val size by parameter("default") // small, default
    val showCover by parameter(true)
    val showExtra by parameter(true)
    val showActions by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Card - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Full featured card
            AntCard(
                title = "Card Title",
                bordered = bordered,
                hoverable = hoverable,
                loading = loading,
                size = when (size) {
                    "small" -> CardSize.Small
                    else -> CardSize.Default
                },
                extra = if (showExtra) {
                    {
                        AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                            Text("More")
                        }
                    }
                } else null,
                cover = if (showCover) {
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color(0xFFE6F7FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Cover Image", fontSize = 24.sp, color = Color(0xFF1890FF))
                                Text("1200 x 400", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                } else null,
                actions = if (showActions) {
                    listOf(
                        {
                            AntButton(onClick = {}, type = ButtonType.Link) {
                                Text("Edit")
                            }
                        },
                        {
                            AntButton(onClick = {}, type = ButtonType.Link) {
                                Text("Delete")
                            }
                        },
                        {
                            AntButton(onClick = {}, type = ButtonType.Link) {
                                Text("Share")
                            }
                        }
                    )
                } else emptyList(),
                modifier = Modifier.weight(1f)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Card content goes here. This is a detailed description of the card content.",
                        fontSize = 14.sp
                    )
                    Text(
                        "Additional information can be added here with full Compose layout support.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AntTag(text = "Feature 1", color = TagColor.Success)
                        AntTag(text = "Feature 2", color = TagColor.Processing)
                        AntTag(text = "Feature 3", color = TagColor.Warning)
                    }
                }
            }

            // Card without cover
            AntCard(
                title = "Simple Card",
                bordered = bordered,
                hoverable = hoverable,
                size = when (size) {
                    "small" -> CardSize.Small
                    else -> CardSize.Default
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Simple card content without cover image or actions.")
            }
        }
    }
}

// ============================================================================
// DESCRIPTIONS - Complete Story with ALL Parameters
// ============================================================================

val DescriptionsComplete by story {
    val bordered by parameter(false)
    val colon by parameter(true)
    val column by parameter(3)
    val layout by parameter("horizontal") // horizontal, vertical
    val size by parameter("default") // default, middle, small
    val showTitle by parameter(true)
    val showExtra by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Descriptions - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        AntDescriptions(
            title = if (showTitle) "User Info" else null,
            bordered = bordered,
            colon = colon,
            column = column,
            layout = when (layout) {
                "vertical" -> DescriptionsLayout.Vertical
                else -> DescriptionsLayout.Horizontal
            },
            size = when (size) {
                "small" -> DescriptionsSize.Small
                "middle" -> DescriptionsSize.Middle
                else -> DescriptionsSize.Default
            },
            extra = if (showExtra) {
                {
                    AntButton(onClick = {}, type = ButtonType.Primary, size = ButtonSize.Small) {
                        Text("Edit")
                    }
                }
            } else null,
            labelStyle = Modifier,
            contentStyle = Modifier,
            items = listOf(
                DescriptionItem(
                    labelComposable = { Text("UserName", fontWeight = FontWeight.Bold) },
                    contentComposable = { Text("Zhou Maomao") },
                    span = DescriptionItemSpan.Fixed(1)
                ),
                DescriptionItem(
                    labelComposable = { Text("Telephone", fontWeight = FontWeight.Bold) },
                    contentComposable = { Text("1810000000") },
                    span = DescriptionItemSpan.Fixed(1)
                ),
                DescriptionItem(
                    labelComposable = { Text("Live", fontWeight = FontWeight.Bold) },
                    contentComposable = { Text("Hangzhou, Zhejiang") },
                    span = DescriptionItemSpan.Fixed(1)
                ),
                DescriptionItem(
                    labelComposable = { Text("Remark", fontWeight = FontWeight.Bold) },
                    contentComposable = { Text("empty") },
                    span = DescriptionItemSpan.Fixed(2)
                ),
                DescriptionItem(
                    labelComposable = { Text("Address", fontWeight = FontWeight.Bold) },
                    contentComposable = {
                        Text("No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China")
                    },
                    span = DescriptionItemSpan.Fixed(3)
                ),
                DescriptionItem(
                    labelComposable = { Text("Status", fontWeight = FontWeight.Bold) },
                    contentComposable = {
                        AntBadge(status = BadgeStatus.Success, text = "Active")
                    },
                    span = DescriptionItemSpan.Fixed(1)
                ),
                DescriptionItem(
                    labelComposable = { Text("Role", fontWeight = FontWeight.Bold) },
                    contentComposable = {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            AntTag(text = "Admin", color = TagColor.Error)
                            AntTag(text = "Developer", color = TagColor.Processing)
                        }
                    },
                    span = DescriptionItemSpan.Fixed(2)
                )
            )
        )
    }
}

// ============================================================================
// COLLAPSE - Complete Story with ALL Parameters
// ============================================================================

val CollapseComplete by story {
    val accordion by parameter(false)
    val bordered by parameter(true)
    val collapsible by parameter("header") // header, icon, disabled
    val expandIconPosition by parameter("start") // start, end
    val ghost by parameter(false)

    var activeKeys by remember { mutableStateOf(listOf("1")) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Collapse - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        AntCollapse(
            activeKey = activeKeys,
            onChange = { activeKeys = it },
            accordion = accordion,
            bordered = bordered,
            collapsible = when (collapsible) {
                "icon" -> CollapseCollapsibleType.Icon
                "disabled" -> CollapseCollapsibleType.Disabled
                else -> CollapseCollapsibleType.Header
            },
            expandIconPosition = when (expandIconPosition) {
                "end" -> ExpandIconPosition.End
                else -> ExpandIconPosition.Start
            },
            ghost = ghost,
            expandIcon = { panelProps ->
                Text(
                    if (panelProps.isActive) "▼" else "▶",
                    fontSize = 12.sp,
                    color = Color(0xFF1890FF)
                )
            },
            items = listOf(
                CollapseItemType(
                    key = "1",
                    label = { Text("Panel 1 - User Information", fontWeight = FontWeight.Bold) },
                    extra = {
                        AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                            Text("Edit")
                        }
                    },
                    children = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("A dog is a type of domesticated animal. Known for its loyalty and faithfulness.")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AntTag(text = "Info", color = TagColor.Processing)
                                AntTag(text = "User", color = TagColor.Success)
                            }
                        }
                    }
                ),
                CollapseItemType(
                    key = "2",
                    label = { Text("Panel 2 - Settings", fontWeight = FontWeight.Bold) },
                    disabled = false,
                    children = {
                        Text("A dog is a type of domesticated animal. Known for its loyalty and faithfulness, it can be found as a welcome guest in many households across the world.")
                    }
                ),
                CollapseItemType(
                    key = "3",
                    label = { Text("Panel 3 - Details", fontWeight = FontWeight.Bold) },
                    showArrow = true,
                    forceRender = true,
                    children = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Detailed information section with multiple elements:")
                            AntDescriptions(
                                bordered = true,
                                size = DescriptionsSize.Small,
                                column = 2,
                                items = listOf(
                                    DescriptionItem(
                                        labelComposable = { Text("Name") },
                                        contentComposable = { Text("John Doe") }
                                    ),
                                    DescriptionItem(
                                        labelComposable = { Text("Email") },
                                        contentComposable = { Text("john@example.com") }
                                    )
                                )
                            )
                        }
                    }
                )
            )
        )
    }
}

// ============================================================================
// TIMELINE - Complete Story with ALL Parameters
// ============================================================================

val TimelineComplete by story {
    val mode by parameter("left") // left, alternate, right
    val pending by parameter(false)
    val reverse by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Timeline - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        AntTimeline(
            mode = when (mode) {
                "alternate" -> TimelineMode.Alternate
                "right" -> TimelineMode.Right
                else -> TimelineMode.Left
            },
            pending = if (pending) "Recording..." else null,
            pendingDot = if (pending) {
                {
                    Text("⟳", fontSize = 16.sp, color = Color(0xFF1890FF))
                }
            } else null,
            reverse = reverse,
            items = listOf(
                TimelineItemType(
                    color = "green",
                    dot = {
                        Text("✓", fontSize = 12.sp, color = Color.White)
                    },
                    label = if (mode == "alternate") {
                        { Text("2015-09-01", fontSize = 12.sp, color = Color.Gray) }
                    } else null,
                    children = {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Create a services site", fontWeight = FontWeight.Bold)
                            Text("This is a description of the event", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                ),
                TimelineItemType(
                    color = "blue",
                    label = if (mode == "alternate") {
                        { Text("2015-09-01 09:12:11", fontSize = 12.sp, color = Color.Gray) }
                    } else null,
                    children = {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Solve initial network problems", fontWeight = FontWeight.Bold)
                            Text("Technical details here", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                ),
                TimelineItemType(
                    color = "red",
                    dot = {
                        Text("!", fontSize = 12.sp, color = Color.White)
                    },
                    label = if (mode == "alternate") {
                        { Text("2015-09-01 10:30:00", fontSize = 12.sp, color = Color.Gray) }
                    } else null,
                    children = {
                        Text("Critical issue identified")
                    }
                ),
                TimelineItemType(
                    position = TimelineItemPosition.Left,
                    label = if (mode == "alternate") {
                        { Text("2015-09-01 11:00:00", fontSize = 12.sp, color = Color.Gray) }
                    } else null,
                    children = {
                        Text("Technical testing completed")
                    }
                ),
                TimelineItemType(
                    color = "gray",
                    label = if (mode == "alternate") {
                        { Text("2015-09-01 12:00:00", fontSize = 12.sp, color = Color.Gray) }
                    } else null,
                    children = {
                        Text("Network problems being solved")
                    }
                )
            )
        )
    }
}

// ============================================================================
// TAG - Complete Story with ALL Parameters
// ============================================================================

val TagComplete by story {
    val bordered by parameter(true)
    val closable by parameter(false)
    val color by parameter("default") // default, success, processing, error, warning
    val showIcon by parameter(false)

    var tags by remember {
        mutableStateOf(listOf("Tag 1", "Tag 2", "Tag 3", "Tag 4", "Tag 5"))
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tag - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("All Color Variants:", fontWeight = FontWeight.Bold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AntTag(
                    text = "Default",
                    color = TagColor.Default,
                    bordered = bordered,
                    closable = closable,
                    icon = if (showIcon) {
                        { Text("●", fontSize = 10.sp) }
                    } else null,
                    onClose = {}
                )
                AntTag(
                    text = "Success",
                    color = TagColor.Success,
                    bordered = bordered,
                    closable = closable,
                    icon = if (showIcon) {
                        { Text("✓", fontSize = 10.sp) }
                    } else null,
                    onClose = {}
                )
                AntTag(
                    text = "Processing",
                    color = TagColor.Processing,
                    bordered = bordered,
                    closable = closable,
                    icon = if (showIcon) {
                        { Text("↻", fontSize = 10.sp) }
                    } else null,
                    onClose = {}
                )
                AntTag(
                    text = "Error",
                    color = TagColor.Error,
                    bordered = bordered,
                    closable = closable,
                    icon = if (showIcon) {
                        { Text("✕", fontSize = 10.sp) }
                    } else null,
                    onClose = {}
                )
                AntTag(
                    text = "Warning",
                    color = TagColor.Warning,
                    bordered = bordered,
                    closable = closable,
                    icon = if (showIcon) {
                        { Text("⚠", fontSize = 10.sp) }
                    } else null,
                    onClose = {}
                )
            }

            Text("Dynamic Tags (closable):", fontWeight = FontWeight.Bold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tags.forEach { tag ->
                    AntTag(
                        text = tag,
                        color = TagColor.Processing,
                        bordered = bordered,
                        closable = true,
                        onClose = {
                            tags = tags.filter { it != tag }
                        }
                    )
                }
                if (tags.isEmpty()) {
                    Text("All tags removed", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Text("Tags with Custom Icons:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTag(
                    text = "Home",
                    color = TagColor.Default,
                    bordered = bordered,
                    icon = { Text("🏠", fontSize = 10.sp) }
                )
                AntTag(
                    text = "User",
                    color = TagColor.Processing,
                    bordered = bordered,
                    icon = { Text("👤", fontSize = 10.sp) }
                )
                AntTag(
                    text = "Star",
                    color = TagColor.Warning,
                    bordered = bordered,
                    icon = { Text("⭐", fontSize = 10.sp) }
                )
                AntTag(
                    text = "Heart",
                    color = TagColor.Error,
                    bordered = bordered,
                    icon = { Text("❤", fontSize = 10.sp) }
                )
            }
        }
    }
}

// ============================================================================
// BADGE - Complete Story with ALL Parameters
// ============================================================================

val BadgeComplete by story {
    val count by parameter(5)
    val showZero by parameter(false)
    val overflowCount by parameter(99)
    val dot by parameter(false)
    val showDot by parameter(false)
    val status by parameter("default") // default, success, processing, error, warning
    val offsetX by parameter(0)
    val offsetY by parameter(0)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Badge - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Badge with count:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntBadge(
                    count = count,
                    showZero = showZero,
                    overflowCount = overflowCount,
                    offset = if (offsetX != 0 || offsetY != 0) Pair(offsetX.dp, offsetY.dp) else null
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📧", fontSize = 20.sp)
                    }
                }

                AntBadge(
                    count = 0,
                    showZero = true
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📧", fontSize = 20.sp)
                    }
                }

                AntBadge(
                    count = 150,
                    overflowCount = 99
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📧", fontSize = 20.sp)
                    }
                }
            }

            Text("Badge with dot:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntBadge(
                    dot = true,
                    status = BadgeStatus.Success
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 20.sp)
                    }
                }
                AntBadge(
                    dot = true,
                    status = BadgeStatus.Processing
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 20.sp)
                    }
                }
                AntBadge(
                    dot = true,
                    status = BadgeStatus.Error
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 20.sp)
                    }
                }
            }

            Text("Badge with status text:", fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntBadge(status = BadgeStatus.Success, text = "Success")
                AntBadge(status = BadgeStatus.Processing, text = "Processing")
                AntBadge(status = BadgeStatus.Error, text = "Error")
                AntBadge(status = BadgeStatus.Warning, text = "Warning")
                AntBadge(status = BadgeStatus.Default, text = "Default")
            }

            Text("Badge with custom colors:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntBadge(
                    count = 5,
                    color = Color(0xFF52C41A)
                ) {
                    AntButton(onClick = {}, type = ButtonType.Default) {
                        Text("Green")
                    }
                }
                AntBadge(
                    count = 5,
                    color = Color(0xFF722ED1)
                ) {
                    AntButton(onClick = {}, type = ButtonType.Default) {
                        Text("Purple")
                    }
                }
                AntBadge(
                    count = 5,
                    color = Color(0xFFFAAD14)
                ) {
                    AntButton(onClick = {}, type = ButtonType.Default) {
                        Text("Orange")
                    }
                }
            }
        }
    }
}

// ============================================================================
// AVATAR - Complete Story with ALL Parameters
// ============================================================================

val AvatarComplete by story {
    val text by parameter("U")
    val size by parameter("default") // small, default, large, custom
    val shape by parameter("circle") // circle, square
    val src by parameter("")
    val alt by parameter("User")
    val gap by parameter(4)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Avatar - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("All Sizes:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                AntAvatar(
                    text = text,
                    size = AvatarSize.Large,
                    shape = when (shape) {
                        "square" -> AvatarShape.Square
                        else -> AvatarShape.Circle
                    },
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White,
                    gap = gap.dp
                )
                AntAvatar(
                    text = text,
                    size = AvatarSize.Default,
                    shape = when (shape) {
                        "square" -> AvatarShape.Square
                        else -> AvatarShape.Circle
                    },
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White,
                    gap = gap.dp
                )
                AntAvatar(
                    text = text,
                    size = AvatarSize.Small,
                    shape = when (shape) {
                        "square" -> AvatarShape.Square
                        else -> AvatarShape.Circle
                    },
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White,
                    gap = gap.dp
                )
                Text("Large / Default / Small", fontSize = 12.sp, color = Color.Gray)
            }

            Text("Both Shapes:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntAvatar(
                    text = "CR",
                    shape = AvatarShape.Circle,
                    backgroundColor = Color(0xFF52C41A),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "SQ",
                    shape = AvatarShape.Square,
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
            }

            Text("With Icons:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntAvatar(
                    icon = { Text("👤", fontSize = 20.sp) },
                    size = AvatarSize.Large,
                    backgroundColor = Color(0xFF1890FF)
                )
                AntAvatar(
                    icon = { Text("🏠", fontSize = 16.sp) },
                    size = AvatarSize.Default,
                    backgroundColor = Color(0xFF52C41A)
                )
                AntAvatar(
                    icon = { Text("⭐", fontSize = 12.sp) },
                    size = AvatarSize.Small,
                    backgroundColor = Color(0xFFFAAD14)
                )
            }

            Text("With Images:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntAvatar(
                    src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=1",
                    alt = alt,
                    size = AvatarSize.Large
                )
                AntAvatar(
                    src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=2",
                    alt = alt,
                    size = AvatarSize.Default
                )
                AntAvatar(
                    src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=3",
                    alt = alt,
                    size = AvatarSize.Small
                )
            }

            Text("Custom Colors:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntAvatar(text = "A", backgroundColor = Color(0xFFFF4D4F), textColor = Color.White)
                AntAvatar(text = "B", backgroundColor = Color(0xFF52C41A), textColor = Color.White)
                AntAvatar(text = "C", backgroundColor = Color(0xFF1890FF), textColor = Color.White)
                AntAvatar(text = "D", backgroundColor = Color(0xFFFAAD14), textColor = Color.White)
                AntAvatar(text = "E", backgroundColor = Color(0xFF722ED1), textColor = Color.White)
            }
        }
    }
}

// ============================================================================
// AVATAR GROUP - Complete Story with ALL Parameters
// ============================================================================

val AvatarGroupComplete by story {
    val maxCount by parameter(3)
    val maxPopoverPlacement by parameter("top") // top, bottom
    val maxPopoverTrigger by parameter("hover") // hover, click, focus
    val size by parameter("default") // small, default, large

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Avatar Group - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("With maxCount = $maxCount:", fontWeight = FontWeight.Bold)
            AntAvatarGroup(
                maxCount = maxCount,
                size = when (size) {
                    "small" -> AvatarSize.Small
                    "large" -> AvatarSize.Large
                    else -> AvatarSize.Default
                },
                content = {
                    AntAvatar(text = "A", backgroundColor = Color(0xFFFF4D4F), textColor = Color.White)
                    AntAvatar(text = "B", backgroundColor = Color(0xFF52C41A), textColor = Color.White)
                    AntAvatar(text = "C", backgroundColor = Color(0xFF1890FF), textColor = Color.White)
                    AntAvatar(text = "D", backgroundColor = Color(0xFFFAAD14), textColor = Color.White)
                    AntAvatar(text = "E", backgroundColor = Color(0xFF722ED1), textColor = Color.White)
                    AntAvatar(text = "F", backgroundColor = Color(0xFFEB2F96), textColor = Color.White)
                    AntAvatar(text = "G", backgroundColor = Color(0xFF13C2C2), textColor = Color.White)
                }
            )

            Text("All Sizes:", fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Large: ", modifier = Modifier.width(80.dp))
                    AntAvatarGroup(size = AvatarSize.Large, content = {
                        AntAvatar(text = "A", backgroundColor = Color(0xFF1890FF), textColor = Color.White, size = AvatarSize.Large)
                        AntAvatar(text = "B", backgroundColor = Color(0xFF52C41A), textColor = Color.White, size = AvatarSize.Large)
                        AntAvatar(text = "C", backgroundColor = Color(0xFFFF4D4F), textColor = Color.White, size = AvatarSize.Large)
                    })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Default: ", modifier = Modifier.width(80.dp))
                    AntAvatarGroup(size = AvatarSize.Default, content = {
                        AntAvatar(text = "A", backgroundColor = Color(0xFF1890FF), textColor = Color.White)
                        AntAvatar(text = "B", backgroundColor = Color(0xFF52C41A), textColor = Color.White)
                        AntAvatar(text = "C", backgroundColor = Color(0xFFFF4D4F), textColor = Color.White)
                    })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Small: ", modifier = Modifier.width(80.dp))
                    AntAvatarGroup(size = AvatarSize.Small, content = {
                        AntAvatar(text = "A", backgroundColor = Color(0xFF1890FF), textColor = Color.White, size = AvatarSize.Small)
                        AntAvatar(text = "B", backgroundColor = Color(0xFF52C41A), textColor = Color.White, size = AvatarSize.Small)
                        AntAvatar(text = "C", backgroundColor = Color(0xFFFF4D4F), textColor = Color.White, size = AvatarSize.Small)
                    })
                }
            }

            Text("With Icons:", fontWeight = FontWeight.Bold)
            AntAvatarGroup(size = AvatarSize.Default, content = {
                AntAvatar(icon = { Text("👤", fontSize = 16.sp) }, backgroundColor = Color(0xFF1890FF))
                AntAvatar(icon = { Text("🏠", fontSize = 16.sp) }, backgroundColor = Color(0xFF52C41A))
                AntAvatar(icon = { Text("⭐", fontSize = 16.sp) }, backgroundColor = Color(0xFFFAAD14))
                AntAvatar(icon = { Text("❤", fontSize = 16.sp) }, backgroundColor = Color(0xFFFF4D4F))
            })
        }
    }
}

// ============================================================================
// COMMENT - Complete Story with ALL Parameters
// ============================================================================

val CommentComplete by story {
    val showActions by parameter(true)
    val showDateTime by parameter(true)
    val showAvatar by parameter(true)
    val nested by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Comment - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        AntComment(
            actions = if (showActions) {
                listOf(
                    {
                        AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                            Text("Reply to")
                        }
                    },
                    {
                        AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                            Text("Like (0)")
                        }
                    },
                    {
                        AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                            Text("Delete")
                        }
                    }
                )
            } else emptyList(),
            author = {
                Text("Han Solo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            },
            avatar = if (showAvatar) {
                {
                    AntAvatar(
                        text = "HS",
                        size = AvatarSize.Default,
                        backgroundColor = Color(0xFF1890FF),
                        textColor = Color.White
                    )
                }
            } else null,
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.",
                        fontSize = 14.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        AntTag(text = "Design", color = TagColor.Processing)
                        AntTag(text = "Principles", color = TagColor.Success)
                    }
                }
            },
            datetime = if (showDateTime) {
                {
                    Text(
                        "Nov 29, 2023 at 10:30 AM",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            } else null,
            children = if (nested) {
                {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AntComment(
                            author = { Text("Luke Skywalker", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                            avatar = {
                                AntAvatar(
                                    text = "LS",
                                    size = AvatarSize.Default,
                                    backgroundColor = Color(0xFF52C41A),
                                    textColor = Color.White
                                )
                            },
                            content = {
                                Text("Thanks for sharing! This is very helpful.", fontSize = 14.sp)
                            },
                            datetime = {
                                Text("Nov 29, 2023 at 11:00 AM", fontSize = 12.sp, color = Color.Gray)
                            },
                            actions = listOf(
                                {
                                    AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                        Text("Reply to")
                                    }
                                },
                                {
                                    AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                        Text("Like (2)")
                                    }
                                }
                            )
                        )

                        AntComment(
                            author = { Text("Princess Leia", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                            avatar = {
                                AntAvatar(
                                    text = "PL",
                                    size = AvatarSize.Default,
                                    backgroundColor = Color(0xFFFF4D4F),
                                    textColor = Color.White
                                )
                            },
                            content = {
                                Text("I agree with Luke. Great resource!", fontSize = 14.sp)
                            },
                            datetime = {
                                Text("Nov 29, 2023 at 11:30 AM", fontSize = 12.sp, color = Color.Gray)
                            },
                            actions = listOf(
                                {
                                    AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                        Text("Reply to")
                                    }
                                }
                            )
                        )
                    }
                }
            } else null
        )
    }
}

// ============================================================================
// CALENDAR - Complete Story with ALL Parameters
// ============================================================================

val CalendarComplete by story {
    var selectedDate by remember { mutableStateOf<String?>(null) }
    val fullscreen by parameter(true)
    val mode by parameter("month") // month, year

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Calendar - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (selectedDate != null) {
            Text(
                "Selected date: $selectedDate",
                fontSize = 14.sp,
                color = Color(0xFF1890FF),
                fontWeight = FontWeight.Bold
            )
        }

        AntCalendar(
            value = selectedDate,
            mode = when (mode) {
                "year" -> CalendarMode.Year
                else -> CalendarMode.Month
            },
            fullscreen = fullscreen,
            onSelect = { selectedDate = it },
            onPanelChange = { date, calendarMode ->
                println("Panel changed: $date, mode: $calendarMode")
            },
            dateCellRender = null,
            monthCellRender = null
        )
    }
}

// ============================================================================
// IMAGE - Complete Story with ALL Parameters
// ============================================================================

val ImageComplete by story {
    val preview by parameter(true)
    val showPlaceholder by parameter(true)
    val showFallback by parameter(false)
    val width by parameter(200)
    val height by parameter(150)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Image - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("With preview enabled:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntImage(
                    src = "https://via.placeholder.com/300/1890FF/FFFFFF",
                    alt = "Blue placeholder",
                    width = width.dp,
                    height = height.dp,
                    preview = true,
                    placeholder = if (showPlaceholder) {
                        {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF0F0F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Loading...", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    } else null
                )

                AntImage(
                    src = "https://via.placeholder.com/300/52C41A/FFFFFF",
                    alt = "Green placeholder",
                    width = width.dp,
                    height = height.dp,
                    preview = preview
                )
            }

            Text("Without preview:", fontWeight = FontWeight.Bold)
            AntImage(
                src = "https://via.placeholder.com/400/FF4D4F/FFFFFF",
                alt = "Red placeholder",
                width = width.dp,
                height = height.dp,
                preview = false
            )

            if (showFallback) {
                Text("With fallback (invalid URL):", fontWeight = FontWeight.Bold)
                AntImage(
                    src = "https://invalid-url.com/image.jpg",
                    alt = "Failed image",
                    width = width.dp,
                    height = height.dp,
                    preview = false,
                    fallback = "https://via.placeholder.com/400/FF4D4F/FFFFFF?text=Error"
                )
            }

            Text("Multiple images gallery:", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("1890FF", "52C41A", "722ED1", "FAAD14").forEach { color ->
                    AntImage(
                        src = "https://via.placeholder.com/150/$color/FFFFFF",
                        alt = "Image $color",
                        width = 120.dp,
                        height = 90.dp,
                        preview = preview
                    )
                }
            }
        }
    }
}

// ============================================================================
// STATISTIC - Complete Story with ALL Parameters
// ============================================================================

val StatisticComplete by story {
    val value by parameter(112893)
    val precision by parameter(2)
    val showPrefix by parameter(true)
    val showSuffix by parameter(true)
    val loading by parameter(false)
    val groupSeparator by parameter(",")
    val decimalSeparator by parameter(".")

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Statistic - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Basic statistic
            AntStatistic(
                value = value,
                title = { Text("Active Users") },
                precision = precision,
                groupSeparator = groupSeparator,
                decimalSeparator = decimalSeparator,
                loading = loading
            )

            // With prefix
            AntStatistic(
                value = 93,
                title = { Text("Account Balance (CNY)") },
                prefix = if (showPrefix) {
                    { Text("¥", fontSize = 20.sp, color = Color(0xFF3F8600)) }
                } else null,
                precision = 2,
                valueStyle = Modifier,
                loading = loading
            )

            // With suffix
            AntStatistic(
                value = 98.5,
                title = { Text("Conversion Rate") },
                suffix = if (showSuffix) {
                    { Text("%", fontSize = 16.sp) }
                } else null,
                precision = 1,
                loading = loading
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Custom formatter
            AntStatistic(
                value = 1128,
                title = { Text("Feedback") },
                formatter = { value ->
                    val num = (value as? Number)?.toInt() ?: 0
                    when {
                        num >= 1000 -> "${num / 1000}k+"
                        else -> num.toString()
                    }
                },
                loading = loading
            )

            // Colored value
            AntStatistic(
                value = 11.28,
                title = { Text("Growth Rate") },
                precision = 2,
                prefix = {
                    Text("↑", fontSize = 20.sp, color = Color(0xFF3F8600))
                },
                suffix = {
                    Text("%", fontSize = 16.sp, color = Color(0xFF3F8600))
                },
                valueStyle = Modifier,
                loading = loading
            )

            // Negative value
            AntStatistic(
                value = -5.32,
                title = { Text("Decline") },
                precision = 2,
                prefix = {
                    Text("↓", fontSize = 20.sp, color = Color(0xFFCF1322))
                },
                suffix = {
                    Text("%", fontSize = 16.sp, color = Color(0xFFCF1322))
                },
                valueStyle = Modifier,
                loading = loading
            )
        }

        // Countdown statistic
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AntStatisticCountdown(
                value = System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000,
                title = { Text("Countdown") },
                format = "HH:mm:ss",
                onFinish = {
                    println("Countdown finished!")
                }
            )

            AntStatisticCountdown(
                value = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3,
                title = { Text("Million Seconds") },
                format = "DD:HH:mm:ss",
                suffix = {
                    Text("Days", fontSize = 14.sp)
                }
            )
        }
    }
}

// ============================================================================
// TREE - Complete Story with ALL Parameters
// ============================================================================

val TreeComplete by story {
    val checkable by parameter(true)
    val selectable by parameter(true)
    val multiple by parameter(false)
    val showLine by parameter(false)
    val showIcon by parameter(false)
    val disabled by parameter(false)
    val draggable by parameter(false)
    val blockNode by parameter(false)

    var checkedKeys by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedKeys by remember { mutableStateOf<List<String>>(emptyList()) }
    var expandedKeys by remember { mutableStateOf(listOf("0-0", "0-0-0")) }

    val treeData = remember {
        listOf(
            TreeNode<Unit>(
                key = "0-0",
                title = "Parent 0",
                icon = if (showIcon) {
                    { Text("📁", fontSize = 14.sp) }
                } else null,
                children = listOf(
                    TreeNode(
                        key = "0-0-0",
                        title = "Child 0-0",
                        icon = if (showIcon) {
                            { Text("📁", fontSize = 14.sp) }
                        } else null,
                        children = listOf(
                            TreeNode(
                                key = "0-0-0-0",
                                title = "Leaf 0-0-0",
                                icon = if (showIcon) {
                                    { Text("📄", fontSize = 14.sp) }
                                } else null,
                                isLeaf = true
                            ),
                            TreeNode(
                                key = "0-0-0-1",
                                title = "Leaf 0-0-1",
                                icon = if (showIcon) {
                                    { Text("📄", fontSize = 14.sp) }
                                } else null,
                                isLeaf = true
                            )
                        )
                    ),
                    TreeNode(
                        key = "0-0-1",
                        title = "Child 0-1",
                        icon = if (showIcon) {
                            { Text("📁", fontSize = 14.sp) }
                        } else null,
                        children = listOf(
                            TreeNode(
                                key = "0-0-1-0",
                                title = "Leaf 0-1-0",
                                icon = if (showIcon) {
                                    { Text("📄", fontSize = 14.sp) }
                                } else null,
                                isLeaf = true
                            )
                        )
                    )
                )
            ),
            TreeNode(
                key = "0-1",
                title = "Parent 1",
                icon = if (showIcon) {
                    { Text("📁", fontSize = 14.sp) }
                } else null,
                children = listOf(
                    TreeNode(
                        key = "0-1-0",
                        title = "Child 1-0",
                        icon = if (showIcon) {
                            { Text("📄", fontSize = 14.sp) }
                        } else null,
                        isLeaf = true
                    )
                )
            ),
            TreeNode(
                key = "0-2",
                title = "Parent 2 (Disabled)",
                icon = if (showIcon) {
                    { Text("📁", fontSize = 14.sp) }
                } else null,
                disabled = true,
                children = listOf(
                    TreeNode(
                        key = "0-2-0",
                        title = "Leaf 2-0",
                        icon = if (showIcon) {
                            { Text("📄", fontSize = 14.sp) }
                        } else null,
                        isLeaf = true
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tree - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (checkedKeys.isNotEmpty()) {
                Text(
                    "Checked keys: ${checkedKeys.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = Color(0xFF52C41A)
                )
            }
            if (selectedKeys.isNotEmpty()) {
                Text(
                    "Selected keys: ${selectedKeys.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = Color(0xFF1890FF)
                )
            }
        }

        AntTree(
            treeData = treeData,
            checkable = checkable,
            selectable = selectable,
            multiple = multiple,
            showLine = showLine,
            showIcon = showIcon,
            disabled = disabled,
            draggable = draggable,
            blockNode = blockNode,
            autoExpandParent = true,
            defaultExpandAll = false,
            defaultExpandParent = false,
            checkedKeys = checkedKeys,
            defaultCheckedKeys = emptyList(),
            selectedKeys = selectedKeys,
            defaultSelectedKeys = emptyList(),
            expandedKeys = expandedKeys,
            defaultExpandedKeys = emptyList(),
            onCheck = { keys, info ->
                checkedKeys = keys
                println("Checked: $keys")
            },
            onSelect = { keys, info ->
                selectedKeys = keys
                println("Selected: $keys")
            },
            onExpand = { keys, info ->
                expandedKeys = keys
                println("Expanded: $keys")
            },
            filterTreeNode = null,
            loadData = null,
            titleRender = null,
            fieldNames = null
        )
    }
}
