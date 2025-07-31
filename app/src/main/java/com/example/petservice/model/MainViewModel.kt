package com.example.petservice.model

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {

    private val _services = getServices().toMutableStateList()
    val services
        get() = _services.toList()

    fun remove(service: Service) {
        _services.remove(service)
    }

    fun add(descricao: String, serviceType: String, location: LatLng?) {
        val validServiceTypes = listOf("Adoção", "Resgate")
        if (serviceType in validServiceTypes) {
            _services.add(Service(descricao = descricao, serviceTypes = listOf(serviceType), location = location))
        }
    }
}

private fun getServices() = List(2) { i ->
    val serviceTypes = if (i % 2 == 0) listOf("Adoção") else listOf("Resgate")
    Service(
        descricao = "Serviço de Pet ${i+1}",
        serviceTypes = serviceTypes,
        location = null
    )
}