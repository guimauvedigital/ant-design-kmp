package digital.guimauve.antdesign

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
import org.jetbrains.compose.storytale.story

val Card by story {
    val bordered by parameter(true)
    val hoverable by parameter(false)
    val withExtra by parameter(false)
    val withActions by parameter(false)
    val withCover by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Card sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small")
                AntCard(
                    title = "Small Card",
                    bordered = bordered,
                    hoverable = hoverable,
                    size = CardSize.Small
                ) {
                    Text("Small card content")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Default")
                AntCard(
                    title = "Default Card",
                    bordered = bordered,
                    hoverable = hoverable,
                    size = CardSize.Default
                ) {
                    Text("Default card content")
                }
            }
        }

        Text("Bordered vs Borderless:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntCard(
                title = "Bordered Card",
                bordered = true,
                hoverable = hoverable
            ) {
                Text("This card has borders")
            }
            AntCard(
                title = "Borderless Card",
                bordered = false,
                hoverable = hoverable
            ) {
                Text("This card has no borders")
            }
        }

        Text("Hoverable examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntCard(
                title = "Non-hoverable",
                bordered = bordered,
                hoverable = false
            ) {
                Text("No hover effect")
            }
            AntCard(
                title = "Hoverable Card",
                bordered = bordered,
                hoverable = true
            ) {
                Text("Hover for shadow effect")
            }
        }

        Text("Card with all features:")
        AntCard(
            title = "Full Featured Card",
            bordered = bordered,
            hoverable = hoverable,
            size = CardSize.Default,
            extra = if (withExtra) {
                { Text("More") }
            } else {
                { Text("Extra") }
            },
            actions = if (withActions) {
                listOf(
                    { AntButton(onClick = {}, type = ButtonType.Link) { Text("Action 1") } },
                    { AntButton(onClick = {}, type = ButtonType.Link) { Text("Action 2") } }
                )
            } else {
                listOf(
                    { AntButton(onClick = {}, type = ButtonType.Link) { Text("Edit") } },
                    { AntButton(onClick = {}, type = ButtonType.Link) { Text("Delete") } }
                )
            },
            cover = if (withCover) {
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFE6F7FF))
                    ) {
                        Text("Cover Image", modifier = Modifier.padding(16.dp))
                    }
                }
            } else {
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFFE6F7FF))
                    ) {
                        Text("Card Cover", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        ) {
            Text("Card content goes here")
            Text("Some details about the card")
            Text("More information can be added")
        }
    }
}

val List by story {
    val bordered by parameter(false)
    val loading by parameter(false)
    val split by parameter(true)

    val dataSource = remember {
        listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("All list sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = bordered,
                    size = ListSize.Small,
                    split = split,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Default")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = bordered,
                    size = ListSize.Default,
                    split = split,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Large")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = bordered,
                    size = ListSize.Large,
                    split = split,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
        }

        Text("Split parameter examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("With split (dividers)")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = bordered,
                    split = true,
                    size = ListSize.Default,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Without split")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = bordered,
                    split = false,
                    size = ListSize.Default,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
        }

        Text("Bordered lists:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Not bordered")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = false,
                    split = split,
                    size = ListSize.Default,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Bordered")
                AntList(
                    dataSource = dataSource.take(3),
                    bordered = true,
                    split = split,
                    size = ListSize.Default,
                    renderItem = { item, index ->
                        AntListItem {
                            Text(item)
                        }
                    }
                )
            }
        }

        Text("List with header and footer:")
        AntList(
            dataSource = dataSource,
            bordered = bordered,
            loading = if (loading) ListLoading.True else ListLoading.False,
            split = split,
            size = ListSize.Default,
            header = { Text("Header", fontWeight = FontWeight.Bold) },
            footer = { Text("Footer - Total: ${dataSource.size} items") },
            renderItem = { item, index ->
                AntListItem {
                    AntListItemMeta(
                        title = item,
                        description = "Description for $item"
                    )
                }
            }
        )

        Text("Loading state:")
        AntList(
            dataSource = dataSource,
            bordered = bordered,
            loading = ListLoading.True,
            split = split,
            size = ListSize.Default,
            renderItem = { item, index ->
                AntListItem {
                    Text(item)
                }
            }
        )

        Text("List items with avatars and actions:")
        AntList(
            dataSource = dataSource.take(3),
            bordered = true,
            split = split,
            size = ListSize.Default,
            renderItem = { item, index ->
                AntListItem(
                    actions = listOf(
                        {
                            AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                Text("Edit")
                            }
                        },
                        {
                            AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) {
                                Text("Delete")
                            }
                        }
                    )
                ) {
                    AntListItemMeta(
                        avatar = {
                            AntAvatar(
                                text = item.first().toString(),
                                size = AvatarSize.Default,
                                backgroundColor = Color(0xFF1890FF),
                                textColor = Color.White
                            )
                        },
                        title = item,
                        description = "Additional info for $item"
                    )
                }
            }
        )
    }
}

