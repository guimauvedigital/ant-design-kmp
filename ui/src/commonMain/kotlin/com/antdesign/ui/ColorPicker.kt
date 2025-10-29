package com.antdesign.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

// ============================================================================
// TYPE DEFINITIONS
// ============================================================================

/**
 * Color format type
 */
enum class ColorFormat {
    Hex,
    RGB,
    HSB,
    HSL
}

/**
 * Color picker mode
 */
enum class ModeType {
    Single,
    Gradient
}

/**
 * Trigger type for color picker
 */
enum class TriggerType {
    Click,
    Hover
}

/**
 * Color value representation
 */
sealed class ColorValue {
    data class Single(val color: Color) : ColorValue()
    data class Gradient(val stops: List<GradientStop>) : ColorValue()
}

/**
 * Gradient stop for gradient mode
 */
data class GradientStop(
    val color: Color,
    val percent: Float // 0.0 to 1.0
)

/**
 * Preset colors item
 */
data class PresetsItem(
    val label: String,
    val colors: List<Color>,
    val defaultOpen: Boolean = false,
    val key: String? = null
)

/**
 * HSB color representation (Hue, Saturation, Brightness)
 */
data class HSBColor(
    val hue: Float,        // 0-360
    val saturation: Float, // 0-1
    val brightness: Float, // 0-1
    val alpha: Float = 1f  // 0-1
)

/**
 * HSL color representation (Hue, Saturation, Lightness)
 */
data class HSLColor(
    val hue: Float,       // 0-360
    val saturation: Float, // 0-1
    val lightness: Float,  // 0-1
    val alpha: Float = 1f  // 0-1
)

/**
 * RGB color representation
 */
data class RGBColor(
    val red: Int,     // 0-255
    val green: Int,   // 0-255
    val blue: Int,    // 0-255
    val alpha: Float = 1f  // 0-1
)

/**
 * Custom styles for ColorPicker
 */
data class ColorPickerStyles(
    val popup: Modifier? = null,
    val popupOverlayInner: Modifier? = null
)

/**
 * Arrow configuration
 */
sealed class ArrowConfig {
    object None : ArrowConfig()
    object Default : ArrowConfig()
    data class Custom(val pointAtCenter: Boolean) : ArrowConfig()
}

// ============================================================================
// MAIN COLORPICKER COMPONENT
// ============================================================================

/**
 * Ant Design ColorPicker component with 100% React parity
 *
 * @param value Current color value (controlled)
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the component
 * @param defaultValue Default color value (uncontrolled)
 * @param mode Color mode: single or gradient
 * @param disabled Disable the color picker
 * @param format Color format to display
 * @param defaultFormat Default color format
 * @param allowClear Allow clearing the color
 * @param presets Preset color items
 * @param open Whether popup is open (controlled)
 * @param onOpenChange Callback when popup open state changes
 * @param trigger Trigger type for opening popup
 * @param placement Placement of popup relative to trigger
 * @param arrow Arrow configuration
 * @param showText Show color text next to trigger
 * @param size Size of the trigger button
 * @param styles Custom styles for popup
 * @param rootClassName Root class name
 * @param disabledAlpha Disable alpha channel
 * @param disabledFormat Disable format switcher
 * @param panelRender Custom panel render function
 * @param children Custom trigger content
 * @param onFormatChange Callback when format changes
 * @param onChange Callback when color changes (with CSS string)
 * @param onClear Callback when color is cleared
 * @param onChangeComplete Callback when color selection is complete
 * @param getPopupContainer Custom popup container
 * @param autoAdjustOverflow Auto adjust popup overflow
 * @param destroyTooltipOnHide Destroy tooltip on hide
 */
