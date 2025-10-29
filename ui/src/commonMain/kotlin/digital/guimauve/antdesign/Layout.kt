package digital.guimauve.antdesign

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import digital.guimauve.antdesign.icons.LeftArrowIcon
import digital.guimauve.antdesign.icons.MenuIcon
import digital.guimauve.antdesign.icons.RightArrowIcon

/**
 * Layout Context for managing siders.
 * Mimics React's LayoutContext for tracking sider components.
 */
data class LayoutContextState(
    val siders: MutableList<String> = mutableListOf(),
)

val LocalLayoutContext = compositionLocalOf { LayoutContextState() }

/**
 * ID generator for siders.
 */
private var siderIdCounter = 0
private fun generateSiderId(): String {
    return "sider-${++siderIdCounter}"
}

/**
 * Sider Context for nested components.
 * Provides collapsed state to child components.
 */
data class SiderContextState(
    val siderCollapsed: Boolean = false,
)

val LocalSiderContext = compositionLocalOf { SiderContextState() }

/**
 * Breakpoint types for responsive layouts.
 * Matches Ant Design breakpoints: xs, sm, md, lg, xl, xxl
 */
enum class BreakpointType(val maxWidthDp: Dp) {
    XS(480.dp),
    SM(576.dp),
    MD(768.dp),
    LG(992.dp),
    XL(1200.dp),
    XXL(1600.dp)
}

/**
 * Sider theme (light or dark).
 */
enum class SiderTheme {
    Light,
    Dark
}

/**
 * Collapse type for onCollapse callback.
 */
enum class CollapseType {
    ClickTrigger,
    Responsive
}

/**
 * Main Layout component.
 * Container for the entire page layout. Automatically detects if sider is present
 * and adjusts flex direction accordingly.
 *
 * @param modifier Modifier to apply to the layout
 * @param className CSS class name for the container
 * @param rootClassName Root element CSS class name
 * @param prefixCls Custom prefix for CSS class names
 * @param suffixCls Suffix modifier for CSS class names
 * @param hasSider Force layout to have sider mode (Row). If null, auto-detects.
 * @param style Additional styling options
 * @param classNames Semantic class names for different parts of the layout
 * @param styles Semantic styles (Modifiers) for different parts of the layout
 * @param content Layout content
 */
@Composable
fun AntLayout(
    modifier: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String? = null,
    suffixCls: String? = null,
    hasSider: Boolean? = null,
    style: LayoutStyle? = null,
    classNames: LayoutClassNames? = null,
    styles: LayoutStyles? = null,
    content: @Composable () -> Unit,
) {
    val config = useConfig()
    val layoutToken = config.theme.components.layout
    val layoutContext = remember { LayoutContextState() }

    // Auto-detect if sider is present
    val mergedHasSider = hasSider ?: layoutContext.siders.isNotEmpty()

    val backgroundColor = style?.backgroundColor ?: layoutToken.bodyBg

    // Apply semantic styles
    val contentModifier = styles?.content ?: Modifier

    CompositionLocalProvider(LocalLayoutContext provides layoutContext) {
        if (mergedHasSider) {
            // Row layout when sider is present
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .then(contentModifier)
            ) {
                content()
            }
        } else {
            // Column layout by default
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .then(contentModifier)
            ) {
                content()
            }
        }
    }
}

/**
 * Layout Header component.
 * Typically contains logo and horizontal menu.
 *
 * @param modifier Modifier to apply to the header
 * @param className CSS class name for the container
 * @param rootClassName Root element CSS class name
 * @param prefixCls Custom prefix for CSS class names
 * @param suffixCls Suffix modifier for CSS class names
 * @param style Header style options
 * @param classNames Semantic class names for different parts of the header
 * @param styles Semantic styles (Modifiers) for different parts of the header
 * @param content Header content
 */
