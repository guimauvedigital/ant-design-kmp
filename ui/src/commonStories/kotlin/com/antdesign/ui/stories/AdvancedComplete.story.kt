package com.antdesign.ui.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antdesign.ui.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.storytale.story

// =============================================================================
// FORM COMPONENT - ALL PARAMETERS
// =============================================================================

val FormComplete by story {
    // Form layout parameters
    val layout by parameter(FormLayout.Vertical)
    val labelAlign by parameter(FormLabelAlign.Right)
    val labelCol by parameter<Int?>(null)
    val wrapperCol by parameter<Int?>(null)
    val colon by parameter(true)
    val disabled by parameter(false)
    val labelWrap by parameter(false)

    // Form behavior parameters
    val preserve by parameter(true)
    val validateTrigger by parameter(ValidateTrigger.OnChange)
    val scrollToFirstError by parameter(false)
    val clearOnDestroy by parameter(false)

    // Form appearance parameters
    val requiredMark by parameter(RequiredMark.Default)
    val size by parameter(ComponentSize.Middle)
    val variant by parameter<FormVariant?>(null)

    // Create form instance
    val form = rememberFormInstance()
    var submitResult by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Form Complete - All Parameters", fontSize = 20.sp)

        AntForm(
            form = form,
            name = "completeForm",
            layout = layout,
            labelAlign = labelAlign,
            labelCol = labelCol,
            labelWrap = labelWrap,
            wrapperCol = wrapperCol,
            colon = colon,
            requiredMark = requiredMark,
            initialValues = mapOf("username" to "", "email" to "", "age" to 0),
            preserve = preserve,
            validateTrigger = validateTrigger,
            scrollToFirstError = if (scrollToFirstError) ScrollOptions() else null,
            size = size,
            disabled = disabled,
            variant = variant,
            clearOnDestroy = clearOnDestroy,
            onValuesChange = { changed, all ->
                println("Values changed: $changed, All: $all")
            },
            onFinish = { values ->
                submitResult = "Success: $values"
            },
            onFinishFailed = { errors, values, outOfDate ->
                submitResult = "Failed: ${errors.map { it.name }}"
            }
        ) {
            // Username field
            AntFormItem(
                name = "username",
                label = "Username",
                required = true,
                rules = listOf(
                    FormRule(
                        required = true,
                        message = "Please enter username"
                    ),
                    FormRule(
                        minLength = 3,
                        message = "Username must be at least 3 characters"
                    )
                ),
                hasFeedback = true,
                tooltip = "Enter your username",
                validateDebounce = 300L
            ) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    placeholder = "Enter username"
                )
            }

            // Email field
            AntFormItem(
                name = "email",
                label = "Email",
                required = true,
                rules = listOf(
                    FormRule(
                        required = true,
                        message = "Please enter email"
                    ),
                    FormRule(
                        type = ValidationType.Email,
                        message = "Please enter a valid email"
                    )
                ),
                hasFeedback = true
            ) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    placeholder = "Enter email"
                )
            }

            // Age field
            AntFormItem(
                name = "age",
                label = "Age",
                rules = listOf(
                    FormRule(
                        type = ValidationType.Integer,
                        message = "Please enter a valid age"
                    ),
                    FormRule(
                        min = 18,
                        max = 120,
                        message = "Age must be between 18 and 120"
                    )
                )
            ) { value, onChange, status ->
                AntInputNumber(
                    value = (value as? Int) ?: 0,
                    onValueChange = { onChange(it) },
                    placeholder = "Enter age",
                    min = 0,
                    max = 150
                )
            }

            // Submit button
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AntButton(
                    onClick = {
                        coroutineScope.launch {
                            form.submit()
                        }
                    },
                    type = ButtonType.Primary
                ) {
                    Text("Submit")
                }

                AntButton(
                    onClick = {
                        form.resetFields()
                        submitResult = null
                    }
                ) {
                    Text("Reset")
                }
            }
        }

        if (submitResult != null) {
            Text(
                text = submitResult!!,
                color = if (submitResult!!.startsWith("Success")) Color(0xFF52C41A) else Color(0xFFFF4D4F)
            )
        }
    }
}