val Calendar by story {
    var selectedDate by remember { mutableStateOf<String?>(null) }
    val fullscreen by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Month mode:")
        AntCalendar(
            value = selectedDate,
            mode = CalendarMode.Month,
            fullscreen = fullscreen,
            onSelect = { selectedDate = it }
        )

        Text("Year mode:")
        AntCalendar(
            value = selectedDate,
            mode = CalendarMode.Year,
            fullscreen = fullscreen,
            onSelect = { selectedDate = it }
        )
    }
}

val Empty by story {
    val description by parameter("No Data")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Image style variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Default style")
                AntEmpty(
                    description = description,
                    imageStyle = EmptyImageStyle.Default
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Simple style")
                AntEmpty(
                    description = description,
                    imageStyle = EmptyImageStyle.Simple
                )
            }
        }

        Text("Different description variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AntEmpty(
                    description = "No Data Available",
                    imageStyle = EmptyImageStyle.Default
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                AntEmpty(
                    description = "No Results Found",
                    imageStyle = EmptyImageStyle.Simple
                )
            }
        }

        Text("Empty with custom image:")
        AntEmpty(
            description = "Custom Empty State",
            imageStyle = EmptyImageStyle.Default,
            image = @Composable { Text("🗂", fontSize = 64.sp, color = Color.Gray.copy(alpha = 0.5f)) }
        )

        Text("Empty with action button (children):")
        AntEmpty(
            description = "No items found",
            imageStyle = EmptyImageStyle.Default,
            children = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary,
                    size = ButtonSize.Small
                ) {
                    Text("Create New")
                }
            }
        )

        Text("Empty with custom image and action:")
        AntEmpty(
            description = "Your list is empty",
            imageStyle = EmptyImageStyle.Simple,
            image = @Composable {
                Text("📋", fontSize = 64.sp, color = Color(0xFF1890FF).copy(alpha = 0.5f))
            },
            children = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(
                        onClick = {},
                        type = ButtonType.Primary,
                        size = ButtonSize.Small
                    ) {
                        Text("Add Item")
                    }
                    AntButton(
                        onClick = {},
                        type = ButtonType.Default,
                        size = ButtonSize.Small
                    ) {
                        Text("Import")
                    }
                }
            }
        )

        Text("Empty states for different scenarios:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AntEmpty(
                    description = "No messages",
                    imageStyle = EmptyImageStyle.Simple,
                    image = @Composable { Text("💬", fontSize = 48.sp, color = Color.Gray.copy(alpha = 0.5f)) }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                AntEmpty(
                    description = "No notifications",
                    imageStyle = EmptyImageStyle.Simple,
                    image = @Composable { Text("🔔", fontSize = 48.sp, color = Color.Gray.copy(alpha = 0.5f)) }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                AntEmpty(
                    description = "No favorites",
                    imageStyle = EmptyImageStyle.Simple,
                    image = @Composable { Text("⭐", fontSize = 48.sp, color = Color.Gray.copy(alpha = 0.5f)) }
                )
            }
        }
    }
}

