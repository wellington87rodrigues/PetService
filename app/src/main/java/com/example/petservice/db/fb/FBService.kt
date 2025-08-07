package com.example.petservice.db.fb


import com.google.android.gms.maps.model.LatLng
import com.example.petservice.model.Service

class FBService {
    var id: String? = null
    var descricao: String? = null
    var serviceTypes: List<String>? = null
    var lat: Double? = null
    var lng: Double? = null
    var status: String? = null
    var solicitanteId: String? = null
    var atendenteId: String? = null

    fun toService(): Service {
        val latLng = if (lat != null && lng != null) LatLng(lat!!, lng!!) else null
        return Service(
            id = id ?: "",
            descricao = descricao ?: "",
            serviceTypes = serviceTypes ?: emptyList(),
            location = latLng,
            status = status ?: "em_aberto",
            solicitanteId = solicitanteId ?: "",
            atendenteId = atendenteId
        )
    }
}

fun Service.toFBService(): FBService {
    val fb = FBService()
    fb.id = this.id
    fb.descricao = this.descricao
    fb.serviceTypes = this.serviceTypes
    fb.lat = this.location?.latitude
    fb.lng = this.location?.longitude
    fb.status = this.status
    fb.solicitanteId = this.solicitanteId
    fb.atendenteId = this.atendenteId
    return fb
}