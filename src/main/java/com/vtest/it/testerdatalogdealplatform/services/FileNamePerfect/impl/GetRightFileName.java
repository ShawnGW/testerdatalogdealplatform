package com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl;

import com.vtest.it.testerdatalogdealplatform.services.tools.DatalogRegexSamples;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GetRightFileName {
    private Pattern pattern = Pattern.compile(DatalogRegexSamples.MODIFYREGEX);

    public String getRightFileName(String fileName, String waferId) {
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            String prefix = matcher.group(1);
            String suffix = matcher.group(3);
            return prefix + "_" + waferId + "_" + suffix;
        }
        return null;
    }
}
