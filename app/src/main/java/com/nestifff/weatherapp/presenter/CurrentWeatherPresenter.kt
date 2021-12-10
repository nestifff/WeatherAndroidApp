package com.nestifff.weatherapp.presenter

import android.location.Location
import android.util.Log
import com.nestifff.weatherapp.TAG
import com.nestifff.weatherapp.model.dataClasses.CurrentWeather
import com.nestifff.weatherapp.model.requests.CurrentWeatherRequestModel
import com.nestifff.weatherapp.view.currWeatherFragment.CurrentWeatherFragment
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

class CurrentWeatherPresenter(
    private val location: Location,
    private val currentWeatherModel: CurrentWeatherRequestModel
) : Serializable {

    private var view: CurrentWeatherFragment? = null
    private lateinit var disposable: Disposable

    private var currentWeather: CurrentWeather? = null

    private fun loadCurrentWeather() {

        view?.showProgressBar()

        currentWeatherModel.getCurrentWeather(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<CurrentWeather> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    Log.i(
                        TAG,
                        "showCurrentWeather onSubscribe: $d, thread: ${Thread.currentThread()}"
                    )
                }

                override fun onError(e: Throwable) {
                    disposable.dispose()
                    view?.hideProgressBar()
                    view?.showLoadingError()
                    Log.i(TAG, "showCurrentWeather onError: $e, thread: ${Thread.currentThread()}")
                }

                override fun onSuccess(t: CurrentWeather) {
                    currentWeather = t
                    disposable.dispose()
                    view?.hideProgressBar()
                    view?.showCurrentWeather(t)
                    Log.i(TAG, "showCurrentWeather onNext: $t, thread: ${Thread.currentThread()}")
                }

            })
    }

    fun retryLoading() {
        loadData()
    }

    fun viewOnStarted() {
        loadData()
    }

    private fun loadData() {
        if (currentWeather == null) {
            loadCurrentWeather()
        } else {
            this.view?.showCurrentWeather(currentWeather!!)
        }
    }

    fun attachView(view: CurrentWeatherFragment) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

}