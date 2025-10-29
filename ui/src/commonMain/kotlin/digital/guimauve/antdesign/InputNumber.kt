package digital.guimauve.antdesign

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.round

/**
 * StepType - Direction of step operation
 */
enum class StepType {
    Up,
    Down
}

/**
 * StepInfo - Information about a step operation
 *
 * @param offset The step offset value
 * @param type The direction of the step (Up or Down)
 */
data class StepInfo(
    val offset: Double,
    val type: StepType,
)

/**
 * ControlsPosition - Position of the stepper controls
 */
enum class ControlsPosition {
    Right,  // Default: up/down buttons on right side
    Left    // Up/down buttons on left side
}

/**
 * ControlsConfig - Configuration for custom stepper controls
 *
 * @param upIcon Custom icon for increment button
 * @param downIcon Custom icon for decrement button
 * @param position Position of controls (Right or Left)
 */
data class ControlsConfig(
    val upIcon: (@Composable () -> Unit)? = null,
    val downIcon: (@Composable () -> Unit)? = null,
    val position: ControlsPosition = ControlsPosition.Right,
)

/**
 * InputNumberClassNames - Semantic class names for InputNumber sub-elements
 *
 * @param input Custom class name for the input field
 * @param prefix Custom class name for the prefix element
 * @param suffix Custom class name for the suffix element
 * @param group Custom class name for the group wrapper
 * @param wrapper Custom class name for the wrapper element
 * @param affixWrapper Custom class name for the affix wrapper
 */
data class InputNumberClassNames(
    val input: String? = null,
    val prefix: String? = null,
    val suffix: String? = null,
    val group: String? = null,
    val wrapper: String? = null,
    val affixWrapper: String? = null,
)

/**
 * InputNumberStyles - Semantic styles for InputNumber sub-elements
 *
 * @param input Custom modifier for the input field
 * @param prefix Custom modifier for the prefix element
 * @param suffix Custom modifier for the suffix element
 * @param group Custom modifier for the group wrapper
 * @param wrapper Custom modifier for the wrapper element
 * @param affixWrapper Custom modifier for the affix wrapper
 */
data class InputNumberStyles(
    val input: Modifier? = null,
    val prefix: Modifier? = null,
    val suffix: Modifier? = null,
    val group: Modifier? = null,
    val wrapper: Modifier? = null,
    val affixWrapper: Modifier? = null,
)

