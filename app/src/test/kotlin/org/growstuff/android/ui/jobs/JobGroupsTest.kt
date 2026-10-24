// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import java.time.LocalDate
import org.growstuff.android.data.Job
import org.junit.Assert.assertEquals
import org.junit.Test

class JobGroupsTest {
    private val today = LocalDate.of(2026, 10, 24)

    private fun job(name: String, due: LocalDate? = null, finishedOn: LocalDate? = null) = Job(
        id = name,
        name = name,
        dueDate = due,
        finished = finishedOn != null,
        finishedOn = finishedOn,
        link = null,
    )

    private fun kindOf(due: LocalDate?) = groupToDo(listOf(job("a", due)), today).single().kind

    @Test
    fun `puts a job due before today in overdue`() {
        assertEquals(JobGroupKind.Overdue, kindOf(today.minusDays(1)))
    }

    @Test
    fun `puts a job due today in today`() {
        assertEquals(JobGroupKind.Today, kindOf(today))
    }

    @Test
    fun `puts a job due in seven days in this week`() {
        assertEquals(JobGroupKind.ThisWeek, kindOf(today.plusDays(7)))
    }

    @Test
    fun `puts a job due in eight days in later`() {
        assertEquals(JobGroupKind.Later, kindOf(today.plusDays(8)))
    }

    @Test
    fun `puts a job with no day in some day`() {
        assertEquals(JobGroupKind.SomeDay, kindOf(null))
    }

    @Test
    fun `orders the groups soonest first`() {
        val jobs =
            listOf(
                job("someday"),
                job("week", today.plusDays(3)),
                job("overdue", today.minusDays(2)),
                job("today", today),
            )
        assertEquals(
            listOf(
                JobGroupKind.Overdue,
                JobGroupKind.Today,
                JobGroupKind.ThisWeek,
                JobGroupKind.SomeDay,
            ),
            groupToDo(jobs, today).map { it.kind },
        )
    }

    @Test
    fun `orders done jobs most recent first`() {
        val jobs =
            listOf(job("old", finishedOn = today.minusDays(9)), job("new", finishedOn = today))
        assertEquals(listOf("new", "old"), groupDone(jobs).single().jobs.map { it.name })
    }

    @Test
    fun `has no groups when nothing is done`() {
        assertEquals(emptyList<JobGroup>(), groupDone(emptyList()))
    }
}
