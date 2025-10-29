package digital.guimauve.antdesign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val Breadcrumb by story {
    val separator by parameter("/")

    val items = remember {
        listOf(
            BreadcrumbItem("Home", onClick = {}),
            BreadcrumbItem("Application", onClick = {}),
            BreadcrumbItem("Detail")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntBreadcrumb(
            items = items,
            separator = separator
        )
    }
}

val Menu by story {
    var selectedKeys by remember { mutableStateOf(listOf("1")) }
    val inlineCollapsed by parameter(false)

    val menuItems = remember {
        listOf(
            MenuItem(key = "1", label = "Navigation One"),
            MenuItem(key = "2", label = "Navigation Two"),
            MenuItem(key = "3", label = "Navigation Three"),
            MenuItem(key = "4", label = "Navigation Four")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Vertical Menu (Light theme):")
        AntMenu(
            selectedKeys = selectedKeys,
            onSelect = { selectedKeys = listOf(it) },
            items = menuItems,
            mode = MenuMode.Vertical,
            theme = MenuTheme.Light,
            inlineCollapsed = inlineCollapsed
        )

        Text("Vertical Menu (Dark theme):")
        AntMenu(
            selectedKeys = selectedKeys,
            onSelect = { selectedKeys = listOf(it) },
            items = menuItems,
            mode = MenuMode.Vertical,
            theme = MenuTheme.Dark,
            inlineCollapsed = false
        )

        Text("Inline Menu:")
        AntMenu(
            selectedKeys = selectedKeys,
            onSelect = { selectedKeys = listOf(it) },
            items = menuItems,
            mode = MenuMode.Inline,
            theme = MenuTheme.Light,
            inlineCollapsed = inlineCollapsed
        )

        Text("Collapsed Inline Menu:")
        AntMenu(
            selectedKeys = selectedKeys,
            onSelect = { selectedKeys = listOf(it) },
            items = menuItems,
            mode = MenuMode.Inline,
            theme = MenuTheme.Light,
            inlineCollapsed = true
        )
    }
}

val MenuHorizontal by story {
    var selectedKeys by remember { mutableStateOf(listOf("1")) }

    val menuItems = remember {
        listOf(
            MenuItem(key = "1", label = "Navigation One"),
            MenuItem(key = "2", label = "Navigation Two"),
            MenuItem(key = "3", label = "Navigation Three")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntMenu(
            selectedKeys = selectedKeys,
            onSelect = { selectedKeys = listOf(it) },
            items = menuItems,
            mode = MenuMode.Horizontal
        )
    }
}

val Pagination by story {
    var current by remember { mutableStateOf(1) }
    val total by parameter(50)
    val pageSize by parameter(10)
    val showTotal by parameter(true)
    val showSizeChanger by parameter(false)
    val showQuickJumper by parameter(false)
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default pagination:")
        AntPagination(
            current = current,
            total = total,
            pageSize = pageSize,
            onChange = { page, _ -> current = page },
            size = PaginationSize.Default,
            showSizeChanger = showSizeChanger,
            showQuickJumper = showQuickJumper,
            showTotal = if (showTotal) { total, range -> "Total $total items" } else null,
            disabled = disabled,
            simple = false
        )

        Text("Small pagination:")
        AntPagination(
            current = current,
            total = total,
            pageSize = pageSize,
            onChange = { page, _ -> current = page },
            size = PaginationSize.Small,
            showTotal = if (showTotal) { total, range -> "Total $total items" } else null
        )

        Text("With showTotal:")
        AntPagination(
            current = current,
            total = 100,
            pageSize = 10,
            onChange = { page, _ -> current = page },
            showTotal = { total, range -> "Total $total items" }
        )

        Text("With showSizeChanger:")
        AntPagination(
            current = current,
            total = total,
            pageSize = pageSize,
            onChange = { page, _ -> current = page },
            showSizeChanger = true
        )

        Text("With showQuickJumper:")
        AntPagination(
            current = current,
            total = total,
            pageSize = pageSize,
            onChange = { page, _ -> current = page },
            showQuickJumper = true
        )
    }
}

val SimplePagination by story {
    var current by remember { mutableStateOf(1) }
    val total by parameter(50)
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntPagination(
            current = current,
            total = total,
            pageSize = 10,
            onChange = { page, _ -> current = page },
            size = PaginationSize.Default,
            showSizeChanger = false,
            showQuickJumper = false,
            showTotal = null,
            disabled = disabled,
            simple = true
        )
    }
}

val Steps by story {
    val current by parameter(1)

    val items = remember {
        listOf(
            StepItem(title = "Finished", description = "This is a description"),
            StepItem(title = "In Progress", description = "This is a description"),
            StepItem(title = "Waiting", description = "This is a description")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default horizontal steps (Process status):")
        AntSteps(
            current = current,
            items = items,
            direction = StepsDirection.Horizontal,
            size = StepsSize.Default,
            status = StepStatus.Process
        )

        Text("Status variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Wait Status")
                AntSteps(
                    current = 0,
                    items = items,
                    direction = StepsDirection.Horizontal,
                    size = StepsSize.Small,
                    status = StepStatus.Wait
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Finish Status")
                AntSteps(
                    current = 2,
                    items = items,
                    direction = StepsDirection.Horizontal,
                    size = StepsSize.Small,
                    status = StepStatus.Finish
                )
            }
        }

        Text("Error status example:")
        AntSteps(
            current = 1,
            items = items,
            direction = StepsDirection.Horizontal,
            size = StepsSize.Default,
            status = StepStatus.Error
        )

        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default Size")
                AntSteps(
                    current = 1,
                    items = items.take(2),
                    size = StepsSize.Default
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small Size")
                AntSteps(
                    current = 1,
                    items = items.take(2),
                    size = StepsSize.Small
                )
            }
        }
    }
}

