package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
data class Service(
    val id: String = "",
    val descricao: String,
    val serviceTypes: List<String>,
    val location: LatLng? = null,
    val status: String = "em_aberto",
    val solicitanteId: String = "",
    val atendenteId: String? = null
)