package com.demn.data.api

import com.demn.core.utils.Result
import com.demn.data.api.dto.CityDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

private const val apiUrl =
    "https://gist.githubusercontent.com/Stronger197/764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/cities.json"

interface CitiesApi {
    suspend fun getAll(): Result<List<CityDto>>
}

class CitiesApiImpl : CitiesApi {
    private val client = HttpClient(CIO)

    override suspend fun getAll(): Result<List<CityDto>> {
        try {
            val response = client.get(apiUrl)

            if (!response.status.isSuccess()) return Result.Error()

            val cities = Json.decodeFromString<List<CityDto>>(response.bodyAsText())

            return Result.Success(cities)
        } catch (ex: Exception) {
            return Result.Error()
        }
    }
}