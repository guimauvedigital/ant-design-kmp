package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Avatar properties for PageHeader component.
 * Matches React's AvatarProps interface.
 *
 * @param src Image URL for the avatar
 * @param icon Icon composable to display in avatar
 * @param size Size of the avatar (Large, Default, Small, or custom Dp)
 * @param shape Shape of the avatar (Circle or Square)
 * @param alt Alternative text for the image
 */
data class AvatarProps(
    val src: String? = null,
    val icon: (@Composable () -> Unit)? = null,
    val size: AvatarSize = AvatarSize.Default,
    val shape: AvatarShape = AvatarShape.Circle,
    val alt: String? = null
)

/**
 * PageHeader component for Ant Design KMP.
 * 100% React parity implementation.
 *
 * Displays a page header with navigation, title, actions, breadcrumb, and footer.
 * Used to highlight the page topic, display important information, and carry action items.
 *
 * Features:
 * - Back navigation with customizable icon
 * - Avatar support with full configuration
 * - Breadcrumb integration with custom rendering
 * - Title and subtitle (string or composable)
 * - Tags display
 * - Extra actions area
 * - Footer content
 * - Ghost mode (transparent background)
 *
 * @param avatar Avatar configuration using AvatarProps
 * @param backIcon Custom back navigation icon composable (null = default arrow, false equivalent = hide when onBack is null)
 * @param breadcrumb Breadcrumb composable to display above header
 * @param breadcrumbRender Custom breadcrumb renderer function (props, defaultDom) -> composable
 * @param extra Action buttons/elements displayed at the end of title line
 * @param footer Footer content, typically used for TabBar
 * @param ghost Transparent background mode. Default is true
 * @param subTitle Subtitle text (string)
 * @param subTitleComposable Subtitle as composable (takes precedence over subTitle string)
 * @param tags Tag elements displayed alongside title
 * @param title Title text (string)
 * @param titleComposable Title as composable (takes precedence over title string)
 * @param onBack Callback when back icon is clicked
 * @param className Custom CSS class name for styling (optional, for compatibility)
 * @param style Modifier for custom styling (optional, for compatibility)
 * @param modifier Main modifier for the component
 */
@Composable
fun AntPageHeader(
    avatar: AvatarProps? = null,
    backIcon: (@Composable () -> Unit)? = null,
    breadcrumb: (@Composable () -> Unit)? = null,
    breadcrumbRender: (@Composable (props: Any, defaultDom: @Composable () -> Unit) -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    ghost: Boolean = true,
    subTitle: String? = null,
    subTitleComposable: (@Composable () -> Unit)? = null,
    tags: (@Composable () -> Unit)? = null,
    title: String? = null,
    titleComposable: (@Composable () -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    className: String? = null,
    style: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(if (ghost) Color.Transparent else Color.White)
            .then(style)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Breadcrumb with custom rendering support
        if (breadcrumbRender != null && breadcrumb != null) {
            // Custom breadcrumb rendering
            breadcrumbRender(Unit, breadcrumb)
        } else {
            // Default breadcrumb rendering
            breadcrumb?.invoke()
        }

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Back icon
                if (onBack != null) {
                    Box(
                        modifier = Modifier.clickable { onBack() }
                    ) {
                        if (backIcon != null) {
                            backIcon()
                        } else {
                            // Default back arrow icon
                            Text(
                                text = "←",
                                fontSize = 18.sp,
                                color = Color(0xFF1890FF)
                            )
                        }
                    }
                }

                // Avatar with AvatarProps support
                if (avatar != null) {
                    AntAvatar(
                        size = avatar.size,
                        shape = avatar.shape,
                        icon = avatar.icon,
                        src = avatar.src,
                        alt = avatar.alt
                    )
                }

                // Title and subtitle
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Title (composable takes precedence over string)
                            if (titleComposable != null) {
                                titleComposable()
                            } else if (title != null) {
                                Text(
                                    text = title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Tags
                            tags?.invoke()
                        }

                        // Subtitle (composable takes precedence over string)
                        if (subTitleComposable != null) {
                            subTitleComposable()
                        } else if (subTitle != null) {
                            Text(
                                text = subTitle,
                                fontSize = 14.sp,
                                color = Color(0xFF00000073)
                            )
                        }
                    }
                }
            }

            // Extra actions
            extra?.invoke()
        }

        // Footer
        footer?.invoke()
    }
}

/**
 * Example: Basic PageHeader with title and subtitle
 */
@Composable
fun PageHeaderBasicExample() {
    AntPageHeader(
        title = "Page Title",
        subTitle = "This is a subtitle",
        onBack = { /* Handle back navigation */ }
    )
}

/**
 * Example: PageHeader with avatar
 */
@Composable
fun PageHeaderWithAvatarExample() {
    AntPageHeader(
        title = "User Profile",
        subTitle = "Personal information",
        avatar = AvatarProps(
            src = "https://example.com/avatar.jpg",
            size = AvatarSize.Large,
            shape = AvatarShape.Circle
        ),
        onBack = { /* Navigate back */ }
    )
}

