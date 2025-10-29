package digital.guimauve.antdesign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class InputSize {
    Large,
    Middle,
    Small
}

enum class InputStatus {
    Default,
    Error,
    Warning
}

enum class InputVariant {
    Outlined,   // Border standard (default) - v5.13.0+
    Filled,     // Filled background - v5.13.0+
    Borderless, // No border - v5.13.0+
    Underlined  // Bottom border only - v5.24.0+
}

/**
 * InputClassNames - Semantic class names for Input component parts
 *
 * @param input Custom class name for the input field
 * @param prefix Custom class name for the prefix element
 * @param suffix Custom class name for the suffix element
 * @param count Custom class name for the count element
 */
data class InputClassNames(
    val input: String? = null,
    val prefix: String? = null,
    val suffix: String? = null,
    val count: String? = null,
)

/**
 * InputStyles - Semantic styles for Input component parts
 *
 * @param input Custom modifier for the input field
 * @param prefix Custom modifier for the prefix element
 * @param suffix Custom modifier for the suffix element
 * @param count Custom modifier for the count element
 * @param affixWrapper Custom modifier for the affix wrapper
 */
data class InputStyles(
    val input: Modifier? = null,
    val prefix: Modifier? = null,
    val suffix: Modifier? = null,
    val count: Modifier? = null,
    val affixWrapper: Modifier? = null,
)

/**
 * InputCountConfig - Configuration for input character counting (v5.10.0+)
 *
 * @param show Whether to show the count or custom formatter function
 * @param max Maximum character count (text becomes red if exceeded)
 * @param strategy Custom counting strategy function (e.g., for grapheme clusters)
 * @param exceedFormatter Custom formatter when count exceeds max
 */
data class InputCountConfig(
    val show: Any = true, // Boolean or ((CountInfo) -> @Composable () -> Unit)
    val max: Int? = null,
    val strategy: ((text: String) -> Int)? = null,
    val exceedFormatter: ((text: String, config: InputCountConfig) -> String)? = null,
)

/**
 * CountInfo - Information passed to count formatter
 *
 * @param value Current input value
 * @param count Character count
 * @param maxLength Maximum length if set
 */
data class CountInfo(
    val value: String,
    val count: Int,
    val maxLength: Int?,
)

/**
 * InputAllowClearConfig - Configuration for clear button (v5.20.0+)
 *
 * @param clearIcon Custom clear icon composable
 */
data class InputAllowClearConfig(
    val clearIcon: (@Composable () -> Unit)? = null,
)

/**
 * TextAreaStyles - Semantic styles for TextArea component parts (v5.4.0+)
 *
 * @param textarea Custom modifier for the textarea field
 * @param count Custom modifier for the count element
 */
data class TextAreaStyles(
    val textarea: Modifier? = null,
    val count: Modifier? = null,
)

/**
 * TextAreaClassNames - Semantic class names for TextArea component parts (v5.4.0+)
 *
 * @param textarea Custom class name for the textarea field
 * @param count Custom class name for the count element
 */
data class TextAreaClassNames(
    val textarea: String? = null,
    val count: String? = null,
)

/**
 * CompositionEvent - Data class for composition events (IME input for Asian languages)
 *
 * @param text The composed text
 * @param isComposing Whether composition is in progress
 */
data class CompositionEvent(
    val text: String,
    val isComposing: Boolean,
)

/**
 * VisibilityToggle - Configuration for password visibility toggle (v4.24.0+)
 *
 * @param visible Whether password is visible (controlled mode)
 * @param onVisibleChange Callback when visibility changes
 */
data class VisibilityToggle(
    val visible: Boolean? = null,
    val onVisibleChange: ((Boolean) -> Unit)? = null,
)

/**
 * InputRef - Reference object for Input component methods
 */
class InputRef {
    internal val focusRequester = FocusRequester()

    /**
     * Get focus on the input
     * @param option Focus options
     */
    fun focus(option: FocusOption? = null) {
        focusRequester.requestFocus()
    }

