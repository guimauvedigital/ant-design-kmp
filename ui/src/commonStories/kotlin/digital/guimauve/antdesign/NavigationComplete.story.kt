package digital.guimauve.antdesign

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story

// ============================================
// 1. MENU - ALL PARAMETERS
// ============================================

val MenuComplete by story {
    var selectedKeys by remember { mutableStateOf(listOf("1")) }
    var openKeys by remember { mutableStateOf(listOf<String>()) }

    val mode by parameter(MenuMode.Vertical)
    val theme by parameter(MenuTheme.Light)
    val inlineCollapsed by parameter(false)
    val multiple by parameter(false)
    val selectable by parameter(true)
    val disabled by parameter(false)
    val inlineIndent by parameter(24)
    val triggerSubMenuAction by parameter(TriggerSubMenuAction.Hover)
    val subMenuOpenDelay by parameter(0L)
    val subMenuCloseDelay by parameter(100L)
    val forceSubMenuRender by parameter(false)
    val defaultActiveFirst by parameter(false)
    val disabledOverflow by parameter(false)

    val menuItems = remember {
        listOf(
            MenuItem(
                key = "1",
                label = "Navigation One",
                icon = { Text("📧") },
                disabled = false,
                danger = false
            ),
            MenuItem(
                key = "sub1",
                label = "Navigation Two",
                icon = { Text("📁") },
                type = MenuItemType.SubMenu,
                children = listOf(
                    MenuItem(key = "1-1", label = "Option 1", icon = { Text("📄") }),
                    MenuItem(key = "1-2", label = "Option 2", icon = { Text("📄") }),
                    MenuItem(
                        key = "sub1-3",
                        label = "Submenu",
                        type = MenuItemType.SubMenu,
                        children = listOf(
                            MenuItem(key = "1-3-1", label = "Option 3"),
                            MenuItem(key = "1-3-2", label = "Option 4")
                        )
                    )
                )
            ),
            MenuItem(
                key = "group1",
                label = "Group Title",
                type = MenuItemType.Group,
                children = listOf(
                    MenuItem(key = "g1-1", label = "Group Item 1"),
                    MenuItem(key = "g1-2", label = "Group Item 2")
                )
            ),
            MenuItem(
                key = "div1",
                type = MenuItemType.Divider,
                dashed = true
            ),
            MenuItem(
                key = "3",
                label = "Navigation Three",
                icon = { Text("📂") },
                danger = true
            ),
            MenuItem(
                key = "4",
                label = "Navigation Four",
                icon = { Text("🔒") },
                disabled = true
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Menu with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Mode: $mode, Theme: $theme, Collapsed: $inlineCollapsed")
        Text("Multiple: $multiple, Selectable: $selectable, Disabled: $disabled")

        AntMenu(
            items = menuItems,
            mode = mode,
            theme = theme,
            selectedKeys = selectedKeys,
            openKeys = openKeys,
            defaultSelectedKeys = listOf("1"),
            defaultOpenKeys = listOf(),
            activeKey = null,
            defaultActiveFirst = defaultActiveFirst,
            onSelect = { selectedKeys = if (multiple) selectedKeys + it else listOf(it) },
            onDeselect = { selectedKeys = selectedKeys - it },
            onClick = { println("Clicked: $it") },
            onOpenChange = { openKeys = it },
            expandIcon = null,
            itemIcon = null,
            inlineIndent = inlineIndent.dp,
            inlineCollapsed = inlineCollapsed,
            multiple = multiple,
            selectable = selectable,
            disabled = disabled,
            direction = MenuDirection.Ltr,
            triggerSubMenuAction = triggerSubMenuAction,
            subMenuOpenDelay = subMenuOpenDelay,
            subMenuCloseDelay = subMenuCloseDelay,
            forceSubMenuRender = forceSubMenuRender,
            overflowedIndicator = null,
            overflowedIndicatorPopupClassName = null,
            getPopupContainer = null,
            motion = MenuMotion(durationMillis = 300, easing = FastOutSlowInEasing),
            defaultMotions = null,
            builtinPlacements = null,
            disabledOverflow = disabledOverflow,
            rootClassName = null,
            classNames = null,
            styles = null,
            menuRef = null
        )
    }
}

// ============================================
// 2. BREADCRUMB - ALL PARAMETERS
// ============================================

val BreadcrumbComplete by story {
    val separator by parameter("/")
    val maxItems by parameter<Int?>(null)

    val items = remember {
        listOf(
            BreadcrumbItem(
                title = "Home",
                href = "/home",
                icon = { Text("🏠", fontSize = 14.sp) },
                onClick = { println("Home clicked") }
            ),
            BreadcrumbItem(
                title = "Application",
                href = "/app",
                onClick = { println("Application clicked") }
            ),
            BreadcrumbItem(
                title = "Categories",
                menu = BreadcrumbMenu(
                    items = listOf(
                        BreadcrumbMenuItem(key = "1", label = "Category 1", onClick = {}),
                        BreadcrumbMenuItem(key = "2", label = "Category 2", onClick = {}),
                        BreadcrumbMenuItem(key = "3", label = "Category 3", onClick = {})
                    )
                ),
                dropdownProps = DropdownProps(
                    placement = DropdownPlacement.BottomLeft,
                    trigger = DropdownTrigger.Hover,
                    arrow = true,
                    disabled = false
                )
            ),
            BreadcrumbItem(
                title = "Detail",
                path = "/detail/:id",
                params = mapOf("id" to "123")
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Breadcrumb with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Separator: '$separator', MaxItems: ${maxItems ?: "none"}")

        AntBreadcrumb(
            items = items,
            modifier = Modifier,
            separator = separator,
            maxItems = maxItems,
            itemRender = null,
            params = mapOf("id" to "123"),
            prefixCls = "ant-breadcrumb",
            className = null,
            rootClassName = null,
            style = null,
            classNames = BreadcrumbClassNames(
                item = "custom-item",
                link = "custom-link",
                separator = "custom-separator"
            ),
            styles = BreadcrumbStyles(
                item = Modifier,
                link = Modifier,
                separator = Modifier
            )
        )

        Text("With custom separator:")
        AntBreadcrumb(
            items = items.take(3),
            separator = { Text(">", color = Color.Gray) }
        )
    }
}

// ============================================
// 3. TABS - ALL PARAMETERS
// ============================================

val TabsComplete by story {
    var activeKey by remember { mutableStateOf("1") }
    var items by remember {
        mutableStateOf(
            listOf(
                TabItem(
                    key = "1",
                    label = "Tab 1",
                    icon = { Text("📄") },
                    disabled = false,
                    closable = true,
                    forceRender = false,
                    destroyOnHidden = false,
                    content = { Text("Content of Tab 1") }
                ),
                TabItem(
                    key = "2",
                    label = "Tab 2",
                    icon = { Text("📋") },
                    content = { Text("Content of Tab 2") }
                ),
                TabItem(
                    key = "3",
                    label = "Tab 3 (Disabled)",
                    disabled = true,
                    content = { Text("Content of Tab 3") }
                )
            )
        )
    }

    val type by parameter(TabType.Line)
    val size by parameter(TabSize.Middle)
    val position by parameter(TabPosition.Top)
    val centered by parameter(false)
    val hideAdd by parameter(false)
    val animatedInkBar by parameter(true)
    val animatedTabPane by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tabs with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Type: $type, Size: $size, Position: $position, Centered: $centered")

        AntTabs(
            items = items,
            activeKey = activeKey,
            defaultActiveKey = "1",
            onChange = { activeKey = it },
            onTabClick = { key, _ -> println("Tab clicked: $key") },
            onTabScroll = { direction -> println("Scrolled: $direction") },
            type = type,
            size = size,
            position = position,
            centered = centered,
            tabBarExtraContent = TabBarExtraContent.Split(
                left = { Text("Left", fontSize = 12.sp, color = Color.Gray) },
                right = { Text("Right", fontSize = 12.sp, color = Color.Gray) }
            ),
            tabBarGutter = 8.dp,
            tabBarStyle = Modifier.background(Color(0xFFF5F5F5)),
            onEdit = { targetKey, action ->
                when (action) {
                    TabAction.Add -> {
                        val newKey = (items.size + 1).toString()
                        items = items + TabItem(
                            key = newKey,
                            label = "New Tab",
                            content = { Text("New content") }
                        )
                        activeKey = newKey
                    }

                    TabAction.Remove -> {
                        val newItems = items.filter { it.key != targetKey }
                        if (newItems.isNotEmpty()) {
                            items = newItems
                            if (activeKey == targetKey) {
                                activeKey = newItems.first().key
                            }
                        }
                    }
                }
            },
            hideAdd = hideAdd,
            addIcon = { Text("+", fontSize = 16.sp) },
            removeIcon = { Text("×", fontSize = 16.sp) },
            animated = TabsAnimated.Simple(
                inkBar = animatedInkBar,
                tabPane = animatedTabPane
            ),
            indicator = TabIndicator(
                size = { it - 20.dp },
                align = TabIndicatorAlign.Center,
                color = null,
                style = Modifier.clip(RoundedCornerShape(2.dp))
            ),
            destroyInactiveTabPane = false,
            destroyOnHidden = false,
            more = MoreProps(icon = null, trigger = "hover"),
            moreIcon = null,
            popupClassName = null,
            renderTabBar = null,
            classNames = TabsClassNames(
                root = "tabs-root",
                nav = "tabs-nav",
                tab = "tabs-tab"
            ),
            styles = TabsStyles(
                root = Modifier,
                nav = Modifier,
                tab = Modifier
            ),
            modifier = Modifier
        )
    }
}

// ============================================
// 4. PAGINATION - ALL PARAMETERS
// ============================================

val PaginationComplete by story {
    var current by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(10) }

    val total by parameter(100)
    val showSizeChanger by parameter(true)
    val showQuickJumper by parameter(true)
    val showTotal by parameter(true)
    val disabled by parameter(false)
    val simple by parameter(false)
    val size by parameter(PaginationSize.Default)
    val align by parameter(PaginationAlign.Start)
    val showLessItems by parameter(false)
    val hideOnSinglePage by parameter(false)
    val responsive by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Pagination with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Current: $current/$total, PageSize: $pageSize")
        Text("Size: $size, Align: $align, Simple: $simple")

        AntPagination(
            current = current,
            total = total,
            pageSize = pageSize,
            onChange = { page, size ->
                current = page
                pageSize = size
            },
            onShowSizeChange = { curr, size ->
                println("Size changed: $size")
            },
            defaultCurrent = 1,
            defaultPageSize = 10,
            disabled = disabled,
            hideOnSinglePage = hideOnSinglePage,
            showSizeChanger = showSizeChanger,
            showQuickJumper = if (showQuickJumper) {
                QuickJumperConfig(
                    goButton = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF1890FF), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("GO", color = Color.White, fontSize = 12.sp)
                        }
                    }
                )
            } else {
                false
            },
            showTotal = if (showTotal) {
                { total, range ->
                    "Showing ${range.first}-${range.last} of $total items"
                }
            } else null,
            showTitle = true,
            simple = simple,
            size = size,
            pageSizeOptions = listOf(10, 20, 50, 100),
            align = align,
            responsive = responsive,
            showLessItems = showLessItems,
            totalBoundaryShowSizeChanger = 50,
            locale = PaginationLocale(
                items_per_page = "/ page",
                jump_to = "Go to",
                jump_to_confirm = "confirm",
                page = "",
                prev_page = "Previous Page",
                next_page = "Next Page",
                prev_5 = "Previous 5 Pages",
                next_5 = "Next 5 Pages",
                prev_3 = "Previous 3 Pages",
                next_3 = "Next 3 Pages"
            ),
            itemRender = null,
            prevIcon = { Text("<", fontSize = 16.sp) },
            nextIcon = { Text(">", fontSize = 16.sp) },
            jumpPrevIcon = { Text("«", fontSize = 14.sp) },
            jumpNextIcon = { Text("»", fontSize = 14.sp) },
            selectPrefixCls = null,
            prefixCls = "ant-pagination",
            rootClassName = null,
            classNames = PaginationClassNames(
                root = "pagination-root",
                item = "pagination-item"
            ),
            styles = PaginationStyles(
                root = Modifier,
                item = Modifier
            ),
            modifier = Modifier
        )
    }
}