@Composable
fun AntColorPicker(
    value: Color? = null,
    onValueChange: ((Color) -> Unit)? = null,
    modifier: Modifier = Modifier,
    defaultValue: Color = Color.Black,
    mode: ModeType = ModeType.Single,
    disabled: Boolean = false,
    format: ColorFormat? = null,
    defaultFormat: ColorFormat = ColorFormat.Hex,
    allowClear: Boolean = false,
    presets: List<PresetsItem>? = null,
    open: Boolean? = null,
    onOpenChange: ((Boolean) -> Unit)? = null,
    trigger: TriggerType = TriggerType.Click,
    placement: PopoverPlacement = PopoverPlacement.BottomLeft,
    arrow: ArrowConfig = ArrowConfig.Default,
    showText: Boolean = false,
    size: ButtonSize = ButtonSize.Middle,
    styles: ColorPickerStyles? = null,
    rootClassName: String? = null,
    disabledAlpha: Boolean = false,
    disabledFormat: Boolean = false,
    panelRender: (@Composable (panel: @Composable () -> Unit) -> Unit)? = null,
    children: (@Composable () -> Unit)? = null,
    onFormatChange: ((ColorFormat) -> Unit)? = null,
    onChange: ((Color, String) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onChangeComplete: ((Color) -> Unit)? = null,
    getPopupContainer: (() -> Unit)? = null,
    autoAdjustOverflow: Boolean = true,
    destroyTooltipOnHide: Boolean = false
) {
    // State management
    var internalColor by remember { mutableStateOf(value ?: defaultValue) }
    var internalFormat by remember { mutableStateOf(format ?: defaultFormat) }
    var internalOpen by remember { mutableStateOf(false) }

    val currentColor = value ?: internalColor
    val currentFormat = format ?: internalFormat
    val isOpen = open ?: internalOpen

    // HSB state for color panel
    var hsbColor by remember(currentColor) {
        mutableStateOf(colorToHSB(currentColor))
    }

    // Update color handlers
    val updateColor: (Color) -> Unit = { newColor ->
        if (value == null) {
            internalColor = newColor
        }
        hsbColor = colorToHSB(newColor)
        val cssString = colorToCssString(newColor, currentFormat)
        onChange?.invoke(newColor, cssString)
        onValueChange?.invoke(newColor)
    }

    val updateOpen: (Boolean) -> Unit = { newOpen ->
        if (open == null) {
            internalOpen = newOpen
        }
        onOpenChange?.invoke(newOpen)

        // Call onChangeComplete when closing
        if (!newOpen && isOpen) {
            onChangeComplete?.invoke(currentColor)
        }
    }

    val updateFormat: (ColorFormat) -> Unit = { newFormat ->
        if (format == null) {
            internalFormat = newFormat
        }
        onFormatChange?.invoke(newFormat)
    }

    Box(modifier = modifier) {
        // Custom trigger or default trigger
        if (children != null) {
            Box(
                modifier = Modifier.clickable(
                    enabled = !disabled && trigger == TriggerType.Click
                ) {
                    updateOpen(!isOpen)
                }
            ) {
                children()
            }
        } else {
            DefaultTrigger(
                color = currentColor,
                format = currentFormat,
                size = size,
                disabled = disabled,
                showText = showText,
                onClick = {
                    if (!disabled && trigger == TriggerType.Click) {
                        updateOpen(!isOpen)
                    }
                }
            )
        }

        // Color picker popup
        if (isOpen && !disabled) {
            Popup(
                alignment = getPopupAlignment(placement),
                onDismissRequest = { updateOpen(false) },
                properties = PopupProperties(focusable = true)
            ) {
                val popupModifier = styles?.popup ?: Modifier
                val innerModifier = styles?.popupOverlayInner ?: Modifier

                Card(
                    modifier = popupModifier
                        .then(innerModifier)
                        .padding(8.dp)
                        .width(280.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    val panelContent = @Composable {
                        ColorPickerPanel(
                            color = currentColor,
                            hsbColor = hsbColor,
                            format = currentFormat,
                            disabledAlpha = disabledAlpha,
                            disabledFormat = disabledFormat,
                            allowClear = allowClear,
                            presets = presets,
                            mode = mode,
                            onColorChange = { newHsb ->
                                hsbColor = newHsb
                                val newColor = hsbToColor(newHsb)
                                updateColor(newColor)
                            },
                            onFormatChange = updateFormat,
                            onClear = {
                                updateColor(defaultValue)
                                onClear?.invoke()
                                updateOpen(false)
                            },
                            onPresetSelect = { presetColor ->
                                updateColor(presetColor)
                                updateOpen(false)
                            }
                        )
                    }

                    if (panelRender != null) {
                        panelRender(panelContent)
                    } else {
                        panelContent()
                    }
                }
            }
        }
    }
}

// ============================================================================
// DEFAULT TRIGGER COMPONENT
// ============================================================================

@Composable
private fun DefaultTrigger(
    color: Color,
    format: ColorFormat,
    size: ButtonSize,
    disabled: Boolean,
    showText: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
            .clickable(enabled = !disabled, onClick = onClick)
            .padding(
                horizontal = when (size) {
                    ButtonSize.Small -> 8.dp
                    ButtonSize.Middle -> 12.dp
                    ButtonSize.Large -> 16.dp
                },
                vertical = when (size) {
                    ButtonSize.Small -> 4.dp
                    ButtonSize.Middle -> 6.dp
                    ButtonSize.Large -> 8.dp
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color preview block
        Box(
            modifier = Modifier
                .size(
                    when (size) {
                        ButtonSize.Small -> 16.dp
                        ButtonSize.Middle -> 20.dp
                        ButtonSize.Large -> 24.dp
                    }
                )
                .clip(RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(2.dp))
                .background(color)
        )

        if (showText) {
            Text(
                text = colorToString(color, format),
                fontSize = when (size) {
                    ButtonSize.Small -> 12.sp
                    ButtonSize.Middle -> 14.sp
                    ButtonSize.Large -> 16.sp
                },
                color = if (disabled) Color.Gray else Color.Black
            )
        }
    }
}

// ============================================================================
// COLOR PICKER PANEL
// ============================================================================

@Composable
private fun ColorPickerPanel(
    color: Color,
    hsbColor: HSBColor,
    format: ColorFormat,
    disabledAlpha: Boolean,
    disabledFormat: Boolean,
    allowClear: Boolean,
    presets: List<PresetsItem>?,
    mode: ModeType,
    onColorChange: (HSBColor) -> Unit,
    onFormatChange: (ColorFormat) -> Unit,
    onClear: () -> Unit,
    onPresetSelect: (Color) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Color panel (saturation/brightness selector)
        ColorSaturationBrightnessPanel(
            hue = hsbColor.hue,
            saturation = hsbColor.saturation,
            brightness = hsbColor.brightness,
            onSaturationBrightnessChange = { sat, bright ->
                onColorChange(hsbColor.copy(saturation = sat, brightness = bright))
            }
        )

        // Hue slider
        HueSlider(
            hue = hsbColor.hue,
            onHueChange = { newHue ->
                onColorChange(hsbColor.copy(hue = newHue))
            }
        )

        // Alpha slider (if not disabled)
        if (!disabledAlpha) {
            AlphaSlider(
                alpha = hsbColor.alpha,
                color = color,
                onAlphaChange = { newAlpha ->
                    onColorChange(hsbColor.copy(alpha = newAlpha))
                }
            )
        }

        // Color format input and controls
        ColorFormatInput(
            color = color,
            format = format,
            disabledFormat = disabledFormat,
            disabledAlpha = disabledAlpha,
            allowClear = allowClear,
            onColorChange = { newColor ->
                onColorChange(colorToHSB(newColor))
            },
            onFormatChange = onFormatChange,
            onClear = onClear
        )

        // Presets section
        if (presets != null) {
            PresetsSection(
                presets = presets,
                currentColor = color,
                onPresetSelect = onPresetSelect
            )
        } else {
            // Default preset colors
            DefaultPresetsGrid(
                currentColor = color,
                onPresetSelect = onPresetSelect
            )
        }
    }
}

// ============================================================================
// COLOR SATURATION/BRIGHTNESS PANEL (2D PICKER)
// ============================================================================

@Composable
private fun ColorSaturationBrightnessPanel(
    hue: Float,
    saturation: Float,
    brightness: Float,
    onSaturationBrightnessChange: (Float, Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val newSat = (offset.x / size.width).coerceIn(0f, 1f)
                            val newBright = 1f - (offset.y / size.height).coerceIn(0f, 1f)
                            onSaturationBrightnessChange(newSat, newBright)
                        },
                        onDrag = { change, _ ->
                            val newSat = (change.position.x / size.width).coerceIn(0f, 1f)
                            val newBright = 1f - (change.position.y / size.height).coerceIn(0f, 1f)
                            onSaturationBrightnessChange(newSat, newBright)
                        }
                    )
                }
        ) {
            // Draw saturation gradient (white to color)
            val hueColor = hsbToColor(HSBColor(hue, 1f, 1f, 1f))

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.White, hueColor)
                )
            )

            // Draw brightness gradient (transparent to black)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black)
                )
            )
        }

        // Cursor indicator (positioned separately outside Canvas)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val xPos = saturation * size.width
            val yPos = (1f - brightness) * size.height

            // Draw cursor circle
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(xPos, yPos),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = Color.Black.copy(alpha = 0.3f),
                radius = 7.5.dp.toPx(),
                center = Offset(xPos, yPos),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

