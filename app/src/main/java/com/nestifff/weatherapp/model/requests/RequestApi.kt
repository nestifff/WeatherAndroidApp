package com.nestifff.weatherapp.model.requests

import com.nestifff.weatherapp.API_KEY_WEATHER
import com.nestifff.weatherapp.model.dataClasses.ForecastJsonObj
import com.nestifff.weatherapp.model.dataClasses.CurrentWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApi {

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY_WEATHER
    ): Single<CurrentWeather>

    @GET("forecast")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY_WEATHER
    ): Single<ForecastJsonObj>


    /*@GET("post/{id}/comments")
    fun getComments(@Path("id") id: Int): Observable<List<Comment>>*/
}