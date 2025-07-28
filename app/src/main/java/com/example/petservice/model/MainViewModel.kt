package com.example.petservice.model

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.mapsplatform.transportation.consumer.model.Location

class MainViewModel : ViewModel() {
    private val _servicies = getServicies().toMutableStateList()
    val servicies
        get() = _servicies.toList()

    fun remove(service: Service) {
        _servicies.remove(service)
    }

    fun add(descriptor: String, location: LatLng? = null) {
        _servicies.add(Service(descriptor = descriptor, location = location))
    }
}

private fun getServicies() = List(6) { i ->
    Service(descriptor = "Descrição $i", photo = "Carregando Photo...")
}
