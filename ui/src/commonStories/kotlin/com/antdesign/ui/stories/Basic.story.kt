package com.antdesign.ui.stories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antdesign.ui.*
import org.jetbrains.compose.storytale.story

val Button by story {
    val text by parameter("Click Me")
    val disabled by parameter(false)
    val danger by parameter(false)
    val loading by parameter(false)
    val ghost by parameter(false)
    val block by parameter(false)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Type variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None,
                ghost = ghost,
                block = block
            ) {
                Text("Primary")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Default,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None,
                ghost = ghost
            ) {
                Text("Default")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Dashed,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None,
                ghost = ghost
            ) {
                Text("Dashed")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Link,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Link")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Text,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Text")
            }
        }

        Text("Variant examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                variant = ButtonVariant.Solid,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Solid")
            }

            AntButton(
                onClick = {},
                variant = ButtonVariant.Outlined,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Outlined")
            }

            AntButton(
                onClick = {},
                variant = ButtonVariant.Dashed,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Dashed")
            }

            AntButton(
                onClick = {},
                variant = ButtonVariant.Text,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Text")
            }

            AntButton(
                onClick = {},
                variant = ButtonVariant.Link,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Link")
            }
        }

        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Large,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Large")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Middle,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Middle")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                size = ButtonSize.Small,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Small")
            }
        }

        Text("Shapes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Default,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Default")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Round,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Round")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("O")
            }
        }

        Text("Buttons with icons - Start position:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                icon = { Text("🔍", fontSize = 14.sp) },
                iconPosition = IconPosition.Start,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Search")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Default,
                icon = { Text("⬇", fontSize = 14.sp) },
                iconPosition = IconPosition.Start,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Download")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                icon = { Text("✓", fontSize = 14.sp) },
                iconPosition = IconPosition.Start,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Save")
            }
        }

        Text("Buttons with icons - End position:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                icon = { Text("→", fontSize = 14.sp) },
                iconPosition = IconPosition.End,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Next")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Default,
                icon = { Text("↗", fontSize = 14.sp) },
                iconPosition = IconPosition.End,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Open")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Link,
                icon = { Text("⚙", fontSize = 14.sp) },
                iconPosition = IconPosition.End,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("Settings")
            }
        }

        Text("Icon-only buttons:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                icon = { Text("🔍", fontSize = 14.sp) },
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Default,
                shape = ButtonShape.Circle,
                icon = { Text("✏", fontSize = 14.sp) },
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("")
            }

            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                shape = ButtonShape.Circle,
                icon = { Text("❤", fontSize = 14.sp) },
                danger = danger,
                disabled = disabled,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text("")
            }
        }

        if (block) {
            Text("Block button:")
            AntButton(
                onClick = {},
                type = ButtonType.Primary,
                block = true,
                disabled = disabled,
                danger = danger,
                loading = if (loading) ButtonLoading.Simple() else ButtonLoading.None
            ) {
                Text(text)
            }
        }
    }
}

val Input by story {
    var value by parameter("Input text")
    val placeholder by parameter("Enter text...")
    val disabled by parameter(false)
    val allowClear by parameter(false)
    val showCount by parameter(false)
    val password by parameter(false)
    var maxLength by parameter(0)
    var clearCount by remember { mutableStateOf(0) }
    var focusCount by remember { mutableStateOf(0) }
    var blurCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sizes:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Large,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password
        )

        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Middle,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password
        )

        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            size = InputSize.Small,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null,
            password = password
        )

        Text("Status:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Default status",
            status = InputStatus.Default,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Error status",
            status = InputStatus.Error,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Warning status",
            status = InputStatus.Warning,
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("With prefix/suffix:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            prefix = { Text("https://") },
            suffix = { Text(".com") },
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("With addons:")
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            addonBefore = { Text("https://") },
            addonAfter = { Text(".com") },
            disabled = disabled,
            allowClear = allowClear,
            showCount = showCount,
            maxLength = if (maxLength > 0) maxLength else null
        )

        Text("With callbacks (onClear, onFocus, onBlur):")
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Clear count: $clearCount", fontSize = 12.sp)
            Text("Focus count: $focusCount", fontSize = 12.sp)
            Text("Blur count: $blurCount", fontSize = 12.sp)
        }
        AntInput(
            value = value,
            onValueChange = { value = it },
            placeholder = "Input with callbacks",
            allowClear = true,
            onClear = { clearCount++ },
            onFocus = { focusCount++ },
            onBlur = { blurCount++ }
        )
    }
}

