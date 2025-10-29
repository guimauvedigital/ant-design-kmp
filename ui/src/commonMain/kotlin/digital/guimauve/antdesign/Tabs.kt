package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tab item data class representing a single tab.
 *
 * @param key Unique identifier for the tab
 * @param label Display label for the tab
 * @param icon Optional icon to display before the label (v5.12.0+)
 * @param disabled Whether the tab is disabled
 * @param closable Whether the tab can be closed (for editable-card type)
 * @param closeIcon Optional custom close icon (overrides default)
 * @param forceRender Force render content even when inactive (lazy loading control)
 * @param destroyOnHidden Destroy tab pane when hidden (v5.25.0+)
 * @param content Content to display when tab is active
 */
data class TabItem(
    val key: String,
    val label: String = "",
    val icon: (@Composable () -> Unit)? = null,
    val disabled: Boolean = false,
    val closable: Boolean = true,
    val closeIcon: (@Composable () -> Unit)? = null,
    val forceRender: Boolean = false,
    val destroyOnHidden: Boolean = false,
    val content: @Composable () -> Unit,
)

/**
 * Tab type enum - determines the visual style of tabs.
 */
enum class TabType {
    Line,           // Line under active tab
    Card,           // Card-style tabs
    EditableCard    // Card with add/remove buttons
}

/**
 * Tab position enum - determines where tabs are placed.
 */
enum class TabPosition {
    Top,
    Right,
    Bottom,
    Left
}

/**
 * Tab size enum - determines the size of tabs.
 */
enum class TabSize {
    Small,
    Middle,
    Large
}

/**
 * Tab edit action enum.
 */
enum class TabAction {
    Add,
    Remove
}

/**
 * Tab indicator alignment for the active bar.
 */
enum class TabIndicatorAlign {
    /** Align indicator to start of tab */
    Start,

    /** Align indicator to center of tab (default) */
    Center,

    /** Align indicator to end of tab */
    End
}

/**
 * Scroll direction for tab scrolling.
 */
enum class ScrollDirection {
    /** Scrolling left */
    Left,

    /** Scrolling right */
    Right
}

/**
 * Tab indicator configuration for customizing the active bar.
 *
 * @param size Height (for horizontal tabs) or width (for vertical tabs) of the indicator.
 *             Can be a fixed Dp or a function that computes size based on the original tab size.
 * @param align Alignment of the indicator relative to the tab
 * @param color Custom color for the indicator (overrides theme color)
 * @param style Custom modifier for additional indicator styling
 */
data class TabIndicator(
    val size: ((originalSize: Dp) -> Dp)? = null,
    val align: TabIndicatorAlign = TabIndicatorAlign.Center,
    val color: Color? = null,
    val style: Modifier = Modifier,
)

/**
 * Animation configuration for tabs.
 * Default is { inkBar: true, tabPane: false } matching React Ant Design v5.x behavior.
 */
sealed class TabsAnimated {
    /** No animations */
    data object Disabled : TabsAnimated()

    /**
     * Animated with control over individual animation types.
     * @param inkBar Enable/disable indicator bar animation (default: true)
     * @param tabPane Enable/disable tab pane content animation (default: false)
     */
    data class Simple(
        val inkBar: Boolean = true,
        val tabPane: Boolean = false,
    ) : TabsAnimated()
}

/**
 * More menu configuration for tabs overflow dropdown.
 * @param icon Custom icon for the more menu
 * @param trigger Trigger mode for dropdown (hover, click, etc.)
 */
data class MoreProps(
    val icon: (@Composable () -> Unit)? = null,
    val trigger: String = "hover",
)

/**
 * Semantic styling configuration for Tabs component (v5.4.0+).
 * Provides className strings for semantic DOM nodes.
 */
data class TabsClassNames(
    val root: String? = null,
    val nav: String? = null,
    val navList: String? = null,
    val navOperations: String? = null,
    val inkBar: String? = null,
    val tab: String? = null,
    val tabActive: String? = null,
    val tabIcon: String? = null,
    val tabLabel: String? = null,
    val tabBtn: String? = null,
    val addIcon: String? = null,
    val removeIcon: String? = null,
    val moreIcon: String? = null,
    val content: String? = null,
    val contentHolder: String? = null,
    val panel: String? = null,
)

