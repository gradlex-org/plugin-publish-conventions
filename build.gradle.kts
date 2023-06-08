plugins {
    id("java-gradle-plugin")
    id("org.gradlex.internal.plugin-publish-conventions") version "0.5"
}

group = "org.gradlex"
version = "0.5"

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

// TODO This can be removed after release 0.6
signing {
    useInMemoryPgpKeys(
            providers.environmentVariable("SIGNING_KEY").orNull,
            providers.environmentVariable("SIGNING_PASSPHRASE").orNull
    )
}

dependencies {
    implementation("com.gradle.publish:plugin-publish-plugin:1.2.0")
}
