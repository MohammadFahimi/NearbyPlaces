package com.mfahimi.nearbyplace.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.databinding.DfEnableLocationBinding


class EnableLocationDialog : DialogFragment(R.layout.df_enable_location) {
    private var _binding: DfEnableLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // request a window without the title
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DfEnableLocationBinding.bind(view)
        binding.tvYes.setOnClickListener {
            setResultBack(true)
        }
        binding.tvNo.setOnClickListener {
            setResultBack(false)
        }
    }

    private fun setResultBack(turnOnGps: Boolean) {
        parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESULT to turnOnGps))
        dismiss()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val REQUEST_KEY = "_key_location_request"
        const val KEY_RESULT = "_key_result"
        fun newInstance() = EnableLocationDialog()
    }
}