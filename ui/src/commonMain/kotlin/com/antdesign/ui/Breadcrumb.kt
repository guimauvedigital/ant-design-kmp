package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Semantic styling support for Breadcrumb component (v5.4.0+)
 * Allows fine-grained control over different parts of the breadcrumb.
 *
 * @param item CSS class name for breadcrumb item wrapper
 * @param link CSS class name for breadcrumb link elements
 * @param separator CSS class name for separator elements
 */
data class BreadcrumbClassNames(
    val item: String? = null,
    val link: String? = null,
    val separator: String? = null
)

/**
 * Semantic styling support for Breadcrumb component (v5.4.0+)
 * Allows fine-grained Compose modifier styling for different parts of the breadcrumb.
 *
 * @param item Modifier for breadcrumb item wrapper
 * @param link Modifier for breadcrumb link elements
 * @param separator Modifier for separator elements
 */
data class BreadcrumbStyles(
    val item: Modifier? = null,
    val link: Modifier? = null,
    val separator: Modifier? = null
)

/**
 * Configuration properties for dropdown menus in breadcrumb items.
 * Controls the behavior and appearance of dropdown overlays.
 *
 * Note: Uses DropdownPlacement and DropdownTrigger enums from Dropdown component.
 *
 * @param placement Position of the dropdown menu relative to the breadcrumb item
 * @param trigger Interaction type that triggers the dropdown menu
 * @param arrow Whether to show an arrow pointing to the breadcrumb item
 * @param disabled Whether the dropdown interaction is disabled
 * @param overlayClassName Custom CSS class name for the dropdown overlay
 * @param overlayStyle Custom Compose modifier for styling the dropdown overlay
 */
data class DropdownProps(
    val placement: DropdownPlacement = DropdownPlacement.BottomLeft,
    val trigger: DropdownTrigger = DropdownTrigger.Hover,
    val arrow: Boolean = false,
    val disabled: Boolean = false,
    val overlayClassName: String? = null,
    val overlayStyle: Modifier? = null
)

/**
 * Breadcrumb item configuration.
 * Represents a single item in the breadcrumb navigation trail.
 *
 * @param title Display text for the breadcrumb item
 * @param href Direct link URL for the item (takes precedence over path)
 * @param path Path segment for route concatenation (used with params for dynamic routes)
 * @param icon Optional icon composable to display before the title
 * @param menu Optional dropdown menu configuration for this item
 * @param onClick Click handler for the breadcrumb item
 * @param className Optional CSS class name for custom styling
 * @param params URL path parameters for dynamic route generation (e.g., mapOf("id" to "123"))
 * @param dropdownProps Configuration for dropdown menu behavior and appearance (when menu is present)
 * @param key Unique identifier for React list reconciliation (useful for dynamic lists)
 * @param breadcrumbName Deprecated: Use title instead
 * @param overlay Deprecated: Use menu instead
 * @param children Nested breadcrumb items for hierarchical navigation
 * @param dataAttributes Custom data-* attributes for the breadcrumb item element
 */
data class BreadcrumbItem(
    val title: String,
    val href: String? = null,
    val path: String? = null,
    val icon: (@Composable () -> Unit)? = null,
    val menu: BreadcrumbMenu? = null,
    val onClick: (() -> Unit)? = null,
    val className: String? = null,
    val params: Map<String, String>? = null,
    val dropdownProps: DropdownProps? = null,
    val key: String? = null,
    @Deprecated("Use title instead", ReplaceWith("title"))
    val breadcrumbName: String? = null,
    @Deprecated("Use menu instead", ReplaceWith("menu"))
    val overlay: (@Composable () -> Unit)? = null,
    val children: List<BreadcrumbItem>? = null,
    val dataAttributes: Map<String, String>? = null
)

data class BreadcrumbMenu(
    val items: List<BreadcrumbMenuItem>
)

data class BreadcrumbMenuItem(
    val key: String,
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)

/**
 * Separator type for custom separators (legacy)
 */
data class BreadcrumbSeparator(
    val content: @Composable () -> Unit
)

