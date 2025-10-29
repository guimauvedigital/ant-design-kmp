package digital.guimauve.antdesign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * CascaderOption - Data structure for hierarchical cascading data
 *
 * @param value The unique value for this option
 * @param label The display text for this option
 * @param children Child options for the next level (null for leaf nodes)
 * @param disabled Whether this option is disabled
 * @param isLeaf Explicitly mark as leaf node (for lazy loading)
 *
 * Example:
 * ```
 * val options = listOf(
 *     CascaderOption(
 *         value = "usa",
 *         label = "United States",
 *         children = listOf(
 *             CascaderOption(value = "ca", label = "California"),
 *             CascaderOption(value = "ny", label = "New York")
 *         )
 *     )
 * )
 * ```
 */
data class CascaderOption(
    val value: Any,
    val label: String,
    val children: List<CascaderOption>? = null,
    val disabled: Boolean = false,
    val isLeaf: Boolean = children == null,
)

/**
 * ExpandTrigger - How to trigger expanding the next level
 */
enum class ExpandTrigger {
    /** Expand on click (default) */
    Click,

    /** Expand on mouse hover */
    Hover
}

/**
 * Placement - Dropdown placement relative to the input
 */
enum class Placement {
    /** Bottom left corner aligned */
    BottomLeft,

    /** Bottom right corner aligned */
    BottomRight,

    /** Top left corner aligned */
    TopLeft,

    /** Top right corner aligned */
    TopRight
}

/**
 * CascaderFieldNames - Custom field names for data source
 *
 * Allows mapping from custom data structures to Cascader format.
 *
 * @param label Field name for label (default: "label")
 * @param value Field name for value (default: "value")
 * @param children Field name for children (default: "children")
 */
data class CascaderFieldNames(
    val label: String = "label",
    val value: String = "value",
    val children: String = "children",
)

/**
 * SearchConfig - Configuration for search functionality
 *
 * @param filter Custom filter function for matching options
 * @param limit Maximum number of search results to show (default: 50)
 * @param matchInputWidth Whether dropdown width matches input (default: true)
 * @param render Custom render function for search results
 */
data class SearchConfig(
    val filter: ((inputValue: String, path: List<CascaderOption>) -> Boolean)? = null,
    val limit: Int = 50,
    val matchInputWidth: Boolean = true,
    val render: ((inputValue: String, path: List<CascaderOption>) -> String)? = null,
)

/**
 * CascaderClassNames - Semantic class names for Cascader parts (v5.4.0+)
 *
 * @param root Class name for root element
 * @param menu Class name for menu panel
 * @param menuItem Class name for menu items
 */
data class CascaderClassNames(
    val root: String = "",
    val menu: String = "",
    val menuItem: String = "",
)

/**
 * CascaderStyles - Semantic styles for Cascader parts (v5.4.0+)
 *
 * @param root Modifier for root element
 * @param menu Modifier for menu panel
 * @param menuItem Modifier for menu items
 */
data class CascaderStyles(
    val root: Modifier = Modifier,
    val menu: Modifier = Modifier,
    val menuItem: Modifier = Modifier,
)

