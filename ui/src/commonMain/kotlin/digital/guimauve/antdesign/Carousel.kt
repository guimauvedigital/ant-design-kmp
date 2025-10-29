package digital.guimauve.antdesign

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import digital.guimauve.antdesign.icons.ArrowDownIcon
import digital.guimauve.antdesign.icons.ArrowLeftIcon
import digital.guimauve.antdesign.icons.ArrowRightIcon
import digital.guimauve.antdesign.icons.ArrowUpIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Carousel effect types
 */
enum class CarouselEffect {
    ScrollX,
    Fade
}

/**
 * Dot position configuration
 */
enum class DotPosition {
    Top,
    Bottom,
    Left,
    Right
}

/**
 * Configuration for dots appearance
 */
data class DotsConfig(
    val className: String? = null,
    val style: Modifier = Modifier,
)

/**
 * Interface for controlling carousel externally
 */
interface CarouselRef {
    /**
     * Navigate to specific slide
     * @param slide Target slide index
     * @param dontAnimate If true, jump without animation
     */
    fun goTo(slide: Int, dontAnimate: Boolean = false)

    /**
     * Navigate to next slide
     */
    fun next()

    /**
     * Navigate to previous slide
     */
    fun prev()
}

/**
 * Internal implementation of CarouselRef
 */
private class CarouselRefImpl(
    private val pagerState: PagerState,
    private val itemCount: Int,
    private val infinite: Boolean,
    private val coroutineScope: CoroutineScope,
) : CarouselRef {
    override fun goTo(slide: Int, dontAnimate: Boolean) {
        coroutineScope.launch {
            val targetSlide = slide.coerceIn(0, itemCount - 1)
            if (dontAnimate) {
                pagerState.scrollToPage(targetSlide)
            } else {
                pagerState.animateScrollToPage(targetSlide)
            }
        }
    }

    override fun next() {
        coroutineScope.launch {
            val nextPage = if (infinite) {
                (pagerState.currentPage + 1) % itemCount
            } else {
                (pagerState.currentPage + 1).coerceAtMost(itemCount - 1)
            }
            pagerState.animateScrollToPage(nextPage)
        }
    }

    override fun prev() {
        coroutineScope.launch {
            val prevPage = if (infinite) {
                if (pagerState.currentPage == 0) itemCount - 1 else pagerState.currentPage - 1
            } else {
                (pagerState.currentPage - 1).coerceAtLeast(0)
            }
            pagerState.animateScrollToPage(prevPage)
        }
    }
}

