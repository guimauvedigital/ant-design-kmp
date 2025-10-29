package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.story

// ============================================================================
// CASCADER COMPLETE - 100% Parameters Coverage
// ============================================================================

val CascaderComplete by story {
    // Core parameters
    val multiple by parameter(false)
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val autoFocus by parameter(false)
    val bordered by parameter(true)
    val changeOnSelect by parameter(false)

    // Display parameters
    val placeholder by parameter("Please select location")
    val size by parameter("middle") // small, middle, large
    val status by parameter("default") // default, error, warning

    // Search parameters
    val showSearch by parameter(true)
    val searchLimit by parameter(50)

    // Multiple mode parameters
    val maxTagCount by parameter(3)
    val maxTagTextLength by parameter(10)

    // Expand parameters
    val expandTrigger by parameter("click") // click, hover

    // Popup parameters
    val placement by parameter("bottomLeft") // bottomLeft, bottomRight, topLeft, topRight
    val defaultOpen by parameter(false)

    // Lazy loading state
    var loadingOptions by remember { mutableStateOf<List<CascaderOption>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Create hierarchical data with lazy loading support
    val baseOptions = remember {
        listOf(
            CascaderOption(
                value = "north-america",
                label = "North America",
                children = listOf(
                    CascaderOption(
                        value = "usa",
                        label = "United States",
                        children = listOf(
                            CascaderOption(value = "california", label = "California", isLeaf = true),
                            CascaderOption(value = "new-york", label = "New York", isLeaf = true),
                            CascaderOption(value = "texas", label = "Texas", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "canada",
                        label = "Canada",
                        children = listOf(
                            CascaderOption(value = "ontario", label = "Ontario", isLeaf = true),
                            CascaderOption(value = "quebec", label = "Quebec", isLeaf = true),
                            CascaderOption(value = "bc", label = "British Columbia", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "mexico",
                        label = "Mexico",
                        children = listOf(
                            CascaderOption(value = "mexico-city", label = "Mexico City", isLeaf = true),
                            CascaderOption(value = "guadalajara", label = "Guadalajara", isLeaf = true)
                        )
                    )
                )
            ),
            CascaderOption(
                value = "europe",
                label = "Europe",
                children = listOf(
                    CascaderOption(
                        value = "france",
                        label = "France",
                        children = listOf(
                            CascaderOption(value = "paris", label = "Paris", isLeaf = true),
                            CascaderOption(value = "marseille", label = "Marseille", isLeaf = true),
                            CascaderOption(value = "lyon", label = "Lyon", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "germany",
                        label = "Germany",
                        children = listOf(
                            CascaderOption(value = "berlin", label = "Berlin", isLeaf = true),
                            CascaderOption(value = "munich", label = "Munich", isLeaf = true),
                            CascaderOption(value = "hamburg", label = "Hamburg", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "uk",
                        label = "United Kingdom",
                        children = listOf(
                            CascaderOption(value = "london", label = "London", isLeaf = true),
                            CascaderOption(value = "manchester", label = "Manchester", isLeaf = true),
                            CascaderOption(value = "edinburgh", label = "Edinburgh", isLeaf = true)
                        )
                    )
                )
            ),
            CascaderOption(
                value = "asia",
                label = "Asia",
                children = listOf(
                    CascaderOption(
                        value = "china",
                        label = "China",
                        children = listOf(
                            CascaderOption(value = "beijing", label = "Beijing", isLeaf = true),
                            CascaderOption(value = "shanghai", label = "Shanghai", isLeaf = true),
                            CascaderOption(value = "guangzhou", label = "Guangzhou", isLeaf = true),
                            CascaderOption(value = "shenzhen", label = "Shenzhen", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "japan",
                        label = "Japan",
                        children = listOf(
                            CascaderOption(value = "tokyo", label = "Tokyo", isLeaf = true),
                            CascaderOption(value = "osaka", label = "Osaka", isLeaf = true),
                            CascaderOption(value = "kyoto", label = "Kyoto", isLeaf = true)
                        )
                    ),
                    CascaderOption(
                        value = "south-korea",
                        label = "South Korea",
                        children = listOf(
                            CascaderOption(value = "seoul", label = "Seoul", isLeaf = true),
                            CascaderOption(value = "busan", label = "Busan", isLeaf = true)
                        )
                    )
                )
            ),
            CascaderOption(
                value = "lazy-load",
                label = "Dynamic Loading (Lazy)",
                isLeaf = false
            )
        )
    }

    // State for value
    var value by remember { mutableStateOf<Any>(emptyList<Any>()) }
    var openState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cascader - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Current selection:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(
                when (value) {
                    is List<*> -> {
                        val list = value as List<*>
                        if (list.isEmpty()) "None"
                        else if (list.first() is List<*>) {
                            // Multiple mode
                            list.joinToString("; ") { path ->
                                (path as? List<*>)?.joinToString(" > ") ?: ""
                            }
                        } else {
                            // Single mode
                            list.joinToString(" > ")
                        }
                    }

                    else -> value.toString()
                },
                fontSize = 12.sp,
                color = Color(0xFF1890FF),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Main Cascader with ALL parameters
        AntCascader(
            // Required parameters
            options = baseOptions + loadingOptions,
            value = if (value is List<*>) value as List<Any> else emptyList(),
            onValueChange = { newValue ->
                value = newValue
                println("Value changed: $newValue")
            },

            // Basic parameters
            modifier = Modifier.fillMaxWidth(),
            defaultValue = null,
            onChange = { valuePath, selectedOptions ->
                println("onChange - Path: $valuePath, Options: ${selectedOptions.map { it.label }}")
            },

            // UI State parameters
            allowClear = allowClear,
            autoFocus = autoFocus,
            bordered = bordered,
            disabled = disabled,

            // Behavior parameters
            changeOnSelect = changeOnSelect,
            expandTrigger = when (expandTrigger) {
                "hover" -> ExpandTrigger.Hover
                else -> ExpandTrigger.Click
            },

            // Display parameters
            placeholder = placeholder,
            size = when (size) {
                "small" -> InputSize.Small
                "large" -> InputSize.Large
                else -> InputSize.Middle
            },
            status = when (status) {
                "error" -> InputStatus.Error
                "warning" -> InputStatus.Warning
                else -> InputStatus.Default
            },

            // Display render
            displayRender = { labels, selectedOptions ->
                if (labels.isEmpty()) placeholder
                else "${labels.joinToString(" / ")} (${selectedOptions.size} levels)"
            },

            // Search parameters
            showSearch = if (showSearch) {
                SearchConfig(
                    filter = { inputValue, path ->
                        path.any { option ->
                            option.label.contains(inputValue, ignoreCase = true)
                        }
                    },
                    limit = searchLimit,
                    matchInputWidth = true,
                    render = { inputValue, path ->
                        val labels = path.map { it.label }
                        labels.joinToString(" / ") { label ->
                            if (label.contains(inputValue, ignoreCase = true)) {
                                "[$label]"
                            } else {
                                label
                            }
                        }
                    }
                )
            } else false,

            // Multiple mode parameters
            multiple = multiple,
            maxTagCount = maxTagCount,

            // Field mapping
            fieldNames = CascaderFieldNames(
                label = "label",
                value = "value",
                children = "children"
            ),

            // Empty content
            notFoundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No Data", fontSize = 14.sp, color = Color.Gray)
                        Text("Try different search terms", fontSize = 10.sp, color = Color.LightGray)
                    }
                }
            },

            // Custom suffix icon
            suffixIcon = {
                Text(
                    text = if (openState) "▲" else "▼",
                    fontSize = 12.sp,
                    color = if (disabled) Color.Gray else Color(0xFF1890FF)
                )
            },

            // Popup parameters
            placement = when (placement) {
                "bottomRight" -> Placement.BottomRight
                "topLeft" -> Placement.TopLeft
                "topRight" -> Placement.TopRight
                else -> Placement.BottomLeft
            },
            open = null,
            defaultOpen = defaultOpen,

            // Dropdown customization
            dropdownRender = { menu ->
                {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF0F2F5))
                                .padding(8.dp)
                        ) {
                            Text(
                                "Select your location",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF595959)
                            )
                        }
                        menu()
                    }
                }
            },
            dropdownStyle = Modifier,
            popupClassName = "custom-cascader-popup",

            // Semantic styling (v5.4.0+)
            classNames = CascaderClassNames(
                root = "cascader-root",
                menu = "cascader-menu",
                menuItem = "cascader-menu-item"
            ),
            styles = CascaderStyles(
                root = Modifier,
                menu = Modifier,
                menuItem = Modifier
            ),

            // Lazy loading
            loadData = { selectedOptions ->
                coroutineScope.launch {
                    if (selectedOptions.last().value == "lazy-load") {
                        // Simulate async loading
                        delay(1000)
                        loadingOptions = listOf(
                            CascaderOption(
                                value = "lazy-load",
                                label = "Dynamic Loading (Lazy)",
                                children = listOf(
                                    CascaderOption(
                                        value = "dynamic-1",
                                        label = "Dynamic Item 1",
                                        isLeaf = true
                                    ),
                                    CascaderOption(
                                        value = "dynamic-2",
                                        label = "Dynamic Item 2",
                                        isLeaf = true
                                    ),
                                    CascaderOption(
                                        value = "dynamic-3",
                                        label = "Dynamic Item 3",
                                        isLeaf = true
                                    )
                                )
                            )
                        )
                    }
                }
            },

            // Event callbacks
            onSelect = { valuePath, selectedOption ->
                println("onSelect - Value: $valuePath, Option: ${selectedOption.label}")
            },
            onDeselect = { valuePath, deselectedOption ->
                println("onDeselect - Value: $valuePath, Option: ${deselectedOption.label}")
            },
            onClear = {
                println("onClear - All selections cleared")
            },
            onDropdownVisibleChange = { visible ->
                openState = visible
                println("onDropdownVisibleChange - Visible: $visible")
            },
            onBlur = {
                println("onBlur - Input lost focus")
            },
            onFocus = {
                println("onFocus - Input gained focus")
            }
        )

        // Demo variations
        Text(
            "Other Examples:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Disabled example
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Disabled State:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntCascader(
                    options = baseOptions,
                    value = listOf("europe", "france", "paris"),
                    onValueChange = {},
                    placeholder = "Disabled",
                    disabled = true,
                    size = InputSize.Small
                )
            }

            // Error status example
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntCascader(
                    options = baseOptions,
                    value = emptyList(),
                    onValueChange = {},
                    placeholder = "Selection required",
                    status = InputStatus.Error,
                    size = InputSize.Small
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Small size example
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small Size:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntCascader(
                    options = baseOptions,
                    value = emptyList(),
                    onValueChange = {},
                    placeholder = "Small size",
                    size = InputSize.Small,
                    showSearch = true
                )
            }

            // Large size example
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large Size:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntCascader(
                    options = baseOptions,
                    value = emptyList(),
                    onValueChange = {},
                    placeholder = "Large size",
                    size = InputSize.Large,
                    showSearch = true
                )
            }
        }
    }
}

// ============================================================================
// TREESELECT COMPLETE - 100% Parameters Coverage
// ============================================================================

val TreeSelectComplete by story {
    // Core parameters
    val multiple by parameter(false)
    val treeCheckable by parameter(false)
    val treeCheckStrictly by parameter(false)
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val autoFocus by parameter(false)

    // Tree display parameters
    val treeDefaultExpandAll by parameter(false)
    val treeLine by parameter(false)
    val showTreeIcon by parameter(true)
    val treeIcon by parameter(true)

    // Search parameters
    val showSearch by parameter(true)
    val autoClearSearchValue by parameter(true)

    // Display parameters
    val placeholder by parameter("Please select")
    val size by parameter("middle") // small, middle, large
    val status by parameter("default") // default, error, warning
    val bordered by parameter(true)

    // Multiple mode parameters
    val maxTagCount by parameter(3)
    val showCheckedStrategy by parameter("ShowChild") // ShowAll, ShowParent, ShowChild

    // Expand parameters
    val treeExpandAction by parameter("Click") // Click, DoubleClick, False

    // Popup parameters
    val placement by parameter("BottomLeft")
    val defaultOpen by parameter(false)
    val popupMatchSelectWidth by parameter(true)
    val virtual by parameter(true)
    val listHeight by parameter(256)

    // Create hierarchical tree data
    val treeData = remember {
        listOf(
            TreeSelectNode(
                value = "node-0",
                title = "Engineering",
                icon = { Text("🏢", fontSize = 14.sp) },
                children = listOf(
                    TreeSelectNode(
                        value = "node-0-0",
                        title = "Frontend",
                        icon = { Text("💻", fontSize = 14.sp) },
                        children = listOf(
                            TreeSelectNode(
                                value = "node-0-0-0",
                                title = "React Developer",
                                icon = { Text("👨‍💻", fontSize = 14.sp) },
                                isLeaf = true
                            ),
                            TreeSelectNode(
                                value = "node-0-0-1",
                                title = "Vue Developer",
                                icon = { Text("👩‍💻", fontSize = 14.sp) },
                                isLeaf = true
                            ),
                            TreeSelectNode(
                                value = "node-0-0-2",
                                title = "Angular Developer",
                                icon = { Text("🧑‍💻", fontSize = 14.sp) },
                                isLeaf = true
                            )
                        )
                    ),
                    TreeSelectNode(
                        value = "node-0-1",
                        title = "Backend",
                        icon = { Text("⚙️", fontSize = 14.sp) },
                        children = listOf(
                            TreeSelectNode(
                                value = "node-0-1-0",
                                title = "Java Developer",
                                icon = { Text("☕", fontSize = 14.sp) },
                                isLeaf = true
                            ),
                            TreeSelectNode(
                                value = "node-0-1-1",
                                title = "Python Developer",
                                icon = { Text("🐍", fontSize = 14.sp) },
                                isLeaf = true
                            ),
                            TreeSelectNode(
                                value = "node-0-1-2",
                                title = "Node.js Developer",
                                icon = { Text("🟢", fontSize = 14.sp) },
                                isLeaf = true
                            )
                        )
                    ),
                    TreeSelectNode(
                        value = "node-0-2",
                        title = "DevOps",
                        icon = { Text("🔧", fontSize = 14.sp) },
                        children = listOf(
                            TreeSelectNode(
                                value = "node-0-2-0",
                                title = "Docker Specialist",
                                icon = { Text("🐳", fontSize = 14.sp) },
                                isLeaf = true
                            ),
                            TreeSelectNode(
                                value = "node-0-2-1",
                                title = "Kubernetes Engineer",
                                icon = { Text("⎈", fontSize = 14.sp) },
                                isLeaf = true
                            )
                        )
                    )
                )
            ),
            TreeSelectNode(
                value = "node-1",
                title = "Design",
                icon = { Text("🎨", fontSize = 14.sp) },
                children = listOf(
                    TreeSelectNode(
                        value = "node-1-0",
                        title = "UI Designer",
                        icon = { Text("🖌️", fontSize = 14.sp) },
                        isLeaf = true
                    ),
                    TreeSelectNode(
                        value = "node-1-1",
                        title = "UX Designer",
                        icon = { Text("📐", fontSize = 14.sp) },
                        isLeaf = true
                    ),
                    TreeSelectNode(
                        value = "node-1-2",
                        title = "Graphic Designer",
                        icon = { Text("🎭", fontSize = 14.sp) },
                        isLeaf = true
                    )
                )
            ),
            TreeSelectNode(
                value = "node-2",
                title = "Marketing",
                icon = { Text("📢", fontSize = 14.sp) },
                children = listOf(
                    TreeSelectNode(
                        value = "node-2-0",
                        title = "Content Marketing",
                        icon = { Text("✍️", fontSize = 14.sp) },
                        isLeaf = true
                    ),
                    TreeSelectNode(
                        value = "node-2-1",
                        title = "SEO Specialist",
                        icon = { Text("🔍", fontSize = 14.sp) },
                        isLeaf = true
                    ),
                    TreeSelectNode(
                        value = "node-2-2",
                        title = "Social Media Manager",
                        icon = { Text("📱", fontSize = 14.sp) },
                        isLeaf = true
                    )
                )
            ),
            TreeSelectNode(
                value = "node-3",
                title = "Sales (Disabled)",
                icon = { Text("💼", fontSize = 14.sp) },
                disabled = true,
                children = listOf(
                    TreeSelectNode(
                        value = "node-3-0",
                        title = "Account Manager",
                        icon = { Text("👔", fontSize = 14.sp) },
                        isLeaf = true
                    )
                )
            ),
            TreeSelectNode(
                value = "node-4",
                title = "HR",
                icon = { Text("👥", fontSize = 14.sp) },
                children = listOf(
                    TreeSelectNode(
                        value = "node-4-0",
                        title = "Recruiter",
                        icon = { Text("🎯", fontSize = 14.sp) },
                        isLeaf = true
                    ),
                    TreeSelectNode(
                        value = "node-4-1",
                        title = "HR Manager",
                        icon = { Text("📋", fontSize = 14.sp) },
                        isLeaf = true
                    )
                )
            )
        )
    }

    var value by remember { mutableStateOf<Any?>(null) }
    var expandedKeys by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("TreeSelect - Complete with ALL Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Current selection:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(
                when (value) {
                    null -> "None"
                    is List<*> -> (value as List<*>).joinToString(", ")
                    is TreeSelectLabelValue -> {
                        val lv = value as TreeSelectLabelValue
                        "${lv.label} (${lv.value})"
                    }

                    else -> value.toString()
                },
                fontSize = 12.sp,
                color = Color(0xFF1890FF),
                modifier = Modifier.padding(start = 8.dp)
            )

            if (expandedKeys.isNotEmpty()) {
                Text("Expanded: ${expandedKeys.joinToString(", ")}", fontSize = 10.sp, color = Color.Gray)
            }
        }

        // Main TreeSelect with ALL parameters
        AntTreeSelect(
            // Required parameters
            value = value,
            onValueChange = { newValue ->
                value = newValue
                println("Value changed: $newValue")
            },
            treeData = treeData,

            // Basic parameters
            modifier = Modifier.fillMaxWidth(),
            defaultValue = null,
            onChange = { changedValue, label, extra ->
                println("onChange - Value: $changedValue, Label: $label")
                println("  Trigger: ${extra.triggerValue}, All checked: ${extra.allCheckedNodes.size}")
            },

            // UI State parameters
            allowClear = allowClear,
            autoFocus = autoFocus,
            bordered = bordered,
            disabled = disabled,

            // Display parameters
            placeholder = placeholder,
            size = when (size) {
                "small" -> InputSize.Small
                "large" -> InputSize.Large
                else -> InputSize.Middle
            },
            status = when (status) {
                "error" -> InputStatus.Error
                "warning" -> InputStatus.Warning
                else -> InputStatus.Default
            },
            variant = null, // Uses bordered parameter

            // Selection parameters
            multiple = multiple,
            treeCheckable = treeCheckable,
            treeCheckStrictly = treeCheckStrictly,
            showCheckedStrategy = when (showCheckedStrategy) {
                "ShowAll" -> TreeSelectCheckedStrategy.ShowAll
                "ShowParent" -> TreeSelectCheckedStrategy.ShowParent
                else -> TreeSelectCheckedStrategy.ShowChild
            },
            labelInValue = false,

            // Tree display parameters
            treeDefaultExpandAll = treeDefaultExpandAll,
            treeDefaultExpandedKeys = null,
            treeExpandedKeys = if (expandedKeys.isEmpty()) null else expandedKeys,
            treeLine = treeLine,
            treeIcon = showTreeIcon || treeIcon,

            // Tree behavior parameters
            treeExpandAction = when (treeExpandAction) {
                "DoubleClick" -> TreeSelectExpandAction.DoubleClick
                "False" -> TreeSelectExpandAction.False
                else -> TreeSelectExpandAction.Click
            },
            treeNodeFilterProp = "title",
            treeNodeLabelProp = "title",

            // Search parameters
            showSearch = showSearch,
            filterTreeNode = { inputValue, treeNode ->
                treeNode.title.contains(inputValue, ignoreCase = true) ||
                        treeNode.value.contains(inputValue, ignoreCase = true)
            },

            // Multiple mode parameters
            maxTagCount = maxTagCount,
            maxTagPlaceholder = { omittedCount ->
                Text(
                    "+ $omittedCount more",
                    fontSize = 12.sp,
                    color = Color(0xFF1890FF),
                    fontWeight = FontWeight.Bold
                )
            },

            // Empty content
            notFoundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🔍", fontSize = 32.sp)
                        Text("No matching data", fontSize = 14.sp, color = Color.Gray)
                        Text("Try different keywords", fontSize = 10.sp, color = Color.LightGray)
                    }
                }
            },

            // Custom icons
            suffixIcon = {
                Text(
                    text = "▼",
                    fontSize = 12.sp,
                    color = if (disabled) Color.Gray else Color(0xFF1890FF)
                )
            },
            switcherIcon = { expanded ->
                Text(
                    text = if (expanded) "⊟" else "⊞",
                    fontSize = 14.sp,
                    color = Color(0xFF1890FF),
                    fontWeight = FontWeight.Bold
                )
            },

            // Popup parameters
            placement = when (placement) {
                "BottomRight" -> TreeSelectPlacement.BottomRight
                "TopLeft" -> TreeSelectPlacement.TopLeft
                "TopRight" -> TreeSelectPlacement.TopRight
                else -> TreeSelectPlacement.BottomLeft
            },
            open = null,
            defaultOpen = defaultOpen,
            popupMatchSelectWidth = popupMatchSelectWidth,

            // Dropdown customization
            dropdownStyle = Modifier,

            // Virtual scrolling
            virtual = virtual,
            listHeight = listHeight.dp,

            // Semantic styling (v5.4.0+)
            classNames = TreeSelectClassNames(
                root = "treeselect-root",
                selector = "treeselect-selector",
                dropdown = "treeselect-dropdown",
                tree = "treeselect-tree"
            ),
            styles = TreeSelectStyles(
                root = Modifier,
                selector = Modifier,
                dropdown = Modifier,
                tree = Modifier
            ),

            // Event callbacks
            onDropdownVisibleChange = { visible ->
                println("onDropdownVisibleChange - Visible: $visible")
            },
            onTreeExpand = { keys ->
                expandedKeys = keys
                println("onTreeExpand - Keys: $keys")
            },
            onSearch = { searchText ->
                searchValue = searchText
                println("onSearch - Query: $searchText")
            },

            // Lazy loading
            loadData = null // Could be used for dynamic loading
        )

        // Demo variations
        Text(
            "Other Examples:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Multiple selection with checkboxes
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Multiple + Checkable:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                var multiValue by remember { mutableStateOf<Any?>(emptyList<String>()) }
                AntTreeSelect(
                    value = multiValue,
                    onValueChange = { multiValue = it },
                    treeData = treeData,
                    placeholder = "Select roles",
                    multiple = true,
                    treeCheckable = true,
                    size = InputSize.Small,
                    maxTagCount = 2,
                    showCheckedStrategy = TreeSelectCheckedStrategy.ShowChild
                )
            }

            // Disabled state
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Disabled State:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntTreeSelect(
                    value = "node-0-0-0",
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "Disabled",
                    disabled = true,
                    size = InputSize.Small
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Error status
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntTreeSelect(
                    value = null,
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "Selection required",
                    status = InputStatus.Error,
                    size = InputSize.Small
                )
            }

            // With tree lines
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("With Tree Lines:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                AntTreeSelect(
                    value = null,
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "With lines",
                    treeLine = true,
                    treeDefaultExpandAll = true,
                    size = InputSize.Small
                )
            }
        }

        // Size variations
        Text("Size Variations:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Small:", fontSize = 10.sp, color = Color.Gray)
                AntTreeSelect(
                    value = null,
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "Small",
                    size = InputSize.Small
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Middle:", fontSize = 10.sp, color = Color.Gray)
                AntTreeSelect(
                    value = null,
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "Middle",
                    size = InputSize.Middle
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Large:", fontSize = 10.sp, color = Color.Gray)
                AntTreeSelect(
                    value = null,
                    onValueChange = {},
                    treeData = treeData,
                    placeholder = "Large",
                    size = InputSize.Large
                )
            }
        }
    }
}
