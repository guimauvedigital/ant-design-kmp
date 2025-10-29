package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story

// ============================================
// TYPOGRAPHY COMPONENTS
// ============================================

val TitleComplete by story {
    val text by parameter("Ant Design Title")
    val level by parameter(listOf(1, 2, 3, 4, 5, 6))
    val color by parameter(Color.Black)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Title (controlled):")
        AntTitle(
            text = text,
            level = level,
            color = color
        )

        Text("All Title Levels:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTitle(text = "h1. Ant Design", level = 1)
            AntTitle(text = "h2. Ant Design", level = 2)
            AntTitle(text = "h3. Ant Design", level = 3)
            AntTitle(text = "h4. Ant Design", level = 4)
            AntTitle(text = "h5. Ant Design", level = 5)
            AntTitle(text = "h6. Ant Design", level = 6)
        }

        Text("Titles with Custom Colors:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTitle(text = "Blue Title", level = 2, color = Color(0xFF1890FF))
                AntTitle(text = "Green Title", level = 2, color = Color(0xFF52C41A))
                AntTitle(text = "Red Title", level = 2, color = Color(0xFFFF4D4F))
                AntTitle(text = "Purple Title", level = 2, color = Color(0xFF722ED1))
                AntTitle(text = "Orange Title", level = 2, color = Color(0xFFFAAD14))
            }
        }

        Text("Different Levels with Colors:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTitle(text = "Level 1 - Primary", level = 1, color = Color(0xFF1890FF))
            AntTitle(text = "Level 2 - Success", level = 2, color = Color(0xFF52C41A))
            AntTitle(text = "Level 3 - Warning", level = 3, color = Color(0xFFFAAD14))
            AntTitle(text = "Level 4 - Error", level = 4, color = Color(0xFFFF4D4F))
            AntTitle(text = "Level 5 - Purple", level = 5, color = Color(0xFF722ED1))
            AntTitle(text = "Level 6 - Default", level = 6, color = Color.Black)
        }

        Text("Titles in Different Contexts:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp)
            ) {
                AntTitle(text = "Card Title", level = 3)
                Text("Card content with title above")
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFE6F7FF))
                    .padding(16.dp)
            ) {
                AntTitle(text = "Section", level = 4, color = Color(0xFF1890FF))
                Text("Colored section title")
            }
        }
    }
}

