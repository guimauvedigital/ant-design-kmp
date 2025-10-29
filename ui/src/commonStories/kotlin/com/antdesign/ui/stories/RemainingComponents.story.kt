package com.antdesign.ui.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.antdesign.ui.*
import org.jetbrains.compose.storytale.story

val AutoComplete by story {
    var value by parameter("")
    val placeholder by parameter("Enter text...")
    val disabled by parameter(false)
    val allowClear by parameter(false)
    val backfill by parameter(false)
    val defaultOpen by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default AutoComplete:")
        AntAutoComplete(
            value = value,
            onChange = { value = it },
            options = listOf(
                AutoCompleteOption<String>(value = "Option 1"),
                AutoCompleteOption<String>(value = "Option 2"),
                AutoCompleteOption<String>(value = "Option 3"),
                AutoCompleteOption<String>(value = "Option 4")
            ),
            placeholder = placeholder,
            disabled = disabled,
            allowClear = allowClear,
            backfill = backfill,
            defaultOpen = defaultOpen,
            filterOption = null,
            notFoundContent = null,
            onSearch = null,
            onSelect = null,
            onClear = null,
            size = InputSize.Middle,
            status = InputStatus.Default
        )

        Text("Status variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default")
                AntAutoComplete(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        AutoCompleteOption<String>(value = "Apple"),
                        AutoCompleteOption<String>(value = "Banana"),
                        AutoCompleteOption<String>(value = "Orange")
                    ),
                    placeholder = "Default status",
                    status = InputStatus.Default,
                    size = InputSize.Middle
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error")
                AntAutoComplete(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        AutoCompleteOption<String>(value = "Apple"),
                        AutoCompleteOption<String>(value = "Banana"),
                        AutoCompleteOption<String>(value = "Orange")
                    ),
                    placeholder = "Error status",
                    status = InputStatus.Error,
                    size = InputSize.Middle
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Warning")
                AntAutoComplete(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        AutoCompleteOption<String>(value = "Apple"),
                        AutoCompleteOption<String>(value = "Banana"),
                        AutoCompleteOption<String>(value = "Orange")
                    ),
                    placeholder = "Warning status",
                    status = InputStatus.Warning,
                    size = InputSize.Middle
                )
            }
        }

        Text("With filterOption (case-insensitive filtering):")
        AntAutoComplete(
            value = value,
            onChange = { value = it },
            options = listOf(
                AutoCompleteOption<String>(value = "Apple"),
                AutoCompleteOption<String>(value = "Apricot"),
                AutoCompleteOption<String>(value = "Banana"),
                AutoCompleteOption<String>(value = "Cherry"),
                AutoCompleteOption<String>(value = "Orange")
            ),
            placeholder = "Type to filter...",
            filterOption = { inputValue, option ->
                option.value.contains(inputValue, ignoreCase = true)
            },
            size = InputSize.Middle
        )

        Text("With backfill enabled:")
        AntAutoComplete(
            value = value,
            onChange = { value = it },
            options = listOf(
                AutoCompleteOption<String>(value = "Gmail"),
                AutoCompleteOption<String>(value = "Yahoo"),
                AutoCompleteOption<String>(value = "Outlook")
            ),
            placeholder = "Email provider...",
            backfill = true,
            allowClear = true,
            size = InputSize.Middle
        )
    }
}

val ColorPicker by story {
    var color by remember { mutableStateOf(Color.Blue) }
    val disabled by parameter(false)
    val showText by parameter(false)
    val allowClear by parameter(false)

    // Create example presets
    val presets = listOf(
        PresetsItem(
            label = "Recommended",
            colors = listOf(
                Color(0xFFFF4D4F),
                Color(0xFFFF7A45),
                Color(0xFFFFA940),
                Color(0xFFFFD666),
                Color(0xFFFFF566)
            )
        ),
        PresetsItem(
            label = "Recent",
            colors = listOf(
                Color(0xFF52C41A),
                Color(0xFF13C2C2),
                Color(0xFF1890FF),
                Color(0xFF2F54EB),
                Color(0xFF722ED1)
            )
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = showText,
                    allowClear = allowClear,
                    size = ButtonSize.Large,
                    format = ColorFormat.Hex
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = showText,
                    allowClear = allowClear,
                    size = ButtonSize.Middle,
                    format = ColorFormat.Hex
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = showText,
                    allowClear = allowClear,
                    size = ButtonSize.Small,
                    format = ColorFormat.Hex
                )
            }
        }

        Text("Format variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hex")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = true,
                    allowClear = allowClear,
                    format = ColorFormat.Hex,
                    placement = PopoverPlacement.BottomLeft
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("RGB")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = true,
                    allowClear = allowClear,
                    format = ColorFormat.RGB,
                    placement = PopoverPlacement.BottomLeft
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("HSB")
                AntColorPicker(
                    value = color,
                    onValueChange = { color = it },
                    disabled = disabled,
                    showText = true,
                    allowClear = allowClear,
                    format = ColorFormat.HSB,
                    placement = PopoverPlacement.BottomLeft
                )
            }
        }

        Text("With presets:")
        AntColorPicker(
            value = color,
            onValueChange = { color = it },
            disabled = disabled,
            showText = showText,
            allowClear = allowClear,
            format = ColorFormat.Hex,
            presets = presets,
            placement = PopoverPlacement.BottomLeft
        )
    }
}

