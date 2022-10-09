package ru.mirea.weather_jetpack.domain.model

data class Weather (
    val dateTime: String?,
    val temp: Double?,
    val feelsLike: Double?,
    val tempMin: Double?,
    val tempMax: Double?,
    val windSpeed: Double?,
    val weatherDescription: String?,
    val iconCode: String?
)