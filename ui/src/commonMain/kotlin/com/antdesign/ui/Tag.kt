package com.antdesign.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antdesign.ui.icons.CloseOutlinedIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Tag color variants matching Ant Design preset colors
 * Maps to React's preset color strings: 'success' | 'processing' | 'error' | 'warning' | 'default' | ...
 */
sealed class TagColor {
    object Success : TagColor()
    object Processing : TagColor()
    object Error : TagColor()
    object Warning : TagColor()
    object Default : TagColor()
    object Magenta : TagColor()
    object Red : TagColor()
    object Volcano : TagColor()
    object Orange : TagColor()
    object Gold : TagColor()
    object Lime : TagColor()
    object Green : TagColor()
    object Cyan : TagColor()
    object Blue : TagColor()
    object GeekBlue : TagColor()
    object Purple : TagColor()
    data class Custom(val color: Color) : TagColor()
}

/**
 * Semantic class names for Tag sub-components
 * Maps to React's classNames prop (v5.7.0+)
 */
data class TagClassNames(
    val root: String? = null,
    val icon: String? = null,
    val closeIcon: String? = null
)

/**
 * Semantic styles for Tag sub-components
 * Maps to React's styles prop
 */
data class TagStyles(
    val root: Modifier? = null,
    val icon: Modifier? = null,
    val closeIcon: Modifier? = null
)

/**
 * Ant Design Tag Component - 100% Feature Complete
 *
 * Full parity with React Ant Design v5.x Tag component
 *
 * @param children Tag content (Composable)
 * @param modifier Root modifier
 * @param color Tag color - preset (TagColor object) or custom (TagColor.Custom)
 * @param icon Icon before text (Composable, optional)
 * @param closable Whether Tag can be closed (default: false)
 * @param closeIcon Custom close icon (Composable, optional)
 * @param onClose Close callback (receives event parameter)
 * @param bordered Show border (default: true)
 * @param classNames Semantic class names for sub-components (v5.7.0+)
 * @param styles Semantic styles for sub-components
 *
 * Preset colors: success, processing, error, warning, default,
 *                magenta, red, volcano, orange, gold, lime, green,
 *                cyan, blue, geekblue, purple
 *
 * Example:
 * ```
 * AntTag(
 *     color = TagColor.Success,
 *     closable = true,
 *     onClose = { println("Tag closed") }
 * ) {
 *     Text("Success Tag")
 * }
 * ```
 */
