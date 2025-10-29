package com.antdesign.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * # Skeleton
 *
 * Provide a placeholder while you wait for content to load, or to visualize content that doesn't exist yet.
 *
 * ## When To Use
 *
 * - When a resource needs long time to load.
 * - When the component contains lots of information, such as List or Card.
 * - Only works when loading for the first time.
 * - Could be replaced by Spin in any situation, but can provide a better user experience.
 *
 * ## Complete API (React Ant Design v5.x Parity)
 *
 * ### Main Skeleton Props
 * - `active: Boolean = false` - Show animated gradient wave effect
 * - `avatar: Any = false` - Boolean or SkeletonAvatarProps configuration
 * - `loading: Boolean = true` - Show skeleton or children content
 * - `paragraph: Any = true` - Boolean or SkeletonParagraphProps configuration
 * - `title: Any = true` - Boolean or SkeletonTitleProps configuration
 * - `round: Boolean = false` - Show paragraph and title with rounded corners
 * - `classNames: SkeletonClassNames?` - Semantic class names (v5.7.0+)
 * - `styles: SkeletonStyles?` - Semantic style modifiers (v5.7.0+)
 * - `children: (@Composable () -> Unit)?` - Content to show when loading=false
 *
 * ### Avatar Configuration (SkeletonAvatarProps)
 * - `active: Boolean = false` - Animation for standalone avatar
 * - `shape: SkeletonAvatarShape = Circle` - Circle or Square
 * - `size: Any = Default` - Default, Large, Small, or custom Dp
 *
 * ### Title Configuration (SkeletonTitleProps)
 * - `width: Any? = null` - String percentage ("40%") or Float fraction (0.4f)
 *
 * ### Paragraph Configuration (SkeletonParagraphProps)
 * - `rows: Int = 3` - Number of paragraph rows
 * - `width: Any? = null` - List<Any> for per-row width, or Any for last row only
 *
 * ### Specialized Variants
 * - `AntSkeletonButton` - Button-shaped skeleton with size and shape options
 * - `AntSkeletonInput` - Input field skeleton with size options
 * - `AntSkeletonImage` - Image placeholder skeleton
 * - `AntSkeletonAvatar` - Standalone avatar skeleton
 * - `AntSkeletonNode` - Custom node skeleton with icon
 *
 * @see <a href="https://ant.design/components/skeleton">Ant Design Skeleton</a>
 */

/**
 * Ant Design Skeleton component with complete React v5.x parity
 *
 * Displays a loading placeholder for content. When loading is false, shows actual content.
 *
 * @param loading Display the skeleton when true, show children when false
 * @param modifier Modifier for the root container
 * @param active Show animation effect (gradient shimmer)
 * @param avatar Show avatar placeholder (Boolean or SkeletonAvatarProps)
 * @param title Show title placeholder (Boolean or SkeletonTitleProps)
 * @param paragraph Show paragraph placeholder (Boolean or SkeletonParagraphProps)
 * @param round Show paragraph and title radius when true
 * @param classNames Semantic class names for styling (v5.7.0+)
 * @param styles Semantic style modifiers (v5.7.0+)
 * @param children Content to display when loading is false
 *
 * @sample
 * ```kotlin
 * // Basic skeleton
 * AntSkeleton(loading = true)
 *
 * // With content toggle
 * AntSkeleton(loading = isLoading) {
 *     Text("Loaded content")
 * }
 *
 * // Custom configuration
 * AntSkeleton(
 *     loading = true,
 *     active = true,
 *     avatar = SkeletonAvatarProps(shape = SkeletonAvatarShape.Square, size = 64.dp),
 *     title = SkeletonTitleProps(width = "60%"),
 *     paragraph = SkeletonParagraphProps(
 *         rows = 4,
 *         width = listOf("100%", "100%", "80%", "60%")
 *     )
 * )
 * ```
 */
