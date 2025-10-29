package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.TimeSource

/**
 * Examples demonstrating 100% React parity for Ant Design Statistic component
 *
 * Features demonstrated:
 * 1. Basic statistic with number formatting
 * 2. Custom precision and separators
 * 3. Composable title, prefix, suffix
 * 4. Custom formatter
 * 5. Loading state
 * 6. Countdown with various formats
 * 7. Countdown callbacks (onFinish, onChange)
 * 8. Value styling with Modifier
 * 9. String values
 * 10. Negative numbers
 */

@Composable
fun StatisticExamplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Ant Design Statistic Examples - 100% React Parity",
            style = MaterialTheme.typography.headlineMedium
        )

        // Basic Statistics
        BasicStatisticsExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Number Formatting
        NumberFormattingExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Composable Content
        ComposableContentExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Custom Formatter
        CustomFormatterExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Loading State
        LoadingStateExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Countdown Examples
        CountdownExamples()

        Spacer(modifier = Modifier.height(8.dp))

        // Countdown Callbacks
        CountdownCallbacksExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Value Styling
        ValueStylingExample()

        Spacer(modifier = Modifier.height(8.dp))

        // Advanced Examples
        AdvancedExamples()
    }
}

@Composable
private fun BasicStatisticsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Basic Statistics", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AntStatistic(
                value = 11.28,
                title = { Text("Active Users") },
                precision = 2,
                suffix = { Text("%") },
                modifier = Modifier.weight(1f)
            )

            AntStatistic(
                value = 93,
                title = { Text("Feedback") },
                suffix = { Text("/100") },
                modifier = Modifier.weight(1f)
            )

            AntStatistic(
                value = 1128,
                title = { Text("Unmerged") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NumberFormattingExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Number Formatting", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large number with group separator
            AntStatistic(
                value = 1234567.89,
                title = { Text("Revenue") },
                precision = 2,
                groupSeparator = ",",
                decimalSeparator = ".",
                prefix = { Text("$") },
                modifier = Modifier.weight(1f)
            )

            // High precision
            AntStatistic(
                value = 0.123456789,
                title = { Text("Conversion Rate") },
                precision = 4,
                suffix = { Text("%") },
                modifier = Modifier.weight(1f)
            )

            // European format
            AntStatistic(
                value = 9876543.21,
                title = { Text("Cost") },
                precision = 2,
                groupSeparator = ".",
                decimalSeparator = ",",
                prefix = { Text("€ ") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Negative number
            AntStatistic(
                value = -1234.56,
                title = { Text("Loss") },
                precision = 2,
                prefix = { Text("$") },
                modifier = Modifier.weight(1f)
            )

            // No grouping
            AntStatistic(
                value = 999999,
                title = { Text("ID") },
                groupSeparator = "",
                modifier = Modifier.weight(1f)
            )

            // No precision (null)
            AntStatistic(
                value = 3.14159265359,
                title = { Text("Pi") },
                precision = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ComposableContentExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Composable Title, Prefix, Suffix", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rich title
            AntStatistic(
                value = 1128,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Active", color = Color(0xFF1890FF))
                        Text("Users", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier.weight(1f)
            )

            // Rich prefix
            AntStatistic(
                value = 93,
                title = { Text("Growth") },
                prefix = {
                    Text("↑ ", color = Color(0xFF52C41A), fontSize = 24.sp)
                },
                suffix = { Text("%") },
                modifier = Modifier.weight(1f)
            )

            // Complex suffix
            AntStatistic(
                value = 128,
                title = { Text("Downloads") },
                suffix = {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("M", fontSize = 14.sp)
                        Text("+5%", fontSize = 12.sp, color = Color(0xFF52C41A))
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CustomFormatterExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Custom Formatters", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // K/M/B formatter
            AntStatistic(
                value = 1234567,
                title = { Text("Users") },
                formatter = { value ->
                    val num = (value as Number).toDouble()
                    when {
                        num < 1000 -> num.toInt().toString()
                        num < 1000000 -> {
                            val k = (num / 1000 * 10).toInt() / 10.0
                            "${k}K"
                        }

                        num < 1000000000 -> {
                            val m = (num / 1000000 * 10).toInt() / 10.0
                            "${m}M"
                        }

                        else -> {
                            val b = (num / 1000000000 * 10).toInt() / 10.0
                            "${b}B"
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )

            // Duration formatter
            AntStatistic(
                value = 3661,
                title = { Text("Duration") },
                formatter = { value ->
                    val seconds = (value as Number).toInt()
                    val h = seconds / 3600
                    val m = (seconds % 3600) / 60
                    val s = seconds % 60
                    "${h}h ${m}m ${s}s"
                },
                modifier = Modifier.weight(1f)
            )

            // String value
            AntStatistic(
                value = "Active",
                title = { Text("Status") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LoadingStateExample() {
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        loading = false
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Loading State", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AntStatistic(
                value = 1234,
                title = { Text("Loading Demo") },
                loading = loading,
                modifier = Modifier.weight(1f)
            )

            AntStatistic(
                value = 5678,
                title = { Text("Always Loaded") },
                loading = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CountdownExamples() {
    val currentTime = remember {
        TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Countdown Formats", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Standard HH:mm:ss
            AntStatisticCountdown(
                value = currentTime + 3661000, // +1 hour 1 minute 1 second
                title = { Text("HH:mm:ss") },
                format = "HH:mm:ss",
                modifier = Modifier.weight(1f)
            )

            // Days with hours
            AntStatisticCountdown(
                value = currentTime + 90061000, // +1 day 1 hour 1 minute 1 second
                title = { Text("DD days HH:mm:ss") },
                format = "DD days HH:mm:ss",
                modifier = Modifier.weight(1f)
            )

            // Minutes and seconds
            AntStatisticCountdown(
                value = currentTime + 125000, // +2 minutes 5 seconds
                title = { Text("mm:ss") },
                format = "mm:ss",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Custom format
            AntStatisticCountdown(
                value = currentTime + 3661000,
                title = { Text("Custom: H hours m min") },
                format = "H hours m min",
                modifier = Modifier.weight(1f)
            )

            // With prefix/suffix
            AntStatisticCountdown(
                value = currentTime + 1000000,
                title = { Text("With Prefix/Suffix") },
                format = "mm:ss",
                prefix = { Text("Ends in ") },
                suffix = { Text(" remaining") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CountdownCallbacksExample() {
    val currentTime = remember {
        TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
    }
    var finishMessage by remember { mutableStateOf("") }
    var lastUpdate by remember { mutableStateOf(0L) }
    var updateCount by remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Countdown Callbacks", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // With onFinish
            Column(modifier = Modifier.weight(1f)) {
                AntStatisticCountdown(
                    value = currentTime + 5000, // 5 seconds
                    title = { Text("With onFinish") },
                    format = "mm:ss",
                    onFinish = {
                        finishMessage = "Countdown finished!"
                    }
                )
                if (finishMessage.isNotEmpty()) {
                    Text(
                        finishMessage,
                        color = Color(0xFF52C41A),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // With onChange
            Column(modifier = Modifier.weight(1f)) {
                AntStatisticCountdown(
                    value = currentTime + 10000, // 10 seconds
                    title = { Text("With onChange") },
                    format = "mm:ss",
                    onChange = { remaining ->
                        lastUpdate = remaining
                        updateCount++
                    }
                )
                Text(
                    "Updates: $updateCount",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ValueStylingExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Value Styling with Modifier", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Background style
            AntStatistic(
                value = 1234,
                title = { Text("With Background") },
                valueStyle = Modifier
                    .background(Color(0xFFF0F0F0))
                    .padding(8.dp),
                modifier = Modifier.weight(1f)
            )

            // Colored background
            AntStatistic(
                value = 5678,
                title = { Text("Success Style") },
                valueStyle = Modifier
                    .background(Color(0xFFF6FFED))
                    .padding(8.dp),
                modifier = Modifier.weight(1f)
            )

            // Error style
            AntStatistic(
                value = -999,
                title = { Text("Error Style") },
                precision = 0,
                valueStyle = Modifier
                    .background(Color(0xFFFFF1F0))
                    .padding(8.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AdvancedExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Advanced Examples", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dashboard card style
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {
                AntStatistic(
                    value = 2849,
                    title = {
                        Text(
                            "Total Revenue",
                            fontSize = 12.sp,
                            color = Color(0xFF00000073)
                        )
                    },
                    prefix = { Text("$", color = Color(0xFF1890FF)) },
                    precision = 0,
                    valueStyle = Modifier,
                    style = Modifier
                )

                Text(
                    "+12% from last month",
                    fontSize = 12.sp,
                    color = Color(0xFF52C41A),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Comparison card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFFFFBE6))
                    .padding(16.dp)
            ) {
                AntStatistic(
                    value = "98.5%",
                    title = { Text("Uptime") },
                    valueStyle = Modifier,
                    style = Modifier
                )

                Text(
                    "Last 30 days",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
