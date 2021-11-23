package com.nestifff.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val requestsApi = retrofit.create(RequestApi::class.java)

    fun getRequestApi(): RequestApi = requestsApi
}
