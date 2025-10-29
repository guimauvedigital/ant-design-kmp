package digital.guimauve.antdesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.storytale.story

val Grid by story {
    val gutter by parameter(16)
    val wrap by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic span variants
        Text("Span variants:")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 12, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-12", color = Color.White)
                }
            }
            AntCol(span = 12, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-12", color = Color.White)
                }
            }
        }

        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-8", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-8", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-8", color = Color.White)
                }
            }
        }

        // Additional span variants
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 6, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-6", color = Color.White)
                }
            }
            AntCol(span = 6, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-6", color = Color.White)
                }
            }
            AntCol(span = 6, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-6", color = Color.White)
                }
            }
            AntCol(span = 6, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFA541C))
                        .padding(8.dp)
                ) {
                    Text("Col-6", color = Color.White)
                }
            }
        }

        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 16, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-16", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-8", color = Color.White)
                }
            }
        }

        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 16, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-16", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        // Align variants
        Text("Align variants:")
        Text("RowAlign.Top")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        Text("RowAlign.Middle")
        AntRow(
            gutter = gutter,
            align = RowAlign.Middle,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        Text("RowAlign.Bottom")
        AntRow(
            gutter = gutter,
            align = RowAlign.Bottom,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("60dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("40dp", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("80dp", color = Color.White)
                }
            }
        }

        // Justify variants
        Text("Justify variants:")
        Text("RowJustify.Start")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        Text("RowJustify.End")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.End,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        Text("RowJustify.Center")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Center,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceBetween")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.SpaceBetween,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceAround")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.SpaceAround,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        Text("RowJustify.SpaceEvenly")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.SpaceEvenly,
            wrap = wrap
        ) {
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
            AntCol(span = 4, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Col-4", color = Color.White)
                }
            }
        }

        // Offset examples
        Text("Offset examples:")
        Text("Offset = 4")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 4, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-8 offset-4", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-8", color = Color.White)
                }
            }
        }

        Text("Offset = 8")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 6, offset = 8, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-6 offset-8", color = Color.White)
                }
            }
            AntCol(span = 6, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-6", color = Color.White)
                }
            }
        }

        Text("Mixed offsets")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 6, offset = 2, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("Col-6 offset-2", color = Color.White)
                }
            }
            AntCol(span = 6, offset = 4, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Col-6 offset-4", color = Color.White)
                }
            }
        }

        // Order examples
        Text("Order examples:")
        Text("Normal order (0, 0, 0)")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("First (order=0)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Second (order=0)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Third (order=0)", color = Color.White)
                }
            }
        }

        Text("Reversed order (2, 1, 0)")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 2) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("First (order=2)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 1) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Second (order=1)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Third (order=0)", color = Color.White)
                }
            }
        }

        Text("Custom order (1, 3, 2)")
        AntRow(
            gutter = gutter,
            align = RowAlign.Top,
            justify = RowJustify.Start,
            wrap = wrap
        ) {
            AntCol(span = 8, offset = 0, order = 1) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF1890FF))
                        .padding(8.dp)
                ) {
                    Text("First (order=1)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 3) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFF52C41A))
                        .padding(8.dp)
                ) {
                    Text("Second (order=3)", color = Color.White)
                }
            }
            AntCol(span = 8, offset = 0, order = 2) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xFFFAAD14))
                        .padding(8.dp)
                ) {
                    Text("Third (order=2)", color = Color.White)
                }
            }
        }
    }
}

val LayoutComponent by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntLayout {
            AntLayoutHeader {
                Text("Header", color = Color.White)
            }
            AntLayoutContent {
                Text("Content")
            }
            AntLayoutFooter {
                Text("Footer")
            }
        }
    }
}

val Space by story {
    val wrap by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("All space sizes (Horizontal):")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small")
                AntSpace(
                    size = SpaceSize.Small,
                    direction = SpaceDirection.Horizontal,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 1") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 2") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 3") }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Middle")
                AntSpace(
                    size = SpaceSize.Middle,
                    direction = SpaceDirection.Horizontal,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 1") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 2") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 3") }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Large")
                AntSpace(
                    size = SpaceSize.Large,
                    direction = SpaceDirection.Horizontal,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 1") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 2") }
                    AntButton(
                        onClick = {},
                        size = ButtonSize.Small
                    ) { Text("Btn 3") }
                }
            }
        }

        Text("Direction variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Horizontal")
                AntSpace(
                    size = SpaceSize.Middle,
                    direction = SpaceDirection.Horizontal,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(onClick = {}) { Text("Button 1") }
                    AntButton(onClick = {}) { Text("Button 2") }
                    AntButton(onClick = {}) { Text("Button 3") }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Vertical")
                AntSpace(
                    size = SpaceSize.Middle,
                    direction = SpaceDirection.Vertical,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(onClick = {}) { Text("Button 1") }
                    AntButton(onClick = {}) { Text("Button 2") }
                    AntButton(onClick = {}) { Text("Button 3") }
                }
            }
        }

        Text("Align variants (Horizontal):")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Align: Start")
            AntSpace(
                size = SpaceSize.Middle,
                direction = SpaceDirection.Horizontal,
                align = SpaceAlign.Start
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "Item 1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "Item 2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "Item 3",
                        color = Color.White
                    )
                }
            }

            Text("Align: Center")
            AntSpace(
                size = SpaceSize.Middle,
                direction = SpaceDirection.Horizontal,
                align = SpaceAlign.Center
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "Item 1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "Item 2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "Item 3",
                        color = Color.White
                    )
                }
            }

            Text("Align: End")
            AntSpace(
                size = SpaceSize.Middle,
                direction = SpaceDirection.Horizontal,
                align = SpaceAlign.End
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "Item 1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "Item 2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "Item 3",
                        color = Color.White
                    )
                }
            }
        }

        Text("Wrap example:")
        AntSpace(
            size = SpaceSize.Middle,
            direction = SpaceDirection.Horizontal,
            align = SpaceAlign.Start,
            wrap = true
        ) {
            repeat(10) { index ->
                AntButton(
                    onClick = {},
                    size = ButtonSize.Small
                ) {
                    Text("Button ${index + 1}")
                }
            }
        }
    }
}

