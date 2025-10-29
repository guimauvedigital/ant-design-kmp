package com.antdesign.ui.stories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.antdesign.ui.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.storytale.story

val Modal by story {
    var visible by remember { mutableStateOf(false) }
    val closable by parameter(true)
    val maskClosable by parameter(true)
    val centered by parameter(false)
    val width by parameter(520)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntButton(
            onClick = { visible = true },
            type = ButtonType.Primary
        ) {
            Text("Open Modal")
        }

        AntModal(
            visible = visible,
            onDismiss = { visible = false },
            title = "Modal Title",
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            centered = centered,
            footer = {
                AntButton(
                    onClick = { visible = false },
                    type = ButtonType.Default
                ) {
                    Text("Cancel")
                }
                AntButton(
                    onClick = { visible = false },
                    type = ButtonType.Primary
                ) {
                    Text("OK")
                }
            }
        ) {
            Text("Modal content goes here")
            Text("Some details...")
            Text("Width: ${width}dp")
            Text("Closable: $closable")
            Text("Mask closable: $maskClosable")
            Text("Centered: $centered")
        }

        // Modal with async operation
        Text("Modal with async operation:")
        var visibleAsync by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        AntButton(
            onClick = { visibleAsync = true },
            type = ButtonType.Primary
        ) {
            Text("Open Async Modal")
        }

        AntModal(
            visible = visibleAsync,
            onDismiss = {
                if (!isLoading) visibleAsync = false
            },
            title = "Async Operation Modal",
            width = 520.dp,
            closable = !isLoading,
            maskClosable = !isLoading,
            centered = false,
            footer = {
                AntButton(
                    onClick = {
                        if (!isLoading) visibleAsync = false
                    },
                    type = ButtonType.Default,
                    disabled = isLoading
                ) {
                    Text("Cancel")
                }
                AntButton(
                    onClick = {
                        isLoading = true
                    },
                    type = ButtonType.Primary,
                    disabled = isLoading,
                    loading = if (isLoading) ButtonLoading.Simple() else ButtonLoading.None
                ) {
                    Text(if (isLoading) "Processing..." else "OK")
                }
            }
        ) {
            Text("Click OK to start an async operation")
            Text("Buttons will be disabled during processing")
            if (isLoading) {
                Text("Status: Processing...")
            }

            LaunchedEffect(isLoading) {
                if (isLoading) {
                    delay(2000)
                    isLoading = false
                    visibleAsync = false
                }
            }
        }

        // Modal widths
        Text("Modal widths:")
        var visible420 by remember { mutableStateOf(false) }
        var visible520 by remember { mutableStateOf(false) }
        var visible720 by remember { mutableStateOf(false) }
        var visible1000 by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntButton(
                onClick = { visible420 = true },
                type = ButtonType.Primary
            ) {
                Text("420dp")
            }
            AntButton(
                onClick = { visible520 = true },
                type = ButtonType.Primary
            ) {
                Text("520dp")
            }
            AntButton(
                onClick = { visible720 = true },
                type = ButtonType.Primary
            ) {
                Text("720dp")
            }
            AntButton(
                onClick = { visible1000 = true },
                type = ButtonType.Primary
            ) {
                Text("1000dp")
            }
        }

        AntModal(
            visible = visible420,
            onDismiss = { visible420 = false },
            title = "Modal 420dp",
            width = 420.dp,
            footer = {
                AntButton(
                    onClick = { visible420 = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal has a width of 420dp")
            Text("Compact width for simple dialogs")
        }

        AntModal(
            visible = visible520,
            onDismiss = { visible520 = false },
            title = "Modal 520dp",
            width = 520.dp,
            footer = {
                AntButton(
                    onClick = { visible520 = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal has a width of 520dp")
            Text("Default width for standard dialogs")
        }

        AntModal(
            visible = visible720,
            onDismiss = { visible720 = false },
            title = "Modal 720dp",
            width = 720.dp,
            footer = {
                AntButton(
                    onClick = { visible720 = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal has a width of 720dp")
            Text("Wider modal for forms with more content")
        }

        AntModal(
            visible = visible1000,
            onDismiss = { visible1000 = false },
            title = "Modal 1000dp",
            width = 1000.dp,
            footer = {
                AntButton(
                    onClick = { visible1000 = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal has a width of 1000dp")
            Text("Large modal for complex content or data tables")
        }

        // Centered example
        Text("Centered vs non-centered modals:")
        var visibleCentered by remember { mutableStateOf(false) }
        var visibleNotCentered by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntButton(
                onClick = { visibleCentered = true },
                type = ButtonType.Primary
            ) {
                Text("Centered Modal")
            }
            AntButton(
                onClick = { visibleNotCentered = true },
                type = ButtonType.Primary
            ) {
                Text("Non-Centered Modal")
            }
        }

        AntModal(
            visible = visibleCentered,
            onDismiss = { visibleCentered = false },
            title = "Centered Modal",
            width = 520.dp,
            centered = true,
            footer = {
                AntButton(
                    onClick = { visibleCentered = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal is centered vertically on the screen")
            Text("Use centered=true for important dialogs")
            Text("It provides better visual balance")
        }

        AntModal(
            visible = visibleNotCentered,
            onDismiss = { visibleNotCentered = false },
            title = "Non-Centered Modal",
            width = 520.dp,
            centered = false,
            footer = {
                AntButton(
                    onClick = { visibleNotCentered = false },
                    type = ButtonType.Primary
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This modal appears near the top of the screen")
            Text("Use centered=false for informational dialogs")
            Text("This is the default positioning")
        }

        // No footer example
        Text("Modal without footer (information only):")
        var visibleNoFooter by remember { mutableStateOf(false) }

        AntButton(
            onClick = { visibleNoFooter = true },
            type = ButtonType.Primary
        ) {
            Text("Open Info Modal")
        }

        AntModal(
            visible = visibleNoFooter,
            onDismiss = { visibleNoFooter = false },
            title = "Information",
            width = 520.dp,
            centered = false
        ) {
            Text("This modal has no footer buttons")
            Text("It's useful for displaying information only")
            Text("Users can close it using the X button or by clicking the mask")
            Text("")
            Text("Common use cases:")
            Text("- Display read-only content")
            Text("- Show notifications or announcements")
            Text("- Present help documentation")
        }
    }
}

val Drawer by story {
    var visibleTop by remember { mutableStateOf(false) }
    var visibleRight by remember { mutableStateOf(false) }
    var visibleBottom by remember { mutableStateOf(false) }
    var visibleLeft by remember { mutableStateOf(false) }
    var visibleFooter by remember { mutableStateOf(false) }
    var visibleExtra by remember { mutableStateOf(false) }
    var visibleCombined by remember { mutableStateOf(false) }

    val width by parameter(378)
    val height by parameter(378)
    val closable by parameter(true)
    val maskClosable by parameter(true)
    val mask by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Drawer placements:")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntButton(
                onClick = { visibleTop = true },
                type = ButtonType.Primary
            ) {
                Text("Open Top Drawer")
            }

            AntButton(
                onClick = { visibleRight = true },
                type = ButtonType.Primary
            ) {
                Text("Open Right Drawer")
            }

            AntButton(
                onClick = { visibleBottom = true },
                type = ButtonType.Primary
            ) {
                Text("Open Bottom Drawer")
            }

            AntButton(
                onClick = { visibleLeft = true },
                type = ButtonType.Primary
            ) {
                Text("Open Left Drawer")
            }
        }

        Text("Drawer with footer:")

        AntButton(
            onClick = { visibleFooter = true },
            type = ButtonType.Primary
        ) {
            Text("Open Drawer with Footer")
        }

        Text("Drawer with extra:")

        AntButton(
            onClick = { visibleExtra = true },
            type = ButtonType.Primary
        ) {
            Text("Open Drawer with Extra")
        }

        Text("Drawer with all options:")

        AntButton(
            onClick = { visibleCombined = true },
            type = ButtonType.Primary
        ) {
            Text("Open Combined Drawer")
        }

        // Top Drawer
        AntDrawer(
            open = visibleTop,
            onClose = { visibleTop = false },
            title = "Top Drawer",
            placement = DrawerPlacement.Top,
            height = height.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask
        ) {
            Text("This drawer opens from the top")
            Text("Height: ${height}dp")
            Text("Closable: $closable")
            Text("Mask closable: $maskClosable")
        }

        // Right Drawer
        AntDrawer(
            open = visibleRight,
            onClose = { visibleRight = false },
            title = "Right Drawer",
            placement = DrawerPlacement.Right,
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask
        ) {
            Text("This drawer opens from the right")
            Text("Width: ${width}dp")
            Text("Closable: $closable")
            Text("Mask closable: $maskClosable")
        }

        // Bottom Drawer
        AntDrawer(
            open = visibleBottom,
            onClose = { visibleBottom = false },
            title = "Bottom Drawer",
            placement = DrawerPlacement.Bottom,
            height = height.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask
        ) {
            Text("This drawer opens from the bottom")
            Text("Height: ${height}dp")
            Text("Closable: $closable")
            Text("Mask closable: $maskClosable")
        }

        // Left Drawer
        AntDrawer(
            open = visibleLeft,
            onClose = { visibleLeft = false },
            title = "Left Drawer",
            placement = DrawerPlacement.Left,
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask
        ) {
            Text("This drawer opens from the left")
            Text("Width: ${width}dp")
            Text("Closable: $closable")
            Text("Mask closable: $maskClosable")
        }

        // Drawer with Footer
        AntDrawer(
            open = visibleFooter,
            onClose = { visibleFooter = false },
            title = "Drawer with Footer",
            placement = DrawerPlacement.Right,
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask,
            footer = {
                AntButton(
                    onClick = { visibleFooter = false },
                    type = ButtonType.Default
                ) {
                    Text("Cancel")
                }
                AntButton(
                    onClick = { visibleFooter = false },
                    type = ButtonType.Primary
                ) {
                    Text("OK")
                }
            }
        ) {
            Text("This drawer includes a footer with action buttons")
            Text("The footer contains Cancel and OK buttons")
            Text("Use the footer for actions related to the drawer content")
        }

        // Drawer with Extra
        AntDrawer(
            open = visibleExtra,
            onClose = { visibleExtra = false },
            title = "Drawer with Extra",
            placement = DrawerPlacement.Right,
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask,
            extra = {
                AntButton(
                    onClick = { },
                    type = ButtonType.Link
                ) {
                    Text("Link")
                }
            }
        ) {
            Text("This drawer includes extra header content")
            Text("The extra area can contain additional buttons or links")
            Text("It appears in the header next to the title")
        }

        // Combined Drawer
        AntDrawer(
            open = visibleCombined,
            onClose = { visibleCombined = false },
            title = "Complete Drawer Example",
            placement = DrawerPlacement.Right,
            width = width.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask,
            footer = {
                AntButton(
                    onClick = { visibleCombined = false },
                    type = ButtonType.Default
                ) {
                    Text("Cancel")
                }
                AntButton(
                    onClick = { visibleCombined = false },
                    type = ButtonType.Primary
                ) {
                    Text("OK")
                }
            },
            extra = {
                AntButton(
                    onClick = { },
                    type = ButtonType.Link
                ) {
                    Text("More")
                }
            }
        ) {
            Text("This drawer demonstrates all features together:")
            Text("- Title: 'Complete Drawer Example'")
            Text("- Extra: Link button in the header")
            Text("- Footer: Cancel and OK buttons")
            Text("- Width: ${width}dp")
            Text("- Closable: $closable")
            Text("- Mask closable: $maskClosable")
            Text("- Mask: $mask")
        }
    }
}

val Popconfirm by story {
    val okText by parameter("OK")
    val cancelText by parameter("Cancel")
    val showCancel by parameter(true)
    val okDanger by parameter(false)
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntPopconfirm(
            title = "Are you sure?",
            description = "This action cannot be undone",
            okText = okText,
            cancelText = cancelText,
            onConfirm = { /* Handle confirm */ },
            onCancel = { /* Handle cancel */ },
            icon = null,
            okType = ButtonType.Primary,
            okDanger = okDanger,
            cancelButtonProps = ButtonType.Default,
            showCancel = showCancel,
            disabled = disabled
        ) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary
            ) {
                Text("Delete")
            }
        }
    }
}

val Popover by story {
    val arrow by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Popover placements:")

        // Top placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Top Left",
                content = "Popover content\nPlacement: TopLeft",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.TopLeft,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Top Left")
                }
            }

            AntPopover(
                title = "Top",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Top,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Top")
                }
            }

            AntPopover(
                title = "Top Right",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.TopRight,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Top Right")
                }
            }
        }

        // Left placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Left Top",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.LeftTop,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Left Top")
                }
            }

            AntPopover(
                title = "Left",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Left,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Left")
                }
            }

            AntPopover(
                title = "Left Bottom",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.LeftBottom,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Left Bottom")
                }
            }
        }

        // Right placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Right Top",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.RightTop,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Right Top")
                }
            }

            AntPopover(
                title = "Right",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Right,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Right")
                }
            }

            AntPopover(
                title = "Right Bottom",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.RightBottom,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Right Bottom")
                }
            }
        }

        // Bottom placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Bottom Left",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.BottomLeft,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Bottom Left")
                }
            }

            AntPopover(
                title = "Bottom",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Bottom,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Bottom")
                }
            }

            AntPopover(
                title = "Bottom Right",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.BottomRight,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Bottom Right")
                }
            }
        }

        Text("Popover triggers:")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Click Trigger",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Top,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Click me")
                }
            }

            AntPopover(
                title = "Hover Trigger",
                content = "Popover content",
                trigger = PopoverTrigger.Hover,
                placement = PopoverPlacement.Top,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Default
                ) {
                    Text("Hover me")
                }
            }

            AntPopover(
                title = "Focus Trigger",
                content = "Popover content",
                trigger = PopoverTrigger.Focus,
                placement = PopoverPlacement.Top,
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Default
                ) {
                    Text("Focus me")
                }
            }
        }

        Text("Controlled visibility:")

        var controlledVisible by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntPopover(
                title = "Controlled Popover",
                content = "Popover content",
                trigger = PopoverTrigger.Click,
                placement = PopoverPlacement.Bottom,
                open = controlledVisible,
                onOpenChange = { controlledVisible = it },
                arrow = arrow
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Controlled Popover")
                }
            }

            AntButton(
                onClick = { controlledVisible = !controlledVisible },
                type = ButtonType.Default
            ) {
                Text(if (controlledVisible) "Hide Popover" else "Show Popover")
            }
        }
    }
}

