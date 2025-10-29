package com.antdesign.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Semantic class names for Radio component parts.
 * Allows customization of specific parts of the Radio component.
 */
data class RadioClassNames(
    val root: String? = null,
    val input: String? = null,
    val inner: String? = null,
    val wrapper: String? = null
)

/**
 * Semantic styles for Radio component parts.
 * Allows customization of Modifier for specific parts of the Radio component.
 */
data class RadioStyles(
    val root: Modifier = Modifier,
    val input: Modifier = Modifier,
    val inner: Modifier = Modifier,
    val wrapper: Modifier = Modifier
)

/**
 * Semantic class names for RadioGroup component parts.
 */
data class RadioGroupClassNames(
    val root: String? = null,
    val wrapper: String? = null
)

/**
 * Semantic styles for RadioGroup component parts.
 */
data class RadioGroupStyles(
    val root: Modifier = Modifier,
    val wrapper: Modifier = Modifier
)

/**
 * Event object for radio change events.
 * Mimics the React RadioChangeEvent structure.
 */
data class RadioChangeEvent(
    val target: RadioChangeEventTarget,
    val nativeEvent: Any? = null
) {
    fun stopPropagation() {
        // Implementation for stopping event propagation
    }

    fun preventDefault() {
        // Implementation for preventing default behavior
    }
}

/**
 * Event target for radio change events.
 * Contains the target state information.
 */
data class RadioChangeEventTarget(
    val checked: Boolean,
    val value: Any,
    val name: String? = null,
    val id: String? = null
)

/**
 * Radio button style variants for RadioGroup
 */
sealed class RadioButtonStyle {
    /** Outlined button style (default) */
    object Outline : RadioButtonStyle()

    /** Solid/filled button style */
    object Solid : RadioButtonStyle()
}

/**
 * Radio option type
 */
sealed class RadioOptionType {
    /** Default radio style with circle indicator */
    object Default : RadioOptionType()

    /** Button style radio */
    object Button : RadioOptionType()
}

/**
 * Size variants for Radio components
 */
sealed class RadioSize {
    object Large : RadioSize()
    object Middle : RadioSize()
    object Small : RadioSize()
}

/**
 * Semantic class names for RadioButton component parts.
 * Allows customization of specific parts of the RadioButton component.
 * @since 5.4.0+
 */
data class RadioButtonClassNames(
    val root: String? = null,
    val button: String? = null,
    val content: String? = null
)

/**
 * Semantic styles for RadioButton component parts.
 * Allows customization of Modifier for specific parts of the RadioButton component.
 * @since 5.4.0+
 */
data class RadioButtonStyles(
    val root: Modifier = Modifier,
    val button: Modifier = Modifier,
    val content: Modifier = Modifier
)

/**
 * Radio option data class for options array.
 * Compatible with CheckboxOptionType structure from Ant Design v5.
 *
 * @param label The label text to display next to the radio
 * @param value The value of the radio option
 * @param disabled If true, this option is disabled
 * @param className CSS class name for this option (v5.25.0+)
 * @param style CSS properties for this option (mapped to Modifier)
 * @param title Title attribute (tooltip text) for this option
 * @param id ID attribute for this option's input element
 * @param required If true, this option is required
 * @param onChange Callback for when this specific option changes
 */
data class RadioOption(
    val label: String,
    val value: Any,
    val disabled: Boolean = false,
    val className: String? = null,
    val style: Modifier = Modifier,
    val title: String? = null,
    val id: String? = null,
    val required: Boolean = false,
    val onChange: ((RadioChangeEvent) -> Unit)? = null
)

/**
 * Focus options for the focus() method.
 * Available in v5.19.0+
 */
data class FocusOptions(
    val cursor: FocusCursor = FocusCursor.All
)

/**
 * Focus cursor position options.
 * @since 5.19.0+
 */
sealed class FocusCursor {
    /** Focus at start position */
    object Start : FocusCursor()

    /** Focus at end position */
    object End : FocusCursor()

    /** Focus and select all (default) */
    object All : FocusCursor()
}

/**
 * Radio component reference for imperative methods.
 * Provides focus() method to programmatically focus the radio element.
 * @since 5.19.0+ (focus with options)
 */