/**
 * Ant Design Carousel component
 *
 * A carousel component for cycling through elements.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AntCarousel(
    modifier: Modifier = Modifier,
    afterChange: ((Int) -> Unit)? = null,
    autoplay: Boolean = false,
    autoplaySpeed: Int = 3000,
    beforeChange: ((Int, Int) -> Unit)? = null,
    dotPosition: DotPosition = DotPosition.Bottom,
    dots: Any = true,
    draggable: Boolean = false,
    easing: String = "linear",
    effect: CarouselEffect = CarouselEffect.ScrollX,
    fade: Boolean = false,
    infinite: Boolean = true,
    initialSlide: Int = 0,
    pauseOnDotsHover: Boolean = false,
    pauseOnFocus: Boolean = false,
    pauseOnHover: Boolean = false,
    speed: Int = 500,
    swipeToSlide: Boolean = false,
    touchMove: Boolean = true,
    vertical: Boolean = false,
    waitForAnimate: Boolean = false,
    arrows: Boolean = false,
    className: String? = null,
    style: Modifier = Modifier,
    children: List<@Composable () -> Unit>,
    carouselRef: ((CarouselRef) -> Unit)? = null,
) {
    // Handle deprecated props
    val actualEffect = if (fade) CarouselEffect.Fade else effect
    val actualDotPosition = if (vertical) {
        if (dotPosition == DotPosition.Bottom) DotPosition.Right else dotPosition
    } else dotPosition

    // Parse dots config
    val showDots = when (dots) {
        is Boolean -> dots
        is DotsConfig -> true
        else -> true
    }

    val dotsConfig = when (dots) {
        is DotsConfig -> dots
        else -> DotsConfig()
    }

    val itemCount = children.size
    val pagerState = rememberPagerState(
        initialPage = initialSlide.coerceIn(0, itemCount - 1),
        pageCount = { itemCount }
    )

    val coroutineScope = rememberCoroutineScope()

    // Setup carousel ref
    LaunchedEffect(Unit) {
        carouselRef?.invoke(
            CarouselRefImpl(pagerState, itemCount, infinite, coroutineScope)
        )
    }

    // Hover and focus states
    val mainInteractionSource = remember { MutableInteractionSource() }
    val isMainHovered by mainInteractionSource.collectIsHoveredAsState()
    val isMainFocused by mainInteractionSource.collectIsFocusedAsState()

    val dotsInteractionSource = remember { MutableInteractionSource() }
    val isDotsHovered by dotsInteractionSource.collectIsHoveredAsState()

    // Determine if autoplay should be paused
    val shouldPauseAutoplay = remember(isMainHovered, isMainFocused, isDotsHovered) {
        derivedStateOf {
            (pauseOnHover && isMainHovered) ||
                    (pauseOnFocus && isMainFocused) ||
                    (pauseOnDotsHover && isDotsHovered)
        }
    }

    // Track previous page for beforeChange callback
    var previousPage by remember { mutableStateOf(pagerState.currentPage) }

    // Call beforeChange and afterChange callbacks
    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        if (currentPage != previousPage) {
            beforeChange?.invoke(previousPage, currentPage)
        }
        previousPage = currentPage
    }

    LaunchedEffect(pagerState.settledPage) {
        afterChange?.invoke(pagerState.settledPage)
    }

    // Autoplay logic
    LaunchedEffect(autoplay, shouldPauseAutoplay.value, pagerState.settledPage) {
        if (autoplay && !shouldPauseAutoplay.value) {
            while (true) {
                delay(autoplaySpeed.toLong())
                if (!shouldPauseAutoplay.value && !pagerState.isScrollInProgress) {
                    val nextPage = if (infinite) {
                        (pagerState.currentPage + 1) % itemCount
                    } else {
                        val next = pagerState.currentPage + 1
                        if (next >= itemCount) break else next
                    }
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = speed,
                            easing = getEasing(easing)
                        )
                    )
                }
            }
        }
    }

    // Animation spec for transitions
    val animationSpec = tween<Float>(
        durationMillis = speed,
        easing = getEasing(easing)
    )

    val isVerticalCarousel = vertical || actualDotPosition == DotPosition.Left || actualDotPosition == DotPosition.Right

    Box(
        modifier = modifier
            .then(style)
            .hoverable(mainInteractionSource)
            .onFocusChanged { }
    ) {
        // Carousel content
        if (actualEffect == CarouselEffect.Fade) {
            // Fade effect using crossfade
            Box(modifier = Modifier.fillMaxSize()) {
                children.forEachIndexed { index, content ->
                    val alpha by animateFloatAsState(
                        targetValue = if (pagerState.currentPage == index) 1f else 0f,
                        animationSpec = animationSpec,
                        label = "fade_$index"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alpha)
                    ) {
                        if (alpha > 0f || pagerState.currentPage == index) {
                            content()
                        }
                    }
                }
            }
        } else {
            // ScrollX effect using pager
            val pagerModifier = Modifier.fillMaxSize().let { baseModifier ->
                if (draggable || touchMove) {
                    baseModifier.pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            if (touchMove) {
                                change.consume()
                            }
                        }
                    }
                } else {
                    baseModifier.draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { },
                        enabled = false
                    )
                }
            }

            if (isVerticalCarousel) {
                VerticalPager(
                    state = pagerState,
                    modifier = pagerModifier,
                    userScrollEnabled = touchMove || draggable
                ) { page ->
                    children[page]()
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = pagerModifier,
                    userScrollEnabled = touchMove || draggable
                ) { page ->
                    children[page]()
                }
            }
        }

        // Dots indicators
        if (showDots) {
            val dotsAlignment = when (actualDotPosition) {
                DotPosition.Bottom -> Alignment.BottomCenter
                DotPosition.Top -> Alignment.TopCenter
                DotPosition.Left -> Alignment.CenterStart
                DotPosition.Right -> Alignment.CenterEnd
            }

            val dotsArrangement = when (actualDotPosition) {
                DotPosition.Left, DotPosition.Right -> Arrangement.Center
                else -> Arrangement.Center
            }

            val isVerticalDots = actualDotPosition == DotPosition.Left || actualDotPosition == DotPosition.Right

            if (isVerticalDots) {
                Column(
                    modifier = Modifier
                        .align(dotsAlignment)
                        .padding(16.dp)
                        .hoverable(dotsInteractionSource)
                        .then(dotsConfig.style),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(itemCount) { index ->
                        DotIndicator(
                            isActive = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    if (waitForAnimate) {
                                        if (!pagerState.isScrollInProgress) {
                                            pagerState.animateScrollToPage(
                                                page = index,
                                                animationSpec = tween(
                                                    durationMillis = speed,
                                                    easing = getEasing(easing)
                                                )
                                            )
                                        }
                                    } else {
                                        pagerState.animateScrollToPage(
                                            page = index,
                                            animationSpec = tween(
                                                durationMillis = speed,
                                                easing = getEasing(easing)
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .align(dotsAlignment)
                        .padding(16.dp)
                        .hoverable(dotsInteractionSource)
                        .then(dotsConfig.style),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(itemCount) { index ->
                        DotIndicator(
                            isActive = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    if (waitForAnimate) {
                                        if (!pagerState.isScrollInProgress) {
                                            pagerState.animateScrollToPage(
                                                page = index,
                                                animationSpec = tween(
                                                    durationMillis = speed,
                                                    easing = getEasing(easing)
                                                )
                                            )
                                        }
                                    } else {
                                        pagerState.animateScrollToPage(
                                            page = index,
                                            animationSpec = tween(
                                                durationMillis = speed,
                                                easing = getEasing(easing)
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // Navigation arrows
        if (arrows) {
            if (isVerticalCarousel) {
                // Top arrow
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val prevPage = if (infinite) {
                                if (pagerState.currentPage == 0) itemCount - 1 else pagerState.currentPage - 1
                            } else {
                                (pagerState.currentPage - 1).coerceAtLeast(0)
                            }
                            pagerState.animateScrollToPage(
                                page = prevPage,
                                animationSpec = tween(
                                    durationMillis = speed,
                                    easing = getEasing(easing)
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    ArrowUpIcon(color = Color.White)
                }

                // Bottom arrow
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val nextPage = if (infinite) {
                                (pagerState.currentPage + 1) % itemCount
                            } else {
                                (pagerState.currentPage + 1).coerceAtMost(itemCount - 1)
                            }
                            pagerState.animateScrollToPage(
                                page = nextPage,
                                animationSpec = tween(
                                    durationMillis = speed,
                                    easing = getEasing(easing)
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    ArrowDownIcon(color = Color.White)
                }
            } else {
                // Left arrow
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val prevPage = if (infinite) {
                                if (pagerState.currentPage == 0) itemCount - 1 else pagerState.currentPage - 1
                            } else {
                                (pagerState.currentPage - 1).coerceAtLeast(0)
                            }
                            pagerState.animateScrollToPage(
                                page = prevPage,
                                animationSpec = tween(
                                    durationMillis = speed,
                                    easing = getEasing(easing)
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    ArrowLeftIcon(color = Color.White)
                }

                // Right arrow
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val nextPage = if (infinite) {
                                (pagerState.currentPage + 1) % itemCount
                            } else {
                                (pagerState.currentPage + 1).coerceAtMost(itemCount - 1)
                            }
                            pagerState.animateScrollToPage(
                                page = nextPage,
                                animationSpec = tween(
                                    durationMillis = speed,
                                    easing = getEasing(easing)
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    ArrowRightIcon(color = Color.White)
                }
            }
        }
    }
}

/**
 * Single dot indicator
 */
@Composable
private fun DotIndicator(
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(
                if (isActive) Color.White else Color.White.copy(alpha = 0.5f)
            )
            .clickable(onClick = onClick)
    )
}

/**
 * Get easing function from string
 */
private fun getEasing(easing: String): Easing {
    return when (easing.lowercase()) {
        "linear" -> LinearEasing
        "ease" -> FastOutSlowInEasing
        "ease-in" -> EaseIn
        "ease-out" -> EaseOut
        "ease-in-out" -> EaseInOut
        else -> LinearEasing
    }
}