@Composable
fun AntLayoutHeader(
    modifier: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String? = null,
    suffixCls: String? = null,
    style: HeaderStyle? = null,
    classNames: HeaderClassNames? = null,
    styles: HeaderStyles? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val config = useConfig()
    val layoutToken = config.theme.components.layout

    val height = style?.height ?: layoutToken.headerHeight
    val backgroundColor = style?.backgroundColor ?: layoutToken.headerBg
    val paddingInline = style?.paddingInline ?: layoutToken.headerPaddingInline
    val textColor = style?.textColor ?: layoutToken.headerColor

    // Apply semantic styles
    val contentModifier = styles?.content ?: Modifier

    CompositionLocalProvider(LocalContentColor provides textColor) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(backgroundColor)
                .padding(horizontal = paddingInline)
                .then(contentModifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

/**
 * Layout Sider component (Sidebar).
 * Collapsible sidebar with animation, responsive breakpoints, and themes.
 *
 * @param modifier Modifier to apply to the sider
 * @param className CSS class name for the container
 * @param rootClassName Root element CSS class name
 * @param prefixCls Custom prefix for CSS class names
 * @param suffixCls Suffix modifier for CSS class names
 * @param width Width when expanded (default: 200.dp)
 * @param collapsedWidth Width when collapsed (default: 80.dp). Set to 0.dp to show zero-width trigger.
 * @param collapsed Current collapsed state (controlled). If null, component manages its own state.
 * @param defaultCollapsed Default collapsed state (uncontrolled) when collapsed is null (default: false)
 * @param onCollapse Callback when collapse state changes, receives (collapsed, type) where type is either ClickTrigger or Responsive
 * @param collapsible Whether sider can be collapsed (default: false)
 * @param reverseArrow Direction of collapse arrow (default: false). When true, arrow points in opposite direction.
 * @param trigger Custom trigger component. Set to null to hide default trigger. Provide a composable to customize.
 * @param theme Sider theme (Light or Dark) (default: Dark)
 * @param breakpoint Breakpoint for responsive collapse (xs, sm, md, lg, xl, xxl). When window width is below breakpoint, sider auto-collapses.
 * @param onBreakpoint Callback when breakpoint is triggered, receives (broken) where broken=true when below breakpoint
 * @param zeroWidthTriggerStyle Custom styling modifier for zero-width trigger button. Applied when collapsedWidth = 0.dp.
 * @param classNames Semantic class names for different parts of the sider
 * @param styles Semantic styles (Modifiers) for different parts of the sider
 * @param content Sider content
 */
@Composable
fun AntLayoutSider(
    modifier: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String? = null,
    suffixCls: String? = null,
    width: Dp = 200.dp,
    collapsedWidth: Dp = 80.dp,
    collapsed: Boolean? = null,
    defaultCollapsed: Boolean = false,
    onCollapse: ((collapsed: Boolean, type: CollapseType) -> Unit)? = null,
    collapsible: Boolean = false,
    reverseArrow: Boolean = false,
    trigger: (@Composable () -> Unit)? = null,
    theme: SiderTheme = SiderTheme.Dark,
    breakpoint: BreakpointType? = null,
    onBreakpoint: ((broken: Boolean) -> Unit)? = null,
    zeroWidthTriggerStyle: Modifier? = null,
    classNames: SiderClassNames? = null,
    styles: SiderStyles? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val config = useConfig()
    val layoutToken = config.theme.components.layout
    val layoutContext = LocalLayoutContext.current

    // State management
    var internalCollapsed by remember { mutableStateOf(defaultCollapsed) }
    val actualCollapsed = collapsed ?: internalCollapsed

    var below by remember { mutableStateOf(false) }

    // Register sider with layout context
    DisposableEffect(Unit) {
        val siderId = generateSiderId()
        layoutContext.siders.add(siderId)
        onDispose {
            layoutContext.siders.remove(siderId)
        }
    }

    // Handle collapse
    val handleSetCollapsed: (Boolean, CollapseType) -> Unit = { value, type ->
        if (collapsed == null) {
            internalCollapsed = value
        }
        onCollapse?.invoke(value, type)
    }

    // Responsive breakpoint handling
    val density = LocalDensity.current
    LaunchedEffect(breakpoint) {
        if (breakpoint != null) {
            // In a real implementation, you would use platform-specific window size detection
            // For now, we'll simulate with a simple check
            // You can hook this up to actual window width in platform-specific code
            val screenWidth = 1024.dp // Placeholder - should come from actual screen width
            val isBelowBreakpoint = screenWidth < breakpoint.maxWidthDp

            below = isBelowBreakpoint
            onBreakpoint?.invoke(isBelowBreakpoint)

            if (actualCollapsed != isBelowBreakpoint) {
                handleSetCollapsed(isBelowBreakpoint, CollapseType.Responsive)
            }
        }
    }

    // Animated width
    val animatedWidth by animateDpAsState(
        targetValue = if (actualCollapsed) collapsedWidth else width,
        animationSpec = tween(durationMillis = 200)
    )

    // Theme colors
    val backgroundColor = when (theme) {
        SiderTheme.Dark -> layoutToken.siderBg
        SiderTheme.Light -> layoutToken.lightSiderBg
    }

    val triggerBg = when (theme) {
        SiderTheme.Dark -> layoutToken.triggerBg
        SiderTheme.Light -> layoutToken.lightTriggerBg
    }

    val triggerColor = when (theme) {
        SiderTheme.Dark -> layoutToken.triggerColor
        SiderTheme.Light -> layoutToken.lightTriggerColor
    }

    // Toggle function
    val toggle = {
        handleSetCollapsed(!actualCollapsed, CollapseType.ClickTrigger)
    }

    // Zero-width trigger (when collapsedWidth is 0)
    // In React, this appears as a floating button when collapsed to 0 width
    val isZeroWidth = collapsedWidth == 0.dp
    val zeroWidthTriggerComponent = if (isZeroWidth) {
        @Composable {
            val defaultZeroWidthModifier = Modifier
                .size(
                    width = layoutToken.zeroTriggerWidth,
                    height = layoutToken.zeroTriggerHeight
                )
                .background(backgroundColor)

            Box(
                modifier = (zeroWidthTriggerStyle ?: defaultZeroWidthModifier)
                    .clickable { toggle() },
                contentAlignment = Alignment.Center
            ) {
                if (trigger != null) {
                    trigger()
                } else {
                    MenuIcon(
                        color = triggerColor,
                        size = layoutToken.zeroTriggerWidth * 0.6f
                    )
                }
            }
        }
    } else null

    // Default trigger icon - arrow direction changes based on collapsed state and reverseArrow
    val defaultTriggerIcon: @Composable () -> Unit = {
        val iconColor = triggerColor
        if (actualCollapsed) {
            if (reverseArrow) {
                LeftArrowIcon(color = iconColor, size = 16.dp)
            } else {
                RightArrowIcon(color = iconColor, size = 16.dp)
            }
        } else {
            if (reverseArrow) {
                RightArrowIcon(color = iconColor, size = 16.dp)
            } else {
                LeftArrowIcon(color = iconColor, size = 16.dp)
            }
        }
    }

    val triggerComponent = if (!collapsible) {
        null
    } else if (isZeroWidth) {
        zeroWidthTriggerComponent
    } else {
        @Composable {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(layoutToken.triggerHeight)
                    .background(triggerBg)
                    .clickable { toggle() },
                contentAlignment = Alignment.Center
            ) {
                if (trigger != null) {
                    trigger()
                } else {
                    defaultTriggerIcon()
                }
            }
        }
    }

    // Determine if we should show trigger
    // In React: {collapsible || (below && zeroWidthTrigger) ? triggerDom : null}
    val shouldShowTrigger = collapsible || (below && zeroWidthTriggerComponent != null)

    // Select which trigger to render
    val triggerToRender = if (shouldShowTrigger) {
        if (zeroWidthTriggerComponent != null) {
            zeroWidthTriggerComponent
        } else {
            triggerComponent
        }
    } else {
        null
    }

    // Apply semantic styles
    val contentModifier = styles?.content ?: Modifier
    val triggerModifier = styles?.trigger ?: Modifier

    // Provide sider context
    CompositionLocalProvider(
        LocalSiderContext provides SiderContextState(siderCollapsed = actualCollapsed)
    ) {
        Box {
            // Main sider column
            Column(
                modifier = modifier
                    .width(animatedWidth)
                    .fillMaxHeight()
                    .background(backgroundColor)
                    .then(contentModifier)
            ) {
                // Sider content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    content()
                }

                // Regular trigger (if not zero-width)
                if (shouldShowTrigger && !isZeroWidth && triggerComponent != null) {
                    Box(modifier = triggerModifier) {
                        triggerComponent.invoke()
                    }
                }
            }

            // Zero-width trigger (shown as floating button outside sider)
            // Rendered when collapsedWidth is 0 and (collapsed OR below breakpoint)
            if (isZeroWidth && zeroWidthTriggerComponent != null && (actualCollapsed || below)) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(if (reverseArrow) Alignment.CenterStart else Alignment.CenterEnd)
                        .then(triggerModifier)
                ) {
                    zeroWidthTriggerComponent.invoke()
                }
            }
        }
    }
}

