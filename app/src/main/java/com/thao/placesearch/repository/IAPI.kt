package com.thao.placesearch.repository

import com.thao.placesearch.model.placedetail.PlacesDetailResponse
import com.thao.placesearch.model.place.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IAPI{
    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(@Query("location") location: String, @Query("radius") radius: Double, @Query("keyword") keyword: String?, @Query("key") key: String): Response<PlacesResponse?>

    @GET("details/json")
    suspend fun getPlaceDetail(@Query("place_id") place_id: String?, @Query("key") key: String): Response<PlacesDetailResponse?>
}