val Checkbox by story {
    var checked by parameter(false)
    val disabled by parameter(false)
    val indeterminate by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default checkbox:")
        AntCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            disabled = disabled,
            indeterminate = indeterminate,
            children = { Text("Checkbox") }
        )

        Text("Indeterminate state examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntCheckbox(
                checked = false,
                onCheckedChange = {},
                indeterminate = false,
                children = { Text("Unchecked") }
            )
            AntCheckbox(
                checked = false,
                onCheckedChange = {},
                indeterminate = true,
                children = { Text("Indeterminate") }
            )
            AntCheckbox(
                checked = true,
                onCheckedChange = {},
                indeterminate = false,
                children = { Text("Checked") }
            )
        }

        Text("Indeterminate use case (Select All):")
        var parentChecked by remember { mutableStateOf(false) }
        var parentIndeterminate by remember { mutableStateOf(false) }
        var option1 by remember { mutableStateOf(false) }
        var option2 by remember { mutableStateOf(false) }
        var option3 by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntCheckbox(
                checked = parentChecked,
                onCheckedChange = { newChecked ->
                    parentChecked = newChecked
                    parentIndeterminate = false
                    option1 = newChecked
                    option2 = newChecked
                    option3 = newChecked
                },
                indeterminate = parentIndeterminate,
                children = { Text("Select All") }
            )
            Column(modifier = Modifier.padding(start = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntCheckbox(
                    checked = option1,
                    onCheckedChange = {
                        option1 = it
                        val checkedCount = listOf(it, option2, option3).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 1") }
                )
                AntCheckbox(
                    checked = option2,
                    onCheckedChange = {
                        option2 = it
                        val checkedCount = listOf(option1, it, option3).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 2") }
                )
                AntCheckbox(
                    checked = option3,
                    onCheckedChange = {
                        option3 = it
                        val checkedCount = listOf(option1, option2, it).count { c -> c }
                        parentChecked = checkedCount == 3
                        parentIndeterminate = checkedCount in 1..2
                    },
                    children = { Text("Option 3") }
                )
            }
        }
    }
}

val Switch by story {
    var checked by parameter(false)
    val disabled by parameter(false)
    val loading by parameter(false)
    val checkedChildren by parameter("")
    val unCheckedChildren by parameter("")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default size:")
        AntSwitch(
            checked = checked,
            onChange = { checked = it },
            disabled = disabled,
            loading = loading,
            size = ComponentSize.Middle,
            checkedChildren = if (checkedChildren.isNotEmpty()) { { Text(checkedChildren) } } else null,
            unCheckedChildren = if (unCheckedChildren.isNotEmpty()) { { Text(unCheckedChildren) } } else null
        )

        Text("Small size:")
        AntSwitch(
            checked = checked,
            onChange = { checked = it },
            disabled = disabled,
            loading = loading,
            size = ComponentSize.Small,
            checkedChildren = if (checkedChildren.isNotEmpty()) { { Text(checkedChildren) } } else null,
            unCheckedChildren = if (unCheckedChildren.isNotEmpty()) { { Text(unCheckedChildren) } } else null
        )
    }
}