// =============================================================================
// TRANSFER COMPONENT - ALL PARAMETERS
// =============================================================================

val TransferComplete by story {
    // Transfer appearance parameters
    val disabled by parameter(false)
    val showSearch by parameter(true)
    val showSelectAll by parameter(true)
    val oneWay by parameter(false)
    val bordered by parameter(true)

    // Transfer behavior parameters
    val status by parameter(TransferStatus.Default)

    // Create transfer data
    val dataSource = remember {
        (1..20).map { i ->
            TransferItem(
                key = "item-$i",
                title = "Item $i",
                description = "Description for item $i",
                disabled = i % 7 == 0
            )
        }
    }

    var targetKeys by remember { mutableStateOf(listOf("item-2", "item-4")) }
    var selectedKeys by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Transfer Complete - All Parameters", fontSize = 20.sp)

        AntTransfer(
            dataSource = dataSource,
            targetKeys = targetKeys,
            onChange = { newTargetKeys, direction, moveKeys ->
                targetKeys = newTargetKeys
                println("Transfer: $direction - Moved: $moveKeys")
            },
            selectedKeys = selectedKeys,
            onSelectChange = { sourceKeys, targetKeys ->
                selectedKeys = sourceKeys + targetKeys
            },
            disabled = disabled,
            titles = listOf("Source List", "Target List"),
            operations = listOf("To Right →", "← To Left"),
            showSearch = showSearch,
            filterOption = { input, item ->
                item.title.contains(input, ignoreCase = true) ||
                item.description?.contains(input, ignoreCase = true) == true
            },
            onSearch = { direction, value ->
                println("Search in $direction: $value")
            },
            searchPlaceholder = "Search items",
            notFoundContent = {
                AntEmpty(description = "No items found")
            },
            render = { item -> "${item.title} (${item.key})" },
            oneWay = oneWay,
            showSelectAll = showSelectAll,
            selectAllLabels = listOf(
                { info -> Text("${info.selectedCount}/${info.totalCount} selected") },
                { info -> Text("${info.selectedCount}/${info.totalCount} items") }
            ),
            pagination = PaginationConfig(pageSize = 10, simple = false),
            status = status,
            classNames = TransferClassNames(
                list = "custom-list",
                listHeader = "custom-header"
            ),
            locale = TransferLocale(
                itemUnit = "item",
                itemsUnit = "items",
                searchPlaceholder = "Search here",
                notFoundContent = "No data"
            )
        )
    }
}

// =============================================================================
// AUTOCOMPLETE COMPONENT - ALL PARAMETERS
// =============================================================================

