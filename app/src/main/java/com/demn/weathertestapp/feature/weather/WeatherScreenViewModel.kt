package com.demn.weathertestapp.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demn.core.models.Weather
import com.demn.core.utils.Result
import com.demn.data.repos.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface WeatherScreenUiState {
    data class Loaded(
        val weather: Weather
    ) : WeatherScreenUiState

    data object NoData : WeatherScreenUiState

    data object Loading : WeatherScreenUiState

    data object Error : WeatherScreenUiState
}

private data class WeatherScreenVmState(
    val isLoading: Boolean,
    val isError: Boolean,
    val weather: Weather? = null
) {
    fun toUiState(): WeatherScreenUiState {
        if (isLoading) return WeatherScreenUiState.Loading
        if (isError) return WeatherScreenUiState.Error
        return if (weather != null) WeatherScreenUiState.Loaded(
            weather
        ) else return WeatherScreenUiState.NoData
    }
}

class WeatherScreenViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        WeatherScreenVmState(
            isLoading = false,
            isError = false,
            weather = null
        )
    )
    val state = _state
        .map(WeatherScreenVmState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, WeatherScreenUiState.NoData)

    fun loadData(cityId: Long) {
        startLoading()

        viewModelScope.launch {
            val weatherResult = weatherRepository.getWeatherByCity(cityId)

            if (weatherResult is Result.Error) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }

                return@launch
            }

            val weather = (weatherResult as Result.Success).value

            _state.update {
                it.copy(
                    isLoading = false,
                    isError = false,
                    weather = weather
                )
            }
        }
    }

    private fun startLoading() {
        _state.update {
            it.copy(isLoading = true)
        }
    }
}