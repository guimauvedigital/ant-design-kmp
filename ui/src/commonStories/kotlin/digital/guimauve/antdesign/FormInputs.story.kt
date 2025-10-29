package digital.guimauve.antdesign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story

/**
 * COMPLETE Button Story with ALL parameters
 * Parameters: type, variant, color, size, shape, icon, iconPosition, loading, disabled, danger,
 * ghost, block, href, target, htmlType, autoInsertSpace, autoFocus, enableWaveEffect,
 * classNames, styles
 */
val ButtonComplete by story {
    // Basic parameters
    val text by parameter("Click Me")
    val disabled by parameter(false)
    val danger by parameter(false)
    val loading by parameter(false)
    val ghost by parameter(false)
    val block by parameter(false)
    val autoFocus by parameter(false)
    val enableWaveEffect by parameter(true)
    val autoInsertSpace by parameter(true)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Button Types:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading),
                ghost = ghost,
                block = block,
                autoFocus = autoFocus,
                enableWaveEffect = enableWaveEffect
            ) { Text("Primary") }
            AntButton(
                onClick = {},
                type = ButtonType.Default,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading),
                ghost = ghost
            ) { Text("Default") }
            AntButton(
                onClick = {},
                type = ButtonType.Dashed,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading),
                ghost = ghost
            ) { Text("Dashed") }
            AntButton(
                onClick = {},
                type = ButtonType.Link,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text("Link") }
            AntButton(
                onClick = {},
                type = ButtonType.Text,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text("Text") }
        }

        Text("Button Variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                variant = ButtonVariant.Solid,
                disabled = disabled,
                danger = danger
            ) { Text("Solid") }
            AntButton(
                onClick = {},
                variant = ButtonVariant.Outlined,
                disabled = disabled,
                danger = danger
            ) { Text("Outlined") }
            AntButton(
                onClick = {},
                variant = ButtonVariant.Dashed,
                disabled = disabled,
                danger = danger
            ) { Text("Dashed") }
            AntButton(
                onClick = {},
                variant = ButtonVariant.Text,
                disabled = disabled,
                danger = danger
            ) { Text("Text") }
            AntButton(
                onClick = {},
                variant = ButtonVariant.Link,
                disabled = disabled,
                danger = danger
            ) { Text("Link") }
        }

        Text("Button Colors (v5.21.0+):")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                color = ButtonColor.Primary,
                disabled = disabled
            ) { Text("Primary") }
            AntButton(
                onClick = {},
                color = ButtonColor.Danger,
                disabled = disabled
            ) { Text("Danger") }
            AntButton(
                onClick = {},
                color = ButtonColor.Default,
                disabled = disabled
            ) { Text("Default") }
        }

        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Large,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text("Large") }
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Middle,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text("Middle") }
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Small,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text("Small") }
        }

        Text("Shapes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Default,
                disabled = disabled
            ) { Text("Default") }
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Round,
                disabled = disabled
            ) { Text("Round") }
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                disabled = disabled
            ) { Text("O") }
        }

        Text("With Icons - Start Position:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                icon = { Text("🔍", fontSize = 14.sp) },
                iconPosition = IconPosition.Start,
                disabled = disabled
            ) { Text("Search") }
            AntButton(
                onClick = {},
                type = ButtonType.Default,
                icon = { Text("⬇", fontSize = 14.sp) },
                iconPosition = IconPosition.Start,
                disabled = disabled
            ) { Text("Download") }
        }

        Text("With Icons - End Position:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                icon = { Text("→", fontSize = 14.sp) },
                iconPosition = IconPosition.End,
                disabled = disabled
            ) { Text("Next") }
            AntButton(
                onClick = {},
                type = ButtonType.Default,
                icon = { Text("↗", fontSize = 14.sp) },
                iconPosition = IconPosition.End,
                disabled = disabled
            ) { Text("Open") }
        }

        Text("Icon-only buttons:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                icon = { Text("🔍", fontSize = 14.sp) },
                disabled = disabled
            ) { Text("") }
            AntButton(
                onClick = {},
                type = ButtonType.Default,
                shape = ButtonShape.Circle,
                icon = { Text("✏", fontSize = 14.sp) },
                disabled = disabled
            ) { Text("") }
        }

        if (block) {
            Text("Block button:")
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                block = true,
                disabled = disabled,
                danger = danger,
                loading = ButtonLoading.fromBoolean(loading)
            ) { Text(text) }
        }

        Text("HTML Types:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                htmlType = ButtonHTMLType.Button
            ) { Text("Button") }
            AntButton(
                onClick = {},
                htmlType = ButtonHTMLType.Submit
            ) { Text("Submit") }
            AntButton(
                onClick = {},
                htmlType = ButtonHTMLType.Reset
            ) { Text("Reset") }
        }
    }
}

