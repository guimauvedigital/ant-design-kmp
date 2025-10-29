package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Menu modes following Ant Design specification
 */
enum class MenuMode {
    Vertical,
    Horizontal,
    Inline
}

/**
 * Menu themes
 */
enum class MenuTheme {
    Light,
    Dark
}

/**
 * Menu item type discriminator
 */
enum class MenuItemType {
    Item,
    SubMenu,
    Group,
    Divider
}

/**
 * Which action can trigger submenu open/close
 */
enum class TriggerSubMenuAction {
    Hover,
    Click
}

/**
 * Text direction for menu
 */
enum class MenuDirection {
    Ltr,
    Rtl
}

/**
 * Semantic class names for Menu styling (v5.4.0+)
 * Allows customization of specific DOM elements within the Menu component
 */
data class MenuClassNames(
    val root: String? = null,
    val item: String? = null,
    val itemIcon: String? = null,
    val itemContent: String? = null,
    val popup: String? = null,
)

/**
 * Semantic styles for Menu styling (v5.4.0+)
 * Allows customization of specific DOM elements within the Menu component
 */
data class MenuStyles(
    val root: Modifier? = null,
    val item: Modifier? = null,
    val itemIcon: Modifier? = null,
    val itemContent: Modifier? = null,
    val popup: Modifier? = null,
    val itemTextColor: Color? = null,
    val itemSelectedTextColor: Color? = null,
    val itemBackgroundColor: Color? = null,
    val itemSelectedBackgroundColor: Color? = null,
)

/**
 * Animation motion configuration for Menu
 * Allows customization of expand/collapse animations
 */
data class MenuMotion(
    val durationMillis: Int = 300,
    val easing: Easing = FastOutSlowInEasing,
)

/**
 * Motion configuration per menu mode
 */
data class MenuDefaultMotions(
    val vertical: MenuMotion? = null,
    val horizontal: MenuMotion? = null,
    val inline: MenuMotion? = null,
    val other: MenuMotion? = null,
)

/**
 * Popup placement configuration for submenus
 * Controls how submenu popups are positioned relative to their trigger
 */
data class MenuBuiltinPlacements(
    val leftTop: Pair<Int, Int>? = null,
    val leftBottom: Pair<Int, Int>? = null,
    val rightTop: Pair<Int, Int>? = null,
    val rightBottom: Pair<Int, Int>? = null,
    val topLeft: Pair<Int, Int>? = null,
    val topRight: Pair<Int, Int>? = null,
    val bottomLeft: Pair<Int, Int>? = null,
    val bottomRight: Pair<Int, Int>? = null,
)

/**
 * Menu ref interface for imperative operations
 * Provides focus control for the menu
 */
interface MenuRef {
    fun focus()
}

/**
 * Complete MenuItem data structure supporting all Ant Design Menu features
 */
data class MenuItem(
    val key: String,
    val label: String = "",
    val icon: (@Composable () -> Unit)? = null,
    val disabled: Boolean = false,
    val danger: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val children: List<MenuItem>? = null,
    val type: MenuItemType = MenuItemType.Item,
    val title: String? = null, // Tooltip title for collapsed items
    val extra: (@Composable () -> Unit)? = null, // Extra content on the right (v5.21.0)
    val dashed: Boolean = false, // For divider
    val popupClassName: String? = null, // SubMenu popup class name (not working when mode="inline")
    val popupOffset: Pair<Int, Int>? = null, // SubMenu offset [x, y] (not working when mode="inline")
    val onTitleClick: ((key: String) -> Unit)? = null, // Callback for SubMenu title click
)

