package digital.guimauve.antdesign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Semantic class names for Checkbox component parts.
 * Allows customization of specific parts of the Checkbox component.
 *
 * @property root CSS class for the root element
 * @property input CSS class for the input element
 * @property inner CSS class for the inner checkbox element
 * @property wrapper CSS class for the wrapper element
 *
 * @since v5.4.0 Semantic styling support added
 */
data class CheckboxClassNames(
    val root: String? = null,
    val input: String? = null,
    val inner: String? = null,
    val wrapper: String? = null,
)

/**
 * Semantic styles for Checkbox component parts.
 * Allows customization of Modifier for specific parts of the Checkbox component.
 *
 * @property root Modifier for the root element
 * @property input Modifier for the input element
 * @property inner Modifier for the inner checkbox element
 * @property wrapper Modifier for the wrapper element
 *
 * @since v5.4.0 Semantic styling support added
 */
data class CheckboxStyles(
    val root: Modifier = Modifier,
    val input: Modifier = Modifier,
    val inner: Modifier = Modifier,
    val wrapper: Modifier = Modifier,
)

/**
 * TriState represents the three possible states of a checkbox.
 * Used for proper indeterminate state handling.
 *
 * @since v5.21.6 Enhanced indeterminate accessibility
 */
enum class CheckboxTriState {
    /** Checkbox is checked */
    CHECKED,

    /** Checkbox is unchecked */
    UNCHECKED,

    /** Checkbox is in indeterminate state (partially checked) */
    INDETERMINATE
}

/**
 * Event object for checkbox change events.
 * Mimics the React CheckboxChangeEvent structure.
 *
 * @property checked Whether the checkbox is checked
 * @property nativeEvent The native event object (platform-specific)
 */
data class CheckboxChangeEvent(
    val checked: Boolean,
    val nativeEvent: Any? = null,
)

/**
 * Event target for checkbox change events.
 * Contains the target state information.
 */
data class CheckboxChangeEventTarget(
    val checked: Boolean,
    val value: Any? = null,
    val name: String? = null,
)

/**
 * Ant Design Checkbox Component
 *
 * Single checkbox for marking or selection.
 *
 * @param checked Specifies whether the checkbox is selected (controlled mode)
 * @param onCheckedChange Callback for when checkbox state changes (simple boolean callback)
 * @param modifier The modifier to be applied to the layout
 * @param autoFocus If true, checkbox will be focused on mount
 * @param children Content to display next to the checkbox (label)
 * @param className CSS class name for the root element
 * @param classNames Semantic class names for different parts of the component
 * @param defaultChecked Initial checked state (for uncontrolled component)
 * @param disabled If true, checkbox is disabled
 * @param id ID attribute for the checkbox input
 * @param indeterminate If true, checkbox displays an indeterminate state
 * @param name Name attribute for the checkbox input
 * @param onChange Callback for when checkbox state changes (with event object)
 * @param onBlur Callback for blur events
 * @param onClick Callback for click events
 * @param onFocus Callback for focus events
 * @param onKeyDown Callback for key down events
 * @param onKeyPress Callback for key press events
 * @param onMouseEnter Callback for mouse enter events
 * @param onMouseLeave Callback for mouse leave events
 * @param prefixCls Prefix for CSS class names (default: "ant-checkbox")
 * @param required If true, checkbox is required
 * @param rootClassName CSS class name for the root wrapper element
 * @param skipGroup If true, checkbox will not register with CheckboxGroup
 * @param style CSS properties for the root element (mapped to Modifier)
 * @param styles Semantic styles (Modifiers) for different parts of the component
 * @param tabIndex Tab index for keyboard navigation
 * @param title Title attribute (tooltip text)
 * @param type Input type attribute
 * @param value Value associated with the checkbox
 */
