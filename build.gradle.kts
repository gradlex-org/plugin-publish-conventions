plugins {
    id("java-gradle-plugin")
    id("org.gradlex.internal.plugin-publish-conventions") version "0.4"
}

group = "org.gradlex"
version = "0.5"

pluginPublishConventions {
    id("${project.group}.internal.${project.name}")
    implementationClass("org.gradlex.conventions.pluginpublish.PluginPublishConventionsPlugin")
    displayName("Plugin Publish Conventions Gradle plugin")
    description("Conventions for publishing GradleX plugins.")
    tags("gradlex", "conventions", "publish", "plugins")
    gitHub("https://github.com/gradlex-org/plugin-publish-conventions")
    developer {
        id.set("britter")
        name.set("Benedikt Ritter")
        email.set("benedikt@gradlex.org")
    }
    developer {
        id.set("jjohannes")
        name.set("Jendrik Johannes")
        email.set("jendrik@gradlex.org")
    }
}

dependencies {
    implementation("com.gradle.publish:plugin-publish-plugin:1.1.0")
}