/**
 * Separator type for items array (v5.3.0+)
 * Used to insert custom separators between specific breadcrumb items.
 *
 * @param type Must be "separator" to identify this as a separator
 * @param separator Custom separator content (default is "/")
 */
data class BreadcrumbSeparatorType(
    val type: String = "separator",
    val separator: (@Composable () -> Unit)? = null
)

/**
 * Route-based breadcrumb item (legacy, deprecated in v5.3.0+)
 * @deprecated Use items prop with BreadcrumbItem instead
 */
@Deprecated("Use items prop with BreadcrumbItem instead", ReplaceWith("BreadcrumbItem"))
data class BreadcrumbRoute(
    val path: String,
    val breadcrumbName: String,
    val children: List<BreadcrumbRoute>? = null
)

/**
 * Substitutes path parameters in a route path.
 * Replaces :paramName with actual values from the params map.
 *
 * Example: path="/user/:id/:action" with params={"id": "123", "action": "edit"} → "/user/123/edit"
 *
 * @param params Map of parameter names to values
 * @param path Route path with :paramName placeholders
 * @return Path with parameters substituted, or null if path is null
 */
private fun getPath(params: Map<String, String>?, path: String?): String? {
    if (path == null) return null
    if (params == null || params.isEmpty()) return path

    var mergedPath = path.removePrefix("/")
    params.forEach { (key, value) ->
        mergedPath = mergedPath.replace(":$key", value)
    }
    return mergedPath
}

/**
 * Main Breadcrumb component for Ant Design KMP
 *
 * Displays a breadcrumb navigation with configurable items, separators, and dropdown menus.
 *
 * Features:
 * - Clickable items with href or onClick
 * - Last item is non-clickable (current page)
 * - Custom separator support (string or composable)
 * - Dropdown menu support for items
 * - Icon support for items
 * - Hover states
 * - Responsive design (mobile optimization)
 * - ConfigProvider integration for theming
 * - Semantic styling support (classNames, styles) - v5.4.0+
 *
 * @param items List of breadcrumb items to display
 * @param modifier Modifier for the breadcrumb container
 * @param separator Custom separator (string or composable). Default is "/"
 * @param maxItems Maximum number of items to show on mobile. Default shows all items
 * @param itemRender Custom renderer for items. Signature: (item, params, items, paths) -> Unit
 * @param params Routing parameters to pass to all items and itemRender
 * @param prefixCls Custom CSS prefix class for the breadcrumb
 * @param className CSS class name for the breadcrumb container
 * @param rootClassName Root CSS class name for the breadcrumb wrapper
 * @param style Custom Compose modifier for inline styling
 * @param classNames Semantic class names for different parts (v5.4.0+)
 * @param styles Semantic Compose modifiers for different parts (v5.4.0+)
 * @param routes Deprecated: Use items instead
 */
@Composable
fun AntBreadcrumb(
    items: List<BreadcrumbItem>,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = { Text("/") },
    maxItems: Int? = null,
    itemRender: (@Composable (
        item: BreadcrumbItem,
        params: Map<String, String>?,
        items: List<BreadcrumbItem>,
        paths: List<String>
    ) -> Unit)? = null,
    params: Map<String, String>? = null,
    prefixCls: String? = null,
    className: String? = null,
    rootClassName: String? = null,
    style: Modifier? = null,
    classNames: BreadcrumbClassNames? = null,
    styles: BreadcrumbStyles? = null
) {
    val theme = useTheme()
    val config = useConfig()

    // Determine if we should show collapsed view (responsive)
    val density = LocalDensity.current
    val windowWidth = remember { mutableStateOf(1200.dp) } // Default to desktop width

    // Calculate which items to show based on maxItems or responsive behavior
    val displayItems = remember(items, maxItems, windowWidth.value) {
        if (maxItems != null && items.size > maxItems) {
            // Show first item, ellipsis, and last (maxItems-1) items
            val lastItems = items.takeLast(maxItems - 1)
            listOf(items.first()) + lastItems
        } else {
            items
        }
    }

    // Build paths array for itemRender (accumulates paths from all items)
    val paths = remember(displayItems, params) {
        val pathsList = mutableListOf<String>()
        displayItems.forEach { item ->
            val itemPath = getPath(params ?: item.params, item.path) ?: item.href ?: ""
            pathsList.add(itemPath)
        }
        pathsList.toList()
    }

    // Combine modifiers: base + style + rootClassName
    val combinedModifier = modifier
        .then(style ?: Modifier)

    Row(
        modifier = combinedModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        displayItems.forEachIndexed { index, item ->
            // Show separator before item (except for first item)
            if (index > 0) {
                BreadcrumbSeparatorItem(
                    separator = separator,
                    theme = theme,
                    classNames = classNames,
                    styles = styles
                )
            }

            // Show ellipsis if we're skipping items
            if (maxItems != null && index == 1 && items.size > maxItems) {
                BreadcrumbEllipsisItem(theme = theme)
                BreadcrumbSeparatorItem(
                    separator = separator,
                    theme = theme,
                    classNames = classNames,
                    styles = styles
                )
            }

            val isLast = index == displayItems.lastIndex

            // Use custom renderer if provided, otherwise use default
            if (itemRender != null) {
                itemRender(item, params, displayItems, paths)
            } else {
                BreadcrumbItemContent(
                    item = item,
                    isLast = isLast,
                    theme = theme,
                    classNames = classNames,
                    styles = styles
                )
            }
        }
    }
}

