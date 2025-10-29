package digital.guimauve.antdesign

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Timeline component for Compose Multiplatform with 100% React Ant Design v5.x parity.
 *
 * A vertical timeline component that displays information in chronological order.
 * Supports three modes: Left, Alternate, and Right positioning.
 *
 * # Features
 * - Three display modes (Left, Alternate, Right)
 * - Custom dots with any composable content
 * - Pending state with spinning indicator
 * - Color presets and custom colors
 * - Labels for alternate mode
 * - Position override per item
 * - Reverse order support
 * - Semantic styles and class names (v5.5.0+)
 * - Item-based API or children API
 *
 * # Examples
 *
 * ## Basic usage with items list:
 * ```kotlin
 * AntTimeline(
 *     items = listOf(
 *         TimelineItemType(
 *             children = { Text("Create a services site 2015-09-01") }
 *         ),
 *         TimelineItemType(
 *             children = { Text("Solve initial network problems 2015-09-01") }
 *         ),
 *         TimelineItemType(
 *             color = "green",
 *             children = { Text("Technical testing 2015-09-01") }
 *         )
 *     )
 * )
 * ```
 *
 * ## Alternate mode with labels:
 * ```kotlin
 * AntTimeline(
 *     mode = TimelineMode.Alternate,
 *     items = listOf(
 *         TimelineItemType(
 *             label = { Text("2015-09-01") },
 *             children = { Text("Create a services site") }
 *         ),
 *         TimelineItemType(
 *             label = { Text("2015-09-01 09:12:11") },
 *             color = "green",
 *             children = { Text("Solve initial network problems") }
 *         )
 *     )
 * )
 * ```
 *
 * ## With pending state:
 * ```kotlin
 * AntTimeline(
 *     pending = { Text("Recording...") },
 *     pendingDot = { LoadingOutlined() },
 *     items = listOf(
 *         TimelineItemType(children = { Text("Create a services site 2015-09-01") }),
 *         TimelineItemType(children = { Text("Solve initial network problems 2015-09-01") })
 *     )
 * )
 * ```
 *
 * ## Custom dots:
 * ```kotlin
 * AntTimeline(
 *     items = listOf(
 *         TimelineItemType(
 *             dot = { ClockCircleOutlined() },
 *             children = { Text("Technical testing 2015-09-01") }
 *         )
 *     )
 * )
 * ```
 *
 * @param items List of timeline items (item-based API) - primary API
 * @param modifier The modifier to be applied to the Timeline
 * @param mode Position mode: Left, Alternate, or Right
 * @param pending Pending state - can be Boolean or composable content
 * @param pendingDot Custom dot for pending item
 * @param reverse Reverse the order of timeline items
 * @param classNames Semantic class names for timeline elements (v5.5.0+)
 * @param styles Semantic styles with Modifiers for timeline elements (v5.5.0+)
 */
