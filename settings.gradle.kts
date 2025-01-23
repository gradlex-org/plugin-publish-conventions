rootProject.name = "plugin-publish-conventions"

plugins {
    id("com.gradle.enterprise") version "3.19.1"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2.1"
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"

        // required to bind this to a local variable for configuration cache compatibility
        val isCi = providers.environmentVariable("CI").getOrElse("false").toBoolean()
        publishing.onlyIf { isCi }
    }
}

