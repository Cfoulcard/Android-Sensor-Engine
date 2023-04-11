package com.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun LightningEffect(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val lightningPath = remember { mutableStateOf<Path?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (progress < 0.1f) {
                if (lightningPath.value == null) {
                    lightningPath.value = generateLightningPath(size.width, size.height)
                }
                lightningPath.value?.let {
                    drawPath(
                        path = it,
                        color = Color.White,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            } else {
                lightningPath.value = null
            }
        }
    }
}



fun generateLightningPath(width: Float, height: Float): Path {
    val path = Path()
    val startY = height * Random.nextFloat() * 0.4f
    val endY = height - (height * Random.nextFloat() * 0.4f)
    val direction = if (Random.nextBoolean()) 1 else -1
    val randomXOffset = width * 0.1f * Random.nextFloat()

    path.moveTo(width / 2, startY)
    path.lineTo(width / 2 + (direction * randomXOffset), height / 2)
    path.lineTo(width / 2, endY)

    return path
}