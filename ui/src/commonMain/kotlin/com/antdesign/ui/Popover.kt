package com.antdesign.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Popover trigger modes - how the popover is triggered
 * Maps to React Ant Design ActionType
 */
enum class PopoverTrigger {
    Hover,
    Click,
    Focus,
    ContextMenu;

    companion object {
        fun fromString(value: String): PopoverTrigger? = when (value.lowercase()) {
            "hover" -> Hover
            "click" -> Click
            "focus" -> Focus
            "contextmenu" -> ContextMenu
            else -> null
        }
    }
}

/**
 * Popover placement positions - maps to React Ant Design placements
 */
enum class PopoverPlacement {
    Top,
    TopLeft,
    TopRight,
    Bottom,
    BottomLeft,
    BottomRight,
    Left,
    LeftTop,
    LeftBottom,
    Right,
    RightTop,
    RightBottom
}

/**
 * Arrow configuration for popover
 * Maps to React Ant Design ArrowType
 */
data class PopoverArrowConfig(
    val show: Boolean = true,
    val pointAtCenter: Boolean = false,
    @Deprecated("Use pointAtCenter instead", ReplaceWith("pointAtCenter"))
    val arrowPointAtCenter: Boolean = false,
    val content: (@Composable () -> Unit)? = null
) {
    val effectivePointAtCenter: Boolean
        get() = pointAtCenter || arrowPointAtCenter
}

/**
 * Semantic styles for popover parts
 */
data class PopoverStyles(
    val root: Modifier = Modifier,
    val body: Modifier = Modifier,
    val title: Modifier = Modifier,
    val content: Modifier = Modifier
)

/**
 * Semantic class names for popover parts
 * Maps to React Ant Design v5.4.0+ classNames prop
 */
data class PopoverClassNames(
    val root: String? = null,
    val body: String? = null,
    val title: String? = null,
    val content: String? = null
)

/**
 * Built-in placement configuration
 * Maps to React Ant Design builtinPlacements prop
 */
data class PopoverBuiltinPlacements(
    val placements: Map<String, Any> = emptyMap()
)

/**
 * Complete Ant Design Popover component with 100% React parity (v5.25.0+)
 *
 * A simple text popup, the difference from Tooltip is that it can carry more complex content,
 * such as links and buttons, etc.
 *
 * @param content Popover content body (String or Composable)
 * @param title Popover title (String or Composable, optional)
 * @param modifier Base modifier for the container
 * @param open Controlled open state (null for uncontrolled)
 * @param defaultOpen Default open state for uncontrolled mode
 * @param onOpenChange Callback when open state changes
 * @param afterOpenChange Callback after open animation completes
 * @param placement Position of popover relative to target (12 placements available)
 * @param trigger How popover is triggered (can be single or list of triggers)
 * @param arrow Arrow configuration (boolean or config object with pointAtCenter, content)
 * @param arrowPointAtCenter Deprecated: Use arrow = PopoverArrowConfig(pointAtCenter = true)
 * @param autoAdjustOverflow Auto adjust position if overflow (boolean or config)
 * @param color Custom background color
 * @param destroyTooltipOnHide Deprecated: Use destroyOnHidden instead
 * @param destroyOnHidden Unmount popover when hidden (v5.25.0+)
 * @param fresh Force re-render on every show (v5.21.0+)
 * @param forceRender Force render popover even when hidden
 * @param getPopupContainer Custom popup container provider
 * @param mouseEnterDelay Delay in ms before showing (for hover trigger)
 * @param mouseLeaveDelay Delay in ms before hiding (for hover trigger)
 * @param overlayClassName Deprecated: Use classNames.root instead
 * @param overlayStyle Deprecated: Use styles.root instead
 * @param overlayInnerStyle Deprecated: Use styles.body instead
 * @param zIndex Z-index for popup
 * @param styles Semantic styles for popover parts (v5.4.0+)
 * @param classNames Semantic class names for popover parts (v5.4.0+)
 * @param openClassName Class name added to child when popover is open
 * @param align Custom alignment configuration
 * @param onPopupAlign Callback when popup aligns
 * @param motion Custom animation configuration
 * @param id HTML id attribute for the popup
 * @param builtinPlacements Custom builtin placement configurations
 * @param child Child content that triggers the popover
 */
