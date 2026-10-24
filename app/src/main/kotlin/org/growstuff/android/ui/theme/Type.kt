// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.growstuff.android.R

private fun raleway(weight: FontWeight) = Font(
    resId = R.font.raleway,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val Raleway = FontFamily(
    raleway(FontWeight.Normal),
    raleway(FontWeight.Medium),
    raleway(FontWeight.SemiBold),
    raleway(FontWeight.Bold),
    raleway(FontWeight.ExtraBold),
)

/** The wordmark face, only for the name "Growstuff". */
val Modak = FontFamily(Font(R.font.modak))

private val base = TextStyle(fontFamily = Raleway, color = GrowstuffColors.Black)

val GrowstuffTypography = Typography(
    displayMedium = base.copy(fontSize = 44.sp, lineHeight = 46.sp, fontWeight = FontWeight.ExtraBold),
    headlineLarge = base.copy(fontSize = 36.sp, lineHeight = 38.sp, fontWeight = FontWeight.ExtraBold),
    headlineSmall = base.copy(fontSize = 24.sp, lineHeight = 26.sp, fontWeight = FontWeight.ExtraBold),
    titleLarge = base.copy(fontSize = 22.sp, lineHeight = 24.sp, fontWeight = FontWeight.ExtraBold),
    titleMedium = base.copy(fontSize = 18.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold),
    bodyLarge = base.copy(fontSize = 18.sp, lineHeight = 25.sp, fontWeight = FontWeight.Medium),
    bodyMedium = base.copy(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    bodySmall = base.copy(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium),
    labelLarge = base.copy(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.06.em),
    labelMedium = base.copy(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.06.em),
    labelSmall = base.copy(fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.06.em),
)
