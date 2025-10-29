# Rate Component - Complete API Documentation

## Overview

The `AntRate` component is a complete Kotlin Multiplatform implementation of Ant Design's Rate component with **100% React parity**. It provides rating functionality with support for full stars, half stars, custom characters, tooltips, keyboard navigation, and comprehensive accessibility features.

## React Parity Status: ✅ 100% Complete

All React Ant Design Rate props and features are implemented:

| Feature | React | Compose | Status |
|---------|-------|---------|--------|
| `value` | ✅ | ✅ | ✅ Controlled mode |
| `defaultValue` | ✅ | ✅ | ✅ Uncontrolled mode |
| `onChange` | ✅ | ✅ | ✅ Value change callback |
| `count` | ✅ | ✅ | ✅ Number of stars |
| `allowHalf` | ✅ | ✅ | ✅ Half star selection |
| `allowClear` | ✅ | ✅ | ✅ Clear by clicking same value |
| `disabled` | ✅ | ✅ | ✅ Read-only mode |
| `character` | ✅ | ✅ | ✅ Custom character composable |
| `className` | ✅ | ✅ | ✅ For API parity (Compose uses modifiers) |
| `style` | ✅ | ✅ | ✅ Maps to Modifier |
| `tooltips` | ✅ | ✅ | ✅ Tooltip array with auto-positioning |
| `onHoverChange` | ✅ | ✅ | ✅ Hover state callback |
| `autoFocus` | ✅ | ✅ | ✅ Auto-focus on mount |
| `tabIndex` | ✅ | ✅ | ✅ Keyboard navigation |
| `onFocus` | ✅ | ✅ | ✅ Focus callback |
| `onBlur` | ✅ | ✅ | ✅ Blur callback |
| `onKeyDown` | ✅ | ✅ | ✅ Keyboard event callback |
| `styles` | ✅ | ✅ | ✅ Semantic styles (root, star) |
| `classNames` | ✅ | ✅ | ✅ Semantic class names |

## API Reference

### AntRate (Main Component)

```kotlin
@Composable
fun AntRate(
    modifier: Modifier = Modifier,
    value: Double? = null,
    onChange: ((Double) -> Unit)? = null,
    defaultValue: Double = 0.0,
    count: Int = 5,
    allowHalf: Boolean = false,
    allowClear: Boolean = true,
    disabled: Boolean = false,
    character: (@Composable (RateCharacterProps) -> Unit)? = null,
    className: String? = null,
    style: Modifier = Modifier,
    tooltips: List<String>? = null,
    onHoverChange: ((Double?) -> Unit)? = null,
    autoFocus: Boolean = false,
    tabIndex: Int? = null,
    onBlur: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onKeyDown: ((KeyEvent) -> Unit)? = null,
    styles: RateStyles = RateStyles(),
    classNames: RateClassNames = RateClassNames()
)
```

### Parameters

#### Core Props

- **`modifier: Modifier`** - Base modifier for the component container
- **`value: Double?`** - Controlled rating value (0.0 to count). Use `null` for uncontrolled mode
- **`onChange: ((Double) -> Unit)?`** - Callback invoked when rating changes
- **`defaultValue: Double`** - Default rating value for uncontrolled mode (default: `0.0`)
- **`count: Int`** - Total number of stars to display (default: `5`)

#### Selection & Interaction

- **`allowHalf: Boolean`** - Allow selecting half stars (e.g., 2.5) by clicking left/right half (default: `false`)
- **`allowClear: Boolean`** - Allow clearing rating by clicking the same star again (default: `true`)
- **`disabled: Boolean`** - Disable interaction, make read-only (default: `false`)

#### Customization

- **`character: (@Composable (RateCharacterProps) -> Unit)?`** - Custom character composable for each star. Receives `RateCharacterProps` with `index`, `value`, `isActive`, and `isHalf`
- **`className: String?`** - Custom class name (kept for React parity, Compose uses modifiers)
- **`style: Modifier`** - Deprecated style prop (use `modifier` instead, kept for React parity)

