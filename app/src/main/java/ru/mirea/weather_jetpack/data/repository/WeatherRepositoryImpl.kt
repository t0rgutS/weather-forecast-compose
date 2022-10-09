package ru.mirea.weather_jetpack.data.repository

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mirea.weather_jetpack.data.api.OpenWeatherApiClient
import ru.mirea.weather_jetpack.data.api.model.ForecastResponse
import ru.mirea.weather_jetpack.data.api.model.WeatherResponse
import ru.mirea.weather_jetpack.domain.model.Weather
import ru.mirea.weather_jetpack.domain.repository.WeatherRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors

class WeatherRepositoryImpl : WeatherRepository {
    private val API_KEY: String = "9b8f651f55e5261e497efd351f97b8e9"
    private val DEFAULT_DATE_FORMAT = "dd.MM.yy HH:mm"
    private val DISPLAY_DATE_FORMAT = "dd.MM.yy"
    private val DISPLAY_HOUR_FORMAT = "HH:mm"
    private val DEFAULT_UNITS = "metric"

    private val AVAILABLE_CITIES = Arrays.asList(
        "Moscow", "St. Petersburg", "Penza", "Kaliningrad",
        "Novosibirsk", "Izhevsk"
    )

    private fun mapWeatherResponse(
        weatherResponse: WeatherResponse,
        dateFormat: String = DEFAULT_DATE_FORMAT
    ): Weather {
        val currentWeatherData = weatherResponse.weather[0]
        return Weather(
            weatherResponse.dt?.toLong()?.let {
                LocalDateTime.ofEpochSecond(
                    it, 0,
                    ZoneOffset.UTC
                ).format(
                    DateTimeFormatter
                        .ofPattern(dateFormat)
                )
            },
            weatherResponse.main?.temp,
            weatherResponse.main?.feelsLike,
            weatherResponse.main?.tempMin,
            weatherResponse.main?.tempMax,
            weatherResponse.wind?.speed,
            currentWeatherData.main,
            currentWeatherData.icon
        )
    }

    override fun getCurrentWeather(city: String): Flow<Weather> = callbackFlow {
        val call = OpenWeatherApiClient.getApi().getCurrentWeather(
            city, DEFAULT_UNITS,
            API_KEY
        )
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        trySend(mapWeatherResponse(it))
                    }
                    close()
                } else {
                    close(
                        Exception(
                            "Получение данных завершилось с кодом ${response.code()}: " +
                                    "${response.errorBody()?.string()}"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                close(t)
            }

        })
        awaitClose { call.cancel() }
    }

    override fun getHourlyForecast(city: String): Flow<List<Weather>> = callbackFlow {
        val call = OpenWeatherApiClient.getApi().getForecast(city, DEFAULT_UNITS, API_KEY)
        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.isSuccessful) {
                    val forecastResponse = response.body()
                    val forecasts = mutableListOf<Weather>()
                    val nowPlus = LocalDateTime.now().plus(12, ChronoUnit.HOURS)
                    forecastResponse?.list?.stream()
                        ?.filter {
                            it?.dt != null && LocalDateTime.ofEpochSecond(
                                it.dt.toLong(), 0,
                                ZoneOffset.UTC
                            ).isBefore(nowPlus)
                        }
                        ?.forEach { forecasts.add(mapWeatherResponse(it, DISPLAY_HOUR_FORMAT)) }
                    trySend(forecasts)
                    close()
                } else {
                    close(
                        Exception(
                            "Получение данных завершилось с кодом ${response.code()}: " +
                                    "${response.errorBody()?.string()}"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                close(t)
            }

        })
        awaitClose { call.cancel() }
    }

    override fun getDailyForecast(city: String): Flow<List<Weather>> = callbackFlow {
        val call = OpenWeatherApiClient.getApi().getForecast(city, DEFAULT_UNITS, API_KEY)
        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.isSuccessful) {
                    val forecastResponse = response.body()
                    val forecasts = mutableListOf<Weather>()

                    val forecastsByDate = forecastResponse?.list?.stream()
                        ?.filter {
                            it?.dt != null && LocalDateTime.ofEpochSecond(
                                it.dt.toLong(), 0,
                                ZoneOffset.UTC
                            ).toLocalDate().isAfter(LocalDate.now())
                        }
                        ?.map { mapWeatherResponse(it, DISPLAY_DATE_FORMAT) }
                        ?.collect(Collectors.toList())?.groupBy { it.dateTime }
                        ?.entries?.forEach { entry ->
                            val date = entry.key
                            val temp = average(entry.value.map { it.temp })
                            val feelsLike = average(entry.value.map { it.feelsLike })
                            val tempMin = average(entry.value.map { it.tempMin })
                            val tempMax = average(entry.value.map { it.tempMax })
                            val windSpeed = average(entry.value.map { it.windSpeed })
                            val weatherDescription =
                                entry.value.groupingBy { it.weatherDescription }
                                    .eachCount().maxByOrNull { it.value }?.key
                            val iconCode = entry.value.groupingBy { it.iconCode }
                                .eachCount().maxByOrNull { it.value }?.key
                            forecasts.add(
                                Weather(
                                    date,
                                    temp,
                                    feelsLike,
                                    tempMin,
                                    tempMax,
                                    windSpeed,
                                    weatherDescription,
                                    iconCode
                                )
                            )
                        }
                    trySend(forecasts)
                    close()
                } else {
                    close(
                        Exception(
                            "Получение данных завершилось с кодом ${response.code()}: " +
                                    "${response.errorBody()?.string()}"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                close(t)
            }

        })
        awaitClose { call.cancel() }
    }

    override fun getAvailableCities(): Flow<List<String>> = flow {
        emit(AVAILABLE_CITIES)
    }

    private fun average(list: List<Double?>): Double {
        var sum = 0.0
        list.forEach { if (it != null) sum += it }
        return sum / list.size
    }
}