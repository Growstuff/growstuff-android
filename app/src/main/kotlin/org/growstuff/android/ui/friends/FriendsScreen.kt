// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.growstuff.android.R
import org.growstuff.android.ui.components.TopBar
import org.growstuff.android.ui.theme.GrowstuffColors

@Composable
fun FriendsScreen(onBack: (() -> Unit)?) {
    Column(Modifier.fillMaxSize().background(GrowstuffColors.White)) {
        TopBar(title = stringResource(R.string.my_friends), onBack = onBack)
        Column(Modifier.padding(28.dp)) {
            Text(
                stringResource(R.string.friends_coming),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                stringResource(R.string.friends_coming_body),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