/**
 * COMPLETE Input Story with ALL parameters
 * Parameters: value, placeholder, size, status, variant, prefix, suffix, addonBefore, addonAfter,
 * allowClear, showCount, maxLength, disabled, readOnly, password, bordered, type, styles, classNames,
 * onClear, onFocus, onBlur, onPressEnter, count, ref, etc.
 */
val InputComplete by story {
    var value by parameter("Input text")
    val placeholder by parameter("Enter text...")
    val disabled by parameter(false)
    val readOnly by parameter(false)
    val allowClear by parameter(false)
    val showCount by parameter(false)
    val password by parameter(false)
    val bordered by parameter(true)
    var maxLength by parameter(0)
    var clearCount by remember { mutableStateOf(0) }
    var focusCount by remember { mutableStateOf(0) }
    var blurCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sizes:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Large,
            disabled = disabled,
            readOnly = readOnly,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password,
            bordered = bordered
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Middle,
            disabled = disabled,
            readOnly = readOnly,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password,
            bordered = bordered
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Small,
            disabled = disabled,
            readOnly = readOnly,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password,
            bordered = bordered
        )

        Text("Status:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Default status",
            status = InputStatus.Default,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Error status",
            status = InputStatus.Error,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Warning status",
            status = InputStatus.Warning,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("Variants (v5.13.0+):")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Outlined",
            variant = InputVariant.Outlined,
            disabled = disabled,
            allowClear = allowClear
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Filled",
            variant = InputVariant.Filled,
            disabled = disabled,
            allowClear = allowClear
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Borderless",
            variant = InputVariant.Borderless,
            disabled = disabled,
            allowClear = allowClear
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Underlined (v5.24.0+)",
            variant = InputVariant.Underlined,
            disabled = disabled,
            allowClear = allowClear
        )

        Text("With prefix/suffix:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            prefix = { Text("https://") },
            suffix = { Text(".com") },
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("With addons:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            addonBefore = { Text("https://") },
            addonAfter = { Text(".com") },
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("Keyboard types:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Text",
            type = KeyboardType.Text,
            disabled = disabled
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Number",
            type = KeyboardType.Number,
            disabled = disabled
        )
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Email",
            type = KeyboardType.Email,
            disabled = disabled
        )

        Text("With callbacks (onClear, onFocus, onBlur):")
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Clear count: $clearCount", fontSize = 12.sp)
            Text("Focus count: $focusCount", fontSize = 12.sp)
            Text("Blur count: $blurCount", fontSize = 12.sp)
        }
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Input with callbacks",
            allowClear = true,
            onClear = { clearCount++ },
            onFocus = { focusCount++ },
            onBlur = { blurCount++ })
    }
}

/**
 * COMPLETE Select Story with ALL parameters
 * Parameters: value, options, mode, placeholder, disabled, allowClear, showSearch, loading, size,
 * status, variant, maxTagCount, maxTagPlaceholder, maxTagTextLength, maxCount, tokenSeparators,
 * optGroups, autoClearSearchValue, searchValue, optionFilterProp, filterOption, filterSort,
 * onSearch, open, defaultOpen, autoFocus, labelInValue, fieldNames, onChange, onSelect, onDeselect,
 * onClear, onOpenChange, virtual, listHeight, showArrow, placement, popupMatchSelectWidth,
 * classNames, styles, tagRender, optionRender, labelRender, notFoundContent, etc.
 */