val Alert by story {
    val message by parameter("Success Text")
    val description by parameter("")
    val closable by parameter(false)
    val showIcon by parameter(false)
    val banner by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Alerts:")
        AntAlert(
            message = message,
            type = AlertType.Success,
            description = description.ifEmpty { null },
            closable = closable,
            showIcon = showIcon,
            banner = banner
        )

        AntAlert(
            message = message,
            type = AlertType.Info,
            description = description.ifEmpty { null },
            closable = closable,
            showIcon = showIcon,
            banner = banner
        )

        AntAlert(
            message = message,
            type = AlertType.Warning,
            description = description.ifEmpty { null },
            closable = closable,
            showIcon = showIcon,
            banner = banner
        )

        AntAlert(
            message = message,
            type = AlertType.Error,
            description = description.ifEmpty { null },
            closable = closable,
            showIcon = showIcon,
            banner = banner
        )

        Text("Alerts with custom icons:")
        AntAlert(
            message = "Custom Success Icon",
            type = AlertType.Success,
            description = "This alert uses a custom icon instead of default",
            icon = { Text("✅", fontSize = 16.sp) },
            showIcon = true,
            closable = closable
        )

        AntAlert(
            message = "Custom Info Icon",
            type = AlertType.Info,
            description = "Information with a custom icon",
            icon = { Text("📢", fontSize = 16.sp) },
            showIcon = true,
            closable = closable
        )

        AntAlert(
            message = "Custom Warning Icon",
            type = AlertType.Warning,
            description = "Warning with a custom icon",
            icon = { Text("🔔", fontSize = 16.sp) },
            showIcon = true,
            closable = closable
        )

        Text("Alerts with custom actions:")
        AntAlert(
            message = "Alert with Action Button",
            type = AlertType.Success,
            description = "This alert has a custom action button",
            showIcon = true,
            action = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Text,
                    size = ButtonSize.Small
                ) {
                    Text("Undo", fontSize = 12.sp)
                }
            }
        )

        AntAlert(
            message = "Error with Retry Action",
            type = AlertType.Error,
            description = "Something went wrong. You can retry the operation.",
            showIcon = true,
            closable = true,
            action = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Link,
                    size = ButtonSize.Small,
                    danger = true
                ) {
                    Text("Retry", fontSize = 12.sp)
                }
            }
        )

        AntAlert(
            message = "Info with Learn More",
            type = AlertType.Info,
            description = "Check out the documentation for more details",
            showIcon = true,
            action = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Link,
                    size = ButtonSize.Small
                ) {
                    Text("Learn More", fontSize = 12.sp)
                }
            }
        )

        Text("Alert with both custom icon and action:")
        AntAlert(
            message = "Complex Alert Example",
            type = AlertType.Warning,
            description = "This alert demonstrates custom icon and action together",
            icon = { Text("⚡", fontSize = 16.sp) },
            showIcon = true,
            closable = true,
            action = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(
                        onClick = {},
                        type = ButtonType.Text,
                        size = ButtonSize.Small
                    ) {
                        Text("Dismiss", fontSize = 12.sp)
                    }
                    AntButton(
                        onClick = {},
                        type = ButtonType.Link,
                        size = ButtonSize.Small
                    ) {
                        Text("Details", fontSize = 12.sp)
                    }
                }
            }
        )
    }
}

