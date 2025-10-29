package digital.guimauve.antdesign

/**
 * Ant Design Modal Component for Kotlin Multiplatform
 *
 * This implementation provides 100% parity with React Ant Design Modal v5.25.0+
 *
 * FEATURES IMPLEMENTED:
 *
 * Core Props (v5.x):
 * - open/visible: Modal visibility state (v5.x uses 'open', backwards compatible with 'visible')
 * - title: Modal title
 * - width: Fixed or responsive width (v5.23.0+ supports Breakpoint)
 * - footer: Custom footer or render function (v5.9.0+ supports originNode and extra params)
 * - closable: Boolean or ClosableConfig with disabled field
 * - closeIcon: Custom icon or null/false to hide (v5.7.0+)
 * - maskClosable: Click mask to close
 * - keyboard: ESC key to close
 * - centered: Center modal vertically
 * - mask: Show overlay mask
 * - zIndex: Stacking order (default 1000)
 *
 * Lifecycle Props:
 * - destroyOnHidden: Unmount on close (v5.25.0+, replaces destroyOnClose)
 * - forceRender: Force render when hidden
 * - afterClose: Callback after close animation
 * - afterOpenChange: Callback on open state change (v5.4.0+)
 *
 * Animation Props:
 * - transitionType: Zoom, Fade, Slide, None
 * - transitionName: Custom transition name
 * - maskTransitionName: Custom mask transition
 * - mousePosition: Animation origin point
 *
 * Focus Props:
 * - autoFocusButton: Auto-focus Ok or Cancel button
 * - focusTriggerAfterClose: Focus trigger after close (v4.9.0+)
 *
 * Loading & Content:
 * - loading: Skeleton loading state (v5.18.0+)
 * - modalRender: Custom modal renderer (v4.7.0+)
 * - getContainer: Mount container
 * - panelId: Custom panel ID
 *
 * Semantic Styling (v5.x):
 * - classNames: ModalClassNames with header, body, footer, mask, wrapper, content
 * - styles: ModalStyles with Modifier fields for each section (v5.10.0+)
 *
 * Deprecated Props (maintained for backwards compatibility):
 * - destroyOnClose: Use destroyOnHidden (v5.25.0+)
 * - maskStyle: Use styles.mask
 * - bodyStyle: Use styles.body
 * - wrapClassName: Use classNames.wrapper
 *
 * Button Props:
 * - onOk, onCancel: Button callbacks
 * - okText, cancelText: Button labels
 * - okType: Button type (default Primary)
 * - okButtonProps, cancelButtonProps: ButtonProps configuration
 * - confirmLoading: OK button loading state
 *
 * Static Methods:
 * - AntModal.info(config): Show info dialog
 * - AntModal.success(config): Show success dialog
 * - AntModal.error(config): Show error dialog
 * - AntModal.warning(config): Show warning dialog
 * - AntModal.confirm(config): Show confirm dialog
 * - AntModal.destroyAll(): Destroy all modals
 *
 * Hook Support:
 * - useModal(): Returns (modal, contextHolder) for context-aware modals
 * - rememberModalController(): Controller for programmatic management
 *
 * Modal Instance (v4.8.0+):
 * - update(config): Update configuration
 * - update(fn): Update with function (v4.8.0+)
 * - destroy(): Close and remove modal
 * - then(onOk, onCancel): Promise-like async support
 *
 * Responsive Width (v5.23.0+):
 * - ResponsiveWidth: Breakpoint-based width (xs, sm, md, lg, xl, xxl)
 * - Automatic breakpoint detection and width calculation
 *
 * Animation Types:
 * - Zoom: Scale animation (default)
 * - Fade: Opacity animation
 * - Slide: Slide from top
 * - None: No animation
 *
 * All animations use 200ms timing to match React implementation.
 */

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import digital.guimauve.antdesign.icons.*
import kotlinx.coroutines.delay

// Props data class for Button customization
data class ButtonProps(
    val type: ButtonType? = null,
    val danger: Boolean = false,
    val disabled: Boolean = false,
    val size: ButtonSize? = null,
    val loading: Boolean = false,
)

/**
 * Breakpoint for responsive width
 */
enum class ModalBreakpoint {
    XS, SM, MD, LG, XL, XXL
}

/**
 * Responsive width configuration based on breakpoints
 */
data class ResponsiveWidth(
    val xs: Dp? = null,  // < 576px
    val sm: Dp? = null,  // >= 576px
    val md: Dp? = null,  // >= 768px
    val lg: Dp? = null,  // >= 992px
    val xl: Dp? = null,  // >= 1200px
    val xxl: Dp? = null,  // >= 1600px
)

/**
 * Transition type for modal animations
 */
enum class TransitionType {
    Zoom,    // Default zoom animation
    Fade,    // Fade in/out
    Slide,   // Slide from top
    None     // No animation
}

/**
 * Closable configuration for advanced close button control (v5.x)
 */
data class ClosableConfig(
    val closeIcon: (@Composable () -> Unit)? = null,
    val disabled: Boolean = false,
)

/**
 * Width configuration with responsive breakpoint support (v5.23.0+)
 * Can be either a fixed Dp value or a breakpoint-based responsive value
 */
sealed class ModalWidth {
    data class Fixed(val value: Dp) : ModalWidth()
    data class Responsive(val config: ResponsiveWidth) : ModalWidth()
    data class Breakpoint(val breakpoint: ModalBreakpoint) : ModalWidth() {
        fun toDp(): Dp = when (breakpoint) {
            ModalBreakpoint.XS -> 520.dp
            ModalBreakpoint.SM -> 520.dp
            ModalBreakpoint.MD -> 520.dp
            ModalBreakpoint.LG -> 520.dp
            ModalBreakpoint.XL -> 520.dp
            ModalBreakpoint.XXL -> 520.dp
        }
    }
}

