package com.thao.placesearch.model.placedetail

import com.thao.placesearch.model.place.Place

data class PlacesDetailResponse(
    val result: Place?,
    val status: String?
) {

}