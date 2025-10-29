package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * Notification types matching Ant Design React Notification component
 */
enum class NotificationType {
    Success,
    Error,
    Info,
    Warning,
    Open
}

/**
 * Notification placement positions
 */
enum class NotificationPlacement {
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight,
    Top,
    Bottom
}

/**
 * Content type for message and description - supports both String and Composable
 */
sealed class NotificationContent {
    data class Text(val text: String) : NotificationContent()
    class Composable(val content: @androidx.compose.runtime.Composable () -> Unit) : NotificationContent()
}

/**
 * Complete NotificationConfig with all Ant Design v5 properties
 * Matches React NotificationArgsProps interface
 */
data class NotificationConfig(
    val message: NotificationContent,
    val description: NotificationContent? = null,
    val type: NotificationType = NotificationType.Open,
    val duration: Double = 4.5, // in seconds, 0 = no auto-close
    val placement: NotificationPlacement = NotificationPlacement.TopRight,
    val btn: (@Composable () -> Unit)? = null,
    val key: String? = null,
    val className: String? = null,
    val style: Map<String, Any>? = null,
    val closeIcon: (@Composable () -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
    val onClose: (() -> Unit)? = null,
    val role: String = "alert", // "alert" or "status"
)

/**
 * Global configuration for Notification component
 * Matches React NotificationConfig interface
 */
data class NotificationGlobalConfig(
    val bottom: Dp = 24.dp,
    val top: Dp = 24.dp,
    val duration: Double = 4.5,
    val placement: NotificationPlacement = NotificationPlacement.TopRight,
    val closeIcon: (@Composable () -> Unit)? = null,
    val rtl: Boolean = false,
    val maxCount: Int = 0, // 0 = unlimited
    val prefixCls: String = "ant-notification",
    val getContainer: (() -> Any)? = null,
)

/**
 * Notification instance with promise-like API
 * Matches React NotificationInstance return type
 */
interface NotificationInstance {
    val key: String

    /**
     * Promise-like then method for chaining
     */
    fun then(onFulfilled: (() -> Unit)? = null): NotificationInstance

    /**
     * Close this notification instance
     */
    fun close()
}

/**
 * Internal implementation of NotificationInstance
 */
private class NotificationInstanceImpl(
    override val key: String,
    private val onCloseCallback: () -> Unit,
) : NotificationInstance {

    private val fulfillCallbacks = mutableListOf<() -> Unit>()
    private var isClosed = false

    override fun then(onFulfilled: (() -> Unit)?): NotificationInstance {
        if (onFulfilled != null) {
            if (isClosed) {
                onFulfilled()
            } else {
                fulfillCallbacks.add(onFulfilled)
            }
        }
        return this
    }

    override fun close() {
        if (!isClosed) {
            isClosed = true
            onCloseCallback()
            fulfillCallbacks.forEach { it() }
            fulfillCallbacks.clear()
        }
    }
}

/**
 * Internal notification item with state
 */
internal data class NotificationItem(
    val key: String,
    val config: NotificationConfig,
    var visible: Boolean = true,
    var job: Job? = null,
)

/**
 * Global notification state manager
 */
private object NotificationState {
    val notifications = mutableStateMapOf<NotificationPlacement, MutableList<NotificationItem>>()
    var globalConfig = NotificationGlobalConfig()
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        // Initialize empty lists for each placement
        NotificationPlacement.entries.forEach { placement ->
            notifications[placement] = mutableListOf()
        }
    }

    fun addNotification(config: NotificationConfig): NotificationInstance {
        val key = config.key ?: "notification_${Random.nextLong()}_${Random.nextInt()}"
        val placement = config.placement

        val list = notifications[placement] ?: mutableListOf<NotificationItem>().also {
            notifications[placement] = it
        }

        // Check maxCount and remove oldest if needed
        if (globalConfig.maxCount > 0 && list.size >= globalConfig.maxCount) {
            val oldest = list.firstOrNull()
            oldest?.let { removeNotification(it.key, placement) }
        }

        val item = NotificationItem(key = key, config = config.copy(key = key))
        list.add(item)

        // Auto-close timer
        val duration = if (config.duration >= 0) config.duration else globalConfig.duration
        if (duration > 0) {
            item.job = scope.launch {
                delay((duration * 1000).toLong())
                removeNotification(key, placement)
            }
        }

        return NotificationInstanceImpl(key) {
            removeNotification(key, placement)
        }
    }

    fun removeNotification(key: String, placement: NotificationPlacement? = null) {
        if (placement != null) {
            val list = notifications[placement]
            val index = list?.indexOfFirst { it.key == key } ?: -1
            if (index != -1 && list != null) {
                val item = list[index]
                item.job?.cancel()
                item.config.onClose?.invoke()
                list.removeAt(index)
            }
        } else {
            // Search all placements
            notifications.values.forEach { list ->
                val index = list.indexOfFirst { it.key == key }
                if (index != -1) {
                    val item = list[index]
                    item.job?.cancel()
                    item.config.onClose?.invoke()
                    list.removeAt(index)
                    return
                }
            }
        }
    }

    fun updateConfig(options: NotificationGlobalConfig) {
        globalConfig = options
    }

    fun destroyAll() {
        notifications.values.forEach { list ->
            list.forEach { it.job?.cancel() }
            list.clear()
        }
    }
}