@Composable
fun AntSkeleton(
    loading: Boolean = true,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    avatar: Any = false,
    title: Any = true,
    paragraph: Any = true,
    round: Boolean = false,
    classNames: SkeletonClassNames? = null,
    styles: SkeletonStyles? = null,
    children: (@Composable () -> Unit)? = null
) {
    if (loading) {
        // Parse avatar configuration
        val avatarProps = when (avatar) {
            is Boolean -> if (avatar) SkeletonAvatarProps() else null
            is SkeletonAvatarProps -> avatar
            else -> null
        }

        // Parse title configuration
        val titleProps = when (title) {
            is Boolean -> if (title) SkeletonTitleProps() else null
            is SkeletonTitleProps -> title
            else -> null
        }

        // Parse paragraph configuration
        val paragraphProps = when (paragraph) {
            is Boolean -> if (paragraph) SkeletonParagraphProps() else null
            is SkeletonParagraphProps -> paragraph
            else -> null
        }

        // Apply root styles
        val rootModifier = modifier.then(styles?.root ?: Modifier)

        Row(
            modifier = rootModifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar skeleton
            if (avatarProps != null) {
                SkeletonAvatarElement(
                    props = avatarProps,
                    active = active,
                    modifier = styles?.avatar ?: Modifier
                )
            }

            // Title and paragraph column
            if (titleProps != null || paragraphProps != null) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title skeleton
                    if (titleProps != null) {
                        SkeletonTitleElement(
                            props = titleProps,
                            active = active,
                            round = round,
                            modifier = styles?.title ?: Modifier
                        )
                    }

                    // Paragraph skeleton
                    if (paragraphProps != null) {
                        SkeletonParagraphElement(
                            props = paragraphProps,
                            active = active,
                            round = round,
                            modifier = styles?.paragraph ?: Modifier
                        )
                    }
                }
            }
        }
    } else {
        // Show actual content when not loading
        children?.invoke()
    }
}

/**
 * Internal avatar skeleton element
 */
@Composable
private fun SkeletonAvatarElement(
    props: SkeletonAvatarProps,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val size = when (val s = props.size) {
        is Dp -> s
        is AvatarSize -> when (s) {
            AvatarSize.Small -> 24.dp
            AvatarSize.Default -> 40.dp
            AvatarSize.Large -> 48.dp
        }
        is String -> {
            // Parse string like "large", "small", "default"
            when (s.lowercase()) {
                "small" -> 24.dp
                "large" -> 48.dp
                else -> 40.dp
            }
        }
        is Number -> s.toFloat().dp
        else -> 40.dp
    }

    val shape = when (props.shape) {
        SkeletonAvatarShape.Circle -> CircleShape
        SkeletonAvatarShape.Square -> RoundedCornerShape(4.dp)
    }

    SkeletonElement(
        modifier = modifier
            .size(size)
            .clip(shape),
        active = active || props.active
    )
}

/**
 * Internal title skeleton element
 */
@Composable
private fun SkeletonTitleElement(
    props: SkeletonTitleProps,
    active: Boolean,
    round: Boolean,
    modifier: Modifier = Modifier
) {
    val width = parseWidth(props.width, defaultValue = 0.4f)
    val shape = if (round) RoundedCornerShape(100.dp) else RoundedCornerShape(4.dp)

    SkeletonElement(
        modifier = modifier
            .fillMaxWidth(width)
            .height(16.dp)
            .clip(shape),
        active = active
    )
}

/**
 * Internal paragraph skeleton element
 */
@Composable
private fun SkeletonParagraphElement(
    props: SkeletonParagraphProps,
    active: Boolean,
    round: Boolean,
    modifier: Modifier = Modifier
) {
    val rows = props.rows ?: 3
    val shape = if (round) RoundedCornerShape(100.dp) else RoundedCornerShape(4.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(rows) { index ->
            val rowWidth = when (val w = props.width) {
                is List<*> -> {
                    // Per-row width specified
                    if (index < w.size) {
                        parseWidth(w[index], defaultValue = 1f)
                    } else {
                        1f
                    }
                }
                null -> {
                    // Default: last row is 60%, others are 100%
                    if (index == rows - 1) 0.6f else 1f
                }
                else -> {
                    // Single width applies to last row only
                    if (index == rows - 1) {
                        parseWidth(w, defaultValue = 0.6f)
                    } else {
                        1f
                    }
                }
            }

            SkeletonElement(
                modifier = Modifier
                    .fillMaxWidth(rowWidth)
                    .height(14.dp)
                    .clip(shape),
                active = active
            )
        }
    }
}

/**
 * Core skeleton element with optional shimmer animation
 */