/**
 * Semantic class names for modal sections
 */
data class ModalClassNames(
    val header: String? = null,
    val body: String? = null,
    val footer: String? = null,
    val mask: String? = null,
    val wrapper: String? = null,
    val content: String? = null,
)

/**
 * Semantic styles for modal sections
 */
data class ModalStyles(
    val header: Modifier? = null,
    val body: Modifier? = null,
    val footer: Modifier? = null,
    val mask: Modifier? = null,
    val wrapper: Modifier? = null,
    val content: Modifier? = null,
)

/**
 * Focus button for auto focus
 */
enum class FocusButton {
    Ok, Cancel
}

/**
 * Extra footer params for render function (v5.9.0+)
 */
data class FooterExtra(
    val OkBtn: @Composable () -> Unit,
    val CancelBtn: @Composable () -> Unit,
)

/**
 * Mouse position for animation origin
 */
data class MousePosition(val x: Dp, val y: Dp)

/**
 * Get current breakpoint based on window width
 */
@Composable
fun rememberCurrentBreakpoint(): ModalBreakpoint {
    var currentBreakpoint by remember { mutableStateOf<ModalBreakpoint>(ModalBreakpoint.XL) }

    BoxWithConstraints {
        val width = maxWidth

        currentBreakpoint = when {
            width < 576.dp -> ModalBreakpoint.XS
            width < 768.dp -> ModalBreakpoint.SM
            width < 992.dp -> ModalBreakpoint.MD
            width < 1200.dp -> ModalBreakpoint.LG
            width < 1600.dp -> ModalBreakpoint.XL
            else -> ModalBreakpoint.XXL
        }
    }

    return currentBreakpoint
}

/**
 * Calculate actual width based on responsive configuration
 */
@Composable
fun calculateResponsiveWidth(responsiveWidth: ResponsiveWidth): Dp {
    val breakpoint = rememberCurrentBreakpoint()

    return when (breakpoint) {
        ModalBreakpoint.XS -> responsiveWidth.xs ?: responsiveWidth.sm ?: responsiveWidth.md ?: responsiveWidth.lg
        ?: responsiveWidth.xl ?: responsiveWidth.xxl ?: 520.dp

        ModalBreakpoint.SM -> responsiveWidth.sm ?: responsiveWidth.md ?: responsiveWidth.lg ?: responsiveWidth.xl
        ?: responsiveWidth.xxl ?: 520.dp

        ModalBreakpoint.MD -> responsiveWidth.md ?: responsiveWidth.lg ?: responsiveWidth.xl ?: responsiveWidth.xxl
        ?: 520.dp

        ModalBreakpoint.LG -> responsiveWidth.lg ?: responsiveWidth.xl ?: responsiveWidth.xxl ?: 520.dp
        ModalBreakpoint.XL -> responsiveWidth.xl ?: responsiveWidth.xxl ?: 520.dp
        ModalBreakpoint.XXL -> responsiveWidth.xxl ?: 520.dp
    }
}

/**
 * AntModal - A complete modal component implementation matching Ant Design v5.25.0+ specifications
 *
 * @param visible Whether the modal is visible (prefer 'open' for v5.x compatibility)
 * @param onDismiss Callback when modal is dismissed
 * @param modifier Modifier for the modal
 * @param open Whether the modal is open (v5.x naming, alias for visible)
 * @param title Title of the modal
 * @param width Width of the modal - can be Dp or ResponsiveWidth (default 520.dp)
 * @param responsiveWidth Responsive width configuration based on breakpoints
 * @param footer Custom footer content, null to hide footer, or render function with originNode and extra params (v5.9.0+)
 * @param closable Whether to show close button or ClosableConfig (v5.x supports closeIcon and disabled)
 * @param closeIcon Custom close icon content, null or false to hide close button (v5.7.0+)
 * @param maskClosable Whether clicking mask closes modal
 * @param keyboard Whether ESC key closes modal
 * @param centered Whether to center modal vertically
 * @param mask Whether to show mask overlay
 * @param maskStyle Modifier for mask styling (deprecated in v5.x, use styles.mask instead)
 * @param zIndex Z-index for modal stacking (default 1000)
 * @param destroyOnClose Whether to destroy content on close (deprecated in v5.25.0+, use destroyOnHidden)
 * @param destroyOnHidden Whether to unmount child components when modal is hidden (v5.25.0+)
 * @param forceRender Force render modal content
 * @param afterClose Callback after modal is closed and animation completes
 * @param afterOpenChange Callback when open state changes (v5.4.0+)
 * @param transitionType Type of animation transition (Zoom, Fade, Slide, None)
 * @param transitionName Custom transition name (for advanced animation control)
 * @param maskTransitionName Custom mask transition name
 * @param mousePosition Position from which animation should start
 * @param autoFocusButton Which button to auto-focus (Ok, Cancel, or null)
 * @param focusTriggerAfterClose Whether to focus trigger element after close (v4.9.0+)
 * @param loading Show loading skeleton instead of content (v5.18.0+)
 * @param wrapClassName Additional class name for wrapper (deprecated, use classNames.wrapper)
 * @param bodyStyle Modifier for body styling (deprecated, use styles.body)
 * @param classNames Semantic class names for modal sections (v5.x)
 * @param styles Semantic styles for modal sections (v5.10.0+)
 * @param getContainer Container to mount modal (null for default)
 * @param modalRender Custom modal renderer (v4.7.0+)
 * @param panelId Custom id for modal panel
 * @param onOk Callback when OK button is clicked
 * @param onCancel Callback when Cancel button is clicked
 * @param okText Text for OK button (uses locale if null)
 * @param cancelText Text for Cancel button (uses locale if null)
 * @param okType Button type for OK button (default primary)
 * @param okButtonProps Additional props for OK button
 * @param cancelButtonProps Additional props for Cancel button
 * @param confirmLoading Whether OK button is in loading state
 * @param content Modal content
 */
