package com.antdesign.ui.stories

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.antdesign.ui.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.storytale.story

// ==================== MODAL ====================
val ModalComplete by story {
    var visible by remember { mutableStateOf(false) }
    val title by parameter("Modal Title")
    val width by parameter(520)
    val centered by parameter(false)
    val closable by parameter(true)
    val maskClosable by parameter(true)
    val keyboard by parameter(true)
    val mask by parameter(true)
    val destroyOnHidden by parameter(false)
    val forceRender by parameter(false)
    val confirmLoading by parameter(false)
    val transitionType by parameter("Zoom")
    val okText by parameter("OK")
    val cancelText by parameter("Cancel")
    val okType by parameter("Primary")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntButton(onClick = { visible = true }, type = ButtonType.Primary) {
            Text("Open Modal (All Parameters)")
        }

        AntModal(
            visible = visible,
            onDismiss = { visible = false },
            title = title,
            width = width.dp,
            centered = centered,
            closable = closable,
            maskClosable = maskClosable,
            keyboard = keyboard,
            mask = mask,
            destroyOnHidden = destroyOnHidden,
            forceRender = forceRender,
            transitionType = when (transitionType) {
                "Zoom" -> TransitionType.Zoom
                "Fade" -> TransitionType.Fade
                "Slide" -> TransitionType.Slide
                else -> TransitionType.None
            },
            onOk = { visible = false },
            onCancel = { visible = false },
            okText = okText,
            cancelText = cancelText,
            okType = when (okType) {
                "Primary" -> ButtonType.Primary
                "Dashed" -> ButtonType.Dashed
                "Text" -> ButtonType.Text
                "Link" -> ButtonType.Link
                else -> ButtonType.Default
            },
            confirmLoading = confirmLoading,
            afterOpenChange = { println("Modal open state: $it") }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Modal content with all parameters configured:")
                Text("• Title: $title")
                Text("• Width: ${width}dp")
                Text("• Centered: $centered")
                Text("• Closable: $closable")
                Text("• Mask Closable: $maskClosable")
                Text("• Keyboard: $keyboard")
                Text("• Mask: $mask")
                Text("• Destroy on Hidden: $destroyOnHidden")
                Text("• Force Render: $forceRender")
                Text("• Transition: $transitionType")
                Text("• Confirm Loading: $confirmLoading")
            }
        }
    }
}

// ==================== DRAWER ====================
val DrawerComplete by story {
    var open by remember { mutableStateOf(false) }
    val title by parameter("Drawer Title")
    val placement by parameter("Right")
    val width by parameter(378)
    val height by parameter(378)
    val closable by parameter(true)
    val maskClosable by parameter(true)
    val mask by parameter(true)
    val keyboard by parameter(true)
    val destroyOnHidden by parameter(false)
    val forceRender by parameter(false)
    val autoFocus by parameter(true)
    val size by parameter("Default")
    val loading by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntButton(onClick = { open = true }, type = ButtonType.Primary) {
            Text("Open Drawer (All Parameters)")
        }

        AntDrawer(
            open = open,
            onClose = { open = false },
            title = title,
            placement = when (placement) {
                "Left" -> DrawerPlacement.Left
                "Right" -> DrawerPlacement.Right
                "Top" -> DrawerPlacement.Top
                else -> DrawerPlacement.Bottom
            },
            width = width.dp,
            height = height.dp,
            closable = closable,
            maskClosable = maskClosable,
            mask = mask,
            keyboard = keyboard,
            destroyOnHidden = destroyOnHidden,
            forceRender = forceRender,
            autoFocus = autoFocus,
            size = if (size == "Large") DrawerSize.Large else DrawerSize.Default,
            loading = loading,
            afterOpenChange = { println("Drawer open state: $it") },
            extra = {
                Text("Extra content", color = Color.Gray)
            },
            footer = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AntButton(onClick = { open = false }) {
                        Text("Cancel")
                    }
                    AntButton(onClick = { open = false }, type = ButtonType.Primary) {
                        Text("Submit")
                    }
                }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Drawer content with all parameters:")
                Text("• Title: $title")
                Text("• Placement: $placement")
                Text("• Width: ${width}dp")
                Text("• Height: ${height}dp")
                Text("• Closable: $closable")
                Text("• Mask Closable: $maskClosable")
                Text("• Mask: $mask")
                Text("• Keyboard: $keyboard")
                Text("• Destroy on Hidden: $destroyOnHidden")
                Text("• Auto Focus: $autoFocus")
                Text("• Size: $size")
                Text("• Loading: $loading")
            }
        }
    }
}

