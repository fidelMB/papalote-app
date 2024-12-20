package com.example.papalote_app.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.example.papalote_app.model.UserData
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.papalote_app.R
import com.example.papalote_app.components.BarcodeAnalyser
import com.example.papalote_app.model.Activity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun QR(userData: UserData, firestore: FirebaseFirestore) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        Column(
            modifier = Modifier
                .padding(16.dp, 40.dp, 16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(225.dp)
                    .padding(0.dp, 60.dp, 0.dp, 32.dp)
            )

            PreviewViewComposable(userData, firestore)
        }
    }
}

@kotlin.OptIn(ExperimentalPermissionsApi::class)
@ExperimentalGetImage
@Composable
fun PreviewViewComposable(userData: UserData, firestore: FirebaseFirestore) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val scannedQRCode = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
        } else {
            // Handle permission denial
        }
    }

    LaunchedEffect(cameraPermissionState) {
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            // Show rationale if needed
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(4.dp, Color(0xFFC4D600), RoundedCornerShape(16.dp))
            .clipToBounds()
    ) {
        AndroidView({ context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser { qrContent ->
                            scannedQRCode.value = qrContent
                            var isAlreadyScanned = false

                            for (activity in userData.activities) {
                                if (activity.qr == qrContent) {
                                    isAlreadyScanned = true
                                }
                            }

                            if (isAlreadyScanned == false) {
                                coroutineScope.launch {
                                    firestore
                                        .collection("activities")
                                        .whereEqualTo("qr", qrContent)
                                        .get()
                                        .addOnSuccessListener { documents ->
                                            for (document in documents) {
                                                val scanActivity =
                                                    document.toObject(Activity::class.java)
                                                userData.activities.add(scanActivity)
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d(
                                                "QR Scanner",
                                                "Failure getting activity with QR code",
                                                exception
                                            )
                                        }
                                        .await()

                                    firestore
                                        .collection("users")
                                        .document(userData.userId)
                                        .update("activities", userData.activities)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "Activity Notification Firestore",
                                                "Succesfully updated document with scanned activity"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "Activity Notification Firestore",
                                                "Error updating document",
                                                e
                                            )
                                        }
                                        .await()
                                }
                            }

                            // Open the URL in a browser
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(qrContent)
                            }
                            context.startActivity(intent)

                            Toast.makeText(context, "Scanned: $qrContent", Toast.LENGTH_SHORT).show()
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer)

                } catch(exc: Exception) {
                    Log.e("DEBUG", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))
            previewView
        })
    }
}