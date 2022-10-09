package ru.mirea.weather_jetpack.data.api.model

import com.google.gson.annotations.SerializedName

class ForecastResponse {
    @SerializedName("cod")
    val cod: String? = null

    @SerializedName("message")
    val message: Int? = null

    @SerializedName("cnt")
    val cnt: Int? = null

    @SerializedName("list")
    val list: List<WeatherResponse> = ArrayList()

    @SerializedName("city")
    val city: City? = null

    class City {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("coord")
        val coord: Coord? = null

        @SerializedName("country")
        val country: String? = null

        @SerializedName("population")
        val population: Int? = null

        @SerializedName("timezone")
        val timezone: Int? = null

        @SerializedName("sunrise")
        val sunrise: Int? = null

        @SerializedName("sunset")
        val sunset: Int? = null
    }

    class Coord {
        @SerializedName("lat")
        val lat: Double? = null

        @SerializedName("lon")
        val lon: Double? = null
    }
}