/**
 * Complete Ant Design Menu component with full feature support:
 * - Multiple modes: vertical, horizontal, inline
 * - Theme support: light, dark
 * - SubMenus with expand/collapse animations
 * - Item groups
 * - Dividers
 * - Multiple and single selection
 * - Disabled and danger states
 * - Hover and active states with animations
 * - ConfigProvider integration
 * - onDeselect callback for multiple mode
 * - Custom expand icon
 * - Trigger submenu action (hover/click)
 * - Submenu open/close delays
 * - Force submenu render
 * - Overflowed indicator customization
 * - Popup container control
 * - Semantic styling (classNames, styles) - v5.4.0+
 * - Direction support (LTR/RTL)
 * - Active key control
 * - Focus management via MenuRef
 * - Motion customization
 * - Builtin placements for popups
 *
 * @param items Menu items to display (preferred over children since v4.20.0)
 * @param modifier Modifier for styling
 * @param mode Menu mode (vertical, horizontal, inline)
 * @param theme Color theme (light, dark)
 * @param selectedKeys Controlled selected keys
 * @param openKeys Controlled open submenu keys
 * @param defaultSelectedKeys Default selected keys
 * @param defaultOpenKeys Default open submenu keys
 * @param activeKey Currently active (but not selected) item key
 * @param defaultActiveFirst Whether to activate the first item by default
 * @param onSelect Called when item is selected
 * @param onDeselect Called when item is deselected (multiple mode only)
 * @param onClick Called when item is clicked
 * @param onOpenChange Called when submenus are opened/closed
 * @param expandIcon Custom expand icon for submenus
 * @param itemIcon Custom icon for all menu items
 * @param inlineIndent Indent width for inline menu items (default 24dp)
 * @param inlineCollapsed Collapsed status for inline mode
 * @param multiple Allow multiple item selection
 * @param selectable Allow selecting menu items
 * @param disabled Disable all menu items
 * @param direction Text direction (ltr or rtl)
 * @param triggerSubMenuAction How to trigger submenu (hover or click)
 * @param subMenuOpenDelay Delay before submenu opens on hover (ms)
 * @param subMenuCloseDelay Delay before submenu closes on hover (ms)
 * @param forceSubMenuRender Render submenu even when hidden
 * @param overflowedIndicator Custom icon for overflowed items in horizontal mode
 * @param overflowedIndicatorPopupClassName Class name for overflowed indicator popup
 * @param getPopupContainer Control where submenu popups are rendered
 * @param motion Custom animation motion configuration
 * @param defaultMotions Custom animation per menu mode
 * @param builtinPlacements Custom popup placement configuration
 * @param disabledOverflow Disable auto overflow behavior (experimental)
 * @param rootClassName Additional class name for root element
 * @param classNames Semantic class names for styling (v5.4.0+)
 * @param styles Semantic styles for styling (v5.4.0+)
 * @param menuRef Menu ref for imperative operations
 * @param children Deprecated - Use items prop instead (since v4.20.0)
 */
