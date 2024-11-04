package com.example.papalote_app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.papalote_app.navigation.Screen

@Composable
fun NavBar(modifier: Modifier = Modifier, navController: NavController) {

        NavigationBar {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBarItem(
                icon = {

                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Events")

                },

                onClick = { navController.navigate(Screen.Events.route) },
                selected = currentRoute == Screen.Events.route
            )

            NavigationBarItem(
                icon = {

                    Icon(imageVector = Icons.Outlined.Place, contentDescription = "Map")

                },
                onClick = { navController.navigate(Screen.Map.route) },
                selected = currentRoute == Screen.Map.route
            )

            NavigationBarItem(
                icon = {
                    Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = "QR")

                },
                onClick = { navController.navigate(Screen.QR.route) },
                selected = currentRoute == Screen.QR.route
            )

            NavigationBarItem(
                icon = {
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Favorites")

                },
                onClick = { navController.navigate(Screen.Favorites.route) },
                selected = currentRoute == Screen.Favorites.route
            )

            NavigationBarItem(
                icon = {
                    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Profile")

                },
                onClick = { navController.navigate(Screen.Profile.route) },
                selected = currentRoute == Screen.Profile.route
            )

        }



}