val AutoCompleteComplete by story {
    // AutoComplete appearance parameters
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val autoFocus by parameter(false)
    val backfill by parameter(false)
    val size by parameter(InputSize.Middle)
    val status by parameter(InputStatus.Default)
    val variant by parameter(InputVariant.Outlined)

    // AutoComplete behavior parameters
    val defaultActiveFirstOption by parameter(true)
    val defaultOpen by parameter(false)
    val popupMatchSelectWidth by parameter(true)

    var value by remember { mutableStateOf("") }

    val options = remember {
        listOf(
            "apple", "banana", "cherry", "date", "elderberry",
            "fig", "grape", "honeydew", "kiwi", "lemon"
        ).map { fruit -> AutoCompleteOption<String>(value = fruit, labelText = fruit.uppercase()) }
    }

    val groupedOptions = remember {
        listOf(
            AutoCompleteGroupOption<String>(
                label = "Fruits",
                options = listOf(
                    AutoCompleteOption<String>(value = "apple", labelText = "Apple"),
                    AutoCompleteOption<String>(value = "banana", labelText = "Banana"),
                    AutoCompleteOption<String>(value = "cherry", labelText = "Cherry")
                )
            ),
            AutoCompleteGroupOption<String>(
                label = "Vegetables",
                options = listOf(
                    AutoCompleteOption<String>(value = "carrot", labelText = "Carrot"),
                    AutoCompleteOption<String>(value = "broccoli", labelText = "Broccoli")
                )
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("AutoComplete Complete - All Parameters", fontSize = 20.sp)

        Text("Flat Options:")
        AntAutoComplete<String>(
            value = value,
            onChange = { value = it },
            defaultValue = "",
            options = options,
            allowClear = allowClear,
            autoFocus = autoFocus,
            backfill = backfill,
            defaultActiveFirstOption = defaultActiveFirstOption,
            defaultOpen = defaultOpen,
            disabled = disabled,
            popupMatchSelectWidth = popupMatchSelectWidth,
            placeholder = "Type to search...",
            status = status,
            size = size,
            variant = variant,
            filterOption = { input, option ->
                option.value.contains(input, ignoreCase = true)
            },
            onBlur = { println("AutoComplete blurred") },
            onFocus = { println("AutoComplete focused") },
            onSearch = { query -> println("Searching: $query") },
            onSelect = { data, option ->
                println("Selected: ${option.value}")
            },
            onClear = { println("Cleared") },
            onDropdownVisibleChange = { visible ->
                println("Dropdown visible: $visible")
            },
            notFoundContent = {
                Text("No results found", color = Color.Gray)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Grouped Options:")
        AntAutoComplete<String>(
            value = value,
            onChange = { value = it },
            groupedOptions = groupedOptions,
            placeholder = "Type to search grouped...",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =============================================================================
// MENTIONS COMPONENT - ALL PARAMETERS
// =============================================================================

val MentionsComplete by story {
    // Mentions appearance parameters
    val disabled by parameter(false)
    val autoFocus by parameter(false)
    val status by parameter(InputStatus.Default)
    val prefix by parameter("@")
    val split by parameter(" ")

    var text by remember { mutableStateOf("Hello @alice, how are you?") }

    val options = remember {
        listOf(
            MentionOption(key = "1", value = "alice", label = "Alice"),
            MentionOption(key = "2", value = "bob", label = "Bob"),
            MentionOption(key = "3", value = "charlie", label = "Charlie"),
            MentionOption(key = "4", value = "david", label = "David", disabled = true)
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mentions Complete - All Parameters", fontSize = 20.sp)

        AntMentions(
            value = text,
            onChange = { text = it },
            defaultValue = "",
            options = options,
            autoFocus = autoFocus,
            autoSize = AutoSizeConfig(minRows = 3, maxRows = 6),
            disabled = disabled,
            filterOption = { query, option ->
                option.value.contains(query, ignoreCase = true) ||
                option.label?.contains(query, ignoreCase = true) == true
            },
            notFoundContent = {
                Text("No users found", color = Color.Gray)
            },
            placement = PopoverPlacement.BottomLeft,
            prefix = prefix,
            split = split,
            status = status,
            validateSearch = { query, mentions ->
                query.length >= 1
            },
            onBlur = { println("Mentions blurred") },
            onFocus = { println("Mentions focused") },
            onResize = { width, height ->
                println("Mentions resized: $width x $height")
            },
            onSearch = { query, prefix ->
                println("Searching: $query with prefix $prefix")
            },
            onSelect = { option, prefix ->
                println("Selected: ${option.value} with prefix $prefix")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Current text: $text", fontSize = 12.sp, color = Color.Gray)
    }
}

// =============================================================================
// COLORPICKER COMPONENT - ALL PARAMETERS
// =============================================================================

val ColorPickerComplete by story {
    // ColorPicker appearance parameters
    val disabled by parameter(false)
    val showText by parameter(true)
    val allowClear by parameter(true)
    val size by parameter(ButtonSize.Middle)
    val format by parameter(ColorFormat.Hex)
    val mode by parameter(ModeType.Single)

    // ColorPicker behavior parameters
    val disabledAlpha by parameter(false)
    val disabledFormat by parameter(false)
    val trigger by parameter(TriggerType.Click)
    val arrow by parameter(ArrowConfig.Default)
    val placement by parameter(PopoverPlacement.BottomLeft)

    var color by remember { mutableStateOf(Color(0xFF1890FF)) }
    var isOpen by remember { mutableStateOf(false) }

    val presets = remember {
        listOf(
            PresetsItem(
                label = "Recommended",
                colors = listOf(
                    Color(0xFFFF4D4F), Color(0xFFFAAD14), Color(0xFF52C41A),
                    Color(0xFF1890FF), Color(0xFF722ED1), Color(0xFFEB2F96)
                ),
                defaultOpen = true
            ),
            PresetsItem(
                label = "Grays",
                colors = listOf(
                    Color(0xFF000000), Color(0xFF434343), Color(0xFF8C8C8C),
                    Color(0xFFBFBFBF), Color(0xFFE8E8E8), Color(0xFFFFFFFF)
                ),
                defaultOpen = false
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ColorPicker Complete - All Parameters", fontSize = 20.sp)

        AntColorPicker(
            value = color,
            onValueChange = { color = it },
            defaultValue = Color.Black,
            mode = mode,
            disabled = disabled,
            format = format,
            defaultFormat = ColorFormat.Hex,
            allowClear = allowClear,
            presets = presets,
            open = isOpen,
            onOpenChange = { isOpen = it },
            trigger = trigger,
            placement = placement,
            arrow = arrow,
            showText = showText,
            size = size,
            disabledAlpha = disabledAlpha,
            disabledFormat = disabledFormat,
            onFormatChange = { newFormat ->
                println("Format changed to: $newFormat")
            },
            onChange = { newColor, cssString ->
                println("Color changed: $cssString")
            },
            onClear = {
                println("Color cleared")
                color = Color.Transparent
            },
            onChangeComplete = { finalColor ->
                println("Color selection complete: $finalColor")
            },
            autoAdjustOverflow = true,
            destroyTooltipOnHide = false
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Selected color:")
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color)
            )
        }
    }
}

// =============================================================================
// QRCODE COMPONENT - ALL PARAMETERS
// =============================================================================

val QRCodeComplete by story {
    // QRCode appearance parameters
    val type by parameter(QRCodeType.Canvas)
    val size by parameter(160.dp)
    val bordered by parameter(true)
    val color by parameter(Color.Black)
    val bgColor by parameter(Color.White)

    // QRCode behavior parameters
    val errorLevel by parameter(QRCodeErrorLevel.M)
    val boostLevel by parameter(true)
    val status by parameter(QRCodeStatus.Active)

    var qrValue by remember { mutableStateOf("https://ant.design") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("QRCode Complete - All Parameters", fontSize = 20.sp)

        AntInput(
            value = qrValue,
            onValueChange = { qrValue = it },
            placeholder = "Enter text or URL",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active QRCode
            AntQRCode(
                value = qrValue,
                type = type,
                size = size,
                iconSize = IconSize.Simple(40.dp),
                color = color,
                bgColor = bgColor,
                bordered = bordered,
                errorLevel = errorLevel,
                boostLevel = boostLevel,
                status = status,
                onRefresh = {
                    println("QRCode refresh requested")
                },
                prefixCls = "ant-qrcode"
            )

            // Loading QRCode
            AntQRCode(
                value = qrValue,
                size = size,
                status = QRCodeStatus.Loading
            )

            // Expired QRCode
            AntQRCode(
                value = qrValue,
                size = size,
                status = QRCodeStatus.Expired,
                onRefresh = {
                    println("Refresh clicked")
                }
            )

            // Scanned QRCode
            AntQRCode(
                value = qrValue,
                size = size,
                status = QRCodeStatus.Scanned
            )
        }

        Text("List QRCode (v5.28.0+):", fontSize = 14.sp)
        AntQRCode(
            value = listOf("Line 1", "Line 2", "Line 3"),
            size = size,
            bordered = bordered
        )
    }
}

// =============================================================================
// WATERMARK COMPONENT - ALL PARAMETERS
// =============================================================================

val WatermarkComplete by story {
    // Watermark appearance parameters
    val rotate by parameter(-22f)
    val width by parameter(120.dp)
    val height by parameter(64.dp)
    val zIndex by parameter(9)

    var contentText by remember { mutableStateOf("Ant Design KMP") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Watermark Complete - All Parameters", fontSize = 20.sp)

        AntInput(
            value = contentText,
            onValueChange = { contentText = it },
            placeholder = "Enter watermark text",
            modifier = Modifier.fillMaxWidth()
        )

        // Single line watermark
        AntWatermark(
            content = contentText,
            width = width,
            height = height,
            rotate = rotate,
            zIndex = zIndex,
            gap = Pair(100.dp, 100.dp),
            offset = Pair(10.dp, 10.dp),
            font = WatermarkFont(
                color = Color(0x40000000),
                fontSize = 16,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
                fontFamily = "sans-serif",
                fontStyle = androidx.compose.ui.text.font.FontStyle.Normal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F2F5))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Protected Content", fontSize = 18.sp)
                    Text("This content is protected by a watermark")
                    Text("The watermark is non-blocking and allows interaction")
                }
            }
        }

        // Multiline watermark
        AntWatermark(
            content = listOf("Ant Design", "KMP", "Watermark"),
            width = 150.dp,
            height = 100.dp,
            rotate = 0f,
            gap = Pair(80.dp, 80.dp),
            font = WatermarkFont(
                color = Color(0x30FF0000),
                fontSize = 14
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text("Multiline Watermark Example")
            }
        }
    }
}

// =============================================================================
// TOUR COMPONENT - ALL PARAMETERS
// =============================================================================

val TourComplete by story {
    // Tour behavior parameters
    val arrow by parameter(true)
    val type by parameter(TourType.Default)
    val zIndex by parameter(1001)

    var isOpen by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(0) }

    var step1Coords by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }
    var step2Coords by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }
    var step3Coords by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }

    val steps = remember {
        listOf(
            TourStep(
                target = { step1Coords },
                title = "Welcome",
                description = "Welcome to Ant Design KMP Tour!",
                placement = PopoverPlacement.Bottom,
                type = TourType.Primary,
                arrow = true
            ),
            TourStep(
                target = { step2Coords },
                title = "Feature 1",
                description = "This is the first amazing feature",
                placement = PopoverPlacement.Right,
                nextButtonProps = TourButtonProps(
                    children = "Next Step",
                    type = ButtonType.Primary
                )
            ),
            TourStep(
                target = { step3Coords },
                title = "Feature 2",
                description = "This is the second amazing feature",
                placement = PopoverPlacement.Left,
                mask = MaskConfig(color = Color.Black.copy(alpha = 0.6f))
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tour Complete - All Parameters", fontSize = 20.sp)

        AntButton(
            onClick = { isOpen = true },
            type = ButtonType.Primary
        ) {
            Text("Start Tour")
        }

        // Tour target elements
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AntButton(
                onClick = {},
                modifier = Modifier.onGloballyPositioned { step1Coords = it }
            ) {
                Text("Step 1 Target")
            }

            AntButton(
                onClick = {},
                modifier = Modifier.onGloballyPositioned { step2Coords = it }
            ) {
                Text("Step 2 Target")
            }

            AntButton(
                onClick = {},
                modifier = Modifier.onGloballyPositioned { step3Coords = it }
            ) {
                Text("Step 3 Target")
            }
        }

        AntTour(
            steps = steps,
            open = isOpen,
            onChange = { step ->
                currentStep = step
                println("Tour step changed to: $step")
            },
            onClose = {
                isOpen = false
                println("Tour closed")
            },
            onFinish = {
                isOpen = false
                println("Tour finished!")
            },
            current = currentStep,
            animated = AnimationConfig(
                enable = true,
                duration = 300,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            ),
            arrow = arrow,
            closeIcon = {
                Text("✕", fontSize = 16.sp)
            },
            gap = TourGap(offset = 6, radius = 2),
            indicatorsRender = { current, total ->
                Text("$current / $total", fontSize = 12.sp)
            },
            mask = true,
            placement = PopoverPlacement.Bottom,
            scrollIntoViewOptions = ScrollIntoViewOptions(
                behavior = "smooth",
                block = "center",
                inline = "nearest"
            ),
            type = type,
            zIndex = zIndex
        )
    }
}

// =============================================================================
// FLOATBUTTON COMPONENT - ALL PARAMETERS
// =============================================================================

val FloatButtonComplete by story {
    // FloatButton appearance parameters
    val type by parameter(FloatButtonType.Default)
    val shape by parameter(FloatButtonShape.Circle)
    val description by parameter<String?>(null)
    val tooltip by parameter<String?>("Float Button Tooltip")

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("FloatButton Complete - All Parameters", fontSize = 20.sp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic FloatButton
            AntFloatButton(
                onClick = { println("Float button clicked") },
                icon = {
                    Text("↑", fontSize = 20.sp)
                },
                description = description,
                tooltip = tooltip,
                type = type,
                shape = shape,
                badge = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            )

            // BackTop FloatButton
            AntFloatButtonBackTop(
                visibilityHeight = 400.dp,
                onClick = { println("Back to top clicked") },
                target = null,
                duration = 450,
                icon = {
                    Text("⬆", fontSize = 24.sp)
                },
                description = "Back",
                tooltip = "Back to top",
                type = type
            )
        }

        // FloatButton Group
        AntFloatButtonGroup(
            trigger = FloatButtonTrigger.Click,
            icon = {
                Text("➕", fontSize = 20.sp)
            },
            closeIcon = {
                Text("✕", fontSize = 20.sp)
            },
            description = "Actions",
            tooltip = "Action Menu",
            type = type,
            shape = shape,
            children = listOf(
                {
                    AntFloatButton(
                        onClick = { println("Action 1") },
                        icon = { Text("📝", fontSize = 16.sp) },
                        tooltip = "Edit",
                        type = FloatButtonType.Primary,
                        shape = FloatButtonShape.Circle
                    )
                },
                {
                    AntFloatButton(
                        onClick = { println("Action 2") },
                        icon = { Text("🗑", fontSize = 16.sp) },
                        tooltip = "Delete",
                        type = FloatButtonType.Default,
                        shape = FloatButtonShape.Circle
                    )
                },
                {
                    AntFloatButton(
                        onClick = { println("Action 3") },
                        icon = { Text("📤", fontSize = 16.sp) },
                        tooltip = "Share",
                        type = FloatButtonType.Default,
                        shape = FloatButtonShape.Circle
                    )
                }
            )
        )
    }
}

// =============================================================================
// SEGMENTED COMPONENT - ALL PARAMETERS
// =============================================================================

val SegmentedComplete by story {
    // Segmented appearance parameters
    val disabled by parameter(false)
    val size by parameter(ComponentSize.Middle)

    var value by remember { mutableStateOf("Daily") }

    val options = remember { listOf("Daily", "Weekly", "Monthly", "Yearly") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Segmented Complete - All Parameters", fontSize = 20.sp)

        // String options
        AntSegmentedString(
            options = options,
            value = value,
            onValueChange = { value = it },
            disabled = disabled,
            size = size,
            block = false,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Selected: $value", fontSize = 14.sp, color = Color.Gray)

        // Block segmented
        AntSegmentedString(
            options = listOf("Option 1", "Option 2", "Option 3"),
            value = value,
            onValueChange = { value = it },
            disabled = disabled,
            size = size,
            block = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =============================================================================
// CAROUSEL COMPONENT - ALL PARAMETERS
// =============================================================================

val CarouselComplete by story {
    // Carousel behavior parameters
    val autoplay by parameter(false)
    val autoplaySpeed by parameter(3000L)
    val dots by parameter(true)
    val infinite by parameter(true)
    val dotPosition by parameter(DotPosition.Bottom)
    val effect by parameter(CarouselEffect.ScrollX)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Carousel Complete - All Parameters", fontSize = 20.sp)

        AntCarousel(
            children = listOf(
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF1890FF))
                    ) {
                        Text(
                            "Slide 1",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF52C41A))
                    ) {
                        Text(
                            "Slide 2",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFFAAD14))
                    ) {
                        Text(
                            "Slide 3",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFFF4D4F))
                    ) {
                        Text(
                            "Slide 4",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            ),
            autoplay = autoplay,
            autoplaySpeed = autoplaySpeed.toInt(),
            dots = dots,
            dotPosition = dotPosition,
            effect = effect,
            infinite = infinite,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =============================================================================
// EMPTY COMPONENT - ALL PARAMETERS
// =============================================================================

val EmptyComplete by story {
    // Empty appearance parameters
    val description by parameter("No Data")
    val imageStyle by parameter("Simple")

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Empty Complete - All Parameters", fontSize = 20.sp)

        // Simple empty
        AntEmpty(
            description = description,
            modifier = Modifier.fillMaxWidth()
        )

        // Empty with custom content
        AntEmptyCustom(
            description = "No items found",
            image = @Composable { Text("🔍", fontSize = 48.sp) },
            children = {
                AntButton(
                    onClick = { println("Create new item") },
                    type = ButtonType.Primary
                ) {
                    Text("Create Now")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =============================================================================
// RIBBON COMPONENT - ALL PARAMETERS
// =============================================================================

val RibbonComplete by story {
    // Ribbon appearance parameters
    val placement by parameter(RibbonPlacement.TopStart)
    val text by parameter("Ribbon")
    val color by parameter<Color?>(null)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Ribbon Complete - All Parameters", fontSize = 20.sp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ribbon at TopStart
            AntRibbon(
                text = text,
                children = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFF0F2F5))
                            .padding(16.dp)
                    ) {
                        Text("Card content with ribbon")
                    }
                },
                modifier = Modifier.weight(1f),
                color = color,
                placement = placement
            )

            // Ribbon at TopEnd
            AntRibbon(
                text = "Sale",
                children = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFF0F2F5))
                            .padding(16.dp)
                    ) {
                        Text("Special offer")
                    }
                },
                modifier = Modifier.weight(1f),
                color = Color(0xFFFF4D4F),
                placement = RibbonPlacement.TopEnd
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ribbon at BottomStart
            AntRibbon(
                text = "New",
                children = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFF0F2F5))
                            .padding(16.dp)
                    ) {
                        Text("New arrival")
                    }
                },
                modifier = Modifier.weight(1f),
                color = Color(0xFF52C41A),
                placement = RibbonPlacement.BottomStart
            )

            // Ribbon at BottomEnd
            AntRibbon(
                text = "Premium",
                children = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFF0F2F5))
                            .padding(16.dp)
                    ) {
                        Text("Premium content")
                    }
                },
                modifier = Modifier.weight(1f),
                color = Color(0xFFFAAD14),
                placement = RibbonPlacement.BottomEnd
            )
        }
    }
}

