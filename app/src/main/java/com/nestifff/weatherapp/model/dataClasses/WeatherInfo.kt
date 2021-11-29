package com.nestifff.weatherapp.model.dataClasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherInfo {

    @Expose
    @SerializedName("main")
    var name: String? = null

    @Expose
    @SerializedName("icon")
    var icon: String? = null

    override fun toString(): String {
        return "GeneralWeather(name=$name, icon=$icon)"
    }

}