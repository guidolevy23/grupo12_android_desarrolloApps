plugins {
    alias(libs.plugins.android.application)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.ritmofit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ritmofit"
        minSdk = 24 // ⚡ mejor 24 en lugar de 34 para compatibilidad
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

    // 👉 Java 17 + Desugaring
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    buildToolsVersion = "30.0.3"
}

dependencies {
    // AndroidX base
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)

    implementation("com.google.android.material:material:1.11.0")
    
    // SwipeRefreshLayout for pull-to-refresh functionality
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    
    // RecyclerView for lists
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // Security Crypto para EncryptedSharedPreferences
    implementation("androidx.security:security-crypto:1.1.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    annotationProcessor("com.google.dagger:hilt-android-compiler:2.48")

    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")

    // DataStore
    implementation(libs.datastore.core)

    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    // Unit Testing
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    
    // Android Testing
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.48")
    androidTestImplementation("org.mockito:mockito-android:5.7.0")
}