val Tooltip by story {
    val title by parameter("Tooltip text")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tooltip placements:")

        // Top placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "TopLeft tooltip",
                placement = TooltipPlacementExt.TopLeft,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("TopLeft")
                }
            }

            AntTooltip(
                title = "Top tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Top")
                }
            }

            AntTooltip(
                title = "TopRight tooltip",
                placement = TooltipPlacementExt.TopRight,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("TopRight")
                }
            }
        }

        // Left placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "LeftTop tooltip",
                placement = TooltipPlacementExt.LeftTop,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("LeftTop")
                }
            }

            AntTooltip(
                title = "Left tooltip",
                placement = TooltipPlacementExt.Left,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Left")
                }
            }

            AntTooltip(
                title = "LeftBottom tooltip",
                placement = TooltipPlacementExt.LeftBottom,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("LeftBottom")
                }
            }
        }

        // Right placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "RightTop tooltip",
                placement = TooltipPlacementExt.RightTop,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("RightTop")
                }
            }

            AntTooltip(
                title = "Right tooltip",
                placement = TooltipPlacementExt.Right,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Right")
                }
            }

            AntTooltip(
                title = "RightBottom tooltip",
                placement = TooltipPlacementExt.RightBottom,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("RightBottom")
                }
            }
        }

        // Bottom placements
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "BottomLeft tooltip",
                placement = TooltipPlacementExt.BottomLeft,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("BottomLeft")
                }
            }

            AntTooltip(
                title = "Bottom tooltip",
                placement = TooltipPlacementExt.Bottom,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Bottom")
                }
            }

            AntTooltip(
                title = "BottomRight tooltip",
                placement = TooltipPlacementExt.BottomRight,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("BottomRight")
                }
            }
        }

        Text("Tooltip triggers:")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "Hover trigger tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Hover Trigger")
                }
            }

            AntTooltip(
                title = "Focus trigger tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Focus,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Focus Trigger")
                }
            }

            AntTooltip(
                title = "Click trigger tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Click,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Click Trigger")
                }
            }

            AntTooltip(
                title = "ContextMenu trigger tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.ContextMenu,
                open = true
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("ContextMenu Trigger")
                }
            }
        }

        Text("Tooltip colors:")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntTooltip(
                title = "Default dark tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true,
                color = Color(0xFF000000).copy(alpha = 0.75f)
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Dark (Default)")
                }
            }

            AntTooltip(
                title = "Blue tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true,
                color = Color(0xFF1890FF).copy(alpha = 0.85f)
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Blue")
                }
            }

            AntTooltip(
                title = "Red tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true,
                color = Color(0xFFFF4D4F).copy(alpha = 0.85f)
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Red")
                }
            }

            AntTooltip(
                title = "Green tooltip",
                placement = TooltipPlacementExt.Top,
                trigger = TooltipTrigger.Hover,
                open = true,
                color = Color(0xFF52C41A).copy(alpha = 0.85f)
            ) {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Green")
                }
            }
        }

        Text("Custom tooltip with parameter:")

        AntTooltip(
            title = title,
            placement = TooltipPlacementExt.Top,
            trigger = TooltipTrigger.Hover,
            open = true,
            color = Color(0xFF000000).copy(alpha = 0.75f)
        ) {
            AntButton(
                onClick = {},
                type = ButtonType.Primary
            ) {
                Text("Custom Text")
            }
        }
    }
}

