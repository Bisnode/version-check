package com.bisnode.versioncheck.rules;

import java.util.List;
import java.util.regex.Pattern;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Checks that a group of modules have the same version
 */
public class SameVersionGroupRule implements VersionRule {

    private static final Logger logger = Logging.getLogger(SameVersionGroupRule.class);

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
    public void apply(Configuration config, List<ModuleVersionIdentifier> allDeps) {
        String version = null;
        String firstHit = null;
        boolean writeFirstHit = true;

        for (ModuleVersionIdentifier info : allDeps) {
            String id = info.getGroup() + ":" + info.getName();
            if (matchPattern.matcher(id).matches()) {
                if (version == null) {
                    version = info.getVersion();
                    firstHit = id;
                } else {
                    if (!version.equals(info.getVersion())) {
                        if (writeFirstHit) {
                            logger.warn("Version group mismatch for '{}'", inputPattern);
                            logger.warn("  |- '{}:{}'", firstHit, version);
                            writeFirstHit = false;
                        }
                        logger.warn("  |- '{}:{}'", id, info.getVersion());
                    }
                }
            }
        }
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
