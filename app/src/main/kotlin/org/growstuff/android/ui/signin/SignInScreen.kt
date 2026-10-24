// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.growstuff.android.R
import org.growstuff.android.ui.Links
import org.growstuff.android.ui.components.BlockButton
import org.growstuff.android.ui.components.GrowstuffIcons
import org.growstuff.android.ui.components.Rule
import org.growstuff.android.ui.components.TopBar
import org.growstuff.android.ui.components.VerticalRule
import org.growstuff.android.ui.components.outlined
import org.growstuff.android.ui.theme.GrowstuffColors
import org.growstuff.android.ui.theme.MinTapTarget

@Composable
fun SignInScreen(onSignIn: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize().background(GrowstuffColors.Beige)) {
        TopBar()
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Column(Modifier.fillMaxWidth().background(GrowstuffColors.Green).padding(24.dp)) {
                Text(
                    stringResource(R.string.sign_in_title),
                    style = MaterialTheme.typography.displayMedium,
                    color = GrowstuffColors.White,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    stringResource(R.string.sign_in_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = GrowstuffColors.White,
                    modifier = Modifier.padding(top = 14.dp),
                )
            }
            Rule()
            Image(
                painterResource(R.drawable.sign_in_garden),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxWidth().height(170.dp),
            )
            Rule()
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BlockButton(
                    stringResource(R.string.sign_in),
                    onClick = onSignIn,
                    icon = GrowstuffIcons.Arrow,
                    modifier = Modifier.fillMaxWidth(),
                )
                BlockButton(
                    stringResource(R.string.create_account),
                    onClick = { uriHandler.openUri(Links.SIGN_UP) },
                    primary = false,
                    icon = GrowstuffIcons.External,
                    minHeight = MinTapTarget,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    Modifier.fillMaxWidth().height(
                        IntrinsicSize.Min,
                    ).background(GrowstuffColors.White).outlined(),
                ) {
                    Box(
                        Modifier.width(56.dp).fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(GrowstuffIcons.Lock, null, Modifier.size(24.dp))
                    }
                    VerticalRule()
                    Text(
                        stringResource(R.string.sign_in_password_note),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    )
                }
            }
        }
        Rule()
        Row(
            Modifier.fillMaxWidth().height(
                IntrinsicSize.Min,
            ).background(GrowstuffColors.White).navigationBarsPadding(),
        ) {
            FooterLink(stringResource(R.string.privacy), Modifier.weight(1f)) {
                uriHandler.openUri(Links.PRIVACY)
            }
            VerticalRule()
            FooterLink(stringResource(R.string.open_source), Modifier.weight(1f)) {
                uriHandler.openUri(Links.SOURCE)
            }
        }
    }
}

@Composable
private fun FooterLink(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.heightIn(min = MinTapTarget).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text.uppercase(), style = MaterialTheme.typography.labelMedium)
    }
}
