package com.antdesign.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow

/**
 * Theme utilities and token management for Ant Design components.
 * Provides default theme, dark theme, and compact theme configurations with full React Ant Design parity.
 *
 * Features:
 * - Theme algorithms (default, dark, compact)
 * - Token system (colors, spacing, typography)
 * - Component-specific tokens
 * - Theme extension and customization
 * - Seed token generation
 * - Alias token resolution
 * - Theme merging logic
 */

object AntDesignTheme {
    /**
     * Default theme with standard Ant Design colors and tokens
     */
    val defaultTheme = AntThemeConfig(
        token = ThemeToken(
            colorPrimary = Color(0xFF1890FF),
            colorSuccess = Color(0xFF52C41A),
            colorWarning = Color(0xFFFAAD14),
            colorError = Color(0xFFFF4D4F),
            colorInfo = Color(0xFF1890FF),
            colorTextBase = Color(0xFF000000),
            colorBgBase = Color(0xFFFFFFFF)
        ),
        algorithm = ThemeAlgorithm.DefaultAlgorithm
    )

    /**
     * Dark theme with inverted colors for dark mode
     * Implements the Dark Algorithm from React Ant Design
     */
    val darkTheme = AntThemeConfig(
        token = ThemeToken(
            colorPrimary = Color(0xFF177DDC),
            colorSuccess = Color(0xFF49AA19),
            colorWarning = Color(0xFFD89614),
            colorError = Color(0xFFD32029),
            colorInfo = Color(0xFF177DDC),
            colorTextBase = Color(0xFFFFFFFF),
            colorBgBase = Color(0xFF141414)
        ),
        algorithm = ThemeAlgorithm.DarkAlgorithm
    )

    /**
     * Compact theme with reduced spacing and sizes
     * Implements the Compact Algorithm from React Ant Design
     */
    val compactTheme = AntThemeConfig(
        token = ThemeToken(
            colorPrimary = Color(0xFF1890FF),
            colorSuccess = Color(0xFF52C41A),
            colorWarning = Color(0xFFFAAD14),
            colorError = Color(0xFFFF4D4F),
            colorInfo = Color(0xFF1890FF),
            colorTextBase = Color(0xFF000000),
            colorBgBase = Color(0xFFFFFFFF),
            fontSize = 12,
            marginXS = 4.dp,
            marginSM = 8.dp,
            margin = 12.dp,
            marginMD = 16.dp,
            marginLG = 20.dp,
            marginXL = 24.dp,
            marginXXL = 32.dp,
            controlHeight = 24.dp,
            controlHeightLG = 32.dp,
            controlHeightSM = 20.dp
        ),
        algorithm = ThemeAlgorithm.CompactAlgorithm
    )

    /**
     * Generate custom theme with specific primary color
     * Automatically derives all related colors from the primary color
     */
    fun customTheme(
        primaryColor: Color = Color(0xFF1890FF),
        successColor: Color = Color(0xFF52C41A),
        warningColor: Color = Color(0xFFFAAD14),
        errorColor: Color = Color(0xFFFF4D4F),
        fontSize: Int = 14,
        borderRadius: Dp = 6.dp,
        algorithm: ThemeAlgorithm? = null
    ): AntThemeConfig {
        return AntThemeConfig(
            token = ThemeToken(
                colorPrimary = primaryColor,
                colorSuccess = successColor,
                colorWarning = warningColor,
                colorError = errorColor,
                colorInfo = primaryColor,
                fontSize = fontSize,
                borderRadius = borderRadius
            ),
            algorithm = algorithm
        )
    }

    /**
     * Extend an existing theme configuration with custom overrides
     * Merges tokens and component tokens intelligently
     */
    fun extendTheme(
        baseTheme: AntThemeConfig,
        tokenOverrides: ThemeToken? = null,
        componentOverrides: ComponentToken? = null,
        algorithm: ThemeAlgorithm? = null
    ): AntThemeConfig {
        return AntThemeConfig(
            token = mergeTokens(baseTheme.token, tokenOverrides),
            components = mergeComponentTokens(baseTheme.components, componentOverrides),
            algorithm = algorithm ?: baseTheme.algorithm
        )
    }

