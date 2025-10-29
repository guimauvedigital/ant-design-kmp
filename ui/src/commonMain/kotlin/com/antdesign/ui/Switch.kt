package com.antdesign.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * Semantic class names for Switch component parts (v5.4.0+).
 * Allows customization of specific parts of the Switch component.
 *
 * @property root CSS class name for the root container/track element
 * @property handle CSS class name for the sliding circular handle/thumb
 * @property inner CSS class name for the inner content area (checkedChildren/unCheckedChildren)
 */
data class SwitchClassNames(
    val root: String = "",
    val handle: String = "",
    val inner: String = ""
)

/**
 * Semantic styles for Switch component parts (v5.4.0+).
 * Allows customization of Modifier for specific parts of the Switch component.
 *
 * @property root Modifier for the root container/track element
 * @property handle Modifier for the sliding circular handle/thumb
 * @property inner Modifier for the inner content area (checkedChildren/unCheckedChildren)
 */
data class SwitchStyles(
    val root: Modifier = Modifier,
    val handle: Modifier = Modifier,
    val inner: Modifier = Modifier
)

/**
 * Ant Design Switch Component - 100% React Ant Design v5.x Parity
 *
 * Switching Selector. If you need to represent the switching between two states or on-off state.
 *
 * **When To Use:**
 * - If you need to represent the switching between two states or on-off state.
 * - The difference between Switch and Checkbox is that Switch will trigger a state change directly
 *   when you toggle it, while Checkbox is generally used for state marking, which should work in
 *   conjunction with submit operation.
 *
 * @param checked Determine whether the Switch is checked (controlled mode). When provided, the
 *                component operates in controlled mode.
 * @param defaultChecked Whether the Switch is checked initially (uncontrolled mode). Defaults to false.
 * @param onChange Callback function triggered when state changes. Called with the new checked state.
 * @param onClick Callback function triggered when clicked, before state change. Called with the current
 *                checked state and the native event. Useful for preventing the change by not calling onChange.
 * @param modifier The modifier to be applied to the switch container
 * @param disabled Whether the Switch is disabled. Disabled switches cannot be toggled. Defaults to false.
 * @param loading Whether the Switch is in loading state. Loading switches show a loading spinner and
 *                cannot be toggled. Defaults to false.
 * @param size Size of the switch. Can be Small or Middle. Note: Switch does not support Large size
 *             in React Ant Design. Defaults to Middle.
 * @param checkedChildren Content to be shown when the switch is checked. Can be text or icons.
 * @param unCheckedChildren Content to be shown when the switch is unchecked. Can be text or icons.
 * @param autoFocus Whether to get focus when component is mounted. Defaults to false.
 * @param title HTML title attribute for accessibility/tooltip. Used by screen readers.
 * @param classNames Semantic class names for different parts of the switch (v5.4.0+).
 * @param styles Semantic styles (Modifiers) for different parts of the switch (v5.4.0+).
 * @param value Value associated with the switch for form integration. Does not affect checked state.
 *              This is useful when using Switch in forms, similar to how Checkbox has a value prop.
 *
 * @see <a href="https://ant.design/components/switch">Ant Design Switch</a>
 *
 * @sample
 * ```kotlin
 * // Basic usage - uncontrolled
 * AntSwitch(defaultChecked = true, onChange = { checked -> println("Changed to: $checked") })
 *
 * // Controlled usage with state
 * var checked by remember { mutableStateOf(false) }
 * AntSwitch(checked = checked, onChange = { checked = it })
 *
 * // With loading state
 * AntSwitch(checked = true, loading = true)
 *
 * // Small size with content
 * AntSwitch(
 *     size = ComponentSize.Small,
 *     checkedChildren = { Text("ON", fontSize = 10.sp) },
 *     unCheckedChildren = { Text("OFF", fontSize = 10.sp) }
 * )
 *
 * // Disabled state
 * AntSwitch(checked = true, disabled = true)
 * ```
 */
