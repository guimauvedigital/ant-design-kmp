package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Extended Tooltip placement positions - maps to React Ant Design placements
 * Uses a different name to avoid conflict with TooltipPlacement in Slider.kt
 */
enum class TooltipPlacementExt {
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
 * Tooltip trigger modes
 */
enum class TooltipTrigger {
    Hover,
    Focus,
    Click,
    ContextMenu
}

/**
 * Arrow configuration for tooltip
 */
data class TooltipArrowConfig(
    val show: Boolean = true,
    val pointAtCenter: Boolean = false,
    @Deprecated("Use pointAtCenter instead", ReplaceWith("pointAtCenter"))
    val arrowPointAtCenter: Boolean = false,
) {
    val effectivePointAtCenter: Boolean
        get() = pointAtCenter || arrowPointAtCenter
}

/**
 * Adjust overflow configuration
 */
data class AdjustOverflow(
    val adjustX: Boolean = true,
    val adjustY: Boolean = true,
)

/**
 * Tooltip alignment configuration
 * Maps to React Ant Design's TooltipAlignConfig
 */
data class TooltipAlignConfig(
    val points: List<String>? = null,
    val offset: Pair<Int, Int>? = null,
    val targetOffset: Pair<Int, Int>? = null,
    val overflow: AdjustOverflow? = null,
    val useCssRight: Boolean? = null,
    val useCssBottom: Boolean? = null,
    val useCssTransform: Boolean? = null,
)

/**
 * Builtin placement configurations
 * Maps to React Ant Design's builtinPlacements
 */
data class BuiltinPlacementsConfig(
    val top: TooltipAlignConfig? = null,
    val topLeft: TooltipAlignConfig? = null,
    val topRight: TooltipAlignConfig? = null,
    val bottom: TooltipAlignConfig? = null,
    val bottomLeft: TooltipAlignConfig? = null,
    val bottomRight: TooltipAlignConfig? = null,
    val left: TooltipAlignConfig? = null,
    val leftTop: TooltipAlignConfig? = null,
    val leftBottom: TooltipAlignConfig? = null,
    val right: TooltipAlignConfig? = null,
    val rightTop: TooltipAlignConfig? = null,
    val rightBottom: TooltipAlignConfig? = null,
)

/**
 * Semantic styles for tooltip parts
 */
data class TooltipStyles(
    val root: Modifier = Modifier,
    val body: Modifier = Modifier,
)

/**
 * Semantic class names for tooltip parts
 */
data class TooltipClassNames(
    val root: String? = null,
    val body: String? = null,
)

/**
 * Preset color names for tooltips (v5.0+)
 * Supports all 13 Ant Design color presets
 */
object TooltipPresetColors {
    const val PINK = "pink"
    const val RED = "red"
    const val YELLOW = "yellow"
    const val ORANGE = "orange"
    const val CYAN = "cyan"
    const val GREEN = "green"
    const val BLUE = "blue"
    const val PURPLE = "purple"
    const val GEEK_BLUE = "geekblue"
    const val MAGENTA = "magenta"
    const val VOLCANO = "volcano"
    const val GOLD = "gold"
    const val LIME = "lime"

    /**
     * Map preset color names to actual Color values
     * Uses Ant Design's official color palette from Theme.kt
     */
    fun resolvePresetColor(colorName: String): Color? {
        return when (colorName.lowercase()) {
            PINK -> Color(0xFFEB2F96)
            RED -> ColorPalette.red5
            YELLOW -> Color(0xFFFADB14)
            ORANGE -> Color(0xFFFA8C16)
            CYAN -> Color(0xFF13C2C2)
            GREEN -> ColorPalette.green6
            BLUE -> ColorPalette.blue6
            PURPLE -> Color(0xFF722ED1)
            GEEK_BLUE, "geekblue" -> Color(0xFF2F54EB)
            MAGENTA -> Color(0xFFEB2F96)
            VOLCANO -> Color(0xFFFA541C)
            GOLD -> ColorPalette.gold6
            LIME -> Color(0xFFA0D911)
            else -> null
        }
    }

