package digital.guimauve.antdesign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.TimeSource

/**
 * AntStatistic - Displays a statistic value with title and formatting
 *
 * @param value The value to display - can be Number or String
 * @param title Optional composable title displayed above the value
 * @param prefix Optional composable prefix displayed before the value
 * @param suffix Optional composable suffix displayed after the value
 * @param formatter Custom formatter function to format the value
 * @param precision Number of decimal places (null = no rounding)
 * @param decimalSeparator Character used for decimal separation
 * @param groupSeparator Character used for thousands separation
 * @param valueStyle Modifier to apply styling to the value display
 * @param loading Whether to show loading skeleton
 * @param className Optional class name for styling (reserved for future use)
 * @param style Modifier to apply to the entire component
 * @param modifier Additional modifier for the component
 */
@Composable
fun AntStatistic(
    value: Any,
    title: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    formatter: ((Any) -> String)? = null,
    precision: Int? = null,
    decimalSeparator: String = ".",
    groupSeparator: String = ",",
    valueStyle: Modifier = Modifier,
    loading: Boolean = false,
    className: String? = null,
    style: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.then(style),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title
        if (title != null) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF00000073)
                )
            ) {
                title()
            }
        }

        // Value with loading state
        if (loading) {
            AntSkeleton(
                active = true,
                paragraph = false,
                modifier = Modifier.width(120.dp)
            )
        } else {
            // Format value
            val formattedValue = when {
                formatter != null -> formatter(value)
                value is String -> value
                value is Number -> formatNumber(
                    value,
                    precision,
                    groupSeparator,
                    decimalSeparator
                )

                else -> value.toString()
            }

            // Value row with prefix and suffix
            Row(
                modifier = valueStyle,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Prefix
                if (prefix != null) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            color = Color(0xFF000000D9)
                        )
                    ) {
                        prefix()
                    }
                }

                // Main value
                Text(
                    text = formattedValue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF000000D9)
                )

                // Suffix
                if (suffix != null) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            color = Color(0xFF000000D9)
                        )
                    ) {
                        suffix()
                    }
                }
            }
        }
    }
}

/**
 * AntStatisticCountdown - Displays a countdown timer
 *
 * @param value Target timestamp in milliseconds
 * @param format Time format string (supports DD, HH, mm, ss)
 * @param prefix Optional composable prefix displayed before the countdown
 * @param suffix Optional composable suffix displayed after the countdown
 * @param title Optional composable title displayed above the countdown
 * @param valueStyle Modifier to apply styling to the countdown display
 * @param onFinish Callback invoked when countdown reaches zero
 * @param onChange Callback invoked on each countdown update with remaining time
 * @param className Optional class name for styling (reserved for future use)
 * @param style Modifier to apply to the entire component
 * @param modifier Additional modifier for the component
 */
@Composable
fun AntStatisticCountdown(
    value: Long,
    format: String = "HH:mm:ss",
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    valueStyle: Modifier = Modifier,
    onFinish: (() -> Unit)? = null,
    onChange: ((Long) -> Unit)? = null,
    className: String? = null,
    style: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    var remainingTime by remember(value) { mutableStateOf(calculateRemainingTime(value)) }
    var hasFinished by remember(value) { mutableStateOf(false) }

    LaunchedEffect(value) {
        while (isActive) {
            val newRemaining = calculateRemainingTime(value)

            if (newRemaining != remainingTime) {
                remainingTime = newRemaining
                onChange?.invoke(newRemaining)
            }

            if (newRemaining <= 0 && !hasFinished) {
                hasFinished = true
                onFinish?.invoke()
                break
            }

            if (newRemaining <= 0) break

            delay(16) // ~60fps for smooth countdown
        }
    }

    val formattedTime = formatTime(remainingTime.coerceAtLeast(0), format)

    Column(
        modifier = modifier.then(style),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title
        if (title != null) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF00000073)
                )
            ) {
                title()
            }
        }

        // Countdown value
        Row(
            modifier = valueStyle,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Prefix
            if (prefix != null) {
                CompositionLocalProvider(
                    LocalTextStyle provides LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        color = Color(0xFF000000D9)
                    )
                ) {
                    prefix()
                }
            }

            // Countdown text
            Text(
                text = formattedTime,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF000000D9)
            )

            // Suffix
            if (suffix != null) {
                CompositionLocalProvider(
                    LocalTextStyle provides LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        color = Color(0xFF000000D9)
                    )
                ) {
                    suffix()
                }
            }
        }
    }
}

// Legacy compatibility APIs
enum class TrendDirection {
    Up,
    Down
}

@Deprecated(
    "Use AntStatistic with composable parameters instead",
    ReplaceWith(
        "AntStatistic(value = value, title = { Text(title) }, prefix = { Text(prefix) }, suffix = { Text(suffix) })"
    )
)
@Composable
fun AntStatisticLegacy(
    title: String,
    value: Number,
    modifier: Modifier = Modifier,
    precision: Int = 0,
    prefix: String = "",
    suffix: String = "",
    valueStyle: Color = Color.Black,
    groupSeparator: String = ",",
    decimalSeparator: String = ".",
    formatter: ((Number) -> String)? = null,
    loading: Boolean = false,
) {
    AntStatistic(
        value = value,
        title = if (title.isNotEmpty()) {
            { Text(title) }
        } else null,
        prefix = if (prefix.isNotEmpty()) {
            { Text(prefix) }
        } else null,
        suffix = if (suffix.isNotEmpty()) {
            { Text(suffix) }
        } else null,
        formatter = formatter?.let { f -> { v: Any -> if (v is Number) f(v) else v.toString() } },
        precision = precision,
        decimalSeparator = decimalSeparator,
        groupSeparator = groupSeparator,
        valueStyle = Modifier, // Note: Color not directly convertible to Modifier
        loading = loading,
        modifier = modifier
    )
}

