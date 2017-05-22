package com.bisnode.versioncheck.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import com.bisnode.versioncheck.ValidationTask;

public class EqualsRule implements VersionRule {

    private static final Logger logger = Logging.getLogger(EqualsRule.class);

    private String group;

    public EqualsRule(String group) {
        this.group = group;
    }

    @Override
    public void apply(List<ModuleVersionIdentifier> allDeps) {
        String version = null;
        for (ModuleVersionIdentifier info : allDeps) {
            if (group.equals(info.getGroup())) {
                if (version == null) {
                    version = info.getVersion();
                } else {
                    if (!version.equals(info.getVersion())) {
                        logger.error("Version mismatch for {}:{} vs. {}", info.getModule(), version, info.getVersion());
                    }
                }
            }
        }
    }
}