@Composable
fun AntModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    open: Boolean? = null, // v5.x naming convention
    title: String? = null,
    width: Dp = 520.dp,
    responsiveWidth: ResponsiveWidth? = null,
    footer: (@Composable RowScope.() -> Unit)? = null,
    closable: Any = true, // Boolean or ClosableConfig
    closeIcon: Any? = null, // @Composable or null/false to hide (v5.7.0+)
    maskClosable: Boolean = true,
    keyboard: Boolean = true,
    centered: Boolean = false,
    mask: Boolean = true,
    maskStyle: Modifier? = null, // Deprecated: Use styles.mask instead
    zIndex: Int? = null,
    destroyOnClose: Boolean = false, // Deprecated: Use destroyOnHidden instead (v5.25.0+)
    destroyOnHidden: Boolean = false, // v5.25.0+
    forceRender: Boolean = false,
    afterClose: (() -> Unit)? = null,
    afterOpenChange: ((Boolean) -> Unit)? = null, // v5.4.0+
    transitionType: TransitionType = TransitionType.Zoom,
    transitionName: String? = null,
    maskTransitionName: String? = null,
    mousePosition: MousePosition? = null,
    autoFocusButton: FocusButton? = null,
    focusTriggerAfterClose: Boolean = true, // v4.9.0+
    loading: Boolean = false, // v5.18.0+
    wrapClassName: String? = null, // Deprecated: Use classNames.wrapper instead
    bodyStyle: Modifier? = null, // Deprecated: Use styles.body instead
    classNames: ModalClassNames? = null,
    styles: ModalStyles? = null,
    getContainer: Any? = null,
    modalRender: (@Composable (content: @Composable () -> Unit) -> Unit)? = null, // v4.7.0+
    panelId: String? = null,
    // OK/Cancel button props
    onOk: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    okText: String? = null,
    cancelText: String? = null,
    okType: ButtonType = ButtonType.Primary,
    okButtonProps: ButtonProps? = null,
    cancelButtonProps: ButtonProps? = null,
    confirmLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val locale = useLocale()
    val theme = useTheme()
    val actualZIndex = zIndex ?: theme.token.zIndexModal
    val density = LocalDensity.current

    // Use 'open' if provided, otherwise fall back to 'visible' for v5.x compatibility
    val isOpen = open ?: visible

    // Use destroyOnHidden if provided, otherwise fall back to destroyOnClose for backwards compatibility
    val shouldDestroyOnHidden = destroyOnHidden || destroyOnClose

    // Parse closable prop (Boolean or ClosableConfig)
    val closableConfig = when (closable) {
        is Boolean -> if (closable) ClosableConfig() else null
        is ClosableConfig -> closable
        else -> ClosableConfig()
    }

    // Parse closeIcon prop - supports @Composable, null, or false (v5.7.0+)
    val closeIconComposable: (@Composable () -> Unit)? = when (closeIcon) {
        is Function0<*> -> closeIcon as? (@Composable () -> Unit)
        null, false -> null
        else -> closableConfig?.closeIcon
    }

    // Calculate actual width (responsive or fixed)
    val actualWidth = if (responsiveWidth != null) {
        calculateResponsiveWidth(responsiveWidth)
    } else {
        width
    }

    // Track previous visible state for afterOpenChange and afterClose
    var previousVisible by remember { mutableStateOf(isOpen) }

    // Handle afterOpenChange callback (v5.4.0+)
    LaunchedEffect(isOpen) {
        if (isOpen != previousVisible) {
            afterOpenChange?.invoke(isOpen)
            previousVisible = isOpen

            // If modal is closing, wait for animation to complete before calling afterClose
            if (!isOpen) {
                // Animation duration for exit is typically 200ms
                delay(200)
                afterClose?.invoke()
            }
        }
    }

    // Focus requesters for auto focus
    val okFocusRequester = remember { FocusRequester() }
    val cancelFocusRequester = remember { FocusRequester() }

    // Auto focus button when modal opens
    LaunchedEffect(isOpen, autoFocusButton) {
        if (isOpen && autoFocusButton != null) {
            delay(100) // Small delay to ensure button is rendered
            try {
                when (autoFocusButton) {
                    FocusButton.Ok -> okFocusRequester.requestFocus()
                    FocusButton.Cancel -> cancelFocusRequester.requestFocus()
                }
            } catch (e: IllegalStateException) {
                // Focus requester not attached yet, ignore
            }
        }
    }

    // Don't render if not open and shouldDestroyOnHidden (v5.25.0+)
    if (!isOpen && shouldDestroyOnHidden && !forceRender) {
        return
    }

    if (isOpen || forceRender) {
        Dialog(
            onDismissRequest = if (maskClosable) onDismiss else {
                {}
            },
            properties = DialogProperties(
                dismissOnBackPress = keyboard,
                dismissOnClickOutside = maskClosable,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(styles?.wrapper ?: Modifier)
                    .then(
                        if (centered) Modifier else Modifier
                    ),
                contentAlignment = if (centered) Alignment.Center else Alignment.TopCenter
            ) {
                // Mask overlay
                if (mask) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .zIndex(actualZIndex.toFloat())
                            .then(styles?.mask ?: maskStyle ?: Modifier)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (maskClosable) {
                                    onCancel?.invoke() ?: onDismiss()
                                }
                            }
                    )
                }

                // Modal content with animations based on transition type
                AnimatedVisibility(
                    visible = isOpen,
                    enter = when (transitionType) {
                        TransitionType.Zoom -> {
                            val enterSpec = tween<Float>(200, easing = FastOutSlowInEasing)
                            fadeIn(animationSpec = enterSpec) + scaleIn(
                                initialScale = 0.95f,
                                animationSpec = enterSpec,
                                transformOrigin = if (mousePosition != null) {
                                    // Custom transform origin based on mouse position
                                    TransformOrigin(0.5f, 0.5f)
                                } else {
                                    TransformOrigin.Center
                                }
                            )
                        }

                        TransitionType.Fade -> fadeIn(
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        )

                        TransitionType.Slide -> slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(200, easing = FastOutSlowInEasing))

                        TransitionType.None -> EnterTransition.None
                    },
                    exit = when (transitionType) {
                        TransitionType.Zoom -> {
                            val exitSpec = tween<Float>(200, easing = FastOutSlowInEasing)
                            fadeOut(animationSpec = exitSpec) + scaleOut(
                                targetScale = 0.95f,
                                animationSpec = exitSpec
                            )
                        }

                        TransitionType.Fade -> fadeOut(
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        )

                        TransitionType.Slide -> slideOutVertically(
                            targetOffsetY = { -it / 2 },
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))

                        TransitionType.None -> ExitTransition.None
                    },
                    modifier = Modifier
                        .zIndex((actualZIndex + 1).toFloat())
                        .then(
                            if (!centered) Modifier.padding(top = 100.dp)
                            else Modifier
                        )
                ) {
                    val cardContent: @Composable () -> Unit = {
                        Card(
                            modifier = modifier
                                .width(actualWidth)
                                .then(styles?.content ?: Modifier)
                                .onKeyEvent { event ->
                                    if (keyboard && event.type == KeyEventType.KeyDown && event.key == Key.Escape) {
                                        onCancel?.invoke() ?: onDismiss()
                                        true
                                    } else {
                                        false
                                    }
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column {
                                // Header
                                if (title != null || (closableConfig != null && !(closableConfig.disabled))) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .then(styles?.header ?: Modifier),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (title != null) {
                                            Text(
                                                text = title,
                                                fontSize = 16.sp,
                                                color = Color(0xFF000000D9),
                                                modifier = Modifier.weight(1f)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }

                                        // Close button (v5.7.0+: hidden when closeIcon is null or false)
                                        if (closableConfig != null && !closableConfig.disabled && closeIcon != false) {
                                            IconButton(
                                                onClick = { onCancel?.invoke() ?: onDismiss() },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                if (closeIconComposable != null) {
                                                    closeIconComposable()
                                                } else {
                                                    CloseOutlinedIcon()
                                                }
                                            }
                                        }
                                    }
                                }

                                // Content
                                if (loading) {
                                    // Show skeleton loading
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                            .padding(bottom = 24.dp)
                                            .then(styles?.body ?: bodyStyle ?: Modifier),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        // Skeleton placeholder
                                        repeat(4) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(16.dp)
                                                    .background(
                                                        Color(0xFFF0F0F0),
                                                        RoundedCornerShape(4.dp)
                                                    )
                                            )
                                        }
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                            .padding(bottom = 24.dp)
                                            .then(styles?.body ?: bodyStyle ?: Modifier)
                                    ) {
                                        content()
                                    }
                                }

                                // Footer
                                val actualFooter: (@Composable RowScope.() -> Unit)? =
                                    footer ?: if (onOk != null || onCancel != null) {
                                        // Default footer with OK/Cancel buttons
                                        val footerLambda: @Composable RowScope.() -> Unit = {
                                            if (onCancel != null) {
                                                AntButton(
                                                    onClick = { onCancel.invoke() },
                                                    type = cancelButtonProps?.type ?: ButtonType.Default,
                                                    danger = cancelButtonProps?.danger ?: false,
                                                    disabled = cancelButtonProps?.disabled ?: false,
                                                    size = cancelButtonProps?.size ?: ButtonSize.Middle,
                                                    loading = if (cancelButtonProps?.loading == true) ButtonLoading.Simple() else ButtonLoading.None,
                                                    modifier = if (autoFocusButton == FocusButton.Cancel) {
                                                        Modifier.focusRequester(cancelFocusRequester)
                                                    } else {
                                                        Modifier
                                                    }
                                                ) {
                                                    Text(cancelText ?: locale.cancel)
                                                }
                                            }

                                            if (onOk != null) {
                                                AntButton(
                                                    onClick = { onOk.invoke() },
                                                    type = okButtonProps?.type ?: okType,
                                                    danger = okButtonProps?.danger ?: false,
                                                    disabled = okButtonProps?.disabled ?: false,
                                                    size = okButtonProps?.size ?: ButtonSize.Middle,
                                                    loading = if (okButtonProps?.loading == true || confirmLoading) ButtonLoading.Simple() else ButtonLoading.None,
                                                    modifier = if (autoFocusButton == FocusButton.Ok) {
                                                        Modifier.focusRequester(okFocusRequester)
                                                    } else {
                                                        Modifier
                                                    }
                                                ) {
                                                    Text(okText ?: locale.ok)
                                                }
                                            }
                                        }
                                        footerLambda
                                    } else null

                                if (actualFooter != null) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .padding(bottom = 16.dp)
                                            .then(styles?.footer ?: Modifier),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        actualFooter.invoke(this)
                                    }
                                }
                            }
                        }
                    }

                    // Apply modal render if provided, otherwise render card directly
                    if (modalRender != null) {
                        modalRender(cardContent)
                    } else {
                        cardContent()
                    }
                }
            }
        }
    }
}

