package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story

// ==================== 1. Layout Component ====================
val LayoutCompleteBasic by story {
    val hasSider by parameter<Boolean?>(null)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout - Basic (hasSider = null, auto-detect):")
        AntLayout(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            hasSider = hasSider
        ) {
            AntLayoutHeader {
                Text("Header", color = Color.White)
            }
            AntLayoutContent {
                Text("Content - No Sider")
            }
            AntLayoutFooter {
                Text("Footer")
            }
        }
    }
}

val LayoutCompleteWithSider by story {
    val hasSider by parameter<Boolean?>(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout - With Sider (hasSider = true):")
        AntLayout(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            hasSider = hasSider
        ) {
            AntLayoutSider(
                width = 200.dp,
                collapsedWidth = 80.dp,
                collapsible = true,
                theme = SiderTheme.Dark
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sider Menu", color = Color.White)
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                AntLayoutHeader {
                    Text("Header", color = Color.White)
                }
                AntLayoutContent {
                    Text("Content Area")
                }
                AntLayoutFooter {
                    Text("Footer")
                }
            }
        }
    }
}

val LayoutCompleteAllParams by story {
    val hasSider by parameter<Boolean?>(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout - All Parameters:")
        AntLayout(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            className = "custom-layout",
            rootClassName = "root-layout",
            prefixCls = "ant",
            suffixCls = "custom",
            hasSider = hasSider,
            style = LayoutStyle(
                backgroundColor = Color(0xFFF0F2F5)
            ),
            classNames = LayoutClassNames(
                content = "layout-content"
            ),
            styles = LayoutStyles(
                content = Modifier.padding(4.dp)
            )
        ) {
            AntLayoutSider(
                width = 200.dp,
                collapsible = true
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sider", color = Color.White)
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                AntLayoutHeader {
                    Text("Header", color = Color.White)
                }
                AntLayoutContent {
                    Text("Content with all layout params")
                }
                AntLayoutFooter {
                    Text("Footer")
                }
            }
        }
    }
}

// ==================== 2. LayoutHeader Component ====================
val LayoutHeaderComplete by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutHeader - Basic:")
        AntLayoutHeader {
            Text("Basic Header", color = Color.White)
        }

        Text("LayoutHeader - All Parameters:")
        AntLayoutHeader(
            modifier = Modifier.fillMaxWidth(),
            className = "custom-header",
            rootClassName = "root-header",
            prefixCls = "ant",
            suffixCls = "custom",
            style = HeaderStyle(
                height = 80.dp,
                backgroundColor = Color(0xFF002140),
                paddingInline = 32.dp,
                textColor = Color.White
            ),
            classNames = HeaderClassNames(
                content = "header-content"
            ),
            styles = HeaderStyles(
                content = Modifier.padding(8.dp)
            )
        ) {
            Text("Custom Header with all params", color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text("Menu", color = Color.White)
        }
    }
}

// ==================== 3. LayoutSider Component ====================
val LayoutSiderComplete by story {
    val width by parameter(200)
    val collapsedWidth by parameter(80)
    val collapsed by parameter<Boolean?>(null)
    val defaultCollapsed by parameter(false)
    val collapsible by parameter(true)
    val reverseArrow by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutSider - Basic:")
        Row(modifier = Modifier.height(300.dp)) {
            AntLayoutSider(
                width = width.dp,
                collapsedWidth = collapsedWidth.dp,
                collapsed = collapsed,
                defaultCollapsed = defaultCollapsed,
                collapsible = collapsible
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu Item 1", color = Color.White)
                    Text("Menu Item 2", color = Color.White)
                    Text("Menu Item 3", color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content Area")
            }
        }
    }
}