/**
 * Overload for string separator with simplified itemRender
 */
@Composable
fun AntBreadcrumb(
    items: List<BreadcrumbItem>,
    modifier: Modifier = Modifier,
    separator: String = "/",
    maxItems: Int? = null,
    params: Map<String, String>? = null,
    prefixCls: String? = null,
    className: String? = null,
    rootClassName: String? = null,
    style: Modifier? = null,
    classNames: BreadcrumbClassNames? = null,
    styles: BreadcrumbStyles? = null,
    itemRender: (@Composable (
        item: BreadcrumbItem,
        params: Map<String, String>?,
        items: List<BreadcrumbItem>,
        paths: List<String>
    ) -> Unit)? = null
) {
    AntBreadcrumb(
        items = items,
        modifier = modifier,
        separator = { Text(separator) },
        maxItems = maxItems,
        itemRender = itemRender,
        params = params,
        prefixCls = prefixCls,
        className = className,
        rootClassName = rootClassName,
        style = style,
        classNames = classNames,
        styles = styles
    )
}

/**
 * Internal composable for rendering a breadcrumb item
 */
@Composable
private fun BreadcrumbItemContent(
    item: BreadcrumbItem,
    isLast: Boolean,
    theme: AntThemeConfig,
    classNames: BreadcrumbClassNames? = null,
    styles: BreadcrumbStyles? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Determine if item is clickable
    val isClickable = !isLast && (item.onClick != null || item.href != null)

    // Colors based on state
    val textColor = when {
        isLast -> theme.token.colorTextBase.copy(alpha = 0.85f)
        isHovered && isClickable -> theme.token.colorPrimary
        isClickable -> theme.token.colorPrimary.copy(alpha = 0.85f)
        else -> theme.token.colorTextBase.copy(alpha = 0.45f)
    }

    // Apply semantic styling
    val itemModifier = Modifier
        .then(styles?.item ?: Modifier)

    val linkModifier = Modifier
        .then(styles?.link ?: Modifier)

    // If item has a menu, show dropdown
    if (item.menu != null && !isLast) {
        BreadcrumbItemWithMenu(
            item = item,
            textColor = textColor,
            theme = theme,
            interactionSource = interactionSource,
            itemModifier = itemModifier,
            linkModifier = linkModifier
        )
    } else {
        // Regular item
        Row(
            modifier = itemModifier
                .hoverable(interactionSource = interactionSource)
                .then(
                    if (isClickable) {
                        linkModifier.clickable { item.onClick?.invoke() }
                    } else {
                        Modifier
                    }
                )
                .padding(vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Icon if provided
            item.icon?.invoke()

            // Title
            Text(
                text = item.title,
                style = TextStyle(
                    color = textColor,
                    fontSize = theme.token.fontSize.sp,
                    lineHeight = (theme.token.fontSize * theme.token.lineHeight).sp
                )
            )
        }
    }
}

/**
 * Breadcrumb item with dropdown menu.
 * Applies dropdownProps configuration to control dropdown behavior.
 */
@Composable
private fun BreadcrumbItemWithMenu(
    item: BreadcrumbItem,
    textColor: Color,
    theme: AntThemeConfig,
    interactionSource: MutableInteractionSource,
    itemModifier: Modifier = Modifier,
    linkModifier: Modifier = Modifier
) {
    // Get dropdown configuration or use defaults
    val dropdownConfig = item.dropdownProps ?: DropdownProps()

    // Skip dropdown if disabled
    if (dropdownConfig.disabled) {
        // Show item without dropdown functionality
        Row(
            modifier = itemModifier
                .hoverable(interactionSource = interactionSource)
                .padding(vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item.icon?.invoke()
            Text(
                text = item.title,
                style = TextStyle(
                    color = textColor,
                    fontSize = theme.token.fontSize.sp,
                    lineHeight = (theme.token.fontSize * theme.token.lineHeight).sp
                )
            )
        }
        return
    }

    var expanded by remember { mutableStateOf(false) }

    // Determine trigger behavior
    val triggerModifier = when (dropdownConfig.trigger) {
        DropdownTrigger.Click -> Modifier.clickable { expanded = !expanded }
        DropdownTrigger.Hover -> Modifier
            .hoverable(interactionSource = interactionSource)
            .clickable { expanded = !expanded }
        DropdownTrigger.ContextMenu -> Modifier // Context menu handling would require platform-specific implementation
    }

    Box {
        Row(
            modifier = itemModifier
                .then(linkModifier)
                .hoverable(interactionSource = interactionSource)
                .then(triggerModifier)
                .padding(vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Icon if provided
            item.icon?.invoke()

            // Title
            Text(
                text = item.title,
                style = TextStyle(
                    color = textColor,
                    fontSize = theme.token.fontSize.sp,
                    lineHeight = (theme.token.fontSize * theme.token.lineHeight).sp
                )
            )

            // Down arrow icon (or custom arrow if specified)
            if (dropdownConfig.arrow || true) { // Always show arrow for breadcrumb menus
                Text(
                    text = "▼",
                    style = TextStyle(
                        color = textColor,
                        fontSize = (theme.token.fontSize - 2).sp
                    )
                )
            }
        }

        // Dropdown menu with custom styling if provided
        val menuModifier = dropdownConfig.overlayStyle ?: Modifier

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = menuModifier
        ) {
            item.menu?.items?.forEach { menuItem ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            menuItem.icon?.invoke()
                            Text(menuItem.label)
                        }
                    },
                    onClick = {
                        menuItem.onClick?.invoke()
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Separator between breadcrumb items
 */
@Composable
private fun BreadcrumbSeparatorItem(
    separator: @Composable () -> Unit,
    theme: AntThemeConfig,
    classNames: BreadcrumbClassNames? = null,
    styles: BreadcrumbStyles? = null
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .then(styles?.separator ?: Modifier),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            androidx.compose.material3.LocalTextStyle provides TextStyle(
                color = theme.token.colorTextBase.copy(alpha = 0.45f),
                fontSize = theme.token.fontSize.sp
            )
        ) {
            separator()
        }
    }
}

/**
 * Ellipsis item for collapsed breadcrumbs
 */
@Composable
private fun BreadcrumbEllipsisItem(
    theme: AntThemeConfig
) {
    Text(
        text = "...",
        style = TextStyle(
            color = theme.token.colorTextBase.copy(alpha = 0.45f),
            fontSize = theme.token.fontSize.sp
        )
    )
}

/**
 * Example: Basic breadcrumb
 */
@Composable
fun BreadcrumbBasicExample() {
    AntBreadcrumb(
        separator = "/",
        items = listOf(
            BreadcrumbItem(title = "Home", onClick = { println("Navigate to Home") }),
            BreadcrumbItem(title = "Application Center", onClick = { println("Navigate to Application Center") }),
            BreadcrumbItem(title = "Application List", onClick = { println("Navigate to Application List") }),
            BreadcrumbItem(title = "An Application")
        )
    )
}

/**
 * Example: Breadcrumb with icons
 */
@Composable
fun BreadcrumbWithIconsExample() {
    AntBreadcrumb(
        separator = "/",
        items = listOf(
            BreadcrumbItem(
                title = "Home",
                icon = { Text("🏠") },
                onClick = { println("Navigate to Home") }
            ),
            BreadcrumbItem(
                title = "User",
                icon = { Text("👤") },
                onClick = { println("Navigate to User") }
            ),
            BreadcrumbItem(
                title = "Profile"
            )
        )
    )
}

/**
 * Example: Breadcrumb with custom separator
 */
@Composable
fun BreadcrumbCustomSeparatorExample() {
    AntBreadcrumb(
        items = listOf(
            BreadcrumbItem(title = "Home", onClick = { println("Navigate to Home") }),
            BreadcrumbItem(title = "Products", onClick = { println("Navigate to Products") }),
            BreadcrumbItem(title = "Electronics", onClick = { println("Navigate to Electronics") }),
            BreadcrumbItem(title = "Laptop")
        ),
        separator = { Text(">") }
    )
}

/**
 * Example: Breadcrumb with dropdown menu
 */
@Composable
fun BreadcrumbWithMenuExample() {
    AntBreadcrumb(
        separator = "/",
        items = listOf(
            BreadcrumbItem(
                title = "Ant Design",
                onClick = { println("Navigate to Ant Design") }
            ),
            BreadcrumbItem(
                title = "Components",
                onClick = { println("Navigate to Components") }
            ),
            BreadcrumbItem(
                title = "Navigation",
                menu = BreadcrumbMenu(
                    items = listOf(
                        BreadcrumbMenuItem(
                            key = "1",
                            label = "Menu",
                            onClick = { println("Navigate to Menu") }
                        ),
                        BreadcrumbMenuItem(
                            key = "2",
                            label = "Dropdown",
                            onClick = { println("Navigate to Dropdown") }
                        ),
                        BreadcrumbMenuItem(
                            key = "3",
                            label = "Pagination",
                            onClick = { println("Navigate to Pagination") }
                        )
                    )
                ),
                onClick = { println("Navigate to Navigation") }
            ),
            BreadcrumbItem(title = "Breadcrumb")
        )
    )
}

/**
 * Example: Responsive breadcrumb (mobile-friendly)
 */
@Composable
fun BreadcrumbResponsiveExample() {
    AntBreadcrumb(
        separator = "/",
        items = listOf(
            BreadcrumbItem(title = "Home", onClick = { println("Navigate to Home") }),
            BreadcrumbItem(title = "Category", onClick = { println("Navigate to Category") }),
            BreadcrumbItem(title = "Subcategory", onClick = { println("Navigate to Subcategory") }),
            BreadcrumbItem(title = "Product Type", onClick = { println("Navigate to Product Type") }),
            BreadcrumbItem(title = "Product")
        ),
        maxItems = 3  // Show first item, ellipsis, and last 2 items on mobile
    )
}

/**
 * Example: All features combined
 */
@Composable
fun BreadcrumbCompleteExample() {
    AntBreadcrumb(
        items = listOf(
            BreadcrumbItem(
                title = "Home",
                icon = { Text("🏠") },
                onClick = { println("Navigate to Home") }
            ),
            BreadcrumbItem(
                title = "Products",
                icon = { Text("📦") },
                onClick = { println("Navigate to Products") }
            ),
            BreadcrumbItem(
                title = "Categories",
                icon = { Text("📁") },
                menu = BreadcrumbMenu(
                    items = listOf(
                        BreadcrumbMenuItem(
                            key = "1",
                            label = "Electronics",
                            onClick = { println("Navigate to Electronics") }
                        ),
                        BreadcrumbMenuItem(
                            key = "2",
                            label = "Clothing",
                            onClick = { println("Navigate to Clothing") }
                        ),
                        BreadcrumbMenuItem(
                            key = "3",
                            label = "Books",
                            onClick = { println("Navigate to Books") }
                        )
                    )
                )
            ),
            BreadcrumbItem(
                title = "Current Item",
                icon = { Text("📄") }
            )
        ),
        separator = "›",
        maxItems = 3
    )
}
