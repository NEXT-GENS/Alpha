plugins {
    id("com.android.application") version "7.4.2"
    kotlin("android") version "1.7.20"
}

android {
    compileSdk = 33 // Menambahkan '='
    namespace = "id.extension.komikindo"

    defaultConfig {
        applicationId = "id.extension.komikindo"
        minSdk = 21 // Menambahkan '='
        targetSdk = 33 // Menambahkan '='
        versionCode = 1 // Menambahkan '='
        versionName = "1.0" // Menambahkan '='
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    sourceSets {
        getByName("main") {
            java.setSrcDirs(listOf("src/main/java"))
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}