val LayoutSiderThemes by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutSider - Themes:")

        Text("Dark Theme:")
        Row(modifier = Modifier.height(200.dp)) {
            AntLayoutSider(
                width = 200.dp,
                collapsible = true,
                theme = SiderTheme.Dark
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dark Sider", color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content")
            }
        }

        Text("Light Theme:")
        Row(modifier = Modifier.height(200.dp)) {
            AntLayoutSider(
                width = 200.dp,
                collapsible = true,
                theme = SiderTheme.Light
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Light Sider", color = Color.Black)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content")
            }
        }
    }
}

val LayoutSiderAllParams by story {
    var collapsedState by remember { mutableStateOf(false) }
    var broken by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutSider - All Parameters:")
        Text("Collapsed: $collapsedState, Broken: $broken", fontSize = 12.sp)

        Row(modifier = Modifier.height(400.dp)) {
            AntLayoutSider(
                modifier = Modifier,
                className = "custom-sider",
                rootClassName = "root-sider",
                prefixCls = "ant",
                suffixCls = "custom",
                width = 250.dp,
                collapsedWidth = 80.dp,
                collapsed = collapsedState,
                defaultCollapsed = false,
                onCollapse = { newCollapsed, type ->
                    collapsedState = newCollapsed
                },
                collapsible = true,
                reverseArrow = false,
                trigger = null, // Use default trigger
                theme = SiderTheme.Dark,
                breakpoint = BreakpointType.LG,
                onBreakpoint = { isBroken ->
                    broken = isBroken
                },
                zeroWidthTriggerStyle = null,
                classNames = SiderClassNames(
                    content = "sider-content",
                    trigger = "sider-trigger"
                ),
                styles = SiderStyles(
                    content = Modifier.padding(4.dp),
                    trigger = Modifier
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu Item 1", color = Color.White)
                    Text("Menu Item 2", color = Color.White)
                    Text("Menu Item 3", color = Color.White)
                    Text("Menu Item 4", color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content Area - All Sider Parameters Demonstrated")
            }
        }
    }
}

val LayoutSiderZeroWidth by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutSider - Zero Width (collapsedWidth = 0):")

        Row(modifier = Modifier.height(300.dp)) {
            AntLayoutSider(
                width = 200.dp,
                collapsedWidth = 0.dp,
                defaultCollapsed = true,
                collapsible = true
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Hidden Menu", color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content - Sider with zero-width trigger")
            }
        }
    }
}

val LayoutSiderReverseArrow by story {
    val reverseArrow by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutSider - Reverse Arrow (reverseArrow = $reverseArrow):")

        Row(modifier = Modifier.height(300.dp)) {
            AntLayoutSider(
                width = 200.dp,
                collapsedWidth = 80.dp,
                collapsible = true,
                reverseArrow = reverseArrow
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu", color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5)).padding(16.dp)) {
                Text("Content")
            }
        }
    }
}

// ==================== 4. LayoutContent Component ====================
val LayoutContentComplete by story {
    val scrollable by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutContent - Basic:")
        AntLayoutContent(
            modifier = Modifier.fillMaxWidth().height(150.dp)
        ) {
            Text("Basic Content Area")
        }

        Text("LayoutContent - All Parameters:")
        AntLayoutContent(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            className = "custom-content",
            rootClassName = "root-content",
            prefixCls = "ant",
            suffixCls = "custom",
            scrollable = scrollable,
            style = ContentStyle(
                backgroundColor = Color.White,
                padding = 32.dp
            ),
            classNames = ContentClassNames(
                content = "content-inner"
            ),
            styles = ContentStyles(
                content = Modifier.padding(8.dp)
            )
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Content with all parameters")
                Text("Line 2")
                Text("Line 3")
                Text("Line 4")
                Text("Line 5")
            }
        }
    }
}

val LayoutContentScrollable by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutContent - Scrollable:")
        AntLayoutContent(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            scrollable = true
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(20) { index ->
                    Text("Scrollable Content Line ${index + 1}")
                }
            }
        }
    }
}

