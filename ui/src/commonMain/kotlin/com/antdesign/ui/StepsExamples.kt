package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StepsExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("Ant Design Steps Examples", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        // Example 1: Basic horizontal steps
        BasicHorizontalStepsExample()

        // Example 2: Vertical steps
        VerticalStepsExample()

        // Example 3: Steps with description
        StepsWithDescriptionExample()

        // Example 4: Steps with custom icons
        StepsWithCustomIconsExample()

        // Example 5: Navigation type
        NavigationStepsExample()

        // Example 6: Progress dot style
        ProgressDotStepsExample()

        // Example 7: Steps with error
        StepsWithErrorExample()

        // Example 8: Clickable steps
        ClickableStepsExample()

        // Example 9: Inline steps
        InlineStepsExample()

        // Example 10: Small size steps
        SmallSizeStepsExample()
    }
}

@Composable
private fun BasicHorizontalStepsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Basic Horizontal Steps", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description."
                ),
                StepItem(
                    title = "In Progress",
                    subTitle = "Left 00:00:08",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )
    }
}

@Composable
private fun VerticalStepsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Vertical Steps", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            direction = StepsDirection.Vertical,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description. This is a description."
                ),
                StepItem(
                    title = "In Progress",
                    description = "This is a description. This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )
    }
}

@Composable
private fun StepsWithDescriptionExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Steps with Detailed Description", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            items = listOf(
                StepItem(
                    title = "Login",
                    description = "Enter your credentials to access the system.",
                    subTitle = "Required"
                ),
                StepItem(
                    title = "Verification",
                    description = "Verify your identity through email or SMS.",
                    subTitle = "In progress"
                ),
                StepItem(
                    title = "Done",
                    description = "You are successfully logged in!"
                )
            )
        )
    }
}

@Composable
private fun StepsWithCustomIconsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Steps with Custom Icons", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            items = listOf(
                StepItem(
                    title = "Login",
                    icon = {
                        Text("👤", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    }
                ),
                StepItem(
                    title = "Verification",
                    icon = {
                        Text("🔑", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    }
                ),
                StepItem(
                    title = "Pay",
                    icon = {
                        Text("💳", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    }
                ),
                StepItem(
                    title = "Done",
                    icon = {
                        Text("✅", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    }
                )
            )
        )
    }
}

@Composable
private fun NavigationStepsExample() {
    var current by remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Navigation Steps", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            type = StepsType.Navigation,
            size = StepsSize.Small,
            current = current,
            onChange = { current = it },
            items = listOf(
                StepItem(
                    title = "Step 1",
                    subTitle = "00:00:05",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Step 2",
                    subTitle = "00:01:02",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Step 3",
                    subTitle = "waiting for longlong time",
                    description = "This is a description."
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Click on steps to navigate",
             style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
             color = Color.Gray
        )
    }
}

@Composable
private fun ProgressDotStepsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Progress Dot Style", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            progressDot = true,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description."
                ),
                StepItem(
                    title = "In Progress",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Vertical Progress Dot", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)

        AntSteps(
            current = 2,
            progressDot = true,
            direction = StepsDirection.Vertical,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "You can hover on the dot."
                ),
                StepItem(
                    title = "Finished",
                    description = "You can hover on the dot."
                ),
                StepItem(
                    title = "In Progress",
                    description = "You can hover on the dot."
                ),
                StepItem(
                    title = "Waiting",
                    description = "You can hover on the dot."
                )
            )
        )
    }
}

@Composable
private fun StepsWithErrorExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Steps with Error Status", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            status = StepStatus.Error,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description."
                ),
                StepItem(
                    title = "In Process",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("The current step shows an error icon",
             style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
             color = Color.Gray
        )
    }
}

@Composable
private fun ClickableStepsExample() {
    var current by remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Clickable Steps (onChange)", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = current,
            onChange = { newStep ->
                current = newStep
                println("Step changed to: $newStep")
            },
            items = listOf(
                StepItem(
                    title = "Step 1",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Step 2",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Step 3",
                    description = "This is a description."
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { if (current > 0) current-- },
                disabled = current <= 0
            ) {
                Text("Previous")
            }

            AntButton(
                onClick = { if (current < 2) current++ },
                type = ButtonType.Primary,
                disabled = current >= 2
            ) {
                Text("Next")
            }
        }

        Text("Current step: ${current + 1}",
             style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
             color = Color.Gray
        )
    }
}

@Composable
private fun InlineStepsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Inline Steps (Compact)", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            type = StepsType.Inline,
            current = 1,
            items = listOf(
                StepItem(title = "Login"),
                StepItem(title = "Verification"),
                StepItem(title = "Pay"),
                StepItem(title = "Done")
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("With error status:", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)

        AntSteps(
            type = StepsType.Inline,
            current = 1,
            status = StepStatus.Error,
            items = listOf(
                StepItem(title = "Login"),
                StepItem(title = "Verification"),
                StepItem(title = "Pay"),
                StepItem(title = "Done")
            )
        )
    }
}

@Composable
private fun SmallSizeStepsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Small Size Steps", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        AntSteps(
            current = 1,
            size = StepsSize.Small,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description."
                ),
                StepItem(
                    title = "In Progress",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Vertical Small Size", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)

        AntSteps(
            current = 1,
            size = StepsSize.Small,
            direction = StepsDirection.Vertical,
            items = listOf(
                StepItem(
                    title = "Finished",
                    description = "This is a description."
                ),
                StepItem(
                    title = "In Progress",
                    description = "This is a description."
                ),
                StepItem(
                    title = "Waiting",
                    description = "This is a description."
                )
            )
        )
    }
}

@Composable
fun StepsFeatureComparison() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Feature Completeness vs React Version",
             style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

        FeatureItem("✅", "Basic horizontal steps")
        FeatureItem("✅", "Vertical steps")
        FeatureItem("✅", "Step status (Wait, Process, Finish, Error)")
        FeatureItem("✅", "Automatic status calculation based on current")
        FeatureItem("✅", "Custom step status override")
        FeatureItem("✅", "Step sizes (Default, Small)")
        FeatureItem("✅", "Step with title")
        FeatureItem("✅", "Step with description")
        FeatureItem("✅", "Step with subtitle")
        FeatureItem("✅", "Custom icons")
        FeatureItem("✅", "Progress dot style")
        FeatureItem("✅", "Navigation type")
        FeatureItem("✅", "Inline type")
        FeatureItem("✅", "Label placement (Horizontal, Vertical)")
        FeatureItem("✅", "Clickable steps (onChange)")
        FeatureItem("✅", "Disabled steps")
        FeatureItem("✅", "Connection lines with proper colors")
        FeatureItem("✅", "ConfigProvider integration (theme colors)")
        FeatureItem("✅", "Checkmark icon for finished steps")
        FeatureItem("✅", "Error icon (X) for error steps")
        FeatureItem("✅", "Step numbers for wait/process status")
        FeatureItem("✅", "Responsive design support")
        FeatureItem("⚠️", "Progress percentage (React feature, not implemented)")
        FeatureItem("⚠️", "Custom progress dot renderer (basic progressDot only)")
    }
}

@Composable
private fun FeatureItem(icon: String, feature: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(icon)
        Text(feature, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
    }
}