val Image by story {
    val preview by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Image with preview:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntImage(
                src = "https://via.placeholder.com/300",
                alt = "Placeholder image",
                width = 200.dp,
                height = 150.dp,
                preview = true
            )
            AntImage(
                src = "https://via.placeholder.com/300/FF0000",
                alt = "Red placeholder",
                width = 200.dp,
                height = 150.dp,
                preview = preview
            )
        }

        Text("Image without preview:")
        AntImage(
            src = "https://via.placeholder.com/300",
            alt = "No preview",
            width = 200.dp,
            height = 150.dp,
            preview = false
        )

        Text("Image with placeholder:")
        AntImage(
            src = "https://via.placeholder.com/300",
            alt = "With placeholder",
            width = 200.dp,
            height = 150.dp,
            preview = preview,
            placeholder = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF0F0F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⏳", fontSize = 24.sp)
                        Text("Loading...", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        )

        Text("Image with fallback:")
        AntImage(
            src = "https://invalid-url.com/image.jpg",
            alt = "Failed image",
            width = 200.dp,
            height = 150.dp,
            preview = preview,
            fallback = "https://via.placeholder.com/200/FF0000/FFFFFF?text=Error"
        )

        Text("Multiple images with different sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntImage(
                src = "https://via.placeholder.com/100",
                alt = "Small",
                width = 100.dp,
                height = 100.dp,
                preview = preview
            )
            AntImage(
                src = "https://via.placeholder.com/150",
                alt = "Medium",
                width = 150.dp,
                height = 150.dp,
                preview = preview
            )
            AntImage(
                src = "https://via.placeholder.com/200",
                alt = "Large",
                width = 200.dp,
                height = 200.dp,
                preview = preview
            )
        }

        Text("Images with custom placeholders:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntImage(
                src = "https://via.placeholder.com/150/1890FF",
                alt = "Blue image",
                width = 150.dp,
                height = 150.dp,
                preview = preview,
                placeholder = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE6F7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📷", fontSize = 32.sp, color = Color(0xFF1890FF))
                    }
                }
            )
            AntImage(
                src = "https://via.placeholder.com/150/52C41A",
                alt = "Green image",
                width = 150.dp,
                height = 150.dp,
                preview = preview,
                placeholder = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF6FFED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🖼", fontSize = 32.sp, color = Color(0xFF52C41A))
                    }
                }
            )
        }
    }
}

val Skeleton by story {
    val loading by parameter(true)
    val avatar by parameter(true)
    val paragraphRows by parameter(3)
    val title by parameter(true)
    val paragraphEnabled by parameter(true)
    val active by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntSkeleton(
            loading = loading,
            avatar = avatar,
            paragraph = if (paragraphEnabled) SkeletonParagraphProps(rows = paragraphRows) else false,
            title = title,
            active = active
        ) {
            Text("Actual content that appears when loading is false")
        }
    }
}