/**
 * Semantic styling configuration for Tabs component (v5.4.0+).
 * Provides Modifier instances for semantic DOM nodes.
 */
data class TabsStyles(
    val root: Modifier? = null,
    val nav: Modifier? = null,
    val navList: Modifier? = null,
    val navOperations: Modifier? = null,
    val inkBar: Modifier? = null,
    val tab: Modifier? = null,
    val tabActive: Modifier? = null,
    val tabIcon: Modifier? = null,
    val tabLabel: Modifier? = null,
    val tabBtn: Modifier? = null,
    val addIcon: Modifier? = null,
    val removeIcon: Modifier? = null,
    val moreIcon: Modifier? = null,
    val content: Modifier? = null,
    val contentHolder: Modifier? = null,
    val panel: Modifier? = null,
)

/**
 * Tab bar extra content configuration.
 * Can be a simple composable or split into left/right sections (v4.6.0+).
 */
sealed class TabBarExtraContent {
    /** Simple extra content */
    data class Simple(val content: @Composable () -> Unit) : TabBarExtraContent()

    /** Split extra content into left and right sections */
    data class Split(
        val left: (@Composable () -> Unit)? = null,
        val right: (@Composable () -> Unit)? = null,
    ) : TabBarExtraContent()
}

/**
 * Main Tabs component with full Ant Design functionality.
 *
 * @param items List of tab items to display (v4.23.0+)
 * @param activeKey Currently active tab key (controlled mode)
 * @param defaultActiveKey Default active tab key (uncontrolled mode)
 * @param onChange Callback when active tab changes
 * @param onTabClick Callback when a tab is clicked (fires even if already active)
 * @param onTabScroll Callback when tab list is scrolled (for scrollable tabs, v4.3.0+)
 * @param type Tab type (Line, Card, EditableCard)
 * @param size Tab size
 * @param position Tab position (Top, Right, Bottom, Left)
 * @param tabPosition Alias for position (deprecated - use position instead)
 * @param centered Center tabs horizontally (v4.4.0+)
 * @param tabBarExtraContent Extra content to show in tab bar (v4.6.0+ supports left/right split)
 * @param tabBarGutter Gap between tabs in pixels
 * @param tabBarStyle Custom styling for the tab bar
 * @param onEdit Callback for add/remove actions (EditableCard only)
 * @param hideAdd Hide the add button (EditableCard only)
 * @param addIcon Custom icon for add button (v4.4.0+)
 * @param removeIcon Custom icon for remove button (v5.15.0+)
 * @param animated Enable/configure animations. Default is { inkBar: true, tabPane: false }
 * @param indicator Custom indicator configuration (size, align, color, v5.13.0+)
 * @param destroyInactiveTabPane Unmount inactive tab content (deprecated - use destroyOnHidden)
 * @param destroyOnHidden Destroy inactive tab panes (v5.25.0+)
 * @param more Custom configuration for overflow dropdown menu
 * @param moreIcon Custom icon for overflow dropdown (card type only)
 * @param popupClassName Class name for dropdown menu popup (v4.21.0+)
 * @param renderTabBar Custom tab bar wrapper/replacement function
 * @param classNames Semantic class names for sub-components (v5.4.0+)
 * @param styles Semantic styles for sub-components (v5.4.0+)
 * @param modifier Modifier for the component
 */