/**
 * AntInputNumber - Number input component with full React Ant Design v5.x parity
 *
 * A numeric input component with increment/decrement controls, validation,
 * formatting, and comprehensive keyboard/mouse support.
 *
 * ## Features
 * - Controlled and uncontrolled modes
 * - Min/max constraints with auto-correction
 * - Custom precision and decimal separator
 * - Formatter/parser for display customization
 * - Stepper controls (customizable position and icons)
 * - Keyboard support (arrow keys, enter)
 * - Mouse wheel support (optional)
 * - Long press for continuous stepping
 * - String mode for large numbers (no precision loss)
 * - Addons and affixes support
 * - Validation status (error/warning)
 * - Multiple size variants
 * - Full theme integration
 *
 * @param value Controlled value (null for empty input)
 * @param onValueChange Callback when value changes
 * @param modifier Modifier to be applied to the component
 * @param defaultValue Default value for uncontrolled mode
 * @param onChange Callback when value changes (with nullable Double)
 * @param onPressEnter Callback when Enter key is pressed
 * @param onStep Callback when step buttons are clicked with direction info
 * @param min Minimum allowed value (default: negative infinity)
 * @param max Maximum allowed value (default: positive infinity)
 * @param step Step increment/decrement value (default: 1.0)
 * @param precision Decimal precision (null = auto-detect from step)
 * @param decimalSeparator Decimal separator character (default: ".")
 * @param formatter Custom formatter for display value
 * @param parser Custom parser to extract number from formatted string
 * @param disabled Disable the input
 * @param readOnly Read-only mode (no editing, but not disabled visually)
 * @param keyboard Enable keyboard input (default: true)
 * @param controls Show/hide/configure stepper controls
 * @param addonBefore Composable rendered before the input (outside wrapper)
 * @param addonAfter Composable rendered after the input (outside wrapper)
 * @param prefix Composable rendered inside input before text
 * @param suffix Composable rendered inside input after text
 * @param size Component size (Small/Middle/Large)
 * @param status Validation status (Default/Error/Warning)
 * @param placeholder Placeholder text when empty
 * @param bordered Show border (default: true)
 * @param autoFocus Auto-focus on mount
 * @param stringMode String mode for large numbers (prevents precision loss)
 * @param changeOnWheel Enable mouse wheel to change value (default: false)
 * @param changeOnBlur Trigger onChange on blur (default: true)
 * @param classNames Semantic class names for sub-elements
 * @param styles Semantic styles for sub-elements
 * @param onFocus Callback when input gains focus
 * @param onBlur Callback when input loses focus
 *
 * @since 5.0.0
 * @see <a href="https://ant.design/components/input-number">Ant Design InputNumber</a>
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AntInputNumber(
    value: Double?,
    onValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: Double? = null,
    onChange: ((Double?) -> Unit)? = null,
    onPressEnter: (() -> Unit)? = null,
    onStep: ((Double, StepInfo) -> Unit)? = null,
    min: Double = Double.NEGATIVE_INFINITY,
    max: Double = Double.POSITIVE_INFINITY,
    step: Double = 1.0,
    precision: Int? = null,
    decimalSeparator: String = ".",
    formatter: ((Double?) -> String)? = null,
    parser: ((String) -> Double?)? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    keyboard: Boolean = true,
    controls: Any = true, // Boolean or ControlsConfig
    addonBefore: (@Composable () -> Unit)? = null,
    addonAfter: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    size: ComponentSize = ComponentSize.Middle,
    status: InputStatus = InputStatus.Default,
    placeholder: String = "",
    bordered: Boolean = true,
    autoFocus: Boolean = false,
    stringMode: Boolean = false,
    changeOnWheel: Boolean = false,
    changeOnBlur: Boolean = true,
    classNames: InputNumberClassNames? = null,
    styles: InputNumberStyles? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    // Get theme and config
    val config = useConfig()
    val theme = useTheme()

    // State management for controlled/uncontrolled mode
    var internalValue by remember(value) { mutableStateOf(value ?: defaultValue) }
    val effectiveValue = value ?: internalValue

    // Input text state (separate from numeric value for formatting)
    var inputText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var isComposing by remember { mutableStateOf(false) }

    // Focus management
    val focusRequester = remember { FocusRequester() }

    // Auto-detect precision from step if not specified
    val effectivePrecision = remember(precision, step) {
        precision ?: detectPrecisionFromStep(step)
    }

    // Format value for display
    val formatValue: (Double?) -> String = remember(formatter, effectivePrecision, decimalSeparator) {
        { v ->
            if (v == null) {
                ""
            } else if (formatter != null) {
                formatter(v)
            } else {
                formatNumber(v, effectivePrecision, decimalSeparator, stringMode)
            }
        }
    }

    // Parse input string to number
    val parseValue: (String) -> Double? = remember(parser, decimalSeparator) {
        { text ->
            if (text.isEmpty() || text == "-") {
                null
            } else if (parser != null) {
                parser(text)
            } else {
                parseNumber(text, decimalSeparator)
            }
        }
    }

    // Update input text when value changes (but not while typing)
    LaunchedEffect(effectiveValue, isFocused) {
        if (!isFocused) {
            inputText = formatValue(effectiveValue)
        }
    }

    // Auto focus
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Validate and constrain value to min/max
    val constrainValue: (Double?) -> Double? = remember(min, max) {
        { v ->
            v?.let {
                when {
                    it < min -> min
                    it > max -> max
                    else -> it
                }
            }
        }
    }

    // Apply precision rounding
    val roundToPrecision: (Double) -> Double = remember(effectivePrecision) {
        { v -> roundToStep(v, effectivePrecision) }
    }

    // Check if increment/decrement is possible
    val canIncrement = effectiveValue?.let { it < max } ?: (min < max)
    val canDecrement = effectiveValue?.let { it > min } ?: (min < max)

    // Update value helper
    val updateValue: (Double?, Boolean) -> Unit = { newValue, triggerOnChange ->
        val constrained = constrainValue(newValue)?.let { roundToPrecision(it) }

        if (value == null) {
            // Uncontrolled mode
            internalValue = constrained
        }

        onValueChange(constrained)

        if (triggerOnChange) {
            onChange?.invoke(constrained)
        }
    }

    // Step up/down handlers
    val stepUp: () -> Unit = {
        val currentValue = effectiveValue ?: 0.0
        val newValue = currentValue + step
        val constrained = constrainValue(newValue)?.let { roundToPrecision(it) }

        if (constrained != null && constrained != effectiveValue) {
            updateValue(constrained, true)
            onStep?.invoke(constrained, StepInfo(step, StepType.Up))
        }
    }

    val stepDown: () -> Unit = {
        val currentValue = effectiveValue ?: 0.0
        val newValue = currentValue - step
        val constrained = constrainValue(newValue)?.let { roundToPrecision(it) }

        if (constrained != null && constrained != effectiveValue) {
            updateValue(constrained, true)
            onStep?.invoke(constrained, StepInfo(step, StepType.Down))
        }
    }

    // Handle blur validation
    val handleBlur: () -> Unit = {
        isFocused = false
        onBlur?.invoke()

        // Parse and validate current input text
        val parsed = parseValue(inputText)
        val validated = constrainValue(parsed)?.let { roundToPrecision(it) }

        if (changeOnBlur && validated != effectiveValue) {
            updateValue(validated, true)
        } else if (validated != null) {
            updateValue(validated, false)
        }

        // Update display text with formatted value
        inputText = formatValue(validated)
    }

    // Determine control configuration
    val controlsConfig = when (controls) {
        is ControlsConfig -> controls
        true -> ControlsConfig()
        else -> null
    }

    // Calculate padding based on size
    val padding = when (size) {
        ComponentSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ComponentSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ComponentSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    val fontSize = when (size) {
        ComponentSize.Large -> 16.sp
        ComponentSize.Middle -> 14.sp
        ComponentSize.Small -> 12.sp
    }

    val controlsSize = when (size) {
        ComponentSize.Large -> 18.dp
        ComponentSize.Middle -> 16.dp
        ComponentSize.Small -> 14.dp
    }

    // Determine border and background colors
    val borderColor = when {
        !bordered -> Color.Transparent
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        isFocused -> config.theme.components.input.activeBorderColor
        disabled -> Color(0xFFD9D9D9)
        else -> config.theme.components.input.borderColor
    }

    val backgroundColor = when {
        disabled -> Color(0xFFF5F5F5)
        readOnly -> Color(0xFFFAFAFA)
        else -> Color.White
    }

    val borderWidth = when {
        !bordered -> 0.dp
        isFocused -> 2.dp
        else -> 1.dp
    }

    // Main layout
    Row(
        modifier = modifier.then(styles?.wrapper ?: Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Addon before
        if (addonBefore != null) {
            Box(modifier = Modifier.padding(end = 8.dp)) {
                addonBefore()
            }
        }

        // Main input with controls
        Card(
            modifier = Modifier
                .weight(1f)
                .then(styles?.affixWrapper ?: Modifier)
                .onFocusChanged {
                    if (it.isFocused && !isFocused) {
                        isFocused = true
                        onFocus?.invoke()
                        // When focusing, show current value or empty
                        inputText = formatValue(effectiveValue)
                    } else if (!it.isFocused && isFocused) {
                        handleBlur()
                    }
                }
                .let { mod ->
                    // Add mouse wheel support if enabled
                    if (changeOnWheel && !disabled && !readOnly) {
                        mod.pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    if (event.type == PointerEventType.Scroll && isFocused) {
                                        val change = event.changes.firstOrNull()
                                        val scrollDelta = change?.scrollDelta?.y ?: 0f

                                        if (scrollDelta < 0) {
                                            stepUp()
                                        } else if (scrollDelta > 0) {
                                            stepDown()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        mod
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = if (borderWidth > 0.dp) BorderStroke(borderWidth, borderColor) else null,
            shape = RoundedCornerShape(theme.token.borderRadius)
        ) {
            Row(
                modifier = Modifier
                    .padding(padding)
                    .then(styles?.group ?: Modifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Controls on left (if configured)
                if (controlsConfig != null && controlsConfig.position == ControlsPosition.Left) {
                    InputNumberControls(
                        onUp = stepUp,
                        onDown = stepDown,
                        canIncrement = canIncrement && !disabled && !readOnly,
                        canDecrement = canDecrement && !disabled && !readOnly,
                        config = controlsConfig,
                        size = controlsSize,
                        disabled = disabled
                    )
                }

                // Prefix
                if (prefix != null) {
                    Box(modifier = styles?.prefix ?: Modifier) {
                        prefix()
                    }
                }

                // Text input field
                BasicTextField(
                    value = inputText,
                    onValueChange = { newText ->
                        if (keyboard && !readOnly) {
                            inputText = newText

                            // Try to parse and update value in real-time
                            val parsed = parseValue(newText)
                            if (parsed != null) {
                                val constrained = constrainValue(parsed)
                                if (!changeOnBlur) {
                                    // Update immediately if changeOnBlur is false
                                    updateValue(constrained, true)
                                } else {
                                    // Update without triggering onChange (will trigger on blur)
                                    updateValue(constrained, false)
                                }
                            } else if (newText.isEmpty() || newText == "-") {
                                // Allow empty or negative sign
                                if (!changeOnBlur) {
                                    updateValue(null, true)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .then(styles?.input ?: Modifier)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && !disabled && !readOnly) {
                                when (keyEvent.key) {
                                    Key.DirectionUp -> {
                                        stepUp()
                                        true
                                    }

                                    Key.DirectionDown -> {
                                        stepDown()
                                        true
                                    }

                                    Key.Enter, Key.NumPadEnter -> {
                                        onPressEnter?.invoke()
                                        true
                                    }

                                    else -> false
                                }
                            } else {
                                false
                            }
                        },
                    enabled = !disabled,
                    readOnly = readOnly || !keyboard,
                    textStyle = TextStyle(
                        fontSize = fontSize,
                        color = if (disabled) Color.Gray else theme.token.colorTextBase,
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onPressEnter?.invoke()
                        }
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(theme.token.colorPrimary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (inputText.isEmpty() && placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontSize = fontSize,
                                        color = Color(0xFFBFBFBF)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Suffix (only show if controls are not on the right or controls are hidden)
                if (suffix != null && (controlsConfig == null || controlsConfig.position == ControlsPosition.Left)) {
                    Box(modifier = styles?.suffix ?: Modifier) {
                        suffix()
                    }
                }

                // Controls on right (default position)
                if (controlsConfig != null && controlsConfig.position == ControlsPosition.Right) {
                    InputNumberControls(
                        onUp = stepUp,
                        onDown = stepDown,
                        canIncrement = canIncrement && !disabled && !readOnly,
                        canDecrement = canDecrement && !disabled && !readOnly,
                        config = controlsConfig,
                        size = controlsSize,
                        disabled = disabled
                    )
                }
            }
        }

        // Addon after
        if (addonAfter != null) {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                addonAfter()
            }
        }
    }
}

/**
 * InputNumberControls - Stepper controls for increment/decrement
 *
 * Supports:
 * - Click to step once
 * - Long press to continuously step
 * - Custom icons
 * - Disabled states
 */
