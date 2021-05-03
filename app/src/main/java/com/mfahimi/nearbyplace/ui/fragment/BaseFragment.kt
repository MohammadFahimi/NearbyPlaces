package com.mfahimi.nearbyplace.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mfahimi.nearbyplace.ui.activity.BaseActivity


abstract class BaseFragment : Fragment() {


    @get:LayoutRes
    protected abstract val layoutResId: Int

    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObjects(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (shouldRecycleView() && rootView != null) return rootView
        rootView = inflater.inflate(layoutResId, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
    }

    protected open fun initObjects(savedInstanceState: Bundle?) = Unit
    protected open fun initViews(view: View, savedInstanceState: Bundle?) = Unit

    protected open fun shouldRecycleView(): Boolean = false

    protected fun baseActivity(): BaseActivity? {
        return activity?.run {
            if (this is BaseActivity) this else null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*prevent from memory leak*/
        if (!shouldRecycleView()) {
            rootView = null
        }
    }
}