val Tag by story {
    val text by parameter("Tag")
    val bordered by parameter(true)
    val closable by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Color variants:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTag(
                text = text,
                color = TagColor.Default,
                bordered = bordered,
                closable = closable
            )
            AntTag(
                text = text,
                color = TagColor.Success,
                bordered = bordered,
                closable = closable
            )
            AntTag(
                text = text,
                color = TagColor.Processing,
                bordered = bordered,
                closable = closable
            )
            AntTag(
                text = text,
                color = TagColor.Error,
                bordered = bordered,
                closable = closable
            )
            AntTag(
                text = text,
                color = TagColor.Warning,
                bordered = bordered,
                closable = closable
            )
        }

        Text("Tags with icons:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTag(
                text = "Success",
                color = TagColor.Success,
                bordered = bordered,
                icon = { Text("✓", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Info",
                color = TagColor.Processing,
                bordered = bordered,
                icon = { Text("ℹ", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Warning",
                color = TagColor.Warning,
                bordered = bordered,
                icon = { Text("⚠", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Error",
                color = TagColor.Error,
                bordered = bordered,
                icon = { Text("✕", fontSize = 10.sp) },
                closable = closable
            )
        }

        Text("Tags with various icons:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTag(
                text = "Home",
                color = TagColor.Default,
                bordered = bordered,
                icon = { Text("🏠", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "User",
                color = TagColor.Processing,
                bordered = bordered,
                icon = { Text("👤", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Star",
                color = TagColor.Warning,
                bordered = bordered,
                icon = { Text("⭐", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Heart",
                color = TagColor.Error,
                bordered = bordered,
                icon = { Text("❤", fontSize = 10.sp) },
                closable = closable
            )
            AntTag(
                text = "Check",
                color = TagColor.Success,
                bordered = bordered,
                icon = { Text("✅", fontSize = 10.sp) },
                closable = closable
            )
        }

        Text("Tags with icons and closable:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTag(
                text = "Tag 1",
                color = TagColor.Default,
                bordered = bordered,
                icon = { Text("📌", fontSize = 10.sp) },
                closable = true
            )
            AntTag(
                text = "Tag 2",
                color = TagColor.Success,
                bordered = bordered,
                icon = { Text("🔖", fontSize = 10.sp) },
                closable = true
            )
            AntTag(
                text = "Tag 3",
                color = TagColor.Processing,
                bordered = bordered,
                icon = { Text("🏷", fontSize = 10.sp) },
                closable = true
            )
        }
    }
}

val Badge by story {
    val count by parameter(5)
    val showZero by parameter(false)
    val overflowCount by parameter(99)
    val dot by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Badge with count:")
        AntBadge(
            count = count,
            showZero = showZero,
            overflowCount = overflowCount,
            dot = dot
        ) {
            AntButton(onClick = {}, type = ButtonType.Primary) {
                Text("Button")
            }
        }

        Text("Badge with showZero parameter:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntBadge(
                count = 0,
                showZero = false
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("No Badge")
                }
            }
            AntBadge(
                count = 0,
                showZero = true
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Show Zero")
                }
            }
        }

        Text("Badge dot mode:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntBadge(
                dot = true,
                status = BadgeStatus.Success
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Success Dot")
                }
            }
            AntBadge(
                dot = true,
                status = BadgeStatus.Processing
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Processing Dot")
                }
            }
            AntBadge(
                dot = true,
                status = BadgeStatus.Error
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Error Dot")
                }
            }
            AntBadge(
                dot = true,
                status = BadgeStatus.Warning
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Warning Dot")
                }
            }
        }

        Text("Badge with status:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntBadge(status = BadgeStatus.Success, text = "Success")
            AntBadge(status = BadgeStatus.Processing, text = "Processing")
            AntBadge(status = BadgeStatus.Error, text = "Error")
            AntBadge(status = BadgeStatus.Warning, text = "Warning")
            AntBadge(status = BadgeStatus.Default, text = "Default")
        }

        Text("Badge with custom colors:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntBadge(
                count = 5,
                color = Color(0xFF52C41A)
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Green")
                }
            }
            AntBadge(
                count = 5,
                color = Color(0xFF1890FF)
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Blue")
                }
            }
            AntBadge(
                count = 5,
                color = Color(0xFF722ED1)
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Purple")
                }
            }
            AntBadge(
                count = 5,
                color = Color(0xFFFAAD14)
            ) {
                AntButton(onClick = {}, type = ButtonType.Default) {
                    Text("Orange")
                }
            }
        }

        Text("Badge with offset:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntBadge(
                count = 10,
                offset = Pair(10.dp, (-5).dp)
            ) {
                AntButton(onClick = {}, type = ButtonType.Primary) {
                    Text("Offset Badge")
                }
            }
        }
    }
}

