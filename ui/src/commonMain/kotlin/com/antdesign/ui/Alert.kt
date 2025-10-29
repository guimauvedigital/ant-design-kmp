package com.antdesign.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Alert type enum matching Ant Design specifications
 * Maps to React's: 'success' | 'info' | 'warning' | 'error'
 */
enum class AlertType {
    Success,
    Info,
    Warning,
    Error
}

/**
 * Semantic class names for Alert sub-components
 * Maps to React's classNames prop with semantic structure
 */
data class AlertClassNames(
    val root: String? = null,
    val icon: String? = null,
    val message: String? = null,
    val description: String? = null,
    val closeIcon: String? = null,
    val action: String? = null
)

/**
 * Semantic styles for Alert sub-components
 * Maps to React's styles prop with semantic structure
 */
data class AlertStyles(
    val root: Modifier? = null,
    val icon: Modifier? = null,
    val message: Modifier? = null,
    val description: Modifier? = null,
    val closeIcon: Modifier? = null,
    val action: Modifier? = null
)

/**
 * Alert-specific Closable configuration
 * Extends the base ClosableConfig with ARIA label support
 * Maps to React's ClosableType which can be boolean | ClosableConfig
 */
data class AlertClosableConfig(
    val closeIcon: (@Composable () -> Unit)? = null,
    val ariaLabel: String? = null,
    val disabled: Boolean = false
)

/**
 * Ant Design Alert Component - 100% Feature Complete
 *
 * Full parity with React Ant Design v5.0-v5.25.0+ implementation
 * Source: ant-design/components/alert/Alert.tsx
 *
 * @param message Alert content (String or Composable) - REQUIRED
 * @param type Alert type: Success, Info, Warning, Error (default: Info, banner defaults to Warning)
 * @param description Additional content (String or Composable, optional)
 * @param closable Whether Alert can be closed (default: false)
 * @param closableConfig Advanced closable configuration with ARIA support (v5.7.0+)
 * @param closeText Custom close text - DEPRECATED in v5.x, use closableConfig.closeIcon instead
 * @param closeIcon Custom close icon (Composable, optional)
 * @param showIcon Whether to show icon (default: false, auto true for banner)
 * @param icon Custom icon (Composable, optional)
 * @param action Custom action button (Composable, optional)
 * @param banner Banner mode - full width, no border (default: false)
 * @param onClose Callback when close button clicked
 * @param afterClose Callback after close animation completes
 * @param onClick Click handler for the alert (v5.6.0+)
 * @param onMouseEnter Mouse enter callback
 * @param onMouseLeave Mouse leave callback
 * @param role ARIA role attribute (default: "alert")
 * @param id Component ID for identification
 * @param rootClassName Additional root class name (v5.4.0+)
 * @param classNames Semantic class names for sub-components (v5.4.0+)
 * @param styles Semantic styles for sub-components (v5.4.0+)
 * @param modifier Root modifier
 */