val Result by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntResult(
            status = ResultStatus.Success,
            title = "Success",
            subTitle = "Your operation has been completed successfully",
            extra = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Go Home")
                }
            }
        )
    }
}

val ErrorResult by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntResult(
            status = ResultStatus.Error,
            title = "Error",
            subTitle = "Something went wrong",
            extra = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Try Again")
                }
            }
        )
    }
}

val NotFoundResult by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntResult(
            status = ResultStatus.NotFound,
            title = "404 Not Found",
            subTitle = "The page you are looking for does not exist",
            extra = {
                AntButton(
                    onClick = {},
                    type = ButtonType.Primary
                ) {
                    Text("Back Home")
                }
            }
        )
    }
}

val MessageDemo by story {
    val content by parameter("This is a message")
    val (message, contextHolder) = useMessage()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Message API Examples:")

        Text("Click buttons to show messages:")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntButton(
                onClick = {
                    message.success(content)
                },
                type = ButtonType.Primary
            ) {
                Text("Success")
            }

            AntButton(
                onClick = {
                    message.error(content)
                },
                type = ButtonType.Default,
                danger = true
            ) {
                Text("Error")
            }

            AntButton(
                onClick = {
                    message.warning(content)
                },
                type = ButtonType.Default
            ) {
                Text("Warning")
            }

            AntButton(
                onClick = {
                    message.info(content)
                },
                type = ButtonType.Default
            ) {
                Text("Info")
            }

            AntButton(
                onClick = {
                    message.loading(content)
                },
                type = ButtonType.Default
            ) {
                Text("Loading")
            }
        }

        contextHolder()
    }
}

