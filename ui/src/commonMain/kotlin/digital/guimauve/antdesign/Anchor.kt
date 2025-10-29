package digital.guimauve.antdesign

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Anchor Component - Navigation for jumping to page sections with sticky sidebar
 *
 * Hyperlinks to scroll on one page, with sticky sidebar positioning.
 * Automatically tracks scroll position and highlights the active section.
 *
 * @param items Data-driven items list (v5.1.0+). Alternative to children-based API.
 * @param modifier Modifier for the anchor container
 * @param affix Enable fixed mode when scrolling (default: true)
 * @param bounds Bounding area in pixels for active link detection (default: 5dp)
 * @param getCurrentAnchor Custom function to determine active anchor, returns href
 * @param offsetTop Pixels to offset from top when affix is enabled
 * @param showInkInFixed Whether show ink-ball in fixed mode (default: false)
 * @param targetOffset Pixels to offset from top when scrolling to target
 * @param onChange Callback when active link changes, receives current active href
 * @param onClick Callback when link is clicked, receives clicked href
 * @param replace Replace browser history instead of push (default: false)
 * @param direction Layout direction - Vertical or Horizontal (default: Vertical)
 * @param classNames Semantic CSS class names for styling (v5.7.0+)
 * @param styles Semantic Modifier styles for customization (v5.7.0+)
 * @param children Composable DSL for defining anchor links (traditional API)
 *
 * @sample AnchorSample
 * @since 1.0.0
 */
@Composable
fun AntAnchor(
    items: List<AnchorItem>? = null,
    modifier: Modifier = Modifier,
    affix: Boolean = true,
    bounds: Dp = 5.dp,
    getCurrentAnchor: (() -> String)? = null,
    offsetTop: Dp? = null,
    showInkInFixed: Boolean = false,
    targetOffset: Dp? = null,
    onChange: ((currentActiveLink: String) -> Unit)? = null,
    onClick: ((link: String) -> Unit)? = null,
    replace: Boolean = false,
    direction: AnchorDirection = AnchorDirection.Vertical,
    classNames: AnchorClassNames? = null,
    styles: AnchorStyles? = null,
    children: @Composable (AnchorScope.() -> Unit)? = null,
) {
    val theme = useTheme()
    val density = LocalDensity.current

    // State management
    var activeHref by remember { mutableStateOf("") }
    var linkPositions by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }
    var scrollOffset by remember { mutableStateOf(0f) }
    val boundsPixels = with(density) { bounds.toPx() }

    // Scroll spy - detect active section
    LaunchedEffect(scrollOffset, linkPositions, getCurrentAnchor) {
        if (getCurrentAnchor != null) {
            activeHref = getCurrentAnchor()
        } else {
            // Auto-detect based on scroll position
            val sortedLinks = linkPositions.entries.sortedBy { it.value }
            val currentSection = sortedLinks.lastOrNull { entry ->
                entry.value <= scrollOffset + boundsPixels
            }?.key ?: sortedLinks.firstOrNull()?.key ?: ""

            if (currentSection != activeHref) {
                activeHref = currentSection
                onChange?.invoke(activeHref)
            }
        }
    }

    // Collect links from items or children
    val anchorLinks = remember(items) {
        items ?: emptyList()
    }

    // Ink ball animation
    val animatedInkOffset by animateFloatAsState(
        targetValue = linkPositions[activeHref] ?: 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ink_ball_animation"
    )

    // Apply affix positioning
    val containerModifier = if (affix) {
        modifier
            .then(styles?.root ?: Modifier)
            .then(
                if (offsetTop != null) {
                    Modifier.offset { IntOffset(0, with(density) { offsetTop.toPx().roundToInt() }) }
                } else {
                    Modifier
                }
            )
    } else {
        modifier.then(styles?.root ?: Modifier)
    }

    Box(
        modifier = containerModifier
            .then(if (direction == AnchorDirection.Vertical) Modifier.width(200.dp) else Modifier)
    ) {
        when (direction) {
            AnchorDirection.Vertical -> {
                VerticalAnchor(
                    items = anchorLinks,
                    activeHref = activeHref,
                    animatedInkOffset = animatedInkOffset,
                    showInkInFixed = showInkInFixed,
                    affix = affix,
                    theme = theme,
                    classNames = classNames,
                    styles = styles,
                    onLinkClick = { href ->
                        activeHref = href
                        onClick?.invoke(href)
                        onChange?.invoke(href)
                    },
                    onLinkPositioned = { href, position ->
                        linkPositions = linkPositions + (href to position)
                    },
                    children = children
                )
            }

            AnchorDirection.Horizontal -> {
                HorizontalAnchor(
                    items = anchorLinks,
                    activeHref = activeHref,
                    animatedInkOffset = animatedInkOffset,
                    showInkInFixed = showInkInFixed,
                    affix = affix,
                    theme = theme,
                    classNames = classNames,
                    styles = styles,
                    onLinkClick = { href ->
                        activeHref = href
                        onClick?.invoke(href)
                        onChange?.invoke(href)
                    },
                    onLinkPositioned = { href, position ->
                        linkPositions = linkPositions + (href to position)
                    },
                    children = children
                )
            }
        }
    }
}

