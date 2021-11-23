package com.nestifff.weatherapp

import com.nestifff.weatherapp.models.forecast.ForecastJsonObj
import com.nestifff.weatherapp.models.weather.CurrentWeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApi {

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Call<CurrentWeatherInfo>

    @GET("forecast")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Call<ForecastJsonObj>


    /*@GET("post/{id}/comments")
    fun getComments(@Path("id") id: Int): Observable<List<Comment>>*/
}