// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/** Thickness of the black outline around every block and control. */
val Outline = 4.dp

/** The smallest height for a main control. */
val MinTapTarget = 56.dp

private val colors = lightColorScheme(
    primary = GrowstuffColors.Green,
    onPrimary = GrowstuffColors.White,
    secondary = GrowstuffColors.Brown,
    onSecondary = GrowstuffColors.White,
    background = GrowstuffColors.Beige,
    onBackground = GrowstuffColors.Black,
    surface = GrowstuffColors.White,
    onSurface = GrowstuffColors.Black,
    outline = GrowstuffColors.Black,
)

private val square = RoundedCornerShape(0.dp)

@Composable
fun GrowstuffTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        typography = GrowstuffTypography,
        shapes = Shapes(square, square, square, square, square),
        content = content,
    )
}
