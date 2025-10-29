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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

val Collapse by story {
    val accordion by parameter(false)
    val bordered by parameter(true)

    val panels = remember {
        listOf(
            CollapseItemType(
                key = "1",
                label = { Text("Panel 1") },
                children = { Text("Content of panel 1") }
            ),
            CollapseItemType(
                key = "2",
                label = { Text("Panel 2") },
                children = { Text("Content of panel 2") }
            ),
            CollapseItemType(
                key = "3",
                label = { Text("Panel 3") },
                children = { Text("Content of panel 3") },
                disabled = true
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Default collapse (multiple panels can be open):")
        AntCollapse(
            items = panels,
            defaultActiveKey = listOf("1"),
            accordion = accordion,
            bordered = bordered
        )

        Text("Accordion mode (only one panel can be open at a time):")
        AntCollapse(
            items = panels,
            defaultActiveKey = listOf("1"),
            accordion = true,
            bordered = bordered
        )

        Text("Borderless collapse:")
        AntCollapse(
            items = panels,
            defaultActiveKey = listOf("1"),
            accordion = false,
            bordered = false
        )

        Text("Collapse with extra content:")
        AntCollapse(
            items = listOf(
                CollapseItemType(
                    key = "1",
                    label = { Text("Panel with extra") },
                    children = { Text("This panel has extra content in the header") },
                    extra = { Text("Extra", color = Color(0xFF1890FF)) }
                ),
                CollapseItemType(
                    key = "2",
                    label = { Text("Another panel") },
                    children = { Text("Content here") }
                )
            ),
            defaultActiveKey = listOf("1"),
            accordion = false,
            bordered = bordered
        )
    }
}

val Carousel by story {
    val autoplay by parameter(false)
    val autoplaySpeed by parameter(3000)
    val dots by parameter(true)
    val infinite by parameter(true)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntCarousel(
            children = listOf(
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF1890FF))
                    ) {
                        Text("Slide 1", color = Color.White, modifier = Modifier.padding(16.dp))
                    }
                },
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF52C41A))
                    ) {
                        Text("Slide 2", color = Color.White, modifier = Modifier.padding(16.dp))
                    }
                },
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFFAAD14))
                    ) {
                        Text("Slide 3", color = Color.White, modifier = Modifier.padding(16.dp))
                    }
                }
            ),
            autoplay = autoplay,
            autoplaySpeed = autoplaySpeed,
            dots = dots,
            dotPosition = DotPosition.Bottom,
            effect = CarouselEffect.ScrollX,
            infinite = infinite
        )
    }
}

val Segmented by story {
    var value by remember { mutableStateOf("Daily") }
    val disabled by parameter(false)

    val options = remember { listOf("Daily", "Weekly", "Monthly", "Yearly") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntSegmentedString(
            options = options,
            value = value,
            onValueChange = { value = it },
            disabled = disabled
        )
    }
}

