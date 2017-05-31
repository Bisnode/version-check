package com.bisnode.versioncheck.rules;

import java.util.List;
import java.util.regex.Pattern;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import com.bisnode.versioncheck.ValidationReportRenderer;

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
        // check if a string with the specified pattern
        this.matchPattern = Pattern.compile(Pattern.quote(inputPattern) + ".*");
    }

    @Override
    public void apply(Configuration config, List<ModuleVersionIdentifier> allDeps, ValidationReportRenderer renderer) {
        String version = null;
        String firstName = null;
        boolean writeFirstHit = true;

        for (ModuleVersionIdentifier info : allDeps) {
            String id = info.getGroup() + ":" + info.getName();
            if (matchPattern.matcher(id).matches()) {
                if (version == null) {
                    version = info.getVersion();
                    firstName = info.getName();
                } else {
                    if (!version.equals(info.getVersion())) {
                        if (writeFirstHit) {
                            renderer.startViolationGroup("Version group mismatch for '" + inputPattern + "'");
                            renderer.reportViolation("%s for %s", version, firstName);
                            writeFirstHit = false;
                        }
                        renderer.reportViolation("%s for %s", info.getVersion(), info.getName());
                    }
                }
            }
        }

        renderer.completeViolationGroup();
    }

    @Override
    public String toString() {
        return "SameVersionGroupRule [inputPattern=" + inputPattern + "]";
    }

//    private static String createRegexFromGlob(String glob) {
//        String out = "";
//        for (int i = 0; i < glob.length(); ++i) {
//            final char c = glob.charAt(i);
//            switch (c) {
//                case '*': out += ".*?"; break;
//                case '?': out += '.'; break;
//                case '.': out += "\\."; break;
//                case '\\': out += "\\\\"; break;
//                default: out += c;
//            }
//        }
//        return out;
//    }


}
