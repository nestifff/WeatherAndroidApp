package com.nestifff.weatherapp.forecastFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.models.forecast.Forecast


class ForecastFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentView = inflater.inflate(R.layout.fragment_forecast, container, false)
        recyclerView = fragmentView.findViewById(R.id.rv_forecasts)
        recyclerView.layoutManager = LinearLayoutManager(fragmentView.context)
        recyclerView.adapter = ForecastsAdapter()
        return fragmentView
    }

    fun showForecast(forecasts: List<Forecast>) {
        (recyclerView.adapter as ForecastsAdapter).setForecasts(forecasts)
    }
}