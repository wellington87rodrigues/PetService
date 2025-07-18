package com.example.petservice

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.petservice.ui.theme.CityDialog
import com.example.petservice.model.MainViewModel
import com.example.petservice.ui.nav.BottomNavBar
import com.example.petservice.ui.nav.BottomNavItem
import com.example.petservice.ui.nav.MainNavHost
import com.example.petservice.ui.nav.Route
import com.example.petservice.ui.theme.PetServiceTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var showDialog by remember { mutableStateOf(false) }
            val viewModel : MainViewModel by viewModels()
            val navController = rememberNavController()
            val currentRouter = navController.currentBackStackEntryAsState()
            val showButton = currentRouter.value?.destination?.hasRoute(Route.List::class) == true
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {} )

            PetServiceTheme {
                if (showDialog) CityDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { city ->
                        if (city.isNotBlank()) viewModel.add(city)
                        showDialog = false
                    })
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a!") },
                            actions = {
                                IconButton( onClick = { finish() } ) {
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
                        if(showButton){
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        MainNavHost(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}