val TextComplete by story {
    val text by parameter("Ant Design Text")
    val type by parameter(listOf("default", "secondary", "success", "warning", "danger"))
    val size by parameter(listOf("small", "default", "large"))
    val strong by parameter(false)
    val italic by parameter(false)
    val underline by parameter(false)
    val delete by parameter(false)
    val code by parameter(false)
    val mark by parameter(false)
    val copyable by parameter(false)

    val textType = when (type) {
        "default" -> TextType.Default
        "secondary" -> TextType.Secondary
        "success" -> TextType.Success
        "warning" -> TextType.Warning
        "danger" -> TextType.Danger
        else -> TextType.Default
    }

    val textSize = when (size) {
        "small" -> TextSize.Small
        "default" -> TextSize.Default
        "large" -> TextSize.Large
        else -> TextSize.Default
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Text (controlled):")
        AntText(
            text = text,
            type = textType,
            size = textSize,
            strong = strong,
            italic = italic,
            underline = underline,
            delete = delete,
            code = code,
            mark = mark,
            copyable = copyable
        )

        Text("Text Types:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "Default Text", type = TextType.Default)
            AntText(text = "Secondary Text", type = TextType.Secondary)
            AntText(text = "Success Text", type = TextType.Success)
            AntText(text = "Warning Text", type = TextType.Warning)
            AntText(text = "Danger Text", type = TextType.Danger)
        }

        Text("Text Sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntText(text = "Small Text", size = TextSize.Small)
            AntText(text = "Default Text", size = TextSize.Default)
            AntText(text = "Large Text", size = TextSize.Large)
        }

        Text("Text Styles:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "Strong Text", strong = true)
            AntText(text = "Italic Text", italic = true)
            AntText(text = "Underlined Text", underline = true)
            AntText(text = "Deleted Text", delete = true)
            AntText(text = "Code Text", code = true)
            AntText(text = "Marked Text", mark = true)
        }

        Text("Combined Styles:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "Strong + Italic", strong = true, italic = true)
            AntText(text = "Underline + Strong", underline = true, strong = true)
            AntText(text = "Code + Strong", code = true, strong = true)
            AntText(text = "Mark + Italic", mark = true, italic = true)
            AntText(text = "Delete + Underline", delete = true, underline = true)
        }

        Text("Types with Styles:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "Success + Strong", type = TextType.Success, strong = true)
            AntText(text = "Warning + Code", type = TextType.Warning, code = true)
            AntText(text = "Danger + Strong", type = TextType.Danger, strong = true)
            AntText(text = "Secondary + Italic", type = TextType.Secondary, italic = true)
        }

        Text("Copyable Text:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "This text is copyable", copyable = true)
            AntText(text = "Copyable + Strong", copyable = true, strong = true)
            AntText(text = "Copyable Code", copyable = true, code = true)
        }

        Text("Complex Combinations:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(
                text = "Large + Strong + Success",
                size = TextSize.Large,
                strong = true,
                type = TextType.Success
            )
            AntText(
                text = "Small + Italic + Code + Warning",
                size = TextSize.Small,
                italic = true,
                code = true,
                type = TextType.Warning
            )
            AntText(
                text = "Mark + Underline + Strong + Danger",
                mark = true,
                underline = true,
                strong = true,
                type = TextType.Danger
            )
        }

        Text("Custom Colors:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntText(text = "Blue Text", color = Color(0xFF1890FF))
            AntText(text = "Purple Text", color = Color(0xFF722ED1))
            AntText(text = "Cyan Text", color = Color(0xFF13C2C2))
            AntText(text = "Magenta Text", color = Color(0xFFEB2F96))
        }

        Text("All Styles Showcase:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                AntText(text = "Normal", size = TextSize.Small)
                AntText(text = "Strong", strong = true, size = TextSize.Small)
                AntText(text = "Italic", italic = true, size = TextSize.Small)
                AntText(text = "Underline", underline = true, size = TextSize.Small)
                AntText(text = "Delete", delete = true, size = TextSize.Small)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                AntText(text = "Code", code = true, size = TextSize.Small)
                AntText(text = "Mark", mark = true, size = TextSize.Small)
                AntText(text = "Success", type = TextType.Success, size = TextSize.Small)
                AntText(text = "Warning", type = TextType.Warning, size = TextSize.Small)
                AntText(text = "Danger", type = TextType.Danger, size = TextSize.Small)
            }
        }
    }
}

val ParagraphComplete by story {
    val text by parameter("Ant Design, a design language for background applications, is refined by Ant UED Team. This is a demonstration of the paragraph component with comprehensive text content to showcase how it handles longer text blocks and multiple lines.")
    val color by parameter(Color.Black)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Paragraph (controlled):")
        AntParagraph(
            text = text,
            color = color
        )

        Text("Multiple Paragraphs:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntParagraph(
                text = "First paragraph: This is the opening paragraph that introduces the topic. It provides context and sets the stage for the information that follows."
            )
            AntParagraph(
                text = "Second paragraph: This paragraph expands on the ideas introduced in the first paragraph. It provides more detailed information and examples."
            )
            AntParagraph(
                text = "Third paragraph: This concluding paragraph summarizes the key points and provides final thoughts on the topic."
            )
        }

        Text("Paragraphs with Colors:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntParagraph(
                text = "Default black paragraph with standard text color for regular content.",
                color = Color.Black
            )
            AntParagraph(
                text = "Blue paragraph for informational content or primary messaging.",
                color = Color(0xFF1890FF)
            )
            AntParagraph(
                text = "Green paragraph for success messages or positive feedback.",
                color = Color(0xFF52C41A)
            )
            AntParagraph(
                text = "Orange paragraph for warnings or important notices.",
                color = Color(0xFFFAAD14)
            )
            AntParagraph(
                text = "Red paragraph for errors, alerts, or critical information.",
                color = Color(0xFFFF4D4F)
            )
            AntParagraph(
                text = "Gray paragraph for secondary or less important information.",
                color = Color.Gray
            )
        }

        Text("Paragraphs in Containers:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp)
            ) {
                AntTitle(text = "Article Title", level = 4)
                AntParagraph(
                    text = "This is a paragraph within a styled container. The container provides visual separation and context for the content."
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFE6F7FF))
                    .padding(16.dp)
            ) {
                AntTitle(text = "Info Box", level = 4, color = Color(0xFF1890FF))
                AntParagraph(
                    text = "Information paragraph in a blue-tinted container for emphasis.",
                    color = Color(0xFF1890FF)
                )
            }
        }

        Text("Long Form Content:")
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AntTitle(text = "Understanding Typography", level = 3)
            AntParagraph(
                text = "Typography is the art and technique of arranging type to make written language legible, readable, and appealing when displayed. The arrangement of type involves selecting typefaces, point sizes, line lengths, line-spacing, and letter-spacing."
            )
            AntTitle(text = "Importance in Design", level = 4)
            AntParagraph(
                text = "Good typography establishes a visual hierarchy that guides the reader's eye through the content. It creates a mood and communicates the brand's personality. Typography affects readability and user experience significantly."
            )
            AntParagraph(
                text = "In digital design, typography must work across different devices and screen sizes while maintaining legibility and visual harmony. The choice of typeface, size, weight, and spacing all contribute to the overall effectiveness of the design."
            )
        }

        Text("Paragraph Widths:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntParagraph(
                text = "Narrow paragraph: Short width for sidebar content or constrained spaces.",
                modifier = Modifier.width(250.dp)
            )
            AntParagraph(
                text = "Medium paragraph: Comfortable reading width suitable for most content. This width provides a good balance between line length and readability.",
                modifier = Modifier.width(400.dp)
            )
            AntParagraph(
                text = "Wide paragraph: Extended width for full-width layouts or detailed content. This format works well when you need to display more information in a single line while maintaining readability. However, extremely long lines can become difficult to read."
            )
        }
    }
}