val Avatar by story {
    val text by parameter("U")
    val src by parameter("")
    val backgroundColor by parameter(Color(0xFF1890FF))
    val textColor by parameter(Color.White)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sizes:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntAvatar(
                text = text,
                size = AvatarSize.Large,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            AntAvatar(
                text = text,
                size = AvatarSize.Default,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            AntAvatar(
                text = text,
                size = AvatarSize.Small,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
        }

        Text("Shapes:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntAvatar(
                text = text,
                shape = AvatarShape.Circle,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            AntAvatar(
                text = text,
                shape = AvatarShape.Square,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
        }

        Text("With icon:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntAvatar(
                icon = { Text("👤", fontSize = 20.sp) },
                size = AvatarSize.Large,
                backgroundColor = backgroundColor
            )
            AntAvatar(
                icon = { Text("👤", fontSize = 16.sp) },
                size = AvatarSize.Default,
                backgroundColor = backgroundColor
            )
            AntAvatar(
                icon = { Text("👤", fontSize = 12.sp) },
                size = AvatarSize.Small,
                backgroundColor = backgroundColor
            )
        }

        Text("With image src:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntAvatar(
                src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=1",
                alt = "User",
                size = AvatarSize.Large,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            AntAvatar(
                src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=2",
                alt = "User",
                size = AvatarSize.Default,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            AntAvatar(
                src = if (src.isNotEmpty()) src else "https://i.pravatar.cc/150?img=3",
                alt = "User",
                size = AvatarSize.Small,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
        }

        Text("Custom colors:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntAvatar(
                text = "A",
                backgroundColor = Color(0xFFFF4D4F),
                textColor = Color.White
            )
            AntAvatar(
                text = "B",
                backgroundColor = Color(0xFF52C41A),
                textColor = Color.White
            )
            AntAvatar(
                text = "C",
                backgroundColor = Color(0xFFFAAD14),
                textColor = Color.White
            )
            AntAvatar(
                text = "D",
                backgroundColor = Color(0xFF722ED1),
                textColor = Color.White
            )
        }
    }
}

val AvatarGroup by story {
    val maxCount by parameter(3)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Avatar Group - Default size:")
        AntAvatarGroup(
            size = AvatarSize.Default,
            content = {
                AntAvatar(
                    text = "A",
                    backgroundColor = Color(0xFFFF4D4F),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "B",
                    backgroundColor = Color(0xFF52C41A),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "C",
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "D",
                    backgroundColor = Color(0xFFFAAD14),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "E",
                    backgroundColor = Color(0xFF722ED1),
                    textColor = Color.White
                )
            }
        )

        Text("Avatar Group - Large size:")
        AntAvatarGroup(
            size = AvatarSize.Large,
            content = {
                AntAvatar(
                    icon = { Text("👤", fontSize = 20.sp) },
                    size = AvatarSize.Large,
                    backgroundColor = Color(0xFF1890FF)
                )
                AntAvatar(
                    icon = { Text("👤", fontSize = 20.sp) },
                    size = AvatarSize.Large,
                    backgroundColor = Color(0xFF52C41A)
                )
                AntAvatar(
                    icon = { Text("👤", fontSize = 20.sp) },
                    size = AvatarSize.Large,
                    backgroundColor = Color(0xFFFF4D4F)
                )
            }
        )

        Text("Avatar Group - Small size:")
        AntAvatarGroup(
            size = AvatarSize.Small,
            content = {
                AntAvatar(
                    text = "U1",
                    size = AvatarSize.Small,
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "U2",
                    size = AvatarSize.Small,
                    backgroundColor = Color(0xFF52C41A),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "U3",
                    size = AvatarSize.Small,
                    backgroundColor = Color(0xFFFF4D4F),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "U4",
                    size = AvatarSize.Small,
                    backgroundColor = Color(0xFFFAAD14),
                    textColor = Color.White
                )
            }
        )

        Text("Avatar Group - With maxCount:")
        AntAvatarGroup(
            maxCount = maxCount,
            size = AvatarSize.Default,
            content = {
                AntAvatar(
                    text = "A",
                    backgroundColor = Color(0xFFFF4D4F),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "B",
                    backgroundColor = Color(0xFF52C41A),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "C",
                    backgroundColor = Color(0xFF1890FF),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "D",
                    backgroundColor = Color(0xFFFAAD14),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "E",
                    backgroundColor = Color(0xFF722ED1),
                    textColor = Color.White
                )
                AntAvatar(
                    text = "F",
                    backgroundColor = Color(0xFFEB2F96),
                    textColor = Color.White
                )
            }
        )
    }
}

val Progress by story {
    val percent by parameter(50)
    val showInfo by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Line progress - Normal:")
        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo
        )

        Text("Line progress - Success:")
        AntProgress(
            percent = 100.0,
            type = ProgressType.Line,
            status = ProgressStatus.Success,
            showInfo = showInfo
        )

        Text("Line progress - Exception:")
        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Exception,
            showInfo = showInfo
        )

        Text("Line progress with custom stroke width:")
        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeWidth = 4.dp
        )

        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeWidth = 12.dp
        )

        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeWidth = 20.dp
        )

        Text("Line progress with custom colors:")
        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFF52C41A),
            trailColor = Color(0xFFE8F5E9),
            strokeWidth = 10.dp
        )

        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFFFF4D4F),
            trailColor = Color(0xFFFFEBEE),
            strokeWidth = 10.dp
        )

        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFFFAAD14),
            trailColor = Color(0xFFFFF8E1),
            strokeWidth = 10.dp
        )

        AntProgress(
            percent = percent.toDouble(),
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFF722ED1),
            trailColor = Color(0xFFF3E5F5),
            strokeWidth = 10.dp
        )

        Text("Combined: Custom width and colors:")
        AntProgress(
            percent = 75.0,
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFF1890FF),
            trailColor = Color(0xFFE3F2FD),
            strokeWidth = 16.dp
        )

        AntProgress(
            percent = 30.0,
            type = ProgressType.Line,
            status = ProgressStatus.Normal,
            showInfo = showInfo,
            strokeColor = Color(0xFFEB2F96),
            trailColor = Color(0xFFFCE4EC),
            strokeWidth = 6.dp
        )

        Text("Circle progress - Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntProgress(
                percent = percent.toDouble(),
                type = ProgressType.Circle,
                size = ProgressSize.Small,
                showInfo = showInfo
            )
            AntProgress(
                percent = percent.toDouble(),
                type = ProgressType.Circle,
                size = ProgressSize.Default,
                showInfo = showInfo
            )
            AntProgress(
                percent = percent.toDouble(),
                type = ProgressType.Circle,
                size = 140.dp,
                showInfo = showInfo
            )
        }

        Text("Circle progress with custom colors:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntProgress(
                percent = 65.0,
                type = ProgressType.Circle,
                size = ProgressSize.Default,
                showInfo = showInfo,
                strokeColor = Color(0xFF52C41A),
                trailColor = Color(0xFFE8F5E9)
            )
            AntProgress(
                percent = 80.0,
                type = ProgressType.Circle,
                size = ProgressSize.Default,
                showInfo = showInfo,
                strokeColor = Color(0xFFFF4D4F),
                trailColor = Color(0xFFFFEBEE)
            )
            AntProgress(
                percent = 45.0,
                type = ProgressType.Circle,
                size = ProgressSize.Default,
                showInfo = showInfo,
                strokeColor = Color(0xFF722ED1),
                trailColor = Color(0xFFF3E5F5)
            )
        }
    }
}

