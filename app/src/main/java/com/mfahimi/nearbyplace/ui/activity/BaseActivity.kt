package com.mfahimi.nearbyplace.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    @AnimRes
    protected open val startEnterAnim = 0

    @AnimRes
    protected open val startExitAnim = 0

    @AnimRes
    protected open val finishEnterAnim = 0

    @AnimRes
    protected open val finishExitAnim = 0

    protected open val screenOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = screenOrientation
        if (layoutResId != 0)
            setContentView(layoutResId)
        initObjects(savedInstanceState)
        initViews(savedInstanceState)
    }

    protected open fun initObjects(savedInstanceState: Bundle?) {}

    protected open fun initViews(savedInstanceState: Bundle?) {}

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(startEnterAnim, startExitAnim)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(finishEnterAnim, finishExitAnim)
    }
}