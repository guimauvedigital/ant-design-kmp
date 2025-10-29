package com.antdesign.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ConfigProvider provides global configuration for all Ant Design components.
 * It allows customization of theme, locale, component sizes, and other global settings.
 *
 * This component wraps your application or specific sections to provide consistent
 * configuration across all child components. It supports:
 * - Theme customization (colors, tokens, algorithms)
 * - Internationalization (locale settings)
 * - Component defaults (size, disabled state)
 * - Layout direction (LTR/RTL)
 * - Popup/dropdown behavior
 * - CSS prefix customization
 *
 * Example:
 * ```
 * AntConfigProvider(
 *     theme = AntThemeConfig(
 *         token = ThemeToken(colorPrimary = Color.Blue)
 *     ),
 *     locale = LocaleConfig(locale = "en_US"),
 *     componentSize = ComponentSize.Large
 * ) {
 *     // Your app content
 * }
 * ```
 */

/**
 * Theme configuration for Ant Design components.
 *
 * @param token Design tokens (colors, sizes, spacing, etc.)
 * @param components Component-specific token overrides
 * @param algorithm Theme algorithm (default, dark, compact)
 * @param inherit Whether to inherit theme from parent ConfigProvider (default: true)
 */
data class AntThemeConfig(
    val token: ThemeToken = ThemeToken(),
    val components: ComponentToken = ComponentToken(),
    val algorithm: ThemeAlgorithm? = null,
    val inherit: Boolean = true
)

/**
 * Design tokens that define the visual appearance of all components.
 * These tokens follow the Ant Design 5.x token system.
 *
 * Color tokens use Ant Design's color system:
 * - Primary: Brand color for primary actions
 * - Success: Indicates successful operations
 * - Warning: Indicates warning states
 * - Error: Indicates error states
 * - Info: Informational states
 *
 * Font tokens control typography:
 * - Base font size and family
 * - Heading sizes (H1-H5)
 * - Line heights for readability
 *
 * Spacing tokens define consistent spacing:
 * - XS (8dp) to XXL (48dp)
 *
 * Control tokens define interactive element sizes:
 * - Small, Middle (default), Large variants
 *
 * Z-index tokens control stacking order of overlays
 */
