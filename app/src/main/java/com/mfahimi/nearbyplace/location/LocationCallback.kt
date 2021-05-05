package com.mfahimi.nearbyplace.location

import android.location.Location

interface LocationCallback {
    fun onPermissionDenied() {}
    fun onProviderDisabled() {}
    fun onStartListening() {}
    fun onLocationReceived(location: Location) {}
    fun onLocationUnAvailable() {}
}