val DatePicker by story {
    var value by remember { mutableStateOf<String?>(null) }
    val placeholder by parameter("Select date")
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val showToday by parameter(true)
    val showNow by parameter(false)
    val format by parameter("YYYY-MM-DD")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showToday = showToday,
                    showNow = showNow,
                    format = format,
                    size = InputSize.Large,
                    picker = DatePickerMode.Date,
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showToday = showToday,
                    showNow = showNow,
                    format = format,
                    size = InputSize.Middle,
                    picker = DatePickerMode.Date,
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showToday = showToday,
                    showNow = showNow,
                    format = format,
                    size = InputSize.Small,
                    picker = DatePickerMode.Date,
                    modifier = Modifier.width(200.dp)
                )
            }
        }

        Text("Picker mode variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Date")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = "Select date",
                    disabled = disabled,
                    allowClear = allowClear,
                    showToday = showToday,
                    format = format,
                    picker = DatePickerMode.Date,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Week")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = "Select week",
                    disabled = disabled,
                    allowClear = allowClear,
                    format = format,
                    picker = DatePickerMode.Week,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Month")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = "Select month",
                    disabled = disabled,
                    allowClear = allowClear,
                    format = "YYYY-MM",
                    picker = DatePickerMode.Month,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Quarter")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = "Select quarter",
                    disabled = disabled,
                    allowClear = allowClear,
                    format = "YYYY-[Q]Q",
                    picker = DatePickerMode.Quarter,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Year")
                AntDatePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = "Select year",
                    disabled = disabled,
                    allowClear = allowClear,
                    format = "YYYY",
                    picker = DatePickerMode.Year,
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
}

