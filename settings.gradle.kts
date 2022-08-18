plugins {
    id("com.gradle.enterprise") version "3.11.1"
}

rootProject.name = "plugin-publish-conventions"

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

gradleEnterprise {
    val runsOnCI = providers.environmentVariable("CI").getOrElse("false").toBoolean()
    if (runsOnCI) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}
