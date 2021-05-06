package com.mfahimi.nearbyplace.ui.activity

import androidx.annotation.IdRes
import com.mfahimi.nearbyplace.ui.fragment.NavigationFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions

abstract class NavigationActivity : BaseActivity() {
    val fragNavController: FragNavController by lazy {
        FragNavController(supportFragmentManager, fragmentContainer).apply {
            defaultTransactionOptions = FragNavTransactionOptions.newBuilder()
                .run {
                    allowStateLoss = true
                    build()
                }
        }
    }

    @get:IdRes
    abstract val fragmentContainer: Int

    override fun onBackPressed() {
        val currentFrag = fragNavController.currentFrag
        var consumed = false
        if (currentFrag != null && currentFrag is NavigationFragment<*>) {
            consumed = currentFrag.onBackPressed()
        }
        if (!consumed) {
            if (fragNavController.isRootFragment) super.onBackPressed()
            else fragNavController.popFragment()
        }
    }
}