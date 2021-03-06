package com.nestifff.weatherapp.view.forecastFragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.model.dataClasses.Forecast

class ForecastsViewHolder(
    private val itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.tv_forecast_info)

    fun bind(forecast: Forecast) {
        textView.text = forecast.toString()
    }

}