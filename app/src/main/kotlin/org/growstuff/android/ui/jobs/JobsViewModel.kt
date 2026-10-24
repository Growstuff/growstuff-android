// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.growstuff.android.data.Job
import org.growstuff.android.data.JobsRepository

enum class JobsTab { ToDo, Done }

data class JobsUiState(
    val tab: JobsTab = JobsTab.ToDo,
    val groups: List<JobGroup> = emptyList(),
    val toDoCount: Int = 0,
    val doneCount: Int = 0,
    val ideaCount: Int = 0,
    val today: LocalDate = LocalDate.MIN,
    /** The job just crossed off, offered for undo. */
    val crossedOff: Job? = null,
)

private data class LocalState(
    val tab: JobsTab = JobsTab.ToDo,
    /** Jobs ticked or unticked since the tab was opened. They stay put until the tab changes. */
    val moved: Set<String> = emptySet(),
    val crossedOffId: String? = null,
)

class JobsViewModel(private val repository: JobsRepository, private val clock: Clock) : ViewModel() {
    private val local = MutableStateFlow(LocalState())

    val state: StateFlow<JobsUiState> = combine(repository.jobs, repository.ideaCount, local) {
            jobs,
            ideas,
            local,
        ->
        val today = LocalDate.now(clock)
        val shown = jobs.filter { job ->
            val stays = job.id in local.moved
            when (local.tab) {
                JobsTab.ToDo -> !job.finished || stays
                JobsTab.Done -> job.finished || stays
            }
        }
        JobsUiState(
            tab = local.tab,
            groups = if (local.tab == JobsTab.ToDo) groupToDo(shown, today) else groupDone(shown),
            toDoCount = jobs.count { !it.finished },
            doneCount = jobs.count { it.finished },
            ideaCount = ideas,
            today = today,
            crossedOff = jobs.find { it.id == local.crossedOffId && it.finished },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), JobsUiState())

    fun showTab(tab: JobsTab) {
        local.value = LocalState(tab = tab)
    }

    fun setFinished(job: Job, finished: Boolean) {
        local.update {
            it.copy(moved = it.moved + job.id, crossedOffId = if (finished) job.id else null)
        }
        viewModelScope.launch { repository.setFinished(job.id, finished) }
    }

    fun undoCrossOff() {
        val id = local.value.crossedOffId ?: return
        local.update { it.copy(crossedOffId = null) }
        viewModelScope.launch { repository.setFinished(id, false) }
    }

    fun dismissCrossOff() {
        local.update { it.copy(crossedOffId = null) }
    }

    fun add(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repository.add(trimmed) }
    }
}