@Composable
private fun InputNumberControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    canIncrement: Boolean,
    canDecrement: Boolean,
    config: ControlsConfig,
    size: Dp,
    disabled: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    var isPressingUp by remember { mutableStateOf(false) }
    var isPressingDown by remember { mutableStateOf(false) }

    // Long press handler for continuous stepping
    LaunchedEffect(isPressingUp) {
        if (isPressingUp && canIncrement) {
            delay(500) // Initial delay before repeating
            while (isPressingUp && canIncrement) {
                onUp()
                delay(100) // Repeat interval
            }
        }
    }

    LaunchedEffect(isPressingDown) {
        if (isPressingDown && canDecrement) {
            delay(500) // Initial delay before repeating
            while (isPressingDown && canDecrement) {
                onDown()
                delay(100) // Repeat interval
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Up button
        IconButton(
            onClick = {
                if (canIncrement) {
                    onUp()
                }
            },
            enabled = canIncrement && !disabled,
            modifier = Modifier
                .size(size)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (canIncrement) {
                                isPressingUp = true
                                tryAwaitRelease()
                                isPressingUp = false
                            }
                        }
                    )
                }
        ) {
            if (config.upIcon != null) {
                config.upIcon.invoke()
            } else {
                Text(
                    "▲",
                    fontSize = (size.value * 0.5).sp,
                    color = if (canIncrement && !disabled) Color(0xFF595959) else Color(0xFFBFBFBF)
                )
            }
        }

        // Down button
        IconButton(
            onClick = {
                if (canDecrement) {
                    onDown()
                }
            },
            enabled = canDecrement && !disabled,
            modifier = Modifier
                .size(size)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (canDecrement) {
                                isPressingDown = true
                                tryAwaitRelease()
                                isPressingDown = false
                            }
                        }
                    )
                }
        ) {
            if (config.downIcon != null) {
                config.downIcon.invoke()
            } else {
                Text(
                    "▼",
                    fontSize = (size.value * 0.5).sp,
                    color = if (canDecrement && !disabled) Color(0xFF595959) else Color(0xFFBFBFBF)
                )
            }
        }
    }
}

