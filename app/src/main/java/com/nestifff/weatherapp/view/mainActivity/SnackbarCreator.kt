package com.nestifff.weatherapp.view.mainActivity

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nestifff.weatherapp.R

fun showLocationPermissionSnackbar(view: View, activity: MainActivity) {

    val snackbar: Snackbar = Snackbar.make(
        view,
        "Give location permission, otherwise the app can't work",
        Snackbar.LENGTH_INDEFINITE
    )
    snackbar.setAction("Ok") {
        activity.requestLocationPermission()
        snackbar.dismiss()
    }

    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.light_gray))
    snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.light_blue))
    snackbar.show()
}

fun showImpossibleRetrieveLocationSnackbar(view: View, activity: MainActivity) {

    val snackbar: Snackbar = Snackbar.make(
        view,
        "Can't determine location. Turn on mobile data, Wi-Fi or GPS, and try again",
        Snackbar.LENGTH_INDEFINITE
    )
    snackbar.setAction("Try again") {
        activity.retrieveLocation()
        snackbar.dismiss()
    }

    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.light_gray))
    snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.light_blue))
    snackbar.show()
}

