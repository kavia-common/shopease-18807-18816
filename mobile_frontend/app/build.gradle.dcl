androidApplication {
    namespace = "com.shopease.app"

    // Note: Declarative Gradle DSL (experimental) does not support nested android {} here.
    // ViewBinding is enabled by default in this sample plugin setup. If needed, configure
    // in defaults at settings.gradle.dcl.

    dependencies {
        // AndroidX core and appcompat
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.startup:startup-runtime:1.1.1")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
        implementation("androidx.activity:activity-ktx:1.9.2")
        implementation("androidx.fragment:fragment-ktx:1.8.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
        implementation("androidx.datastore:datastore-preferences:1.1.1")

        // Networking (Retrofit + OkHttp + Moshi)
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
        implementation("com.squareup.moshi:moshi:1.15.1")
        implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

        // Image loading
        implementation("io.coil-kt:coil:2.6.0")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    }
}
