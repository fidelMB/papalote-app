package com.example.papalote_app.screens
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.papalote_app.R
import com.example.papalote_app.model.UserProfile
import coil.compose.rememberImagePainter
import com.example.papalote_app.model.UserData

@Composable
fun Profile(
    userData: UserData,
    navController: NavController,
    user: UserProfile,
    onSignOut: () -> Unit,
    onImageChange: (Uri) -> Unit = {},  // Predeterminado vacío si no se usa
    onDefaultImageSelect: (Int) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    // Opciones predeterminadas de imágenes
    val defaultImages = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF1D1B20),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
            )

            // Imagen de perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, Color.LightGray)
                ) {
                    // Usar la imagen según `profilePicture`
                    Image(
                        painter = painterResource(
                            id = defaultImages[userData.profilePicture - 1]
                        ),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Botón para editar la imagen
                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .offset(x = 40.dp, y = 40.dp)
                        .size(32.dp)
                        .background(Color(0xFFC4D600), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change profile picture",
                        tint = Color(0XFF5C631D),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Nombre y correo del usuario
            Text(
                text = userData.fullName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1D1B20)
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userData.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            // Opciones predeterminadas (solo si se selecciona editar imagen)
            if (showDialog.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Selecciona un avatar predeterminado:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D1B20)
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        defaultImages.forEachIndexed { index, imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Avatar $index",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clickable {
                                        userData.profilePicture = index + 1
                                        onDefaultImageSelect(index + 1)
                                        showDialog.value = false // Cierra el diálogo
                                    }
                                    .border(
                                        BorderStroke(
                                            2.dp,
                                            if (userData.profilePicture == index + 1) Color.Green else Color.Gray
                                        ),
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }

            // Botón de cerrar sesión
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC4D600)
                ),
                shape = RoundedCornerShape(40.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0XFF5C631D)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Cerrar sesión",
                        color = Color(0XFF5C631D)
                    )
                }
            }
        }
    }
}

