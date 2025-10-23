package com.bumperpick.bumperickUser.Screens.Component

import android.Manifest
import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// Data class for confetti particles
data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val size: Float,
    val rotation: Float
)


// Gradient types for overlay
sealed class OverlayType {
    data class SolidColor(val color: Color) : OverlayType()
    data class LinearGradient(
        val startColor: Color,
        val endColor: Color,
        val direction: GradientDirection = GradientDirection.TopToBottom
    ) : OverlayType()
    data class RadialGradient(
        val centerColor: Color,
        val edgeColor: Color,
        val radius: Float = 1.0f
    ) : OverlayType()
    data class MultiColorGradient(
        val colors: List<Color>,
        val direction: GradientDirection = GradientDirection.TopToBottom
    ) : OverlayType()
}

enum class GradientDirection {
    TopToBottom,
    BottomToTop,
    LeftToRight,
    RightToLeft,
    TopLeftToBottomRight,
    TopRightToBottomLeft,
    BottomLeftToTopRight,
    BottomRightToTopLeft
}

// Scratch patterns and effects
sealed class ScratchPattern {
    object Normal : ScratchPattern()
    object Sparkle : ScratchPattern()
    object Glow : ScratchPattern()
    object Metallic : ScratchPattern()
}

// Animation types for win celebration
sealed class WinAnimation {
    object Confetti : WinAnimation()
    object Fireworks : WinAnimation()
    object Sparkle : WinAnimation()
    object Bounce : WinAnimation()
}

class ScratchCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var overlayBitmap: Bitmap? = null
    private var overlayCanvas: Canvas? = null
    private val scratchPath = Path()
    private val sparklePath = Path()
    private val overlayPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 80f
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    
    private val sparklePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 40f
        color = android.graphics.Color.WHITE
        setShadowLayer(10f, 0f, 0f, android.graphics.Color.YELLOW)
    }

    private val basePaint = Paint()
    private var overlayType: OverlayType = OverlayType.SolidColor(Color.Black)
    private var scratchPattern: ScratchPattern = ScratchPattern.Normal
    private var revealThreshold = 0.3f
    private var onScratchComplete: (() -> Unit)? = null
    private var onScratchProgress: ((Float) -> Unit)? = null
    private var onScratchStart: (() -> Unit)? = null
    private var isRevealed = false
    private var isScratching = false
    private var scratchCount = 0

    private var lastX = 0f
    private var lastY = 0f
    private var totalScratchedArea = 0f
    private var lastProgress = 0f
    
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val hapticFeedback = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
    
    // Sound effects
    private var soundPool: SoundPool? = null
    private var scratchSoundId: Int = 0
    private var winSoundId: Int = 0
    
    init {
        setupSounds()
    }
    
    private fun setupSounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            
            soundPool = SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            @Suppress("DEPRECATION")
            soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        }
        
        // Note: You'll need to add sound files to res/raw/ folder
        // scratchSoundId = soundPool?.load(context, R.raw.scratch_sound, 1) ?: 0
        // winSoundId = soundPool?.load(context, R.raw.win_sound, 1) ?: 0
    }

    fun setOverlayColor(color: Int) {
        overlayType = OverlayType.SolidColor(Color(color))
        invalidate()
    }
    
    fun setOverlayType(type: OverlayType) {
        overlayType = type
        invalidate()
    }
    
    fun setScratchPattern(pattern: ScratchPattern) {
        scratchPattern = pattern
    }

    fun setRevealThreshold(threshold: Float) {
        revealThreshold = threshold
    }

    fun setOnScratchCompleteListener(listener: () -> Unit) {
        onScratchComplete = listener
    }
    
    fun setOnScratchProgressListener(listener: (Float) -> Unit) {
        onScratchProgress = listener
    }
    
    fun setOnScratchStartListener(listener: () -> Unit) {
        onScratchStart = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        overlayBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        overlayCanvas = Canvas(overlayBitmap!!)
        drawOverlay(w, h)
    }
    
    private fun drawOverlay(width: Int, height: Int) {
        overlayCanvas?.let { canvas ->
            when (val type = overlayType) {
                is OverlayType.SolidColor -> {
                    canvas.drawColor(type.color.toArgb())
                }
                is OverlayType.LinearGradient -> {
                    val gradient = createLinearGradient(
                        type.startColor, type.endColor, type.direction, width, height
                    )
                    val paint = Paint().apply { shader = gradient }
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                }
                is OverlayType.RadialGradient -> {
                    val gradient = RadialGradient(
                        width / 2f, height / 2f, (kotlin.math.min(width, height) * type.radius),
                        type.centerColor.toArgb(), type.edgeColor.toArgb(),
                        Shader.TileMode.CLAMP
                    )
                    val paint = Paint().apply { shader = gradient }
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                }
                is OverlayType.MultiColorGradient -> {
                    val gradient = createMultiColorGradient(
                        type.colors, type.direction, width, height
                    )
                    val paint = Paint().apply { shader = gradient }
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                }
            }
        }
    }
    
    private fun createLinearGradient(
        startColor: Color, endColor: Color, direction: GradientDirection,
        width: Int, height: Int
    ): LinearGradient {
        val coordinates = when (direction) {
            GradientDirection.TopToBottom -> arrayOf(0f, 0f, 0f, height.toFloat())
            GradientDirection.BottomToTop -> arrayOf(0f, height.toFloat(), 0f, 0f)
            GradientDirection.LeftToRight -> arrayOf(0f, 0f, width.toFloat(), 0f)
            GradientDirection.RightToLeft -> arrayOf(width.toFloat(), 0f, 0f, 0f)
            GradientDirection.TopLeftToBottomRight -> arrayOf(0f, 0f, width.toFloat(), height.toFloat())
            GradientDirection.TopRightToBottomLeft -> arrayOf(width.toFloat(), 0f, 0f, height.toFloat())
            GradientDirection.BottomLeftToTopRight -> arrayOf(0f, height.toFloat(), width.toFloat(), 0f)
            GradientDirection.BottomRightToTopLeft -> arrayOf(width.toFloat(), height.toFloat(), 0f, 0f)
        }
        
        return LinearGradient(
            coordinates[0], coordinates[1], coordinates[2], coordinates[3],
            startColor.toArgb(), endColor.toArgb(),
            Shader.TileMode.CLAMP
        )
    }
    
    private fun createMultiColorGradient(
        colors: List<Color>, direction: GradientDirection,
        width: Int, height: Int
    ): LinearGradient {
        val coordinates = when (direction) {
            GradientDirection.TopToBottom -> arrayOf(0f, 0f, 0f, height.toFloat())
            GradientDirection.BottomToTop -> arrayOf(0f, height.toFloat(), 0f, 0f)
            GradientDirection.LeftToRight -> arrayOf(0f, 0f, width.toFloat(), 0f)
            GradientDirection.RightToLeft -> arrayOf(width.toFloat(), 0f, 0f, 0f)
            GradientDirection.TopLeftToBottomRight -> arrayOf(0f, 0f, width.toFloat(), height.toFloat())
            GradientDirection.TopRightToBottomLeft -> arrayOf(width.toFloat(), 0f, 0f, height.toFloat())
            GradientDirection.BottomLeftToTopRight -> arrayOf(0f, height.toFloat(), width.toFloat(), 0f)
            GradientDirection.BottomRightToTopLeft -> arrayOf(width.toFloat(), height.toFloat(), 0f, 0f)
        }
        
        val colorArray = colors.map { it.toArgb() }.toIntArray()
        val positions = FloatArray(colors.size) { it / (colors.size - 1).toFloat() }
        
        return LinearGradient(
            coordinates[0], coordinates[1], coordinates[2], coordinates[3],
            colorArray, positions,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        overlayBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, basePaint)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isRevealed) return false
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isScratching) {
                    isScratching = true
                    scratchCount++
                    onScratchStart?.invoke()
                }
                
                scratchPath.moveTo(x, y)
                lastX = x
                lastY = y
                
                // Add haptic feedback when starting to scratch
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(hapticFeedback)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(50)
                }
                
                // Play scratch sound
                playScratchSound()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isScratching) return true
                
                val dx = (x - lastX).pow(2)
                val dy = (y - lastY).pow(2)
                val distance = sqrt(dx + dy)
                
                if (distance >= 3) {
                    scratchPath.lineTo(x, y)
                    
                    // Apply different scratch patterns
                    when (scratchPattern) {
                        ScratchPattern.Normal -> {
                            overlayCanvas?.drawPath(scratchPath, overlayPaint)
                        }
                        ScratchPattern.Sparkle -> {
                            overlayCanvas?.drawPath(scratchPath, overlayPaint)
                            drawSparkleEffect(x, y)
                        }
                        ScratchPattern.Glow -> {
                            overlayCanvas?.drawPath(scratchPath, overlayPaint)
                            drawGlowEffect(x, y)
                        }
                        ScratchPattern.Metallic -> {
                            overlayCanvas?.drawPath(scratchPath, overlayPaint)
                            drawMetallicEffect(x, y)
                        }
                    }
                    
                    invalidate()
                    lastX = x
                    lastY = y
                    
                    // Check progress periodically
                    checkScratchProgress()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isScratching = false
                checkRevealProgress()
            }
        }
        return true
    }
    
    private fun drawSparkleEffect(x: Float, y: Float) {
        if (scratchCount % 3 == 0) { // Every 3rd scratch
            sparklePath.reset()
            repeat(5) {
                val angle = (it * 72) * Math.PI / 180
                val sparkleX = x + kotlin.math.cos(angle).toFloat() * 20
                val sparkleY = y + kotlin.math.sin(angle).toFloat() * 20
                sparklePath.moveTo(x, y)
                sparklePath.lineTo(sparkleX, sparkleY)
            }
            overlayCanvas?.drawPath(sparklePath, sparklePaint)
        }
    }
    
    private fun drawGlowEffect(x: Float, y: Float) {
        val glowPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 120f
            color = android.graphics.Color.WHITE
            setShadowLayer(15f, 0f, 0f, android.graphics.Color.CYAN)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        overlayCanvas?.drawCircle(x, y, 30f, glowPaint)
    }
    
    private fun drawMetallicEffect(x: Float, y: Float) {
        val metallicPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 100f
            color = android.graphics.Color.WHITE
            setShadowLayer(20f, 0f, 0f, android.graphics.Color.GRAY)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        overlayCanvas?.drawCircle(x, y, 25f, metallicPaint)
    }
    
    private fun playScratchSound() {
        if (scratchSoundId > 0) {
            soundPool?.play(scratchSoundId, 0.3f, 0.3f, 1, 0, 1f)
        }
    }
    
    private fun playWinSound() {
        if (winSoundId > 0) {
            soundPool?.play(winSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    private fun checkScratchProgress() {
        overlayBitmap?.let { bmp ->
            val totalPixels = bmp.width * bmp.height
            val pixels = IntArray(totalPixels)
            bmp.getPixels(pixels, 0, bmp.width, 0, 0, bmp.width, bmp.height)
            val clearedPixels = pixels.count { android.graphics.Color.alpha(it) == 0 }
            
            val progress = clearedPixels.toFloat() / totalPixels
            
            // Only trigger progress callback if significant change
            if (kotlin.math.abs(progress - lastProgress) > 0.05f) {
                lastProgress = progress
                onScratchProgress?.invoke(progress)
            }
        }
    }
    
    private fun checkRevealProgress() {
        overlayBitmap?.let { bmp ->
            val totalPixels = bmp.width * bmp.height
            val pixels = IntArray(totalPixels)
            bmp.getPixels(pixels, 0, bmp.width, 0, 0, bmp.width, bmp.height)
            val clearedPixels = pixels.count { android.graphics.Color.alpha(it) == 0 }

            val ratio = clearedPixels.toFloat() / totalPixels
            if (ratio > revealThreshold && !isRevealed) {
                isRevealed = true
                
                // Strong haptic feedback for completion
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val completionFeedback = VibrationEffect.createWaveform(
                        longArrayOf(0, 100, 50, 100),
                        intArrayOf(0, 255, 0, 255),
                        -1
                    )
                    vibrator.vibrate(completionFeedback)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 100, 50, 100), -1)
                }
                
                // Play win sound
                playWinSound()
                
                onScratchComplete?.invoke()
            }
        }
    }
    
    fun reset() {
        isRevealed = false
        isScratching = false
        scratchPath.reset()
        sparklePath.reset()
        totalScratchedArea = 0f
        lastProgress = 0f
        scratchCount = 0
        overlayBitmap?.let { bmp ->
            drawOverlay(bmp.width, bmp.height)
        }
        invalidate()
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        soundPool?.release()
    }
}

