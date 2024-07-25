package com.burakhkahraman.countryflags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.burakhkahraman.countryflags.databinding.CountryItemRowBinding
import com.burakhkahraman.countryflags.network.response.Country


class CountryAdapter(private var countryList: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(val binding: CountryItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country) {
            binding.txtCountryName.text = "Country Name: ${country.name.common}"
            binding.txtPopulation.text = "Population: ${country.population.toString()}"
            binding.txtCapital.text = "Capital City: ${country.capital.joinToString(", ")}"
            Glide.with(binding.imageView.context).load(country.flags.png).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])

        holder.itemView.setOnClickListener {
            val action =
                CountryListDirections.actionCountryListToCountryDetails(countryList[position])
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = countryList.size

    fun updateCountryList(newList: List<Country>) {
        countryList = newList
        notifyDataSetChanged()
    }
}