// ==================== MESSAGE ====================
val MessageComplete by story {
    val content by parameter("This is a message")
    val type by parameter("Info")
    val duration by parameter(3.0)
    val key by parameter("")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Message Component (All Parameters):")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(onClick = {
                when (type) {
                    "Success" -> AntMessage.success(content, duration)
                    "Error" -> AntMessage.error(content, duration)
                    "Warning" -> AntMessage.warning(content, duration)
                    "Loading" -> AntMessage.loading(content, duration)
                    else -> AntMessage.info(content, duration)
                }
            }, type = ButtonType.Primary) {
                Text("Show $type Message")
            }

            AntButton(onClick = {
                AntMessage.open(MessageConfig(
                    content = content,
                    type = when (type) {
                        "Success" -> MessageType.Success
                        "Error" -> MessageType.Error
                        "Warning" -> MessageType.Warning
                        "Loading" -> MessageType.Loading
                        else -> MessageType.Info
                    },
                    duration = duration,
                    key = if (key.isNotEmpty()) key else null,
                    onClose = { println("Message closed") },
                    onClick = { println("Message clicked") }
                ))
            }) {
                Text("With Config")
            }

            AntButton(onClick = { AntMessage.destroy() }, danger = true) {
                Text("Destroy All")
            }
        }

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Content: $content")
            Text("• Type: $type")
            Text("• Duration: ${duration}s")
            if (key.isNotEmpty()) Text("• Key: $key")
        }
    }
}

// ==================== NOTIFICATION ====================
val NotificationComplete by story {
    val message by parameter("Notification Title")
    val description by parameter("This is the notification description with detailed information.")
    val type by parameter("Info")
    val placement by parameter("TopRight")
    val duration by parameter(4.5)
    val showButton by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Notification Component (All Parameters):")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(onClick = {
                val placementEnum = when (placement) {
                    "TopLeft" -> NotificationPlacement.TopLeft
                    "TopRight" -> NotificationPlacement.TopRight
                    "BottomLeft" -> NotificationPlacement.BottomLeft
                    "BottomRight" -> NotificationPlacement.BottomRight
                    "Top" -> NotificationPlacement.Top
                    else -> NotificationPlacement.Bottom
                }

                when (type) {
                    "Success" -> AntNotification.success(
                        message, description, duration, placementEnum,
                        btn = if (showButton) {
                            { AntButton(onClick = {}, type = ButtonType.Link) { Text("Action") } }
                        } else null
                    )
                    "Error" -> AntNotification.error(message, description, duration, placementEnum)
                    "Warning" -> AntNotification.warning(message, description, duration, placementEnum)
                    "Open" -> AntNotification.open(NotificationConfig(
                        message = NotificationContent.Text(message),
                        description = NotificationContent.Text(description),
                        type = NotificationType.Open,
                        placement = placementEnum,
                        duration = duration
                    ))
                    else -> AntNotification.info(message, description, duration, placementEnum)
                }
            }, type = ButtonType.Primary) {
                Text("Show $type")
            }

            AntButton(onClick = { AntNotification.destroy() }, danger = true) {
                Text("Destroy All")
            }
        }

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Message: $message")
            Text("• Description: $description")
            Text("• Type: $type")
            Text("• Placement: $placement")
            Text("• Duration: ${duration}s")
            Text("• Show Button: $showButton")
        }
    }
}

// ==================== ALERT ====================
val AlertComplete by story {
    val message by parameter("Alert Message")
    val description by parameter("Additional description and information about the alert.")
    val type by parameter("Info")
    val closable by parameter(false)
    val showIcon by parameter(true)
    val banner by parameter(false)
    val showAction by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Alert Component (All Parameters):")

        AntAlert(
            message = message,
            description = if (description.isNotEmpty()) description else null,
            type = when (type) {
                "Success" -> AlertType.Success
                "Warning" -> AlertType.Warning
                "Error" -> AlertType.Error
                else -> AlertType.Info
            },
            closable = closable,
            showIcon = showIcon,
            banner = banner,
            onClose = { println("Alert closed") },
            afterClose = { println("Alert animation complete") },
            onClick = { println("Alert clicked") },
            onMouseEnter = { println("Mouse entered") },
            onMouseLeave = { println("Mouse left") },
            action = if (showAction) {
                { AntButton(type = ButtonType.Link, onClick = {}) { Text("Details") } }
            } else null
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Message: $message")
            if (description.isNotEmpty()) Text("• Description: $description")
            Text("• Type: $type")
            Text("• Closable: $closable")
            Text("• Show Icon: $showIcon")
            Text("• Banner: $banner")
            Text("• Show Action: $showAction")
        }
    }
}

