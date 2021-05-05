package com.mfahimi.nearbyplace.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*
import com.mfahimi.nearbyplace.ext.isLocationProviderEnabled
import com.mfahimi.nearbyplace.ext.requestLocationPermission
import java.lang.ref.WeakReference

/**
 * get user location using play service location provider.
 * by using WeakReference prevents from memory leaks
 * it could be onetime location request or continues update
 * it sends result to applicant using [LocationCallback] callback
 * */
class PlayServiceLocationProvider(
    oneTime: Boolean,
    context: Context,
    callback: LocationCallback,
    intervalTime: Long
) : ILocationProvider() {
    private val contextRef = WeakReference(context)
    private var mCallback = WeakReference(callback)
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = intervalTime
        if (oneTime)
            numUpdates = 1
    }
    private var locationCallback: com.google.android.gms.location.LocationCallback

    init {
        locationCallback =
            WeakLocationCallback(object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult == null) {
                        return
                    }
                    mCallback.get()?.onLocationReceived(locationResult.lastLocation)
                    if (oneTime)
                        removeLocationUpdates()
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    if (p0?.isLocationAvailable == false)
                        mCallback.get()?.onLocationUnAvailable()
                }
            })
    }

    override fun startLocationUpdates() {
        contextRef.get()?.apply {
            requestLocationPermission { granted, denied, permanentlyDenied ->
                if (granted.isEmpty()) {
                    mCallback.get()?.onPermissionDenied()
                } else {
                    if (!isLocationProviderEnabled())
                        mCallback.get()?.onProviderDisabled()
                    else {
                        mCallback.get()?.onStartListening()
                        doRequestLocationUpdates()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun doRequestLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}