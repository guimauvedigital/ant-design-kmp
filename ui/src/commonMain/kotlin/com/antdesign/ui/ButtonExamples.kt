package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Examples demonstrating all Button features for Ant Design KMP
 * This file showcases:
 * - Wave effect on click
 * - Button.Group with fused borders
 * - Loading with delay
 * - Custom loading icon
 * - Link button (href)
 * - All button types and variants
 */

@Composable
fun ButtonExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Ant Design Button Examples", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        // Basic button types
        BasicButtonsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Loading examples
        LoadingButtonsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Button Group
        ButtonGroupExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Link buttons
        LinkButtonsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Wave effect demo
        WaveEffectExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Sizes
        ButtonSizesExample()
    }
}

@Composable
private fun BasicButtonsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Basic Button Types", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { println("Primary clicked") },
                type = ButtonType.Primary
            ) {
                Text("Primary")
            }

            AntButton(
                onClick = { println("Default clicked") }
            ) {
                Text("Default")
            }

            AntButton(
                onClick = { println("Dashed clicked") },
                type = ButtonType.Dashed
            ) {
                Text("Dashed")
            }

            AntButton(
                onClick = { println("Text clicked") },
                type = ButtonType.Text
            ) {
                Text("Text")
            }

            AntButton(
                onClick = { println("Link clicked") },
                type = ButtonType.Link
            ) {
                Text("Link")
            }
        }

        // Danger buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { println("Danger Primary clicked") },
                type = ButtonType.Primary,
                danger = true
            ) {
                Text("Danger Primary")
            }

            AntButton(
                onClick = { println("Danger Default clicked") },
                danger = true
            ) {
                Text("Danger Default")
            }
        }
    }
}

@Composable
private fun LoadingButtonsExample() {
    var simpleLoading by remember { mutableStateOf(false) }
    var delayedLoading by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Loading States", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Simple loading
            AntButton(
                onClick = {
                    simpleLoading = true
                    // Simulate async operation
                },
                type = ButtonType.Primary,
                loading = if (simpleLoading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Simple Loading")
            }

            // Loading with delay (prevents flash for quick operations)
            AntButton(
                onClick = {
                    delayedLoading = true
                },
                type = ButtonType.Primary,
                loading = if (delayedLoading) ButtonLoading.Simple(delay = 500) else ButtonLoading.None
            ) {
                Text("Loading with 500ms Delay")
            }

            // Custom loading icon
            AntButton(
                onClick = { println("Custom loading") },
                type = ButtonType.Primary,
                loading = ButtonLoading.Custom(
                    icon = {
                        Text("⟳") // Custom icon placeholder
                    }
                )
            ) {
                Text("Custom Loading Icon")
            }

            // Always loading (demo)
            AntButton(
                onClick = { },
                loading = ButtonLoading.Simple()
            ) {
                Text("Always Loading")
            }
        }

        // Reset buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (simpleLoading) {
                AntButton(
                    onClick = { simpleLoading = false },
                    type = ButtonType.Text
                ) {
                    Text("Reset Simple Loading")
                }
            }

            if (delayedLoading) {
                AntButton(
                    onClick = { delayedLoading = false },
                    type = ButtonType.Text
                ) {
                    Text("Reset Delayed Loading")
                }
            }
        }
    }
}

@Composable
private fun ButtonGroupExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Button Groups (Fused Borders)", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        // Basic group
        AntButtonGroup {
            AntButton(onClick = { println("Left") }) {
                Text("Left")
            }
            AntButton(onClick = { println("Middle") }) {
                Text("Middle")
            }
            AntButton(onClick = { println("Right") }) {
                Text("Right")
            }
        }

        // Primary group
        AntButtonGroup {
            AntButton(
                onClick = { println("Option 1") },
                type = ButtonType.Primary
            ) {
                Text("Option 1")
            }
            AntButton(
                onClick = { println("Option 2") },
                type = ButtonType.Primary
            ) {
                Text("Option 2")
            }
            AntButton(
                onClick = { println("Option 3") },
                type = ButtonType.Primary
            ) {
                Text("Option 3")
            }
            AntButton(
                onClick = { println("Option 4") },
                type = ButtonType.Primary
            ) {
                Text("Option 4")
            }
        }

        // Small group
        AntButtonGroup(size = ButtonSize.Small) {
            AntButton(onClick = { println("Small 1") }) {
                Text("Small")
            }
            AntButton(onClick = { println("Small 2") }) {
                Text("Small")
            }
            AntButton(onClick = { println("Small 3") }) {
                Text("Small")
            }
        }
    }
}