@Composable
private fun SkeletonElement(
    modifier: Modifier = Modifier,
    active: Boolean = false
) {
    val theme = useTheme()
    val baseColor = ColorPalette.gray4 // #F0F0F0

    if (active) {
        // Animated shimmer effect
        val infiniteTransition = rememberInfiniteTransition(label = "skeleton_shimmer")
        val offset by infiniteTransition.animateFloat(
            initialValue = -1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_offset"
        )

        Box(
            modifier = modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        baseColor,
                        ColorPalette.gray3, // Lighter highlight
                        baseColor
                    ),
                    start = Offset(offset * 1000f, offset * 1000f),
                    end = Offset((offset + 0.5f) * 1000f, (offset + 0.5f) * 1000f)
                )
            )
        )
    } else {
        // Static skeleton
        Box(
            modifier = modifier.background(baseColor)
        )
    }
}

/**
 * Parse width from various formats: String percentage, Float fraction, Int pixels
 */
private fun parseWidth(width: Any?, defaultValue: Float): Float {
    return when (width) {
        is String -> {
            // Parse percentage like "40%" or "0.4"
            val cleaned = width.trim().removeSuffix("%")
            cleaned.toFloatOrNull()?.let {
                if (width.contains("%")) it / 100f else it
            } ?: defaultValue
        }
        is Float -> width.coerceIn(0f, 1f)
        is Double -> width.toFloat().coerceIn(0f, 1f)
        is Int -> (width / 100f).coerceIn(0f, 1f)
        null -> defaultValue
        else -> defaultValue
    }
}

// ============================================================================
// Skeleton Button - Specialized button-shaped skeleton
// ============================================================================

/**
 * Skeleton Button - Button-shaped skeleton placeholder
 *
 * @param modifier Modifier for the skeleton
 * @param active Show animation effect
 * @param size Button size (Small, Middle, Large)
 * @param shape Button shape (Default, Circle, Round, Square)
 * @param block Fit button width to parent width
 *
 * @sample
 * ```kotlin
 * AntSkeletonButton(
 *     active = true,
 *     size = ButtonSize.Large,
 *     shape = SkeletonButtonShape.Round
 * )
 * ```
 */
@Composable
fun AntSkeletonButton(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    size: ButtonSize = ButtonSize.Middle,
    shape: SkeletonButtonShape = SkeletonButtonShape.Default,
    block: Boolean = false
) {
    val height = when (size) {
        ButtonSize.Small -> 24.dp
        ButtonSize.Middle -> 32.dp
        ButtonSize.Large -> 40.dp
    }

    val width = if (block) null else when (size) {
        ButtonSize.Small -> 60.dp
        ButtonSize.Middle -> 80.dp
        ButtonSize.Large -> 100.dp
    }

    val skeletonShape = when (shape) {
        SkeletonButtonShape.Circle -> CircleShape
        SkeletonButtonShape.Round -> RoundedCornerShape(height / 2)
        SkeletonButtonShape.Square -> RoundedCornerShape(0.dp)
        SkeletonButtonShape.Default -> RoundedCornerShape(6.dp)
    }

    SkeletonElement(
        modifier = modifier
            .then(if (block) Modifier.fillMaxWidth() else Modifier.width(width ?: 80.dp))
            .height(height)
            .clip(skeletonShape),
        active = active
    )
}

// ============================================================================
// Skeleton Input - Specialized input field skeleton
// ============================================================================

/**
 * Skeleton Input - Input field skeleton placeholder
 *
 * @param modifier Modifier for the skeleton
 * @param active Show animation effect
 * @param size Input size (Small, Middle, Large)
 * @param block Fit input width to parent width
 *
 * @sample
 * ```kotlin
 * AntSkeletonInput(
 *     active = true,
 *     size = InputSize.Large
 * )
 * ```
 */
@Composable
fun AntSkeletonInput(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    size: InputSize = InputSize.Middle,
    block: Boolean = true
) {
    val height = when (size) {
        InputSize.Small -> 24.dp
        InputSize.Middle -> 32.dp
        InputSize.Large -> 40.dp
    }

    SkeletonElement(
        modifier = modifier
            .then(if (block) Modifier.fillMaxWidth() else Modifier.width(200.dp))
            .height(height)
            .clip(RoundedCornerShape(6.dp)),
        active = active
    )
}

// ============================================================================
// Skeleton Avatar - Standalone avatar skeleton
// ============================================================================

/**
 * Skeleton Avatar - Standalone avatar skeleton placeholder
 *
 * @param modifier Modifier for the skeleton
 * @param active Show animation effect
 * @param size Avatar size (Small, Default, Large, or custom Dp)
 * @param shape Avatar shape (Circle or Square)
 *
 * @sample
 * ```kotlin
 * AntSkeletonAvatar(
 *     active = true,
 *     size = AvatarSize.Large,
 *     shape = SkeletonAvatarShape.Square
 * )
 * ```
 */