// ==================== POPCONFIRM ====================
val PopconfirmComplete by story {
    val title by parameter("Are you sure?")
    val description by parameter("This action cannot be undone.")
    val okText by parameter("Yes")
    val cancelText by parameter("No")
    val okType by parameter("Primary")
    val showCancel by parameter(true)
    val disabled by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Popconfirm Component (All Parameters):")

        AntPopconfirm(
            title = title,
            description = if (description.isNotEmpty()) description else null,
            onConfirm = { println("Confirmed!") },
            onCancel = { println("Cancelled!") },
            okText = okText,
            cancelText = cancelText,
            okType = when (okType) {
                "Danger" -> ButtonType.Primary
                "Default" -> ButtonType.Default
                else -> ButtonType.Primary
            },
            okDanger = okType == "Danger",
            showCancel = showCancel,
            disabled = disabled,
            child = {
                AntButton(onClick = {}, type = ButtonType.Primary) {
                    Text("Delete (Click to Confirm)")
                }
            }
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Title: $title")
            if (description.isNotEmpty()) Text("• Description: $description")
            Text("• OK Text: $okText")
            Text("• Cancel Text: $cancelText")
            Text("• OK Type: $okType")
            Text("• Show Cancel: $showCancel")
            Text("• Disabled: $disabled")
        }
    }
}

// ==================== TOOLTIP ====================
val TooltipComplete by story {
    val title by parameter("Tooltip content")
    val placement by parameter("Top")
    val trigger by parameter("Hover")
    val arrow by parameter(true)
    val color by parameter("Default")
    val mouseEnterDelay by parameter(100)
    val mouseLeaveDelay by parameter(100)
    val destroyOnHidden by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tooltip Component (All Parameters):")

        AntTooltip(
            title = title,
            placement = when (placement) {
                "TopLeft" -> TooltipPlacementExt.TopLeft
                "TopRight" -> TooltipPlacementExt.TopRight
                "Bottom" -> TooltipPlacementExt.Bottom
                "BottomLeft" -> TooltipPlacementExt.BottomLeft
                "BottomRight" -> TooltipPlacementExt.BottomRight
                "Left" -> TooltipPlacementExt.Left
                "LeftTop" -> TooltipPlacementExt.LeftTop
                "LeftBottom" -> TooltipPlacementExt.LeftBottom
                "Right" -> TooltipPlacementExt.Right
                "RightTop" -> TooltipPlacementExt.RightTop
                "RightBottom" -> TooltipPlacementExt.RightBottom
                else -> TooltipPlacementExt.Top
            },
            trigger = when (trigger) {
                "Click" -> TooltipTrigger.Click
                "Focus" -> TooltipTrigger.Focus
                "ContextMenu" -> TooltipTrigger.ContextMenu
                else -> TooltipTrigger.Hover
            },
            arrow = arrow,
            color = when (color) {
                "Blue" -> TooltipPresetColors.BLUE
                "Red" -> TooltipPresetColors.RED
                "Green" -> TooltipPresetColors.GREEN
                "Pink" -> TooltipPresetColors.PINK
                "Orange" -> TooltipPresetColors.ORANGE
                else -> null
            },
            mouseEnterDelay = mouseEnterDelay.toLong(),
            mouseLeaveDelay = mouseLeaveDelay.toLong(),
            destroyOnHidden = destroyOnHidden,
            content = {
                AntButton(onClick = {}) {
                    Text("Hover me (or trigger)")
                }
            }
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Title: $title")
            Text("• Placement: $placement")
            Text("• Trigger: $trigger")
            Text("• Arrow: $arrow")
            Text("• Color: $color")
            Text("• Mouse Enter Delay: ${mouseEnterDelay}ms")
            Text("• Mouse Leave Delay: ${mouseLeaveDelay}ms")
            Text("• Destroy on Hidden: $destroyOnHidden")
        }
    }
}

