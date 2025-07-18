package com.example.petservice.model

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {
    private val _cities = getCities().toMutableStateList()
    val cities
        get() = _cities.toList()
    fun remove(city: Animal) {
        _cities.remove(city)
    }
    fun add(name: String, location : LatLng? = null) {
        _cities.add(Animal(name = name, location = location))
    }
}

private fun getCities() = List(5) { i ->
    Animal(name = "Animal $i", weather = "Informações do animal")
}