// ==================== 5. LayoutFooter Component ====================
val LayoutFooterComplete by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("LayoutFooter - Basic:")
        AntLayoutFooter {
            Text("Basic Footer")
        }

        Text("LayoutFooter - All Parameters:")
        AntLayoutFooter(
            modifier = Modifier.fillMaxWidth(),
            className = "custom-footer",
            rootClassName = "root-footer",
            prefixCls = "ant",
            suffixCls = "custom",
            style = FooterStyle(
                backgroundColor = Color(0xFF001529),
                padding = 32.dp
            ),
            classNames = FooterClassNames(
                content = "footer-content"
            ),
            styles = FooterStyles(
                content = Modifier.padding(8.dp)
            )
        ) {
            Text("Copyright 2024", color = Color.White)
        }
    }
}

// ==================== 6. Grid (Row/Col) Component ====================
val GridComplete by story {
    val gutter by parameter(16)
    val wrap by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - Basic Spans:")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 12) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-12", color = Color.White)
                }
            }
            AntCol(span = 12) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-12", color = Color.White)
                }
            }
        }
    }
}

val GridAllAlignments by story {
    val gutter by parameter(16)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - All Row Alignments:")

        Text("RowAlign.Top:")
        AntRow(gutter = gutter, align = RowAlign.Top) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        Text("RowAlign.Middle:")
        AntRow(gutter = gutter, align = RowAlign.Middle) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        Text("RowAlign.Bottom:")
        AntRow(gutter = gutter, align = RowAlign.Bottom) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        Text("RowAlign.Stretch:")
        AntRow(gutter = gutter, align = RowAlign.Stretch) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("Stretch", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("Stretch", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("Stretch", color = Color.White)
                }
            }
        }
    }
}

val GridAllJustify by story {
    val gutter by parameter(16)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - All Row Justify:")

        Text("RowJustify.Start:")
        AntRow(gutter = gutter, justify = RowJustify.Start) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }

        Text("RowJustify.End:")
        AntRow(gutter = gutter, justify = RowJustify.End) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }

        Text("RowJustify.Center:")
        AntRow(gutter = gutter, justify = RowJustify.Center) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceBetween:")
        AntRow(gutter = gutter, justify = RowJustify.SpaceBetween) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceAround:")
        AntRow(gutter = gutter, justify = RowJustify.SpaceAround) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceEvenly:")
        AntRow(gutter = gutter, justify = RowJustify.SpaceEvenly) {
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("1", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("2", color = Color.White)
                }
            }
            AntCol(span = 4) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("3", color = Color.White)
                }
            }
        }
    }
}

val GridColAllParams by story {
    val gutter by parameter(16)
    val offset by parameter(4)
    val pull by parameter(0)
    val push by parameter(0)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - Col with All Parameters:")

        Text("Offset:")
        AntRow(gutter = gutter) {
            AntCol(span = 8, offset = offset) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("offset=$offset", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("No offset", color = Color.White)
                }
            }
        }

        Text("Order:")
        AntRow(gutter = gutter) {
            AntCol(span = 8, order = 2) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("order=2", color = Color.White)
                }
            }
            AntCol(span = 8, order = 1) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("order=1", color = Color.White)
                }
            }
            AntCol(span = 8, order = 0) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("order=0", color = Color.White)
                }
            }
        }

        Text("Push/Pull:")
        AntRow(gutter = gutter) {
            AntCol(span = 8, push = push) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("push=$push", color = Color.White)
                }
            }
            AntCol(span = 8, pull = pull) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("pull=$pull", color = Color.White)
                }
            }
        }

        Text("Flex:")
        AntRow(gutter = gutter) {
            AntCol(flex = "auto") {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("flex=auto", color = Color.White)
                }
            }
            AntCol(flex = 2) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("flex=2", color = Color.White)
                }
            }
            AntCol(flex = 1) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text("flex=1", color = Color.White)
                }
            }
        }
    }
}

