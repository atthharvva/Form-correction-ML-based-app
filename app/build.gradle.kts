plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.accurepp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.accurepp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }

    sourceSets {
        named("main") {
            assets.srcDirs("src/main/assets")
        }
    }

}

dependencies {

//    implementation(libs.tensorflow.lite.task.vision.v042)

    implementation(libs.tensorflow.lite.task.vision.v044) {
        exclude(group = "org.tensorflow", module = "tensorflow-lite-support")
    }

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(project(":openCV"))
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu){
        exclude(group = "org.tensorflow", module = "tensorflow-lite-support")
    }
    implementation(libs.pose.detection.common) {
        exclude(group = "org.tensorflow", module = "tensorflow-lite-support")
    }
    implementation(libs.camera.core.v130)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.camera.view)
    implementation(libs.camera.lifecycle)
    implementation(libs.firebase.crashlytics.buildtools) {
        exclude(group = "com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent", module = "listenablefuture")
    }
    implementation(libs.tensorflow.lite.support.v044)
    testImplementation(libs.junit)
    implementation(libs.guava)
    implementation(libs.camera.core)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.material.v190)




}
