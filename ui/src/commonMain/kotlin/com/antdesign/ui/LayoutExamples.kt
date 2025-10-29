package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antdesign.ui.icons.MenuIcon
import com.antdesign.ui.icons.RightArrowIcon

/**
 * Example 1: Basic Layout - Header + Content + Footer
 * Simple vertical layout without sidebar.
 */
@Composable
fun BasicLayoutExample() {
    AntConfigProvider {
        AntLayout {
            AntLayoutHeader {
                Text(
                    text = "Header",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayoutContent {
                Text(
                    text = "Content Area",
                    fontSize = 16.sp
                )
            }

            AntLayoutFooter {
                Text(
                    text = "Footer - Ant Design KMP ©2025",
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * Example 2: Layout with Sider
 * Header + Sidebar + Content layout.
 */
@Composable
fun LayoutWithSiderExample() {
    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "My Application",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 200.dp,
                    theme = SiderTheme.Dark
                ) {
                    // Sidebar content (typically a Menu component would go here)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Menu Item 1", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Menu Item 2", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Menu Item 3", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Menu Item 4", color = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent {
                        Text(
                            text = "Main Content Area",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 3: Complete Layout with Collapsible Sider
 * Full webapp layout: Header + Collapsible Sidebar + Content + Footer
 */
@Composable
fun CompleteLayoutExample() {
    var collapsed by remember { mutableStateOf(false) }

    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "Dashboard Application",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true, modifier = Modifier.weight(1f)) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsedWidth = 80.dp,
                    collapsed = collapsed,
                    collapsible = true,
                    theme = SiderTheme.Dark,
                    onCollapse = { isCollapsed, type ->
                        collapsed = isCollapsed
                        println("Sider collapsed: $isCollapsed, type: $type")
                    }
                ) {
                    // Sidebar menu
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (!collapsed) {
                            Text("Dashboard", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("Users", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("Settings", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("Reports", color = Color.White, modifier = Modifier.padding(8.dp))
                        } else {
                            // Show icons only when collapsed
                            Text("D", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("U", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("S", color = Color.White, modifier = Modifier.padding(8.dp))
                            Text("R", color = Color.White, modifier = Modifier.padding(8.dp))
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent {
                        Column {
                            Text(
                                text = "Welcome to Dashboard",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "This is a complete layout example with collapsible sidebar. " +
                                        "Click the collapse button at the bottom of the sidebar to toggle.",
                                fontSize = 16.sp
                            )
                        }
                    }

                    AntLayoutFooter {
                        Text(
                            text = "Ant Design KMP ©2025 Created by Community",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 4: Light Theme Sider
 * Layout with light-themed sidebar.
 */
@Composable
fun LightThemeSiderExample() {
    var collapsed by remember { mutableStateOf(false) }

    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "Light Theme App",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsedWidth = 80.dp,
                    collapsed = collapsed,
                    collapsible = true,
                    theme = SiderTheme.Light,
                    onCollapse = { isCollapsed, _ ->
                        collapsed = isCollapsed
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Home", modifier = Modifier.padding(8.dp))
                        Text("Products", modifier = Modifier.padding(8.dp))
                        Text("About", modifier = Modifier.padding(8.dp))
                        Text("Contact", modifier = Modifier.padding(8.dp))
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent(
                        style = ContentStyle(backgroundColor = Color.White)
                    ) {
                        Text(
                            text = "Content with Light Sidebar Theme",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 5: Responsive Layout with Breakpoint
 * Sidebar automatically collapses on small screens.
 */
@Composable
fun ResponsiveLayoutExample() {
    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "Responsive Layout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsedWidth = 0.dp, // Collapse to zero width
                    defaultCollapsed = false,
                    collapsible = true,
                    theme = SiderTheme.Dark,
                    breakpoint = BreakpointType.LG,
                    onBreakpoint = { broken ->
                        println("Breakpoint reached, below LG: $broken")
                    },
                    onCollapse = { isCollapsed, type ->
                        println("Collapsed: $isCollapsed, Type: $type")
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Nav 1", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Nav 2", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Nav 3", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Nav 4", color = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent {
                        Text(
                            text = "This layout adapts to screen size. " +
                                    "The sidebar collapses automatically on screens below LG (992dp).",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 6: Nested Layouts
 * Complex nested layout structure.
 */
@Composable
fun NestedLayoutExample() {
    AntConfigProvider {
        AntLayout {
            AntLayoutHeader {
                Text(
                    text = "Nested Layout Example",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true, modifier = Modifier.fillMaxHeight()) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsible = true,
                    theme = SiderTheme.Dark
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Menu 1", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Menu 2", color = Color.White, modifier = Modifier.padding(8.dp))
                        Text("Menu 3", color = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Nested sub-header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color(0xFFF0F2F5))
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sub-Header / Breadcrumb")
                    }

                    AntLayoutContent {
                        Text(
                            text = "Nested Layout Content",
                            fontSize = 16.sp
                        )
                    }

                    AntLayoutFooter {
                        Text("Inner Footer")
                    }
                }
            }

            AntLayoutFooter {
                Text("Outer Footer - Copyright 2025")
            }
        }
    }
}

/**
 * Example 7: Custom Styled Layout
 * Layout with custom colors and styling.
 */
@Composable
fun CustomStyledLayoutExample() {
    var collapsed by remember { mutableStateOf(false) }

    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader(
                style = HeaderStyle(
                    backgroundColor = Color(0xFF6200EA),
                    height = 72.dp
                )
            ) {
                Text(
                    text = "Custom Styled App",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 250.dp,
                    collapsedWidth = 60.dp,
                    collapsed = collapsed,
                    collapsible = true,
                    theme = SiderTheme.Light,
                    onCollapse = { isCollapsed, _ ->
                        collapsed = isCollapsed
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (!collapsed) {
                            Text("Dashboard", modifier = Modifier.padding(8.dp))
                            Text("Analytics", modifier = Modifier.padding(8.dp))
                            Text("Reports", modifier = Modifier.padding(8.dp))
                        } else {
                            Text("D", modifier = Modifier.padding(8.dp))
                            Text("A", modifier = Modifier.padding(8.dp))
                            Text("R", modifier = Modifier.padding(8.dp))
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent(
                        style = ContentStyle(
                            backgroundColor = Color(0xFFF5F5F5),
                            padding = 32.dp
                        )
                    ) {
                        Column {
                            Text(
                                text = "Custom Styled Content",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EA)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This layout uses custom colors and dimensions.",
                                fontSize = 16.sp
                            )
                        }
                    }

                    AntLayoutFooter(
                        style = FooterStyle(
                            backgroundColor = Color(0xFF6200EA),
                            padding = 16.dp
                        )
                    ) {
                        Text(
                            text = "Custom Footer",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 8: Fixed Sider with Scrollable Content
 * Sider stays fixed while content scrolls.
 */
@Composable
fun FixedSiderScrollableContentExample() {
    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "Fixed Sider App",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsible = true,
                    theme = SiderTheme.Dark
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        repeat(20) { index ->
                            Text(
                                "Menu Item ${index + 1}",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent(scrollable = true) {
                        Column {
                            repeat(50) { index ->
                                Text(
                                    text = "Content Line ${index + 1}",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Example 9: Custom Trigger - Trigger in Header
 * Sider with trigger=null and custom collapse button in header.
 * Mimics the custom-trigger demo from Ant Design React.
 */
@Composable
fun CustomTriggerExample() {
    var collapsed by remember { mutableStateOf(false) }

    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayout(hasSider = true) {
                // Sider with trigger=null to hide default trigger
                AntLayoutSider(
                    width = 200.dp,
                    collapsedWidth = 80.dp,
                    collapsed = collapsed,
                    trigger = null, // Hide default trigger
                    collapsible = true,
                    theme = SiderTheme.Dark
                ) {
                    // Logo area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(Color(0xFF002140)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (collapsed) "L" else "LOGO",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Menu items
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (collapsed) "U" else "User",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (collapsed) "V" else "Video",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (collapsed) "U" else "Upload",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    // Header with custom trigger button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(Color.White)
                            .padding(horizontal = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom trigger button
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clickable { collapsed = !collapsed },
                            contentAlignment = Alignment.Center
                        ) {
                            if (collapsed) {
                                RightArrowIcon(
                                    size = 24.dp,
                                    color = Color.Black
                                )
                            } else {
                                MenuIcon(
                                    size = 24.dp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    AntLayoutContent(
                        style = ContentStyle(
                            backgroundColor = Color.White,
                            padding = 24.dp
                        )
                    ) {
                        Text(
                            text = "Content - Custom trigger in header example",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example 10: Sider with Custom Trigger Component
 * Provides a custom composable as trigger.
 */
@Composable
fun CustomTriggerComponentExample() {
    var collapsed by remember { mutableStateOf(false) }

    AntConfigProvider {
        Column(modifier = Modifier.fillMaxSize()) {
            AntLayoutHeader {
                Text(
                    text = "Custom Trigger Component",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AntLayout(hasSider = true) {
                AntLayoutSider(
                    width = 200.dp,
                    collapsedWidth = 80.dp,
                    collapsed = collapsed,
                    collapsible = true,
                    theme = SiderTheme.Dark,
                    onCollapse = { isCollapsed, _ ->
                        collapsed = isCollapsed
                    },
                    // Custom trigger composable
                    trigger = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (collapsed) ">>>" else "<<<",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (collapsed) "H" else "Home",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (collapsed) "P" else "Products",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (collapsed) "A" else "About",
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AntLayoutContent {
                        Text(
                            text = "Content with custom trigger component (arrows)",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