val GridResponsive by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - Responsive (xs, sm, md, lg, xl, xxl):")

        AntRow(gutter = 16) {
            AntCol(
                xs = 24,
                sm = 12,
                md = 8,
                lg = 6,
                xl = 4,
                xxl = 2
            ) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("Responsive Col", color = Color.White, fontSize = 12.sp)
                }
            }
            AntCol(
                xs = 24,
                sm = 12,
                md = 8,
                lg = 6,
                xl = 4,
                xxl = 2
            ) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text("Responsive Col", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Text("Advanced Responsive Config:")
        AntRow(gutter = 16) {
            AntCol(
                md = ColResponsiveConfig(
                    span = 12,
                    offset = 2,
                    order = 1
                ),
                lg = ColResponsiveConfig(
                    span = 8,
                    offset = 0,
                    order = 0
                )
            ) {
                Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text("Advanced Responsive", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

val GridWrap by story {
    val wrap by parameter(true)
    val gutter by parameter(16)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - Wrap (wrap = $wrap):")

        AntRow(gutter = gutter, wrap = wrap) {
            repeat(10) { index ->
                AntCol(span = 6) {
                    Box(Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                        Text("${index + 1}", color = Color.White)
                    }
                }
            }
        }
    }
}

val GridGutterVariants by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grid - Gutter Variants:")

        Text("Gutter: 8")
        AntRow(gutter = 8) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                    Text("8", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(4.dp)) {
                    Text("8", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(4.dp)) {
                    Text("8", color = Color.White)
                }
            }
        }

        Text("Gutter: 16")
        AntRow(gutter = 16) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                    Text("16", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(4.dp)) {
                    Text("16", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(4.dp)) {
                    Text("16", color = Color.White)
                }
            }
        }

        Text("Gutter: 32")
        AntRow(gutter = 32) {
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                    Text("32", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF52C41A)).padding(4.dp)) {
                    Text("32", color = Color.White)
                }
            }
            AntCol(span = 8) {
                Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFFAAD14)).padding(4.dp)) {
                    Text("32", color = Color.White)
                }
            }
        }

        Text("Gutter: Pair(horizontal=16, vertical=24)")
        AntRow(gutter = Pair(16, 24), wrap = true) {
            repeat(6) { index ->
                AntCol(span = 8) {
                    Box(Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                        Text("${index + 1}", color = Color.White)
                    }
                }
            }
        }
    }
}

// ==================== 7. Space Component ====================
val SpaceComplete by story {
    val wrap by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - All Sizes:")

        Text("SpaceSize.Small (8dp):")
        AntSpace(
            size = SpaceSize.Small,
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Start,
            wrap = wrap
        ) {
            AntButton(onClick = {}) { Text("Btn 1") }
            AntButton(onClick = {}) { Text("Btn 2") }
            AntButton(onClick = {}) { Text("Btn 3") }
        }

        Text("SpaceSize.Middle (16dp):")
        AntSpace(
            size = SpaceSize.Middle,
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Start,
            wrap = wrap
        ) {
            AntButton(onClick = {}) { Text("Btn 1") }
            AntButton(onClick = {}) { Text("Btn 2") }
            AntButton(onClick = {}) { Text("Btn 3") }
        }

        Text("SpaceSize.Large (24dp):")
        AntSpace(
            size = SpaceSize.Large,
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Start,
            wrap = wrap
        ) {
            AntButton(onClick = {}) { Text("Btn 1") }
            AntButton(onClick = {}) { Text("Btn 2") }
            AntButton(onClick = {}) { Text("Btn 3") }
        }
    }
}

val SpaceDirections by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - Direction Variants:")

        Text("SpaceDirection.Horizontal:")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            size = SpaceSize.Middle
        ) {
            AntButton(onClick = {}) { Text("Button 1") }
            AntButton(onClick = {}) { Text("Button 2") }
            AntButton(onClick = {}) { Text("Button 3") }
        }

        Text("SpaceDirection.Vertical:")
        AntSpace(
            direction = SpaceDirection.Vertical,
            size = SpaceSize.Middle
        ) {
            AntButton(onClick = {}) { Text("Button 1") }
            AntButton(onClick = {}) { Text("Button 2") }
            AntButton(onClick = {}) { Text("Button 3") }
        }
    }
}

