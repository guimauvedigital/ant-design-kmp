package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

// ==================== SLIDER COMPLETE ====================
val SliderComplete by story {
    // Value state
    var value by remember { mutableStateOf(30.0) }
    var rangeValue by remember { mutableStateOf(Pair(20.0, 50.0)) }

    // Basic parameters
    val range by parameter(false)
    val min by parameter(0.0)
    val max by parameter(100.0)
    val step by parameter(1.0)
    val disabled by parameter(false)
    val vertical by parameter(false)
    val reverse by parameter(false)

    // Visual parameters
    val dots by parameter(false)
    val included by parameter(true)
    val autoFocus by parameter(false)
    val keyboard by parameter(true)

    // Marks configuration
    val showMarks by parameter(false)
    val marksType by parameter(values = listOf("Simple", "Custom"), defaultValueIndex = 0)

    // Tooltip configuration
    val tooltipType by parameter(values = listOf("Auto", "AlwaysOn", "None"), defaultValueIndex = 0)
    val tooltipPlacement by parameter(values = listOf("Top", "Left", "Right", "Bottom"), defaultValueIndex = 0)
    val showTooltipFormatter by parameter(false)

    // Style customization
    val customHandleStyle by parameter(false)
    val customTrackStyle by parameter(false)
    val customRailStyle by parameter(false)
    val customDotStyle by parameter(false)

    // Range-specific
    val draggableTrack by parameter(false)

    // Build marks if enabled
    val marks = if (showMarks) {
        if (marksType == "Simple") {
            mapOf(
                0.0 to "0",
                26.0 to "26",
                37.0 to "37",
                100.0 to "100"
            )
        } else {
            mapOf(
                0.0 to MarkConfig(label = "Min", style = Modifier),
                50.0 to MarkConfig(label = "Mid", style = Modifier),
                100.0 to MarkConfig(label = "Max", style = Modifier)
            )
        }
    } else null

    // Build tooltip config
    val tooltipConfig = when (tooltipType) {
        "AlwaysOn" -> TooltipConfig(
            open = true,
            placement = when (tooltipPlacement) {
                "Left" -> digital.guimauve.antdesign.TooltipPlacement.Left
                "Right" -> digital.guimauve.antdesign.TooltipPlacement.Right
                "Bottom" -> digital.guimauve.antdesign.TooltipPlacement.Bottom
                else -> digital.guimauve.antdesign.TooltipPlacement.Top
            },
            formatter = if (showTooltipFormatter) { v -> "${v.toInt()}%" } else null
        )

        "None" -> TooltipConfig(open = false)
        else -> TooltipConfig(
            open = null,
            placement = when (tooltipPlacement) {
                "Left" -> digital.guimauve.antdesign.TooltipPlacement.Left
                "Right" -> digital.guimauve.antdesign.TooltipPlacement.Right
                "Bottom" -> digital.guimauve.antdesign.TooltipPlacement.Bottom
                else -> digital.guimauve.antdesign.TooltipPlacement.Top
            },
            formatter = if (showTooltipFormatter) { v -> "${v.toInt()}%" } else null
        )
    }

    // Custom styles
    val handleStyle = if (customHandleStyle) {
        Modifier
            .background(Color(0xFF1890FF))
            .border(2.dp, Color(0xFF096DD9))
    } else null

    val trackStyle = if (customTrackStyle) {
        Modifier.background(Color(0xFF52C41A))
    } else null

    val railStyle = if (customRailStyle) {
        Modifier.background(Color(0xFFF0F0F0))
    } else null

    val dotStyle = if (customDotStyle) {
        Modifier.background(Color(0xFFFF7875))
    } else null

    val activeDotStyle = if (customDotStyle) {
        Modifier.background(Color(0xFF52C41A))
    } else null

    // Semantic styles
    val styles = SliderStyles(
        rail = railStyle ?: Modifier,
        track = trackStyle ?: Modifier,
        handle = handleStyle ?: Modifier,
        dot = dotStyle ?: Modifier
    )

    val classNames = SliderClassNames(
        rail = "custom-rail",
        track = "custom-track",
        handle = "custom-handle",
        mark = "custom-mark",
        markText = "custom-mark-text",
        dot = "custom-dot"
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Slider Component (100% Parameters):")

        // Display current value
        Text(
            if (range) {
                "Range Value: [${rangeValue.first.toInt()}, ${rangeValue.second.toInt()}]"
            } else {
                "Value: ${value.toInt()}"
            }
        )

        // The slider with ALL parameters
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (vertical) Modifier.height(300.dp) else Modifier)
        ) {
            AntSlider(
                // Core value parameters
                value = if (range) rangeValue else value,
                onChange = { newValue ->
                    if (range) {
                        rangeValue = newValue as Pair<Double, Double>
                    } else {
                        value = newValue as Double
                    }
                },
                defaultValue = if (range) Pair(20.0, 50.0) else 30.0,
                onChangeComplete = { finalValue ->
                    println("Change complete: $finalValue")
                },

                // Range and bounds
                range = range,
                min = min,
                max = max,
                step = if (step > 0) step else null,

                // State parameters
                disabled = disabled,
                vertical = vertical,
                reverse = reverse,

                // Visual features
                dots = dots,
                included = included,
                marks = marks,

                // Tooltip configuration
                tooltip = tooltipConfig,

                // Interaction
                keyboard = keyboard,
                autoFocus = autoFocus,
                draggableTrack = draggableTrack && range,

                // Custom styles
                handleStyle = handleStyle,
                trackStyle = trackStyle,
                railStyle = railStyle,
                dotStyle = dotStyle,
                activeDotStyle = activeDotStyle,

                // Semantic structure
                classNames = classNames,
                styles = styles,

                // Modifier
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Parameter summary
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("Parameters:")
            Text("• Range: $range")
            Text("• Min: ${min.toInt()}, Max: ${max.toInt()}, Step: ${step}")
            Text("• Disabled: $disabled")
            Text("• Vertical: $vertical, Reverse: $reverse")
            Text("• Dots: $dots, Included: $included")
            Text("• Show Marks: $showMarks")
            if (showMarks) Text("  Marks Type: $marksType")
            Text("• Tooltip: $tooltipType")
            if (tooltipType != "None") Text("  Placement: $tooltipPlacement")
            Text("• Tooltip Formatter: $showTooltipFormatter")
            Text("• Keyboard: $keyboard, AutoFocus: $autoFocus")
            if (range) Text("• Draggable Track: $draggableTrack")
            Text("• Custom Handle: $customHandleStyle")
            Text("• Custom Track: $customTrackStyle")
            Text("• Custom Rail: $customRailStyle")
            Text("• Custom Dot: $customDotStyle")
        }
    }
}

