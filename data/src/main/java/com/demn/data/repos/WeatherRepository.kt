package com.demn.data.repos

import com.demn.core.models.City
import com.demn.core.models.Coordinates
import com.demn.core.models.Weather
import com.demn.core.utils.Result
import com.demn.data.api.CitiesApi
import com.demn.data.api.WeatherApi
import com.demn.data.api.dto.CityDto
import com.demn.data.api.dto.toCity
import com.demn.data.api.dto.toWeather

interface WeatherRepository {
    suspend fun getAllCities(): Result<List<City>>

    suspend fun getWeatherByCity(id: Long): Result<Weather>
}

class MockWeatherRepository : WeatherRepository {
    override suspend fun getAllCities(): Result<List<City>> {
        return Result.Success(
            listOf(
                City(
                    id = 152150,
                    name = "Ростов",
                    coordinates = Coordinates(latitude = "57.2050177", longitude = "39.4378357")
                ),
                City(
                    id = 152900,
                    name = "Рыбинск",
                    coordinates = Coordinates(latitude = "58.0483802", longitude = "38.858338")
                ),
                City(
                    id = 152300,
                    name = "Тутаев",
                    coordinates = Coordinates(latitude = "57.8674237", longitude = "39.5368234")
                ),
                City(
                    id = 152610,
                    name = "Углич",
                    coordinates = Coordinates(latitude = "57.5223866", longitude = "38.3019793")
                ),
                City(
                    id = 150000,
                    name = "Ярославль",
                    coordinates = Coordinates(latitude = "57.6216145", longitude = "39.897878")
                )
            )
        )
    }

    override suspend fun getWeatherByCity(id: Long): Result<Weather> {
        return Result.Success(
            Weather(
                cityName = "Москва",
                degreesCelsius = 23
            )
        )
    }

}

class WeatherRepositoryImpl(
    private val citiesApi: CitiesApi,
    private val weatherApi: WeatherApi
) : WeatherRepository {
    override suspend fun getAllCities(): Result<List<City>> {
        val result = citiesApi.getAll()

        if (result is Result.Error) return Result.Error()

        val cities = (result as Result.Success).value

        return Result.Success(
            cities
                .mapNotNull(CityDto::toCity)
        )
    }

    override suspend fun getWeatherByCity(id: Long): Result<Weather> {
        val allCitiesResult = getAllCities()

        if (allCitiesResult is Result.Error) return Result.Error()

        val allCities = (allCitiesResult as Result.Success).value

        val city = allCities
            .find { it.id == id }

        if (city == null) return Result.Error()

        val weatherResult =
            weatherApi.getData(city.coordinates.longitude, city.coordinates.latitude)

        if (weatherResult is Result.Error) return Result.Error()

        val weather = (weatherResult as Result.Success).value

        return Result.Success(weather.toWeather(city.name))
    }
}