package com.demn.core.models

data class Coordinates(
    val latitude: String,
    val longitude: String
)

data class City(
    val id: Long,
    val name: String,
    val coordinates: Coordinates
)
