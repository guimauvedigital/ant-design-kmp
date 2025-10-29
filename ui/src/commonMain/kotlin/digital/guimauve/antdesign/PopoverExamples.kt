package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Comprehensive examples demonstrating all Popover features and parameters
 */

/**
 * Example 1: Basic Popover with Hover trigger (default)
 */
@Composable
fun PopoverBasicExample() {
    AntPopover(
        content = "This is the popover content. It appears when you hover over the button.",
        title = "Popover Title"
    ) {
        Button(onClick = {}) {
            Text("Hover me")
        }
    }
}

/**
 * Example 2: All Trigger Modes
 */
@Composable
fun PopoverTriggerModesExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hover trigger
        AntPopover(
            content = "Triggered by hover",
            title = "Hover Trigger",
            trigger = PopoverTrigger.Hover
        ) {
            Button(onClick = {}) {
                Text("Hover Trigger")
            }
        }

        // Click trigger
        AntPopover(
            content = "Triggered by click",
            title = "Click Trigger",
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Click Trigger")
            }
        }

        // Focus trigger
        AntPopover(
            content = "Triggered by focus",
            title = "Focus Trigger",
            trigger = PopoverTrigger.Focus
        ) {
            Button(onClick = {}) {
                Text("Focus Trigger")
            }
        }

        // Context Menu trigger (long press)
        AntPopover(
            content = "Triggered by long press",
            title = "Context Menu Trigger",
            trigger = PopoverTrigger.ContextMenu
        ) {
            Button(onClick = {}) {
                Text("Long Press Trigger")
            }
        }
    }
}

/**
 * Example 3: All 12 Placements
 */
@Composable
fun PopoverPlacementsExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Top row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            AntPopover(
                content = "Top Left placement",
                placement = PopoverPlacement.TopLeft,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("TL") }
            }

            AntPopover(
                content = "Top placement",
                placement = PopoverPlacement.Top,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("Top") }
            }

            AntPopover(
                content = "Top Right placement",
                placement = PopoverPlacement.TopRight,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("TR") }
            }
        }

        // Middle row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntPopover(
                    content = "Left Top placement",
                    placement = PopoverPlacement.LeftTop,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("LT") }
                }

                AntPopover(
                    content = "Left placement",
                    placement = PopoverPlacement.Left,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("Left") }
                }

                AntPopover(
                    content = "Left Bottom placement",
                    placement = PopoverPlacement.LeftBottom,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("LB") }
                }
            }

            Spacer(modifier = Modifier.width(100.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntPopover(
                    content = "Right Top placement",
                    placement = PopoverPlacement.RightTop,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("RT") }
                }

                AntPopover(
                    content = "Right placement",
                    placement = PopoverPlacement.Right,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("Right") }
                }

                AntPopover(
                    content = "Right Bottom placement",
                    placement = PopoverPlacement.RightBottom,
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) { Text("RB") }
                }
            }
        }

        // Bottom row
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntPopover(
                content = "Bottom Left placement",
                placement = PopoverPlacement.BottomLeft,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("BL") }
            }

            AntPopover(
                content = "Bottom placement",
                placement = PopoverPlacement.Bottom,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("Bottom") }
            }

            AntPopover(
                content = "Bottom Right placement",
                placement = PopoverPlacement.BottomRight,
                trigger = PopoverTrigger.Click
            ) {
                Button(onClick = {}) { Text("BR") }
            }
        }
    }
}

/**
 * Example 4: Controlled Popover with state
 */
@Composable
fun PopoverControlledExample() {
    var isOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("External control: ${if (isOpen) "Open" else "Closed"}")

        Button(onClick = { isOpen = !isOpen }) {
            Text("Toggle Popover")
        }

        AntPopover(
            content = "This is a controlled popover",
            title = "Controlled",
            open = isOpen,
            onOpenChange = { isOpen = it }
        ) {
            Button(onClick = {}) {
                Text("Controlled Popover Target")
            }
        }
    }
}

/**
 * Example 5: Custom Colors
 */
@Composable
fun PopoverCustomColorExample() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntPopover(
            content = "Custom blue background",
            title = "Blue Popover",
            color = Color(0xFF1890FF),
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Blue Popover")
            }
        }

        AntPopover(
            content = "Custom green background",
            title = "Green Popover",
            color = Color(0xFF52C41A),
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Green Popover")
            }
        }

        AntPopover(
            content = "Custom red background",
            title = "Red Popover",
            color = Color(0xFFFF4D4F),
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Red Popover")
            }
        }
    }
}

/**
 * Example 6: Complex Content (Composable)
 */
