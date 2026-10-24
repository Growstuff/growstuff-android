// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.growstuff.android.R
import org.growstuff.android.ui.components.AreaRow
import org.growstuff.android.ui.components.GrowstuffIcons
import org.growstuff.android.ui.components.Rule
import org.growstuff.android.ui.components.Tag
import org.growstuff.android.ui.components.TopBar
import org.growstuff.android.ui.components.VerticalRule
import org.growstuff.android.ui.theme.GrowstuffColors
import org.growstuff.android.ui.theme.MinTapTarget

/** The three parts of the app reached from home. */
enum class Area { Gardens, Friends, Jobs }

@Composable
fun HomeScreen(
    loginName: String,
    hasIdeas: Boolean,
    selected: Area?,
    onOpen: (Area) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(GrowstuffColors.White)) {
        TopBar {
            VerticalRule()
            Box(Modifier.width(68.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Avatar(loginName)
            }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Column(Modifier.fillMaxWidth().background(GrowstuffColors.Green).padding(24.dp)) {
                Text(
                    stringResource(R.string.hello, loginName),
                    style = MaterialTheme.typography.headlineLarge,
                    color = GrowstuffColors.White,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    stringResource(R.string.hello_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = GrowstuffColors.White,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
            Rule()
            AreaRow(
                GrowstuffIcons.Sprout,
                stringResource(R.string.my_gardens).uppercase(),
                stringResource(R.string.my_gardens_body),
                onClick = { onOpen(Area.Gardens) },
                selected = selected == Area.Gardens,
                modifier = Modifier.heightIn(min = 150.dp),
            )
            Rule()
            AreaRow(
                GrowstuffIcons.Friends,
                stringResource(R.string.my_friends).uppercase(),
                stringResource(R.string.my_friends_body),
                onClick = { onOpen(Area.Friends) },
                selected = selected == Area.Friends,
                modifier = Modifier.heightIn(min = 150.dp),
            )
            Rule()
            AreaRow(
                GrowstuffIcons.Jobs,
                stringResource(R.string.my_jobs).uppercase(),
                stringResource(R.string.my_jobs_body),
                onClick = { onOpen(Area.Jobs) },
                selected = selected == Area.Jobs,
                modifier = Modifier.heightIn(min = 150.dp),
            ) {
                if (hasIdeas) {
                    Box(Modifier.padding(top = 4.dp)) {
                        Tag(stringResource(R.string.ideas_for_you), GrowstuffColors.IdeasYellow)
                    }
                }
            }
            Rule()
        }
        Rule()
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .heightIn(min = MinTapTarget)
                .clickable(role = Role.Button, onClick = onSignOut),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(GrowstuffIcons.SignOut, null, Modifier.size(22.dp))
            Text(
                stringResource(R.string.sign_out).uppercase(),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun Avatar(loginName: String) {
    val label = stringResource(R.string.account_label, loginName)
    Box(
        Modifier
            .size(44.dp)
            .background(GrowstuffColors.MemberChip, CircleShape)
            .border(3.dp, GrowstuffColors.Black, CircleShape)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Text(loginName.take(1).uppercase(), style = MaterialTheme.typography.titleLarge)
    }
}