val TimePicker by story {
    var value by remember { mutableStateOf<String?>(null) }
    val placeholder by parameter("Select time")
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val showNow by parameter(true)
    val hourStep by parameter(1)
    val minuteStep by parameter(1)
    val secondStep by parameter(1)
    val use12Hours by parameter(false)
    val hideDisabledOptions by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntTimePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showNow = showNow,
                    hourStep = hourStep,
                    minuteStep = minuteStep,
                    secondStep = secondStep,
                    use12Hours = use12Hours,
                    hideDisabledOptions = hideDisabledOptions,
                    size = InputSize.Large,
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntTimePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showNow = showNow,
                    hourStep = hourStep,
                    minuteStep = minuteStep,
                    secondStep = secondStep,
                    use12Hours = use12Hours,
                    hideDisabledOptions = hideDisabledOptions,
                    size = InputSize.Middle,
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntTimePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showNow = showNow,
                    hourStep = hourStep,
                    minuteStep = minuteStep,
                    secondStep = secondStep,
                    use12Hours = use12Hours,
                    hideDisabledOptions = hideDisabledOptions,
                    size = InputSize.Small,
                    modifier = Modifier.width(200.dp)
                )
            }
        }

        Text("Format variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("24 Hour")
                AntTimePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showNow = showNow,
                    hourStep = hourStep,
                    minuteStep = minuteStep,
                    secondStep = secondStep,
                    use12Hours = false,
                    format = "HH:mm:ss",
                    hideDisabledOptions = hideDisabledOptions,
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("12 Hour")
                AntTimePicker(
                    value = value,
                    onChange = { newValue, _ -> value = newValue as? String },
                    placeholder = placeholder,
                    disabled = disabled,
                    allowClear = allowClear,
                    showNow = showNow,
                    hourStep = hourStep,
                    minuteStep = minuteStep,
                    secondStep = secondStep,
                    use12Hours = true,
                    format = "hh:mm:ss a",
                    hideDisabledOptions = hideDisabledOptions,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

val Upload by story {
    var fileList by remember { mutableStateOf(emptyList<UploadFile>()) }
    val accept by parameter("")
    val multiple by parameter(false)
    val maxCount by parameter(0)
    val disabled by parameter(false)
    val directory by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Upload list type variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Text")
                AntUpload(
                    fileList = fileList,
                    onChange = { param -> fileList = param.fileList },
                    listType = UploadListType.Text,
                    accept = accept.ifEmpty { null },
                    multiple = multiple,
                    maxCount = if (maxCount > 0) maxCount else null,
                    disabled = disabled,
                    directory = directory,
                    beforeUpload = null,
                    onRemove = null,
                    onPreview = null,
                    children = {
                        AntButton(onClick = {}, type = ButtonType.Default, disabled = disabled) {
                            Text("Click to Upload")
                        }
                    }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Picture")
                AntUpload(
                    fileList = fileList,
                    onChange = { param -> fileList = param.fileList },
                    listType = UploadListType.Picture,
                    accept = accept.ifEmpty { "image/*" },
                    multiple = multiple,
                    maxCount = if (maxCount > 0) maxCount else null,
                    disabled = disabled,
                    directory = directory,
                    beforeUpload = null,
                    onRemove = null,
                    onPreview = null,
                    children = {
                        AntButton(onClick = {}, type = ButtonType.Default, disabled = disabled) {
                            Text("Upload Picture")
                        }
                    }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Picture Card")
                AntUpload(
                    fileList = fileList,
                    onChange = { param -> fileList = param.fileList },
                    listType = UploadListType.PictureCard,
                    accept = accept.ifEmpty { "image/*" },
                    multiple = multiple,
                    maxCount = if (maxCount > 0) maxCount else null,
                    disabled = disabled,
                    directory = directory,
                    beforeUpload = null,
                    onRemove = null,
                    onPreview = null,
                    children = {
                        Text("+ Upload")
                    }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Picture Circle")
                AntUpload(
                    fileList = fileList,
                    onChange = { param -> fileList = param.fileList },
                    listType = UploadListType.PictureCircle,
                    accept = accept.ifEmpty { "image/*" },
                    multiple = multiple,
                    maxCount = if (maxCount > 0) maxCount else null,
                    disabled = disabled,
                    directory = directory,
                    beforeUpload = null,
                    onRemove = null,
                    onPreview = null,
                    children = {
                        Text("+ Upload")
                    }
                )
            }
        }
    }
}

val Dragger by story {
    var fileList by remember { mutableStateOf(emptyList<UploadFile>()) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntDragger(
            fileList = fileList,
            onChange = { param -> fileList = param.fileList },
            hint = "Click or drag file to this area to upload"
        )
    }
}

val Transfer by story {
    var targetKeys by remember { mutableStateOf(emptyList<String>()) }
    val disabled by parameter(false)
    val showSearch by parameter(false)
    val oneWay by parameter(false)
    val showSelectAll by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Transfer without pagination:")
        AntTransfer(
            dataSource = listOf(
                TransferItem(key = "1", title = "Content 1"),
                TransferItem(key = "2", title = "Content 2"),
                TransferItem(key = "3", title = "Content 3"),
                TransferItem(key = "4", title = "Content 4"),
                TransferItem(key = "5", title = "Content 5")
            ),
            targetKeys = targetKeys,
            onChange = { newKeys, _, _ -> targetKeys = newKeys },
            disabled = disabled,
            titles = listOf("Source", "Target"),
            operations = listOf("→", "←"),
            showSearch = showSearch,
            searchPlaceholder = "Search",
            filterOption = null,
            render = null,
            oneWay = oneWay,
            pagination = false,
            showSelectAll = showSelectAll
        )

        Text("Transfer with pagination:")
        var targetKeysPaginated by remember { mutableStateOf(emptyList<String>()) }
        AntTransfer(
            dataSource = listOf(
                TransferItem(key = "1", title = "Item 1"),
                TransferItem(key = "2", title = "Item 2"),
                TransferItem(key = "3", title = "Item 3"),
                TransferItem(key = "4", title = "Item 4"),
                TransferItem(key = "5", title = "Item 5"),
                TransferItem(key = "6", title = "Item 6"),
                TransferItem(key = "7", title = "Item 7"),
                TransferItem(key = "8", title = "Item 8"),
                TransferItem(key = "9", title = "Item 9"),
                TransferItem(key = "10", title = "Item 10"),
                TransferItem(key = "11", title = "Item 11"),
                TransferItem(key = "12", title = "Item 12"),
                TransferItem(key = "13", title = "Item 13"),
                TransferItem(key = "14", title = "Item 14"),
                TransferItem(key = "15", title = "Item 15")
            ),
            targetKeys = targetKeysPaginated,
            onChange = { newKeys, _, _ -> targetKeysPaginated = newKeys },
            disabled = disabled,
            titles = listOf("Source (Paginated)", "Target (Paginated)"),
            operations = listOf("→", "←"),
            showSearch = showSearch,
            searchPlaceholder = "Search",
            filterOption = null,
            render = null,
            oneWay = oneWay,
            pagination = true,
            showSelectAll = showSelectAll
        )
    }
}

val Cascader by story {
    var value by remember { mutableStateOf(emptyList<Any>()) }
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val showSearch by parameter(false)
    val changeOnSelect by parameter(false)
    val multiple by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntCascader(
                    options = listOf(
                        CascaderOption(
                            value = "zhejiang",
                            label = "Zhejiang",
                            children = listOf(
                                CascaderOption(value = "hangzhou", label = "Hangzhou"),
                                CascaderOption(value = "ningbo", label = "Ningbo")
                            )
                        ),
                        CascaderOption(
                            value = "jiangsu",
                            label = "Jiangsu",
                            children = listOf(
                                CascaderOption(value = "nanjing", label = "Nanjing"),
                                CascaderOption(value = "suzhou", label = "Suzhou")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    showSearch = showSearch,
                    changeOnSelect = changeOnSelect,
                    expandTrigger = ExpandTrigger.Click
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntCascader(
                    options = listOf(
                        CascaderOption(
                            value = "zhejiang",
                            label = "Zhejiang",
                            children = listOf(
                                CascaderOption(value = "hangzhou", label = "Hangzhou"),
                                CascaderOption(value = "ningbo", label = "Ningbo")
                            )
                        ),
                        CascaderOption(
                            value = "jiangsu",
                            label = "Jiangsu",
                            children = listOf(
                                CascaderOption(value = "nanjing", label = "Nanjing"),
                                CascaderOption(value = "suzhou", label = "Suzhou")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    showSearch = showSearch,
                    changeOnSelect = changeOnSelect,
                    expandTrigger = ExpandTrigger.Click
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntCascader(
                    options = listOf(
                        CascaderOption(
                            value = "zhejiang",
                            label = "Zhejiang",
                            children = listOf(
                                CascaderOption(value = "hangzhou", label = "Hangzhou"),
                                CascaderOption(value = "ningbo", label = "Ningbo")
                            )
                        ),
                        CascaderOption(
                            value = "jiangsu",
                            label = "Jiangsu",
                            children = listOf(
                                CascaderOption(value = "nanjing", label = "Nanjing"),
                                CascaderOption(value = "suzhou", label = "Suzhou")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    showSearch = showSearch,
                    changeOnSelect = changeOnSelect,
                    expandTrigger = ExpandTrigger.Click
                )
            }
        }

        Text("Status variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default")
                AntCascader(
                    options = listOf(
                        CascaderOption(value = "option1", label = "Option 1"),
                        CascaderOption(value = "option2", label = "Option 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Default status",
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error")
                AntCascader(
                    options = listOf(
                        CascaderOption(value = "option1", label = "Option 1"),
                        CascaderOption(value = "option2", label = "Option 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Error status",
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Warning")
                AntCascader(
                    options = listOf(
                        CascaderOption(value = "option1", label = "Option 1"),
                        CascaderOption(value = "option2", label = "Option 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Warning status",
                )
            }
        }
    }
}

val TreeSelect by story {
    var value by remember { mutableStateOf<Any?>(null) }
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val showSearch by parameter(false)
    val treeCheckable by parameter(false)
    val multiple by parameter(false)
    val treeDefaultExpandAll by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(
                            value = "0-0",
                            title = "Parent 1",
                            children = listOf(
                                TreeSelectNode(value = "0-0-0", title = "Leaf 1"),
                                TreeSelectNode(value = "0-0-1", title = "Leaf 2")
                            )
                        ),
                        TreeSelectNode(
                            value = "0-1",
                            title = "Parent 2",
                            children = listOf(
                                TreeSelectNode(value = "0-1-0", title = "Leaf 3"),
                                TreeSelectNode(value = "0-1-1", title = "Leaf 4")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    treeCheckable = treeCheckable,
                    multiple = multiple,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(
                            value = "0-0",
                            title = "Parent 1",
                            children = listOf(
                                TreeSelectNode(value = "0-0-0", title = "Leaf 1"),
                                TreeSelectNode(value = "0-0-1", title = "Leaf 2")
                            )
                        ),
                        TreeSelectNode(
                            value = "0-1",
                            title = "Parent 2",
                            children = listOf(
                                TreeSelectNode(value = "0-1-0", title = "Leaf 3"),
                                TreeSelectNode(value = "0-1-1", title = "Leaf 4")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    treeCheckable = treeCheckable,
                    multiple = multiple,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(
                            value = "0-0",
                            title = "Parent 1",
                            children = listOf(
                                TreeSelectNode(value = "0-0-0", title = "Leaf 1"),
                                TreeSelectNode(value = "0-0-1", title = "Leaf 2")
                            )
                        ),
                        TreeSelectNode(
                            value = "0-1",
                            title = "Parent 2",
                            children = listOf(
                                TreeSelectNode(value = "0-1-0", title = "Leaf 3"),
                                TreeSelectNode(value = "0-1-1", title = "Leaf 4")
                            )
                        )
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Please select",
                    disabled = disabled,
                    allowClear = allowClear,
                    treeCheckable = treeCheckable,
                    multiple = multiple,
                )
            }
        }

        Text("Status variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(value = "node1", title = "Node 1"),
                        TreeSelectNode(value = "node2", title = "Node 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Default status",
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(value = "node1", title = "Node 1"),
                        TreeSelectNode(value = "node2", title = "Node 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Error status",
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Warning")
                AntTreeSelect(
                    treeData = listOf(
                        TreeSelectNode(value = "node1", title = "Node 1"),
                        TreeSelectNode(value = "node2", title = "Node 2")
                    ),
                    value = value,
                    onValueChange = { value = it },
                    placeholder = "Warning status",
                )
            }
        }
    }
}

val Tree by story {
    val checkable by parameter(true)
    val defaultExpandAll by parameter(false)

    val treeData = remember {
        listOf(
            TreeNode<String>(
                key = "0-0",
                title = "Parent 1",
                children = listOf(
                    TreeNode<String>(key = "0-0-0", title = "Leaf 0-0-0"),
                    TreeNode<String>(key = "0-0-1", title = "Leaf 0-0-1"),
                    TreeNode<String>(key = "0-0-2", title = "Leaf 0-0-2")
                )
            ),
            TreeNode<String>(
                key = "0-1",
                title = "Parent 2",
                children = listOf(
                    TreeNode<String>(key = "0-1-0", title = "Leaf 0-1-0"),
                    TreeNode<String>(key = "0-1-1", title = "Leaf 0-1-1")
                )
            ),
            TreeNode<String>(
                key = "0-2",
                title = "Parent 3",
                children = listOf(
                    TreeNode<String>(key = "0-2-0", title = "Leaf 0-2-0")
                )
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default tree (checkable):")
        AntTree(
            treeData = treeData,
            checkable = checkable,
            defaultExpandAll = defaultExpandAll,
            defaultExpandedKeys = emptyList(),
            defaultSelectedKeys = emptyList(),
            defaultCheckedKeys = emptyList(),
            onSelect = null,
            onCheck = null,
            onExpand = null
        )

        Text("Tree with checkable disabled (selectable only):")
        AntTree(
            treeData = treeData,
            checkable = false,
            defaultExpandAll = false,
            defaultExpandedKeys = emptyList(),
            defaultSelectedKeys = emptyList(),
            defaultCheckedKeys = emptyList(),
            onSelect = null,
            onCheck = null,
            onExpand = null
        )

        Text("Tree with expanded keys:")
        AntTree(
            treeData = treeData,
            checkable = true,
            defaultExpandAll = false,
            defaultExpandedKeys = listOf("0-0", "0-1"),
            defaultSelectedKeys = emptyList(),
            defaultCheckedKeys = emptyList(),
            onSelect = null,
            onCheck = null,
            onExpand = null
        )

        Text("Tree with selected keys:")
        AntTree(
            treeData = treeData,
            checkable = false,
            defaultExpandAll = true,
            defaultExpandedKeys = emptyList(),
            defaultSelectedKeys = listOf("0-0-1", "0-1-0"),
            defaultCheckedKeys = emptyList(),
            onSelect = null,
            onCheck = null,
            onExpand = null
        )

        Text("Tree with checked keys:")
        AntTree(
            treeData = treeData,
            checkable = true,
            defaultExpandAll = true,
            defaultExpandedKeys = emptyList(),
            defaultSelectedKeys = emptyList(),
            defaultCheckedKeys = listOf("0-0-0", "0-1-1", "0-2-0"),
            onSelect = null,
            onCheck = null,
            onExpand = null
        )
    }
}

val Mentions by story {
    var value by remember { mutableStateOf("") }
    val disabled by parameter(false)
    val prefix by parameter("@")
    val split by parameter(" ")
    val autoSize by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Size variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntMentions(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        MentionOption(value = "afc163", label = "afc163"),
                        MentionOption(value = "zombieJ", label = "zombieJ"),
                        MentionOption(value = "yesmeck", label = "yesmeck")
                    ),
                    disabled = disabled,
                    prefix = prefix,
                    split = split,
                    autoSize = autoSize,
                    placement = PopoverPlacement.BottomLeft
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntMentions(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        MentionOption(value = "afc163", label = "afc163"),
                        MentionOption(value = "zombieJ", label = "zombieJ"),
                        MentionOption(value = "yesmeck", label = "yesmeck")
                    ),
                    disabled = disabled,
                    prefix = prefix,
                    split = split,
                    autoSize = autoSize,
                    placement = PopoverPlacement.BottomLeft
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntMentions(
                    value = value,
                    onChange = { value = it },
                    options = listOf(
                        MentionOption(value = "afc163", label = "afc163"),
                        MentionOption(value = "zombieJ", label = "zombieJ"),
                        MentionOption(value = "yesmeck", label = "yesmeck")
                    ),
                    disabled = disabled,
                    prefix = prefix,
                    split = split,
                    autoSize = autoSize,
                    placement = PopoverPlacement.BottomLeft
                )
            }
        }
    }
}

val Form by story {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val disabled by parameter(false)
    val colon by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout variants:")

        Text("Horizontal layout:")
        AntForm(
            layout = FormLayout.Horizontal,
            labelCol = 6,
            wrapperCol = 18,
            colon = colon,
        ) {
            AntFormItem(label = "Username", required = true) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Username"
                )
            }

            AntFormItem(label = "Password", required = true) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Password",
                    password = true
                )
            }

            AntFormItem { value, onChange, status ->
                AntCheckbox(
                    checked = value as? Boolean ?: false,
                    onCheckedChange = { onChange(it) },
                    children = { Text("Remember me") }
                )
            }

            AntFormItem { value, onChange, status ->
                AntButton(onClick = {}, type = ButtonType.Primary) {
                    Text("Submit")
                }
            }
        }

        Text("Vertical layout:")
        AntForm(
            layout = FormLayout.Vertical,
            colon = colon,
        ) {
            AntFormItem(label = "Username", required = true) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Username"
                )
            }

            AntFormItem(label = "Password", required = true) { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Password",
                    password = true
                )
            }

            AntFormItem { value, onChange, status ->
                AntButton(onClick = {}, type = ButtonType.Primary) {
                    Text("Submit")
                }
            }
        }

        Text("Inline layout:")
        AntForm(
            layout = FormLayout.Inline,
        ) {
            AntFormItem(label = "Username") { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Username"
                )
            }

            AntFormItem(label = "Password") { value, onChange, status ->
                AntInput(
                    value = value as? String ?: "",
                    onValueChange = { onChange(it) },
                    placeholder = "Password",
                    password = true
                )
            }

            AntFormItem { value, onChange, status ->
                AntButton(onClick = {}, type = ButtonType.Primary) {
                    Text("Submit")
                }
            }
        }

        Text("Form sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Large")
                AntForm(layout = FormLayout.Vertical) {
                    AntFormItem(label = "Name") { value, onChange, status ->
                        AntInput(
                            value = value as? String ?: "",
                            onValueChange = { onChange(it) },
                            placeholder = "Large input"
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Middle")
                AntForm(layout = FormLayout.Vertical) {
                    AntFormItem(label = "Name") { value, onChange, status ->
                        AntInput(
                            value = value as? String ?: "",
                            onValueChange = { onChange(it) },
                            placeholder = "Middle input"
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Small")
                AntForm(layout = FormLayout.Vertical) {
                    AntFormItem(label = "Name") { value, onChange, status ->
                        AntInput(
                            value = value as? String ?: "",
                            onValueChange = { onChange(it) },
                            placeholder = "Small input"
                        )
                    }
                }
            }
        }
    }
}

val Comment by story {
    val author by parameter("Han Solo")
    val content by parameter("This is a comment")
    val datetime by parameter("2 hours ago")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic comment:")
        AntComment(
            author = { Text(author) },
            content = { Text(content) },
            datetime = { Text(datetime) },
            avatar = null,
            actions = emptyList()
        )

        Text("Comment with avatar:")
        AntComment(
            author = { Text(author) },
            content = { Text(content) },
            datetime = { Text(datetime) },
            avatar = {
                AntAvatar(
                    text = "HS",
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
            },
            actions = emptyList()
        )

        Text("Comment with actions:")
        AntComment(
            author = { Text(author) },
            content = { Text(content) },
            datetime = { Text(datetime) },
            avatar = {
                AntAvatar(
                    text = "HS",
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
            },
            actions = listOf(
                { AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) { Text("Reply") } },
                { AntButton(onClick = {}, type = ButtonType.Link, size = ButtonSize.Small) { Text("Like") } }
            )
        )
    }
}

val FloatButton by story {
    val tooltip by parameter("Help")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Float button variants:")

        Text("Type variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default")
                AntFloatButton(
                    onClick = {},
                    tooltip = tooltip,
                    type = FloatButtonType.Default,
                    shape = FloatButtonShape.Circle,
                    icon = { Text("?") },
                    badge = null
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Primary")
                AntFloatButton(
                    onClick = {},
                    tooltip = tooltip,
                    type = FloatButtonType.Primary,
                    shape = FloatButtonShape.Circle,
                    icon = { Text("!") },
                    badge = null
                )
            }
        }

        Text("Shape variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Circle")
                AntFloatButton(
                    onClick = {},
                    tooltip = "Circle shape",
                    shape = FloatButtonShape.Circle,
                    icon = { Text("+") }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Square")
                AntFloatButton(
                    onClick = {},
                    tooltip = "Square shape",
                    shape = FloatButtonShape.Square,
                    icon = { Text("+") }
                )
            }
        }

        Text("With badge:")
        AntFloatButton(
            onClick = {},
            tooltip = tooltip,
            icon = { Text("@") },
            badge = {
                AntBadge(count = 5, dot = false)
            }
        )
    }
}

val Ribbon by story {
    val text by parameter("Ribbon")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Placement variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Start placement")
                AntRibbon(
                    text = text,
                    children = {
                        AntCard(title = "Card with ribbon") {
                            Text("Ribbon at start position")
                        }
                    },
                    color = Color(0xFF1890FF),
                    placement = RibbonPlacement.TopStart
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("End placement (default)")
                AntRibbon(
                    text = text,
                    children = {
                        AntCard(title = "Card with ribbon") {
                            Text("Ribbon at end position")
                        }
                    },
                    color = Color(0xFF1890FF),
                    placement = RibbonPlacement.TopEnd
                )
            }
        }

        Text("Color variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Blue",
                    children = {
                        AntCard(title = "Blue Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFF1890FF)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Green",
                    children = {
                        AntCard(title = "Green Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFF52C41A)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Red",
                    children = {
                        AntCard(title = "Red Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFFFF4D4F)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Orange",
                    children = {
                        AntCard(title = "Orange Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFFFAAD14)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Purple",
                    children = {
                        AntCard(title = "Purple Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFF722ED1)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                AntRibbon(
                    text = "Pink",
                    children = {
                        AntCard(title = "Pink Ribbon") {
                            Text("Content here")
                        }
                    },
                    color = Color(0xFFEB2F96)
                )
            }
        }

        Text("Different ribbon texts and placements:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntRibbon(
                text = "NEW",
                children = {
                    AntCard(title = "New Feature") {
                        Text("Latest addition to our product")
                    }
                },
                color = Color(0xFF52C41A),
                placement = RibbonPlacement.TopStart
            )

            AntRibbon(
                text = "HOT",
                children = {
                    AntCard(title = "Hot Deal") {
                        Text("Don't miss this opportunity")
                    }
                },
                color = Color(0xFFFF4D4F),
                placement = RibbonPlacement.TopEnd
            )
        }
    }
}

val PageHeader by story {
    val title by parameter("Title")
    val subTitle by parameter("This is a subtitle")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic page header:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = null,
            avatar = null,
            tags = null,
            extra = null,
            footer = null
        )

        Text("With breadcrumb:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = {
                AntBreadcrumb(
                    items = listOf(
                        BreadcrumbItem("Home", onClick = {}),
                        BreadcrumbItem("List", onClick = {}),
                        BreadcrumbItem("Detail")
                    ),
                    separator = "/"
                )
            },
            avatar = null,
            tags = null,
            extra = null,
            footer = null
        )

        Text("With avatar:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = null,
            avatar = AvatarProps(
                size = AvatarSize.Default,
                icon = {
                    Text("U")
                }
            ),
            tags = null,
            extra = null,
            footer = null
        )

        Text("With tags:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = null,
            avatar = null,
            tags = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntTag(text = "Running", color = TagColor.Processing)
                    AntTag(text = "New", color = TagColor.Success)
                }
            },
            extra = null,
            footer = null
        )

        Text("With extra content:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = null,
            avatar = null,
            tags = null,
            extra = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(onClick = {}, type = ButtonType.Default) {
                        Text("Cancel")
                    }
                    AntButton(onClick = {}, type = ButtonType.Primary) {
                        Text("Save")
                    }
                }
            },
            footer = null
        )

        Text("With footer:")
        AntPageHeader(
            title = title,
            subTitle = subTitle,
            onBack = {},
            breadcrumb = null,
            avatar = null,
            tags = null,
            extra = null,
            footer = {
                AntTabs(
                    activeKey = "1",
                    onChange = {},
                    items = listOf(
                        TabItem(key = "1", label = "Details", content = { Text("Details content") }),
                        TabItem(key = "2", label = "Rules", content = { Text("Rules content") })
                    ),
                    type = TabType.Line
                )
            }
        )
    }
}

val Affix by story {
    val offsetTop by parameter(10)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Affix with offsetTop (${offsetTop}dp from top):")
        repeat(10) {
            Text("Content row $it")
        }

        AntAffix(offsetTop = offsetTop.dp) {
            AntButton(onClick = {}, type = ButtonType.Primary) {
                Text("Affixed to Top")
            }
        }

        repeat(10) {
            Text("More content row ${it + 10}")
        }

        Text("Affix with offsetBottom:")
        AntAffix(offsetBottom = 20.dp) {
            AntButton(onClick = {}, type = ButtonType.Default) {
                Text("Affixed to Bottom")
            }
        }

        Text("Multiple affix examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntAffix(offsetTop = 50.dp) {
                AntButton(onClick = {}, size = ButtonSize.Small) {
                    Text("Top 50dp")
                }
            }
            AntAffix(offsetTop = 100.dp) {
                AntButton(onClick = {}, size = ButtonSize.Small) {
                    Text("Top 100dp")
                }
            }
        }
    }
}

val Anchor by story {
    val affix by parameter(true)
    val bounds by parameter(5)
    val offsetTop by parameter(0)
    val targetOffset by parameter(0)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Anchor navigation:")
        AntAnchor(
            items = listOf(
                AnchorItem(key = "1", href = "#basic", title = "Basic usage"),
                AnchorItem(key = "2", href = "#static", title = "Static Anchor"),
                AnchorItem(
                    key = "3",
                    href = "#api",
                    title = "API",
                    children = listOf(
                        AnchorItem(key = "3-1", href = "#anchor-props", title = "Anchor Props"),
                        AnchorItem(key = "3-2", href = "#link-props", title = "Link Props")
                    )
                )
            ),
            affix = affix,
            bounds = bounds.dp,
            offsetTop = offsetTop.dp,
            targetOffset = targetOffset.dp,
            getCurrentAnchor = null,
            onClick = null
        )
    }
}

val Tour by story {
    var open by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic tour:")
        AntButton(
            onClick = { open = true },
            type = ButtonType.Primary
        ) {
            Text("Start Tour")
        }

        if (open) {
            AntTour(
                open = open,
                onClose = { open = false },
                steps = listOf(
                    TourStep(
                        title = "Upload File",
                        description = "Put your files here."
                    ),
                    TourStep(
                        title = "Save",
                        description = "Save your changes."
                    ),
                    TourStep(
                        title = "Other Actions",
                        description = "Click to see other actions."
                    )
                )
            )
        }

        Text("Tour with different step configurations:")
        var complexOpen by remember { mutableStateOf(false) }
        AntButton(
            onClick = { complexOpen = true },
            type = ButtonType.Default
        ) {
            Text("Start Complex Tour")
        }

        if (complexOpen) {
            AntTour(
                open = complexOpen,
                onClose = { complexOpen = false },
                steps = listOf(
                    TourStep(
                        title = "Welcome",
                        description = "Welcome to the product tour! Let's explore the features."
                    ),
                    TourStep(
                        title = "Dashboard",
                        description = "This is your main dashboard where you can see an overview."
                    ),
                    TourStep(
                        title = "Settings",
                        description = "Customize your preferences in the settings panel."
                    ),
                    TourStep(
                        title = "Profile",
                        description = "Manage your profile information here."
                    ),
                    TourStep(
                        title = "Help Center",
                        description = "Need assistance? Visit our help center for support."
                    )
                )
            )
        }
    }
}

val Wave by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntWave {
            AntButton(onClick = {}, type = ButtonType.Primary) {
                Text("Wave Effect")
            }
        }
    }
}