@Composable
fun AntMenu(
    items: List<MenuItem> = emptyList(),
    modifier: Modifier = Modifier,
    mode: MenuMode = MenuMode.Vertical,
    theme: MenuTheme = MenuTheme.Light,
    selectedKeys: List<String> = emptyList(),
    openKeys: List<String> = emptyList(),
    defaultSelectedKeys: List<String> = emptyList(),
    defaultOpenKeys: List<String> = emptyList(),
    activeKey: String? = null,
    defaultActiveFirst: Boolean = false,
    onSelect: ((key: String) -> Unit)? = null,
    onDeselect: ((key: String) -> Unit)? = null,
    onClick: ((key: String) -> Unit)? = null,
    onOpenChange: ((openKeys: List<String>) -> Unit)? = null,
    expandIcon: (@Composable (expanded: Boolean) -> Unit)? = null,
    itemIcon: (@Composable () -> Unit)? = null,
    inlineIndent: Dp = 24.dp,
    inlineCollapsed: Boolean = false,
    multiple: Boolean = false,
    selectable: Boolean = true,
    disabled: Boolean = false,
    direction: MenuDirection = MenuDirection.Ltr,
    triggerSubMenuAction: TriggerSubMenuAction = TriggerSubMenuAction.Hover,
    subMenuOpenDelay: Long = 0L,
    subMenuCloseDelay: Long = 100L,
    forceSubMenuRender: Boolean = false,
    overflowedIndicator: (@Composable () -> Unit)? = null,
    overflowedIndicatorPopupClassName: String? = null,
    getPopupContainer: (() -> Any)? = null,
    motion: MenuMotion? = null,
    defaultMotions: MenuDefaultMotions? = null,
    builtinPlacements: MenuBuiltinPlacements? = null,
    disabledOverflow: Boolean = false,
    rootClassName: String? = null,
    classNames: MenuClassNames? = null,
    styles: MenuStyles? = null,
    menuRef: MenuRef? = null,
    // @Deprecated: Use items prop instead of children (since v4.20.0)
    children: (@Composable () -> Unit)? = null,
) {
    // Get theme from ConfigProvider
    val configTheme = useTheme()

    // Focus management
    val focusRequester = remember { FocusRequester() }

    // Setup MenuRef
    LaunchedEffect(menuRef) {
        menuRef?.let { ref ->
            // Implement focus functionality
            object : MenuRef {
                override fun focus() {
                    focusRequester.requestFocus()
                }
            }
        }
    }

    // Manage internal state for selected and open keys
    var internalSelectedKeys by remember { mutableStateOf(defaultSelectedKeys) }
    var internalOpenKeys by remember { mutableStateOf(defaultOpenKeys) }
    var internalActiveKey by remember { mutableStateOf(if (defaultActiveFirst && items.isNotEmpty()) items[0].key else null) }

    val currentSelectedKeys = if (selectedKeys.isNotEmpty()) selectedKeys else internalSelectedKeys
    val currentOpenKeys = if (openKeys.isNotEmpty()) openKeys else internalOpenKeys
    val currentActiveKey = activeKey ?: internalActiveKey

    // Theme colors
    val colors = remember(theme, configTheme) {
        MenuColors.fromTheme(theme, configTheme)
    }

    // Apply motion configuration
    val effectiveMotion = motion ?: when (mode) {
        MenuMode.Vertical -> defaultMotions?.vertical ?: MenuMotion()
        MenuMode.Horizontal -> defaultMotions?.horizontal ?: MenuMotion()
        MenuMode.Inline -> defaultMotions?.inline ?: MenuMotion()
    }

    // Apply semantic styles
    val rootModifier = modifier
        .then(styles?.root ?: Modifier)
        .focusRequester(focusRequester)
        .focusable()

    // Handle item click
    val handleClick: (String) -> Unit = { key ->
        onClick?.invoke(key)
        internalActiveKey = key
    }

    // Handle item selection
    val handleSelect: (String) -> Unit = { key ->
        if (selectable && !disabled) {
            val wasSelected = currentSelectedKeys.contains(key)

            internalSelectedKeys = if (multiple) {
                if (wasSelected) {
                    // Deselecting in multiple mode
                    onDeselect?.invoke(key)
                    currentSelectedKeys - key
                } else {
                    // Selecting in multiple mode
                    onSelect?.invoke(key)
                    currentSelectedKeys + key
                }
            } else {
                // Single selection mode
                if (!wasSelected) {
                    onSelect?.invoke(key)
                }
                listOf(key)
            }
        }
    }

    // Handle submenu open/close
    val handleOpenChange: (String, Boolean) -> Unit = { key, isOpen ->
        val newOpenKeys = if (isOpen) {
            currentOpenKeys + key
        } else {
            currentOpenKeys - key
        }
        internalOpenKeys = newOpenKeys
        onOpenChange?.invoke(newOpenKeys)
    }

    // Main container
    Box(
        modifier = rootModifier
            .background(colors.backgroundColor)
    ) {
        when (mode) {
            MenuMode.Vertical, MenuMode.Inline -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEach { item ->
                        RenderMenuItem(
                            item = item,
                            level = 0,
                            mode = mode,
                            theme = theme,
                            colors = colors,
                            selectedKeys = currentSelectedKeys,
                            openKeys = currentOpenKeys,
                            activeKey = currentActiveKey,
                            onSelect = handleSelect,
                            onClick = handleClick,
                            onOpenChange = handleOpenChange,
                            expandIcon = expandIcon,
                            itemIcon = itemIcon,
                            inlineIndent = inlineIndent,
                            inlineCollapsed = inlineCollapsed,
                            disabled = disabled,
                            direction = direction,
                            triggerSubMenuAction = triggerSubMenuAction,
                            subMenuOpenDelay = subMenuOpenDelay,
                            subMenuCloseDelay = subMenuCloseDelay,
                            forceSubMenuRender = forceSubMenuRender,
                            motion = effectiveMotion,
                            classNames = classNames,
                            styles = styles
                        )
                    }

                    // Render deprecated children if provided
                    children?.invoke()
                }
            }

            MenuMode.Horizontal -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items.forEach { item ->
                        RenderMenuItem(
                            item = item,
                            level = 0,
                            mode = mode,
                            theme = theme,
                            colors = colors,
                            selectedKeys = currentSelectedKeys,
                            openKeys = currentOpenKeys,
                            activeKey = currentActiveKey,
                            onSelect = handleSelect,
                            onClick = handleClick,
                            onOpenChange = handleOpenChange,
                            expandIcon = expandIcon,
                            itemIcon = itemIcon,
                            inlineIndent = inlineIndent,
                            inlineCollapsed = inlineCollapsed,
                            disabled = disabled,
                            direction = direction,
                            triggerSubMenuAction = triggerSubMenuAction,
                            subMenuOpenDelay = subMenuOpenDelay,
                            subMenuCloseDelay = subMenuCloseDelay,
                            forceSubMenuRender = forceSubMenuRender,
                            motion = effectiveMotion,
                            classNames = classNames,
                            styles = styles
                        )
                    }

                    // Render deprecated children if provided
                    children?.invoke()
                }
            }
        }
    }
}