@Composable
fun AntTabs(
    items: List<TabItem>,
    activeKey: String? = null,
    defaultActiveKey: String? = null,
    onChange: ((key: String) -> Unit)? = null,
    onTabClick: ((key: String, event: Any?) -> Unit)? = null,
    onTabScroll: ((direction: ScrollDirection) -> Unit)? = null,
    type: TabType = TabType.Line,
    size: TabSize = TabSize.Middle,
    position: TabPosition = TabPosition.Top,
    // Deprecated: Use position instead
    tabPosition: TabPosition? = null,
    centered: Boolean = false,
    tabBarExtraContent: Any? = null, // @Composable () -> Unit or TabBarExtraContent
    tabBarGutter: Dp? = null,
    tabBarStyle: Modifier? = null,
    onEdit: ((targetKey: String, action: TabAction) -> Unit)? = null,
    hideAdd: Boolean = false,
    addIcon: (@Composable () -> Unit)? = null,
    removeIcon: (@Composable () -> Unit)? = null,
    animated: Any = TabsAnimated.Simple(), // Boolean or TabsAnimated, default { inkBar: true, tabPane: false }
    indicator: TabIndicator? = null,
    // Deprecated: Use destroyOnHidden instead (v5.25.0+)
    destroyInactiveTabPane: Boolean = false,
    destroyOnHidden: Boolean = false,
    more: MoreProps? = null,
    moreIcon: (@Composable () -> Unit)? = null,
    popupClassName: String? = null,
    renderTabBar: (@Composable (defaultTabBar: @Composable () -> Unit) -> Unit)? = null,
    classNames: TabsClassNames? = null,
    styles: TabsStyles? = null,
    modifier: Modifier = Modifier,
) {
    // Get theme from ConfigProvider
    val theme = useTheme()

    // Resolve position (handle deprecated tabPosition)
    val effectivePosition = tabPosition ?: position

    // Resolve destroyOnHidden (handle deprecated destroyInactiveTabPane)
    val effectiveDestroyOnHidden = destroyOnHidden || destroyInactiveTabPane

    // Manage active key (controlled/uncontrolled)
    var internalActiveKey by remember {
        mutableStateOf(defaultActiveKey ?: items.firstOrNull()?.key ?: "")
    }

    val currentActiveKey = activeKey ?: internalActiveKey

    val handleTabChange: (String) -> Unit = { key ->
        if (activeKey == null) {
            internalActiveKey = key
        }
        onChange?.invoke(key)
    }

    // Parse animated parameter
    val animationConfig = when (animated) {
        is TabsAnimated -> animated
        is Boolean -> if (animated) TabsAnimated.Simple() else TabsAnimated.Disabled
        else -> TabsAnimated.Simple() // Default to { inkBar: true, tabPane: false }
    }

    // Default tab bar composable
    val defaultTabBar: @Composable () -> Unit = {
        TabBar(
            items = items,
            activeKey = currentActiveKey,
            onTabChange = handleTabChange,
            onTabClick = onTabClick,
            onTabScroll = onTabScroll,
            type = type,
            size = size,
            position = effectivePosition,
            centered = centered,
            tabBarExtraContent = tabBarExtraContent,
            tabBarGutter = tabBarGutter,
            tabBarStyle = tabBarStyle,
            onEdit = onEdit,
            hideAdd = hideAdd,
            addIcon = addIcon,
            removeIcon = removeIcon,
            theme = theme,
            indicator = indicator,
            animationConfig = animationConfig,
            more = more,
            moreIcon = moreIcon,
            popupClassName = popupClassName,
            classNames = classNames,
            styles = styles
        )
    }

    // Apply root styles
    val rootModifier = modifier
        .then(styles?.root ?: Modifier)

    // Layout based on position
    when (effectivePosition) {
        TabPosition.Top -> {
            Column(modifier = rootModifier) {
                if (renderTabBar != null) {
                    renderTabBar(defaultTabBar)
                } else {
                    defaultTabBar()
                }
                TabContent(
                    items = items,
                    activeKey = currentActiveKey,
                    animationConfig = animationConfig,
                    destroyOnHidden = effectiveDestroyOnHidden,
                    styles = styles
                )
            }
        }

        TabPosition.Bottom -> {
            Column(modifier = rootModifier) {
                TabContent(
                    items = items,
                    activeKey = currentActiveKey,
                    animationConfig = animationConfig,
                    destroyOnHidden = effectiveDestroyOnHidden,
                    styles = styles
                )
                if (renderTabBar != null) {
                    renderTabBar(defaultTabBar)
                } else {
                    defaultTabBar()
                }
            }
        }

        TabPosition.Left -> {
            Row(modifier = rootModifier) {
                if (renderTabBar != null) {
                    renderTabBar(defaultTabBar)
                } else {
                    defaultTabBar()
                }
                TabContent(
                    items = items,
                    activeKey = currentActiveKey,
                    animationConfig = animationConfig,
                    destroyOnHidden = effectiveDestroyOnHidden,
                    styles = styles,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TabPosition.Right -> {
            Row(modifier = rootModifier) {
                TabContent(
                    items = items,
                    activeKey = currentActiveKey,
                    animationConfig = animationConfig,
                    destroyOnHidden = effectiveDestroyOnHidden,
                    styles = styles,
                    modifier = Modifier.weight(1f)
                )
                if (renderTabBar != null) {
                    renderTabBar(defaultTabBar)
                } else {
                    defaultTabBar()
                }
            }
        }
    }
}

@Composable
private fun TabBar(
    items: List<TabItem>,
    activeKey: String,
    onTabChange: (String) -> Unit,
    onTabClick: ((key: String, event: Any?) -> Unit)?,
    onTabScroll: ((direction: ScrollDirection) -> Unit)?,
    type: TabType,
    size: TabSize,
    position: TabPosition,
    centered: Boolean,
    tabBarExtraContent: Any?,
    tabBarGutter: Dp?,
    tabBarStyle: Modifier?,
    onEdit: ((targetKey: String, action: TabAction) -> Unit)?,
    hideAdd: Boolean,
    addIcon: (@Composable () -> Unit)?,
    removeIcon: (@Composable () -> Unit)?,
    theme: AntThemeConfig,
    indicator: TabIndicator?,
    animationConfig: TabsAnimated,
    more: MoreProps?,
    moreIcon: (@Composable () -> Unit)?,
    popupClassName: String?,
    classNames: TabsClassNames?,
    styles: TabsStyles?,
) {
    val isVertical = position == TabPosition.Left || position == TabPosition.Right

    // State for animated indicator
    var indicatorOffset by remember { mutableStateOf(0.dp) }
    var indicatorSize by remember { mutableStateOf(0.dp) }
    var originalTabSize by remember { mutableStateOf(0.dp) }

    // Determine if ink bar animation is enabled
    val inkBarAnimationEnabled = when (animationConfig) {
        is TabsAnimated.Disabled -> false
        is TabsAnimated.Simple -> animationConfig.inkBar
    }

    val animatedOffset by animateDpAsState(
        targetValue = indicatorOffset,
        animationSpec = if (inkBarAnimationEnabled) {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        } else {
            snap()
        }
    )

    val animatedSize by animateDpAsState(
        targetValue = indicatorSize,
        animationSpec = if (inkBarAnimationEnabled) {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        } else {
            snap()
        }
    )

    val density = LocalDensity.current

    // Keyboard navigation
    val focusRequesters = remember(items.size) {
        items.map { FocusRequester() }
    }
    var focusedIndex by remember { mutableStateOf(items.indexOfFirst { it.key == activeKey }.coerceAtLeast(0)) }

    val backgroundColor = when (type) {
        TabType.Card, TabType.EditableCard -> Color(0xFFF0F0F0)
        else -> Color.Transparent
    }

    // Parse tabBarExtraContent
    val extraContentLeft: (@Composable () -> Unit)? = when (tabBarExtraContent) {
        is TabBarExtraContent.Split -> tabBarExtraContent.left
        else -> null
    }
    val extraContentRight: (@Composable () -> Unit)? = when (tabBarExtraContent) {
        is TabBarExtraContent.Simple -> tabBarExtraContent.content
        is TabBarExtraContent.Split -> tabBarExtraContent.right
        is Function0<*> -> tabBarExtraContent as? @Composable () -> Unit
        else -> null
    }

    if (isVertical) {
        // Vertical tabs
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .then(tabBarStyle ?: Modifier)
                .then(styles?.nav ?: Modifier)
                .padding(if (type == TabType.Line) 0.dp else 2.dp)
        ) {
            items.forEachIndexed { index, item ->
                TabButton(
                    item = item,
                    isActive = item.key == activeKey,
                    onClick = {
                        onTabClick?.invoke(item.key, null)
                        if (!item.disabled) {
                            onTabChange(item.key)
                            focusedIndex = index
                        }
                    },
                    onClose = if (type == TabType.EditableCard && item.closable) {
                        { onEdit?.invoke(item.key, TabAction.Remove) }
                    } else null,
                    closeIcon = item.closeIcon ?: removeIcon,
                    type = type,
                    size = size,
                    position = position,
                    theme = theme,
                    classNames = classNames,
                    styles = styles,
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .focusable()
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = index
                        }
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                when (event.key) {
                                    Key.DirectionUp -> {
                                        val nextIndex = (focusedIndex - 1).coerceAtLeast(0)
                                        focusRequesters[nextIndex].requestFocus()
                                        true
                                    }

                                    Key.DirectionDown -> {
                                        val nextIndex = (focusedIndex + 1).coerceAtMost(items.size - 1)
                                        focusRequesters[nextIndex].requestFocus()
                                        true
                                    }

                                    Key.Enter, Key.Spacebar -> {
                                        if (!items[focusedIndex].disabled) {
                                            onTabClick?.invoke(items[focusedIndex].key, null)
                                            onTabChange(items[focusedIndex].key)
                                        }
                                        true
                                    }

                                    else -> false
                                }
                            } else false
                        }
                )
            }

            if (type == TabType.EditableCard && !hideAdd) {
                AddButton(
                    onClick = { onEdit?.invoke("", TabAction.Add) },
                    size = size,
                    isVertical = true,
                    customIcon = addIcon,
                    styles = styles
                )
            }

            extraContentLeft?.invoke()
            extraContentRight?.invoke()
        }
    } else {
        // Horizontal tabs
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .then(tabBarStyle ?: Modifier)
                .then(styles?.nav ?: Modifier)
        ) {
            extraContentLeft?.invoke()

            Row(
                modifier = Modifier
                    .then(if (centered) Modifier.align(Alignment.Center) else Modifier)
                    .then(styles?.navList ?: Modifier)
                    .padding(if (type == TabType.Line) 0.dp else 2.dp),
                horizontalArrangement = Arrangement.spacedBy(tabBarGutter ?: 0.dp)
            ) {
                items.forEachIndexed { index, item ->
                    TabButton(
                        item = item,
                        isActive = item.key == activeKey,
                        onClick = {
                            onTabClick?.invoke(item.key, null)
                            if (!item.disabled) {
                                onTabChange(item.key)
                                focusedIndex = index
                            }
                        },
                        onClose = if (type == TabType.EditableCard && item.closable) {
                            { onEdit?.invoke(item.key, TabAction.Remove) }
                        } else null,
                        closeIcon = item.closeIcon ?: removeIcon,
                        type = type,
                        size = size,
                        position = position,
                        theme = theme,
                        classNames = classNames,
                        styles = styles,
                        modifier = Modifier
                            .focusRequester(focusRequesters[index])
                            .focusable()
                            .onFocusChanged {
                                if (it.isFocused) focusedIndex = index
                            }
                            .onKeyEvent { event ->
                                if (event.type == KeyEventType.KeyDown) {
                                    when (event.key) {
                                        Key.DirectionLeft -> {
                                            val nextIndex = (focusedIndex - 1).coerceAtLeast(0)
                                            focusRequesters[nextIndex].requestFocus()
                                            true
                                        }

                                        Key.DirectionRight -> {
                                            val nextIndex = (focusedIndex + 1).coerceAtMost(items.size - 1)
                                            focusRequesters[nextIndex].requestFocus()
                                            true
                                        }

                                        Key.Enter, Key.Spacebar -> {
                                            if (!items[focusedIndex].disabled) {
                                                onTabClick?.invoke(items[focusedIndex].key, null)
                                                onTabChange(items[focusedIndex].key)
                                            }
                                            true
                                        }

                                        else -> false
                                    }
                                } else false
                            }
                            .onGloballyPositioned { coordinates ->
                                if (item.key == activeKey && type == TabType.Line) {
                                    with(density) {
                                        val fullWidth = coordinates.size.width.toDp()
                                        originalTabSize = fullWidth
                                        indicatorOffset = coordinates.positionInParent().x.toDp()
                                        indicatorSize = fullWidth
                                    }
                                }
                            }
                    )
                }

                if (type == TabType.EditableCard && !hideAdd) {
                    AddButton(
                        onClick = { onEdit?.invoke("", TabAction.Add) },
                        size = size,
                        isVertical = false,
                        customIcon = addIcon,
                        styles = styles
                    )
                }

                if (!centered) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            extraContentRight?.invoke()

            // Animated indicator for Line type
            if (type == TabType.Line && position == TabPosition.Top) {
                // Calculate indicator dimensions based on custom configuration
                val computedSize = indicator?.size?.invoke(originalTabSize) ?: 2.dp
                val indicatorColor = indicator?.color ?: theme.token.colorPrimary

                // Calculate offset based on alignment
                val alignedOffset = when (indicator?.align ?: TabIndicatorAlign.Center) {
                    TabIndicatorAlign.Start -> animatedOffset
                    TabIndicatorAlign.Center -> {
                        val centeringOffset =
                            (animatedSize - (indicator?.size?.invoke(originalTabSize) ?: animatedSize)) / 2
                        animatedOffset + centeringOffset
                    }

                    TabIndicatorAlign.End -> {
                        animatedOffset + animatedSize - (indicator?.size?.invoke(originalTabSize) ?: animatedSize)
                    }
                }

                val finalWidth = when (indicator?.align ?: TabIndicatorAlign.Center) {
                    TabIndicatorAlign.Center -> indicator?.size?.invoke(originalTabSize) ?: animatedSize
                    else -> animatedSize
                }

                Box(
                    modifier = Modifier
                        .offset(x = alignedOffset)
                        .width(finalWidth)
                        .height(computedSize)
                        .then(indicator?.style ?: Modifier)
                        .then(styles?.inkBar ?: Modifier)
                        .background(indicatorColor)
                        .align(Alignment.BottomStart)
                )
            }
        }

        // Bottom indicator for Line type with Bottom position
        if (type == TabType.Line && position == TabPosition.Bottom) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0xFFF0F0F0))
            )
        }
    }
}