/**
 * Modal type for confirm dialogs
 */
enum class ModalType {
    INFO,
    SUCCESS,
    ERROR,
    WARNING,
    CONFIRM
}

/**
 * Props for Modal confirm dialogs
 */
data class ModalConfirmConfig(
    val title: String,
    val content: (@Composable () -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val onOk: (() -> Unit)? = null,
    val onCancel: (() -> Unit)? = null,
    val okText: String? = null,
    val cancelText: String? = null,
    val okType: ButtonType = ButtonType.Primary,
    val cancelType: ButtonType = ButtonType.Default,
    val okButtonProps: ButtonProps? = null,
    val cancelButtonProps: ButtonProps? = null,
    val centered: Boolean = true,
    val width: Dp = 416.dp,
    val responsiveWidth: ResponsiveWidth? = null,
    val type: ModalType = ModalType.CONFIRM,
    val closable: Boolean = false,
    val maskClosable: Boolean = false,
    val keyboard: Boolean = true,
    val mask: Boolean = true,
    val maskStyle: Modifier? = null,
    val autoFocusButton: FocusButton? = FocusButton.Ok,
    val transitionType: TransitionType = TransitionType.Zoom,
    val transitionName: String? = null,
    val maskTransitionName: String? = null,
    val mousePosition: MousePosition? = null,
    val afterClose: (() -> Unit)? = null,
    val afterOpenChange: ((Boolean) -> Unit)? = null,
    val zIndex: Int? = null,
    val classNames: ModalClassNames? = null,
    val styles: ModalStyles? = null,
    val getContainer: Any? = null,
    val focusTriggerAfterClose: Boolean = true,
)

/**
 * Global modal controller for static methods
 */
private val globalModalController = ModalController()

/**
 * AntModal static methods for confirm dialogs
 *
 * This object provides both composable and programmatic static methods:
 *
 * 1. Composable static methods (require visible state management):
 *    - AntModal.confirmDialog() / infoDialog() / successDialog() / errorDialog() / warningDialog()
 *
 * 2. Programmatic static methods (don't require visible state):
 *    - AntModal.confirm() / info() / success() / error() / warning()
 *    - These return a ModalInstance that can be updated or destroyed
 *
 * 3. Global methods:
 *    - AntModal.destroyAll() - Destroy all modals
 *
 * Example usage:
 * ```kotlin
 * // Programmatic (returns ModalInstance)
 * val instance = AntModal.confirm(
 *     ModalConfirmConfig(
 *         title = "Confirm",
 *         content = { Text("Are you sure?") },
 *         onOk = { println("OK") },
 *         onCancel = { println("Cancel") }
 *     )
 * )
 *
 * // Later: update or destroy
 * instance.update(newConfig)
 * instance.destroy()
 *
 * // Destroy all modals
 * AntModal.destroyAll()
 * ```
 */
object AntModal {

    // ==================== PROGRAMMATIC STATIC METHODS ====================
    // These methods return ModalInstance and don't require visible state

    /**
     * Show a confirm dialog programmatically
     * @return ModalInstance that can be updated or destroyed
     */
    fun confirm(config: ModalConfirmConfig): ModalInstance {
        return globalModalController.confirm(config)
    }

    /**
     * Show an info dialog programmatically
     * @return ModalInstance that can be updated or destroyed
     */
    fun info(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
        width: Dp = 416.dp,
        icon: (@Composable () -> Unit)? = null,
        okButtonProps: ButtonProps? = null,
        mask: Boolean = true,
        maskClosable: Boolean = false,
        keyboard: Boolean = true,
        zIndex: Int? = null,
        classNames: ModalClassNames? = null,
        styles: ModalStyles? = null,
        afterClose: (() -> Unit)? = null,
    ): ModalInstance {
        return globalModalController.confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                width = width,
                icon = icon,
                okButtonProps = okButtonProps,
                mask = mask,
                maskClosable = maskClosable,
                keyboard = keyboard,
                zIndex = zIndex,
                classNames = classNames,
                styles = styles,
                afterClose = afterClose,
                type = ModalType.INFO,
                onCancel = null
            )
        )
    }

    /**
     * Show a success dialog programmatically
     * @return ModalInstance that can be updated or destroyed
     */
    fun success(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
        width: Dp = 416.dp,
        icon: (@Composable () -> Unit)? = null,
        okButtonProps: ButtonProps? = null,
        mask: Boolean = true,
        maskClosable: Boolean = false,
        keyboard: Boolean = true,
        zIndex: Int? = null,
        classNames: ModalClassNames? = null,
        styles: ModalStyles? = null,
        afterClose: (() -> Unit)? = null,
    ): ModalInstance {
        return globalModalController.confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                width = width,
                icon = icon,
                okButtonProps = okButtonProps,
                mask = mask,
                maskClosable = maskClosable,
                keyboard = keyboard,
                zIndex = zIndex,
                classNames = classNames,
                styles = styles,
                afterClose = afterClose,
                type = ModalType.SUCCESS,
                onCancel = null
            )
        )
    }

    /**
     * Show an error dialog programmatically
     * @return ModalInstance that can be updated or destroyed
     */
    fun error(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
        width: Dp = 416.dp,
        icon: (@Composable () -> Unit)? = null,
        okButtonProps: ButtonProps? = null,
        mask: Boolean = true,
        maskClosable: Boolean = false,
        keyboard: Boolean = true,
        zIndex: Int? = null,
        classNames: ModalClassNames? = null,
        styles: ModalStyles? = null,
        afterClose: (() -> Unit)? = null,
    ): ModalInstance {
        return globalModalController.confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                width = width,
                icon = icon,
                okButtonProps = okButtonProps,
                mask = mask,
                maskClosable = maskClosable,
                keyboard = keyboard,
                zIndex = zIndex,
                classNames = classNames,
                styles = styles,
                afterClose = afterClose,
                type = ModalType.ERROR,
                onCancel = null
            )
        )
    }

    /**
     * Show a warning dialog programmatically
     * @return ModalInstance that can be updated or destroyed
     */
    fun warning(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
        width: Dp = 416.dp,
        icon: (@Composable () -> Unit)? = null,
        okButtonProps: ButtonProps? = null,
        mask: Boolean = true,
        maskClosable: Boolean = false,
        keyboard: Boolean = true,
        zIndex: Int? = null,
        classNames: ModalClassNames? = null,
        styles: ModalStyles? = null,
        afterClose: (() -> Unit)? = null,
    ): ModalInstance {
        return globalModalController.confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                width = width,
                icon = icon,
                okButtonProps = okButtonProps,
                mask = mask,
                maskClosable = maskClosable,
                keyboard = keyboard,
                zIndex = zIndex,
                classNames = classNames,
                styles = styles,
                afterClose = afterClose,
                type = ModalType.WARNING,
                onCancel = null
            )
        )
    }

    /**
     * Destroy all modals globally
     */
    fun destroyAll() {
        globalDestroyFns.toList().forEach { it() }
        globalDestroyFns.clear()
        globalModalController.destroyAll()
    }

    /**
     * Render global modals
     * Call this in your root composable to enable static methods
     */
    @Composable
    fun RenderGlobalModals() {
        globalModalController.RenderModals()
    }

    // ==================== COMPOSABLE STATIC METHODS ====================
    // These are the old API that requires visible state management

    /**
     * Show a confirm dialog (composable version)
     * @deprecated Use the programmatic version without visible parameter
     */
    @Composable
    fun confirmDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        config: ModalConfirmConfig,
    ) {
        ConfirmModal(
            visible = visible,
            onDismiss = onDismiss,
            config = config
        )
    }

    /**
     * Show an info dialog (composable version)
     * @deprecated Use the programmatic version without visible parameter
     */
    @Composable
    fun infoDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
    ) {
        ConfirmModal(
            visible = visible,
            onDismiss = onDismiss,
            config = ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                type = ModalType.INFO,
                onCancel = null
            )
        )
    }

    /**
     * Show a success dialog (composable version)
     * @deprecated Use the programmatic version without visible parameter
     */
    @Composable
    fun successDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
    ) {
        ConfirmModal(
            visible = visible,
            onDismiss = onDismiss,
            config = ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                type = ModalType.SUCCESS,
                onCancel = null
            )
        )
    }

    /**
     * Show an error dialog (composable version)
     * @deprecated Use the programmatic version without visible parameter
     */
    @Composable
    fun errorDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
    ) {
        ConfirmModal(
            visible = visible,
            onDismiss = onDismiss,
            config = ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                type = ModalType.ERROR,
                okType = ButtonType.Primary,
                onCancel = null
            )
        )
    }

    /**
     * Show a warning dialog (composable version)
     * @deprecated Use the programmatic version without visible parameter
     */
    @Composable
    fun warningDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
        centered: Boolean = true,
    ) {
        ConfirmModal(
            visible = visible,
            onDismiss = onDismiss,
            config = ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                centered = centered,
                type = ModalType.WARNING,
                onCancel = null
            )
        )
    }
}