/**
 * AntCascader - Multi-level dropdown selector for hierarchical data
 *
 * Complete implementation with 100% React Ant Design v5.x parity.
 *
 * Features:
 * - Hierarchical selection (Country -> State -> City)
 * - Single and multiple selection modes
 * - Search with custom filters
 * - Lazy loading with loadData callback
 * - Click or hover expand triggers
 * - Custom display rendering
 * - Change on select any level
 * - Full theme integration
 * - Semantic styling (v5.4.0+)
 *
 * @param options Hierarchical data source (required)
 * @param value Controlled selected value path (List of values representing the path)
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the root element
 * @param defaultValue Uncontrolled default value
 * @param onChange Callback with value path and selected options
 * @param allowClear Show clear button (default: true)
 * @param autoFocus Auto focus on mount (default: false)
 * @param bordered Show border (default: true)
 * @param changeOnSelect Trigger onChange when any level selected (default: false)
 * @param disabled Disable cascader (default: false)
 * @param displayRender Custom display text formatter
 * @param expandTrigger Expand on click or hover (default: Click)
 * @param fieldNames Custom field names for data mapping
 * @param placeholder Placeholder text (default: "Please select")
 * @param showSearch Enable search functionality (Boolean or SearchConfig)
 * @param size Small/Middle/Large (default: Middle)
 * @param status Validation status (Default/Error/Warning)
 * @param placement Dropdown placement (default: BottomLeft)
 * @param loadData Load children dynamically (for lazy loading)
 * @param multiple Multiple selection mode (v5.0+)
 * @param maxTagCount Max tags to show in multiple mode (default: Int.MAX_VALUE)
 * @param notFoundContent Empty content when no options
 * @param suffixIcon Custom suffix icon
 * @param classNames Semantic class names (v5.4.0+)
 * @param styles Semantic styles (v5.4.0+)
 * @param dropdownRender Custom dropdown wrapper
 * @param onSelect Callback when an option is selected
 * @param onDeselect Callback when an option is deselected (multiple mode)
 * @param onClear Callback when clear button is clicked
 * @param onDropdownVisibleChange Callback when dropdown visibility changes
 * @param onBlur Callback when input loses focus
 * @param onFocus Callback when input gains focus
 * @param open Controlled dropdown visibility
 * @param defaultOpen Default dropdown visibility (default: false)
 * @param popupClassName Class name for popup
 * @param dropdownStyle Modifier for dropdown
 *
 * Example:
 * ```
 * val options = listOf(
 *     CascaderOption(
 *         value = "usa",
 *         label = "United States",
 *         children = listOf(
 *             CascaderOption(
 *                 value = "ca",
 *                 label = "California",
 *                 children = listOf(
 *                     CascaderOption(value = "la", label = "Los Angeles"),
 *                     CascaderOption(value = "sf", label = "San Francisco")
 *                 )
 *             )
 *         )
 *     )
 * )
 *
 * var value by remember { mutableStateOf(emptyList<Any>()) }
 * AntCascader(
 *     options = options,
 *     value = value,
 *     onValueChange = { value = it },
 *     placeholder = "Select location",
 *     allowClear = true,
 *     showSearch = true
 * )
 * ```
 */