val LinkComplete by story {
    val text by parameter("Click here")
    val disabled by parameter(false)
    val underline by parameter(true)
    val color by parameter(Color(0xFF1890FF))

    var clickCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Link (controlled) - Click count: $clickCount")
        AntLink(
            text = text,
            onClick = { clickCount++ },
            disabled = disabled,
            underline = underline,
            color = color
        )

        Text("Link States:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntLink(
                text = "Normal Link",
                onClick = { clickCount++ }
            )
            AntLink(
                text = "Disabled Link",
                onClick = { clickCount++ },
                disabled = true
            )
            AntLink(
                text = "No Underline",
                onClick = { clickCount++ },
                underline = false
            )
        }

        Text("Link Colors:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntLink(
                text = "Primary Blue Link",
                onClick = { clickCount++ },
                color = Color(0xFF1890FF)
            )
            AntLink(
                text = "Success Green Link",
                onClick = { clickCount++ },
                color = Color(0xFF52C41A)
            )
            AntLink(
                text = "Warning Orange Link",
                onClick = { clickCount++ },
                color = Color(0xFFFAAD14)
            )
            AntLink(
                text = "Danger Red Link",
                onClick = { clickCount++ },
                color = Color(0xFFFF4D4F)
            )
            AntLink(
                text = "Purple Link",
                onClick = { clickCount++ },
                color = Color(0xFF722ED1)
            )
        }

        Text("Links in Text:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                Text("Visit our ")
                AntLink(text = "documentation", onClick = { clickCount++ })
                Text(" for more info.")
            }
            Row {
                Text("Read the ")
                AntLink(text = "user guide", onClick = { clickCount++ }, underline = false)
                Text(" to get started.")
            }
            Row {
                AntLink(text = "Learn more", onClick = { clickCount++ })
                Text(" about our features and capabilities.")
            }
        }

        Text("Styled Links:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AntLink(
                text = "Link without underline",
                onClick = { clickCount++ },
                underline = false,
                color = Color(0xFF1890FF)
            )
            AntLink(
                text = "Link with custom color",
                onClick = { clickCount++ },
                color = Color(0xFF722ED1)
            )
            AntLink(
                text = "Disabled link (gray)",
                onClick = { clickCount++ },
                disabled = true
            )
        }

        Text("Links as Navigation:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AntLink(text = "Home", onClick = { clickCount++ })
            AntLink(text = "Products", onClick = { clickCount++ })
            AntLink(text = "About", onClick = { clickCount++ })
            AntLink(text = "Contact", onClick = { clickCount++ })
        }

        Text("Links with Icons:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("🔗")
                AntLink(text = "External Link", onClick = { clickCount++ })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("📄")
                AntLink(text = "Documentation", onClick = { clickCount++ })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("📧")
                AntLink(text = "Contact Us", onClick = { clickCount++ }, color = Color(0xFF52C41A))
            }
        }

        Text("Footer Links:")
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Company", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                AntLink(text = "About Us", onClick = { clickCount++ }, color = Color.Gray)
                AntLink(text = "Careers", onClick = { clickCount++ }, color = Color.Gray)
                AntLink(text = "Blog", onClick = { clickCount++ }, color = Color.Gray)
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Resources", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                AntLink(text = "Documentation", onClick = { clickCount++ }, color = Color.Gray)
                AntLink(text = "API Reference", onClick = { clickCount++ }, color = Color.Gray)
                AntLink(text = "Support", onClick = { clickCount++ }, color = Color.Gray)
            }
        }

        Text("All Link Variations:")
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntLink(text = "Standard link", onClick = { clickCount++ })
            AntLink(text = "Link no underline", onClick = { clickCount++ }, underline = false)
            AntLink(text = "Custom color link", onClick = { clickCount++ }, color = Color(0xFFEB2F96))
            AntLink(text = "Disabled state", onClick = { clickCount++ }, disabled = true)
            AntLink(text = "Success colored", onClick = { clickCount++ }, color = Color(0xFF52C41A))
        }
    }
}

