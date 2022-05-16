package com.thao.placesearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thao.placesearch.model.place.Place
import com.thao.placesearch.model.placedetail.PlacesDetailResponse
import com.thao.placesearch.model.place.PlacesResponse
import com.thao.placesearch.repository.PlaceSearchRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PlaceViewModel : ViewModel(), CoroutineScope {
    private var job = Job()
    private var repository: PlaceSearchRepository = PlaceSearchRepository()

    private val _placesLiveData = MutableLiveData<PlacesResponse?>()
    val placesLiveData: LiveData<PlacesResponse?> = _placesLiveData

    private val _placeItemLiveData = MutableLiveData<Place?>()
    val placeSelectedItem: MutableLiveData<Place?> = _placeItemLiveData

    private val _placeDetailLiveData = MutableLiveData<PlacesDetailResponse?>()
    val placeDetailLiveData: MutableLiveData<PlacesDetailResponse?> = _placeDetailLiveData

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    fun setPlaceSelectedItem(item: Place) {
        placeSelectedItem.value = item
    }

    fun getPlacesNearyby(location: String, keyword: String, radius: Double, key: String) {
        scope.launch {
            val response = repository.getNearbyPlaces(
                location = location,
                keyword = keyword,
                radius = radius,
                key = key
            )
            _placesLiveData.postValue(response)
        }
    }

    fun getPlaceDetail(placeId: String?, key: String) {
        scope.launch {
            repository.getPlaceDetail(placeId = placeId, key = key).apply {
                setPlaceDetailValue(this)
            }
        }
    }

    fun setPlaceDetailValue(placesDetailResponse: PlacesDetailResponse?) {
        _placeDetailLiveData.postValue(placesDetailResponse)
    }

    fun isShowWebsite(place: Place) = !place.website.isNullOrEmpty()

    fun getRating(place: Place): String? {
        place.rating?.let {
            if(it != 0.0)
                return "$it (${place.user_ratings_total})"
        }
        return null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}