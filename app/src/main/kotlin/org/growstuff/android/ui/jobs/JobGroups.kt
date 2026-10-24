// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import java.time.LocalDate
import org.growstuff.android.data.Job

enum class JobGroupKind { Overdue, Today, ThisWeek, Later, SomeDay, Done }

data class JobGroup(val kind: JobGroupKind, val jobs: List<Job>)

/** Jobs still to do, by when they are due, soonest first. Empty groups are left out. */
fun groupToDo(jobs: List<Job>, today: LocalDate): List<JobGroup> {
    val weekEnd = today.plusDays(7)
    return jobs
        .groupBy { job ->
            val due = job.dueDate
            when {
                due == null -> JobGroupKind.SomeDay
                due.isBefore(today) -> JobGroupKind.Overdue
                due == today -> JobGroupKind.Today
                !due.isAfter(weekEnd) -> JobGroupKind.ThisWeek
                else -> JobGroupKind.Later
            }
        }
        .map { (kind, grouped) ->
            JobGroup(kind, grouped.sortedWith(compareBy(nullsLast()) { it.dueDate }))
        }
        .sortedBy { it.kind }
}

/** Jobs that are done, most recently done first. */
fun groupDone(jobs: List<Job>): List<JobGroup> = if (jobs.isEmpty()) {
    emptyList()
} else {
    listOf(
        JobGroup(
            JobGroupKind.Done,
            jobs.sortedWith(
                compareByDescending(nullsFirst()) {
                    it.finishedOn
                },
            ),
        ),
    )
}
