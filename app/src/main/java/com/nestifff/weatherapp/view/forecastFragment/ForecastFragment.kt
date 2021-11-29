package com.nestifff.weatherapp.view.forecastFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.model.dataClasses.Forecast
import com.nestifff.weatherapp.presenter.ForecastPresenter


class ForecastFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var recyclerView: RecyclerView

    private lateinit var presenter: ForecastPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        presenter = arguments?.getSerializable(forecastPresenterKey) as ForecastPresenter
        presenter.attachView(this)

        fragmentView = inflater.inflate(R.layout.fragment_forecast, container, false)
        recyclerView = fragmentView.findViewById(R.id.rv_forecasts)
        recyclerView.layoutManager = LinearLayoutManager(fragmentView.context)
        recyclerView.adapter = ForecastsAdapter()

        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        presenter.viewOnStarted()
    }

    fun setCityName(cityName: String) {
        view?.findViewById<TextView>(R.id.tv_view_mode_forecast)?.text = cityName
    }

    fun showForecast(forecasts: List<Forecast>) {
        (recyclerView.adapter as ForecastsAdapter).setForecasts(forecasts)
    }

    fun showLoadingError() {
        Toast.makeText(
            fragmentView.context,
            "Error while loading forecast",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }
}

const val forecastPresenterKey: String = "ForecastFragment.presenter"