@Deprecated(
    "Use AntStatisticCountdown instead",
    ReplaceWith("AntStatisticCountdown(value, format, prefix, suffix, title, valueStyle, onFinish, modifier = modifier)")
)
@Composable
fun AntCountdown(
    title: String,
    value: Long,
    modifier: Modifier = Modifier,
    format: String = "HH:mm:ss",
    prefix: String = "",
    suffix: String = "",
    valueStyle: Color = Color.Black,
    onFinish: (() -> Unit)? = null,
) {
    AntStatisticCountdown(
        value = value,
        format = format,
        title = if (title.isNotEmpty()) {
            { Text(title) }
        } else null,
        prefix = if (prefix.isNotEmpty()) {
            { Text(prefix) }
        } else null,
        suffix = if (suffix.isNotEmpty()) {
            { Text(suffix) }
        } else null,
        onFinish = onFinish,
        modifier = modifier
    )
}

@Composable
fun AntStatisticWithTrend(
    title: String,
    value: Number,
    modifier: Modifier = Modifier,
    precision: Int = 0,
    prefix: String = "",
    suffix: String = "",
    trend: TrendDirection? = null,
    trendValue: Number? = null,
    trendPrefix: String = "",
    trendSuffix: String = "%",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AntStatistic(
            value = value,
            title = { Text(title) },
            prefix = if (prefix.isNotEmpty()) {
                { Text(prefix) }
            } else null,
            suffix = if (suffix.isNotEmpty()) {
                { Text(suffix) }
            } else null,
            precision = precision
        )

        if (trend != null && trendValue != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val trendColor = when (trend) {
                    TrendDirection.Up -> Color(0xFF52C41A)
                    TrendDirection.Down -> Color(0xFFFF4D4F)
                }

                val trendIcon = when (trend) {
                    TrendDirection.Up -> "↑"
                    TrendDirection.Down -> "↓"
                }

                Text(
                    text = trendIcon,
                    color = trendColor,
                    fontSize = 14.sp
                )

                Text(
                    text = "$trendPrefix${formatNumber(trendValue, precision, ",", ".")}$trendSuffix",
                    color = trendColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Internal helper functions

private fun calculateRemainingTime(targetTimestamp: Long): Long {
    // Get current time - in production, use kotlinx-datetime
    val currentTime = TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
    return targetTimestamp - currentTime
}

private fun formatNumber(
    value: Number,
    precision: Int?,
    groupSeparator: String,
    decimalSeparator: String,
): String {
    val doubleValue = value.toDouble()

    // Handle negative numbers
    val isNegative = doubleValue < 0
    val absValue = abs(doubleValue)

    // Apply precision if specified
    val processedValue = if (precision != null) {
        val multiplier = 10.0.pow(precision)
        round(absValue * multiplier) / multiplier
    } else {
        absValue
    }

    // Split into integer and decimal parts
    val intPart = processedValue.toLong()
    val decimalPart = if (precision != null && precision > 0) {
        val multiplier = 10.0.pow(precision)
        val decimal = abs(((processedValue - intPart) * multiplier).toLong())
        decimal.toString().padStart(precision, '0')
    } else ""

    // Format integer part with group separator
    val integerPart = intPart.toString()
        .reversed()
        .chunked(3)
        .joinToString(groupSeparator)
        .reversed()

    // Build final string
    val formattedNumber = if (precision != null && precision > 0) {
        "$integerPart$decimalSeparator$decimalPart"
    } else {
        integerPart
    }

    return if (isNegative) "-$formattedNumber" else formattedNumber
}

private fun formatTime(milliseconds: Long, format: String): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val days = hours / 24
    val remainingHours = hours % 24

    var result = format

    // Replace tokens in order (longer patterns first to avoid conflicts)
    if (result.contains("DD")) {
        result = result.replace("DD", days.toString().padStart(2, '0'))
    }

    if (result.contains("HH")) {
        val hoursValue = if (format.contains("DD")) remainingHours else hours
        result = result.replace("HH", hoursValue.toString().padStart(2, '0'))
    }

    if (result.contains("mm")) {
        result = result.replace("mm", minutes.toString().padStart(2, '0'))
    }

    if (result.contains("ss")) {
        result = result.replace("ss", seconds.toString().padStart(2, '0'))
    }

    // Support single digit formats
    if (result.contains("D")) {
        result = result.replace("D", days.toString())
    }

    if (result.contains("H")) {
        val hoursValue = if (format.contains("D")) remainingHours else hours
        result = result.replace("H", hoursValue.toString())
    }

    if (result.contains("m")) {
        result = result.replace("m", minutes.toString())
    }

    if (result.contains("s")) {
        result = result.replace("s", seconds.toString())
    }

    return result
}