val SelectComplete by story {
    var value by parameter<String?>(null)
    val placeholder by parameter("Please select")
    val disabled by parameter(false)
    val allowClear by parameter(false)
    val showSearch by parameter(false)
    val loading by parameter(false)
    val showArrow by parameter(true)
    val autoFocus by parameter(false)

    val options = remember {
        listOf(
            SelectOption("option1", "Option 1"),
            SelectOption("option2", "Option 2"),
            SelectOption("option3", "Option 3"),
            SelectOption("option4", "Option 4 - Disabled", disabled = true),
            SelectOption("option5", "Option 5")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Single Select Mode:")
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            mode = SelectMode.Single,
            placeholder = placeholder,
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch,
            loading = loading,
            showArrow = showArrow,
            autoFocus = autoFocus
        )

        Text("Sizes:")
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            size = InputSize.Large,
            placeholder = "Large"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            size = InputSize.Middle,
            placeholder = "Middle"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            size = InputSize.Small,
            placeholder = "Small"
        )

        Text("Status:")
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            status = InputStatus.Default,
            placeholder = "Default"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            status = InputStatus.Error,
            placeholder = "Error"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            status = InputStatus.Warning,
            placeholder = "Warning"
        )

        Text("Variants:")
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            variant = SelectVariant.Outlined,
            placeholder = "Outlined"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            variant = SelectVariant.Filled,
            placeholder = "Filled"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            variant = SelectVariant.Borderless,
            placeholder = "Borderless"
        )
        AntSelect(
            value = value,
            onValueChange = { value = it },
            options = options,
            variant = SelectVariant.Underlined,
            placeholder = "Underlined"
        )

        Text("Multiple Select Mode:")
        var multipleValues by remember { mutableStateOf<Any?>(emptyList<String>()) }
        AntSelect<String>(
            value = multipleValues as? String,
            onValueChange = { multipleValues = it },
            options = options,
            mode = SelectMode.Multiple,
            placeholder = "Select multiple options",
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch
        )

        Text("Tags Mode:")
        var tagsValues by remember { mutableStateOf<Any?>(emptyList<String>()) }
        AntSelect<String>(
            value = tagsValues as? String,
            onValueChange = { tagsValues = it },
            options = options,
            mode = SelectMode.Tags,
            placeholder = "Create and select tags",
            disabled = disabled,
            allowClear = allowClear,
            showSearch = true,
            tokenSeparators = listOf(",", " ")
        )
    }
}

/**
 * COMPLETE Checkbox Story with ALL parameters
 * Parameters: checked, defaultChecked, disabled, indeterminate, onChange, classNames, styles,
 * autoFocus, id, name, value, tabIndex, etc.
 */
