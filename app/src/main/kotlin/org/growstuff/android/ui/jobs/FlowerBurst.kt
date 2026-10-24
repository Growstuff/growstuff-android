// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/** One burst of flowers from the tick box at [origin], in the overlay's own coordinates. */
data class Burst(val origin: Offset, val key: Long)

private data class Flower(
    val color: Color,
    val sizeDp: Float,
    val dxDp: Float,
    val dyDp: Float,
    val rotation: Float,
    val delay: Float,
)

private val petalColors = listOf(
    Color(0xFFFFA500),
    Color(0xFFE4A0A0),
    Color(0xFFFFF3A8),
    Color(0xFFC9A0DC),
    Color(0xFFFF8A65),
    Color(0xFFFFFFFF),
)
private val flowerMiddle = Color(0xFFC9A800)

private const val FLIGHT_SECONDS = 1.4f
private const val TOTAL_SECONDS = 1.9f

/** True when the person has turned animations off in Android's settings. */
@Composable
fun animationsOff(): Boolean {
    val resolver = LocalContext.current.contentResolver
    return remember(resolver) {
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
}

/** Flowers bloom out of the tick box, drift up and fade, as on the website. */
@Composable
fun FlowerBurst(burst: Burst, onFinished: () -> Unit, modifier: Modifier = Modifier) {
    val clock = remember(burst.key) { Animatable(0f) }
    val flowers = remember(burst.key) {
        List(10) { i ->
            Flower(
                color = petalColors[i % petalColors.size],
                sizeDp = 20f + Random.nextFloat() * 18f,
                dxDp = (Random.nextFloat() * 2 - 1) * 130f,
                dyDp = -(50f + Random.nextFloat() * 130f),
                rotation = (Random.nextFloat() * 2 - 1) * 200f,
                delay = Random.nextFloat() * 0.25f,
            )
        }
    }
    LaunchedEffect(burst.key) {
        clock.animateTo(TOTAL_SECONDS, tween((TOTAL_SECONDS * 1000).toInt(), easing = LinearEasing))
        onFinished()
    }
    Canvas(modifier) {
        flowers.forEach { flower ->
            val t = ((clock.value - flower.delay) / FLIGHT_SECONDS).coerceIn(0f, 1f)
            if (t <= 0f || t >= 1f) return@forEach
            val eased = FastOutSlowInEasing.transform(t)
            val alpha = when {
                t < 0.15f -> t / 0.15f
                t > 0.7f -> (1f - t) / 0.3f
                else -> 1f
            }
            val centre =
                burst.origin + Offset(flower.dxDp.dp.toPx() * eased, flower.dyDp.dp.toPx() * eased)
            val unit = flower.sizeDp.dp.toPx() / 20f * (0.2f + 0.8f * eased)
            rotate(flower.rotation * eased, centre) {
                drawFlower(centre, unit, flower.color, alpha)
            }
        }
    }
}

private fun DrawScope.drawFlower(centre: Offset, unit: Float, color: Color, alpha: Float) {
    val outline = Stroke(width = 0.7f * unit)
    repeat(5) { petal ->
        rotate(petal * 72f, centre) {
            val at = centre + Offset(0f, -5.2f * unit)
            drawCircle(color, 3.9f * unit, at, alpha)
            drawCircle(Color.Black, 3.9f * unit, at, alpha, outline)
        }
    }
    drawCircle(flowerMiddle, 2.7f * unit, centre, alpha)
    drawCircle(Color.Black, 2.7f * unit, centre, alpha, outline)
}
