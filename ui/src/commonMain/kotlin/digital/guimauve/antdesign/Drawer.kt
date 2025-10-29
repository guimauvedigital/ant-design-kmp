package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import digital.guimauve.antdesign.icons.CloseOutlinedIcon
import kotlinx.coroutines.delay

/**
 * Drawer placement enum - determines from which side the drawer slides in
 */
enum class DrawerPlacement {
    /** Drawer slides in from the left side */
    Left,

    /** Drawer slides in from the right side */
    Right,

    /** Drawer slides in from the top */
    Top,

    /** Drawer slides in from the bottom */
    Bottom
}

/**
 * Drawer size preset enum
 */
enum class DrawerSize {
    /** Default size: 378dp */
    Default,

    /** Large size: 736dp */
    Large
}

/**
 * Configuration for drawer push behavior when multiple drawers are open
 *
 * @param distance Distance to push the previous drawer (default: 180dp)
 */
data class DrawerPushConfig(
    val distance: Dp = 180.dp,
)

/**
 * Semantic class names for drawer parts
 *
 * @param header Class name for header section
 * @param body Class name for body section
 * @param footer Class name for footer section
 * @param mask Class name for mask overlay
 * @param wrapper Class name for wrapper container
 * @param content Class name for drawer content panel
 */
data class DrawerClassNames(
    val header: String? = null,
    val body: String? = null,
    val footer: String? = null,
    val mask: String? = null,
    val wrapper: String? = null,
    val content: String? = null,
)

/**
 * Semantic styles (Modifiers) for drawer parts
 *
 * @param header Modifier for header section
 * @param body Modifier for body section
 * @param footer Modifier for footer section
 * @param mask Modifier for mask overlay
 * @param wrapper Modifier for wrapper container
 * @param content Modifier for drawer content panel
 */
data class DrawerStyles(
    val header: Modifier? = null,
    val body: Modifier? = null,
    val footer: Modifier? = null,
    val mask: Modifier? = null,
    val wrapper: Modifier? = null,
    val content: Modifier? = null,
)

/**
 * AntDrawer - A complete drawer/side panel component implementation matching Ant Design specifications
 *
 * A Drawer is a panel that slides in from the edge of the screen. It's commonly used for navigation menus,
 * filters, or displaying additional information without leaving the current context.
 *
 * @param open Whether the drawer is visible
 * @param onClose Callback when drawer should close
 * @param placement Which side the drawer slides in from (Left, Right, Top, Bottom)
 * @param title Title text for the drawer header (mutually exclusive with titleComposable)
 * @param titleComposable Custom composable for the drawer title (mutually exclusive with title)
 * @param closable Whether to show the close button in header
 * @param closeIcon Custom composable for the close icon (set to null to hide)
 * @param mask Whether to show the overlay mask behind drawer
 * @param maskClosable Whether clicking the mask closes the drawer
 * @param maskStyle Modifier for styling the mask overlay
 * @param width Width of drawer when placement is Left or Right (default based on size)
 * @param height Height of drawer when placement is Top or Bottom (default based on size)
 * @param zIndex Z-index for stacking order (default: 1000)
 * @param style Modifier for general drawer styling
 * @param className Additional CSS class name (for compatibility)
 * @param rootClassName Root CSS class name (for compatibility)
 * @param rootStyle Modifier for root container styling
 * @param destroyOnClose Whether to unmount content when drawer is closed (deprecated in v5.25.0, use destroyOnHidden)
 * @param destroyOnHidden Whether to unmount content when drawer is hidden (v5.25.0+)
 * @param forceRender Force render drawer content even when closed
 * @param keyboard Whether ESC key closes the drawer
 * @param push Push behavior when multiple drawers are open (Boolean or DrawerPushConfig)
 * @param footer Custom footer content composable
 * @param footerStyle Modifier for footer styling (deprecated, use styles.footer)
 * @param headerStyle Modifier for header styling (deprecated, use styles.header)
 * @param bodyStyle Modifier for body styling (deprecated, use styles.body)
 * @param drawerStyle Modifier for drawer panel styling (deprecated, use styles.content)
 * @param contentWrapperStyle Modifier for content wrapper styling (deprecated, use styles.wrapper)
 * @param extra Extra content in header (typically displayed on the right side)
 * @param loading Whether to show loading skeleton instead of content (v5.17.0+, v5.18.0 changed to Skeleton)
 * @param afterOpenChange Callback after drawer open/close animation completes
 * @param classNames Semantic class names for different drawer parts
 * @param styles Semantic styles (Modifiers) for different drawer parts
 * @param autoFocus Whether to automatically focus drawer when opened (v4.17.0+)
 * @param size Drawer size preset (Default: 378dp, Large: 736dp) (v4.17.0+)
 * @param getContainer Composable that returns the container for mounting the drawer (null = body)
 * @param panelRef Callback to receive reference to the drawer panel element
 * @param drawerRender Custom render function for drawer content (v5.18.0+)
 * @param content The main content of the drawer
 */