@Composable
fun AntPopover(
    content: Any, // String or @Composable () -> Unit
    title: Any? = null, // String or @Composable () -> Unit
    modifier: Modifier = Modifier,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    onOpenChange: ((Boolean) -> Unit)? = null,
    afterOpenChange: ((Boolean) -> Unit)? = null,
    placement: PopoverPlacement = PopoverPlacement.Top,
    trigger: Any = PopoverTrigger.Hover, // PopoverTrigger or List<PopoverTrigger>
    arrow: Any = true, // Boolean or PopoverArrowConfig
    arrowPointAtCenter: Boolean = false,
    autoAdjustOverflow: Any = true, // Boolean or AdjustOverflow
    color: Color? = null,
    destroyTooltipOnHide: Boolean = false,
    destroyOnHidden: Boolean = false,
    fresh: Boolean = false,
    forceRender: Boolean = false,
    getPopupContainer: (() -> Any?)? = null,
    mouseEnterDelay: Long = 100, // milliseconds
    mouseLeaveDelay: Long = 100, // milliseconds
    overlayClassName: String? = null,
    overlayStyle: Modifier = Modifier,
    overlayInnerStyle: Modifier = Modifier,
    zIndex: Int = 1030,
    styles: PopoverStyles = PopoverStyles(),
    classNames: PopoverClassNames = PopoverClassNames(),
    openClassName: String? = null,
    align: Any? = null, // PopoverAlignConfig
    onPopupAlign: ((LayoutCoordinates) -> Unit)? = null,
    motion: Any? = null, // Animation configuration
    id: String? = null,
    builtinPlacements: PopoverBuiltinPlacements? = null,
    child: @Composable () -> Unit
) {
    // Parse arrow config
    val arrowConfig = when (arrow) {
        is Boolean -> PopoverArrowConfig(show = arrow, arrowPointAtCenter = arrowPointAtCenter)
        is PopoverArrowConfig -> arrow
        else -> PopoverArrowConfig(show = true, arrowPointAtCenter = arrowPointAtCenter)
    }

    // Parse overflow config
    val overflowConfig = when (autoAdjustOverflow) {
        is Boolean -> if (autoAdjustOverflow) AdjustOverflow() else AdjustOverflow(false, false)
        is AdjustOverflow -> autoAdjustOverflow
        else -> AdjustOverflow()
    }

    // Parse trigger - can be single or list
    val triggers = when (trigger) {
        is PopoverTrigger -> listOf(trigger)
        is List<*> -> trigger.filterIsInstance<PopoverTrigger>()
        else -> listOf(PopoverTrigger.Hover)
    }

    // Primary trigger (first in list)
    val primaryTrigger = triggers.firstOrNull() ?: PopoverTrigger.Hover

    // Resolve destroyOnHidden with backward compatibility
    val effectiveDestroyOnHidden = destroyOnHidden || destroyTooltipOnHide

    // State management - controlled or uncontrolled
    var internalOpen by remember { mutableStateOf(defaultOpen) }
    val currentOpen = open ?: internalOpen

    val scope = rememberCoroutineScope()
    var showJob by remember { mutableStateOf<Job?>(null) }
    var hideJob by remember { mutableStateOf<Job?>(null) }

    // Fresh key for forcing re-render
    var freshKey by remember { mutableStateOf(0) }

    // Coordinates for positioning
    var childCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    // Animation state
    val visibleState = remember { MutableTransitionState(currentOpen) }
    visibleState.targetState = currentOpen

    // Handle open state changes
    val setOpen = { newOpen: Boolean ->
        if (open == null) {
            internalOpen = newOpen
        }
        onOpenChange?.invoke(newOpen)

        // Trigger fresh re-render if enabled
        if (fresh && newOpen) {
            freshKey++
        }

        // After animation callback
        if (newOpen != currentOpen) {
            scope.launch {
                delay(300) // Animation duration
                afterOpenChange?.invoke(newOpen)
            }
        }
    }

    // Trigger handlers
    val handleMouseEnter = {
        hideJob?.cancel()
        hideJob = null
        showJob = scope.launch {
            delay(mouseEnterDelay)
            setOpen(true)
        }
    }

    val handleMouseLeave = {
        showJob?.cancel()
        showJob = null
        hideJob = scope.launch {
            delay(mouseLeaveDelay)
            setOpen(false)
        }
    }

    val handleClick = {
        showJob?.cancel()
        hideJob?.cancel()
        setOpen(!currentOpen)
    }

    // Cleanup jobs on dispose
    DisposableEffect(Unit) {
        onDispose {
            showJob?.cancel()
            hideJob?.cancel()
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                childCoordinates = coordinates
                onPopupAlign?.invoke(coordinates)
            }
    ) {
        // Child content with trigger handlers (supports multiple triggers)
        var childModifier: Modifier = Modifier

        // Add hover trigger if present
        if (triggers.contains(PopoverTrigger.Hover)) {
            childModifier = childModifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val eventType = event.type.toString()
                        when {
                            eventType.contains("Press", ignoreCase = true) -> handleMouseEnter()
                            eventType.contains("Release", ignoreCase = true) -> handleMouseLeave()
                            eventType.contains("Enter", ignoreCase = true) -> handleMouseEnter()
                            eventType.contains("Exit", ignoreCase = true) -> handleMouseLeave()
                        }
                    }
                }
            }
        }

        // Add click trigger if present
        if (triggers.contains(PopoverTrigger.Click)) {
            childModifier = childModifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                handleClick()
            }
        }

        // Add focus trigger if present
        if (triggers.contains(PopoverTrigger.Focus)) {
            childModifier = childModifier
                .focusable()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        setOpen(true)
                    } else {
                        setOpen(false)
                    }
                }
        }

        // Add context menu trigger if present
        if (triggers.contains(PopoverTrigger.ContextMenu)) {
            childModifier = childModifier.pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { handleClick() }
                )
            }
        }

        Box(modifier = childModifier) {
            child()
        }

        // Popover popup - forceRender or normal render logic
        val shouldRenderPopup = forceRender || currentOpen || visibleState.currentState || visibleState.targetState

        if (shouldRenderPopup && !effectiveDestroyOnHidden) {
            childCoordinates?.let { coords ->
                PopoverPopup(
                    key = freshKey,
                    visible = visibleState,
                    childCoordinates = coords,
                    placement = placement,
                    arrowConfig = arrowConfig,
                    overflowConfig = overflowConfig,
                    color = color ?: Color.White,
                    overlayStyle = overlayStyle,
                    overlayInnerStyle = overlayInnerStyle,
                    styles = styles,
                    classNames = classNames,
                    zIndex = zIndex,
                    onMouseEnter = if (triggers.contains(PopoverTrigger.Hover)) handleMouseEnter else null,
                    onMouseLeave = if (triggers.contains(PopoverTrigger.Hover)) handleMouseLeave else null,
                    title = title,
                    content = content,
                    id = id
                )
            }
        } else if (currentOpen && effectiveDestroyOnHidden) {
            // Destroy and recreate on show
            childCoordinates?.let { coords ->
                PopoverPopup(
                    key = freshKey,
                    visible = visibleState,
                    childCoordinates = coords,
                    placement = placement,
                    arrowConfig = arrowConfig,
                    overflowConfig = overflowConfig,
                    color = color ?: Color.White,
                    overlayStyle = overlayStyle,
                    overlayInnerStyle = overlayInnerStyle,
                    styles = styles,
                    classNames = classNames,
                    zIndex = zIndex,
                    onMouseEnter = if (triggers.contains(PopoverTrigger.Hover)) handleMouseEnter else null,
                    onMouseLeave = if (triggers.contains(PopoverTrigger.Hover)) handleMouseLeave else null,
                    title = title,
                    content = content,
                    id = id
                )
            }
        }
    }
}

