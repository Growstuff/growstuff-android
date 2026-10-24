// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.delay
import org.growstuff.android.R
import org.growstuff.android.data.Job
import org.growstuff.android.data.JobLink
import org.growstuff.android.ui.Links
import org.growstuff.android.ui.components.BlockButton
import org.growstuff.android.ui.components.GrowstuffIcons
import org.growstuff.android.ui.components.Rule
import org.growstuff.android.ui.components.TopBar
import org.growstuff.android.ui.components.VerticalRule
import org.growstuff.android.ui.components.outlined
import org.growstuff.android.ui.theme.GrowstuffColors
import org.growstuff.android.ui.theme.MinTapTarget
import org.growstuff.android.ui.theme.Raleway

@Composable
fun JobsScreen(viewModel: JobsViewModel, onBack: (() -> Unit)?) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val noAnimations = animationsOff()
    var burst by remember { mutableStateOf<Burst?>(null) }
    var overlayOrigin by remember { mutableStateOf(Offset.Zero) }
    val tickCentres = remember { mutableStateMapOf<String, Offset>() }

    Box(Modifier.fillMaxSize().onGloballyPositioned { overlayOrigin = it.positionInRoot() }) {
        Column(Modifier.fillMaxSize().background(GrowstuffColors.White)) {
            TopBar(title = stringResource(R.string.my_jobs), onBack = onBack)
            Tabs(state, viewModel::showTab)
            Rule()
            if (state.ideaCount > 0) {
                IdeasBanner(state.ideaCount) { uriHandler.openUri(Links.JOB_IDEAS) }
                Rule()
            }
            Box(Modifier.weight(1f)) {
                JobList(
                    state = state,
                    onToggle = { job, centre ->
                        val crossingOff = !job.finished
                        viewModel.setFinished(job, crossingOff)
                        if (crossingOff && !noAnimations) {
                            burst = Burst(centre - overlayOrigin, System.nanoTime())
                        }
                    },
                    tickCentres = tickCentres,
                )
                state.crossedOff?.let { job ->
                    CrossedOffToast(
                        job = job,
                        onUndo = viewModel::undoCrossOff,
                        onTimeout = viewModel::dismissCrossOff,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp),
                    )
                }
            }
            Rule()
            AddJobBar(viewModel::add)
        }
        burst?.let {
            FlowerBurst(it, onFinished = { burst = null }, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun Tabs(state: JobsUiState, onSelect: (JobsTab) -> Unit) {
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Tab(
            stringResource(R.string.jobs_to_do),
            state.toDoCount,
            state.tab == JobsTab.ToDo,
            Modifier.weight(1f),
        ) {
            onSelect(JobsTab.ToDo)
        }
        VerticalRule()
        Tab(
            stringResource(R.string.jobs_done),
            state.doneCount,
            state.tab == JobsTab.Done,
            Modifier.weight(1f),
        ) {
            onSelect(JobsTab.Done)
        }
    }
}

@Composable
private fun Tab(label: String, count: Int, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val description = stringResource(R.string.jobs_tab_label, label, count)
    Row(
        modifier
            .heightIn(min = 60.dp)
            .background(if (selected) GrowstuffColors.Green else GrowstuffColors.White)
            .selectable(selected, role = Role.Tab, onClick = onClick)
            .semantics(mergeDescendants = true) { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontSize = 16.sp,
            color = if (selected) GrowstuffColors.White else GrowstuffColors.Black,
        )
        Text(
            count.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontSize = 14.sp,
            modifier = Modifier
                .background(if (selected) GrowstuffColors.White else GrowstuffColors.Beige)
                .outlined(3.dp)
                .padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun IdeasBanner(count: Int, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = MinTapTarget)
            .background(GrowstuffColors.IdeasYellow)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(GrowstuffIcons.Idea, null, Modifier.size(26.dp))
        Text(
            pluralStringResource(R.plurals.job_ideas, count, count),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        Text(
            stringResource(R.string.see_ideas).uppercase() + " →",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 13.sp,
        )
    }
}

@Composable
private fun JobList(state: JobsUiState, onToggle: (Job, Offset) -> Unit, tickCentres: MutableMap<String, Offset>) {
    if (state.groups.isEmpty()) {
        val (title, body) = if (state.tab == JobsTab.ToDo) {
            R.string.jobs_nothing_left to R.string.jobs_nothing_left_body
        } else {
            R.string.jobs_nothing_done to R.string.jobs_nothing_done_body
        }
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
            Text(
                stringResource(body),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        return
    }
    LazyColumn(Modifier.fillMaxSize()) {
        state.groups.forEach { group ->
            item(key = "head-${group.kind}") { GroupHeading(group.kind, state.today) }
            items(group.jobs, key = { it.id }) { job ->
                JobRow(
                    job = job,
                    today = state.today,
                    onToggle = { onToggle(job, tickCentres[job.id] ?: Offset.Zero) },
                    onTickPlaced = { tickCentres[job.id] = it },
                )
            }
        }
    }
}

@Composable
private fun GroupHeading(kind: JobGroupKind, today: LocalDate) {
    val dayMonth = DateTimeFormatter.ofPattern("EEE d MMM")
    val (title, note) = when (kind) {
        JobGroupKind.Overdue -> stringResource(R.string.jobs_overdue) to ""
        JobGroupKind.Today -> stringResource(R.string.jobs_today) to today.format(dayMonth)
        JobGroupKind.ThisWeek -> stringResource(R.string.jobs_this_week) to
            "${today.plusDays(
                1,
            ).format(DateTimeFormatter.ofPattern("EEE d"))} – ${today.plusDays(7).format(dayMonth)}"
        JobGroupKind.Later -> stringResource(R.string.jobs_later) to ""
        JobGroupKind.SomeDay -> stringResource(R.string.jobs_some_day) to
            stringResource(R.string.jobs_some_day_note)
        JobGroupKind.Done -> stringResource(R.string.jobs_done) to
            stringResource(R.string.jobs_done_note)
    }
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    if (kind ==
                        JobGroupKind.Overdue
                    ) {
                        GrowstuffColors.Red
                    } else {
                        GrowstuffColors.Panel
                    },
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 14.sp,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                note,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Rule(3.dp)
    }
}

@Composable
private fun JobRow(job: Job, today: LocalDate, onToggle: () -> Unit, onTickPlaced: (Offset) -> Unit) {
    val label =
        stringResource(if (job.finished) R.string.put_back else R.string.cross_off, job.name)
    Column {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .width(68.dp)
                    .fillMaxHeight()
                    .heightIn(min = 76.dp)
                    .toggleable(value = job.finished, role = Role.Checkbox, onValueChange = {
                        onToggle()
                    })
                    .semantics { contentDescription = label },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(34.dp)
                        .background(
                            if (job.finished) GrowstuffColors.Green else GrowstuffColors.White,
                        )
                        .outlined()
                        .onGloballyPositioned { onTickPlaced(it.boundsInRoot().center) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (job.finished) {
                        Icon(
                            GrowstuffIcons.Tick,
                            null,
                            Modifier.size(26.dp),
                            tint = GrowstuffColors.White,
                        )
                    }
                }
            }
            VerticalRule(3.dp)
            Row(
                Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        job.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (job.finished) GrowstuffColors.Muted else GrowstuffColors.Black,
                        textDecoration = if (job.finished) TextDecoration.LineThrough else null,
                    )
                    job.link?.let { LinkChip(it) }
                }
                dayLabel(job, today)?.let { day ->
                    Text(
                        day,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 14.sp,
                        letterSpacing = 0.sp,
                        color = if (!job.finished && job.dueDate?.isBefore(today) == true) {
                            GrowstuffColors.DayOverdue
                        } else {
                            GrowstuffColors.DayGreen
                        },
                    )
                }
            }
        }
        Rule(3.dp)
    }
}