// ============================================
// SPLITTER COMPONENT
// ============================================

val SplitterComplete by story {
    val layout by parameter(listOf("horizontal", "vertical"))
    val initialSize by parameter(200)
    val minSize by parameter(50)
    val maxSize by parameter(500)
    val resizable by parameter(true)

    val splitterLayout = when (layout) {
        "horizontal" -> SplitterLayout.Horizontal
        "vertical" -> SplitterLayout.Vertical
        else -> SplitterLayout.Horizontal
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Splitter (controlled) - ${layout.uppercase()}:")
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AntSplitter(
                layout = splitterLayout,
                initialSize = initialSize.dp,
                minSize = minSize.dp,
                maxSize = maxSize.dp,
                resizable = resizable,
                onResize = null,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFE6F7FF))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        AntTitle(text = "Panel 1", level = 4, color = Color(0xFF1890FF))
                        AntText(text = "Left/Top panel content")
                        if (resizable) {
                            AntText(
                                text = "Drag the divider to resize",
                                type = TextType.Secondary,
                                size = TextSize.Small
                            )
                        }
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFF7E6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        AntTitle(text = "Panel 2", level = 4, color = Color(0xFFFAAD14))
                        AntText(text = "Right/Bottom panel content")
                    }
                }
            )
        }

        Text("Horizontal Splitter - Resizable:")
        Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 250.dp,
                minSize = 100.dp,
                maxSize = 400.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF0F5FF))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Left Panel")
                        Text("Resizable horizontally", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFFBE6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Right Panel")
                        Text("Adjusts automatically", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            )
        }

        Text("Vertical Splitter - Resizable:")
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Vertical,
                initialSize = 150.dp,
                minSize = 80.dp,
                maxSize = 220.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFE6FFFB))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Top Panel")
                        Text("Drag divider down/up", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFFF1F0))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Bottom Panel")
                        Text("Flexible height", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            )
        }

        Text("Non-Resizable Horizontal:")
        Box(modifier = Modifier.height(150.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 200.dp,
                minSize = 50.dp,
                maxSize = 400.dp,
                resizable = false,
                panel1 = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFE6F7))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Left\n(200dp)")
                    }
                },
                panel2 = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF6FFED))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Right\nCannot resize")
                    }
                }
            )
        }

        Text("Non-Resizable Vertical:")
        Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Vertical,
                initialSize = 120.dp,
                minSize = 50.dp,
                maxSize = 200.dp,
                resizable = false,
                panel1 = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF0F6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Top (120dp)")
                    }
                },
                panel2 = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFBE6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Fixed Bottom\nStatic layout")
                    }
                }
            )
        }

        Text("Different Initial Sizes - Horizontal:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.weight(1f).height(150.dp)) {
                AntSplitter(
                    layout = SplitterLayout.Horizontal,
                    initialSize = 100.dp,
                    minSize = 50.dp,
                    maxSize = 200.dp,
                    resizable = true,
                    panel1 = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE6F7FF))
                                .padding(8.dp)
                                .fillMaxSize()
                        ) {
                            Text("100dp", fontSize = 12.sp)
                        }
                    },
                    panel2 = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF7E6))
                                .padding(8.dp)
                                .fillMaxSize()
                        ) {
                            Text("Flex", fontSize = 12.sp)
                        }
                    }
                )
            }
            Box(modifier = Modifier.weight(1f).height(150.dp)) {
                AntSplitter(
                    layout = SplitterLayout.Horizontal,
                    initialSize = 180.dp,
                    minSize = 80.dp,
                    maxSize = 250.dp,
                    resizable = true,
                    panel1 = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE6F7FF))
                                .padding(8.dp)
                                .fillMaxSize()
                        ) {
                            Text("180dp", fontSize = 12.sp)
                        }
                    },
                    panel2 = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF7E6))
                                .padding(8.dp)
                                .fillMaxSize()
                        ) {
                            Text("Flex", fontSize = 12.sp)
                        }
                    }
                )
            }
        }

        Text("Min/Max Constraints:")
        Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 150.dp,
                minSize = 100.dp,
                maxSize = 300.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF0F5FF))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Constrained Panel", fontWeight = FontWeight.Bold)
                        Text("Min: 100dp", fontSize = 12.sp)
                        Text("Max: 300dp", fontSize = 12.sp)
                        Text("Initial: 150dp", fontSize = 12.sp)
                    }
                },
                panel2 = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFBE6))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Flexible Panel\nAdjusts to available space")
                    }
                }
            )
        }

        Text("Nested Content:")
        Box(modifier = Modifier.height(350.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 280.dp,
                minSize = 200.dp,
                maxSize = 400.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFE6F7FF))
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AntTitle(text = "Navigation", level = 4)
                        AntLink(text = "Dashboard", onClick = {})
                        AntLink(text = "Projects", onClick = {})
                        AntLink(text = "Tasks", onClick = {})
                        AntLink(text = "Settings", onClick = {})
                        Spacer(modifier = Modifier.weight(1f))
                        AntText(text = "Version 1.0.0", type = TextType.Secondary, size = TextSize.Small)
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AntTitle(text = "Main Content Area", level = 3)
                        AntParagraph(
                            text = "This is the main content area that displays the primary information. The splitter allows users to adjust the navigation panel width according to their preference."
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AntButton(
                                onClick = {},
                                type = ButtonType.Primary
                            ) {
                                Text("Action")
                            }
                            AntButton(
                                onClick = {},
                                type = ButtonType.Default
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            )
        }

        Text("Sidebar Layout:")
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Horizontal,
                initialSize = 220.dp,
                minSize = 180.dp,
                maxSize = 350.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5))
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AntTitle(text = "Sidebar", level = 5)
                        AntDivider(type = DividerType.Solid)
                        repeat(5) { index ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index == 0) Color(0xFFE6F7FF) else Color.Transparent)
                                    .padding(8.dp)
                            ) {
                                Text("Item ${index + 1}", fontSize = 14.sp)
                            }
                        }
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        AntTitle(text = "Content", level = 4)
                        AntParagraph(
                            text = "Main content area with flexible width. Resize the sidebar by dragging the divider."
                        )
                    }
                }
            )
        }

        Text("Code Editor Layout:")
        Box(modifier = Modifier.height(280.dp).fillMaxWidth()) {
            AntSplitter(
                layout = SplitterLayout.Vertical,
                initialSize = 180.dp,
                minSize = 120.dp,
                maxSize = 220.dp,
                resizable = true,
                panel1 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF1E1E1E))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("// Code Editor", color = Color(0xFF4EC9B0), fontSize = 12.sp)
                        Text("function hello() {", color = Color(0xFFDCDCAA), fontSize = 12.sp)
                        Text("  console.log('Hello');", color = Color(0xFFCE9178), fontSize = 12.sp)
                        Text("}", color = Color(0xFFDCDCAA), fontSize = 12.sp)
                    }
                },
                panel2 = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF252526))
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Terminal Output", color = Color(0xFF4FC1FF), fontSize = 12.sp)
                        Text("> npm run dev", color = Color.White, fontSize = 11.sp)
                        Text("Server running...", color = Color(0xFF89D185), fontSize = 11.sp)
                    }
                }
            )
        }
    }
}