/**
 * Router function to render different menu item types
 */
@Composable
private fun RenderMenuItem(
    item: MenuItem,
    level: Int,
    mode: MenuMode,
    theme: MenuTheme,
    colors: MenuColors,
    selectedKeys: List<String>,
    openKeys: List<String>,
    activeKey: String?,
    onSelect: (String) -> Unit,
    onClick: (String) -> Unit,
    onOpenChange: (String, Boolean) -> Unit,
    expandIcon: (@Composable (expanded: Boolean) -> Unit)?,
    itemIcon: (@Composable () -> Unit)?,
    inlineIndent: Dp,
    inlineCollapsed: Boolean,
    disabled: Boolean,
    direction: MenuDirection,
    triggerSubMenuAction: TriggerSubMenuAction,
    subMenuOpenDelay: Long,
    subMenuCloseDelay: Long,
    forceSubMenuRender: Boolean,
    motion: MenuMotion,
    classNames: MenuClassNames?,
    styles: MenuStyles?,
) {
    when (item.type) {
        MenuItemType.Divider -> {
            MenuDividerComponent(
                dashed = item.dashed,
                colors = colors
            )
        }

        MenuItemType.Group -> {
            MenuItemGroupComponent(
                item = item,
                level = level,
                mode = mode,
                theme = theme,
                colors = colors,
                selectedKeys = selectedKeys,
                openKeys = openKeys,
                activeKey = activeKey,
                onSelect = onSelect,
                onClick = onClick,
                onOpenChange = onOpenChange,
                expandIcon = expandIcon,
                itemIcon = itemIcon,
                inlineIndent = inlineIndent,
                inlineCollapsed = inlineCollapsed,
                disabled = disabled,
                direction = direction,
                triggerSubMenuAction = triggerSubMenuAction,
                subMenuOpenDelay = subMenuOpenDelay,
                subMenuCloseDelay = subMenuCloseDelay,
                forceSubMenuRender = forceSubMenuRender,
                motion = motion,
                classNames = classNames,
                styles = styles
            )
        }

        MenuItemType.SubMenu -> {
            MenuSubMenuComponent(
                item = item,
                level = level,
                mode = mode,
                theme = theme,
                colors = colors,
                selectedKeys = selectedKeys,
                openKeys = openKeys,
                activeKey = activeKey,
                onSelect = onSelect,
                onClick = onClick,
                onOpenChange = onOpenChange,
                expandIcon = expandIcon,
                itemIcon = itemIcon,
                inlineIndent = inlineIndent,
                inlineCollapsed = inlineCollapsed,
                disabled = disabled,
                direction = direction,
                triggerSubMenuAction = triggerSubMenuAction,
                subMenuOpenDelay = subMenuOpenDelay,
                subMenuCloseDelay = subMenuCloseDelay,
                forceSubMenuRender = forceSubMenuRender,
                motion = motion,
                classNames = classNames,
                styles = styles
            )
        }

        MenuItemType.Item -> {
            MenuItemComponent(
                item = item,
                level = level,
                mode = mode,
                colors = colors,
                isSelected = selectedKeys.contains(item.key),
                isActive = activeKey == item.key,
                onSelect = onSelect,
                onClick = onClick,
                itemIcon = itemIcon,
                inlineIndent = inlineIndent,
                inlineCollapsed = inlineCollapsed,
                disabled = disabled,
                direction = direction,
                classNames = classNames,
                styles = styles
            )
        }
    }
}

