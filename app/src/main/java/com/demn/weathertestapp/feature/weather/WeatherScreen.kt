package com.demn.weathertestapp.feature.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demn.core.models.Weather
import com.demn.data.repos.MockWeatherRepository
import com.demn.weathertestapp.R
import com.demn.weathertestapp.ui.theme.WeatherTestAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    cityId: Long,
    modifier: Modifier = Modifier,
    vm: WeatherScreenViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadData(cityId)
    }

    Box(
        modifier = modifier
            .padding(top = 40.dp, bottom = 36.dp)
            .padding(horizontal = 16.dp)
    ) {
        when (state) {
            is WeatherScreenUiState.Loaded -> {
                LoadedState(
                    state as WeatherScreenUiState.Loaded,
                    onRefresh = {
                        vm.loadData(cityId)
                    }
                )
            }

            is WeatherScreenUiState.Error, WeatherScreenUiState.NoData -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.error_occured))

                    Spacer(modifier = Modifier.height(42.dp))

                    Button(onClick = { vm.loadData(cityId) }) {
                        Text(stringResource(R.string.refresh))
                    }
                }
            }

            is WeatherScreenUiState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .width(48.dp)
                )
            }
        }
    }
}

@Composable
private fun BoxScope.LoadedState(
    state: WeatherScreenUiState.Loaded,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier.Companion.align(Alignment.TopCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${state.weather.degreesCelsius}ºC",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = state.weather.cityName,
            style = MaterialTheme.typography.titleMedium
        )
    }

    Button(
        onClick = onRefresh,
        modifier = Modifier.Companion.align(Alignment.BottomCenter)
    ) {
        Text(
            text = stringResource(R.string.refresh),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun WeatherScreenPreview(
    modifier: Modifier = Modifier
) {
    WeatherTestAppTheme {
        Surface(Modifier.fillMaxSize()) {
            WeatherScreen(
                0,
                Modifier.fillMaxSize(),
                vm = WeatherScreenViewModel(MockWeatherRepository()),
            )
        }
    }
}