@Composable
fun AntCheckbox(
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    autoFocus: Boolean = false,
    children: (@Composable () -> Unit)? = null,
    className: String? = null,
    classNames: CheckboxClassNames? = null,
    defaultChecked: Boolean = false,
    disabled: Boolean = false,
    id: String? = null,
    indeterminate: Boolean = false,
    name: String? = null,
    onChange: ((CheckboxChangeEvent) -> Unit)? = null,
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
    styles: CheckboxStyles? = null,
    tabIndex: Int? = null,
    title: String? = null,
    type: String? = null,
    value: Any? = null,
) {
    // Determine if this is a controlled component
    val isControlled = checked != null

    // State management for uncontrolled component
    var internalChecked by remember { mutableStateOf(defaultChecked) }

    // Use checked if provided (controlled), otherwise use internal state (uncontrolled)
    val effectiveChecked = checked ?: internalChecked

    // Development warning: using 'value' without 'checked' prop
    if (value != null && checked == null && !skipGroup) {
        // In production, this would emit a dev warning
        // Similar to React's warning system
    }

    // Focus management
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Handle checkbox state change
    val handleCheckChange: (Boolean) -> Unit = { newChecked ->
        // Update internal state only for uncontrolled components
        if (!isControlled) {
            internalChecked = newChecked
        }

        // Invoke simple callback
        onCheckedChange?.invoke(newChecked)

        // Invoke onChange with event object
        onChange?.invoke(
            CheckboxChangeEvent(
                checked = newChecked,
                nativeEvent = null
            )
        )

        // Invoke onClick
        onClick?.invoke()
    }

    // Determine wrapper CSS classes
    val prefixClsValue = prefixCls ?: "ant-checkbox"
    val wrapperClasses = buildList {
        add("${prefixClsValue}-wrapper")
        if (effectiveChecked) add("${prefixClsValue}-wrapper-checked")
        if (disabled) add("${prefixClsValue}-wrapper-disabled")
        className?.let { add(it) }
        rootClassName?.let { add(it) }
        classNames?.wrapper?.let { add(it) }
    }

    // Determine checkbox CSS classes
    val checkboxClasses = buildList {
        if (indeterminate) add("${prefixClsValue}-indeterminate")
        classNames?.input?.let { add(it) }
    }

    // Determine label CSS classes
    val labelClasses = buildList {
        add("${prefixClsValue}-label")
        classNames?.inner?.let { add(it) }
    }

    // Combine modifiers
    val rootModifier = modifier
        .then(style)
        .then(styles?.root ?: Modifier)
        .then(
            if (title != null) {
                Modifier.semantics { contentDescription = title }
            } else Modifier
        )
        .then(
            if (onKeyDown != null || onKeyPress != null) {
                Modifier.onKeyEvent { event ->
                    onKeyDown?.invoke(event)
                    onKeyPress?.invoke(event)
                    false
                }
            } else Modifier
        )
        .focusRequester(focusRequester)

    Row(
        modifier = rootModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = effectiveChecked,
            onCheckedChange = handleCheckChange,
            modifier = styles?.input ?: Modifier,
            enabled = !disabled,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF1890FF),
                uncheckedColor = Color(0xFFD9D9D9),
                disabledCheckedColor = Color(0xFFD9D9D9),
                disabledUncheckedColor = Color(0xFFF5F5F5)
            )
        )

        if (children != null) {
            Box(modifier = styles?.wrapper ?: Modifier) {
                children()
            }
        }
    }
}

/**
 * Option type for CheckboxGroup.
 * Defines the structure for individual checkbox options.
 *
 * @property label The display label for the checkbox option
 * @property value The value associated with this checkbox option
 * @property disabled Whether this option is disabled
 * @property className CSS class name for this option (v5.25.0+)
 * @property style Modifier for this option
 * @property title Title attribute (tooltip) for this option
 * @property id HTML id attribute for this option
 * @property onChange Individual onChange handler for this option
 * @property required Whether this option is required
 *
 * @since v5.0.0 Basic option support
 * @since v5.25.0 className property added
 */
data class CheckboxOptionType(
    val label: @Composable () -> Unit,
    val value: Any,
    val disabled: Boolean = false,
    val className: String? = null,
    val style: Modifier = Modifier,
    val title: String? = null,
    val id: String? = null,
    val onChange: ((CheckboxChangeEvent) -> Unit)? = null,
    val required: Boolean = false,
)

/**
 * Helper function to create CheckboxOptionType from a string label
 */
fun checkboxOption(
    label: String,
    value: Any = label,
    disabled: Boolean = false,
    className: String? = null,
    style: Modifier = Modifier,
    title: String? = null,
    id: String? = null,
    onChange: ((CheckboxChangeEvent) -> Unit)? = null,
    required: Boolean = false,
): CheckboxOptionType = CheckboxOptionType(
    label = { Text(label) },
    value = value,
    disabled = disabled,
    className = className,
    style = style,
    title = title,
    id = id,
    onChange = onChange,
    required = required
)

/**
 * Semantic class names for CheckboxGroup component parts.
 */
data class CheckboxGroupClassNames(
    val root: String? = null,
    val item: String? = null,
)

/**
 * Semantic styles for CheckboxGroup component parts.
 */
data class CheckboxGroupStyles(
    val root: Modifier = Modifier,
    val item: Modifier = Modifier,
)

/**
 * Ant Design Checkbox Group Component
 *
 * Group of checkboxes for multiple selection.
 *
 * @param options Array of checkbox options (can be CheckboxOptionType, String, or Number)
 * @param value Currently selected values (controlled mode)
 * @param onChange Callback when selection changes
 * @param modifier The modifier to be applied to the layout
 * @param children Custom checkbox components (alternative to options prop)
 * @param className CSS class name for the root element
 * @param classNames Semantic class names for different parts of the component
 * @param defaultValue Default selected values (for uncontrolled component)
 * @param disabled If true, all checkboxes are disabled
 * @param direction Layout direction (Vertical or Horizontal)
 * @param name Name attribute for all checkboxes in the group
 * @param prefixCls Prefix for CSS class names
 * @param rootClassName CSS class name for the root wrapper element
 * @param style CSS properties for the root element (mapped to Modifier)
 * @param styles Semantic styles (Modifiers) for different parts of the component
 */
