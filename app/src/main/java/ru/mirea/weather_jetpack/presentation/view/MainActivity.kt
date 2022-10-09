package ru.mirea.weather_jetpack.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.mirea.weather_jetpack.R
import ru.mirea.weather_jetpack.presentation.view.theme.WeatherJetpackTheme
import ru.mirea.weather_jetpack.presentation.viewModel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        lifecycleScope.launch {
            // repeatOnLifecycle(Lifecycle.State.STARTED) {
            //   weatherViewModel.currentWeather.collect {
            setContent {
                val currentWeather = weatherViewModel.currentWeather.collectAsState()
                val hourlyForecat = weatherViewModel.currentHourlyForecast.collectAsState()
                val dailyForecast = weatherViewModel.currentDailyForecast.collectAsState()
                val availableCities = weatherViewModel.availableCities.collectAsState()
                val currentCity = weatherViewModel.currentCity.collectAsState()
                val navController = rememberNavController()
                WeatherJetpackTheme {
                    Image(
                        painter = painterResource(
                            id = R.drawable.weather_bg
                        ),
                        contentDescription = "background",
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.5f),
                        contentScale = ContentScale.FillBounds
                    )
                    NavHost(
                        navController = navController,
                        startDestination = "forecast"
                    ) {
                        composable("forecast") {
                            Column {
                                MainCard(
                                    currentCity.value,
                                    currentWeather.value,
                                    { weatherViewModel.refreshData() },
                                    navController
                                )
                                TabLayout(hourlyForecat.value, dailyForecast.value)
                            }
                        }
                        composable("settings") {
                            CityChoiceCard(
                                availableCities.value,
                                currentCity.value,
                                weatherViewModel::applySelectedCity,
                                navController
                            )
                        }
                    }
                }
            }
            // }
            //  }
        }

    }
}