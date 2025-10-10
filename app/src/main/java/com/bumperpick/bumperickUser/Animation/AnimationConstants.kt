package com.bumperpick.bumperickUser.Animation

import androidx.compose.animation.core.*
import androidx.compose.ui.unit.dp

/**
 * Animation constants and utilities for consistent animations across the app.
 * Following Material 3 motion guidelines for natural and user-friendly experience.
 */
object AnimationConstants {
    
    // Duration constants (in milliseconds)
    const val DURATION_FAST = 150
    const val DURATION_NORMAL = 300
    const val DURATION_SLOW = 500
    const val DURATION_EXTRA_SLOW = 800
    
    // Screen transition durations
    const val SCREEN_TRANSITION_DURATION = 300
    const val SCREEN_EXIT_DURATION = 200
    
    // Micro-animation durations
    const val BUTTON_PRESS_DURATION = 100
    const val CARD_HOVER_DURATION = 200
    const val LIST_ITEM_ANIMATION_DURATION = 250
    const val FAB_ANIMATION_DURATION = 200
    
    // Easing curves following Material 3 guidelines
    val EASE_IN_OUT = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val EASE_OUT = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val EASE_IN = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
    val SHARP_EASE_OUT = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
    
    // Spring animations for natural feel
    val SPRING_DAMPING_RATIO = Spring.DampingRatioMediumBouncy
    val SPRING_STIFFNESS = Spring.StiffnessMedium
    
    // Animation specs for different use cases
    val FAST_TWEEN = tween<Float>(DURATION_FAST, easing = EASE_OUT)
    val NORMAL_TWEEN = tween<Float>(DURATION_NORMAL, easing = EASE_IN_OUT)
    val SLOW_TWEEN = tween<Float>(DURATION_SLOW, easing = EASE_IN_OUT)
    
    val FAST_SPRING = spring<Float>(
        dampingRatio = SPRING_DAMPING_RATIO,
        stiffness = Spring.StiffnessHigh
    )
    
    val NORMAL_SPRING = spring<Float>(
        dampingRatio = SPRING_DAMPING_RATIO,
        stiffness = SPRING_STIFFNESS
    )
    
    val SLOW_SPRING = spring<Float>(
        dampingRatio = SPRING_DAMPING_RATIO,
        stiffness = Spring.StiffnessLow
    )
    
    // Screen transition specs
    val SCREEN_ENTER_SPEC = tween<Float>(SCREEN_TRANSITION_DURATION, easing = EASE_OUT)
    val SCREEN_EXIT_SPEC = tween<Float>(SCREEN_EXIT_DURATION, easing = EASE_IN)
    
    // Micro-animation specs
    val BUTTON_PRESS_SPEC = tween<Float>(BUTTON_PRESS_DURATION, easing = EASE_OUT)
    val CARD_HOVER_SPEC = tween<Float>(CARD_HOVER_DURATION, easing = EASE_IN_OUT)
    val LIST_ITEM_SPEC = tween<Float>(LIST_ITEM_ANIMATION_DURATION, easing = EASE_OUT)
    val FAB_SPEC = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    // Stagger delays for list animations
    const val STAGGER_DELAY = 50
    const val MAX_STAGGER_DELAY = 300
}

/**
 * Animation types for different UI elements
 */
enum class AnimationType {
    SCREEN_TRANSITION,
    BUTTON_PRESS,
    CARD_HOVER,
    LIST_ITEM,
    FAB,
    DIALOG,
    BOTTOM_SHEET,
    SEARCH_BAR,
    LOADING,
    SUCCESS,
    ERROR
}

/**
 * Animation directions for screen transitions
 */
enum class AnimationDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP,
    FADE,
    SCALE,
    SLIDE_UP,
    SLIDE_DOWN
}
