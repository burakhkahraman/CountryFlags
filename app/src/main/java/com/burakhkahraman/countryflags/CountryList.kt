package com.burakhkahraman.countryflags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakhkahraman.countryflags.databinding.FragmentCountryListBinding
import com.burakhkahraman.countryflags.viewmodel.CountryListViewModel
import com.burakhkahraman.countryflags.network.response.Country
//import com.burakhkahraman.itunessearch.network.response.SearchResultResponse


class CountryList : Fragment() {
    private lateinit var binding: FragmentCountryListBinding
    private lateinit var countryAdapter: CountryAdapter
    private var countryList: List<Country> = emptyList()
    private val viewModel = CountryListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        viewModel.fetchCountries()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        countryAdapter = CountryAdapter(emptyList())
        binding.countryRecyclerView.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.onCountriesUpdated = { countries ->
            countryList = countries
            countryAdapter.updateCountryList(countries)
        }

        viewModel.onError = { error ->
            showAlertDialog(error)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCountries(newText.orEmpty())
                return true
            }
        })
    }

    private fun filterCountries(term: String) {
        val filteredList = countryList.filter {
            it.name.common.contains(term, ignoreCase = true)
        }
        if (filteredList.isEmpty() && term.isNotEmpty()) {
            showAlertDialog(term)
        }
        countryAdapter.updateCountryList(filteredList)
    }

    private fun showAlertDialog(query: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("No Results Found")
            .setMessage("No country named \"$query\" was found.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}