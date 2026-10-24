// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data

import java.time.LocalDate
import org.growstuff.android.data.fake.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class JsonApiMappersTest {
    private val fixtures = testFixtures()
    private val gardens = fixtures.document("gardens").toGardens(Fixtures.FIXTURE_DAY)
    private val jobs = fixtures.document("jobs").toJobs()

    private fun garden(name: String) = gardens.single { it.name == name }

    private fun job(name: String) = jobs.single { it.name == name }

    @Test
    fun `counts plantings that are not finished as growing`() {
        assertEquals(4, garden("Bed 1, the herb bed").growingCount)
    }

    @Test
    fun `counts growing plantings past their first harvest date as ready`() {
        assertEquals(1, garden("Bed 2, the sunny one").readyToHarvestCount)
    }

    @Test
    fun `counts nothing growing in a garden with only finished plantings`() {
        assertEquals(0, garden("Bed 3, by the fence").growingCount)
    }

    @Test
    fun `reads the due date`() {
        assertEquals(LocalDate.of(2026, 10, 20), job("Fix the hose tap").dueDate)
    }

    @Test
    fun `reads the day a job was finished from its timestamp`() {
        assertEquals(LocalDate.of(2026, 10, 23), job("Net the strawberries").finishedOn)
    }

    @Test
    fun `links a job to its planting and the planting's garden`() {
        assertEquals(
            JobLink(JobLink.Kind.Planting, "sunflower", "Bed 2, the sunny one"),
            job("Plant out the sunflowers").link,
        )
    }

    @Test
    fun `links a job to seeds`() {
        assertEquals(JobLink(JobLink.Kind.Seeds, "broad bean", null), job("Sow the broad beans").link)
    }

    @Test
    fun `leaves a job without a link when it is about nothing in particular`() {
        assertNull(job("Order more compost").link)
    }
}
