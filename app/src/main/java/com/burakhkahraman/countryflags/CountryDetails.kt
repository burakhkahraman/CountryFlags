package com.burakhkahraman.countryflags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.burakhkahraman.countryflags.databinding.FragmentCountryDetailsBinding
import com.burakhkahraman.countryflags.viewmodel.CountryDetailsViewModel
import com.burakhkahraman.countryflags.network.response.Country
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CountryDetails : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val viewModel = CountryDetailsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CountryDetailsArgs by navArgs()
        val country = args.country

        setupObservers()
        viewModel.setCountryDetails(country)

        mapView = binding.countryMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        MapsInitializer.initialize(requireContext())
    }

    private fun setupObservers() {
        viewModel.onCountryDetailsUpdated = { country ->
            binding.txtCountryName.text = "Country Name: ${country.name.common}"
            binding.txtCountryPopulation.text = "Population: ${country.population}"
            binding.txtCapital.text = "Capital City: ${country.capital.joinToString(", ")}"
            Glide.with(binding.imageView2.context).load(country.flags.png).into(binding.imageView2)

            googleMap?.let { setupMap(it, country) }
        }

        viewModel.onError = { error ->
            showAlertDialog(error)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val args: CountryDetailsArgs by navArgs()
        val country = args.country

        country?.let { setupMap(googleMap, it) }
    }

    private fun setupMap(googleMap: GoogleMap, country: Country) {
        country.capitalInfo?.latlng?.let { latlng ->
            if (latlng.size >= 2) {
                val capitalLocation = LatLng(latlng[0], latlng[1])
                googleMap.addMarker(
                    MarkerOptions().position(capitalLocation)
                        .title("Capital: ${country.capital.joinToString(", ")}")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(capitalLocation, 10f))
            }
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}