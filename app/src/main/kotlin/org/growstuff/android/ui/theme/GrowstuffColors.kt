// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.theme

import androidx.compose.ui.graphics.Color

/** Growstuff's design tokens. Contrast ratios are for the pairs the screens use. */
object GrowstuffColors {
    val Brown = Color(0xFF413F3B) // top bar; white on it 10.5:1
    val Beige = Color(0xFFF4F2EF) // ground
    val Green = Color(0xFF57803C) // the one green; white on it 4.6:1
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000) // outlines and text
    val MemberChip = Color(0xFFA4C98C) // avatars and link chips, black text on it
    val Panel = Color(0xFFECEBE6) // group headings
    val Red = Color(0xFFFF4D43) // the overdue heading, black text on it
    val IdeasYellow = Color(0xFFFFF3A8) // only the "ideas for you" box
    val Muted = Color(0xFF6C757D) // done jobs, 4.7:1 on white
    val DayGreen = Color(0xFF3F5E2B) // due day, 7.6:1 on white
    val DayOverdue = Color(0xFFC62828) // overdue day, 5.6:1 on white
    val Soil = Color(0xFF6D4C41)
}
