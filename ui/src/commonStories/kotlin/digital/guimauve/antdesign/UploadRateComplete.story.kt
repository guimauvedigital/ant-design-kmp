package digital.guimauve.antdesign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// ==================== UPLOAD COMPLETE ====================

@OptIn(ExperimentalTime::class)
val UploadComplete by story {
    // File list state
    var fileList by remember {
        mutableStateOf(
            listOf(
                UploadFile(
                    uid = "1",
                    name = "example.pdf",
                    status = UploadFileStatus.Done,
                    url = "https://example.com/example.pdf"
                )
            )
        )
    }

    // Parameters for all Upload props
    val action by parameter("https://api.example.com/upload")
    val method by parameter(listOf("POST", "PUT", "PATCH"))
    val directory by parameter(false)
    val name by parameter("file")
    val multiple by parameter(false)
    val accept by parameter(".jpg,.png,.pdf")
    val disabled by parameter(false)
    val listType by parameter(listOf("Text", "Picture", "PictureCard", "PictureCircle"))
    val maxCount by parameter(5)
    val openFileDialogOnClick by parameter(true)
    val withCredentials by parameter(false)
    val showUploadList by parameter(true)
    val showPreviewIcon by parameter(true)
    val showRemoveIcon by parameter(true)
    val showDownloadIcon by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Upload Component - ALL PARAMETERS",
            style = MaterialTheme.typography.headlineSmall
        )

        // Display configuration
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Configuration:", style = MaterialTheme.typography.titleSmall)
            Text("• Action: $action", fontSize = 12.sp)
            Text("• Method: $method", fontSize = 12.sp)
            Text("• Directory: $directory", fontSize = 12.sp)
            Text("• Name: $name", fontSize = 12.sp)
            Text("• Multiple: $multiple", fontSize = 12.sp)
            Text("• Accept: $accept", fontSize = 12.sp)
            Text("• Disabled: $disabled", fontSize = 12.sp)
            Text("• List Type: $listType", fontSize = 12.sp)
            Text("• Max Count: $maxCount", fontSize = 12.sp)
            Text("• Open Dialog on Click: $openFileDialogOnClick", fontSize = 12.sp)
            Text("• With Credentials: $withCredentials", fontSize = 12.sp)
            Text("• Show Upload List: $showUploadList", fontSize = 12.sp)
            Text("• Files: ${fileList.size}/$maxCount", fontSize = 12.sp)
        }

        AntDivider()

        // Upload component with ALL parameters
        AntUpload(
            // Core upload parameters
            action = action,
            method = method,
            directory = directory,
            data = mapOf("userId" to "123", "timestamp" to Clock.System.now().toEpochMilliseconds()),
            headers = mapOf(
                "Authorization" to "Bearer token123",
                "X-Custom-Header" to "custom-value"
            ),
            name = name,
            multiple = multiple,
            accept = accept,

            // Upload behavior
            beforeUpload = { file ->
                println("Before upload: ${file.name}, size: ${file.size}")
                val isValidSize = file.size < 5 * 1024 * 1024 // 5MB
                if (!isValidSize) {
                    println("File size must be less than 5MB")
                }
                isValidSize
            },
            customRequest = { option ->
                println("Custom request for: ${option.file.name}")
                // Simulate upload progress
                option.onProgress(25, option.file)
                option.onProgress(50, option.file)
                option.onProgress(75, option.file)
                option.onProgress(100, option.file)
                // Simulate success
                option.onSuccess(
                    mapOf("url" to "https://cdn.example.com/${option.file.name}"),
                    option.file
                )
            },
            disabled = disabled,

            // File list control
            fileList = fileList,
            defaultFileList = emptyList(),

            // Display options
            listType = when (listType) {
                "Picture" -> UploadListType.Picture
                "PictureCard" -> UploadListType.PictureCard
                "PictureCircle" -> UploadListType.PictureCircle
                else -> UploadListType.Text
            },
            maxCount = maxCount,

            // Callbacks
            onChange = { param ->
                println("Upload changed: ${param.file.name} - ${param.file.status}")
                fileList = param.fileList
            },
            onDrop = { event ->
                println("Files dropped")
            },
            onDownload = { file ->
                println("Download: ${file.name}")
            },
            onPreview = { file ->
                println("Preview: ${file.name}")
            },
            onRemove = { file ->
                println("Remove: ${file.name}")
                true // Return false to prevent removal
            },

            // Advanced options
            openFileDialogOnClick = openFileDialogOnClick,
            previewFile = { file ->
                file.url ?: "https://example.com/preview/${file.name}"
            },
            progress = UploadProgress(
                strokeColor = Color(0xFF52C41A),
                strokeWidth = 3,
                format = { percent -> "$percent% uploaded" },
                showInfo = true
            ),
            showUploadList = if (showUploadList) {
                ShowUploadListInterface(
                    showPreviewIcon = showPreviewIcon,
                    showRemoveIcon = showRemoveIcon,
                    showDownloadIcon = showDownloadIcon,
                    previewIcon = { file ->
                        Text("👁", fontSize = 16.sp)
                    },
                    removeIcon = { file ->
                        Text("🗑", fontSize = 16.sp)
                    },
                    downloadIcon = { file ->
                        Text("💾", fontSize = 16.sp)
                    }
                )
            } else {
                false
            },
            withCredentials = withCredentials,

            // Custom renderers
            itemRender = { props ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📎 ${props.file.name}", fontSize = 14.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AntButton(
                            size = ButtonSize.Small,
                            onClick = { props.actions.preview() }
                        ) {
                            Text("View")
                        }
                        AntButton(
                            size = ButtonSize.Small,
                            danger = true,
                            onClick = { props.actions.remove() }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            },
            isImageUrl = { file ->
                file.type.startsWith("image/") ||
                        file.name.endsWith(".jpg") ||
                        file.name.endsWith(".png") ||
                        file.name.endsWith(".gif")
            },
            iconRender = { file ->
                when {
                    file.type.startsWith("image/") -> Text("🖼", fontSize = 20.sp)
                    file.type == "application/pdf" -> Text("📄", fontSize = 20.sp)
                    else -> Text("📎", fontSize = 20.sp)
                }
            },

            // Styling
            className = "custom-upload-class",
            style = Modifier.padding(4.dp),
            children = {
                // Custom upload trigger
                AntButton(
                    onClick = { /* Upload trigger handled by AntUpload component */ },
                    type = ButtonType.Primary,
                    disabled = disabled || fileList.size >= maxCount
                ) {
                    Text("Click to Upload (${fileList.size}/$maxCount)")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Upload list types examples
        AntDivider(
            children = {
                Text("Upload List Types")
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Text List:", fontSize = 14.sp, color = ColorPalette.gray7)
            Text("Picture List:", fontSize = 14.sp, color = ColorPalette.gray7)
            Text("Picture Card:", fontSize = 14.sp, color = ColorPalette.gray7)
            Text("Picture Circle:", fontSize = 14.sp, color = ColorPalette.gray7)
        }
    }
}

// ==================== UPLOAD DRAGGER ====================

val UploadDraggerComplete by story {
    var fileList by remember { mutableStateOf(emptyList<UploadFile>()) }
    val multiple by parameter(true)
    val disabled by parameter(false)
    val maxCount by parameter(3)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Upload Dragger - Drag & Drop", style = MaterialTheme.typography.headlineSmall)

        Text("Dragger Configuration:", style = MaterialTheme.typography.titleSmall)
        Text("• Multiple: $multiple", fontSize = 12.sp)
        Text("• Disabled: $disabled", fontSize = 12.sp)
        Text("• Max Count: $maxCount", fontSize = 12.sp)
        Text("• Files: ${fileList.size}/$maxCount", fontSize = 12.sp)

        AntDragger(
            fileList = fileList,
            multiple = multiple,
            disabled = disabled,
            maxCount = maxCount,
            accept = ".jpg,.jpeg,.png,.pdf,.doc,.docx",
            hint = "Click or drag files to this area to upload",
            description = "Support for single or bulk upload. Strictly prohibit from uploading company data or sensitive files.",
            onChange = { param ->
                println("Dragger changed: ${param.file.name}")
                fileList = param.fileList
            },
            onDrop = { event ->
                println("Files dropped in dragger area")
            },
            beforeUpload = { file ->
                val isValid = file.size < 10 * 1024 * 1024 // 10MB
                if (!isValid) {
                    println("File ${file.name} is too large")
                }
                isValid
            }
        )

        if (fileList.isNotEmpty()) {
            AntDivider()
            Text("Uploaded Files:", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
            fileList.forEach { file ->
                Text("• ${file.name} (${file.status})", fontSize = 12.sp)
            }
        }
    }
}

// ==================== RATE COMPLETE ====================

val RateComplete by story {
    // Rate state
    var rating by remember { mutableStateOf(3.0) }
    var hoverValue by remember { mutableStateOf<Double?>(null) }
    var focusState by remember { mutableStateOf("not focused") }
    var lastKey by remember { mutableStateOf("none") }

    // Parameters for all Rate props
    val count by parameter(5)
    val allowHalf by parameter(true)
    val allowClear by parameter(true)
    val disabled by parameter(false)
    val autoFocus by parameter(false)
    val showTooltips by parameter(true)
    val characterType by parameter(listOf("Star", "Heart", "Letter", "Custom"))

    val tooltips = listOf("terrible", "bad", "normal", "good", "wonderful")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Rate Component - ALL PARAMETERS",
            style = MaterialTheme.typography.headlineSmall
        )

        // Display configuration
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Configuration:", style = MaterialTheme.typography.titleSmall)
            Text("• Value: $rating", fontSize = 12.sp)
            Text("• Count: $count", fontSize = 12.sp)
            Text("• Allow Half: $allowHalf", fontSize = 12.sp)
            Text("• Allow Clear: $allowClear", fontSize = 12.sp)
            Text("• Disabled: $disabled", fontSize = 12.sp)
            Text("• Auto Focus: $autoFocus", fontSize = 12.sp)
            Text("• Show Tooltips: $showTooltips", fontSize = 12.sp)
            Text("• Character Type: $characterType", fontSize = 12.sp)
            Text("• Focus State: $focusState", fontSize = 12.sp)
            Text("• Hover Value: ${hoverValue ?: "none"}", fontSize = 12.sp)
            Text("• Last Key: $lastKey", fontSize = 12.sp)
        }

        AntDivider()

        // Rate component with ALL parameters
        AntRate(
            // Core parameters
            value = rating,
            onChange = { newValue ->
                println("Rating changed to: $newValue")
                rating = newValue
            },
            defaultValue = 0.0,

            // Count and behavior
            count = count,
            allowHalf = allowHalf,
            allowClear = allowClear,
            disabled = disabled,

            // Custom character
            character = when (characterType) {
                "Heart" -> { props ->
                    Text(
                        text = "♥",
                        fontSize = 24.sp,
                        color = if (props.isActive) Color(0xFFEB2F96) else ColorPalette.gray5
                    )
                }

                "Letter" -> { props ->
                    Text(
                        text = "A",
                        fontSize = 24.sp,
                        color = if (props.isActive) ColorPalette.blue6 else ColorPalette.gray5
                    )
                }

                "Custom" -> { props ->
                    Text(
                        text = if (props.isActive) "😍" else "😐",
                        fontSize = 24.sp
                    )
                }

                else -> null // Default star
            },

            // Tooltips
            tooltips = if (showTooltips) tooltips else null,

            // Hover callback
            onHoverChange = { value ->
                println("Hover value: $value")
                hoverValue = value
            },

            // Focus management
            autoFocus = autoFocus,
            tabIndex = 0,
            onFocus = {
                println("Rate focused")
                focusState = "focused"
            },
            onBlur = {
                println("Rate blurred")
                focusState = "blurred"
            },

            // Keyboard events
            onKeyDown = { event ->
                val key = event.key
                println("Key pressed: $key")
                lastKey = key.toString()
            },

            // Styling
            className = "custom-rate-class",
            style = Modifier.padding(4.dp),
            styles = RateStyles(
                root = Modifier.padding(8.dp),
                star = Modifier.size(28.dp)
            ),
            classNames = RateClassNames(
                root = "rate-root",
                star = "rate-star"
            ),
            modifier = Modifier
        )

        // Control buttons
        AntDivider()

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AntButton(
                onClick = { rating = 0.0 },
                type = ButtonType.Default,
                size = ButtonSize.Small
            ) {
                Text("Clear")
            }

            AntButton(
                onClick = { rating = (rating - 0.5).coerceAtLeast(0.0) },
                type = ButtonType.Default,
                size = ButtonSize.Small,
                disabled = rating <= 0.0
            ) {
                Text("-0.5")
            }

            AntButton(
                onClick = { rating = (rating + 0.5).coerceAtMost(count.toDouble()) },
                type = ButtonType.Primary,
                size = ButtonSize.Small,
                disabled = rating >= count.toDouble()
            ) {
                Text("+0.5")
            }

            AntButton(
                onClick = { rating = count.toDouble() },
                type = ButtonType.Default,
                size = ButtonSize.Small
            ) {
                Text("Max")
            }
        }

        // Rating display
        Text(
            text = "Current rating: $rating${
                if (showTooltips && rating > 0) " (${
                    tooltips.getOrNull(
                        (rating.toInt() - 1).coerceIn(
                            0,
                            tooltips.size - 1
                        )
                    )
                })" else ""
            }",
            style = MaterialTheme.typography.titleMedium,
            color = when {
                rating >= 4.5 -> Color(0xFF52C41A)
                rating >= 3.0 -> ColorPalette.blue6
                rating >= 1.5 -> Color(0xFFFAAD14)
                rating > 0.0 -> Color(0xFFFF4D4F)
                else -> ColorPalette.gray6
            }
        )
    }
}

