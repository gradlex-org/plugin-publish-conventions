/*
 * Copyright 2022 the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.conventions.pluginpublish;

import com.gradle.publish.PublishPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;

@SuppressWarnings("unused")
public abstract class PluginPublishConventionsPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        PluginContainer plugins = project.getPlugins();
        ExtensionContainer extensions = project.getExtensions();
        ProviderFactory providers = project.getProviders();
        
        plugins.apply(CheckstylePlugin.class);
        plugins.apply(PublishPlugin.class);
        plugins.apply(SigningPlugin.class);

        GradlePluginDevelopmentExtension gradlePlugin = extensions.getByType(GradlePluginDevelopmentExtension.class);
        PublishingExtension publishing = extensions.getByType(PublishingExtension.class);
        CheckstyleExtension checkstyle = extensions.getByType(CheckstyleExtension.class);
        SigningExtension signing = extensions.getByType(SigningExtension.class);

        signing.useInMemoryPgpKeys(
                providers.environmentVariable("SIGNING_KEY").getOrNull(),
                providers.environmentVariable("SIGNING_PASSPHRASE").getOrNull()
        );

        PluginPublishConventionsExtension pluginPublishConventions = extensions.create(
                PluginPublishConventionsExtension.NAME, PluginPublishConventionsExtension.class,
                project, gradlePlugin);

        publishing.getPublications().withType(MavenPublication.class).configureEach(p -> {
            p.getPom().getName().set(pluginPublishConventions.getDisplayName());
            p.getPom().getDescription().set(pluginPublishConventions.getDescription());
            p.getPom().getUrl().set(pluginPublishConventions.getWebsite());
            p.getPom().licenses(licenses -> {
                licenses.license(l -> {
                    l.getName().set("Apache-2.0");
                    l.getUrl().set("http://www.apache.org/licenses/LICENSE-2.0.txt");
                });
            });
            p.getPom().scm(scm -> scm.getUrl().set(pluginPublishConventions.getGitHub()));
            p.getPom().developers(d -> pluginPublishConventions.developers.forEach(d::developer));
        });

        checkstyle.getConfigDirectory().set(project.getLayout().getProjectDirectory().dir("gradle/checkstyle"));
        project.getDependencies().getConstraints()
            .add("checkstyle", "com.google.guava:guava:33.4.8-jre", c -> c.because("CVE-2023-2976, CVE-2020-8908"));
        project.getDependencies().getConstraints()
            .add("checkstyle", "commons-beanutils:commons-beanutils:1.11.0", c -> c.because("CVE-2025-48734"));
    }
}
