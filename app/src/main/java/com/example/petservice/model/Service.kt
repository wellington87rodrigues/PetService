package com.example.petservice.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
data class Service(
    val id: String = "",                // documentId do Firestore
    val descricao: String = "",
    val serviceTypes: List<String> = emptyList(),
    val location: LatLng? = null,

    // fluxo "partida dobrada"
    val status: String = ServiceStatus.OPEN,
    val solicitanteId: String = "",     // uid de quem abriu
    val atendenteId: String? = null,    // uid de quem aceitou (se houver)

    // (opcional) para histórico
    val openedAt: Long? = null,
    val acceptedAt: Long? = null,
    val completedAt: Long? = null
)