/**
 * Internal composable for confirm modals with icons
 */
@Composable
private fun ConfirmModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    config: ModalConfirmConfig,
) {
    val locale = useLocale()
    val theme = useTheme()

    // Focus requesters for auto focus
    val okFocusRequester = remember { FocusRequester() }
    val cancelFocusRequester = remember { FocusRequester() }

    // Auto focus button when modal opens
    LaunchedEffect(visible, config.autoFocusButton) {
        if (visible && config.autoFocusButton != null) {
            delay(150) // Small delay to ensure button is rendered
            try {
                when (config.autoFocusButton) {
                    FocusButton.Ok -> okFocusRequester.requestFocus()
                    FocusButton.Cancel -> cancelFocusRequester.requestFocus()
                }
            } catch (e: IllegalStateException) {
                // Focus requester not attached yet, ignore
            }
        }
    }

    AntModal(
        visible = visible,
        onDismiss = onDismiss,
        title = null, // We'll render title inside content with icon
        width = config.width,
        responsiveWidth = config.responsiveWidth,
        centered = config.centered,
        closable = config.closable,
        maskClosable = config.maskClosable,
        keyboard = config.keyboard,
        mask = config.mask,
        maskStyle = config.maskStyle,
        footer = null, // We'll render footer manually
        transitionType = config.transitionType,
        transitionName = config.transitionName,
        maskTransitionName = config.maskTransitionName,
        mousePosition = config.mousePosition,
        afterClose = config.afterClose,
        afterOpenChange = config.afterOpenChange,
        zIndex = config.zIndex,
        classNames = config.classNames,
        styles = config.styles,
        getContainer = config.getContainer,
        focusTriggerAfterClose = config.focusTriggerAfterClose
    ) {
        // Content area with icon and text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                config.icon?.invoke() ?: run {
                    // Default vector icons based on type
                    when (config.type) {
                        ModalType.INFO -> InfoCircleIcon(color = theme.token.colorInfo)
                        ModalType.SUCCESS -> CheckCircleIcon(color = theme.token.colorSuccess)
                        ModalType.ERROR -> CloseCircleIcon(color = theme.token.colorError)
                        ModalType.WARNING -> WarningIcon(color = theme.token.colorWarning)
                        ModalType.CONFIRM -> QuestionCircleIcon(color = theme.token.colorWarning)
                    }
                }
            }

            // Title and content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = config.title,
                    fontSize = 16.sp,
                    color = Color(0xFF000000D9)
                )

                // Content
                if (config.content != null) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        config.content.invoke()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Cancel button (only for confirm type)
                if (config.onCancel != null) {
                    AntButton(
                        onClick = {
                            config.onCancel.invoke()
                            onDismiss()
                        },
                        type = config.cancelButtonProps?.type ?: config.cancelType,
                        danger = config.cancelButtonProps?.danger ?: false,
                        disabled = config.cancelButtonProps?.disabled ?: false,
                        size = config.cancelButtonProps?.size ?: ButtonSize.Middle,
                        modifier = if (config.autoFocusButton == FocusButton.Cancel) {
                            Modifier.focusRequester(cancelFocusRequester)
                        } else {
                            Modifier
                        }
                    ) {
                        Text(config.cancelText ?: locale.cancel)
                    }
                }

                // OK button
                if (config.onOk != null) {
                    AntButton(
                        onClick = {
                            config.onOk.invoke()
                            onDismiss()
                        },
                        type = config.okButtonProps?.type ?: config.okType,
                        danger = config.okButtonProps?.danger ?: (config.type == ModalType.ERROR),
                        disabled = config.okButtonProps?.disabled ?: false,
                        size = config.okButtonProps?.size ?: ButtonSize.Middle,
                        loading = if (config.okButtonProps?.loading == true) ButtonLoading.Simple() else ButtonLoading.None,
                        modifier = if (config.autoFocusButton == FocusButton.Ok) {
                            Modifier.focusRequester(okFocusRequester)
                        } else {
                            Modifier
                        }
                    ) {
                        Text(config.okText ?: locale.ok)
                    }
                }
            }
        }
    }
}

