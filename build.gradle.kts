buildscript {
    repositories {
        google()
        // 다른 저장소...
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.4.1") // Google Services Plugin 버전은 업데이트에 따라 다를 수 있습니다.
        // 다른 종속성...
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}