package com.bumperpick.bumperickUser.Animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Animated UI components that provide smooth, delightful interactions.
 * These components follow Material 3 design guidelines and provide consistent animations.
 */

/**
 * Animated button with press feedback and loading states
 */
@Composable
fun AnimatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: RoundedCornerShape = RoundedCornerShape(16.dp)
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .animateScale(isPressed = isPressed)
            .clickable(
                enabled = enabled && !loading,
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        colors = colors,
        shape = shape
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100.milliseconds)
            isPressed = false
        }
    }
}

/**
 * Animated card with hover effects and press feedback
 */
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .animateScale(isPressed = isPressed)
            .animateElevation(isHovered = isHovered)
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        colors = colors,
        elevation = elevation,
        shape = shape
    ) {
        Column(content = content)
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100.milliseconds)
            isPressed = false
        }
    }
}

/**
 * Animated list item with staggered entrance
 */
@Composable
fun AnimatedListItem(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay((index * AnimationConstants.STAGGER_DELAY).milliseconds)
        isVisible = true
    }
    
    Box(
        modifier = modifier
            .animateStaggered(index = index, isVisible = isVisible)
    ) {
        content()
    }
}

/**
 * Animated floating action button with scale and rotation effects
 */
@Composable
fun AnimatedFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    contentDescription: String? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        isVisible = true
    }
    
    FloatingActionButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .animateScale(isPressed = isPressed)
            .animateFadeTransition(isVisible = isVisible)
            .animateRotation(targetRotation = if (isPressed) 45f else 0f),
        content = { icon() }
    )
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200.milliseconds)
            isPressed = false
        }
    }
}

/**
 * Animated search bar with slide and fade effects
 */
@Composable
fun AnimatedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true
) {
    var isVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50.milliseconds)
        isVisible = true
    }
    
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .animateSlide(
                direction = AnimationDirection.TOP_TO_BOTTOM,
                isVisible = isVisible
            )
            .animateElevation(isHovered = isFocused),
        placeholder = { Text(placeholder) },
        enabled = enabled,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * Animated loading indicator with pulse effect
 */
@Composable
fun AnimatedLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    var isPulsing by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        while (isPulsing) {
            delay(1000)
        }
    }
    
    CircularProgressIndicator(
        modifier = modifier
            .size(size)
            .animatePulse(isPulsing = isPulsing),
        color = color,
        strokeWidth = 2.dp
    )
}

/**
 * Animated success indicator with bounce effect
 */
@Composable
fun AnimatedSuccessIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    var isBouncing by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isBouncing = true
        delay(500.milliseconds)
        isBouncing = false
    }
    
    Box(
        modifier = modifier
            .size(size)
            .animateBounce(isBouncing = isBouncing),
        contentAlignment = Alignment.Center
    ) {
        // Success icon or checkmark
        Text(
            text = "✓",
            color = color,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Animated error indicator with shake effect
 */
@Composable
fun AnimatedErrorIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    var isShaking by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isShaking = true
        delay(300)
        isShaking = false
    }
    
    Box(
        modifier = modifier
            .size(size)
            .animateTranslation(
                offsetX = if (isShaking) 10f else 0f,
                animationSpec = tween(
                    durationMillis = 50,
                    easing = LinearEasing
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Error icon or X
        Text(
            text = "✗",
            color = color,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Animated shimmer effect for loading states
 */
@Composable
fun AnimatedShimmer(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    var isShimmering by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        while (isShimmering) {
            delay(1000)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(shape)
            .background(color)
            .animateShimmer(isShimmering = isShimmering)
    )
}

/**
 * Animated bottom navigation item
 */
@Composable
fun AnimatedBottomNavigationItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Simple animated button for navigation
    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .animateScale(isPressed = isPressed)
            .animateRotation(targetRotation = if (selected) 360f else 0f)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Text(label)
        }
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100.milliseconds)
            isPressed = false
        }
    }
}

/**
 * Animated dialog with scale and fade effects
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50.milliseconds)
        isVisible = true
    }
    
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .animateScaleTransition(isVisible = isVisible)
            .animateFadeTransition(isVisible = isVisible),
        content = content
    )
}

/**
 * Animated bottom sheet with slide effects
 */
@Composable
fun AnimatedBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = bottomSheetSlideIn(),
        exit = bottomSheetSlideOut(),
        modifier = modifier
    ) {
        content()
    }
}