val Table by story {
    val bordered by parameter(true)
    val loading by parameter(false)
    val showHeader by parameter(true)
    val pagination by parameter(true)

    data class Person(val name: String, val age: Int, val address: String, val salary: Int)

    val dataSource = remember {
        listOf(
            Person("John Brown", 32, "New York No. 1", 50000),
            Person("Jim Green", 42, "London No. 1", 65000),
            Person("Joe Black", 32, "Sidney No. 1", 48000),
            Person("Jane Smith", 28, "Toronto No. 2", 52000),
            Person("Bob Wilson", 35, "Paris No. 3", 58000)
        )
    }

    val columns = remember {
        listOf(
            TableColumn<Person>(
                title = "Name",
                render = { person, _ -> Text(person.name) }
            ),
            TableColumn<Person>(
                title = "Age",
                align = TableColumnAlign.Center,
                render = { person, _ -> Text(person.age.toString()) }
            ),
            TableColumn<Person>(
                title = "Address",
                render = { person, _ -> Text(person.address) }
            )
        )
    }

    var selectedKeys by remember { mutableStateOf<List<Int>>(emptyList()) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default Table with Checkbox Selection:")
        AntTable(
            columns = columns,
            dataSource = dataSource,
            bordered = bordered,
            loading = loading,
            showHeader = showHeader,
            pagination = if (pagination) TablePagination(
                total = dataSource.size,
                pageSize = 10
            ) else null,
            size = TableSize.Default,
            rowSelection = TableRowSelection(
                selectedRowKeys = selectedKeys,
                onChange = { keys, rows, info -> selectedKeys = keys },
                type = TableRowSelectionType.Checkbox
            )
        )

        Text("Table Sizes (Small, Default, Large):")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small")
                AntTable(
                    columns = columns,
                    dataSource = dataSource.take(3),
                    size = TableSize.Small
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Default")
                AntTable(
                    columns = columns,
                    dataSource = dataSource.take(3),
                    size = TableSize.Default
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Large")
                AntTable(
                    columns = columns,
                    dataSource = dataSource.take(3),
                    size = TableSize.Large
                )
            }
        }

        Text("Table with onRow Callback:")
        var clickedRow by remember { mutableStateOf<String?>(null) }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (clickedRow != null) {
                Text(
                    "Last clicked: $clickedRow",
                    color = Color(0xFF1890FF),
                    fontWeight = FontWeight.Bold
                )
            }
            AntTable(
                columns = columns,
                dataSource = dataSource.take(3),
                onRow = { person, index ->
                    RowEventHandlers(
                        onClick = { clickedRow = "${person.name} at row $index" }
                    )
                }
            )
        }

        Text("Column Alignment Examples (Left, Center, Right):")
        val alignmentColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Left Aligned",
                    align = TableColumnAlign.Left,
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Center Aligned",
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Right Aligned",
                    align = TableColumnAlign.Right,
                    render = { person, _ -> Text("$${person.salary}") }
                )
            )
        }
        AntTable(
            columns = alignmentColumns,
            dataSource = dataSource.take(3)
        )

        Text("Column Width Specifications:")
        val widthColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name (2x width)",
                    width = 200.dp,
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age",
                    width = 100.dp,
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Address (1.5x width)",
                    width = 150.dp,
                    render = { person, _ -> Text(person.address) }
                )
            )
        }
        AntTable(
            columns = widthColumns,
            dataSource = dataSource.take(3)
        )

        Text("Fixed Columns (Left and Right):")
        val fixedColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name (Fixed Left)",
                    fixed = TableColumnFixed.Left,
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age",
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Address",
                    render = { person, _ -> Text(person.address) }
                ),
                TableColumn<Person>(
                    title = "Salary (Fixed Right)",
                    fixed = TableColumnFixed.Right,
                    align = TableColumnAlign.Right,
                    render = { person, _ -> Text("$${person.salary}") }
                )
            )
        }
        AntTable(
            columns = fixedColumns,
            dataSource = dataSource.take(3)
        )

        Text("Table with Sorting:")
        var sortedData by remember { mutableStateOf(dataSource) }
        var sortAscending by remember { mutableStateOf(true) }
        val sortableColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name",
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age (Sortable)",
                    align = TableColumnAlign.Center,
                    sorter = TableColumnSorter(compare = { a, b -> a.age.compareTo(b.age) }),
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Salary (Sortable)",
                    align = TableColumnAlign.Right,
                    sorter = TableColumnSorter(compare = { a, b -> a.salary.compareTo(b.salary) }),
                    render = { person, _ -> Text("$${person.salary}") }
                )
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(
                    onClick = {
                        sortedData = sortedData.sortedBy { it.age }
                        sortAscending = true
                    }
                ) {
                    Text("Sort by Age (Asc)")
                }
                AntButton(
                    onClick = {
                        sortedData = sortedData.sortedByDescending { it.age }
                        sortAscending = false
                    }
                ) {
                    Text("Sort by Age (Desc)")
                }
                AntButton(
                    onClick = {
                        sortedData = sortedData.sortedBy { it.salary }
                        sortAscending = true
                    }
                ) {
                    Text("Sort by Salary (Asc)")
                }
                AntButton(
                    onClick = {
                        sortedData = sortedData.sortedByDescending { it.salary }
                        sortAscending = false
                    }
                ) {
                    Text("Sort by Salary (Desc)")
                }
            }
            AntTable(
                columns = sortableColumns,
                dataSource = sortedData
            )
        }

        Text("Table with Filters:")
        var filteredData by remember { mutableStateOf(dataSource) }
        var activeFilter by remember { mutableStateOf<String?>(null) }
        val filterColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name",
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age (Filterable)",
                    align = TableColumnAlign.Center,
                    filters = listOf(
                        TableFilter("Under 30", "under30"),
                        TableFilter("30-40", "30-40"),
                        TableFilter("Over 40", "over40")
                    ),
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Address",
                    render = { person, _ -> Text(person.address) }
                )
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(
                    onClick = {
                        filteredData = dataSource.filter { it.age < 30 }
                        activeFilter = "Under 30"
                    },
                    type = if (activeFilter == "Under 30") ButtonType.Primary else ButtonType.Default
                ) {
                    Text("Under 30")
                }
                AntButton(
                    onClick = {
                        filteredData = dataSource.filter { it.age in 30..40 }
                        activeFilter = "30-40"
                    },
                    type = if (activeFilter == "30-40") ButtonType.Primary else ButtonType.Default
                ) {
                    Text("30-40")
                }
                AntButton(
                    onClick = {
                        filteredData = dataSource.filter { it.age > 40 }
                        activeFilter = "Over 40"
                    },
                    type = if (activeFilter == "Over 40") ButtonType.Primary else ButtonType.Default
                ) {
                    Text("Over 40")
                }
                AntButton(
                    onClick = {
                        filteredData = dataSource
                        activeFilter = null
                    },
                    type = ButtonType.Default
                ) {
                    Text("Clear Filter")
                }
            }
            if (activeFilter != null) {
                Text("Active filter: $activeFilter", color = Color(0xFF1890FF))
            }
            AntTable(
                columns = filterColumns,
                dataSource = filteredData
            )
        }

        Text("Table with Radio Selection:")
        var radioSelectedKey by remember { mutableStateOf<List<Int>>(emptyList()) }
        AntTable(
            columns = columns,
            dataSource = dataSource.take(3),
            rowSelection = TableRowSelection(
                selectedRowKeys = radioSelectedKey,
                onChange = { newSelection, rows, info ->
                    // For radio, only keep the last selected item
                    radioSelectedKey = if (newSelection.isNotEmpty()) {
                        listOf(newSelection.last())
                    } else {
                        emptyList()
                    }
                },
                type = TableRowSelectionType.Radio
            )
        )
        if (radioSelectedKey.isNotEmpty()) {
            Text(
                "Selected person: ${dataSource[radioSelectedKey.first()].name}",
                color = Color(0xFF1890FF),
                fontWeight = FontWeight.Bold
            )
        }

        Text("Table with rowKey for Unique Row Identification:")
        val uniqueDataSource = remember {
            listOf(
                Person("Alice Johnson", 29, "Seattle No. 5", 55000),
                Person("Bob Smith", 34, "Portland No. 8", 60000),
                Person("Charlie Brown", 31, "Austin No. 12", 54000)
            )
        }
        AntTable(
            columns = columns,
            dataSource = uniqueDataSource,
            rowKey = { person -> "${person.name}-${person.age}" },
            bordered = bordered
        )

        Text("Table with dataIndex in TableColumn:")
        val dataIndexColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name",
                    dataIndex = "name",
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age",
                    dataIndex = "age",
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Address",
                    dataIndex = "address",
                    render = { person, _ -> Text(person.address) }
                ),
                TableColumn<Person>(
                    title = "Salary",
                    dataIndex = "salary",
                    align = TableColumnAlign.Right,
                    render = { person, _ -> Text("$${person.salary}") }
                )
            )
        }
        AntTable(
            columns = dataIndexColumns,
            dataSource = dataSource.take(3)
        )

        Text("Table with key parameter in TableColumn:")
        val keyColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Name",
                    key = "person_name",
                    render = { person, _ -> Text(person.name) }
                ),
                TableColumn<Person>(
                    title = "Age",
                    key = "person_age",
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text(person.age.toString()) }
                ),
                TableColumn<Person>(
                    title = "Location",
                    key = "person_address",
                    render = { person, _ -> Text(person.address) }
                )
            )
        }
        AntTable(
            columns = keyColumns,
            dataSource = dataSource.take(3)
        )

        Text("Table with Combined dataIndex and key:")
        val combinedColumns = remember {
            listOf(
                TableColumn<Person>(
                    title = "Full Name",
                    dataIndex = "name",
                    key = "col_name",
                    render = { person, _ -> Text(person.name, fontWeight = FontWeight.Bold) }
                ),
                TableColumn<Person>(
                    title = "Years Old",
                    dataIndex = "age",
                    key = "col_age",
                    align = TableColumnAlign.Center,
                    render = { person, _ -> Text("${person.age} years") }
                ),
                TableColumn<Person>(
                    title = "Residence",
                    dataIndex = "address",
                    key = "col_address",
                    render = { person, _ -> Text(person.address) }
                )
            )
        }
        AntTable(
            columns = combinedColumns,
            dataSource = dataSource.take(3),
            rowKey = { person -> "row-${person.name.replace(" ", "-")}" }
        )
    }
}
