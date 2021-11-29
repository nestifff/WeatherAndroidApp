package com.nestifff.weatherapp.view.currWeatherFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.model.dataClasses.CurrentWeather
import com.nestifff.weatherapp.presenter.CurrentWeatherPresenter

class CurrentWeatherFragment : Fragment() {

    private lateinit var fragmentView: View
    // if change orientation very quickly, lateinit may not be initialized
    // and activity will not be able to destroy
    private var presenter: CurrentWeatherPresenter? = null

    override fun onStart() {
        super.onStart()
        presenter?.viewOnStarted()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenter =
            arguments?.getSerializable(currentWeatherPresenterKey) as CurrentWeatherPresenter?
        presenter?.attachView(this)
        fragmentView = inflater.inflate(R.layout.current_weather_fragment, container, false)
        return fragmentView
    }

    fun showCurrentWeather(currWeather: CurrentWeather) {
        fragmentView.findViewById<TextView>(R.id.tv_curr_weather).text =
            currWeather.toString()
    }

    fun showLoadingError() {
        Toast.makeText(
            fragmentView.context,
            "Error while loading current weather",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }
}

const val currentWeatherPresenterKey: String = "CurrentWeatherFragment.presenter"