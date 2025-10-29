package digital.guimauve.antdesign

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Message types matching Ant Design React Message component
 */
enum class MessageType {
    Success,
    Error,
    Warning,
    Info,
    Loading
}

/**
 * Complete MessageConfig with all Ant Design v5 properties
 * Matches React ArgsProps interface
 */
data class MessageConfig(
    val content: String,
    val type: MessageType = MessageType.Info,
    val duration: Double = 3.0, // in seconds, 0 = no auto-close
    val icon: (@Composable () -> Unit)? = null,
    val key: String? = null,
    val className: String? = null,
    val style: Map<String, Any>? = null,
    val onClose: (() -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
)

/**
 * Global configuration for Message component
 * Matches React ConfigOptions interface
 */
data class MessageGlobalConfig(
    val top: Dp = 24.dp,
    val duration: Double = 3.0,
    val maxCount: Int = 0, // 0 = unlimited
    val rtl: Boolean = false,
    val prefixCls: String = "ant-message",
    val getContainer: (() -> Any)? = null,
)

/**
 * Message instance with promise-like API
 * Matches React MessageType return type
 */
interface MessageInstance {
    val key: String

    /**
     * Promise-like then method for chaining
     */
    fun then(onFulfilled: (() -> Unit)? = null): MessageInstance

    /**
     * Close this message instance
     */
    fun close()
}

/**
 * Internal implementation of MessageInstance
 */
private class MessageInstanceImpl(
    override val key: String,
    private val onCloseCallback: () -> Unit,
) : MessageInstance {

    private val fulfillCallbacks = mutableListOf<() -> Unit>()
    private var isClosed = false

    override fun then(onFulfilled: (() -> Unit)?): MessageInstance {
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
 * Internal message item with state
 */
internal data class MessageItem(
    val key: String,
    val config: MessageConfig,
    var visible: Boolean = true,
    var job: Job? = null,
)

/**
 * Global message state manager
 */
private object MessageState {
    val messages = mutableStateListOf<MessageItem>()
    var globalConfig = MessageGlobalConfig()
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun addMessage(config: MessageConfig): MessageInstance {
        val key = config.key ?: "message_${Random.nextLong()}_${Random.nextInt()}"

        // Check maxCount and remove oldest if needed
        if (globalConfig.maxCount > 0 && messages.size >= globalConfig.maxCount) {
            val oldest = messages.firstOrNull()
            oldest?.let { removeMessage(it.key) }
        }

        val item = MessageItem(key = key, config = config.copy(key = key))
        messages.add(item)

        // Auto-close timer
        val duration = if (config.duration >= 0) config.duration else globalConfig.duration
        if (duration > 0) {
            item.job = scope.launch {
                delay((duration * 1000).toLong())
                removeMessage(key)
            }
        }

        return MessageInstanceImpl(key) {
            removeMessage(key)
        }
    }

    fun removeMessage(key: String) {
        val index = messages.indexOfFirst { it.key == key }
        if (index != -1) {
            val item = messages[index]
            item.job?.cancel()
            item.config.onClose?.invoke()
            messages.removeAt(index)
        }
    }

    fun updateConfig(options: MessageGlobalConfig) {
        globalConfig = options
    }

    fun destroyAll() {
        messages.forEach { it.job?.cancel() }
        messages.clear()
    }
}

/**
 * Global Message API matching Ant Design React v5
 * Usage: AntMessage.success("Success!"), AntMessage.error("Error!"), etc.
 */
object AntMessage {

    /**
     * Show a success message
     * @param content Message content to display
     * @param duration Duration in seconds (0 = no auto-close)
     * @param onClose Callback when message closes
     * @return MessageInstance with then() and close() methods
     */
    fun success(
        content: String,
        duration: Double = 3.0,
        onClose: (() -> Unit)? = null,
    ): MessageInstance {
        return open(
            MessageConfig(
                content = content,
                type = MessageType.Success,
                duration = duration,
                onClose = onClose
            )
        )
    }

    /**
     * Show an error message
     * @param content Message content to display
     * @param duration Duration in seconds (0 = no auto-close)
     * @param onClose Callback when message closes
     * @return MessageInstance with then() and close() methods
     */
    fun error(
        content: String,
        duration: Double = 3.0,
        onClose: (() -> Unit)? = null,
    ): MessageInstance {
        return open(
            MessageConfig(
                content = content,
                type = MessageType.Error,
                duration = duration,
                onClose = onClose
            )
        )
    }

    /**
     * Show an info message
     * @param content Message content to display
     * @param duration Duration in seconds (0 = no auto-close)
     * @param onClose Callback when message closes
     * @return MessageInstance with then() and close() methods
     */
    fun info(
        content: String,
        duration: Double = 3.0,
        onClose: (() -> Unit)? = null,
    ): MessageInstance {
        return open(
            MessageConfig(
                content = content,
                type = MessageType.Info,
                duration = duration,
                onClose = onClose
            )
        )
    }

    /**
     * Show a warning message
     * @param content Message content to display
     * @param duration Duration in seconds (0 = no auto-close)
     * @param onClose Callback when message closes
     * @return MessageInstance with then() and close() methods
     */
    fun warning(
        content: String,
        duration: Double = 3.0,
        onClose: (() -> Unit)? = null,
    ): MessageInstance {
        return open(
            MessageConfig(
                content = content,
                type = MessageType.Warning,
                duration = duration,
                onClose = onClose
            )
        )
    }

    /**
     * Show a loading message (no auto-close by default)
     * @param content Message content to display
     * @param duration Duration in seconds (0 = no auto-close)
     * @param onClose Callback when message closes
     * @return MessageInstance with then() and close() methods
     */
    fun loading(
        content: String,
        duration: Double = 0.0,
        onClose: (() -> Unit)? = null,
    ): MessageInstance {
        return open(
            MessageConfig(
                content = content,
                type = MessageType.Loading,
                duration = duration,
                onClose = onClose
            )
        )
    }

    /**
     * Open a message with full configuration
     * @param config Complete message configuration
     * @return MessageInstance with then() and close() methods
     */
    fun open(config: MessageConfig): MessageInstance {
        return MessageState.addMessage(config)
    }

    /**
     * Destroy message(s)
     * @param key If null, destroy all messages. Otherwise destroy specific message by key.
     */
    fun destroy(key: String? = null) {
        if (key == null) {
            MessageState.destroyAll()
        } else {
            MessageState.removeMessage(key)
        }
    }

    /**
     * Update global configuration
     * @param options Global configuration options
     */
    fun config(options: MessageGlobalConfig) {
        MessageState.updateConfig(options)
    }

    /**
     * Get current global configuration
     */
    fun getConfig(): MessageGlobalConfig {
        return MessageState.globalConfig
    }
}

/**
 * Hook for using message with context holder
 * Returns a pair of (MessageHookApi, contextHolder)
 *
 * Usage:
 * ```
 * val (messageApi, contextHolder) = useMessage()
 *
 * Column {
 *     contextHolder() // Must be called to render messages
 *
 *     Button(onClick = {
 *         messageApi.success("Success!")
 *     }) {
 *         Text("Show Message")
 *     }
 * }
 * ```
 */
@Composable
fun useMessage(): Pair<MessageHookApi, @Composable () -> Unit> {
    val messages = remember { mutableStateListOf<MessageItem>() }
    val scope = rememberCoroutineScope()
    val globalConfig = remember { mutableStateOf(MessageGlobalConfig()) }

    val api = remember {
        object : MessageHookApi {
            override fun success(content: String, duration: Double, onClose: (() -> Unit)?): MessageInstance {
                return open(
                    MessageConfig(
                        content = content,
                        type = MessageType.Success,
                        duration = duration,
                        onClose = onClose
                    )
                )
            }

            override fun error(content: String, duration: Double, onClose: (() -> Unit)?): MessageInstance {
                return open(
                    MessageConfig(
                        content = content,
                        type = MessageType.Error,
                        duration = duration,
                        onClose = onClose
                    )
                )
            }

            override fun info(content: String, duration: Double, onClose: (() -> Unit)?): MessageInstance {
                return open(
                    MessageConfig(
                        content = content,
                        type = MessageType.Info,
                        duration = duration,
                        onClose = onClose
                    )
                )
            }

            override fun warning(content: String, duration: Double, onClose: (() -> Unit)?): MessageInstance {
                return open(
                    MessageConfig(
                        content = content,
                        type = MessageType.Warning,
                        duration = duration,
                        onClose = onClose
                    )
                )
            }

            override fun loading(content: String, duration: Double, onClose: (() -> Unit)?): MessageInstance {
                return open(
                    MessageConfig(
                        content = content,
                        type = MessageType.Loading,
                        duration = duration,
                        onClose = onClose
                    )
                )
            }

            override fun open(config: MessageConfig): MessageInstance {
                val key = config.key ?: "message_${Random.nextLong()}_${Random.nextInt()}"

                // Check maxCount
                if (globalConfig.value.maxCount > 0 && messages.size >= globalConfig.value.maxCount) {
                    val oldest = messages.firstOrNull()
                    oldest?.let {
                        it.job?.cancel()
                        messages.remove(it)
                    }
                }

                val item = MessageItem(key = key, config = config.copy(key = key))
                messages.add(item)

                // Auto-close timer
                val duration = if (config.duration >= 0) config.duration else globalConfig.value.duration
                if (duration > 0) {
                    item.job = scope.launch {
                        delay((duration * 1000).toLong())
                        val index = messages.indexOfFirst { it.key == key }
                        if (index != -1) {
                            val removedItem = messages[index]
                            removedItem.config.onClose?.invoke()
                            messages.removeAt(index)
                        }
                    }
                }

                return MessageInstanceImpl(key) {
                    val index = messages.indexOfFirst { it.key == key }
                    if (index != -1) {
                        val removedItem = messages[index]
                        removedItem.job?.cancel()
                        removedItem.config.onClose?.invoke()
                        messages.removeAt(index)
                    }
                }
            }

            override fun destroy(key: String?) {
                if (key == null) {
                    messages.forEach { it.job?.cancel() }
                    messages.clear()
                } else {
                    val index = messages.indexOfFirst { it.key == key }
                    if (index != -1) {
                        val item = messages[index]
                        item.job?.cancel()
                        messages.removeAt(index)
                    }
                }
            }

            override fun config(options: MessageGlobalConfig) {
                globalConfig.value = options
            }
        }
    }

    val contextHolder: @Composable () -> Unit = {
        MessageContextHolder(messages = messages, globalConfig = globalConfig.value)
    }

    return api to contextHolder
}

/**
 * Message Hook API interface for useMessage hook
 */
interface MessageHookApi {
    fun success(content: String, duration: Double = 3.0, onClose: (() -> Unit)? = null): MessageInstance
    fun error(content: String, duration: Double = 3.0, onClose: (() -> Unit)? = null): MessageInstance
    fun info(content: String, duration: Double = 3.0, onClose: (() -> Unit)? = null): MessageInstance
    fun warning(content: String, duration: Double = 3.0, onClose: (() -> Unit)? = null): MessageInstance
    fun loading(content: String, duration: Double = 0.0, onClose: (() -> Unit)? = null): MessageInstance
    fun open(config: MessageConfig): MessageInstance
    fun destroy(key: String? = null)
    fun config(options: MessageGlobalConfig)
}

/**
 * Message context holder for useMessage hook
 */
@Composable
private fun MessageContextHolder(
    messages: List<MessageItem>,
    globalConfig: MessageGlobalConfig,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            messages.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        AntMessageItem(config = item.config)
                    }
                }
            }
        }
    }
}