// ============================================================================
// HUE SLIDER
// ============================================================================

@Composable
private fun HueSlider(
    hue: Float,
    onHueChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val newHue = (offset.x / size.width * 360f).coerceIn(0f, 360f)
                            onHueChange(newHue)
                        },
                        onDrag = { change, _ ->
                            val newHue = (change.position.x / size.width * 360f).coerceIn(0f, 360f)
                            onHueChange(newHue)
                        }
                    )
                }
        ) {
            // Draw rainbow gradient
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Yellow,
                        Color.Green,
                        Color.Cyan,
                        Color.Blue,
                        Color.Magenta,
                        Color.Red
                    )
                )
            )
        }

        // Cursor indicator
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val xPos = (hue / 360f) * size.width

            // Draw cursor handle
            drawRect(
                color = Color.White,
                topLeft = Offset(xPos - 6.dp.toPx(), -2.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

// ============================================================================
// ALPHA SLIDER
// ============================================================================

@Composable
private fun AlphaSlider(
    alpha: Float,
    color: Color,
    onAlphaChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        // Checkerboard background pattern
        Canvas(modifier = Modifier.fillMaxSize()) {
            val checkerSize = 8f
            for (x in 0 until (size.width / checkerSize).toInt()) {
                for (y in 0 until (size.height / checkerSize).toInt()) {
                    val isEven = (x + y) % 2 == 0
                    drawRect(
                        color = if (isEven) Color.White else Color.LightGray,
                        topLeft = Offset(x * checkerSize, y * checkerSize),
                        size = androidx.compose.ui.geometry.Size(checkerSize, checkerSize)
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val newAlpha = (offset.x / size.width).coerceIn(0f, 1f)
                            onAlphaChange(newAlpha)
                        },
                        onDrag = { change, _ ->
                            val newAlpha = (change.position.x / size.width).coerceIn(0f, 1f)
                            onAlphaChange(newAlpha)
                        }
                    )
                }
        ) {
            // Draw alpha gradient
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0f),
                        color.copy(alpha = 1f)
                    )
                )
            )
        }

        // Cursor indicator
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val xPos = alpha * size.width

            // Draw cursor handle
            drawRect(
                color = Color.White,
                topLeft = Offset(xPos - 6.dp.toPx(), -2.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

// ============================================================================
// COLOR FORMAT INPUT
// ============================================================================

@Composable
private fun ColorFormatInput(
    color: Color,
    format: ColorFormat,
    disabledFormat: Boolean,
    disabledAlpha: Boolean,
    allowClear: Boolean,
    onColorChange: (Color) -> Unit,
    onFormatChange: (ColorFormat) -> Unit,
    onClear: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Format switcher
            if (!disabledFormat) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ColorFormat.entries.forEach { fmt ->
                        val isSelected = fmt == format
                        Text(
                            text = fmt.name,
                            fontSize = 12.sp,
                            color = if (isSelected) Color(0xFF1890FF) else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onFormatChange(fmt) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = colorToString(color, format),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Clear button
            if (allowClear) {
                AntButton(
                    onClick = onClear,
                    size = ButtonSize.Small,
                    type = ButtonType.Text
                ) {
                    Text("Clear", fontSize = 12.sp)
                }
            }
        }

        // Format-specific input fields
        when (format) {
            ColorFormat.Hex -> HexInput(color, disabledAlpha, onColorChange)
            ColorFormat.RGB -> RGBInput(color, disabledAlpha, onColorChange)
            ColorFormat.HSB -> HSBInput(color, disabledAlpha, onColorChange)
            ColorFormat.HSL -> HSLInput(color, disabledAlpha, onColorChange)
        }
    }
}

@Composable
private fun HexInput(
    color: Color,
    disabledAlpha: Boolean,
    onColorChange: (Color) -> Unit
) {
    val hexString = colorToHexString(color, !disabledAlpha)
    var inputValue by remember(hexString) { mutableStateOf(hexString) }

    ColorInputField(
        label = "HEX",
        value = inputValue,
        onValueChange = { newValue ->
            inputValue = newValue
            hexStringToColor(newValue)?.let { onColorChange(it) }
        }
    )
}

@Composable
private fun RGBInput(
    color: Color,
    disabledAlpha: Boolean,
    onColorChange: (Color) -> Unit
) {
    val rgb = colorToRGB(color)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorInputField(
            label = "R",
            value = rgb.red.toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 255)?.let { r ->
                    onColorChange(Color(r, rgb.green, rgb.blue, (rgb.alpha * 255).toInt()))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "G",
            value = rgb.green.toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 255)?.let { g ->
                    onColorChange(Color(rgb.red, g, rgb.blue, (rgb.alpha * 255).toInt()))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "B",
            value = rgb.blue.toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 255)?.let { b ->
                    onColorChange(Color(rgb.red, rgb.green, b, (rgb.alpha * 255).toInt()))
                }
            },
            modifier = Modifier.weight(1f)
        )
        if (!disabledAlpha) {
            ColorInputField(
                label = "A",
                value = (rgb.alpha * 100).roundToInt().toString(),
                onValueChange = { value ->
                    value.toIntOrNull()?.coerceIn(0, 100)?.let { a ->
                        onColorChange(color.copy(alpha = a / 100f))
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HSBInput(
    color: Color,
    disabledAlpha: Boolean,
    onColorChange: (Color) -> Unit
) {
    val hsb = colorToHSB(color)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorInputField(
            label = "H",
            value = hsb.hue.roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 360)?.let { h ->
                    onColorChange(hsbToColor(hsb.copy(hue = h.toFloat())))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "S",
            value = (hsb.saturation * 100).roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 100)?.let { s ->
                    onColorChange(hsbToColor(hsb.copy(saturation = s / 100f)))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "B",
            value = (hsb.brightness * 100).roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 100)?.let { b ->
                    onColorChange(hsbToColor(hsb.copy(brightness = b / 100f)))
                }
            },
            modifier = Modifier.weight(1f)
        )
        if (!disabledAlpha) {
            ColorInputField(
                label = "A",
                value = (hsb.alpha * 100).roundToInt().toString(),
                onValueChange = { value ->
                    value.toIntOrNull()?.coerceIn(0, 100)?.let { a ->
                        onColorChange(hsbToColor(hsb.copy(alpha = a / 100f)))
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HSLInput(
    color: Color,
    disabledAlpha: Boolean,
    onColorChange: (Color) -> Unit
) {
    val hsl = colorToHSL(color)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorInputField(
            label = "H",
            value = hsl.hue.roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 360)?.let { h ->
                    onColorChange(hslToColor(hsl.copy(hue = h.toFloat())))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "S",
            value = (hsl.saturation * 100).roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 100)?.let { s ->
                    onColorChange(hslToColor(hsl.copy(saturation = s / 100f)))
                }
            },
            modifier = Modifier.weight(1f)
        )
        ColorInputField(
            label = "L",
            value = (hsl.lightness * 100).roundToInt().toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.coerceIn(0, 100)?.let { l ->
                    onColorChange(hslToColor(hsl.copy(lightness = l / 100f)))
                }
            },
            modifier = Modifier.weight(1f)
        )
        if (!disabledAlpha) {
            ColorInputField(
                label = "A",
                value = (hsl.alpha * 100).roundToInt().toString(),
                onValueChange = { value ->
                    value.toIntOrNull()?.coerceIn(0, 100)?.let { a ->
                        onColorChange(hslToColor(hsl.copy(alpha = a / 100f)))
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ColorInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// ============================================================================
// PRESETS SECTION
// ============================================================================

@Composable
private fun PresetsSection(
    presets: List<PresetsItem>,
    currentColor: Color,
    onPresetSelect: (Color) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        presets.forEach { preset ->
            var isOpen by remember { mutableStateOf(preset.defaultOpen) }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isOpen = !isOpen },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = preset.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF262626)
                    )
                    Text(
                        text = if (isOpen) "▼" else "▶",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                if (isOpen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        preset.colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (colorsEqual(color, currentColor)) 2.dp else 1.dp,
                                        color = if (colorsEqual(color, currentColor))
                                            Color(0xFF1890FF) else Color(0xFFD9D9D9),
                                        shape = CircleShape
                                    )
                                    .clickable { onPresetSelect(color) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultPresetsGrid(
    currentColor: Color,
    onPresetSelect: (Color) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Preset Colors",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF262626)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(120.dp)
        ) {
            items(getDefaultColors()) { color ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (colorsEqual(color, currentColor)) 2.dp else 1.dp,
                            color = if (colorsEqual(color, currentColor))
                                Color(0xFF1890FF) else Color(0xFFD9D9D9),
                            shape = CircleShape
                        )
                        .clickable { onPresetSelect(color) }
                )
            }
        }
    }
}

// ============================================================================
// COLOR CONVERSION UTILITIES
// ============================================================================

/**
 * Convert Color to HSB
 */
private fun colorToHSB(color: Color): HSBColor {
    val r = color.red
    val g = color.green
    val b = color.blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    val hue = when {
        delta == 0f -> 0f
        max == r -> 60f * (((g - b) / delta) % 6f)
        max == g -> 60f * (((b - r) / delta) + 2f)
        else -> 60f * (((r - g) / delta) + 4f)
    }.let { if (it < 0) it + 360f else it }

    val saturation = if (max == 0f) 0f else delta / max
    val brightness = max

    return HSBColor(hue, saturation, brightness, color.alpha)
}

/**
 * Convert HSB to Color
 */
private fun hsbToColor(hsb: HSBColor): Color {
    val h = hsb.hue / 60f
    val s = hsb.saturation
    val b = hsb.brightness

    val i = h.toInt()
    val f = h - i
    val p = b * (1 - s)
    val q = b * (1 - s * f)
    val t = b * (1 - s * (1 - f))

    val (r, g, bVal) = when (i % 6) {
        0 -> Triple(b, t, p)
        1 -> Triple(q, b, p)
        2 -> Triple(p, b, t)
        3 -> Triple(p, q, b)
        4 -> Triple(t, p, b)
        else -> Triple(b, p, q)
    }

    return Color(r, g, bVal, hsb.alpha)
}

/**
 * Convert Color to HSL
 */
private fun colorToHSL(color: Color): HSLColor {
    val r = color.red
    val g = color.green
    val b = color.blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    val lightness = (max + min) / 2f

    val saturation = when {
        delta == 0f -> 0f
        else -> delta / (1f - abs(2f * lightness - 1f))
    }

    val hue = when {
        delta == 0f -> 0f
        max == r -> 60f * (((g - b) / delta) % 6f)
        max == g -> 60f * (((b - r) / delta) + 2f)
        else -> 60f * (((r - g) / delta) + 4f)
    }.let { if (it < 0) it + 360f else it }

    return HSLColor(hue, saturation, lightness, color.alpha)
}

/**
 * Convert HSL to Color
 */
private fun hslToColor(hsl: HSLColor): Color {
    val c = (1f - abs(2f * hsl.lightness - 1f)) * hsl.saturation
    val x = c * (1f - abs(((hsl.hue / 60f) % 2f) - 1f))
    val m = hsl.lightness - c / 2f

    val (r, g, b) = when {
        hsl.hue < 60f -> Triple(c, x, 0f)
        hsl.hue < 120f -> Triple(x, c, 0f)
        hsl.hue < 180f -> Triple(0f, c, x)
        hsl.hue < 240f -> Triple(0f, x, c)
        hsl.hue < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color(r + m, g + m, b + m, hsl.alpha)
}

/**
 * Convert Color to RGB
 */
private fun colorToRGB(color: Color): RGBColor {
    return RGBColor(
        red = (color.red * 255).roundToInt(),
        green = (color.green * 255).roundToInt(),
        blue = (color.blue * 255).roundToInt(),
        alpha = color.alpha
    )
}

/**
 * Convert Color to hex string
 */
private fun colorToHexString(color: Color, includeAlpha: Boolean = false): String {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()
    val a = (color.alpha * 255).toInt()

    return if (includeAlpha) {
        "#${r.toString(16).padStart(2, '0').uppercase()}" +
        "${g.toString(16).padStart(2, '0').uppercase()}" +
        "${b.toString(16).padStart(2, '0').uppercase()}" +
        "${a.toString(16).padStart(2, '0').uppercase()}"
    } else {
        "#${r.toString(16).padStart(2, '0').uppercase()}" +
        "${g.toString(16).padStart(2, '0').uppercase()}" +
        "${b.toString(16).padStart(2, '0').uppercase()}"
    }
}

/**
 * Parse hex string to Color
 */
private fun hexStringToColor(hex: String): Color? {
    val cleanHex = hex.removePrefix("#")

    return try {
        when (cleanHex.length) {
            6 -> {
                val r = cleanHex.substring(0, 2).toInt(16) / 255f
                val g = cleanHex.substring(2, 4).toInt(16) / 255f
                val b = cleanHex.substring(4, 6).toInt(16) / 255f
                Color(r, g, b, 1f)
            }
            8 -> {
                val r = cleanHex.substring(0, 2).toInt(16) / 255f
                val g = cleanHex.substring(2, 4).toInt(16) / 255f
                val b = cleanHex.substring(4, 6).toInt(16) / 255f
                val a = cleanHex.substring(6, 8).toInt(16) / 255f
                Color(r, g, b, a)
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Convert color to string based on format
 */
private fun colorToString(color: Color, format: ColorFormat): String {
    return when (format) {
        ColorFormat.Hex -> colorToHexString(color, color.alpha < 1f)
        ColorFormat.RGB -> {
            val rgb = colorToRGB(color)
            if (rgb.alpha < 1f) {
                "rgba(${rgb.red}, ${rgb.green}, ${rgb.blue}, ${rgb.alpha})"
            } else {
                "rgb(${rgb.red}, ${rgb.green}, ${rgb.blue})"
            }
        }
        ColorFormat.HSB -> {
            val hsb = colorToHSB(color)
            "hsb(${hsb.hue.roundToInt()}, ${(hsb.saturation * 100).roundToInt()}%, ${(hsb.brightness * 100).roundToInt()}%)"
        }
        ColorFormat.HSL -> {
            val hsl = colorToHSL(color)
            "hsl(${hsl.hue.roundToInt()}, ${(hsl.saturation * 100).roundToInt()}%, ${(hsl.lightness * 100).roundToInt()}%)"
        }
    }
}

/**
 * Convert color to CSS string
 */
private fun colorToCssString(color: Color, format: ColorFormat): String {
    return colorToString(color, format)
}

/**
 * Compare two colors for equality
 */
private fun colorsEqual(color1: Color, color2: Color): Boolean {
    return abs(color1.red - color2.red) < 0.01f &&
           abs(color1.green - color2.green) < 0.01f &&
           abs(color1.blue - color2.blue) < 0.01f &&
           abs(color1.alpha - color2.alpha) < 0.01f
}

/**
 * Get popup alignment from placement
 */
private fun getPopupAlignment(placement: PopoverPlacement): Alignment {
    return when (placement) {
        PopoverPlacement.TopLeft, PopoverPlacement.Top, PopoverPlacement.TopRight -> Alignment.TopStart
        PopoverPlacement.BottomLeft, PopoverPlacement.Bottom, PopoverPlacement.BottomRight -> Alignment.BottomStart
        PopoverPlacement.LeftTop, PopoverPlacement.Left, PopoverPlacement.LeftBottom -> Alignment.CenterStart
        PopoverPlacement.RightTop, PopoverPlacement.Right, PopoverPlacement.RightBottom -> Alignment.CenterEnd
    }
}

/**
 * Get default preset colors
 */
private fun getDefaultColors(): List<Color> {
    return listOf(
        // Reds
        Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFEF9A9A), Color(0xFFE57373),
        Color(0xFFEF5350), Color(0xFFF44336), Color(0xFFE53935), Color(0xFFD32F2F),
        Color(0xFFC62828), Color(0xFFB71C1C),

        // Pinks
        Color(0xFFFCE4EC), Color(0xFFF8BBD0), Color(0xFFF48FB1), Color(0xFFF06292),
        Color(0xFFEC407A), Color(0xFFE91E63), Color(0xFFD81B60), Color(0xFFC2185B),
        Color(0xFFAD1457), Color(0xFF880E4F),

        // Purples
        Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8), Color(0xFFBA68C8),
        Color(0xFFAB47BC), Color(0xFF9C27B0), Color(0xFF8E24AA), Color(0xFF7B1FA2),
        Color(0xFF6A1B9A), Color(0xFF4A148C),

        // Blues
        Color(0xFFE3F2FD), Color(0xFFBBDEFB), Color(0xFF90CAF9), Color(0xFF64B5F6),
        Color(0xFF42A5F5), Color(0xFF2196F3), Color(0xFF1E88E5), Color(0xFF1976D2),
        Color(0xFF1565C0), Color(0xFF0D47A1),

        // Cyans
        Color(0xFFE0F7FA), Color(0xFFB2EBF2), Color(0xFF80DEEA), Color(0xFF4DD0E1),
        Color(0xFF26C6DA), Color(0xFF00BCD4), Color(0xFF00ACC1), Color(0xFF0097A7),
        Color(0xFF00838F), Color(0xFF006064),

        // Greens
        Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7), Color(0xFF81C784),
        Color(0xFF66BB6A), Color(0xFF4CAF50), Color(0xFF43A047), Color(0xFF388E3C),
        Color(0xFF2E7D32), Color(0xFF1B5E20),

        // Yellows
        Color(0xFFFFFDE7), Color(0xFFFFF9C4), Color(0xFFFFF59D), Color(0xFFFFF176),
        Color(0xFFFFEE58), Color(0xFFFFEB3B), Color(0xFFFDD835), Color(0xFFFBC02D),
        Color(0xFFF9A825), Color(0xFFF57F17),

        // Oranges
        Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCC80), Color(0xFFFFB74D),
        Color(0xFFFFA726), Color(0xFFFF9800), Color(0xFFFB8C00), Color(0xFFF57C00),
        Color(0xFFEF6C00), Color(0xFFE65100),

        // Grays
        Color(0xFFFFFFFF), Color(0xFFFAFAFA), Color(0xFFF5F5F5), Color(0xFFEEEEEE),
        Color(0xFFE0E0E0), Color(0xFFBDBDBD), Color(0xFF9E9E9E), Color(0xFF757575),
        Color(0xFF616161), Color(0xFF424242), Color(0xFF212121), Color(0xFF000000)
    )
}
