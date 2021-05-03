package com.mfahimi.nearbyplace.ui.fragment

import com.mfahimi.nearbyplace.ui.activity.NavigationActivity
import com.ncapdevi.fragnav.FragNavController

abstract class NavigationFragment : BaseFragment() {
    /**
     * This is called on back button press, return true to consume it or leave it false
     */
    open fun onBackPressed(): Boolean = false

    protected fun fragNavController(): FragNavController {
        return if (activity != null && activity is NavigationActivity) (activity as NavigationActivity).fragNavController
        else throw IllegalArgumentException("Host activity should be an instance of NavigationActivity")
    }

    protected fun navigateBack() = activity?.onBackPressed()
}