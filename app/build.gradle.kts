plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}
android {
    compileSdk = 34
    namespace = "com.unamad.aulago"
    buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "com.unamad.profego"
        minSdk = 26
        versionCode = 23
        targetSdk = 34
        versionName = "beta 0.4.7"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        ksp.arg("room.schemaLocation", "$projectDir/schemas")


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "IS_PRODUCTION", "true")

        }
        debug {
            applicationIdSuffix = ".profegodev"
            versionNameSuffix = "-dev"
            buildConfigField("boolean", "IS_PRODUCTION", "false")
        }
    }

    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17

    kotlinOptions.jvmTarget = "17"
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.3"
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {

    val accompanist = "0.32.0"
    val roomVersion = "2.6.0"

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")

    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime-livedata")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose Test
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation Manager
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")

    // Life cycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-beta01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0-beta01")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")

    // Google FCM
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-directboot")

    // DAGGER
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.hilt:hilt-common:1.1.0")
    implementation("androidx.hilt:hilt-work:1.1.0")

    // KOTLIN
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")

    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist")
}

kapt.correctErrorTypes true