// =============================================================================
// WAVE COMPONENT - ALL PARAMETERS
// =============================================================================

val WaveComplete by story {
    // Wave appearance parameters
    val disabled by parameter(false)
    val color by parameter(Color(0xFF1890FF).copy(alpha = 0.2f))
    val duration by parameter(600)
    val borderRadius by parameter(6.dp)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Wave Complete - All Parameters", fontSize = 20.sp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Wave with default settings
            AntWave(
                disabled = disabled,
                color = color,
                duration = duration,
                borderRadius = borderRadius
            ) {
                AntButton(
                    onClick = { println("Wave button clicked") },
                    type = ButtonType.Primary
                ) {
                    Text("Click for Wave")
                }
            }

            // Wave with custom color
            AntWave(
                disabled = false,
                color = Color(0xFFFF4D4F).copy(alpha = 0.3f),
                duration = 800,
                borderRadius = 4.dp,
                component = WaveComponent.Button
            ) {
                AntButton(
                    onClick = { println("Red wave clicked") },
                    danger = true
                ) {
                    Text("Red Wave")
                }
            }

            // Wave with config
            AntWave(
                config = WaveConfig(
                    disabled = false,
                    color = Color(0xFF52C41A).copy(alpha = 0.25f),
                    duration = 700,
                    borderRadius = 8.dp,
                    showEffect = { info ->
                        println("Wave effect at: ${info.position}")
                    }
                )
            ) {
                AntButton(
                    onClick = { println("Green wave clicked") },
                    type = ButtonType.Default
                ) {
                    Text("Green Wave")
                }
            }
        }

        Text("Wave disabled:", fontSize = 14.sp)
        AntWave(
            disabled = true
        ) {
            AntButton(
                onClick = { println("No wave") }
            ) {
                Text("No Wave Effect")
            }
        }
    }
}

