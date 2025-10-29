package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * # Ant Design Slider Component
 *
 * A Slider component for displaying current value and intervals in range.
 * Used to input a value within a specified range with full React Ant Design v5.x parity.
 *
 * ## Features
 * - Single value and range (dual handle) modes
 * - Horizontal and vertical orientations
 * - Marks with labels and custom styling
 * - Dots for step values
 * - Tooltip with custom formatting
 * - Keyboard navigation (arrow keys, home, end, page up/down)
 * - Disabled state
 * - Reverse direction
 * - Controlled/uncontrolled patterns
 * - Custom styling for all components (handle, track, rail, dots, marks)
 * - Semantic class names and styles (v5.10.0+)
 *
 * ## Basic Usage
 * ```kotlin
 * // Single value slider
 * var value by remember { mutableStateOf(30.0) }
 * AntSlider(
 *     value = value,
 *     onChange = { value = it as Double }
 * )
 *
 * // Range slider
 * var range by remember { mutableStateOf(Pair(20.0, 80.0)) }
 * AntSlider(
 *     value = range,
 *     onChange = { range = it as Pair<Double, Double> },
 *     range = true
 * )
 *
 * // With marks
 * AntSlider(
 *     value = value,
 *     onChange = { value = it as Double },
 *     marks = mapOf(
 *         0.0 to "0°C",
 *         26.0 to "26°C",
 *         37.0 to "37°C",
 *         100.0 to "100°C"
 *     )
 * )
 *
 * // Vertical slider
 * AntSlider(
 *     value = value,
 *     onChange = { value = it as Double },
 *     vertical = true
 * )
 * ```
 *
 * @param value Current value (Double for single, Pair<Double, Double> for range)
 * @param onChange Callback fired when user changes slider value (during drag)
 * @param modifier Modifier for the slider container
 * @param defaultValue Default value for uncontrolled mode
 * @param onChangeComplete Callback fired when mouseup or keyup is fired (drag/interaction complete)
 * @param min Minimum value the slider can slide to
 * @param max Maximum value the slider can slide to
 * @param step Step increment (null for continuous sliding)
 * @param disabled If true, the slider will not be interactive
 * @param range Enable dual handle mode for range selection
 * @param reverse Reverse the component (max on left/bottom)
 * @param vertical If true, the slider will be vertical
 * @param marks Tick marks with labels (Map<Double, String> or Map<Double, MarkConfig>)
 * @param included Takes effect when marks is not null, colors track segments between marks
 * @param dots Whether to show dots at step positions
 * @param tooltip Tooltip configuration for value display
 * @param keyboard Support using keyboard to move handlers
 * @param autoFocus Whether to get focus when component is mounted
 * @param handleStyle Custom style for slider handles
 * @param trackStyle Custom style for active track
 * @param railStyle Custom style for rail (background track)
 * @param dotStyle Custom style for dots
 * @param activeDotStyle Custom style for active dots (within range)
 * @param classNames Semantic structure class names (v5.10.0+)
 * @param styles Semantic structure styles (v5.10.0+)
 * @param draggableTrack Whether range track can be dragged (range mode only)
 *
 * @since 1.0.0
 */