@Composable
fun AntDrawer(
    open: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    placement: DrawerPlacement = DrawerPlacement.Right,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    closable: Boolean = true,
    closeIcon: (@Composable () -> Unit)? = null,
    mask: Boolean = true,
    maskClosable: Boolean = true,
    maskStyle: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    zIndex: Int = 1000,
    style: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    rootStyle: Modifier = Modifier,
    destroyOnClose: Boolean = false,
    destroyOnHidden: Boolean = destroyOnClose, // Default to destroyOnClose for backward compatibility
    forceRender: Boolean = false,
    keyboard: Boolean = true,
    push: Any? = null, // Boolean or DrawerPushConfig
    footer: (@Composable () -> Unit)? = null,
    footerStyle: Modifier = Modifier,
    headerStyle: Modifier = Modifier,
    bodyStyle: Modifier = Modifier,
    drawerStyle: Modifier = Modifier,
    contentWrapperStyle: Modifier = Modifier,
    extra: (@Composable () -> Unit)? = null,
    loading: Boolean = false,
    afterOpenChange: ((open: Boolean) -> Unit)? = null,
    classNames: DrawerClassNames? = null,
    styles: DrawerStyles? = null,
    autoFocus: Boolean = true,
    size: DrawerSize = DrawerSize.Default,
    getContainer: (@Composable () -> Unit)? = null,
    panelRef: ((Any?) -> Unit)? = null,
    drawerRender: ((@Composable () -> Unit) -> (@Composable () -> Unit))? = null,
    content: @Composable () -> Unit,
) {
    val theme = useTheme()

    // Calculate actual dimensions based on size preset or explicit values
    val actualWidth = width ?: when (size) {
        DrawerSize.Default -> 378.dp
        DrawerSize.Large -> 736.dp
    }

    val actualHeight = height ?: when (size) {
        DrawerSize.Default -> 378.dp
        DrawerSize.Large -> 736.dp
    }

    // Track previous open state for afterOpenChange callback
    var previousOpen by remember { mutableStateOf(open) }

    LaunchedEffect(open) {
        if (open != previousOpen) {
            // Wait for animation to complete
            delay(300)
            afterOpenChange?.invoke(open)
            previousOpen = open
        }
    }

    // Focus management
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(open, autoFocus) {
        if (open && autoFocus) {
            delay(100) // Small delay to ensure drawer is rendered
            try {
                focusRequester.requestFocus()
            } catch (e: IllegalStateException) {
                // Focus requester not attached yet, ignore
            }
        }
    }

    // Don't render if not open and destroyOnHidden (or legacy destroyOnClose)
    if (!open && destroyOnHidden && !forceRender) {
        return
    }

    if (open || forceRender) {
        Dialog(
            onDismissRequest = if (maskClosable) onClose else {
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
                    .then(rootStyle)
            ) {
                // Mask overlay
                AnimatedVisibility(
                    visible = mask && open,
                    enter = fadeIn(
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .zIndex(zIndex.toFloat())
                            .then(styles?.mask ?: Modifier)
                            .then(maskStyle)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (maskClosable) {
                                    onClose()
                                }
                            }
                    )
                }

                // Drawer panel with slide animation
                AnimatedVisibility(
                    visible = open,
                    enter = when (placement) {
                        DrawerPlacement.Left -> slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Right -> slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Top -> slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Bottom -> slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    } + fadeIn(
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        initialAlpha = 0.7f
                    ),
                    exit = when (placement) {
                        DrawerPlacement.Left -> slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Right -> slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Top -> slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )

                        DrawerPlacement.Bottom -> slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    } + fadeOut(
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        targetAlpha = 0.7f
                    ),
                    modifier = Modifier
                        .align(
                            when (placement) {
                                DrawerPlacement.Left -> Alignment.CenterStart
                                DrawerPlacement.Right -> Alignment.CenterEnd
                                DrawerPlacement.Top -> Alignment.TopCenter
                                DrawerPlacement.Bottom -> Alignment.BottomCenter
                            }
                        )
                        .zIndex((zIndex + 1).toFloat())
                        .then(styles?.wrapper ?: Modifier)
                        .then(contentWrapperStyle)
                ) {
                    // Default drawer content composable
                    val defaultDrawerContent: @Composable () -> Unit = {
                        Surface(
                            modifier = when (placement) {
                                DrawerPlacement.Left, DrawerPlacement.Right -> Modifier
                                    .fillMaxHeight()
                                    .width(actualWidth)

                                DrawerPlacement.Top, DrawerPlacement.Bottom -> Modifier
                                    .fillMaxWidth()
                                    .height(actualHeight)
                            }
                                .shadow(16.dp)
                                .then(styles?.content ?: Modifier)
                                .then(drawerStyle)
                                .then(style)
                                .then(modifier)
                                .onKeyEvent { event ->
                                    if (keyboard && event.type == KeyEventType.KeyDown && event.key == Key.Escape) {
                                        onClose()
                                        true
                                    } else {
                                        false
                                    }
                                }
                                .then(if (autoFocus) Modifier.focusRequester(focusRequester) else Modifier),
                            color = Color.White,
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            DrawerPanel(
                                title = title,
                                titleComposable = titleComposable,
                                closable = closable,
                                closeIcon = closeIcon,
                                onClose = onClose,
                                extra = extra,
                                footer = footer,
                                loading = loading,
                                headerStyle = styles?.header ?: headerStyle,
                                bodyStyle = styles?.body ?: bodyStyle,
                                footerStyle = styles?.footer ?: footerStyle,
                                classNames = classNames,
                                panelRef = panelRef,
                                content = content
                            )
                        }
                    }

                    // Apply custom drawerRender if provided, otherwise use default
                    if (drawerRender != null) {
                        drawerRender(defaultDrawerContent).invoke()
                    } else {
                        defaultDrawerContent()
                    }
                }
            }
        }
    }
}

