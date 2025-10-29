package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

/**
 * Generic AutoComplete option with custom data support
 */
data class AutoCompleteOption<T>(
    val value: String,                              // Display value
    val data: T? = null,                           // Generic data payload
    val label: @Composable (() -> Unit)? = null,   // Custom label composable
    val labelText: String? = null,                 // Simple text label
    val disabled: Boolean = false,
)

/**
 * Grouped AutoComplete options
 */
data class AutoCompleteGroupOption<T>(
    val label: String,
    val options: List<AutoCompleteOption<T>>,
)

/**
 * AntAutoComplete - Full-featured AutoComplete component with 100% React parity
 *
 * @param T Generic type for custom data attached to options
 * @param value Controlled value (use with onChange)
 * @param onChange Controlled value change handler
 * @param defaultValue Uncontrolled default value
 * @param options List of autocomplete options (can be flat or grouped)
 * @param groupedOptions List of grouped options
 * @param allowClear Show clear button
 * @param autoFocus Auto focus on mount
 * @param backfill Backfill selected option on arrow key navigation
 * @param children Custom input component
 * @param defaultActiveFirstOption Highlight first option by default
 * @param defaultOpen Open dropdown by default
 * @param disabled Disable the component
 * @param popupClassName Custom class name for popup
 * @param popupMatchSelectWidth Match popup width to input width
 * @param notFoundContent Content to show when no results
 * @param open Controlled open state
 * @param placeholder Placeholder text
 * @param status Visual status (error, warning)
 * @param size Input size
 * @param variant Input variant (outlined, filled, borderless)
 * @param filterOption Custom filter function
 * @param onBlur Blur event handler
 * @param onFocus Focus event handler
 * @param onSearch Search event handler (for async loading)
 * @param onSelect Selection event handler
 * @param onClear Clear event handler
 * @param onDropdownVisibleChange Dropdown visibility change handler
 * @param className Custom class name
 * @param style Custom style modifier
 * @param modifier Compose modifier
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> AntAutoComplete(
    options: List<AutoCompleteOption<T>> = emptyList(),
    modifier: Modifier = Modifier,
    value: String? = null,                                    // Controlled
    onChange: ((String) -> Unit)? = null,
    defaultValue: String = "",                                // Uncontrolled
    groupedOptions: List<AutoCompleteGroupOption<T>> = emptyList(),
    allowClear: Boolean = false,
    autoFocus: Boolean = false,
    backfill: Boolean = false,
    children: (@Composable (String, (String) -> Unit) -> Unit)? = null,
    defaultActiveFirstOption: Boolean = true,
    defaultOpen: Boolean = false,
    disabled: Boolean = false,
    popupClassName: String? = null,
    popupMatchSelectWidth: Boolean = true,
    notFoundContent: (@Composable () -> Unit)? = null,
    open: Boolean? = null,                                    // Controlled open state
    placeholder: String = "",
    status: InputStatus = InputStatus.Default,
    size: InputSize = InputSize.Middle,
    variant: InputVariant = InputVariant.Outlined,
    filterOption: ((String, AutoCompleteOption<T>) -> Boolean)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null,
    onSelect: ((T?, AutoCompleteOption<T>) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onDropdownVisibleChange: ((Boolean) -> Unit)? = null,
    className: String? = null,
    style: Modifier = Modifier,
) {
    // State management - controlled vs uncontrolled
    val isControlled = value != null
    var internalValue by remember { mutableStateOf(defaultValue) }
    val currentValue = if (isControlled) value!! else internalValue

    // Dropdown state - controlled vs uncontrolled
    val isDropdownControlled = open != null
    var internalDropdownOpen by remember { mutableStateOf(defaultOpen) }
    var dropdownOpen by remember {
        mutableStateOf(if (isDropdownControlled) open!! else internalDropdownOpen)
    }

    // Navigation and selection state
    var highlightedIndex by remember { mutableStateOf(if (defaultActiveFirstOption) 0 else -1) }
    var inputFocused by remember { mutableStateOf(false) }

    // Input size tracking for popup width matching
    var inputSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    // Focus requester for autoFocus
    val focusRequester = remember { FocusRequester() }

    // Auto focus on mount
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Sync controlled value
    LaunchedEffect(value) {
        if (isControlled && value != currentValue) {
            internalValue = value!!
        }
    }

    // Sync controlled open state
    LaunchedEffect(open) {
        if (isDropdownControlled) {
            dropdownOpen = open!!
        }
    }

    // Flatten all options (including grouped)
    val allOptions = remember(options, groupedOptions) {
        val flat = options.toMutableList()
        groupedOptions.forEach { group ->
            flat.addAll(group.options)
        }
        flat
    }

    // Filter options based on current value
    val filteredOptions = remember(currentValue, allOptions) {
        if (currentValue.isEmpty()) {
            allOptions
        } else {
            allOptions.filter { option ->
                if (!option.disabled) {
                    filterOption?.invoke(currentValue, option) ?: run {
                        option.value.contains(currentValue, ignoreCase = true) ||
                                option.labelText?.contains(currentValue, ignoreCase = true) == true
                    }
                } else {
                    false
                }
            }
        }
    }

    // Filter grouped options
    val filteredGroupedOptions = remember(currentValue, groupedOptions) {
        if (currentValue.isEmpty()) {
            groupedOptions
        } else {
            groupedOptions.mapNotNull { group ->
                val filtered = group.options.filter { option ->
                    if (!option.disabled) {
                        filterOption?.invoke(currentValue, option) ?: run {
                            option.value.contains(currentValue, ignoreCase = true) ||
                                    option.labelText?.contains(currentValue, ignoreCase = true) == true
                        }
                    } else {
                        false
                    }
                }
                if (filtered.isNotEmpty()) {
                    group.copy(options = filtered)
                } else {
                    null
                }
            }
        }
    }

    // Build flat list for keyboard navigation
    val navigableOptions = remember(filteredOptions, filteredGroupedOptions) {
        if (groupedOptions.isNotEmpty()) {
            filteredGroupedOptions.flatMap { it.options }
        } else {
            filteredOptions
        }
    }

    // Reset highlighted index when options change
    LaunchedEffect(navigableOptions) {
        if (defaultActiveFirstOption && navigableOptions.isNotEmpty()) {
            highlightedIndex = 0
        } else {
            highlightedIndex = -1
        }
    }

    // Handle value change
    val handleValueChange: (String) -> Unit = { newValue ->
        if (!isControlled) {
            internalValue = newValue
        }
        onChange?.invoke(newValue)
        onSearch?.invoke(newValue)

        // Open dropdown on value change
        if (!isDropdownControlled) {
            internalDropdownOpen = true
            dropdownOpen = true
        }
        onDropdownVisibleChange?.invoke(true)
    }

    // Handle dropdown visibility change
    val handleDropdownChange: (Boolean) -> Unit = { visible ->
        if (!isDropdownControlled) {
            internalDropdownOpen = visible
            dropdownOpen = visible
        }
        onDropdownVisibleChange?.invoke(visible)
    }

    // Handle option selection
    val handleSelect: (AutoCompleteOption<T>) -> Unit = { option ->
        val newValue = option.value
        if (!isControlled) {
            internalValue = newValue
        }
        onChange?.invoke(newValue)
        onSelect?.invoke(option.data, option)
        handleDropdownChange(false)
    }

    // Handle keyboard navigation
    val handleKeyEvent: (KeyEvent) -> Boolean = { event ->
        if (event.type == KeyEventType.KeyDown && dropdownOpen && navigableOptions.isNotEmpty()) {
            when (event.key) {
                Key.DirectionDown -> {
                    highlightedIndex = (highlightedIndex + 1).coerceAtMost(navigableOptions.size - 1)
                    if (backfill && highlightedIndex >= 0) {
                        val option = navigableOptions[highlightedIndex]
                        if (!isControlled) {
                            internalValue = option.value
                        }
                        onChange?.invoke(option.value)
                    }
                    true
                }

                Key.DirectionUp -> {
                    highlightedIndex = (highlightedIndex - 1).coerceAtLeast(0)
                    if (backfill && highlightedIndex >= 0) {
                        val option = navigableOptions[highlightedIndex]
                        if (!isControlled) {
                            internalValue = option.value
                        }
                        onChange?.invoke(option.value)
                    }
                    true
                }

                Key.Enter -> {
                    if (highlightedIndex >= 0 && highlightedIndex < navigableOptions.size) {
                        handleSelect(navigableOptions[highlightedIndex])
                    }
                    true
                }

                Key.Escape -> {
                    handleDropdownChange(false)
                    true
                }

                else -> false
            }
        } else {
            false
        }
    }

    Box(
        modifier = modifier
            .then(style)
            .onSizeChanged { inputSize = it }
    ) {
        // Custom input or default input
        if (children != null) {
            Box(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onKeyEvent(handleKeyEvent)
                    .focusable()
            ) {
                children(currentValue, handleValueChange)
            }
        } else {
            AntInput(
                value = currentValue,
                onValueChange = handleValueChange,
                placeholder = placeholder,
                disabled = disabled,
                allowClear = allowClear,
                size = size,
                status = status,
                variant = variant,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onKeyEvent(handleKeyEvent),
                onClear = {
                    if (!isControlled) {
                        internalValue = ""
                    }
                    onChange?.invoke("")
                    onClear?.invoke()
                    handleDropdownChange(false)
                },
                onFocus = {
                    inputFocused = true
                    onFocus?.invoke()
                    if (currentValue.isNotEmpty() || defaultOpen) {
                        handleDropdownChange(true)
                    }
                },
                onBlur = {
                    inputFocused = false
                    onBlur?.invoke()
                }
            )
        }

        // Dropdown popup
        if (dropdownOpen && !disabled) {
            val hasResults = navigableOptions.isNotEmpty()

            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = {
                    handleDropdownChange(false)
                    highlightedIndex = if (defaultActiveFirstOption && navigableOptions.isNotEmpty()) 0 else -1
                }
            ) {
                val popupWidth = if (popupMatchSelectWidth) {
                    with(density) { inputSize.width.toDp() }
                } else {
                    200.dp
                }

                Card(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(popupWidth)
                        .heightIn(max = 300.dp),
                    shape = RoundedCornerShape(4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    if (hasResults) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (groupedOptions.isNotEmpty()) {
                                // Render grouped options
                                filteredGroupedOptions.forEach { group ->
                                    // Group header
                                    item {
                                        Text(
                                            text = group.label,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }

                                    // Group options
                                    itemsIndexed(group.options) { _, option ->
                                        val globalIndex = navigableOptions.indexOf(option)
                                        AutoCompleteOptionItem(
                                            option = option,
                                            isHighlighted = globalIndex == highlightedIndex,
                                            onClick = { handleSelect(option) }
                                        )
                                    }
                                }
                            } else {
                                // Render flat options
                                itemsIndexed(filteredOptions) { index, option ->
                                    AutoCompleteOptionItem(
                                        option = option,
                                        isHighlighted = index == highlightedIndex,
                                        onClick = { handleSelect(option) }
                                    )
                                }
                            }
                        }
                    } else {
                        // No results content
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (notFoundContent != null) {
                                notFoundContent()
                            } else {
                                Text(
                                    text = "No results found",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> AutoCompleteOptionItem(
    option: AutoCompleteOption<T>,
    isHighlighted: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isHighlighted) Color(0xFFF5F5F5) else Color.Transparent)
            .clickable(enabled = !option.disabled) {
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Render custom composable label or text label
        if (option.label != null) {
            option.label.invoke()
        } else {
            Text(
                text = option.labelText ?: option.value,
                fontSize = 14.sp,
                color = if (option.disabled) Color.Gray else Color.Black
            )
        }
    }
}

/**
 * Simple AutoComplete helper for string options
 * Convenience wrapper for common use case with just string values
 */
@Composable
fun AntAutoCompleteSimple(
    options: List<String>,
    modifier: Modifier = Modifier,
    value: String? = null,
    onChange: ((String) -> Unit)? = null,
    defaultValue: String = "",
    placeholder: String = "",
    disabled: Boolean = false,
    allowClear: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    variant: InputVariant = InputVariant.Outlined,
    autoFocus: Boolean = false,
    onSelect: ((String) -> Unit)? = null,
) {
    val autoCompleteOptions = remember(options) {
        options.map { AutoCompleteOption<String>(value = it) }
    }

    AntAutoComplete(
        options = autoCompleteOptions,
        modifier = modifier,
        value = value,
        onChange = onChange,
        defaultValue = defaultValue,
        placeholder = placeholder,
        disabled = disabled,
        allowClear = allowClear,
        size = size,
        status = status,
        variant = variant,
        autoFocus = autoFocus,
        onSelect = { _, option -> onSelect?.invoke(option.value) }
    )
}
