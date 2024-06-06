package com.demn.weathertestapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demn.core.models.City
import com.demn.core.utils.Result
import com.demn.data.repos.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Loaded(
        val cities: List<City>
    ) : HomeUiState

    data object Loading : HomeUiState

    data object NoData : HomeUiState
}

private data class HomeVmState(
    val cities: List<City>? = null,
    val isLoading: Boolean
) {
    fun toUiState(): HomeUiState {
        if (isLoading) return HomeUiState.Loading
        if (cities.isNullOrEmpty()) return HomeUiState.NoData

        return HomeUiState.Loaded(cities)
    }
}


class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeVmState(isLoading = false))

    val state = _state
        .map(HomeVmState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            HomeUiState.NoData
        )

    fun loadData() {
        startLoading()

        viewModelScope.launch {
            val citiesResult = weatherRepository
                .getAllCities()

            if (citiesResult is Result.Error) {
                _state.update {
                    it.copy(
                        cities = null,
                        isLoading = false
                    )
                }

                return@launch
            }

            val cities = (citiesResult as Result.Success).value

            _state.update { state ->
                state.copy(
                    cities = cities
                        .sortedBy { it.name },
                    isLoading = false
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