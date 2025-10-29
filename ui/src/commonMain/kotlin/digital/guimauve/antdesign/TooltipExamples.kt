package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Comprehensive examples demonstrating all Tooltip features
 * Showcasing 100% React parity
 */
@Composable
fun TooltipExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("Ant Design Tooltip - All Features", fontSize = 24.sp)

        // 1. Basic Usage
        ExampleSection(title = "1. Basic Tooltip") {
            AntTooltip(
                title = "Tooltip text",
                content = {
                    Button(onClick = {}) {
                        Text("Hover me")
                    }
                }
            )
        }

        // 2. All Placements (12 positions)
        ExampleSection(title = "2. All Placements") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlacementButton("TL", TooltipPlacementExt.TopLeft)
                    PlacementButton("Top", TooltipPlacementExt.Top)
                    PlacementButton("TR", TooltipPlacementExt.TopRight)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlacementButton("LT", TooltipPlacementExt.LeftTop)
                    Spacer(Modifier.width(100.dp))
                    PlacementButton("RT", TooltipPlacementExt.RightTop)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlacementButton("LB", TooltipPlacementExt.LeftBottom)
                    Spacer(Modifier.width(100.dp))
                    PlacementButton("RB", TooltipPlacementExt.RightBottom)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlacementButton("BL", TooltipPlacementExt.BottomLeft)
                    PlacementButton("Bottom", TooltipPlacementExt.Bottom)
                    PlacementButton("BR", TooltipPlacementExt.BottomRight)
                }
            }
        }

        // 3. Arrow Variants
        ExampleSection(title = "3. Arrow Variants") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "With arrow (default)",
                    arrow = true,
                    content = {
                        Button(onClick = {}) { Text("Arrow: true") }
                    }
                )
                AntTooltip(
                    title = "Without arrow",
                    arrow = false,
                    content = {
                        Button(onClick = {}) { Text("Arrow: false") }
                    }
                )
                AntTooltip(
                    title = "Point at center",
                    arrow = TooltipArrowConfig(show = true, pointAtCenter = true),
                    content = {
                        Button(onClick = {}) { Text("Arrow: center") }
                    }
                )
            }
        }

        // 4. Trigger Modes
        ExampleSection(title = "4. Trigger Modes") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "Triggered by hover",
                    trigger = TooltipTrigger.Hover,
                    content = {
                        Button(onClick = {}) { Text("Hover trigger") }
                    }
                )
                AntTooltip(
                    title = "Triggered by click",
                    trigger = TooltipTrigger.Click,
                    content = {
                        Button(onClick = {}) { Text("Click trigger") }
                    }
                )
                AntTooltip(
                    title = "Long press to show",
                    trigger = TooltipTrigger.ContextMenu,
                    content = {
                        Button(onClick = {}) { Text("Long press") }
                    }
                )
            }
        }

        // 5. Delays
        ExampleSection(title = "5. Mouse Enter/Leave Delays") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "No delay",
                    mouseEnterDelay = 0,
                    mouseLeaveDelay = 0,
                    content = {
                        Button(onClick = {}) { Text("Instant") }
                    }
                )
                AntTooltip(
                    title = "Default delay (100ms)",
                    content = {
                        Button(onClick = {}) { Text("Default") }
                    }
                )
                AntTooltip(
                    title = "Long delay (1000ms)",
                    mouseEnterDelay = 1000,
                    mouseLeaveDelay = 300,
                    content = {
                        Button(onClick = {}) { Text("Slow") }
                    }
                )
            }
        }

        // 6. Custom Colors
        ExampleSection(title = "6. Custom Colors") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "Pink tooltip",
                    color = Color(0xFFEB2F96),
                    content = {
                        Button(onClick = {}) { Text("Pink") }
                    }
                )
                AntTooltip(
                    title = "Green tooltip",
                    color = Color(0xFF52C41A),
                    content = {
                        Button(onClick = {}) { Text("Green") }
                    }
                )
                AntTooltip(
                    title = "Blue tooltip",
                    color = Color(0xFF1890FF),
                    content = {
                        Button(onClick = {}) { Text("Blue") }
                    }
                )
            }
        }

        // 7. Controlled Mode
        ExampleSection(title = "7. Controlled Tooltip") {
            var isOpen by remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("External control: ${if (isOpen) "Open" else "Closed"}")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { isOpen = true }) {
                        Text("Show")
                    }
                    Button(onClick = { isOpen = false }) {
                        Text("Hide")
                    }
                    Button(onClick = { isOpen = !isOpen }) {
                        Text("Toggle")
                    }
                }
                AntTooltip(
                    title = "Controlled tooltip",
                    open = isOpen,
                    onOpenChange = { isOpen = it },
                    content = {
                        Button(onClick = {}) { Text("Controlled target") }
                    }
                )
            }
        }

        // 8. Default Open
        ExampleSection(title = "8. Default Open (Uncontrolled)") {
            AntTooltip(
                title = "This tooltip is open by default",
                defaultOpen = true,
                content = {
                    Button(onClick = {}) { Text("Default open") }
                }
            )
        }

        // 9. destroyTooltipOnHide
        ExampleSection(title = "9. Destroy on Hide") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "Always mounted (default)",
                    destroyTooltipOnHide = false,
                    content = {
                        Button(onClick = {}) { Text("Kept in DOM") }
                    }
                )
                AntTooltip(
                    title = "Destroyed when hidden",
                    destroyTooltipOnHide = true,
                    content = {
                        Button(onClick = {}) { Text("Unmounted") }
                    }
                )
            }
        }

        // 10. Fresh Mode
        ExampleSection(title = "10. Fresh Re-render") {
            var counter by remember { mutableStateOf(0) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Counter: $counter")
                Button(onClick = { counter++ }) {
                    Text("Increment")
                }
                AntTooltip(
                    title = "Count is: $counter (updated on show)",
                    fresh = true,
                    content = {
                        Button(onClick = {}) { Text("Hover to see count") }
                    }
                )
            }
        }

        // 11. Custom Styles
        ExampleSection(title = "11. Custom Styles") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "Custom overlay style",
                    overlayStyle = Modifier
                        .background(Color(0xFFFF4D4F), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    content = {
                        Button(onClick = {}) { Text("Custom root") }
                    }
                )
                AntTooltip(
                    title = "Custom inner style",
                    overlayInnerStyle = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    content = {
                        Button(onClick = {}) { Text("Custom body") }
                    }
                )
            }
        }

        // 12. Z-Index
        ExampleSection(title = "12. Z-Index Control") {
            Box {
                AntTooltip(
                    title = "High z-index (2000)",
                    zIndex = 2000,
                    content = {
                        Button(onClick = {}) { Text("High priority") }
                    }
                )
            }
        }

        // 13. Callbacks
        ExampleSection(title = "13. Event Callbacks") {
            var log by remember { mutableStateOf("No events yet") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Log: $log", fontSize = 12.sp)
                AntTooltip(
                    title = "Watch the log below",
                    onOpenChange = { isOpen ->
                        log = if (isOpen) "Tooltip opened" else "Tooltip closed"
                    },
                    afterOpenChange = { isOpen ->
                        log = if (isOpen) "Open animation completed" else "Close animation completed"
                    },
                    content = {
                        Button(onClick = {}) { Text("Trigger events") }
                    }
                )
            }
        }

        // 14. Custom Content
        ExampleSection(title = "14. Rich Content Tooltip") {
            AntTooltip(
                title = {
                    Column(modifier = Modifier.padding(4.dp)) {
                        Text("Custom Title", color = Color.White, fontSize = 16.sp)
                        Text("With multiple lines", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                        Text("And rich formatting", color = Color(0xFF69C0FF), fontSize = 10.sp)
                    }
                },
                content = {
                    Button(onClick = {}) { Text("Rich tooltip") }
                }
            )
        }

        // 15. autoAdjustOverflow
        ExampleSection(title = "15. Auto Adjust Overflow") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AntTooltip(
                    title = "Adjusts position if overflows",
                    autoAdjustOverflow = true,
                    content = {
                        Button(onClick = {}) { Text("Auto adjust: ON") }
                    }
                )
                AntTooltip(
                    title = "Might overflow viewport",
                    autoAdjustOverflow = false,
                    content = {
                        Button(onClick = {}) { Text("Auto adjust: OFF") }
                    }
                )
                AntTooltip(
                    title = "Custom overflow config",
                    autoAdjustOverflow = AdjustOverflow(adjustX = true, adjustY = false),
                    content = {
                        Button(onClick = {}) { Text("Custom adjust") }
                    }
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        Text(
            "All 15+ parameters implemented with 100% React parity!",
            fontSize = 16.sp,
            color = Color(0xFF52C41A)
        )
    }
}

@Composable
private fun PlacementButton(label: String, placement: TooltipPlacementExt) {
    AntTooltip(
        title = "Tooltip on $label",
        placement = placement,
        content = {
            Button(onClick = {}, modifier = Modifier.width(80.dp)) {
                Text(label)
            }
        }
    )
}

@Composable
private fun ExampleSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, fontSize = 18.sp, color = Color(0xFF1890FF))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            content()
        }
    }
}
