import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val keystoreProperties = Properties().apply {
    val keystoreEnv = System.getenv("KEYSTORE")?.byteInputStream()
    val keystoreFile by lazy {
        val path = if (project.hasProperty("test")) "sample.keystore" else ".keystore"
        rootProject.file(path).inputStream()
    }
    load(keystoreEnv ?: keystoreFile)
}

android {
    namespace = "com.jx.workflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jx.workflow"
        minSdk = 24
        targetSdk = 35
        versionCode = generateVersionCode()
        versionName = libs.versions.app.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TEST_VARIABLE", keystoreProperties["testVariable"] as String)
    }
    signingConfigs {
        create("release") {
            storeFile = file("../release.jks")
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storePassword = keystoreProperties["storePassword"] as String
        }
        getByName("debug") {
            storeFile = file("../debug.jks")
            keyAlias = "debug"
            keyPassword = "debugkey"
            storePassword = "debugkey"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            versionNameSuffix = "-debug"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    flavorDimensions.add("environment")
    productFlavors {
        create("production") {
            resValue("string", "app_name", "Workflow")
        }
        create("staging") {
            resValue("string", "app_name", "Workflow STG")
            applicationIdSuffix = ".stg"
            versionNameSuffix = "-stg"
        }
    }
    applicationVariants.all {
        val suffix = System.getenv("VERSION_SUFFIX")?.let { "-$it" } ?: return@all
        outputs.forEach { output ->
            val newVersionCode = versionName + suffix
            (output as ApkVariantOutputImpl).versionNameOverride = newVersionCode
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
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
}

tasks.register("printVersion") {
    group = "versioning"
    description = "Prints the dynamically generated versionCode and versionName to the terminal"
    val versionCode = android.defaultConfig.versionCode
    val versionName = android.defaultConfig.versionName
    doLast {
        println("VERSION_CODE=$versionCode")
        println("VERSION_NAME=$versionName")
    }
}

private fun generateVersionCode(): Int {
    val name = libs.versions.app.version.name.get()
    val build = System.getenv("VERSION_BUILD")?.toIntOrNull() ?: 0
    val (major, minor, patch) = name.split(".").map(String::toInt)
    return major * 10_000_000 + minor * 10_000 + patch * 100 + build
}