@Composable
private fun LinkButtonsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Link Buttons (href support)", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { println("Fallback if href fails") },
                href = "https://ant.design",
                type = ButtonType.Primary
            ) {
                Text("Visit Ant Design")
            }

            AntButton(
                onClick = { println("Fallback") },
                href = "https://github.com",
                type = ButtonType.Link
            ) {
                Text("GitHub Link")
            }
        }

        Text(
            "Note: href navigation uses platform-specific URI handler",
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun WaveEffectExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Wave Effect (Click to see ripple)", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { println("Wave effect!") },
                type = ButtonType.Primary,
                enableWaveEffect = true
            ) {
                Text("Click for Wave")
            }

            AntButton(
                onClick = { println("No wave") },
                type = ButtonType.Primary,
                enableWaveEffect = false
            ) {
                Text("No Wave Effect")
            }

            AntButton(
                onClick = { println("Circle wave") },
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                enableWaveEffect = true
            ) {
                Text("🌊")
            }

            AntButton(
                onClick = { println("Round wave") },
                type = ButtonType.Primary,
                shape = ButtonShape.Round,
                enableWaveEffect = true
            ) {
                Text("Round Wave")
            }
        }

        Text(
            "Note: Text and Link buttons don't show wave effect by default",
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun ButtonSizesExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Button Sizes", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            AntButton(
                onClick = { println("Large") },
                type = ButtonType.Primary,
                size = ButtonSize.Large
            ) {
                Text("Large")
            }

            AntButton(
                onClick = { println("Middle") },
                type = ButtonType.Primary,
                size = ButtonSize.Middle
            ) {
                Text("Middle (Default)")
            }

            AntButton(
                onClick = { println("Small") },
                type = ButtonType.Primary,
                size = ButtonSize.Small
            ) {
                Text("Small")
            }
        }

        // Block button (full width)
        AntButton(
            onClick = { println("Block button") },
            type = ButtonType.Primary,
            block = true
        ) {
            Text("Block Button (Full Width)")
        }
    }
}

/**
 * Comprehensive feature comparison with React version
 */
@Composable
fun ButtonFeatureComparison() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Feature Completeness vs React Version", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

        FeatureItem("✅", "Button types (Primary, Default, Dashed, Link, Text)")
        FeatureItem("✅", "Button variants (Solid, Outlined, Dashed, Text, Link)")
        FeatureItem("✅", "Danger mode")
        FeatureItem("✅", "Ghost mode")
        FeatureItem("✅", "Shapes (Default, Circle, Round)")
        FeatureItem("✅", "Sizes (Small, Middle, Large)")
        FeatureItem("✅", "Block mode (full width)")
        FeatureItem("✅", "Disabled state")
        FeatureItem("✅", "Loading state with delay support")
        FeatureItem("✅", "Custom loading icon")
        FeatureItem("✅", "Icon position (start/end)")
        FeatureItem("✅", "Wave effect (ripple animation)")
        FeatureItem("✅", "Button.Group with fused borders")
        FeatureItem("✅", "href support (link button)")
        FeatureItem("✅", "htmlType (Button, Submit, Reset)")
        FeatureItem("✅", "autoInsertSpace (Chinese chars)")
        FeatureItem("✅", "ConfigProvider integration")
        FeatureItem("✅", "Professional loading spinner")
        FeatureItem("⚠️", "Two Chinese chars auto spacing (parameter added, logic TODO)")
    }
}

@Composable
private fun FeatureItem(icon: String, feature: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(icon)
        Text(feature, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
    }
}
