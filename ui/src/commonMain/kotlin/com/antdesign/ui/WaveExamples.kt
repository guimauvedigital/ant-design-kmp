package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Examples demonstrating Wave effect features for Ant Design KMP
 *
 * This file showcases:
 * - Basic wave click effect
 * - Custom color waves
 * - Different durations
 * - Disabled state
 * - Multiple simultaneous waves
 * - Component-specific quick animations
 * - Border radius inheritance
 * - Custom configurations
 *
 * React equivalent demos:
 * https://ant.design/components/button#components-button-demo-wave
 */

@Composable
fun WaveExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Ant Design Wave Effect Examples",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        // Basic wave effect
        BasicWaveExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom color waves
        CustomColorWaveExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Different durations
        DurationWaveExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Disabled wave
        DisabledWaveExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Component types
        ComponentTypesWaveExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Advanced configuration
        AdvancedWaveConfigExample()
    }
}

/**
 * Basic wave effect
 *
 * React equivalent:
 * ```jsx
 * <Wave>
 *   <Button>Click me</Button>
 * </Wave>
 * ```
 */
@Composable
fun BasicWaveExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Basic Wave Effect", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Text("Click the box to see wave animation", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)

        AntWave(
            modifier = Modifier
                .size(200.dp, 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1890FF), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Click me", color = Color.White)
            }
        }
    }
}

/**
 * Custom color waves
 *
 * React equivalent:
 * ```jsx
 * <ConfigProvider wave={{ color: '#ff0000' }}>
 *   <Button>Red Wave</Button>
 * </ConfigProvider>
 * ```
 */
@Composable
fun CustomColorWaveExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom Color Waves", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Red wave
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                color = Color.Red.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFF4D4F), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Red", color = Color.White)
                }
            }

            // Green wave
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                color = Color.Green.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF52C41A), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Green", color = Color.White)
                }
            }

            // Purple wave
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                color = Color.Magenta.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF722ED1), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Purple", color = Color.White)
                }
            }
        }
    }
}

/**
 * Different animation durations
 *
 * React equivalent:
 * ```jsx
 * <ConfigProvider wave={{ duration: 300 }}>
 *   <Button>Fast Wave</Button>
 * </ConfigProvider>
 * ```
 */
@Composable
fun DurationWaveExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Different Durations", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Fast (300ms)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                duration = 300
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1890FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fast (300ms)", color = Color.White)
                }
            }

            // Normal (600ms - default)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                duration = 600
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1890FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Normal (600ms)", color = Color.White)
                }
            }

            // Slow (1200ms)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                duration = 1200
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1890FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Slow (1200ms)", color = Color.White)
                }
            }
        }
    }
}

/**
 * Disabled wave effect
 *
 * React equivalent:
 * ```jsx
 * <ConfigProvider wave={{ disabled: true }}>
 *   <Button>No Wave</Button>
 * </ConfigProvider>
 * ```
 */
@Composable
fun DisabledWaveExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Disabled Wave", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Text("No animation on click", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)

        AntWave(
            modifier = Modifier.size(200.dp, 100.dp),
            disabled = true
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Disabled - No Wave", color = Color.White)
            }
        }
    }
}

/**
 * Component-specific wave behavior
 *
 * React equivalent:
 * ```jsx
 * <Wave component="Checkbox">
 *   <Checkbox>Quick Animation</Checkbox>
 * </Wave>
 * ```
 */
@Composable
fun ComponentTypesWaveExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Component Types", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Text("Checkbox/Radio use quicker animation (70% duration)", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Button component (normal speed)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                component = WaveComponent.Button
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1890FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Button", color = Color.White)
                }
            }

            // Checkbox component (quick)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                component = WaveComponent.Checkbox
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF52C41A), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Checkbox (Quick)", color = Color.White)
                }
            }

            // Radio component (quick)
            AntWave(
                modifier = Modifier.size(120.dp, 80.dp),
                component = WaveComponent.Radio
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFA8C16), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Radio (Quick)", color = Color.White)
                }
            }
        }
    }
}

/**
 * Advanced configuration with WaveConfig
 *
 * React equivalent:
 * ```jsx
 * <ConfigProvider
 *   wave={{
 *     disabled: false,
 *     color: '#1890ff',
 *     duration: 600,
 *     showEffect: (element, info) => {
 *       console.log('Wave triggered', info);
 *     }
 *   }}
 * >
 *   <Button>Advanced Config</Button>
 * </ConfigProvider>
 * ```
 */
@Composable
fun AdvancedWaveConfigExample() {
    var lastWaveInfo by remember { mutableStateOf("No waves yet") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Advanced Configuration", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Text("Using WaveConfig with callback", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        Text("Last wave: $lastWaveInfo", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)

        AntWave(
            modifier = Modifier.size(200.dp, 100.dp),
            config = WaveConfig(
                disabled = false,
                color = Color(0xFF1890FF).copy(alpha = 0.3f),
                duration = 800,
                borderRadius = 12.dp,
                showEffect = { info ->
                    lastWaveInfo = "x: ${info.position.x.toInt()}, y: ${info.position.y.toInt()}"
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1890FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Click anywhere", color = Color.White)
            }
        }
    }
}