val CheckboxComplete by story {
    var checked by parameter(false)
    val disabled by parameter(false)
    val indeterminate by parameter(false)
    val autoFocus by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Checkbox:")
        AntCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            disabled = disabled,
            indeterminate = indeterminate,
            children = { Text("Checkbox") }
        )

        Text("Checkbox States:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntCheckbox(checked = false, onCheckedChange = {}, children = { Text("Unchecked") })
            AntCheckbox(
                checked = false,
                onCheckedChange = {},
                indeterminate = true,
                children = { Text("Indeterminate") })
            AntCheckbox(checked = true, onCheckedChange = {}, children = { Text("Checked") })
            AntCheckbox(checked = true, onCheckedChange = {}, disabled = true, children = { Text("Disabled") })
        }

        Text("Indeterminate use case (Select All):")
        var parentChecked by remember { mutableStateOf(false) }
        var parentIndeterminate by remember { mutableStateOf(false) }
        var option1 by remember { mutableStateOf(false) }
        var option2 by remember { mutableStateOf(false) }
        var option3 by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntCheckbox(
                checked = parentChecked,
                onCheckedChange = { newChecked ->
                    parentChecked = newChecked
                    parentIndeterminate = false
                    option1 = newChecked
                    option2 = newChecked
                    option3 = newChecked
                },
                indeterminate = parentIndeterminate,
                children = { Text("Select All") }
            )
            Column(modifier = Modifier.padding(start = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntCheckbox(
                    checked = option1,
                    onCheckedChange = {
                        option1 = it
                        val checkedCount = listOf(it, option2, option3).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 1") }
                )
                AntCheckbox(
                    checked = option2,
                    onCheckedChange = {
                        option2 = it
                        val checkedCount = listOf(option1, it, option3).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 2") }
                )
                AntCheckbox(
                    checked = option3,
                    onCheckedChange = {
                        option3 = it
                        val checkedCount = listOf(option1, option2, it).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 3") }
                )
            }
        }
    }
}

/**
 * COMPLETE Radio Story with ALL parameters
 * Parameters: value, checked, defaultChecked, disabled, autoFocus, name, id, onChange, onBlur,
 * onClick, onFocus, onKeyDown, onKeyPress, onMouseEnter, onMouseLeave, className, classNames,
 * style, styles, prefixCls, required, rootClassName, skipGroup, tabIndex, title, type, ref, etc.
 */
val RadioComplete by story {
    var selectedValue by parameter<Any>("option1")
    val disabled by parameter(false)
    val autoFocus by parameter(false)
    val required by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Radio Buttons:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntRadio(
                value = "option1",
                checked = selectedValue == "option1",
                disabled = disabled,
                autoFocus = autoFocus,
                required = required,
                onChange = { event -> selectedValue = event.target.value },
                children = { Text("Option 1") }
            )
            AntRadio(
                value = "option2",
                checked = selectedValue == "option2",
                disabled = disabled,
                required = required,
                onChange = { event -> selectedValue = event.target.value },
                children = { Text("Option 2") }
            )
            AntRadio(
                value = "option3",
                checked = selectedValue == "option3",
                disabled = disabled,
                required = required,
                onChange = { event -> selectedValue = event.target.value },
                children = { Text("Option 3") }
            )
        }

        Text("Radio with title (tooltip):")
        AntRadio(
            value = "tooltip",
            checked = selectedValue == "tooltip",
            title = "This is a tooltip",
            onChange = { event -> selectedValue = event.target.value },
            children = { Text("Hover for tooltip") }
        )
    }
}

/**
 * COMPLETE RadioGroup Story with ALL parameters
 * Parameters: value, defaultValue, disabled, name, options, optionType, buttonStyle, size, onChange,
 * onBlur, onFocus, onMouseEnter, onMouseLeave, className, classNames, rootClassName, style, styles,
 * block, id, etc.
 */
val RadioGroupComplete by story {
    var groupValue by parameter<Any>("option1")
    val disabled by parameter(false)
    val block by parameter(false)

    val options = remember {
        listOf(
            RadioOption(label = "Option 1", value = "option1"),
            RadioOption(label = "Option 2", value = "option2"),
            RadioOption(label = "Option 3", value = "option3"),
            RadioOption(label = "Option 4 - Disabled", value = "option4", disabled = true),
            RadioOption(label = "Option 5", value = "option5")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("RadioGroup - Default Type:")
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options,
            optionType = RadioOptionType.Default,
            disabled = disabled,
            block = block
        )

        Text("RadioGroup - Button Type - Outline:")
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options,
            optionType = RadioOptionType.Button,
            buttonStyle = RadioButtonStyle.Outline,
            disabled = disabled,
            block = block
        )

        Text("RadioGroup - Button Type - Solid:")
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options,
            optionType = RadioOptionType.Button,
            buttonStyle = RadioButtonStyle.Solid,
            disabled = disabled,
            block = block
        )

        Text("RadioGroup - Sizes:")
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options.take(3),
            optionType = RadioOptionType.Button,
            size = RadioSize.Large,
            disabled = disabled
        )
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options.take(3),
            optionType = RadioOptionType.Button,
            size = RadioSize.Middle,
            disabled = disabled
        )
        AntRadioGroup(
            value = groupValue,
            onChange = { event -> groupValue = event.target.value },
            options = options.take(3),
            optionType = RadioOptionType.Button,
            size = RadioSize.Small,
            disabled = disabled
        )
    }
}

