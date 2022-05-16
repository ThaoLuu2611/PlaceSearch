package com.thao.placesearch.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.thao.placesearch.R
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.thao.placesearch.BuildConfig
import com.thao.placesearch.databinding.SearchNearbyPlacesFragmentBinding
import com.thao.placesearch.model.place.Place
import com.thao.placesearch.ui.viewmodel.PlaceViewModel
import com.thao.placesearch.utils.Constants
import com.thao.placesearch.utils.Utils

class SearchNearbyPlacesFragment : Fragment(), PlacesAdapter.OnItemClickListener {
    lateinit var binding: SearchNearbyPlacesFragmentBinding
    private val viewModel: PlaceViewModel by activityViewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationPermissionGranted = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchNearbyPlacesFragmentBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.searchPlace.setIconifiedByDefault(true)
        binding.searchPlace.isFocusable = true
        binding.searchPlace.isIconified = false
        binding.searchPlace.requestFocusFromTouch()
        getLocationPermission()
        getLocation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getNearbyPlaces()
        updateNearbyPlacesUI()
    }

    private fun getNearbyPlaces() {
        binding.searchPlace.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getPlacesNearyby(
                    location = "${Utils.LATITUDE}, ${Utils.LONGITUDE}",
                    keyword = query,
                    radius = Constants.NEARBY_RADIUS,
                    key = BuildConfig.PLACES_API_KEY
                )
                return false
            }
        })
    }

    override fun onItemClick(item: Place) {
        (activity as MainActivity).addFragmentView(
            R.id.myNavHostFragment,
            DetailPlaceFragment::class.java.name,
            null
        )
        viewModel.setPlaceSelectedItem(item)
    }

    private fun updateNearbyPlacesUI() {
        viewModel.placesLiveData.observe(this) { response ->
            binding.progressBar.visibility = View.GONE
            response?.let {

                if (response.results?.isNotEmpty() == true) {
                    binding.layoutPlaces.visibility = View.VISIBLE
                    binding.tvNoPlace.visibility = View.GONE

                    val adapter = PlacesAdapter(requireContext(), response.results, viewModel, this)
                    binding.rcvPlaceItem.layoutManager = LinearLayoutManager(context)
                    binding.rcvPlaceItem.adapter = adapter
                    binding.rcvPlaceItem.addItemDecoration(
                        DividerItemDecoration(
                            this.context,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                } else {
                    binding.apply {
                        layoutPlaces.visibility = View.GONE
                        tvNoPlace.visibility = View.VISIBLE
                        tvNoPlace.text = getString(R.string.no_place_available)
                    }
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (it.key == Manifest.permission.ACCESS_FINE_LOCATION) {
                    locationPermissionGranted = true
                }
                if (locationPermissionGranted) {
                    getLocation()
                }
            }
        }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    location?.let {
                        //var geoCoder = Geocoder(requireContext(), Locale.getDefault())
                        Utils.LATITUDE = location.latitude
                        Utils.LONGITUDE = location.longitude
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}