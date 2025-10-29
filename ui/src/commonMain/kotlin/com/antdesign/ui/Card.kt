package com.antdesign.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Card size variants matching Ant Design
 */
enum class CardSize {
    Default,
    Small
}

/**
 * Card type variants
 */
enum class CardType {
    Default,
    Inner
}

/**
 * Card variant for border styling
 */
enum class CardVariant {
    Outlined,
    Borderless
}

/**
 * Card tab item for tabbed cards
 */
data class CardTab(
    val key: String,
    val label: String,
    val disabled: Boolean = false
)

/**
 * Semantic class names for Card sections
 */
data class CardClassNames(
    val header: String? = null,
    val body: String? = null,
    val extra: String? = null,
    val title: String? = null,
    val actions: String? = null,
    val cover: String? = null
)

/**
 * Semantic styles (Modifiers) for Card sections
 */
data class CardStyles(
    val header: Modifier? = null,
    val body: Modifier? = null,
    val extra: Modifier? = null,
    val title: Modifier? = null,
    val actions: Modifier? = null,
    val cover: Modifier? = null
)

/**
 * Card Meta component for structured content (avatar, title, description)
 */
@Composable
fun CardMeta(
    modifier: Modifier = Modifier,
    avatar: (@Composable () -> Unit)? = null,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    description: String? = null,
    descriptionComposable: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        avatar?.let {
            Box(modifier = Modifier.align(Alignment.Top)) {
                it()
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (title != null || titleComposable != null) {
                if (titleComposable != null) {
                    titleComposable()
                } else {
                    Text(
                        text = title!!,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF000000).copy(alpha = 0.85f)
                    )
                }
            }

            if (description != null || descriptionComposable != null) {
                if (descriptionComposable != null) {
                    descriptionComposable()
                } else {
                    Text(
                        text = description!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF000000).copy(alpha = 0.45f)
                    )
                }
            }
        }
    }
}

/**
 * Ant Design Card component with full feature parity
 *
 * @param modifier Modifier for the card container
 * @param title Card title as String
 * @param titleComposable Card title as Composable (takes precedence over title)
 * @param extra Extra content in the header (top-right)
 * @param cover Cover image/media at the top of the card
 * @param actions List of action items displayed in the footer
 * @param tabList List of tabs to display in the header
 * @param activeTabKey Currently active tab key (controlled)
 * @param defaultActiveTabKey Default active tab key (uncontrolled)
 * @param onTabChange Callback when tab changes
 * @param tabBarExtraContent Extra content in the tab bar
 * @param bordered Whether the card has a border (deprecated, use variant)
 * @param variant Card border variant (Outlined or Borderless)
 * @param hoverable Whether the card has hover effects
 * @param loading Show loading skeleton
 * @param size Card size (Default or Small)
 * @param type Card type (Default or Inner)
 * @param headStyle Modifier for the header (deprecated, use styles.header)
 * @param bodyStyle Modifier for the body (deprecated, use styles.body)
 * @param classNames Semantic class names for different sections
 * @param styles Semantic styles (Modifiers) for different sections
 * @param onClick Click handler for the entire card
 * @param content Card content
 */