/**
 * Layout Content component.
 * Main content area that takes remaining space.
 *
 * @param modifier Modifier to apply to the content
 * @param className CSS class name for the container
 * @param rootClassName Root element CSS class name
 * @param prefixCls Custom prefix for CSS class names
 * @param suffixCls Suffix modifier for CSS class names
 * @param scrollable Whether content should be scrollable
 * @param style Content style options
 * @param classNames Semantic class names for different parts of the content
 * @param styles Semantic styles (Modifiers) for different parts of the content
 * @param content Content to display
 */
@Composable
fun AntLayoutContent(
    modifier: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String? = null,
    suffixCls: String? = null,
    scrollable: Boolean = false,
    style: ContentStyle? = null,
    classNames: ContentClassNames? = null,
    styles: ContentStyles? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val config = useConfig()
    val layoutToken = config.theme.components.layout

    val backgroundColor = style?.backgroundColor ?: layoutToken.bodyBg
    val padding = style?.padding ?: 24.dp

    val scrollModifier = if (scrollable) {
        Modifier.verticalScroll(rememberScrollState())
    } else {
        Modifier
    }

    // Apply semantic styles
    val contentModifier = styles?.content ?: Modifier

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(scrollModifier)
            .padding(padding)
            .then(contentModifier)
    ) {
        content()
    }
}

