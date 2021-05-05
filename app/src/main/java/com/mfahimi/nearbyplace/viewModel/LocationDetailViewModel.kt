package com.mfahimi.nearbyplace.viewModel

import androidx.lifecycle.viewModelScope
import com.mfahimi.nearbyplace.data.repositoty.detail.LocationDetailRepo
import com.mfahimi.nearbyplace.model.LocationDetailItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationDetailViewModel(private val repository: LocationDetailRepo) : BaseViewModel() {

    private val _detailObservable = mutableStateFlow<LocationDetailItem>()
    val detailObservable = _detailObservable.immutable()
    fun getVenueDetail(venId: String) = repository.getVenueDetail(venId).onEach {
        _detailObservable.emit(it)
    }.launchIn(viewModelScope)
}