#### Tooltips

- **`tooltips: List<String>?`** - Array of tooltip texts, one per star (e.g., `["terrible", "bad", "normal", "good", "wonderful"]`)

#### Callbacks

- **`onHoverChange: ((Double?) -> Unit)?`** - Callback invoked when hovering over stars. Receives hovered value or `null` when not hovering
- **`onFocus: (() -> Unit)?`** - Callback invoked when component receives focus
- **`onBlur: (() -> Unit)?`** - Callback invoked when component loses focus
- **`onKeyDown: ((KeyEvent) -> Unit)?`** - Callback for keyboard events

#### Focus & Accessibility

- **`autoFocus: Boolean`** - Automatically focus on mount (default: `false`)
- **`tabIndex: Int?`** - Tab index for keyboard navigation (default: `null` = default behavior)

#### Semantic Styling

- **`styles: RateStyles`** - Semantic styles for component parts (`root`, `star`)
- **`classNames: RateClassNames`** - Semantic class names for component parts (kept for API parity)

### RateCharacterProps

Props passed to custom `character` composable:

```kotlin
data class RateCharacterProps(
    val index: Int,        // 0-based index (0 to count-1)
    val value: Double,     // Current rating value
    val isActive: Boolean, // Whether this star is active/filled
    val isHalf: Boolean    // Whether this is a half star
)
```

### RateStyles

Semantic styles for Rate parts:

```kotlin
data class RateStyles(
    val root: Modifier = Modifier,  // Style for root container
    val star: Modifier = Modifier   // Style for each star
)
```

### RateClassNames

Semantic class names for Rate parts:

```kotlin
data class RateClassNames(
    val root: String? = null,  // Class for root container
    val star: String? = null   // Class for each star
)
```

## Usage Examples

### Basic Usage

```kotlin
var rating by remember { mutableStateOf(0.0) }

AntRate(
    value = rating,
    onChange = { rating = it }
)
```

### Half Stars

```kotlin
var rating by remember { mutableStateOf(2.5) }

AntRate(
    value = rating,
    onChange = { rating = it },
    allowHalf = true
)
```

### With Tooltips

```kotlin
var rating by remember { mutableStateOf(3.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    tooltips = listOf("terrible", "bad", "normal", "good", "wonderful")
)
```

### Read-Only (Disabled)

```kotlin
AntRate(
    value = 4.0,
    onChange = null,
    disabled = true
)
```

### Custom Character

```kotlin
var rating by remember { mutableStateOf(3.0) }

// Heart icon
AntRate(
    value = rating,
    onChange = { rating = it },
    character = { props ->
        Text(
            text = "♥",
            fontSize = 24.sp,
            color = if (props.isActive) Color(0xFFEB2F96) else ColorPalette.gray5
        )
    }
)
```

### Custom Character with Half Support

```kotlin
var rating by remember { mutableStateOf(2.5) }

AntRate(
    value = rating,
    onChange = { rating = it },
    allowHalf = true,
    character = { props ->
        Box(modifier = Modifier.size(24.dp)) {
            when {
                props.isHalf -> {
                    // Draw half-filled custom character
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Custom drawing logic for half state
                    }
                }
                props.isActive -> {
                    // Full active state
                    Text("★", fontSize = 24.sp, color = Color.Red)
                }
                else -> {
                    // Empty state
                    Text("☆", fontSize = 24.sp, color = Color.Gray)
                }
            }
        }
    }
)
```

### Custom Star Count

```kotlin
var rating by remember { mutableStateOf(5.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    count = 10
)
```

### Keyboard Navigation

```kotlin
var rating by remember { mutableStateOf(0.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    allowHalf = true,
    onKeyDown = { event ->
        println("Key pressed: ${event.key}")
    }
)
```

