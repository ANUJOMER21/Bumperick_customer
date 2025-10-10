package com.bumperpick.bumperickUser.Animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset

/**
 * Screen transition animations for navigation between different screens.
 * Provides smooth, professional transitions that follow Material 3 guidelines.
 */

/**
 * Creates a slide transition from right to left (forward navigation)
 */
fun slideInFromRight() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_TRANSITION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun slideOutToLeft() = slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a slide transition from left to right (back navigation)
 */
fun slideInFromLeft() = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_TRANSITION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun slideOutToRight() = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a slide transition from bottom to top (modal presentation)
 */
fun slideInFromBottom() = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_TRANSITION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun slideOutToBottom() = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a slide transition from top to bottom (dismissal)
 */
fun slideInFromTop() = slideInVertically(
    initialOffsetY = { -it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_TRANSITION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun slideOutToTop() = slideOutVertically(
    targetOffsetY = { -it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a fade transition
 */
fun fadeInTransition() = fadeIn(
    animationSpec = AnimationConstants.SCREEN_ENTER_SPEC
)

fun fadeOutTransition() = fadeOut(
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
)

/**
 * Creates a scale transition
 */
fun scaleInTransition() = scaleIn(
    initialScale = 0.8f,
    animationSpec = AnimationConstants.SCREEN_ENTER_SPEC
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun scaleOutTransition() = scaleOut(
    targetScale = 1.1f,
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a combined slide and scale transition
 */
fun slideScaleInTransition() = slideInHorizontally(
    initialOffsetX = { it / 2 },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_TRANSITION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + scaleIn(
    initialScale = 0.9f,
    animationSpec = AnimationConstants.SCREEN_ENTER_SPEC
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun slideScaleOutTransition() = slideOutHorizontally(
    targetOffsetX = { -it / 2 },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + scaleOut(
    targetScale = 1.1f,
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a smooth transition for tab changes
 */
fun tabTransition() = slideInHorizontally(
    initialOffsetX = { it / 3 },
    animationSpec = tween(
        durationMillis = AnimationConstants.DURATION_FAST,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.FAST_TWEEN)

/**
 * Creates a smooth transition for modal presentations
 */
fun modalSlideIn() = slideInVertically(
    initialOffsetY = { it },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun modalSlideOut() = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a smooth transition for bottom sheet presentations
 */
fun bottomSheetSlideIn() = slideInVertically(
    initialOffsetY = { it },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun bottomSheetSlideOut() = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(
        durationMillis = AnimationConstants.SCREEN_EXIT_DURATION,
        easing = AnimationConstants.EASE_IN
    )
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a smooth transition for dialog presentations
 */
fun dialogSlideIn() = scaleIn(
    initialScale = 0.8f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun dialogSlideOut() = scaleOut(
    targetScale = 0.8f,
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)

/**
 * Creates a smooth transition for list item animations
 */
fun listItemSlideIn() = slideInVertically(
    initialOffsetY = { 50 },
    animationSpec = tween(
        durationMillis = AnimationConstants.LIST_ITEM_ANIMATION_DURATION,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeIn(animationSpec = AnimationConstants.LIST_ITEM_SPEC)

fun listItemSlideOut() = slideOutVertically(
    targetOffsetY = { -50 },
    animationSpec = tween(
        durationMillis = AnimationConstants.DURATION_FAST,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeOut(animationSpec = AnimationConstants.FAST_TWEEN)

/**
 * Creates a smooth transition for search bar animations
 */
fun searchBarSlideIn() = slideInVertically(
    initialOffsetY = { -50 },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )
) + fadeIn(animationSpec = AnimationConstants.FAST_TWEEN)

fun searchBarSlideOut() = slideOutVertically(
    targetOffsetY = { -50 },
    animationSpec = tween(
        durationMillis = AnimationConstants.DURATION_FAST,
        easing = AnimationConstants.EASE_OUT
    )
) + fadeOut(animationSpec = AnimationConstants.FAST_TWEEN)

/**
 * Creates a smooth transition for loading states
 */
fun loadingFadeIn() = fadeIn(
    animationSpec = AnimationConstants.SCREEN_ENTER_SPEC
)

fun loadingFadeOut() = fadeOut(
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
)

/**
 * Creates a smooth transition for error states
 */
fun errorShakeIn() = slideInHorizontally(
    initialOffsetX = { -20 },
    animationSpec = tween(
        durationMillis = 100,
        easing = LinearEasing
    )
) + fadeIn(animationSpec = AnimationConstants.FAST_TWEEN)

fun errorShakeOut() = slideOutHorizontally(
    targetOffsetX = { 20 },
    animationSpec = tween(
        durationMillis = 100,
        easing = LinearEasing
    )
) + fadeOut(animationSpec = AnimationConstants.FAST_TWEEN)

/**
 * Creates a smooth transition for success states
 */
fun successScaleIn() = scaleIn(
    initialScale = 0.5f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessHigh
    )
) + fadeIn(animationSpec = AnimationConstants.SCREEN_ENTER_SPEC)

fun successScaleOut() = scaleOut(
    targetScale = 1.2f,
    animationSpec = AnimationConstants.SCREEN_EXIT_SPEC
) + fadeOut(animationSpec = AnimationConstants.SCREEN_EXIT_SPEC)