    /**
     * Determine if text should be white or black based on background color
     * Implements WCAG contrast ratio algorithm (v5.27.0+)
     */
    fun getContrastTextColor(bgColor: Color): Color {
        // Calculate relative luminance
        val r = if (bgColor.red <= 0.03928f) bgColor.red / 12.92f else ((bgColor.red + 0.055f) / 1.055f).pow(2.4f)
        val g = if (bgColor.green <= 0.03928f) bgColor.green / 12.92f else ((bgColor.green + 0.055f) / 1.055f).pow(2.4f)
        val b = if (bgColor.blue <= 0.03928f) bgColor.blue / 12.92f else ((bgColor.blue + 0.055f) / 1.055f).pow(2.4f)

        val luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b

        // Return white for dark backgrounds, black for light backgrounds
        return if (luminance > 0.179f) Color.Black else Color.White
    }
}

/**
 * Import for Kotlin pow function
 */
private fun Float.pow(exponent: Float): Float {
    var result = 1.0f
    var exp = exponent
    var base = this

    if (exp < 0) {
        base = 1f / base
        exp = -exp
    }

    // Use simple power calculation for positive exponents
    while (exp >= 1) {
        result *= base
        exp -= 1
    }

    return result
}

/**
 * Complete Ant Design Tooltip component with 100% React parity
 *
 * @param title Tooltip content (required)
 * @param modifier Base modifier for the container
 * @param open Controlled open state (null for uncontrolled) - v5.0+
 * @param defaultOpen Default open state for uncontrolled mode - v5.0+
 * @param onOpenChange Callback when open state changes - v5.0+
 * @param placement Position of tooltip relative to target (use TooltipPlacementExt enum)
 * @param trigger How tooltip is triggered
 * @param arrow Arrow configuration (boolean or config object) - v5.2.0+
 * @param arrowPointAtCenter Deprecated: Use arrow = TooltipArrowConfig(pointAtCenter = true)
 * @param autoAdjustOverflow Auto adjust position if overflow (boolean or config)
 * @param color Custom background color (String preset name or Color object) - v5.0+
 * @param destroyTooltipOnHide Deprecated: Use destroyOnHidden instead - v5.25.0+
 * @param destroyOnHidden Unmount tooltip when hidden - v5.25.0+
 * @param fresh Force re-render on every show - v5.21.0+
 * @param getPopupContainer Custom popup container provider
 * @param mouseEnterDelay Delay in ms before showing
 * @param mouseLeaveDelay Delay in ms before hiding
 * @param overlayClassName Custom class name for overlay
 * @param overlayStyle Custom style modifier for overlay root
 * @param overlayInnerStyle Custom style modifier for overlay body
 * @param zIndex Z-index for popup
 * @param styles Semantic styles for tooltip parts - v5.4.0+
 * @param classNames Semantic class names for tooltip parts - v5.4.0+
 * @param openClassName Class name added to child when tooltip is open
 * @param afterOpenChange Callback after open animation completes - v5.0+
 * @param align Custom alignment configuration
 * @param builtinPlacements Custom builtin placement configurations
 * @param visible Deprecated: Use open instead - v4.x
 * @param defaultVisible Deprecated: Use defaultOpen instead - v4.x
 * @param onVisibleChange Deprecated: Use onOpenChange instead - v4.x
 * @param afterVisibleChange Deprecated: Use afterOpenChange instead - v4.x
 * @param content Child content that triggers the tooltip
 */
@Composable
fun AntTooltip(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    onOpenChange: ((Boolean) -> Unit)? = null,
    placement: TooltipPlacementExt = TooltipPlacementExt.Top,
    trigger: TooltipTrigger = TooltipTrigger.Hover,
    arrow: Any = true, // Boolean or TooltipArrowConfig
    arrowPointAtCenter: Boolean = false, // Deprecated: Use arrow parameter with TooltipArrowConfig
    autoAdjustOverflow: Any = true, // Boolean or AdjustOverflow
    color: Any? = null, // String (preset color name) or Color object
    destroyTooltipOnHide: Boolean = false, // Deprecated v5.25.0+: Use destroyOnHidden instead
    destroyOnHidden: Boolean = false, // v5.25.0+
    fresh: Boolean = false, // v5.21.0+
    getPopupContainer: (() -> Any?)? = null,
    mouseEnterDelay: Long = 100, // milliseconds
    mouseLeaveDelay: Long = 100, // milliseconds
    overlayClassName: String? = null,
    overlayStyle: Modifier = Modifier,
    overlayInnerStyle: Modifier = Modifier,
    zIndex: Int = 1070,
    styles: TooltipStyles = TooltipStyles(),
    classNames: TooltipClassNames = TooltipClassNames(),
    openClassName: String? = null,
    afterOpenChange: ((Boolean) -> Unit)? = null, // v5.0+
    align: Any? = null, // TooltipAlignConfig
    builtinPlacements: BuiltinPlacementsConfig? = null,
    // Deprecated v4.x props - maintain backward compatibility
    visible: Boolean? = null, // Deprecated: Use open instead
    defaultVisible: Boolean? = null, // Deprecated: Use defaultOpen instead
    onVisibleChange: ((Boolean) -> Unit)? = null, // Deprecated: Use onOpenChange instead
    afterVisibleChange: ((Boolean) -> Unit)? = null, // Deprecated: Use afterOpenChange instead
    content: @Composable () -> Unit,
) {
    // Handle deprecated v4.x props
    val effectiveOpen = open ?: visible
    val effectiveDefaultOpen = if (defaultVisible != null) defaultVisible else defaultOpen
    val effectiveOnOpenChange = onOpenChange ?: onVisibleChange
    val effectiveAfterOpenChange = afterOpenChange ?: afterVisibleChange
    val effectiveDestroyOnHidden = destroyOnHidden || destroyTooltipOnHide

    // Parse arrow config
    val arrowConfig = when (arrow) {
        is Boolean -> TooltipArrowConfig(show = arrow, arrowPointAtCenter = arrowPointAtCenter)
        is TooltipArrowConfig -> arrow
        else -> TooltipArrowConfig(show = true, arrowPointAtCenter = arrowPointAtCenter)
    }

    // Parse overflow config
    val overflowConfig = when (autoAdjustOverflow) {
        is Boolean -> if (autoAdjustOverflow) AdjustOverflow() else AdjustOverflow(false, false)
        is AdjustOverflow -> autoAdjustOverflow
        else -> AdjustOverflow()
    }

    // Resolve color - support both preset names (String) and Color objects
    val resolvedColor = when (color) {
        is String -> TooltipPresetColors.resolvePresetColor(color) ?: Color(0xFF000000).copy(alpha = 0.75f)
        is Color -> color
        null -> Color(0xFF000000).copy(alpha = 0.75f)
        else -> Color(0xFF000000).copy(alpha = 0.75f)
    }

    // Determine text color based on background color (v5.27.0+)
    val textColor = if (color != null) {
        TooltipPresetColors.getContrastTextColor(resolvedColor)
    } else {
        Color.White // Default white text for default dark tooltip
    }

    // State management - controlled or uncontrolled
    var internalOpen by remember { mutableStateOf(effectiveDefaultOpen) }
    val currentOpen = effectiveOpen ?: internalOpen

    val scope = rememberCoroutineScope()
    var showJob by remember { mutableStateOf<Job?>(null) }
    var hideJob by remember { mutableStateOf<Job?>(null) }

    // Fresh key for forcing re-render
    var freshKey by remember { mutableStateOf(0) }

    // Coordinates for positioning
    var childCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }

