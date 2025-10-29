package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CalendarMode {
    Month,
    Year
}

@Composable
fun AntCalendar(
    modifier: Modifier = Modifier,
    value: String? = null, // Format: YYYY-MM-DD
    mode: CalendarMode = CalendarMode.Month,
    fullscreen: Boolean = true,
    onSelect: ((String) -> Unit)? = null,
    onPanelChange: ((String, CalendarMode) -> Unit)? = null,
    dateCellRender: (@Composable (String) -> Unit)? = null,
    monthCellRender: (@Composable (Int) -> Unit)? = null,
) {
    var currentYear by remember { mutableStateOf(2025) }
    var currentMonth by remember { mutableStateOf(10) } // October
    var selectedDate by remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AntButton(
                onClick = {
                    when (mode) {
                        CalendarMode.Month -> {
                            if (currentMonth == 1) {
                                currentMonth = 12
                                currentYear--
                            } else {
                                currentMonth--
                            }
                        }

                        CalendarMode.Year -> currentYear--
                    }
                },
                size = ButtonSize.Small
            ) {
                Text("◀")
            }

            Text(
                text = when (mode) {
                    CalendarMode.Month -> "${getMonthName(currentMonth)} $currentYear"
                    CalendarMode.Year -> "$currentYear"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            AntButton(
                onClick = {
                    when (mode) {
                        CalendarMode.Month -> {
                            if (currentMonth == 12) {
                                currentMonth = 1
                                currentYear++
                            } else {
                                currentMonth++
                            }
                        }

                        CalendarMode.Year -> currentYear++
                    }
                },
                size = ButtonSize.Small
            ) {
                Text("▶")
            }
        }

        when (mode) {
            CalendarMode.Month -> {
                // Days of week
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }

                // Calendar grid
                val daysInMonth = getDaysInMonth(currentMonth, currentYear)
                val firstDayOfWeek = getFirstDayOfWeek(currentMonth, currentYear)

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    var dayCounter = 1
                    repeat(6) { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(7) { dayOfWeek ->
                                val cellIndex = week * 7 + dayOfWeek
                                val shouldShowDay = cellIndex >= firstDayOfWeek && dayCounter <= daysInMonth

                                if (shouldShowDay) {
                                    val dateString = "${currentYear}-${
                                        currentMonth.toString().padStart(2, '0')
                                    }-${dayCounter.toString().padStart(2, '0')}"
                                    val isSelected = dateString == selectedDate

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .then(
                                                if (isSelected) {
                                                    Modifier.background(Color(0xFF1890FF))
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .border(1.dp, Color(0xFFD9D9D9))
                                            .clickable {
                                                selectedDate = dateString
                                                onSelect?.invoke(dateString)
                                            }
                                            .padding(8.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        dateCellRender?.invoke(dateString) ?: Text(
                                            text = dayCounter.toString(),
                                            fontSize = 14.sp,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }
                                    dayCounter++
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .border(1.dp, Color(0xFFD9D9D9))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            CalendarMode.Year -> {
                // Month grid
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(3) { col ->
                                val month = row * 3 + col + 1
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .background(Color(0xFFFAFAFA))
                                        .border(1.dp, Color(0xFFD9D9D9))
                                        .clickable {
                                            currentMonth = month
                                            onPanelChange?.invoke(
                                                "$currentYear-${month.toString().padStart(2, '0')}",
                                                CalendarMode.Month
                                            )
                                        }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    monthCellRender?.invoke(month) ?: Text(
                                        text = getMonthName(month),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getMonthName(month: Int): String {
    return listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )[month - 1]
}

private fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun getFirstDayOfWeek(month: Int, year: Int): Int {
    // Simplified implementation - would use proper date library in production
    return 2 // Monday start for demo
}
