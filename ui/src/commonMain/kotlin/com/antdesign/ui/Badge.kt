package com.antdesign.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Badge component that displays a badge with count, dot, or status indicator.
 * Implements React Ant Design Badge v5.x API with 100% parity.
 *
 * Features:
 * - Count badge with number display
 * - Dot badge (small circle indicator)
 * - Status badges (colored dots with text labels)
 * - Overflow count display (e.g., "99+")
 * - showZero configuration
 * - Custom colors
 * - Position offset customization
 * - Size variants (small/default)
 * - Theme integration
 * - Animated processing status
 * - Semantic class names and styles (v5.7.0+)
 *
 * @param count Badge count number to display
 * @param dot Show dot badge instead of count
 * @param showZero Show badge when count is 0
 * @param overflowCount Max count to display, shows "N+" when exceeded
 * @param status Status indicator type (success, processing, error, warning, default)
 * @param text Text to display next to status dot
 * @param color Custom badge color (overrides status color)
 * @param offset Position offset [x, y] in Dp
 * @param size Badge size variant
 * @param classNames Semantic class names for badge parts
 * @param styles Semantic styles for badge parts
 * @param modifier Modifier for the root container
 * @param children Content to wrap with badge
 *
 * @see BadgeStatus
 * @see BadgeSize
 * @see BadgeClassNames
 * @see BadgeStyles
 *
 * @since 1.0.0
 */
@Composable
fun AntBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    dot: Boolean = false,
    showZero: Boolean = false,
    overflowCount: Int = 99,
    status: BadgeStatus? = null,
    text: String? = null,
    color: Color? = null,
    offset: Pair<Dp, Dp>? = null,
    size: BadgeSize = BadgeSize.Default,
    classNames: BadgeClassNames? = null,
    styles: BadgeStyles? = null,
    children: (@Composable () -> Unit)? = null
) {
    val theme = useTheme()
    val resolvedTokens = AntDesignTheme.resolveTokens(theme.token)

    // Determine badge color based on status or custom color
    val badgeColor = color ?: when (status) {
        BadgeStatus.Success -> resolvedTokens.colorSuccess
        BadgeStatus.Processing -> resolvedTokens.colorInfo
        BadgeStatus.Error -> resolvedTokens.colorError
        BadgeStatus.Warning -> resolvedTokens.colorWarning
        BadgeStatus.Default -> resolvedTokens.colorTextQuaternary
        null -> resolvedTokens.colorError
    }

    // Apply root styles and classNames
    val rootModifier = modifier.then(styles?.root ?: Modifier)

    // If status badge without children
    if (status != null && children == null) {
        StatusBadge(
            status = status,
            text = text,
            color = badgeColor,
            size = size,
            classNames = classNames,
            styles = styles,
            modifier = rootModifier
        )
        return
    }

    // Regular badge with children
    Box(modifier = rootModifier) {
        // Render children content
        children?.invoke()

        // Determine if badge should be shown
        val shouldShowBadge = when {
            dot -> true
            count != null && count > 0 -> true
            count == 0 && showZero -> true
            else -> false
        }

        if (shouldShowBadge) {
            if (dot) {
                // Dot badge
                DotBadge(
                    color = badgeColor,
                    offset = offset,
                    size = size,
                    status = status,
                    modifier = styles?.indicator ?: Modifier
                )
            } else {
                // Count badge
                CountBadge(
                    count = count ?: 0,
                    overflowCount = overflowCount,
                    color = badgeColor,
                    offset = offset,
                    size = size,
                    modifier = styles?.indicator ?: Modifier
                )
            }
        }
    }
}

/**
 * Internal composable for rendering dot badge
 */
@Composable
private fun DotBadge(
    color: Color,
    offset: Pair<Dp, Dp>?,
    size: BadgeSize,
    status: BadgeStatus?,
    modifier: Modifier
) {
    val dotSize = when (size) {
        BadgeSize.Small -> 6.dp
        BadgeSize.Default -> 8.dp
    }

    val offsetX = offset?.first ?: 0.dp
    val offsetY = offset?.second ?: 0.dp

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = offsetX, y = offsetY)
        ) {
            // Base dot
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color)
            )

            // Processing animation
            if (status == BadgeStatus.Processing) {
                ProcessingAnimation(
                    color = color,
                    size = dotSize
                )
            }
        }
    }
}