// ============================================
// 5. STEPS - ALL PARAMETERS
// ============================================

val StepsComplete by story {
    val current by parameter(1)
    val direction by parameter(StepsDirection.Horizontal)
    val type by parameter(StepsType.Default)
    val size by parameter(StepsSize.Default)
    val labelPlacement by parameter(StepsLabelPlacement.Horizontal)
    val progressDot by parameter(false)
    val status by parameter(StepStatus.Process)
    val percent by parameter<Float?>(null)

    val items = remember {
        listOf(
            StepItem(
                title = "Finished",
                description = "This is a description",
                subTitle = "00:00:08",
                icon = null,
                status = null,
                disabled = false
            ),
            StepItem(
                title = "In Progress",
                description = "This is a description",
                subTitle = "00:01:02",
                icon = null,
                status = null,
                disabled = false
            ),
            StepItem(
                title = "Waiting",
                description = "This is a description",
                subTitle = null,
                icon = null,
                status = null,
                disabled = false
            ),
            StepItem(
                title = "Disabled",
                description = "This step is disabled",
                subTitle = null,
                icon = null,
                status = StepStatus.Wait,
                disabled = true
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Steps with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Current: $current, Direction: $direction, Type: $type")
        Text("Size: $size, Status: $status, ProgressDot: $progressDot")

        AntSteps(
            items = items,
            modifier = Modifier,
            current = current,
            direction = direction,
            type = type,
            size = size,
            labelPlacement = labelPlacement,
            progressDot = progressDot,
            status = status,
            initial = 0,
            percent = percent,
            onChange = { index -> println("Step clicked: $index") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Navigation Type:")
        AntSteps(
            items = items.take(3),
            current = current,
            type = StepsType.Navigation,
            onChange = { println("Navigation step: $it") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Inline Type:")
        AntSteps(
            items = items.take(3),
            current = current,
            type = StepsType.Inline,
            onChange = { println("Inline step: $it") }
        )
    }
}

// ============================================
// 6. DROPDOWN - ALL PARAMETERS
// ============================================

val DropdownComplete by story {
    var visible by remember { mutableStateOf(false) }

    val trigger by parameter(DropdownTrigger.Click)
    val placement by parameter(DropdownPlacement.BottomLeft)
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dropdown with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Trigger: $trigger, Placement: $placement, Disabled: $disabled")

        AntDropdown(
            menu = {
                DropdownMenuItem(
                    text = "Menu Item 1",
                    onClick = { println("Item 1 clicked"); visible = false },
                    disabled = false,
                    icon = { Text("📄", fontSize = 14.sp) }
                )
                DropdownMenuItem(
                    text = "Menu Item 2",
                    onClick = { println("Item 2 clicked"); visible = false },
                    disabled = false,
                    icon = { Text("📋", fontSize = 14.sp) }
                )
                DropdownMenuItem(
                    text = "Disabled Item",
                    onClick = {},
                    disabled = true,
                    icon = { Text("🔒", fontSize = 14.sp) }
                )
            },
            modifier = Modifier,
            trigger = trigger,
            placement = placement,
            disabled = disabled,
            visible = visible,
            onVisibleChange = { visible = it }
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1890FF), RoundedCornerShape(4.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Dropdown Menu", color = Color.White)
            }
        }
    }
}

// ============================================
// 7. PAGEHEADER - ALL PARAMETERS
// ============================================

val PageHeaderComplete by story {
    val ghost by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PageHeader with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Ghost mode: $ghost")

        AntPageHeader(
            avatar = AvatarProps(
                icon = { Text("U", fontSize = 18.sp) },
                size = AvatarSize.Large,
                shape = AvatarShape.Circle,
                src = null,
                alt = "User Avatar"
            ),
            backIcon = { Text("←", fontSize = 20.sp, color = Color(0xFF1890FF)) },
            breadcrumb = {
                AntBreadcrumb(
                    items = listOf(
                        BreadcrumbItem(title = "Home", onClick = {}),
                        BreadcrumbItem(title = "List", onClick = {}),
                        BreadcrumbItem(title = "Detail")
                    ),
                    separator = ">"
                )
            },
            breadcrumbRender = null,
            extra = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(
                        onClick = {},
                        type = ButtonType.Default
                    ) {
                        Text("Action 1")
                    }
                    AntButton(
                        onClick = {},
                        type = ButtonType.Primary
                    ) {
                        Text("Action 2")
                    }
                }
            },
            footer = {
                AntTabs(
                    items = listOf(
                        TabItem(key = "details", label = "Details", content = {}),
                        TabItem(key = "history", label = "History", content = {})
                    ),
                    activeKey = "details",
                    onChange = {}
                )
            },
            ghost = ghost,
            subTitle = "This is a subtitle with detailed information",
            subTitleComposable = null,
            tags = {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    AntTag(children = { Text("Active") }, color = TagColor.Success)
                    AntTag(children = { Text("Premium") }, color = TagColor.Gold)
                }
            },
            title = "Complete Page Header",
            titleComposable = null,
            onBack = { println("Back clicked") },
            className = null,
            style = Modifier,
            modifier = Modifier
        )
    }
}

