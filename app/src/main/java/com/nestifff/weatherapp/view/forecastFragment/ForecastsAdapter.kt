package com.nestifff.weatherapp.view.forecastFragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.TAG
import com.nestifff.weatherapp.model.dataClasses.Forecast

class ForecastsAdapter: RecyclerView.Adapter<ForecastsViewHolder>() {

    private var forecasts: List<Forecast> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setForecasts(newForecasts: List<Forecast>) {
        forecasts = newForecasts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastsViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_forecast, null, false)
        return ForecastsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastsViewHolder, position: Int) {
        holder.bind(forecasts[position])
    }

    override fun getItemCount() = forecasts.size


}