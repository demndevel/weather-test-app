package com.demn.data.di

import com.demn.data.api.CitiesApi
import com.demn.data.api.CitiesApiImpl
import com.demn.data.api.WeatherApi
import com.demn.data.api.WeatherApiImpl
import com.demn.data.repos.WeatherRepository
import com.demn.data.repos.WeatherRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    factory<CitiesApi> { CitiesApiImpl() }

    factory<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}