    /**
     * Remove focus from the input
     */
    fun blur() {
        focusRequester.freeFocus()
    }
}

/**
 * FocusOption - Options for focus method
 *
 * @param preventScroll Whether to prevent scrolling (not applicable in Compose)
 * @param cursor Cursor position after focus
 */
data class FocusOption(
    val preventScroll: Boolean = false,
    val cursor: CursorPosition = CursorPosition.End,
)

/**
 * CursorPosition - Position of cursor after focus
 */
enum class CursorPosition {
    Start,
    End,
    All
}

/**
 * AntInput - A basic input component with full React Ant Design v5 parity
 *
 * Supports both controlled and uncontrolled modes:
 * - Controlled: Pass both `value` and `onValueChange`
 * - Uncontrolled: Pass only `defaultValue`
 *
 * @param value Current input value (controlled mode)
 * @param onValueChange Callback when value changes (controlled mode)
 * @param modifier Modifier for the root element
 * @param defaultValue Initial value for uncontrolled mode
 * @param placeholder Placeholder text
 * @param disabled Whether the input is disabled
 * @param readOnly Whether the input is read-only
 * @param size Size of the input: Large, Middle, or Small
 * @param status Validation status: Default, Error, or Warning
 * @param variant Style variant: Outlined, Filled, Borderless, or Underlined (v5.13.0+)
 * @param bordered Deprecated - use variant instead
 * @param type Keyboard type for the input
 * @param prefix Prefix icon or element
 * @param suffix Suffix icon or element
 * @param addonBefore Element to show before input
 * @param addonAfter Element to show after input
 * @param allowClear Show clear button (Boolean or InputAllowClearConfig with custom icon)
 * @param maxLength Maximum character length
 * @param showCount Show character count (Boolean or custom formatter function)
 * @param count Advanced count configuration (v5.10.0+)
 * @param password Internal prop for password mode
 * @param styles Semantic styles for component parts (v5.4.0+)
 * @param classNames Semantic class names for component parts (v5.4.0+)
 * @param rootClassName Root element class name
 * @param htmlSize HTML size attribute (not used in Compose)
 * @param ref InputRef for imperative focus/blur methods
 * @param keyboardOptions Keyboard configuration
 * @param keyboardActions Keyboard action handlers
 * @param onClear Callback when clear button is clicked (v5.20.0+)
 * @param onPressEnter Callback when Enter key is pressed
 * @param onCompositionStart Callback when IME composition starts
 * @param onCompositionUpdate Callback when IME composition updates
 * @param onCompositionEnd Callback when IME composition ends
 * @param onFocus Callback when input gains focus
 * @param onBlur Callback when input loses focus
 */