/**
 * Global Notification API matching Ant Design React v5
 * Usage: AntNotification.success(config), AntNotification.error(config), etc.
 */
object AntNotification {

    /**
     * Show a success notification
     * @param message Message content to display (String or Composable)
     * @param description Optional description content
     * @param duration Duration in seconds (0 = no auto-close)
     * @param placement Position of the notification
     * @param btn Optional button composable
     * @param onClose Callback when notification closes
     * @return NotificationInstance with then() and close() methods
     */
    fun success(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance {
        return open(
            NotificationConfig(
                message = NotificationContent.Text(message),
                description = description?.let { NotificationContent.Text(it) },
                type = NotificationType.Success,
                duration = duration,
                placement = placement,
                btn = btn,
                onClose = onClose
            )
        )
    }

    /**
     * Show a success notification with full config
     */
    fun success(config: NotificationConfig): NotificationInstance {
        return open(config.copy(type = NotificationType.Success))
    }

    /**
     * Show an error notification
     * @param message Message content to display (String or Composable)
     * @param description Optional description content
     * @param duration Duration in seconds (0 = no auto-close)
     * @param placement Position of the notification
     * @param btn Optional button composable
     * @param onClose Callback when notification closes
     * @return NotificationInstance with then() and close() methods
     */
    fun error(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance {
        return open(
            NotificationConfig(
                message = NotificationContent.Text(message),
                description = description?.let { NotificationContent.Text(it) },
                type = NotificationType.Error,
                duration = duration,
                placement = placement,
                btn = btn,
                onClose = onClose
            )
        )
    }

    /**
     * Show an error notification with full config
     */
    fun error(config: NotificationConfig): NotificationInstance {
        return open(config.copy(type = NotificationType.Error))
    }

    /**
     * Show an info notification
     * @param message Message content to display (String or Composable)
     * @param description Optional description content
     * @param duration Duration in seconds (0 = no auto-close)
     * @param placement Position of the notification
     * @param btn Optional button composable
     * @param onClose Callback when notification closes
     * @return NotificationInstance with then() and close() methods
     */
    fun info(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance {
        return open(
            NotificationConfig(
                message = NotificationContent.Text(message),
                description = description?.let { NotificationContent.Text(it) },
                type = NotificationType.Info,
                duration = duration,
                placement = placement,
                btn = btn,
                onClose = onClose
            )
        )
    }

    /**
     * Show an info notification with full config
     */
    fun info(config: NotificationConfig): NotificationInstance {
        return open(config.copy(type = NotificationType.Info))
    }

    /**
     * Show a warning notification
     * @param message Message content to display (String or Composable)
     * @param description Optional description content
     * @param duration Duration in seconds (0 = no auto-close)
     * @param placement Position of the notification
     * @param btn Optional button composable
     * @param onClose Callback when notification closes
     * @return NotificationInstance with then() and close() methods
     */
    fun warning(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance {
        return open(
            NotificationConfig(
                message = NotificationContent.Text(message),
                description = description?.let { NotificationContent.Text(it) },
                type = NotificationType.Warning,
                duration = duration,
                placement = placement,
                btn = btn,
                onClose = onClose
            )
        )
    }

    /**
     * Show a warning notification with full config
     */
    fun warning(config: NotificationConfig): NotificationInstance {
        return open(config.copy(type = NotificationType.Warning))
    }

    /**
     * Open a notification with full configuration
     * @param config Complete notification configuration
     * @return NotificationInstance with then() and close() methods
     */
    fun open(config: NotificationConfig): NotificationInstance {
        return NotificationState.addNotification(config)
    }

    /**
     * Destroy notification(s)
     * @param key If null, destroy all notifications. Otherwise destroy specific notification by key.
     */
    fun destroy(key: String? = null) {
        if (key == null) {
            NotificationState.destroyAll()
        } else {
            NotificationState.removeNotification(key)
        }
    }

    /**
     * Update global configuration
     * @param options Global configuration options
     */
    fun config(options: NotificationGlobalConfig) {
        NotificationState.updateConfig(options)
    }

    /**
     * Get current global configuration
     */
    fun getConfig(): NotificationGlobalConfig {
        return NotificationState.globalConfig
    }
}

/**
 * Hook for using notification with context holder
 * Returns a pair of (NotificationHookApi, contextHolder)
 *
 * Usage:
 * ```
 * val (notificationApi, contextHolder) = useNotification()
 *
 * Column {
 *     contextHolder() // Must be called to render notifications
 *
 *     Button(onClick = {
 *         notificationApi.success("Success!", "This is the description")
 *     }) {
 *         Text("Show Notification")
 *     }
 * }
 * ```
 */
@Composable
fun useNotification(): Pair<NotificationHookApi, @Composable () -> Unit> {
    val notificationsByPlacement = remember {
        mutableStateMapOf<NotificationPlacement, MutableList<NotificationItem>>().apply {
            NotificationPlacement.entries.forEach { placement ->
                this[placement] = mutableListOf()
            }
        }
    }
    val scope = rememberCoroutineScope()
    val globalConfig = remember { mutableStateOf(NotificationGlobalConfig()) }

    val api = remember {
        object : NotificationHookApi {
            override fun success(
                message: String,
                description: String?,
                duration: Double,
                placement: NotificationPlacement,
                btn: (@Composable () -> Unit)?,
                onClose: (() -> Unit)?,
            ): NotificationInstance {
                return open(
                    NotificationConfig(
                        message = NotificationContent.Text(message),
                        description = description?.let { NotificationContent.Text(it) },
                        type = NotificationType.Success,
                        duration = duration,
                        placement = placement,
                        btn = btn,
                        onClose = onClose
                    )
                )
            }

            override fun success(config: NotificationConfig): NotificationInstance {
                return open(config.copy(type = NotificationType.Success))
            }

            override fun error(
                message: String,
                description: String?,
                duration: Double,
                placement: NotificationPlacement,
                btn: (@Composable () -> Unit)?,
                onClose: (() -> Unit)?,
            ): NotificationInstance {
                return open(
                    NotificationConfig(
                        message = NotificationContent.Text(message),
                        description = description?.let { NotificationContent.Text(it) },
                        type = NotificationType.Error,
                        duration = duration,
                        placement = placement,
                        btn = btn,
                        onClose = onClose
                    )
                )
            }

            override fun error(config: NotificationConfig): NotificationInstance {
                return open(config.copy(type = NotificationType.Error))
            }

            override fun info(
                message: String,
                description: String?,
                duration: Double,
                placement: NotificationPlacement,
                btn: (@Composable () -> Unit)?,
                onClose: (() -> Unit)?,
            ): NotificationInstance {
                return open(
                    NotificationConfig(
                        message = NotificationContent.Text(message),
                        description = description?.let { NotificationContent.Text(it) },
                        type = NotificationType.Info,
                        duration = duration,
                        placement = placement,
                        btn = btn,
                        onClose = onClose
                    )
                )
            }

            override fun info(config: NotificationConfig): NotificationInstance {
                return open(config.copy(type = NotificationType.Info))
            }

            override fun warning(
                message: String,
                description: String?,
                duration: Double,
                placement: NotificationPlacement,
                btn: (@Composable () -> Unit)?,
                onClose: (() -> Unit)?,
            ): NotificationInstance {
                return open(
                    NotificationConfig(
                        message = NotificationContent.Text(message),
                        description = description?.let { NotificationContent.Text(it) },
                        type = NotificationType.Warning,
                        duration = duration,
                        placement = placement,
                        btn = btn,
                        onClose = onClose
                    )
                )
            }

            override fun warning(config: NotificationConfig): NotificationInstance {
                return open(config.copy(type = NotificationType.Warning))
            }

            override fun open(config: NotificationConfig): NotificationInstance {
                val key = config.key ?: "notification_${Random.nextLong()}_${Random.nextInt()}"
                val placement = config.placement

                val list = notificationsByPlacement[placement] ?: mutableListOf<NotificationItem>().also {
                    notificationsByPlacement[placement] = it
                }

                // Check maxCount
                if (globalConfig.value.maxCount > 0 && list.size >= globalConfig.value.maxCount) {
                    val oldest = list.firstOrNull()
                    oldest?.let {
                        it.job?.cancel()
                        list.remove(it)
                    }
                }

                val item = NotificationItem(key = key, config = config.copy(key = key))
                list.add(item)

                // Auto-close timer
                val duration = if (config.duration >= 0) config.duration else globalConfig.value.duration
                if (duration > 0) {
                    item.job = scope.launch {
                        delay((duration * 1000).toLong())
                        val index = list.indexOfFirst { it.key == key }
                        if (index != -1) {
                            val removedItem = list[index]
                            removedItem.config.onClose?.invoke()
                            list.removeAt(index)
                        }
                    }
                }

                return NotificationInstanceImpl(key) {
                    val index = list.indexOfFirst { it.key == key }
                    if (index != -1) {
                        val removedItem = list[index]
                        removedItem.job?.cancel()
                        removedItem.config.onClose?.invoke()
                        list.removeAt(index)
                    }
                }
            }

            override fun destroy(key: String?) {
                if (key == null) {
                    notificationsByPlacement.values.forEach { list ->
                        list.forEach { it.job?.cancel() }
                        list.clear()
                    }
                } else {
                    notificationsByPlacement.values.forEach { list ->
                        val index = list.indexOfFirst { it.key == key }
                        if (index != -1) {
                            val item = list[index]
                            item.job?.cancel()
                            list.removeAt(index)
                            return
                        }
                    }
                }
            }

            override fun config(options: NotificationGlobalConfig) {
                globalConfig.value = options
            }
        }
    }

    val contextHolder: @Composable () -> Unit = {
        NotificationContextHolder(
            notificationsByPlacement = notificationsByPlacement,
            globalConfig = globalConfig.value
        )
    }

    return api to contextHolder
}

/**
 * Notification Hook API interface for useNotification hook
 */
interface NotificationHookApi {
    fun success(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance

    fun success(config: NotificationConfig): NotificationInstance
    fun error(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance

    fun error(config: NotificationConfig): NotificationInstance
    fun info(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance

    fun info(config: NotificationConfig): NotificationInstance
    fun warning(
        message: String,
        description: String? = null,
        duration: Double = 4.5,
        placement: NotificationPlacement = NotificationPlacement.TopRight,
        btn: (@Composable () -> Unit)? = null,
        onClose: (() -> Unit)? = null,
    ): NotificationInstance

    fun warning(config: NotificationConfig): NotificationInstance
    fun open(config: NotificationConfig): NotificationInstance
    fun destroy(key: String? = null)
    fun config(options: NotificationGlobalConfig)
}

/**
 * Notification context holder for useNotification hook
 */
@Composable
private fun NotificationContextHolder(
    notificationsByPlacement: Map<NotificationPlacement, List<NotificationItem>>,
    globalConfig: NotificationGlobalConfig,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // TopLeft
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.TopLeft]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // TopRight
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.TopRight]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // BottomLeft
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.BottomLeft]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // BottomRight
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.BottomRight]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // Top (center)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            notificationsByPlacement[NotificationPlacement.Top]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // Bottom (center)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            notificationsByPlacement[NotificationPlacement.Bottom]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }
    }
}

/**
 * Global Notification Container - must be placed at app root
 * Displays all notifications created via AntNotification global API
 */
@Composable
fun AntNotificationContainer() {
    val notificationsByPlacement = NotificationState.notifications
    val globalConfig = NotificationState.globalConfig

    Box(modifier = Modifier.fillMaxSize()) {
        // TopLeft
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.TopLeft]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // TopRight
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.TopRight]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // BottomLeft
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.BottomLeft]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // BottomRight
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            notificationsByPlacement[NotificationPlacement.BottomRight]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // Top (center)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            notificationsByPlacement[NotificationPlacement.Top]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }

