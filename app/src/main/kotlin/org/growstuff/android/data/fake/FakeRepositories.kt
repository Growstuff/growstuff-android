// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data.fake

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.growstuff.android.data.GardensRepository
import org.growstuff.android.data.Job
import org.growstuff.android.data.JobsRepository
import org.growstuff.android.data.Member
import org.growstuff.android.data.SessionRepository
import org.growstuff.android.data.toGardens
import org.growstuff.android.data.toJobs

/** Signs in without the website: there is no OAuth on the server yet. */
class FakeSessionRepository(private val fixtures: Fixtures) : SessionRepository {
    override val member = MutableStateFlow<Member?>(null)

    override suspend fun signIn() {
        val resource = fixtures.document("member").data.first()
        member.value = Member(id = resource.id, loginName = resource.string("login_name").orEmpty())
    }

    override suspend fun signOut() {
        member.value = null
    }
}

/** Jobs from the fixtures, kept in memory, so ticking off and adding work until the app is closed. */
class FakeJobsRepository(private val fixtures: Fixtures) : JobsRepository {
    override val jobs: MutableStateFlow<List<Job>> = MutableStateFlow(
        fixtures.document("jobs").toJobs().map {
            it.copy(dueDate = fixtures.shift(it.dueDate), finishedOn = fixtures.shift(it.finishedOn))
        },
    )

    override val ideaCount: StateFlow<Int> = MutableStateFlow(fixtures.document("job_ideas").data.size)

    override suspend fun setFinished(id: String, finished: Boolean) {
        jobs.update { all ->
            all.map {
                if (it.id == id) it.copy(finished = finished, finishedOn = fixtures.today().takeIf { finished }) else it
            }
        }
    }

    override suspend fun add(name: String) {
        jobs.update { all ->
            val id = "new-${all.size + 1}"
            all + Job(id = id, name = name, dueDate = null, finished = false, finishedOn = null, link = null)
        }
    }
}

class FakeGardensRepository(private val fixtures: Fixtures) : GardensRepository {
    override val gardens = MutableStateFlow(load())

    override suspend fun refresh() {
        // Long enough to see the pull-to-refresh indicator.
        delay(600)
        gardens.value = load()
    }

    private fun load() = fixtures.document("gardens").toGardens(today = Fixtures.FIXTURE_DAY)
}
