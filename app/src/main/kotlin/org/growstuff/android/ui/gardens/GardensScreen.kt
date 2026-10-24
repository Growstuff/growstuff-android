// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.gardens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.time.Clock
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.delay
import org.growstuff.android.R
import org.growstuff.android.data.Garden
import org.growstuff.android.ui.Links
import org.growstuff.android.ui.components.AreaRow
import org.growstuff.android.ui.components.BlockButton
import org.growstuff.android.ui.components.GrowstuffIcons
import org.growstuff.android.ui.components.Rule
import org.growstuff.android.ui.components.Tag
import org.growstuff.android.ui.components.TopBar
import org.growstuff.android.ui.theme.GrowstuffColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardensScreen(viewModel: GardensViewModel, clock: Clock, onBack: (() -> Unit)?) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize().background(GrowstuffColors.Beige)) {
        TopBar(title = stringResource(R.string.my_gardens), onBack = onBack)
        Text(
            stringResource(R.string.last_watered, wateredAgo(state.refreshedAt, clock)),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().background(
                GrowstuffColors.White,
            ).padding(horizontal = 16.dp, vertical = 10.dp),
        )
        Rule()
        PullToRefreshBox(
            isRefreshing = state.refreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.weight(1f).background(GrowstuffColors.White),
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(state.gardens, key = { it.id }) { garden ->
                    GardenRow(garden)
                    Rule()
                }
            }
        }
        Rule()
        BlockButton(
            stringResource(R.string.add_a_garden),
            onClick = { uriHandler.openUri(Links.NEW_GARDEN) },
            icon = GrowstuffIcons.External,
            minHeight = 60.dp,
            modifier = Modifier.navigationBarsPadding().padding(12.dp).fillMaxWidth(),
        )
    }
}

@Composable
private fun GardenRow(garden: Garden) {
    val growing = garden.growingCount > 0
    AreaRow(
        icon = if (growing) GrowstuffIcons.Sprout else GrowstuffIcons.Resting,
        title = garden.name,
        body = listOfNotNull(garden.location, area(garden)).joinToString(" · "),
        // A garden's own screen comes next; until then the row doesn't lead anywhere.
        onClick = null,
        iconBlockWidth = 88.dp,
        iconBackground = if (growing) GrowstuffColors.Green else GrowstuffColors.Panel,
        iconTint = if (growing) GrowstuffColors.White else GrowstuffColors.Soil,
        modifier = Modifier.heightIn(min = 132.dp),
    ) {
        if (growing) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Tag(
                    pluralStringResource(
                        R.plurals.growing,
                        garden.growingCount,
                        garden.growingCount,
                    ),
                )
                if (garden.readyToHarvestCount > 0) {
                    Tag(
                        pluralStringResource(
                            R.plurals.ready_to_harvest,
                            garden.readyToHarvestCount,
                            garden.readyToHarvestCount,
                        ),
                        GrowstuffColors.MemberChip,
                    )
                }
            }
        } else {
            Text(
                stringResource(R.string.soil_resting),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun area(garden: Garden): String? {
    val size = garden.area ?: return null
    val number = NumberFormat.getNumberInstance().format(size)
    return when (garden.areaUnit) {
        "square metre" -> stringResource(R.string.square_metres, number)
        "square foot" -> stringResource(R.string.square_feet, number)
        else -> listOfNotNull(number, garden.areaUnit).joinToString(" ")
    }
}

/** "just now" or "3 minutes ago", kept up to date while the screen is open. */
@Composable
private fun wateredAgo(at: Instant, clock: Clock): String {
    var now by remember { mutableStateOf(clock.instant()) }
    LaunchedEffect(at) {
        while (true) {
            now = clock.instant()
            delay(30_000)
        }
    }
    val minutes = Duration.between(at, now).toMinutes().toInt()
    return if (minutes <
        1
    ) {
        stringResource(R.string.just_now)
    } else {
        pluralStringResource(R.plurals.minutes_ago, minutes, minutes)
    }
}