    /**
     * Generate seed color palette from a primary color
     * Creates 10-step color palette similar to React Ant Design
     */
    fun generateSeedPalette(seedColor: Color): Map<String, List<Color>> {
        return mapOf(
            "primary" to generateColorPalette(seedColor, dark = false),
            "success" to generateColorPalette(ColorPalette.green6, dark = false),
            "warning" to generateColorPalette(ColorPalette.gold6, dark = false),
            "error" to generateColorPalette(ColorPalette.red5, dark = false)
        )
    }

    /**
     * Resolve all computed tokens from seed tokens
     * Calculates derived tokens like borders, shadows, etc.
     */
    fun resolveTokens(baseToken: ThemeToken): ResolvedThemeToken {
        return ResolvedThemeToken(
            // Seed tokens
            colorPrimary = baseToken.colorPrimary,
            colorSuccess = baseToken.colorSuccess,
            colorWarning = baseToken.colorWarning,
            colorError = baseToken.colorError,
            colorInfo = baseToken.colorInfo,
            colorTextBase = baseToken.colorTextBase,
            colorBgBase = baseToken.colorBgBase,

            // Derived color tokens
            colorPrimaryBg = lighten(baseToken.colorPrimary, 0.9f),
            colorPrimaryBorder = lighten(baseToken.colorPrimary, 0.7f),
            colorPrimaryHover = lighten(baseToken.colorPrimary, 0.15f),
            colorPrimaryActive = darken(baseToken.colorPrimary, 0.1f),

            // Text colors
            colorText = baseToken.colorTextBase,
            colorTextSecondary = adjustAlpha(baseToken.colorTextBase, 0.65f),
            colorTextTertiary = adjustAlpha(baseToken.colorTextBase, 0.45f),
            colorTextQuaternary = adjustAlpha(baseToken.colorTextBase, 0.25f),

            // Background colors
            colorBg = baseToken.colorBgBase,
            colorBgContainer = baseToken.colorBgBase,
            colorBgElevated = baseToken.colorBgBase,
            colorBgLayout = mix(baseToken.colorBgBase, baseToken.colorTextBase, 0.02f),

            // Border colors
            colorBorder = mix(baseToken.colorTextBase, baseToken.colorBgBase, 0.2f),
            colorBorderSecondary = mix(baseToken.colorTextBase, baseToken.colorBgBase, 0.06f),

            // Status colors background and border variants
            colorSuccessBg = lighten(baseToken.colorSuccess, 0.9f),
            colorSuccessBorder = lighten(baseToken.colorSuccess, 0.7f),
            colorWarningBg = lighten(baseToken.colorWarning, 0.9f),
            colorWarningBorder = lighten(baseToken.colorWarning, 0.7f),
            colorErrorBg = lighten(baseToken.colorError, 0.9f),
            colorErrorBorder = lighten(baseToken.colorError, 0.7f),
            colorInfoBg = lighten(baseToken.colorInfo, 0.9f),
            colorInfoBorder = lighten(baseToken.colorInfo, 0.7f),

            // Other derived tokens
            fontSize = baseToken.fontSize,
            fontSizeLG = (baseToken.fontSize * 1.14f).toInt(),
            fontSizeSM = (baseToken.fontSize * 0.86f).toInt(),
            lineHeight = baseToken.lineHeight,
            lineHeightLG = 1.5f,
            lineHeightSM = 1.66f,
            borderRadius = baseToken.borderRadius,
            borderRadiusLG = baseToken.borderRadiusLG,
            borderRadiusSM = baseToken.borderRadiusSM,
            controlHeight = baseToken.controlHeight,
            controlHeightLG = baseToken.controlHeightLG,
            controlHeightSM = baseToken.controlHeightSM
        )
    }

