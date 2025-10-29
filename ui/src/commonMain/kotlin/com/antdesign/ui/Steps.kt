package com.antdesign.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Represents a single step item in the Steps component.
 *
 * @property title Main title text for the step
 * @property description Optional detailed description text displayed below the title
 * @property subTitle Optional subtitle text displayed next to the title (smaller, lighter text)
 * @property icon Optional custom icon to display instead of the default step number/icon
 * @property status Optional status override for this specific step (overrides automatic status calculation)
 * @property disabled Whether this step is disabled and cannot be clicked
 */
data class StepItem(
    val title: String,
    val description: String? = null,
    val subTitle: String? = null,
    val icon: (@Composable () -> Unit)? = null,
    val status: StepStatus? = null,
    val disabled: Boolean = false
)

/**
 * Status of a step in the Steps component.
 */
enum class StepStatus {
    /** Step is waiting to be processed */
    Wait,
    /** Step is currently being processed */
    Process,
    /** Step has been completed successfully */
    Finish,
    /** Step encountered an error */
    Error
}

/**
 * Direction/orientation of the Steps component.
 */
enum class StepsDirection {
    /** Steps are laid out horizontally (left to right) */
    Horizontal,
    /** Steps are laid out vertically (top to bottom) */
    Vertical
}

/**
 * Visual type/variant of the Steps component.
 */
enum class StepsType {
    /** Standard steps with icons and connecting lines */
    Default,
    /** Navigation-style steps with clickable boxes */
    Navigation,
    /** Compact inline steps separated by arrows */
    Inline
}

/**
 * Size variant of the Steps component.
 */
enum class StepsSize {
    /** Default size with standard spacing and icons */
    Default,
    /** Smaller, more compact size */
    Small
}

/**
 * Placement of step labels relative to step icons.
 */
enum class StepsLabelPlacement {
    /** Labels placed horizontally (centered below icons) */
    Horizontal,
    /** Labels placed vertically (aligned to the left) */
    Vertical
}

/**
 * AntSteps component - A navigation bar that guides users through the steps of a task.
 *
 * @param items List of step items to display
 * @param modifier Modifier to be applied to the component
 * @param current Current step index (0-based)
 * @param direction Direction of the steps (Horizontal or Vertical)
 * @param type Type of steps (Default, Navigation, or Inline)
 * @param size Size of the steps (Default or Small)
 * @param labelPlacement Placement of labels (Horizontal or Vertical)
 * @param progressDot Whether to show progress dots instead of numbers/icons
 * @param status Current step status (affects the styling of the current step)
 * @param initial Initial step index (0-based)
 * @param percent Progress percentage for the current step (0-100), shows circular progress indicator
 * @param onChange Callback when a step is clicked (only works if type is Navigation or when custom onClick is needed)
 */