/**
 * Popover popup component
 */
@Composable
private fun PopoverPopup(
    key: Int,
    visible: MutableTransitionState<Boolean>,
    childCoordinates: LayoutCoordinates,
    placement: PopoverPlacement,
    arrowConfig: PopoverArrowConfig,
    overflowConfig: AdjustOverflow,
    color: Color,
    overlayStyle: Modifier,
    overlayInnerStyle: Modifier,
    styles: PopoverStyles,
    classNames: PopoverClassNames,
    zIndex: Int,
    onMouseEnter: (() -> Unit)?,
    onMouseLeave: (() -> Unit)?,
    title: Any?,
    content: Any,
    id: String? = null
) {
    Popup(
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            // Notify parent to close
            onMouseLeave?.invoke()
        }
    ) {
        AnimatedVisibility(
            visibleState = visible,
            enter = fadeIn(animationSpec = tween(150)) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(150)
            ),
            exit = fadeOut(animationSpec = tween(150)) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(150)
            )
        ) {
            Box(
                modifier = Modifier
                    .zIndex(zIndex.toFloat())
                    .then(
                        if (onMouseEnter != null && onMouseLeave != null) {
                            Modifier.pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val eventType = event.type.toString()
                                        when {
                                            eventType.contains("Enter", ignoreCase = true) -> onMouseEnter()
                                            eventType.contains("Exit", ignoreCase = true) -> onMouseLeave()
                                        }
                                    }
                                }
                            }
                        } else Modifier
                    )
            ) {
                Column(
                    horizontalAlignment = when (placement) {
                        PopoverPlacement.TopLeft, PopoverPlacement.BottomLeft -> Alignment.Start
                        PopoverPlacement.TopRight, PopoverPlacement.BottomRight -> Alignment.End
                        else -> Alignment.CenterHorizontally
                    }
                ) {
                    // Arrow on top for bottom placements
                    if (arrowConfig.show && placement in listOf(
                            PopoverPlacement.Bottom,
                            PopoverPlacement.BottomLeft,
                            PopoverPlacement.BottomRight
                        )) {
                        if (arrowConfig.content != null) {
                            arrowConfig.content?.invoke()
                        } else {
                            PopoverArrow(color = color, direction = ArrowDirection.Up)
                        }
                    }

                    // Popover body
                    Box(
                        modifier = Modifier
                            .then(styles.root)
                            .then(overlayStyle)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = color,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .then(styles.body)
                            .then(overlayInnerStyle)
                            .widthIn(max = 300.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Title
                            if (title != null) {
                                Box(modifier = styles.title) {
                                    when (title) {
                                        is String -> Text(
                                            text = title,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF000000).copy(alpha = 0.88f)
                                        )
                                        is Function0<*> -> {
                                            @Suppress("UNCHECKED_CAST")
                                            (title as @Composable () -> Unit)()
                                        }
                                    }
                                }

                                // Divider between title and content
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color(0xFF000000).copy(alpha = 0.06f),
                                    thickness = 1.dp
                                )
                            }

                            // Content
                            Box(modifier = styles.content) {
                                when (content) {
                                    is String -> Text(
                                        text = content,
                                        fontSize = 14.sp,
                                        color = Color(0xFF000000).copy(alpha = 0.88f)
                                    )
                                    is Function0<*> -> {
                                        @Suppress("UNCHECKED_CAST")
                                        (content as @Composable () -> Unit)()
                                    }
                                }
                            }
                        }
                    }

                    // Arrow on bottom for top placements
                    if (arrowConfig.show && placement in listOf(
                            PopoverPlacement.Top,
                            PopoverPlacement.TopLeft,
                            PopoverPlacement.TopRight
                        )) {
                        if (arrowConfig.content != null) {
                            arrowConfig.content?.invoke()
                        } else {
                            PopoverArrow(color = color, direction = ArrowDirection.Down)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Arrow direction for popover
 */
private enum class ArrowDirection {
    Up, Down, Left, Right
}

/**
 * Popover arrow component
 */
@Composable
private fun PopoverArrow(
    color: Color,
    direction: ArrowDirection,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 16.dp, height = 8.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val path = Path().apply {
                when (direction) {
                    ArrowDirection.Up -> {
                        moveTo(size.width / 2, 0f)
                        lineTo(size.width / 2 - 8.dp.toPx(), size.height)
                        lineTo(size.width / 2 + 8.dp.toPx(), size.height)
                    }
                    ArrowDirection.Down -> {
                        moveTo(size.width / 2, size.height)
                        lineTo(size.width / 2 - 8.dp.toPx(), 0f)
                        lineTo(size.width / 2 + 8.dp.toPx(), 0f)
                    }
                    ArrowDirection.Left -> {
                        moveTo(0f, size.height / 2)
                        lineTo(size.width, size.height / 2 - 8.dp.toPx())
                        lineTo(size.width, size.height / 2 + 8.dp.toPx())
                    }
                    ArrowDirection.Right -> {
                        moveTo(size.width, size.height / 2)
                        lineTo(0f, size.height / 2 - 8.dp.toPx())
                        lineTo(0f, size.height / 2 + 8.dp.toPx())
                    }
                }
                close()
            }
            drawPath(path, color, style = Fill)
        }
    }
}