@Composable
private fun TabButton(
    item: TabItem,
    isActive: Boolean,
    onClick: () -> Unit,
    onClose: (() -> Unit)?,
    closeIcon: (@Composable () -> Unit)?,
    type: TabType,
    size: TabSize,
    position: TabPosition,
    theme: AntThemeConfig,
    classNames: TabsClassNames?,
    styles: TabsStyles?,
    modifier: Modifier = Modifier,
) {
    val isVertical = position == TabPosition.Left || position == TabPosition.Right

    val backgroundColor = when {
        type == TabType.Card || type == TabType.EditableCard -> {
            if (isActive) Color.White else Color.Transparent
        }

        else -> Color.Transparent
    }

    val textColor = when {
        item.disabled -> Color(0xFFBFBFBF)
        isActive -> theme.token.colorPrimary
        else -> Color(0xFF000000D9)
    }

    val padding = when (size) {
        TabSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        TabSize.Middle -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        TabSize.Large -> PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    }

    val shape = when (type) {
        TabType.Card, TabType.EditableCard -> {
            when (position) {
                TabPosition.Top -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                TabPosition.Bottom -> RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                TabPosition.Left -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                TabPosition.Right -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            }
        }

        else -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = modifier
            .then(
                if (type == TabType.Card || type == TabType.EditableCard) {
                    Modifier.clip(shape)
                } else Modifier
            )
            .background(backgroundColor)
            .then(if (isActive) styles?.tabActive ?: Modifier else styles?.tab ?: Modifier)
            .then(styles?.tabBtn ?: Modifier)
            .clickable(
                enabled = !item.disabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(padding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(if (item.label.isNotEmpty() && item.icon != null) 8.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Render icon if present
            if (item.icon != null) {
                Box(
                    modifier = Modifier
                        .size(
                            when (size) {
                                TabSize.Small -> 14.dp
                                TabSize.Middle -> 16.dp
                                TabSize.Large -> 18.dp
                            }
                        )
                        .then(styles?.tabIcon ?: Modifier)
                ) {
                    item.icon.invoke()
                }
            }

            // Render label if not empty (support icon-only tabs)
            if (item.label.isNotEmpty()) {
                Text(
                    text = item.label,
                    color = textColor,
                    fontSize = when (size) {
                        TabSize.Small -> 12.sp
                        TabSize.Middle -> 14.sp
                        TabSize.Large -> 16.sp
                    },
                    fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                    modifier = styles?.tabLabel ?: Modifier
                )
            }

            // Render close icon/button if closable
            if (onClose != null) {
                Spacer(modifier = Modifier.width(4.dp))
                if (closeIcon != null) {
                    // Custom close icon
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .then(styles?.removeIcon ?: Modifier)
                            .clickable(onClick = onClose),
                        contentAlignment = Alignment.Center
                    ) {
                        closeIcon()
                    }
                } else {
                    // Default close button
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(16.dp)
                            .then(styles?.removeIcon ?: Modifier)
                    ) {
                        Text("×", fontSize = 16.sp, color = textColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddButton(
    onClick: () -> Unit,
    size: TabSize,
    isVertical: Boolean,
    customIcon: (@Composable () -> Unit)?,
    styles: TabsStyles?,
) {
    val padding = when (size) {
        TabSize.Small -> 8.dp
        TabSize.Middle -> 12.dp
        TabSize.Large -> 16.dp
    }

    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .then(styles?.addIcon ?: Modifier)
            .clickable(onClick = onClick)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        if (customIcon != null) {
            customIcon()
        } else {
            Text(
                text = "+",
                fontSize = when (size) {
                    TabSize.Small -> 14.sp
                    TabSize.Middle -> 16.sp
                    TabSize.Large -> 18.sp
                },
                color = Color(0xFF000000D9)
            )
        }
    }
}

@Composable
private fun TabContent(
    items: List<TabItem>,
    activeKey: String,
    animationConfig: TabsAnimated,
    destroyOnHidden: Boolean,
    styles: TabsStyles?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(styles?.content ?: Modifier)
            .padding(24.dp)
    ) {
        // Determine if tab pane animation is enabled
        val tabPaneAnimationEnabled = when (animationConfig) {
            is TabsAnimated.Disabled -> false
            is TabsAnimated.Simple -> animationConfig.tabPane
        }

        items.forEach { item ->
            // Determine if this specific item should be destroyed when hidden
            val shouldDestroy = item.destroyOnHidden || destroyOnHidden
            val isActive = item.key == activeKey

            // forceRender: render even when inactive (but may still be hidden)
            // destroyOnHidden: unmount when inactive
            val shouldRender = isActive || item.forceRender || !shouldDestroy

            if (shouldRender) {
                Box(
                    modifier = Modifier.then(styles?.panel ?: Modifier)
                ) {
                    if (tabPaneAnimationEnabled) {
                        AnimatedVisibility(
                            visible = isActive,
                            enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                        ) {
                            item.content()
                        }
                    } else {
                        if (isActive || item.forceRender) {
                            Box(modifier = if (isActive) Modifier else Modifier.then(Modifier)) {
                                item.content()
                            }
                        }
                    }
                }
            }
        }
    }
}

// Example demos

@Composable
fun TabsLineExample() {
    val items = listOf(
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

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        type = TabType.Line
    )
}

@Composable
fun TabsCardExample() {
    val items = listOf(
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

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        type = TabType.Card
    )
}

@Composable
fun TabsEditableCardExample() {
    var items by remember {
        mutableStateOf(
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
                    closable = false,
                    content = { Text("Content of Tab 3 (not closable)") }
                )
            )
        )
    }

    var activeKey by remember { mutableStateOf("1") }
    var newTabIndex by remember { mutableStateOf(4) }

    AntTabs(
        items = items,
        activeKey = activeKey,
        onChange = { activeKey = it },
        type = TabType.EditableCard,
        onEdit = { targetKey, action ->
            when (action) {
                TabAction.Add -> {
                    val newKey = newTabIndex.toString()
                    items = items + TabItem(
                        key = newKey,
                        label = "New Tab $newTabIndex",
                        content = { Text("Content of new tab $newTabIndex") }
                    )
                    activeKey = newKey
                    newTabIndex++
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
        }
    )
}

@Composable
fun TabsVerticalExample() {
    val items = listOf(
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

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        position = TabPosition.Left
    )
}

@Composable
fun TabsDisabledExample() {
    val items = listOf(
        TabItem(
            key = "1",
            label = "Tab 1",
            content = { Text("Content of Tab 1") }
        ),
        TabItem(
            key = "2",
            label = "Tab 2",
            disabled = true,
            content = { Text("Content of Tab 2 (disabled)") }
        ),
        TabItem(
            key = "3",
            label = "Tab 3",
            content = { Text("Content of Tab 3") }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1"
    )
}

@Composable
fun TabsCenteredWithExtraExample() {
    val items = listOf(
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

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        centered = true,
        tabBarExtraContent = @Composable {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(0xFF1890FF), RoundedCornerShape(4.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Extra Action", color = Color.White, fontSize = 14.sp)
            }
        }
    )
}

/**
 * Example: Custom Indicator
 * Demonstrates custom indicator size, alignment, and color.
 */
@Composable
fun TabsCustomIndicatorExample() {
    val items = listOf(
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

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        indicator = TabIndicator(
            size = { origin -> origin - 20.dp }, // Indicator 20dp shorter than tab
            align = TabIndicatorAlign.Center,
            color = Color(0xFFFF4D4F),
            style = Modifier.clip(RoundedCornerShape(2.dp))
        )
    )
}

/**
 * Example: Icon Tabs
 * Demonstrates tabs with icons and icon-only tabs.
 */
@Composable
fun TabsIconExample() {
    val items = listOf(
        TabItem(
            key = "1",
            label = "Account",
            icon = { Text("👤") },
            content = { Text("Account Content") }
        ),
        TabItem(
            key = "2",
            label = "Settings",
            icon = { Text("⚙️") },
            content = { Text("Settings Content") }
        ),
        TabItem(
            key = "3",
            label = "", // Icon-only tab
            icon = { Text("🔔") },
            content = { Text("Notifications Content") }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1"
    )
}

/**
 * Example: Advanced Animation Control
 * Demonstrates fine-grained control over animations.
 */
@Composable
fun TabsAdvancedAnimationExample() {
    var inkBarEnabled by remember { mutableStateOf(true) }
    var tabPaneEnabled by remember { mutableStateOf(true) }

    val items = listOf(
        TabItem(
            key = "1",
            label = "Tab 1",
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFE6F7FF))
                ) {
                    Text("Content 1", modifier = Modifier.align(Alignment.Center))
                }
            }
        ),
        TabItem(
            key = "2",
            label = "Tab 2",
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFFFF7E6))
                ) {
                    Text("Content 2", modifier = Modifier.align(Alignment.Center))
                }
            }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        animated = TabsAnimated.Simple(
            inkBar = inkBarEnabled,
            tabPane = tabPaneEnabled
        )
    )
}

