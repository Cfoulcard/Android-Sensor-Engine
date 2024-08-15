package com.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/** Adds a floating island effect to compose views. To apply use
 * .graphicsLayer(translationY = addFloatingUpAndDownAnimation(duration))
 * on the Modifier property
 */
@Composable
fun addFloatingUpAndDownAnimation(duration: Int): Float {
    val infiniteTransition = rememberInfiniteTransition()
    val floatingUpAndDown by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    return floatingUpAndDown
}

/** This is to be used for the colorFilter property for images. To apply use
 * androidx.compose.ui.graphics.Color.White.copy(alpha = alpha) for instance
 */
@Composable
fun addLightLikeAnimationForImages(duration: Int): Float {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    return alpha
}
