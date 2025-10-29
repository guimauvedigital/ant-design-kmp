package com.antdesign.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * App component provides global context and configuration for all Ant Design components.
 * It wraps the entire application and provides access to message, notification, and modal APIs
 * through hooks without manually managing contextHolder nodes.
 *
 * This is the Compose equivalent of Ant Design React v5's App component.
 *
 * Features:
 * - Provides message, notification, and modal instances via useApp() hook
 * - Automatically manages contextHolder rendering
 * - Supports context isolation for multiple App instances
 * - Works with ConfigProvider for theme and locale support
 *
 * Example usage:
 * ```kotlin
 * @Composable
 * fun MyApp() {
 *     AntApp(
 *         message = MessageGlobalConfig(duration = 2.0),
 *         notification = NotificationGlobalConfig(placement = NotificationPlacement.TopRight)
 *     ) {
 *         // Your app content
 *         MyComponent()
 *     }
 * }
 *
 * @Composable
 * fun MyComponent() {
 *     val app = useApp()
 *
 *     AntButton(onClick = {
 *         app.message.success("Operation successful!")
 *     }) {
 *         Text("Click me")
 *     }
 * }
 * ```
 *
 * @since 1.0.0
 * @see useApp
 * @see MessageHookApi
 * @see NotificationHookApi
 * @see UseModalHookReturn
 */

/**
 * Configuration for the App component
 * Contains API instances for message, notification, and modal
 */
data class AppContext(
    val message: MessageHookApi,
    val notification: NotificationHookApi,
    val modal: UseModalHookReturn
)

/**
 * CompositionLocal for providing App context throughout the component tree
 * This allows child components to access message, notification, and modal APIs
 * without prop drilling.
 */
val LocalAppContext = compositionLocalOf<AppContext?> { null }

/**
 * AntApp - Application wrapper component
 *
 * Provides global context and configuration for message, notification, and modal components.
 * This component must wrap your application to use the useApp() hook.
 *
 * Features matching React Ant Design v5:
 * - Provides message API with context support
 * - Provides notification API with context support
 * - Provides modal API with context support
 * - Automatic contextHolder management (no manual rendering required)
 * - Context isolation for multiple App instances
 * - Theme and locale integration via ConfigProvider
 *
 * Props:
 * @param modifier Modifier for styling the wrapper
 * @param message Optional MessageGlobalConfig for message defaults
 * @param notification Optional NotificationGlobalConfig for notification defaults
 * @param children Content to render inside the App
 *
 * Example:
 * ```kotlin
 * AntApp(
 *     message = MessageGlobalConfig(
 *         duration = 2.0,
 *         maxCount = 3,
 *         top = 24.dp
 *     ),
 *     notification = NotificationGlobalConfig(
 *         placement = NotificationPlacement.TopRight,
 *         duration = 4.5
 *     )
 * ) {
 *     MyApplicationContent()
 * }
 * ```
 */
@Composable
fun AntApp(
    modifier: Modifier = Modifier,
    message: MessageGlobalConfig? = null,
    notification: NotificationGlobalConfig? = null,
    children: @Composable () -> Unit
) {
    // Initialize hooks for message, notification, and modal
    // Each hook returns an API and a contextHolder composable
    val (messageApi, messageContextHolder) = useMessage()
    val (notificationApi, notificationContextHolder) = useNotification()
    val modalHook = useModal()

    // Apply global configuration if provided
    message?.let { messageApi.config(it) }
    notification?.let { notificationApi.config(it) }

    // Create the app context with all APIs
    val appContext = remember(messageApi, notificationApi, modalHook) {
        AppContext(
            message = messageApi,
            notification = notificationApi,
            modal = modalHook
        )
    }

    // Provide the context to children
    CompositionLocalProvider(LocalAppContext provides appContext) {
        Box(modifier = modifier) {
            // Render children content
            children()

            // Render contextHolders for message, notification, and modal
            // These are positioned absolutely and don't affect layout
            messageContextHolder()
            notificationContextHolder()
            modalHook.contextHolder()
        }
    }
}

/**
 * useApp - Hook to access App context
 *
 * Returns the AppContext containing message, notification, and modal APIs.
 * This hook must be used within an AntApp component.
 *
 * This is the Compose equivalent of React's `App.useApp()` hook.
 *
 * Benefits:
 * - No need to manually render contextHolder
 * - Access to context (theme, locale from ConfigProvider)
 * - Better scoping than global static methods
 * - Easier to test
 *
 * Return value:
 * - message: MessageHookApi - Methods to show messages (success, error, info, warning, loading)
 * - notification: NotificationHookApi - Methods to show notifications
 * - modal: UseModalHookReturn - Methods to show modals (confirm, info, success, error, warning)
 *
 * Example:
 * ```kotlin
 * @Composable
 * fun MyComponent() {
 *     val app = useApp()
 *
 *     AntButton(onClick = {
 *         // Show success message
 *         app.message.success("Operation completed!")
 *
 *         // Show notification
 *         app.notification.info(
 *             message = "Info",
 *             description = "This is an informational notification"
 *         )
 *
 *         // Show confirm modal
 *         app.modal.confirm(
 *             ModalConfirmConfig(
 *                 title = "Confirm",
 *                 content = { Text("Are you sure?") },
 *                 onOk = { println("OK") },
 *                 onCancel = { println("Cancel") }
 *             )
 *         )
 *     }) {
 *         Text("Trigger Actions")
 *     }
 * }
 * ```
 *
 * @throws IllegalStateException if called outside of AntApp context
 * @return AppContext with message, notification, and modal APIs
 */
