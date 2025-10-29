package digital.guimauve.antdesign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AntTitle(
    text: String,
    modifier: Modifier = Modifier,
    level: Int = 1,
    color: Color = Color.Black,
    style: TextStyle = LocalTextStyle.current,
) {
    val fontSize = when (level) {
        1 -> 38.sp
        2 -> 30.sp
        3 -> 24.sp
        4 -> 20.sp
        5 -> 16.sp
        else -> 14.sp
    }

    val fontWeight = when (level) {
        1, 2 -> FontWeight.Bold
        else -> FontWeight.SemiBold
    }

    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )
    )
}

@Composable
fun AntText(
    text: String,
    modifier: Modifier = Modifier,
    type: TextType = TextType.Default,
    size: TextSize = TextSize.Default,
    strong: Boolean = false,
    italic: Boolean = false,
    underline: Boolean = false,
    delete: Boolean = false,
    code: Boolean = false,
    mark: Boolean = false,
    copyable: Boolean = false,
    color: Color? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val textColor = color ?: when (type) {
        TextType.Default -> Color.Black
        TextType.Secondary -> Color.Gray
        TextType.Success -> Color(0xFF52C41A)
        TextType.Warning -> Color(0xFFFAAD14)
        TextType.Danger -> Color(0xFFFF4D4F)
    }

    val fontSize = when (size) {
        TextSize.Small -> 12.sp
        TextSize.Default -> 14.sp
        TextSize.Large -> 16.sp
    }

    val textDecoration = when {
        underline && delete -> TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
        underline -> TextDecoration.Underline
        delete -> TextDecoration.LineThrough
        else -> null
    }

    val finalStyle = style.copy(
        fontSize = fontSize,
        fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        textDecoration = textDecoration,
        color = textColor,
        background = if (mark) Color(0xFFFFE58F) else Color.Unspecified,
        fontFamily = if (code) FontFamily.Monospace else null
    )

    if (copyable) {
        SelectionContainer {
            Text(
                text = text,
                modifier = modifier,
                style = finalStyle
            )
        }
    } else {
        Text(
            text = text,
            modifier = modifier,
            style = finalStyle
        )
    }
}

enum class TextType {
    Default,
    Secondary,
    Success,
    Warning,
    Danger
}

enum class TextSize {
    Small,
    Default,
    Large
}

@Composable
fun AntParagraph(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        modifier = modifier.padding(vertical = 8.dp),
        style = style.copy(
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = color
        )
    )
}

@Composable
fun AntLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    href: String? = null,
    target: String? = null,
    disabled: Boolean = false,
    underline: Boolean = true,
    color: Color = Color(0xFF1890FF),
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        modifier = modifier
            .clickable(enabled = !disabled) { onClick() },
        style = style.copy(
            color = if (disabled) Color.Gray else color,
            textDecoration = if (underline) TextDecoration.Underline else null
        )
    )
}