val QRCode by story {
    val value by parameter("https://ant.design")
    val size by parameter(160)
    val iconSize by parameter(40)
    val bordered by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic QR Code:")
        AntQRCode(
            value = value,
            size = size.dp,
            icon = null,
            iconSize = IconSize.Simple(iconSize.dp),
            color = Color(0xFF000000),
            bgColor = Color.White,
            errorLevel = QRCodeErrorLevel.M,
            bordered = bordered
        )

        Text("With custom colors:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Blue")
                AntQRCode(
                    value = value,
                    size = 120.dp,
                    color = Color(0xFF1890FF),
                    bgColor = Color(0xFFE6F7FF),
                    errorLevel = QRCodeErrorLevel.M,
                    bordered = bordered
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Green")
                AntQRCode(
                    value = value,
                    size = 120.dp,
                    color = Color(0xFF52C41A),
                    bgColor = Color(0xFFF6FFED),
                    errorLevel = QRCodeErrorLevel.M,
                    bordered = bordered
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Red")
                AntQRCode(
                    value = value,
                    size = 120.dp,
                    color = Color(0xFFFF4D4F),
                    bgColor = Color(0xFFFFF1F0),
                    errorLevel = QRCodeErrorLevel.M,
                    bordered = bordered
                )
            }
        }

        Text("Error level variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("L (7%)")
                AntQRCode(
                    value = value,
                    size = 100.dp,
                    errorLevel = QRCodeErrorLevel.L,
                    bordered = bordered
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("M (15%)")
                AntQRCode(
                    value = value,
                    size = 100.dp,
                    errorLevel = QRCodeErrorLevel.M,
                    bordered = bordered
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Q (25%)")
                AntQRCode(
                    value = value,
                    size = 100.dp,
                    errorLevel = QRCodeErrorLevel.Q,
                    bordered = bordered
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("H (30%)")
                AntQRCode(
                    value = value,
                    size = 100.dp,
                    errorLevel = QRCodeErrorLevel.H,
                    bordered = bordered
                )
            }
        }
    }
}

val Watermark by story {
    val content by parameter("Ant Design")
    val rotate by parameter(-22)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic watermark:")
        AntWatermark(
            content = content,
            rotate = rotate.toFloat(),
            gap = Pair(100.dp, 100.dp),
            offset = null,
            image = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp)
            ) {
                repeat(10) {
                    Text("Content line $it")
                }
            }
        }

        Text("Custom gap settings:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Gap: 50dp x 50dp")
                AntWatermark(
                    content = "DRAFT",
                    rotate = -22f,
                    gap = Pair(50.dp, 50.dp),
                    offset = null,
                    image = null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFFFFBE6))
                            .padding(8.dp)
                    ) {
                        Text("Dense watermark")
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Gap: 150dp x 150dp")
                AntWatermark(
                    content = "DRAFT",
                    rotate = -22f,
                    gap = Pair(150.dp, 150.dp),
                    offset = null,
                    image = null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFFFFBE6))
                            .padding(8.dp)
                    ) {
                        Text("Sparse watermark")
                    }
                }
            }
        }

        Text("Custom offset:")
        AntWatermark(
            content = content,
            rotate = rotate.toFloat(),
            gap = Pair(100.dp, 100.dp),
            offset = Pair(50.dp, 50.dp),
            image = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp)
            ) {
                repeat(10) {
                    Text("Content with offset watermark $it")
                }
            }
        }

        Text("Different content types:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntWatermark(
                content = "CONFIDENTIAL",
                rotate = -22f,
                gap = Pair(120.dp, 100.dp),
                offset = null,
                image = null
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(150.dp)
                        .background(Color(0xFFFFF1F0))
                        .padding(8.dp)
                ) {
                    Text("Confidential document")
                }
            }

            AntWatermark(
                content = "SAMPLE",
                rotate = 0f,
                gap = Pair(100.dp, 80.dp),
                offset = null,
                image = null
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(150.dp)
                        .background(Color(0xFFE6F7FF))
                        .padding(8.dp)
                ) {
                    Text("Sample content (horizontal)")
                }
            }
        }
    }
}