/**
 * Detect decimal precision from step value
 * e.g., step=0.01 -> precision=2, step=1 -> precision=0
 */
private fun detectPrecisionFromStep(step: Double): Int {
    if (step >= 1.0) return 0

    val stepString = step.toString()
    val decimalIndex = stepString.indexOf('.')

    return if (decimalIndex >= 0) {
        val decimalPart = stepString.substring(decimalIndex + 1)
        // Remove trailing zeros
        decimalPart.trimEnd('0').length
    } else {
        0
    }
}

/**
 * Format number with precision and decimal separator
 */
private fun formatNumber(
    value: Double,
    precision: Int,
    decimalSeparator: String,
    stringMode: Boolean,
): String {
    if (stringMode) {
        // In string mode, preserve full precision for large numbers
        return value.toString().replace(".", decimalSeparator)
    }

    return if (precision > 0) {
        val multiplier = 10.0.pow(precision.toDouble())
        val rounded = round(value * multiplier) / multiplier

        // Format with fixed decimal places
        val formatted = "%.${precision}f".format(rounded)
        formatted.replace(".", decimalSeparator)
    } else {
        // No decimals
        round(value).toLong().toString()
    }
}

/**
 * Parse number from string with custom decimal separator
 */
private fun parseNumber(text: String, decimalSeparator: String): Double? {
    return try {
        val normalized = text.replace(decimalSeparator, ".")
        normalized.toDoubleOrNull()
    } catch (e: Exception) {
        null
    }
}