// ============================================
// 8. ANCHOR - ALL PARAMETERS
// ============================================

val AnchorComplete by story {
    val affix by parameter(true)
    val bounds by parameter(5)
    val showInkInFixed by parameter(false)
    val direction by parameter(AnchorDirection.Vertical)

    val items = remember {
        listOf(
            AnchorItem(
                key = "intro",
                href = "#intro",
                title = "Introduction",
                children = null
            ),
            AnchorItem(
                key = "features",
                href = "#features",
                title = "Features",
                children = listOf(
                    AnchorItem(
                        key = "feature-1",
                        href = "#feature-1",
                        title = "Feature 1"
                    ),
                    AnchorItem(
                        key = "feature-2",
                        href = "#feature-2",
                        title = "Feature 2"
                    )
                )
            ),
            AnchorItem(
                key = "api",
                href = "#api",
                title = "API Reference"
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Anchor with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("Affix: $affix, Bounds: ${bounds}dp, Direction: $direction")

        AntAnchor(
            items = items,
            modifier = Modifier,
            affix = affix,
            bounds = bounds.dp,
            getCurrentAnchor = null,
            offsetTop = 16.dp,
            showInkInFixed = showInkInFixed,
            targetOffset = 80.dp,
            onChange = { href -> println("Active changed to: $href") },
            onClick = { href -> println("Clicked: $href") },
            replace = false,
            direction = direction,
            classNames = AnchorClassNames(
                root = "anchor-root",
                link = "anchor-link",
                linkTitle = "anchor-title",
                linkActive = "anchor-active"
            ),
            styles = AnchorStyles(
                root = Modifier,
                link = Modifier,
                linkTitle = Modifier,
                linkActive = Modifier
            ),
            children = null
        )
    }
}

// ============================================
// 9. AFFIX - ALL PARAMETERS
// ============================================

val AffixComplete by story {
    val offsetTop by parameter<Int?>(0)
    val offsetBottom by parameter<Int?>(null)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Affix with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text("OffsetTop: ${offsetTop?.let { "${it}dp" } ?: "null"}")
        Text("OffsetBottom: ${offsetBottom?.let { "${it}dp" } ?: "null"}")

        // Content before affix
        repeat(5) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text("Content block $index")
            }
        }

        // Affixed component
        AntAffixWithScroll(
            scrollState = scrollState,
            modifier = Modifier,
            offsetTop = offsetTop?.dp,
            offsetBottom = offsetBottom?.dp,
            onChange = { affixed ->
                println("Affix state changed: $affixed")
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1890FF), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    "Affixed Element (scroll to see effect)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Content after affix
        repeat(10) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFE6F7FF), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text("Content block ${index + 5}")
            }
        }
    }
}

// ============================================
// 10. BACKTOP - ALL PARAMETERS
// ============================================

val BackTopComplete by story {
    val visibilityHeight by parameter(400)
    val duration by parameter(450)

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("BackTop with ALL parameters:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Text("VisibilityHeight: ${visibilityHeight}dp, Duration: ${duration}ms")
            Text("Scroll down to see the BackTop button appear")

            // Generate content to enable scrolling
            repeat(50) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            if (index % 2 == 0) Color(0xFFF5F5F5) else Color(0xFFE6F7FF),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text("Content block $index - Scroll down to test BackTop")
                }
            }
        }

        // BackTop with ALL parameters
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            AntBackTop(
                modifier = Modifier,
                visibilityHeight = visibilityHeight.dp,
                onClick = {
                    println("BackTop clicked")
                },
                target = null,
                duration = duration,
                children = null // Uses default circular button
            )
        }

        // BackTop with custom children
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 110.dp, bottom = 50.dp)
        ) {
            AntBackTop(
                visibilityHeight = visibilityHeight.dp,
                onClick = { println("Custom BackTop clicked") },
                duration = duration,
                children = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF52C41A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("UP", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