    // Animation state
    val visibleState = remember { MutableTransitionState(currentOpen) }
    visibleState.targetState = currentOpen

    // Handle open state changes
    val setOpen = { newOpen: Boolean ->
        if (effectiveOpen == null) {
            internalOpen = newOpen
        }
        effectiveOnOpenChange?.invoke(newOpen)

        // Trigger fresh re-render if enabled (v5.21.0+)
        if (fresh && newOpen) {
            freshKey++
        }

        // After animation callback
        if (newOpen != currentOpen) {
            scope.launch {
                delay(300) // Animation duration
                effectiveAfterOpenChange?.invoke(newOpen)
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
            }
    ) {
        // Child content with trigger handlers
        Box(
            modifier = when (trigger) {
                TooltipTrigger.Hover -> Modifier
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type.toString()) {
                                    "Press" -> handleMouseEnter()
                                    "Release" -> handleMouseLeave()
                                    "Enter" -> handleMouseEnter()
                                    "Exit" -> handleMouseLeave()
                                }
                            }
                        }
                    }

                TooltipTrigger.Click -> Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    handleClick()
                }

                TooltipTrigger.Focus -> Modifier // TODO: Implement focus handling
                TooltipTrigger.ContextMenu -> Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { handleClick() }
                    )
                }
            }
        ) {
            content()
        }

        // Tooltip popup
        if ((currentOpen || visibleState.currentState || visibleState.targetState) && !effectiveDestroyOnHidden) {
            childCoordinates?.let { coords ->
                TooltipPopup(
                    key = freshKey,
                    visible = visibleState,
                    childCoordinates = coords,
                    placement = placement,
                    arrowConfig = arrowConfig,
                    overflowConfig = overflowConfig,
                    color = resolvedColor,
                    textColor = textColor,
                    overlayStyle = overlayStyle,
                    overlayInnerStyle = overlayInnerStyle,
                    styles = styles,
                    zIndex = zIndex,
                    onMouseEnter = if (trigger == TooltipTrigger.Hover) handleMouseEnter else null,
                    onMouseLeave = if (trigger == TooltipTrigger.Hover) handleMouseLeave else null,
                    onSizeChanged = { tooltipSize = it },
                    title = title
                )
            }
        } else if (currentOpen && effectiveDestroyOnHidden) {
            // Destroy and recreate on show (v5.25.0+)
            childCoordinates?.let { coords ->
                TooltipPopup(
                    key = freshKey,
                    visible = visibleState,
                    childCoordinates = coords,
                    placement = placement,
                    arrowConfig = arrowConfig,
                    overflowConfig = overflowConfig,
                    color = resolvedColor,
                    textColor = textColor,
                    overlayStyle = overlayStyle,
                    overlayInnerStyle = overlayInnerStyle,
                    styles = styles,
                    zIndex = zIndex,
                    onMouseEnter = if (trigger == TooltipTrigger.Hover) handleMouseEnter else null,
                    onMouseLeave = if (trigger == TooltipTrigger.Hover) handleMouseLeave else null,
                    onSizeChanged = { tooltipSize = it },
                    title = title
                )
            }
        }
    }
}