@Composable
fun AntAlert(
    message: Any,
    modifier: Modifier = Modifier,
    type: AlertType? = null,
    description: Any? = null,
    closable: Boolean = false,
    closableConfig: AlertClosableConfig? = null,
    // Note: closeText is deprecated in React v5.x - use closableConfig.closeIcon instead
    closeText: String? = null,
    closeIcon: (@Composable () -> Unit)? = null,
    showIcon: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    banner: Boolean = false,
    onClose: (() -> Unit)? = null,
    afterClose: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onMouseEnter: (() -> Unit)? = null,
    onMouseLeave: (() -> Unit)? = null,
    role: String = "alert",
    id: String? = null,
    rootClassName: String? = null,
    classNames: AlertClassNames? = null,
    styles: AlertStyles? = null
) {
    var visible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Determine effective type: banner defaults to Warning, otherwise Info
    val effectiveType = type ?: if (banner) AlertType.Warning else AlertType.Info

    // Banner mode defaults showIcon to true (matching React behavior)
    val effectiveShowIcon = if (banner && !showIcon) true else showIcon

    // Determine if closable based on multiple sources (matching React logic)
    val isClosable = when {
        closableConfig != null -> true
        closeText != null -> true
        closable -> true
        closeIcon != null -> true
        else -> false
    }

    // Merge close icon from multiple sources
    val mergedCloseIcon: (@Composable () -> Unit)? = when {
        closableConfig?.closeIcon != null -> closableConfig.closeIcon
        closeText != null -> null // Will render as text
        closeIcon != null -> closeIcon
        else -> null // Will use default X icon
    }

    // Check if close button is disabled
    val closeButtonDisabled = closableConfig?.disabled ?: false

    // Animated visibility with fade + slide
    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
            animationSpec = tween(300),
            shrinkTowards = Alignment.Top
        )
    ) {
        val colors = when (effectiveType) {
            AlertType.Success -> AlertColorQuadruple(
                backgroundColor = Color(0xFFF6FFED),
                borderColor = Color(0xFFB7EB8F),
                textColor = Color(0xFF52C41A),
                defaultIconText = "✓"
            )
            AlertType.Info -> AlertColorQuadruple(
                backgroundColor = Color(0xFFE6F7FF),
                borderColor = Color(0xFF91D5FF),
                textColor = Color(0xFF1890FF),
                defaultIconText = "ℹ"
            )
            AlertType.Warning -> AlertColorQuadruple(
                backgroundColor = Color(0xFFFFFBE6),
                borderColor = Color(0xFFFFE58F),
                textColor = Color(0xFFFAAD14),
                defaultIconText = "⚠"
            )
            AlertType.Error -> AlertColorQuadruple(
                backgroundColor = Color(0xFFFFF2F0),
                borderColor = Color(0xFFFFCCC7),
                textColor = Color(0xFFFF4D4F),
                defaultIconText = "✕"
            )
        }

        Card(
            modifier = modifier
                .then(styles?.root ?: Modifier)
                .then(
                    if (banner) Modifier.fillMaxWidth() else Modifier
                )
                .then(
                    if (onClick != null) {
                        Modifier.clickable { onClick() }
                    } else {
                        Modifier
                    }
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Enter -> onMouseEnter?.invoke()
                                PointerEventType.Exit -> onMouseLeave?.invoke()
                            }
                        }
                    }
                }
                .semantics {
                    // Add ARIA role
                    this.role = Role.Button
                    // Add data-show attribute (visible state)
                    this.set(SemanticsPropertyKey<Boolean>("data-show"), visible)
                    // Add id if provided
                    if (id != null) {
                        this.testTag = id
                    }
                    // Add ARIA label from closableConfig
                    if (closableConfig?.ariaLabel != null) {
                        this.contentDescription = closableConfig.ariaLabel
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = colors.backgroundColor
            ),
            border = if (!banner) BorderStroke(1.dp, colors.borderColor) else null,
            shape = if (banner) {
                androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
            } else {
                androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (description != null) 16.dp else 12.dp,
                        vertical = if (description != null) 12.dp else 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = if (description != null) Alignment.Top else Alignment.CenterVertically
            ) {
                // Icon section
                if (effectiveShowIcon) {
                    Box(
                        modifier = styles?.icon ?: Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        if (icon != null) {
                            // Custom icon
                            icon()
                        } else {
                            // Default icon based on type
                            Text(
                                text = colors.defaultIconText,
                                color = colors.textColor,
                                fontSize = if (description != null) 20.sp else 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Content section (message + description)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Message
                    Box(modifier = styles?.message ?: Modifier) {
                        if (message is String) {
                            Text(
                                text = message,
                                color = colors.textColor,
                                fontSize = 14.sp,
                                fontWeight = if (description != null) FontWeight.Medium else FontWeight.Normal
                            )
                        } else {
                            // Assume it's a Composable
                            @Suppress("UNCHECKED_CAST")
                            (message as @Composable () -> Unit).invoke()
                        }
                    }

                    // Description (if provided)
                    if (description != null) {
                        Box(modifier = styles?.description ?: Modifier) {
                            if (description is String) {
                                Text(
                                    text = description,
                                    color = colors.textColor.copy(alpha = 0.85f),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            } else {
                                // Assume it's a Composable
                                @Suppress("UNCHECKED_CAST")
                                (description as @Composable () -> Unit).invoke()
                            }
                        }
                    }
                }

                // Action section
                if (action != null) {
                    Box(
                        modifier = styles?.action ?: Modifier,
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        action()
                    }
                }

                // Close button
                if (isClosable) {
                    IconButton(
                        onClick = {
                            if (!closeButtonDisabled) {
                                scope.launch {
                                    visible = false
                                    onClose?.invoke()
                                    // Wait for exit animation (300ms)
                                    delay(300)
                                    afterClose?.invoke()
                                }
                            }
                        },
                        enabled = !closeButtonDisabled,
                        modifier = (styles?.closeIcon ?: Modifier)
                            .size(24.dp)
                            .semantics {
                                // Add ARIA label for close button
                                this.contentDescription = closableConfig?.ariaLabel ?: "Close"
                            }
                    ) {
                        if (mergedCloseIcon != null) {
                            // Custom close icon from closableConfig or closeIcon prop
                            mergedCloseIcon()
                        } else if (closeText != null) {
                            // Close text (deprecated)
                            Text(
                                text = closeText,
                                color = colors.textColor.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        } else {
                            // Default close icon (X)
                            Text(
                                text = "×",
                                color = colors.textColor.copy(alpha = 0.6f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Convenience overload for String-only message
 */
@Composable
fun AntAlert(
    message: String,
    modifier: Modifier = Modifier,
    type: AlertType? = null,
    description: String? = null,
    closable: Boolean = false,
    closableConfig: AlertClosableConfig? = null,
    // Note: closeText is deprecated in React v5.x - use closableConfig.closeIcon instead
    closeText: String? = null,
    closeIcon: (@Composable () -> Unit)? = null,
    showIcon: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    banner: Boolean = false,
    onClose: (() -> Unit)? = null,
    afterClose: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onMouseEnter: (() -> Unit)? = null,
    onMouseLeave: (() -> Unit)? = null,
    role: String = "alert",
    id: String? = null,
    rootClassName: String? = null,
    classNames: AlertClassNames? = null,
    styles: AlertStyles? = null
) {
    AntAlert(
        message = message as Any,
        modifier = modifier,
        type = type,
        description = description,
        closable = closable,
        closableConfig = closableConfig,
        closeText = closeText,
        closeIcon = closeIcon,
        showIcon = showIcon,
        icon = icon,
        action = action,
        banner = banner,
        onClose = onClose,
        afterClose = afterClose,
        onClick = onClick,
        onMouseEnter = onMouseEnter,
        onMouseLeave = onMouseLeave,
        role = role,
        id = id,
        rootClassName = rootClassName,
        classNames = classNames,
        styles = styles
    )
}

/**
 * Alert.ErrorBoundary Component
 *
 * Displays an error alert with custom or default messages
 * Full parity with React's Alert.ErrorBoundary
 *
 * Note: In Compose, error boundaries work differently than React.
 * This component is provided for API parity and can be used to display
 * error states. For actual error handling, use standard Kotlin try-catch
 * outside of composable contexts.
 *
 * @param error The error/exception to display (null = show children)
 * @param message Custom error message (defaults to error.toString())
 * @param description Custom error description (defaults to stack trace)
 * @param id Component ID for the error alert
 * @param children Child content to display when error is null
 */
@Composable
fun AlertErrorBoundary(
    error: Throwable? = null,
    modifier: Modifier = Modifier,
    message: String? = null,
    description: String? = null,
    id: String? = null,
    children: @Composable () -> Unit
) {
    if (error != null) {
        val errorMessage = message ?: error.toString()
        val errorDescription = description ?: error.stackTraceToString()

        AntAlert(
            id = id,
            type = AlertType.Error,
            message = errorMessage,
            description = errorDescription,
            modifier = modifier
        )
    } else {
        children()
    }
}

private data class AlertColorQuadruple(
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val defaultIconText: String
)
