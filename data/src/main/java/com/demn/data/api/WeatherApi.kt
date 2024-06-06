package com.demn.data.api

import com.demn.data.api.dto.WeatherDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import com.demn.core.utils.Result

interface WeatherApi {
    suspend fun getData(longitude: String, latitude: String): Result<WeatherDto>
}

private const val apiUrl = "https://api.openweathermap.org/data/2.5/weather"

class WeatherApiImpl(private val apiKey: String) : WeatherApi {
    private val client = HttpClient(CIO)

    override suspend fun getData(
        longitude: String,
        latitude: String
    ): Result<WeatherDto> {
        try {
            val response = client.get(apiUrl) {
                url {
                    parameters.append("lat", latitude)
                    parameters.append("lon", longitude)
                    parameters.append("exclude", "minutely,hourly,daily,alerts")
                    parameters.append("units", "metric")
                    parameters.append("appid", apiKey)
                }
            }

            val parser = Json { ignoreUnknownKeys = true }

            return Result.Success(parser.decodeFromString<WeatherDto>(response.bodyAsText()))
        } catch (ex: Exception) {
            return Result.Error()
        }
    }

}