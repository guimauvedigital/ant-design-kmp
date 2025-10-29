package digital.guimauve.antdesign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Empty state component for displaying empty content with customizable image, description, and actions.
 * Provides 100% React Ant Design parity with image variants and theme integration.
 *
 * @param modifier Modifier for the container
 * @param image Custom image composable, URL string, or EmptyImage enum variant
 * @param description Custom description text or composable
 * @param imageStyle Style for the image (deprecated, use image param instead)
 * @param children Custom action buttons or content below the empty state
 */
@Composable
fun AntEmpty(
    modifier: Modifier = Modifier,
    image: Any? = null,
    description: Any? = null,
    imageStyle: EmptyImageStyle = EmptyImageStyle.Default,
    children: (@Composable () -> Unit)? = null,
) {
    val theme = useTheme()
    val resolvedTheme = AntDesignTheme.resolveTokens(theme.token)

    // Resolve description - can be String or @Composable
    val descriptionText = when (description) {
        is String -> description
        null -> "No Data"
        else -> null
    }
    val descriptionComposable = description as? (@Composable () -> Unit)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = theme.token.marginXXL,
                horizontal = theme.token.marginXL
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(theme.token.marginLG)
    ) {
        // Image section
        RenderEmptyImage(
            image = image,
            imageStyle = imageStyle,
            theme = theme,
            resolvedTheme = resolvedTheme
        )

        // Description section
        if (descriptionComposable != null) {
            descriptionComposable()
        } else if (descriptionText != null) {
            Text(
                text = descriptionText,
                fontSize = theme.token.fontSize.sp,
                color = resolvedTheme.colorTextSecondary,
                style = TextStyle(
                    letterSpacing = 0.sp
                )
            )
        }

        // Actions section
        if (children != null) {
            Box(
                modifier = Modifier.padding(top = theme.token.marginMD)
            ) {
                children()
            }
        }
    }
}

/**
 * Renders the image content based on type: composable, URL, or EmptyImage enum
 */
@Composable
private fun RenderEmptyImage(
    image: Any?,
    imageStyle: EmptyImageStyle,
    theme: AntThemeConfig,
    resolvedTheme: ResolvedThemeToken,
) {
    when (image) {
        // EmptyImage enum
        is EmptyImage -> {
            RenderEmptyImageVariant(
                variant = image,
                theme = theme,
                resolvedTheme = resolvedTheme
            )
        }

        // String URL (future image loading support)
        is String -> {
            // Placeholder for image loading from URL
            Text(
                text = "🖼️",
                fontSize = 64.sp,
                color = resolvedTheme.colorTextTertiary
            )
        }

        // Custom composable function
        is Function0<*> -> {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                @Suppress("UNCHECKED_CAST")
                val composable = image as @Composable () -> Unit
                composable()
            }
        }

        // Default icons based on imageStyle enum
        else -> {
            RenderDefaultEmptyIcon(
                imageStyle = imageStyle,
                resolvedTheme = resolvedTheme
            )
        }
    }
}

/**
 * Renders specific EmptyImage variant with detailed illustrations
 */
@Composable
private fun RenderEmptyImageVariant(
    variant: EmptyImage,
    theme: AntThemeConfig,
    resolvedTheme: ResolvedThemeToken,
) {
    when (variant) {
        EmptyImage.Simple -> {
            // Minimal icon - simple empty state
            Text(
                text = "□",
                fontSize = 72.sp,
                color = resolvedTheme.colorTextTertiary.copy(alpha = 0.45f)
            )
        }

        EmptyImage.Default, EmptyImage.Presented -> {
            // Rich illustration with more visual appeal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(width = 120.dp, height = 100.dp)
            ) {
                // Top part - empty box outline
                Text(
                    text = "📦",
                    fontSize = 80.sp,
                    color = resolvedTheme.colorTextTertiary.copy(alpha = 0.35f)
                )
            }
        }
    }
}

/**
 * Renders default empty icon based on EmptyImageStyle enum
 */
@Composable
private fun RenderDefaultEmptyIcon(
    imageStyle: EmptyImageStyle,
    resolvedTheme: ResolvedThemeToken,
) {
    when (imageStyle) {
        EmptyImageStyle.Simple -> {
            Text(
                text = "□",
                fontSize = 72.sp,
                color = resolvedTheme.colorTextTertiary.copy(alpha = 0.45f)
            )
        }

        EmptyImageStyle.Default -> {
            Text(
                text = "📦",
                fontSize = 80.sp,
                color = resolvedTheme.colorTextTertiary.copy(alpha = 0.35f)
            )
        }
    }
}

/**
 * EmptyImage enum provides predefined empty state image variants
 * Aligned with React Ant Design's Empty.PRESENTED_IMAGE_* constants
 *
 * Simple: Minimal UI empty state
 * Default: Standard empty state (equivalent to Ant Design default)
 * Presented: Rich, detailed illustration for prominent empty states
 */
enum class EmptyImage {
    /**
     * Simple variant - minimal empty state with small icon
     * Use when space is limited or for subtle empty states
     */
    Simple,

    /**
     * Default variant - standard Ant Design empty state
     * Most commonly used, provides good visual balance
     */
    Default,

    /**
     * Presented variant - detailed, rich illustration
     * Use for prominent empty states that need visual emphasis
     */
    Presented
}

/**
 * EmptyImageStyle enum (deprecated) - use EmptyImage instead
 * Kept for backward compatibility
 */
enum class EmptyImageStyle {
    Simple,
    Default
}

/**
 * AntEmpty composable shorthand for common empty state patterns
 *
 * Example usage:
 * ```
 * AntEmptyDefault(description = "No items found")
 * AntEmptySimple()
 * AntEmptyCustom(
 *     image = EmptyImage.Presented,
 *     description = "Custom message",
 *     children = { AntButton(text = "Add Item") }
 * )
 * ```
 */

/**
 * Default empty state with standard icon and customizable description
 */
@Composable
fun AntEmptyDefault(
    modifier: Modifier = Modifier,
    description: String = "No Data",
    children: (@Composable () -> Unit)? = null,
) {
    AntEmpty(
        modifier = modifier,
        image = EmptyImage.Default,
        description = description,
        children = children
    )
}

/**
 * Simple empty state with minimal icon for compact layouts
 */
@Composable
fun AntEmptySimple(
    modifier: Modifier = Modifier,
    description: String = "No Data",
    children: (@Composable () -> Unit)? = null,
) {
    AntEmpty(
        modifier = modifier,
        image = EmptyImage.Simple,
        description = description,
        children = children
    )
}

/**
 * Presented empty state with rich illustration for prominent empty states
 */
@Composable
fun AntEmptyPresented(
    modifier: Modifier = Modifier,
    description: String = "No Data",
    children: (@Composable () -> Unit)? = null,
) {
    AntEmpty(
        modifier = modifier,
        image = EmptyImage.Presented,
        description = description,
        children = children
    )
}

/**
 * Custom empty state with full configuration options
 */
@Composable
fun AntEmptyCustom(
    modifier: Modifier = Modifier,
    image: Any? = null,
    description: Any? = null,
    children: (@Composable () -> Unit)? = null,
) {
    AntEmpty(
        modifier = modifier,
        image = image,
        description = description,
        children = children
    )
}
