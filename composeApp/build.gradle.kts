import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)

    // Room
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

room {
    schemaDirectory("$projectDir/schemas")
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

        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            // Ktor dependencies
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)

            // Koin
            implementation("io.insert-koin:koin-android")

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
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)

            // Ktor dependencies
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation("io.ktor:ktor-client-logging:3.4.1")

            // Koin
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.2.0"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-compose")
            implementation("io.insert-koin:koin-compose-viewmodel")

            // Coil CMP
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.serialization.json)

            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // commonMain
            implementation(libs.filekit.dialogs.compose)
        }

        iosMain.dependencies {
            // Ktor dependencies
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)

            // Koin
            implementation("io.insert-koin:koin-test")
        }
    }
}

android {
    namespace = "com.example.aigenerator"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.aigenerator"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    
    // Room KSP
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}