val NotificationDemo by story {
    val message by parameter("Notification")
    val description by parameter("This is the content of the notification.")
    val (notification, contextHolder) = useNotification()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Notification API Examples:")

        Text("Click buttons to show notifications:")

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Types:")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AntButton(
                    onClick = {
                        notification.success(message, description)
                    },
                    type = ButtonType.Primary
                ) {
                    Text("Success")
                }

                AntButton(
                    onClick = {
                        notification.info(message, description)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Info")
                }

                AntButton(
                    onClick = {
                        notification.warning(message, description)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Warning")
                }

                AntButton(
                    onClick = {
                        notification.error(message, description)
                    },
                    type = ButtonType.Default,
                    danger = true
                ) {
                    Text("Error")
                }
            }

            Text("Placements:")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AntButton(
                    onClick = {
                        notification.info("Top Right", description, placement = NotificationPlacement.TopRight)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Top Right")
                }

                AntButton(
                    onClick = {
                        notification.info("Top Left", description, placement = NotificationPlacement.TopLeft)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Top Left")
                }

                AntButton(
                    onClick = {
                        notification.info("Bottom Right", description, placement = NotificationPlacement.BottomRight)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Bottom Right")
                }

                AntButton(
                    onClick = {
                        notification.info("Bottom Left", description, placement = NotificationPlacement.BottomLeft)
                    },
                    type = ButtonType.Default
                ) {
                    Text("Bottom Left")
                }
            }
        }

        contextHolder()
    }
}
