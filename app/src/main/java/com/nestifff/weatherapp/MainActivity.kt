package com.nestifff.weatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nestifff.weatherapp.forecastFragment.ForecastFragment
import com.nestifff.weatherapp.models.forecast.ForecastJsonObj
import com.nestifff.weatherapp.models.weather.CurrentWeatherInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currWeatherFragment: CurrentWeatherFragment
    private lateinit var forecastFragment: ForecastFragment

    private lateinit var tvViewMode: TextView

    private lateinit var location: Location
    private var currentWeather: CurrentWeatherInfo? = null
    private var cityName: String? = null

    private val requestPermissionLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {

                    getLastLocationShowCurrWeather()

                } else {
                    Toast.makeText(this, "You are idiot", Toast.LENGTH_SHORT).show()
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeAndStatusBarColor()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.supportActionBar?.hide()

        tvViewMode = findViewById(R.id.tv_view_mode)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        forecastFragment = ForecastFragment()
        currWeatherFragment = CurrentWeatherFragment()

        // TODO restore activity state (after rotate)
        if (savedInstanceState != null) {

        }

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, currWeatherFragment)
                .commit()
        tvViewMode.text = getString(R.string.today)

        requestPermissionAndGetLocation()

        findViewById<BottomNavigationView>(R.id.menu_bottom).setOnNavigationItemSelectedListener {
            val currFragments = supportFragmentManager.fragments
            when (it.itemId) {
                R.id.menu_curr_weather -> {
                    if (currFragments.isNotEmpty() &&
                            currFragments[currFragments.size - 1] !is CurrentWeatherFragment
                    ) {
                        currWeatherFragment = CurrentWeatherFragment()
                        setCurrentFragment(currWeatherFragment)
                    }
                }
                R.id.menu_forecast -> {
                    if (currFragments.isNotEmpty() &&
                            currFragments[currFragments.size - 1] !is ForecastFragment
                    ) {
                        forecastFragment = ForecastFragment()
                        setCurrentFragment(forecastFragment)
                    }
                }
            }
            true
        }
    }

    private fun setThemeAndStatusBarColor() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        }
    }

    private fun requestPermissionAndGetLocation() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getLastLocationShowCurrWeather()
            return
        }

        when {
            ContextCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> getLastLocationShowCurrWeather()

            shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {

                Toast.makeText(this,
                        "You should provide your location info to show weather and forecast",
                        Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(ACCESS_COARSE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

        if (fragment is ForecastFragment) {
            tvViewMode.text = cityName ?: getString(R.string.forecast)
            showForecast()
        } else if (fragment is CurrentWeatherFragment) {
            tvViewMode.text = getString(R.string.today)
            showCurrentWeather()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocationShowCurrWeather() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { it: Location? ->
                    if (it != null) {
                        location = it
                        Log.i(TAG, "lastLocation: $location")
                        showCurrentWeather()
                    } else {
                        // TODO: ask turn on gps (else can't determine location (?))
                        //TODO: try/catch (if not determined -> error)
                        Toast.makeText(this, "Location null, getting current location..", Toast.LENGTH_SHORT).show()
                        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null).addOnSuccessListener {
                            it?.let {
                                Toast.makeText(this, "Location determined", Toast.LENGTH_SHORT).show()
                                location = it
                                Log.i(TAG, "getCurrentLocation: $location")
                                showCurrentWeather()
                            }
                            if (it == null) {
                                Toast.makeText(this, "Impossible get current location", Toast.LENGTH_SHORT).show()
                                Log.i(TAG, "getCurrentLocation: $location")
                            }
                        }
                    }
                }
    }

    private fun showCurrentWeather() {

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
                        cityName = response.body()?.cityName ?: getString(R.string.forecast)
                        currentWeather = response.body()
                        currWeatherFragment.showCurrentWeather(response.body())
                        Log.i(TAG, "onResponse: ${response.body().toString()}")
                    }

                    override fun onFailure(call: Call<CurrentWeatherInfo>, t: Throwable) {
                        Log.e(TAG, "onFailure: ", t)
                    }

                })
    }

    private fun showForecast() {

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