val Typography by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Title levels:")
        AntTitle(level = 1, text = "h1. Ant Design")
        AntTitle(level = 2, text = "h2. Ant Design")
        AntTitle(level = 3, text = "h3. Ant Design")
        AntTitle(level = 4, text = "h4. Ant Design")

        Text("Text types:")
        AntText(text = "Ant Design (default)", type = TextType.Default)
        AntText(text = "Ant Design (secondary)", type = TextType.Secondary)
        AntText(text = "Ant Design (success)", type = TextType.Success)
        AntText(text = "Ant Design (warning)", type = TextType.Warning)
        AntText(text = "Ant Design (danger)", type = TextType.Danger)

        Text("Text styles:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                AntText(text = "Code", code = true)
                AntText(text = "Marked", mark = true)
                AntText(text = "Deleted", delete = true)
                AntText(text = "Underlined", underline = true)
                AntText(text = "Strong", strong = true)
                AntText(text = "Italic", italic = true)
            }
        }

        Text("Paragraph:")
        AntParagraph(text = "Ant Design, a design language for background applications, is refined by Ant UED Team. This is a demonstration of the paragraph component with various features.")

        Text("Combined styles:")
        AntText(text = "Code + Strong", code = true, strong = true)
        AntText(text = "Marked + Italic", mark = true, italic = true)
        AntText(text = "Underlined + Strong", underline = true, strong = true)
    }
}

