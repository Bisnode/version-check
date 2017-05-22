package com.bisnode.versioncheck.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DuplicateRule implements VersionRule {

    private static final Logger logger = Logging.getLogger(DuplicateRule.class);

    public DuplicateRule() {
    }

    @Override
    public void apply(List<ModuleVersionIdentifier> allDeps) {
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

        for (Entry<String, Set<String>> entry : versionGroup.entrySet()) {
            if (entry.getValue().size() > 1) {
                logger.warn("Version mismatch for '{}' - Conflicting versions: {}", entry.getKey(), entry.getValue());
            }
        }
    }
}
