package com.example.petservice.ui.Page

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.petservice.model.MainViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
        Modifier.fillMaxSize(),
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true),
        onMapClick = {
            viewModel.add("Cidade@${it.latitude}:${it.longitude}", location = it) }
        ) {
            viewModel.servicies.forEach {
                if (it.location != null) {
                    Marker(
                        state = MarkerState(position = it.location),
                        title = it.descriptor, snippet = "${it.location}"
                    )
                }
            }
        }
    }