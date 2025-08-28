package com.example.petservice.ui.Page

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petservice.model.MainViewModel
import com.example.petservice.model.RequestsViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.petservice.model.ServiceStatus


@Composable
fun HomePage( viewModel: MainViewModel) {
    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true),
        onMapClick = {
            //viewModel.add(descricao = "Descrição", serviceTypes = listOf("Resgate"), location = it)
        }
    ) {

//        val reqVM: RequestsViewModel = viewModel()
//        val open by reqVM.openRequests.collectAsState(emptyList())
//        val accepted by reqVM.acceptedByMe.collectAsState(emptyList())
//        val points = (open + accepted).distinctBy { it.id }   // não inclui completed
//
//        points.forEach { service ->
//            service.location?.let { loc ->
//                Marker(
//                    state = MarkerState(position = loc),
//                    title = service.descricao,
//                    snippet = "Tipo: ${service.serviceTypes.joinToString(", ")}",
//                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//                )
//            }
//        }


        viewModel.services.filter { it.status != ServiceStatus.COMPLETED }.forEach { service ->
            if (service.location != null) {
                Marker(
                    state = MarkerState(position = service.location),
                    title = service.descricao,
                    snippet = "Tipo: ${service.serviceTypes.joinToString(", ")}",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}