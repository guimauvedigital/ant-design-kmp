package digital.guimauve.antdesign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Comprehensive examples for Ant Design Alert Component
 *
 * Demonstrates 100% feature parity with React version:
 * - All 4 alert types (Success, Info, Warning, Error)
 * - Message & Description (String + Composable support)
 * - Closable with callbacks (onClose, afterClose)
 * - Custom icons & close icons
 * - Action buttons
 * - Banner mode
 * - Mouse events
 * - Semantic styles (AlertStyles)
 * - All 12+ parameters
 */

@Composable
fun AlertExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Ant Design Alert Examples - 100% Complete",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Basic types
        BasicAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // With description
        DescriptionAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Closable alerts
        ClosableAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // With icons
        IconAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom icons
        CustomIconAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // With actions
        ActionAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Banner mode
        BannerAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Advanced features
        AdvancedAlertsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Feature comparison
        AlertFeatureComparison()
    }
}

@Composable
private fun BasicAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "1. Basic Alert Types",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        AntAlert(
            message = "Success Text",
            type = AlertType.Success
        )

        AntAlert(
            message = "Info Text",
            type = AlertType.Info
        )

        AntAlert(
            message = "Warning Text",
            type = AlertType.Warning
        )

        AntAlert(
            message = "Error Text",
            type = AlertType.Error
        )
    }
}

@Composable
private fun DescriptionAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "2. With Description (String + Composable)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // String description
        AntAlert(
            message = "Success Tips",
            description = "Detailed description and advice about successful copywriting. This is a longer text that provides more context.",
            type = AlertType.Success,
            showIcon = true
        )

        // Composable description
        AntAlert(
            message = "Custom Content Alert",
            description = @Composable {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("This is a custom composable description:", fontSize = 12.sp)
                    Text("- Bullet point 1", fontSize = 12.sp, color = Color.Gray)
                    Text("- Bullet point 2", fontSize = 12.sp, color = Color.Gray)
                    Text("- Bullet point 3", fontSize = 12.sp, color = Color.Gray)
                }
            } as Any,
            type = AlertType.Info,
            showIcon = true
        )

        AntAlert(
            message = "Warning Notice",
            description = "This is a warning notice about copywriting. Pay attention to the details.",
            type = AlertType.Warning,
            showIcon = true
        )

        AntAlert(
            message = "Error",
            description = "This is an error message about copywriting. Please fix the issues.",
            type = AlertType.Error,
            showIcon = true
        )
    }
}

@Composable
private fun ClosableAlertsExample() {
    var closeCallbackMessage by remember { mutableStateOf("") }
    var afterCloseMessage by remember { mutableStateOf("") }
    var closeCallbackCount by remember { mutableIntStateOf(0) }
    var afterCloseCount by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "3. Closable Alerts (onClose + afterClose)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Basic closable
        AntAlert(
            message = "Warning Text (closable)",
            type = AlertType.Warning,
            closable = true
        )

        // With onClose callback
        AntAlert(
            message = "Info Text with onClose callback",
            type = AlertType.Info,
            closable = true,
            onClose = {
                closeCallbackCount++
                closeCallbackMessage = "onClose called (#$closeCallbackCount)"
            }
        )

        // With afterClose callback (fired after animation)
        AntAlert(
            message = "Success with afterClose callback",
            type = AlertType.Success,
            closable = true,
            afterClose = {
                afterCloseCount++
                afterCloseMessage = "afterClose called (#$afterCloseCount)"
            }
        )

        // Custom close text
        AntAlert(
            message = "Custom close text",
            type = AlertType.Info,
            closable = true,
            closeText = "Close"
        )

        // Callback status
        if (closeCallbackMessage.isNotEmpty()) {
            Text(
                "onClose: $closeCallbackMessage",
                fontSize = 12.sp,
                color = Color.Blue
            )
        }
        if (afterCloseMessage.isNotEmpty()) {
            Text(
                "afterClose: $afterCloseMessage",
                fontSize = 12.sp,
                color = Color.Green
            )
        }
    }
}

@Composable
private fun IconAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "4. With Default Icons (showIcon = true)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        AntAlert(
            message = "Success Tips",
            type = AlertType.Success,
            showIcon = true
        )

        AntAlert(
            message = "Informational Notes",
            type = AlertType.Info,
            showIcon = true
        )

        AntAlert(
            message = "Warning",
            type = AlertType.Warning,
            showIcon = true
        )

        AntAlert(
            message = "Error",
            type = AlertType.Error,
            showIcon = true,
            description = "Error with description also shows icon"
        )
    }
}

@Composable
private fun CustomIconAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "5. Custom Icons",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        AntAlert(
            message = "Custom smile icon",
            type = AlertType.Success,
            showIcon = true,
            icon = {
                Text("😊", fontSize = 20.sp)
            }
        )

        AntAlert(
            message = "Custom rocket icon",
            type = AlertType.Info,
            showIcon = true,
            icon = {
                Text("🚀", fontSize = 20.sp)
            },
            description = "Launch successful!"
        )

        AntAlert(
            message = "Custom fire icon",
            type = AlertType.Warning,
            showIcon = true,
            icon = {
                Text("🔥", fontSize = 20.sp)
            }
        )

        // Custom close icon
        AntAlert(
            message = "Custom close icon",
            type = AlertType.Info,
            closable = true,
            closeIcon = {
                Text("✖", fontSize = 16.sp, color = Color.Red)
            }
        )
    }
}

