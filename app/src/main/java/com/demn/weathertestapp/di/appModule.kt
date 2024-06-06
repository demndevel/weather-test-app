package com.demn.weathertestapp.di

import com.demn.data.api.WeatherApi
import com.demn.data.api.WeatherApiImpl
import com.demn.weathertestapp.BuildConfig
import com.demn.weathertestapp.feature.home.HomeViewModel
import com.demn.weathertestapp.feature.weather.WeatherScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { WeatherScreenViewModel(get()) }

    factory<WeatherApi> { WeatherApiImpl(BuildConfig.OPENWEATHER_API_KEY) }
}