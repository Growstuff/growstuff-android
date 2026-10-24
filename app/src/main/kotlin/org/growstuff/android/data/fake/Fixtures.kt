// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data.fake

import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.growstuff.android.data.jsonapi.JsonApiDocument

/**
 * Canned API responses for endpoints the app can't call yet, kept in `assets/fixtures`.
 *
 * The fixtures are written as if today were [FIXTURE_DAY]; [shift] moves their dates
 * so that "today" in the fixtures is today on the phone.
 */
class Fixtures(
    private val read: (name: String) -> String,
    private val clock: Clock,
) {
    fun document(name: String): JsonApiDocument = JsonApiDocument.parse(read("fixtures/$name.json"))

    fun today(): LocalDate = LocalDate.now(clock)

    fun shift(date: LocalDate?): LocalDate? = date?.plusDays(ChronoUnit.DAYS.between(FIXTURE_DAY, today()))

    companion object {
        val FIXTURE_DAY: LocalDate = LocalDate.of(2026, 10, 24)
    }
}
