package com.antdesign.ui.stories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.antdesign.ui.*
import org.jetbrains.compose.storytale.story

val InputNumber by story {
    var value by remember { mutableStateOf<Double?>(0.0) }
    val min by parameter(-10.0)
    val max by parameter(100.0)
    val step by parameter(1.0)
    val disabled by parameter(false)
    val precision by parameter(0)
    val controls by parameter(true)
    val keyboard by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default:")
        AntInputNumber(
            value = value,
            onValueChange = { value = it },
            min = min,
            max = max,
            step = step,
            disabled = disabled,
            precision = precision,
            controls = controls,
            keyboard = keyboard
        )

        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntInputNumber(
                value = value,
                onValueChange = { value = it },
                size = ComponentSize.Large,
                disabled = disabled,
                controls = controls
            )
            AntInputNumber(
                value = value,
                onValueChange = { value = it },
                size = ComponentSize.Middle,
                disabled = disabled,
                controls = controls
            )
            AntInputNumber(
                value = value,
                onValueChange = { value = it },
                size = ComponentSize.Small,
                disabled = disabled,
                controls = controls
            )
        }

        Text("With formatter (currency):")
        AntInputNumber(
            value = value,
            onValueChange = { value = it },
            formatter = { "$${it?.toInt() ?: 0}" },
            parser = { it.replace("$", "").toDoubleOrNull() },
            min = 0.0,
            max = 1000.0,
            step = 10.0
        )

        Text("With formatter (percentage):")
        AntInputNumber(
            value = value,
            onValueChange = { value = it },
            formatter = { "${it?.toInt() ?: 0}%" },
            parser = { it.replace("%", "").toDoubleOrNull() },
            min = 0.0,
            max = 100.0,
            step = 1.0
        )
    }
}

val Radio by story {
    var selectedOption by remember { mutableStateOf<String?>("Option 1") }
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntRadioGroup(
            options = listOf(
                RadioOption(label = "Option 1", value = "Option 1"),
                RadioOption(label = "Option 2", value = "Option 2"),
                RadioOption(label = "Option 3", value = "Option 3")
            ),
            value = selectedOption,
            onChange = { event -> selectedOption = event.target.value as String },
            disabled = disabled
        )
    }
}

val RadioButtons by story {
    var selectedOption by remember { mutableStateOf<String?>("Option 1") }
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntRadioGroup(
            options = listOf(
                RadioOption(label = "Option 1", value = "Option 1"),
                RadioOption(label = "Option 2", value = "Option 2"),
                RadioOption(label = "Option 3", value = "Option 3")
            ),
            value = selectedOption,
            onChange = { event -> selectedOption = event.target.value as String },
            disabled = disabled,
            optionType = RadioOptionType.Button
        )
    }
}

val Rate by story {
    var value by remember { mutableStateOf(3) }
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val count by parameter(5)
    val allowHalf by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default rate:")
        AntRate(
            value = value,
            onValueChange = { value = it },
            disabled = disabled,
            allowClear = allowClear,
            count = count,
            allowHalf = allowHalf
        )

        Text("Rate with allowHalf enabled:")
        AntRate(
            value = value,
            onValueChange = { value = it },
            disabled = disabled,
            allowClear = allowClear,
            count = 5,
            allowHalf = true
        )

        Text("Rate with custom character:")
        AntRate(
            value = value,
            onValueChange = { value = it },
            disabled = disabled,
            allowClear = allowClear,
            count = 5,
            character = "❤"
        )

        Text("Rate with different characters:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Heart")
                AntRate(
                    value = 3,
                    onValueChange = {},
                    count = 5,
                    character = "❤"
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Thumb")
                AntRate(
                    value = 4,
                    onValueChange = {},
                    count = 5,
                    character = "👍"
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Circle")
                AntRate(
                    value = 2,
                    onValueChange = {},
                    count = 5,
                    character = "●"
                )
            }
        }

        Text("Rate with tooltips:")
        AntRate(
            value = value,
            onValueChange = { value = it },
            count = 5,
            tooltips = listOf("Terrible", "Bad", "Normal", "Good", "Excellent")
        )

        Text("Rate with tooltips and allowHalf:")
        AntRate(
            value = value,
            onValueChange = { value = it },
            count = 5,
            allowHalf = true,
            tooltips = listOf("Very Poor", "Poor", "Fair", "Good", "Amazing")
        )
    }
}