/**
 * COMPLETE Switch Story with ALL parameters
 * Parameters: checked, defaultChecked, disabled, loading, size, checkedChildren, unCheckedChildren,
 * onChange, onClick, className, autoFocus, id, title, tabIndex, value, etc.
 */
val SwitchComplete by story {
    var checked by parameter(false)
    val disabled by parameter(false)
    val loading by parameter(false)
    val checkedChildren by parameter("")
    val unCheckedChildren by parameter("")
    val autoFocus by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Switch:")
        AntSwitch(
            checked = checked,
            onChange = { checked = it },
            disabled = disabled,
            loading = loading,
            size = ComponentSize.Middle,
            checkedChildren = if (checkedChildren.isEmpty()) null else {
                { Text(checkedChildren) }
            },
            unCheckedChildren = if (unCheckedChildren.isEmpty()) null else {
                { Text(unCheckedChildren) }
            }
        )

        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AntSwitch(
                checked = checked,
                onChange = { checked = it },
                size = ComponentSize.Middle,
                disabled = disabled,
                loading = loading
            )
            AntSwitch(
                checked = checked,
                onChange = { checked = it },
                size = ComponentSize.Small,
                disabled = disabled,
                loading = loading
            )
        }

        Text("Switch States:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AntSwitch(checked = false, onChange = {})
            AntSwitch(checked = true, onChange = {})
            AntSwitch(checked = false, onChange = {}, disabled = true)
            AntSwitch(checked = true, onChange = {}, disabled = true)
            AntSwitch(checked = checked, onChange = { checked = it }, loading = true)
        }

        Text("With Text:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AntSwitch(
                checked = checked,
                onChange = { checked = it },
                checkedChildren = { Text("ON") },
                unCheckedChildren = { Text("OFF") })
            AntSwitch(
                checked = checked,
                onChange = { checked = it },
                checkedChildren = { Text("✓") },
                unCheckedChildren = { Text("✕") },
                size = ComponentSize.Small
            )
        }
    }
}

/**
 * COMPLETE InputPassword Story with ALL parameters
 * Parameters: value, onValueChange, placeholder, disabled, readOnly, size, status, variant,
 * visibilityToggle, iconRender, prefix, suffix, allowClear, maxLength, showCount, count,
 * styles, classNames, ref, onPressEnter, onClear, etc.
 */
val InputPasswordComplete by story {
    var password by parameter("")
    val placeholder by parameter("Enter password...")
    val disabled by parameter(false)
    val readOnly by parameter(false)
    val allowClear by parameter(false)
    val showCount by parameter(false)
    val visibilityToggle by parameter(true)
    var maxLength by parameter(0)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Password Input:")
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            placeholder = placeholder,
            disabled = disabled,
            readOnly = readOnly,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            visibilityToggle = visibilityToggle
        )

        Text("Sizes:")
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            size = InputSize.Large,
            placeholder = "Large"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            size = InputSize.Middle,
            placeholder = "Middle"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            size = InputSize.Small,
            placeholder = "Small"
        )

        Text("Status:")
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            status = InputStatus.Default,
            placeholder = "Default"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            status = InputStatus.Error,
            placeholder = "Error"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            status = InputStatus.Warning,
            placeholder = "Warning"
        )

        Text("Variants:")
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            variant = InputVariant.Outlined,
            placeholder = "Outlined"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            variant = InputVariant.Filled,
            placeholder = "Filled"
        )
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            variant = InputVariant.Borderless,
            placeholder = "Borderless"
        )

        Text("Without visibility toggle:")
        AntInputPassword(
            value = password,
            onValueChange = { password = it },
            visibilityToggle = false,
            placeholder = "No toggle"
        )
    }
}