val Divider by story {
    val dashed by parameter(false)
    val plain by parameter(false)
    val text by parameter("")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Horizontal divider:")
        Text("Text above")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            dashed = dashed
        )
        Text("Text below")

        Text("Divider with text:")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Center,
            dashed = dashed,
            plain = plain,
            children = { Text(text.ifEmpty { "Center Text" }) }
        )

        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Left,
            dashed = dashed,
            plain = plain,
            children = { Text(text.ifEmpty { "Left Text" }) }
        )

        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Right,
            dashed = dashed,
            plain = plain,
            children = { Text(text.ifEmpty { "Right Text" }) }
        )

        Text("Dividers with custom colors:")
        Text("Default color")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFFD9D9D9)
        )

        Text("Blue divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFF1890FF)
        )

        Text("Green divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFF52C41A)
        )

        Text("Red divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFFFF4D4F)
        )

        Text("Purple divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFF722ED1)
        )

        Text("Dividers with custom thickness:")
        Text("Thin (1dp)")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            thickness = 1.dp,
            color = Color(0xFF1890FF)
        )

        Text("Medium (3dp)")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            thickness = 3.dp,
            color = Color(0xFF1890FF)
        )

        Text("Thick (5dp)")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            thickness = 5.dp,
            color = Color(0xFF1890FF)
        )

        Text("Extra thick (8dp)")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            thickness = 8.dp,
            color = Color(0xFF1890FF)
        )

        Text("Combined: Custom color and thickness:")
        Text("Thin green divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFF52C41A),
            thickness = 2.dp
        )

        Text("Thick red divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFFFF4D4F),
            thickness = 6.dp
        )

        Text("Medium purple divider")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            color = Color(0xFF722ED1),
            thickness = 4.dp
        )

        Text("Dividers with text, custom color and thickness:")
        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Center,
            color = Color(0xFF1890FF),
            thickness = 2.dp,
            plain = plain,
            children = { Text("Custom Blue") }
        )

        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Left,
            color = Color(0xFF52C41A),
            thickness = 3.dp,
            plain = plain,
            children = { Text("Custom Green") }
        )

        AntDivider(
            orientation = DividerOrientation.Horizontal,
            textAlign = DividerTextAlign.Right,
            color = Color(0xFFFAAD14),
            thickness = 2.dp,
            plain = plain,
            children = { Text("Custom Orange") }
        )
    }
}