val Compact by story {
    var value by remember { mutableStateOf("") }
    var selectValue by remember { mutableStateOf<String?>(null) }
    val block by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Button + Input combination:")
        AntCompact(
            direction = SpaceDirection.Horizontal,
            block = block
        ) {
            AntInput(
                value = value,
                onValueChange = { value = it },
                placeholder = "Input"
            )
            AntButton(onClick = {}, type = ButtonType.Primary) {
                Text("Submit")
            }
        }

        Text("Multiple Buttons:")
        AntCompact(
            direction = SpaceDirection.Horizontal,
            block = block
        ) {
            AntButton(onClick = {}, type = ButtonType.Default) {
                Text("Left")
            }
            AntButton(onClick = {}, type = ButtonType.Default) {
                Text("Middle")
            }
            AntButton(onClick = {}, type = ButtonType.Default) {
                Text("Right")
            }
        }

        Text("Input + Select combination:")
        AntCompact(
            direction = SpaceDirection.Horizontal,
            block = false
        ) {
            AntInput(
                value = value,
                onValueChange = { value = it },
                placeholder = "Enter value",
                modifier = Modifier.width(200.dp)
            )
            AntSelect(
                value = selectValue,
                onValueChange = { selectValue = it },
                options = listOf(
                    SelectOption(value = "option1", label = "Option 1"),
                    SelectOption(value = "option2", label = "Option 2")
                ),
                placeholder = "Select",
                modifier = Modifier.width(150.dp)
            )
        }

        Text("Button + Select + Button:")
        AntCompact(
            direction = SpaceDirection.Horizontal,
            block = false
        ) {
            AntButton(onClick = {}, type = ButtonType.Default, size = ButtonSize.Middle) {
                Text("Action")
            }
            AntSelect(
                value = selectValue,
                onValueChange = { selectValue = it },
                options = listOf(
                    SelectOption(value = "a", label = "Type A"),
                    SelectOption(value = "b", label = "Type B"),
                    SelectOption(value = "c", label = "Type C")
                ),
                placeholder = "Type",
                size = InputSize.Middle,
                modifier = Modifier.width(120.dp)
            )
            AntButton(onClick = {}, type = ButtonType.Primary, size = ButtonSize.Middle) {
                Text("Go")
            }
        }

        Text("Block mode (full width):")
        AntCompact(
            direction = SpaceDirection.Horizontal,
            block = true
        ) {
            AntInput(
                value = value,
                onValueChange = { value = it },
                placeholder = "Block input"
            )
            AntButton(onClick = {}, type = ButtonType.Primary) {
                Text("Submit")
            }
        }
    }
}