/**
 * Layout Footer component.
 * Typically contains copyright, links, etc.
 *
 * @param modifier Modifier to apply to the footer
 * @param className CSS class name for the container
 * @param rootClassName Root element CSS class name
 * @param prefixCls Custom prefix for CSS class names
 * @param suffixCls Suffix modifier for CSS class names
 * @param style Footer style options
 * @param classNames Semantic class names for different parts of the footer
 * @param styles Semantic styles (Modifiers) for different parts of the footer
 * @param content Footer content
 */
@Composable
fun AntLayoutFooter(
    modifier: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String? = null,
    suffixCls: String? = null,
    style: FooterStyle? = null,
    classNames: FooterClassNames? = null,
    styles: FooterStyles? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val config = useConfig()
    val layoutToken = config.theme.components.layout

    val backgroundColor = style?.backgroundColor ?: layoutToken.footerBg
    val padding = style?.padding ?: layoutToken.footerPadding

    // Apply semantic styles
    val contentModifier = styles?.content ?: Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(padding)
            .then(contentModifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

// ==================== Style Classes ====================

/**
 * Layout style options.
 */
data class LayoutStyle(
    val backgroundColor: Color? = null,
)

/**
 * Header style options.
 */
data class HeaderStyle(
    val height: Dp? = null,
    val backgroundColor: Color? = null,
    val paddingInline: Dp? = null,
    val textColor: Color? = null,
)

/**
 * Content style options.
 */
data class ContentStyle(
    val backgroundColor: Color? = null,
    val padding: Dp? = null,
)

/**
 * Footer style options.
 */
data class FooterStyle(
    val backgroundColor: Color? = null,
    val padding: Dp? = null,
)

// ==================== Semantic Styling Classes ====================

/**
 * Semantic class names for Layout component.
 * Allows customizing CSS classes for different parts of the layout.
 *
 * @param content Class name for the main content area
 */
data class LayoutClassNames(
    val content: String? = null,
)

/**
 * Semantic styles (Modifiers) for Layout component.
 * Allows customizing Modifiers for different parts of the layout.
 *
 * @param content Modifier for the main content area
 */
data class LayoutStyles(
    val content: Modifier? = null,
)

/**
 * Semantic class names for Header component.
 *
 * @param content Class name for the header content area
 */
data class HeaderClassNames(
    val content: String? = null,
)

/**
 * Semantic styles (Modifiers) for Header component.
 *
 * @param content Modifier for the header content area
 */
data class HeaderStyles(
    val content: Modifier? = null,
)

/**
 * Semantic class names for Content component.
 *
 * @param content Class name for the content area
 */
data class ContentClassNames(
    val content: String? = null,
)

/**
 * Semantic styles (Modifiers) for Content component.
 *
 * @param content Modifier for the content area
 */
data class ContentStyles(
    val content: Modifier? = null,
)

/**
 * Semantic class names for Footer component.
 *
 * @param content Class name for the footer content area
 */
data class FooterClassNames(
    val content: String? = null,
)

/**
 * Semantic styles (Modifiers) for Footer component.
 *
 * @param content Modifier for the footer content area
 */
data class FooterStyles(
    val content: Modifier? = null,
)

/**
 * Semantic class names for Sider component.
 *
 * @param content Class name for the sider content area
 * @param trigger Class name for the collapse trigger
 */
data class SiderClassNames(
    val content: String? = null,
    val trigger: String? = null,
)

/**
 * Semantic styles (Modifiers) for Sider component.
 *
 * @param content Modifier for the sider content area
 * @param trigger Modifier for the collapse trigger
 */
data class SiderStyles(
    val content: Modifier? = null,
    val trigger: Modifier? = null,
)

// ==================== Helper Composables ====================

/**
 * Helper to get current sider collapsed state from context.
 * Use this in child components to access the parent Sider's collapsed state.
 *
 * @return Boolean indicating whether the parent Sider is collapsed
 */
@Composable
fun useSiderCollapsed(): Boolean {
    return LocalSiderContext.current.siderCollapsed
}
