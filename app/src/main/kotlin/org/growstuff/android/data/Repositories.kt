// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data

import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    /** The signed-in member, or null when nobody is signed in. */
    val member: StateFlow<Member?>

    suspend fun signIn()

    suspend fun signOut()
}

interface JobsRepository {
    val jobs: StateFlow<List<Job>>

    /** How many suggested jobs there are for this week. */
    val ideaCount: StateFlow<Int>

    suspend fun setFinished(id: String, finished: Boolean)

    suspend fun add(name: String)
}

interface GardensRepository {
    val gardens: StateFlow<List<Garden>>

    suspend fun refresh()
}