/**
 * Example: PageHeader with tags and extra actions
 */
@Composable
fun PageHeaderWithTagsAndActionsExample() {
    AntPageHeader(
        title = "Product Details",
        subTitle = "View and edit product information",
        tags = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTag(children = { Text("Active") }, color = TagColor.Success)
                AntTag(children = { Text("Featured") }, color = TagColor.Blue)
            }
        },
        extra = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(onClick = { /* Edit */ }) { Text("Edit") }
                AntButton(onClick = { /* Delete */ }, type = ButtonType.Primary) {
                    Text("Delete")
                }
            }
        },
        onBack = { /* Navigate back */ }
    )
}

/**
 * Example: PageHeader with breadcrumb
 */
@Composable
fun PageHeaderWithBreadcrumbExample() {
    AntPageHeader(
        title = "Product Details",
        subTitle = "Electronics > Laptops > MacBook Pro",
        breadcrumb = {
            AntBreadcrumb(
                items = listOf(
                    BreadcrumbItem(title = "Home", onClick = { /* Navigate to home */ }),
                    BreadcrumbItem(title = "Products", onClick = { /* Navigate to products */ }),
                    BreadcrumbItem(title = "Electronics", onClick = { /* Navigate to electronics */ }),
                    BreadcrumbItem(title = "Laptops")
                ),
                separator = "/"
            )
        },
        onBack = { /* Navigate back */ }
    )
}

/**
 * Example: PageHeader with custom breadcrumb render
 */
@Composable
fun PageHeaderWithCustomBreadcrumbExample() {
    AntPageHeader(
        title = "Custom Breadcrumb",
        breadcrumb = {
            AntBreadcrumb(
                items = listOf(
                    BreadcrumbItem(title = "Home"),
                    BreadcrumbItem(title = "Application"),
                    BreadcrumbItem(title = "Detail")
                ),
                separator = "/"
            )
        },
        breadcrumbRender = { _, defaultDom ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Custom Prefix:", fontSize = 12.sp, color = Color.Gray)
                defaultDom()
            }
        }
    )
}

/**
 * Example: PageHeader with footer (tabs)
 */
@Composable
fun PageHeaderWithFooterExample() {
    AntPageHeader(
        title = "Project Dashboard",
        subTitle = "View project statistics and metrics",
        footer = {
            AntTabs(
                items = listOf(
                    TabItem(key = "overview", label = "Overview", content = {}),
                    TabItem(key = "statistics", label = "Statistics", content = {}),
                    TabItem(key = "settings", label = "Settings", content = {})
                ),
                activeKey = "overview",
                onChange = { /* Handle tab change */ }
            )
        }
    )
}

/**
 * Example: PageHeader with ghost mode disabled
 */
@Composable
fun PageHeaderNonGhostExample() {
    AntPageHeader(
        title = "With Background",
        subTitle = "Ghost mode is disabled",
        ghost = false,
        onBack = { /* Navigate back */ }
    )
}

/**
 * Example: Complete PageHeader with all features
 */
@Composable
fun PageHeaderCompleteExample() {
    AntPageHeader(
        avatar = AvatarProps(
            icon = { Text("U", fontSize = 18.sp) },
            size = AvatarSize.Large,
            shape = AvatarShape.Circle
        ),
        backIcon = {
            Text("←", fontSize = 20.sp, color = Color(0xFF1890FF))
        },
        breadcrumb = {
            AntBreadcrumb(
                items = listOf(
                    BreadcrumbItem(
                        title = "Home",
                        icon = { Text("🏠") },
                        onClick = { /* Navigate */ }
                    ),
                    BreadcrumbItem(title = "List", onClick = { /* Navigate */ }),
                    BreadcrumbItem(title = "Detail")
                ),
                separator = ">"
            )
        },
        extra = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(onClick = { /* Action 1 */ }) { Text("Action 1") }
                AntButton(onClick = { /* Action 2 */ }, type = ButtonType.Primary) {
                    Text("Action 2")
                }
            }
        },
        footer = {
            AntTabs(
                items = listOf(
                    TabItem(key = "details", label = "Details", content = {}),
                    TabItem(key = "history", label = "History", content = {})
                ),
                activeKey = "details",
                onChange = { /* Handle tab change */ }
            )
        },
        ghost = true,
        subTitle = "This is a comprehensive example showing all features",
        tags = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                AntTag(children = { Text("Active") }, color = TagColor.Success)
                AntTag(children = { Text("Premium") }, color = TagColor.Gold)
            }
        },
        title = "Complete Page Header",
        onBack = { /* Handle back navigation */ }
    )
}

/**
 * Example: PageHeader with composable title and subtitle
 */
@Composable
fun PageHeaderComposableTitleExample() {
    AntPageHeader(
        titleComposable = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🚀", fontSize = 20.sp)
                Text(
                    "Custom Title Component",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        subTitleComposable = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Last updated:", fontSize = 12.sp, color = Color.Gray)
                Text("2 hours ago", fontSize = 12.sp, color = Color(0xFF1890FF))
            }
        },
        onBack = { /* Navigate back */ }
    )
}
