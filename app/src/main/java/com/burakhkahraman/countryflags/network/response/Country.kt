package com.burakhkahraman.countryflags.network.response


import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Country(
    val name: Name,
    val flags: Flags,
    val population: Int,
    val capital: List<String>,
    val capitalInfo: CapitalInfo?
): Parcelable

@Parcelize
data class Name(
    val common: String,
    val official: String
):Parcelable

@Parcelize
data class Flags(
    val png: String,
    val svg:String
):Parcelable

@Parcelize
data class CapitalInfo(
    val latlng: List<Double>?
): Parcelable