/**
 * Round value to precision
 */
private fun roundToStep(value: Double, precision: Int): Double {
    if (precision <= 0) {
        return round(value)
    }

    val multiplier = 10.0.pow(precision.toDouble())
    return round(value * multiplier) / multiplier
}

/**
 * Simplified AntInputNumber for basic use cases
 *
 * @param value Current value
 * @param onValueChange Callback when value changes
 * @param modifier Modifier to be applied
 * @param min Minimum value
 * @param max Maximum value
 * @param step Step value
 * @param precision Decimal precision
 * @param placeholder Placeholder text
 * @param disabled Disable input
 * @param size Component size
 */
@Composable
fun AntInputNumber(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    placeholder: String = "",
    disabled: Boolean = false,
    size: ComponentSize = ComponentSize.Middle,
) {
    AntInputNumber(
        value = value?.toDouble(),
        onValueChange = { onValueChange(it?.toInt()) },
        modifier = modifier,
        min = min.toDouble(),
        max = max.toDouble(),
        step = step.toDouble(),
        precision = 0,
        placeholder = placeholder,
        disabled = disabled,
        size = size
    )
}

/**
 * Example usage demonstrating all features
 *
 * ```kotlin
 * // Basic usage
 * var value by remember { mutableStateOf(0.0) }
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 }
 * )
 *
 * // With constraints and precision
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     min = 0.0,
 *     max = 100.0,
 *     step = 0.1,
 *     precision = 2
 * )
 *
 * // With formatter (currency)
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     formatter = { v -> v?.let { "$$it" } ?: "" },
 *     parser = { text -> text.removePrefix("$").toDoubleOrNull() }
 * )
 *
 * // With custom controls
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     controls = ControlsConfig(
 *         upIcon = { Icon(Icons.Default.Add, null) },
 *         downIcon = { Icon(Icons.Default.Remove, null) },
 *         position = ControlsPosition.Left
 *     )
 * )
 *
 * // With addons and affixes
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     addonBefore = { Text("$") },
 *     addonAfter = { Text("USD") },
 *     prefix = { Icon(Icons.Default.AttachMoney, null) }
 * )
 *
 * // With validation status
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     status = if (value < 0) InputStatus.Error else InputStatus.Default
 * )
 *
 * // With mouse wheel support
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     changeOnWheel = true
 * )
 *
 * // Read-only mode
 * AntInputNumber(
 *     value = value,
 *     onValueChange = {},
 *     readOnly = true
 * )
 *
 * // String mode for large numbers
 * AntInputNumber(
 *     value = value,
 *     onValueChange = { value = it ?: 0.0 },
 *     stringMode = true,
 *     precision = 10
 * )
 * ```
 */