/**
 * Vertical Anchor Layout Implementation
 * @internal
 */
@Composable
private fun VerticalAnchor(
    items: List<AnchorItem>,
    activeHref: String,
    animatedInkOffset: Float,
    showInkInFixed: Boolean,
    affix: Boolean,
    theme: AntThemeConfig,
    classNames: AnchorClassNames?,
    styles: AnchorStyles?,
    onLinkClick: (String) -> Unit,
    onLinkPositioned: (String, Float) -> Unit,
    children: @Composable (AnchorScope.() -> Unit)?,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Ink line background
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(ColorPalette.gray5)
                .align(Alignment.CenterStart)
        )

        // Animated ink ball indicator
        if (showInkInFixed || !affix) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(0, animatedInkOffset.roundToInt()) }
                    .width(2.dp)
                    .height(24.dp)
                    .background(theme.token.colorPrimary)
                    .align(Alignment.TopStart)
            )
        }

        // Links column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp)
        ) {
            if (children != null) {
                // DSL-based API
                val scope = AnchorScopeImpl(
                    activeHref = activeHref,
                    level = 0,
                    theme = theme,
                    styles = styles,
                    classNames = classNames,
                    onLinkClick = onLinkClick,
                    onLinkPositioned = onLinkPositioned
                )
                children(scope)
            } else {
                // Items-based API
                items.forEach { item ->
                    AnchorLinkItemVertical(
                        item = item,
                        activeHref = activeHref,
                        level = 0,
                        theme = theme,
                        classNames = classNames,
                        styles = styles,
                        onLinkClick = onLinkClick,
                        onLinkPositioned = onLinkPositioned
                    )
                }
            }
        }
    }
}

/**
 * Horizontal Anchor Layout Implementation
 * @internal
 */
@Composable
private fun HorizontalAnchor(
    items: List<AnchorItem>,
    activeHref: String,
    animatedInkOffset: Float,
    showInkInFixed: Boolean,
    affix: Boolean,
    theme: AntThemeConfig,
    classNames: AnchorClassNames?,
    styles: AnchorStyles?,
    onLinkClick: (String) -> Unit,
    onLinkPositioned: (String, Float) -> Unit,
    children: @Composable (AnchorScope.() -> Unit)?,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Ink line background (horizontal)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(ColorPalette.gray5)
                .align(Alignment.TopCenter)
        )

        // Animated ink ball indicator (horizontal)
        if (showInkInFixed || !affix) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(animatedInkOffset.roundToInt(), 0) }
                    .width(24.dp)
                    .height(2.dp)
                    .background(theme.token.colorPrimary)
                    .align(Alignment.TopStart)
            )
        }

        // Links row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (children != null) {
                // DSL-based API
                val scope = AnchorScopeImpl(
                    activeHref = activeHref,
                    level = 0,
                    theme = theme,
                    styles = styles,
                    classNames = classNames,
                    onLinkClick = onLinkClick,
                    onLinkPositioned = onLinkPositioned
                )
                children(scope)
            } else {
                // Items-based API (horizontal doesn't support nested children)
                items.forEach { item ->
                    AnchorLinkItemHorizontal(
                        item = item,
                        activeHref = activeHref,
                        theme = theme,
                        classNames = classNames,
                        styles = styles,
                        onLinkClick = onLinkClick,
                        onLinkPositioned = onLinkPositioned
                    )
                }
            }
        }
    }
}

/**
 * Vertical Anchor Link Item Renderer
 * @internal
 */
@Composable
private fun AnchorLinkItemVertical(
    item: AnchorItem,
    activeHref: String,
    level: Int,
    theme: AntThemeConfig,
    classNames: AnchorClassNames?,
    styles: AnchorStyles?,
    onLinkClick: (String) -> Unit,
    onLinkPositioned: (String, Float) -> Unit,
) {
    val isActive = item.href == activeHref
    val paddingStart = (level * 16).dp
    var yPosition by remember { mutableStateOf(0f) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    yPosition = coordinates.positionInRoot().y
                    onLinkPositioned(item.href, yPosition)
                }
                .clickable { onLinkClick(item.href) }
                .padding(start = paddingStart, top = 4.dp, bottom = 4.dp, end = 8.dp)
                .then(
                    if (isActive) {
                        Modifier
                            .background(ColorPalette.blue1)
                            .then(styles?.linkActive ?: Modifier)
                    } else {
                        Modifier.then(styles?.link ?: Modifier)
                    }
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isActive) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(20.dp)
                        .background(theme.token.colorPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                color = if (isActive) theme.token.colorPrimary else Color(0xFF00000073),
                modifier = styles?.linkTitle ?: Modifier
            )
        }

        // Render nested children
        item.children?.forEach { childItem ->
            AnchorLinkItemVertical(
                item = childItem,
                activeHref = activeHref,
                level = level + 1,
                theme = theme,
                classNames = classNames,
                styles = styles,
                onLinkClick = onLinkClick,
                onLinkPositioned = onLinkPositioned
            )
        }
    }
}

