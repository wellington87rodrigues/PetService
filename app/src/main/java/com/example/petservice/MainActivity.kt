package com.example.petservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petservice.model.MainViewModel
import com.example.petservice.ui.ServiceDialog
import com.example.petservice.ui.nav.BottomNavBar
import com.example.petservice.ui.nav.BottomNavItem
import com.example.petservice.ui.nav.MainNavHost
import com.example.petservice.ui.nav.Route
import com.example.petservice.ui.theme.PetServiceTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel by viewModels()
            var showDialog by remember { mutableStateOf(false) }
            var currentLatLng by remember { mutableStateOf<LatLng?>(null) }
            val context = LocalContext.current
            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.hasRoute(Route.Home::class) == true
            val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
            ) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                ) {
                    getLastLocation(context) { latLng ->
                        currentLatLng = latLng
                    }
                } else {
                    Toast.makeText(context, "Permissão de localização negada.", Toast.LENGTH_SHORT).show()
                }
            }
            PetServiceTheme {
                if (showDialog) { ServiceDialog(
                    onDismiss = {
                        showDialog = false
                        currentLatLng = null
                        },
                        onConfirm = { description, serviceType, location ->
                            if (description.isNotBlank()) {
                                viewModel.add(description, serviceType, location)
                                Toast.makeText(context, "Serviço adicionado!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Descrição do serviço não pode ser vazia.", Toast.LENGTH_SHORT).show()
                            }
                            showDialog = false
                            currentLatLng = null
                        },
                        onGetLocationClick = {
                            when {
                                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                                    getLastLocation(context) { latLng ->
                                        currentLatLng = latLng
                                    }
                                }
                                else -> {
                                    requestLocationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            }
                        },
                        currentLocation = currentLatLng
                    )
                }
                Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Bem-vindo/a!") },
                                actions = {
                                    IconButton( onClick = {
                                        Firebase.auth.signOut()
                                    } ) {
                                        Icon(
                                            imageVector =
                                                Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            val items = listOf(
                                BottomNavItem.HomeButton,
                                BottomNavItem.ListButton,
                                BottomNavItem.MapButton,
                            )
                            BottomNavBar(navController = navController, items)
                        },
                        floatingActionButton = {
                            if(showButton) {
                                FloatingActionButton(onClick = { showDialog = true }) {
                                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                                }
                            }
                        }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavHost(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(context: Context, onLocationResult: (LatLng?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    onLocationResult(latLng)
                    Toast.makeText(context, "Localização obtida!", Toast.LENGTH_SHORT).show()
                } else {
                    onLocationResult(null)
                    Toast.makeText(context, "Não foi possível obter a localização atual.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                onLocationResult(null)
                Toast.makeText(context, "Erro ao obter localização: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}