package com.example.petservice.db.fb


import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class FBDatabase {
    interface Listener {
        fun onUserLoaded(user: FBUser)
        fun onUserSignOut()
        fun onServiceAdded(servico: FBService)
        fun onServiceUpdated(servico: FBService)
        fun onServiceRemoved(servico: FBService)
    }
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var servicesListReg: ListenerRegistration? = null
    private var listener : Listener? = null
    init {
        auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                servicesListReg?.remove()
                listener?.onUserSignOut()
                return@addAuthStateListener
            }
            val refCurrUser = db.collection("users").document(auth.currentUser!!.uid)
            refCurrUser.get().addOnSuccessListener {
                it.toObject(FBUser::class.java)?.let { user ->
                    listener?.onUserLoaded(user)
                }
            }
            servicesListReg = refCurrUser.collection("services")
                .addSnapshotListener { snapshots, ex ->
                    if (ex != null) return@addSnapshotListener
                    snapshots?.documentChanges?.forEach { change ->
                        val fbService = change.document.toObject(FBService::class.java)
                        fbService.id = change.document.id
                        if (change.type == DocumentChange.Type.ADDED) {
                            listener?.onServiceAdded(fbService)
                        } else if (change.type == DocumentChange.Type.MODIFIED) {
                            listener?.onServiceUpdated(fbService)
                        } else if (change.type == DocumentChange.Type.REMOVED) {
                            listener?.onServiceRemoved(fbService)
                        }
                    }
                }
        }

    }

    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }
    fun register(user: FBUser) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid + "").set(user);
    }

    fun add(service: FBService) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        if (service.serviceTypes == null || service.serviceTypes!!.isEmpty())
            throw RuntimeException("Tipo Servico with null or empty name!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).collection("services")
            .add(service)
//        val tiposString = service.serviceTypes?.joinToString(", ") ?: "Servico Padrão"
//        db.collection("users").document(uid).collection("services")
//            .document(tiposString!!).set(service)
    }
    fun remove(service: FBService) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        if (service.serviceTypes == null || service.serviceTypes!!.isEmpty())
            throw RuntimeException("Tipo Servico with null or empty name!")
        val uid = auth.currentUser!!.uid
        val tiposString = service.serviceTypes?.joinToString(", ") ?: "Servico Padrão"
        db.collection("users").document(uid).collection("services")
            .document(tiposString!!).delete()
    }
}
