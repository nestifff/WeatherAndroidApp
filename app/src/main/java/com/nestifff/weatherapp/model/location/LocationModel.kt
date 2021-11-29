package com.nestifff.weatherapp.model.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class LocationModel(
    appContext: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    private var locationObservable = PublishSubject.create<Location>()

    @SuppressLint("MissingPermission")
    fun getLocation(): Observable<Location> {
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    locationObservable.onNext(it)
                } else {
                    // TODO: ask turn on gps (else can't determine location (?))
                    //TODO: try/catch (if not determined -> error)
                    getCurrentLocation()
                }
            }
        return locationObservable
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, null)
            .addOnSuccessListener {
                if (it != null) {
                    locationObservable.onNext(it)
                } else {
                    locationObservable.onError(Throwable("Unable retrieve location"))
                }
            }
    }

}