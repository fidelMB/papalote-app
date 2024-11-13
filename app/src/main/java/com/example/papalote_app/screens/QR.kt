package com.example.papalote_app.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.papalote_app.R
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QR(navController: NavController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            scanQR(context)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

fun scanQR(context: Context) {

    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC)
        .build()


    val scanner = GmsBarcodeScanning.getClient(context)


    scanner.startScan()
        .addOnSuccessListener { barcode ->
            Log.d("TAG", "onCreate: ${barcode.rawValue}")
            // Check if the barcode contains a valid URL
            val url = barcode.rawValue
            if (!url.isNullOrEmpty()) {
                // Log the URL for debugging purposes
                Log.d("TAG", "onCreate: $url")

                // Create an Intent to open the URL in the default browser
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                // Make sure to add the FLAG_ACTIVITY_NEW_TASK flag if launching from a non-activity context
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                // Start the activity with the context
                context.startActivity(browserIntent)
            }
        }
        .addOnCanceledListener {
            // Task canceled
        }
        .addOnFailureListener { e ->
            // Task failed with an exception
        }
}