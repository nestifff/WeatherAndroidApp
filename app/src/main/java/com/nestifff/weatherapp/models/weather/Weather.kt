package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Weather {

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