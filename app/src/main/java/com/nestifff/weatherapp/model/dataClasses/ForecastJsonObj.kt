package com.nestifff.weatherapp.model.dataClasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastJsonObj {

    @Expose
    @SerializedName("list")
    lateinit var forecasts: List<Forecast>

    @Expose
    @SerializedName("city")
    lateinit var city: City

    class City {
        @Expose
        @SerializedName("name")
        lateinit var name: String
    }

}