/**
 * Individual menu item with hover/active states and ripple effect
 */
@Composable
private fun MenuItemComponent(
    item: MenuItem,
    level: Int,
    mode: MenuMode,
    colors: MenuColors,
    isSelected: Boolean,
    isActive: Boolean,
    onSelect: (String) -> Unit,
    onClick: (String) -> Unit,
    itemIcon: (@Composable () -> Unit)?,
    inlineIndent: Dp,
    inlineCollapsed: Boolean,
    disabled: Boolean,
    direction: MenuDirection,
    classNames: MenuClassNames?,
    styles: MenuStyles?,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Determine if item is disabled
    val isDisabled = item.disabled || disabled

    // Background color based on state
    val backgroundColor = when {
        isSelected -> styles?.itemSelectedBackgroundColor ?: colors.selectedBg
        isActive -> styles?.itemBackgroundColor ?: colors.hoverBg.copy(alpha = 0.5f)
        isHovered && !isDisabled -> styles?.itemBackgroundColor ?: colors.hoverBg
        else -> styles?.itemBackgroundColor ?: Color.Transparent
    }

    // Text color
    val textColor = when {
        isDisabled -> colors.disabledTextColor
        item.danger -> colors.dangerColor
        isSelected -> styles?.itemSelectedTextColor ?: colors.selectedTextColor
        else -> styles?.itemTextColor ?: colors.textColor
    }

    // Padding based on level and mode
    val startPadding = if (mode == MenuMode.Inline || mode == MenuMode.Vertical) {
        inlineIndent * level + 24.dp
    } else {
        16.dp
    }

    // Apply RTL direction
    val contentArrangement = if (direction == MenuDirection.Rtl) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Box(
        modifier = Modifier
            .then(
                if (mode == MenuMode.Horizontal) {
                    Modifier.wrapContentWidth()
                } else {
                    Modifier.fillMaxWidth()
                }
            )
            .then(styles?.item ?: Modifier)
            .background(backgroundColor)
            .hoverable(interactionSource)
            .clickable(
                enabled = !isDisabled,
                onClick = {
                    item.onClick?.invoke()
                    onClick(item.key)
                    onSelect(item.key)
                },
                indication = null,
                interactionSource = interactionSource
            )
            .padding(
                start = startPadding,
                end = 24.dp,
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon (from item or from menu-level itemIcon)
            if (item.icon != null || itemIcon != null) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .then(styles?.itemIcon ?: Modifier)
                ) {
                    item.icon?.invoke() ?: itemIcon?.invoke()
                }
            }

            // Label
            if (!inlineCollapsed || level > 0) {
                Row(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .then(styles?.itemContent ?: Modifier),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.label,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )

                    // Extra content
                    item.extra?.invoke()
                }
            }
        }
    }

    // Bottom border for horizontal mode when selected
    if (mode == MenuMode.Horizontal && isSelected) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(colors.selectedTextColor)
        )
    }
}

/**
 * SubMenu component with expand/collapse animation
 * Supports custom expand icons, hover/click triggers, and open/close delays
 */
