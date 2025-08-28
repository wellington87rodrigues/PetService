package com.example.petservice.db.fb




import com.example.petservice.model.Service
import com.example.petservice.model.ServiceStatus
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class RequestsRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val col = db.collection("requests")

    fun create(description: String, types: List<String>, location: LatLng?) {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("Usuário não logado")
        val data = hashMapOf(
            "descricao" to description,
            "serviceTypes" to types,
            "lat" to location?.latitude,
            "lng" to location?.longitude,
            "status" to ServiceStatus.OPEN,
            "solicitanteId" to uid,
            "atendenteId" to null,
            "openedAt" to FieldValue.serverTimestamp(),
            "acceptedAt" to null,
            "completedAt" to null
        )
        col.add(data) // gera ID; depois operamos sempre com esse ID
    }

    fun listenOpen(onChanged: (List<Service>) -> Unit) =
        col.whereEqualTo("status", ServiceStatus.OPEN)
            .orderBy("openedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onChanged(snap?.documents?.map { d ->
                    val lat = d.getDouble("lat"); val lng = d.getDouble("lng")
                    Service(
                        id = d.id,
                        descricao = d.getString("descricao") ?: "",
                        serviceTypes = (d.get("serviceTypes") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        location = if (lat != null && lng != null) LatLng(lat, lng) else null,
                        status = d.getString("status") ?: ServiceStatus.OPEN,
                        solicitanteId = d.getString("solicitanteId") ?: "",
                        atendenteId = d.getString("atendenteId"),
                        openedAt = (d.getTimestamp("openedAt") as? Timestamp)?.seconds,
                        acceptedAt = (d.getTimestamp("acceptedAt") as? Timestamp)?.seconds,
                        completedAt = (d.getTimestamp("completedAt") as? Timestamp)?.seconds
                    )
                } ?: emptyList())
            }

    fun listenMine(onChanged: (List<Service>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        col.whereEqualTo("solicitanteId", uid)
            .whereIn("status", listOf(ServiceStatus.OPEN, ServiceStatus.ACCEPTED))
            .orderBy("openedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onChanged(snap?.documents?.map { d ->
                    val lat = d.getDouble("lat"); val lng = d.getDouble("lng")
                    Service(
                        id = d.id,
                        descricao = d.getString("descricao") ?: "",
                        serviceTypes = (d.get("serviceTypes") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        location = if (lat != null && lng != null) LatLng(lat, lng) else null,
                        status = d.getString("status") ?: ServiceStatus.OPEN,
                        solicitanteId = d.getString("solicitanteId") ?: "",
                        atendenteId = d.getString("atendenteId")
                    )
                } ?: emptyList())
            }
    }

    fun listenAcceptedByMe(onChanged: (List<Service>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        col.whereEqualTo("atendenteId", uid)
            .whereEqualTo("status", ServiceStatus.ACCEPTED)
            .orderBy("acceptedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onChanged(snap?.documents?.map { d ->
                    val lat = d.getDouble("lat"); val lng = d.getDouble("lng")
                    Service(
                        id = d.id,
                        descricao = d.getString("descricao") ?: "",
                        serviceTypes = (d.get("serviceTypes") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        location = if (lat != null && lng != null) LatLng(lat, lng) else null,
                        status = d.getString("status") ?: ServiceStatus.OPEN,
                        solicitanteId = d.getString("solicitanteId") ?: "",
                        atendenteId = d.getString("atendenteId")
                    )
                } ?: emptyList())
            }
    }

    fun accept(requestId: String, onFail: (String) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return onFail("Usuário não logado")
        val ref = col.document(requestId)
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            val status = snap.getString("status")
            if (status != ServiceStatus.OPEN) error("Já foi aceito/encerrado")
            tx.update(ref, mapOf(
                "status" to ServiceStatus.ACCEPTED,
                "atendenteId" to uid,
                "acceptedAt" to FieldValue.serverTimestamp()
            ))
        }.addOnFailureListener { onFail(it.message ?: "Falha ao aceitar") }
    }

    fun complete(requestId: String, onFail: (String) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return onFail("Usuário não logado")
        val ref = col.document(requestId)
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            val status = snap.getString("status")
            val atendente = snap.getString("atendenteId")
            if (status != ServiceStatus.ACCEPTED || atendente != uid) error("Somente o atendente conclui")
            tx.update(ref, mapOf(
                "status" to ServiceStatus.COMPLETED,
                "completedAt" to FieldValue.serverTimestamp()
            ))
        }.addOnFailureListener { onFail(it.message ?: "Falha ao concluir") }
    }

    fun cancel(requestId: String, onFail: (String) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return onFail("Usuário não logado")
        val ref = col.document(requestId)
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            if (snap.getString("solicitanteId") != uid) error("Apenas o solicitante pode cancelar")
            val status = snap.getString("status")
            if (status == ServiceStatus.COMPLETED) error("Já concluído")
            tx.update(ref, mapOf("status" to ServiceStatus.CANCELED))
        }.addOnFailureListener { onFail(it.message ?: "Falha ao cancelar") }
    }

}