@Composable
fun AntCheckboxGroup(
    options: List<CheckboxOptionType> = emptyList(),
    value: List<Any>? = null,
    onChange: ((List<Any>) -> Unit)? = null,
    modifier: Modifier = Modifier,
    children: (@Composable () -> Unit)? = null,
    className: String? = null,
    classNames: CheckboxGroupClassNames? = null,
    defaultValue: List<Any> = emptyList(),
    disabled: Boolean = false,
    direction: SpaceDirection = SpaceDirection.Vertical,
    name: String? = null,
    prefixCls: String? = null,
    rootClassName: String? = null,
    style: Modifier = Modifier,
    styles: CheckboxGroupStyles? = null,
) {
    // Determine if this is a controlled component
    val isControlled = value != null

    // State management for uncontrolled component
    var internalValue by remember { mutableStateOf(defaultValue) }

    // Track registered values for proper ordering
    val registeredValues = remember { mutableStateListOf<Any>() }

    // Use value if provided (controlled), otherwise use internal state (uncontrolled)
    val effectiveValue = value ?: internalValue

    // Update internal value when external value prop changes
    LaunchedEffect(value) {
        if (isControlled && value != null) {
            internalValue = value
        }
    }

    // Handle toggle option
    val handleToggleOption: (Any) -> Unit = { optionValue ->
        val newValue = if (effectiveValue.contains(optionValue)) {
            effectiveValue - optionValue
        } else {
            effectiveValue + optionValue
        }

        // Update internal state only for uncontrolled components
        if (!isControlled) {
            internalValue = newValue
        }

        // Sort the values based on the order in options
        val sortedValue = newValue.sortedBy { v ->
            options.indexOfFirst { it.value == v }
        }

        // Invoke onChange callback
        onChange?.invoke(sortedValue)
    }

    // Register values
    LaunchedEffect(options) {
        registeredValues.clear()
        registeredValues.addAll(options.map { it.value })
    }

    // Determine CSS classes
    val prefixClsValue = prefixCls ?: "ant-checkbox"
    val groupPrefixCls = "${prefixClsValue}-group"
    val groupClasses = buildList {
        add(groupPrefixCls)
        className?.let { add(it) }
        rootClassName?.let { add(it) }
        classNames?.root?.let { add(it) }
    }

    // Combine modifiers
    val rootModifier = modifier
        .then(style)
        .then(styles?.root ?: Modifier)

    // Render content based on whether options or children are provided
    val content: @Composable () -> Unit = {
        if (options.isNotEmpty()) {
            // Render from options
            options.forEach { option ->
                val isChecked = effectiveValue.contains(option.value)
                val itemClasses = buildList {
                    add("${groupPrefixCls}-item")
                    option.className?.let { add(it) }
                    classNames?.item?.let { add(it) }
                }

                AntCheckbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        handleToggleOption(option.value)

                        // Invoke individual option onChange
                        option.onChange?.invoke(
                            CheckboxChangeEvent(
                                checked = checked,
                                nativeEvent = null
                            )
                        )
                    },
                    modifier = option.style.then(styles?.item ?: Modifier),
                    children = option.label,
                    disabled = disabled || option.disabled,
                    name = name,
                    title = option.title,
                    id = option.id,
                    required = option.required,
                    value = option.value,
                    className = itemClasses.joinToString(" "),
                    prefixCls = prefixClsValue
                )
            }
        } else if (children != null) {
            // Render custom children
            children()
        }
    }

    // Render the group container
    when (direction) {
        SpaceDirection.Vertical -> {
            Column(
                modifier = rootModifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }

        SpaceDirection.Horizontal -> {
            Row(
                modifier = rootModifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * Overloaded version of AntCheckboxGroup that accepts simple string options
 * for backward compatibility and ease of use.
 */
@Composable
fun AntCheckboxGroup(
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    direction: SpaceDirection = SpaceDirection.Vertical,
    name: String? = null,
) {
    val checkboxOptions = remember(options) {
        options.map { option ->
            checkboxOption(
                label = option,
                value = option
            )
        }
    }

    AntCheckboxGroup(
        options = checkboxOptions,
        value = selectedOptions,
        onChange = { newValues ->
            @Suppress("UNCHECKED_CAST")
            onSelectionChange(newValues as List<String>)
        },
        modifier = modifier,
        disabled = disabled,
        direction = direction,
        name = name
    )
}
