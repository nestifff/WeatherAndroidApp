package com.nestifff.weatherapp.model.requests

import android.location.Location
import com.nestifff.weatherapp.model.dataClasses.ForecastJsonObj
import io.reactivex.Single

class ForecastRequestModel {

    fun loadForecast(location: Location): Single<ForecastJsonObj> {
        return ServiceGenerator.getRequestApi()
            .getForecast(
                location.latitude,
                location.longitude
            )
    }
}