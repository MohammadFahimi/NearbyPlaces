package com.mfahimi.nearbyplace.viewModel

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.mfahimi.nearbyplace.data.network.Response
import com.mfahimi.nearbyplace.data.repositoty.list.LocationListRepo
import com.mfahimi.nearbyplace.model.LocationDataItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationListViewModel(private val repository: LocationListRepo) : BaseViewModel() {

    private val _locationData = mutableStateFlow<List<LocationDataItem>>()
    val locationData = _locationData.immutable()
    fun explore(pageIndex: Int,location:Location) {
        repository.getLocationList(pageIndex, location.latitude, location.longitude).onEach {
            _locationData.emit(it)
        }.launchIn(viewModelScope)
    }
}
