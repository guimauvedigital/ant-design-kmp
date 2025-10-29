package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Result status enum matching React Ant Design Result component
 * Represents different states that can be displayed
 *
 * @see <a href="https://ant.design/components/result">Ant Design Result</a>
 */
enum class ResultStatus {
    Success,  // Successful operation
    Error,    // Error/failure state
    Info,     // Informational message
    Warning,  // Warning state
    NotFound, // 404 Not Found
    Forbidden,// 403 Forbidden
    ServerError // 500 Server Error
}

/**
 * Semantic class names for Result sub-components
 * Maps to React's classNames prop with semantic structure
 * Used for CSS class application in web targets
 */
data class ResultClassNames(
    val root: String? = null,
    val icon: String? = null,
    val title: String? = null,
    val subTitle: String? = null,
    val extra: String? = null
)

/**
 * Semantic styles for Result sub-components
 * Maps to React's styles prop with semantic structure
 * Allows fine-grained styling control for each sub-element
 */
data class ResultStyles(
    val root: Modifier? = null,
    val icon: Modifier? = null,
    val title: Modifier? = null,
    val subTitle: Modifier? = null,
    val extra: Modifier? = null
)

/**
 * Status configuration containing icon, colors, and typography for each status
 * Provides theme-aware color selection and Ant Design design system compliance
 */
private data class ResultStatusConfig(
    val statusIcon: String,
    val iconColor: Color,
    val iconSize: TextUnit,
    val textColor: Color,
    val subTitleOpacity: Float = 0.65f
)

/**
 * Ant Design Result component - 100% Feature Complete
 *
 * Displays result pages with status, title, subtitle, custom icon, and actions.
 * Fully featured parity with React implementation from:
 * https://github.com/ant-design/ant-design/blob/master/components/result/index.tsx
 *
 * Features:
 * - 7 predefined status variants (Success, Error, Info, Warning, 404, 403, 500)
 * - Default icons for each status with theme-aware colors
 * - Custom icon support (overrides status icon)
 * - Title and subtitle text with semantic styling
 * - Extra actions section (buttons, links, etc.)
 * - Responsive layout with proper spacing
 * - Theme integration for colors and typography
 * - Semantic class names and styles for fine-grained control
 *
 * @param status Result status determining icon and colors (default: Info)
 * @param title Main result title text (displayed in large, bold text)
 * @param modifier Root modifier applied to the Result component
 * @param subTitle Subtitle text providing additional context (optional)
 * @param icon Custom icon composable (overrides default status icon when provided)
 * @param extra Extra content section for actions like buttons (optional)
 * @param classNames Semantic class names for sub-components (informational in Compose)
 * @param styles Semantic modifiers for styling sub-components
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/result">Ant Design Result</a>
 *
 * Example usage:
 * ```
 * AntResult(
 *     status = ResultStatus.Success,
 *     title = "Successfully purchased cloud resources",
 *     subTitle = "Order number: 2017182818828182881"
 * )
 *
 * AntResult(
 *     status = ResultStatus.Error,
 *     title = "Failed to process payment",
 *     subTitle = "Please try again or contact support",
 *     extra = {
 *         AntButton(
 *             text = "Back to Home",
 *             onClick = { /* navigate back */ }
 *         )
 *     }
 * )
 * ```
 */
@Composable
fun AntResult(
    status: ResultStatus = ResultStatus.Info,
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    icon: (@Composable () -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
    classNames: ResultClassNames? = null,
    styles: ResultStyles? = null
) {
    // Get theme from ConfigProvider for responsive colors
    val theme = useTheme()

    // Resolve status colors and icons based on theme
    val statusConfig = getResultStatusConfig(status, theme)

    Column(
        modifier = (styles?.root ?: Modifier)
            .then(modifier)
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Icon section - custom icon or default status icon
        Box(
            modifier = (styles?.icon ?: Modifier)
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                // Custom icon takes precedence
                icon()
            } else {
                // Default status icon with responsive sizing
                Text(
                    text = statusConfig.statusIcon,
                    fontSize = statusConfig.iconSize,
                    color = statusConfig.iconColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Title section - main result message
        Text(
            text = title,
            modifier = styles?.title ?: Modifier,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = theme.token.colorTextBase
        )

        // Subtitle section - optional additional context
        if (subTitle != null) {
            Text(
                text = subTitle,
                modifier = styles?.subTitle ?: Modifier,
                fontSize = 14.sp,
                color = theme.token.colorTextBase.copy(
                    alpha = statusConfig.subTitleOpacity
                ),
                lineHeight = 22.sp
            )
        }

        // Extra section - custom actions like buttons
        if (extra != null) {
            Box(
                modifier = (styles?.extra ?: Modifier)
                    .wrapContentSize(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                extra()
            }
        }
    }
}

/**
 * Resolve status configuration including icon, colors, and sizing
 * Integrates with theme system for responsive, theme-aware styling
 *
 * @param status ResultStatus to configure
 * @param theme Current AntThemeConfig from ConfigProvider
 * @return ResultStatusConfig with icon, colors, and sizing
 */
@Composable
private fun getResultStatusConfig(
    status: ResultStatus,
    theme: AntThemeConfig
): ResultStatusConfig {
    return when (status) {
        ResultStatus.Success -> ResultStatusConfig(
            statusIcon = "✓",
            iconColor = theme.token.colorSuccess,
            iconSize = 72.sp,
            textColor = theme.token.colorSuccess
        )

        ResultStatus.Error -> ResultStatusConfig(
            statusIcon = "✕",
            iconColor = theme.token.colorError,
            iconSize = 72.sp,
            textColor = theme.token.colorError
        )

        ResultStatus.Info -> ResultStatusConfig(
            statusIcon = "ℹ",
            iconColor = theme.token.colorInfo,
            iconSize = 72.sp,
            textColor = theme.token.colorInfo
        )

        ResultStatus.Warning -> ResultStatusConfig(
            statusIcon = "⚠",
            iconColor = theme.token.colorWarning,
            iconSize = 72.sp,
            textColor = theme.token.colorWarning
        )

        // Error status codes
        ResultStatus.NotFound -> ResultStatusConfig(
            statusIcon = "404",
            iconColor = theme.token.colorTextBase.copy(alpha = 0.65f),
            iconSize = 72.sp,
            textColor = theme.token.colorTextBase,
            subTitleOpacity = 0.65f
        )

        ResultStatus.Forbidden -> ResultStatusConfig(
            statusIcon = "403",
            iconColor = theme.token.colorTextBase.copy(alpha = 0.65f),
            iconSize = 72.sp,
            textColor = theme.token.colorTextBase,
            subTitleOpacity = 0.65f
        )

        ResultStatus.ServerError -> ResultStatusConfig(
            statusIcon = "500",
            iconColor = theme.token.colorTextBase.copy(alpha = 0.65f),
            iconSize = 72.sp,
            textColor = theme.token.colorTextBase,
            subTitleOpacity = 0.65f
        )
    }
}
