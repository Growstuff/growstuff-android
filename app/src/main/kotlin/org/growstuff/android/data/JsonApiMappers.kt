// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data

import java.time.LocalDate
import org.growstuff.android.data.jsonapi.JsonApiDocument
import org.growstuff.android.data.jsonapi.JsonApiResource
import org.growstuff.android.data.jsonapi.ResourceIdentifier

/** Gardens from GET /api/v1/gardens?include=plantings. */
fun JsonApiDocument.toGardens(today: LocalDate): List<Garden> = data.map { garden ->
    val growing = garden.many("plantings")
        .mapNotNull { findIncluded(it) }
        .filter { it.boolean("finished") != true }
    Garden(
        id = garden.id,
        name = garden.string("name").orEmpty(),
        location = garden.string("location"),
        area = garden.double("area"),
        areaUnit = garden.string("area_unit"),
        growingCount = growing.size,
        readyToHarvestCount = growing.count { planting ->
            planting.date("first_harvest_date")?.let { !it.isAfter(today) } == true
        },
    )
}

/** Jobs from GET /api/v1/jobs?include=garden, with the planting or seed each job is about in `included`. */
fun JsonApiDocument.toJobs(): List<Job> = data.map { job ->
    Job(
        id = job.id,
        name = job.string("name").orEmpty(),
        dueDate = job.date("due_date"),
        finished = job.boolean("finished") == true,
        finishedOn = job.date("finished_at"),
        link = linkFor(job),
    )
}

private fun JsonApiDocument.linkFor(job: JsonApiResource): JobLink? {
    val garden = job.one("garden")?.let { findIncluded(it) }
    val jobbableId = job.string("jobbable_id")
    return when (job.string("jobbable_type")) {
        "Planting" -> jobbableId?.let { findIncluded(ResourceIdentifier("plantings", it)) }?.let { planting ->
            JobLink(JobLink.Kind.Planting, planting.string("crop_name").orEmpty(), garden?.string("name"))
        }
        "Seed" -> jobbableId?.let { findIncluded(ResourceIdentifier("seeds", it)) }?.let { seed ->
            JobLink(JobLink.Kind.Seeds, seed.string("crop_name").orEmpty(), null)
        }
        else -> garden?.let { JobLink(JobLink.Kind.Garden, it.string("name").orEmpty(), null) }
    }
}