**Supported keyboard shortcuts:**
- **Arrow Right / Arrow Up**: Increase rating by 1 (or 0.5 if `allowHalf`)
- **Arrow Left / Arrow Down**: Decrease rating by 1 (or 0.5 if `allowHalf`)
- **Home**: Jump to minimum (1.0 or 0.5)
- **End**: Jump to maximum (`count`)
- **Number keys (1-9)**: Set specific rating
- **0**: Clear rating (if `allowClear`)

### Auto Focus

```kotlin
var rating by remember { mutableStateOf(0.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    autoFocus = true,
    onFocus = { println("Rate focused") },
    onBlur = { println("Rate blurred") }
)
```

### Controlled Mode

```kotlin
var rating by remember { mutableStateOf(3.0) }

Row {
    AntButton(
        onClick = { rating = (rating - 1).coerceAtLeast(0.0) }
    ) {
        Text("-")
    }

    AntRate(
        value = rating,
        onChange = { rating = it }
    )

    AntButton(
        onClick = { rating = (rating + 1).coerceAtMost(5.0) }
    ) {
        Text("+")
    }
}
```

### Uncontrolled Mode

```kotlin
AntRate(
    defaultValue = 3.0,
    onChange = { newValue ->
        println("Rating changed to: $newValue")
    }
)
```

### Hover Change Callback

```kotlin
var rating by remember { mutableStateOf(0.0) }
var hoverValue by remember { mutableStateOf<Double?>(null) }

AntRate(
    value = rating,
    onChange = { rating = it },
    onHoverChange = { hoverValue = it }
)

Text("Hovering: ${hoverValue ?: "none"}")
```

### Custom Styling

```kotlin
var rating by remember { mutableStateOf(3.0) }

// Large stars
AntRate(
    value = rating,
    onChange = { rating = it },
    styles = RateStyles(
        star = Modifier.size(32.dp)
    )
)

// Small stars with custom spacing
AntRate(
    value = rating,
    onChange = { rating = it },
    styles = RateStyles(
        root = Modifier.padding(8.dp),
        star = Modifier.size(16.dp)
    )
)
```

### Allow Clear

```kotlin
var rating by remember { mutableStateOf(3.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    allowClear = true  // Click same star to clear
)

// Click the currently selected star to set rating to 0
```

### Disable Clear

```kotlin
var rating by remember { mutableStateOf(3.0) }

AntRate(
    value = rating,
    onChange = { rating = it },
    allowClear = false  // Cannot clear by clicking same star
)
```

## Simplified Int Overload

For simpler use cases with integer ratings:

```kotlin
@Composable
fun AntRate(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    count: Int = 5,
    allowHalf: Boolean = false,
    allowClear: Boolean = true,
    disabled: Boolean = false,
    character: String = "★",
    tooltips: List<String>? = null
)
```

**Example:**

```kotlin
var rating by remember { mutableStateOf(3) }

AntRate(
    value = rating,
    onValueChange = { rating = it },
    character = "★"
)
```

## Comparison: React vs Compose

### React (TypeScript)

```tsx
import { Rate } from 'antd';

function MyComponent() {
  const [rating, setRating] = useState(3);

  return (
    <Rate
      value={rating}
      onChange={(value) => setRating(value)}
      allowHalf
      tooltips={['terrible', 'bad', 'normal', 'good', 'wonderful']}
      onHoverChange={(value) => console.log(value)}
      autoFocus
      onFocus={() => console.log('focused')}
      onBlur={() => console.log('blurred')}
      onKeyDown={(e) => console.log(e.key)}
      character={<HeartOutlined />}
      count={5}
      disabled={false}
      allowClear
    />
  );
}
```

### Compose (Kotlin)

