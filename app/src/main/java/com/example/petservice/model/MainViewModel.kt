package com.example.petservice.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petservice.db.fb.FBDatabase
import com.example.petservice.db.fb.FBService
import com.example.petservice.db.fb.FBUser
import com.example.petservice.db.fb.toFBService
import com.google.android.gms.maps.model.LatLng
import com.example.petservice.model.User
import com.example.petservice.model.Service


class MainViewModel(private val db: FBDatabase) : ViewModel(),
    FBDatabase.Listener {


        private val _services = mutableStateListOf<Service>()
        val services
            get() = _services.toList()
        private val _user = mutableStateOf<User?> (null)
        val user : User?
        get() = _user.value
        init {
            db.setListener(this)
        }
        fun remove(service: Service) {
            db.remove(service.toFBService())
        }
        fun add(descricao: String, serviceTypes: List<String>,  location : LatLng? = null, status: String, ) {
            db.add(Service(descricao = descricao, serviceTypes =serviceTypes , location = location, status = status).toFBService())
        }
      
        override fun onUserLoaded(user: FBUser) {
            _user.value = user.toUser()
        }
        override fun onUserSignOut() {
            //TODO("Not yet implemented")
        }

        override fun onServiceAdded(servico: FBService) {
            _services.add(servico.toService())
        }

        override fun onServiceUpdated(servico: FBService) {
            val updated = servico.toService()
            val index = _services.indexOfFirst { it.id == updated.id }
            if (index != -1) {
                _services[index] = updated
            }
        }

        override fun onServiceRemoved(servico: FBService) {
            _services.removeAll { it.id == servico.id }
        }

    }

//    private val _services = getServices().toMutableStateList()
//    val services
//        get() = _services.toList()
//
//    fun remove(service: Service) {
//        _services.remove(service)
//    }
//
//    fun add(descricao: String, serviceType: String, location: LatLng?) {
//        val validServiceTypes = listOf("Adoção", "Resgate")
//        if (serviceType in validServiceTypes) {
//            _services.add(Service(descricao = descricao, serviceTypes = listOf(serviceType), location = location))
//        }
//    }
//}

private fun getServices() = List(2) { i ->
    val serviceTypes = if (i % 2 == 0) listOf("Adoção") else listOf("Resgate")
    Service(
        descricao = "Serviço de Pet ${i+1}",
        serviceTypes = serviceTypes,
        location = null
    )
}

class MainViewModelFactory(private val db : FBDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}