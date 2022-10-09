package ru.mirea.weather_jetpack.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mirea.weather_jetpack.data.repository.WeatherRepositoryImpl
import ru.mirea.weather_jetpack.domain.model.Weather
import ru.mirea.weather_jetpack.domain.repository.WeatherRepository

class WeatherViewModel constructor(
    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl()
) : ViewModel() {
    private val DEFAULT_CITY = "Moscow"
    private val mutableWeather: MutableStateFlow<Weather?> = MutableStateFlow(null)
    private val mutableHourlyForecast: MutableStateFlow<List<Weather>> =
        MutableStateFlow(emptyList())
    private val mutableDailyForecast: MutableStateFlow<List<Weather>> =
        MutableStateFlow(emptyList())
    private val mutableCityList: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val mutableCurrentCity: MutableStateFlow<String> = MutableStateFlow(DEFAULT_CITY)
    val currentWeather: StateFlow<Weather?> = mutableWeather
    val currentHourlyForecast: StateFlow<List<Weather>> = mutableHourlyForecast
    val currentDailyForecast: StateFlow<List<Weather>> = mutableDailyForecast
    val availableCities: StateFlow<List<String>> = mutableCityList
    val currentCity: StateFlow<String> = mutableCurrentCity

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather(currentCity.value).collect { weather ->
                mutableWeather.value = weather
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getHourlyForecast(currentCity.value).collect { forecast ->
                mutableHourlyForecast.value = forecast
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getDailyForecast(currentCity.value).collect { forecast ->
                mutableDailyForecast.value = forecast
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getAvailableCities().collect { cities ->
                mutableCityList.value = cities;
            }
        }
    }

    fun applySelectedCity(selectedCity: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableCurrentCity.value = selectedCity
            refreshData()
        }
    }
}