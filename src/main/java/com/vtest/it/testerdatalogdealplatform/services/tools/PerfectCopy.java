package com.vtest.it.testerdatalogdealplatform.services.tools;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PerfectCopy {
    public void copy(File source,File target){
        try {
            FileUtils.copyFile(source,target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!DiffUtil.check(source,target)){
            copy(source,target);
        }
    }
}
