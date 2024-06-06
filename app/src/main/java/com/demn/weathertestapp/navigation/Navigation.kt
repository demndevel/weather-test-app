package com.demn.weathertestapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demn.weathertestapp.feature.home.HomeScreen
import com.demn.weathertestapp.feature.weather.WeatherScreen

enum class Routes(val route: String) {
    Home("home"),
    Weather("weather")
}

@Composable
fun NavigationHost(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cityIdArgument = "cityId"
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                onCityClick = { id -> navController.navigate("${Routes.Weather}/$id") },
                modifier.fillMaxSize()
            )
        }

        composable(
            route = "${Routes.Weather.route}/{$cityIdArgument}",
            arguments = listOf(navArgument(cityIdArgument) { type = NavType.LongType })
        ) { backStackEntry ->
            WeatherScreen(
                cityId = backStackEntry.arguments?.getLong(cityIdArgument) ?: 0,
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )
        }
    }
}