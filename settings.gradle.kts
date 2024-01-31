pluginManagement {
    includeBuild("buildLibraryConfig")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 友盟远程仓库：https://info.umeng.com/detail?id=443&cateId=1
        maven("https://repo1.maven.org/maven2")
        maven("https://jitpack.io")
    }
}

rootProject.name = "MyProject"
include(":app")
include(":base")
