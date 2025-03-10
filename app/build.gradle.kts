plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.application.timer_dmb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.application.timer_dmb"
        minSdk = 27
        targetSdk = 34
        versionCode = 6
        versionName = "1.1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }



    buildTypes {

        debug {
            isMinifyEnabled = false
            isDebuggable = true
            buildConfigField("String", "BASE_URL", "\"93.183.82.224\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = true
            isDebuggable = false
            buildConfigField("String", "BASE_URL", "\"5.23.52.32\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.android.compiler)
    ksp (libs.androidx.hilt.compiler)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation("androidx.hilt:hilt-work:1.2.0")

    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.ktor.client.auth)
    implementation(libs.ktor.client.websockets)

    //Splash
    implementation(libs.androidx.core.splashscreen)

    //AsyncImage
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Blur
    implementation(libs.haze.jetpack.compose)

    //Calendar
    implementation ("io.github.boguszpawlowski.composecalendar:composecalendar:1.3.0")

    //SwipeRefresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    //WheelDatePicker
    implementation ("com.github.ozcanalasalvar.picker:datepicker:2.0.7")
    implementation ("com.github.ozcanalasalvar.picker:wheelview:2.0.7")

    //BottomSheetDialog
    implementation ("com.holix.android:bottomsheetdialog-compose:1.5.1")

    //crop
    implementation("io.github.mr0xf00:easycrop:0.1.1")

    //Room
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    //CaptureImage
    implementation ("dev.shreyaspatil:capturable:2.1.0")

    //Widgets
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material)

    //Workers
    implementation(libs.androidx.work.runtime.ktx)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-messaging")


}