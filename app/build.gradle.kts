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

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }

  // Evita erros de merge de recursos META-INF (com OkHttp/Jsoup)
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "META-INF/DEPENDENCIES"
      excludes += "META-INF/LICENSE"
      excludes += "META-INF/LICENSE.txt"
      excludes += "META-INF/NOTICE"
      excludes += "META-INF/NOTICE.txt"
      excludes += "META-INF/INDEX.LIST"
    }
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  // Alinhar com compileSdk 34
  implementation("androidx.activity:activity-ktx:1.9.2")
  implementation("androidx.fragment:fragment-ktx:1.7.1")

  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("org.jsoup:jsoup:1.17.2")
}
