package com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl;

import com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.DatalogFileNameCheckTool;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FileNameCheck implements DatalogFileNameCheckTool {
    @Value("${system.properties.datalog.error-path}")
    private String errorPath;

    @Override
    public boolean check(File in) {
        if (in.isDirectory()) {
            try {
                FileUtils.forceDelete(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            String fileName = in.getName();
            if (!fileName.startsWith("VTEST_CP")) {
                try {
                    FileUtils.copyFile(in, new File(errorPath + "/" + in.getName()));
                    FileUtils.forceDelete(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                return true;
            }
        }
    }
}