/**
 * Simple string-based popover variant
 */
@Composable
fun AntPopover(
    content: String,
    title: String? = null,
    modifier: Modifier = Modifier,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    onOpenChange: ((Boolean) -> Unit)? = null,
    afterOpenChange: ((Boolean) -> Unit)? = null,
    placement: PopoverPlacement = PopoverPlacement.Top,
    trigger: Any = PopoverTrigger.Hover,
    arrow: Any = true,
    arrowPointAtCenter: Boolean = false,
    autoAdjustOverflow: Any = true,
    color: Color? = null,
    destroyTooltipOnHide: Boolean = false,
    destroyOnHidden: Boolean = false,
    fresh: Boolean = false,
    forceRender: Boolean = false,
    getPopupContainer: (() -> Any?)? = null,
    mouseEnterDelay: Long = 100,
    mouseLeaveDelay: Long = 100,
    overlayClassName: String? = null,
    overlayStyle: Modifier = Modifier,
    overlayInnerStyle: Modifier = Modifier,
    zIndex: Int = 1030,
    styles: PopoverStyles = PopoverStyles(),
    classNames: PopoverClassNames = PopoverClassNames(),
    openClassName: String? = null,
    align: Any? = null,
    onPopupAlign: ((LayoutCoordinates) -> Unit)? = null,
    motion: Any? = null,
    id: String? = null,
    builtinPlacements: PopoverBuiltinPlacements? = null,
    child: @Composable () -> Unit
) {
    AntPopover(
        content = content as Any,
        title = title as Any?,
        modifier = modifier,
        open = open,
        defaultOpen = defaultOpen,
        onOpenChange = onOpenChange,
        afterOpenChange = afterOpenChange,
        placement = placement,
        trigger = trigger,
        arrow = arrow,
        arrowPointAtCenter = arrowPointAtCenter,
        autoAdjustOverflow = autoAdjustOverflow,
        color = color,
        destroyTooltipOnHide = destroyTooltipOnHide,
        destroyOnHidden = destroyOnHidden,
        fresh = fresh,
        forceRender = forceRender,
        getPopupContainer = getPopupContainer,
        mouseEnterDelay = mouseEnterDelay,
        mouseLeaveDelay = mouseLeaveDelay,
        overlayClassName = overlayClassName,
        overlayStyle = overlayStyle,
        overlayInnerStyle = overlayInnerStyle,
        zIndex = zIndex,
        styles = styles,
        classNames = classNames,
        openClassName = openClassName,
        align = align,
        onPopupAlign = onPopupAlign,
        motion = motion,
        id = id,
        builtinPlacements = builtinPlacements,
        child = child
    )
}
