package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// TODO: create fields not null
class CurrentWeatherInfo {

    @Expose
    @SerializedName("weather")
    var weather: List<Weather>? = null

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

}