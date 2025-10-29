package com.antdesign.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Collapse collapsible type enumeration
 * Determines what triggers panel expansion/collapse
 *
 * Matches React Ant Design CollapsibleType
 * @since 1.0.0
 */
enum class CollapseCollapsibleType {
    /** Click entire header area to toggle */
    Header,
    /** Click only the icon to toggle */
    Icon,
    /** Panel cannot be toggled */
    Disabled
}

/**
 * Position of the expand icon
 *
 * Matches React Ant Design ExpandIconPosition
 * @since 1.0.0
 */
enum class ExpandIconPosition {
    /** Icon at the start (left in LTR, right in RTL) */
    Start,
    /** Icon at the end (right in LTR, left in RTL) */
    End
}

/**
 * Semantic class names for Collapse sub-elements (v5.5.0+)
 * In Compose/KMP, class names are informational only.
 * Use [CollapseStyles] for actual styling.
 *
 * Matches React Ant Design's CollapseProps.classNames interface
 *
 * @property header Custom class name for header wrapper (informational)
 * @property content Custom class name for content wrapper (informational)
 * @property item Custom class name for each collapse item (informational)
 * @since 5.5.0
 */
data class CollapseClassNames(
    val header: String? = null,
    val content: String? = null,
    val item: String? = null
)

/**
 * Semantic modifiers for Collapse sub-elements (v5.5.0+)
 * Allows targeted styling using Compose modifiers
 *
 * Matches React Ant Design's CollapseProps.styles interface, adapted for Compose
 *
 * @property header Custom modifier for header wrapper
 * @property content Custom modifier for content wrapper
 * @property item Custom modifier for each collapse item
 * @since 5.5.0
 */
data class CollapseStyles(
    val header: Modifier? = null,
    val content: Modifier? = null,
    val item: Modifier? = null
)

/**
 * Semantic class names for Panel sub-elements (v5.5.0+)
 * In Compose/KMP, class names are informational only.
 * Use [PanelStyles] for actual styling.
 *
 * Matches React Ant Design's CollapsePanelProps.classNames interface
 *
 * @property header Custom class name for panel header (informational)
 * @property body Custom class name for panel body wrapper (informational)
 * @property content Custom class name for panel content (informational)
 * @since 5.5.0
 */
data class PanelClassNames(
    val header: String? = null,
    val body: String? = null,
    val content: String? = null
)

/**
 * Semantic modifiers for Panel sub-elements (v5.5.0+)
 * Allows targeted styling using Compose modifiers
 *
 * Matches React Ant Design's CollapsePanelProps.styles interface, adapted for Compose
 *
 * @property header Custom modifier for panel header
 * @property body Custom modifier for panel body wrapper
 * @property content Custom modifier for panel content
 * @since 5.5.0
 */
data class PanelStyles(
    val header: Modifier? = null,
    val body: Modifier? = null,
    val content: Modifier? = null
)

/**
 * Props for expand icon rendering
 * Passed to custom expandIcon function
 *
 * @property isActive Whether the panel is currently expanded
 * @property disabled Whether the panel is disabled
 * @property panelKey The unique key of the panel
 */
data class PanelProps(
    val isActive: Boolean,
    val disabled: Boolean = false,
    val panelKey: String
)

/**
 * Item-based API for Collapse panels (v5.6.0+)
 * Alternative to using CollapsePanel children
 *
 * Matches React Ant Design's CollapsibleType interface
 *
 * @property key Unique identifier for the panel (required)
 * @property label Panel header content
 * @property children Panel content
 * @property collapsible Override parent collapsible setting for this panel
 * @property disabled Disable this panel
 * @property extra Extra content in header (right side)
 * @property forceRender Force render content even when collapsed
 * @property showArrow Show expand icon
 * @property classNames Semantic class names for this panel
 * @property styles Semantic styles for this panel
 * @since 5.6.0
 */
data class CollapseItemType(
    val key: String,
    val label: @Composable () -> Unit,
    val children: @Composable () -> Unit,
    val collapsible: CollapseCollapsibleType? = null,
    val disabled: Boolean = false,
    val extra: (@Composable () -> Unit)? = null,
    val forceRender: Boolean = false,
    val showArrow: Boolean = true,
    val classNames: PanelClassNames? = null,
    val styles: PanelStyles? = null
)