@Composable
private fun MenuSubMenuComponent(
    item: MenuItem,
    level: Int,
    mode: MenuMode,
    theme: MenuTheme,
    colors: MenuColors,
    selectedKeys: List<String>,
    openKeys: List<String>,
    activeKey: String?,
    onSelect: (String) -> Unit,
    onClick: (String) -> Unit,
    onOpenChange: (String, Boolean) -> Unit,
    expandIcon: (@Composable (expanded: Boolean) -> Unit)?,
    itemIcon: (@Composable () -> Unit)?,
    inlineIndent: Dp,
    inlineCollapsed: Boolean,
    disabled: Boolean,
    direction: MenuDirection,
    triggerSubMenuAction: TriggerSubMenuAction,
    subMenuOpenDelay: Long,
    subMenuCloseDelay: Long,
    forceSubMenuRender: Boolean,
    motion: MenuMotion,
    classNames: MenuClassNames?,
    styles: MenuStyles?,
) {
    val isOpen = openKeys.contains(item.key)
    val hasSelectedChild = item.children?.any { child ->
        selectedKeys.contains(child.key) ||
                (child.children?.any { selectedKeys.contains(it.key) } == true)
    } ?: false

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Determine if submenu is disabled
    val isDisabled = item.disabled || disabled

    // Delayed open/close for hover trigger
    var hoverOpenJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Handle hover trigger with delays
    LaunchedEffect(isHovered, triggerSubMenuAction) {
        if (triggerSubMenuAction == TriggerSubMenuAction.Hover && !isDisabled) {
            hoverOpenJob?.cancel()
            if (isHovered && !isOpen) {
                hoverOpenJob = coroutineScope.launch {
                    delay(subMenuOpenDelay)
                    onOpenChange(item.key, true)
                }
            } else if (!isHovered && isOpen) {
                hoverOpenJob = coroutineScope.launch {
                    delay(subMenuCloseDelay)
                    onOpenChange(item.key, false)
                }
            }
        }
    }

    // Background color
    val backgroundColor = when {
        hasSelectedChild -> colors.selectedBg.copy(alpha = 0.5f)
        isHovered && !isDisabled -> colors.hoverBg
        else -> Color.Transparent
    }

    // Text color
    val textColor = when {
        isDisabled -> colors.disabledTextColor
        hasSelectedChild || isOpen -> styles?.itemSelectedTextColor ?: colors.selectedTextColor
        else -> styles?.itemTextColor ?: colors.textColor
    }

    val startPadding = if (mode == MenuMode.Inline || mode == MenuMode.Vertical) {
        inlineIndent * level + 24.dp
    } else {
        16.dp
    }

    Column {
        // SubMenu title
        Box(
            modifier = Modifier
                .then(
                    if (mode == MenuMode.Horizontal) {
                        Modifier.wrapContentWidth()
                    } else {
                        Modifier.fillMaxWidth()
                    }
                )
                .then(styles?.item ?: Modifier)
                .background(backgroundColor)
                .hoverable(interactionSource)
                .clickable(
                    enabled = !isDisabled,
                    onClick = {
                        // Handle click trigger
                        if (triggerSubMenuAction == TriggerSubMenuAction.Click) {
                            onOpenChange(item.key, !isOpen)
                        }
                        // Call onTitleClick callback if provided
                        item.onTitleClick?.invoke(item.key)
                        onClick(item.key)
                    },
                    indication = null,
                    interactionSource = interactionSource
                )
                .padding(
                    start = startPadding,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icon (from item or from menu-level itemIcon)
                    if ((item.icon != null || itemIcon != null) && !inlineCollapsed) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .then(styles?.itemIcon ?: Modifier)
                        ) {
                            item.icon?.invoke() ?: itemIcon?.invoke()
                        }
                    }

                    // Label
                    if (!inlineCollapsed || level > 0) {
                        Box(modifier = Modifier.then(styles?.itemContent ?: Modifier)) {
                            Text(
                                text = item.label,
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = if (hasSelectedChild) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    } else if (inlineCollapsed && level == 0) {
                        Text(
                            text = item.label.firstOrNull()?.toString() ?: "",
                            color = textColor,
                            fontSize = 14.sp
                        )
                    }
                }

                // Expand icon
                if (!inlineCollapsed || level > 0) {
                    if (expandIcon != null) {
                        // Custom expand icon
                        val rotation by animateFloatAsState(
                            targetValue = if (isOpen) 90f else 0f,
                            animationSpec = tween(durationMillis = 200),
                            label = "expandIconRotation"
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .then(
                                    if (mode != MenuMode.Horizontal) {
                                        Modifier.graphicsLayer { rotationZ = rotation }
                                    } else Modifier
                                )
                        ) {
                            expandIcon(isOpen)
                        }
                    } else {
                        // Default expand icon
                        val rotation by animateFloatAsState(
                            targetValue = if (isOpen) 90f else 0f,
                            animationSpec = tween(durationMillis = 200),
                            label = "expandIconRotation"
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .then(
                                    if (mode != MenuMode.Horizontal) {
                                        Modifier.graphicsLayer { rotationZ = rotation }
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (mode == MenuMode.Horizontal) "▼" else "▶",
                                color = textColor,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // SubMenu children (inline mode)
        if ((mode == MenuMode.Inline || mode == MenuMode.Vertical) && !inlineCollapsed) {
            // Force render or use animated visibility
            if (forceSubMenuRender) {
                // Always render, but hide with alpha
                val alpha by animateFloatAsState(
                    targetValue = if (isOpen) 1f else 0f,
                    animationSpec = tween(durationMillis = motion.durationMillis, easing = motion.easing),
                    label = "subMenuAlpha"
                )
                Column(
                    modifier = Modifier
                        .then(styles?.popup ?: Modifier)
                        .graphicsLayer {
                            this.alpha = alpha
                        }
                ) {
                    if (isOpen || forceSubMenuRender) {
                        item.children?.forEach { child ->
                            RenderMenuItem(
                                item = child,
                                level = level + 1,
                                mode = mode,
                                theme = theme,
                                colors = colors,
                                selectedKeys = selectedKeys,
                                openKeys = openKeys,
                                activeKey = activeKey,
                                onSelect = onSelect,
                                onClick = onClick,
                                onOpenChange = onOpenChange,
                                expandIcon = expandIcon,
                                itemIcon = itemIcon,
                                inlineIndent = inlineIndent,
                                inlineCollapsed = inlineCollapsed,
                                disabled = disabled,
                                direction = direction,
                                triggerSubMenuAction = triggerSubMenuAction,
                                subMenuOpenDelay = subMenuOpenDelay,
                                subMenuCloseDelay = subMenuCloseDelay,
                                forceSubMenuRender = forceSubMenuRender,
                                motion = motion,
                                classNames = classNames,
                                styles = styles
                            )
                        }
                    }
                }
            } else {
                // Standard animated visibility
                AnimatedVisibility(
                    visible = isOpen,
                    enter = expandVertically(
                        animationSpec = tween(durationMillis = motion.durationMillis, easing = motion.easing),
                        expandFrom = Alignment.Top
                    ) + fadeIn(animationSpec = tween(durationMillis = motion.durationMillis, easing = motion.easing)),
                    exit = shrinkVertically(
                        animationSpec = tween(durationMillis = motion.durationMillis, easing = motion.easing),
                        shrinkTowards = Alignment.Top
                    ) + fadeOut(animationSpec = tween(durationMillis = motion.durationMillis, easing = motion.easing))
                ) {
                    Column(modifier = Modifier.then(styles?.popup ?: Modifier)) {
                        item.children?.forEach { child ->
                            RenderMenuItem(
                                item = child,
                                level = level + 1,
                                mode = mode,
                                theme = theme,
                                colors = colors,
                                selectedKeys = selectedKeys,
                                openKeys = openKeys,
                                activeKey = activeKey,
                                onSelect = onSelect,
                                onClick = onClick,
                                onOpenChange = onOpenChange,
                                expandIcon = expandIcon,
                                itemIcon = itemIcon,
                                inlineIndent = inlineIndent,
                                inlineCollapsed = inlineCollapsed,
                                disabled = disabled,
                                direction = direction,
                                triggerSubMenuAction = triggerSubMenuAction,
                                subMenuOpenDelay = subMenuOpenDelay,
                                subMenuCloseDelay = subMenuCloseDelay,
                                forceSubMenuRender = forceSubMenuRender,
                                motion = motion,
                                classNames = classNames,
                                styles = styles
                            )
                        }
                    }
                }
            }
        }

        // For horizontal mode, we would need a dropdown/popup here
        // This would require additional positioning logic with DropdownMenu or Popup
    }
}

/**
 * Menu item group - groups items with a title (non-selectable)
 */
@Composable
private fun MenuItemGroupComponent(
    item: MenuItem,
    level: Int,
    mode: MenuMode,
    theme: MenuTheme,
    colors: MenuColors,
    selectedKeys: List<String>,
    openKeys: List<String>,
    activeKey: String?,
    onSelect: (String) -> Unit,
    onClick: (String) -> Unit,
    onOpenChange: (String, Boolean) -> Unit,
    expandIcon: (@Composable (expanded: Boolean) -> Unit)?,
    itemIcon: (@Composable () -> Unit)?,
    inlineIndent: Dp,
    inlineCollapsed: Boolean,
    disabled: Boolean,
    direction: MenuDirection,
    triggerSubMenuAction: TriggerSubMenuAction,
    subMenuOpenDelay: Long,
    subMenuCloseDelay: Long,
    forceSubMenuRender: Boolean,
    motion: MenuMotion,
    classNames: MenuClassNames?,
    styles: MenuStyles?,
) {
    Column {
        // Group title
        if (!inlineCollapsed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = inlineIndent * level + 24.dp,
                        end = 24.dp,
                        top = 8.dp,
                        bottom = 4.dp
                    )
            ) {
                Text(
                    text = item.label,
                    color = colors.textColor.copy(alpha = 0.45f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Group children
        item.children?.forEach { child ->
            RenderMenuItem(
                item = child,
                level = level,
                mode = mode,
                theme = theme,
                colors = colors,
                selectedKeys = selectedKeys,
                openKeys = openKeys,
                activeKey = activeKey,
                onSelect = onSelect,
                onClick = onClick,
                onOpenChange = onOpenChange,
                expandIcon = expandIcon,
                itemIcon = itemIcon,
                inlineIndent = inlineIndent,
                inlineCollapsed = inlineCollapsed,
                disabled = disabled,
                direction = direction,
                triggerSubMenuAction = triggerSubMenuAction,
                subMenuOpenDelay = subMenuOpenDelay,
                subMenuCloseDelay = subMenuCloseDelay,
                forceSubMenuRender = forceSubMenuRender,
                motion = motion,
                classNames = classNames,
                styles = styles
            )
        }
    }
}

/**
 * Menu divider component
 */
@Composable
private fun MenuDividerComponent(
    dashed: Boolean,
    colors: MenuColors,
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        thickness = 1.dp,
        color = colors.dividerColor
    )
}

/**
 * Menu color scheme based on theme
 */
private data class MenuColors(
    val backgroundColor: Color,
    val textColor: Color,
    val selectedBg: Color,
    val selectedTextColor: Color,
    val hoverBg: Color,
    val disabledTextColor: Color,
    val dangerColor: Color,
    val dividerColor: Color,
) {
    companion object {
        fun fromTheme(theme: MenuTheme, configTheme: AntThemeConfig): MenuColors {
            return when (theme) {
                MenuTheme.Light -> MenuColors(
                    backgroundColor = Color.White,
                    textColor = Color(0xFF000000).copy(alpha = 0.85f),
                    selectedBg = configTheme.token.colorPrimary.copy(alpha = 0.1f),
                    selectedTextColor = configTheme.token.colorPrimary,
                    hoverBg = Color(0xFF000000).copy(alpha = 0.06f),
                    disabledTextColor = Color(0xFF000000).copy(alpha = 0.25f),
                    dangerColor = configTheme.token.colorError,
                    dividerColor = Color(0xFF000000).copy(alpha = 0.06f)
                )

                MenuTheme.Dark -> MenuColors(
                    backgroundColor = Color(0xFF001529),
                    textColor = Color.White.copy(alpha = 0.85f),
                    selectedBg = configTheme.token.colorPrimary,
                    selectedTextColor = Color.White,
                    hoverBg = Color.White.copy(alpha = 0.08f),
                    disabledTextColor = Color.White.copy(alpha = 0.25f),
                    dangerColor = configTheme.token.colorError,
                    dividerColor = Color.White.copy(alpha = 0.12f)
                )
            }
        }
    }
}

