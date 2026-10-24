// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.data.jsonapi

import java.time.LocalDate
import java.time.OffsetDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** A JSON:API document, as served by the Growstuff API under /api/v1. */
@Serializable
data class JsonApiDocument(
    val data: List<JsonApiResource>,
    val included: List<JsonApiResource> = emptyList(),
) {
    fun findIncluded(identifier: ResourceIdentifier): JsonApiResource? =
        included.find { it.type == identifier.type && it.id == identifier.id }

    companion object {
        val json = Json { ignoreUnknownKeys = true }

        fun parse(text: String): JsonApiDocument = json.decodeFromString(serializer(), text)
    }
}

@Serializable
data class JsonApiResource(
    val type: String,
    val id: String,
    val attributes: JsonObject = JsonObject(emptyMap()),
    val relationships: Map<String, JsonApiRelationship> = emptyMap(),
) {
    fun string(name: String): String? = attribute(name)?.jsonPrimitive?.contentOrNull

    fun boolean(name: String): Boolean? = attribute(name)?.jsonPrimitive?.booleanOrNull

    fun double(name: String): Double? = attribute(name)?.jsonPrimitive?.doubleOrNull

    /** Reads a date attribute, which the API sends either as a date or as a timestamp. */
    fun date(name: String): LocalDate? {
        val value = string(name) ?: return null
        return if (value.length == 10) LocalDate.parse(value) else OffsetDateTime.parse(value).toLocalDate()
    }

    fun one(name: String): ResourceIdentifier? = relationships[name]?.identifiers()?.firstOrNull()

    fun many(name: String): List<ResourceIdentifier> = relationships[name]?.identifiers().orEmpty()

    private fun attribute(name: String): JsonElement? = attributes[name]?.takeUnless { it is JsonNull }
}

@Serializable
data class JsonApiRelationship(
    val data: JsonElement? = null,
) {
    fun identifiers(): List<ResourceIdentifier> = when (val linkage = data) {
        is JsonArray -> linkage.map { it.toIdentifier() }
        is JsonObject -> listOf(linkage.toIdentifier())
        else -> emptyList()
    }

    private fun JsonElement.toIdentifier(): ResourceIdentifier {
        val fields = jsonObject
        return ResourceIdentifier(
            type = fields.getValue("type").jsonPrimitive.content,
            id = fields.getValue("id").jsonPrimitive.content,
        )
    }
}

data class ResourceIdentifier(
    val type: String,
    val id: String,
)