/**
 * Ant Design Collapse component for Compose Multiplatform
 * Full feature parity with React Ant Design v5.x
 *
 * A content area which can be collapsed and expanded.
 *
 * ## When To Use
 * - Can be used to group or hide complex regions to keep the page clean.
 * - Accordion is a special kind of Collapse, which allows only one panel to be expanded at a time.
 *
 * ## Two APIs
 * - **Traditional API**: Use CollapsePanel children
 * - **Items API (v5.6.0+)**: Use items list for configuration
 *
 * @param modifier Modifier to be applied to the collapse container
 * @param activeKey Controlled active panels (list of keys). Use with onChange for controlled mode
 * @param defaultActiveKey Uncontrolled default active panels (list of keys)
 * @param accordion Accordion mode - only one panel can be expanded at a time
 * @param bordered Show border around collapse
 * @param collapsible What triggers panel expansion (header, icon, disabled)
 * @param destroyInactivePanel Destroy inactive panel content for performance (deprecated, use destroyOnHidden)
 * @param destroyOnHidden Destroy hidden panel content for performance
 * @param expandIcon Custom expand icon renderer. Receives PanelProps with isActive state
 * @param expandIconPosition Position of expand icon (start or end)
 * @param ghost Transparent background, no border
 * @param size Component size (small, middle, large)
 * @param onChange Callback when active panels change
 * @param classNames Semantic class names for sub-elements (v5.5.0+)
 * @param styles Semantic styles for sub-elements (v5.5.0+)
 * @param items Item-based API (v5.6.0+) - alternative to children
 * @param children Traditional API - CollapsePanel children
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/collapse">Ant Design Collapse</a>
 */
@Composable
fun AntCollapse(
    modifier: Modifier = Modifier,
    activeKey: List<String>? = null,
    defaultActiveKey: List<String> = emptyList(),
    accordion: Boolean = false,
    bordered: Boolean = true,
    collapsible: CollapseCollapsibleType = CollapseCollapsibleType.Header,
    destroyInactivePanel: Boolean = false,
    destroyOnHidden: Boolean = false,
    expandIcon: (@Composable (PanelProps) -> Unit)? = null,
    expandIconPosition: ExpandIconPosition = ExpandIconPosition.Start,
    ghost: Boolean = false,
    size: ComponentSize = ComponentSize.Middle,
    onChange: ((List<String>) -> Unit)? = null,
    classNames: CollapseClassNames? = null,
    styles: CollapseStyles? = null,
    items: List<CollapseItemType>? = null,
    children: @Composable CollapseScope.() -> Unit = {}
) {
    val theme = useTheme()
    val config = useConfig()

    // Determine effective size
    val effectiveSize = if (size == ComponentSize.Middle) config.componentSize else size

    // Controlled vs uncontrolled state management
    var internalActiveKeys by remember { mutableStateOf(defaultActiveKey) }
    val currentActiveKeys = activeKey ?: internalActiveKeys

    // Collect panels from children
    val scope = remember { CollapseScopeImpl() }
    children(scope)
    val childPanels = scope.panels

    // Use items if provided, otherwise use children
    val panels = items ?: childPanels

    // Calculate background and border colors
    val backgroundColor = if (ghost) Color.Transparent else theme.token.colorBgBase
    val borderColor = if (ghost) Color.Transparent else Color(0xFFD9D9D9)

    val handlePanelToggle: (String, Boolean, CollapseCollapsibleType?) -> Unit = handlePanelToggle@{ key, isDisabled, panelCollapsible ->
        if (isDisabled || panelCollapsible == CollapseCollapsibleType.Disabled || collapsible == CollapseCollapsibleType.Disabled) {
            return@handlePanelToggle
        }

        val newActiveKeys = if (accordion) {
            // Accordion mode: only one panel open
            if (currentActiveKeys.contains(key)) emptyList() else listOf(key)
        } else {
            // Normal mode: multiple panels can be open
            if (currentActiveKeys.contains(key)) {
                currentActiveKeys - key
            } else {
                currentActiveKeys + key
            }
        }

        // Update state
        if (activeKey == null) {
            internalActiveKeys = newActiveKeys
        }
        onChange?.invoke(newActiveKeys)
    }

    Column(
        modifier = modifier
            .then(if (bordered && !ghost) Modifier.border(1.dp, borderColor, RoundedCornerShape(theme.token.borderRadius)) else Modifier)
            .background(backgroundColor)
            .clip(RoundedCornerShape(theme.token.borderRadius))
            .then(styles?.item ?: Modifier)
    ) {
        panels.forEachIndexed { index, panel ->
            val isActive = currentActiveKeys.contains(panel.key)
            val isLastPanel = index == panels.lastIndex
            val panelCollapsible = panel.collapsible ?: collapsible
            val isDisabled = panel.disabled

            CollapsePanel(
                key = panel.key,
                isActive = isActive,
                isLastPanel = isLastPanel,
                header = panel.label,
                content = panel.children,
                extra = panel.extra,
                showArrow = panel.showArrow,
                disabled = isDisabled,
                collapsible = panelCollapsible,
                forceRender = panel.forceRender,
                bordered = bordered,
                ghost = ghost,
                size = effectiveSize,
                expandIcon = expandIcon,
                expandIconPosition = expandIconPosition,
                destroyInactive = destroyInactivePanel || destroyOnHidden,
                theme = theme,
                panelClassNames = panel.classNames,
                panelStyles = panel.styles,
                collapseClassNames = classNames,
                collapseStyles = styles,
                onToggle = { handlePanelToggle(panel.key, isDisabled, panelCollapsible) }
            )
        }
    }
}