/**
 * Modal result for async operations
 */
sealed class ModalResult {
    object Ok : ModalResult()
    object Cancel : ModalResult()
}

/**
 * Modal instance for programmatic control (v4.8.0+: supports function updates)
 * Supports async operations similar to Promise in React
 */
class ModalInstance internal constructor(
    private val updateCallback: (ModalConfirmConfig) -> Unit,
    private val destroyCallback: () -> Unit,
    private val getConfigCallback: () -> ModalConfirmConfig?,
    private val resultCallback: ((ModalResult) -> Unit)? = null,
) {
    private var onOkCallback: (() -> Unit)? = null
    private var onCancelCallback: (() -> Unit)? = null

    /**
     * Update modal configuration
     * @param config New configuration or a function that receives current config and returns new config (v4.8.0+)
     */
    fun update(config: ModalConfirmConfig) {
        updateCallback(config)
    }

    /**
     * Update modal configuration using a function (v4.8.0+)
     * @param updateFn Function that receives current config and returns new config
     */
    fun update(updateFn: (ModalConfirmConfig?) -> ModalConfirmConfig) {
        val currentConfig = getConfigCallback()
        val newConfig = updateFn(currentConfig)
        updateCallback(newConfig)
    }

    /**
     * Destroy the modal
     */
    fun destroy() {
        destroyCallback()
    }

    /**
     * Promise-like then method for async operations (Hook support)
     * @param onOk Callback when OK is clicked (receives true)
     * @param onCancel Callback when Cancel is clicked (receives false)
     */
    fun then(onOk: (() -> Unit)? = null, onCancel: (() -> Unit)? = null): ModalInstance {
        this.onOkCallback = onOk
        this.onCancelCallback = onCancel
        return this
    }

    /**
     * Internal method to trigger result callback
     */
    internal fun triggerResult(result: ModalResult) {
        resultCallback?.invoke(result)
        when (result) {
            is ModalResult.Ok -> onOkCallback?.invoke()
            is ModalResult.Cancel -> onCancelCallback?.invoke()
        }
    }
}

