package com.demn.weathertestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.BuildCompat
import com.demn.core.di.coreModule
import com.demn.data.api.CitiesApiImpl
import com.demn.data.api.WeatherApiImpl
import com.demn.data.di.dataModule
import com.demn.weathertestapp.di.appModule
import com.demn.weathertestapp.feature.home.HomeScreen
import com.demn.weathertestapp.navigation.NavigationHost
import com.demn.weathertestapp.ui.theme.WeatherTestAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                modules(coreModule, dataModule, appModule)
            }
        }

        enableEdgeToEdge()

        setContent {
            WeatherTestAppTheme {
                Surface {
                    NavigationHost(
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}