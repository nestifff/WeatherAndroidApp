package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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