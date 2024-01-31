plugins {
    `kotlin-dsl`
}
group="project.android.base.buildsrc"
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
dependencies {
    compileOnly("com.android.tools.build:gradle:8.1.3")
    compileOnly("com.android.tools:common:31.1.3")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.10-1.0.13")

}
gradlePlugin{
    plugins{
        register("androidLibraryConfig"){
            id="project.android.library"
            implementationClass="AndroidLibraryConfigPlugin"
        }
    }
}
repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
}