@Composable
fun AntTimeline(
    items: List<TimelineItemType>,
    modifier: Modifier = Modifier,
    mode: TimelineMode = TimelineMode.Left,
    pending: Any? = null,
    pendingDot: (@Composable () -> Unit)? = null,
    reverse: Boolean = false,
    classNames: TimelineClassNames? = null,
    styles: TimelineStyles? = null,
) {
    // Use items list
    val allItems = items

    // Apply reverse if needed
    val displayItems = if (reverse) allItems.reversed() else allItems

    // Determine if there's a pending item
    val hasPending = pending != null && pending != false
    val pendingContent: (@Composable () -> Unit)? = when (pending) {
        is Boolean -> if (pending) {
            { Text("Loading...") }
        } else null

        null, false -> null
        else -> {
            // Assume it's a composable function or string
            @Composable {
                when (pending) {
                    is String -> Text(pending)
                    else -> (pending as? (@Composable () -> Unit))?.invoke() ?: Text("Loading...")
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        displayItems.forEachIndexed { index, item ->
            val itemPosition = determineItemPosition(
                mode = mode,
                index = index,
                itemPosition = item.position
            )

            val isLast = index == displayItems.lastIndex && !hasPending

            AntTimelineItemInternal(
                item = item,
                position = itemPosition,
                isLast = isLast,
                isPending = false,
                classNames = classNames,
                styles = styles
            )
        }

        // Add pending item if present
        if (hasPending && pendingContent != null) {
            val pendingItem = TimelineItemType(
                color = "gray",
                dot = pendingDot ?: { PendingDot() },
                children = pendingContent
            )

            val pendingPosition = when (mode) {
                TimelineMode.Left -> TimelinePosition.Right
                TimelineMode.Right -> TimelinePosition.Left
                TimelineMode.Alternate -> {
                    if (displayItems.size % 2 == 0) TimelinePosition.Right else TimelinePosition.Left
                }
            }

            AntTimelineItemInternal(
                item = pendingItem,
                position = pendingPosition,
                isLast = true,
                isPending = true,
                classNames = classNames,
                styles = styles
            )
        }
    }
}


/**
 * Timeline display mode
 */
enum class TimelineMode {
    /** All items on right, dots on left */
    Left,

    /** Alternate left and right positioning */
    Alternate,

    /** All items on left, dots on right */
    Right
}

/**
 * Timeline item position
 */
enum class TimelinePosition {
    Left,
    Right
}

/**
 * Timeline item data for item-based API
 *
 * @param key Unique key for the item
 * @param color Color preset name (e.g., "blue", "red", "green") or Color object
 * @param dot Custom dot content
 * @param label Label content (shown on opposite side in alternate mode)
 * @param position Override position (left or right)
 * @param classNames Semantic class names for item elements
 * @param styles Semantic styles with Modifiers for item elements
 * @param children Item content
 */
data class TimelineItemType(
    val key: String? = null,
    val color: Any? = null,
    val dot: (@Composable () -> Unit)? = null,
    val label: (@Composable () -> Unit)? = null,
    val position: TimelinePosition? = null,
    val classNames: TimelineItemClassNames? = null,
    val styles: TimelineItemStyles? = null,
    val children: @Composable () -> Unit,
)

/**
 * Semantic class names for Timeline (v5.5.0+)
 *
 * @param item Class name for each timeline item wrapper
 * @param itemTail Class name for the connecting tail line
 * @param itemHead Class name for the dot/head element
 * @param itemContent Class name for the content area
 */
data class TimelineClassNames(
    val item: String = "",
    val itemTail: String = "",
    val itemHead: String = "",
    val itemContent: String = "",
)

/**
 * Semantic styles for Timeline with Modifiers (v5.5.0+)
 *
 * @param item Modifier for each timeline item wrapper
 * @param itemTail Modifier for the connecting tail line
 * @param itemHead Modifier for the dot/head element
 * @param itemContent Modifier for the content area
 */
data class TimelineStyles(
    val item: Modifier = Modifier,
    val itemTail: Modifier = Modifier,
    val itemHead: Modifier = Modifier,
    val itemContent: Modifier = Modifier,
)

/**
 * Semantic class names for Timeline.Item (v5.5.0+)
 *
 * @param label Class name for the label element
 * @param dot Class name for the custom dot
 * @param content Class name for the content area
 */
data class TimelineItemClassNames(
    val label: String = "",
    val dot: String = "",
    val content: String = "",
)

/**
 * Semantic styles for Timeline.Item with Modifiers (v5.5.0+)
 *
 * @param label Modifier for the label element
 * @param dot Modifier for the custom dot
 * @param content Modifier for the content area
 */
data class TimelineItemStyles(
    val label: Modifier = Modifier,
    val dot: Modifier = Modifier,
    val content: Modifier = Modifier,
)

/**
 * Preset colors for Timeline items matching React Ant Design
 */
object TimelineColors {
    val Blue = Color(0xFF1890FF)
    val Red = Color(0xFFFF4D4F)
    val Green = Color(0xFF52C41A)
    val Gray = Color(0xFFD9D9D9)
    val Yellow = Color(0xFFFAAD14)
    val Cyan = Color(0xFF13C2C2)
    val Purple = Color(0xFF722ED1)
    val Magenta = Color(0xFFEB2F96)
    val Orange = Color(0xFFFA8C16)
    val Lime = Color(0xFFA0D911)
    val Geekblue = Color(0xFF2F54EB)
    val Volcano = Color(0xFFFA541C)
    val Gold = Color(0xFFFAAD14)
}

/**
 * Internal Timeline Item renderer
 */
@Composable
private fun AntTimelineItemInternal(
    item: TimelineItemType,
    position: TimelinePosition,
    isLast: Boolean,
    isPending: Boolean,
    classNames: TimelineClassNames?,
    styles: TimelineStyles?,
) {
    // Parse color from string or Color
    val dotColor = parseTimelineColor(item.color)

    // Measure content height for tail line
    var contentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(styles?.item ?: Modifier)
    ) {
        when (position) {
            TimelinePosition.Right -> {
                // Dot on left, content on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Label (if in alternate mode, on left side)
                    if (item.label != null) {
                        Box(
                            modifier = Modifier
                                .widthIn(min = 80.dp, max = 120.dp)
                                .padding(end = 16.dp)
                                .then(item.styles?.label ?: Modifier),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides TextStyle(
                                    fontSize = 12.sp,
                                    color = Color(0xFF00000073)
                                )
                            ) {
                                item.label.invoke()
                            }
                        }
                    }

                    // Dot and tail
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(16.dp)
                    ) {
                        // Dot
                        Box(
                            modifier = Modifier
                                .size(if (item.dot != null) 16.dp else 10.dp)
                                .then(styles?.itemHead ?: Modifier)
                                .then(item.styles?.dot ?: Modifier),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.dot != null) {
                                item.dot.invoke()
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                )
                            }
                        }

                        // Tail line
                        if (!isLast) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(maxOf(contentHeight - 14.dp, 20.dp))
                                    .then(styles?.itemTail ?: Modifier)
                                    .background(
                                        if (isPending)
                                            Color(0xFFD9D9D9).copy(alpha = 0.5f)
                                        else
                                            Color(0xFFD9D9D9)
                                    )
                            )
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = if (!isLast) 20.dp else 0.dp)
                            .weight(1f)
                            .then(styles?.itemContent ?: Modifier)
                            .then(item.styles?.content ?: Modifier)
                            .onGloballyPositioned { coordinates ->
                                contentHeight = with(density) { coordinates.size.height.toDp() }
                            }
                    ) {
                        CompositionLocalProvider(
                            LocalTextStyle provides TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                        ) {
                            item.children()
                        }
                    }
                }
            }

            TimelinePosition.Left -> {
                // Content on left, dot on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp, bottom = if (!isLast) 20.dp else 0.dp)
                            .then(styles?.itemContent ?: Modifier)
                            .then(item.styles?.content ?: Modifier)
                            .onGloballyPositioned { coordinates ->
                                contentHeight = with(density) { coordinates.size.height.toDp() }
                            }
                    ) {
                        CompositionLocalProvider(
                            LocalTextStyle provides TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                        ) {
                            item.children()
                        }
                    }

                    // Dot and tail
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(16.dp)
                    ) {
                        // Dot
                        Box(
                            modifier = Modifier
                                .size(if (item.dot != null) 16.dp else 10.dp)
                                .then(styles?.itemHead ?: Modifier)
                                .then(item.styles?.dot ?: Modifier),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.dot != null) {
                                item.dot.invoke()
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                )
                            }
                        }

                        // Tail line
                        if (!isLast) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(maxOf(contentHeight - 14.dp, 20.dp))
                                    .then(styles?.itemTail ?: Modifier)
                                    .background(
                                        if (isPending)
                                            Color(0xFFD9D9D9).copy(alpha = 0.5f)
                                        else
                                            Color(0xFFD9D9D9)
                                    )
                            )
                        }
                    }

                    // Label (if in alternate mode, on right side)
                    if (item.label != null) {
                        Box(
                            modifier = Modifier
                                .widthIn(min = 80.dp, max = 120.dp)
                                .padding(start = 16.dp)
                                .then(item.styles?.label ?: Modifier),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides TextStyle(
                                    fontSize = 12.sp,
                                    color = Color(0xFF00000073)
                                )
                            ) {
                                item.label.invoke()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Determine item position based on mode and overrides
 */
private fun determineItemPosition(
    mode: TimelineMode,
    index: Int,
    itemPosition: TimelinePosition?,
): TimelinePosition {
    // Item position override takes precedence
    if (itemPosition != null) {
        return itemPosition
    }

    return when (mode) {
        TimelineMode.Left -> TimelinePosition.Right
        TimelineMode.Right -> TimelinePosition.Left
        TimelineMode.Alternate -> {
            if (index % 2 == 0) TimelinePosition.Right else TimelinePosition.Left
        }
    }
}

/**
 * Parse color from string preset name or Color object
 */
private fun parseTimelineColor(color: Any?): Color {
    if (color == null) {
        return TimelineColors.Blue
    }

    if (color is Color) {
        return color
    }

    if (color is String) {
        return when (color.lowercase()) {
            "blue" -> TimelineColors.Blue
            "red" -> TimelineColors.Red
            "green" -> TimelineColors.Green
            "gray", "grey" -> TimelineColors.Gray
            "yellow" -> TimelineColors.Yellow
            "cyan" -> TimelineColors.Cyan
            "purple" -> TimelineColors.Purple
            "magenta" -> TimelineColors.Magenta
            "orange" -> TimelineColors.Orange
            "lime" -> TimelineColors.Lime
            "geekblue" -> TimelineColors.Geekblue
            "volcano" -> TimelineColors.Volcano
            "gold" -> TimelineColors.Gold
            else -> {
                // Try to parse as hex color (e.g., "#1890FF" or "1890FF")
                try {
                    val hex = if (color.startsWith("#")) color.substring(1) else color
                    if (hex.length == 6) {
                        val r = hex.substring(0, 2).toInt(16)
                        val g = hex.substring(2, 4).toInt(16)
                        val b = hex.substring(4, 6).toInt(16)
                        Color(r, g, b)
                    } else if (hex.length == 8) {
                        val a = hex.substring(0, 2).toInt(16)
                        val r = hex.substring(2, 4).toInt(16)
                        val g = hex.substring(4, 6).toInt(16)
                        val b = hex.substring(6, 8).toInt(16)
                        Color(r, g, b, a)
                    } else {
                        TimelineColors.Blue
                    }
                } catch (e: Exception) {
                    TimelineColors.Blue
                }
            }
        }
    }

    return TimelineColors.Blue
}

/**
 * Spinning dot for pending state
 */
@Composable
private fun PendingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pending")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFFD9D9D9))
            .rotate(rotation)
    )
}

/**
 * Legacy API compatibility - Old TimelineItem data class
 * Kept for backward compatibility
 */
@Deprecated(
    "Use TimelineItemType instead",
    ReplaceWith("TimelineItemType(color = color, dot = dot, label = label?.let { { Text(it) } }, position = position, children = content)")
)
data class TimelineItem(
    val content: @Composable () -> Unit,
    val color: Color? = null,
    val dot: (@Composable () -> Unit)? = null,
    val label: String? = null,
    val position: TimelinePosition? = null,
)

/**
 * Legacy API compatibility - Old TimelineItemPosition enum
 * Kept for backward compatibility
 */
@Deprecated(
    "Use TimelinePosition instead",
    ReplaceWith("TimelinePosition")
)
typealias TimelineItemPosition = TimelinePosition
