package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Comprehensive examples demonstrating all Badge component features
 * Showcases 100% React Ant Design parity implementation
 */
@Composable
fun BadgeExamples() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("Badge Component Examples", fontSize = 24.sp)

        // Basic count badge
        ExampleSection("Basic Count Badge") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(count = 5) {
                    ExampleBox()
                }
                AntBadge(count = 0) {
                    ExampleBox()
                }
                AntBadge(count = 0, showZero = true) {
                    ExampleBox()
                }
            }
        }

        // Overflow count
        ExampleSection("Overflow Count") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(count = 99) {
                    ExampleBox()
                }
                AntBadge(count = 100) {
                    ExampleBox()
                }
                AntBadge(count = 100, overflowCount = 10) {
                    ExampleBox()
                }
                AntBadge(count = 1000, overflowCount = 999) {
                    ExampleBox()
                }
            }
        }

        // Dot badge
        ExampleSection("Dot Badge") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(dot = true) {
                    ExampleBox()
                }
                AntBadge(dot = true, count = 5) {
                    ExampleBox(text = "Dot overrides count")
                }
            }
        }

        // Status badges
        ExampleSection("Status Badges") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AntBadge(status = BadgeStatus.Success, text = "Success")
                AntBadge(status = BadgeStatus.Error, text = "Error")
                AntBadge(status = BadgeStatus.Default, text = "Default")
                AntBadge(status = BadgeStatus.Processing, text = "Processing (animated)")
                AntBadge(status = BadgeStatus.Warning, text = "Warning")
            }
        }

        // Status badges without text
        ExampleSection("Status Badges (No Text)") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(status = BadgeStatus.Success)
                AntBadge(status = BadgeStatus.Error)
                AntBadge(status = BadgeStatus.Default)
                AntBadge(status = BadgeStatus.Processing)
                AntBadge(status = BadgeStatus.Warning)
            }
        }

        // Custom colors
        ExampleSection("Custom Colors") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(count = 5, color = Color(0xFFFF6B9D)) {
                    ExampleBox()
                }
                AntBadge(count = 10, color = Color(0xFF52C41A)) {
                    ExampleBox()
                }
                AntBadge(dot = true, color = Color(0xFF722ED1)) {
                    ExampleBox()
                }
            }
        }

        // Position offset
        ExampleSection("Position Offset") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(count = 5) {
                    ExampleBox()
                }
                AntBadge(count = 5, offset = Pair((-5).dp, 5.dp)) {
                    ExampleBox()
                }
                AntBadge(count = 5, offset = Pair(10.dp, (-10).dp)) {
                    ExampleBox()
                }
            }
        }

        // Size variants
        ExampleSection("Size Variants") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AntBadge(count = 5, size = BadgeSize.Default) {
                        ExampleBox()
                    }
                    Text("Default", fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AntBadge(count = 5, size = BadgeSize.Small) {
                        ExampleBox()
                    }
                    Text("Small", fontSize = 12.sp)
                }
            }
        }

        // Size variants with dot
        ExampleSection("Size Variants (Dot)") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AntBadge(dot = true, size = BadgeSize.Default) {
                        ExampleBox()
                    }
                    Text("Default", fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AntBadge(dot = true, size = BadgeSize.Small) {
                        ExampleBox()
                    }
                    Text("Small", fontSize = 12.sp)
                }
            }
        }

        // Size variants with status
        ExampleSection("Size Variants (Status)") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AntBadge(
                    status = BadgeStatus.Processing,
                    text = "Default size",
                    size = BadgeSize.Default
                )
                AntBadge(
                    status = BadgeStatus.Processing,
                    text = "Small size",
                    size = BadgeSize.Small
                )
            }
        }

        // Dynamic count
        ExampleSection("Dynamic Count") {
            var count by remember { mutableStateOf(5) }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AntBadge(count = count) {
                    ExampleBox()
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(onClick = { count++ }) {
                        Text("+")
                    }
                    AntButton(onClick = { count = maxOf(0, count - 1) }) {
                        Text("-")
                    }
                    AntButton(onClick = { count = 0 }) {
                        Text("Reset")
                    }
                }
            }
        }

        // Processing animation
        ExampleSection("Processing Animation (with dot)") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(
                    dot = true,
                    status = BadgeStatus.Processing
                ) {
                    ExampleBox()
                }
                AntBadge(
                    count = 5,
                    status = BadgeStatus.Processing
                ) {
                    ExampleBox()
                }
            }
        }

        // Custom styles
        ExampleSection("Custom Styles") {
            AntBadge(
                count = 10,
                styles = BadgeStyles(
                    root = Modifier.padding(8.dp),
                    indicator = Modifier.padding(2.dp)
                )
            ) {
                ExampleBox()
            }
        }

        // Standalone badge (no children)
        ExampleSection("Standalone Badge (No Children)") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AntBadge(count = 25, color = Color(0xFF1890FF))
                AntBadge(dot = true, color = Color(0xFFFF4D4F))
            }
        }

        // Complex example: Notification center
        ExampleSection("Complex Example: Notification Center") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntBadge(count = 12, color = Color(0xFF1890FF)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFE6F7FF), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Messages", fontSize = 10.sp)
                    }
                }

                AntBadge(dot = true, color = Color(0xFFFF4D4F)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFFFF1F0), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Alerts", fontSize = 10.sp)
                    }
                }

                AntBadge(count = 99, overflowCount = 99) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF0F2F5), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Updates", fontSize = 10.sp)
                    }
                }
            }
        }

        // All status with text example
        ExampleSection("All Status Types") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AntBadge(status = BadgeStatus.Success, text = "Success")
                    AntBadge(status = BadgeStatus.Processing, text = "Processing")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AntBadge(status = BadgeStatus.Error, text = "Error")
                    AntBadge(status = BadgeStatus.Warning, text = "Warning")
                }
                AntBadge(status = BadgeStatus.Default, text = "Default/Waiting")
            }
        }
    }
}

@Composable
private fun ExampleSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.85f)
        )
        content()
    }
}

@Composable
private fun ExampleBox(text: String = "") {
    Box(
        modifier = Modifier
            .size(if (text.isEmpty()) 48.dp else 80.dp, 48.dp)
            .background(Color(0xFFF0F2F5), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (text.isNotEmpty()) {
            Text(text, fontSize = 10.sp)
        }
    }
}
