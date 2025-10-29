package digital.guimauve.antdesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * QRCode status types
 * - Active: QRCode is active and scannable
 * - Expired: QRCode has expired
 * - Loading: QRCode is loading
 * - Scanned: QRCode has been scanned (since v5.13.0)
 */
enum class QRCodeStatus {
    Active,
    Expired,
    Loading,
    Scanned
}

/**
 * QRCode error correction level
 * - L: ~7% correction
 * - M: ~15% correction
 * - Q: ~25% correction
 * - H: ~30% correction
 */
enum class QRCodeErrorLevel {
    L, // 7%
    M, // 15%
    Q, // 25%
    H  // 30%
}

/**
 * QRCode rendering type
 * - Canvas: Render as Canvas (default)
 * - SVG: Render as SVG (since v5.6.0)
 */
enum class QRCodeType {
    Canvas,
    SVG
}

/**
 * Icon size specification
 * Can be a simple size or specific width/height
 */
sealed class IconSize {
    data class Simple(val size: Dp) : IconSize()
    data class Detailed(val width: Dp, val height: Dp) : IconSize()
}

/**
 * Status render information passed to custom status renderer
 */
data class StatusRenderInfo(
    val status: QRCodeStatus,
    val onRefresh: (() -> Unit)?,
)

/**
 * QRCode component - Convert text into QR code with support for custom color and logo
 *
 * @param value Scanned text (supports String or List<String> since v5.28.0)
 * @param modifier Modifier for styling
 * @param type Render type: Canvas or SVG (default: Canvas, SVG since v5.6.0)
 * @param icon Include image url (only image links are supported)
 * @param size QRCode size (default: 160.dp)
 * @param iconSize Include image size (default: 40.dp, supports detailed size since v5.19.0)
 * @param color QRCode color (default: Color.Black / #000)
 * @param bgColor QRCode background color (default: Color.Transparent, since v5.5.0)
 * @param bordered Whether has border style (default: true)
 * @param errorLevel Error correction level: L/M/Q/H (default: M)
 * @param boostLevel If enabled, the Error Correction Level of the result may be higher than specified (default: true, since v5.28.0)
 * @param status QRCode status: Active/Expired/Loading/Scanned (Scanned since v5.13.0)
 * @param onRefresh Callback function for refresh action
 * @param statusRender Custom status render function (since v5.20.0)
 * @param className CSS class name
 * @param rootClassName Root class name
 * @param prefixCls Prefix class name (default: "ant-qrcode")
 */
@Composable
fun AntQRCode(
    value: Any, // String or List<String>
    modifier: Modifier = Modifier,
    type: QRCodeType = QRCodeType.Canvas,
    icon: String? = null,
    size: Dp = 160.dp,
    iconSize: IconSize = IconSize.Simple(40.dp),
    color: Color = Color.Black,
    bgColor: Color = Color.Transparent,
    bordered: Boolean = true,
    errorLevel: QRCodeErrorLevel = QRCodeErrorLevel.M,
    boostLevel: Boolean = true,
    status: QRCodeStatus = QRCodeStatus.Active,
    onRefresh: (() -> Unit)? = null,
    statusRender: (@Composable (StatusRenderInfo) -> Unit)? = null,
    className: String? = null,
    rootClassName: String? = null,
    prefixCls: String = "ant-qrcode",
) {
    // Convert value to string
    val qrValue = when (value) {
        is String -> value
        is List<*> -> value.joinToString("\n")
        else -> value.toString()
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (bordered) {
                    Modifier.border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
            .background(bgColor)
            .padding(if (bordered) 16.dp else 0.dp)
    ) {
        when (status) {
            QRCodeStatus.Active -> {
                // Render QR code based on type
                when (type) {
                    QRCodeType.Canvas -> {
                        QRCodeCanvas(
                            value = qrValue,
                            size = size - if (bordered) 32.dp else 0.dp,
                            color = color,
                            bgColor = bgColor,
                            iconSize = iconSize,
                            icon = icon,
                            errorLevel = errorLevel,
                            boostLevel = boostLevel
                        )
                    }

                    QRCodeType.SVG -> {
                        QRCodeSVG(
                            value = qrValue,
                            size = size - if (bordered) 32.dp else 0.dp,
                            color = color,
                            bgColor = bgColor,
                            iconSize = iconSize,
                            icon = icon,
                            errorLevel = errorLevel,
                            boostLevel = boostLevel
                        )
                    }
                }
            }

            QRCodeStatus.Loading, QRCodeStatus.Expired, QRCodeStatus.Scanned -> {
                // Use custom statusRender if provided
                if (statusRender != null) {
                    statusRender(StatusRenderInfo(status, onRefresh))
                } else {
                    // Default status rendering
                    DefaultStatusRender(status, onRefresh)
                }
            }
        }
    }
}

/**
 * Default status rendering for non-active states
 */
@Composable
private fun DefaultStatusRender(
    status: QRCodeStatus,
    onRefresh: (() -> Unit)?,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            QRCodeStatus.Loading -> {
                AntSpin(spinning = true)
            }

            QRCodeStatus.Expired -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "QR code expired",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    if (onRefresh != null) {
                        AntButton(
                            onClick = onRefresh,
                            size = ButtonSize.Small
                        ) {
                            Text("Refresh")
                        }
                    }
                }
            }

            QRCodeStatus.Scanned -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Scanned successfully",
                        fontSize = 14.sp,
                        color = Color(0xFF52C41A) // Success green
                    )
                }
            }

            else -> { /* Active state handled elsewhere */
            }
        }
    }
}

