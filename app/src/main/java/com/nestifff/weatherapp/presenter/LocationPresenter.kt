package com.nestifff.weatherapp.presenter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.nestifff.weatherapp.model.location.LocationModel
import com.nestifff.weatherapp.view.mainActivity.MainActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LocationPresenter(
    val model: LocationModel
) {

    private var view: MainActivity? = null
    private lateinit var disposable: Disposable
    private var location: Location? = null

    private fun requestPermissionLocation() {

        val activity = view?.getActivity() ?: return

        val requestPermissionLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    permissionGranted()
                } else {
                    permissionDenied()
                }
            }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted()
            return
        }

        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ->
                permissionGranted()

            activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {

                view?.showRequestPermissionLocationRationale()
                //TODO: delete (shackbar show rationale)
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun permissionGranted() {
        retrieveLocation()
    }

    private fun permissionDenied() {
        view?.showLocationPermissionDenied()
    }

    fun locationRetrieved(location: Location) {
        view?.locationLoaded(location)
        view?.startShowFragments()
    }

    fun impossibleRetrieveLocation() {
        view?.impossibleRetrieveLocation()
    }

    private fun retrieveLocation() {

        model.getLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(object : Observer<Location> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Location) {
                    locationRetrieved(t)
                }

                override fun onError(e: Throwable) {
                    impossibleRetrieveLocation()
                    disposable.dispose()
                }

                override fun onComplete() {
                    disposable.dispose()
                }

            })
    }

    fun attachView(view: MainActivity) {
        this.view = view
        if (location == null) {
            requestPermissionLocation()
        }
    }

    fun detachView() {
        view = null
    }
}