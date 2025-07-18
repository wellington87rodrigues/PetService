package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng

data class Service(
    val endereco: String? = null,
    val location: LatLng? = null,
    val servicos: MutableList<String> = mutableListOf("Resgate", "Doação", "vende-se", "Cruzamento"),
    val foto: String? = null,
    val animals: MutableList<String> = mutableListOf ("Cachorro", "Gato"),
    val descricao: String
)