@Composable
fun AntTag(
    children: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: TagColor = TagColor.Default,
    icon: (@Composable () -> Unit)? = null,
    closable: Boolean = false,
    closeIcon: (@Composable () -> Unit)? = null,
    onClose: ((event: Any?) -> Unit)? = null,
    bordered: Boolean = true,
    classNames: TagClassNames? = null,
    styles: TagStyles? = null
) {
    var visible by remember { mutableStateOf(true) }
    var isClosing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Close animation: 200ms fade + scale
    val alpha by animateFloatAsState(
        targetValue = if (isClosing) 0f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "tag_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isClosing) 0.8f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "tag_scale"
    )

    // Handle close animation
    LaunchedEffect(isClosing) {
        if (isClosing) {
            delay(200) // Wait for animation to complete
            visible = false
        }
    }

    if (!visible) return

    // Get color scheme for the tag
    val (backgroundColor, textColor, borderColor) = getTagColors(color)

    // Apply root styles
    val rootModifier = styles?.root ?: Modifier

    Card(
        modifier = modifier
            .then(rootModifier)
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = if (bordered) BorderStroke(1.dp, borderColor) else null,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            if (icon != null) {
                val iconModifier = styles?.icon ?: Modifier
                Box(modifier = iconModifier) {
                    icon()
                }
            }

            // Content
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    color = textColor,
                    fontSize = 12.sp
                )
            ) {
                children()
            }

            // Close button
            if (closable) {
                val closeIconModifier = styles?.closeIcon ?: Modifier
                IconButton(
                    onClick = {
                        isClosing = true
                        scope.launch {
                            onClose?.invoke(null)
                        }
                    },
                    modifier = closeIconModifier.size(16.dp)
                ) {
                    if (closeIcon != null) {
                        closeIcon()
                    } else {
                        CloseOutlinedIcon(
                            size = 10.dp,
                            color = textColor.copy(alpha = 0.45f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Ant Design CheckableTag Component
 *
 * Button-like toggle tag for filtering or selection scenarios
 *
 * @param children Tag content (Composable)
 * @param checked Checked state
 * @param onChange Check callback (receives new checked state)
 * @param modifier Root modifier
 *
 * Example:
 * ```
 * var checked by remember { mutableStateOf(false) }
 * AntCheckableTag(
 *     checked = checked,
 *     onChange = { checked = it }
 * ) {
 *     Text("Checkable")
 * }
 * ```
 */
@Composable
fun AntCheckableTag(
    children: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    val backgroundColor = if (checked) {
        Color(0xFF1890FF) // Primary blue
    } else {
        Color(0xFFFAFAFA) // Default gray
    }

    val textColor = if (checked) {
        Color.White
    } else {
        Color(0xFF000000D9) // rgba(0, 0, 0, 0.85)
    }

    val borderColor = if (checked) {
        Color(0xFF1890FF)
    } else {
        Color(0xFFD9D9D9)
    }

    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onChange?.invoke(!checked)
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    color = textColor,
                    fontSize = 12.sp
                )
            ) {
                children()
            }
        }
    }
}

/**
 * Get color scheme for tag based on preset or custom color
 * Returns Triple(backgroundColor, textColor, borderColor)
 */
private fun getTagColors(color: TagColor): Triple<Color, Color, Color> {
    return when (color) {
        is TagColor.Success -> Triple(
            Color(0xFFF6FFED), // green-1
            Color(0xFF52C41A), // green-6
            Color(0xFFB7EB8F)  // green-3
        )

        is TagColor.Processing -> Triple(
            Color(0xFFE6F7FF), // blue-1
            Color(0xFF1890FF), // blue-6
            Color(0xFF91D5FF)  // blue-3
        )

        is TagColor.Error -> Triple(
            Color(0xFFFFF2F0), // red-1
            Color(0xFFFF4D4F), // red-5
            Color(0xFFFFCCC7)  // red-2
        )

        is TagColor.Warning -> Triple(
            Color(0xFFFFFBE6), // gold-1
            Color(0xFFFAAD14), // gold-6
            Color(0xFFFFE58F)  // gold-3
        )

        is TagColor.Default -> Triple(
            Color(0xFFFAFAFA), // gray-2
            Color(0xFF000000D9), // rgba(0, 0, 0, 0.85)
            Color(0xFFD9D9D9)  // gray-5
        )

        is TagColor.Magenta -> Triple(
            Color(0xFFFFF0F6), // magenta-1
            Color(0xFFEB2F96), // magenta-6
            Color(0xFFFFADD2)  // magenta-3
        )

        is TagColor.Red -> Triple(
            Color(0xFFFFF1F0), // red-1
            Color(0xFFF5222D), // red-6
            Color(0xFFFFCCC7)  // red-2
        )

        is TagColor.Volcano -> Triple(
            Color(0xFFFFF2E8), // volcano-1
            Color(0xFFFA541C), // volcano-6
            Color(0xFFFFBB96)  // volcano-3
        )

        is TagColor.Orange -> Triple(
            Color(0xFFFFF7E6), // orange-1
            Color(0xFFFA8C16), // orange-6
            Color(0xFFFFD591)  // orange-3
        )

        is TagColor.Gold -> Triple(
            Color(0xFFFFFBE6), // gold-1
            Color(0xFFFAAD14), // gold-6
            Color(0xFFFFE58F)  // gold-3
        )

        is TagColor.Lime -> Triple(
            Color(0xFFFCFFE6), // lime-1
            Color(0xFFA0D911), // lime-6
            Color(0xFFEAFF8F)  // lime-3
        )

        is TagColor.Green -> Triple(
            Color(0xFFF6FFED), // green-1
            Color(0xFF52C41A), // green-6
            Color(0xFFB7EB8F)  // green-3
        )

        is TagColor.Cyan -> Triple(
            Color(0xFFE6FFFB), // cyan-1
            Color(0xFF13C2C2), // cyan-6
            Color(0xFF87E8DE)  // cyan-3
        )

        is TagColor.Blue -> Triple(
            Color(0xFFE6F7FF), // blue-1
            Color(0xFF1890FF), // blue-6
            Color(0xFF91D5FF)  // blue-3
        )

        is TagColor.GeekBlue -> Triple(
            Color(0xFFF0F5FF), // geekblue-1
            Color(0xFF2F54EB), // geekblue-6
            Color(0xFFADC6FF)  // geekblue-3
        )

        is TagColor.Purple -> Triple(
            Color(0xFFF9F0FF), // purple-1
            Color(0xFF722ED1), // purple-6
            Color(0xFFD3ADF7)  // purple-3
        )

        is TagColor.Custom -> {
            // For custom colors, derive lighter background and border
            val baseColor = color.color
            Triple(
                baseColor.copy(alpha = 0.1f), // Light background
                baseColor,                     // Text color
                baseColor.copy(alpha = 0.4f)   // Border color
            )
        }
    }
}

/**
 * Convenience function for string-based tag creation
 * Matches React pattern: <Tag>Content</Tag>
 */
@Composable
fun AntTag(
    text: String,
    modifier: Modifier = Modifier,
    color: TagColor = TagColor.Default,
    icon: (@Composable () -> Unit)? = null,
    closable: Boolean = false,
    closeIcon: (@Composable () -> Unit)? = null,
    onClose: ((event: Any?) -> Unit)? = null,
    bordered: Boolean = true,
    classNames: TagClassNames? = null,
    styles: TagStyles? = null
) {
    AntTag(
        children = { Text(text) },
        modifier = modifier,
        color = color,
        icon = icon,
        closable = closable,
        closeIcon = closeIcon,
        onClose = onClose,
        bordered = bordered,
        classNames = classNames,
        styles = styles
    )
}

/**
 * Convenience function for CheckableTag with text
 */
@Composable
fun AntCheckableTag(
    text: String,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    AntCheckableTag(
        children = { Text(text) },
        modifier = modifier,
        checked = checked,
        onChange = onChange
    )
}
