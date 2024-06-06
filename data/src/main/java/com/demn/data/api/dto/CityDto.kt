package com.demn.data.api.dto

import com.demn.core.models.City
import com.demn.core.models.Coordinates
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val id: String,
    val city: String,
    val latitude: String,
    val longitude: String
)

internal fun CityDto.toCity(): City? {
    if (this.id.toLongOrNull() == null) return null
    if (this.city.isBlank()) return null
    if (this.longitude.isBlank()) return null
    if (this.latitude.isBlank()) return null

    return City(
        name = this.city,
        id = this.id.toLong(),
        coordinates = Coordinates(
            latitude = this.latitude,
            longitude = this.longitude
        )
    )
}