/**
 * Example: Destroy Inactive Tab Pane
 * Demonstrates performance optimization by unmounting inactive tabs.
 */
@Composable
fun TabsDestroyInactiveExample() {
    val items = listOf(
        TabItem(
            key = "1",
            label = "Tab 1",
            content = {
                // Expensive content that should be unmounted when inactive
                Text("Expensive Content 1 - Will be destroyed when inactive")
            }
        ),
        TabItem(
            key = "2",
            label = "Tab 2",
            content = {
                Text("Expensive Content 2 - Will be destroyed when inactive")
            }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        destroyInactiveTabPane = true
    )
}

/**
 * Example: Custom Tab Bar with renderTabBar
 * Demonstrates wrapping the tab bar for sticky behavior or custom styling.
 */
@Composable
fun TabsCustomTabBarExample() {
    val items = listOf(
        TabItem(
            key = "1",
            label = "Tab 1",
            content = {
                Column {
                    repeat(20) {
                        Text("Line $it", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        ),
        TabItem(
            key = "2",
            label = "Tab 2",
            content = { Text("Content of Tab 2") }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        renderTabBar = { defaultTabBar ->
            // Wrap the default tab bar with custom styling
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            ) {
                defaultTabBar()
            }
        }
    )
}

/**
 * Example: Callbacks (onTabClick, onChange)
 * Demonstrates the difference between onTabClick and onChange.
 */
@Composable
fun TabsCallbacksExample() {
    var clickLog by remember { mutableStateOf("") }
    var changeLog by remember { mutableStateOf("") }

    val items = listOf(
        TabItem(
            key = "1",
            label = "Tab 1",
            content = {
                Column {
                    Text("Click Log: $clickLog")
                    Text("Change Log: $changeLog")
                }
            }
        ),
        TabItem(
            key = "2",
            label = "Tab 2",
            content = { Text("Content of Tab 2") }
        )
    )

    AntTabs(
        items = items,
        defaultActiveKey = "1",
        onTabClick = { key, _ ->
            clickLog = "Clicked: $key"
        },
        onChange = { key ->
            changeLog = "Changed to: $key"
        }
    )
}
