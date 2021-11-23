package com.nestifff.weatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.nestifff.weatherapp.forecastFragment.ForecastFragment
import com.nestifff.weatherapp.models.forecast.ForecastJsonObj
import com.nestifff.weatherapp.models.weather.CurrentWeatherInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), CurrentWeatherFragment.ShowForecast {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currWeatherFragment: CurrentWeatherFragment
    private lateinit var forecastFragment: ForecastFragment

    private lateinit var location: Location

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

                invokeGetLastLocation()

            } else {
                Toast.makeText(this, "You are idiot", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        when {
            ContextCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

                invokeGetLastLocation()
            }
            /*Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {
                Toast.makeText(this, "Please", Toast.LENGTH_SHORT).show()
            }*/
            else -> {
                requestPermissionLauncher.launch(ACCESS_COARSE_LOCATION)
            }
        }

        currWeatherFragment = CurrentWeatherFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, currWeatherFragment)
            .commit()

    }

    @SuppressLint("MissingPermission")
    private fun invokeGetLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { it: Location? ->
                // Got last known location. In some rare situations this can be null.
                it?.let {

                    location = it

                    Log.i(TAG, "invokeGetLastLocation: $location")
                    currWeatherFragment.showLocation(location)

                    ServiceGenerator.getRequestApi()
                        .getCurrentWeather(
                            location.latitude,
                            location.longitude
                        )
                        .enqueue(object : Callback<CurrentWeatherInfo> {
                            override fun onResponse(
                                call: Call<CurrentWeatherInfo>,
                                response: Response<CurrentWeatherInfo>
                            ) {
                                currWeatherFragment.showCurrentWeather(response.body())
                                Log.i(TAG, "onResponse: ${response.body().toString()}")
                            }

                            override fun onFailure(call: Call<CurrentWeatherInfo>, t: Throwable) {
                                Log.e(TAG, "onFailure: ", t)
                            }

                        })
                }


            }
    }

    override fun showForecast() {

        forecastFragment = ForecastFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, forecastFragment)
            .addToBackStack(null)
            .commit()

        ServiceGenerator.getRequestApi()
            .getForecast(
                location.latitude,
                location.longitude
            )
            .enqueue(object : Callback<ForecastJsonObj> {
                override fun onResponse(
                    call: Call<ForecastJsonObj>,
                    response: Response<ForecastJsonObj>
                ) {
                    forecastFragment.showForecast(response.body()?.forecasts ?: listOf())
                    Log.i(TAG, "onResponse: ${response.body().toString()}")
                }

                override fun onFailure(call: Call<ForecastJsonObj>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                }

            })
    }

}