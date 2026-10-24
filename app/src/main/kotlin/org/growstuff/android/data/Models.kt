// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data

import java.time.LocalDate

data class Member(
    val id: String,
    val loginName: String,
)

data class Garden(
    val id: String,
    val name: String,
    val location: String?,
    val area: Double?,
    val areaUnit: String?,
    val growingCount: Int,
    val readyToHarvestCount: Int,
)

data class Job(
    val id: String,
    val name: String,
    val dueDate: LocalDate?,
    val finished: Boolean,
    val finishedOn: LocalDate?,
    val link: JobLink?,
)

/** What a job is about: a planting, a garden, or some seeds. */
data class JobLink(
    val kind: Kind,
    val name: String,
    val gardenName: String?,
) {
    enum class Kind { Planting, Garden, Seeds }
}
