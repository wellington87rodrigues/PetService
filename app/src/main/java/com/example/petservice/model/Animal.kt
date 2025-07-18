package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng

data class Animal (
    val name : String,
    val weather: String? = null,
    val location: LatLng? = null
)