@Composable
fun ScratchCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    overlayType: OverlayType = OverlayType.SolidColor(Color(0xFF8E8E93)),
    scratchPattern: ScratchPattern = ScratchPattern.Normal,
    winAnimation: WinAnimation = WinAnimation.Confetti,
    overlayContent: (@Composable BoxScope.() -> Unit)? = null,
    revealThreshold: Float = 0.3f,
    onScratchComplete: (() -> Unit)? = null,
    onScratchProgress: ((Float) -> Unit)? = null,
    onScratchStart: (() -> Unit)? = null
) {
    // Backward compatibility
    @Deprecated("Use overlayType instead")
    @Composable
    fun ScratchCard(
        modifier: Modifier = Modifier,
        imageUrl: String,
        overlayColor: Color = Color(0xFF8E8E93),
        overlayContent: (@Composable BoxScope.() -> Unit)? = null,
        revealThreshold: Float = 0.3f,
        onScratchComplete: (() -> Unit)? = null,
        onScratchProgress: ((Float) -> Unit)? = null
    ) = ScratchCard(
        modifier = modifier,
        imageUrl = imageUrl,
        overlayType = OverlayType.SolidColor(overlayColor),
        overlayContent = overlayContent,
        revealThreshold = revealThreshold,
        onScratchComplete = onScratchComplete,
        onScratchProgress = onScratchProgress
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var revealed by remember { mutableStateOf(false) }
    var scratchProgress by remember { mutableStateOf(0f) }
    var showWinAnimation by remember { mutableStateOf(false) }
    var isScratching by remember { mutableStateOf(false) }
    var confettiParticles by remember { mutableStateOf(listOf<ConfettiParticle>()) }
    
    // Win animation states
    val winScale by animateFloatAsState(
        targetValue = if (showWinAnimation) 1.2f else 1f,
        animationSpec = tween(800, easing = EaseOutElastic),
        label = "winScale"
    )
    
    val winAlpha by animateFloatAsState(
        targetValue = if (showWinAnimation) 1f else 0f,
        animationSpec = tween(600, easing = EaseOut),
        label = "winAlpha"
    )
    
    val cardRotation by animateFloatAsState(
        targetValue = if (showWinAnimation) 0f else 0f,
        animationSpec = tween(1000, easing = EaseOutElastic),
        label = "cardRotation"
    )
    
    val sparkleRotation by rememberInfiniteTransition(label = "sparkle").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkleRotation"
    )
    
    val bounceScale by rememberInfiniteTransition(label = "bounce").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceScale"
    )

    LaunchedEffect(revealed) {
        if (revealed) {
            showWinAnimation = true
            
            // Create confetti particles
            if (winAnimation == WinAnimation.Confetti) {
                confettiParticles = (0..50).map {
                    ConfettiParticle(
                        x = (0..1000).random().toFloat(),
                        y = -50f,
                        velocityX = (-5..5).random().toFloat(),
                        velocityY = (2..8).random().toFloat(),
                        color = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan).random(),
                        size = (8..16).random().toFloat(),
                        rotation = (0..360).random().toFloat()
                    )
                }
                
                // Animate confetti
                coroutineScope.launch {
                    repeat(100) {
                        confettiParticles = confettiParticles.map { particle ->
                            particle.copy(
                                x = particle.x + particle.velocityX,
                                y = particle.y + particle.velocityY,
                                velocityY = particle.velocityY + 0.1f, // gravity
                                rotation = particle.rotation + 5f
                            )
                        }
                        delay(16)
                    }
                }
            }
            
            delay(3000)
            showWinAnimation = false
        }
    }

    Box(modifier = modifier) {
        // Background image (the prize)
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )

        if (!revealed) {
            // Overlay scratch view
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        ScratchCardView(context).apply {
                            setOverlayType(overlayType)
                            setScratchPattern(scratchPattern)
                            setRevealThreshold(revealThreshold)
                            setOnScratchCompleteListener {
                                revealed = true
                                onScratchComplete?.invoke()
                            }
                            setOnScratchProgressListener { progress ->
                                scratchProgress = progress
                                onScratchProgress?.invoke(progress)
                            }
                            setOnScratchStartListener {
                                isScratching = true
                                onScratchStart?.invoke()
                            }
                        }
                    }
                )

                // Progress indicator
                if (scratchProgress > 0.1f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${(scratchProgress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Optional overlay composable content
                overlayContent?.invoke(this)
            }
        } else {
            // Win animation overlay
            if (showWinAnimation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(winAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    // Confetti particles
                    if (winAnimation == WinAnimation.Confetti) {
                        confettiParticles.forEach { particle ->
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = particle.x.dp,
                                        y = particle.y.dp
                                    )
                                    .rotate(particle.rotation)
                                    .scale(particle.size / 12f)
                            ) {
                                Text(
                                    text = "🎊",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    
                    // Sparkle effects
                    when (winAnimation) {
                        WinAnimation.Sparkle -> {
                            repeat(12) { index ->
                                val sparkleOffset = (index * 30f + sparkleRotation) * 3.14159f / 180f
                                val sparkleX = (kotlin.math.cos(sparkleOffset) * 100f).toInt()
                                val sparkleY = (kotlin.math.sin(sparkleOffset) * 100f).toInt()
                                
                                Text(
                                    text = "✨",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .offset(x = sparkleX.dp, y = sparkleY.dp)
                                        .scale(winScale)
                                )
                            }
                        }
                        WinAnimation.Fireworks -> {
                            repeat(8) { index ->
                                val angle = index * 45f
                                val distance = 120f
                                val x = (kotlin.math.cos(Math.toRadians(angle.toDouble())) * distance).toInt()
                                val y = (kotlin.math.sin(Math.toRadians(angle.toDouble())) * distance).toInt()
                                
                                Text(
                                    text = "💥",
                                    fontSize = 32.sp,
                                    modifier = Modifier
                                        .offset(x = x.dp, y = y.dp)
                                        .scale(bounceScale)
                                )
                            }
                        }
                        else -> {}
                    }
                    
                    // Win message with enhanced animation
                    Card(
                        modifier = Modifier
                            .padding(32.dp)
                            .scale(if (winAnimation == WinAnimation.Bounce) bounceScale else winScale)
                            .rotate(cardRotation),
                        colors = CardDefaults.cardColors(
                            containerColor = when (winAnimation) {
                                WinAnimation.Confetti -> Color(0xFF4CAF50)
                                WinAnimation.Fireworks -> Color(0xFFFF5722)
                                WinAnimation.Sparkle -> Color(0xFF9C27B0)
                                WinAnimation.Bounce -> Color(0xFF2196F3)
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (winAnimation) {
                                    WinAnimation.Confetti -> "🎉"
                                    WinAnimation.Fireworks -> "💥"
                                    WinAnimation.Sparkle -> "✨"
                                    WinAnimation.Bounce -> "🏆"
                                },
                                fontSize = 56.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Congratulations!",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = when (winAnimation) {
                                    WinAnimation.Confetti -> "You won a prize!"
                                    WinAnimation.Fireworks -> "Amazing win!"
                                    WinAnimation.Sparkle -> "Sparkling victory!"
                                    WinAnimation.Bounce -> "Champion!"
                                },
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun ScratchCardDemo() {
    ScratchCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        imageUrl = "https://picsum.photos/500/300",
        overlayType = OverlayType.LinearGradient(
            startColor = Color(0xFF8E8E93),
            endColor = Color(0xFF5D5D5D),
            direction = GradientDirection.TopLeftToBottomRight
        ),
        overlayContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🎁",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Scratch Here",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "to reveal your prize!",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        },
        onScratchComplete = {
            println("🎉 Scratch completed! Show reward popup or navigate to reward screen.")
        },
        onScratchProgress = { progress ->
            println("Scratch progress: ${(progress * 100).toInt()}%")
        }
    )
}

@Preview
@Composable
fun ScratchCardGradientDemo() {
    ScratchCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        imageUrl = "https://picsum.photos/500/300",
        overlayType = OverlayType.MultiColorGradient(
            colors = listOf(
                Color(0xFFFF6B6B),
                Color(0xFF4ECDC4),
                Color(0xFF45B7D1),
                Color(0xFF96CEB4)
            ),
            direction = GradientDirection.TopLeftToBottomRight
        ),
        overlayContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌈 Scratch Me! 🌈",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    )
}

@Preview
@Composable
fun ScratchCardRadialDemo() {
    ScratchCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        imageUrl = "https://picsum.photos/500/300",
        overlayType = OverlayType.RadialGradient(
            centerColor = Color(0xFF6C5CE7),
            edgeColor = Color(0xFF2D3436),
            radius = 0.8f
        ),
        overlayContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💎 Premium Prize 💎",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    )
}

@Preview
@Composable
fun ScratchCardWinDemo() {
    var revealed by remember { mutableStateOf(true) }
    
    if (revealed) {
        ScratchCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            imageUrl = "https://picsum.photos/500/300",
            overlayType = OverlayType.SolidColor(Color(0xFF8E8E93)),
            onScratchComplete = {
                println("🎉 Prize revealed!")
            }
        )
    } else {
        Button(onClick = { revealed = true }) {
            Text("Reveal Card")
        }
    }
}
