package com.example.petservice.ui.Page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.petservice.model.MainViewModel

@Composable
fun MapPage( viewModel: MainViewModel) {

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Blue)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Home",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}
//
//val recife = LatLng(-8.05, -34.9)
//    val caruaru = LatLng(-8.27, -35.98)
//    val joaopessoa = LatLng(-7.12, -34.84)
//
//    val camPosState = rememberCameraPositionState()
//
//    val context = LocalContext.current
//    val hasLocationPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION) ==
//                    PackageManager.PERMISSION_GRANTED
//        )
//    }
//    GoogleMap(
//        Modifier.fillMaxSize(),
//        cameraPositionState = camPosState,
//        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
//        uiSettings = MapUiSettings(myLocationButtonEnabled = true),
//        onMapClick = {
//            viewModel.add("Cidade@${it.latitude}:${it.longitude}", location = it) }
//        ) {
//        viewModel.cities.forEach {
//            if (it.location != null) {
//                Marker( state = MarkerState(position = it.location),
//                    title = it.name, snippet = "${it.location}")
//            }
//        }
//        Marker(
//            state = MarkerState(position = recife),
//            title = "Recife",
//            snippet = "Marcador em Recife",
//            icon = BitmapDescriptorFactory.defaultMarker(
//                BitmapDescriptorFactory.HUE_BLUE)
//        )
//        Marker(
//            state = MarkerState(position = caruaru),
//            title = "Caruaru",
//            snippet = "Marcador em Caruaru",
//            icon = BitmapDescriptorFactory.defaultMarker(
//                BitmapDescriptorFactory.HUE_GREEN)
//        )
//        Marker(
//            state = MarkerState(position = joaopessoa),
//            title = "João Pessoa",
//            snippet = "Marcador em João Pessoa",
//            icon = BitmapDescriptorFactory.defaultMarker(
//                BitmapDescriptorFactory.HUE_ROSE)
//        )
//    }
//}