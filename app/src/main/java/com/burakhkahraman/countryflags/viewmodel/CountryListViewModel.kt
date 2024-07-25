package com.burakhkahraman.countryflags.viewmodel

import com.burakhkahraman.countryflags.network.response.Country
import com.burakhkahraman.itunessearch.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryListViewModel{


    var onCountriesUpdated: ((List<Country>) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun fetchCountries() {
        Network.service.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries ->
                        onCountriesUpdated?.invoke(countries)
                    } ?: run {
                        onError?.invoke("No countries found")
                    }
                } else {
                    onError?.invoke("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                onError?.invoke("Failure: ${t.message}")
            }
        })
    }
}