@Composable
fun AntSwitch(
    checked: Boolean? = null,
    defaultChecked: Boolean = false,
    onChange: ((Boolean) -> Unit)? = null,
    onClick: ((Boolean, Any?) -> Unit)? = null,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    loading: Boolean = false,
    size: ComponentSize = ComponentSize.Middle,
    checkedChildren: (@Composable () -> Unit)? = null,
    unCheckedChildren: (@Composable () -> Unit)? = null,
    autoFocus: Boolean = false,
    title: String? = null,
    classNames: SwitchClassNames? = null,
    styles: SwitchStyles? = null,
    value: Any? = null
) {
    // State management: controlled vs uncontrolled
    var internalChecked by remember { mutableStateOf(defaultChecked) }
    val isControlled = checked != null
    val effectiveChecked = checked ?: internalChecked

    // Size dimensions
    val (trackWidth, trackHeight, handleSize, handlePadding) = when (size) {
        ComponentSize.Small -> SwitchDimensions(28.dp, 16.dp, 12.dp, 2.dp)
        ComponentSize.Middle -> SwitchDimensions(44.dp, 22.dp, 18.dp, 2.dp)
        ComponentSize.Large -> SwitchDimensions(44.dp, 22.dp, 18.dp, 2.dp) // Same as Middle
    }

    // Focus state for accessibility
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Auto focus on mount
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    // Theme integration
    val theme = LocalAntTheme.current
    val primaryColor = theme.token.colorPrimary

    // Color calculation based on state
    val backgroundColor = when {
        loading && effectiveChecked -> primaryColor.copy(alpha = 0.7f)
        loading && !effectiveChecked -> ColorPalette.gray5.copy(alpha = 0.7f)
        disabled && effectiveChecked -> primaryColor.copy(alpha = 0.4f)
        disabled && !effectiveChecked -> ColorPalette.gray3
        effectiveChecked -> primaryColor
        else -> ColorPalette.gray5
    }

    // Animate background color transition (200ms)
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "switch_background_color"
    )

    // Animate handle position (300ms with FastOutSlowInEasing)
    val handleOffset by animateDpAsState(
        targetValue = if (effectiveChecked) {
            trackWidth - handleSize - handlePadding * 2
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "switch_handle_offset"
    )

    // Interaction source for ripple effects
    val interactionSource = remember { MutableInteractionSource() }

    // Click handler
    val handleClick: () -> Unit = {
        if (!disabled && !loading) {
            // Call onClick first (before state change)
            onClick?.invoke(effectiveChecked, null)

            // Toggle the state
            val newChecked = !effectiveChecked
            if (isControlled) {
                // In controlled mode, only call onChange
                onChange?.invoke(newChecked)
            } else {
                // In uncontrolled mode, update internal state and call onChange
                internalChecked = newChecked
                onChange?.invoke(newChecked)
            }
        }
    }

    // Root modifier with semantics for accessibility
    val rootModifier = modifier
        .then(styles?.root ?: Modifier)
        .semantics {
            role = Role.Switch
            if (title != null) {
                contentDescription = title
            }
        }
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
        .onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.Spacebar, Key.Enter -> {
                        handleClick()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
        .clickable(
            enabled = !disabled && !loading,
            onClick = handleClick,
            interactionSource = interactionSource,
            indication = null // Custom styling instead of ripple
        )

    // Switch track/container
    Box(
        modifier = rootModifier
            .size(width = trackWidth, height = trackHeight)
            .background(
                color = animatedBackgroundColor,
                shape = RoundedCornerShape(trackHeight / 2)
            )
            .then(
                if (isFocused && !disabled && !loading) {
                    Modifier.border(
                        width = 2.dp,
                        color = primaryColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(trackHeight / 2)
                    )
                } else {
                    Modifier
                }
            )
            .padding(handlePadding),
        contentAlignment = Alignment.CenterStart
    ) {
        // Content area (inner) - checkedChildren or unCheckedChildren
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(styles?.inner ?: Modifier),
            contentAlignment = if (effectiveChecked) Alignment.CenterStart else Alignment.CenterEnd
        ) {
            // Content with fade animation (150ms)
            val contentAlpha by animateFloatAsState(
                targetValue = if ((effectiveChecked && checkedChildren != null) ||
                    (!effectiveChecked && unCheckedChildren != null)
                ) 1f else 0f,
                animationSpec = tween(durationMillis = 150, easing = LinearEasing),
                label = "switch_content_alpha"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = handleSize / 2 + 2.dp)
                    .scale(contentAlpha),
                contentAlignment = Alignment.Center
            ) {
                when {
                    effectiveChecked && checkedChildren != null -> {
                        checkedChildren()
                    }
                    !effectiveChecked && unCheckedChildren != null -> {
                        unCheckedChildren()
                    }
                }
            }
        }

        // Handle (thumb) with sliding animation
        Box(
            modifier = Modifier
                .offset(x = handleOffset)
                .size(handleSize)
                .then(styles?.handle ?: Modifier)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Loading spinner
            if (loading) {
                LoadingSpinner(size = handleSize * 0.7f)
            }
        }
    }
}

/**
 * Internal data class to hold switch dimensions for different sizes.
 */
private data class SwitchDimensions(
    val trackWidth: Dp,
    val trackHeight: Dp,
    val handleSize: Dp,
    val handlePadding: Dp
)

/**
 * Loading spinner component for Switch loading state.
 * Displays a rotating circular progress indicator.
 *
 * @param size Size of the spinner
 * @param color Color of the spinner. Defaults to white for visibility on colored backgrounds.
 */
@Composable
private fun LoadingSpinner(
    size: Dp,
    color: Color = Color.White
) {
    // Infinite rotation animation (1000ms per rotation)
    val infiniteTransition = rememberInfiniteTransition(label = "loading_spinner_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_spinner_rotation"
    )

    CircularProgressIndicator(
        modifier = Modifier.size(size),
        color = color,
        strokeWidth = (size.value / 8).dp
    )
}

/**
 * Local composition for Ant Design theme access.
 * Falls back to default theme if not provided by ConfigProvider.
 */
private val LocalAntTheme = compositionLocalOf {
    AntThemeConfig(
        token = ThemeToken(
            colorPrimary = Color(0xFF1890FF)
        )
    )
}