    /**
     * Generate CSS variables from theme tokens for web usage
     * Useful for cross-platform theming on web targets
     */
    fun generateCSSVariables(token: ThemeToken, prefix: String = "--ant"): String {
        return buildString {
            append(":root {\n")
            append("  $prefix-primary: ${token.colorPrimary.toHex()};\n")
            append("  $prefix-success: ${token.colorSuccess.toHex()};\n")
            append("  $prefix-warning: ${token.colorWarning.toHex()};\n")
            append("  $prefix-error: ${token.colorError.toHex()};\n")
            append("  $prefix-info: ${token.colorInfo.toHex()};\n")
            append("  $prefix-text-base: ${token.colorTextBase.toHex()};\n")
            append("  $prefix-bg-base: ${token.colorBgBase.toHex()};\n")
            append("  $prefix-font-size: ${token.fontSize}px;\n")
            append("  $prefix-border-radius: ${token.borderRadius.value.toInt()}px;\n")
            append("  $prefix-control-height: ${token.controlHeight.value.toInt()}px;\n")
            append("}\n")
        }
    }
}

/**
 * Merge two theme tokens intelligently, preferring non-default values from override
 */
internal fun mergeTokens(baseToken: ThemeToken, overrideToken: ThemeToken?): ThemeToken {
    if (overrideToken == null) return baseToken
    return baseToken.copy(
        colorPrimary = if (overrideToken.colorPrimary != Color(0xFF1890FF)) overrideToken.colorPrimary else baseToken.colorPrimary,
        colorSuccess = if (overrideToken.colorSuccess != Color(0xFF52C41A)) overrideToken.colorSuccess else baseToken.colorSuccess,
        colorWarning = if (overrideToken.colorWarning != Color(0xFFFAAD14)) overrideToken.colorWarning else baseToken.colorWarning,
        colorError = if (overrideToken.colorError != Color(0xFFFF4D4F)) overrideToken.colorError else baseToken.colorError,
        colorInfo = if (overrideToken.colorInfo != Color(0xFF1890FF)) overrideToken.colorInfo else baseToken.colorInfo,
        colorTextBase = if (overrideToken.colorTextBase != Color(0xFF000000)) overrideToken.colorTextBase else baseToken.colorTextBase,
        colorBgBase = if (overrideToken.colorBgBase != Color(0xFFFFFFFF)) overrideToken.colorBgBase else baseToken.colorBgBase,
        fontSize = if (overrideToken.fontSize != 14) overrideToken.fontSize else baseToken.fontSize,
        fontSizeHeading1 = if (overrideToken.fontSizeHeading1 != 38) overrideToken.fontSizeHeading1 else baseToken.fontSizeHeading1,
        fontSizeHeading2 = if (overrideToken.fontSizeHeading2 != 30) overrideToken.fontSizeHeading2 else baseToken.fontSizeHeading2,
        fontSizeHeading3 = if (overrideToken.fontSizeHeading3 != 24) overrideToken.fontSizeHeading3 else baseToken.fontSizeHeading3,
        fontSizeHeading4 = if (overrideToken.fontSizeHeading4 != 20) overrideToken.fontSizeHeading4 else baseToken.fontSizeHeading4,
        fontSizeHeading5 = if (overrideToken.fontSizeHeading5 != 16) overrideToken.fontSizeHeading5 else baseToken.fontSizeHeading5,
        marginXS = if (overrideToken.marginXS != 8.dp) overrideToken.marginXS else baseToken.marginXS,
        marginSM = if (overrideToken.marginSM != 12.dp) overrideToken.marginSM else baseToken.marginSM,
        margin = if (overrideToken.margin != 16.dp) overrideToken.margin else baseToken.margin,
        marginMD = if (overrideToken.marginMD != 20.dp) overrideToken.marginMD else baseToken.marginMD,
        marginLG = if (overrideToken.marginLG != 24.dp) overrideToken.marginLG else baseToken.marginLG,
        marginXL = if (overrideToken.marginXL != 32.dp) overrideToken.marginXL else baseToken.marginXL,
        marginXXL = if (overrideToken.marginXXL != 48.dp) overrideToken.marginXXL else baseToken.marginXXL,
        borderRadius = if (overrideToken.borderRadius != 6.dp) overrideToken.borderRadius else baseToken.borderRadius,
        controlHeight = if (overrideToken.controlHeight != 32.dp) overrideToken.controlHeight else baseToken.controlHeight,
        controlHeightLG = if (overrideToken.controlHeightLG != 40.dp) overrideToken.controlHeightLG else baseToken.controlHeightLG,
        controlHeightSM = if (overrideToken.controlHeightSM != 24.dp) overrideToken.controlHeightSM else baseToken.controlHeightSM
    )
}