/**
 * Render QRCode using Canvas
 */
@Composable
private fun QRCodeCanvas(
    value: String,
    size: Dp,
    color: Color,
    bgColor: Color,
    iconSize: IconSize,
    icon: String?,
    errorLevel: QRCodeErrorLevel,
    boostLevel: Boolean,
) {
    // Simplified QR code pattern
    // In real implementation, would use proper QR code generation library
    Canvas(
        modifier = Modifier.size(size)
    ) {
        val canvasSize = size.toPx()
        val gridSize = 21 // Standard QR code size for version 1
        val cellSize = canvasSize / gridSize

        // Draw background
        drawRect(
            color = bgColor,
            size = Size(canvasSize, canvasSize)
        )

        // Calculate icon size in pixels
        val iconSizePx = when (iconSize) {
            is IconSize.Simple -> iconSize.size.toPx()
            is IconSize.Detailed -> maxOf(iconSize.width.toPx(), iconSize.height.toPx())
        }

        val iconWidthPx = when (iconSize) {
            is IconSize.Simple -> iconSize.size.toPx()
            is IconSize.Detailed -> iconSize.width.toPx()
        }

        val iconHeightPx = when (iconSize) {
            is IconSize.Simple -> iconSize.size.toPx()
            is IconSize.Detailed -> iconSize.height.toPx()
        }

        // Draw a simplified pattern based on value hash
        // Note: errorLevel and boostLevel would be used in actual QR code generation
        val hash = value.hashCode()
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                // Simplified pattern generation
                val shouldFill = ((row * gridSize + col + hash) % 3) != 0

                // Skip center area if icon is present
                val inIconArea = icon != null &&
                        row >= gridSize / 2 - 2 && row <= gridSize / 2 + 2 &&
                        col >= gridSize / 2 - 2 && col <= gridSize / 2 + 2

                if (shouldFill && !inIconArea) {
                    drawRect(
                        color = color,
                        topLeft = Offset(col * cellSize, row * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }

        // Draw finder patterns (corner squares)
        listOf(
            Pair(0, 0),
            Pair(gridSize - 7, 0),
            Pair(0, gridSize - 7)
        ).forEach { (row, col) ->
            // Outer square
            drawRect(
                color = color,
                topLeft = Offset(col * cellSize, row * cellSize),
                size = Size(cellSize * 7, cellSize * 7)
            )
            // Inner white square
            drawRect(
                color = bgColor,
                topLeft = Offset((col + 1) * cellSize, (row + 1) * cellSize),
                size = Size(cellSize * 5, cellSize * 5)
            )
            // Center black square
            drawRect(
                color = color,
                topLeft = Offset((col + 2) * cellSize, (row + 2) * cellSize),
                size = Size(cellSize * 3, cellSize * 3)
            )
        }

        // Draw icon placeholder if icon is present
        if (icon != null) {
            val iconOffset = (canvasSize - iconWidthPx) / 2
            val iconOffsetY = (canvasSize - iconHeightPx) / 2

            // White background for icon
            drawRect(
                color = Color.White,
                topLeft = Offset(iconOffset, iconOffsetY),
                size = Size(iconWidthPx, iconHeightPx)
            )
            // Icon placeholder (gray box)
            drawRect(
                color = Color(0xFFD9D9D9),
                topLeft = Offset(iconOffset + iconWidthPx / 4, iconOffsetY + iconHeightPx / 4),
                size = Size(iconWidthPx / 2, iconHeightPx / 2)
            )
        }
    }
}

/**
 * Render QRCode using SVG
 * Note: This is a simplified implementation using Canvas
 * In a real implementation, this would use actual SVG rendering
 */
@Composable
private fun QRCodeSVG(
    value: String,
    size: Dp,
    color: Color,
    bgColor: Color,
    iconSize: IconSize,
    icon: String?,
    errorLevel: QRCodeErrorLevel,
    boostLevel: Boolean,
) {
    // For now, SVG rendering uses the same Canvas implementation
    // In a real implementation, this would generate actual SVG elements
    QRCodeCanvas(
        value = value,
        size = size,
        color = color,
        bgColor = bgColor,
        iconSize = iconSize,
        icon = icon,
        errorLevel = errorLevel,
        boostLevel = boostLevel
    )
}
