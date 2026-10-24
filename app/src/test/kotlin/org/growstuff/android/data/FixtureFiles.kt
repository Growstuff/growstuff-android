// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data

import java.io.File
import java.time.Clock
import java.time.ZoneOffset
import org.growstuff.android.data.fake.Fixtures

/** The app's own fixtures, read from the source tree, with the clock stopped on the fixture day. */
fun testFixtures(): Fixtures = Fixtures(
    read = { name -> File("src/main/assets/$name").readText() },
    clock = Clock.fixed(Fixtures.FIXTURE_DAY.atTime(9, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC),
)