data class ThemeToken(
    // Colors - Brand & Semantic
    val colorPrimary: Color = Color(0xFF1890FF),
    val colorSuccess: Color = Color(0xFF52C41A),
    val colorWarning: Color = Color(0xFFFAAD14),
    val colorError: Color = Color(0xFFFF4D4F),
    val colorInfo: Color = Color(0xFF1890FF),
    val colorTextBase: Color = Color(0xFF000000),
    val colorBgBase: Color = Color(0xFFFFFFFF),

    // Colors - Neutral
    val colorText: Color = Color(0xFF000000D9), // rgba(0, 0, 0, 0.88)
    val colorTextSecondary: Color = Color(0xFF00000073), // rgba(0, 0, 0, 0.45)
    val colorTextTertiary: Color = Color(0xFF0000004D), // rgba(0, 0, 0, 0.3)
    val colorTextQuaternary: Color = Color(0xFF00000040), // rgba(0, 0, 0, 0.25)
    val colorBorder: Color = Color(0xFFD9D9D9),
    val colorBorderSecondary: Color = Color(0xFFF0F0F0),
    val colorFill: Color = Color(0xFF00000026), // rgba(0, 0, 0, 0.15)
    val colorFillSecondary: Color = Color(0xFF0000000F), // rgba(0, 0, 0, 0.06)
    val colorFillTertiary: Color = Color(0xFF0000000A), // rgba(0, 0, 0, 0.04)
    val colorFillQuaternary: Color = Color(0xFF00000005), // rgba(0, 0, 0, 0.02)
    val colorBgContainer: Color = Color(0xFFFFFFFF),
    val colorBgElevated: Color = Color(0xFFFFFFFF),
    val colorBgLayout: Color = Color(0xFFF5F5F5),
    val colorBgSpotlight: Color = Color(0xFF00000085), // rgba(0, 0, 0, 0.85)
    val colorBgMask: Color = Color(0xFF00000073), // rgba(0, 0, 0, 0.45)

    // Font - Size
    val fontSize: Int = 14,
    val fontSizeSM: Int = 12,
    val fontSizeLG: Int = 16,
    val fontSizeXL: Int = 20,
    val fontSizeHeading1: Int = 38,
    val fontSizeHeading2: Int = 30,
    val fontSizeHeading3: Int = 24,
    val fontSizeHeading4: Int = 20,
    val fontSizeHeading5: Int = 16,
    val fontSizeIcon: Int = 12,

    // Font - Family & Weight
    val fontFamily: String = "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial",
    val fontFamilyCode: String = "'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace",
    val fontWeightStrong: Int = 600,

    // Spacing - Margin
    val marginXXS: Dp = 4.dp,
    val marginXS: Dp = 8.dp,
    val marginSM: Dp = 12.dp,
    val margin: Dp = 16.dp,
    val marginMD: Dp = 20.dp,
    val marginLG: Dp = 24.dp,
    val marginXL: Dp = 32.dp,
    val marginXXL: Dp = 48.dp,

    // Spacing - Padding
    val paddingXXS: Dp = 4.dp,
    val paddingXS: Dp = 8.dp,
    val paddingSM: Dp = 12.dp,
    val padding: Dp = 16.dp,
    val paddingMD: Dp = 20.dp,
    val paddingLG: Dp = 24.dp,
    val paddingXL: Dp = 32.dp,

    // Border
    val borderRadiusXS: Dp = 2.dp,
    val borderRadiusSM: Dp = 4.dp,
    val borderRadius: Dp = 6.dp,
    val borderRadiusLG: Dp = 8.dp,
    val borderRadiusOuter: Dp = 4.dp,
    val lineWidth: Dp = 1.dp,
    val lineWidthBold: Dp = 2.dp,
    val lineType: String = "solid",

    // Line Height
    val lineHeight: Float = 1.5714f,
    val lineHeightSM: Float = 1.6666f,
    val lineHeightLG: Float = 1.5f,
    val lineHeightHeading1: Float = 1.2105f,
    val lineHeightHeading2: Float = 1.2666f,
    val lineHeightHeading3: Float = 1.3333f,
    val lineHeightHeading4: Float = 1.4f,
    val lineHeightHeading5: Float = 1.5f,

    // Control
    val controlHeight: Dp = 32.dp,
    val controlHeightSM: Dp = 24.dp,
    val controlHeightLG: Dp = 40.dp,
    val controlHeightXS: Dp = 16.dp,
    val controlInteractiveSize: Dp = 16.dp,
    val controlItemBgHover: Color = Color(0xFF00000005),
    val controlItemBgActive: Color = Color(0xFFE6F7FF),
    val controlItemBgActiveHover: Color = Color(0xFFBAE7FF),
    val controlOutlineWidth: Dp = 2.dp,

    // Motion
    val motionUnit: Float = 0.1f, // 100ms
    val motionBase: Float = 0f,
    val motionEaseInOut: String = "cubic-bezier(0.645, 0.045, 0.355, 1)",
    val motionEaseOut: String = "cubic-bezier(0.215, 0.61, 0.355, 1)",
    val motionEaseIn: String = "cubic-bezier(0.55, 0.055, 0.675, 0.19)",

    // Z-index
    val zIndexBase: Int = 0,
    val zIndexPopupBase: Int = 1000,
    val zIndexModalMask: Int = 1000,
    val zIndexModal: Int = 1000,
    val zIndexNotification: Int = 1010,
    val zIndexMessage: Int = 1010,
    val zIndexPopover: Int = 1030,
    val zIndexDropdown: Int = 1050,
    val zIndexPopconfirm: Int = 1060,
    val zIndexTooltip: Int = 1070,

    // Screen breakpoints (for responsive design)
    val screenXS: Int = 480,
    val screenXSMin: Int = 480,
    val screenSM: Int = 576,
    val screenSMMin: Int = 576,
    val screenMD: Int = 768,
    val screenMDMin: Int = 768,
    val screenLG: Int = 992,
    val screenLGMin: Int = 992,
    val screenXL: Int = 1200,
    val screenXLMin: Int = 1200,
    val screenXXL: Int = 1600,
    val screenXXLMin: Int = 1600,

    // Box Shadow
    val boxShadow: String = "0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05)",
    val boxShadowSecondary: String = "0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 9px 28px 8px rgba(0, 0, 0, 0.05)",
    val boxShadowTertiary: String = "0 1px 2px 0 rgba(0, 0, 0, 0.03), 0 1px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px 0 rgba(0, 0, 0, 0.02)",

    // Opacity
    val opacityLoading: Float = 0.65f,

    // Link
    val linkDecoration: String = "none",
    val linkHoverDecoration: String = "none",
    val linkFocusDecoration: String = "none"
)