/**
 * Merge two component token objects intelligently
 */
internal fun mergeComponentTokens(baseComponents: ComponentToken, overrideComponents: ComponentToken?): ComponentToken {
    if (overrideComponents == null) return baseComponents
    return baseComponents.copy(
        button = overrideComponents.button ?: baseComponents.button,
        input = overrideComponents.input ?: baseComponents.input,
        select = overrideComponents.select ?: baseComponents.select,
        table = overrideComponents.table ?: baseComponents.table,
        modal = overrideComponents.modal ?: baseComponents.modal,
        layout = overrideComponents.layout ?: baseComponents.layout
    )
}

/**
 * Resolved theme token containing all computed and derived tokens
 * Extends ThemeToken with computed values for colors, shadows, and other derived properties
 */
data class ResolvedThemeToken(
    // Seed tokens
    val colorPrimary: Color,
    val colorSuccess: Color,
    val colorWarning: Color,
    val colorError: Color,
    val colorInfo: Color,
    val colorTextBase: Color,
    val colorBgBase: Color,

    // Derived primary color tokens
    val colorPrimaryBg: Color,
    val colorPrimaryBorder: Color,
    val colorPrimaryHover: Color,
    val colorPrimaryActive: Color,

    // Text color tokens (with various opacity levels)
    val colorText: Color,
    val colorTextSecondary: Color,
    val colorTextTertiary: Color,
    val colorTextQuaternary: Color,

    // Background color tokens
    val colorBg: Color,
    val colorBgContainer: Color,
    val colorBgElevated: Color,
    val colorBgLayout: Color,

    // Border color tokens
    val colorBorder: Color,
    val colorBorderSecondary: Color,

    // Status color tokens with derived variants
    val colorSuccessBg: Color,
    val colorSuccessBorder: Color,
    val colorWarningBg: Color,
    val colorWarningBorder: Color,
    val colorErrorBg: Color,
    val colorErrorBorder: Color,
    val colorInfoBg: Color,
    val colorInfoBorder: Color,

    // Typography tokens
    val fontSize: Int,
    val fontSizeLG: Int,
    val fontSizeSM: Int,
    val lineHeight: Float,
    val lineHeightLG: Float,
    val lineHeightSM: Float,

    // Border radius tokens
    val borderRadius: Dp,
    val borderRadiusLG: Dp,
    val borderRadiusSM: Dp,

    // Control size tokens
    val controlHeight: Dp,
    val controlHeightLG: Dp,
    val controlHeightSM: Dp
)

/**
 * Color utility functions for theme token derivation
 */

/**
 * Lighten a color by increasing its brightness
 * Factor: 0.0 = black, 1.0 = original color, > 1.0 = lighter
 */
internal fun lighten(color: Color, factor: Float): Color {
    return Color(
        red = (color.red + (1f - color.red) * (factor - 1f)).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * (factor - 1f)).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * (factor - 1f)).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

/**
 * Darken a color by decreasing its brightness
 * Factor: 0.0 = black, 1.0 = original color, < 0.0 = darker
 */