val VerticalSteps by story {
    val current by parameter(1)

    val items = remember {
        listOf(
            StepItem(title = "Finished", description = "This is a description"),
            StepItem(title = "In Progress", description = "This is a description"),
            StepItem(title = "Waiting", description = "This is a description")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Vertical direction (Process status):")
        AntSteps(
            current = current,
            items = items,
            direction = StepsDirection.Vertical,
            size = StepsSize.Default,
            status = StepStatus.Process
        )

        Text("Vertical with Error status:")
        AntSteps(
            current = 1,
            items = items,
            direction = StepsDirection.Vertical,
            size = StepsSize.Default,
            status = StepStatus.Error
        )
    }
}

val Tabs by story {
    var activeKey by remember { mutableStateOf("1") }

    val items = remember {
        listOf(
            TabItem(
                key = "1",
                label = "Tab 1",
                content = { Text("Content of Tab 1") }
            ),
            TabItem(
                key = "2",
                label = "Tab 2",
                content = { Text("Content of Tab 2") }
            ),
            TabItem(
                key = "3",
                label = "Tab 3",
                content = { Text("Content of Tab 3") }
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Line Tabs (Default):")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            type = TabType.Line,
            position = TabPosition.Top,
            size = TabSize.Middle
        )

        Text("Card Tabs:")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            type = TabType.Card,
            position = TabPosition.Top,
            size = TabSize.Middle
        )

        Text("Editable Card Tabs:")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            type = TabType.EditableCard,
            position = TabPosition.Top,
            size = TabSize.Middle
        )

        Text("Tab Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text("Small:")
                AntTabs(
                    activeKey = activeKey,
                    onChange = { activeKey = it },
                    items = items,
                    size = TabSize.Small
                )
            }
            Column {
                Text("Large:")
                AntTabs(
                    activeKey = activeKey,
                    onChange = { activeKey = it },
                    items = items,
                    size = TabSize.Large
                )
            }
        }

        Text("Tab Positions:")
        Text("Bottom position:")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            position = TabPosition.Bottom,
            size = TabSize.Middle
        )

        Text("Left position:")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            position = TabPosition.Left,
            size = TabSize.Middle
        )

        Text("Right position:")
        AntTabs(
            activeKey = activeKey,
            onChange = { activeKey = it },
            items = items,
            position = TabPosition.Right,
            size = TabSize.Middle
        )
    }
}

val BackTop by story {
    val visibilityHeight by parameter(400)
    val duration by parameter(450)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default BackTop (visibilityHeight: ${visibilityHeight}dp, duration: ${duration}ms):")
        AntBackTop(
            visibilityHeight = visibilityHeight.dp,
            onClick = { /* Scroll to top */ },
            target = null,
            duration = duration,
            children = null
        )

        Text("BackTop with custom duration (200ms):")
        AntBackTop(
            visibilityHeight = 200.dp,
            onClick = { /* Scroll to top */ },
            target = null,
            duration = 200,
            children = null
        )

        Text("BackTop with longer duration (1000ms):")
        AntBackTop(
            visibilityHeight = 300.dp,
            onClick = { /* Scroll to top */ },
            target = null,
            duration = 1000,
            children = null
        )
    }
}
