package com.mfahimi.nearbyplace.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.databinding.ActivityMainBinding
import com.mfahimi.nearbyplace.ui.fragment.LocationListFragment
import com.ncapdevi.fragnav.FragNavController

class MainActivity : NavigationActivity() {

    override val _binding: ViewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val fragmentContainer: Int = R.id.fl_fragment_container

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        fragNavController.rootFragmentListener = object : FragNavController.RootFragmentListener {
            override fun getRootFragment(index: Int): Fragment {
                return LocationListFragment.newInstance()
            }

            override val numberOfRootFragments: Int = 1
        }
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
    }
}