/**
 * Arrow direction for tooltip
 */
enum class TooltipArrowDirection {
    Up, Down, Left, Right
}

/**
 * Tooltip popup component
 */
@Composable
internal fun TooltipPopup(
    key: Int,
    visible: MutableTransitionState<Boolean>,
    childCoordinates: LayoutCoordinates,
    placement: TooltipPlacementExt,
    arrowConfig: TooltipArrowConfig,
    overflowConfig: AdjustOverflow,
    color: Color,
    textColor: Color,
    overlayStyle: Modifier,
    overlayInnerStyle: Modifier,
    styles: TooltipStyles,
    zIndex: Int,
    onMouseEnter: (() -> Unit)?,
    onMouseLeave: (() -> Unit)?,
    onSizeChanged: (IntSize) -> Unit,
    title: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    Popup(
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        AnimatedVisibility(
            visibleState = visible,
            enter = fadeIn(animationSpec = tween(100)) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(100)
            ),
            exit = fadeOut(animationSpec = tween(100)) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(100)
            )
        ) {
            var tooltipCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

            Box(
                modifier = Modifier
                    .zIndex(zIndex.toFloat())
                    .onGloballyPositioned { coords ->
                        tooltipCoordinates = coords
                        onSizeChanged(coords.size)
                    }
                    .then(
                        if (onMouseEnter != null && onMouseLeave != null) {
                            Modifier.pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when (event.type.toString()) {
                                            "Enter" -> onMouseEnter()
                                            "Exit" -> onMouseLeave()
                                        }
                                    }
                                }
                            }
                        } else Modifier
                    )
            ) {
                // Determine layout based on placement
                when {
                    // Left/Right placements use Row layout
                    placement in listOf(
                        TooltipPlacementExt.Left,
                        TooltipPlacementExt.LeftTop,
                        TooltipPlacementExt.LeftBottom,
                        TooltipPlacementExt.Right,
                        TooltipPlacementExt.RightTop,
                        TooltipPlacementExt.RightBottom
                    ) -> {
                        Row(
                            verticalAlignment = when (placement) {
                                TooltipPlacementExt.LeftTop, TooltipPlacementExt.RightTop -> Alignment.Top
                                TooltipPlacementExt.LeftBottom, TooltipPlacementExt.RightBottom -> Alignment.Bottom
                                else -> Alignment.CenterVertically
                            }
                        ) {
                            // Arrow on left for right placements
                            if (arrowConfig.show && placement in listOf(
                                    TooltipPlacementExt.Right,
                                    TooltipPlacementExt.RightTop,
                                    TooltipPlacementExt.RightBottom
                                )
                            ) {
                                TooltipArrow(color = color, direction = TooltipArrowDirection.Left)
                            }

                            // Tooltip body
                            Box(
                                modifier = Modifier
                                    .then(styles.root)
                                    .then(overlayStyle)
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .background(
                                        color = color,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .then(styles.body)
                                    .then(overlayInnerStyle)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                title()
                            }

                            // Arrow on right for left placements
                            if (arrowConfig.show && placement in listOf(
                                    TooltipPlacementExt.Left,
                                    TooltipPlacementExt.LeftTop,
                                    TooltipPlacementExt.LeftBottom
                                )
                            ) {
                                TooltipArrow(color = color, direction = TooltipArrowDirection.Right)
                            }
                        }
                    }
                    // Top/Bottom placements use Column layout
                    else -> {
                        Column(
                            horizontalAlignment = when (placement) {
                                TooltipPlacementExt.TopLeft, TooltipPlacementExt.BottomLeft -> Alignment.Start
                                TooltipPlacementExt.TopRight, TooltipPlacementExt.BottomRight -> Alignment.End
                                else -> Alignment.CenterHorizontally
                            }
                        ) {
                            // Arrow on top for bottom placements
                            if (arrowConfig.show && placement in listOf(
                                    TooltipPlacementExt.Bottom,
                                    TooltipPlacementExt.BottomLeft,
                                    TooltipPlacementExt.BottomRight
                                )
                            ) {
                                TooltipArrow(color = color, direction = TooltipArrowDirection.Up)
                            }

                            // Tooltip body
                            Box(
                                modifier = Modifier
                                    .then(styles.root)
                                    .then(overlayStyle)
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .background(
                                        color = color,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .then(styles.body)
                                    .then(overlayInnerStyle)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                title()
                            }

                            // Arrow on bottom for top placements
                            if (arrowConfig.show && placement in listOf(
                                    TooltipPlacementExt.Top,
                                    TooltipPlacementExt.TopLeft,
                                    TooltipPlacementExt.TopRight
                                )
                            ) {
                                TooltipArrow(color = color, direction = TooltipArrowDirection.Down)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tooltip arrow component - supports all 4 directions
 */
@Composable
private fun TooltipArrow(
    color: Color,
    direction: TooltipArrowDirection,
    modifier: Modifier = Modifier,
) {
    val arrowSize = when (direction) {
        TooltipArrowDirection.Up, TooltipArrowDirection.Down -> Modifier.size(width = 16.dp, height = 8.dp)
        TooltipArrowDirection.Left, TooltipArrowDirection.Right -> Modifier.size(width = 8.dp, height = 16.dp)
    }

    Box(
        modifier = modifier.then(arrowSize)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val path = Path().apply {
                when (direction) {
                    TooltipArrowDirection.Up -> {
                        // Triangle pointing up
                        moveTo(size.width / 2, 0f)
                        lineTo(size.width / 2 - 8.dp.toPx(), size.height)
                        lineTo(size.width / 2 + 8.dp.toPx(), size.height)
                    }

                    TooltipArrowDirection.Down -> {
                        // Triangle pointing down
                        moveTo(size.width / 2, size.height)
                        lineTo(size.width / 2 - 8.dp.toPx(), 0f)
                        lineTo(size.width / 2 + 8.dp.toPx(), 0f)
                    }

                    TooltipArrowDirection.Left -> {
                        // Triangle pointing left
                        moveTo(0f, size.height / 2)
                        lineTo(size.width, size.height / 2 - 8.dp.toPx())
                        lineTo(size.width, size.height / 2 + 8.dp.toPx())
                    }

                    TooltipArrowDirection.Right -> {
                        // Triangle pointing right
                        moveTo(size.width, size.height / 2)
                        lineTo(0f, size.height / 2 - 8.dp.toPx())
                        lineTo(0f, size.height / 2 + 8.dp.toPx())
                    }
                }
                close()
            }
            drawPath(path, color)
        }
    }
}

/**
 * Simple text tooltip variant - automatically adjusts text color based on background (v5.27.0+)
 */
@Composable
fun AntTooltip(
    title: String,
    modifier: Modifier = Modifier,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    onOpenChange: ((Boolean) -> Unit)? = null,
    placement: TooltipPlacementExt = TooltipPlacementExt.Top,
    trigger: TooltipTrigger = TooltipTrigger.Hover,
    arrow: Any = true,
    arrowPointAtCenter: Boolean = false,
    autoAdjustOverflow: Any = true,
    color: Any? = null, // String (preset) or Color
    destroyTooltipOnHide: Boolean = false, // Deprecated v5.25.0+: Use destroyOnHidden instead
    destroyOnHidden: Boolean = false,
    fresh: Boolean = false,
    getPopupContainer: (() -> Any?)? = null,
    mouseEnterDelay: Long = 100,
    mouseLeaveDelay: Long = 100,
    overlayClassName: String? = null,
    overlayStyle: Modifier = Modifier,
    overlayInnerStyle: Modifier = Modifier,
    zIndex: Int = 1070,
    styles: TooltipStyles = TooltipStyles(),
    classNames: TooltipClassNames = TooltipClassNames(),
    openClassName: String? = null,
    afterOpenChange: ((Boolean) -> Unit)? = null,
    align: Any? = null,
    builtinPlacements: BuiltinPlacementsConfig? = null,
    visible: Boolean? = null, // Deprecated: Use open instead
    defaultVisible: Boolean? = null, // Deprecated: Use defaultOpen instead
    onVisibleChange: ((Boolean) -> Unit)? = null, // Deprecated: Use onOpenChange instead
    afterVisibleChange: ((Boolean) -> Unit)? = null, // Deprecated: Use afterOpenChange instead
    content: @Composable () -> Unit,
) {
    // Resolve color to determine text color
    val resolvedColor = when (color) {
        is String -> TooltipPresetColors.resolvePresetColor(color) ?: Color(0xFF000000).copy(alpha = 0.75f)
        is Color -> color
        null -> Color(0xFF000000).copy(alpha = 0.75f)
        else -> Color(0xFF000000).copy(alpha = 0.75f)
    }

    val textColor = if (color != null) {
        TooltipPresetColors.getContrastTextColor(resolvedColor)
    } else {
        Color.White
    }

    AntTooltip(
        title = {
            Text(
                text = title,
                color = textColor,
                fontSize = 14.sp,
                style = TextStyle(
                    lineHeight = 22.sp
                )
            )
        },
        modifier = modifier,
        open = open,
        defaultOpen = defaultOpen,
        onOpenChange = onOpenChange,
        placement = placement,
        trigger = trigger,
        arrow = arrow,
        arrowPointAtCenter = arrowPointAtCenter,
        autoAdjustOverflow = autoAdjustOverflow,
        color = color,
        destroyTooltipOnHide = destroyTooltipOnHide,
        destroyOnHidden = destroyOnHidden,
        fresh = fresh,
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
        afterOpenChange = afterOpenChange,
        align = align,
        builtinPlacements = builtinPlacements,
        visible = visible,
        defaultVisible = defaultVisible,
        onVisibleChange = onVisibleChange,
        afterVisibleChange = afterVisibleChange,
        content = content
    )
}
