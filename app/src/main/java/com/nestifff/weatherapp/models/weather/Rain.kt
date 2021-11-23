package com.nestifff.weatherapp.models.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rain {

    @Expose
    @SerializedName("1h")
    var h1: String? = null
}