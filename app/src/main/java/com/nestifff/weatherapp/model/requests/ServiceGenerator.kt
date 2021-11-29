package com.nestifff.weatherapp.model.requests

import com.nestifff.weatherapp.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val requestsApi = retrofit.create(RequestApi::class.java)

    fun getRequestApi(): RequestApi = requestsApi
}
