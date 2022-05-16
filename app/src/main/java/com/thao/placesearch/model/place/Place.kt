package com.thao.placesearch.model.place

import com.thao.placesearch.model.Photos
import com.thao.placesearch.model.placedetail.Geometry

data class Place(
    val geometry: Geometry?,
    val name: String?,
    val photos: List<Photos>?,
    val rating: Double?,
    val user_ratings_total: Int?,
    val place_id: String?,
    val vicinity: String?,
    val website: String?
) {
    fun getPhotoReference() = photos?.get(0)?.photo_reference

    fun getLatitude() = geometry?.location?.lat ?: 0
    fun getLongitude() = geometry?.location?.lng ?: 0
    fun getRatingReview() : String{
        rating?.let{
            return "$rating($user_ratings_total)"
        }
        return ""
    }
}