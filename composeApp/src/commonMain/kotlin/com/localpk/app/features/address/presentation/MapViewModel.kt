package com.localpk.app.features.address.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.address.data.AddAddressUiData
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.address.data.AreaData
import com.localpk.app.features.address.data.Polygon
import com.localpk.app.features.address.data.UserAddress
import com.localpk.app.features.address.domain.GetMapPolygonUseCase
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel(
    private val getMapPolygonUseCase: GetMapPolygonUseCase
) : BaseViewModel<Any>() {

    val serviceAreaList = MutableStateFlow<List<AreaData>>(emptyList())

    private val _mapPolygon = MutableStateFlow<Polygon?>(null)
    val mapPolygon: StateFlow<Polygon?> = _mapPolygon

    private val _isPinLocationOutsideArea = MutableStateFlow<Boolean>(false)
    val isPinLocationOutsideArea: StateFlow<Boolean> = _isPinLocationOutsideArea


   fun fetchMapPolygon() {
        unZipViewStateFlow(
            getMapPolygonUseCase(Unit),
            onSuccess = {
                _mapPolygon.value = it as Polygon
            }
        )

    }

    fun updateIsPinLocationOutsideArea(isPinLocationOutsideArea: Boolean) {
        _isPinLocationOutsideArea.value = isPinLocationOutsideArea
    }

}