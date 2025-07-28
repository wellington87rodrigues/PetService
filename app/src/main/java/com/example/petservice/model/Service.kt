package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng

data class Service (
    val descriptor: String,
    val photo: String? = null,
    //val serviceTypes?: List<String>,
    val location: LatLng?  = null
)