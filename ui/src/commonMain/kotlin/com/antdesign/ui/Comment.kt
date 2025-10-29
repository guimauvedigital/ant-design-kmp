package com.antdesign.ui

import androidx.compose.foundation.background
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
 * Comment component displays user comments with avatar, author, content, and actions.
 * Supports nested comments for threaded discussions.
 *
 * Matches React Ant Design Comment API:
 * - actions: List of action items (like, reply, delete)
 * - author: The comment author (composable for flexibility)
 * - avatar: The comment avatar (composable for flexibility)
 * - content: The main comment content (composable for flexibility)
 * - datetime: Time information (composable for flexibility)
 * - children: Nested comments (for threading)
 *
 * @param actions List of action items rendered below the comment content
 * @param author The element to display as the comment author
 * @param avatar The element to display as the comment avatar (defaults to initial-based avatar)
 * @param content The main content of the comment (required)
 * @param datetime A datetime element containing the time to be displayed
 * @param className CSS class name for custom styling
 * @param style Additional Compose Modifier for styling
 * @param modifier Root modifier for the component
 * @param children Nested comments should be provided as children of the Comment
 */
@Composable
fun AntComment(
    actions: List<@Composable () -> Unit> = emptyList(),
    author: (@Composable () -> Unit)? = null,
    avatar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    datetime: (@Composable () -> Unit)? = null,
    className: String? = null,
    style: Modifier = Modifier,
    modifier: Modifier = Modifier,
    children: (@Composable () -> Unit)? = null
) {
    // Get theme tokens for consistent styling
    val theme = useTheme()
    val tokens = theme.token

    Column(
        modifier = modifier
            .then(style)
            .fillMaxWidth()
            .padding(vertical = tokens.marginSM)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(tokens.marginSM)
        ) {
            // Avatar section
            Box {
                if (avatar != null) {
                    avatar()
                } else {
                    // Default avatar fallback
                    AntAvatar(
                        size = AvatarSize.Default
                    )
                }
            }

            // Content section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(tokens.marginXS)
            ) {
                // Header: author + datetime
                if (author != null || datetime != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(tokens.marginXS),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Author
                        if (author != null) {
                            Box {
                                author()
                            }
                        }

                        // Datetime
                        if (datetime != null) {
                            Box {
                                datetime()
                            }
                        }
                    }
                }

                // Main content
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    content()
                }

                // Actions (like, reply, delete, etc.)
                if (actions.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(tokens.margin),
                        modifier = Modifier.padding(top = tokens.marginXS)
                    ) {
                        actions.forEach { action ->
                            Box {
                                action()
                            }
                        }
                    }
                }
            }
        }

        // Nested comments (children)
        if (children != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 44.dp, // Avatar width (32dp) + spacing (12dp)
                        top = tokens.marginSM
                    )
            ) {
                children()
            }
        }
    }
}
