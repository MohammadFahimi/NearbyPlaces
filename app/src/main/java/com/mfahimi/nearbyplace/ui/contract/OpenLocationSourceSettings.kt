package com.mfahimi.nearbyplace.ui.contract

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class OpenLocationSourceSettings : ActivityResultContract<Any?, Any?>() {

    override fun createIntent(context: Context, input: Any?): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Any? {
        return null
    }
}