val SpaceAlignments by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - All Alignments (Horizontal):")

        Text("SpaceAlign.Start:")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Start,
            size = SpaceSize.Middle
        ) {
            Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                Text("40dp", color = Color.White)
            }
            Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                Text("60dp", color = Color.White)
            }
            Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                Text("80dp", color = Color.White)
            }
        }

        Text("SpaceAlign.Center:")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Center,
            size = SpaceSize.Middle
        ) {
            Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                Text("40dp", color = Color.White)
            }
            Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                Text("60dp", color = Color.White)
            }
            Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                Text("80dp", color = Color.White)
            }
        }

        Text("SpaceAlign.End:")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.End,
            size = SpaceSize.Middle
        ) {
            Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                Text("40dp", color = Color.White)
            }
            Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                Text("60dp", color = Color.White)
            }
            Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                Text("80dp", color = Color.White)
            }
        }

        Text("SpaceAlign.Baseline:")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Baseline,
            size = SpaceSize.Middle
        ) {
            Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                Text("40dp", color = Color.White)
            }
            Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                Text("60dp", color = Color.White)
            }
            Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                Text("80dp", color = Color.White)
            }
        }
    }
}

val SpaceWrap by story {
    val wrap by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - Wrap (wrap = $wrap):")
        AntSpace(
            direction = SpaceDirection.Horizontal,
            size = SpaceSize.Middle,
            wrap = wrap
        ) {
            repeat(15) { index ->
                AntButton(onClick = {}) {
                    Text("Button ${index + 1}")
                }
            }
        }
    }
}

val SpaceAllParams by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - All Parameters:")
        AntSpace(
            modifier = Modifier.fillMaxWidth(),
            align = SpaceAlign.Center,
            direction = SpaceDirection.Horizontal,
            size = Pair(16, 24), // Custom horizontal/vertical spacing
            split = { Text("|", color = Color.Gray) },
            wrap = true,
            classNames = SpaceClassNames(
                root = "space-root",
                item = "space-item",
                split = "space-split"
            ),
            styles = SpaceStyles(
                root = Modifier.padding(8.dp),
                item = Modifier,
                split = Modifier
            )
        ) {
            AntButton(onClick = {}) { Text("Item 1") }
            AntButton(onClick = {}) { Text("Item 2") }
            AntButton(onClick = {}) { Text("Item 3") }
            AntButton(onClick = {}) { Text("Item 4") }
        }
    }
}

val SpaceCustomSize by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Space - Custom Sizes:")

        Text("Custom: 4.dp")
        AntSpace(size = 4.dp) {
            AntButton(
                onClick = {},
                size = ButtonSize.Small
            ) { Text("Btn 1") }
            AntButton(
                onClick = {},
                size = ButtonSize.Small
            ) { Text("Btn 2") }
            AntButton(
                onClick = {},
                size = ButtonSize.Small
            ) { Text("Btn 3") }
        }

        Text("Custom: 32.dp")
        AntSpace(size = 32.dp) {
            AntButton(onClick = {}) { Text("Btn 1") }
            AntButton(onClick = {}) { Text("Btn 2") }
            AntButton(onClick = {}) { Text("Btn 3") }
        }

        Text("Custom Pair: (8, 16)")
        AntSpace(
            size = Pair(8, 16),
            wrap = true,
            direction = SpaceDirection.Horizontal
        ) {
            repeat(8) { index ->
                AntButton(onClick = {}) { Text("Btn ${index + 1}") }
            }
        }
    }
}

// ==================== 8. Divider Component ====================
val DividerComplete by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - Basic:")
        Text("Content above")
        AntDivider()
        Text("Content below")

        Text("Divider - Dashed:")
        Text("Content above")
        AntDivider(dashed = true)
        Text("Content below")
    }
}

