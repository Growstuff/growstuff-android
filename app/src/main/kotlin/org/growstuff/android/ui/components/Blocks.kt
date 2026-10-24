// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.growstuff.android.R
import org.growstuff.android.ui.theme.GrowstuffColors
import org.growstuff.android.ui.theme.MinTapTarget
import org.growstuff.android.ui.theme.Modak
import org.growstuff.android.ui.theme.Outline

/** A black line across the screen, between blocks. */
@Composable
fun Rule(thickness: Dp = Outline) {
    Box(Modifier.fillMaxWidth().height(thickness).background(GrowstuffColors.Black))
}

/** A black line down a row. The row needs `Modifier.height(IntrinsicSize.Min)`. */
@Composable
fun VerticalRule(thickness: Dp = Outline) {
    Box(Modifier.fillMaxHeight().width(thickness).background(GrowstuffColors.Black))
}

fun Modifier.outlined(thickness: Dp = Outline) = border(thickness, GrowstuffColors.Black, RectangleShape)

/** The brown bar along the top: the wordmark, or a back button and a title. */
@Composable
fun TopBar(
    title: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(Modifier.background(GrowstuffColors.Brown).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min).heightIn(min = 68.dp)) {
            if (onBack != null) {
                Box(
                    Modifier
                        .width(68.dp)
                        .fillMaxHeight()
                        .clickable(onClickLabel = stringResource(R.string.back), role = Role.Button, onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(GrowstuffIcons.Back, stringResource(R.string.back), Modifier.size(28.dp), tint = GrowstuffColors.White)
                }
                VerticalRule()
            }
            if (title == null) {
                Box(Modifier.fillMaxHeight().padding(horizontal = 18.dp), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.app_name),
                        fontFamily = Modak,
                        fontSize = 32.sp,
                        lineHeight = 32.sp,
                        color = GrowstuffColors.White,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                VerticalRule()
            } else {
                Text(
                    title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = GrowstuffColors.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 18.dp)
                        .semantics { heading() },
                )
            }
            Spacer(Modifier.weight(1f))
            actions()
        }
        Rule()
    }
}

/** A big outlined button with a short label in capitals. */
@Composable
fun BlockButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = true,
    icon: ImageVector? = null,
    minHeight: Dp = 64.dp,
) {
    val content = if (primary) GrowstuffColors.White else GrowstuffColors.Black
    Row(
        modifier
            .heightIn(min = minHeight)
            .background(if (primary) GrowstuffColors.Green else GrowstuffColors.White)
            .outlined()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = content,
            textAlign = TextAlign.Center,
        )
        if (icon != null) Icon(icon, null, Modifier.size(24.dp), tint = content)
    }
}

/** A full-width row that leads somewhere: an icon block, a title and a line, and a chevron. */
@Composable
fun AreaRow(
    icon: ImageVector,
    title: String,
    body: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    iconBlockWidth: Dp = 112.dp,
    iconBackground: Color = GrowstuffColors.Green,
    iconTint: Color = GrowstuffColors.White,
    extra: @Composable () -> Unit = {},
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(GrowstuffColors.White)
            .then(if (onClick != null) Modifier.selectable(selected, role = Role.Button, onClick = onClick) else Modifier),
    ) {
        Box(
            Modifier.width(iconBlockWidth).fillMaxHeight().background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, Modifier.size(iconBlockWidth * 0.5f), tint = iconTint)
        }
        VerticalRule()
        Column(
            Modifier.weight(1f).padding(16.dp).align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Text(body, style = MaterialTheme.typography.bodyMedium)
            extra()
        }
        if (onClick != null) {
            VerticalRule()
            Box(
                Modifier
                    .width(MinTapTarget)
                    .fillMaxHeight()
                    .background(if (selected) GrowstuffColors.Green else GrowstuffColors.White),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    GrowstuffIcons.Chevron,
                    null,
                    Modifier.size(28.dp),
                    tint = if (selected) GrowstuffColors.White else GrowstuffColors.Black,
                )
            }
        }
    }
}

/** A small outlined label, such as "3 growing". */
@Composable
fun Tag(text: String, background: Color = GrowstuffColors.White, outline: Dp = 3.dp) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(background)
            .outlined(outline)
            .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}
