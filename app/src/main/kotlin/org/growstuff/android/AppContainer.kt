// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android

import android.content.Context
import java.time.Clock
import org.growstuff.android.data.GardensRepository
import org.growstuff.android.data.JobsRepository
import org.growstuff.android.data.SessionRepository
import org.growstuff.android.data.fake.FakeGardensRepository
import org.growstuff.android.data.fake.FakeJobsRepository
import org.growstuff.android.data.fake.FakeSessionRepository
import org.growstuff.android.data.fake.Fixtures

/** Holds the app's repositories. Every one is a fake until the API can be signed in to (#2). */
class AppContainer(context: Context) {
    val clock: Clock = Clock.systemDefaultZone()

    private val fixtures = Fixtures(
        read = { name -> context.assets.open(name).bufferedReader().use { it.readText() } },
        clock = clock,
    )

    val session: SessionRepository = FakeSessionRepository(fixtures)
    val jobs: JobsRepository = FakeJobsRepository(fixtures)
    val gardens: GardensRepository = FakeGardensRepository(fixtures)
}
