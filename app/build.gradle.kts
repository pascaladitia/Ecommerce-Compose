plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.pascal.ecommercecompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pascal.ecommercecompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    applicationVariants.all{
        outputs.all {
            if(name.contains("release"))
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl)
                    .outputFileName = "app-$name-$versionName.apk"
        }
    }

    flavorDimensions += "environment"
    var baseUrl : String
    var apiKey : String

    productFlavors {
        create("dev") {
            baseUrl = "sample_url"
            apiKey = "api-key"
            dimension = "environment"
            buildConfigField("String", "API_KEY", "\"" + apiKey + "\"")
            buildConfigField("String", "BASE_URL", "\"" + baseUrl + "\"")
        }

        create("staging") {
            baseUrl = "sample_url"
            apiKey = "api-key"
            dimension = "environment"
            buildConfigField("String", "API_KEY", "\"" + apiKey + "\"")
            buildConfigField("String", "BASE_URL", "\"" + baseUrl + "\"")
        }

        create("prod") {
            baseUrl = "sample_url"
            apiKey = "api-key"
            dimension = "environment"
            buildConfigField("String", "API_KEY", "\"" + apiKey + "\"")
            buildConfigField("String", "BASE_URL", "\"" + baseUrl + "\"")
        }
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

room {
    schemaDirectory("$projectDir/schemas")
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

    // UI
    implementation(libs.constraintlayout.compose)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material.icons.core)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.pager)
    implementation(libs.feather.icons)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.koin)
    implementation(libs.koin.annotations)

    // Paging
    implementation(libs.paging.common.ktx)
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // Navigation
    implementation(libs.navigation)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.runtime.android)
    ksp(libs.room.compiler)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.auth)
}