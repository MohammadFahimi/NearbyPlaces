package com.mfahimi.nearbyplace.ext

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

fun Context.isGpsProviderEnabled(): Boolean {
    val locationManager = ContextCompat.getSystemService(this, LocationManager::class.java)
    return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.isNetworkProviderEnabled(): Boolean {
    val locationManager = ContextCompat.getSystemService(this, LocationManager::class.java)
    return locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun Context.isLocationProviderEnabled(): Boolean {
    val locationManager = ContextCompat.getSystemService(this, LocationManager::class.java)
    return locationManager != null &&
            (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
}

fun Context.registerLocationProviderReceiver(receiver: BroadcastReceiver) {
    registerReceiver(receiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
}

fun Context.unregisterLocationProviderReceiver(receiver: BroadcastReceiver) {
    unregisterReceiver(receiver)
}

fun Context.isGooglePlayServiceAvailable(): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
    if (status != ConnectionResult.SUCCESS)
        return false

    return true
}