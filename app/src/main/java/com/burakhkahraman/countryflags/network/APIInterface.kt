package com.burakhkahraman.itunessearch.network

import com.burakhkahraman.countryflags.network.response.Country
//import com.burakhkahraman.itunessearch.network.response.SearchResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("v3.1/all")
   fun getCountries(): Call<List<Country>>
}