package com.nestifff.weatherapp.model.dataClasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// TODO: create fields not null
// TODO: inner classes
class CurrentWeather {

    @Expose
    @SerializedName("weather")
    var weather: List<WeatherInfo>? = null

    @Expose
    @SerializedName("main")
    var temperatureInfo: TemperatureInfo? = null

    @Expose
    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("rain")
    var rain: Rain? = null

    @SerializedName("snow")
    var snow: Snow? = null

    @Expose
    @SerializedName("sys")
    var systemInfo: SystemInfo? = null

    @Expose
    @SerializedName("name")
    var cityName: String? = null

    @Expose
    @SerializedName("cod")
    var code: String? = null

    override fun toString(): String {
        return "CurrentWeatherInfo\n(weather=$weather, temperatureInfo=$temperatureInfo, \nwind=$wind, rain=$rain, \nsnow=$snow, systemInfo=$systemInfo, cityName=$cityName, code=$code)"
    }

    class Rain {
        @Expose
        @SerializedName("1h")
        var h1: String? = null
    }

    class Snow {
        @Expose
        @SerializedName("1h")
        var h1: String? = null
    }

    class SystemInfo {

        @Expose
        @SerializedName("country")
        var country: String? = null

        @Expose
        @SerializedName("sunrise")
        var sunrise: String? = null

        @Expose
        @SerializedName("sunset")
        var sunset: String? = null

        override fun toString(): String {
            return "SystemInfo(country=$country, sunrise=$sunrise, sunset=$sunset)"
        }

    }

    class Wind {

        @Expose
        @SerializedName("speed")
        var speed: String? = null

        @Expose
        @SerializedName("deg")
        var deg: String? = null


        override fun toString(): String {
            return "Wind(speed=$speed, deg=$deg)"
        }

    }

}