class RadioRef(private val focusRequester: FocusRequester) {
    /**
     * Focus the radio element programmatically.
     * @param options Focus options including cursor position (v5.19.0+)
     */
    fun focus(options: FocusOptions? = null) {
        focusRequester.requestFocus()
        // Note: Cursor positioning is handled by the platform
        // In web, this would set cursor position; in Compose, focus behavior is managed by the system
    }

    /**
     * Remove focus from the radio element.
     */
    fun blur() {
        focusRequester.freeFocus()
    }
}

/**
 * Ant Design Radio Component
 *
 * Radio component for single selection among multiple options.
 *
 * @param value The value of the radio option
 * @param modifier The modifier to be applied to the layout
 * @param autoFocus If true, radio will be focused on mount
 * @param checked Specifies whether the radio is selected (controlled)
 * @param className CSS class name for the root element
 * @param classNames Semantic class names for different parts of the component
 * @param defaultChecked Initial checked state (for uncontrolled component)
 * @param disabled If true, radio is disabled
 * @param id ID attribute for the radio input
 * @param name Name attribute for the radio input (groups radios together)
 * @param onChange Callback for when radio state changes (with event object)
 * @param onBlur Callback for blur events
 * @param onClick Callback for click events
 * @param onFocus Callback for focus events
 * @param onKeyDown Callback for key down events
 * @param onKeyPress Callback for key press events
 * @param onMouseEnter Callback for mouse enter events
 * @param onMouseLeave Callback for mouse leave events
 * @param prefixCls Prefix for CSS class names
 * @param required If true, radio is required
 * @param rootClassName CSS class name for the root wrapper element
 * @param skipGroup If true, radio will not register with RadioGroup
 * @param style CSS properties for the root element (mapped to Modifier)
 * @param styles Semantic styles (Modifiers) for different parts of the component
 * @param tabIndex Tab index for keyboard navigation
 * @param title Title attribute (tooltip text)
 * @param type Input type attribute
 * @param children Content to display next to the radio (label)
 * @param ref Callback to receive RadioRef for imperative methods (v5.19.0+)
 */
@Composable
fun AntRadio(
    value: Any,
    modifier: Modifier = Modifier,
    autoFocus: Boolean = false,
    checked: Boolean? = null,
    className: String? = null,
    classNames: RadioClassNames? = null,
    defaultChecked: Boolean = false,
    disabled: Boolean = false,
    id: String? = null,
    name: String? = null,
    onChange: ((RadioChangeEvent) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onKeyDown: ((KeyEvent) -> Unit)? = null,
    onKeyPress: ((KeyEvent) -> Unit)? = null,
    onMouseEnter: ((PointerEvent) -> Unit)? = null,
    onMouseLeave: ((PointerEvent) -> Unit)? = null,
    prefixCls: String? = null,
    required: Boolean = false,
    rootClassName: String? = null,
    skipGroup: Boolean = false,
    style: Modifier = Modifier,
    styles: RadioStyles? = null,
    tabIndex: Int? = null,
    title: String? = null,
    type: String? = null,
    children: (@Composable () -> Unit)? = null,
    ref: ((RadioRef) -> Unit)? = null
) {
    // State management for uncontrolled component
    var internalChecked by remember { mutableStateOf(defaultChecked) }
    val effectiveChecked = checked ?: internalChecked

    // Focus management
    val focusRequester = remember { FocusRequester() }
    val radioRef = remember { RadioRef(focusRequester) }

    // Invoke ref callback when RadioRef is created
    LaunchedEffect(Unit) {
        ref?.invoke(radioRef)
    }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Combine modifiers
    val rootModifier = modifier
        .then(style)
        .then(styles?.root ?: Modifier)
        .then(if (title != null) Modifier.semantics { contentDescription = title } else Modifier)
        .focusRequester(focusRequester)

    // Handle events with pointer input
    val eventModifier = if (onMouseEnter != null || onMouseLeave != null) {
        Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type.toString()) {
                        "Enter" -> onMouseEnter?.invoke(event)
                        "Exit" -> onMouseLeave?.invoke(event)
                    }
                }
            }
        }
    } else Modifier

    val keyEventModifier = if (onKeyDown != null || onKeyPress != null) {
        Modifier.onKeyEvent { keyEvent ->
            onKeyDown?.invoke(keyEvent)
            onKeyPress?.invoke(keyEvent)
            false
        }
    } else Modifier

    Row(
        modifier = rootModifier
            .then(eventModifier)
            .then(keyEventModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = effectiveChecked,
            onClick = {
                if (!disabled) {
                    val newChecked = true
                    internalChecked = newChecked

                    onClick?.invoke()

                    onChange?.invoke(
                        RadioChangeEvent(
                            target = RadioChangeEventTarget(
                                checked = newChecked,
                                value = value,
                                name = name,
                                id = id
                            )
                        )
                    )
                }
            },
            enabled = !disabled,
            modifier = styles?.input ?: Modifier,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF1890FF),
                unselectedColor = Color(0xFFD9D9D9),
                disabledSelectedColor = Color(0xFFD9D9D9),
                disabledUnselectedColor = Color(0xFFF5F5F5)
            ),
            interactionSource = remember { MutableInteractionSource() }
        )

        if (children != null) {
            Box(modifier = styles?.wrapper ?: Modifier) {
                children()
            }
        }
    }
}

