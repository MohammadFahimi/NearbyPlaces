package com.mfahimi.nearbyplace.location

import android.content.Context
import com.mfahimi.nearbyplace.ext.isGooglePlayServiceAvailable


class ApplicationLocationProvider(
    oneTime: Boolean = true,
    context: Context,
    callback: LocationCallback,
    intervalTime: Long = 10000
) : ILocationProvider() {
    private var mLocationProvider: ILocationProvider =
//        if (context.isGooglePlayServiceAvailable())
//            PlayServiceLocationProvider(oneTime, context, callback, intervalTime)
//        else
            DefaultLocationProvider(oneTime, context, callback, intervalTime)

    override fun startLocationUpdates() {
        mLocationProvider.startLocationUpdates()
    }

    override fun removeLocationUpdates() {
        mLocationProvider.removeLocationUpdates()
    }

}