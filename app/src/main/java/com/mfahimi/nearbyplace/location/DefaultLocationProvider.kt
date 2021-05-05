package com.mfahimi.nearbyplace.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.mfahimi.nearbyplace.ext.isLocationProviderEnabled
import com.mfahimi.nearbyplace.ext.requestLocationPermission
import java.lang.ref.WeakReference


/**
 * get user location using android location provider.
 * by using WeakReference prevents from memory leaks
 * it could be onetime location request or continues update
 * it sends result to applicant using [LocationCallback] callback
 * */
class DefaultLocationProvider(
    private val oneTime: Boolean,
    context: Context,
    callback: LocationCallback,
    private val intervalTime: Long
) : LocationListener, ILocationProvider() {
    private val contextRef = WeakReference(context)
    private var mCallback = WeakReference(callback)

    private val locationManager: LocationManager? = contextRef.get()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    init {
        // getting GPS status
        val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // getting network status
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGPSEnabled && !isNetworkEnabled) {
            mCallback.get()?.onProviderDisabled()
        }

    }

    override fun onLocationChanged(location: Location) {
        location.let {
            if (isBetterLocation(it, lastLocation))
                mCallback.get()?.onLocationReceived(it)
            else
                mCallback.get()?.onLocationReceived(lastLocation!!)

            lastLocation = location

            if (oneTime)
                removeLocationUpdates()
        }
    }

    override fun onProviderEnabled(provider: String) {
        println(provider)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        println(provider)
    }

    override fun onProviderDisabled(provider: String) {
        mCallback.get()?.onProviderDisabled()
        removeLocationUpdates()
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
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            intervalTime,
            10.toFloat(),
            this
        );
    }

    @SuppressLint("MissingPermission")
    override fun removeLocationUpdates() {
        locationManager?.removeUpdates(this)
    }
}