@Composable
fun AntInput(
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    defaultValue: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    variant: InputVariant = InputVariant.Outlined,
    bordered: Boolean = true,
    type: KeyboardType = KeyboardType.Text,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    addonBefore: (@Composable () -> Unit)? = null,
    addonAfter: (@Composable () -> Unit)? = null,
    allowClear: Any = false, // Boolean or AllowClearConfig
    maxLength: Int? = null,
    showCount: Any = false, // Boolean or ((CountInfo) -> @Composable () -> Unit)
    count: InputCountConfig? = null,
    password: Boolean = false,
    styles: InputStyles? = null,
    classNames: InputClassNames? = null,
    rootClassName: String? = null,
    htmlSize: Int? = null,
    ref: InputRef? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClear: (() -> Unit)? = null,
    onPressEnter: (() -> Unit)? = null,
    onCompositionStart: ((CompositionEvent) -> Unit)? = null,
    onCompositionUpdate: ((CompositionEvent) -> Unit)? = null,
    onCompositionEnd: ((CompositionEvent) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val config = useConfig()
    val theme = useTheme()

    // Handle controlled vs uncontrolled state
    var internalValue by remember { mutableStateOf(defaultValue ?: "") }
    val currentValue = value ?: internalValue
    val handleValueChange: (String) -> Unit = { newValue ->
        if (value == null) {
            internalValue = newValue
        }
        onValueChange?.invoke(newValue)
    }

    var isFocused by remember { mutableStateOf(false) }
    var isComposing by remember { mutableStateOf(false) }

    // Attach focus requester if ref is provided
    val focusRequester = ref?.focusRequester ?: remember { FocusRequester() }

    // Determine the effective count configuration
    val effectiveCount = count ?: when (showCount) {
        is Boolean -> if (showCount) InputCountConfig(show = true, max = maxLength) else null
        else -> InputCountConfig(show = showCount, max = maxLength)
    }

    // Determine allowClear configuration
    val allowClearConfig = when (allowClear) {
        is Boolean -> if (allowClear) InputAllowClearConfig() else null
        is InputAllowClearConfig -> allowClear
        else -> null
    }

    // Calculate character count using custom strategy if provided
    val charCount = remember(currentValue, effectiveCount) {
        effectiveCount?.strategy?.invoke(currentValue) ?: currentValue.length
    }

    // Determine if count exceeds max
    val isCountExceeded = effectiveCount?.max?.let { charCount > it } ?: false

    // Determine border and background colors based on variant and status
    val borderColor = when {
        !bordered || variant == InputVariant.Borderless || variant == InputVariant.Underlined -> Color.Transparent
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        isFocused -> config.theme.components.input.activeBorderColor
        disabled -> Color(0xFFD9D9D9)
        else -> config.theme.components.input.borderColor
    }

    val backgroundColor = when {
        disabled -> Color(0xFFF5F5F5)
        variant == InputVariant.Filled -> Color(0xFFFAFAFA)
        variant == InputVariant.Borderless || variant == InputVariant.Underlined -> Color.Transparent
        else -> Color.White
    }

    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        InputSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    val borderWidth = when {
        !bordered || variant == InputVariant.Borderless -> 0.dp
        variant == InputVariant.Underlined && !isFocused -> 1.dp
        variant == InputVariant.Underlined && isFocused -> 2.dp
        isFocused -> 2.dp
        else -> 1.dp
    }

    // Custom shape for underlined variant
    val shape = when (variant) {
        InputVariant.Underlined -> RoundedCornerShape(0.dp) // No rounded corners for underlined
        else -> RoundedCornerShape(theme.token.borderRadius)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        addonBefore?.invoke()

        Card(
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) {
                        onFocus?.invoke()
                    } else {
                        onBlur?.invoke()
                    }
                }
                .then(styles?.affixWrapper ?: Modifier),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = when {
                variant == InputVariant.Underlined && borderWidth > 0.dp ->
                    BorderStroke(borderWidth, borderColor) // Only bottom border for underlined
                borderWidth > 0.dp -> BorderStroke(borderWidth, borderColor)
                else -> null
            },
            shape = shape
        ) {
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (prefix != null) {
                    Box(modifier = styles?.prefix ?: Modifier) {
                        prefix.invoke()
                    }
                }

                BasicTextField(
                    value = currentValue,
                    onValueChange = { newValue ->
                        // Respect maxLength unless using custom count strategy
                        val shouldUpdate = if (effectiveCount?.strategy != null) {
                            // With custom strategy, let it decide
                            effectiveCount.max?.let { max ->
                                (effectiveCount.strategy.invoke(newValue)) <= max
                            } ?: true
                        } else {
                            maxLength == null || newValue.length <= maxLength
                        }

                        if (shouldUpdate) {
                            handleValueChange(newValue)
                            if (isComposing) {
                                onCompositionUpdate?.invoke(CompositionEvent(newValue, true))
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .then(styles?.input ?: Modifier),
                    enabled = !disabled,
                    readOnly = readOnly,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = if (disabled) Color.Gray else theme.token.colorTextBase
                    ),
                    keyboardOptions = keyboardOptions.copy(keyboardType = type),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onPressEnter?.invoke()
                            keyboardActions.onDone?.invoke(this)
                        },
                        onGo = {
                            onPressEnter?.invoke()
                            keyboardActions.onGo?.invoke(this)
                        },
                        onNext = keyboardActions.onNext,
                        onPrevious = keyboardActions.onPrevious,
                        onSearch = {
                            onPressEnter?.invoke()
                            keyboardActions.onSearch?.invoke(this)
                        },
                        onSend = {
                            onPressEnter?.invoke()
                            keyboardActions.onSend?.invoke(this)
                        }
                    ),
                    singleLine = true,
                    visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
                    cursorBrush = SolidColor(theme.token.colorPrimary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (currentValue.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color(0xFFBFBFBF)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                if (allowClearConfig != null && currentValue.isNotEmpty() && !disabled && !readOnly) {
                    IconButton(
                        onClick = {
                            handleValueChange("")
                            onClear?.invoke()
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        if (allowClearConfig.clearIcon != null) {
                            allowClearConfig.clearIcon.invoke()
                        } else {
                            Text("✕", color = Color(0xFFBFBFBF), fontSize = 12.sp)
                        }
                    }
                }

                if (suffix != null) {
                    Box(modifier = styles?.suffix ?: Modifier) {
                        suffix.invoke()
                    }
                }
            }
        }

        addonAfter?.invoke()
    }

    // Render count outside the main input for better positioning
    if (effectiveCount != null) {
        when (val showConfig = effectiveCount.show) {
            is Boolean -> {
                if (showConfig) {
                    Box(
                        modifier = Modifier.padding(start = 8.dp).then(styles?.count ?: Modifier)
                    ) {
                        val countText = when {
                            effectiveCount.exceedFormatter != null && isCountExceeded ->
                                effectiveCount.exceedFormatter.invoke(currentValue, effectiveCount)

                            effectiveCount.max != null -> "$charCount/${effectiveCount.max}"
                            else -> charCount.toString()
                        }

                        Text(
                            text = countText,
                            fontSize = 12.sp,
                            color = if (isCountExceeded) theme.token.colorError else Color(0xFFBFBFBF)
                        )
                    }
                }
            }

            is Function1<*, *> -> {
                // Custom formatter function
                @Suppress("UNCHECKED_CAST")
                val formatter = showConfig as? ((CountInfo) -> @Composable () -> Unit)
                if (formatter != null) {
                    val countInfo = CountInfo(currentValue, charCount, effectiveCount.max)
                    Box(
                        modifier = Modifier.padding(start = 8.dp).then(styles?.count ?: Modifier)
                    ) {
                        formatter(countInfo).invoke()
                    }
                }
            }
        }
    }
}

/**
 * AntInputSearch - Input component with search button (React Ant Design v5 parity)
 *
 * Supports both controlled and uncontrolled modes.
 *
 * @param value Current input value (controlled mode)
 * @param onValueChange Callback when value changes (controlled mode)
 * @param onSearch Callback when search is triggered (button click or Enter key)
 * @param modifier Modifier for the root element
 * @param defaultValue Initial value for uncontrolled mode
 * @param placeholder Placeholder text
 * @param disabled Whether the input is disabled
 * @param readOnly Whether the input is read-only
 * @param size Size of the input
 * @param status Validation status
 * @param variant Style variant
 * @param loading Show loading indicator in button
 * @param enterButton Show enter button (false = no button, true = icon button, String = text button)
 * @param allowClear Show clear button (Boolean or InputAllowClearConfig)
 * @param prefix Prefix icon or element
 * @param addonBefore Element to show before input
 * @param maxLength Maximum character length
 * @param showCount Show character count (Boolean or custom formatter)
 * @param count Advanced count configuration
 * @param styles Semantic styles (v5.4.0+)
 * @param classNames Semantic class names (v5.4.0+)
 * @param ref InputRef for imperative methods
 * @param onCompositionStart IME composition start callback
 * @param onCompositionUpdate IME composition update callback
 * @param onCompositionEnd IME composition end callback
 * @param onFocus Focus callback
 * @param onBlur Blur callback
 */
@Composable
fun AntInputSearch(
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    variant: InputVariant = InputVariant.Outlined,
    loading: Boolean = false,
    enterButton: Any = false, // Boolean or String or @Composable
    allowClear: Any = false, // Boolean or AllowClearConfig
    prefix: (@Composable () -> Unit)? = null,
    addonBefore: (@Composable () -> Unit)? = null,
    maxLength: Int? = null,
    showCount: Any = false,
    count: InputCountConfig? = null,
    styles: InputStyles? = null,
    classNames: InputClassNames? = null,
    ref: InputRef? = null,
    onCompositionStart: ((CompositionEvent) -> Unit)? = null,
    onCompositionUpdate: ((CompositionEvent) -> Unit)? = null,
    onCompositionEnd: ((CompositionEvent) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val theme = useTheme()

    // Handle controlled vs uncontrolled state
    var internalValue by remember { mutableStateOf(defaultValue ?: "") }
    val currentValue = value ?: internalValue
    val handleValueChange: (String) -> Unit = { newValue ->
        if (value == null) {
            internalValue = newValue
        }
        onValueChange?.invoke(newValue)
    }

    val searchButton: @Composable () -> Unit = {
        val buttonSize = when (size) {
            InputSize.Large -> ButtonSize.Large
            InputSize.Middle -> ButtonSize.Middle
            InputSize.Small -> ButtonSize.Small
        }

        val buttonVariant = when {
            variant == InputVariant.Borderless || variant == InputVariant.Filled -> ButtonVariant.Text
            enterButton is String || enterButton == true -> ButtonVariant.Solid
            else -> ButtonVariant.Outlined
        }

        AntButton(
            onClick = { onSearch(currentValue) },
            type = if (enterButton is String || enterButton == true) ButtonType.Primary else ButtonType.Default,
            variant = buttonVariant,
            size = buttonSize,
            disabled = disabled,
            loading = ButtonLoading.fromBoolean(loading),
            icon = if (enterButton == true || enterButton == false) {
                { Text("\uD83D\uDD0D", fontSize = 14.sp) } // Search icon
            } else null
        ) {
            if (enterButton is String) {
                Text(enterButton)
            } else if (enterButton == true) {
                // Icon only button - no text
            }
        }
    }

    AntInput(
        value = currentValue,
        onValueChange = handleValueChange,
        modifier = modifier,
        placeholder = placeholder,
        disabled = disabled,
        readOnly = readOnly,
        size = size,
        status = status,
        variant = variant,
        prefix = prefix,
        allowClear = allowClear,
        addonBefore = addonBefore,
        addonAfter = if (enterButton != false) searchButton else null,
        maxLength = maxLength,
        showCount = showCount,
        count = count,
        styles = styles,
        classNames = classNames,
        ref = ref,
        onPressEnter = { onSearch(currentValue) },
        onCompositionStart = onCompositionStart,
        onCompositionUpdate = onCompositionUpdate,
        onCompositionEnd = onCompositionEnd,
        onFocus = onFocus,
        onBlur = onBlur,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(currentValue) }
        )
    )
}

/**
 * AntInputPassword - Input component with password visibility toggle (React Ant Design v5 parity)
 *
 * Supports both controlled and uncontrolled modes.
 *
 * @param value Current input value (controlled mode)
 * @param onValueChange Callback when value changes (controlled mode)
 * @param modifier Modifier for the root element
 * @param defaultValue Initial value for uncontrolled mode
 * @param placeholder Placeholder text
 * @param disabled Whether the input is disabled
 * @param readOnly Whether the input is read-only
 * @param size Size of the input
 * @param status Validation status
 * @param variant Style variant
 * @param visibilityToggle Show visibility toggle (Boolean or VisibilityToggle config for controlled mode)
 * @param iconRender Custom icon renderer for visibility states (v4.3.0+)
 * @param prefix Prefix icon or element
 * @param suffix Suffix icon or element
 * @param allowClear Show clear button (Boolean or InputAllowClearConfig)
 * @param maxLength Maximum character length
 * @param showCount Show character count (Boolean or custom formatter)
 * @param count Advanced count configuration
 * @param styles Semantic styles (v5.4.0+)
 * @param classNames Semantic class names (v5.4.0+)
 * @param ref InputRef for imperative methods
 * @param onPressEnter Enter key callback
 * @param onCompositionStart IME composition start callback
 * @param onCompositionUpdate IME composition update callback
 * @param onCompositionEnd IME composition end callback
 * @param onFocus Focus callback
 * @param onBlur Blur callback
 */
@Composable
fun AntInputPassword(
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    defaultValue: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    variant: InputVariant = InputVariant.Outlined,
    visibilityToggle: Any = true, // Boolean or VisibilityToggle
    iconRender: (@Composable (visible: Boolean) -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    allowClear: Any = false, // Boolean or AllowClearConfig
    maxLength: Int? = null,
    showCount: Any = false,
    count: InputCountConfig? = null,
    styles: InputStyles? = null,
    classNames: InputClassNames? = null,
    ref: InputRef? = null,
    onPressEnter: (() -> Unit)? = null,
    onCompositionStart: ((CompositionEvent) -> Unit)? = null,
    onCompositionUpdate: ((CompositionEvent) -> Unit)? = null,
    onCompositionEnd: ((CompositionEvent) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    // Determine if visibility is controlled
    val toggleConfig = visibilityToggle as? VisibilityToggle
    val isControlled = toggleConfig?.visible != null

    var passwordVisible by remember {
        mutableStateOf(toggleConfig?.visible ?: false)
    }

    // Sync controlled state
    LaunchedEffect(toggleConfig?.visible) {
        if (isControlled && toggleConfig?.visible != null) {
            passwordVisible = toggleConfig.visible
        }
    }

    val visibilityIcon: @Composable () -> Unit = {
        // Show toggle if visibilityToggle is true or a VisibilityToggle object
        val shouldShowToggle = visibilityToggle == true || visibilityToggle is VisibilityToggle

        if (shouldShowToggle) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        enabled = !disabled && !readOnly,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val newVisible = !passwordVisible
                        if (!isControlled) {
                            passwordVisible = newVisible
                        }
                        toggleConfig?.onVisibleChange?.invoke(newVisible)
                    }
            ) {
                if (iconRender != null) {
                    iconRender(passwordVisible)
                } else {
                    // Default eye icons
                    Text(
                        text = if (passwordVisible) "\uD83D\uDC41" else "\uD83D\uDEAB\uD83D\uDC41", // Eye / Eye-slash
                        fontSize = 14.sp,
                        color = Color(0xFFBFBFBF)
                    )
                }
            }
        }
    }

    // Combine suffix with visibility icon
    val combinedSuffix: @Composable () -> Unit = {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            visibilityIcon()
            suffix?.invoke()
        }
    }

    AntInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        defaultValue = defaultValue,
        placeholder = placeholder,
        disabled = disabled,
        readOnly = readOnly,
        size = size,
        status = status,
        variant = variant,
        prefix = prefix,
        suffix = combinedSuffix,
        allowClear = allowClear,
        maxLength = maxLength,
        showCount = showCount,
        count = count,
        styles = styles,
        classNames = classNames,
        ref = ref,
        password = !passwordVisible,
        onPressEnter = onPressEnter,
        onCompositionStart = onCompositionStart,
        onCompositionUpdate = onCompositionUpdate,
        onCompositionEnd = onCompositionEnd,
        onFocus = onFocus,
        onBlur = onBlur,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}