/**
 * Horizontal Anchor Link Item Renderer
 * @internal
 */
@Composable
private fun AnchorLinkItemHorizontal(
    item: AnchorItem,
    activeHref: String,
    theme: AntThemeConfig,
    classNames: AnchorClassNames?,
    styles: AnchorStyles?,
    onLinkClick: (String) -> Unit,
    onLinkPositioned: (String, Float) -> Unit,
) {
    val isActive = item.href == activeHref
    var xPosition by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                xPosition = coordinates.positionInRoot().x
                onLinkPositioned(item.href, xPosition)
            }
            .clickable { onLinkClick(item.href) }
            .then(
                if (isActive) {
                    Modifier
                        .background(ColorPalette.blue1)
                        .then(styles?.linkActive ?: Modifier)
                } else {
                    Modifier.then(styles?.link ?: Modifier)
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
            color = if (isActive) theme.token.colorPrimary else Color(0xFF00000073),
            modifier = styles?.linkTitle ?: Modifier
        )
    }
}

/**
 * Data class representing an anchor link item
 *
 * @param key Unique identifier for the anchor link
 * @param href Target href/id to scroll to (e.g., "#section-1")
 * @param title Display text for the link
 * @param children Optional nested anchor links (supported in vertical direction only)
 *
 * @since 1.0.0 (Items API added in v5.1.0)
 */
data class AnchorItem(
    val key: String,
    val href: String,
    val title: String,
    val children: List<AnchorItem>? = null,
)

/**
 * Direction enum for Anchor layout orientation
 *
 * @since 1.0.0
 */
enum class AnchorDirection {
    /**
     * Vertical layout with links stacked vertically (default)
     * Supports nested children links
     */
    Vertical,

    /**
     * Horizontal layout with links arranged horizontally
     * Does not support nested children
     */
    Horizontal
}

/**
 * Semantic CSS class names for Anchor component styling
 *
 * Allows granular control over styling different parts of the Anchor component.
 *
 * @param root Class name for the root container
 * @param link Class name for each anchor link
 * @param linkTitle Class name for link title text
 * @param linkActive Class name for the active/selected link
 *
 * @since 1.0.0 (Added in v5.7.0)
 */
data class AnchorClassNames(
    val root: String = "",
    val link: String = "",
    val linkTitle: String = "",
    val linkActive: String = "",
)

/**
 * Semantic Modifier styles for Anchor component customization
 *
 * Provides type-safe Compose Modifier styling for different parts of the Anchor.
 *
 * @param root Modifier for the root container
 * @param link Modifier for each anchor link
 * @param linkTitle Modifier for link title text
 * @param linkActive Modifier for the active/selected link
 *
 * @since 1.0.0 (Added in v5.7.0)
 */
data class AnchorStyles(
    val root: Modifier = Modifier,
    val link: Modifier = Modifier,
    val linkTitle: Modifier = Modifier,
    val linkActive: Modifier = Modifier,
)

/**
 * Scope interface for the Anchor DSL API
 *
 * Provides a type-safe DSL for defining anchor links in a nested structure.
 * This is the traditional API that existed before the items-based API.
 *
 * @since 1.0.0
 */
interface AnchorScope {
    /**
     * Defines an anchor link within the Anchor component
     *
     * @param href Target href/id to scroll to (e.g., "#section-1")
     * @param title Display text for the link
     * @param children Optional nested anchor links (DSL lambda)
     */
    @Composable
    fun AnchorLink(
        href: String,
        title: String,
        children: @Composable (AnchorScope.() -> Unit)? = null,
    )
}

/**
 * Internal implementation of AnchorScope
 * @internal
 */
