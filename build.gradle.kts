plugins {
    id("java-gradle-plugin")
    id("org.gradlex.internal.plugin-publish-conventions") version "0.5"
}

group = "org.gradlex"
version = "0.6"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

pluginPublishConventions {
    id("${project.group}.internal.${project.name}")
    implementationClass("org.gradlex.conventions.pluginpublish.PluginPublishConventionsPlugin")
    displayName("Plugin Publish Conventions Gradle plugin")
    description("Conventions for publishing GradleX plugins.")
    tags("gradlex", "conventions", "publish", "plugins")
    gitHub("https://github.com/gradlex-org/plugin-publish-conventions")
    developer {
        id = "britter"
        name = "Benedikt Ritter"
        email = "benedikt@gradlex.org"
    }
    developer {
        id = "jjohannes"
        name = "Jendrik Johannes"
        email = "jendrik@gradlex.org"
    }
}

dependencies {
    implementation("com.gradle.publish:plugin-publish-plugin:1.2.0")
}