// ==================== SLIDER EXAMPLES ====================
val SliderExamples by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Slider Examples:")

        // Example 1: Simple slider
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("1. Simple Slider")
            var value1 by remember { mutableStateOf(30.0) }
            Text("Value: ${value1.toInt()}")
            AntSlider(
                value = value1,
                onChange = { value1 = it as Double },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 2: Range slider
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("2. Range Slider")
            var range2 by remember { mutableStateOf(Pair(20.0, 80.0)) }
            Text("Range: [${range2.first.toInt()}, ${range2.second.toInt()}]")
            AntSlider(
                value = range2,
                onChange = { range2 = it as Pair<Double, Double> },
                range = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 3: With marks
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("3. Slider with Marks")
            var value3 by remember { mutableStateOf(26.0) }
            Text("Temperature: ${value3.toInt()}°C")
            AntSlider(
                value = value3,
                onChange = { value3 = it as Double },
                marks = mapOf(
                    0.0 to "0°C",
                    26.0 to "26°C",
                    37.0 to "37°C",
                    100.0 to "100°C"
                ),
                step = 1.0,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 4: With dots
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("4. Slider with Dots")
            var value4 by remember { mutableStateOf(20.0) }
            Text("Value: ${value4.toInt()}")
            AntSlider(
                value = value4,
                onChange = { value4 = it as Double },
                dots = true,
                step = 10.0,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 5: Vertical slider
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("5. Vertical Slider")
            var value5 by remember { mutableStateOf(50.0) }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntSlider(
                    value = value5,
                    onChange = { value5 = it as Double },
                    vertical = true,
                    modifier = Modifier.height(200.dp)
                )
                Text("Value: ${value5.toInt()}")
            }
        }

        // Example 6: Custom tooltip
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("6. Custom Tooltip Formatter")
            var value6 by remember { mutableStateOf(30.0) }
            Text("Value: ${value6.toInt()}%")
            AntSlider(
                value = value6,
                onChange = { value6 = it as Double },
                tooltip = TooltipConfig(
                    open = null,
                    formatter = { v -> "${v.toInt()}%" }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ==================== INPUTNUMBER COMPLETE ====================
val InputNumberComplete by story {
    // Value state
    var value by remember { mutableStateOf<Double?>(0.0) }

    // Basic parameters
    val min by parameter(-100.0)
    val max by parameter(100.0)
    val step by parameter(1.0)
    val precision by parameter(0)
    val usePrecision by parameter(false)

    // State parameters
    val disabled by parameter(false)
    val readOnly by parameter(false)
    val keyboard by parameter(true)
    val autoFocus by parameter(false)

    // Size and status
    val size by parameter(values = listOf("Small", "Middle", "Large"), defaultValueIndex = 1)
    val status by parameter(values = listOf("Default", "Error", "Warning"), defaultValueIndex = 0)
    val variant by parameter(values = listOf("Outlined", "Filled", "Borderless"), defaultValueIndex = 0)

    // Controls configuration
    val showControls by parameter(true)
    val controlsPosition by parameter(values = listOf("Right", "Left"), defaultValueIndex = 0)

    // Formatter/Parser
    val useFormatter by parameter(false)
    val formatterType by parameter(values = listOf("Currency", "Percent", "Custom"), defaultValueIndex = 0)
    val decimalSeparator by parameter(".")

    // String mode for large numbers
    val stringMode by parameter(false)

    // Addons and affixes
    val showPrefix by parameter(false)
    val showSuffix by parameter(false)
    val showAddonBefore by parameter(false)
    val showAddonAfter by parameter(false)

    // Interaction options
    val changeOnWheel by parameter(false)
    val changeOnBlur by parameter(true)

    // Placeholder
    val placeholder by parameter("Enter number")
    val bordered by parameter(true)

    // Build formatter/parser
    val formatter: ((Double?) -> String)? = if (useFormatter) {
        when (formatterType) {
            "Currency" -> { v -> v?.let { "$ ${it}" } ?: "" }
            "Percent" -> { v -> v?.let { "${it}%" } ?: "" }
            "Custom" -> { v -> v?.let { "[${it}]" } ?: "" }
            else -> null
        }
    } else null

    val parser: ((String) -> Double?)? = if (useFormatter) {
        when (formatterType) {
            "Currency" -> { text -> text.removePrefix("$").trim().toDoubleOrNull() }
            "Percent" -> { text -> text.removeSuffix("%").trim().toDoubleOrNull() }
            "Custom" -> { text -> text.removePrefix("[").removeSuffix("]").trim().toDoubleOrNull() }
            else -> null
        }
    } else null

    // Build controls config
    val controlsConfig = if (showControls) {
        ControlsConfig(
            position = if (controlsPosition == "Left") ControlsPosition.Left else ControlsPosition.Right
        )
    } else {
        false
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("InputNumber Component (100% Parameters):")

        // Display current value
        Text("Value: ${value ?: "null"}")

        // The InputNumber with ALL parameters
        AntInputNumber(
            // Core value parameters
            value = value,
            onValueChange = { newValue ->
                value = newValue
                println("Value changed: $newValue")
            },
            defaultValue = 0.0,
            onChange = { finalValue ->
                println("onChange callback: $finalValue")
            },

            // Bounds and step
            min = min,
            max = max,
            step = step,
            precision = if (usePrecision) precision else null,

            // Formatter and parser
            formatter = formatter,
            parser = parser,
            decimalSeparator = decimalSeparator,

            // State parameters
            disabled = disabled,
            readOnly = readOnly,
            keyboard = keyboard,
            autoFocus = autoFocus,
            stringMode = stringMode,

            // Controls
            controls = controlsConfig,

            // Addons and affixes
            prefix = if (showPrefix) {
                { Text("$", color = Color.Gray) }
            } else null,
            suffix = if (showSuffix) {
                { Text("USD", color = Color.Gray) }
            } else null,
            addonBefore = if (showAddonBefore) {
                { Text("+") }
            } else null,
            addonAfter = if (showAddonAfter) {
                { Text(".00") }
            } else null,

            // Visual parameters
            size = when (size) {
                "Small" -> ComponentSize.Small
                "Large" -> ComponentSize.Large
                else -> ComponentSize.Middle
            },
            status = when (status) {
                "Error" -> InputStatus.Error
                "Warning" -> InputStatus.Warning
                else -> InputStatus.Default
            },
            placeholder = placeholder,
            bordered = bordered,

            // Interaction options
            changeOnWheel = changeOnWheel,
            changeOnBlur = changeOnBlur,

            // Callbacks
            onPressEnter = {
                println("Enter pressed!")
            },
            onStep = { newValue, stepInfo ->
                println("Stepped ${stepInfo.type}: $newValue (offset: ${stepInfo.offset})")
            },
            onFocus = {
                println("Input focused")
            },
            onBlur = {
                println("Input blurred")
            },

            // Semantic structure
            classNames = InputNumberClassNames(
                input = "custom-input",
                prefix = "custom-prefix",
                suffix = "custom-suffix",
                group = "custom-group",
                wrapper = "custom-wrapper",
                affixWrapper = "custom-affix-wrapper"
            ),
            styles = InputNumberStyles(
                input = Modifier,
                prefix = Modifier,
                suffix = Modifier,
                group = Modifier,
                wrapper = Modifier,
                affixWrapper = Modifier
            ),

            // Modifier
            modifier = Modifier.fillMaxWidth()
        )

        // Parameter summary
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("Parameters:")
            Text("• Min: ${min.toInt()}, Max: ${max.toInt()}, Step: ${step}")
            Text("• Precision: ${if (usePrecision) precision else "auto"}")
            Text("• Disabled: $disabled, ReadOnly: $readOnly")
            Text("• Keyboard: $keyboard, AutoFocus: $autoFocus")
            Text("• Size: $size, Status: $status")
            Text("• Variant: $variant")
            Text("• Show Controls: $showControls")
            if (showControls) Text("  Position: $controlsPosition")
            Text("• Formatter: $useFormatter")
            if (useFormatter) Text("  Type: $formatterType")
            Text("• Decimal Separator: '$decimalSeparator'")
            Text("• String Mode: $stringMode")
            Text("• Prefix: $showPrefix, Suffix: $showSuffix")
            Text("• Addon Before: $showAddonBefore, After: $showAddonAfter")
            Text("• Change on Wheel: $changeOnWheel")
            Text("• Change on Blur: $changeOnBlur")
            Text("• Placeholder: '$placeholder'")
            Text("• Bordered: $bordered")
        }
    }
}

// ==================== INPUTNUMBER EXAMPLES ====================
val InputNumberExamples by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("InputNumber Examples:")

        // Example 1: Basic
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("1. Basic InputNumber")
            var value1 by remember { mutableStateOf<Double?>(0.0) }
            Text("Value: ${value1 ?: "null"}")
            AntInputNumber(
                value = value1,
                onValueChange = { value1 = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 2: With min/max
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("2. With Min/Max Constraints")
            var value2 by remember { mutableStateOf<Double?>(5.0) }
            Text("Value: ${value2 ?: "null"} (min: 0, max: 10)")
            AntInputNumber(
                value = value2,
                onValueChange = { value2 = it },
                min = 0.0,
                max = 10.0,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 3: With precision
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("3. With Decimal Precision")
            var value3 by remember { mutableStateOf<Double?>(3.14) }
            Text("Value: ${value3 ?: "null"}")
            AntInputNumber(
                value = value3,
                onValueChange = { value3 = it },
                step = 0.01,
                precision = 2,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 4: Currency formatter
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("4. Currency Formatter")
            var value4 by remember { mutableStateOf<Double?>(100.0) }
            Text("Value: ${value4 ?: "null"}")
            AntInputNumber(
                value = value4,
                onValueChange = { value4 = it },
                formatter = { v -> v?.let { "$ ${it}" } ?: "" },
                parser = { text -> text.removePrefix("$").trim().toDoubleOrNull() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 5: With addons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("5. With Addons and Prefix")
            var value5 by remember { mutableStateOf<Double?>(10.0) }
            Text("Value: ${value5 ?: "null"}")
            AntInputNumber(
                value = value5,
                onValueChange = { value5 = it },
                prefix = { Text("$") },
                suffix = { Text("USD") },
                addonBefore = { Text("+") },
                addonAfter = { Text(".00") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Example 6: Different sizes
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("6. Different Sizes")
            var valueSmall by remember { mutableStateOf<Double?>(1.0) }
            var valueMedium by remember { mutableStateOf<Double?>(2.0) }
            var valueLarge by remember { mutableStateOf<Double?>(3.0) }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Small:")
                    AntInputNumber(
                        value = valueSmall,
                        onValueChange = { valueSmall = it },
                        size = ComponentSize.Small,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Middle:")
                    AntInputNumber(
                        value = valueMedium,
                        onValueChange = { valueMedium = it },
                        size = ComponentSize.Middle,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Large:")
                    AntInputNumber(
                        value = valueLarge,
                        onValueChange = { valueLarge = it },
                        size = ComponentSize.Large,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Example 7: Status variants
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("7. Status Variants")
            var valueNormal by remember { mutableStateOf<Double?>(0.0) }
            var valueError by remember { mutableStateOf<Double?>(0.0) }
            var valueWarning by remember { mutableStateOf<Double?>(0.0) }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntInputNumber(
                    value = valueNormal,
                    onValueChange = { valueNormal = it },
                    placeholder = "Normal",
                    modifier = Modifier.fillMaxWidth()
                )
                AntInputNumber(
                    value = valueError,
                    onValueChange = { valueError = it },
                    status = InputStatus.Error,
                    placeholder = "Error status",
                    modifier = Modifier.fillMaxWidth()
                )
                AntInputNumber(
                    value = valueWarning,
                    onValueChange = { valueWarning = it },
                    status = InputStatus.Warning,
                    placeholder = "Warning status",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Example 8: Controls position
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("8. Controls Position")
            var valueLeft by remember { mutableStateOf<Double?>(5.0) }
            var valueRight by remember { mutableStateOf<Double?>(5.0) }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Controls on Left:")
                AntInputNumber(
                    value = valueLeft,
                    onValueChange = { valueLeft = it },
                    controls = ControlsConfig(position = ControlsPosition.Left),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Controls on Right:")
                AntInputNumber(
                    value = valueRight,
                    onValueChange = { valueRight = it },
                    controls = ControlsConfig(position = ControlsPosition.Right),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
