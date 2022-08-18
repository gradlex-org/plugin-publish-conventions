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

import com.gradle.publish.PluginBundleExtension;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension;
import org.gradle.plugin.devel.PluginDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class PluginPublishConventionsExtension {

    public static final String NAME = "pluginPublishConventions";

    private final Project project;
    private final PluginDeclaration pluginDefinition;
    private final PluginBundleExtension pluginBundle;

    List<Action<MavenPomDeveloper>> developers = new ArrayList<>();

    public PluginPublishConventionsExtension(
            Project project,
            GradlePluginDevelopmentExtension gradlePlugin,
            PluginBundleExtension pluginBundle
    ) {
        this.project = project;
        this.pluginDefinition = gradlePlugin.getPlugins().create(project.getName());
        this.pluginBundle = pluginBundle;
    }


    public Provider<String> getId() {
        return project.getProviders().provider(pluginDefinition::getId);
    }

    public Provider<String> getImplementationClass() {
        return project.getProviders().provider(pluginDefinition::getImplementationClass);
    }

    public Provider<String> getDisplayName() {
        return project.getProviders().provider(pluginDefinition::getDisplayName);
    }

    public Provider<String> getDescription() {
        return project.getProviders().provider(pluginDefinition::getDescription);
    }

    public Provider<String> getGitHub() {
        return project.getProviders().provider(pluginBundle::getVcsUrl);
    }

    public Provider<String> getWebsite() {
        return project.getProviders().provider(pluginBundle::getVcsUrl);
    }

    public Provider<Collection<String>> getTags() {
        return project.getProviders().provider(pluginBundle::getTags);
    }

    public void id(String id) {
        pluginDefinition.setId(id);
    }

    public void implementationClass(String implementationClass) {
        pluginDefinition.setImplementationClass(implementationClass);
    }

    public void displayName(String displayName) {
        pluginDefinition.setDisplayName(displayName);
    }

    public void description(String description) {
        pluginDefinition.setDescription(description);
        pluginBundle.setDescription(description);
    }

    public void gitHub(String gitHub) {
        pluginBundle.setVcsUrl(gitHub);
        if (pluginBundle.getWebsite() == null || pluginBundle.getWebsite().isEmpty()) {
            pluginBundle.setWebsite(gitHub);
        }
    }

    public void website(String website) {
        pluginBundle.setWebsite(website);
    }

    public void tags(String... tags) {
        pluginBundle.setTags(Arrays.asList(tags));
    }

    public void developer(Action<MavenPomDeveloper> action) {
        developers.add(action);
    }
}
