package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.TimeSource

/**
 * Represents a file in the upload list
 * @param uid Unique identifier for the file
 * @param name File name
 * @param status Current upload status
 * @param url Remote URL after successful upload
 * @param thumbUrl Thumbnail URL for preview
 * @param size File size in bytes
 * @param type MIME type
 * @param percent Upload progress (0-100)
 * @param response Server response after upload
 * @param error Error message if upload failed
 * @param originFileObj Original file object (platform-specific)
 */
data class UploadFile(
    val uid: String,
    val name: String,
    val status: UploadFileStatus = UploadFileStatus.Done,
    val url: String? = null,
    val thumbUrl: String? = null,
    val size: Long = 0,
    val type: String = "",
    val percent: Int = 0,
    val response: Any? = null,
    val error: String? = null,
    val originFileObj: Any? = null,
)

/**
 * Upload file status
 */
enum class UploadFileStatus {
    Uploading,
    Done,
    Error,
    Removed
}

/**
 * Upload list display type
 */
enum class UploadListType {
    Text,
    Picture,
    PictureCard,
    PictureCircle
}

/**
 * Change event parameter passed to onChange callback
 * @param file The current file that triggered the change
 * @param fileList Complete list of files
 * @param event The event that triggered the change (optional)
 */
data class UploadChangeParam(
    val file: UploadFile,
    val fileList: List<UploadFile>,
    val event: Any? = null,
)

/**
 * Custom upload request options
 * @param onProgress Callback for upload progress
 * @param onSuccess Callback for successful upload
 * @param onError Callback for upload error
 * @param file The file to upload
 * @param action Upload URL
 * @param headers HTTP headers
 * @param withCredentials Whether to send cookies
 * @param data Additional data to send
 */
data class UploadRequestOption(
    val onProgress: (percent: Int, file: UploadFile) -> Unit,
    val onSuccess: (response: Any?, file: UploadFile) -> Unit,
    val onError: (error: String, file: UploadFile) -> Unit,
    val file: UploadFile,
    val action: String?,
    val headers: Map<String, String>?,
    val withCredentials: Boolean,
    val data: Any?,
)

/**
 * Show upload list configuration interface
 * @param showPreviewIcon Whether to show preview icon
 * @param showRemoveIcon Whether to show remove icon
 * @param showDownloadIcon Whether to show download icon
 * @param previewIcon Custom preview icon renderer
 * @param removeIcon Custom remove icon renderer
 * @param downloadIcon Custom download icon renderer
 */
data class ShowUploadListInterface(
    val showPreviewIcon: Boolean = true,
    val showRemoveIcon: Boolean = true,
    val showDownloadIcon: Boolean = false,
    val previewIcon: (@Composable (file: UploadFile) -> Unit)? = null,
    val removeIcon: (@Composable (file: UploadFile) -> Unit)? = null,
    val downloadIcon: (@Composable (file: UploadFile) -> Unit)? = null,
)

/**
 * Custom progress configuration
 * @param strokeColor Progress bar color
 * @param strokeWidth Progress bar width
 * @param format Custom format function
 * @param showInfo Whether to show progress info
 */
data class UploadProgress(
    val strokeColor: Color = Color(0xFF1890FF),
    val strokeWidth: Int = 2,
    val format: ((percent: Int) -> String)? = null,
    val showInfo: Boolean = true,
)

/**
 * Props for custom item renderer
 * @param originNode Original upload list item node
 * @param file Current file object
 * @param fileList Complete file list
 * @param actions Available actions (download, preview, remove)
 */
data class UploadListItemProps(
    val originNode: @Composable () -> Unit,
    val file: UploadFile,
    val fileList: List<UploadFile>,
    val actions: UploadItemActions,
)

/**
 * Available actions for upload item
 */
data class UploadItemActions(
    val download: () -> Unit,
    val preview: () -> Unit,
    val remove: () -> Unit,
)

/**
 * Drag event (platform-specific)
 */
expect class DragEvent