/**
 * COMPLETE InputSearch Story with ALL parameters
 * Parameters: value, onValueChange, onSearch, placeholder, disabled, readOnly, size, status, variant,
 * loading, enterButton, allowClear, prefix, addonBefore, maxLength, showCount, count, styles,
 * classNames, ref, etc.
 */
val InputSearchComplete by story {
    var searchValue by parameter("")
    val placeholder by parameter("Search...")
    val disabled by parameter(false)
    val loading by parameter(false)
    val enterButton by parameter<Any>(true)
    val allowClear by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search Input:")
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = { /* handle search */ },
            placeholder = placeholder,
            disabled = disabled,
            loading = loading,
            enterButton = enterButton,
            allowClear = allowClear
        )

        Text("Sizes:")
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            size = InputSize.Large,
            placeholder = "Large"
        )
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            size = InputSize.Middle,
            placeholder = "Middle"
        )
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            size = InputSize.Small,
            placeholder = "Small"
        )

        Text("Enter Button Variants:")
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            enterButton = true,
            placeholder = "With icon button"
        )
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            enterButton = "Search",
            placeholder = "With text button"
        )
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            enterButton = false,
            placeholder = "No button"
        )

        Text("Variants:")
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            variant = InputVariant.Outlined,
            placeholder = "Outlined"
        )
        AntInputSearch(
            value = searchValue,
            onValueChange = { searchValue = it },
            onSearch = {},
            variant = InputVariant.Filled,
            placeholder = "Filled"
        )
    }
}

/**
 * COMPLETE TextArea Story with ALL parameters
 * Parameters: value, onValueChange, placeholder, disabled, readOnly, status, variant, rows, maxLength,
 * showCount, count, autoSize, minRows, maxRows, styles, classNames, ref, onResize, etc.
 */
val TextAreaComplete by story {
    var value by parameter("This is a text area with multiple lines.\nYou can edit this text.")
    val placeholder by parameter("Enter your text here...")
    val disabled by parameter(false)
    val readOnly by parameter(false)
    val rows by parameter(3)
    val maxLength by parameter(0)
    val showCount by parameter(false)
    val autoSize by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default TextArea:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            disabled = disabled,
            readOnly = readOnly,
            rows = rows,
            maxLength = if (maxLength > 0) maxLength else null,
            showCount = showCount,
            autoSize = autoSize,
            modifier = Modifier.width(400.dp)
        )

        Text("Status:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            status = InputStatus.Default,
            placeholder = "Default",
            modifier = Modifier.width(400.dp)
        )
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            status = InputStatus.Error,
            placeholder = "Error",
            modifier = Modifier.width(400.dp)
        )
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            status = InputStatus.Warning,
            placeholder = "Warning",
            modifier = Modifier.width(400.dp)
        )

        Text("Variants:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            variant = InputVariant.Outlined,
            placeholder = "Outlined",
            modifier = Modifier.width(400.dp)
        )
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            variant = InputVariant.Filled,
            placeholder = "Filled",
            modifier = Modifier.width(400.dp)
        )
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            variant = InputVariant.Borderless,
            placeholder = "Borderless",
            modifier = Modifier.width(400.dp)
        )
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            variant = InputVariant.Underlined,
            placeholder = "Underlined",
            modifier = Modifier.width(400.dp)
        )

        Text("With character count:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            rows = rows,
            maxLength = 200,
            showCount = true,
            modifier = Modifier.width(400.dp)
        )

        Text("Disabled state:")
        AntTextArea(
            value = "This text area is disabled",
            onValueChange = {},
            placeholder = placeholder,
            disabled = true,
            rows = rows,
            modifier = Modifier.width(400.dp)
        )

        Text("Different row sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("2 rows")
                AntTextArea(value = value, onValueChange = { value = it }, rows = 2, modifier = Modifier.width(180.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("5 rows")
                AntTextArea(value = value, onValueChange = { value = it }, rows = 5, modifier = Modifier.width(180.dp))
            }
        }
    }
}