// ==================== POPOVER ====================
val PopoverComplete by story {
    val contentText by parameter("Popover content goes here")
    val title by parameter("Popover Title")
    val placement by parameter("Top")
    val trigger by parameter("Hover")
    val arrow by parameter(true)
    val mouseEnterDelay by parameter(100)
    val mouseLeaveDelay by parameter(100)
    val destroyOnHidden by parameter(false)
    val fresh by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Popover Component (All Parameters):")

        AntPopover(
            content = contentText,
            title = if (title.isNotEmpty()) title else null,
            placement = when (placement) {
                "TopLeft" -> PopoverPlacement.TopLeft
                "TopRight" -> PopoverPlacement.TopRight
                "Bottom" -> PopoverPlacement.Bottom
                "BottomLeft" -> PopoverPlacement.BottomLeft
                "BottomRight" -> PopoverPlacement.BottomRight
                "Left" -> PopoverPlacement.Left
                "LeftTop" -> PopoverPlacement.LeftTop
                "LeftBottom" -> PopoverPlacement.LeftBottom
                "Right" -> PopoverPlacement.Right
                "RightTop" -> PopoverPlacement.RightTop
                "RightBottom" -> PopoverPlacement.RightBottom
                else -> PopoverPlacement.Top
            },
            trigger = when (trigger) {
                "Click" -> PopoverTrigger.Click
                "Focus" -> PopoverTrigger.Focus
                else -> PopoverTrigger.Hover
            },
            arrow = arrow,
            mouseEnterDelay = mouseEnterDelay.toLong(),
            mouseLeaveDelay = mouseLeaveDelay.toLong(),
            destroyOnHidden = destroyOnHidden,
            fresh = fresh,
            onOpenChange = { println("Popover open: $it") },
            afterOpenChange = { println("Popover animation complete: $it") },
            child = {
                AntButton(onClick = {}) {
                    Text("Click/Hover me")
                }
            }
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Content: $contentText")
            if (title.isNotEmpty()) Text("• Title: $title")
            Text("• Placement: $placement")
            Text("• Trigger: $trigger")
            Text("• Arrow: $arrow")
            Text("• Mouse Enter Delay: ${mouseEnterDelay}ms")
            Text("• Mouse Leave Delay: ${mouseLeaveDelay}ms")
            Text("• Destroy on Hidden: $destroyOnHidden")
            Text("• Fresh: $fresh")
        }
    }
}

// ==================== PROGRESS ====================
val ProgressComplete by story {
    val percent by parameter(75.0)
    val type by parameter("Line")
    val status by parameter("Normal")
    val strokeLinecap by parameter("Round")
    val strokeWidth by parameter(8)
    val showInfo by parameter(true)
    val size by parameter("Default")
    val steps by parameter(0)
    val successPercent by parameter(0.0)
    val gapDegree by parameter(75.0)
    val gapPosition by parameter("Bottom")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Progress Component (All Parameters):")

        AntProgress(
            percent = percent,
            type = when (type) {
                "Circle" -> ProgressType.Circle
                "Dashboard" -> ProgressType.Dashboard
                else -> ProgressType.Line
            },
            status = when (status) {
                "Success" -> ProgressStatus.Success
                "Exception" -> ProgressStatus.Exception
                "Active" -> ProgressStatus.Active
                else -> ProgressStatus.Normal
            },
            strokeLinecap = when (strokeLinecap) {
                "Square" -> StrokeLinecap.Square
                "Butt" -> StrokeLinecap.Butt
                else -> StrokeLinecap.Round
            },
            strokeWidth = if (strokeWidth > 0) strokeWidth.dp else null,
            showInfo = showInfo,
            size = if (size == "Small") ProgressSize.Small else ProgressSize.Default,
            steps = if (steps > 0) steps else null,
            success = if (successPercent > 0) SuccessConfig(percent = successPercent) else null,
            gapDegree = if (type == "Dashboard") gapDegree else null,
            gapPosition = when (gapPosition) {
                "Top" -> GapPosition.Top
                "Left" -> GapPosition.Left
                "Right" -> GapPosition.Right
                else -> GapPosition.Bottom
            },
            format = { p, s -> if (s != null) "${p.toInt()}% + ${s.toInt()}%" else "${p.toInt()}%" }
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Percent: ${percent.toInt()}%")
            Text("• Type: $type")
            Text("• Status: $status")
            Text("• Stroke Linecap: $strokeLinecap")
            Text("• Stroke Width: ${strokeWidth}dp")
            Text("• Show Info: $showInfo")
            Text("• Size: $size")
            if (steps > 0) Text("• Steps: $steps")
            if (successPercent > 0) Text("• Success Percent: ${successPercent.toInt()}%")
            if (type == "Dashboard") {
                Text("• Gap Degree: ${gapDegree.toInt()}°")
                Text("• Gap Position: $gapPosition")
            }
        }
    }
}

