package com.thao.placesearch.repository

import com.thao.placesearch.model.placedetail.PlacesDetailResponse
import com.thao.placesearch.model.place.PlacesResponse
import retrofit2.Response

class APIClient(private  val apiService: IAPI) {
    suspend fun getNearbyPlaces(location: String, keyword: String, radius: Double, key: String): Response<PlacesResponse?> {
        return apiService.getNearbyPlaces(location = location, keyword = keyword, radius = radius, key = key)
    }

    suspend fun getPlaceDetail(placeId: String?, key: String): Response<PlacesDetailResponse?> {
        return apiService.getPlaceDetail(place_id = placeId,  key = key)
    }
}