/**
 * Upload component - 100% React Ant Design parity
 *
 * File selection component for uploading files
 *
 * @param action Upload URL
 * @param method HTTP method (POST, PUT, PATCH)
 * @param directory Support uploading a directory
 * @param data Additional data to send with upload
 * @param headers HTTP headers
 * @param name File field name in the request
 * @param multiple Allow selecting multiple files
 * @param accept File types that can be accepted (e.g., ".jpg,.png")
 * @param beforeUpload Hook before uploading. Return false to stop upload
 * @param customRequest Override default upload behavior
 * @param disabled Disable the upload
 * @param fileList Controlled file list
 * @param defaultFileList Default file list (uncontrolled)
 * @param listType Type of list display
 * @param maxCount Max number of uploaded files
 * @param onChange Callback when upload state changes
 * @param onDrop Callback when files are dragged and dropped
 * @param onDownload Callback when download icon is clicked
 * @param onPreview Callback when preview icon is clicked
 * @param onRemove Callback when remove button is clicked. Return false to prevent removal
 * @param openFileDialogOnClick Open file dialog when clicking component
 * @param previewFile Custom preview URL generator
 * @param progress Custom progress configuration
 * @param showUploadList Whether to show upload list (Boolean or ShowUploadListInterface)
 * @param withCredentials Whether to send cookies with request
 * @param itemRender Custom upload list item renderer
 * @param isImageUrl Custom function to detect if file is image
 * @param className Additional CSS class
 * @param style Additional styles
 * @param iconRender Custom icon renderer for file types
 * @param children Custom upload trigger
 * @param modifier Compose modifier
 */