val Select by story {
    var selectedValue by remember { mutableStateOf<String?>(null) }
    val disabled by parameter(false)
    val allowClear by parameter(true)
    val showSearch by parameter(false)
    val loading by parameter(false)

    val options = remember {
        listOf(
            SelectOption(value = "jack", label = "Jack"),
            SelectOption(value = "lucy", label = "Lucy"),
            SelectOption(value = "tom", label = "Tom"),
            SelectOption(value = "mike", label = "Mike"),
            SelectOption(value = "sarah", label = "Sarah")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default (Single mode):")
        AntSelect(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            placeholder = "Select a person",
            disabled = disabled,
            allowClear = allowClear,
            showSearch = showSearch,
            loading = loading,
            size = InputSize.Middle,
            mode = SelectMode.Single
        )

        Text("Multiple selection mode:")
        AntSelect(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            placeholder = "Select multiple people",
            mode = SelectMode.Multiple,
            allowClear = allowClear,
            showSearch = showSearch
        )

        Text("Tags mode:")
        AntSelect(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            placeholder = "Select or create tags",
            mode = SelectMode.Tags,
            allowClear = allowClear,
            showSearch = true
        )

        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntSelect(
                value = selectedValue,
                onValueChange = { selectedValue = it },
                options = options,
                size = InputSize.Large,
                disabled = disabled
            )
            AntSelect(
                value = selectedValue,
                onValueChange = { selectedValue = it },
                options = options,
                size = InputSize.Middle,
                disabled = disabled
            )
            AntSelect(
                value = selectedValue,
                onValueChange = { selectedValue = it },
                options = options,
                size = InputSize.Small,
                disabled = disabled
            )
        }

        Text("Status variants:")
        AntSelect(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            status = InputStatus.Error,
            placeholder = "Error status"
        )
        AntSelect(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            status = InputStatus.Warning,
            placeholder = "Warning status"
        )
    }
}

val Slider by story {
    var value by remember { mutableStateOf(30.0) }
    val disabled by parameter(false)
    val dots by parameter(false)
    val step by parameter(1.0)
    val included by parameter(true)
    val reverse by parameter(false)
    val vertical by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic slider:")
        AntSlider(
            value = value,
            onChange = { value = it as Double },
            disabled = disabled,
            dots = dots,
            step = step,
            included = included,
            reverse = reverse,
            vertical = vertical
        )

        Text("Slider with marks:")
        AntSlider(
            value = value,
            onChange = { value = it as Double },
            marks = mapOf(
                0.0 to "0°C",
                25.0 to "25°C",
                50.0 to "50°C",
                75.0 to "75°C",
                100.0 to "100°C"
            ),
            step = step,
            included = included
        )

        Text("Slider with custom marks:")
        AntSlider(
            value = value,
            onChange = { value = it as Double },
            marks = mapOf(
                0.0 to "Start",
                50.0 to "Middle",
                100.0 to "End"
            ),
            step = 10.0,
            included = included
        )

        Text("Slider with included=false:")
        AntSlider(
            value = value,
            onChange = { value = it as Double },
            included = false,
            marks = mapOf(
                0.0 to "0",
                100.0 to "100"
            )
        )

        Text("Reversed slider:")
        AntSlider(
            value = value,
            onChange = { value = it as Double },
            reverse = true,
            marks = mapOf(
                0.0 to "Low",
                50.0 to "Medium",
                100.0 to "High"
            )
        )
    }
}

val RangeSlider by story {
    var value by remember { mutableStateOf(Pair(20.0, 80.0)) }
    val disabled by parameter(false)
    val step by parameter(1.0)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntSlider(
            value = value,
            onChange = { value = it as Pair<Double, Double> },
            disabled = disabled,
            step = step,
            range = true
        )
    }
}