/**
 * Internal DrawerPanel component that renders the header, body, and footer structure
 */
@Composable
private fun DrawerPanel(
    title: String?,
    titleComposable: (@Composable () -> Unit)?,
    closable: Boolean,
    closeIcon: (@Composable () -> Unit)?,
    onClose: () -> Unit,
    extra: (@Composable () -> Unit)?,
    footer: (@Composable () -> Unit)?,
    loading: Boolean,
    headerStyle: Modifier,
    bodyStyle: Modifier,
    footerStyle: Modifier,
    classNames: DrawerClassNames?,
    panelRef: ((Any?) -> Unit)?,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                // Notify panelRef when composed
                Modifier.then(
                    remember {
                        Modifier.also {
                            // In Compose, we can't easily get a reference to the layout node
                            // but we can invoke the callback to signal the panel is ready
                            panelRef?.invoke("DrawerPanel")
                        }
                    }
                )
            )
    ) {
        // Header
        if (title != null || titleComposable != null || closable || extra != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(headerStyle)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side: close button + title
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Close button on the left (Ant Design style)
                        if (closable) {
                            IconButton(
                                onClick = onClose,
                                modifier = Modifier.size(24.dp)
                            ) {
                                if (closeIcon != null) {
                                    closeIcon()
                                } else {
                                    CloseOutlinedIcon()
                                }
                            }
                        }

                        // Title
                        if (titleComposable != null) {
                            Box(modifier = Modifier.weight(1f, fill = false)) {
                                titleComposable()
                            }
                        } else if (title != null) {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                color = Color(0xFF000000D9),
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                    }

                    // Right side: extra content
                    if (extra != null) {
                        Box {
                            extra()
                        }
                    }
                }
            }

            // Divider after header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFF0F0F0))
            )
        }

        // Body content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .then(bodyStyle)
        ) {
            if (loading) {
                // Show skeleton loading (v5.18.0+ uses Skeleton instead of Spin)
                DrawerSkeletonLoader(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            } else {
                Box(modifier = Modifier.padding(24.dp)) {
                    content()
                }
            }
        }

        // Footer
        if (footer != null) {
            // Divider before footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFF0F0F0))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(footerStyle)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    footer()
                }
            }
        }
    }
}