@Composable
fun AntSlider(
    value: Any? = null,
    onChange: ((Any) -> Unit)? = null,
    modifier: Modifier = Modifier,
    defaultValue: Any? = null,
    onChangeComplete: ((Any) -> Unit)? = null,
    min: Double = 0.0,
    max: Double = 100.0,
    step: Double? = 1.0,
    disabled: Boolean = false,
    range: Boolean = false,
    reverse: Boolean = false,
    vertical: Boolean = false,
    marks: Map<Double, Any>? = null,
    included: Boolean = true,
    dots: Boolean = false,
    tooltip: TooltipConfig = TooltipConfig(),
    keyboard: Boolean = true,
    autoFocus: Boolean = false,
    handleStyle: Modifier? = null,
    trackStyle: Modifier? = null,
    railStyle: Modifier? = null,
    dotStyle: Modifier? = null,
    activeDotStyle: Modifier? = null,
    classNames: SliderClassNames? = null,
    styles: SliderStyles? = null,
    draggableTrack: Boolean = false,
) {
    val theme = useTheme()

    // State management for controlled/uncontrolled pattern
    val internalValue = remember(value, defaultValue) {
        when {
            value != null -> value
            defaultValue != null -> defaultValue
            range -> Pair(min, max)
            else -> min
        }
    }

    var currentValue by remember(internalValue) {
        mutableStateOf(internalValue)
    }

    // Sync with controlled value
    LaunchedEffect(value) {
        if (value != null) {
            currentValue = value
        }
    }

    // Validate and normalize value
    val normalizedValue = remember(currentValue, min, max, range) {
        when {
            range -> {
                val pair = currentValue as? Pair<*, *>
                val start = (pair?.first as? Number)?.toDouble()?.coerceIn(min, max) ?: min
                val end = (pair?.second as? Number)?.toDouble()?.coerceIn(min, max) ?: max
                Pair(start.coerceAtMost(end), end.coerceAtLeast(start))
            }

            else -> {
                ((currentValue as? Number)?.toDouble() ?: min).coerceIn(min, max)
            }
        }
    }

    // Focus management
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var activeHandleIndex by remember { mutableStateOf(0) } // 0 for single/start, 1 for end

    LaunchedEffect(autoFocus) {
        if (autoFocus && !disabled) {
            focusRequester.requestFocus()
        }
    }

    // Tooltip state
    var showTooltip by remember { mutableStateOf(tooltip.open == true) }
    var hoveredHandle by remember { mutableStateOf<Int?>(null) }
    var isDragging by remember { mutableStateOf(false) }

    val shouldShowTooltip = when (tooltip.open) {
        true -> true
        false -> false
        null -> isDragging || hoveredHandle != null
    }

    // Calculate positions
    val density = LocalDensity.current
    var sliderSize by remember { mutableStateOf(0f) }

    val valueToPosition: (Double) -> Float = { v ->
        val normalized = (v - min) / (max - min)
        val position = if (reverse) 1f - normalized else normalized
        (position * sliderSize).toFloat()
    }

    val positionToValue: (Float) -> Double = { pos ->
        val normalized = if (reverse) 1f - (pos / sliderSize) else pos / sliderSize
        val rawValue = min + (normalized.coerceIn(0f, 1f) * (max - min))

        // Apply step snapping
        if (step != null && step > 0) {
            val steppedValue = (rawValue / step).roundToInt() * step
            steppedValue.coerceIn(min, max)
        } else {
            rawValue.coerceIn(min, max)
        }
    }

    // Handle value change
    val handleValueChange: (Any, Boolean) -> Unit = { newValue, completed ->
        currentValue = newValue

        if (onChange != null && !completed) {
            onChange(newValue)
        }

        if (onChangeComplete != null && completed) {
            onChangeComplete(newValue)
        }
    }

    // Keyboard handler
    val handleKeyEvent: (KeyEvent) -> Boolean = { event ->
        if (!disabled && keyboard && event.type == KeyEventType.KeyDown) {
            val largeStep = (max - min) * 0.1 // 10% for page up/down

            when (event.key) {
                Key.DirectionLeft, Key.DirectionDown -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 0) {
                            val newStart = (pair.first - (step ?: 1.0)).coerceAtLeast(min)
                            Pair(newStart, pair.second.coerceAtLeast(newStart))
                        } else {
                            val newEnd = (pair.second - (step ?: 1.0)).coerceAtLeast(min)
                            Pair(pair.first.coerceAtMost(newEnd), newEnd)
                        }
                    } else {
                        ((normalizedValue as Double) - (step ?: 1.0)).coerceAtLeast(min)
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.DirectionRight, Key.DirectionUp -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 0) {
                            val newStart = (pair.first + (step ?: 1.0)).coerceAtMost(max)
                            Pair(newStart, pair.second.coerceAtLeast(newStart))
                        } else {
                            val newEnd = (pair.second + (step ?: 1.0)).coerceAtMost(max)
                            Pair(pair.first.coerceAtMost(newEnd), newEnd)
                        }
                    } else {
                        ((normalizedValue as Double) + (step ?: 1.0)).coerceAtMost(max)
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.Home, Key.MoveHome -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 0) Pair(min, pair.second) else pair
                    } else {
                        min
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.MoveEnd -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 1) Pair(pair.first, max) else pair
                    } else {
                        max
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.PageUp -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 0) {
                            val newStart = (pair.first + largeStep).coerceAtMost(max)
                            Pair(newStart, pair.second.coerceAtLeast(newStart))
                        } else {
                            val newEnd = (pair.second + largeStep).coerceAtMost(max)
                            Pair(pair.first.coerceAtMost(newEnd), newEnd)
                        }
                    } else {
                        ((normalizedValue as Double) + largeStep).coerceAtMost(max)
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.PageDown -> {
                    val newValue = if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        if (activeHandleIndex == 0) {
                            val newStart = (pair.first - largeStep).coerceAtLeast(min)
                            Pair(newStart, pair.second.coerceAtLeast(newStart))
                        } else {
                            val newEnd = (pair.second - largeStep).coerceAtLeast(min)
                            Pair(pair.first.coerceAtMost(newEnd), newEnd)
                        }
                    } else {
                        ((normalizedValue as Double) - largeStep).coerceAtLeast(min)
                    }
                    handleValueChange(newValue, false)
                    true
                }

                Key.Tab -> {
                    if (range && !event.isShiftPressed) {
                        activeHandleIndex = 1 - activeHandleIndex
                        true
                    } else {
                        false
                    }
                }

                else -> false
            }
        } else {
            false
        }
    }

    // Build the slider
    Box(
        modifier = modifier
            .then(
                if (vertical) {
                    Modifier
                        .height(300.dp)
                        .width(IntrinsicSize.Min)
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                }
            )
            .focusRequester(focusRequester)
            .focusable(!disabled)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent(handleKeyEvent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = if (vertical) Alignment.CenterHorizontally else Alignment.Start
        ) {
            // Slider track container
            Box(
                modifier = Modifier.then(
                    if (vertical) {
                        Modifier
                            .fillMaxHeight()
                            .width(12.dp)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                    }
                ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (vertical) {
                                Modifier
                                    .fillMaxHeight()
                                    .width(4.dp)
                            } else {
                                Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            }
                        )
                        .onGloballyPositioned { coordinates ->
                            sliderSize = if (vertical) {
                                coordinates.size.height.toFloat()
                            } else {
                                coordinates.size.width.toFloat()
                            }
                        }
                ) {
                    // Rail (background track)
                    SliderRail(
                        vertical = vertical,
                        disabled = disabled,
                        theme = theme,
                        customStyle = railStyle ?: styles?.rail,
                        classNames = classNames
                    )

                    // Track (active portion)
                    if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        val startPos = valueToPosition(pair.first)
                        val endPos = valueToPosition(pair.second)

                        SliderTrack(
                            vertical = vertical,
                            disabled = disabled,
                            theme = theme,
                            startPosition = startPos,
                            endPosition = endPos,
                            customStyle = trackStyle ?: styles?.track,
                            classNames = classNames,
                            draggable = draggableTrack && !disabled,
                            onDrag = { delta ->
                                if (sliderSize > 0) {
                                    val positionDelta = if (vertical) -delta else delta
                                    val newStart = positionToValue(startPos + positionDelta)
                                    val newEnd = positionToValue(endPos + positionDelta)

                                    // Ensure both values stay in bounds
                                    val range = newEnd - newStart
                                    val adjustedStart = when {
                                        newStart < min -> min
                                        newEnd > max -> max - range
                                        else -> newStart
                                    }
                                    val adjustedEnd = adjustedStart + range

                                    handleValueChange(Pair(adjustedStart, adjustedEnd), false)
                                }
                            },
                            onDragEnd = {
                                val pair = normalizedValue as Pair<Double, Double>
                                handleValueChange(pair, true)
                            }
                        )
                    } else {
                        val pos = valueToPosition(normalizedValue as Double)

                        SliderTrack(
                            vertical = vertical,
                            disabled = disabled,
                            theme = theme,
                            startPosition = if (reverse) pos else 0f,
                            endPosition = if (reverse) sliderSize else pos,
                            customStyle = trackStyle ?: styles?.track,
                            classNames = classNames
                        )
                    }

                    // Dots
                    if (dots && step != null && step > 0) {
                        SliderDots(
                            min = min,
                            max = max,
                            step = step,
                            vertical = vertical,
                            reverse = reverse,
                            disabled = disabled,
                            theme = theme,
                            activeRange = if (range) {
                                val pair = normalizedValue as Pair<Double, Double>
                                pair.first..pair.second
                            } else {
                                if (reverse) {
                                    (normalizedValue as Double)..max
                                } else {
                                    min..(normalizedValue as Double)
                                }
                            },
                            valueToPosition = valueToPosition,
                            customDotStyle = dotStyle ?: styles?.dot,
                            customActiveDotStyle = activeDotStyle,
                            classNames = classNames
                        )
                    }

                    // Marks
                    if (marks != null) {
                        SliderMarks(
                            marks = marks,
                            min = min,
                            max = max,
                            vertical = vertical,
                            reverse = reverse,
                            disabled = disabled,
                            theme = theme,
                            valueToPosition = valueToPosition,
                            activeRange = if (included) {
                                if (range) {
                                    val pair = normalizedValue as Pair<Double, Double>
                                    pair.first..pair.second
                                } else {
                                    if (reverse) {
                                        (normalizedValue as Double)..max
                                    } else {
                                        min..(normalizedValue as Double)
                                    }
                                }
                            } else null,
                            classNames = classNames,
                            customStyle = styles?.mark
                        )
                    }

                    // Handles
                    if (range) {
                        val pair = normalizedValue as Pair<Double, Double>
                        val startPos = valueToPosition(pair.first)
                        val endPos = valueToPosition(pair.second)

                        // Start handle
                        SliderHandle(
                            position = startPos,
                            value = pair.first,
                            vertical = vertical,
                            disabled = disabled,
                            theme = theme,
                            isFocused = isFocused && activeHandleIndex == 0,
                            tooltip = tooltip,
                            showTooltip = shouldShowTooltip && (hoveredHandle == 0 || isDragging),
                            customStyle = handleStyle ?: styles?.handle,
                            classNames = classNames,
                            onDragStart = {
                                isDragging = true
                                activeHandleIndex = 0
                            },
                            onDrag = { delta ->
                                if (sliderSize > 0) {
                                    val positionDelta = if (vertical) -delta else delta
                                    val newStart = positionToValue(startPos + positionDelta)
                                    handleValueChange(
                                        Pair(newStart, pair.second.coerceAtLeast(newStart)),
                                        false
                                    )
                                }
                            },
                            onDragEnd = {
                                isDragging = false
                                handleValueChange(normalizedValue, true)
                            },
                            onHoverChange = { hovering ->
                                hoveredHandle = if (hovering) 0 else null
                            }
                        )

                        // End handle
                        SliderHandle(
                            position = endPos,
                            value = pair.second,
                            vertical = vertical,
                            disabled = disabled,
                            theme = theme,
                            isFocused = isFocused && activeHandleIndex == 1,
                            tooltip = tooltip,
                            showTooltip = shouldShowTooltip && (hoveredHandle == 1 || isDragging),
                            customStyle = handleStyle ?: styles?.handle,
                            classNames = classNames,
                            onDragStart = {
                                isDragging = true
                                activeHandleIndex = 1
                            },
                            onDrag = { delta ->
                                if (sliderSize > 0) {
                                    val positionDelta = if (vertical) -delta else delta
                                    val newEnd = positionToValue(endPos + positionDelta)
                                    handleValueChange(
                                        Pair(pair.first.coerceAtMost(newEnd), newEnd),
                                        false
                                    )
                                }
                            },
                            onDragEnd = {
                                isDragging = false
                                handleValueChange(normalizedValue, true)
                            },
                            onHoverChange = { hovering ->
                                hoveredHandle = if (hovering) 1 else null
                            }
                        )
                    } else {
                        val pos = valueToPosition(normalizedValue as Double)

                        SliderHandle(
                            position = pos,
                            value = normalizedValue,
                            vertical = vertical,
                            disabled = disabled,
                            theme = theme,
                            isFocused = isFocused,
                            tooltip = tooltip,
                            showTooltip = shouldShowTooltip,
                            customStyle = handleStyle ?: styles?.handle,
                            classNames = classNames,
                            onDragStart = {
                                isDragging = true
                            },
                            onDrag = { delta ->
                                if (sliderSize > 0) {
                                    val positionDelta = if (vertical) -delta else delta
                                    val newValue = positionToValue(pos + positionDelta)
                                    handleValueChange(newValue, false)
                                }
                            },
                            onDragEnd = {
                                isDragging = false
                                handleValueChange(normalizedValue, true)
                            },
                            onHoverChange = { hovering ->
                                hoveredHandle = if (hovering) 0 else null
                            }
                        )
                    }

                    // Click to position
                    if (!disabled) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        val clickPos = if (vertical) {
                                            offset.y
                                        } else {
                                            offset.x
                                        }

                                        val clickValue = positionToValue(clickPos)

                                        if (range) {
                                            val pair = normalizedValue as Pair<Double, Double>
                                            val distToStart = abs(clickValue - pair.first)
                                            val distToEnd = abs(clickValue - pair.second)

                                            // Move the closest handle
                                            if (distToStart < distToEnd) {
                                                activeHandleIndex = 0
                                                handleValueChange(
                                                    Pair(clickValue, pair.second.coerceAtLeast(clickValue)),
                                                    true
                                                )
                                            } else {
                                                activeHandleIndex = 1
                                                handleValueChange(
                                                    Pair(pair.first.coerceAtMost(clickValue), clickValue),
                                                    true
                                                )
                                            }
                                        } else {
                                            handleValueChange(clickValue, true)
                                        }

                                        focusRequester.requestFocus()
                                    }
                                }
                        )
                    }
                }
            }

            // Mark labels (rendered below for horizontal, to the right for vertical)
            if (marks != null) {
                Spacer(modifier = Modifier.height(8.dp))

                if (!vertical) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        marks.entries.sortedBy { it.key }.forEach { (value, label) ->
                            val pos = valueToPosition(value)

                            Box(
                                modifier = Modifier
                                    .offset {
                                        IntOffset((pos - 10).roundToInt(), 0)
                                    }
                                    .width(20.dp)
                            ) {
                                when (label) {
                                    is String -> {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            color = if (disabled) {
                                                ColorPalette.gray6
                                            } else {
                                                ColorPalette.gray7
                                            },
                                            textAlign = TextAlign.Center,
                                            modifier = (styles?.markText ?: Modifier)
                                                .fillMaxWidth()
                                        )
                                    }

                                    is MarkConfig -> {
                                        when (val markLabel = label.label) {
                                            is String -> {
                                                Text(
                                                    text = markLabel,
                                                    fontSize = 12.sp,
                                                    color = if (disabled) {
                                                        ColorPalette.gray6
                                                    } else {
                                                        ColorPalette.gray7
                                                    },
                                                    textAlign = TextAlign.Center,
                                                    modifier = (label.style ?: styles?.markText ?: Modifier)
                                                        .fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Slider rail component (background track)
 */
@Composable
private fun SliderRail(
    vertical: Boolean,
    disabled: Boolean,
    theme: AntThemeConfig,
    customStyle: Modifier?,
    classNames: SliderClassNames?,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(customStyle ?: Modifier)
            .background(
                color = if (disabled) {
                    ColorPalette.gray4
                } else {
                    ColorPalette.gray5
                },
                shape = RoundedCornerShape(2.dp)
            )
    )
}

/**
 * Slider track component (active/selected portion)
 */
@Composable
private fun SliderTrack(
    vertical: Boolean,
    disabled: Boolean,
    theme: AntThemeConfig,
    startPosition: Float,
    endPosition: Float,
    customStyle: Modifier?,
    classNames: SliderClassNames?,
    draggable: Boolean = false,
    onDrag: ((Float) -> Unit)? = null,
    onDragEnd: (() -> Unit)? = null,
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .then(
                if (vertical) {
                    Modifier
                        .fillMaxWidth()
                        .height(with(density) { (endPosition - startPosition).toDp() })
                        .offset { IntOffset(0, startPosition.roundToInt()) }
                } else {
                    Modifier
                        .fillMaxHeight()
                        .width(with(density) { (endPosition - startPosition).toDp() })
                        .offset { IntOffset(startPosition.roundToInt(), 0) }
                }
            )
            .then(customStyle ?: Modifier)
            .background(
                color = if (disabled) {
                    ColorPalette.gray6
                } else {
                    theme.token.colorPrimary
                },
                shape = RoundedCornerShape(2.dp)
            )
            .then(
                if (draggable && onDrag != null) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { },
                            onDragEnd = { onDragEnd?.invoke() },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val delta = if (vertical) dragAmount.y else dragAmount.x
                                onDrag(delta)
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
    )
}

/**
 * Slider handle component (thumb/knob)
 */
@Composable
private fun SliderHandle(
    position: Float,
    value: Double,
    vertical: Boolean,
    disabled: Boolean,
    theme: AntThemeConfig,
    isFocused: Boolean,
    tooltip: TooltipConfig,
    showTooltip: Boolean,
    customStyle: Modifier?,
    classNames: SliderClassNames?,
    onDragStart: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
    onHoverChange: (Boolean) -> Unit,
) {
    val density = LocalDensity.current
    val handleSize = 14.dp
    val handleOffset = with(density) { (handleSize / 2).toPx() }

    Box(
        modifier = Modifier
            .then(
                if (vertical) {
                    Modifier.offset { IntOffset(0, (position - handleOffset).roundToInt()) }
                } else {
                    Modifier.offset { IntOffset((position - handleOffset).roundToInt(), 0) }
                }
            )
            .size(handleSize)
    ) {
        // Tooltip
        if (showTooltip) {
            SliderTooltip(
                value = value,
                vertical = vertical,
                tooltip = tooltip,
                theme = theme
            )
        }

        // Handle
        Box(
            modifier = Modifier
                .size(handleSize)
                .then(customStyle ?: Modifier)
                .shadow(
                    elevation = if (isFocused) 4.dp else 2.dp,
                    shape = CircleShape
                )
                .background(
                    color = if (disabled) {
                        ColorPalette.gray1
                    } else {
                        Color.White
                    },
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = if (disabled) {
                        ColorPalette.gray6
                    } else if (isFocused) {
                        theme.token.colorPrimary.copy(alpha = 0.5f)
                    } else {
                        theme.token.colorPrimary
                    },
                    shape = CircleShape
                )
                .then(
                    if (!disabled) {
                        Modifier.pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { onDragStart() },
                                onDragEnd = { onDragEnd() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val delta = if (vertical) dragAmount.y else dragAmount.x
                                    onDrag(delta)
                                }
                            )
                        }
                    } else {
                        Modifier
                    }
                )
        )
    }
}

/**
 * Slider tooltip component
 */
@Composable
private fun SliderTooltip(
    value: Double,
    vertical: Boolean,
    tooltip: TooltipConfig,
    theme: AntThemeConfig,
) {
    val displayValue = tooltip.formatter?.invoke(value) ?: value.toString()

    Box(
        modifier = Modifier
            .offset(
                x = when (tooltip.placement) {
                    TooltipPlacement.Left -> (-40).dp
                    TooltipPlacement.Right -> 40.dp
                    else -> 0.dp
                },
                y = when (tooltip.placement) {
                    TooltipPlacement.Top -> (-32).dp
                    TooltipPlacement.Bottom -> 32.dp
                    else -> 0.dp
                }
            )
    ) {
        Surface(
            color = ColorPalette.gray9.copy(alpha = 0.9f),
            shape = RoundedCornerShape(4.dp),
            shadowElevation = 4.dp
        ) {
            Text(
                text = displayValue,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Slider dots component
 */
@Composable
private fun SliderDots(
    min: Double,
    max: Double,
    step: Double,
    vertical: Boolean,
    reverse: Boolean,
    disabled: Boolean,
    theme: AntThemeConfig,
    activeRange: ClosedFloatingPointRange<Double>,
    valueToPosition: (Double) -> Float,
    customDotStyle: Modifier?,
    customActiveDotStyle: Modifier?,
    classNames: SliderClassNames?,
) {
    val dotValues = mutableListOf<Double>()
    var currentValue = min
    while (currentValue <= max) {
        dotValues.add(currentValue)
        currentValue += step
    }

    dotValues.forEach { dotValue ->
        val position = valueToPosition(dotValue)
        val isActive = dotValue in activeRange

        Box(
            modifier = Modifier
                .then(
                    if (vertical) {
                        Modifier.offset { IntOffset(0, (position - 2).roundToInt()) }
                    } else {
                        Modifier.offset { IntOffset((position - 2).roundToInt(), 0) }
                    }
                )
                .size(4.dp)
                .then(
                    if (isActive) {
                        customActiveDotStyle ?: customDotStyle ?: Modifier
                    } else {
                        customDotStyle ?: Modifier
                    }
                )
                .background(
                    color = when {
                        disabled -> ColorPalette.gray5
                        isActive -> theme.token.colorPrimary
                        else -> ColorPalette.gray6
                    },
                    shape = CircleShape
                )
        )
    }
}

/**
 * Slider marks component
 */
@Composable
private fun SliderMarks(
    marks: Map<Double, Any>,
    min: Double,
    max: Double,
    vertical: Boolean,
    reverse: Boolean,
    disabled: Boolean,
    theme: AntThemeConfig,
    valueToPosition: (Double) -> Float,
    activeRange: ClosedFloatingPointRange<Double>?,
    classNames: SliderClassNames?,
    customStyle: Modifier?,
) {
    marks.entries.forEach { (value, label) ->
        val position = valueToPosition(value)
        val isActive = activeRange?.let { value in it } ?: false

        Box(
            modifier = Modifier
                .then(
                    if (vertical) {
                        Modifier.offset { IntOffset(0, (position - 2).roundToInt()) }
                    } else {
                        Modifier.offset { IntOffset((position - 2).roundToInt(), 0) }
                    }
                )
                .size(4.dp)
                .then(customStyle ?: Modifier)
                .background(
                    color = when {
                        disabled -> ColorPalette.gray5
                        isActive -> theme.token.colorPrimary
                        else -> ColorPalette.gray6
                    },
                    shape = CircleShape
                )
        )
    }
}

/**
 * Tooltip configuration for Slider
 *
 * @param open Tooltip visibility control (null = auto show on hover/drag, true = always show, false = never show)
 * @param placement Tooltip placement relative to handle
 * @param getPopupContainer Custom container function (not applicable in Compose)
 * @param formatter Custom formatter for tooltip value display
 */
data class TooltipConfig(
    val open: Boolean? = null,
    val placement: TooltipPlacement = TooltipPlacement.Top,
    val getPopupContainer: (() -> Any)? = null,
    val formatter: ((Double) -> String)? = null,
)

/**
 * Tooltip placement options
 */
enum class TooltipPlacement {
    Top,
    Left,
    Right,
    Bottom
}

/**
 * Mark configuration for custom mark styling
 *
 * @param label Mark label (String or @Composable)
 * @param style Custom style for mark
 */
data class MarkConfig(
    val label: Any,
    val style: Modifier? = null,
)

/**
 * Semantic class names for Slider sub-components (v5.10.0+)
 *
 * @param rail Class name for rail (background track)
 * @param track Class name for track (active portion)
 * @param handle Class name for handle (thumb)
 * @param mark Class name for marks
 * @param markText Class name for mark labels
 * @param dot Class name for dots
 */
data class SliderClassNames(
    val rail: String = "",
    val track: String = "",
    val handle: String = "",
    val mark: String = "",
    val markText: String = "",
    val dot: String = "",
)

/**
 * Semantic styles for Slider sub-components (v5.10.0+)
 *
 * @param rail Style for rail (background track)
 * @param track Style for track (active portion)
 * @param handle Style for handle (thumb)
 * @param mark Style for marks
 * @param markText Style for mark labels
 * @param dot Style for dots
 */
data class SliderStyles(
    val rail: Modifier = Modifier,
    val track: Modifier = Modifier,
    val handle: Modifier = Modifier,
    val mark: Modifier = Modifier,
    val markText: Modifier = Modifier,
    val dot: Modifier = Modifier,
)
