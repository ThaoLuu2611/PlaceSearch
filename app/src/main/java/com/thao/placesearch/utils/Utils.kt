package com.thao.placesearch.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

class Utils {
    companion object {
        var LATITUDE = 20.9648872
        var LONGITUDE = 105.7954515
        fun getFragment(fragmentName: String, bundle: Bundle?): Fragment {
            try {
                val tags = fragmentName.split("_")
                val fragment = Class.forName(tags[0]).newInstance() as Fragment
                fragment.arguments = bundle
                return fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }

            throw Exception("This fragment Not exist")
        }
    }

}