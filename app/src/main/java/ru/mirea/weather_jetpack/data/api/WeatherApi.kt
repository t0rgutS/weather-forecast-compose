package ru.mirea.weather_jetpack.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mirea.weather_jetpack.data.api.model.ForecastResponse
import ru.mirea.weather_jetpack.data.api.model.WeatherResponse

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    @GET("/data/2.5/forecast")
    fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponse>
}