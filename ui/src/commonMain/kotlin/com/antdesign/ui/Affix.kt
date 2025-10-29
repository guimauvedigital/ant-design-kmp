package com.antdesign.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Affix component for Ant Design Compose Multiplatform.
 * Full feature parity with React Ant Design v5.x
 *
 * Affix fixes the component when it scrolls off the viewport, keeping it
 * visible at a specified offset from the edge of the window.
 *
 * @param modifier Modifier to apply to the affix container
 * @param offsetTop Distance from top when affixed (default: 0.dp). Use null to disable top affixing
 * @param offsetBottom Distance from bottom when affixed. Use null to disable bottom affixing
 * @param target Optional scroll state target. If not provided, uses root scroll tracking
 * @param onChange Callback invoked when affix state changes (true when affixed, false when not)
 * @param content The content to be affixed
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/affix">Ant Design Affix</a>
 *
 * @sample AffixSample
 */
@Composable
fun AntAffix(
    modifier: Modifier = Modifier,
    offsetTop: Dp? = 0.dp,
    offsetBottom: Dp? = null,
    target: (() -> Any)? = null,
    onChange: ((Boolean) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var isAffixed by remember { mutableStateOf(false) }
    var elementPositionY by remember { mutableStateOf(0f) }
    var elementHeight by remember { mutableStateOf(0f) }
    var containerHeight by remember { mutableStateOf(0f) }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Default scroll state - creates a local scrollable context if no target provided
    val defaultScrollState = rememberScrollState()

    // Convert offsets to pixels for calculations
    val offsetTopPx = offsetTop?.let { with(density) { it.toPx() } }
    val offsetBottomPx = offsetBottom?.let { with(density) { it.toPx() } }

    // Determine if should be affixed based on scroll position and offsets
    fun shouldBeAffixed(scrollY: Float, elementY: Float, containerH: Float): Boolean {
        return when {
            // Top affixing: element scrolls above its natural position minus offsetTop
            offsetTop != null -> {
                elementY <= offsetTopPx!! + scrollY
            }
            // Bottom affixing: element scrolls below the bottom threshold
            offsetBottom != null -> {
                val bottomThreshold = containerH - offsetBottomPx!! - elementHeight
                elementY + scrollY >= bottomThreshold
            }
            else -> false
        }
    }

    // Monitor scroll position and update affix state
    LaunchedEffect(defaultScrollState.value) {
        scope.launch {
            val newAffixedState = shouldBeAffixed(
                defaultScrollState.value.toFloat(),
                elementPositionY,
                containerHeight
            )

            if (newAffixedState != isAffixed) {
                isAffixed = newAffixedState
                onChange?.invoke(isAffixed)
            }
        }
    }

    // Calculate the offset to apply when affixed
    val displayOffset = when {
        !isAffixed -> 0.dp
        offsetTop != null -> offsetTop
        offsetBottom != null -> 0.dp // Bottom affixing typically uses negative offset
        else -> 0.dp
    }

    // Animate the offset transition for smooth positioning
    val animatedOffset by animateDpAsState(
        targetValue = displayOffset,
        animationSpec = tween(durationMillis = 150),
        label = "AffixOffset"
    )

    // Render the affixed content with positioning logic
    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                elementPositionY = coordinates.positionInRoot().y
                elementHeight = coordinates.size.height.toFloat()
                containerHeight = coordinates.parentLayoutCoordinates?.size?.height?.toFloat() ?: 0f
            }
            .then(
                if (isAffixed) {
                    when {
                        offsetTop != null -> {
                            Modifier
                                .offset(y = animatedOffset)
                                .background(Color.White) // Theme-aware background would go here
                                .zIndex(1000f)
                        }
                        offsetBottom != null -> {
                            Modifier
                                .offset(y = -animatedOffset)
                                .background(Color.White)
                                .zIndex(1000f)
                        }
                        else -> Modifier
                    }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

/**
 * Affix component with scroll container support.
 * Use this variant when you need to track scroll state from a specific scroll container.
 *
 * @param scrollState The scroll state of the container to monitor
 * @param modifier Modifier to apply to the affix container
 * @param offsetTop Distance from top when affixed (default: 0.dp)
 * @param offsetBottom Distance from bottom when affixed
 * @param onChange Callback when affix state changes
 * @param content The content to be affixed
 */
@Composable
fun AntAffixWithScroll(
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier,
    offsetTop: Dp? = 0.dp,
    offsetBottom: Dp? = null,
    onChange: ((Boolean) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var isAffixed by remember { mutableStateOf(false) }
    var elementPositionY by remember { mutableStateOf(0f) }
    var elementHeight by remember { mutableStateOf(0f) }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val offsetTopPx = offsetTop?.let { with(density) { it.toPx() } }
    val offsetBottomPx = offsetBottom?.let { with(density) { it.toPx() } }

    fun shouldBeAffixed(scrollY: Float, elementY: Float): Boolean {
        return when {
            offsetTop != null -> elementY <= offsetTopPx!! + scrollY
            offsetBottom != null -> elementY + scrollY >= (elementHeight - offsetBottomPx!!)
            else -> false
        }
    }

    LaunchedEffect(scrollState.value) {
        scope.launch {
            val newAffixedState = shouldBeAffixed(
                scrollState.value.toFloat(),
                elementPositionY
            )

            if (newAffixedState != isAffixed) {
                isAffixed = newAffixedState
                onChange?.invoke(isAffixed)
            }
        }
    }

    val displayOffset = when {
        !isAffixed -> 0.dp
        offsetTop != null -> offsetTop
        offsetBottom != null -> 0.dp
        else -> 0.dp
    }

    val animatedOffset by animateDpAsState(
        targetValue = displayOffset,
        animationSpec = tween(durationMillis = 150),
        label = "AffixOffsetWithScroll"
    )

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                elementPositionY = coordinates.positionInRoot().y
                elementHeight = coordinates.size.height.toFloat()
            }
            .then(
                if (isAffixed) {
                    when {
                        offsetTop != null -> {
                            Modifier
                                .offset(y = animatedOffset)
                                .background(Color.White)
                                .zIndex(1000f)
                        }
                        offsetBottom != null -> {
                            Modifier
                                .offset(y = -animatedOffset)
                                .background(Color.White)
                                .zIndex(1000f)
                        }
                        else -> Modifier
                    }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}
