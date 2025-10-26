plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
  namespace = "com.divaneural.argus"
  compileSdk = 34
  defaultConfig {
    applicationId = "com.divaneural.argus"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0"
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
    debug { isDebuggable = true }
  }
  compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("org.jsoup:jsoup:1.17.2")
}