/**
 * AntInputGroup - Group multiple inputs horizontally (React Ant Design v5 parity)
 *
 * @param modifier Modifier for the root element
 * @param size Default size for child inputs
 * @param compact Whether to use compact mode (fused borders)
 * @param content Content lambda with RowScope for child inputs
 */
@Composable
fun AntInputGroup(
    modifier: Modifier = Modifier,
    size: InputSize = InputSize.Middle,
    compact: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (compact) Arrangement.spacedBy((-1).dp) else Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * AntTextArea - Multi-line text input component (React Ant Design v5 parity)
 *
 * Supports both controlled and uncontrolled modes.
 *
 * @param value Current input value (controlled mode)
 * @param onValueChange Callback when value changes (controlled mode)
 * @param modifier Modifier for the root element
 * @param defaultValue Initial value for uncontrolled mode
 * @param placeholder Placeholder text
 * @param disabled Whether the textarea is disabled
 * @param readOnly Whether the textarea is read-only
 * @param status Validation status
 * @param variant Style variant (supports Underlined in v5.24.0+)
 * @param rows Number of rows (when autoSize is false)
 * @param maxLength Maximum character length
 * @param showCount Show character count (Boolean or custom formatter)
 * @param count Advanced count configuration
 * @param autoSize Auto-resize based on content
 * @param minRows Minimum number of rows (when autoSize is true)
 * @param maxRows Maximum number of rows (when autoSize is true)
 * @param styles Semantic styles (v5.4.0+)
 * @param classNames Semantic class names (v5.4.0+)
 * @param ref InputRef for imperative methods
 * @param onResize Callback when textarea is resized
 * @param onCompositionStart IME composition start callback
 * @param onCompositionUpdate IME composition update callback
 * @param onCompositionEnd IME composition end callback
 * @param onFocus Focus callback
 * @param onBlur Blur callback
 */
@Composable
fun AntTextArea(
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    defaultValue: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    status: InputStatus = InputStatus.Default,
    variant: InputVariant = InputVariant.Outlined,
    rows: Int = 3,
    maxLength: Int? = null,
    showCount: Any = false, // Boolean or ((CountInfo) -> @Composable () -> Unit)
    count: InputCountConfig? = null,
    autoSize: Boolean = false,
    minRows: Int = 3,
    maxRows: Int? = null,
    styles: TextAreaStyles? = null,
    classNames: TextAreaClassNames? = null,
    ref: InputRef? = null,
    onResize: ((width: Dp, height: Dp) -> Unit)? = null,
    onCompositionStart: ((CompositionEvent) -> Unit)? = null,
    onCompositionUpdate: ((CompositionEvent) -> Unit)? = null,
    onCompositionEnd: ((CompositionEvent) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val theme = useTheme()

    // Handle controlled vs uncontrolled state
    var internalValue by remember { mutableStateOf(defaultValue ?: "") }
    val currentValue = value ?: internalValue
    val handleValueChange: (String) -> Unit = { newValue ->
        if (value == null) {
            internalValue = newValue
        }
        onValueChange?.invoke(newValue)
    }

    var isFocused by remember { mutableStateOf(false) }
    var isComposing by remember { mutableStateOf(false) }

    // Attach focus requester if ref is provided
    val focusRequester = ref?.focusRequester ?: remember { FocusRequester() }

    // Determine the effective count configuration
    val effectiveCount = count ?: when (showCount) {
        is Boolean -> if (showCount) InputCountConfig(show = true, max = maxLength) else null
        else -> InputCountConfig(show = showCount, max = maxLength)
    }

    // Calculate character count using custom strategy if provided
    val charCount = remember(currentValue, effectiveCount) {
        effectiveCount?.strategy?.invoke(currentValue) ?: currentValue.length
    }

    // Determine if count exceeds max
    val isCountExceeded = effectiveCount?.max?.let { charCount > it } ?: false

    // Calculate dynamic height for autoSize
    val lineCount = remember(currentValue) {
        if (autoSize) {
            val lines = currentValue.count { it == '\n' } + 1
            when {
                maxRows != null -> lines.coerceIn(minRows, maxRows)
                else -> lines.coerceAtLeast(minRows)
            }
        } else {
            rows
        }
    }

    val borderColor = when {
        variant == InputVariant.Borderless || variant == InputVariant.Underlined -> Color.Transparent
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        isFocused -> theme.components.input.activeBorderColor
        disabled -> Color(0xFFD9D9D9)
        else -> theme.components.input.borderColor
    }

    val backgroundColor = when {
        disabled -> Color(0xFFF5F5F5)
        variant == InputVariant.Filled -> Color(0xFFFAFAFA)
        variant == InputVariant.Borderless || variant == InputVariant.Underlined -> Color.Transparent
        else -> Color.White
    }

    val borderWidth = when {
        variant == InputVariant.Borderless -> 0.dp
        variant == InputVariant.Underlined && !isFocused -> 1.dp
        variant == InputVariant.Underlined && isFocused -> 2.dp
        isFocused -> 2.dp
        else -> 1.dp
    }

    // Custom shape for underlined variant
    val shape = when (variant) {
        InputVariant.Underlined -> RoundedCornerShape(0.dp)
        else -> RoundedCornerShape(theme.token.borderRadius)
    }

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = when {
                variant == InputVariant.Underlined && borderWidth > 0.dp ->
                    BorderStroke(borderWidth, borderColor)

                borderWidth > 0.dp -> BorderStroke(borderWidth, borderColor)
                else -> null
            },
            shape = shape
        ) {
            BasicTextField(
                value = currentValue,
                onValueChange = { newValue ->
                    // Respect maxLength unless using custom count strategy
                    val shouldUpdate = if (effectiveCount?.strategy != null) {
                        effectiveCount.max?.let { max ->
                            (effectiveCount.strategy.invoke(newValue)) <= max
                        } ?: true
                    } else {
                        maxLength == null || newValue.length <= maxLength
                    }

                    if (shouldUpdate) {
                        handleValueChange(newValue)
                        if (isComposing) {
                            onCompositionUpdate?.invoke(CompositionEvent(newValue, true))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = (lineCount * 24).dp)
                    .padding(12.dp)
                    .focusRequester(focusRequester)
                    .then(styles?.textarea ?: Modifier)
                    .onFocusChanged {
                        isFocused = it.isFocused
                        if (it.isFocused) {
                            onFocus?.invoke()
                        } else {
                            onBlur?.invoke()
                            if (isComposing) {
                                isComposing = false
                                onCompositionEnd?.invoke(CompositionEvent(currentValue, false))
                            }
                        }
                    },
                enabled = !disabled,
                readOnly = readOnly,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = if (disabled) Color.Gray else theme.token.colorTextBase,
                    lineHeight = 24.sp
                ),
                cursorBrush = SolidColor(theme.token.colorPrimary),
                decorationBox = { innerTextField ->
                    Box {
                        if (currentValue.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color(0xFFBFBFBF)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        // Render count
        if (effectiveCount != null) {
            when (val showConfig = effectiveCount.show) {
                is Boolean -> {
                    if (showConfig) {
                        val countText = when {
                            effectiveCount.exceedFormatter != null && isCountExceeded ->
                                effectiveCount.exceedFormatter.invoke(currentValue, effectiveCount)

                            effectiveCount.max != null -> "$charCount/${effectiveCount.max}"
                            else -> charCount.toString()
                        }

                        Text(
                            text = countText,
                            fontSize = 12.sp,
                            color = if (isCountExceeded) theme.token.colorError else Color(0xFFBFBFBF),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp)
                                .then(styles?.count ?: Modifier)
                        )
                    }
                }

                is Function1<*, *> -> {
                    // Custom formatter function
                    @Suppress("UNCHECKED_CAST")
                    val formatter = showConfig as? ((CountInfo) -> @Composable () -> Unit)
                    if (formatter != null) {
                        val countInfo = CountInfo(currentValue, charCount, effectiveCount.max)
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp)
                                .then(styles?.count ?: Modifier)
                        ) {
                            formatter(countInfo).invoke()
                        }
                    }
                }
            }
        }
    }
}