@Composable
fun AntSkeletonAvatar(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    size: Any = AvatarSize.Default,
    shape: SkeletonAvatarShape = SkeletonAvatarShape.Circle
) {
    SkeletonAvatarElement(
        props = SkeletonAvatarProps(
            active = active,
            shape = shape,
            size = size
        ),
        active = false, // Active is handled by props.active
        modifier = modifier
    )
}

// ============================================================================
// Skeleton Image - Image placeholder skeleton
// ============================================================================

/**
 * Skeleton Image - Image placeholder skeleton
 *
 * @param modifier Modifier for the skeleton
 * @param active Show animation effect
 *
 * @sample
 * ```kotlin
 * AntSkeletonImage(active = true)
 * ```
 */
@Composable
fun AntSkeletonImage(
    modifier: Modifier = Modifier,
    active: Boolean = false
) {
    SkeletonElement(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(4.dp)),
        active = active
    )
}

// ============================================================================
// Skeleton Node - Custom node skeleton with icon
// ============================================================================

/**
 * Skeleton Node - Custom node skeleton with optional icon
 *
 * Allows creating custom skeleton placeholders with arbitrary content and icons.
 *
 * @param modifier Modifier for the skeleton
 * @param active Show animation effect
 * @param children Custom content to display on skeleton
 *
 * @sample
 * ```kotlin
 * AntSkeletonNode(active = true) {
 *     Icon(Icons.Default.Image, contentDescription = null)
 * }
 * ```
 */
@Composable
fun AntSkeletonNode(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    children: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        SkeletonElement(
            modifier = Modifier.matchParentSize(),
            active = active
        )

        if (children != null) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                children()
            }
        }
    }
}

// ============================================================================
// Configuration Data Classes
// ============================================================================

/**
 * Avatar configuration for Skeleton
 *
 * @property active Show animation effect (only for standalone avatar)
 * @property shape Avatar shape (Circle or Square)
 * @property size Avatar size (Default, Large, Small, or custom Dp)
 */
data class SkeletonAvatarProps(
    val active: Boolean = false,
    val shape: SkeletonAvatarShape = SkeletonAvatarShape.Circle,
    val size: Any = AvatarSize.Default
)

/**
 * Title configuration for Skeleton
 *
 * @property width Title width (String percentage like "40%" or Float fraction like 0.4f)
 */
data class SkeletonTitleProps(
    val width: Any? = null // String percentage or Float fraction
)

/**
 * Paragraph configuration for Skeleton
 *
 * @property rows Number of paragraph rows
 * @property width Width configuration (List for per-row, single value for last row only)
 */
data class SkeletonParagraphProps(
    val rows: Int? = 3,
    val width: Any? = null // List<Any> for per-row width, or Any for last row only
)

/**
 * Semantic class names for Skeleton styling (v5.7.0+)
 *
 * @property root Class name for root element
 * @property avatar Class name for avatar element
 * @property title Class name for title element
 * @property paragraph Class name for paragraph element
 * @property button Class name for button element
 * @property input Class name for input element
 * @property image Class name for image element
 */
data class SkeletonClassNames(
    val root: String = "",
    val avatar: String = "",
    val title: String = "",
    val paragraph: String = "",
    val button: String = "",
    val input: String = "",
    val image: String = ""
)

/**
 * Semantic styles for Skeleton customization (v5.7.0+)
 *
 * @property root Modifier for root element
 * @property avatar Modifier for avatar element
 * @property title Modifier for title element
 * @property paragraph Modifier for paragraph element
 * @property button Modifier for button element
 * @property input Modifier for input element
 * @property image Modifier for image element
 */
data class SkeletonStyles(
    val root: Modifier = Modifier,
    val avatar: Modifier = Modifier,
    val title: Modifier = Modifier,
    val paragraph: Modifier = Modifier,
    val button: Modifier = Modifier,
    val input: Modifier = Modifier,
    val image: Modifier = Modifier
)

// ============================================================================
// Enums
// ============================================================================

/**
 * Avatar shape for Skeleton
 */
enum class SkeletonAvatarShape {
    Circle,
    Square
}

/**
 * Button shape for Skeleton Button
 */
enum class SkeletonButtonShape {
    Default,
    Circle,
    Round,
    Square
}