// ==================== SPIN ====================
val SpinComplete by story {
    var spinning by remember { mutableStateOf(true) }
    val size by parameter("Default")
    val delay by parameter(0)
    val tip by parameter("Loading...")
    val showTip by parameter(true)
    val fullscreen by parameter(false)
    val percent by parameter(0.0)
    val showContent by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Spin Component (All Parameters):")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(onClick = { spinning = !spinning }, type = ButtonType.Primary) {
                Text(if (spinning) "Stop Spinning" else "Start Spinning")
            }
        }

        AntSpin(
            spinning = spinning,
            size = when (size) {
                "Small" -> SpinSize.Small
                "Large" -> SpinSize.Large
                else -> SpinSize.Default
            },
            delay = if (delay > 0) delay else null,
            tip = if (showTip) tip else null,
            fullscreen = fullscreen,
            percent = if (percent > 0) percent else null,
            children = if (showContent) {
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Content area with all parameters:")
                            Text("• Size: $size")
                            Text("• Delay: ${delay}ms")
                            if (showTip) Text("• Tip: $tip")
                            Text("• Fullscreen: $fullscreen")
                            if (percent > 0) Text("• Percent: ${percent.toInt()}%")
                            Text("• Spinning: $spinning")
                        }
                    }
                }
            } else null
        )
    }
}

// ==================== SKELETON ====================
val SkeletonComplete by story {
    val loading by parameter(true)
    val active by parameter(true)
    val avatar by parameter(true)
    val title by parameter(true)
    val paragraph by parameter(true)
    val round by parameter(false)
    val avatarShape by parameter("Circle")
    val avatarSize by parameter("Default")
    val paragraphRows by parameter(3)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Skeleton Component (All Parameters):")

        AntSkeleton(
            loading = loading,
            active = active,
            avatar = if (avatar) SkeletonAvatarProps(
                shape = if (avatarShape == "Square") SkeletonAvatarShape.Square else SkeletonAvatarShape.Circle,
                size = when (avatarSize) {
                    "Small" -> AvatarSize.Small
                    "Large" -> AvatarSize.Large
                    else -> AvatarSize.Default
                }
            ) else false,
            title = if (title) SkeletonTitleProps(width = "40%") else false,
            paragraph = if (paragraph) SkeletonParagraphProps(
                rows = paragraphRows,
                width = listOf("100%", "100%", "60%")
            ) else false,
            round = round
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Loaded Content!")
                Text("This content is shown when loading = false")
                Text("• Avatar Shape: $avatarShape")
                Text("• Avatar Size: $avatarSize")
                Text("• Paragraph Rows: $paragraphRows")
                Text("• Round: $round")
            }
        }

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Loading: $loading")
            Text("• Active: $active")
            Text("• Avatar: $avatar")
            Text("• Title: $title")
            Text("• Paragraph: $paragraph")
            Text("• Round: $round")
        }
    }
}

// ==================== RESULT ====================
val ResultComplete by story {
    val status by parameter("Success")
    val title by parameter("Successfully Completed")
    val subTitle by parameter("The operation has been completed successfully.")
    val showExtra by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Result Component (All Parameters):")

        AntResult(
            status = when (status) {
                "Error" -> ResultStatus.Error
                "Info" -> ResultStatus.Info
                "Warning" -> ResultStatus.Warning
                "NotFound" -> ResultStatus.NotFound
                "Forbidden" -> ResultStatus.Forbidden
                "ServerError" -> ResultStatus.ServerError
                else -> ResultStatus.Success
            },
            title = title,
            subTitle = if (subTitle.isNotEmpty()) subTitle else null,
            extra = if (showExtra) {
                {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AntButton(onClick = {}, type = ButtonType.Primary) {
                            Text("Back Home")
                        }
                        AntButton(onClick = {}) {
                            Text("Buy Again")
                        }
                    }
                }
            } else null
        )

        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("• Status: $status")
            Text("• Title: $title")
            if (subTitle.isNotEmpty()) Text("• SubTitle: $subTitle")
            Text("• Show Extra: $showExtra")
        }
    }
}