@Composable
private fun LinkChip(link: JobLink) {
    val kind = stringResource(
        when (link.kind) {
            JobLink.Kind.Planting -> R.string.link_planting
            JobLink.Kind.Garden -> R.string.link_garden
            JobLink.Kind.Seeds -> R.string.link_seeds
        },
    )
    val inGarden = link.gardenName?.let { stringResource(R.string.link_in_garden, it) }
    Text(
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp,
                ),
            ) {
                append(kind.uppercase())
            }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(link.name) }
            if (inGarden != null) {
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append(" $inGarden") }
            }
        },
        fontFamily = Raleway,
        fontSize = 13.sp,
        modifier = Modifier
            .background(GrowstuffColors.MemberChip)
            .outlined(2.dp)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun dayLabel(job: Job, today: LocalDate): String? {
    val weekday = DateTimeFormatter.ofPattern("EEE")
    val dayMonth = DateTimeFormatter.ofPattern("d MMM")
    fun near(date: LocalDate) = ChronoUnit.DAYS.between(date, today).let { it in -7..6 }
    if (job.finished) {
        val on = job.finishedOn ?: return null
        val text = when {
            on == today -> stringResource(R.string.due_today)
            near(on) -> on.format(weekday)
            else -> on.format(dayMonth)
        }
        return stringResource(R.string.done_on, text)
    }
    val due = job.dueDate ?: return null
    return when {
        due == today -> stringResource(R.string.due_today)
        near(due) -> due.format(weekday)
        else -> due.format(dayMonth)
    }
}

@Composable
private fun CrossedOffToast(job: Job, onUndo: () -> Unit, onTimeout: () -> Unit, modifier: Modifier) {
    LaunchedEffect(job.id) {
        delay(7_000)
        onTimeout()
    }
    Row(
        modifier
            .fillMaxWidth()
            .background(GrowstuffColors.Brown)
            .outlined()
            .padding(start = 16.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.crossed_off, job.name),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = GrowstuffColors.White,
            modifier = Modifier.weight(1f),
        )
        BlockButton(
            stringResource(R.string.undo),
            onClick = onUndo,
            primary = false,
            minHeight = 48.dp,
        )
    }
}

@Composable
private fun AddJobBar(onAdd: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val placeholder = stringResource(R.string.add_a_job)
    val submit = {
        onAdd(text)
        text = ""
    }
    Row(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
            .height(IntrinsicSize.Min)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() }),
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 64.dp)
                .semantics { contentDescription = placeholder },
            decorationBox = { field ->
                Box(
                    Modifier.fillMaxSize().background(
                        GrowstuffColors.White,
                    ).outlined().padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (text.isEmpty()) {
                        Text(
                            placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 17.sp,
                            color = Color(0xBF413F3B),
                        )
                    }
                    field()
                }
            },
        )
        BlockButton(
            stringResource(R.string.add),
            onClick = submit,
            modifier = Modifier.width(92.dp).fillMaxHeight(),
        )
    }
}
