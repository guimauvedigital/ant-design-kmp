package digital.guimauve.antdesign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

// Select Modes
enum class SelectMode {
    Single,
    Multiple,
    Tags
}

// Select Variant
enum class SelectVariant {
    Outlined,
    Filled,
    Borderless,
    Underlined
}

// Placement options
enum class SelectPlacement {
    TopLeft,
    TopCenter,
    TopRight,
    BottomLeft,
    BottomCenter,
    BottomRight
}

// Select Option with OptGroup support
data class SelectOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
    val icon: (@Composable () -> Unit)? = null,
    val data: Map<String, Any>? = null,
)

// OptGroup for grouped options
data class SelectOptGroup<T>(
    val label: String,
    val options: List<SelectOption<T>>,
)

// Field names customization
data class FieldNames(
    val label: String = "label",
    val value: String = "value",
    val options: String = "options",
)

// Semantic class names for Select (v5.25.0+)
data class SelectClassNames(
    val root: String? = null,
    val popup: PopupClassNames? = null,
)

// Popup class names
data class PopupClassNames(
    val root: String? = null,
)

// Semantic styles for Select (v5.25.0+)
data class SelectStyles(
    val root: Modifier? = null,
    val popup: PopupStyles? = null,
)

// Popup styles
data class PopupStyles(
    val root: Modifier? = null,
)

// Labeled value for labelInValue mode
data class LabeledValue<T>(
    val value: T,
    val label: String,
)

// MaxTagCount type - supports number or 'responsive'
sealed class MaxTagCount {
    data class Number(val count: Int) : MaxTagCount()
    object Responsive : MaxTagCount()
}

// PopupMatchSelectWidth type - supports boolean or number (Dp)
sealed class PopupMatchSelectWidth {
    object True : PopupMatchSelectWidth()
    object False : PopupMatchSelectWidth()
    data class Width(val width: Dp) : PopupMatchSelectWidth()
}

// TagRender props data class
data class TagRenderProps<T>(
    val label: String,
    val value: T,
    val disabled: Boolean,
    val closable: Boolean,
    val onClose: () -> Unit,
)

// LabelRender props data class (matches LabelInValueType from rc-select)
data class LabelRenderProps(
    val label: String,
    val value: Any?,
)

// FilterSort info parameter
data class FilterSortInfo(
    val searchValue: String,
)

// OptionRender info parameter
data class OptionRenderInfo(
    val index: Int,
)

/**
 * Complete Ant Design Select component with support for:
 * - Single, Multiple, and Tags modes
 * - OptGroup (grouped options)
 * - maxTagCount with placeholder
 * - Custom filtering and search
 * - Virtual scrolling
 * - Variants (Outlined, Filled, Borderless, Underlined)
 * - Semantic styling (classNames, styles) v5.25.0+
 * - Full callback support
 */