/**
 * Skeleton loader for Drawer loading state
 * Matches Ant Design v5.18.0+ Skeleton implementation
 */
@Composable
private fun DrawerSkeletonLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .background(
                    Color(0xFFF0F0F0),
                    RoundedCornerShape(4.dp)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Paragraph skeletons
        repeat(4) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(
                            Color(0xFFF0F0F0),
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(
                            Color(0xFFF0F0F0),
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .background(
                            Color(0xFFF0F0F0),
                            RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Drawer with multiple drawers support using push behavior
 *
 * When multiple drawers are open simultaneously, this component can push
 * previous drawers to make room for new ones.
 *
 * @param open Whether this drawer is visible
 * @param onClose Callback when drawer should close
 * @param pushConfig Configuration for push behavior (distance, etc.)
 * @param isPushed Whether this drawer is currently pushed by another drawer
 * @param other parameters Same as AntDrawer
 */
@Composable
fun AntDrawerWithPush(
    open: Boolean,
    onClose: () -> Unit,
    pushConfig: DrawerPushConfig = DrawerPushConfig(),
    isPushed: Boolean = false,
    modifier: Modifier = Modifier,
    placement: DrawerPlacement = DrawerPlacement.Right,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    closable: Boolean = true,
    closeIcon: (@Composable () -> Unit)? = null,
    mask: Boolean = true,
    maskClosable: Boolean = true,
    maskStyle: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    zIndex: Int = 1000,
    style: Modifier = Modifier,
    className: String? = null,
    rootClassName: String? = null,
    rootStyle: Modifier = Modifier,
    destroyOnClose: Boolean = false,
    destroyOnHidden: Boolean = destroyOnClose,
    forceRender: Boolean = false,
    keyboard: Boolean = true,
    footer: (@Composable () -> Unit)? = null,
    footerStyle: Modifier = Modifier,
    headerStyle: Modifier = Modifier,
    bodyStyle: Modifier = Modifier,
    drawerStyle: Modifier = Modifier,
    contentWrapperStyle: Modifier = Modifier,
    extra: (@Composable () -> Unit)? = null,
    loading: Boolean = false,
    afterOpenChange: ((open: Boolean) -> Unit)? = null,
    classNames: DrawerClassNames? = null,
    styles: DrawerStyles? = null,
    autoFocus: Boolean = true,
    size: DrawerSize = DrawerSize.Default,
    getContainer: (@Composable () -> Unit)? = null,
    panelRef: ((Any?) -> Unit)? = null,
    drawerRender: ((@Composable () -> Unit) -> (@Composable () -> Unit))? = null,
    content: @Composable () -> Unit,
) {
    // Animated offset for push behavior
    val pushOffset by animateDpAsState(
        targetValue = if (isPushed) pushConfig.distance else 0.dp,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "drawer_push_offset"
    )

    // Apply push offset based on placement
    val pushModifier = when (placement) {
        DrawerPlacement.Left -> Modifier.offset(x = -pushOffset)
        DrawerPlacement.Right -> Modifier.offset(x = pushOffset)
        DrawerPlacement.Top -> Modifier.offset(y = -pushOffset)
        DrawerPlacement.Bottom -> Modifier.offset(y = pushOffset)
    }

    AntDrawer(
        open = open,
        onClose = onClose,
        modifier = modifier.then(pushModifier),
        placement = placement,
        title = title,
        titleComposable = titleComposable,
        closable = closable,
        closeIcon = closeIcon,
        mask = mask,
        maskClosable = maskClosable,
        maskStyle = maskStyle,
        width = width,
        height = height,
        zIndex = zIndex,
        style = style,
        className = className,
        rootClassName = rootClassName,
        rootStyle = rootStyle,
        destroyOnClose = destroyOnClose,
        destroyOnHidden = destroyOnHidden,
        forceRender = forceRender,
        keyboard = keyboard,
        push = null, // Don't propagate push config
        footer = footer,
        footerStyle = footerStyle,
        headerStyle = headerStyle,
        bodyStyle = bodyStyle,
        drawerStyle = drawerStyle,
        contentWrapperStyle = contentWrapperStyle,
        extra = extra,
        loading = loading,
        afterOpenChange = afterOpenChange,
        classNames = classNames,
        styles = styles,
        autoFocus = autoFocus,
        size = size,
        getContainer = getContainer,
        panelRef = panelRef,
        drawerRender = drawerRender,
        content = content
    )
}

// ==================== EXAMPLE USAGE ====================

/**
 * Example: Basic drawer usage
 */
@Composable
fun DrawerBasicExample() {
    var open by remember { mutableStateOf(false) }

    AntButton(onClick = { open = true }) {
        Text("Open Drawer")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Basic Drawer",
        placement = DrawerPlacement.Right
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Some contents...")
            Text("Some contents...")
            Text("Some contents...")
        }
    }
}

/**
 * Example: Drawer with footer
 */
@Composable
fun DrawerWithFooterExample() {
    var open by remember { mutableStateOf(false) }

    AntButton(onClick = { open = true }) {
        Text("Open Drawer with Footer")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Drawer with Footer",
        placement = DrawerPlacement.Right,
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(
                    onClick = { open = false },
                    type = ButtonType.Default
                ) {
                    Text("Cancel")
                }
                AntButton(
                    onClick = {
                        // Handle submit
                        open = false
                    },
                    type = ButtonType.Primary
                ) {
                    Text("Submit")
                }
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Form content goes here...")
        }
    }
}

/**
 * Example: Drawer with different placements
 */
@Composable
fun DrawerPlacementExample() {
    var openLeft by remember { mutableStateOf(false) }
    var openRight by remember { mutableStateOf(false) }
    var openTop by remember { mutableStateOf(false) }
    var openBottom by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AntButton(onClick = { openLeft = true }) {
            Text("Left")
        }
        AntButton(onClick = { openRight = true }) {
            Text("Right")
        }
        AntButton(onClick = { openTop = true }) {
            Text("Top")
        }
        AntButton(onClick = { openBottom = true }) {
            Text("Bottom")
        }
    }

    AntDrawer(
        open = openLeft,
        onClose = { openLeft = false },
        title = "Left Drawer",
        placement = DrawerPlacement.Left
    ) {
        Text("Content from left")
    }

    AntDrawer(
        open = openRight,
        onClose = { openRight = false },
        title = "Right Drawer",
        placement = DrawerPlacement.Right
    ) {
        Text("Content from right")
    }

    AntDrawer(
        open = openTop,
        onClose = { openTop = false },
        title = "Top Drawer",
        placement = DrawerPlacement.Top
    ) {
        Text("Content from top")
    }

    AntDrawer(
        open = openBottom,
        onClose = { openBottom = false },
        title = "Bottom Drawer",
        placement = DrawerPlacement.Bottom
    ) {
        Text("Content from bottom")
    }
}

/**
 * Example: Drawer with custom size
 */
@Composable
fun DrawerSizeExample() {
    var openDefault by remember { mutableStateOf(false) }
    var openLarge by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AntButton(onClick = { openDefault = true }) {
            Text("Default Size")
        }
        AntButton(onClick = { openLarge = true }) {
            Text("Large Size")
        }
    }

    AntDrawer(
        open = openDefault,
        onClose = { openDefault = false },
        title = "Default Drawer",
        size = DrawerSize.Default
    ) {
        Text("Default size drawer (378dp)")
    }

    AntDrawer(
        open = openLarge,
        onClose = { openLarge = false },
        title = "Large Drawer",
        size = DrawerSize.Large
    ) {
        Text("Large size drawer (736dp)")
    }
}

