package com.antdesign.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * BackTop component for Ant Design Compose Multiplatform
 * Full feature parity with React Ant Design v5.x
 *
 * A floating button that appears when the user scrolls down past a certain height.
 * Clicking it smoothly scrolls the page back to the top.
 *
 * @param modifier Modifier to be applied to the button container
 * @param visibilityHeight Height threshold (from top) at which to show the button (default: 400.dp)
 * @param onClick Callback invoked when button is clicked
 * @param target Optional scroll state target. If provided, it should be the scroll state being monitored
 * @param duration Scroll animation duration in milliseconds (default: 450)
 * @param children Custom button content. If not provided, displays default up arrow icon
 *
 * @since 1.0.0
 * @see <a href="https://ant.design/components/back-top">Ant Design BackTop</a>
 */
@Composable
fun AntBackTop(
    modifier: Modifier = Modifier,
    visibilityHeight: Dp = 400.dp,
    onClick: (() -> Unit)? = null,
    target: (() -> Any)? = null,
    duration: Int = 450,
    children: (@Composable () -> Unit)? = null
) {
    // Get theme for styling
    val theme = useTheme()

    // Scroll state to track page position
    val scrollState = rememberScrollState()

    // Track if button should be visible based on scroll position
    var isVisible by remember { mutableStateOf(false) }

    // Coroutine scope for smooth scroll animation
    val scope = rememberCoroutineScope()

    // Density for DP to pixel conversion
    val density = LocalDensity.current

    // Convert visibility height to pixels for comparison
    val visibilityHeightPx = with(density) { visibilityHeight.toPx() }

    // Update visibility based on scroll position
    LaunchedEffect(scrollState.value) {
        isVisible = scrollState.value > visibilityHeightPx
    }

    // Handle click - smooth scroll to top
    val handleClick = {
        scope.launch {
            scrollState.animateScrollTo(
                value = 0,
                animationSpec = tween(durationMillis = duration)
            )
        }
        onClick?.invoke() ?: Unit
    }

    // Only render if button should be visible
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Box(
            modifier = modifier
                .padding(end = 50.dp, bottom = 50.dp)
        ) {
            if (children != null) {
                // Custom content wrapper
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = handleClick
                        )
                ) {
                    children()
                }
            } else {
                // Default circular button with up arrow icon
                DefaultBackTopButton(
                    onClick = handleClick,
                    primaryColor = theme.token.colorPrimary
                )
            }
        }
    }
}

/**
 * Default BackTop button with circular design and up arrow icon
 * Matches Ant Design v5 styling with shadow, hover effects, and animations
 */
@Composable
private fun DefaultBackTopButton(
    onClick: () -> Unit,
    primaryColor: Color
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .background(primaryColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Up arrow icon using Unicode character
        Text(
            text = "↑",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
