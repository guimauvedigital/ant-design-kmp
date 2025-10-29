package digital.guimauve.antdesign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

@Composable
fun AntPopconfirm(
    title: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    okText: String = "OK",
    cancelText: String = "Cancel",
    onCancel: (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    okType: ButtonType = ButtonType.Primary,
    okDanger: Boolean = false,
    cancelButtonProps: ButtonType = ButtonType.Default,
    showCancel: Boolean = true,
    disabled: Boolean = false,
    child: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier.clickable(enabled = !disabled) {
                visible = true
            }
        ) {
            child()
        }

        if (visible) {
            Popup(
                onDismissRequest = { visible = false }
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(300.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            icon?.invoke() ?: Text(
                                text = "⚠",
                                color = Color(0xFFFAAD14),
                                fontSize = 16.sp
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )

                                if (description != null) {
                                    Text(
                                        text = description,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            if (showCancel) {
                                AntButton(
                                    onClick = {
                                        visible = false
                                        onCancel?.invoke()
                                    },
                                    type = cancelButtonProps,
                                    size = ButtonSize.Small
                                ) {
                                    Text(cancelText)
                                }
                            }

                            AntButton(
                                onClick = {
                                    visible = false
                                    onConfirm()
                                },
                                type = okType,
                                danger = okDanger,
                                size = ButtonSize.Small
                            ) {
                                Text(okText)
                            }
                        }
                    }
                }
            }
        }
    }
}
