package com.nestifff.weatherapp.view.currWeatherFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nestifff.weatherapp.R
import com.nestifff.weatherapp.model.dataClasses.CurrentWeather
import com.nestifff.weatherapp.presenter.CurrentWeatherPresenter


class CurrentWeatherFragment : Fragment() {

    private var dataView: View? = null
    private var errorView: View? = null
    private lateinit var progressBar: CircularProgressIndicator

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

        presenter = arguments?.getSerializable(currentWeatherPresenterKey)
                as CurrentWeatherPresenter?
        presenter?.attachView(this)

        val view = inflater.inflate(R.layout.current_weather_fragment, container, false)
        progressBar = view.findViewById(R.id.pi_current_weather)

        val errorStub: ViewStub = view.findViewById(R.id.stub_loading_error_cw)
        val dataStub: ViewStub = view.findViewById(R.id.stub_current_weather)

        errorStub.setOnInflateListener { _, inflated ->
            inflated.findViewById<Button>(R.id.btn_retry_loading_from_api)?.setOnClickListener {
                presenter?.retryLoading()
            }
        }

        dataView = dataStub.inflate()
        errorView = errorStub.inflate()

        return view
    }

    fun showProgressBar() {
        errorView?.visibility = GONE
        progressBar.visibility = VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = GONE
    }

    fun showLoadingError() {
        dataView?.visibility = GONE
        errorView?.visibility = VISIBLE
    }

    fun showCurrentWeather(currWeather: CurrentWeather) {
        errorView?.visibility = GONE
        dataView?.visibility = VISIBLE
        dataView?.findViewById<TextView>(R.id.tv_curr_weather)?.text =
            currWeather.toString()
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }
}

const val currentWeatherPresenterKey: String = "CurrentWeatherFragment.presenter"