/**
 * Global Message Container - must be placed at app root
 * Displays all messages created via AntMessage global API
 */
@Composable
fun AntMessageContainer() {
    val messages = MessageState.messages
    val globalConfig = MessageState.globalConfig

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top = globalConfig.top),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            messages.forEach { item ->
                key(item.key) {
                    AnimatedVisibility(
                        visible = item.visible,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        AntMessageItem(config = item.config)
                    }
                }
            }
        }
    }
}

/**
 * Individual message item component
 */
@Composable
private fun AntMessageItem(config: MessageConfig) {
    val (iconText, iconColor) = when (config.type) {
        MessageType.Success -> "✓" to Color(0xFF52C41A)
        MessageType.Error -> "✕" to Color(0xFFFF4D4F)
        MessageType.Warning -> "⚠" to Color(0xFFFAAD14)
        MessageType.Info -> "ℹ" to Color(0xFF1890FF)
        MessageType.Loading -> "⟳" to Color(0xFF1890FF)
    }

    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(6.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .then(
                if (config.onClick != null) {
                    Modifier.clickable { config.onClick.invoke() }
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            config.icon?.invoke() ?: Text(
                text = iconText,
                color = iconColor,
                fontSize = 14.sp
            )
            Text(
                text = config.content,
                fontSize = 14.sp,
                color = Color(0xFF000000D9)
            )
        }
    }
}
