package com.nestifff.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nestifff.weatherapp.models.weather.CurrentWeatherInfo

class CurrentWeatherFragment() : Fragment() {

    private lateinit var fragmentView: View

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.current_weather_fragment, container, false)
        return fragmentView
    }

    fun showCurrentWeather(currWeather: CurrentWeatherInfo?) {
        fragmentView.findViewById<TextView>(R.id.tv_curr_weather).text =
                currWeather?.toString() ?: "null"
    }

}