@Composable
fun AntSteps(
    items: List<StepItem>,
    modifier: Modifier = Modifier,
    current: Int = 0,
    direction: StepsDirection = StepsDirection.Horizontal,
    type: StepsType = StepsType.Default,
    size: StepsSize = StepsSize.Default,
    labelPlacement: StepsLabelPlacement = StepsLabelPlacement.Horizontal,
    progressDot: Boolean = false,
    status: StepStatus = StepStatus.Process,
    initial: Int = 0,
    percent: Float? = null,
    onChange: ((Int) -> Unit)? = null
) {
    val theme = useTheme()

    when (type) {
        StepsType.Default -> {
            DefaultSteps(
                items = items,
                current = current,
                direction = direction,
                size = size,
                labelPlacement = labelPlacement,
                progressDot = progressDot,
                status = status,
                percent = percent,
                onChange = onChange,
                theme = theme,
                modifier = modifier
            )
        }
        StepsType.Navigation -> {
            NavigationSteps(
                items = items,
                current = current,
                size = size,
                status = status,
                onChange = onChange,
                theme = theme,
                modifier = modifier
            )
        }
        StepsType.Inline -> {
            InlineSteps(
                items = items,
                current = current,
                status = status,
                onChange = onChange,
                theme = theme,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DefaultSteps(
    items: List<StepItem>,
    current: Int,
    direction: StepsDirection,
    size: StepsSize,
    labelPlacement: StepsLabelPlacement,
    progressDot: Boolean,
    status: StepStatus,
    percent: Float?,
    onChange: ((Int) -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    when (direction) {
        StepsDirection.Horizontal -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items.forEachIndexed { index, item ->
                    DefaultStep(
                        item = item,
                        index = index,
                        current = current,
                        status = item.status ?: when {
                            index < current -> StepStatus.Finish
                            index == current -> status
                            else -> StepStatus.Wait
                        },
                        size = size,
                        isLast = index == items.lastIndex,
                        direction = direction,
                        labelPlacement = labelPlacement,
                        progressDot = progressDot,
                        percent = if (index == current) percent else null,
                        onClick = if (onChange != null && !item.disabled) {{ onChange(index) }} else null,
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        StepsDirection.Vertical -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items.forEachIndexed { index, item ->
                    DefaultStep(
                        item = item,
                        index = index,
                        current = current,
                        status = item.status ?: when {
                            index < current -> StepStatus.Finish
                            index == current -> status
                            else -> StepStatus.Wait
                        },
                        size = size,
                        isLast = index == items.lastIndex,
                        direction = direction,
                        labelPlacement = labelPlacement,
                        progressDot = progressDot,
                        percent = if (index == current) percent else null,
                        onClick = if (onChange != null && !item.disabled) {{ onChange(index) }} else null,
                        theme = theme
                    )
                }
            }
        }
    }
}

@Composable
private fun DefaultStep(
    item: StepItem,
    index: Int,
    current: Int,
    status: StepStatus,
    size: StepsSize,
    isLast: Boolean,
    direction: StepsDirection,
    labelPlacement: StepsLabelPlacement,
    progressDot: Boolean,
    percent: Float?,
    onClick: (() -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    val circleSize = if (progressDot) {
        if (size == StepsSize.Small) 8.dp else 10.dp
    } else {
        if (size == StepsSize.Small) 24.dp else 32.dp
    }

    val iconSize = if (size == StepsSize.Small) 14.sp else 16.sp
    val titleSize = if (size == StepsSize.Small) 14.sp else 16.sp
    val descriptionSize = 14.sp
    val subTitleSize = 12.sp

    val (iconColor, textColor, lineColor) = getStepColors(status, theme)

    when (direction) {
        StepsDirection.Horizontal -> {
            HorizontalStep(
                item = item,
                index = index,
                status = status,
                circleSize = circleSize,
                iconSize = iconSize,
                titleSize = titleSize,
                descriptionSize = descriptionSize,
                subTitleSize = subTitleSize,
                iconColor = iconColor,
                textColor = textColor,
                lineColor = lineColor,
                isLast = isLast,
                labelPlacement = labelPlacement,
                progressDot = progressDot,
                percent = percent,
                onClick = onClick,
                theme = theme,
                modifier = modifier
            )
        }
        StepsDirection.Vertical -> {
            VerticalStep(
                item = item,
                index = index,
                status = status,
                circleSize = circleSize,
                iconSize = iconSize,
                titleSize = titleSize,
                descriptionSize = descriptionSize,
                subTitleSize = subTitleSize,
                iconColor = iconColor,
                textColor = textColor,
                lineColor = lineColor,
                isLast = isLast,
                progressDot = progressDot,
                percent = percent,
                onClick = onClick,
                theme = theme
            )
        }
    }
}

@Composable
private fun HorizontalStep(
    item: StepItem,
    index: Int,
    status: StepStatus,
    circleSize: Dp,
    iconSize: androidx.compose.ui.unit.TextUnit,
    titleSize: androidx.compose.ui.unit.TextUnit,
    descriptionSize: androidx.compose.ui.unit.TextUnit,
    subTitleSize: androidx.compose.ui.unit.TextUnit,
    iconColor: Color,
    textColor: Color,
    lineColor: Color,
    isLast: Boolean,
    labelPlacement: StepsLabelPlacement,
    progressDot: Boolean,
    percent: Float?,
    onClick: (() -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Step icon/number with optional progress indicator
            if (status == StepStatus.Process && percent != null && !progressDot) {
                // Show progress indicator
                Box(
                    modifier = Modifier.size(circleSize),
                    contentAlignment = Alignment.Center
                ) {
                    // Progress circle background
                    Canvas(modifier = Modifier.size(circleSize)) {
                        val strokeWidth = 4.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2

                        // Background circle
                        drawCircle(
                            color = Color(0xFFE8E8E8),
                            radius = radius,
                            style = Stroke(width = strokeWidth)
                        )

                        // Progress arc
                        val sweepAngle = (percent.coerceIn(0f, 100f) / 100f) * 360f
                        drawArc(
                            color = theme.token.colorPrimary,
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Inner icon/number circle
                    Box(
                        modifier = Modifier
                            .size(circleSize - 8.dp)
                            .clip(CircleShape)
                            .background(iconColor)
                            .then(
                                if (onClick != null) {
                                    Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onClick
                                    )
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        StepIcon(
                            item = item,
                            index = index,
                            status = status,
                            iconSize = iconSize,
                            progressDot = progressDot
                        )
                    }
                }
            } else {
                // Standard step icon without progress
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(iconColor)
                        .then(
                            if (onClick != null) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClick
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    StepIcon(
                        item = item,
                        index = index,
                        status = status,
                        iconSize = iconSize,
                        progressDot = progressDot
                    )
                }
            }

            // Connection line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(if (progressDot) 1.dp else 2.dp)
                        .background(lineColor)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Labels
        Column(
            horizontalAlignment = when (labelPlacement) {
                StepsLabelPlacement.Horizontal -> Alignment.CenterHorizontally
                StepsLabelPlacement.Vertical -> Alignment.Start
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    color = textColor,
                    fontSize = titleSize,
                    fontWeight = if (status == StepStatus.Process) FontWeight.Bold else FontWeight.Normal
                )

                if (item.subTitle != null) {
                    Text(
                        text = item.subTitle,
                        color = Color.Gray,
                        fontSize = subTitleSize
                    )
                }
            }

            if (item.description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    color = Color(0xFF8C8C8C),
                    fontSize = descriptionSize
                )
            }
        }
    }
}

@Composable
private fun VerticalStep(
    item: StepItem,
    index: Int,
    status: StepStatus,
    circleSize: Dp,
    iconSize: androidx.compose.ui.unit.TextUnit,
    titleSize: androidx.compose.ui.unit.TextUnit,
    descriptionSize: androidx.compose.ui.unit.TextUnit,
    subTitleSize: androidx.compose.ui.unit.TextUnit,
    iconColor: Color,
    textColor: Color,
    lineColor: Color,
    isLast: Boolean,
    progressDot: Boolean,
    percent: Float?,
    onClick: (() -> Unit)?,
    theme: AntThemeConfig
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step icon/number with optional progress indicator
            if (status == StepStatus.Process && percent != null && !progressDot) {
                // Show progress indicator
                Box(
                    modifier = Modifier.size(circleSize),
                    contentAlignment = Alignment.Center
                ) {
                    // Progress circle background
                    Canvas(modifier = Modifier.size(circleSize)) {
                        val strokeWidth = 4.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2

                        // Background circle
                        drawCircle(
                            color = Color(0xFFE8E8E8),
                            radius = radius,
                            style = Stroke(width = strokeWidth)
                        )

                        // Progress arc
                        val sweepAngle = (percent.coerceIn(0f, 100f) / 100f) * 360f
                        drawArc(
                            color = theme.token.colorPrimary,
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Inner icon/number circle
                    Box(
                        modifier = Modifier
                            .size(circleSize - 8.dp)
                            .clip(CircleShape)
                            .background(iconColor)
                            .then(
                                if (onClick != null) {
                                    Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onClick
                                    )
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        StepIcon(
                            item = item,
                            index = index,
                            status = status,
                            iconSize = iconSize,
                            progressDot = progressDot
                        )
                    }
                }
            } else {
                // Standard step icon without progress
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(iconColor)
                        .then(
                            if (onClick != null) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClick
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    StepIcon(
                        item = item,
                        index = index,
                        status = status,
                        iconSize = iconSize,
                        progressDot = progressDot
                    )
                }
            }

            // Connection line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(if (progressDot) 1.dp else 2.dp)
                        .height(if (item.description != null) 60.dp else 40.dp)
                        .background(lineColor)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Labels
        Column(
            modifier = Modifier.padding(bottom = if (!isLast) 16.dp else 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    color = textColor,
                    fontSize = titleSize,
                    fontWeight = if (status == StepStatus.Process) FontWeight.Bold else FontWeight.Normal
                )

                if (item.subTitle != null) {
                    Text(
                        text = item.subTitle,
                        color = Color.Gray,
                        fontSize = subTitleSize
                    )
                }
            }

            if (item.description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    color = Color(0xFF8C8C8C),
                    fontSize = descriptionSize
                )
            }
        }
    }
}

@Composable
private fun StepIcon(
    item: StepItem,
    index: Int,
    status: StepStatus,
    iconSize: androidx.compose.ui.unit.TextUnit,
    progressDot: Boolean
) {
    if (progressDot) {
        // For progress dot, we don't show any icon inside
        return
    }

    if (item.icon != null) {
        item.icon.invoke()
    } else {
        when (status) {
            StepStatus.Finish -> {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = iconSize,
                    fontWeight = FontWeight.Bold
                )
            }
            StepStatus.Error -> {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = iconSize,
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {
                Text(
                    text = (index + 1).toString(),
                    color = Color.White,
                    fontSize = iconSize,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun NavigationSteps(
    items: List<StepItem>,
    current: Int,
    size: StepsSize,
    status: StepStatus,
    onChange: ((Int) -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEachIndexed { index, item ->
            val itemStatus = item.status ?: when {
                index < current -> StepStatus.Finish
                index == current -> status
                else -> StepStatus.Wait
            }

            NavigationStep(
                item = item,
                index = index,
                status = itemStatus,
                size = size,
                onClick = if (onChange != null && !item.disabled) {{ onChange(index) }} else null,
                theme = theme,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavigationStep(
    item: StepItem,
    index: Int,
    status: StepStatus,
    size: StepsSize,
    onClick: (() -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    val height = if (size == StepsSize.Small) 40.dp else 48.dp
    val (backgroundColor, textColor) = when (status) {
        StepStatus.Finish -> Color(0xFFE6F7FF) to theme.token.colorPrimary
        StepStatus.Process -> theme.token.colorPrimary to Color.White
        StepStatus.Error -> Color(0xFFFFE6E6) to theme.token.colorError
        StepStatus.Wait -> Color(0xFFFAFAFA) to Color(0xFF8C8C8C)
    }

    Box(
        modifier = modifier
            .height(height)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    color = textColor,
                    fontSize = if (size == StepsSize.Small) 14.sp else 16.sp,
                    fontWeight = if (status == StepStatus.Process) FontWeight.Bold else FontWeight.Normal
                )

                if (item.subTitle != null) {
                    Text(
                        text = item.subTitle,
                        color = textColor.copy(alpha = 0.65f),
                        fontSize = 12.sp
                    )
                }
            }

            if (item.description != null) {
                Text(
                    text = item.description,
                    color = textColor.copy(alpha = 0.65f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun InlineSteps(
    items: List<StepItem>,
    current: Int,
    status: StepStatus,
    onChange: ((Int) -> Unit)?,
    theme: AntThemeConfig,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val itemStatus = item.status ?: when {
                index < current -> StepStatus.Finish
                index == current -> status
                else -> StepStatus.Wait
            }

            InlineStep(
                item = item,
                index = index,
                status = itemStatus,
                onClick = if (onChange != null && !item.disabled) {{ onChange(index) }} else null,
                theme = theme
            )

            if (index < items.lastIndex) {
                Text(
                    text = ">",
                    color = Color(0xFFD9D9D9),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun InlineStep(
    item: StepItem,
    index: Int,
    status: StepStatus,
    onClick: (() -> Unit)?,
    theme: AntThemeConfig
) {
    val textColor = when (status) {
        StepStatus.Finish -> theme.token.colorSuccess
        StepStatus.Process -> theme.token.colorPrimary
        StepStatus.Error -> theme.token.colorError
        StepStatus.Wait -> Color(0xFF8C8C8C)
    }

    Text(
        text = item.title,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = if (status == StepStatus.Process) FontWeight.Bold else FontWeight.Normal,
        modifier = if (onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        }
    )
}

private fun getStepColors(status: StepStatus, theme: AntThemeConfig): Triple<Color, Color, Color> {
    return when (status) {
        StepStatus.Finish -> Triple(
            theme.token.colorSuccess,
            Color(0xFF8C8C8C),
            theme.token.colorSuccess
        )
        StepStatus.Process -> Triple(
            theme.token.colorPrimary,
            Color(0xFF000000),
            theme.token.colorPrimary
        )
        StepStatus.Error -> Triple(
            theme.token.colorError,
            theme.token.colorError,
            theme.token.colorError
        )
        StepStatus.Wait -> Triple(
            Color(0xFFD9D9D9),
            Color(0xFF8C8C8C),
            Color(0xFFD9D9D9)
        )
    }
}

@Deprecated("Use StepItem instead", ReplaceWith("StepItem(title, description, null, icon)"))
data class OldStepItem(
    val title: String,
    val description: String? = null,
    val icon: (@Composable () -> Unit)? = null
)