/**
 * Component-specific token overrides.
 * Each component can have its own customized design tokens.
 */
data class ComponentToken(
    val button: ButtonToken = ButtonToken(),
    val input: InputToken = InputToken(),
    val select: SelectToken = SelectToken(),
    val table: TableToken = TableToken(),
    val modal: ModalToken = ModalToken(),
    val layout: LayoutToken = LayoutToken(),
    val card: CardToken = CardToken(),
    val checkbox: CheckboxToken = CheckboxToken(),
    val radio: RadioToken = RadioToken(),
    val switch: SwitchToken = SwitchToken(),
    val slider: SliderToken = SliderToken()
)

data class ButtonToken(
    val primaryColor: Color = Color(0xFF1890FF),
    val defaultBorderColor: Color = Color(0xFFD9D9D9),
    val defaultColor: Color = Color(0xFF000000),
    val dangerColor: Color = Color(0xFFFF4D4F)
)

data class InputToken(
    val borderColor: Color = Color(0xFFD9D9D9),
    val hoverBorderColor: Color = Color(0xFF40A9FF),
    val activeBorderColor: Color = Color(0xFF1890FF)
)

data class SelectToken(
    val optionSelectedBg: Color = Color(0xFFE6F7FF),
    val optionActiveBg: Color = Color(0xFFF5F5F5)
)

data class TableToken(
    val headerBg: Color = Color(0xFFFAFAFA),
    val headerSplitColor: Color = Color(0xFFF0F0F0),
    val rowHoverBg: Color = Color(0xFFFAFAFA)
)

data class ModalToken(
    val headerBg: Color = Color(0xFFFFFFFF),
    val contentBg: Color = Color(0xFFFFFFFF),
    val footerBg: Color = Color(0xFFFFFFFF)
)

data class LayoutToken(
    // Body/Content
    val bodyBg: Color = Color(0xFFF0F2F5),

    // Header
    val headerBg: Color = Color(0xFF001529),
    val headerHeight: Dp = 64.dp,
    val headerPaddingInline: Dp = 50.dp,
    val headerColor: Color = Color(0xFFFFFFFF),

    // Footer
    val footerBg: Color = Color(0xFFF0F2F5),
    val footerPadding: Dp = 24.dp,

    // Sider
    val siderBg: Color = Color(0xFF001529),
    val triggerHeight: Dp = 48.dp,
    val triggerBg: Color = Color(0xFF002140),
    val triggerColor: Color = Color(0xFFFFFFFF),
    val zeroTriggerWidth: Dp = 36.dp,
    val zeroTriggerHeight: Dp = 42.dp,

    // Light theme
    val lightSiderBg: Color = Color(0xFFFFFFFF),
    val lightTriggerBg: Color = Color(0xFFFFFFFF),
    val lightTriggerColor: Color = Color(0xFF000000)
)

data class CardToken(
    val headerBg: Color = Color(0x00000000), // transparent
    val actionsBg: Color = Color(0xFFFAFAFA)
)