@Composable
private fun ActionAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "6. With Action Buttons",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        AntAlert(
            message = "Success Tips",
            type = AlertType.Success,
            showIcon = true,
            closable = true,
            action = {
                AntButton(
                    onClick = { println("UNDO clicked") },
                    type = ButtonType.Text,
                    size = ButtonSize.Small
                ) {
                    Text("UNDO")
                }
            }
        )

        AntAlert(
            message = "Error Text",
            description = "Error Description Error Description Error Description",
            type = AlertType.Error,
            showIcon = true,
            action = {
                AntButton(
                    onClick = { println("Detail clicked") },
                    size = ButtonSize.Small,
                    danger = true
                ) {
                    Text("Detail")
                }
            }
        )

        AntAlert(
            message = "Warning Text",
            type = AlertType.Warning,
            closable = true,
            action = {
                AntButton(
                    onClick = { println("Done clicked") },
                    type = ButtonType.Text,
                    size = ButtonSize.Small
                ) {
                    Text("Done")
                }
            }
        )

        AntAlert(
            message = "Info Text",
            description = "Info Description with multiple actions",
            type = AlertType.Info,
            showIcon = true,
            closable = true,
            action = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AntButton(
                        onClick = { println("Accept") },
                        type = ButtonType.Primary,
                        size = ButtonSize.Small
                    ) {
                        Text("Accept")
                    }
                    AntButton(
                        onClick = { println("Decline") },
                        size = ButtonSize.Small,
                        danger = true
                    ) {
                        Text("Decline")
                    }
                }
            }
        )
    }
}

@Composable
private fun BannerAlertsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "7. Banner Mode (Full Width, No Border)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            "Note: banner = true automatically enables showIcon",
            fontSize = 12.sp,
            color = Color.Gray
        )

        AntAlert(
            message = "Warning: This is a banner alert!",
            type = AlertType.Warning,
            banner = true,
            closable = true
        )

        AntAlert(
            message = "Error banner with description",
            description = "This is an error banner message with additional details.",
            type = AlertType.Error,
            banner = true
        )

        AntAlert(
            message = "Success banner",
            type = AlertType.Success,
            banner = true
        )

        AntAlert(
            message = "Info banner",
            type = AlertType.Info,
            banner = true
        )
    }
}

@Composable
private fun AdvancedAlertsExample() {
    var mouseEnterCount by remember { mutableStateOf(0) }
    var mouseLeaveCount by remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "8. Advanced Features",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Mouse events
        AntAlert(
            message = "Alert with mouse events (hover over me)",
            type = AlertType.Info,
            showIcon = true,
            onMouseEnter = {
                mouseEnterCount++
            },
            onMouseLeave = {
                mouseLeaveCount++
            }
        )

        Text(
            "Mouse enter: $mouseEnterCount, Mouse leave: $mouseLeaveCount",
            fontSize = 12.sp,
            color = Color.DarkGray
        )

        // Semantic styles
        AntAlert(
            message = "Alert with custom semantic styles",
            description = "Using AlertStyles for granular control",
            type = AlertType.Success,
            showIcon = true,
            styles = AlertStyles(
                message = Modifier.padding(4.dp),
                description = Modifier.padding(start = 8.dp)
            )
        )

        // Composable message
        AntAlert(
            message = @Composable {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Custom ", fontWeight = FontWeight.Bold)
                    Text("composable ", color = Color.Blue, fontWeight = FontWeight.Bold)
                    Text("message!", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            } as Any,
            type = AlertType.Warning,
            showIcon = true
        )

        // All features combined
        AntAlert(
            message = "The Ultimate Alert",
            description = "This alert has EVERYTHING: icon, description, action, closable, callbacks, and more!",
            type = AlertType.Success,
            showIcon = true,
            icon = {
                Text("🎉", fontSize = 20.sp)
            },
            closable = true,
            closeIcon = {
                Text("×", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            },
            action = {
                AntButton(
                    onClick = { println("Ultimate action!") },
                    type = ButtonType.Primary,
                    size = ButtonSize.Small
                ) {
                    Text("Action")
                }
            },
            onClose = {
                println("onClose called")
            },
            afterClose = {
                println("afterClose called after animation")
            },
            onMouseEnter = {
                println("Mouse entered")
            },
            onMouseLeave = {
                println("Mouse left")
            },
            styles = AlertStyles(
                root = Modifier.padding(2.dp)
            )
        )
    }
}

/**
 * Feature completeness comparison with React version
 */
@Composable
fun AlertFeatureComparison() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Feature Completeness vs React (100%)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        FeatureItem("✅", "type: Success, Info, Warning, Error")
        FeatureItem("✅", "message: String | Composable")
        FeatureItem("✅", "description: String | Composable | null")
        FeatureItem("✅", "closable: Boolean")
        FeatureItem("✅", "closeText: String?")
        FeatureItem("✅", "closeIcon: Composable?")
        FeatureItem("✅", "showIcon: Boolean (auto-true for banner)")
        FeatureItem("✅", "icon: Composable? (custom icon)")
        FeatureItem("✅", "action: Composable? (action button)")
        FeatureItem("✅", "banner: Boolean (full width, no border)")
        FeatureItem("✅", "onClose: () -> Unit")
        FeatureItem("✅", "afterClose: () -> Unit (after animation)")
        FeatureItem("✅", "onMouseEnter: () -> Unit")
        FeatureItem("✅", "onMouseLeave: () -> Unit")
        FeatureItem("✅", "classNames: AlertClassNames (semantic)")
        FeatureItem("✅", "styles: AlertStyles (semantic)")
        FeatureItem("✅", "Close animation (300ms fade + shrink)")
        FeatureItem("✅", "Default icons per type")
        FeatureItem("✅", "Responsive padding (with/without description)")
        FeatureItem("✅", "Full React parity (100%)")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Total: 20/20 features implemented",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF52C41A)
        )
    }
}

@Composable
private fun FeatureItem(icon: String, feature: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(icon, fontSize = 14.sp)
        Text(feature, style = MaterialTheme.typography.bodyMedium)
    }
}
