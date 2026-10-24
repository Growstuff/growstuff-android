// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.jobs

import java.time.Clock
import java.time.ZoneOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.growstuff.android.data.fake.FakeJobsRepository
import org.growstuff.android.data.fake.Fixtures
import org.growstuff.android.data.testFixtures
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JobsViewModelTest {
    private val repository = FakeJobsRepository(testFixtures())
    private val clock = Clock.fixed(
        Fixtures.FIXTURE_DAY.atTime(9, 0).toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC,
    )
    private val viewModel by lazy { JobsViewModel(repository, clock) }

    private val compost get() = repository.jobs.value.single { it.name == "Order more compost" }

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Keeps the state flowing, as the screen does while it's open. */
    private fun TestScope.collectState() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect {} }
    }

    private fun shownToDo() = viewModel.state.value.groups.flatMap { it.jobs }.map { it.name }

    @Test
    fun `keeps a crossed-off job on the to do list until the tab changes`() = runTest {
        collectState()
        viewModel.setFinished(compost, true)
        assertTrue("Order more compost" in shownToDo())
    }

    @Test
    fun `moves a crossed-off job off the to do list when the tab changes`() = runTest {
        collectState()
        viewModel.setFinished(compost, true)
        viewModel.showTab(JobsTab.Done)
        viewModel.showTab(JobsTab.ToDo)
        assertFalse("Order more compost" in shownToDo())
    }

    @Test
    fun `offers the crossed-off job for undo`() = runTest {
        collectState()
        viewModel.setFinished(compost, true)
        assertEquals("Order more compost", viewModel.state.value.crossedOff?.name)
    }

    @Test
    fun `puts the job back on undo`() = runTest {
        collectState()
        viewModel.setFinished(compost, true)
        viewModel.undoCrossOff()
        assertFalse(compost.finished)
    }

    @Test
    fun `adds a job with no day`() = runTest {
        viewModel.add("  Turn the compost  ")
        assertEquals(null, repository.jobs.first().single { it.name == "Turn the compost" }.dueDate)
    }

    @Test
    fun `ignores a blank new job`() = runTest {
        val before = repository.jobs.value.size
        viewModel.add("   ")
        assertEquals(before, repository.jobs.value.size)
    }
}
