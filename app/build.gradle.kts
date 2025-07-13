plugins {
//    alias(libs.plugins.android.application)
    id("com.android.application")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.example.tuitioninfoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tuitioninfoapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}



    dependencies {

        implementation(libs.appcompat)
        implementation(libs.material) // Uses 1.12.0 from libs.versions.toml
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        implementation(libs.recyclerview) // Uses 1.4.0 from libs.versions.toml
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)

        // Firebase
        // Import the BoM for the Firebase platform
        implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
        // Declare আন্দাজdependencies for Firebase products without versions
        // Versions are managed by the BoM
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-firestore")
        implementation(libs.firebase.auth) // Uses 23.2.1 from toml, consistent with BoM or BoM will override
        implementation(libs.firebase.storage) // Uses 21.0.2 from toml

        // ZXing QR code scanning
        // You only need to declare this once
        implementation("com.journeyapps:zxing-android-embedded:4.3.0")
        implementation("com.google.zxing:core:3.5.2") // Ensure this version is compatible

        // CameraX dependencies
        implementation("androidx.camera:camera-core:1.3.1")
        implementation("androidx.camera:camera-lifecycle:1.3.1")
        implementation("androidx.camera:camera-view:1.3.1")
        implementation("androidx.camera:camera-camera2:1.3.1")

        // Glide
        implementation("com.github.bumptech.glide:glide:4.16.0") // Corrected version
        annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // Consider aligning this version with the glide implementation if issues persist

        // Google Play Services for Maps and Location
        implementation("com.google.android.gms:play-services-maps:18.2.0")
        implementation("com.google.android.gms:play-services-location:21.0.1")
        implementation ("com.google.firebase:firebase-messaging:23.4.0")
        implementation ("com.google.android.material:material:1.11.0")


    }


