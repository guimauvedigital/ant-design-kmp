package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Comprehensive examples demonstrating all Tour features and parameters
 */

/**
 * Example 1: Basic Tour
 * Simple tour with 3 steps
 */
@Composable
fun TourBasicExample() {
    var openTour by remember { mutableStateOf(false) }
    var ref1 by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var ref2 by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var ref3 by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val steps = listOf(
        TourStep(
            title = "Upload File",
            description = "Put your files here.",
            target = { ref1 }
        ),
        TourStep(
            title = "Save",
            description = "Save your changes.",
            target = { ref2 }
        ),
        TourStep(
            title = "Other Actions",
            description = "Click to see other actions.",
            target = { ref3 }
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { openTour = true }) {
            Text("Start Tour")
        }

        Card(modifier = Modifier.onGloballyPositioned { ref1 = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Upload Area")
            }
        }

        Card(modifier = Modifier.onGloballyPositioned { ref2 = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Save Button")
            }
        }

        Card(modifier = Modifier.onGloballyPositioned { ref3 = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Actions Menu")
            }
        }
    }

    AntTour(
        steps = steps,
        open = openTour,
        onClose = { openTour = false }
    )
}

/**
 * Example 2: Controlled Tour
 * Tour with controlled current step
 */
@Composable
fun TourControlledExample() {
    var open by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(0) }

    val steps = listOf(
        TourStep(
            title = "Step 1",
            description = "This is the first step."
        ),
        TourStep(
            title = "Step 2",
            description = "This is the second step."
        ),
        TourStep(
            title = "Step 3",
            description = "This is the third step."
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { open = true }) {
                Text("Start Tour")
            }
            Button(
                onClick = { current = (current - 1).coerceAtLeast(0) },
                enabled = current > 0
            ) {
                Text("Previous")
            }
            Button(
                onClick = { current = (current + 1).coerceAtMost(steps.size - 1) },
                enabled = current < steps.size - 1
            ) {
                Text("Next")
            }
        }

        Text("Current Step: ${current + 1} / ${steps.size}")
    }

    AntTour(
        steps = steps,
        open = open,
        current = current,
        onChange = { current = it },
        onClose = { open = false }
    )
}

/**
 * Example 3: Primary Type Tour
 * Tour with primary styling
 */
@Composable
fun TourPrimaryTypeExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Welcome!",
            description = "Let's start exploring the features."
        ),
        TourStep(
            title = "Feature 1",
            description = "This is an amazing feature you'll love."
        ),
        TourStep(
            title = "Feature 2",
            description = "Another great feature to discover."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Primary Tour")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        type = TourType.Primary
    )
}

/**
 * Example 4: Tour with Custom Buttons
 * Tour with customized navigation buttons
 */
@Composable
fun TourCustomButtonsExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Step 1",
            description = "First step with custom buttons.",
            nextButtonProps = TourButtonProps(
                children = "Continue",
                type = ButtonType.Primary
            )
        ),
        TourStep(
            title = "Step 2",
            description = "Middle step.",
            prevButtonProps = TourButtonProps(
                children = "Go Back"
            ),
            nextButtonProps = TourButtonProps(
                children = "Proceed"
            )
        ),
        TourStep(
            title = "Step 3",
            description = "Last step with custom finish button.",
            prevButtonProps = TourButtonProps(
                children = "Previous"
            ),
            nextButtonProps = TourButtonProps(
                children = "Complete",
                type = ButtonType.Primary,
                danger = false
            )
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Custom Buttons")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false }
    )
}

/**
 * Example 5: Tour with Cover Images
 * Tour with media/image covers in steps
 */
@Composable
fun TourWithCoverExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Welcome",
            description = "Welcome to our application!",
            cover = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF1890FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Cover Image",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ),
        TourStep(
            title = "Features",
            description = "Discover our amazing features.",
            cover = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF52C41A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Feature Preview",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Covers")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false }
    )
}

/**
 * Example 6: Tour with Custom Indicators
 * Tour with custom progress indicators
 */
@Composable
fun TourCustomIndicatorsExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(title = "Step 1", description = "First step"),
        TourStep(title = "Step 2", description = "Second step"),
        TourStep(title = "Step 3", description = "Third step"),
        TourStep(title = "Step 4", description = "Fourth step")
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Custom Indicators")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        indicatorsRender = { current, total ->
            // Custom indicator: "1 / 4" text
            Text(
                text = "${current + 1} / $total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1890FF)
            )
        }
    )
}

/**
 * Example 7: Tour without Mask
 * Tour without the dark overlay
 */
@Composable
fun TourNoMaskExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "No Mask Tour",
            description = "This tour doesn't have a dark overlay."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour without Mask")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        mask = false
    )
}

/**
 * Example 8: Tour without Arrow
 * Tour without the arrow pointing to targets
 */
@Composable
fun TourNoArrowExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "No Arrow Tour",
            description = "This tour doesn't show arrows."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour without Arrow")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        arrow = false
    )
}

/**
 * Example 9: Tour with Custom Mask
 * Tour with custom mask color
 */
@Composable
fun TourCustomMaskExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Custom Mask",
            description = "This tour has a custom colored mask."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Custom Mask")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        mask = MaskConfig(
            color = Color(0xFF1890FF).copy(alpha = 0.3f)
        )
    )
}

/**
 * Example 10: Tour with Placements
 * Tour with different placements for each step
 */