/**
 * Internal CollapsePanel implementation
 * Renders a single collapsible panel with header and content
 */
@Composable
private fun CollapsePanel(
    key: String,
    isActive: Boolean,
    isLastPanel: Boolean,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    extra: (@Composable () -> Unit)?,
    showArrow: Boolean,
    disabled: Boolean,
    collapsible: CollapseCollapsibleType,
    forceRender: Boolean,
    bordered: Boolean,
    ghost: Boolean,
    size: ComponentSize,
    expandIcon: (@Composable (PanelProps) -> Unit)?,
    expandIconPosition: ExpandIconPosition,
    destroyInactive: Boolean,
    theme: AntThemeConfig,
    panelClassNames: PanelClassNames?,
    panelStyles: PanelStyles?,
    collapseClassNames: CollapseClassNames?,
    collapseStyles: CollapseStyles?,
    onToggle: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    // Calculate padding based on size
    val headerPadding = when (size) {
        ComponentSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ComponentSize.Middle -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ComponentSize.Large -> PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    }

    val contentPadding = when (size) {
        ComponentSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        ComponentSize.Middle -> PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ComponentSize.Large -> PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    }

    val fontSize = when (size) {
        ComponentSize.Small -> 12.sp
        ComponentSize.Middle -> 14.sp
        ComponentSize.Large -> 16.sp
    }

    // Background colors
    val headerBgColor = if (ghost) Color.Transparent else Color(0xFFFAFAFA)
    val contentBgColor = if (ghost) Color.Transparent else theme.token.colorBgBase
    val borderColor = if (ghost) Color.Transparent else Color(0xFFD9D9D9)

    // Clickable behavior based on collapsible type
    val canClickHeader = collapsible == CollapseCollapsibleType.Header && !disabled
    val canClickIcon = collapsible != CollapseCollapsibleType.Disabled && !disabled

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBgColor)
                .then(if (canClickHeader) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = true,
                            role = Role.Button,
                            onClick = onToggle
                        )
                        .focusRequester(focusRequester)
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown &&
                                (event.key == Key.Enter || event.key == Key.Spacebar)) {
                                onToggle()
                                true
                            } else false
                        }
                        .semantics { role = Role.Button }
                } else Modifier)
                .padding(headerPadding)
                .then(collapseStyles?.header ?: Modifier)
                .then(panelStyles?.header ?: Modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Expand icon at start
                if (showArrow && expandIconPosition == ExpandIconPosition.Start) {
                    ExpandIconWrapper(
                        isActive = isActive,
                        disabled = disabled,
                        panelKey = key,
                        expandIcon = expandIcon,
                        canClick = canClickIcon && collapsible == CollapseCollapsibleType.Icon,
                        onClick = if (collapsible == CollapseCollapsibleType.Icon) onToggle else null
                    )
                }

                // Header content
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    CompositionLocalProvider(
                        androidx.compose.material3.LocalTextStyle provides TextStyle(
                            fontSize = fontSize,
                            color = if (disabled) Color.Gray else theme.token.colorTextBase,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        header()
                    }
                }

                // Expand icon at end
                if (showArrow && expandIconPosition == ExpandIconPosition.End) {
                    ExpandIconWrapper(
                        isActive = isActive,
                        disabled = disabled,
                        panelKey = key,
                        expandIcon = expandIcon,
                        canClick = canClickIcon && collapsible == CollapseCollapsibleType.Icon,
                        onClick = if (collapsible == CollapseCollapsibleType.Icon) onToggle else null
                    )
                }
            }

            // Extra content
            if (extra != null) {
                Box(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    extra()
                }
            }
        }

        // Content
        val shouldRenderContent = forceRender || isActive || !destroyInactive

        if (shouldRenderContent) {
            AnimatedVisibility(
                visible = isActive,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(contentBgColor)
                        .then(panelStyles?.body ?: Modifier)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentPadding)
                            .then(collapseStyles?.content ?: Modifier)
                            .then(panelStyles?.content ?: Modifier)
                    ) {
                        CompositionLocalProvider(
                            androidx.compose.material3.LocalTextStyle provides TextStyle(
                                fontSize = fontSize,
                                color = theme.token.colorTextBase
                            )
                        ) {
                            content()
                        }
                    }
                }
            }
        }

        // Divider between panels
        if (!isLastPanel && bordered && !ghost) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(borderColor)
            )
        }
    }
}

