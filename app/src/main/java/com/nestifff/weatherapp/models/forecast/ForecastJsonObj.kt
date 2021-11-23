package com.nestifff.weatherapp.models.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastJsonObj {

    @Expose
    @SerializedName("list")
    lateinit var forecasts: List<Forecast>

}