/**
 * Ant Design Radio Group Component
 *
 * Used to select a single state from multiple options.
 *
 * @param modifier The modifier to be applied to the layout
 * @param block If true, the radio group will fill the parent width
 * @param buttonStyle Style for radio buttons when optionType is Button
 * @param children Custom content (can contain individual Radio components)
 * @param className CSS class name for the root element
 * @param classNames Semantic class names for different parts of the component
 * @param defaultValue Initial selected value (for uncontrolled component)
 * @param disabled If true, all radios in the group are disabled
 * @param id ID attribute for the radio group
 * @param name Name attribute passed to all radio inputs
 * @param onChange Callback for when selection changes
 * @param onBlur Callback for blur events
 * @param onFocus Callback for focus events
 * @param onMouseEnter Callback for mouse enter events
 * @param onMouseLeave Callback for mouse leave events
 * @param options Array of options to render radios automatically
 * @param optionType Type of radio rendering (default circle or button style)
 * @param rootClassName CSS class name for the root wrapper element
 * @param size Size of the radio buttons (when optionType is Button)
 * @param style CSS properties for the root element (mapped to Modifier)
 * @param styles Semantic styles (Modifiers) for different parts of the component
 * @param value Currently selected value (controlled)
 */
@Composable
fun AntRadioGroup(
    modifier: Modifier = Modifier,
    block: Boolean = false,
    buttonStyle: RadioButtonStyle = RadioButtonStyle.Outline,
    children: (@Composable () -> Unit)? = null,
    className: String? = null,
    classNames: RadioGroupClassNames? = null,
    defaultValue: Any? = null,
    disabled: Boolean = false,
    id: String? = null,
    name: String? = null,
    onChange: ((RadioChangeEvent) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onMouseEnter: ((PointerEvent) -> Unit)? = null,
    onMouseLeave: ((PointerEvent) -> Unit)? = null,
    options: List<RadioOption>? = null,
    optionType: RadioOptionType = RadioOptionType.Default,
    rootClassName: String? = null,
    size: RadioSize = RadioSize.Middle,
    style: Modifier = Modifier,
    styles: RadioGroupStyles? = null,
    value: Any? = null
) {
    // State management for uncontrolled component
    var internalValue by remember { mutableStateOf(defaultValue) }
    val effectiveValue = value ?: internalValue

    // Combine modifiers
    val rootModifier = modifier
        .then(style)
        .then(styles?.root ?: Modifier)
        .then(if (block) Modifier.fillMaxWidth() else Modifier)

    // Handle pointer events
    val eventModifier = if (onMouseEnter != null || onMouseLeave != null) {
        Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type.toString()) {
                        "Enter" -> onMouseEnter?.invoke(event)
                        "Exit" -> onMouseLeave?.invoke(event)
                    }
                }
            }
        }
    } else Modifier

    Column(
        modifier = rootModifier
            .then(eventModifier)
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (options != null) {
            // Render from options array
            for (option in options) {
                when (optionType) {
                    is RadioOptionType.Button -> {
                        AntRadioButton(
                            value = option.value,
                            label = option.label,
                            selected = effectiveValue == option.value,
                            onClick = {
                                if (!disabled && !option.disabled) {
                                    internalValue = option.value
                                    val event = RadioChangeEvent(
                                        target = RadioChangeEventTarget(
                                            checked = true,
                                            value = option.value,
                                            name = name,
                                            id = option.id ?: id
                                        )
                                    )
                                    // Call option-specific onChange first if provided (v5.25.0+)
                                    option.onChange?.invoke(event)
                                    // Then call group-level onChange
                                    onChange?.invoke(event)
                                }
                            },
                            disabled = disabled || option.disabled,
                            buttonStyle = buttonStyle,
                            size = size,
                            modifier = option.style,
                            className = option.className,
                            title = option.title,
                            id = option.id,
                            required = option.required
                        )
                    }
                    is RadioOptionType.Default -> {
                        Row(
                            modifier = option.style,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AntRadio(
                                value = option.value,
                                checked = effectiveValue == option.value,
                                disabled = disabled || option.disabled,
                                name = name,
                                id = option.id,
                                title = option.title,
                                required = option.required,
                                className = option.className,
                                onChange = { event ->
                                    internalValue = option.value
                                    // Call option-specific onChange first if provided (v5.25.0+)
                                    option.onChange?.invoke(event)
                                    // Then call group-level onChange
                                    onChange?.invoke(event)
                                },
                                children = {
                                    Text(
                                        text = option.label,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            color = if (disabled || option.disabled) Color.Gray else Color.Black
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        } else if (children != null) {
            // Render custom children
            Box(modifier = styles?.wrapper ?: Modifier) {
                children()
            }
        }
    }
}

/**
 * Ant Design Radio Button Component
 *
 * Radio button with button styling instead of traditional radio circle.
 *
 * @param value The value of the radio option
 * @param label The label text to display
 * @param selected Whether this button is selected
 * @param onClick Callback when the button is clicked
 * @param modifier The modifier to be applied to the layout
 * @param disabled If true, the button is disabled
 * @param buttonStyle The style of the button (outline or solid)
 * @param size The size of the button
 * @param className CSS class name for the button
 * @param classNames Semantic class names for different parts (v5.4.0+)
 * @param style CSS properties for the button (mapped to Modifier)
 * @param styles Semantic styles (Modifiers) for different parts (v5.4.0+)
 * @param title Title attribute (tooltip text)
 * @param id ID attribute for the button element
 * @param required If true, the button is required
 */
@Composable
fun AntRadioButton(
    value: Any,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    buttonStyle: RadioButtonStyle = RadioButtonStyle.Outline,
    size: RadioSize = RadioSize.Middle,
    className: String? = null,
    classNames: RadioButtonClassNames? = null,
    style: Modifier = Modifier,
    styles: RadioButtonStyles? = null,
    title: String? = null,
    id: String? = null,
    required: Boolean = false
) {
    val colors = when (buttonStyle) {
        is RadioButtonStyle.Solid -> {
            if (selected) {
                ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1890FF),
                    contentColor = Color.White
                )
            } else {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            }
        }
        is RadioButtonStyle.Outline -> {
            if (selected) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFE6F7FF),
                    contentColor = Color(0xFF1890FF)
                )
            } else {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            }
        }
    }

    val buttonHeight = when (size) {
        is RadioSize.Large -> 40.dp
        is RadioSize.Middle -> 32.dp
        is RadioSize.Small -> 24.dp
    }

    val fontSize = when (size) {
        is RadioSize.Large -> 16.sp
        is RadioSize.Middle -> 14.sp
        is RadioSize.Small -> 12.sp
    }

    val border = when (buttonStyle) {
        is RadioButtonStyle.Solid -> {
            if (selected) null else BorderStroke(1.dp, Color(0xFFD9D9D9))
        }
        is RadioButtonStyle.Outline -> {
            BorderStroke(
                1.dp,
                if (selected) Color(0xFF1890FF) else Color(0xFFD9D9D9)
            )
        }
    }

    // Apply semantic modifiers with proper composition
    val rootModifier = modifier
        .then(style)
        .then(styles?.root ?: Modifier)
        .then(if (title != null) Modifier.semantics { contentDescription = title } else Modifier)

    val buttonModifier = (styles?.button ?: Modifier)
        .height(buttonHeight)

    Button(
        onClick = onClick,
        enabled = !disabled,
        colors = colors,
        border = border,
        modifier = rootModifier.then(buttonModifier),
        contentPadding = PaddingValues(horizontal = 15.dp)
    ) {
        Box(modifier = styles?.content ?: Modifier) {
            Text(
                text = label,
                fontSize = fontSize
            )
        }
    }
}
