package com.nestifff.weatherapp.view.mainActivity

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.TAG
import com.nestifff.weatherapp.model.location.LocationModel
import com.nestifff.weatherapp.model.requests.CurrentWeatherRequestModel
import com.nestifff.weatherapp.model.requests.ForecastRequestModel
import com.nestifff.weatherapp.presenter.CurrentWeatherPresenter
import com.nestifff.weatherapp.presenter.ForecastPresenter
import com.nestifff.weatherapp.presenter.LocationPresenter
import com.nestifff.weatherapp.view.currWeatherFragment.CurrentWeatherFragment
import com.nestifff.weatherapp.view.currWeatherFragment.currentWeatherPresenterKey
import com.nestifff.weatherapp.view.forecastFragment.ForecastFragment
import com.nestifff.weatherapp.view.forecastFragment.forecastPresenterKey


class MainActivity : AppCompatActivity() {

    private lateinit var currWeatherFragment: CurrentWeatherFragment
    private lateinit var forecastFragment: ForecastFragment

    private var locationPresenter: LocationPresenter? = null
    private var forecastPresenter: ForecastPresenter? = null
    private var currentWeatherPresenter: CurrentWeatherPresenter? = null

    private lateinit var bottomMenu: BottomNavigationView

    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeAndStatusBarColor()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.supportActionBar?.hide()
        changeBottomMenuState(false)

        bottomMenu = findViewById(R.id.menu_bottom)

        location = savedInstanceState?.getParcelable(mainActivityLocationKey)
        forecastPresenter = savedInstanceState?.getSerializable(
            mainActivityForecastPresenterKey
        ) as ForecastPresenter?
        Log.i(TAG, "onCreate: $forecastPresenter from savedInstanceState")

        if (location == null) {
            locationPresenter = LocationPresenter(LocationModel(applicationContext))
            locationPresenter?.attachView(this)
        } else {
            startShowFragments()
        }

        bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_curr_weather -> {
                    if (checkTypeActiveFragment<ForecastFragment>()) {
                        supportFragmentManager.popBackStack()
                    }
                }
                R.id.menu_forecast -> {
                    if (checkTypeActiveFragment<CurrentWeatherFragment>()) {
                        addForecastFragment()
                    }
                }
            }
            true
        }
    }

    fun getActivity(): AppCompatActivity {
        return this
    }

    fun showRequestPermissionLocationRationale() {
        Toast.makeText(
            this,
            "You should provide your location info to show weather and forecast",
            Toast.LENGTH_LONG
        ).show()
        // TODO: shackbar show rationale: yes -> locationPresenter.justRequestPermission()
        // no -> presenter.permissionDenied()
    }

    private fun setThemeAndStatusBarColor() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        }
    }

    private fun addForecastFragment() {

        forecastFragment = ForecastFragment()
        forecastFragment.arguments = bundleOf(forecastPresenterKey to forecastPresenter)

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container, forecastFragment)
            .addToBackStack(null)
            .commit()

        addBackStackChangeListener()
    }

    fun startShowFragments() {

        if (supportFragmentManager.fragments.isEmpty()) {

            currWeatherFragment = CurrentWeatherFragment()
            currWeatherFragment.arguments = bundleOf(
                currentWeatherPresenterKey to currentWeatherPresenter
            )
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, currWeatherFragment)
                .commit()
        }

        changeBottomMenuState(true)
        addBackStackChangeListener()
    }

    fun showLocationPermissionDenied() {
        Toast.makeText(
            this, "Permission denied, can't show weather",
            Toast.LENGTH_SHORT
        ).show()
        //TODO view to ask location permission again
    }

    fun impossibleRetrieveLocation() {
        Toast.makeText(
            this, "Impossible retrieve location, can't show weather",
            Toast.LENGTH_SHORT
        ).show()
        //TODO view to try retrieve location again
    }

    private fun changeBottomMenuState(isEnabled: Boolean) {
        findViewById<BottomNavigationView>(R.id.menu_bottom).menu
            .forEach { it.isEnabled = isEnabled }
    }

    fun locationLoaded(location: Location) {
        this.location = location
        currentWeatherPresenter =
            CurrentWeatherPresenter(location, CurrentWeatherRequestModel())
        forecastPresenter = ForecastPresenter(location, ForecastRequestModel())
    }

    private inline fun <reified T> checkTypeActiveFragment(): Boolean {
        val currFragments = supportFragmentManager.fragments
        return currFragments.isNotEmpty() &&
                currFragments[currFragments.size - 1] is T
    }

    // in order to switch bottom menu active item
    private fun addBackStackChangeListener() {

        val count = supportFragmentManager.backStackEntryCount
        supportFragmentManager.addOnBackStackChangedListener(object : OnBackStackChangedListener {
            override fun onBackStackChanged() {
                if (supportFragmentManager.backStackEntryCount <= count) {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.removeOnBackStackChangedListener(this)
                    bottomMenu.menu.getItem(0).isChecked = true
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: $outState")
        outState.putSerializable(mainActivityForecastPresenterKey, forecastPresenter)
        outState.putParcelable(mainActivityLocationKey, location)
    }

    override fun onDestroy() {
        locationPresenter?.detachView()
        Log.i(TAG, "onDestroy: MainActivity")
        super.onDestroy()
    }
}

const val mainActivityLocationKey = "MainActivity.location"
const val mainActivityForecastPresenterKey = "MainActivity.forecastPresenter"