package com.thao.placesearch.ui.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.thao.placesearch.ui.viewmodel.PlaceViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.thao.placesearch.BuildConfig
import com.thao.placesearch.R
import com.thao.placesearch.databinding.ActivityMainBinding
import com.thao.placesearch.utils.Utils

class MainActivity : AppCompatActivity() {
    private var currentFragment = ""
    var mFragmentManager: FragmentManager = supportFragmentManager
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.PLACES_API_KEY)
        }

        viewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        addFragmentView(R.id.myNavHostFragment, SearchNearbyPlacesFragment::class.java.name, null)
    }

    fun addFragmentView(
        containerId: Int,
        fragment: String,
        bundle: Bundle?
    ) {

        var fragmentTAg = mFragmentManager.findFragmentByTag(fragment)
        if (fragmentTAg != null)
            return
        currentFragment = fragment
        mFragmentManager.beginTransaction()
            .replace(containerId, Utils.getFragment(fragment, bundle), fragment)
            .addToBackStack(fragment)
            .commit()
    }

    private fun getFragmentCount(): Int {
        return mFragmentManager.backStackEntryCount
    }

    override fun onBackPressed() {
        when {
            getFragmentCount() == 1 -> {

                this.finishAffinity()
            }
            getFragmentCount() > 1 -> {
                mFragmentManager.popBackStack()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}