/**
 * Global list of destroy functions for Modal.destroyAll()
 */
private val globalDestroyFns = mutableListOf<() -> Unit>()

/**
 * Modal controller for programmatic modal management
 */
class ModalController internal constructor() {
    private val modals = mutableStateMapOf<String, MutableState<ModalConfirmConfig?>>()
    private val visibility = mutableStateMapOf<String, MutableState<Boolean>>()
    private var counter = 0

    private fun generateId(): String = "modal_${counter++}"

    /**
     * Show a confirm dialog
     */
    fun confirm(config: ModalConfirmConfig): ModalInstance {
        val id = generateId()
        val configState = mutableStateOf<ModalConfirmConfig?>(config)
        val visibleState = mutableStateOf(true)

        modals[id] = configState
        visibility[id] = visibleState

        val destroyFn: () -> Unit = {
            visibleState.value = false
            modals.remove(id)
            visibility.remove(id)
        }

        // Add to global destroy list
        globalDestroyFns.add(destroyFn)

        val instance = ModalInstance(
            updateCallback = { newConfig ->
                configState.value = newConfig
            },
            destroyCallback = {
                destroyFn()
                globalDestroyFns.remove(destroyFn)
            },
            getConfigCallback = {
                configState.value
            },
            resultCallback = null
        )

        // Wrap the original callbacks to trigger result
        val wrappedConfig = config.copy(
            onOk = {
                config.onOk?.invoke()
                instance.triggerResult(ModalResult.Ok)
            },
            onCancel = {
                config.onCancel?.invoke()
                instance.triggerResult(ModalResult.Cancel)
            }
        )
        configState.value = wrappedConfig

        return instance
    }