val Container by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntContainer {
            Column {
                Text("Container with default max width")
                Text("The content will be centered and have a max width")
                Text("This is useful for responsive layouts")
            }
        }
    }
}

val Splitter by story {
    val resizable by parameter(true)
    val initialSize by parameter(200)
    val minSize by parameter(50)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Horizontal splitter (resizable):")
        Box(modifier = Modifier.height(200.dp)) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = initialSize.dp,
                minSize = minSize.dp,
                maxSize = null,
                resizable = resizable,
                onResize = null,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFE6F7FF))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Left Panel")
                        Text("Drag the divider to resize")
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFF7E6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Right Panel")
                        Text("Content here")
                    }
                }
            )
        }

        Text("Vertical splitter:")
        Box(modifier = Modifier.height(300.dp)) {
            AntSplitter(
                layout = SplitterLayout.Vertical,
                initialSize = 150.dp,
                minSize = 50.dp,
                maxSize = 250.dp,
                resizable = true,
                onResize = null,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF0F5FF))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Top Panel")
                        Text("Vertical layout")
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFFBE6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Bottom Panel")
                        Text("Resizable vertically")
                    }
                }
            )
        }

        Text("Non-resizable splitter:")
        Box(modifier = Modifier.height(150.dp)) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 250.dp,
                minSize = 50.dp,
                maxSize = 500.dp,
                resizable = false,
                onResize = null,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFF1F0))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Left")
                        Text("Cannot resize")
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF6FFED))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Right")
                        Text("Static layout")
                    }
                }
            )
        }

        Text("Splitter with different initial sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small initial (100dp)")
                Box(modifier = Modifier.height(120.dp)) {
                    AntSplitter(
                        layout = SplitterLayout.Horizontal,
                        initialSize = 100.dp,
                        minSize = 50.dp,
                        maxSize = 300.dp,
                        resizable = true,
                        onResize = null,
                        panel1 = {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE6F7FF))
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Text("Left")
                            }
                        },
                        panel2 = {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF7E6))
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Text("Right")
                            }
                        }
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Large initial (300dp)")
                Box(modifier = Modifier.height(120.dp)) {
                    AntSplitter(
                        layout = SplitterLayout.Horizontal,
                        initialSize = 300.dp,
                        minSize = 50.dp,
                        maxSize = 400.dp,
                        resizable = true,
                        onResize = null,
                        panel1 = {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE6F7FF))
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Text("Left")
                            }
                        },
                        panel2 = {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF7E6))
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Text("Right")
                            }
                        }
                    )
                }
            }
        }
    }
}
