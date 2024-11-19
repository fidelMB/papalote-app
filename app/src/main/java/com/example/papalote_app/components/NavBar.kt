package com.example.papalote_app.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.papalote_app.navigation.Screen
import android.Manifest
import android.util.Log

@Composable
fun NavBar(modifier: Modifier = Modifier, navController: NavController) {

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("NavBar", "Notification permission granted")
        } else {
            Log.d("NavBar", "Notification permission denied")
        }
    }


    NavigationBar(
        modifier = modifier.drawBehind {
            drawLine(
                color = Color(0xFFD9DBD0),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2.dp.toPx()
            )
        },
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // boton de eventos, al hacer click también va a pedir permisos de notificaciones en caso de no tener
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Events")
            },
            onClick = {
                        navController.navigate(Screen.Events.route)
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                      },
                selected = currentRoute == Screen.Events.route,
                colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5C631D),
                indicatorColor = Color(0xFFC4D600),
                unselectedIconColor = Color.Black,
            )

        )

        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Outlined.Place, contentDescription = "Map")
            },
            onClick = { navController.navigate(Screen.Map.route) },
            selected = currentRoute == Screen.Map.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5C631D),
                indicatorColor = Color(0xFFC4D600),
                unselectedIconColor = Color.Black,
            )
        )

        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = "QR")
            },
            onClick = { navController.navigate(Screen.QR.route) },
            selected = currentRoute == Screen.QR.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5C631D),
                indicatorColor = Color(0xFFC4D600),
                unselectedIconColor = Color.Black,
            )
        )

        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Favorites")
            },
            onClick = { navController.navigate(Screen.Favorites.route) },
            selected = currentRoute == Screen.Favorites.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5C631D),
                indicatorColor = Color(0xFFC4D600),
                unselectedIconColor = Color.Black,
            )
        )

        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Profile")
            },
            onClick = { navController.navigate(Screen.Profile.route) },
            selected = currentRoute == Screen.Profile.route,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5C631D),
                indicatorColor = Color(0xFFC4D600),
                unselectedIconColor = Color.Black,
            )
        )
    }
}