internal fun darken(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * (1f - factor)).coerceIn(0f, 1f),
        green = (color.green * (1f - factor)).coerceIn(0f, 1f),
        blue = (color.blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

/**
 * Mix two colors with specified weight
 * Weight: 0.0 = color1, 1.0 = color2
 */
internal fun mix(color1: Color, color2: Color, weight: Float): Color {
    val w = weight.coerceIn(0f, 1f)
    return Color(
        red = (color1.red * (1f - w) + color2.red * w).coerceIn(0f, 1f),
        green = (color1.green * (1f - w) + color2.green * w).coerceIn(0f, 1f),
        blue = (color1.blue * (1f - w) + color2.blue * w).coerceIn(0f, 1f),
        alpha = (color1.alpha * (1f - w) + color2.alpha * w).coerceIn(0f, 1f)
    )
}

/**
 * Adjust color alpha (opacity)
 * Alpha: 0.0 = transparent, 1.0 = opaque
 */
internal fun adjustAlpha(color: Color, alpha: Float): Color {
    return Color(
        red = color.red,
        green = color.green,
        blue = color.blue,
        alpha = alpha.coerceIn(0f, 1f)
    )
}

/**
 * Convert Color to hex string representation
 */
internal fun Color.toHex(): String {
    val r = (red * 255).toInt().toString(16).padStart(2, '0')
    val g = (green * 255).toInt().toString(16).padStart(2, '0')
    val b = (blue * 255).toInt().toString(16).padStart(2, '0')
    return "#$r$g$b"
}

/**
 * Color palette utilities
 */
object ColorPalette {
    // Blue palette
    val blue1 = Color(0xFFE6F7FF)
    val blue2 = Color(0xFFBAE7FF)
    val blue3 = Color(0xFF91D5FF)
    val blue4 = Color(0xFF69C0FF)
    val blue5 = Color(0xFF40A9FF)
    val blue6 = Color(0xFF1890FF) // Primary
    val blue7 = Color(0xFF096DD9)
    val blue8 = Color(0xFF0050B3)
    val blue9 = Color(0xFF003A8C)
    val blue10 = Color(0xFF002766)

    // Green palette
    val green1 = Color(0xFFF6FFED)
    val green2 = Color(0xFFD9F7BE)
    val green3 = Color(0xFFB7EB8F)
    val green4 = Color(0xFF95DE64)
    val green5 = Color(0xFF73D13D)
    val green6 = Color(0xFF52C41A) // Success
    val green7 = Color(0xFF389E0D)
    val green8 = Color(0xFF237804)
    val green9 = Color(0xFF135200)
    val green10 = Color(0xFF092B00)

    // Red palette
    val red1 = Color(0xFFFFF1F0)
    val red2 = Color(0xFFFFCCC7)
    val red3 = Color(0xFFFFA39E)
    val red4 = Color(0xFFFF7875)
    val red5 = Color(0xFFFF4D4F) // Error
    val red6 = Color(0xFFF5222D)
    val red7 = Color(0xFFCF1322)
    val red8 = Color(0xFFA8071A)
    val red9 = Color(0xFF820014)
    val red10 = Color(0xFF5C0011)

    // Gold palette
    val gold1 = Color(0xFFFFFBE6)
    val gold2 = Color(0xFFFFF1B8)
    val gold3 = Color(0xFFFFE58F)
    val gold4 = Color(0xFFFFD666)
    val gold5 = Color(0xFFFFC53D)
    val gold6 = Color(0xFFFAAD14) // Warning
    val gold7 = Color(0xFFD48806)
    val gold8 = Color(0xFFAD6800)
    val gold9 = Color(0xFF874D00)
    val gold10 = Color(0xFF613400)

    // Neutral palette
    val gray1 = Color(0xFFFFFFFF)
    val gray2 = Color(0xFFFAFAFA)
    val gray3 = Color(0xFFF5F5F5)
    val gray4 = Color(0xFFF0F0F0)
    val gray5 = Color(0xFFD9D9D9)
    val gray6 = Color(0xFFBFBFBF)
    val gray7 = Color(0xFF8C8C8C)
    val gray8 = Color(0xFF595959)
    val gray9 = Color(0xFF434343)
    val gray10 = Color(0xFF262626)
    val gray11 = Color(0xFF1F1F1F)
    val gray12 = Color(0xFF141414)
    val gray13 = Color(0xFF000000)
}

/**
 * Generate color palette from base color using advanced color algorithm
 * Creates a 10-step color palette mirroring React Ant Design's generateColorPalette
 *
 * For light themes: generates lighter to darker variants
 * For dark themes: generates darker to lighter variants
 */
fun generateColorPalette(baseColor: Color, dark: Boolean = false): List<Color> {
    val colors = mutableListOf<Color>()

    // Convert RGB to HSL for better color manipulation
    val (h, s, l) = rgbToHsl(baseColor.red, baseColor.green, baseColor.blue)

    for (i in 1..10) {
        val lightness = if (dark) {
            // Dark theme: start from 85% lightness and decrease
            (0.85f - (i - 1) * 0.08f).coerceIn(0f, 1f)
        } else {
            // Light theme: start from 5% and increase
            (0.05f + (i - 1) * 0.09f).coerceIn(0f, 1f)
        }

        val (r, g, b) = hslToRgb(h, s, lightness)
        colors.add(
            Color(
                red = r.coerceIn(0f, 1f),
                green = g.coerceIn(0f, 1f),
                blue = b.coerceIn(0f, 1f),
                alpha = baseColor.alpha
            )
        )
    }

    return colors
}

/**
 * Convert RGB color to HSL (Hue, Saturation, Lightness)
 */
internal fun rgbToHsl(r: Float, g: Float, b: Float): Triple<Float, Float, Float> {
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val l = (max + min) / 2f

    if (max == min) {
        return Triple(0f, 0f, l) // Achromatic (gray)
    }

    val d = max - min
    val s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)

    val h = when {
        max == r -> ((g - b) / d + (if (g < b) 6f else 0f)) / 6f
        max == g -> ((b - r) / d + 2f) / 6f
        else -> ((r - g) / d + 4f) / 6f
    }

    return Triple(h.coerceIn(0f, 1f), s.coerceIn(0f, 1f), l.coerceIn(0f, 1f))
}