```kotlin
@Composable
fun MyComponent() {
    var rating by remember { mutableStateOf(3.0) }

    AntRate(
        value = rating,
        onChange = { rating = it },
        allowHalf = true,
        tooltips = listOf("terrible", "bad", "normal", "good", "wonderful"),
        onHoverChange = { println(it) },
        autoFocus = true,
        onFocus = { println("focused") },
        onBlur = { println("blurred") },
        onKeyDown = { println(it.key) },
        character = { props ->
            Text("♥", fontSize = 24.sp,
                 color = if (props.isActive) Color.Red else Color.Gray)
        },
        count = 5,
        disabled = false,
        allowClear = true
    )
}
```

## Implementation Details

### Half Star Detection

When `allowHalf = true`, clicking on the left half of a star selects the half value (e.g., 2.5), while clicking the right half selects the full value (e.g., 3.0).

The component uses pointer position detection:
- Left 50% of star → Half value (index + 0.5)
- Right 50% of star → Full value (index + 1.0)

### Star Rendering

The default star is rendered using Canvas with a 5-pointed star path:
- **Empty state**: Gray color (#D9D9D9)
- **Full state**: Gold color (#FAAD14)
- **Half state**: Left half gold, right half gray
- **Disabled state**: Light gray (#F0F0F0)

### Tooltip Positioning

Tooltips automatically position themselves above the hovered star using a Popup with calculated offsets based on star coordinates.

### Keyboard Navigation

The component registers keyboard handlers on the root Row and responds to:
- Arrow keys for increment/decrement
- Number keys for direct selection
- Home/End for min/max
- 0 for clear (if `allowClear`)

### Focus Management

The component uses Compose's `FocusRequester` for focus management:
- `autoFocus = true` requests focus after a 100ms delay on mount
- `onFocusChanged` tracks focus state transitions
- Focus/blur callbacks are invoked only on state changes

### Accessibility

The component includes proper semantics:
- `role = Role.Slider` for screen readers
- `contentDescription` with current rating
- Keyboard navigation support
- Focus indicators (via Compose's default behavior)

## Theme Integration

The Rate component integrates with the Ant Design theme:
- Active color: `ColorPalette.gold6` (#FAAD14)
- Inactive color: `ColorPalette.gray5` (#D9D9D9)
- Disabled color: `ColorPalette.gray4` (#F0F0F0)

## Best Practices

1. **Use controlled mode** for most cases where you need to track the rating value
2. **Use `allowHalf = true`** for more granular ratings (e.g., 2.5 stars)
3. **Provide tooltips** for better user experience describing rating levels
4. **Use `disabled = true`** for displaying ratings without allowing changes
5. **Implement `onHoverChange`** for real-time preview of ratings
6. **Use keyboard navigation** by enabling focus for accessibility
7. **Custom characters** should maintain consistent sizing for visual coherence

## Testing Examples

See `RateExamples.kt` for comprehensive examples of all features.

## Performance Considerations

- The component uses `remember` for internal state management
- Pointer input handlers are optimized with proper keys
- Tooltip popups only render when hovering
- Focus requester is created once and reused

## Browser/Platform Support

Works across all Kotlin Multiplatform targets:
- ✅ JVM (Desktop)
- ✅ JS (Web)
- ✅ iOS
- ✅ Android
- ✅ Native

## Migration from Old Implementation

If you're upgrading from the minimal implementation:

### Old (80%)
```kotlin
AntRate(
    value = rating,
    onValueChange = { rating = it },
    count = 5,
    allowHalf = false
)
```

### New (100%)
```kotlin
AntRate(
    value = rating.toDouble(),  // Now uses Double for half-star precision
    onChange = { rating = it.toInt() },
    count = 5,
    allowHalf = true,  // Now fully functional
    tooltips = listOf("bad", "normal", "good", "very good", "excellent"),
    onHoverChange = { hoveredValue -> println("Hovering: $hoveredValue") },
    autoFocus = false,
    onFocus = { println("Focused") },
    onBlur = { println("Blurred") }
)
```

Or use the Int overload for backward compatibility:
```kotlin
AntRate(
    value = rating,
    onValueChange = { rating = it },
    count = 5
)
```

## License

Part of Ant Design KMP library. See main project license.