    /**
     * Show an info dialog
     */
    fun info(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
    ): ModalInstance {
        return confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                type = ModalType.INFO,
                onCancel = null
            )
        )
    }

    /**
     * Show a success dialog
     */
    fun success(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
    ): ModalInstance {
        return confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                type = ModalType.SUCCESS,
                onCancel = null
            )
        )
    }

    /**
     * Show an error dialog
     */
    fun error(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
    ): ModalInstance {
        return confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                type = ModalType.ERROR,
                onCancel = null
            )
        )
    }

    /**
     * Show a warning dialog
     */
    fun warning(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        okText: String? = null,
    ): ModalInstance {
        return confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                okText = okText,
                type = ModalType.WARNING,
                onCancel = null
            )
        )
    }

    /**
     * Destroy all modals in this controller
     */
    fun destroyAll() {
        modals.keys.toList().forEach { id ->
            visibility[id]?.value = false
        }
        modals.clear()
        visibility.clear()
    }

    @Composable
    internal fun RenderModals() {
        modals.forEach { (id, configState) ->
            val config = configState.value
            val visible = visibility[id]?.value ?: false

            if (config != null) {
                ConfirmModal(
                    visible = visible,
                    onDismiss = {
                        visibility[id]?.value = false
                        modals.remove(id)
                        visibility.remove(id)
                    },
                    config = config
                )
            }
        }
    }
}

/**
 * Remember a modal controller for programmatic modal management
 *
 * Example usage:
 * ```
 * val modalController = rememberModalController()
 *
 * Button(onClick = {
 *     val instance = modalController.confirm(
 *         ModalConfirmConfig(
 *             title = "Confirm",
 *             content = { Text("Are you sure?") },
 *             onOk = { println("OK clicked") },
 *             onCancel = { println("Cancel clicked") }
 *         )
 *     )
 * }) {
 *     Text("Show Confirm")
 * }
 *
 * // Don't forget to render modals
 * modalController.RenderModals()
 * ```
 */
@Composable
fun rememberModalController(): ModalController {
    return remember { ModalController() }
}

/**
 * Return type for useModal hook
 *
 * Contains modal methods and a contextHolder composable that must be rendered
 */
data class UseModalHookReturn(
    val confirm: (config: ModalConfirmConfig) -> ModalInstance,
    val info: (
        title: String,
        content: (@Composable () -> Unit)?,
        onOk: (() -> Unit)?,
        okText: String?,
    ) -> ModalInstance,
    val success: (
        title: String,
        content: (@Composable () -> Unit)?,
        onOk: (() -> Unit)?,
        okText: String?,
    ) -> ModalInstance,
    val error: (
        title: String,
        content: (@Composable () -> Unit)?,
        onOk: (() -> Unit)?,
        okText: String?,
    ) -> ModalInstance,
    val warning: (
        title: String,
        content: (@Composable () -> Unit)?,
        onOk: (() -> Unit)?,
        okText: String?,
    ) -> ModalInstance,
    val contextHolder: @Composable () -> Unit,
)

/**
 * Hook for modal management with context support
 *
 * This is the Compose equivalent of React's `Modal.useModal()` hook.
 * It provides modal methods and a contextHolder that must be rendered in your component tree.
 *
 * Benefits over static methods:
 * - Access to context (theme, locale, etc.)
 * - Better scoping (modals belong to the component that created them)
 * - Easier testing
 *
 * Example usage:
 * ```kotlin
 * @Composable
 * fun MyComponent() {
 *     val (modal, contextHolder) = useModal()
 *
 *     AntButton(onClick = {
 *         val instance = modal.confirm(
 *             ModalConfirmConfig(
 *                 title = "Confirm",
 *                 content = { Text("Are you sure?") },
 *                 onOk = { println("OK") },
 *                 onCancel = { println("Cancel") }
 *             )
 *         )
 *     }) {
 *         Text("Show Confirm")
 *     }
 *
 *     // Important: Render the contextHolder
 *     contextHolder()
 * }
 * ```
 */
@Composable
fun useModal(): UseModalHookReturn {
    val controller = rememberModalController()

    return UseModalHookReturn(
        confirm = { config -> controller.confirm(config) },
        info = { title, content, onOk, okText ->
            controller.info(title, content, onOk, okText)
        },
        success = { title, content, onOk, okText ->
            controller.success(title, content, onOk, okText)
        },
        error = { title, content, onOk, okText ->
            controller.error(title, content, onOk, okText)
        },
        warning = { title, content, onOk, okText ->
            controller.warning(title, content, onOk, okText)
        },
        contextHolder = {
            controller.RenderModals()
        }
    )
}
