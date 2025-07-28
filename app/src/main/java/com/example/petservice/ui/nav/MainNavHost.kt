package com.example.petservice.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.petservice.model.MainViewModel
import com.example.petservice.ui.Page.HomePage
import com.example.petservice.ui.Page.ListPage
import com.example.petservice.ui.Page.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,

) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(viewModel = MainViewModel()) }
        composable<Route.List> { ListPage(viewModel = MainViewModel()) }
        composable<Route.Map>  { MapPage (viewModel = MainViewModel()) }
    }
}