@Composable
fun <T> AntSelect(
    value: T?,
    onValueChange: (T?) -> Unit,
    options: List<SelectOption<T>>,
    modifier: Modifier = Modifier,
    mode: SelectMode = SelectMode.Single,
    placeholder: String = "Please select",
    disabled: Boolean = false,
    allowClear: Boolean = false,
    showSearch: Boolean = false,
    loading: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    // Multiple/Tags specific props
    maxTagCount: MaxTagCount? = null,
    maxTagPlaceholder: @Composable ((omittedValues: List<LabeledValue<T>>) -> Unit)? = null,
    maxTagTextLength: Int? = null,
    maxCount: Int? = null,
    tokenSeparators: List<String> = listOf(","),
    // OptGroup support
    optGroups: List<SelectOptGroup<T>>? = null,
    // Search configuration
    autoClearSearchValue: Boolean = true,
    searchValue: String? = null,
    optionFilterProp: String = "label",
    optionLabelProp: String = "children",
    filterOption: ((input: String, option: SelectOption<T>) -> Boolean)? = null,
    filterSort: ((a: SelectOption<T>, b: SelectOption<T>, info: FilterSortInfo) -> Int)? = null,
    onSearch: ((value: String) -> Unit)? = null,
    // State control
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    autoFocus: Boolean = false,
    defaultActiveFirstOption: Boolean = true,
    // Value formatting
    labelInValue: Boolean = false,
    // Field names
    fieldNames: FieldNames? = null,
    // Callbacks
    onChange: ((value: T?, option: SelectOption<T>?) -> Unit)? = null,
    onSelect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onDeselect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onOpenChange: ((open: Boolean) -> Unit)? = null,
    // Deprecated: Use onOpenChange instead
    onDropdownVisibleChange: ((open: Boolean) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onInputKeyDown: ((KeyEvent) -> Unit)? = null,
    onPopupScroll: (() -> Unit)? = null,
    // Virtual scrolling
    virtual: Boolean = true,
    listHeight: Dp = 256.dp,
    // Appearance
    variant: SelectVariant? = null,
    // Deprecated: Use variant instead
    bordered: Boolean? = null,
    showArrow: Boolean = true,
    placement: SelectPlacement = SelectPlacement.BottomLeft,
    popupMatchSelectWidth: PopupMatchSelectWidth = PopupMatchSelectWidth.True,
    // Deprecated: Use styles.popup.root instead
    dropdownStyle: Modifier = Modifier,
    // Deprecated: Use classNames.popup.root instead
    popupClassName: String? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null,
    removeIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    menuItemSelectedIcon: (@Composable () -> Unit)? = null,
    // Semantic styling (v5.25.0+)
    classNames: SelectClassNames? = null,
    styles: SelectStyles? = null,
    // Custom renders
    // Deprecated: Use popupRender instead
    dropdownRender: (@Composable (menu: @Composable () -> Unit) -> Unit)? = null,
    popupRender: (@Composable (menu: @Composable () -> Unit) -> Unit)? = null,
    tagRender: (@Composable (props: TagRenderProps<T>) -> Unit)? = null,
    optionRender: (@Composable (option: SelectOption<T>, info: OptionRenderInfo) -> Unit)? = null,
    labelRender: (@Composable (props: LabelRenderProps) -> Unit)? = null,
    notFoundContent: @Composable (() -> Unit)? = null,
    // Container
    getPopupContainer: (() -> Any)? = null,
    // Combobox mode specific
    backfill: Boolean = false,
) {
    // Handle deprecated props
    val effectiveDropdownRender = popupRender ?: dropdownRender
    val effectiveOnOpenChange = onOpenChange ?: onDropdownVisibleChange
    val effectiveVariant = variant ?: if (bordered == false) SelectVariant.Borderless else null
    val effectiveDropdownStyle = styles?.popup?.root ?: dropdownStyle

    when (mode) {
        SelectMode.Single -> AntSelectSingle(
            value = value,
            onValueChange = onValueChange,
            options = options,
            modifier = modifier,
            placeholder = placeholder,
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch,
            loading = loading,
            size = size,
            status = status,
            autoClearSearchValue = autoClearSearchValue,
            searchValue = searchValue,
            optGroups = optGroups,
            optionFilterProp = optionFilterProp,
            optionLabelProp = optionLabelProp,
            filterOption = filterOption,
            filterSort = filterSort,
            onSearch = onSearch,
            open = open,
            defaultOpen = defaultOpen,
            autoFocus = autoFocus,
            defaultActiveFirstOption = defaultActiveFirstOption,
            labelInValue = labelInValue,
            fieldNames = fieldNames,
            onChange = onChange,
            onSelect = onSelect,
            onClear = onClear,
            onOpenChange = effectiveOnOpenChange,
            onBlur = onBlur,
            onFocus = onFocus,
            onInputKeyDown = onInputKeyDown,
            onPopupScroll = onPopupScroll,
            virtual = virtual,
            listHeight = listHeight,
            variant = effectiveVariant,
            showArrow = showArrow,
            placement = placement,
            popupMatchSelectWidth = popupMatchSelectWidth,
            dropdownStyle = effectiveDropdownStyle,
            classNames = classNames,
            styles = styles,
            prefixIcon = prefixIcon,
            suffixIcon = suffixIcon,
            clearIcon = clearIcon,
            menuItemSelectedIcon = menuItemSelectedIcon,
            dropdownRender = effectiveDropdownRender,
            optionRender = optionRender,
            labelRender = labelRender,
            notFoundContent = notFoundContent,
            getPopupContainer = getPopupContainer,
            backfill = backfill
        )

        SelectMode.Multiple -> AntSelectMultiple(
            values = (value as? List<T>) ?: emptyList(),
            onValuesChange = { onValueChange(it as T?) },
            options = options,
            modifier = modifier,
            placeholder = placeholder,
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch,
            loading = loading,
            size = size,
            status = status,
            autoClearSearchValue = autoClearSearchValue,
            searchValue = searchValue,
            maxTagCount = maxTagCount,
            maxTagPlaceholder = maxTagPlaceholder,
            maxTagTextLength = maxTagTextLength,
            maxCount = maxCount,
            optGroups = optGroups,
            optionFilterProp = optionFilterProp,
            optionLabelProp = optionLabelProp,
            filterOption = filterOption,
            filterSort = filterSort,
            onSearch = onSearch,
            open = open,
            defaultOpen = defaultOpen,
            autoFocus = autoFocus,
            defaultActiveFirstOption = defaultActiveFirstOption,
            labelInValue = labelInValue,
            fieldNames = fieldNames,
            onChange = onChange,
            onSelect = onSelect,
            onDeselect = onDeselect,
            onClear = onClear,
            onOpenChange = effectiveOnOpenChange,
            onBlur = onBlur,
            onFocus = onFocus,
            onInputKeyDown = onInputKeyDown,
            onPopupScroll = onPopupScroll,
            virtual = virtual,
            listHeight = listHeight,
            variant = effectiveVariant,
            showArrow = showArrow,
            placement = placement,
            popupMatchSelectWidth = popupMatchSelectWidth,
            dropdownStyle = effectiveDropdownStyle,
            classNames = classNames,
            styles = styles,
            prefixIcon = prefixIcon,
            suffixIcon = suffixIcon,
            removeIcon = removeIcon,
            clearIcon = clearIcon,
            menuItemSelectedIcon = menuItemSelectedIcon,
            dropdownRender = effectiveDropdownRender,
            tagRender = tagRender,
            optionRender = optionRender,
            labelRender = labelRender,
            notFoundContent = notFoundContent,
            getPopupContainer = getPopupContainer
        )

        SelectMode.Tags -> AntSelectTags(
            values = (value as? List<T>) ?: emptyList(),
            onValuesChange = { onValueChange(it as T?) },
            options = options,
            modifier = modifier,
            placeholder = placeholder,
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch,
            loading = loading,
            size = size,
            status = status,
            autoClearSearchValue = autoClearSearchValue,
            searchValue = searchValue,
            maxTagCount = maxTagCount,
            maxTagPlaceholder = maxTagPlaceholder,
            maxTagTextLength = maxTagTextLength,
            maxCount = maxCount,
            tokenSeparators = tokenSeparators,
            optGroups = optGroups,
            optionFilterProp = optionFilterProp,
            optionLabelProp = optionLabelProp,
            filterOption = filterOption,
            filterSort = filterSort,
            onSearch = onSearch,
            open = open,
            defaultOpen = defaultOpen,
            autoFocus = autoFocus,
            defaultActiveFirstOption = defaultActiveFirstOption,
            labelInValue = labelInValue,
            fieldNames = fieldNames,
            onChange = onChange,
            onSelect = onSelect,
            onDeselect = onDeselect,
            onClear = onClear,
            onOpenChange = effectiveOnOpenChange,
            onBlur = onBlur,
            onFocus = onFocus,
            onInputKeyDown = onInputKeyDown,
            onPopupScroll = onPopupScroll,
            virtual = virtual,
            listHeight = listHeight,
            variant = effectiveVariant,
            showArrow = showArrow,
            placement = placement,
            popupMatchSelectWidth = popupMatchSelectWidth,
            dropdownStyle = effectiveDropdownStyle,
            classNames = classNames,
            styles = styles,
            prefixIcon = prefixIcon,
            suffixIcon = suffixIcon,
            removeIcon = removeIcon,
            clearIcon = clearIcon,
            menuItemSelectedIcon = menuItemSelectedIcon,
            dropdownRender = effectiveDropdownRender,
            tagRender = tagRender,
            optionRender = optionRender,
            labelRender = labelRender,
            notFoundContent = notFoundContent,
            getPopupContainer = getPopupContainer
        )
    }
}

// Single Select Implementation
@Composable
private fun <T> AntSelectSingle(
    value: T?,
    onValueChange: (T?) -> Unit,
    options: List<SelectOption<T>>,
    modifier: Modifier = Modifier,
    placeholder: String = "Please select",
    disabled: Boolean = false,
    allowClear: Boolean = false,
    showSearch: Boolean = false,
    loading: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    autoClearSearchValue: Boolean = true,
    searchValue: String? = null,
    optGroups: List<SelectOptGroup<T>>? = null,
    optionFilterProp: String = "label",
    optionLabelProp: String = "children",
    filterOption: ((input: String, option: SelectOption<T>) -> Boolean)? = null,
    filterSort: ((a: SelectOption<T>, b: SelectOption<T>, info: FilterSortInfo) -> Int)? = null,
    onSearch: ((value: String) -> Unit)? = null,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    autoFocus: Boolean = false,
    defaultActiveFirstOption: Boolean = true,
    labelInValue: Boolean = false,
    fieldNames: FieldNames? = null,
    onChange: ((value: T?, option: SelectOption<T>?) -> Unit)? = null,
    onSelect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onOpenChange: ((open: Boolean) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onInputKeyDown: ((KeyEvent) -> Unit)? = null,
    onPopupScroll: (() -> Unit)? = null,
    virtual: Boolean = true,
    listHeight: Dp = 256.dp,
    variant: SelectVariant? = null,
    showArrow: Boolean = true,
    placement: SelectPlacement = SelectPlacement.BottomLeft,
    popupMatchSelectWidth: PopupMatchSelectWidth = PopupMatchSelectWidth.True,
    dropdownStyle: Modifier = Modifier,
    classNames: SelectClassNames? = null,
    styles: SelectStyles? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    menuItemSelectedIcon: (@Composable () -> Unit)? = null,
    dropdownRender: (@Composable (menu: @Composable () -> Unit) -> Unit)? = null,
    optionRender: (@Composable (option: SelectOption<T>, info: OptionRenderInfo) -> Unit)? = null,
    labelRender: (@Composable (props: LabelRenderProps) -> Unit)? = null,
    notFoundContent: @Composable (() -> Unit)? = null,
    getPopupContainer: (() -> Any)? = null,
    backfill: Boolean = false,
) {
    val config = useConfig()
    val theme = useTheme()

    var expanded by remember { mutableStateOf(defaultOpen) }
    var internalSearchQuery by remember { mutableStateOf("") }
    var activeOptionIndex by remember { mutableStateOf(if (defaultActiveFirstOption) 0 else -1) }
    val focusRequester = remember { FocusRequester() }

    val searchQuery = searchValue ?: internalSearchQuery
    val isExpanded = open ?: expanded

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
            onFocus?.invoke()
        }
    }

    val effectiveVariant = variant ?: SelectVariant.Outlined

    val (borderColor, backgroundColor) = getVariantColors(effectiveVariant, status, disabled, theme)

    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        InputSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    LaunchedEffect(isExpanded) {
        onOpenChange?.invoke(isExpanded)
    }

    Box(modifier = modifier.then(styles?.root ?: Modifier)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    if (open == null) {
                        expanded = !expanded
                    }
                    onFocus?.invoke()
                }
                .onKeyEvent { keyEvent ->
                    onInputKeyDown?.invoke(keyEvent)
                    false
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = if (effectiveVariant == SelectVariant.Borderless) null
            else BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(theme.token.borderRadius)
        ) {
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefixIcon?.invoke()

                    val selectedOption = options.find { it.value == value }
                    val displayText = selectedOption?.label ?: placeholder

                    if (labelRender != null && selectedOption != null) {
                        labelRender(LabelRenderProps(label = displayText, value = value))
                    } else {
                        Text(
                            text = displayText,
                            fontSize = 14.sp,
                            color = if (value == null) Color.Gray else Color.Black
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (allowClear && value != null) {
                        IconButton(
                            onClick = {
                                val selectedOption = options.find { it.value == value }
                                onValueChange(null)
                                onChange?.invoke(null, null)
                                onClear?.invoke()
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            if (clearIcon != null) {
                                clearIcon()
                            } else {
                                Text("✕", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    if (showArrow) {
                        if (suffixIcon != null) {
                            suffixIcon()
                        } else {
                            Text(
                                text = if (isExpanded) "▲" else "▼",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (isExpanded) {
            Popup(
                onDismissRequest = {
                    if (open == null) {
                        expanded = false
                    }
                    if (autoClearSearchValue && searchValue == null) {
                        internalSearchQuery = ""
                    }
                    onBlur?.invoke()
                },
                alignment = Alignment.TopStart
            ) {
                Card(
                    modifier = Modifier
                        .then(
                            when (popupMatchSelectWidth) {
                                is PopupMatchSelectWidth.True -> Modifier.fillMaxWidth()
                                is PopupMatchSelectWidth.False -> Modifier.widthIn(min = 200.dp)
                                is PopupMatchSelectWidth.Width -> Modifier.width(popupMatchSelectWidth.width)
                            }
                        )
                        .heightIn(max = listHeight)
                        .then(dropdownStyle)
                        .then(styles?.popup?.root ?: Modifier),
                    shape = RoundedCornerShape(theme.token.borderRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val menu = @Composable {
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AntSpin(spinning = true, size = SpinSize.Small)
                            }
                        } else {
                            LazyColumn {
                                if (showSearch) {
                                    item {
                                        AntInput(
                                            value = searchQuery,
                                            onValueChange = {
                                                if (searchValue == null) {
                                                    internalSearchQuery = it
                                                }
                                                onSearch?.invoke(it)
                                            },
                                            placeholder = "Search...",
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }

                                val allOptions = optGroups?.flatMap { it.options } ?: options
                                var filteredOptions = if (searchQuery.isNotEmpty()) {
                                    val filter = filterOption ?: { input, option ->
                                        option.label.contains(input, ignoreCase = true)
                                    }
                                    allOptions.filter { filter(searchQuery, it) }
                                } else {
                                    allOptions
                                }

                                if (filterSort != null && searchQuery.isNotEmpty()) {
                                    filteredOptions = filteredOptions.sortedWith { a, b ->
                                        filterSort(a, b, FilterSortInfo(searchValue = searchQuery))
                                    }
                                }

                                if (filteredOptions.isEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            notFoundContent?.invoke() ?: Text(
                                                "No data",
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else if (optGroups != null) {
                                    optGroups.forEach { group ->
                                        val groupFilteredOptions = group.options.filter {
                                            filteredOptions.contains(it)
                                        }
                                        if (groupFilteredOptions.isNotEmpty()) {
                                            item {
                                                Text(
                                                    text = group.label,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                                )
                                            }
                                            items(groupFilteredOptions) { option ->
                                                SelectOptionItem(
                                                    option = option,
                                                    selected = option.value == value,
                                                    mode = SelectMode.Single,
                                                    theme = theme,
                                                    onClick = {
                                                        if (!option.disabled) {
                                                            onValueChange(option.value)
                                                            onChange?.invoke(option.value, option)
                                                            onSelect?.invoke(option.value, option)
                                                            if (open == null) {
                                                                expanded = false
                                                            }
                                                            if (autoClearSearchValue && searchValue == null) {
                                                                internalSearchQuery = ""
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    items(filteredOptions.size) { index ->
                                        val option = filteredOptions[index]
                                        if (optionRender != null) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable(enabled = !option.disabled) {
                                                        if (!option.disabled) {
                                                            onValueChange(option.value)
                                                            onChange?.invoke(option.value, option)
                                                            onSelect?.invoke(option.value, option)
                                                            if (open == null) {
                                                                expanded = false
                                                            }
                                                            if (autoClearSearchValue && searchValue == null) {
                                                                internalSearchQuery = ""
                                                            }
                                                        }
                                                    }
                                            ) {
                                                optionRender(option, OptionRenderInfo(index = index))
                                            }
                                        } else {
                                            SelectOptionItem(
                                                option = option,
                                                selected = option.value == value,
                                                mode = SelectMode.Single,
                                                theme = theme,
                                                menuItemSelectedIcon = menuItemSelectedIcon,
                                                onClick = {
                                                    if (!option.disabled) {
                                                        onValueChange(option.value)
                                                        onChange?.invoke(option.value, option)
                                                        onSelect?.invoke(option.value, option)
                                                        if (open == null) {
                                                            expanded = false
                                                        }
                                                        if (autoClearSearchValue && searchValue == null) {
                                                            internalSearchQuery = ""
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (dropdownRender != null) {
                        dropdownRender(menu)
                    } else {
                        menu()
                    }
                }
            }
        }
    }
}

// Multiple Select Implementation
@Composable
private fun <T> AntSelectMultiple(
    values: List<T>,
    onValuesChange: (List<T>) -> Unit,
    options: List<SelectOption<T>>,
    modifier: Modifier = Modifier,
    placeholder: String = "Please select",
    disabled: Boolean = false,
    allowClear: Boolean = false,
    showSearch: Boolean = false,
    loading: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    autoClearSearchValue: Boolean = true,
    searchValue: String? = null,
    maxTagCount: MaxTagCount? = null,
    maxTagPlaceholder: @Composable ((omittedValues: List<LabeledValue<T>>) -> Unit)? = null,
    maxTagTextLength: Int? = null,
    maxCount: Int? = null,
    optGroups: List<SelectOptGroup<T>>? = null,
    optionFilterProp: String = "label",
    optionLabelProp: String = "children",
    filterOption: ((input: String, option: SelectOption<T>) -> Boolean)? = null,
    filterSort: ((a: SelectOption<T>, b: SelectOption<T>, info: FilterSortInfo) -> Int)? = null,
    onSearch: ((value: String) -> Unit)? = null,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    autoFocus: Boolean = false,
    defaultActiveFirstOption: Boolean = true,
    labelInValue: Boolean = false,
    fieldNames: FieldNames? = null,
    onChange: ((value: T?, option: SelectOption<T>?) -> Unit)? = null,
    onSelect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onDeselect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onOpenChange: ((open: Boolean) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onInputKeyDown: ((KeyEvent) -> Unit)? = null,
    onPopupScroll: (() -> Unit)? = null,
    virtual: Boolean = true,
    listHeight: Dp = 256.dp,
    variant: SelectVariant? = null,
    showArrow: Boolean = true,
    placement: SelectPlacement = SelectPlacement.BottomLeft,
    popupMatchSelectWidth: PopupMatchSelectWidth = PopupMatchSelectWidth.True,
    dropdownStyle: Modifier = Modifier,
    classNames: SelectClassNames? = null,
    styles: SelectStyles? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null,
    removeIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    menuItemSelectedIcon: (@Composable () -> Unit)? = null,
    dropdownRender: (@Composable (menu: @Composable () -> Unit) -> Unit)? = null,
    tagRender: (@Composable (props: TagRenderProps<T>) -> Unit)? = null,
    optionRender: (@Composable (option: SelectOption<T>, info: OptionRenderInfo) -> Unit)? = null,
    labelRender: (@Composable (props: LabelRenderProps) -> Unit)? = null,
    notFoundContent: @Composable (() -> Unit)? = null,
    getPopupContainer: (() -> Any)? = null,
) {
    val config = useConfig()
    val theme = useTheme()

    var expanded by remember { mutableStateOf(defaultOpen) }
    var internalSearchQuery by remember { mutableStateOf("") }
    var activeOptionIndex by remember { mutableStateOf(if (defaultActiveFirstOption) 0 else -1) }

    val searchQuery = searchValue ?: internalSearchQuery
    val isExpanded = open ?: expanded
    val effectiveMaxCount = maxCount ?: Int.MAX_VALUE

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            onFocus?.invoke()
        }
    }

    val effectiveVariant = variant ?: SelectVariant.Outlined

    val (borderColor, backgroundColor) = getVariantColors(effectiveVariant, status, disabled, theme)

    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Middle -> PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        InputSize.Small -> PaddingValues(horizontal = 6.dp, vertical = 4.dp)
    }

    LaunchedEffect(isExpanded) {
        onOpenChange?.invoke(isExpanded)
    }

    Box(modifier = modifier.then(styles?.root ?: Modifier)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    if (open == null) {
                        expanded = !expanded
                    }
                    onFocus?.invoke()
                }
                .onKeyEvent { keyEvent ->
                    onInputKeyDown?.invoke(keyEvent)
                    false
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = if (effectiveVariant == SelectVariant.Borderless) null
            else BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(theme.token.borderRadius)
        ) {
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefixIcon?.invoke()

                    // Display selected tags
                    if (values.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Handle maxTagCount: Number or Responsive
                            val effectiveMaxTagCount = when (maxTagCount) {
                                is MaxTagCount.Number -> maxTagCount.count
                                is MaxTagCount.Responsive -> 3 // TODO: Calculate based on available width
                                null -> null
                            }

                            val displayValues =
                                if (effectiveMaxTagCount != null && values.size > effectiveMaxTagCount) {
                                    values.take(effectiveMaxTagCount)
                                } else {
                                    values
                                }

                            displayValues.forEach { value ->
                                val option = options.find { it.value == value }
                                if (option != null && values.size < effectiveMaxCount) {
                                    val label =
                                        if (maxTagTextLength != null && option.label.length > maxTagTextLength) {
                                            option.label.take(maxTagTextLength) + "..."
                                        } else {
                                            option.label
                                        }

                                    if (tagRender != null) {
                                        val tagProps = TagRenderProps(
                                            label = label,
                                            value = value,
                                            disabled = option.disabled,
                                            closable = true,
                                            onClose = {
                                                val newValues = values.filter { it != value }
                                                onValuesChange(newValues)
                                                onDeselect?.invoke(value, option)
                                            }
                                        )
                                        tagRender(tagProps)
                                    } else {
                                        AntTag(
                                            text = label,
                                            closable = true,
                                            onClose = {
                                                val newValues = values.filter { it != value }
                                                onValuesChange(newValues)
                                                onDeselect?.invoke(value, option)
                                            }
                                        )
                                    }
                                }
                            }

                            // Show maxTagPlaceholder if exceeded
                            if (effectiveMaxTagCount != null && values.size > effectiveMaxTagCount) {
                                val omittedValues = values.drop(effectiveMaxTagCount).map { v ->
                                    val opt = options.find { it.value == v }
                                    LabeledValue(value = v, label = opt?.label ?: v.toString())
                                }
                                if (maxTagPlaceholder != null) {
                                    maxTagPlaceholder(omittedValues)
                                } else {
                                    Text(
                                        text = "+${omittedValues.size}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (allowClear && values.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onValuesChange(emptyList())
                                onChange?.invoke(null, null)
                                onClear?.invoke()
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            if (clearIcon != null) {
                                clearIcon()
                            } else if (removeIcon != null) {
                                removeIcon()
                            } else {
                                Text("✕", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    if (showArrow) {
                        if (suffixIcon != null) {
                            suffixIcon()
                        } else {
                            Text(
                                text = if (isExpanded) "▲" else "▼",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (isExpanded) {
            Popup(
                onDismissRequest = {
                    if (open == null) {
                        expanded = false
                    }
                    if (autoClearSearchValue && searchValue == null) {
                        internalSearchQuery = ""
                    }
                    onBlur?.invoke()
                },
                alignment = Alignment.TopStart
            ) {
                Card(
                    modifier = Modifier
                        .then(
                            when (popupMatchSelectWidth) {
                                is PopupMatchSelectWidth.True -> Modifier.fillMaxWidth()
                                is PopupMatchSelectWidth.False -> Modifier.widthIn(min = 200.dp)
                                is PopupMatchSelectWidth.Width -> Modifier.width(popupMatchSelectWidth.width)
                            }
                        )
                        .heightIn(max = listHeight)
                        .then(dropdownStyle)
                        .then(styles?.popup?.root ?: Modifier),
                    shape = RoundedCornerShape(theme.token.borderRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val menu = @Composable {
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AntSpin(spinning = true, size = SpinSize.Small)
                            }
                        } else {
                            LazyColumn {
                                if (showSearch) {
                                    item {
                                        AntInput(
                                            value = searchQuery,
                                            onValueChange = {
                                                if (searchValue == null) {
                                                    internalSearchQuery = it
                                                }
                                                onSearch?.invoke(it)
                                            },
                                            placeholder = "Search...",
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }

                                val allOptions = optGroups?.flatMap { it.options } ?: options
                                var filteredOptions = if (searchQuery.isNotEmpty()) {
                                    val filter = filterOption ?: { input, option ->
                                        option.label.contains(input, ignoreCase = true)
                                    }
                                    allOptions.filter { filter(searchQuery, it) }
                                } else {
                                    allOptions
                                }

                                if (filterSort != null && searchQuery.isNotEmpty()) {
                                    filteredOptions = filteredOptions.sortedWith { a, b ->
                                        filterSort(a, b, FilterSortInfo(searchValue = searchQuery))
                                    }
                                }

                                if (filteredOptions.isEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            notFoundContent?.invoke() ?: Text(
                                                "No data",
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else if (optGroups != null) {
                                    optGroups.forEach { group ->
                                        val groupFilteredOptions = group.options.filter {
                                            filteredOptions.contains(it)
                                        }
                                        if (groupFilteredOptions.isNotEmpty()) {
                                            item {
                                                Text(
                                                    text = group.label,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                                )
                                            }
                                            items(groupFilteredOptions) { option ->
                                                SelectOptionItem(
                                                    option = option,
                                                    selected = values.contains(option.value),
                                                    mode = SelectMode.Multiple,
                                                    theme = theme,
                                                    onClick = {
                                                        if (!option.disabled) {
                                                            val newValues = if (values.contains(option.value)) {
                                                                onDeselect?.invoke(option.value, option)
                                                                values.filter { it != option.value }
                                                            } else {
                                                                if (values.size < effectiveMaxCount) {
                                                                    onSelect?.invoke(option.value, option)
                                                                    values + option.value
                                                                } else {
                                                                    values
                                                                }
                                                            }
                                                            onValuesChange(newValues)
                                                            if (autoClearSearchValue && searchValue == null) {
                                                                internalSearchQuery = ""
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    items(filteredOptions) { option ->
                                        SelectOptionItem(
                                            option = option,
                                            selected = values.contains(option.value),
                                            mode = SelectMode.Multiple,
                                            theme = theme,
                                            onClick = {
                                                if (!option.disabled) {
                                                    val newValues = if (values.contains(option.value)) {
                                                        onDeselect?.invoke(option.value, option)
                                                        values.filter { it != option.value }
                                                    } else {
                                                        if (values.size < effectiveMaxCount) {
                                                            onSelect?.invoke(option.value, option)
                                                            values + option.value
                                                        } else {
                                                            values
                                                        }
                                                    }
                                                    onValuesChange(newValues)
                                                    if (autoClearSearchValue && searchValue == null) {
                                                        internalSearchQuery = ""
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (dropdownRender != null) {
                        dropdownRender(menu)
                    } else {
                        menu()
                    }
                }
            }
        }
    }
}

// Tags Select Implementation
@Composable
private fun <T> AntSelectTags(
    values: List<T>,
    onValuesChange: (List<T>) -> Unit,
    options: List<SelectOption<T>>,
    modifier: Modifier = Modifier,
    placeholder: String = "Please select",
    disabled: Boolean = false,
    allowClear: Boolean = false,
    showSearch: Boolean = true, // Tags mode typically has search enabled
    loading: Boolean = false,
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    autoClearSearchValue: Boolean = true,
    searchValue: String? = null,
    maxTagCount: MaxTagCount? = null,
    maxTagPlaceholder: @Composable ((omittedValues: List<LabeledValue<T>>) -> Unit)? = null,
    maxTagTextLength: Int? = null,
    maxCount: Int? = null,
    tokenSeparators: List<String> = listOf(","),
    optGroups: List<SelectOptGroup<T>>? = null,
    optionFilterProp: String = "label",
    optionLabelProp: String = "children",
    filterOption: ((input: String, option: SelectOption<T>) -> Boolean)? = null,
    filterSort: ((a: SelectOption<T>, b: SelectOption<T>, info: FilterSortInfo) -> Int)? = null,
    onSearch: ((value: String) -> Unit)? = null,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    autoFocus: Boolean = false,
    defaultActiveFirstOption: Boolean = true,
    labelInValue: Boolean = false,
    fieldNames: FieldNames? = null,
    onChange: ((value: T?, option: SelectOption<T>?) -> Unit)? = null,
    onSelect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onDeselect: ((value: T, option: SelectOption<T>) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onOpenChange: ((open: Boolean) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onInputKeyDown: ((KeyEvent) -> Unit)? = null,
    onPopupScroll: (() -> Unit)? = null,
    virtual: Boolean = true,
    listHeight: Dp = 256.dp,
    variant: SelectVariant? = null,
    showArrow: Boolean = true,
    placement: SelectPlacement = SelectPlacement.BottomLeft,
    popupMatchSelectWidth: PopupMatchSelectWidth = PopupMatchSelectWidth.True,
    dropdownStyle: Modifier = Modifier,
    classNames: SelectClassNames? = null,
    styles: SelectStyles? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null,
    removeIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    menuItemSelectedIcon: (@Composable () -> Unit)? = null,
    dropdownRender: (@Composable (menu: @Composable () -> Unit) -> Unit)? = null,
    tagRender: (@Composable (props: TagRenderProps<T>) -> Unit)? = null,
    optionRender: (@Composable (option: SelectOption<T>, info: OptionRenderInfo) -> Unit)? = null,
    labelRender: (@Composable (props: LabelRenderProps) -> Unit)? = null,
    notFoundContent: @Composable (() -> Unit)? = null,
    getPopupContainer: (() -> Any)? = null,
) {
    val config = useConfig()
    val theme = useTheme()

    var expanded by remember { mutableStateOf(defaultOpen) }
    var internalSearchQuery by remember { mutableStateOf("") }
    var activeOptionIndex by remember { mutableStateOf(if (defaultActiveFirstOption) 0 else -1) }

    val searchQuery = searchValue ?: internalSearchQuery
    val isExpanded = open ?: expanded
    val effectiveMaxCount = maxCount ?: Int.MAX_VALUE

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            onFocus?.invoke()
        }
    }

    val effectiveVariant = variant ?: SelectVariant.Outlined

    val (borderColor, backgroundColor) = getVariantColors(effectiveVariant, status, disabled, theme)

    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Middle -> PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        InputSize.Small -> PaddingValues(horizontal = 6.dp, vertical = 4.dp)
    }

    LaunchedEffect(isExpanded) {
        onOpenChange?.invoke(isExpanded)
    }

    // Handle token separators for creating new tags
    fun handleInput(input: String) {
        var remainingInput = input
        tokenSeparators.forEach { separator ->
            if (remainingInput.contains(separator)) {
                val parts = remainingInput.split(separator)
                parts.filter { it.isNotBlank() }.forEach { part ->
                    val trimmed = part.trim()
                    // Create new tag (cast String to T - assuming T is String for tags mode)
                    @Suppress("UNCHECKED_CAST")
                    val newValue = trimmed as T
                    if (!values.contains(newValue) && values.size < effectiveMaxCount) {
                        val newOption = SelectOption(newValue, trimmed)
                        onValuesChange(values + newValue)
                        onChange?.invoke(newValue, newOption)
                    }
                }
                remainingInput = ""
                if (searchValue == null) {
                    internalSearchQuery = ""
                }
            }
        }
        if (remainingInput.isNotBlank()) {
            if (searchValue == null) {
                internalSearchQuery = remainingInput
            }
        }
    }

    Box(modifier = modifier.then(styles?.root ?: Modifier)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    if (open == null) {
                        expanded = !expanded
                    }
                    onFocus?.invoke()
                }
                .onKeyEvent { keyEvent ->
                    onInputKeyDown?.invoke(keyEvent)
                    false
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = if (effectiveVariant == SelectVariant.Borderless) null
            else BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(theme.token.borderRadius)
        ) {
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefixIcon?.invoke()

                    // Display selected tags with input
                    Column {
                        if (values.isEmpty() && searchQuery.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Handle maxTagCount: Number or Responsive
                                val effectiveMaxTagCount = when (maxTagCount) {
                                    is MaxTagCount.Number -> maxTagCount.count
                                    is MaxTagCount.Responsive -> 3 // TODO: Calculate based on available width
                                    null -> null
                                }

                                val displayValues =
                                    if (effectiveMaxTagCount != null && values.size > effectiveMaxTagCount) {
                                        values.take(effectiveMaxTagCount)
                                    } else {
                                        values
                                    }

                                displayValues.forEach { value ->
                                    val label =
                                        if (maxTagTextLength != null && value.toString().length > maxTagTextLength) {
                                            value.toString().take(maxTagTextLength) + "..."
                                        } else {
                                            value.toString()
                                        }

                                    if (tagRender != null) {
                                        val tagProps = TagRenderProps(
                                            label = label,
                                            value = value,
                                            disabled = false,
                                            closable = true,
                                            onClose = {
                                                val newValues = values.filter { it != value }
                                                onValuesChange(newValues)
                                                onDeselect?.invoke(value, SelectOption(value, value.toString()))
                                            }
                                        )
                                        tagRender(tagProps)
                                    } else {
                                        AntTag(
                                            text = label,
                                            closable = true,
                                            onClose = {
                                                val newValues = values.filter { it != value }
                                                onValuesChange(newValues)
                                                onDeselect?.invoke(value, SelectOption(value, value.toString()))
                                            }
                                        )
                                    }
                                }

                                // Show maxTagPlaceholder if exceeded
                                if (effectiveMaxTagCount != null && values.size > effectiveMaxTagCount) {
                                    val omittedValues = values.drop(effectiveMaxTagCount).map { v ->
                                        LabeledValue(value = v, label = v.toString())
                                    }
                                    if (maxTagPlaceholder != null) {
                                        maxTagPlaceholder(omittedValues)
                                    } else {
                                        Text(
                                            text = "+${omittedValues.size}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                // Inline input for creating new tags
                                if (isExpanded) {
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = {
                                            handleInput(it)
                                            onSearch?.invoke(it)
                                        },
                                        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                        modifier = Modifier
                                            .widthIn(min = 50.dp)
                                            .onKeyEvent { keyEvent ->
                                                if (keyEvent.type == KeyEventType.KeyUp &&
                                                    keyEvent.key == Key.Enter &&
                                                    searchQuery.isNotBlank() &&
                                                    values.size < effectiveMaxCount
                                                ) {
                                                    @Suppress("UNCHECKED_CAST")
                                                    val newValue = searchQuery.trim() as T
                                                    if (!values.contains(newValue)) {
                                                        onValuesChange(values + newValue)
                                                    }
                                                    if (searchValue == null) {
                                                        internalSearchQuery = ""
                                                    }
                                                    true
                                                } else {
                                                    onInputKeyDown?.invoke(keyEvent)
                                                    false
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (allowClear && values.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onValuesChange(emptyList())
                                onChange?.invoke(null, null)
                                onClear?.invoke()
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            if (clearIcon != null) {
                                clearIcon()
                            } else if (removeIcon != null) {
                                removeIcon()
                            } else {
                                Text("✕", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    if (showArrow) {
                        if (suffixIcon != null) {
                            suffixIcon()
                        } else {
                            Text(
                                text = if (isExpanded) "▲" else "▼",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (isExpanded) {
            Popup(
                onDismissRequest = {
                    if (open == null) {
                        expanded = false
                    }
                    if (autoClearSearchValue && searchValue == null) {
                        internalSearchQuery = ""
                    }
                    onBlur?.invoke()
                },
                alignment = Alignment.TopStart
            ) {
                Card(
                    modifier = Modifier
                        .then(
                            when (popupMatchSelectWidth) {
                                is PopupMatchSelectWidth.True -> Modifier.fillMaxWidth()
                                is PopupMatchSelectWidth.False -> Modifier.widthIn(min = 200.dp)
                                is PopupMatchSelectWidth.Width -> Modifier.width(popupMatchSelectWidth.width)
                            }
                        )
                        .heightIn(max = listHeight)
                        .then(dropdownStyle)
                        .then(styles?.popup?.root ?: Modifier),
                    shape = RoundedCornerShape(theme.token.borderRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val menu = @Composable {
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AntSpin(spinning = true, size = SpinSize.Small)
                            }
                        } else {
                            LazyColumn {
                                val allOptions = optGroups?.flatMap { it.options } ?: options
                                var filteredOptions = if (searchQuery.isNotEmpty()) {
                                    val filter = filterOption ?: { input, option ->
                                        option.label.contains(input, ignoreCase = true)
                                    }
                                    allOptions.filter { filter(searchQuery, it) }
                                } else {
                                    allOptions
                                }

                                if (filterSort != null && searchQuery.isNotEmpty()) {
                                    filteredOptions = filteredOptions.sortedWith { a, b ->
                                        filterSort(a, b, FilterSortInfo(searchValue = searchQuery))
                                    }
                                }

                                // Show "Create" option if search query doesn't match any existing option
                                if (searchQuery.isNotBlank() &&
                                    !allOptions.any { it.label.equals(searchQuery, ignoreCase = true) } &&
                                    values.size < effectiveMaxCount
                                ) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    @Suppress("UNCHECKED_CAST")
                                                    val newValue = searchQuery.trim() as T
                                                    if (!values.contains(newValue)) {
                                                        onValuesChange(values + newValue)
                                                    }
                                                    if (searchValue == null) {
                                                        internalSearchQuery = ""
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Create \"$searchQuery\"",
                                                fontSize = 14.sp,
                                                color = theme.token.colorPrimary
                                            )
                                        }
                                    }
                                }

                                if (filteredOptions.isEmpty() && searchQuery.isBlank()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            notFoundContent?.invoke() ?: Text(
                                                "No data",
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else if (optGroups != null) {
                                    optGroups.forEach { group ->
                                        val groupFilteredOptions = group.options.filter {
                                            filteredOptions.contains(it)
                                        }
                                        if (groupFilteredOptions.isNotEmpty()) {
                                            item {
                                                Text(
                                                    text = group.label,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                                )
                                            }
                                            items(groupFilteredOptions) { option ->
                                                SelectOptionItem(
                                                    option = option,
                                                    selected = values.contains(option.value),
                                                    mode = SelectMode.Tags,
                                                    theme = theme,
                                                    onClick = {
                                                        if (!option.disabled) {
                                                            val newValues = if (values.contains(option.value)) {
                                                                onDeselect?.invoke(option.value, option)
                                                                values.filter { it != option.value }
                                                            } else {
                                                                onSelect?.invoke(option.value, option)
                                                                values + option.value
                                                            }
                                                            onValuesChange(newValues)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    items(filteredOptions) { option ->
                                        SelectOptionItem(
                                            option = option,
                                            selected = values.contains(option.value),
                                            mode = SelectMode.Tags,
                                            theme = theme,
                                            onClick = {
                                                if (!option.disabled) {
                                                    val newValues = if (values.contains(option.value)) {
                                                        onDeselect?.invoke(option.value, option)
                                                        values.filter { it != option.value }
                                                    } else {
                                                        onSelect?.invoke(option.value, option)
                                                        values + option.value
                                                    }
                                                    onValuesChange(newValues)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (dropdownRender != null) {
                        dropdownRender(menu)
                    } else {
                        menu()
                    }
                }
            }
        }
    }
}

// Shared Option Item Component
@Composable
private fun <T> SelectOptionItem(
    option: SelectOption<T>,
    selected: Boolean,
    mode: SelectMode,
    theme: AntThemeConfig,
    onClick: () -> Unit,
    menuItemSelectedIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) theme.components.select.optionSelectedBg else Color.Transparent)
            .clickable(enabled = !option.disabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Show checkbox for Multiple and Tags mode
        if (mode == SelectMode.Multiple || mode == SelectMode.Tags) {
            AntCheckbox(
                checked = selected,
                onCheckedChange = { onClick() },
                disabled = option.disabled,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        option.icon?.invoke()

        Text(
            text = option.label,
            fontSize = 14.sp,
            color = if (option.disabled) Color.Gray
            else if (selected) theme.token.colorPrimary
            else Color.Black,
            modifier = Modifier.weight(1f).padding(start = if (option.icon != null) 8.dp else 0.dp)
        )

        // Show checkmark for Single mode when selected
        if (mode == SelectMode.Single && selected) {
            if (menuItemSelectedIcon != null) {
                menuItemSelectedIcon()
            } else {
                Text("✓", color = theme.token.colorPrimary, fontSize = 14.sp)
            }
        }
    }
}

// Helper function to get variant colors
private fun getVariantColors(
    variant: SelectVariant,
    status: InputStatus,
    disabled: Boolean,
    theme: AntThemeConfig,
): Pair<Color, Color> {
    val borderColor = when {
        disabled -> Color(0xFFD9D9D9)
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        variant == SelectVariant.Borderless -> Color.Transparent
        else -> Color(0xFFD9D9D9)
    }

    val backgroundColor = when {
        disabled -> Color(0xFFF5F5F5)
        variant == SelectVariant.Filled -> Color(0xFFFAFAFA)
        variant == SelectVariant.Borderless -> Color.Transparent
        else -> Color.White
    }

    return Pair(borderColor, backgroundColor)
}
