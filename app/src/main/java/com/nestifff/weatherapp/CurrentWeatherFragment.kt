package com.nestifff.weatherapp

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nestifff.weatherapp.models.weather.CurrentWeatherInfo

class CurrentWeatherFragment(
//    val showForecast: ShowForecast
) : Fragment() {

    private lateinit var fragmentView: View
    private var listener: ShowForecast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.current_weather_fragment, container, false)
        fragmentView.findViewById<Button>(R.id.btn_show_forecast).setOnClickListener {
            listener?.showForecast()
        }
        return fragmentView
    }

    fun showLocation(location: Location) {
        fragmentView.findViewById<TextView>(R.id.tv_location).text = location.toString() +
                "\n\nlatitude: ${location.latitude}, \nlongitude: ${location.longitude}"
    }

    fun showCurrentWeather(currWeather: CurrentWeatherInfo?) {
        fragmentView.findViewById<TextView>(R.id.tv_curr_weather).text =
            currWeather?.toString() ?: "null"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ShowForecast) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface ShowForecast {
        fun showForecast()
    }

}