val Spin by story {
    val spinning by parameter(true)
    val tip by parameter("")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Large,
                tip = tip.ifEmpty { null }
            )
            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = tip.ifEmpty { null }
            )
            AntSpin(
                spinning = spinning,
                size = SpinSize.Small,
                tip = tip.ifEmpty { null }
            )
        }

        Text("With delay (500ms):")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                delay = 500,
                tip = "Loading with delay..."
            )
        }

        Text("With longer delay (1000ms):")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                delay = 1000,
                tip = "1 second delay"
            )
        }

        Text("Custom indicator examples:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Custom spinner",
                indicator = {
                    Text("⟳", fontSize = 32.sp, color = Color(0xFF1890FF))
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Loading...",
                indicator = {
                    Text("⌛", fontSize = 32.sp)
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Processing",
                indicator = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("●", fontSize = 24.sp, color = Color(0xFF52C41A))
                        Text("●", fontSize = 24.sp, color = Color(0xFF1890FF))
                        Text("●", fontSize = 24.sp, color = Color(0xFFFF4D4F))
                    }
                }
            )
        }

        Text("Custom indicators with different sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Large,
                indicator = {
                    Text("🔄", fontSize = 40.sp)
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                indicator = {
                    Text("⚙", fontSize = 32.sp, color = Color(0xFF722ED1))
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Small,
                indicator = {
                    Text("↻", fontSize = 20.sp, color = Color(0xFFFAAD14))
                }
            )
        }

        Text("With content:")
        AntSpin(
            spinning = spinning,
            tip = tip.ifEmpty { null },
            children = {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text("Content inside spin")
                    Text("More content...")
                }
            }
        )

        Text("Custom indicator with content and delay:")
        AntSpin(
            spinning = spinning,
            delay = 300,
            tip = "Loading content...",
            indicator = {
                Text("🔃", fontSize = 32.sp, color = Color(0xFF1890FF))
            },
            children = {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text("This content appears with a custom spinner")
                    Text("Delay: 300ms")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AntButton(onClick = {}, type = ButtonType.Primary) {
                            Text("Action")
                        }
                        AntButton(onClick = {}, type = ButtonType.Default) {
                            Text("Cancel")
                        }
                    }
                }
            }
        )

        Text("Multiple custom indicators:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Circular",
                indicator = {
                    Text("◐", fontSize = 32.sp, color = Color(0xFFEB2F96))
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Square",
                indicator = {
                    Text("◻", fontSize = 32.sp, color = Color(0xFF13C2C2))
                }
            )

            AntSpin(
                spinning = spinning,
                size = SpinSize.Default,
                tip = "Diamond",
                indicator = {
                    Text("◆", fontSize = 32.sp, color = Color(0xFF52C41A))
                }
            )
        }
    }
}

val TextArea by story {
    var value by parameter("This is a text area with multiple lines.\nYou can edit this text.")
    val placeholder by parameter("Enter your text here...")
    val disabled by parameter(false)
    val rows by parameter(3)
    val maxLength by parameter(0)
    val showCount by parameter(false)
    val autoSize by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default TextArea:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            disabled = disabled,
            rows = rows,
            maxLength = if (maxLength > 0) maxLength else null,
            showCount = showCount,
            autoSize = autoSize,
            modifier = Modifier.width(400.dp)
        )

        Text("With character count:")
        AntTextArea(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            disabled = disabled,
            rows = rows,
            maxLength = 200,
            showCount = true,
            autoSize = autoSize,
            modifier = Modifier.width(400.dp)
        )

        Text("Disabled state:")
        AntTextArea(
            value = "This text area is disabled",
            onValueChange = {},
            placeholder = placeholder,
            disabled = true,
            rows = rows,
            maxLength = if (maxLength > 0) maxLength else null,
            showCount = showCount,
            autoSize = autoSize,
            modifier = Modifier.width(400.dp)
        )

        Text("Different row sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("2 rows")
                AntTextArea(
                    value = value,
                    onValueChange = { value = it },
                    placeholder = placeholder,
                    disabled = disabled,
                    rows = 2,
                    maxLength = if (maxLength > 0) maxLength else null,
                    showCount = showCount,
                    autoSize = autoSize,
                    modifier = Modifier.width(180.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("5 rows")
                AntTextArea(
                    value = value,
                    onValueChange = { value = it },
                    placeholder = placeholder,
                    disabled = disabled,
                    rows = 5,
                    maxLength = if (maxLength > 0) maxLength else null,
                    showCount = showCount,
                    autoSize = autoSize,
                    modifier = Modifier.width(180.dp)
                )
            }
        }
    }
}