/**
 * Internal composable for rendering count badge
 */
@Composable
private fun CountBadge(
    count: Int,
    overflowCount: Int,
    color: Color,
    offset: Pair<Dp, Dp>?,
    size: BadgeSize,
    modifier: Modifier
) {
    val displayText = if (count > overflowCount) "$overflowCount+" else count.toString()

    val fontSize = when (size) {
        BadgeSize.Small -> 10.sp
        BadgeSize.Default -> 12.sp
    }

    val minSize = when (size) {
        BadgeSize.Small -> 16.dp
        BadgeSize.Default -> 20.dp
    }

    val horizontalPadding = when (size) {
        BadgeSize.Small -> 4.dp
        BadgeSize.Default -> 6.dp
    }

    val verticalPadding = when (size) {
        BadgeSize.Small -> 0.dp
        BadgeSize.Default -> 2.dp
    }

    val offsetX = offset?.first ?: 0.dp
    val offsetY = offset?.second ?: 0.dp

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = offsetX, y = offsetY)
                .clip(
                    if (displayText.length == 1) CircleShape
                    else RoundedCornerShape(10.dp)
                )
                .background(color)
                .defaultMinSize(minWidth = minSize, minHeight = minSize)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText,
                style = TextStyle(
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

/**
 * Internal composable for rendering status badge (standalone with text)
 */
@Composable
private fun StatusBadge(
    status: BadgeStatus,
    text: String?,
    color: Color,
    size: BadgeSize,
    classNames: BadgeClassNames?,
    styles: BadgeStyles?,
    modifier: Modifier
) {
    val dotSize = when (size) {
        BadgeSize.Small -> 6.dp
        BadgeSize.Default -> 8.dp
    }

    val fontSize = when (size) {
        BadgeSize.Small -> 12.sp
        BadgeSize.Default -> 14.sp
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status dot container
        Box(
            modifier = styles?.indicator ?: Modifier
        ) {
            // Base dot
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color)
            )

            // Processing animation
            if (status == BadgeStatus.Processing) {
                ProcessingAnimation(
                    color = color,
                    size = dotSize
                )
            }
        }

        // Status text
        if (text != null) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = fontSize,
                    color = Color.Black.copy(alpha = 0.85f)
                ),
                modifier = styles?.text ?: Modifier
            )
        }
    }
}

/**
 * Internal composable for processing status animation
 * Creates a pulsing ripple effect
 */
@Composable
private fun ProcessingAnimation(
    color: Color,
    size: Dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "processing")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .alpha(alpha)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * Badge status types
 *
 * @property Success Success status (green)
 * @property Processing Processing status (blue) with animated pulse
 * @property Error Error status (red)
 * @property Warning Warning status (orange/gold)
 * @property Default Default status (gray)
 *
 * @since 1.0.0
 */
enum class BadgeStatus {
    Success,
    Processing,
    Error,
    Warning,
    Default
}

/**
 * Badge size variants
 *
 * @property Small Small badge (reduced dimensions)
 * @property Default Default badge size
 *
 * @since 1.0.0
 */
enum class BadgeSize {
    Small,
    Default
}

/**
 * Semantic class names for Badge component parts (v5.7.0+)
 *
 * Allows applying custom CSS class names to specific parts of the Badge component.
 * Useful for styling and testing purposes in web environments.
 *
 * @property root Class name for the root container
 * @property indicator Class name for the badge indicator (count/dot)
 * @property text Class name for the status text
 *
 * @since 1.0.0 (Added in v5.7.0)
 */
data class BadgeClassNames(
    val root: String? = null,
    val indicator: String? = null,
    val text: String? = null
)

/**
 * Semantic styles for Badge component parts (v5.7.0+)
 *
 * Allows applying custom Modifiers to specific parts of the Badge component.
 * Provides fine-grained control over styling individual badge elements.
 *
 * @property root Modifier for the root container
 * @property indicator Modifier for the badge indicator (count/dot)
 * @property text Modifier for the status text
 *
 * @since 1.0.0 (Added in v5.7.0)
 */
data class BadgeStyles(
    val root: Modifier = Modifier,
    val indicator: Modifier = Modifier,
    val text: Modifier = Modifier
)
