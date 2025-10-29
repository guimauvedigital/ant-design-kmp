package digital.guimauve.antdesign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Examples demonstrating the AntComment component with 100% React parity.
 * Shows basic usage, actions, nested comments, and advanced features.
 */

@Composable
fun CommentBasicExample() {
    AntComment(
        author = {
            Text(
                text = "Han Solo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF000000D9)
            )
        },
        avatar = {
            AntAvatar(
                src = "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
                alt = "Han Solo"
            )
        },
        content = {
            Text(
                text = "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.",
                fontSize = 14.sp,
                color = Color(0xFF00000073),
                lineHeight = 22.sp
            )
        },
        datetime = {
            Text(
                text = "2 hours ago",
                fontSize = 12.sp,
                color = Color(0xFF00000073)
            )
        }
    )
}

@Composable
fun CommentWithActionsExample() {
    var likes by remember { mutableStateOf(0) }
    var dislikes by remember { mutableStateOf(0) }

    AntComment(
        actions = listOf(
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { likes++ }
                ) {
                    Text(
                        text = "👍",
                        fontSize = 12.sp
                    )
                    if (likes > 0) {
                        Text(
                            text = likes.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF00000073)
                        )
                    }
                }
            },
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { dislikes++ }
                ) {
                    Text(
                        text = "👎",
                        fontSize = 12.sp
                    )
                    if (dislikes > 0) {
                        Text(
                            text = dislikes.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF00000073)
                        )
                    }
                }
            },
            {
                Text(
                    text = "Reply to",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073),
                    modifier = Modifier.clickable {
                        println("Reply clicked")
                    }
                )
            }
        ),
        author = {
            Text(
                text = "Han Solo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF000000D9)
            )
        },
        avatar = {
            AntAvatar(
                text = "H",
                size = AvatarSize.Default
            )
        },
        content = {
            Text(
                text = "We supply a series of design principles, practical patterns and high quality design resources.",
                fontSize = 14.sp,
                color = Color(0xFF00000073),
                lineHeight = 22.sp
            )
        },
        datetime = {
            Text(
                text = "2 hours ago",
                fontSize = 12.sp,
                color = Color(0xFF00000073)
            )
        }
    )
}

@Composable
fun CommentNestedExample() {
    AntComment(
        actions = listOf(
            {
                Text(
                    text = "Reply to",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073),
                    modifier = Modifier.clickable {
                        println("Reply clicked")
                    }
                )
            }
        ),
        author = {
            Text(
                text = "Han Solo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF000000D9)
            )
        },
        avatar = {
            AntAvatar(
                text = "H",
                size = AvatarSize.Default
            )
        },
        content = {
            Text(
                text = "This is the main comment with nested replies below.",
                fontSize = 14.sp,
                color = Color(0xFF00000073),
                lineHeight = 22.sp
            )
        },
        datetime = {
            Text(
                text = "3 hours ago",
                fontSize = 12.sp,
                color = Color(0xFF00000073)
            )
        }
    ) {
        // Nested comment 1
        AntComment(
            actions = listOf(
                {
                    Text(
                        text = "Reply to",
                        fontSize = 12.sp,
                        color = Color(0xFF00000073)
                    )
                }
            ),
            author = {
                Text(
                    text = "Luke Skywalker",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF000000D9)
                )
            },
            avatar = {
                AntAvatar(
                    text = "L",
                    size = AvatarSize.Default
                )
            },
            content = {
                Text(
                    text = "This is a nested reply to the main comment.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp
                )
            },
            datetime = {
                Text(
                    text = "2 hours ago",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073)
                )
            }
        )

        // Nested comment 2
        AntComment(
            author = {
                Text(
                    text = "Leia Organa",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF000000D9)
                )
            },
            avatar = {
                AntAvatar(
                    text = "L",
                    size = AvatarSize.Default
                )
            },
            content = {
                Text(
                    text = "Another nested reply in the same thread.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp
                )
            },
            datetime = {
                Text(
                    text = "1 hour ago",
                    fontSize = 12.sp,
                    color = Color(0xFF00000073)
                )
            }
        ) {
            // Deeply nested comment
            AntComment(
                author = {
                    Text(
                        text = "Chewbacca",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF000000D9)
                    )
                },
                avatar = {
                    AntAvatar(
                        text = "C",
                        size = AvatarSize.Default
                    )
                },
                content = {
                    Text(
                        text = "RRRRR-GHGHGHGHGH!",
                        fontSize = 14.sp,
                        color = Color(0xFF00000073),
                        lineHeight = 22.sp
                    )
                },
                datetime = {
                    Text(
                        text = "30 minutes ago",
                        fontSize = 12.sp,
                        color = Color(0xFF00000073)
                    )
                }
            )
        }
    }
}

@Composable
fun CommentListExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Header
        Text(
            text = "Comments (3)",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF000000D9),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Comment list
        AntComment(
            actions = listOf(
                { Text("Reply to", fontSize = 12.sp, color = Color(0xFF00000073)) }
            ),
            author = {
                Text(
                    text = "Han Solo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            avatar = {
                AntAvatar(text = "H")
            },
            content = {
                Text(
                    text = "Great article! Very informative and well-written.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp
                )
            },
            datetime = {
                Text("3 hours ago", fontSize = 12.sp, color = Color(0xFF00000073))
            }
        )

        AntDivider()

        AntComment(
            actions = listOf(
                { Text("Reply to", fontSize = 12.sp, color = Color(0xFF00000073)) }
            ),
            author = {
                Text(
                    text = "Luke Skywalker",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            avatar = {
                AntAvatar(text = "L")
            },
            content = {
                Text(
                    text = "I agree! This helped me understand the concepts much better.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp
                )
            },
            datetime = {
                Text("2 hours ago", fontSize = 12.sp, color = Color(0xFF00000073))
            }
        )

        AntDivider()

        AntComment(
            actions = listOf(
                { Text("Reply to", fontSize = 12.sp, color = Color(0xFF00000073)) }
            ),
            author = {
                Text(
                    text = "Leia Organa",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            avatar = {
                AntAvatar(text = "L")
            },
            content = {
                Text(
                    text = "Thanks for sharing! Looking forward to more content like this.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp
                )
            },
            datetime = {
                Text("1 hour ago", fontSize = 12.sp, color = Color(0xFF00000073))
            }
        )
    }
}

@Composable
fun CommentMinimalExample() {
    // Minimal example with only required props
    AntComment(
        content = {
            Text(
                text = "This is a minimal comment with only content.",
                fontSize = 14.sp,
                color = Color(0xFF00000073)
            )
        }
    )
}

@Composable
fun CommentCustomStylingExample() {
    AntComment(
        actions = listOf(
            { Text("Like", fontSize = 12.sp, color = Color(0xFF1890FF)) },
            { Text("Reply", fontSize = 12.sp, color = Color(0xFF1890FF)) }
        ),
        author = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "VIP User",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFAD14)
                )
                Text(
                    text = " ⭐",
                    fontSize = 14.sp
                )
            }
        },
        avatar = {
            AntAvatar(
                text = "V",
                size = AvatarSize.Large,
                shape = AvatarShape.Square
            )
        },
        content = {
            Column {
                Text(
                    text = "Premium Content Access",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF000000D9)
                )
                Text(
                    text = "This user has access to premium features and exclusive content.",
                    fontSize = 14.sp,
                    color = Color(0xFF00000073),
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        datetime = {
            Text(
                text = "Just now",
                fontSize = 12.sp,
                color = Color(0xFF52C41A),
                fontWeight = FontWeight.Medium
            )
        },
        style = Modifier.padding(16.dp)
    )
}
