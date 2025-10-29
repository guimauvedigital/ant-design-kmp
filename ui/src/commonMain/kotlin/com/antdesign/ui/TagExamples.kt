package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Comprehensive examples demonstrating all Tag features
 * 100% parity with React Ant Design v5.x
 */

@Composable
fun TagExamplesBasic() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Basic Tag")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(text = "Tag 1")
            AntTag(text = "Tag 2")
            AntTag(text = "Tag 3")
        }

        Text("Preset Colors")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(text = "Success", color = TagColor.Success)
            AntTag(text = "Processing", color = TagColor.Processing)
            AntTag(text = "Error", color = TagColor.Error)
            AntTag(text = "Warning", color = TagColor.Warning)
            AntTag(text = "Default", color = TagColor.Default)
        }

        Text("Extended Colors")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTag(text = "Magenta", color = TagColor.Magenta)
                AntTag(text = "Red", color = TagColor.Red)
                AntTag(text = "Volcano", color = TagColor.Volcano)
                AntTag(text = "Orange", color = TagColor.Orange)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTag(text = "Gold", color = TagColor.Gold)
                AntTag(text = "Lime", color = TagColor.Lime)
                AntTag(text = "Green", color = TagColor.Green)
                AntTag(text = "Cyan", color = TagColor.Cyan)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntTag(text = "Blue", color = TagColor.Blue)
                AntTag(text = "GeekBlue", color = TagColor.GeekBlue)
                AntTag(text = "Purple", color = TagColor.Purple)
            }
        }
    }
}

@Composable
fun TagExamplesClosable() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Closable Tags")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Tag 1",
                closable = true,
                onClose = { println("Tag 1 closed") }
            )
            AntTag(
                text = "Tag 2",
                color = TagColor.Success,
                closable = true,
                onClose = { println("Tag 2 closed") }
            )
            AntTag(
                text = "Tag 3",
                color = TagColor.Error,
                closable = true,
                onClose = { println("Tag 3 closed") }
            )
        }

        Text("Closable with Custom Close Icon")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Custom Close",
                closable = true,
                closeIcon = {
                    Text("X", color = Color.Red)
                },
                onClose = { println("Custom close clicked") }
            )
        }
    }
}

@Composable
fun TagExamplesBordered() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Bordered Tags (Default)")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(text = "Success", color = TagColor.Success, bordered = true)
            AntTag(text = "Processing", color = TagColor.Processing, bordered = true)
            AntTag(text = "Error", color = TagColor.Error, bordered = true)
        }

        Text("Borderless Tags")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(text = "Success", color = TagColor.Success, bordered = false)
            AntTag(text = "Processing", color = TagColor.Processing, bordered = false)
            AntTag(text = "Error", color = TagColor.Error, bordered = false)
        }
    }
}

@Composable
fun TagExamplesWithIcon() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tags with Icon")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Success",
                color = TagColor.Success,
                icon = {
                    Text("✓")
                }
            )
            AntTag(
                text = "Error",
                color = TagColor.Error,
                icon = {
                    Text("✗")
                }
            )
            AntTag(
                text = "Warning",
                color = TagColor.Warning,
                icon = {
                    Text("⚠")
                }
            )
        }
    }
}

@Composable
fun TagExamplesCustomColor() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Custom Color Tags")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Custom Pink",
                color = TagColor.Custom(Color(0xFFFF1493))
            )
            AntTag(
                text = "Custom Teal",
                color = TagColor.Custom(Color(0xFF008080))
            )
            AntTag(
                text = "Custom Brown",
                color = TagColor.Custom(Color(0xFFA0522D))
            )
        }
    }
}

@Composable
fun TagExamplesCheckable() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }
    var checked3 by remember { mutableStateOf(false) }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Checkable Tags")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntCheckableTag(
                text = "Tag 1",
                checked = checked1,
                onChange = { checked1 = it }
            )
            AntCheckableTag(
                text = "Tag 2",
                checked = checked2,
                onChange = { checked2 = it }
            )
            AntCheckableTag(
                text = "Tag 3",
                checked = checked3,
                onChange = { checked3 = it }
            )
        }

        Text("Checkable Tags - Multiple Selection")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Movies", "Books", "Music", "Sports").forEach { tag ->
                AntCheckableTag(
                    text = tag,
                    checked = selectedTags.contains(tag),
                    onChange = { checked ->
                        selectedTags = if (checked) {
                            selectedTags + tag
                        } else {
                            selectedTags - tag
                        }
                    }
                )
            }
        }
        Text("Selected: ${selectedTags.joinToString(", ")}")
    }
}

@Composable
fun TagExamplesSemanticStyles() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tags with Semantic Styles")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Styled Tag",
                color = TagColor.Processing,
                styles = TagStyles(
                    root = Modifier.padding(4.dp)
                )
            )
            AntTag(
                text = "With Icon Style",
                color = TagColor.Success,
                icon = { Text("★") },
                styles = TagStyles(
                    icon = Modifier.size(16.dp)
                )
            )
        }
    }
}

@Composable
fun TagExamplesComposableContent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tags with Composable Content")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntTag(
                text = "Complex Content",
                color = TagColor.Blue,
                closable = true,
                icon = { Text("🎉") }
            )

            AntTag(
                text = "Multi-Line",
                color = TagColor.Purple,
                icon = { Text("⭐") }
            )
        }
    }
}

@Composable
fun TagExamplesAll() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TagExamplesBasic()
        TagExamplesClosable()
        TagExamplesBordered()
        TagExamplesWithIcon()
        TagExamplesCustomColor()
        TagExamplesCheckable()
        TagExamplesSemanticStyles()
        TagExamplesComposableContent()
    }
}
