import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.time.LocalDateTime

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id ("kotlin-kapt")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.hilt)
    id("android.aop")
}

android {
    namespace = "com.mvvm.demo"
    compileSdk = 34
    buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "com.mvvm.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations += listOf("zh")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }
    //app签名
    signingConfigs {
        create("release") {
            storeFile = rootProject.file("myProject.jks")
            storePassword = "123456"
            keyAlias = "myProject"
            keyPassword = "123456"
        }
    }
    buildTypes {
        debug {
            // 日志打印开关
            buildConfigField("Boolean", "LOG_ENABLE", "true")
            // 测试包下的 BuglyId
            buildConfigField("String", "BUGLY_ID", "\"buglyId\"")
            // 测试服务器的主机地址
            buildConfigField("String", "HOST_URL", "\"https://debug\"")
            applicationIdSuffix = ".debug"
            isJniDebuggable = true
            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            //清单占位符
            addManifestPlaceholders(mapOf("app_name" to "安卓技术中台Debug版"))
            ndk {
                //noinspection ChromeOsAbiSupport
                abiFilters += mutableSetOf("armeabi-v7a", "x86_64")
            }
        }
        create("preview") {
            applicationIdSuffix = ""
            initWith(getByName("debug"))
            //清单占位符
            addManifestPlaceholders(mapOf("app_name" to "安卓技术中台preview版"))
        }

        release {
            // 日志打印开关
            buildConfigField("Boolean", "LOG_ENABLE", "false")
            // 测试包下的 BuglyId
            buildConfigField("String", "BUGLY_ID", "\"id\"")
            // 测试服务器的主机地址
            buildConfigField("String", "HOST_URL", "\"https://release\"")
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            //清单占位符
            addManifestPlaceholders(mapOf("app_name" to "@string/app_name"))
            ndk {
                // armeabi：万金油架构平台（占用率：0%）
                // armeabi-v7a：曾经主流的架构平台（占用率：10%）
                // arm64-v8a：目前主流架构平台（占用率：95%）
                //noinspection ChromeOsAbiSupport
                abiFilters += mutableSetOf("armeabi-v7a", "arm64-v8a")
            }
            // Ensure Baseline Profile is fresh for release builds.
            // baselineProfile.automaticGenerationDuringBuild = true
        }
        //使用benchmark测试版本
        create("benchmark") {
            // Enable all the optimizations from release build through initWith(release).
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            // Debug key signing is available to everyone.
            signingConfig = signingConfigs.getByName("release")
            // Only use benchmark proguard rules
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = ".benchmark"
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildToolsVersion = "34.0.0"

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    applicationVariants.all {
        outputs.all {
            val ver = defaultConfig.versionName
            val date = LocalDateTime.now()
            val time: String =
                "${date.year}_${date.month}_${date.dayOfMonth}-${date.hour}-${date.minute}"
            (this as BaseVariantOutputImpl).outputFileName =
                "${rootProject.name}_${ver.orEmpty()}_${buildType.name}${time}.apk"
        }
    }
}
ksp{

}
androidAopConfig {

    // enabled 为 false 切面不再起作用，默认不写为 true
    enabled = true
    // include 不设置默认全部扫描，设置后只扫描设置的包名的代码
    include(
        android.defaultConfig.applicationId.orEmpty(),
    )
    // exclude 是扫描时排除的包
    // 可排除 kotlin 相关，提高速度
//    exclude ("kotlin.jvm", "kotlin.internal","kotlinx.coroutines.internal",
//        "kotlinx.coroutines.android")

    // verifyLeafExtends 是否开启验证叶子继承，默认打开，如果没有设置 @AndroidAopMatchClassMethod 的 type = MatchType.LEAF_EXTENDS，可以关闭
    verifyLeafExtends = true
    //默认关闭，开启在 Build 或 打包后 将会生成切点信息json文件在 app/build/tmp/cutInfo.json
    cutInfoJson = false
    //默认开启，设置 false 后会没有增量编译效果 筛选（关键字： AndroidAOP woven info code） build 输出日志可看时间
    increment = true
//修改、增加、删除匹配切面的话，就会走全量编译
}
dependencies {
    implementation(fileTree("libs").include("*.jar", "*.aar"))
    implementation(project(":base"))
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.extensions)
    //fragment
   // implementation (libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //hilt代替dagger
    implementation(libs.hilt.android)
    implementation(libs.legacy.support.v4)
    ksp(libs.hilt.compiler)
    implementation(libs.glide)
    //联网
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    //aop插件
    implementation(libs.aspectj.core)
    annotationProcessor(libs.aspectj.annotation)
    //非必须项 👇，如果你想自定义切面需要用到，⚠️支持Java和Kotlin代码写的切面
    ksp(libs.aspectj.ksp)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
