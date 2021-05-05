package com.mfahimi.nearbyplace.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.databinding.FragmentLocationDetailBinding
import com.mfahimi.nearbyplace.ext.gone
import com.mfahimi.nearbyplace.ext.handleApiResponse
import com.mfahimi.nearbyplace.ext.visible
import com.mfahimi.nearbyplace.model.LocationDetailItem
import com.mfahimi.nearbyplace.viewModel.LocationDetailViewModel
import kotlinx.coroutines.flow.launchIn
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class LocationDetailFragment :
    NavigationFragment<FragmentLocationDetailBinding>(R.layout.fragment_location_detail) {
    private val binding get() = _binding!!

    private val viewModel: LocationDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationDetailBinding.bind(view)
        binding.toolbar.refresh.gone()
        binding.loadingView.hideView()
        observeData()
        viewModel.getVenueDetail(getExtLocationId())
    }

    private fun observeData() {
        viewModel.detailObservable.handleApiResponse(
            onLoad = {
                binding.loadingView.showLoading(viewModel.detailObservable)
            },
            onSuccess = {
                processData(it)
                binding.loadingView.hideView()
            },
            onError = {
                binding.loadingView.showFullScreenError(it, null) {
                    viewModel.getVenueDetail(getExtLocationId())
                }
            }
        ).launchIn(lifecycleScope)
    }

    private fun processData(data: LocationDetailItem) {
        binding.viewsGroup.visible()
        binding.toolbar.tvTitle.text = data.name
        binding.address.text = data.address
        binding.city.text = data.city
        binding.createdAt.text = data.createData?.let {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it*1000
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format( Date(cal.timeInMillis))
        }

        val lat = data.lat
        val lng = data.lng
        binding.showOnMap.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${data.name})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent);
            }
        }
    }

    private fun getExtLocationId(): String = requireArguments().getString(KEY_LOCATION_ID, "")

    companion object {
        const val KEY_LOCATION_ID = "_key_location_id"
        fun newInstance(locationId: String): LocationDetailFragment {
            return LocationDetailFragment().apply {
                arguments = bundleOf(KEY_LOCATION_ID to locationId)
            }
        }
    }
}