data class CheckboxToken(
    val colorPrimary: Color = Color(0xFF1890FF),
    val colorPrimaryHover: Color = Color(0xFF40A9FF),
    val colorBorder: Color = Color(0xFFD9D9D9)
)

data class RadioToken(
    val colorPrimary: Color = Color(0xFF1890FF),
    val colorPrimaryHover: Color = Color(0xFF40A9FF),
    val colorBorder: Color = Color(0xFFD9D9D9),
    val dotSize: Dp = 8.dp
)

data class SwitchToken(
    val colorPrimary: Color = Color(0xFF1890FF),
    val colorTextQuaternary: Color = Color(0xFF00000040),
    val handleSize: Dp = 18.dp,
    val trackHeight: Dp = 22.dp,
    val trackMinWidth: Dp = 44.dp,
    val innerMinMargin: Dp = 4.dp,
    val innerMaxMargin: Dp = 24.dp
)

data class SliderToken(
    val colorPrimary: Color = Color(0xFF1890FF),
    val colorPrimaryBorder: Color = Color(0xFF91D5FF),
    val handleSize: Dp = 14.dp,
    val handleSizeHover: Dp = 16.dp,
    val railSize: Dp = 4.dp,
    val dotSize: Dp = 8.dp
)

/**
 * Theme algorithms for generating design tokens.
 *
 * - DefaultAlgorithm: Standard light theme
 * - DarkAlgorithm: Dark theme with inverted colors
 * - CompactAlgorithm: Compact theme with reduced spacing
 */
enum class ThemeAlgorithm {
    DefaultAlgorithm,
    DarkAlgorithm,
    CompactAlgorithm
}

/**
 * Locale configuration for internationalization.
 * Contains all text strings used by Ant Design components.
 *
 * @param locale Locale identifier (e.g., "en_US", "zh_CN", "fr_FR")
 */
data class LocaleConfig(
    val locale: String = "en_US",

    // Common
    val ok: String = "OK",
    val cancel: String = "Cancel",
    val justNow: String = "Just now",
    val close: String = "Close",
    val copy: String = "Copy",
    val copied: String = "Copied",
    val expand: String = "Expand",
    val collapse: String = "Collapse",

    // Pagination
    val itemsPerPage: String = "/ page",
    val jumpTo: String = "Go to",
    val jumpToConfirm: String = "confirm",
    val page: String = "Page",
    val prevPage: String = "Previous Page",
    val nextPage: String = "Next Page",
    val prev5: String = "Previous 5 Pages",
    val next5: String = "Next 5 Pages",
    val prev3: String = "Previous 3 Pages",
    val next3: String = "Next 3 Pages",
    val totalItems: String = "Total {0} items",

    // Empty
    val emptyText: String = "No data",
    val emptyDescription: String = "No Data",

    // Upload
    val uploadText: String = "Upload",
    val uploadingText: String = "Uploading...",
    val uploadErrorText: String = "Upload error",
    val uploadSuccessText: String = "Upload success",
    val previewFile: String = "Preview file",
    val downloadFile: String = "Download file",
    val removeFile: String = "Remove file",

    // Table
    val filterTitle: String = "Filter menu",
    val filterConfirm: String = "OK",
    val filterReset: String = "Reset",
    val filterEmptyText: String = "No filters",
    val filterCheckall: String = "Select all items",
    val filterSearchPlaceholder: String = "Search in filters",
    val selectAll: String = "Select current page",
    val selectInvert: String = "Invert current page",
    val selectNone: String = "Clear all data",
    val selectionAll: String = "Select all data",
    val sortTitle: String = "Sort",
    val triggerDesc: String = "Click to sort descending",
    val triggerAsc: String = "Click to sort ascending",
    val cancelSort: String = "Click to cancel sorting",

    // Modal
    val okText: String = "OK",
    val cancelText: String = "Cancel",

    // Popconfirm
    val popconfirmOkText: String = "OK",
    val popconfirmCancelText: String = "Cancel",

    // Transfer
    val transferSearchPlaceholder: String = "Search here",
    val transferItemUnit: String = "item",
    val transferItemsUnit: String = "items",
    val transferRemove: String = "Remove",
    val transferSelectAll: String = "Select all",
    val transferSelectCurrent: String = "Select current page",
    val transferSelectInvert: String = "Invert current page",

    // Select
    val selectPlaceholder: String = "Please select",
    val selectNotFoundContent: String = "Not Found",

    // DatePicker
    val datePickerPlaceholder: String = "Select date",
    val datePickerRangePlaceholder: List<String> = listOf("Start date", "End date"),
    val datePickerToday: String = "Today",
    val datePickerNow: String = "Now",
    val datePickerOk: String = "OK",

    // TimePicker
    val timePickerPlaceholder: String = "Select time",

    // Calendar
    val calendarToday: String = "Today",
    val calendarMonth: String = "Month",
    val calendarYear: String = "Year",
    val calendarOk: String = "OK",

    // Form
    val formDefaultValidateMessages: Map<String, String> = mapOf(
        "required" to "\${label} is required",
        "whitespace" to "\${label} cannot be empty",
        "pattern.mismatch" to "\${label} does not match pattern \${pattern}"
    ),

    // Image
    val imagePreview: String = "Preview"
)

