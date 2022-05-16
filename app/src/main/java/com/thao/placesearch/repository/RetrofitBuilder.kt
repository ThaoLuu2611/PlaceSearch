package com.thao.placesearch.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val clientService: IAPI by lazy {
        retrofit.create(IAPI::class.java)
    }
    val apiClient = APIClient(clientService)

}