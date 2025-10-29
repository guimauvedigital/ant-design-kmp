# Tooltip Quick Start Guide

## Installation

The Tooltip component is part of the Ant Design KMP library. No additional dependencies required.

```kotlin
import com.antdesign.ui.AntTooltip
import com.antdesign.ui.TooltipPlacement
import com.antdesign.ui.TooltipTrigger
```

## 5-Minute Quick Start

### 1. Basic Tooltip (30 seconds)

```kotlin
AntTooltip(
    title = "This is a tooltip",
    content = {
        Button(onClick = {}) { Text("Hover me") }
    }
)
```

### 2. Change Position (30 seconds)

```kotlin
AntTooltip(
    title = "Bottom tooltip",
    placement = TooltipPlacement.Bottom,
    content = {
        Button(onClick = {}) { Text("Hover me") }
    }
)
```

### 3. Click Trigger (1 minute)

```kotlin
AntTooltip(
    title = "Click to show",
    trigger = TooltipTrigger.Click,
    content = {
        Button(onClick = {}) { Text("Click me") }
    }
)
```

### 4. Custom Color (1 minute)

```kotlin
AntTooltip(
    title = "Colorful tooltip",
    color = Color(0xFF1890FF),
    content = {
        Button(onClick = {}) { Text("Hover me") }
    }
)
```

### 5. Controlled Mode (2 minutes)

```kotlin
var isOpen by remember { mutableStateOf(false) }

Column {
    Button(onClick = { isOpen = !isOpen }) {
        Text("Toggle Tooltip")
    }

    AntTooltip(
        title = "Controlled",
        open = isOpen,
        onOpenChange = { isOpen = it },
        content = {
            Button(onClick = {}) { Text("Target") }
        }
    )
}
```

## Common Use Cases

### Show Hint on Icon

```kotlin
AntTooltip(
    title = "Settings",
    placement = TooltipPlacement.Right
) {
    IconButton(onClick = {}) {
        Icon(Icons.Default.Settings, "Settings")
    }
}
```

### Disabled Button Explanation

```kotlin
AntTooltip(
    title = "Please fill the form first",
    trigger = TooltipTrigger.Hover
) {
    Button(
        onClick = {},
        enabled = false
    ) {
        Text("Submit")
    }
}
```

### Form Field Help

```kotlin
Row(verticalAlignment = Alignment.CenterVertically) {
    TextField(value = email, onValueChange = { email = it })

    AntTooltip(
        title = "Enter your work email",
        placement = TooltipPlacement.RightTop
    ) {
        Icon(Icons.Default.Info, "Help")
    }
}
```

### Complex Tooltip Content

```kotlin
AntTooltip(
    title = {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Feature: Premium", color = Color.White, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                "Unlock this feature with a premium subscription",
                color = Color.White.copy(0.85f),
                fontSize = 12.sp
            )
            Spacer(Modifier.height(8.dp))
            Text("Learn more →", color = Color(0xFF69C0FF), fontSize = 11.sp)
        }
    },
    color = Color(0xFF1890FF)
) {
    Icon(Icons.Default.Lock, "Locked feature")
}
```

## All 12 Placements

```kotlin
// Top
TooltipPlacement.TopLeft
TooltipPlacement.Top
TooltipPlacement.TopRight

// Right
TooltipPlacement.RightTop
TooltipPlacement.Right
TooltipPlacement.RightBottom

// Bottom
TooltipPlacement.BottomLeft
TooltipPlacement.Bottom
TooltipPlacement.BottomRight

// Left
TooltipPlacement.LeftTop
TooltipPlacement.Left
TooltipPlacement.LeftBottom
```

## All Trigger Modes

```kotlin
// Hover (default)
trigger = TooltipTrigger.Hover

// Click
trigger = TooltipTrigger.Click

// Long press
trigger = TooltipTrigger.ContextMenu

// Focus (pending implementation)
trigger = TooltipTrigger.Focus
```

## Advanced Features

### Custom Delays

```kotlin
AntTooltip(
    title = "Delayed tooltip",
    mouseEnterDelay = 500,  // 0.5s to show
    mouseLeaveDelay = 200,  // 0.2s to hide
    content = { /* ... */ }
)
```

### Hide Arrow

```kotlin
AntTooltip(
    title = "No arrow",
    arrow = false,
    content = { /* ... */ }
)
```

### Custom Styles

