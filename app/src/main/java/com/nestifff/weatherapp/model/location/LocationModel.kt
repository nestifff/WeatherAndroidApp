package com.nestifff.weatherapp.model.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.nestifff.weatherapp.TAG
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class LocationModel(
    appContext: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    private lateinit var locationObservable: PublishSubject<Location>

    @SuppressLint("MissingPermission")
    fun getLocation(): Observable<Location> {

        locationObservable = PublishSubject.create()

        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    locationObservable.onNext(it)
                } else {
                    getCurrentLocation()
                    Log.i(TAG, "getLocation: try to getCurrentLocation")
                }
            }
        return locationObservable
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        // TODO: set timeout for getCurrentLocation
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, null) /*object : CancellationToken() {

            override fun isCancellationRequested(): Boolean {
            }
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
            }

        })*/
            .addOnSuccessListener {
                if (it != null) {
                    locationObservable.onNext(it)
                } else {
                    locationObservable.onError(Throwable("Unable retrieve location"))
                }
            }
    }

}