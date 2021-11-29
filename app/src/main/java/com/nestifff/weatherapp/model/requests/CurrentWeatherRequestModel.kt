package com.nestifff.weatherapp.model.requests

import android.location.Location
import com.nestifff.weatherapp.model.dataClasses.CurrentWeather
import io.reactivex.Single

class CurrentWeatherRequestModel {

    fun getCurrentWeather(location: Location): Single<CurrentWeather> {
        return ServiceGenerator.getRequestApi()
            .getCurrentWeather(
                location.latitude,
                location.longitude
            )
    }

}