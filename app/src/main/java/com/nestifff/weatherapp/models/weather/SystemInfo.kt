package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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