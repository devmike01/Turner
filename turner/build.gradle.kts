plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id ( "maven-publish")

}


android {
    namespace = "dev.gbenga.turner"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

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

    publishing {
        singleVariant("release")
       // withSourcesJar()
    }

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release"){
                from(components["release"])
            }
        }
    }
}



dependencies {
    implementation(libs.androidx.animation.graphics.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.foundation.android)
    implementation (libs.androidx.runtime)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.foundation.layout.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // https://mvnrepository.com/artifact/io.mockk/mockk-android
    testImplementation(libs.mockk.test)
}