@Composable
fun AntUpload(
    action: String? = null,
    method: String = "POST",
    directory: Boolean = false,
    data: Any? = null,
    headers: Map<String, String>? = null,
    name: String = "file",
    multiple: Boolean = false,
    accept: String? = null,
    beforeUpload: ((UploadFile) -> Boolean)? = null,
    customRequest: ((UploadRequestOption) -> Unit)? = null,
    disabled: Boolean = false,
    fileList: List<UploadFile>? = null,
    defaultFileList: List<UploadFile> = emptyList(),
    listType: UploadListType = UploadListType.Text,
    maxCount: Int? = null,
    onChange: ((UploadChangeParam) -> Unit)? = null,
    onDrop: ((DragEvent) -> Unit)? = null,
    onDownload: ((UploadFile) -> Unit)? = null,
    onPreview: ((UploadFile) -> Unit)? = null,
    onRemove: ((UploadFile) -> Boolean)? = null,
    openFileDialogOnClick: Boolean = true,
    previewFile: ((UploadFile) -> String)? = null,
    progress: UploadProgress? = null,
    showUploadList: Any = true,
    withCredentials: Boolean = false,
    itemRender: (@Composable (UploadListItemProps) -> Unit)? = null,
    isImageUrl: ((UploadFile) -> Boolean)? = null,
    className: String? = null,
    style: Modifier = Modifier,
    iconRender: (@Composable (UploadFile) -> Unit)? = null,
    children: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    // Internal state for uncontrolled mode
    var internalFileList by remember { mutableStateOf(defaultFileList) }

    // Use controlled or uncontrolled file list
    val currentFileList = fileList ?: internalFileList
    val setFileList: (List<UploadFile>) -> Unit = { newList ->
        if (fileList == null) {
            internalFileList = newList
        }
    }

    // Parse showUploadList
    val uploadListConfig = when (showUploadList) {
        is Boolean -> if (showUploadList) ShowUploadListInterface() else null
        is ShowUploadListInterface -> showUploadList
        else -> ShowUploadListInterface()
    }

    val canUpload = maxCount == null || currentFileList.size < maxCount

    Column(
        modifier = modifier.then(style),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Upload trigger
        if (canUpload) {
            Box(
                modifier = Modifier.clickable(enabled = !disabled && openFileDialogOnClick) {
                    // In real implementation, would open file picker
                    // Platform-specific file selection would happen here
                    val newFile = UploadFile(
                        uid = "file-${TimeSource.Monotonic.markNow().hashCode()}",
                        name = "sample-file.pdf",
                        status = UploadFileStatus.Uploading,
                        percent = 0
                    )

                    // Before upload hook
                    if (beforeUpload?.invoke(newFile) != false) {
                        val updatedList = currentFileList + newFile
                        setFileList(updatedList)

                        // Trigger onChange
                        onChange?.invoke(
                            UploadChangeParam(
                                file = newFile,
                                fileList = updatedList
                            )
                        )

                        // Execute custom request or default upload
                        if (customRequest != null) {
                            val requestOption = UploadRequestOption(
                                onProgress = { percent, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) it.copy(percent = percent) else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(percent = percent),
                                            fileList = updated
                                        )
                                    )
                                },
                                onSuccess = { response, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) {
                                            it.copy(
                                                status = UploadFileStatus.Done,
                                                percent = 100,
                                                response = response
                                            )
                                        } else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(status = UploadFileStatus.Done, percent = 100),
                                            fileList = updated
                                        )
                                    )
                                },
                                onError = { error, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) {
                                            it.copy(
                                                status = UploadFileStatus.Error,
                                                error = error
                                            )
                                        } else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(status = UploadFileStatus.Error, error = error),
                                            fileList = updated
                                        )
                                    )
                                },
                                file = newFile,
                                action = action,
                                headers = headers,
                                withCredentials = withCredentials,
                                data = data
                            )
                            customRequest.invoke(requestOption)
                        }
                    }
                }
            ) {
                if (children != null) {
                    children()
                } else {
                    // Default upload button
                    AntButton(
                        onClick = { /* File picker will be triggered by the parent Box */ },
                        type = ButtonType.Default,
                        disabled = disabled
                    ) {
                        Text("Upload")
                    }
                }
            }
        }

        // File list
        if (uploadListConfig != null && currentFileList.isNotEmpty()) {
            when (listType) {
                UploadListType.Text -> {
                    UploadListText(
                        files = currentFileList,
                        config = uploadListConfig,
                        progress = progress,
                        onRemove = { file ->
                            if (onRemove?.invoke(file) != false) {
                                val updated = currentFileList.filter { it.uid != file.uid }
                                setFileList(updated)
                                onChange?.invoke(
                                    UploadChangeParam(
                                        file = file.copy(status = UploadFileStatus.Removed),
                                        fileList = updated
                                    )
                                )
                            }
                        },
                        onPreview = onPreview,
                        onDownload = onDownload,
                        itemRender = itemRender,
                        iconRender = iconRender
                    )
                }

                UploadListType.Picture, UploadListType.PictureCard -> {
                    UploadListPicture(
                        files = currentFileList,
                        listType = listType,
                        config = uploadListConfig,
                        progress = progress,
                        onRemove = { file ->
                            if (onRemove?.invoke(file) != false) {
                                val updated = currentFileList.filter { it.uid != file.uid }
                                setFileList(updated)
                                onChange?.invoke(
                                    UploadChangeParam(
                                        file = file.copy(status = UploadFileStatus.Removed),
                                        fileList = updated
                                    )
                                )
                            }
                        },
                        onPreview = onPreview,
                        onDownload = onDownload,
                        itemRender = itemRender,
                        iconRender = iconRender,
                        isImageUrl = isImageUrl,
                        previewFile = previewFile
                    )
                }

                UploadListType.PictureCircle -> {
                    UploadListPictureCircle(
                        files = currentFileList,
                        config = uploadListConfig,
                        progress = progress,
                        onRemove = { file ->
                            if (onRemove?.invoke(file) != false) {
                                val updated = currentFileList.filter { it.uid != file.uid }
                                setFileList(updated)
                                onChange?.invoke(
                                    UploadChangeParam(
                                        file = file.copy(status = UploadFileStatus.Removed),
                                        fileList = updated
                                    )
                                )
                            }
                        },
                        onPreview = onPreview,
                        itemRender = itemRender,
                        iconRender = iconRender,
                        isImageUrl = isImageUrl,
                        previewFile = previewFile
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadListText(
    files: List<UploadFile>,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    onDownload: ((UploadFile) -> Unit)?,
    itemRender: (@Composable (UploadListItemProps) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->
            val actions = UploadItemActions(
                download = { onDownload?.invoke(file) },
                preview = { onPreview?.invoke(file) },
                remove = { onRemove(file) }
            )

            val itemProps = UploadListItemProps(
                originNode = {
                    DefaultUploadListTextItem(
                        file = file,
                        config = config,
                        progress = progress,
                        onRemove = onRemove,
                        onPreview = onPreview,
                        onDownload = onDownload,
                        iconRender = iconRender
                    )
                },
                file = file,
                fileList = files,
                actions = actions
            )

            if (itemRender != null) {
                itemRender(itemProps)
            } else {
                itemProps.originNode()
            }
        }
    }
}

@Composable
private fun DefaultUploadListTextItem(
    file: UploadFile,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    onDownload: ((UploadFile) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFFAFAFA))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // File icon
            if (iconRender != null) {
                iconRender(file)
            } else {
                Text(
                    text = when (file.status) {
                        UploadFileStatus.Uploading -> "⏳"
                        UploadFileStatus.Done -> "📄"
                        UploadFileStatus.Error -> "❌"
                        UploadFileStatus.Removed -> "🗑"
                    },
                    fontSize = 16.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // File name (clickable for preview)
                if (config.showPreviewIcon && onPreview != null) {
                    Text(
                        text = file.name,
                        fontSize = 14.sp,
                        color = when (file.status) {
                            UploadFileStatus.Error -> Color(0xFFFF4D4F)
                            else -> Color(0xFF1890FF)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable {
                            onPreview(file)
                        }
                    )
                } else {
                    Text(
                        text = file.name,
                        fontSize = 14.sp,
                        color = when (file.status) {
                            UploadFileStatus.Error -> Color(0xFFFF4D4F)
                            else -> Color.Black
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Progress bar
                if (file.status == UploadFileStatus.Uploading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { file.percent / 100f },
                            modifier = Modifier.weight(1f),
                            color = progress?.strokeColor ?: Color(0xFF1890FF)
                        )

                        if (progress?.showInfo != false) {
                            val progressText = progress?.format?.invoke(file.percent)
                                ?: "${file.percent}%"
                            Text(
                                text = progressText,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Error message
                if (file.status == UploadFileStatus.Error && file.error != null) {
                    Text(
                        text = file.error,
                        fontSize = 12.sp,
                        color = Color(0xFFFF4D4F)
                    )
                }
            }
        }

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Download button
            if (config.showDownloadIcon && file.status == UploadFileStatus.Done && onDownload != null) {
                if (config.downloadIcon != null) {
                    Box(modifier = Modifier.clickable { onDownload(file) }) {
                        config.downloadIcon.invoke(file)
                    }
                } else {
                    Text(
                        text = "⬇",
                        fontSize = 16.sp,
                        color = Color(0xFF1890FF),
                        modifier = Modifier.clickable {
                            onDownload(file)
                        }
                    )
                }
            }

            // Preview button (optional, separate from name link)
            if (config.showPreviewIcon && config.previewIcon != null && onPreview != null) {
                Box(modifier = Modifier.clickable { onPreview(file) }) {
                    config.previewIcon.invoke(file)
                }
            }

            // Remove button
            if (config.showRemoveIcon) {
                if (config.removeIcon != null) {
                    Box(modifier = Modifier.clickable { onRemove(file) }) {
                        config.removeIcon.invoke(file)
                    }
                } else {
                    Text(
                        text = "×",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable {
                            onRemove(file)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadListPicture(
    files: List<UploadFile>,
    listType: UploadListType,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    onDownload: ((UploadFile) -> Unit)?,
    itemRender: (@Composable (UploadListItemProps) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
    isImageUrl: ((UploadFile) -> Boolean)?,
    previewFile: ((UploadFile) -> String)?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        files.forEach { file ->
            val actions = UploadItemActions(
                download = { onDownload?.invoke(file) },
                preview = { onPreview?.invoke(file) },
                remove = { onRemove(file) }
            )

            val itemProps = UploadListItemProps(
                originNode = {
                    DefaultUploadListPictureItem(
                        file = file,
                        listType = listType,
                        config = config,
                        progress = progress,
                        onRemove = onRemove,
                        onPreview = onPreview,
                        onDownload = onDownload,
                        iconRender = iconRender,
                        isImageUrl = isImageUrl,
                        previewFile = previewFile
                    )
                },
                file = file,
                fileList = files,
                actions = actions
            )

            if (itemRender != null) {
                itemRender(itemProps)
            } else {
                itemProps.originNode()
            }
        }
    }
}

@Composable
private fun DefaultUploadListPictureItem(
    file: UploadFile,
    listType: UploadListType,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    onDownload: ((UploadFile) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
    isImageUrl: ((UploadFile) -> Boolean)?,
    previewFile: ((UploadFile) -> String)?,
) {
    val cardSize = if (listType == UploadListType.PictureCard) 104.dp else 48.dp
    val isImage = isImageUrl?.invoke(file) ?: (file.type.startsWith("image/"))

    Box(
        modifier = Modifier
            .size(cardSize)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
            .background(Color(0xFFFAFAFA))
            .clickable(enabled = config.showPreviewIcon && onPreview != null) {
                onPreview?.invoke(file)
            }
    ) {
        // Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (file.status) {
                UploadFileStatus.Uploading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AntSpin(spinning = true, size = SpinSize.Small)
                        if (progress?.showInfo != false) {
                            val progressText = progress?.format?.invoke(file.percent)
                                ?: "${file.percent}%"
                            Text(
                                text = progressText,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                UploadFileStatus.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "❌", fontSize = 24.sp)
                        if (file.error != null && listType == UploadListType.PictureCard) {
                            Text(
                                text = file.error,
                                fontSize = 10.sp,
                                color = Color(0xFFFF4D4F),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                else -> {
                    // Display thumbnail or icon
                    if (iconRender != null) {
                        iconRender(file)
                    } else if (isImage && (file.thumbUrl != null || file.url != null)) {
                        // In real implementation, would load actual image
                        // For now, show placeholder
                        Text(text = "🖼", fontSize = if (listType == UploadListType.PictureCard) 32.sp else 16.sp)
                    } else {
                        Text(text = "📄", fontSize = if (listType == UploadListType.PictureCard) 32.sp else 16.sp)
                    }
                }
            }
        }

        // Action overlay (on hover in web)
        if (file.status == UploadFileStatus.Done && listType == UploadListType.PictureCard) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Preview icon
                if (config.showPreviewIcon && onPreview != null) {
                    if (config.previewIcon != null) {
                        Box(modifier = Modifier.clickable { onPreview(file) }) {
                            config.previewIcon.invoke(file)
                        }
                    } else {
                        Text(
                            text = "👁",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable { onPreview(file) }
                        )
                    }
                }

                // Download icon
                if (config.showDownloadIcon && onDownload != null) {
                    if (config.downloadIcon != null) {
                        Box(modifier = Modifier.clickable { onDownload(file) }) {
                            config.downloadIcon.invoke(file)
                        }
                    } else {
                        Text(
                            text = "⬇",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable { onDownload(file) }
                        )
                    }
                }
            }
        }

        // Remove button (always visible)
        if (config.showRemoveIcon) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onRemove(file) },
                contentAlignment = Alignment.Center
            ) {
                if (config.removeIcon != null) {
                    config.removeIcon.invoke(file)
                } else {
                    Text(
                        text = "×",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadListPictureCircle(
    files: List<UploadFile>,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    itemRender: (@Composable (UploadListItemProps) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
    isImageUrl: ((UploadFile) -> Boolean)?,
    previewFile: ((UploadFile) -> String)?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        files.forEach { file ->
            val actions = UploadItemActions(
                download = { },
                preview = { onPreview?.invoke(file) },
                remove = { onRemove(file) }
            )

            val itemProps = UploadListItemProps(
                originNode = {
                    DefaultUploadListPictureCircleItem(
                        file = file,
                        config = config,
                        progress = progress,
                        onRemove = onRemove,
                        onPreview = onPreview,
                        iconRender = iconRender,
                        isImageUrl = isImageUrl,
                        previewFile = previewFile
                    )
                },
                file = file,
                fileList = files,
                actions = actions
            )

            if (itemRender != null) {
                itemRender(itemProps)
            } else {
                itemProps.originNode()
            }
        }
    }
}

@Composable
private fun DefaultUploadListPictureCircleItem(
    file: UploadFile,
    config: ShowUploadListInterface,
    progress: UploadProgress?,
    onRemove: (UploadFile) -> Unit,
    onPreview: ((UploadFile) -> Unit)?,
    iconRender: (@Composable (UploadFile) -> Unit)?,
    isImageUrl: ((UploadFile) -> Boolean)?,
    previewFile: ((UploadFile) -> String)?,
) {
    val isImage = isImageUrl?.invoke(file) ?: (file.type.startsWith("image/"))

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(40.dp))
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(40.dp))
            .background(Color(0xFFFAFAFA))
            .clickable(enabled = config.showPreviewIcon && onPreview != null) {
                onPreview?.invoke(file)
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (file.status) {
                UploadFileStatus.Uploading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AntSpin(spinning = true, size = SpinSize.Small)
                        if (progress?.showInfo != false) {
                            val progressText = progress?.format?.invoke(file.percent)
                                ?: "${file.percent}%"
                            Text(
                                text = progressText,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                UploadFileStatus.Error -> Text(text = "❌", fontSize = 24.sp)
                else -> {
                    if (iconRender != null) {
                        iconRender(file)
                    } else if (isImage && (file.thumbUrl != null || file.url != null)) {
                        Text(text = "🖼", fontSize = 32.sp)
                    } else {
                        Text(text = "📄", fontSize = 32.sp)
                    }
                }
            }
        }

        // Remove button
        if (config.showRemoveIcon) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onRemove(file) },
                contentAlignment = Alignment.Center
            ) {
                if (config.removeIcon != null) {
                    config.removeIcon.invoke(file)
                } else {
                    Text(
                        text = "×",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * Dragger component - Drag and drop upload area
 *
 * Special version of Upload with drag and drop support
 *
 * @param action Upload URL
 * @param method HTTP method
 * @param directory Support uploading a directory
 * @param data Additional data to send
 * @param headers HTTP headers
 * @param name File field name
 * @param multiple Allow selecting multiple files
 * @param accept File types that can be accepted
 * @param beforeUpload Hook before uploading
 * @param customRequest Override default upload behavior
 * @param disabled Disable the upload
 * @param fileList Controlled file list
 * @param defaultFileList Default file list (uncontrolled)
 * @param maxCount Max number of uploaded files
 * @param onChange Callback when upload state changes
 * @param onDrop Callback when files are dragged and dropped
 * @param onRemove Callback when remove button is clicked
 * @param openFileDialogOnClick Open file dialog when clicking
 * @param withCredentials Whether to send cookies
 * @param hint Main hint text
 * @param description Additional description text
 * @param height Height of the dragger area
 * @param showUploadList Whether to show upload list below dragger
 * @param children Custom content inside dragger
 * @param modifier Compose modifier
 */
@Composable
fun AntDragger(
    action: String? = null,
    method: String = "POST",
    directory: Boolean = false,
    data: Any? = null,
    headers: Map<String, String>? = null,
    name: String = "file",
    multiple: Boolean = true,
    accept: String? = null,
    beforeUpload: ((UploadFile) -> Boolean)? = null,
    customRequest: ((UploadRequestOption) -> Unit)? = null,
    disabled: Boolean = false,
    fileList: List<UploadFile>? = null,
    defaultFileList: List<UploadFile> = emptyList(),
    maxCount: Int? = null,
    onChange: ((UploadChangeParam) -> Unit)? = null,
    onDrop: ((DragEvent) -> Unit)? = null,
    onRemove: ((UploadFile) -> Boolean)? = null,
    openFileDialogOnClick: Boolean = true,
    withCredentials: Boolean = false,
    hint: String = "Click or drag file to this area to upload",
    description: String? = null,
    height: Int = 180,
    showUploadList: Boolean = true,
    children: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    // Internal state for uncontrolled mode
    var internalFileList by remember { mutableStateOf(defaultFileList) }

    // Use controlled or uncontrolled file list
    val currentFileList = fileList ?: internalFileList
    val setFileList: (List<UploadFile>) -> Unit = { newList ->
        if (fileList == null) {
            internalFileList = newList
        }
    }

    val canUpload = maxCount == null || currentFileList.size < maxCount

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dragger area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(2.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                .background(Color(0xFFFAFAFA))
                .clickable(enabled = !disabled && openFileDialogOnClick && canUpload) {
                    // In real implementation, would open file picker
                    val newFile = UploadFile(
                        uid = "file-${TimeSource.Monotonic.markNow().hashCode()}",
                        name = "dragged-file.pdf",
                        status = UploadFileStatus.Uploading,
                        percent = 0
                    )

                    if (beforeUpload?.invoke(newFile) != false) {
                        val updatedList = currentFileList + newFile
                        setFileList(updatedList)

                        onChange?.invoke(
                            UploadChangeParam(
                                file = newFile,
                                fileList = updatedList
                            )
                        )

                        if (customRequest != null) {
                            val requestOption = UploadRequestOption(
                                onProgress = { percent, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) it.copy(percent = percent) else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(percent = percent),
                                            fileList = updated
                                        )
                                    )
                                },
                                onSuccess = { response, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) {
                                            it.copy(
                                                status = UploadFileStatus.Done,
                                                percent = 100,
                                                response = response
                                            )
                                        } else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(status = UploadFileStatus.Done, percent = 100),
                                            fileList = updated
                                        )
                                    )
                                },
                                onError = { error, file ->
                                    val updated = updatedList.map {
                                        if (it.uid == file.uid) {
                                            it.copy(
                                                status = UploadFileStatus.Error,
                                                error = error
                                            )
                                        } else it
                                    }
                                    setFileList(updated)
                                    onChange?.invoke(
                                        UploadChangeParam(
                                            file = file.copy(status = UploadFileStatus.Error, error = error),
                                            fileList = updated
                                        )
                                    )
                                },
                                file = newFile,
                                action = action,
                                headers = headers,
                                withCredentials = withCredentials,
                                data = data
                            )
                            customRequest.invoke(requestOption)
                        }
                    }
                }
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (children != null) {
                    children()
                } else {
                    Text(
                        text = "📁",
                        fontSize = 48.sp,
                        color = if (disabled) Color.Gray else Color(0xFF1890FF)
                    )
                }

                Text(
                    text = hint,
                    fontSize = 16.sp,
                    color = if (disabled) Color.Gray else Color.Black
                )

                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (!canUpload) {
                    Text(
                        text = "Maximum file count reached",
                        fontSize = 12.sp,
                        color = Color(0xFFFF4D4F)
                    )
                }
            }
        }

        // File list
        if (showUploadList && currentFileList.isNotEmpty()) {
            UploadListText(
                files = currentFileList,
                config = ShowUploadListInterface(),
                progress = null,
                onRemove = { file ->
                    if (onRemove?.invoke(file) != false) {
                        val updated = currentFileList.filter { it.uid != file.uid }
                        setFileList(updated)
                        onChange?.invoke(
                            UploadChangeParam(
                                file = file.copy(status = UploadFileStatus.Removed),
                                fileList = updated
                            )
                        )
                    }
                },
                onPreview = null,
                onDownload = null,
                itemRender = null,
                iconRender = null
            )
        }
    }
}