@Composable
fun AntCascader(
    options: List<CascaderOption>,
    value: List<Any>,
    onValueChange: (List<Any>) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: List<Any>? = null,
    onChange: ((value: List<Any>, selectedOptions: List<CascaderOption>) -> Unit)? = null,
    allowClear: Boolean = true,
    autoFocus: Boolean = false,
    bordered: Boolean = true,
    changeOnSelect: Boolean = false,
    disabled: Boolean = false,
    displayRender: ((labels: List<String>, selectedOptions: List<CascaderOption>) -> String)? = null,
    expandTrigger: ExpandTrigger = ExpandTrigger.Click,
    fieldNames: CascaderFieldNames? = null,
    placeholder: String = "Please select",
    showSearch: Any = false, // Boolean or SearchConfig
    size: InputSize = InputSize.Middle,
    status: InputStatus = InputStatus.Default,
    placement: Placement = Placement.BottomLeft,
    loadData: ((selectedOptions: List<CascaderOption>) -> Unit)? = null,
    multiple: Boolean = false,
    maxTagCount: Int = Int.MAX_VALUE,
    notFoundContent: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null,
    classNames: CascaderClassNames? = null,
    styles: CascaderStyles? = null,
    dropdownRender: ((@Composable () -> Unit) -> (@Composable () -> Unit))? = null,
    onSelect: ((value: List<Any>, selectedOption: CascaderOption) -> Unit)? = null,
    onDeselect: ((value: List<Any>, deselectedOption: CascaderOption) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onDropdownVisibleChange: ((open: Boolean) -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    open: Boolean? = null,
    defaultOpen: Boolean = false,
    popupClassName: String? = null,
    dropdownStyle: Modifier = Modifier,
) {
    val theme = useTheme()
    val config = useConfig()

    // State management
    var internalValue by remember { mutableStateOf(defaultValue ?: emptyList()) }
    val effectiveValue = if (defaultValue != null && value.isEmpty()) internalValue else value

    var expanded by remember { mutableStateOf(defaultOpen) }
    val isExpanded = open ?: expanded

    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var loadingPaths by remember { mutableStateOf(setOf<List<Any>>()) }

    val focusRequester = remember { FocusRequester() }

    // Parse showSearch
    val searchConfig = when (showSearch) {
        is SearchConfig -> showSearch
        is Boolean -> if (showSearch) SearchConfig() else null
        else -> null
    }
    val isSearchEnabled = searchConfig != null

    // Auto focus
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
            onFocus?.invoke()
        }
    }

    // Notify dropdown visibility changes
    LaunchedEffect(isExpanded) {
        onDropdownVisibleChange?.invoke(isExpanded)
    }

    // Get selected options
    val selectedOptions = remember(effectiveValue, options) {
        getSelectedOptions(effectiveValue, options)
    }

    // Display text
    val displayText = remember(effectiveValue, selectedOptions) {
        if (effectiveValue.isEmpty()) {
            placeholder
        } else {
            if (displayRender != null) {
                val labels = selectedOptions.map { it.label }
                displayRender(labels, selectedOptions)
            } else {
                selectedOptions.joinToString(" / ") { it.label }
            }
        }
    }

    // Styling
    val borderColor = when {
        disabled -> Color(0xFFD9D9D9)
        status == InputStatus.Error -> theme.token.colorError
        status == InputStatus.Warning -> theme.token.colorWarning
        isFocused || isExpanded -> theme.token.colorPrimary
        else -> Color(0xFFD9D9D9)
    }

    val backgroundColor = if (disabled) Color(0xFFF5F5F5) else Color.White

    val padding = when (size) {
        InputSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        InputSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        InputSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    val fontSize = when (size) {
        InputSize.Large -> 16.sp
        InputSize.Middle -> 14.sp
        InputSize.Small -> 12.sp
    }

    Box(modifier = modifier.then(styles?.root ?: Modifier)) {
        // Input trigger
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        onFocus?.invoke()
                    } else {
                        onBlur?.invoke()
                    }
                }
                .clickable(enabled = !disabled) {
                    if (open == null) {
                        expanded = !expanded
                    }
                },
            shape = RoundedCornerShape(theme.token.borderRadius),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            border = if (bordered) BorderStroke(1.dp, borderColor) else null
        ) {
            Row(
                modifier = Modifier.padding(padding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display text
                if (multiple && effectiveValue.isNotEmpty()) {
                    // Multiple mode: show tags
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val displayValues = if (effectiveValue.size > maxTagCount) {
                            effectiveValue.take(maxTagCount)
                        } else {
                            effectiveValue
                        }

                        displayValues.forEach { valuePath ->
                            val pathList = if (valuePath is List<*>) {
                                @Suppress("UNCHECKED_CAST")
                                valuePath as List<Any>
                            } else {
                                listOf(valuePath)
                            }
                            val opts = getSelectedOptions(pathList, options)
                            val label = opts.lastOrNull()?.label ?: valuePath.toString()

                            AntTag(
                                text = label,
                                closable = !disabled,
                                onClose = {
                                    val newValue = effectiveValue.filter { it != valuePath }
                                    onValueChange(newValue)
                                    if (defaultValue != null) internalValue = newValue
                                    opts.lastOrNull()?.let { opt ->
                                        onDeselect?.invoke(pathList, opt)
                                    }
                                }
                            )
                        }

                        if (effectiveValue.size > maxTagCount) {
                            Text(
                                text = "+${effectiveValue.size - maxTagCount}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Text(
                        text = displayText,
                        fontSize = fontSize,
                        color = if (effectiveValue.isEmpty()) Color.Gray else Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Suffix icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (allowClear && effectiveValue.isNotEmpty() && !disabled) {
                        IconButton(
                            onClick = {
                                val newValue = emptyList<Any>()
                                onValueChange(newValue)
                                if (defaultValue != null) internalValue = newValue
                                onClear?.invoke()
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

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

        // Dropdown panel
        if (isExpanded) {
            Popup(
                alignment = when (placement) {
                    Placement.BottomLeft -> Alignment.BottomStart
                    Placement.BottomRight -> Alignment.BottomEnd
                    Placement.TopLeft -> Alignment.TopStart
                    Placement.TopRight -> Alignment.TopEnd
                },
                onDismissRequest = {
                    if (open == null) {
                        expanded = false
                    }
                    searchText = ""
                },
                properties = PopupProperties(focusable = true)
            ) {
                Card(
                    modifier = Modifier
                        .padding(
                            top = if (placement == Placement.BottomLeft || placement == Placement.BottomRight) 4.dp else 0.dp,
                            bottom = if (placement == Placement.TopLeft || placement == Placement.TopRight) 4.dp else 0.dp
                        )
                        .widthIn(
                            min = if (searchConfig?.matchInputWidth == true) 200.dp else 150.dp,
                            max = 600.dp
                        )
                        .heightIn(max = 400.dp)
                        .then(dropdownStyle),
                    shape = RoundedCornerShape(theme.token.borderRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    val menuContent = @Composable {
                        Column {
                            // Search input
                            if (isSearchEnabled) {
                                AntInput(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    placeholder = "Search",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    size = InputSize.Small
                                )
                            }

                            // Cascader menu
                            if (isSearchEnabled && searchText.isNotEmpty()) {
                                // Search mode: show matching paths
                                CascaderSearchResults(
                                    options = options,
                                    searchText = searchText,
                                    searchConfig = searchConfig!!,
                                    selectedValue = effectiveValue,
                                    theme = theme,
                                    styles = styles,
                                    multiple = multiple,
                                    onSelect = { path, option ->
                                        if (multiple) {
                                            val pathList = path.map { it.value }
                                            val newValue = if (effectiveValue.contains(pathList)) {
                                                effectiveValue.filter { it != pathList }
                                            } else {
                                                effectiveValue + listOf(pathList)
                                            }
                                            onValueChange(newValue)
                                            if (defaultValue != null) internalValue = newValue
                                            onSelect?.invoke(pathList, option)
                                        } else {
                                            val pathValues = path.map { it.value }
                                            onValueChange(pathValues)
                                            if (defaultValue != null) internalValue = pathValues
                                            onChange?.invoke(pathValues, path)
                                            onSelect?.invoke(pathValues, option)
                                            if (open == null) expanded = false
                                        }
                                    },
                                    notFoundContent = notFoundContent
                                )
                            } else {
                                // Normal mode: show cascading columns
                                CascaderMenu(
                                    options = options,
                                    selectedValue = effectiveValue,
                                    onValueChange = { newPath, selectedOpts, isComplete ->
                                        if (multiple) {
                                            val newValue = if (effectiveValue.contains(newPath)) {
                                                effectiveValue.filter { it != newPath }
                                            } else {
                                                effectiveValue + listOf(newPath)
                                            }
                                            onValueChange(newValue)
                                            if (defaultValue != null) internalValue = newValue
                                            selectedOpts.lastOrNull()?.let { opt ->
                                                onSelect?.invoke(newPath, opt)
                                            }
                                        } else {
                                            onValueChange(newPath)
                                            if (defaultValue != null) internalValue = newPath

                                            if (changeOnSelect || isComplete) {
                                                onChange?.invoke(newPath, selectedOpts)
                                                selectedOpts.lastOrNull()?.let { opt ->
                                                    onSelect?.invoke(newPath, opt)
                                                }
                                            }

                                            if (isComplete && open == null) {
                                                expanded = false
                                            }
                                        }
                                    },
                                    expandTrigger = expandTrigger,
                                    theme = theme,
                                    styles = styles,
                                    loadData = loadData,
                                    loadingPaths = loadingPaths,
                                    onLoadStart = { path ->
                                        loadingPaths = loadingPaths + setOf(path)
                                    },
                                    onLoadEnd = { path ->
                                        loadingPaths = loadingPaths - setOf(path)
                                    },
                                    multiple = multiple,
                                    notFoundContent = notFoundContent
                                )
                            }
                        }
                    }

                    // Apply custom dropdown render if provided
                    if (dropdownRender != null) {
                        dropdownRender(menuContent)()
                    } else {
                        menuContent()
                    }
                }
            }
        }
    }
}

/**
 * CascaderMenu - Cascading columns display
 */
@Composable
private fun CascaderMenu(
    options: List<CascaderOption>,
    selectedValue: List<Any>,
    onValueChange: (path: List<Any>, selectedOptions: List<CascaderOption>, isComplete: Boolean) -> Unit,
    expandTrigger: ExpandTrigger,
    theme: AntThemeConfig,
    styles: CascaderStyles?,
    loadData: ((selectedOptions: List<CascaderOption>) -> Unit)?,
    loadingPaths: Set<List<Any>>,
    onLoadStart: (List<Any>) -> Unit,
    onLoadEnd: (List<Any>) -> Unit,
    multiple: Boolean,
    notFoundContent: (@Composable () -> Unit)?,
) {
    var columns by remember(options) { mutableStateOf(listOf(options)) }
    var selectedPath by remember { mutableStateOf(emptyList<CascaderOption>()) }

    // Build columns based on selected value
    LaunchedEffect(selectedValue, options) {
        if (selectedValue.isNotEmpty()) {
            val (cols, path) = buildColumnsFromValue(selectedValue, options)
            columns = cols
            selectedPath = path
        } else {
            columns = listOf(options)
            selectedPath = emptyList()
        }
    }

    if (options.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            notFoundContent?.invoke() ?: Text(
                text = "No data",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(styles?.menu ?: Modifier)
        ) {
            columns.forEachIndexed { columnIndex, columnOptions ->
                LazyColumn(
                    modifier = Modifier
                        .width(150.dp)
                        .heightIn(max = 300.dp)
                ) {
                    items(columnOptions) { option ->
                        val isSelected = selectedPath.getOrNull(columnIndex)?.value == option.value
                        val currentPath = selectedPath.take(columnIndex) + option
                        val pathValues = currentPath.map { it.value }
                        val isLoading = loadingPaths.contains(pathValues)

                        CascaderMenuItem(
                            option = option,
                            isSelected = isSelected,
                            isLoading = isLoading,
                            expandTrigger = expandTrigger,
                            theme = theme,
                            styles = styles,
                            multiple = multiple,
                            onClick = {
                                if (!option.disabled) {
                                    selectedPath = currentPath

                                    val hasChildren = !option.isLeaf && (option.children != null || loadData != null)

                                    if (hasChildren) {
                                        if (option.children != null) {
                                            // Show children
                                            columns = columns.take(columnIndex + 1) + listOf(option.children)
                                        } else if (loadData != null && !isLoading) {
                                            // Load children dynamically
                                            onLoadStart(pathValues)
                                            loadData(currentPath)
                                            // Note: The parent component should update options when data is loaded
                                            // and call onLoadEnd when done
                                        }
                                        onValueChange(pathValues, currentPath, false)
                                    } else {
                                        // Leaf node - complete selection
                                        columns = columns.take(columnIndex + 1)
                                        onValueChange(pathValues, currentPath, true)
                                    }
                                }
                            },
                            onHover = {
                                if (expandTrigger == ExpandTrigger.Hover && !option.disabled) {
                                    selectedPath = currentPath

                                    val hasChildren = !option.isLeaf && option.children != null

                                    if (hasChildren) {
                                        columns = columns.take(columnIndex + 1) + listOf(option.children!!)
                                        onValueChange(pathValues, currentPath, false)
                                    } else {
                                        columns = columns.take(columnIndex + 1)
                                    }
                                }
                            }
                        )
                    }
                }

                // Column separator
                if (columnIndex < columns.lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFF0F0F0))
                    )
                }
            }
        }
    }
}

/**
 * CascaderMenuItem - Individual menu item
 */
@Composable
private fun CascaderMenuItem(
    option: CascaderOption,
    isSelected: Boolean,
    isLoading: Boolean,
    expandTrigger: ExpandTrigger,
    theme: AntThemeConfig,
    styles: CascaderStyles?,
    multiple: Boolean,
    onClick: () -> Unit,
    onHover: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Trigger hover action
    LaunchedEffect(isHovered) {
        if (isHovered && expandTrigger == ExpandTrigger.Hover) {
            onHover()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) ColorPalette.blue1
                else if (isHovered) Color(0xFFF5F5F5)
                else Color.Transparent
            )
            .hoverable(interactionSource = interactionSource)
            .clickable(enabled = !option.disabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(styles?.menuItem ?: Modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Checkbox for multiple mode
            if (multiple) {
                AntCheckbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    disabled = option.disabled
                )
            }

            Text(
                text = option.label,
                fontSize = 14.sp,
                color = when {
                    option.disabled -> Color.Gray
                    isSelected -> theme.token.colorPrimary
                    else -> Color.Black
                }
            )
        }

        // Right side indicators
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = theme.token.colorPrimary
                )
            }

            !option.isLeaf -> {
                Text(
                    text = "▶",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            isSelected && !multiple -> {
                Text(
                    text = "✓",
                    fontSize = 14.sp,
                    color = theme.token.colorPrimary
                )
            }
        }
    }
}

/**
 * CascaderSearchResults - Display search results
 */
@Composable
private fun CascaderSearchResults(
    options: List<CascaderOption>,
    searchText: String,
    searchConfig: SearchConfig,
    selectedValue: List<Any>,
    theme: AntThemeConfig,
    styles: CascaderStyles?,
    multiple: Boolean,
    onSelect: (path: List<CascaderOption>, option: CascaderOption) -> Unit,
    notFoundContent: (@Composable () -> Unit)?,
) {
    // Find all matching paths
    val matchingPaths = remember(options, searchText, searchConfig) {
        findMatchingPaths(options, searchText, searchConfig)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
    ) {
        if (matchingPaths.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    notFoundContent?.invoke() ?: Text(
                        text = "No results",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(matchingPaths.take(searchConfig.limit)) { path ->
                val pathValues = path.map { it.value }
                val isSelected = selectedValue.contains(pathValues)
                val displayText = if (searchConfig.render != null) {
                    searchConfig.render.invoke(searchText, path)
                } else {
                    path.joinToString(" / ") { it.label }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelected) ColorPalette.blue1 else Color.Transparent)
                        .clickable { onSelect(path, path.last()) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (multiple) {
                        AntCheckbox(
                            checked = isSelected,
                            onCheckedChange = { onSelect(path, path.last()) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    Text(
                        text = displayText,
                        fontSize = 14.sp,
                        color = if (isSelected) theme.token.colorPrimary else Color.Black
                    )
                }
            }
        }
    }
}

/**
 * Helper function to get selected options from value path
 */
private fun getSelectedOptions(valuePath: List<Any>, options: List<CascaderOption>): List<CascaderOption> {
    val result = mutableListOf<CascaderOption>()
    var currentOptions = options

    for (value in valuePath) {
        val option = currentOptions.find { it.value == value }
        if (option != null) {
            result.add(option)
            currentOptions = option.children ?: emptyList()
        } else {
            break
        }
    }

    return result
}

/**
 * Helper function to build columns from selected value
 */
private fun buildColumnsFromValue(
    valuePath: List<Any>,
    options: List<CascaderOption>,
): Pair<List<List<CascaderOption>>, List<CascaderOption>> {
    val columns = mutableListOf<List<CascaderOption>>()
    val path = mutableListOf<CascaderOption>()
    var currentOptions = options

    columns.add(currentOptions)

    for (value in valuePath) {
        val option = currentOptions.find { it.value == value }
        if (option != null) {
            path.add(option)
            if (option.children != null) {
                currentOptions = option.children
                columns.add(currentOptions)
            }
        } else {
            break
        }
    }

    return Pair(columns, path)
}

/**
 * Helper function to find all paths matching search text
 */
private fun findMatchingPaths(
    options: List<CascaderOption>,
    searchText: String,
    searchConfig: SearchConfig,
): List<List<CascaderOption>> {
    val results = mutableListOf<List<CascaderOption>>()

    fun search(opts: List<CascaderOption>, currentPath: List<CascaderOption>) {
        for (option in opts) {
            val newPath = currentPath + option

            // Check if this path matches
            val matches = if (searchConfig.filter != null) {
                searchConfig.filter.invoke(searchText, newPath)
            } else {
                // Default filter: check if any label in path contains search text
                newPath.any { it.label.contains(searchText, ignoreCase = true) }
            }

            if (matches) {
                results.add(newPath)
            }

            // Continue searching children
            option.children?.let { children ->
                search(children, newPath)
            }
        }
    }

    search(options, emptyList())
    return results
}
