package ru.mirea.weather_jetpack.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherApiClient {
    private val BASE_URL: String = "https://api.openweathermap.org"
    private var retrofit: Retrofit? = null
    private var api: WeatherApi? = null

    fun getApi(): WeatherApi {
        if(api == null) {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            api = retrofit?.create(WeatherApi::class.java)
        }
        return api!!
    }
}