/**
 * Configuration properties for the ConfigProvider.
 * These settings are passed down through the component tree via CompositionLocal.
 *
 * @param theme Theme configuration including tokens and algorithms
 * @param locale Internationalization settings
 * @param componentSize Default size for all components (Small/Middle/Large)
 * @param componentDisabled Global disable state for all components
 * @param direction Text direction (LTR or RTL)
 * @param space Default space configuration
 * @param virtual Enable virtual scrolling for lists
 * @param dropdownMatchSelectWidth Make dropdown width match select width
 * @param getPopupContainer Function to get the container element for popups/dropdowns
 * @param prefixCls Prefix for CSS class names (default: "ant")
 * @param iconPrefixCls Prefix for icon CSS class names (default: "anticon")
 * @param csp Content Security Policy nonce for inline styles
 * @param autoInsertSpaceInButton Insert space between Chinese characters in buttons
 */
data class ConfigProviderProps(
    val theme: AntThemeConfig = AntThemeConfig(),
    val locale: LocaleConfig = LocaleConfig(),
    val componentSize: ComponentSize = ComponentSize.Middle,
    val componentDisabled: Boolean = false,
    val direction: Direction = Direction.LTR,
    val space: SpaceConfig = SpaceConfig(),
    val virtual: Boolean = true,
    val dropdownMatchSelectWidth: Boolean = true,
    val getPopupContainer: (() -> Any)? = null,
    val prefixCls: String = "ant",
    val iconPrefixCls: String = "anticon",
    val csp: CspConfig? = null,
    val autoInsertSpaceInButton: Boolean = true
)

/**
 * Space configuration for controlling default spacing between elements.
 *
 * @param size Default spacing size (can be overridden per component)
 * @param compact Whether to use compact mode (reduced spacing)
 */
data class SpaceConfig(
    val size: Dp = 8.dp,
    val compact: Boolean = false
)

/**
 * Content Security Policy configuration.
 *
 * @param nonce Nonce value for inline styles to satisfy CSP requirements
 */
data class CspConfig(
    val nonce: String? = null
)

/**
 * Component size variants.
 * These control the overall dimensions of interactive components.
 *
 * - Small: Compact size for dense layouts
 * - Middle: Default size for standard layouts
 * - Large: Larger size for spacious layouts or accessibility
 */
enum class ComponentSize {
    Small,
    Middle,
    Large
}

/**
 * Text direction for internationalization.
 *
 * - LTR: Left-to-Right (default for most languages)
 * - RTL: Right-to-Left (for Arabic, Hebrew, etc.)
 */
enum class Direction {
    LTR,
    RTL
}

/**
 * CompositionLocal for ConfigProvider properties.
 * This allows any component in the tree to access the current configuration.
 */
val LocalConfigProvider = compositionLocalOf { ConfigProviderProps() }

