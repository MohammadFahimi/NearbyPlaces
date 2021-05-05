package com.mfahimi.nearbyplace.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mfahimi.nearbyplace.ui.activity.BaseActivity


abstract class BaseFragment<T : ViewBinding>(layoutResId: Int) : Fragment(layoutResId) {

    protected var _binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObjects(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
    }

    protected open fun initObjects(savedInstanceState: Bundle?) = Unit
    protected open fun initViews(view: View, savedInstanceState: Bundle?) = Unit

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun baseActivity(): BaseActivity? {
        return activity?.run {
            if (this is BaseActivity) this else null
        }
    }
}