val DividerOrientations by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - Orientations:")

        Text("Horizontal:")
        Text("Content above")
        AntDivider(orientation = DividerOrientation.Horizontal)
        Text("Content below")

        Text("Vertical:")
        Row(modifier = Modifier.height(60.dp)) {
            Text("Left Content")
            AntDivider(
                orientation = DividerOrientation.Vertical,
                orientationMargin = 8.dp
            )
            Text("Right Content")
        }
    }
}

val DividerTypes by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - Types:")

        Text("Solid:")
        AntDivider(type = DividerType.Solid)

        Text("Dashed:")
        AntDivider(type = DividerType.Dashed)
    }
}

val DividerWithText by story {
    val plain by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - With Text (plain = $plain):")

        Text("TextAlign.Left:")
        AntDivider(
            textAlign = DividerTextAlign.Left,
            plain = plain,
            children = {
                Text("Left Text")
            }
        )

        Text("TextAlign.Center:")
        AntDivider(
            textAlign = DividerTextAlign.Center,
            plain = plain,
            children = {
                Text("Center Text")
            }
        )

        Text("TextAlign.Right:")
        AntDivider(
            textAlign = DividerTextAlign.Right,
            plain = plain,
            children = {
                Text("Right Text")
            }
        )
    }
}

val DividerCustomization by story {
    val thickness by parameter(1)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - Customization:")

        Text("Custom Color (Red):")
        AntDivider(color = Color.Red)

        Text("Custom Thickness ($thickness dp):")
        AntDivider(thickness = thickness.dp)

        Text("Custom Color + Dashed:")
        AntDivider(
            color = Color(0xFF52C41A),
            dashed = true,
            thickness = 2.dp
        )
    }
}

val DividerAllParams by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - All Parameters:")

        Text("Horizontal with all params:")
        AntDivider(
            modifier = Modifier.fillMaxWidth(),
            orientation = DividerOrientation.Horizontal,
            dashed = false,
            type = DividerType.Solid,
            plain = false,
            textAlign = DividerTextAlign.Center,
            color = Color(0xFF1890FF),
            thickness = 2.dp,
            children = {
                Text("Custom Divider")
            },
            classNames = DividerClassNames(
                root = "divider-root",
                text = "divider-text",
                line = "divider-line"
            ),
            styles = DividerStyles(
                root = Modifier.padding(vertical = 8.dp),
                text = Modifier.padding(horizontal = 16.dp),
                line = Modifier
            )
        )

        Text("Vertical with all params:")
        Row(modifier = Modifier.height(80.dp)) {
            Text("Left Side")
            AntDivider(
                modifier = Modifier,
                orientation = DividerOrientation.Vertical,
                dashed = true,
                type = DividerType.Dashed,
                plain = true,
                orientationMargin = 16.dp,
                color = Color(0xFF52C41A),
                thickness = 2.dp,
                classNames = DividerClassNames(
                    root = "divider-root",
                    line = "divider-line"
                ),
                styles = DividerStyles(
                    root = Modifier,
                    line = Modifier
                )
            )
            Text("Right Side")
        }
    }
}

val DividerVerticalMargin by story {
    val orientationMargin by parameter(8)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Divider - Vertical with Margin (orientationMargin = ${orientationMargin}dp):")

        Row(modifier = Modifier.height(100.dp)) {
            Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF0F2F5)).padding(8.dp)) {
                Text("Content 1")
            }
            AntDivider(
                orientation = DividerOrientation.Vertical,
                orientationMargin = orientationMargin.dp
            )
            Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF0F2F5)).padding(8.dp)) {
                Text("Content 2")
            }
            AntDivider(
                orientation = DividerOrientation.Vertical,
                orientationMargin = orientationMargin.dp,
                dashed = true
            )
            Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF0F2F5)).padding(8.dp)) {
                Text("Content 3")
            }
        }
    }
}