val Timeline by story {
    val reverse by parameter(false)
    val showPending by parameter(false)

    val items = remember {
        listOf(
            TimelineItemType(
                children = { Text("Create a services site 2015-09-01") },
                color = Color(0xFF52C41A)
            ),
            TimelineItemType(
                children = { Text("Solve initial network problems 2015-09-01") },
                color = Color(0xFF52C41A)
            ),
            TimelineItemType(
                children = { Text("Technical testing 2015-09-01") },
                color = Color(0xFF1890FF)
            ),
            TimelineItemType(
                children = { Text("Network problems being solved 2015-09-01") },
                color = Color(0xFFFF4D4F)
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Left mode (default):")
        AntTimeline(
            items = items,
            mode = TimelineMode.Left,
            pending = if (showPending) "Recording..." else null,
            reverse = reverse
        )

        Text("Right mode:")
        AntTimeline(
            items = items.take(3),
            mode = TimelineMode.Right,
            pending = null,
            reverse = false
        )

        Text("Alternate mode:")
        AntTimeline(
            items = items,
            mode = TimelineMode.Alternate,
            pending = null,
            reverse = false
        )

        Text("With pending item:")
        AntTimeline(
            items = items.take(3),
            mode = TimelineMode.Left,
            pending = "Recording...",
            pendingDot = { Text("⏱", style = TextStyle(color = Color.Gray)) },
            reverse = false
        )

        Text("Reversed timeline:")
        AntTimeline(
            items = items,
            mode = TimelineMode.Left,
            pending = null,
            reverse = true
        )
    }
}

val Descriptions by story {
    val bordered by parameter(false)
    val column by parameter(3)
    val colon by parameter(true)

    val items = remember {
        listOf(
            DescriptionItem(
                label = "Name",
                contentComposable = { Text("John Brown") }
            ),
            DescriptionItem(
                label = "Telephone",
                contentComposable = { Text("1810000000") }
            ),
            DescriptionItem(
                label = "Live",
                contentComposable = { Text("Hangzhou, Zhejiang") }
            ),
            DescriptionItem(
                label = "Remark",
                contentComposable = { Text("empty") }
            ),
            DescriptionItem(
                label = "Address",
                contentComposable = { Text("No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China") },
                span = DescriptionItemSpan.Fixed(2)
            )
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntDescriptions(
            title = "User Info",
            items = items,
            bordered = bordered,
            column = column,
            size = DescriptionsSize.Default,
            layout = DescriptionsLayout.Horizontal,
            colon = colon
        )
    }
}

val Statistic by story {
    val title by parameter("Active Users")
    val value by parameter(112893)
    val prefix by parameter("")
    val suffix by parameter("")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AntStatistic(
            title = if (title.isNotEmpty()) {
                { Text(title) }
            } else null,
            value = value,
            prefix = if (prefix.isNotEmpty()) {
                { Text(prefix) }
            } else null,
            suffix = if (suffix.isNotEmpty()) {
                { Text(suffix) }
            } else null
        )
    }
}

val StatisticWithTrend by story {
    val value by parameter(11.28)
    val trendValue by parameter(5.27)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            AntStatisticWithTrend(
                title = "Revenue",
                value = value,
                precision = 2,
                suffix = "M",
                trend = TrendDirection.Up,
                trendValue = trendValue
            )

            AntStatisticWithTrend(
                title = "Expenses",
                value = value,
                precision = 2,
                suffix = "M",
                trend = TrendDirection.Down,
                trendValue = trendValue
            )
        }
    }
}

val Dropdown by story {
    val disabled by parameter(false)

    val menuItems = remember {
        listOf(
            MenuItem(key = "1", label = "1st menu item"),
            MenuItem(key = "2", label = "2nd menu item"),
            MenuItem(key = "3", label = "3rd menu item", disabled = true),
            MenuItem(key = "4", label = "4th menu item")
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Trigger types:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hover")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = { /* handle selection */ },
                            items = menuItems
                        )
                    },
                    trigger = DropdownTrigger.Hover,
                    placement = DropdownPlacement.BottomLeft,
                    disabled = disabled
                ) {
                    AntButton(onClick = {}) {
                        Text("Hover me")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Click")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = { /* handle selection */ },
                            items = menuItems
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.BottomLeft,
                    disabled = disabled
                ) {
                    AntButton(onClick = {}) {
                        Text("Click me")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Context Menu")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = { /* handle selection */ },
                            items = menuItems
                        )
                    },
                    trigger = DropdownTrigger.ContextMenu,
                    placement = DropdownPlacement.BottomLeft,
                    disabled = disabled
                ) {
                    AntButton(onClick = {}) {
                        Text("Right click")
                    }
                }
            }
        }

        Text("Placement variants:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("BottomLeft")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.BottomLeft
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("BL")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("BottomCenter")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.BottomCenter
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("BC")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("BottomRight")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.BottomRight
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("BR")
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("TopLeft")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.TopLeft
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("TL")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("TopCenter")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.TopCenter
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("TC")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("TopRight")
                AntDropdown(
                    menu = {
                        AntMenu(
                            selectedKeys = emptyList(),
                            onSelect = {},
                            items = menuItems.take(2)
                        )
                    },
                    trigger = DropdownTrigger.Click,
                    placement = DropdownPlacement.TopRight
                ) {
                    AntButton(onClick = {}, size = ButtonSize.Small) {
                        Text("TR")
                    }
                }
            }
        }
    }
}
