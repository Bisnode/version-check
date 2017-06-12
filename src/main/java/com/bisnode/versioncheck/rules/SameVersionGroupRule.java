package com.bisnode.versioncheck.rules;

import java.util.List;
import java.util.regex.Pattern;

import org.gradle.api.artifacts.ModuleVersionIdentifier;

import com.bisnode.versioncheck.listener.VersionCheckListener;

/**
 * Checks that a group of modules have the same version
 */
public class SameVersionGroupRule implements VersionRule {

    private Pattern matchPattern;
    private String inputPattern;

    /**
     * @param id the prefix of the group
     */
    public SameVersionGroupRule(String id) {
        this.inputPattern = id;
        // check if a string matches with the specified pattern exactly
        this.matchPattern = Pattern.compile(createRegexFromGlob(inputPattern));
    }

    @Override
    public boolean apply(List<ModuleVersionIdentifier> allDeps, VersionCheckListener renderer) {
        String version = null;
        String firstName = null;
        boolean allOk = true;

        for (ModuleVersionIdentifier info : allDeps) {
            if (matchPattern.matcher(info.getGroup()).matches()) {
                if (version == null) {
                    version = info.getVersion();
                    firstName = info.getName();
                } else {
                    if (!version.equals(info.getVersion())) {
                        if (allOk) {
                            renderer.startViolationGroup(inputPattern);
                            renderer.reportViolation("Version %s for %s", version, firstName);
                            allOk = false;
                        }
                        renderer.reportViolation("Version %s for %s", info.getVersion(), info.getName());
                    }
                }
            }
        }

        if (!allOk) {
            renderer.completeViolationGroup();
        }

        return allOk;
    }

    @Override
    public String toString() {
        return "SameVersionGroupRule [inputPattern=" + inputPattern + "]";
    }

    static String createRegexFromGlob(String glob) {
        String out = "";
        for (int i = 0; i < glob.length(); i++) {
            final char c = glob.charAt(i);
            switch (c) {
            case '*':
                out += ".*?";
                break;
            case '?':
                out += '.';
                break;
            case '.':
                out += "\\.";
                break;
            case '\\':
                out += "\\\\";
                break;
            default:
                out += c;
            }
        }
        return out;
    }
}