/**
 * Convert HSL (Hue, Saturation, Lightness) to RGB
 */
internal fun hslToRgb(h: Float, s: Float, l: Float): Triple<Float, Float, Float> {
    if (s == 0f) {
        return Triple(l, l, l) // Achromatic (gray)
    }

    val q = if (l < 0.5f) l * (1f + s) else l + s - l * s
    val p = 2f * l - q

    val r = hueToRgb(p, q, h + 1f / 3f)
    val g = hueToRgb(p, q, h)
    val b = hueToRgb(p, q, h - 1f / 3f)

    return Triple(r, g, b)
}

/**
 * Helper function for HSL to RGB conversion
 */
internal fun hueToRgb(p: Float, q: Float, t: Float): Float {
    val t = if (t < 0f) t + 1f else if (t > 1f) t - 1f else t
    return when {
        t < 1f / 6f -> p + (q - p) * 6f * t
        t < 1f / 2f -> q
        t < 2f / 3f -> p + (q - p) * (2f / 3f - t) * 6f
        else -> p
    }
}

/**
 * Algorithm implementations for theme transformations
 */
object ThemeAlgorithmImpl {
    /**
     * Default Algorithm: Standard Ant Design colors and layout
     * Used for light/default theme
     */
    fun applyDefaultAlgorithm(config: AntThemeConfig): AntThemeConfig {
        return config
    }

