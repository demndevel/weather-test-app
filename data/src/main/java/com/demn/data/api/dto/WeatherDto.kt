package com.demn.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("coord") var coord: Coord? = Coord(),
    @SerialName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerialName("base") var base: String? = null,
    @SerialName("main") var main: Main? = Main(),
    @SerialName("visibility") var visibility: Int? = null,
    @SerialName("wind") var wind: Wind? = Wind(),
    @SerialName("clouds") var clouds: Clouds? = Clouds(),
    @SerialName("dt") var dt: Int? = null,
    @SerialName("sys") var sys: Sys? = Sys(),
    @SerialName("timezone") var timezone: Int? = null,
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("cod") var cod: Int? = null
)

fun WeatherDto.toWeather(cityName: String): com.demn.core.models.Weather {
    return com.demn.core.models.Weather(cityName, this.main?.temp?.toInt() ?: 0)
}

@Serializable
data class Coord(
    @SerialName("lon") var lon: Double? = null,
    @SerialName("lat") var lat: Double? = null
)

@Serializable
data class Weather(
    @SerialName("id") var id: Int? = null,
    @SerialName("main") var main: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("icon") var icon: String? = null
)

@Serializable
data class Main(
    @SerialName("temp") var temp: Double? = null,
    @SerialName("feels_like") var feelsLike: Double? = null,
    @SerialName("temp_min") var tempMin: Double? = null,
    @SerialName("temp_max") var tempMax: Double? = null,
    @SerialName("pressure") var pressure: Int? = null,
    @SerialName("humidity") var humidity: Int? = null
)

@Serializable
data class Wind(
    @SerialName("speed") var speed: Double? = null,
    @SerialName("deg") var deg: Int? = null,
    @SerialName("gust") var gust: Double? = null
)

@Serializable
data class Clouds(
    @SerialName("all") var all: Int? = null
)

@Serializable
data class Sys(
    @SerialName("type") var type: Int? = null,
    @SerialName("id") var id: Int? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("sunrise") var sunrise: Int? = null,
    @SerialName("sunset") var sunset: Int? = null
)