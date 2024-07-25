package com.burakhkahraman.countryflags.viewmodel

import com.burakhkahraman.countryflags.network.response.Country

class CountryDetailsViewModel {
    var onCountryDetailsUpdated: ((Country) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun setCountryDetails(country: Country?) {
        if (country != null) {
            onCountryDetailsUpdated?.invoke(country)
        } else {
            onError?.invoke("Country details are missing")
        }
    }
}