        // Bottom (center)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = globalConfig.bottom),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            notificationsByPlacement[NotificationPlacement.Bottom]?.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                    ) {
                        AntNotificationItem(config = item.config, globalConfig = globalConfig)
                    }
                }
            }
        }
    }
}

/**
 * Individual notification item component
 */
@Composable
private fun AntNotificationItem(
    config: NotificationConfig,
    globalConfig: NotificationGlobalConfig,
) {
    val (iconText, iconColor) = when (config.type) {
        NotificationType.Success -> "\u2713" to Color(0xFF52C41A)
        NotificationType.Error -> "\u2715" to Color(0xFFFF4D4F)
        NotificationType.Warning -> "\u26A0" to Color(0xFFFAAD14)
        NotificationType.Info -> "\u2139" to Color(0xFF1890FF)
        NotificationType.Open -> null to Color.Transparent
    }

    Box(
        modifier = Modifier
            .width(384.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .then(
                if (config.onClick != null) {
                    Modifier.clickable { config.onClick.invoke() }
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            if (config.icon != null || config.type != NotificationType.Open) {
                Box(modifier = Modifier.width(24.dp)) {
                    config.icon?.invoke() ?: if (iconText != null) {
                        Text(
                            text = iconText,
                            color = iconColor,
                            fontSize = 22.sp
                        )
                    } else {
                        Spacer(modifier = Modifier.size(0.dp))
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Message
                when (val msg = config.message) {
                    is NotificationContent.Text -> Text(
                        text = msg.text,
                        fontSize = 16.sp,
                        color = Color(0xFF000000D9)
                    )

                    is NotificationContent.Composable -> msg.content()
                }

                // Description
                config.description?.let { desc ->
                    when (desc) {
                        is NotificationContent.Text -> Text(
                            text = desc.text,
                            fontSize = 14.sp,
                            color = Color(0xFF00000073)
                        )

                        is NotificationContent.Composable -> desc.content()
                    }
                }

                // Button
                config.btn?.let {
                    Box(modifier = Modifier.padding(top = 8.dp)) {
                        it()
                    }
                }
            }

            // Close button
            val closeIcon = config.closeIcon ?: globalConfig.closeIcon
            if (closeIcon != null) {
                IconButton(
                    onClick = {
                        config.onClose?.invoke()
                        NotificationState.removeNotification(config.key ?: "", config.placement)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    closeIcon()
                }
            } else {
                IconButton(
                    onClick = {
                        config.onClose?.invoke()
                        NotificationState.removeNotification(config.key ?: "", config.placement)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Text("\u2715", fontSize = 14.sp, color = Color(0xFF00000073))
                }
            }
        }
    }
}
