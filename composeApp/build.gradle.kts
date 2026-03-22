import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
            implementation("io.insert-koin:koin-android:4.1.1")
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // navigation
            implementation(libs.navigation3.ui)

            // Coil set up + Coil
            implementation(libs.bundles.ktor.common)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.serialization.json)

            // Ktor Coroutine
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
//            implementation("io.ktor:ktor-server-auth:3.4.1")

            // Koin
            implementation("io.insert-koin:koin-compose:4.2.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.2.0")
            implementation(libs.koin.annotations)

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            // viewmodel
            api(libs.androidx.lifecycle.viewmodel)

            // icons
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.minhdk.githubkmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.minhdk.githubkmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"


        // build api key
        val localProps = rootProject.file("local.properties")
        val props = Properties()
        if (localProps.exists()) {
            props.load(localProps.inputStream())
        }
        val githubApiKey = props.getProperty("GITHUB_API_TOKEN") ?: ""
        buildConfigField("String", "GITHUB_API_TOKEN", "\"$githubApiKey\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}
