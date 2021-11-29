package com.nestifff.weatherapp.model.dataClasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Forecast {

    @Expose
    @SerializedName("weather")
    lateinit var weather: List<WeatherInfo>

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