// ==================== RATE VARIATIONS ====================

val RateVariations by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Rate Component Variations", style = MaterialTheme.typography.headlineSmall)

        // Basic
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Basic Rate:", style = MaterialTheme.typography.titleSmall)
            var basicRating by remember { mutableStateOf(0.0) }
            AntRate(
                value = basicRating,
                onChange = { basicRating = it }
            )
            Text("Rating: $basicRating", fontSize = 12.sp, color = ColorPalette.gray7)
        }

        AntDivider()

        // Half stars
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Half Stars:", style = MaterialTheme.typography.titleSmall)
            var halfRating by remember { mutableStateOf(2.5) }
            AntRate(
                value = halfRating,
                onChange = { halfRating = it },
                allowHalf = true
            )
            Text("Rating: $halfRating", fontSize = 12.sp, color = ColorPalette.gray7)
        }

        AntDivider()

        // Read-only
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Read-only (Disabled):", style = MaterialTheme.typography.titleSmall)
            AntRate(
                value = 4.5,
                onChange = null,
                disabled = true,
                allowHalf = true
            )
            Text("This rate is read-only", fontSize = 12.sp, color = ColorPalette.gray7)
        }

        AntDivider()

        // Custom count
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Custom Count (10 stars):", style = MaterialTheme.typography.titleSmall)
            var tenStarRating by remember { mutableStateOf(7.0) }
            AntRate(
                value = tenStarRating,
                onChange = { tenStarRating = it },
                count = 10
            )
            Text("Rating: $tenStarRating / 10", fontSize = 12.sp, color = ColorPalette.gray7)
        }

        AntDivider()

        // Custom characters
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Custom Characters:", style = MaterialTheme.typography.titleSmall)

            var heartRating by remember { mutableStateOf(3.0) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Hearts:", fontSize = 14.sp)
                AntRate(
                    value = heartRating,
                    onChange = { heartRating = it },
                    character = { props ->
                        Text(
                            text = "♥",
                            fontSize = 24.sp,
                            color = if (props.isActive) Color(0xFFEB2F96) else ColorPalette.gray5
                        )
                    }
                )
            }

            var emojiRating by remember { mutableStateOf(2.0) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Emojis:", fontSize = 14.sp)
                AntRate(
                    value = emojiRating,
                    onChange = { emojiRating = it },
                    character = { props ->
                        Text(
                            text = if (props.isActive) "😍" else "😐",
                            fontSize = 24.sp
                        )
                    }
                )
            }
        }

        AntDivider()

        // With tooltips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("With Tooltips (hover):", style = MaterialTheme.typography.titleSmall)
            var tooltipRating by remember { mutableStateOf(3.0) }
            val tooltips = listOf("terrible", "bad", "normal", "good", "wonderful")
            AntRate(
                value = tooltipRating,
                onChange = { tooltipRating = it },
                tooltips = tooltips
            )
            Text(
                "Rating: $tooltipRating - ${tooltips.getOrNull((tooltipRating.toInt() - 1).coerceAtLeast(0)) ?: "none"}",
                fontSize = 12.sp,
                color = ColorPalette.gray7
            )
        }

        AntDivider()

        // Clearable
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Clearable (click same star):", style = MaterialTheme.typography.titleSmall)
            var clearRating by remember { mutableStateOf(3.0) }
            AntRate(
                value = clearRating,
                onChange = { clearRating = it },
                allowClear = true
            )
            Text("Rating: $clearRating (click same star to clear)", fontSize = 12.sp, color = ColorPalette.gray7)
        }
    }
}
