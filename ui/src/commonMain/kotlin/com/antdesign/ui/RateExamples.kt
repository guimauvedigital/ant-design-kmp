package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Examples demonstrating all Rate features for Ant Design KMP
 *
 * This file showcases 100% React parity implementation:
 * - Controlled and uncontrolled modes
 * - Half star selection
 * - Custom characters
 * - Tooltips on hover
 * - Keyboard navigation
 * - Focus management
 * - Clear functionality
 * - Disabled state
 * - Custom styling
 */

@Composable
fun RateExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Ant Design Rate Examples - 100% React Parity",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        // Basic usage
        BasicRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Half stars
        HalfStarRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Show tooltips
        TooltipRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Read only (disabled)
        ReadOnlyRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Clear star (allowClear)
        ClearRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom character
        CustomCharacterRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom count
        CustomCountRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Keyboard navigation
        KeyboardNavigationExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Auto focus
        AutoFocusRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Controlled mode
        ControlledRateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Hover change callback
        HoverChangeExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom styling
        CustomStylingExample()
    }
}

@Composable
private fun BasicRateExample() {
    var rating by remember { mutableStateOf(0.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Basic Usage:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it }
        )
        Text("Current rating: $rating", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun HalfStarRateExample() {
    var rating by remember { mutableStateOf(2.5) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Half Star Selection:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            allowHalf = true
        )
        Text("Current rating: $rating", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun TooltipRateExample() {
    var rating by remember { mutableStateOf(3.0) }
    val tooltips = listOf("terrible", "bad", "normal", "good", "wonderful")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("With Tooltips (hover to see):", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            tooltips = tooltips
        )
        Text("Current rating: $rating - ${tooltips.getOrNull((rating.toInt() - 1).coerceAtLeast(0)) ?: "none"}",
             fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun ReadOnlyRateExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Read Only (Disabled):", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = 4.0,
            onChange = null,
            disabled = true
        )
        Text("This rate is read-only", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun ClearRateExample() {
    var rating by remember { mutableStateOf(3.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Clear Star (click same value to clear):", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            allowClear = true
        )
        Text("Current rating: $rating (click the same star to clear)", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun CustomCharacterRateExample() {
    var rating by remember { mutableStateOf(3.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom Character:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        // Heart character
        AntRate(
            value = rating,
            onChange = { rating = it },
            character = { props ->
                Text(
                    text = "♥",
                    fontSize = 24.sp,
                    color = if (props.isActive) Color(0xFFEB2F96) else ColorPalette.gray5
                )
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // A character
        var letterRating by remember { mutableStateOf(2.0) }
        AntRate(
            value = letterRating,
            onChange = { letterRating = it },
            character = { props ->
                Text(
                    text = "A",
                    fontSize = 24.sp,
                    color = if (props.isActive) ColorPalette.blue6 else ColorPalette.gray5
                )
            }
        )

        Text("Hearts: $rating, Letters: $letterRating", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun CustomCountRateExample() {
    var rating by remember { mutableStateOf(5.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom Star Count:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            count = 10
        )
        Text("Current rating: $rating out of 10", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun KeyboardNavigationExample() {
    var rating by remember { mutableStateOf(0.0) }
    var lastKeyEvent by remember { mutableStateOf("none") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Keyboard Navigation:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            allowHalf = true,
            onKeyDown = { event ->
                lastKeyEvent = "Key pressed"
            }
        )
        Text(
            text = "Current rating: $rating\nUse arrow keys (←/→/↑/↓), Home, End, or number keys (1-5)",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text("Last key: $lastKeyEvent", fontSize = 12.sp, color = ColorPalette.gray7)
    }
}

@Composable
private fun AutoFocusRateExample() {
    var rating by remember { mutableStateOf(0.0) }
    var focusState by remember { mutableStateOf("not focused") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Auto Focus & Focus Events:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            autoFocus = false, // Set to true to auto-focus on mount
            onFocus = {
                focusState = "focused"
            },
            onBlur = {
                focusState = "blurred"
            }
        )
        Text("Current rating: $rating", fontSize = 14.sp, color = Color.Gray)
        Text("Focus state: $focusState (click to focus, click outside to blur)", fontSize = 12.sp, color = ColorPalette.gray7)
    }
}

@Composable
private fun ControlledRateExample() {
    var rating by remember { mutableStateOf(3.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Controlled Mode:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { rating = (rating - 1).coerceAtLeast(0.0) },
                disabled = rating == 0.0
            ) {
                Text("-")
            }

            AntRate(
                value = rating,
                onChange = { rating = it }
            )

            AntButton(
                onClick = { rating = (rating + 1).coerceAtMost(5.0) },
                disabled = rating == 5.0
            ) {
                Text("+")
            }
        }

        Text("Current rating: $rating (controlled externally)", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun HoverChangeExample() {
    var rating by remember { mutableStateOf(0.0) }
    var hoverValue by remember { mutableStateOf<Double?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Hover Change Callback:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        AntRate(
            value = rating,
            onChange = { rating = it },
            onHoverChange = { hoverValue = it }
        )
        Text(
            text = "Current rating: $rating\nHovering: ${hoverValue?.toString() ?: "none"}",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun CustomStylingExample() {
    var rating by remember { mutableStateOf(3.0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom Styling:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        // Large stars
        AntRate(
            value = rating,
            onChange = { rating = it },
            styles = RateStyles(
                star = Modifier.size(32.dp)
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Small stars with custom spacing
        AntRate(
            value = rating,
            onChange = { rating = it },
            styles = RateStyles(
                root = Modifier.padding(8.dp),
                star = Modifier.size(16.dp)
            )
        )

        Text("Same value ($rating) with different sizes", fontSize = 14.sp, color = Color.Gray)
    }
}

/**
 * Comparison example showing React vs Compose API
 */
@Composable
fun RateAPIComparisonExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("React vs Compose API Comparison", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

        Text(
            text = """
            React (TypeScript):
            <Rate
              value={rating}
              onChange={(value) => setRating(value)}
              allowHalf
              tooltips={['terrible', 'bad', 'normal', 'good', 'wonderful']}
              onHoverChange={(value) => console.log(value)}
              autoFocus
              onFocus={() => console.log('focused')}
              onBlur={() => console.log('blurred')}
            />

            Compose (Kotlin):
            AntRate(
                value = rating,
                onChange = { rating = it },
                allowHalf = true,
                tooltips = listOf("terrible", "bad", "normal", "good", "wonderful"),
                onHoverChange = { println(it) },
                autoFocus = true,
                onFocus = { println("focused") },
                onBlur = { println("blurred") }
            )
            """.trimIndent(),
            fontSize = 12.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = ColorPalette.gray9
        )
    }
}