val VerticalSpace by story {
    val wrap by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Vertical Space with all sizes:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Small")
                AntSpace(
                    size = SpaceSize.Small,
                    direction = SpaceDirection.Vertical,
                    align = SpaceAlign.Start
                ) {
                    AntButton(onClick = {}) { Text("Button 1") }
                    AntButton(onClick = {}) { Text("Button 2") }
                    AntButton(onClick = {}) { Text("Button 3") }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Middle")
                AntSpace(
                    size = SpaceSize.Middle,
                    direction = SpaceDirection.Vertical,
                    align = SpaceAlign.Start
                ) {
                    AntButton(onClick = {}) { Text("Button 1") }
                    AntButton(onClick = {}) { Text("Button 2") }
                    AntButton(onClick = {}) { Text("Button 3") }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Large")
                AntSpace(
                    size = SpaceSize.Large,
                    direction = SpaceDirection.Vertical,
                    align = SpaceAlign.Start,
                    wrap = wrap
                ) {
                    AntButton(onClick = {}) { Text("Button 1") }
                    AntButton(onClick = {}) { Text("Button 2") }
                    AntButton(onClick = {}) { Text("Button 3") }
                }
            }
        }
    }
}

val Flex by story {
    val gap by parameter(16)
    val vertical by parameter(false)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("All justify variants:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("FlexJustify.FlexStart")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexStart,
                align = FlexAlign.Center,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexJustify.FlexEnd")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexEnd,
                align = FlexAlign.Center,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexJustify.Center")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.Center,
                align = FlexAlign.Center,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexJustify.SpaceBetween")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.SpaceBetween,
                align = FlexAlign.Center,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexJustify.SpaceAround")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.SpaceAround,
                align = FlexAlign.Center,
                gap = 0.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexJustify.SpaceEvenly")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.SpaceEvenly,
                align = FlexAlign.Center,
                gap = 0.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }
        }

        Text("All align variants:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("FlexAlign.FlexStart")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexStart,
                align = FlexAlign.FlexStart,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexAlign.FlexEnd")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexStart,
                align = FlexAlign.FlexEnd,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexAlign.Center")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexStart,
                align = FlexAlign.Center,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }

            Text("FlexAlign.Stretch")
            AntFlex(
                direction = FlexDirection.Row,
                wrap = FlexWrap.NoWrap,
                justify = FlexJustify.FlexStart,
                align = FlexAlign.Stretch,
                gap = gap.dp
            ) {
                Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                    Text(
                        "1",
                        color = Color.White
                    )
                }
                Box(Modifier.height(60.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                    Text(
                        "2",
                        color = Color.White
                    )
                }
                Box(Modifier.height(80.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                    Text(
                        "3",
                        color = Color.White
                    )
                }
            }
        }

        Text("Direction variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Row")
                AntFlex(
                    direction = FlexDirection.Row,
                    justify = FlexJustify.FlexStart,
                    align = FlexAlign.Center,
                    gap = 8.dp
                ) {
                    Box(Modifier.size(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                        Text(
                            "1",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Box(Modifier.size(40.dp).background(Color(0xFF52C41A)).padding(4.dp)) {
                        Text(
                            "2",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Box(Modifier.size(40.dp).background(Color(0xFFFAAD14)).padding(4.dp)) {
                        Text(
                            "3",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Column")
                AntFlex(
                    direction = FlexDirection.Column,
                    justify = FlexJustify.FlexStart,
                    align = FlexAlign.FlexStart,
                    gap = 8.dp
                ) {
                    Box(Modifier.size(40.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                        Text(
                            "1",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Box(Modifier.size(40.dp).background(Color(0xFF52C41A)).padding(4.dp)) {
                        Text(
                            "2",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Box(Modifier.size(40.dp).background(Color(0xFFFAAD14)).padding(4.dp)) {
                        Text(
                            "3",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Text("Wrap variants:")
        AntFlex(
            direction = FlexDirection.Row,
            wrap = FlexWrap.Wrap,
            justify = FlexJustify.FlexStart,
            align = FlexAlign.FlexStart,
            gap = 8.dp
        ) {
            repeat(10) { index ->
                Box(Modifier.size(60.dp).background(Color(0xFF1890FF)).padding(4.dp)) {
                    Text("${index + 1}", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Text("Vertical parameter:")
        AntFlex(
            direction = FlexDirection.Row,
            wrap = FlexWrap.NoWrap,
            justify = FlexJustify.SpaceBetween,
            align = FlexAlign.Center,
            gap = gap.dp,
            vertical = true
        ) {
            Box(Modifier.height(40.dp).background(Color(0xFF1890FF)).padding(8.dp)) {
                Text(
                    "Item 1",
                    color = Color.White
                )
            }
            Box(Modifier.height(40.dp).background(Color(0xFF52C41A)).padding(8.dp)) {
                Text(
                    "Item 2",
                    color = Color.White
                )
            }
            Box(Modifier.height(40.dp).background(Color(0xFFFAAD14)).padding(8.dp)) {
                Text(
                    "Item 3",
                    color = Color.White
                )
            }
        }
    }
}

val Center by story {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .background(Color(0xFFF0F2F5))
        ) {
            AntCenter {
                Text("Centered Content")
            }
        }
    }
}
