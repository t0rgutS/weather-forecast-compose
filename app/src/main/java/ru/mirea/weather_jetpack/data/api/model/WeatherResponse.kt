package ru.mirea.weather_jetpack.data.api.model

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("dt")
    val dt: Int? = null

    @SerializedName("main")
    val main: Main? = null

    @SerializedName("weather")
    val weather: List<Weather> = ArrayList()

    @SerializedName("clouds")
    val clouds: Clouds? = null

    @SerializedName("wind")
    val wind: Wind? = null

    @SerializedName("visibility")
    val visibility: Int? = null

    @SerializedName("pop")
    val pop: Double? = null

    @SerializedName("sys")
    val sys: Sys? = null

    @SerializedName("dt_txt")
    val dtTxt: String? = null

    @SerializedName("rain")
    val rain: Rain? = null

    class Main {
        @SerializedName("temp")
        val temp: Double? = null

        @SerializedName("feels_like")
        val feelsLike: Double? = null

        @SerializedName("temp_min")
        val tempMin: Double? = null

        @SerializedName("temp_max")
        val tempMax: Double? = null

        @SerializedName("pressure")
        val pressure: Double? = null

        @SerializedName("sea_level")
        val seaLevel: Double? = null

        @SerializedName("grnd_level")
        val grndLevel: Double? = null

        @SerializedName("humidity")
        val humidity: Double? = null

        @SerializedName("temp_kf")
        val tempKf: Double? = null
    }

    class Weather {
        val id: Int? = null
        val main: String? = null
        val description: String? = null
        val icon: String? = null
    }

    class Clouds {
        @SerializedName("all")
        val all: Int? = null
    }

    class Wind {
        val speed: Double? = null
        val deg: Double? = null
        val gust: Double? = null
    }

    class Sys {
        @SerializedName("pod")
        val pod: String? = null
    }

    class Rain {
        @SerializedName("3h")
        val _3h: Double? = null
    }
}