/**
 * ConfigProvider component that sets global configuration for all Ant Design components.
 *
 * This component uses Compose's CompositionLocal system to provide configuration down
 * the component tree. All Ant Design components access this configuration via the
 * useConfig(), useTheme(), and useLocale() hooks.
 *
 * ConfigProviders can be nested - child providers will merge their configuration
 * with parent providers (unless inherit = false in theme config).
 *
 * @param modifier Modifier to apply to the root Box container
 * @param theme Theme configuration (colors, tokens, algorithms)
 * @param locale Locale configuration for i18n
 * @param componentSize Default size for all components
 * @param componentDisabled Global disable state
 * @param direction Text direction (LTR/RTL)
 * @param space Default spacing configuration
 * @param virtual Enable virtual scrolling
 * @param dropdownMatchSelectWidth Match dropdown width to select
 * @param getPopupContainer Function to get popup container element
 * @param prefixCls CSS class name prefix (default: "ant")
 * @param iconPrefixCls Icon CSS class name prefix (default: "anticon")
 * @param csp Content Security Policy configuration
 * @param autoInsertSpaceInButton Auto-insert space in button text
 * @param children Content to wrap with this configuration
 *
 * Example:
 * ```
 * AntConfigProvider(
 *     theme = AntThemeConfig(
 *         token = ThemeToken(
 *             colorPrimary = Color(0xFF00B96B),
 *             borderRadius = 8.dp
 *         )
 *     ),
 *     locale = LocaleConfig(locale = "en_US"),
 *     componentSize = ComponentSize.Large
 * ) {
 *     // Your app components
 *     AntButton(text = "Click me")
 * }
 * ```
 */
@Composable
fun AntConfigProvider(
    modifier: Modifier = Modifier,
    theme: AntThemeConfig? = null,
    locale: LocaleConfig? = null,
    componentSize: ComponentSize? = null,
    componentDisabled: Boolean? = null,
    direction: Direction? = null,
    space: SpaceConfig? = null,
    virtual: Boolean? = null,
    dropdownMatchSelectWidth: Boolean? = null,
    getPopupContainer: (() -> Any)? = null,
    prefixCls: String? = null,
    iconPrefixCls: String? = null,
    csp: CspConfig? = null,
    autoInsertSpaceInButton: Boolean? = null,
    children: @Composable () -> Unit
) {
    val currentConfig = LocalConfigProvider.current

    // Merge theme configuration with parent if inherit = true
    val mergedTheme = when {
        theme == null -> currentConfig.theme
        theme.inherit -> mergeThemes(currentConfig.theme, theme)
        else -> theme
    }

    val newConfig = ConfigProviderProps(
        theme = mergedTheme,
        locale = locale ?: currentConfig.locale,
        componentSize = componentSize ?: currentConfig.componentSize,
        componentDisabled = componentDisabled ?: currentConfig.componentDisabled,
        direction = direction ?: currentConfig.direction,
        space = space ?: currentConfig.space,
        virtual = virtual ?: currentConfig.virtual,
        dropdownMatchSelectWidth = dropdownMatchSelectWidth ?: currentConfig.dropdownMatchSelectWidth,
        getPopupContainer = getPopupContainer ?: currentConfig.getPopupContainer,
        prefixCls = prefixCls ?: currentConfig.prefixCls,
        iconPrefixCls = iconPrefixCls ?: currentConfig.iconPrefixCls,
        csp = csp ?: currentConfig.csp,
        autoInsertSpaceInButton = autoInsertSpaceInButton ?: currentConfig.autoInsertSpaceInButton
    )

    CompositionLocalProvider(LocalConfigProvider provides newConfig) {
        Box(modifier = modifier) {
            children()
        }
    }
}

/**
 * Merges two theme configurations, with the child theme taking precedence.
 * This is used when theme.inherit = true to combine parent and child themes.
 *
 * @param parent Parent theme configuration
 * @param child Child theme configuration
 * @return Merged theme configuration
 */
