package com.nestifff.weatherapp.presenter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.nestifff.weatherapp.TAG
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

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private fun createRequestPermissionLauncher(activity: MainActivity) {

        requestPermissionLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    permissionGranted()
                } else {
                    permissionDenied()
                }
            }
    }

    fun requestPermissionLocation(showRationale: Boolean) {

        val activity = view?.getActivity() ?: return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted()
            return
        }

        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ->
                permissionGranted()

            showRationale && activity.shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                view?.showRequestPermissionLocationRationale()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun permissionGranted() {
        retrieveLocation()
    }

    fun permissionDenied() {
        view?.showLocationPermissionDenied()
    }

    fun locationRetrieved(location: Location) {
        view?.locationLoaded(location)
        view?.startShowFragments()
    }

    fun impossibleRetrieveLocation() {
        view?.impossibleRetrieveLocation()
    }

    fun retrieveLocation() {

        model.getLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(object : Observer<Location> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Location) {
                    locationRetrieved(t)
                    Log.i(TAG, "retrieveLocation presenter onNext: $t")
                }

                override fun onError(e: Throwable) {
                    impossibleRetrieveLocation()
                    Log.i(TAG, "retrieveLocation presenter onError: ${e.message}")
                    disposable.dispose()
                }

                override fun onComplete() {
                    Log.i(TAG, "retrieveLocation presenter onComplete")
                    disposable.dispose()
                }
            })
    }

    fun attachView(view: MainActivity) {
        this.view = view
        if (location == null) {
            createRequestPermissionLauncher(view)
            requestPermissionLocation(true)
        }
    }

    fun detachView() {
        view = null
    }
}