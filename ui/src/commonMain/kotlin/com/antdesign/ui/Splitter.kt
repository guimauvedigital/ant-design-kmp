package com.antdesign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class SplitterLayout {
    Horizontal,
    Vertical
}

@Composable
fun AntSplitter(
    modifier: Modifier = Modifier,
    layout: SplitterLayout = SplitterLayout.Horizontal,
    initialSize: Dp = 200.dp,
    minSize: Dp = 50.dp,
    maxSize: Dp? = null,
    resizable: Boolean = true,
    onResize: ((Dp) -> Unit)? = null,
    panel1: @Composable () -> Unit,
    panel2: @Composable () -> Unit
) {
    var size by remember { mutableStateOf(initialSize) }
    val density = LocalDensity.current

    when (layout) {
        SplitterLayout.Horizontal -> {
            Row(modifier = modifier.fillMaxSize()) {
                // Panel 1
                Box(modifier = Modifier.width(size)) {
                    panel1()
                }

                // Divider
                if (resizable) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFD9D9D9))
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val newSize = with(density) {
                                        (size.toPx() + dragAmount.x).toDp()
                                    }
                                    size = newSize.coerceIn(
                                        minSize,
                                        maxSize ?: Dp.Infinity
                                    )
                                    onResize?.invoke(size)
                                }
                            }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFD9D9D9))
                    )
                }

                // Panel 2
                Box(modifier = Modifier.weight(1f)) {
                    panel2()
                }
            }
        }
        SplitterLayout.Vertical -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Panel 1
                Box(modifier = Modifier.height(size)) {
                    panel1()
                }

                // Divider
                if (resizable) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth()
                            .background(Color(0xFFD9D9D9))
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val newSize = with(density) {
                                        (size.toPx() + dragAmount.y).toDp()
                                    }
                                    size = newSize.coerceIn(
                                        minSize,
                                        maxSize ?: Dp.Infinity
                                    )
                                    onResize?.invoke(size)
                                }
                            }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color(0xFFD9D9D9))
                    )
                }

                // Panel 2
                Box(modifier = Modifier.weight(1f)) {
                    panel2()
                }
            }
        }
    }
}