/**
 * Example: Drawer with loading state
 */
@Composable
fun DrawerLoadingExample() {
    var open by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(open) {
        if (open) {
            loading = true
            delay(2000) // Simulate loading
            loading = false
        }
    }

    AntButton(onClick = { open = true }) {
        Text("Open Drawer with Loading")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Loading Drawer",
        loading = loading
    ) {
        Text("This content appears after loading completes")
    }
}

/**
 * Example: Drawer with extra content in header
 */
@Composable
fun DrawerExtraExample() {
    var open by remember { mutableStateOf(false) }

    AntButton(onClick = { open = true }) {
        Text("Open Drawer with Extra")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Drawer with Extra",
        extra = {
            AntButton(
                type = ButtonType.Link,
                onClick = { /* Handle extra action */ }
            ) {
                Text("Extra Action")
            }
        }
    ) {
        Text("Drawer content with extra header action")
    }
}

/**
 * Example: Multi-level drawer with push behavior
 */
@Composable
fun DrawerMultiLevelExample() {
    var firstOpen by remember { mutableStateOf(false) }
    var secondOpen by remember { mutableStateOf(false) }

    AntButton(onClick = { firstOpen = true }) {
        Text("Open First Drawer")
    }

    AntDrawerWithPush(
        open = firstOpen,
        onClose = { firstOpen = false },
        title = "First Level",
        isPushed = secondOpen,
        pushConfig = DrawerPushConfig(distance = 180.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("First level drawer")
            AntButton(onClick = { secondOpen = true }) {
                Text("Open Second Drawer")
            }
        }
    }

    AntDrawer(
        open = secondOpen,
        onClose = { secondOpen = false },
        title = "Second Level",
        zIndex = 1001
    ) {
        Text("Second level drawer")
    }
}

/**
 * Example: Drawer with custom render (drawerRender)
 */
@Composable
fun DrawerCustomRenderExample() {
    var open by remember { mutableStateOf(false) }

    AntButton(onClick = { open = true }) {
        Text("Open Custom Rendered Drawer")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Custom Rendered Drawer",
        drawerRender = { drawerContent ->
            // Wrap the drawer content with custom elements
            {
                Box {
                    drawerContent()
                    // Add custom overlay or decorations
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            "Custom Render",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    ) {
        Text("This drawer has custom rendering applied")
    }
}

/**
 * Example: Drawer with panelRef callback
 */
@Composable
fun DrawerPanelRefExample() {
    var open by remember { mutableStateOf(false) }
    var panelInfo by remember { mutableStateOf("Panel not loaded") }

    AntButton(onClick = { open = true }) {
        Text("Open Drawer with PanelRef")
    }

    AntDrawer(
        open = open,
        onClose = { open = false },
        title = "Drawer with PanelRef",
        panelRef = { ref ->
            panelInfo = "Panel reference: $ref"
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Panel info: $panelInfo")
            Text("This drawer uses panelRef callback")
        }
    }
}
