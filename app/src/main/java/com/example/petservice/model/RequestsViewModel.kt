package com.example.petservice.model



import androidx.lifecycle.ViewModel
import com.example.petservice.db.fb.RequestsRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RequestsViewModel(
    private val repo: RequestsRepository = RequestsRepository()
) : ViewModel() {

    private val _openRequests = MutableStateFlow<List<Service>>(emptyList())
    val openRequests = _openRequests.asStateFlow()

    private val _myRequests = MutableStateFlow<List<Service>>(emptyList())
    val myRequests = _myRequests.asStateFlow()

    private val _acceptedByMe = MutableStateFlow<List<Service>>(emptyList())
    val acceptedByMe = _acceptedByMe.asStateFlow()

    init {
        repo.listenOpen { _openRequests.value = it }
        repo.listenMine { _myRequests.value = it }
        repo.listenAcceptedByMe { _acceptedByMe.value = it }
    }

    fun createRequest(description: String, types: List<String>, location: LatLng?) {
        repo.create(description, types, location)
    }

    fun acceptRequest(id: String, onFail: (String) -> Unit = {}) =
        repo.accept(id, onFail)

    fun completeRequest(id: String, onFail: (String) -> Unit = {}) =
        repo.complete(id, onFail)

    fun cancelRequest(id: String, onFail: (String) -> Unit = {}) =
        repo.cancel(id, onFail)
}