/**
 * Expand icon wrapper with rotation animation
 */
@Composable
private fun ExpandIconWrapper(
    isActive: Boolean,
    disabled: Boolean,
    panelKey: String,
    expandIcon: (@Composable (PanelProps) -> Unit)?,
    canClick: Boolean,
    onClick: (() -> Unit)?
) {
    val rotation by animateFloatAsState(
        targetValue = if (isActive) 90f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "icon_rotation"
    )

    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(24.dp)
            .then(if (canClick && onClick != null) {
                Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown &&
                            (event.key == Key.Enter || event.key == Key.Spacebar)) {
                            onClick()
                            true
                        } else false
                    }
            } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.rotate(rotation)) {
            if (expandIcon != null) {
                expandIcon(PanelProps(isActive = isActive, disabled = disabled, panelKey = panelKey))
            } else {
                DefaultExpandIcon(disabled = disabled)
            }
        }
    }
}

/**
 * Default expand icon (right arrow)
 */
@Composable
private fun DefaultExpandIcon(disabled: Boolean) {
    Text(
        text = "▶",
        fontSize = 10.sp,
        color = if (disabled) Color.Gray else Color(0xFF000000).copy(alpha = 0.45f)
    )
}

/**
 * Scope for collecting CollapsePanel children
 */
interface CollapseScope {
    /**
     * Add a panel to the collapse
     *
     * @param key Unique identifier for the panel (required)
     * @param header Panel header content
     * @param children Panel content
     * @param collapsible Override parent collapsible setting for this panel
     * @param disabled Disable this panel
     * @param extra Extra content in header (right side)
     * @param forceRender Force render content even when collapsed
     * @param showArrow Show expand icon
     * @param classNames Semantic class names for this panel
     * @param styles Semantic styles for this panel
     */
    fun Panel(
        key: String,
        header: @Composable () -> Unit,
        children: @Composable () -> Unit,
        collapsible: CollapseCollapsibleType? = null,
        disabled: Boolean = false,
        extra: (@Composable () -> Unit)? = null,
        forceRender: Boolean = false,
        showArrow: Boolean = true,
        classNames: PanelClassNames? = null,
        styles: PanelStyles? = null
    )
}

/**
 * Internal implementation of CollapseScope
 */
private class CollapseScopeImpl : CollapseScope {
    val panels = mutableListOf<CollapseItemType>()

    override fun Panel(
        key: String,
        header: @Composable () -> Unit,
        children: @Composable () -> Unit,
        collapsible: CollapseCollapsibleType?,
        disabled: Boolean,
        extra: (@Composable () -> Unit)?,
        forceRender: Boolean,
        showArrow: Boolean,
        classNames: PanelClassNames?,
        styles: PanelStyles?
    ) {
        panels.add(
            CollapseItemType(
                key = key,
                label = header,
                children = children,
                collapsible = collapsible,
                disabled = disabled,
                extra = extra,
                forceRender = forceRender,
                showArrow = showArrow,
                classNames = classNames,
                styles = styles
            )
        )
    }
}

/**
 * CollapsePanel - Traditional child-based API
 * Use within AntCollapse children block
 *
 * ## Example
 * ```kotlin
 * AntCollapse {
 *     Panel(
 *         key = "1",
 *         header = { Text("Panel 1") }
 *     ) {
 *         Text("Content of panel 1")
 *     }
 * }
 * ```
 *
 * @param key Unique identifier for the panel (required)
 * @param header Panel header content
 * @param collapsible Override parent collapsible setting for this panel
 * @param disabled Disable this panel
 * @param extra Extra content in header (right side)
 * @param forceRender Force render content even when collapsed
 * @param showArrow Show expand icon
 * @param classNames Semantic class names for this panel
 * @param styles Semantic styles for this panel
 * @param children Panel content
 *
 * @since 1.0.0
 */
@Composable
fun CollapseScope.CollapsePanel(
    key: String,
    header: @Composable () -> Unit,
    collapsible: CollapseCollapsibleType? = null,
    disabled: Boolean = false,
    extra: (@Composable () -> Unit)? = null,
    forceRender: Boolean = false,
    showArrow: Boolean = true,
    classNames: PanelClassNames? = null,
    styles: PanelStyles? = null,
    children: @Composable () -> Unit
) {
    Panel(
        key = key,
        header = header,
        children = children,
        collapsible = collapsible,
        disabled = disabled,
        extra = extra,
        forceRender = forceRender,
        showArrow = showArrow,
        classNames = classNames,
        styles = styles
    )
}
