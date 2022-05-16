package com.thao.placesearch.repository

import com.thao.placesearch.model.placedetail.PlacesDetailResponse
import com.thao.placesearch.model.place.PlacesResponse

class PlaceSearchRepository {
    suspend fun getNearbyPlaces(location: String, keyword: String, radius: Double, key: String): PlacesResponse? {
        val request = RetrofitBuilder.apiClient.getNearbyPlaces(location, keyword, radius = radius, key = key)
        if(request.isSuccessful) {
            return request.body() as PlacesResponse;
        }
        return null
    }

    suspend fun getPlaceDetail(placeId: String?, key: String): PlacesDetailResponse? {
        val request = RetrofitBuilder.apiClient.getPlaceDetail(placeId, key)
        if(request.isSuccessful) {
            return request.body() as PlacesDetailResponse;
        }
        return null
    }

}