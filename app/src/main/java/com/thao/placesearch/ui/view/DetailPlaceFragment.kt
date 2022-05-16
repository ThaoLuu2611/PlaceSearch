package com.thao.placesearch.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.thao.placesearch.BuildConfig
import com.thao.placesearch.R
import com.thao.placesearch.ui.viewmodel.PlaceViewModel
import com.thao.placesearch.databinding.DetailPlaceFragmentBinding
import com.thao.placesearch.model.place.Place
import com.thao.placesearch.model.placedetail.PlacesDetailResponse

class DetailPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: DetailPlaceFragmentBinding
    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = DetailPlaceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val placeViewModel: PlaceViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        placeViewModel.placeSelectedItem.observe(this) { place ->
            if (place != null) {
                getPlaceDetail(place)
            }
        }
        placeViewModel.placeDetailLiveData.observe(this) {
            it?.let {
                bindUI(it)
            }
        }
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    private fun getPlaceDetail(place: Place) {
        placeViewModel.getPlaceDetail(
            placeId = place.place_id,
            key = BuildConfig.PLACES_API_KEY
        )
    }

    private fun bindUI(placeDetail: PlacesDetailResponse) {
        val mPlace = placeDetail.result
        binding.apply {
            place = mPlace
            viewModel = placeViewModel
            mPlace?.getPhotoReference()?.let {
                val width = mPlace.photos?.get(0)?.width
                val height: Int? = mPlace.photos?.get(0)?.height
                var photoUrl =
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=$width&maxheight=$height&photoreference=$it&key=${
                        BuildConfig.PLACES_API_KEY
                    }"
                Glide.with(requireActivity()).load(photoUrl)
                    .skipMemoryCache(true)
                    .into(imvPhoto)
            } ?: kotlin.run {
                tvNoPhoto.visibility = View.VISIBLE
             }
            val latLng = LatLng(mPlace?.getLatitude() as Double, mPlace?.getLongitude() as Double)
            mMap?.addMarker(MarkerOptions().position(latLng).title(mPlace.name))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng ))
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            placeViewModel.setPlaceDetailValue(null)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}