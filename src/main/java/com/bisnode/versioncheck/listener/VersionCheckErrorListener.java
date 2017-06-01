
package com.bisnode.versioncheck.listener;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Log a rule violation.
 */
public class VersionCheckErrorListener implements VersionCheckListener {

    private static final Logger logger = Logging.getLogger(VersionCheckErrorListener.class);

    private Configuration currentConfig;
    private String currentGroup;

    @Override
    public void startConfiguration(Configuration configuration) {
        this.currentConfig = configuration;
    }

    @Override
    public void completeConfiguration(Configuration configuration) {
        this.currentConfig = null;
    }

    @Override
    public void startViolationGroup(String group) {
        this.currentGroup = group;
        logger.error("Violation for configuration: '{}', group: '{}'", currentConfig, currentGroup);
    }

    @Override
    public void completeViolationGroup() {
        this.currentGroup = null;
    }

    @Override
    public void reportViolation(String formatText, Object... parameters) {
        logger.error(String.format("  " + formatText, parameters));
    }

}