@Composable
fun useApp(): AppContext {
    val context = LocalAppContext.current
    checkNotNull(context) {
        "useApp must be used within AntApp component. " +
                "Please wrap your component tree with AntApp:\n\n" +
                "AntApp {\n" +
                "    YourContent()\n" +
                "}"
    }
    return context
}

/**
 * AntApp - Wrapper component (deprecated name, use AntApp instead)
 *
 * This is an alias for backward compatibility.
 * Please use AntApp instead.
 *
 * @deprecated Use AntApp instead
 */
@Deprecated(
    message = "Use AntApp instead for consistency with Ant Design naming",
    replaceWith = ReplaceWith("AntApp(modifier, message, notification, children)")
)
@Composable
fun App(
    modifier: Modifier = Modifier,
    message: MessageGlobalConfig? = null,
    notification: NotificationGlobalConfig? = null,
    children: @Composable () -> Unit
) {
    AntApp(modifier, message, notification, children)
}

/**
 * Global App container that renders all globally triggered components
 *
 * This container should be placed at the root of your application if you want to use
 * global static methods like AntMessage.success() or AntNotification.info().
 *
 * If you use the AntApp component with useApp() hook, you don't need this container.
 *
 * Example:
 * ```kotlin
 * @Composable
 * fun App() {
 *     Box {
 *         // Your app content
 *         MyContent()
 *
 *         // Global container for static methods
 *         AntAppContainer()
 *     }
 * }
 * ```
 */
@Composable
fun AntAppContainer() {
    // Render global containers for static methods
    AntMessageContainer()
    AntNotificationContainer()
    AntModal.RenderGlobalModals()
}

/**
 * Helper function to create an AppContext for testing or custom scenarios
 *
 * @param message MessageHookApi instance
 * @param notification NotificationHookApi instance
 * @param modal UseModalHookReturn instance
 * @return AppContext with the provided APIs
 */
fun createAppContext(
    message: MessageHookApi,
    notification: NotificationHookApi,
    modal: UseModalHookReturn
): AppContext {
    return AppContext(
        message = message,
        notification = notification,
        modal = modal
    )
}

/**
 * Scope for App DSL builders
 * Provides convenient access to message, notification, and modal APIs
 */
class AppScope(private val context: AppContext) {
    val message: MessageHookApi get() = context.message
    val notification: NotificationHookApi get() = context.notification
    val modal: UseModalHookReturn get() = context.modal

    /**
     * Show a success message
     */
    fun showSuccess(content: String, duration: Double = 3.0) {
        message.success(content, duration)
    }

    /**
     * Show an error message
     */
    fun showError(content: String, duration: Double = 3.0) {
        message.error(content, duration)
    }

    /**
     * Show an info message
     */
    fun showInfo(content: String, duration: Double = 3.0) {
        message.info(content, duration)
    }

    /**
     * Show a warning message
     */
    fun showWarning(content: String, duration: Double = 3.0) {
        message.warning(content, duration)
    }

    /**
     * Show a loading message
     */
    fun showLoading(content: String, duration: Double = 0.0): MessageInstance {
        return message.loading(content, duration)
    }

    /**
     * Show a confirm modal
     */
    fun confirm(
        title: String,
        content: (@Composable () -> Unit)? = null,
        onOk: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    ): ModalInstance {
        return modal.confirm(
            ModalConfirmConfig(
                title = title,
                content = content,
                onOk = onOk,
                onCancel = onCancel
            )
        )
    }
}

/**
 * Extension function to use AppScope DSL
 *
 * Example:
 * ```kotlin
 * @Composable
 * fun MyComponent() {
 *     val app = useApp()
 *
 *     AntButton(onClick = {
 *         app.withScope {
 *             showSuccess("Operation completed!")
 *             confirm(
 *                 title = "Confirm",
 *                 content = { Text("Are you sure?") },
 *                 onOk = { showInfo("Confirmed!") }
 *             )
 *         }
 *     }) {
 *         Text("Click me")
 *     }
 * }
 * ```
 */
inline fun AppContext.withScope(block: AppScope.() -> Unit) {
    AppScope(this).block()
}
