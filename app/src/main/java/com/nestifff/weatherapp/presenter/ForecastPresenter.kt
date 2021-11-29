package com.nestifff.weatherapp.presenter

import android.location.Location
import android.util.Log
import com.nestifff.weatherapp.TAG
import com.nestifff.weatherapp.model.dataClasses.Forecast
import com.nestifff.weatherapp.model.dataClasses.ForecastJsonObj
import com.nestifff.weatherapp.model.requests.ForecastRequestModel
import com.nestifff.weatherapp.view.forecastFragment.ForecastFragment
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

class ForecastPresenter(
    private val location: Location,
    private val forecastModel: ForecastRequestModel,
) : Serializable {

    private var view: ForecastFragment? = null
    private lateinit var disposable: Disposable

    private var cityName: String? = null
    private var forecasts: List<Forecast>? = null

    private fun loadForecast() {

        forecastModel.loadForecast(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<ForecastJsonObj> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    Log.i(TAG, "showForecast onSubscribe: $d, thread: ${Thread.currentThread()}")
                }

                override fun onError(e: Throwable) {
                    disposable.dispose()
                    view?.showLoadingError()
                    Log.i(TAG, "showForecast onError: $e, thread: ${Thread.currentThread()}")
                }

                override fun onSuccess(t: ForecastJsonObj) {
                    disposable.dispose()
                    forecasts = t.forecasts
                    cityName = t.city.name
                    Log.i(TAG, "showForecast onNext: $t, thread: ${Thread.currentThread()}")
                    view?.setCityName(t.city.name)
                    view?.showForecast(t.forecasts)
                }

            })
    }

    fun viewOnStarted() {
        if (forecasts == null) {
            loadForecast()
        } else {
            view?.showForecast(forecasts!!)
            view?.setCityName(cityName ?: "Forecast")
        }
    }


    fun attachView(view: ForecastFragment) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

}