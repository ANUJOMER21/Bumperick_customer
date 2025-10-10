package com.bumperpick.bumperickUser.Animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Reusable animation utilities for consistent animations across the app.
 * Provides composable functions for common animation patterns.
 */

/**
 * Creates a smooth scale animation for button presses and card interactions
 */
@Composable
fun Modifier.animateScale(
    isPressed: Boolean = false,
    scale: Float = 0.95f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.BUTTON_PRESS_SPEC
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isPressed) scale else 1f,
        animationSpec = animationSpec,
        label = "scale_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Creates a smooth elevation animation for cards and buttons
 */
@Composable
fun Modifier.animateElevation(
    isHovered: Boolean = false,
    elevation: Float = 8f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.CARD_HOVER_SPEC
): Modifier {
    val elevationValue by animateFloatAsState(
        targetValue = if (isHovered) elevation else 0f,
        animationSpec = animationSpec,
        label = "elevation_animation"
    )
    
    return this.graphicsLayer {
        shadowElevation = elevationValue
    }
}

/**
 * Creates a smooth alpha animation for fade effects
 */
@Composable
fun Modifier.animateAlpha(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_TWEEN
): Modifier {
    val alphaValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = animationSpec,
        label = "alpha_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
    }
}

/**
 * Creates a smooth rotation animation
 */
@Composable
fun Modifier.animateRotation(
    targetRotation: Float = 0f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_SPRING
): Modifier {
    val rotationValue by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = animationSpec,
        label = "rotation_animation"
    )
    
    return this.graphicsLayer {
        rotationZ = rotationValue
    }
}

/**
 * Creates a smooth translation animation for slide effects
 */
@Composable
fun Modifier.animateTranslation(
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_SPRING
): Modifier {
    val offsetXValue by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = animationSpec,
        label = "translation_x_animation"
    )
    
    val offsetYValue by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = animationSpec,
        label = "translation_y_animation"
    )
    
    return this.graphicsLayer {
        translationX = offsetXValue
        translationY = offsetYValue
    }
}

/**
 * Creates a staggered animation for list items
 */
@Composable
fun Modifier.animateStaggered(
    index: Int,
    isVisible: Boolean = true,
    staggerDelay: Int = AnimationConstants.STAGGER_DELAY,
    animationSpec: AnimationSpec<Float> = AnimationConstants.LIST_ITEM_SPEC
): Modifier {
    val delay = (index * staggerDelay).coerceAtMost(AnimationConstants.MAX_STAGGER_DELAY)
    
    val alphaValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = AnimationConstants.LIST_ITEM_ANIMATION_DURATION,
            delayMillis = delay,
            easing = AnimationConstants.EASE_OUT
        ),
        label = "staggered_alpha_animation"
    )
    
    val offsetYValue by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(
            durationMillis = AnimationConstants.LIST_ITEM_ANIMATION_DURATION,
            delayMillis = delay,
            easing = AnimationConstants.EASE_OUT
        ),
        label = "staggered_offset_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
        translationY = offsetYValue
    }
}

/**
 * Creates a smooth shimmer effect for loading states
 */
@Composable
fun Modifier.animateShimmer(
    isShimmering: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 1000,
        easing = LinearEasing
    )
): Modifier {
    val shimmerValue by animateFloatAsState(
        targetValue = if (isShimmering) 1f else 0f,
        animationSpec = animationSpec,
        label = "shimmer_animation"
    )
    
    return this.graphicsLayer {
        alpha = 0.3f + (shimmerValue * 0.7f)
    }
}

/**
 * Creates a smooth bounce animation for success states
 */
@Composable
fun Modifier.animateBounce(
    isBouncing: Boolean = false,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessHigh
    )
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isBouncing) 1.1f else 1f,
        animationSpec = animationSpec,
        label = "bounce_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Creates a smooth pulse animation for attention-grabbing elements
 */
@Composable
fun Modifier.animatePulse(
    isPulsing: Boolean = false,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 1000,
        easing = LinearEasing
    )
): Modifier {
    val pulseValue by animateFloatAsState(
        targetValue = if (isPulsing) 1f else 0f,
        animationSpec = animationSpec,
        label = "pulse_animation"
    )
    
    return this.graphicsLayer {
        alpha = 0.5f + (pulseValue * 0.5f)
    }
}

/**
 * Creates a smooth slide animation for screen transitions
 */
@Composable
fun Modifier.animateSlide(
    direction: AnimationDirection = AnimationDirection.LEFT_TO_RIGHT,
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.SCREEN_ENTER_SPEC
): Modifier {
    val offsetXValue by animateFloatAsState(
        targetValue = when (direction) {
            AnimationDirection.LEFT_TO_RIGHT -> if (isVisible) 0f else -300f
            AnimationDirection.RIGHT_TO_LEFT -> if (isVisible) 0f else 300f
            else -> 0f
        },
        animationSpec = animationSpec,
        label = "slide_x_animation"
    )
    
    val offsetYValue by animateFloatAsState(
        targetValue = when (direction) {
            AnimationDirection.TOP_TO_BOTTOM -> if (isVisible) 0f else -300f
            AnimationDirection.BOTTOM_TO_TOP -> if (isVisible) 0f else 300f
            AnimationDirection.SLIDE_UP -> if (isVisible) 0f else 300f
            AnimationDirection.SLIDE_DOWN -> if (isVisible) 0f else -300f
            else -> 0f
        },
        animationSpec = animationSpec,
        label = "slide_y_animation"
    )
    
    return this.graphicsLayer {
        translationX = offsetXValue
        translationY = offsetYValue
    }
}

/**
 * Creates a smooth scale animation for screen transitions
 */
@Composable
fun Modifier.animateScaleTransition(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.SCREEN_ENTER_SPEC
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = animationSpec,
        label = "scale_transition_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Creates a smooth fade animation for screen transitions
 */
@Composable
fun Modifier.animateFadeTransition(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.SCREEN_ENTER_SPEC
): Modifier {
    val alphaValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = animationSpec,
        label = "fade_transition_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
    }
}
