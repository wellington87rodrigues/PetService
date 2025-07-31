package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng

data class Service(
    val descricao: String,
    val serviceTypes: List<String>,
    val location: LatLng? = null
)