// SPDX-License-Identifier: Apache-2.0
package org.gradlex.conventions.report;

import buildparameters.BuildParametersExtension;
import buildparameters.GeneratedBuildParametersPlugin;
import com.gradle.CommonCustomUserDataGradlePlugin;
import com.gradle.develocity.agent.gradle.DevelocityConfiguration;
import com.gradle.develocity.agent.gradle.DevelocityPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class DevelocityConventionsPlugin implements Plugin<Settings> {

    private static final String SERVER = "https://community.develocity.cloud";
    private static final String PROJECT_ID = "gradlex-org";

    @Override
    public void apply(Settings settings) {
        var plugins = settings.getPlugins();
        var extensions = settings.getExtensions();

        plugins.apply(DevelocityPlugin.class);
        plugins.apply(CommonCustomUserDataGradlePlugin.class);
        plugins.apply(GeneratedBuildParametersPlugin.class);

        var develocity = extensions.getByType(DevelocityConfiguration.class);
        var buildParameters = extensions.getByType(BuildParametersExtension.class);

        // required to bind this to a local variable for configuration cache compatibility
        var isCi = buildParameters.getCi();
        var hasAccessKey = System.getenv("DEVELOCITY_ACCESS_KEY") != null;

        develocity.getServer().set(SERVER);
        develocity.getProjectId().set(PROJECT_ID);

        develocity.buildScan(buildScan -> {
            buildScan.getUploadInBackground().set(!isCi);
            buildScan.getPublishing().onlyIf(context -> context.isAuthenticated());
            buildScan.getObfuscation().ipAddresses(addresses -> addresses.stream()
                    .map(__ -> "0.0.0.0")
                    .toList());
        });

        settings.buildCache(buildCache -> {
            buildCache.local(local -> local.setEnabled(true));
            buildCache.remote(develocity.getBuildCache(), remote -> {
                remote.setEnabled(true);
                // Check access key presence to avoid build cache errors on PR builds
                // from forks, where the access key is not available.
                remote.setPush(isCi && hasAccessKey);
            });
        });
    }
}