@Composable
fun TourPlacementsExample() {
    var open by remember { mutableStateOf(false) }
    var ref1 by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var ref2 by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val steps = listOf(
        TourStep(
            title = "Top Placement",
            description = "This step appears at the top.",
            placement = PopoverPlacement.Top,
            target = { ref1 }
        ),
        TourStep(
            title = "Bottom Placement",
            description = "This step appears at the bottom.",
            placement = PopoverPlacement.Bottom,
            target = { ref2 }
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { open = true }) {
            Text("Start Tour with Placements")
        }

        Card(modifier = Modifier.onGloballyPositioned { ref1 = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Element 1")
            }
        }

        Spacer(modifier = Modifier.height(100.dp))

        Card(modifier = Modifier.onGloballyPositioned { ref2 = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Element 2")
            }
        }
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false }
    )
}

/**
 * Example 11: Tour with Callbacks
 * Tour demonstrating all callback events
 */
@Composable
fun TourCallbacksExample() {
    var open by remember { mutableStateOf(false) }
    var eventLog by remember { mutableStateOf(listOf<String>()) }

    val steps = listOf(
        TourStep(title = "Step 1", description = "First step"),
        TourStep(title = "Step 2", description = "Second step"),
        TourStep(title = "Step 3", description = "Third step")
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = {
            open = true
            eventLog = eventLog + "Tour opened"
        }) {
            Text("Start Tour with Callbacks")
        }

        Text("Event Log:", fontWeight = FontWeight.Bold)
        eventLog.forEach { event ->
            Text("- $event", fontSize = 12.sp)
        }
    }

    AntTour(
        steps = steps,
        open = open,
        onChange = { step ->
            eventLog = eventLog + "Changed to step ${step + 1}"
        },
        onClose = {
            open = false
            eventLog = eventLog + "Tour closed"
        },
        onFinish = {
            eventLog = eventLog + "Tour finished"
        }
    )
}

/**
 * Example 12: Tour with Custom Close Icon
 * Tour with a custom close button icon
 */
@Composable
fun TourCustomCloseIconExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Custom Close Icon",
            description = "This tour has a custom close button."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Custom Close Icon")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        closeIcon = {
            Text(
                "✕",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    )
}

/**
 * Example 13: Tour with Gap Configuration
 * Tour with custom gap between popover and target
 */
@Composable
fun TourGapExample() {
    var open by remember { mutableStateOf(false) }
    var ref by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val steps = listOf(
        TourStep(
            title = "Custom Gap",
            description = "This tour has a custom gap configuration.",
            target = { ref }
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { open = true }) {
            Text("Start Tour with Custom Gap")
        }

        Card(modifier = Modifier.onGloballyPositioned { ref = it }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Target Element")
            }
        }
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        gap = TourGap(offset = 12, radius = 4)
    )
}

/**
 * Example 14: Tour with Mixed Step Types
 * Tour with different types for different steps
 */
@Composable
fun TourMixedTypesExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "Default Step",
            description = "This step uses the default type.",
            type = TourType.Default
        ),
        TourStep(
            title = "Primary Step",
            description = "This step uses the primary type.",
            type = TourType.Primary
        ),
        TourStep(
            title = "Another Default",
            description = "Back to default type.",
            type = TourType.Default
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour with Mixed Types")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false }
    )
}

/**
 * Example 15: Tour with Custom Animation
 * Tour with disabled animations
 */
@Composable
fun TourCustomAnimationExample() {
    var open by remember { mutableStateOf(false) }

    val steps = listOf(
        TourStep(
            title = "No Animation",
            description = "This tour appears without animations."
        )
    )

    Button(onClick = { open = true }) {
        Text("Start Tour without Animation")
    }

    AntTour(
        steps = steps,
        open = open,
        onClose = { open = false },
        animated = false
    )
}

/**
 * Example 16: Comprehensive Tour Demo
 * Complete tour with all features combined
 */
@Composable
fun TourComprehensiveExample() {
    var open by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf(0) }
    var ref1 by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var ref2 by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var ref3 by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val steps = listOf(
        TourStep(
            title = "Welcome to Our App",
            description = "Let's take a quick tour of the main features.",
            type = TourType.Primary,
            placement = PopoverPlacement.Bottom,
            target = { ref1 },
            nextButtonProps = TourButtonProps(
                children = "Let's Go!",
                type = ButtonType.Primary
            ),
            cover = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFF1890FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Welcome!",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ),
        TourStep(
            title = "Main Dashboard",
            description = "This is your main dashboard where you can see all your important metrics and data.",
            placement = PopoverPlacement.Top,
            target = { ref2 },
            prevButtonProps = TourButtonProps(children = "Back"),
            nextButtonProps = TourButtonProps(children = "Continue")
        ),
        TourStep(
            title = "Settings Panel",
            description = "Click here to access all your settings and preferences.",
            placement = PopoverPlacement.Left,
            target = { ref3 },
            prevButtonProps = TourButtonProps(children = "Previous"),
            nextButtonProps = TourButtonProps(
                children = "Finish Tour",
                type = ButtonType.Primary
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = {
            open = true
            current = 0
        }) {
            Text("Start Comprehensive Tour")
        }

        Card(modifier = Modifier.onGloballyPositioned { ref1 = it }) {
            Box(modifier = Modifier.padding(32.dp)) {
                Text("Header Section", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Card(modifier = Modifier.onGloballyPositioned { ref2 = it }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(32.dp)
            ) {
                Text("Dashboard Area", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Card(modifier = Modifier.onGloballyPositioned { ref3 = it }) {
            Box(modifier = Modifier.padding(32.dp)) {
                Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    AntTour(
        steps = steps,
        open = open,
        current = current,
        onChange = { current = it },
        onClose = { open = false },
        onFinish = {
            println("Tour completed!")
        },
        mask = MaskConfig(
            color = Color.Black.copy(alpha = 0.5f)
        ),
        zIndex = 1001
    )
}
