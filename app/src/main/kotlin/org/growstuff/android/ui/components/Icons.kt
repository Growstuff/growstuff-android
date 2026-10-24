// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/** Stroke icons on a 24 unit grid, drawn to match the designs. They take the tint of the Icon. */
object GrowstuffIcons {
    val Arrow = stroke("arrow", 2.4f, "M4 12h16M14 6l6 6-6 6")
    val Back = stroke("back", 2.6f, "M15 5l-7 7 7 7")
    val Chevron = stroke("chevron", 2.6f, "M9 5l7 7-7 7")
    val External = stroke(
        "external",
        2.4f,
        "M14 4h6v6M20 4l-9 9M18 14v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h5",
    )
    val Lock = stroke(
        "lock",
        2.2f,
        "M6 11h12a1 1 0 0 1 1 1v7a1 1 0 0 1-1 1H6a1 1 0 0 1-1-1v-7a1 1 0 0 1 1-1z",
        "M8 11V8a4 4 0 0 1 8 0v3",
    )
    val Sprout = stroke("sprout", 1.8f, "M12 19v-8M12 11c0-4-3-6-7-6 0 4 3 6 7 6zM12 13c0-3 2-5 6-5 0 3-2 5-6 5zM3 19.5h18")
    val Friends = stroke(
        "friends",
        1.8f,
        circle(9f, 8f, 3.2f),
        "M3 20c0-4 2.7-6.2 6-6.2s6 2.2 6 6.2",
        circle(17.2f, 9f, 2.6f),
        "M16.4 13.9c2.9 0 4.6 1.9 4.6 5.1",
    )
    val Jobs = stroke("jobs", 1.8f, "M5 4h14a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1z", "M8 12.2l3 3 5-6.4")
    val Resting = stroke("resting", 1.8f, "M3 17h18M5 21h14M9 14c0-1.4 1.3-2.5 3-2.5s3 1.1 3 2.5M12 11.5V9")
    val Idea = stroke(
        "idea",
        2f,
        "M9 18h6M10 21h4M12 3a6 6 0 0 0-3.5 10.9c.6.5 1 1.2 1 2.1h5c0-.9.4-1.6 1-2.1A6 6 0 0 0 12 3z",
    )
    val Plus = stroke("plus", 2.8f, "M12 5v14M5 12h14")
    val Tick = stroke("tick", 3.4f, "M5 12.5l5 5L19 7")
    val SignOut = stroke("sign-out", 2.2f, "M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9")

    private fun circle(cx: Float, cy: Float, r: Float) = "M${cx - r} ${cy}a$r $r 0 1 0 ${2 * r} 0a$r $r 0 1 0 ${-2 * r} 0z"

    private fun stroke(name: String, width: Float, vararg paths: String): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            paths.forEach {
                addPath(
                    pathData = addPathNodes(it),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = width,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                )
            }
        }.build()
}
