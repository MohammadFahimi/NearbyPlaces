package com.mfahimi.nearbyplace.ui.fragment

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.app.Constants
import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.databinding.FragmentLocationListBinding
import com.mfahimi.nearbyplace.ext.gone
import com.mfahimi.nearbyplace.ext.handleApiResponse
import com.mfahimi.nearbyplace.ext.requestLocationPermission
import com.mfahimi.nearbyplace.ext.visible
import com.mfahimi.nearbyplace.location.ApplicationLocationProvider
import com.mfahimi.nearbyplace.location.LocationCallback
import com.mfahimi.nearbyplace.model.LocationDataItem
import com.mfahimi.nearbyplace.ui.adapter.AdapterDataModel
import com.mfahimi.nearbyplace.ui.adapter.AdapterListener
import com.mfahimi.nearbyplace.ui.adapter.LocationListAdapter
import com.mfahimi.nearbyplace.ui.contract.OpenLocationSourceSettings
import com.mfahimi.nearbyplace.ui.dialog.EnableLocationDialog
import com.mfahimi.nearbyplace.viewModel.LocationListViewModel
import kotlinx.coroutines.flow.launchIn
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationListFragment :
    NavigationFragment<FragmentLocationListBinding>(R.layout.fragment_location_list) {
    private val binding get() = _binding!!
    private val viewModel: LocationListViewModel by viewModel()
    private var pageIndex = PageStartIndex
    private lateinit var listAdapter: LocationListAdapter
    private var lastLocation: Location? = null
    private var rvState: Parcelable? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var isMoreDataAvailable = false
    private val dataList = ArrayList<AdapterDataModel<LocationDataItem>>()
    private val locationManager by lazy {
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private var locationProvider: ApplicationLocationProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        lifecycleScope.launchWhenResumed { checkPermission() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationListBinding.bind(view)
        binding.progressBar.gone()
        setResultListeners()
        binding.toolbar.tvTitle.text = resources.getString(R.string.nearbyVenue)
        binding.toolbar.refresh.setOnClickListener {
            checkPermission()
        }
        initAdapter()
    }

    private fun setResultListeners() {
        childFragmentManager.setFragmentResultListener(
            EnableLocationDialog.REQUEST_KEY,
            this
        ) { requestKey, bundle ->
            val result = bundle.getBoolean(EnableLocationDialog.KEY_RESULT)
            onClick(result)
        }
    }

    private fun checkPermission() {
        context?.requestLocationPermission { granted, denied, permanentlyDenied ->
            if (granted.isNotEmpty())
                checkIsLocationEnable()
        }

    }

    private fun checkIsLocationEnable() {
        locationProvider = ApplicationLocationProvider(
            oneTime = false,
            context = requireContext(),
            callback = locationCallBack!!
        )
        locationProvider?.startLocationUpdates()
    }

    private var locationCallBack: LocationCallback? = object : LocationCallback {
        override fun onPermissionDenied() {
        }

        override fun onProviderDisabled() {
            val dialogFragment = EnableLocationDialog.newInstance()
            fragNavController().showDialogFragment(dialogFragment)
        }

        override fun onStartListening() {
            binding.progressBar.visible()
            binding.toolbar.refresh.gone()
        }

        override fun onLocationReceived(location: Location) {
            if (lastLocation == null || lastLocation!!.distanceTo(location) > 100) {
                lastLocation = location
                onLocationChanged(location)
            }
        }

        override fun onLocationUnAvailable() {
        }
    }

    private fun initAdapter() {
        listAdapter = LocationListAdapter(object : AdapterListener<LocationDataItem> {
            override fun onLoadMore() {
                listAdapter.addLoadingItem()
                binding.list.post { listAdapter.notifyItemInserted(listAdapter.itemCount - 1) }
                pageIndex++
                viewModel.explore(pageIndex, lastLocation!!)
            }

            override fun retry() {
                viewModel.explore(pageIndex, lastLocation!!)
            }

            override fun onItemClick(item: LocationDataItem) {
                fragNavController().pushFragment(LocationDetailFragment.newInstance(item.id))
            }
        }, dataList)
        listAdapter.setIsMoreDataAvailable(isMoreDataAvailable)
        binding.list.apply {
            mLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = mLayoutManager
            adapter = listAdapter
            mLayoutManager.onRestoreInstanceState(rvState)
        }
    }

    private fun observeData() {
        viewModel.locationData.handleApiResponse(
            onLoad = {
                if (isFirstPage())
                    binding.progressBar.visible()
            },
            onSuccess = {
                if (isFirstPage())
                    binding.progressBar.gone()
                processLocationsData(it)
            },
            onError = {
                if (isFirstPage()) {
                    binding.progressBar.gone()
                } else {
                    listAdapter.updateLoadingState(Response.Error(it))
                    notifyDataChanged(listAdapter.dataList.size - 1)
                }
            }
        ).launchIn(lifecycleScope)
    }

    private fun notifyDataChanged(position: Int) {
        binding.list.post { listAdapter.notifyItemChanged(position) }
    }

    private fun processLocationsData(data: List<LocationDataItem>) {
        binding.list.post { listAdapter.submitList(data) }
        isMoreDataAvailable = !(data.isEmpty() || data.size < Constants.Paging.PAGE_SIZE)
        listAdapter.setIsMoreDataAvailable(isMoreDataAvailable)
        if (isFirstPage() && data.isEmpty())
            showEmptyList()

    }

    private fun isFirstPage(): Boolean = pageIndex == PageStartIndex

    private fun showEmptyList() {
        binding.progressBar.gone()
        binding.emptyList.visible()
        binding.list.gone()
    }

    private fun onLocationChanged(mLocation: Location) {
        pageIndex = PageStartIndex
        lastLocation = mLocation
        listAdapter.clearList()
        viewModel.explore(pageIndex, mLocation)
    }

    override fun onDestroy() {
        locationProvider?.removeLocationUpdates()
        locationCallBack = null
        super.onDestroy()
    }

    private val openLocationSourceSettings =
        registerForActivityResult(OpenLocationSourceSettings()) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                checkIsLocationEnable()
                binding.progressBar.visible()
                binding.emptyList.gone()
                binding.toolbar.refresh.gone()
            }
        }

    private fun onClick(turnOnLocation: Boolean) {
        if (turnOnLocation)
            openLocationSourceSettings.launch(null)
        else
            binding.toolbar.refresh.visible()
    }

    override fun onDestroyView() {
        rvState = mLayoutManager.onSaveInstanceState()
        super.onDestroyView()
    }

    companion object {
        private const val PageStartIndex = 0
        fun newInstance() = LocationListFragment()
    }
}