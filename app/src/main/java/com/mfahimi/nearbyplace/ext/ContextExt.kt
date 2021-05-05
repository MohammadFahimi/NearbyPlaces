package com.mfahimi.nearbyplace.ext

import android.Manifest
import android.content.Context
import android.os.Build
import com.androidisland.ezpermission.EzPermission

fun Context.requestCameraPermission(
    result: (
        granted: Set<String>,
        denied: Set<String>,
        permanentlyDenied: Set<String>
    ) -> Unit
) {
    EzPermission.with(this)
        .permissions(Manifest.permission.CAMERA)
        .request(result)
}

fun Context.requestLocationPermission(
    result: (
        granted: Set<String>,
        denied: Set<String>,
        permanentlyDenied: Set<String>
    ) -> Unit
) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
        EzPermission.with(this)
            .permissions(getLocationPermission())
            .request(result)
    else
        EzPermission.with(this)
            .permissions(getLocationPermission())
            .request(result)

}
fun Any.getLocationPermission(): List<String> {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    else
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
            //,Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
}