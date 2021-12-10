package com.nestifff.weatherapp.view.forecastFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.model.dataClasses.Forecast
import com.nestifff.weatherapp.presenter.ForecastPresenter


class ForecastFragment : Fragment() {

    private var dataView: View? = null
    private var errorView: View? = null
    private lateinit var progressBar: CircularProgressIndicator

    private lateinit var recyclerView: RecyclerView

    // if change orientation very quickly, lateinit may not be initialized
    // and activity will not be able to destroy
    private var presenter: ForecastPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        presenter = arguments?.getSerializable(forecastPresenterKey) as ForecastPresenter?
        presenter?.attachView(this)

        val view = inflater.inflate(R.layout.fragment_forecast, container, false)
        progressBar = view.findViewById(R.id.pi_forecast)

        val errorStub: ViewStub = view.findViewById(R.id.stub_loading_error_forecast)
        val dataStub: ViewStub = view.findViewById(R.id.stub_forecast)

        errorStub.setOnInflateListener { _, inflated ->
            inflated.findViewById<Button>(R.id.btn_retry_loading_from_api)?.setOnClickListener {
                presenter?.retryLoading()
            }
        }

        dataStub.setOnInflateListener { _, inflated ->
            recyclerView = inflated.findViewById(R.id.rv_forecasts)
            recyclerView.layoutManager = LinearLayoutManager(inflated.context)
            recyclerView.adapter = ForecastsAdapter()
        }

        dataView = dataStub.inflate()
        errorView = errorStub.inflate()

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter?.viewOnStarted()
    }

    fun showProgressBar() {
        errorView?.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun showLoadingError() {
        dataView?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
    }

    fun showForecast(forecasts: List<Forecast>) {
        errorView?.visibility = View.GONE
        dataView?.visibility = View.VISIBLE
        (recyclerView.adapter as ForecastsAdapter).setForecasts(forecasts)
    }

    fun setCityName(cityName: String) {
        view?.findViewById<TextView>(R.id.tv_view_mode_forecast)?.text = cityName
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }

}

const val forecastPresenterKey: String = "ForecastFragment.presenter"