@Composable
fun PopoverComplexContentExample() {
    AntPopover(
        content = @Composable {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("This is complex content", fontWeight = FontWeight.Bold)
                Text("You can include any Composable here:")

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {}) { Text("Action 1") }
                    Button(onClick = {}) { Text("Action 2") }
                }

                Text("More text here", fontSize = 12.sp, color = Color.Gray)
            }
        },
        title = @Composable {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Green)
                )
                Text("Custom Title", fontWeight = FontWeight.Bold)
            }
        },
        trigger = PopoverTrigger.Click
    ) {
        Button(onClick = {}) {
            Text("Complex Content")
        }
    }
}

/**
 * Example 7: Arrow Configuration
 */
@Composable
fun PopoverArrowConfigExample() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // With arrow (default)
        AntPopover(
            content = "Popover with arrow",
            title = "With Arrow",
            arrow = true,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("With Arrow")
            }
        }

        // Without arrow
        AntPopover(
            content = "Popover without arrow",
            title = "No Arrow",
            arrow = false,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("No Arrow")
            }
        }

        // Arrow pointing at center
        AntPopover(
            content = "Arrow points at center",
            title = "Centered Arrow",
            arrow = PopoverArrowConfig(show = true, pointAtCenter = true),
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Centered Arrow")
            }
        }
    }
}

/**
 * Example 8: Hover Delays
 */
@Composable
fun PopoverDelaysExample() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fast (default)
        AntPopover(
            content = "Fast response (100ms delay)",
            title = "Fast",
            mouseEnterDelay = 100,
            mouseLeaveDelay = 100
        ) {
            Button(onClick = {}) {
                Text("Fast (100ms)")
            }
        }

        // Slow
        AntPopover(
            content = "Slow response (1000ms delay)",
            title = "Slow",
            mouseEnterDelay = 1000,
            mouseLeaveDelay = 500
        ) {
            Button(onClick = {}) {
                Text("Slow (1000ms)")
            }
        }

        // Instant
        AntPopover(
            content = "Instant response (0ms delay)",
            title = "Instant",
            mouseEnterDelay = 0,
            mouseLeaveDelay = 0
        ) {
            Button(onClick = {}) {
                Text("Instant (0ms)")
            }
        }
    }
}

/**
 * Example 9: Lifecycle Callbacks
 */
@Composable
fun PopoverCallbacksExample() {
    var log by remember { mutableStateOf("No events yet") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Event Log: $log", fontSize = 14.sp)

        AntPopover(
            content = "Popover with callbacks",
            title = "Callbacks Demo",
            trigger = PopoverTrigger.Click,
            onOpenChange = { isOpen ->
                log = "onOpenChange: ${if (isOpen) "opened" else "closed"}"
            },
            afterOpenChange = { isOpen ->
                log = log + " -> afterOpenChange: ${if (isOpen) "opened" else "closed"}"
            }
        ) {
            Button(onClick = {}) {
                Text("Click to see events")
            }
        }
    }
}

/**
 * Example 10: Advanced Options
 */
@Composable
fun PopoverAdvancedExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Default open
        AntPopover(
            content = "This popover starts open",
            title = "Default Open",
            defaultOpen = true,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Default Open")
            }
        }

        // Destroy on hide
        AntPopover(
            content = "Unmounted when hidden (destroyTooltipOnHide)",
            title = "Destroy On Hide",
            destroyTooltipOnHide = true,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Destroy On Hide")
            }
        }

        // Fresh render
        AntPopover(
            content = "Re-renders fresh each time (fresh)",
            title = "Fresh Render",
            fresh = true,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("Fresh Render")
            }
        }

        // Custom z-index
        AntPopover(
            content = "High z-index (2000)",
            title = "High Z-Index",
            zIndex = 2000,
            trigger = PopoverTrigger.Click
        ) {
            Button(onClick = {}) {
                Text("High Z-Index")
            }
        }
    }
}

/**
 * Example 11: Custom Styling
 */
@Composable
fun PopoverCustomStylingExample() {
    AntPopover(
        content = "Custom styled popover",
        title = "Custom Styles",
        trigger = PopoverTrigger.Click,
        overlayStyle = Modifier
            .width(400.dp)
            .padding(16.dp),
        overlayInnerStyle = Modifier
            .background(Color(0xFFFFF1F0)),
        styles = PopoverStyles(
            root = Modifier.shadow(16.dp),
            body = Modifier.padding(16.dp),
            title = Modifier.padding(bottom = 8.dp),
            content = Modifier.padding(top = 8.dp)
        )
    ) {
        Button(onClick = {}) {
            Text("Custom Styles")
        }
    }
}

/**
 * Example 12: Nested Popovers
 */
@Composable
fun PopoverNestedExample() {
    AntPopover(
        content = @Composable {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Outer popover content")

                AntPopover(
                    content = "Inner popover!",
                    title = "Nested",
                    trigger = PopoverTrigger.Click
                ) {
                    Button(onClick = {}) {
                        Text("Open Inner Popover")
                    }
                }
            }
        },
        title = "Outer Popover",
        trigger = PopoverTrigger.Click
    ) {
        Button(onClick = {}) {
            Text("Nested Popovers")
        }
    }
}