private fun mergeThemes(parent: AntThemeConfig, child: AntThemeConfig): AntThemeConfig {
    // For simplicity, we'll use the child theme's token and components
    // In a more sophisticated implementation, we could merge individual token values
    return AntThemeConfig(
        token = child.token,
        components = child.components,
        algorithm = child.algorithm ?: parent.algorithm,
        inherit = child.inherit
    )
}

/**
 * Hook to access the current ConfigProvider configuration.
 *
 * This composable function returns the full configuration from the nearest
 * ConfigProvider ancestor in the component tree.
 *
 * @return Current ConfigProvider configuration
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val config = useConfig()
 *     val isDisabled = config.componentDisabled
 *     val size = config.componentSize
 * }
 * ```
 */
@Composable
fun useConfig(): ConfigProviderProps {
    return LocalConfigProvider.current
}

/**
 * Hook to access the current theme configuration.
 *
 * This is a convenience function that extracts just the theme configuration
 * from the ConfigProvider. Use this when you only need theme-related settings.
 *
 * @return Current theme configuration
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val theme = useTheme()
 *     val primaryColor = theme.token.colorPrimary
 *     val buttonToken = theme.components.button
 * }
 * ```
 */
@Composable
fun useTheme(): AntThemeConfig {
    return LocalConfigProvider.current.theme
}

/**
 * Hook to access the current locale configuration.
 *
 * This is a convenience function that extracts just the locale configuration
 * from the ConfigProvider. Use this for internationalization.
 *
 * @return Current locale configuration
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val locale = useLocale()
 *     Text(locale.ok) // "OK" in current language
 * }
 * ```
 */
@Composable
fun useLocale(): LocaleConfig {
    return LocalConfigProvider.current.locale
}

/**
 * Hook to access the current component size setting.
 *
 * @return Current component size (Small, Middle, or Large)
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val size = useComponentSize()
 *     val height = when (size) {
 *         ComponentSize.Small -> 24.dp
 *         ComponentSize.Middle -> 32.dp
 *         ComponentSize.Large -> 40.dp
 *     }
 * }
 * ```
 */
@Composable
fun useComponentSize(): ComponentSize {
    return LocalConfigProvider.current.componentSize
}

/**
 * Hook to access the current component disabled state.
 *
 * @return Whether components should be disabled globally
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val isDisabled = useComponentDisabled()
 *     AntButton(
 *         text = "Click",
 *         disabled = isDisabled
 *     )
 * }
 * ```
 */
@Composable
fun useComponentDisabled(): Boolean {
    return LocalConfigProvider.current.componentDisabled
}

/**
 * Hook to access the current text direction.
 *
 * @return Current direction (LTR or RTL)
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val direction = useDirection()
 *     val alignment = if (direction == Direction.RTL) {
 *         Alignment.End
 *     } else {
 *         Alignment.Start
 *     }
 * }
 * ```
 */
@Composable
fun useDirection(): Direction {
    return LocalConfigProvider.current.direction
}

/**
 * Hook to access the CSS class prefix.
 *
 * @return Current prefix (default: "ant")
 *
 * Example:
 * ```
 * @Composable
 * fun MyComponent() {
 *     val prefix = usePrefixCls()
 *     // Use as: "$prefix-button", "$prefix-input", etc.
 * }
 * ```
 */
@Composable
fun usePrefixCls(): String {
    return LocalConfigProvider.current.prefixCls
}

/**
 * Hook to get a prefixed CSS class name for a specific component.
 *
 * @param suffix Component name suffix (e.g., "button", "input")
 * @param customPrefix Optional custom prefix to override the global prefix
 * @return Prefixed class name (e.g., "ant-button")
 *
 * Example:
 * ```
 * @Composable
 * fun MyButton() {
 *     val className = usePrefixCls("button") // "ant-button"
 *     val customClassName = usePrefixCls("button", "my") // "my-button"
 * }
 * ```
 */
@Composable
fun usePrefixCls(suffix: String, customPrefix: String? = null): String {
    val defaultPrefix = LocalConfigProvider.current.prefixCls
    val prefix = customPrefix ?: defaultPrefix
    return "$prefix-$suffix"
}