```kotlin
AntTooltip(
    title = "Styled tooltip",
    overlayStyle = Modifier
        .background(Color(0xFFFF4D4F), RoundedCornerShape(12.dp))
        .shadow(8.dp),
    overlayInnerStyle = Modifier.padding(16.dp),
    content = { /* ... */ }
)
```

### Destroy on Hide (Performance)

```kotlin
AntTooltip(
    title = { /* Heavy content */ },
    destroyTooltipOnHide = true,  // Unmount when hidden
    content = { /* ... */ }
)
```

### Dynamic Content (Fresh Mode)

```kotlin
var counter by remember { mutableStateOf(0) }

AntTooltip(
    title = "Count: $counter",
    fresh = true,  // Re-render on every show
    content = {
        Button(onClick = { counter++ }) {
            Text("Click ($counter)")
        }
    }
)
```

### High Z-Index

```kotlin
AntTooltip(
    title = "Always on top",
    zIndex = 9999,
    content = { /* ... */ }
)
```

## Cheat Sheet

| Need | Parameter | Example |
|------|-----------|---------|
| Position | `placement` | `TooltipPlacement.Bottom` |
| How to show | `trigger` | `TooltipTrigger.Click` |
| Custom color | `color` | `Color(0xFF1890FF)` |
| Control state | `open` + `onOpenChange` | `open = isOpen` |
| Delay show | `mouseEnterDelay` | `500` (ms) |
| Delay hide | `mouseLeaveDelay` | `200` (ms) |
| No arrow | `arrow` | `false` |
| Custom style | `overlayStyle` | `Modifier.shadow(4.dp)` |
| Destroy | `destroyTooltipOnHide` | `true` |
| Re-render | `fresh` | `true` |
| Z-index | `zIndex` | `2000` |
| Callback | `onOpenChange` | `{ isOpen -> }` |

## Tips & Tricks

### 1. Prevent Tooltip Flicker

Use delays to prevent tooltips from appearing/disappearing too quickly:

```kotlin
mouseEnterDelay = 100,
mouseLeaveDelay = 100
```

### 2. Long Content

For long content, use a wide tooltip:

```kotlin
overlayInnerStyle = Modifier.width(300.dp)
```

### 3. Multiple Tooltips

Each tooltip is independent. You can have as many as needed:

```kotlin
Row {
    AntTooltip(title = "First") { Icon(...) }
    AntTooltip(title = "Second") { Icon(...) }
    AntTooltip(title = "Third") { Icon(...) }
}
```

### 4. Responsive Position

Use `autoAdjustOverflow` to automatically adjust position if tooltip would overflow:

```kotlin
autoAdjustOverflow = true  // Default
```

### 5. Performance Optimization

For expensive tooltip content, use `destroyTooltipOnHide`:

```kotlin
destroyTooltipOnHide = true
```

## Troubleshooting

### Tooltip doesn't show

1. Check if `title` is not empty
2. Verify trigger mode matches your interaction
3. Ensure content is not blocking pointer events

### Tooltip position is wrong

1. Try different `placement` values
2. Enable `autoAdjustOverflow = true`
3. Check parent container positioning

### Tooltip flickers

1. Increase `mouseEnterDelay` and `mouseLeaveDelay`
2. Check for conflicting pointer events
3. Ensure stable parent layout

### Animation issues

1. Check if content size is stable
2. Use `fresh = false` for static content
3. Verify no conflicting animations

## Best Practices

1. **Keep titles short**: 1-2 lines max
2. **Use appropriate triggers**: Hover for hints, Click for actions
3. **Set reasonable delays**: 100-500ms is usually good
4. **Choose good placements**: Consider available space
5. **Test on all platforms**: Behavior may vary slightly
6. **Use semantic colors**: Match your design system
7. **Provide keyboard access**: Consider Focus trigger
8. **Don't overuse**: Only for helpful information
9. **Test with long content**: Ensure it doesn't overflow
10. **Consider mobile**: Touch interactions differ

## Next Steps

- Read the [Complete API Documentation](./TOOLTIP_API.md)
- Check [React Comparison](./TOOLTIP_COMPARISON.md)
- View [15+ Interactive Examples](../ui/src/commonMain/kotlin/com/antdesign/ui/TooltipExamples.kt)
- Read [Completion Report](../TOOLTIP_COMPLETION_REPORT.md)

## Need Help?

- Check the examples: `TooltipExamplesScreen()`
- Read the API docs: `TOOLTIP_API.md`
- Compare with React: `TOOLTIP_COMPARISON.md`

---

**Ready to use!** Start with the basic example and add features as needed.
