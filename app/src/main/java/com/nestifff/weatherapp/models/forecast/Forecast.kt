package com.nestifff.weatherapp.models.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.nestifff.weatherapp.models.weather.TemperatureInfo
import com.nestifff.weatherapp.models.weather.Weather

class Forecast {

    @Expose
    @SerializedName("weather")
    lateinit var weather: List<Weather>

    @Expose
    @SerializedName("main")
    lateinit var temperatureInfo: TemperatureInfo

    @Expose
    @SerializedName("dt")
    lateinit var date: String


    override fun toString(): String {
        return "Forecast(weather=$weather, temperatureInfo=$temperatureInfo, date=$date)"
    }

}
