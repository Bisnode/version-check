package com.bisnode.versioncheck.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import com.bisnode.versioncheck.listener.VersionCheckListener;

/**
 * Checks whether a dependency exists more than once with different versions.
 */
public class DuplicateRule implements VersionRule {

    private static final Logger logger = Logging.getLogger(DuplicateRule.class);

    @Override
    public boolean apply(Configuration config, List<ModuleVersionIdentifier> allDeps, VersionCheckListener renderer) {
        Map<String, Set<String>> versionGroup = new HashMap<>();

        for (ModuleVersionIdentifier info : allDeps) {
            String id = info.getGroup() + ":" + info.getName();
            Set<String> version = versionGroup.get(id);
            if (version == null) {
                version = new HashSet<>();
                versionGroup.put(id, version);
            }
            version.add(info.getVersion());
        }

        boolean allOk = true;
        for (Entry<String, Set<String>> entry : versionGroup.entrySet()) {
            if (entry.getValue().size() > 1) {
                allOk = false;
                logger.warn("Version mismatch for '{}' - Conflicting versions: {}", entry.getKey(), entry.getValue());
            }
        }

        return allOk;
    }
}
