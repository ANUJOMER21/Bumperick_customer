package com.bumperpick.bumperickUser.Animation

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * Performance optimization utilities for animations.
 * Ensures 60fps performance and prevents frame drops.
 */

/**
 * Optimized animation modifier that only applies animations when necessary
 */
@Composable
fun Modifier.optimizedAnimateScale(
    isPressed: Boolean = false,
    scale: Float = 0.95f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.BUTTON_PRESS_SPEC
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isPressed) scale else 1f,
        animationSpec = animationSpec,
        label = "optimized_scale_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Optimized animation modifier for elevation changes
 */
@Composable
fun Modifier.optimizedAnimateElevation(
    isHovered: Boolean = false,
    elevation: Float = 8f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.CARD_HOVER_SPEC
): Modifier {
    val elevationValue by animateFloatAsState(
        targetValue = if (isHovered) elevation else 0f,
        animationSpec = animationSpec,
        label = "optimized_elevation_animation"
    )
    
    return this.graphicsLayer {
        shadowElevation = elevationValue
    }
}

/**
 * Optimized animation modifier for alpha changes
 */
@Composable
fun Modifier.optimizedAnimateAlpha(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_TWEEN
): Modifier {
    val alphaValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = animationSpec,
        label = "optimized_alpha_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
    }
}

/**
 * Optimized animation modifier for rotation changes
 */
@Composable
fun Modifier.optimizedAnimateRotation(
    targetRotation: Float = 0f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_SPRING
): Modifier {
    val rotationValue by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = animationSpec,
        label = "optimized_rotation_animation"
    )
    
    return this.graphicsLayer {
        rotationZ = rotationValue
    }
}

/**
 * Optimized animation modifier for translation changes
 */
@Composable
fun Modifier.optimizedAnimateTranslation(
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    animationSpec: AnimationSpec<Float> = AnimationConstants.NORMAL_SPRING
): Modifier {
    val offsetXValue by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = animationSpec,
        label = "optimized_translation_x_animation"
    )
    
    val offsetYValue by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = animationSpec,
        label = "optimized_translation_y_animation"
    )
    
    return this.graphicsLayer {
        translationX = offsetXValue
        translationY = offsetYValue
    }
}

/**
 * Optimized staggered animation for list items with performance considerations
 */
@Composable
fun Modifier.optimizedAnimateStaggered(
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
        label = "optimized_staggered_alpha_animation"
    )
    
    val offsetYValue by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(
            durationMillis = AnimationConstants.LIST_ITEM_ANIMATION_DURATION,
            delayMillis = delay,
            easing = AnimationConstants.EASE_OUT
        ),
        label = "optimized_staggered_offset_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
        translationY = offsetYValue
    }
}

/**
 * Performance-optimized shimmer effect
 */
@Composable
fun Modifier.optimizedAnimateShimmer(
    isShimmering: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 1000,
        easing = LinearEasing
    )
): Modifier {
    val shimmerValue by animateFloatAsState(
        targetValue = if (isShimmering) 1f else 0f,
        animationSpec = animationSpec,
        label = "optimized_shimmer_animation"
    )
    
    return this.graphicsLayer {
        alpha = 0.3f + (shimmerValue * 0.7f)
    }
}

/**
 * Performance-optimized bounce animation
 */
@Composable
fun Modifier.optimizedAnimateBounce(
    isBouncing: Boolean = false,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessHigh
    )
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isBouncing) 1.1f else 1f,
        animationSpec = animationSpec,
        label = "optimized_bounce_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Performance-optimized pulse animation
 */
@Composable
fun Modifier.optimizedAnimatePulse(
    isPulsing: Boolean = false,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 1000,
        easing = LinearEasing
    )
): Modifier {
    val pulseValue by animateFloatAsState(
        targetValue = if (isPulsing) 1f else 0f,
        animationSpec = animationSpec,
        label = "optimized_pulse_animation"
    )
    
    return this.graphicsLayer {
        alpha = 0.5f + (pulseValue * 0.5f)
    }
}

/**
 * Performance-optimized slide animation
 */
@Composable
fun Modifier.optimizedAnimateSlide(
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
        label = "optimized_slide_x_animation"
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
        label = "optimized_slide_y_animation"
    )
    
    return this.graphicsLayer {
        translationX = offsetXValue
        translationY = offsetYValue
    }
}

/**
 * Performance-optimized scale transition
 */
@Composable
fun Modifier.optimizedAnimateScaleTransition(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.SCREEN_ENTER_SPEC
): Modifier {
    val scaleValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = animationSpec,
        label = "optimized_scale_transition_animation"
    )
    
    return this.graphicsLayer {
        scaleX = scaleValue
        scaleY = scaleValue
    }
}

/**
 * Performance-optimized fade transition
 */
@Composable
fun Modifier.optimizedAnimateFadeTransition(
    isVisible: Boolean = true,
    animationSpec: AnimationSpec<Float> = AnimationConstants.SCREEN_ENTER_SPEC
): Modifier {
    val alphaValue by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = animationSpec,
        label = "optimized_fade_transition_animation"
    )
    
    return this.graphicsLayer {
        alpha = alphaValue
    }
}

/**
 * Performance optimization guidelines:
 * 
 * 1. Use optimized animation modifiers for better performance
 * 2. Avoid animating too many properties simultaneously
 * 3. Use appropriate animation specs (tween vs spring)
 * 4. Consider using remember for expensive calculations
 * 5. Use LaunchedEffect for one-time animations
 * 6. Avoid nested animations
 * 7. Use graphicsLayer for hardware acceleration
 * 8. Consider using LazyColumn/LazyRow for large lists
 * 9. Use appropriate animation durations
 * 10. Test on lower-end devices
 */
