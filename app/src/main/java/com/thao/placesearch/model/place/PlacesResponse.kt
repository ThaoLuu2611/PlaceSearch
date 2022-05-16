package com.thao.placesearch.model.place

import com.thao.placesearch.model.place.Place

data class PlacesResponse(
    val results: List<Place>?,
    val status: String?
)