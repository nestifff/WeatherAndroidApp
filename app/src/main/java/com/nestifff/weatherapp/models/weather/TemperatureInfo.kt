package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TemperatureInfo {

    @Expose
    @SerializedName("temp")
    var temperature: String? = null

    @Expose
    @SerializedName("feels_like")
    var feelsLike: String? = null

    @Expose
    @SerializedName("pressure")
    var pressure: String? = null

    @Expose
    @SerializedName("humidity")
    var humidity: String? = null


    override fun toString(): String {
        return "TemperatureInfo(temperature=$temperature, feelsLike=$feelsLike, pressure=$pressure, humidity=$humidity)"
    }

}