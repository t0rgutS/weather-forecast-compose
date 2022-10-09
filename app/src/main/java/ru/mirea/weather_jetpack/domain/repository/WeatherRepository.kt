package ru.mirea.weather_jetpack.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.weather_jetpack.domain.model.Weather

interface WeatherRepository {
    fun getCurrentWeather(city: String): Flow<Weather>

    fun getHourlyForecast(city: String): Flow<List<Weather>>

    fun getDailyForecast(city: String): Flow<List<Weather>>

    fun getAvailableCities(): Flow<List<String>>
}