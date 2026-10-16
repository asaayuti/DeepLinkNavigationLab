import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val keyStoreProperties = Properties();
val keyStorePropertiesFile = rootProject.file("keystore.properties")
val hasReleaseSigning = keyStorePropertiesFile.isFile

if (hasReleaseSigning) {
    FileInputStream(keyStorePropertiesFile).use {
        keyStoreProperties.load(it)
    }
}

if (keyStorePropertiesFile.exists()) {
    FileInputStream(keyStorePropertiesFile).use {
        keyStoreProperties.load(it)
    }
}

android {
    namespace = "com.sayuti.deeplinknavigationlab"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.sayuti.deeplinknavigationlab"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(
                    requireNotNull(keyStoreProperties.getProperty("storeFile")) {
                        "Missing storeFile in keystore.properties"
                    }
                )
                storePassword = requireNotNull(
                    keyStoreProperties.getProperty("storePassword")
                )
                keyAlias = requireNotNull(
                    keyStoreProperties.getProperty("keyAlias")
                )
                keyPassword = requireNotNull(
                    keyStoreProperties.getProperty("keyPassword")
                )
            }
        }

    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
}