// =============================================================================
// FLEX COMPONENT - ALL PARAMETERS
// =============================================================================

val FlexComplete by story {
    // Flex layout parameters
    val vertical by parameter(false)
    val direction by parameter(FlexDirection.Row)
    val wrap by parameter(FlexWrap.NoWrap)
    val justify by parameter(FlexJustify.FlexStart)
    val align by parameter(FlexAlign.FlexStart)
    val gap by parameter(16.dp)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Flex Complete - All Parameters", fontSize = 20.sp)

        AntFlex(
            vertical = vertical,
            direction = direction,
            wrap = wrap,
            justify = justify,
            align = align,
            gap = gap,
            flex = "1",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F2F5))
                .padding(16.dp)
        ) {
            repeat(6) { index ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF1890FF))
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

// =============================================================================
// CENTER, COMPACT, CONTAINER COMPONENTS
// =============================================================================

val LayoutHelpersComplete by story {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout Helpers - All Parameters", fontSize = 20.sp)

        // Center
        Text("Center Component:")
        AntCenter(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFF0F2F5))
        ) {
            Text("Centered Content", fontSize = 16.sp)
        }

        // Compact
        Text("Compact Component:")
        AntCompact(
            block = false,
            size = CompactSize.Middle,
            modifier = Modifier.fillMaxWidth()
        ) {
            AntInput(
                value = "",
                onValueChange = {},
                placeholder = "Input 1"
            )
            AntInput(
                value = "",
                onValueChange = {},
                placeholder = "Input 2"
            )
            AntButton(onClick = {}) {
                Text("Submit")
            }
        }

        // Container
        Text("Container Component:")
        AntContainer(
            maxWidth = 800.dp,
            padding = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF1890FF))
            ) {
                Text(
                    "Container Content",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

// =============================================================================
// CONFIGPROVIDER AND APP COMPONENTS
// =============================================================================

val ConfigProviderComplete by story {
    // ConfigProvider parameters
    val direction by parameter(Direction.LTR)
    val componentSize by parameter(ComponentSize.Middle)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ConfigProvider Complete - All Parameters", fontSize = 20.sp)

        AntConfigProvider(
            direction = direction,
            componentSize = componentSize,
            theme = AntThemeConfig(
                token = ThemeToken(
                    colorPrimary = Color(0xFF1890FF),
                    colorSuccess = Color(0xFF52C41A),
                    colorWarning = Color(0xFFFAAD14),
                    colorError = Color(0xFFFF4D4F),
                    colorInfo = Color(0xFF1890FF),
                    colorTextBase = Color(0xFF000000D9),
                    colorBgBase = Color(0xFFFFFFFF),
                    fontSize = 14,
                    borderRadius = 6.dp,
                    borderRadiusLG = 8.dp,
                    borderRadiusSM = 4.dp,
                    borderRadiusXS = 2.dp
                )
            )
        ) {
            AntApp {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Themed Components:")
                    AntButton(
                        onClick = {},
                        type = ButtonType.Primary
                    ) {
                        Text("Primary Button")
                    }
                    AntButton(
                        onClick = {},
                        type = ButtonType.Default
                    ) {
                        Text("Default Button")
                    }
                    AntInput(
                        value = "",
                        onValueChange = {},
                        placeholder = "Themed Input"
                    )
                }
            }
        }
    }
}
