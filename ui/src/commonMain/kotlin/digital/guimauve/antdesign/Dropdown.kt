package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DropdownTrigger {
    Click,
    Hover,
    ContextMenu
}

@Composable
fun AntDropdown(
    menu: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    trigger: DropdownTrigger = DropdownTrigger.Hover,
    placement: DropdownPlacement = DropdownPlacement.BottomLeft,
    disabled: Boolean = false,
    visible: Boolean? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    val menuVisible = visible ?: showMenu

    Box(modifier = modifier) {
        Box(
            modifier = Modifier.clickable(enabled = !disabled) {
                showMenu = !showMenu
                onVisibleChange?.invoke(!showMenu)
            }
        ) {
            content()
        }

        if (menuVisible) {
            Card(
                modifier = Modifier.padding(top = 4.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp)
                ) {
                    menu()
                }
            }
        }
    }
}

@Composable
fun DropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !disabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.invoke()
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (disabled) Color.Gray else Color.Black
        )
    }
}

enum class DropdownPlacement {
    BottomLeft,
    BottomCenter,
    BottomRight,
    TopLeft,
    TopCenter,
    TopRight
}