@Composable
fun AntCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
    cover: (@Composable () -> Unit)? = null,
    actions: List<@Composable () -> Unit> = emptyList(),
    tabList: List<CardTab> = emptyList(),
    activeTabKey: String? = null,
    defaultActiveTabKey: String? = null,
    onTabChange: ((String) -> Unit)? = null,
    tabBarExtraContent: (@Composable () -> Unit)? = null,
    bordered: Boolean = true,
    variant: CardVariant = if (bordered) CardVariant.Outlined else CardVariant.Borderless,
    hoverable: Boolean = false,
    loading: Boolean = false,
    size: CardSize = CardSize.Default,
    type: CardType = CardType.Default,
    headStyle: Modifier = Modifier,
    bodyStyle: Modifier = Modifier,
    classNames: CardClassNames? = null,
    styles: CardStyles? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    // Calculate padding based on size
    val padding = when (size) {
        CardSize.Small -> 12.dp
        CardSize.Default -> 24.dp
    }

    // Manage tab state (controlled or uncontrolled)
    var internalTabKey by remember {
        mutableStateOf(activeTabKey ?: defaultActiveTabKey ?: tabList.firstOrNull()?.key ?: "")
    }
    val currentTabKey = activeTabKey ?: internalTabKey

    // Hover interaction for hoverable cards
    val hoverInteraction = remember { MutableInteractionSource() }
    val isHovered by hoverInteraction.collectIsHoveredAsState()

    // Calculate elevation based on hover state
    val elevation = when {
        hoverable && isHovered -> 8.dp
        hoverable -> 2.dp
        else -> 1.dp
    }

    // Determine border based on variant
    val cardBorder = when (variant) {
        CardVariant.Outlined -> BorderStroke(1.dp, Color(0xFFD9D9D9))
        CardVariant.Borderless -> null
    }

    // Background color based on type
    val backgroundColor = when (type) {
        CardType.Inner -> Color(0xFFFAFAFA)
        CardType.Default -> Color.White
    }

    // Card content builder
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cover section
            if (cover != null) {
                Box(
                    modifier = (styles?.cover ?: Modifier)
                        .fillMaxWidth()
                ) {
                    cover()
                }
            }

            // Header section (title, extra, tabs)
            if (title != null || titleComposable != null || extra != null || tabList.isNotEmpty()) {
                Column(
                    modifier = (styles?.header ?: headStyle)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    // Title and extra wrapper
                    if (title != null || titleComposable != null || extra != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Title
                            if (title != null || titleComposable != null) {
                                Box(
                                    modifier = (styles?.title ?: Modifier)
                                        .weight(1f, fill = false)
                                ) {
                                    if (titleComposable != null) {
                                        titleComposable()
                                    } else {
                                        Text(
                                            text = title!!,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color(0xFF000000).copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }

                            // Extra content
                            if (extra != null) {
                                Box(
                                    modifier = (styles?.extra ?: Modifier)
                                        .padding(start = 8.dp)
                                ) {
                                    extra()
                                }
                            }
                        }
                    }

                    // Tabs section
                    if (tabList.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = padding),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TabRow(
                                selectedTabIndex = tabList.indexOfFirst { it.key == currentTabKey }.coerceAtLeast(0),
                                modifier = Modifier.weight(1f),
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFF1890FF),
                                divider = {}
                            ) {
                                tabList.forEach { tab ->
                                    Tab(
                                        selected = tab.key == currentTabKey,
                                        onClick = {
                                            if (!tab.disabled) {
                                                internalTabKey = tab.key
                                                onTabChange?.invoke(tab.key)
                                            }
                                        },
                                        enabled = !tab.disabled,
                                        text = {
                                            Text(
                                                text = tab.label,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    )
                                }
                            }

                            // Tab bar extra content
                            if (tabBarExtraContent != null) {
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    tabBarExtraContent()
                                }
                            }
                        }
                    }
                }
            }

            // Body section
            Box(
                modifier = (styles?.body ?: bodyStyle)
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                if (loading) {
                    // Loading skeleton
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .background(
                                        color = Color(0xFFF0F0F0),
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                    }
                } else {
                    Column(content = content)
                }
            }

            // Actions section
            if (actions.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color(0xFFD9D9D9)
                )
                Row(
                    modifier = (styles?.actions ?: Modifier)
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions.forEachIndexed { index, action ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            action()
                        }

                        // Add divider between actions
                        if (index < actions.size - 1) {
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = Color(0xFFD9D9D9)
                            )
                        }
                    }
                }
            }
        }
    }

    // Render card with or without click handler
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier
                .then(if (hoverable) Modifier.hoverable(hoverInteraction) else Modifier),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = cardBorder,
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            shape = MaterialTheme.shapes.medium,
            content = cardContent
        )
    } else {
        Card(
            modifier = modifier
                .then(if (hoverable) Modifier.hoverable(hoverInteraction) else Modifier),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = cardBorder,
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            shape = MaterialTheme.shapes.medium,
            content = cardContent
        )
    }
}