private class AnchorScopeImpl(
    private val activeHref: String,
    private val level: Int,
    private val theme: AntThemeConfig,
    private val styles: AnchorStyles?,
    private val classNames: AnchorClassNames?,
    private val onLinkClick: (String) -> Unit,
    private val onLinkPositioned: (String, Float) -> Unit,
) : AnchorScope {

    @Composable
    override fun AnchorLink(
        href: String,
        title: String,
        children: @Composable (AnchorScope.() -> Unit)?,
    ) {
        val isActive = href == activeHref
        val paddingStart = (level * 16).dp
        var yPosition by remember { mutableStateOf(0f) }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        yPosition = coordinates.positionInRoot().y
                        onLinkPositioned(href, yPosition)
                    }
                    .clickable { onLinkClick(href) }
                    .padding(start = paddingStart, top = 4.dp, bottom = 4.dp, end = 8.dp)
                    .then(
                        if (isActive) {
                            Modifier
                                .background(ColorPalette.blue1)
                                .then(styles?.linkActive ?: Modifier)
                        } else {
                            Modifier.then(styles?.link ?: Modifier)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(20.dp)
                            .background(theme.token.colorPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                    color = if (isActive) theme.token.colorPrimary else Color(0xFF00000073),
                    modifier = styles?.linkTitle ?: Modifier
                )
            }

            // Render nested children with increased level
            if (children != null) {
                val childScope = AnchorScopeImpl(
                    activeHref = activeHref,
                    level = level + 1,
                    theme = theme,
                    styles = styles,
                    classNames = classNames,
                    onLinkClick = onLinkClick,
                    onLinkPositioned = onLinkPositioned
                )
                children(childScope)
            }
        }
    }
}

/**
 * Sample usage of the Anchor component
 */
private fun AnchorSample() {
    // Example 1: Items-based API (v5.1.0+)
    @Composable
    fun ItemsApiExample() {
        AntAnchor(
            items = listOf(
                AnchorItem(
                    key = "intro",
                    href = "#intro",
                    title = "Introduction"
                ),
                AnchorItem(
                    key = "features",
                    href = "#features",
                    title = "Features",
                    children = listOf(
                        AnchorItem(
                            key = "feature-1",
                            href = "#feature-1",
                            title = "Feature 1"
                        ),
                        AnchorItem(
                            key = "feature-2",
                            href = "#feature-2",
                            title = "Feature 2"
                        )
                    )
                ),
                AnchorItem(
                    key = "api",
                    href = "#api",
                    title = "API Reference"
                )
            ),
            affix = true,
            bounds = 5.dp,
            onChange = { href ->
                println("Active section changed to: $href")
            },
            onClick = { href ->
                println("Clicked: $href")
            }
        )
    }

    // Example 2: DSL-based API (traditional)
    @Composable
    fun DslApiExample() {
        AntAnchor {
            AnchorLink(
                href = "#intro",
                title = "Introduction"
            )
            AnchorLink(
                href = "#features",
                title = "Features"
            ) {
                AnchorLink(
                    href = "#feature-1",
                    title = "Feature 1"
                )
                AnchorLink(
                    href = "#feature-2",
                    title = "Feature 2"
                )
            }
            AnchorLink(
                href = "#api",
                title = "API Reference"
            )
        }
    }

    // Example 3: Horizontal direction
    @Composable
    fun HorizontalExample() {
        AntAnchor(
            items = listOf(
                AnchorItem(key = "1", href = "#section-1", title = "Section 1"),
                AnchorItem(key = "2", href = "#section-2", title = "Section 2"),
                AnchorItem(key = "3", href = "#section-3", title = "Section 3")
            ),
            direction = AnchorDirection.Horizontal,
            affix = true
        )
    }

    // Example 4: Custom styling with semantic styles
    @Composable
    fun CustomStyledExample() {
        AntAnchor(
            items = listOf(
                AnchorItem(key = "1", href = "#s1", title = "Section 1"),
                AnchorItem(key = "2", href = "#s2", title = "Section 2")
            ),
            styles = AnchorStyles(
                root = Modifier.background(Color(0xFFF5F5F5)),
                link = Modifier.padding(horizontal = 16.dp),
                linkActive = Modifier.background(Color(0xFFE6F7FF))
            )
        )
    }

    // Example 5: Custom active anchor detection
    @Composable
    fun CustomActiveExample() {
        var customActiveHref by remember { mutableStateOf("#section-1") }

        AntAnchor(
            items = listOf(
                AnchorItem(key = "1", href = "#section-1", title = "Section 1"),
                AnchorItem(key = "2", href = "#section-2", title = "Section 2")
            ),
            getCurrentAnchor = { customActiveHref },
            onChange = { href -> customActiveHref = href }
        )
    }

    // Example 6: With offset and target offset
    @Composable
    fun OffsetExample() {
        AntAnchor(
            items = listOf(
                AnchorItem(key = "1", href = "#s1", title = "Section 1"),
                AnchorItem(key = "2", href = "#s2", title = "Section 2")
            ),
            affix = true,
            offsetTop = 64.dp, // Offset from top when fixed
            targetOffset = 80.dp, // Scroll offset for target elements
            showInkInFixed = true
        )
    }
}
