plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-parcelize")
    
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.papalote_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.papalote_app"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation ("androidx.compose.material:material-icons-extended:1.0.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("io.coil-kt:coil-compose:1.4.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("androidx.compose.ui:ui:1.1.0-beta01")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha07")
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.navigation:navigation-compose:2.4.0-beta02")
    implementation ("io.coil-kt:coil-compose:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.activity:activity-ktx:1.2.3")
    implementation ("androidx.fragment:fragment-ktx:1.3.5")

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("io.coil-kt.coil3:coil-compose:3.0.1")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.1")

    implementation( "org.jetbrains.kotlin:kotlin-parcelize-runtime:1.4.20")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1")) // Check for latest version
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")  // Esta dependencia se utiliza para la autenticación
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")  // Esta dependendencia se utiliza para la firestore database

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}