    /**
     * Dark Algorithm: Inverts colors and adjusts for dark mode visibility
     * Transforms from light theme to dark theme automatically
     */
    fun applyDarkAlgorithm(config: AntThemeConfig): AntThemeConfig {
        val token = config.token

        // Adjust base colors for dark theme
        val darkToken = token.copy(
            colorTextBase = Color(0xFFFFFFFF), // White text on dark
            colorBgBase = Color(0xFF141414),   // Very dark background

            // Adjust component-specific colors for dark mode
            marginXS = token.marginXS,
            marginSM = token.marginSM,
            margin = token.margin,
            marginMD = token.marginMD,
            marginLG = token.marginLG,
            marginXL = token.marginXL,
            marginXXL = token.marginXXL,

            // Keep primary color but darken it for better contrast
            colorPrimary = darken(token.colorPrimary, 0.15f),
            colorSuccess = darken(token.colorSuccess, 0.15f),
            colorWarning = darken(token.colorWarning, 0.15f),
            colorError = darken(token.colorError, 0.15f),
            colorInfo = darken(token.colorInfo, 0.15f)
        )

        // Update component tokens for dark mode
        val darkComponents = config.components.copy(
            layout = config.components.layout.copy(
                headerBg = Color(0xFF141414),
                siderBg = Color(0xFF141414),
                lightSiderBg = Color(0xFF1F1F1F),
                bodyBg = Color(0xFF000000),
                footerBg = Color(0xFF141414)
            ),
            table = config.components.table.copy(
                headerBg = Color(0xFF1F1F1F),
                headerSplitColor = Color(0xFF2F2F2F),
                rowHoverBg = Color(0xFF2F2F2F)
            ),
            modal = config.components.modal.copy(
                headerBg = Color(0xFF1F1F1F),
                contentBg = Color(0xFF1F1F1F),
                footerBg = Color(0xFF1F1F1F)
            )
        )

        return config.copy(
            token = darkToken,
            components = darkComponents,
            algorithm = ThemeAlgorithm.DarkAlgorithm
        )
    }

    /**
     * Compact Algorithm: Reduces spacing and component sizes
     * Creates a more compact UI with smaller margins and controls
     */
    fun applyCompactAlgorithm(config: AntThemeConfig): AntThemeConfig {
        val token = config.token

        // Reduce all spacing dimensions by ~60%
        val compactToken = token.copy(
            fontSize = (token.fontSize * 0.85f).toInt(),
            fontSizeHeading1 = (token.fontSizeHeading1 * 0.85f).toInt(),
            fontSizeHeading2 = (token.fontSizeHeading2 * 0.85f).toInt(),
            fontSizeHeading3 = (token.fontSizeHeading3 * 0.85f).toInt(),
            fontSizeHeading4 = (token.fontSizeHeading4 * 0.85f).toInt(),
            fontSizeHeading5 = (token.fontSizeHeading5 * 0.85f).toInt(),

            // Reduce spacing
            marginXS = (token.marginXS.value * 0.6f).dp,
            marginSM = (token.marginSM.value * 0.6f).dp,
            margin = (token.margin.value * 0.6f).dp,
            marginMD = (token.marginMD.value * 0.6f).dp,
            marginLG = (token.marginLG.value * 0.6f).dp,
            marginXL = (token.marginXL.value * 0.6f).dp,
            marginXXL = (token.marginXXL.value * 0.6f).dp,

            // Reduce border radius
            borderRadius = (token.borderRadius.value * 0.75f).dp,
            borderRadiusLG = (token.borderRadiusLG.value * 0.75f).dp,
            borderRadiusSM = (token.borderRadiusSM.value * 0.75f).dp,

            // Reduce control heights
            controlHeight = (token.controlHeight.value * 0.75f).dp,
            controlHeightLG = (token.controlHeightLG.value * 0.75f).dp,
            controlHeightSM = (token.controlHeightSM.value * 0.75f).dp,

            // Reduce line heights
            lineHeight = (token.lineHeight * 0.95f)
        )

        return config.copy(
            token = compactToken,
            algorithm = ThemeAlgorithm.CompactAlgorithm
        )
    }
}

/**
 * Spacing utilities
 */
object Spacing {
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}

/**
 * Typography utilities
 */
object Typography {
    val fontSizeBase = 14
    val fontSizeLG = 16
    val fontSizeSM = 12
    val fontSizeXL = 20

    val heading1 = 38
    val heading2 = 30
    val heading3 = 24
    val heading4 = 20
    val heading5 = 16

    val lineHeightBase = 1.5714f
    val lineHeightLG = 1.5f
    val lineHeightSM = 1.66f
}

/**
 * Shadow utilities
 */
object Shadows {
    val boxShadowBase = "0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05)"
    val boxShadowCard = "0 1px 2px -2px rgba(0, 0, 0, 0.16), 0 3px 6px 0 rgba(0, 0, 0, 0.12), 0 5px 12px 4px rgba(0, 0, 0, 0.09)"
    val boxShadowDrawer = "0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05)"
    val boxShadowModal = "0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05)"
}
