package com.vtest.it.testerdatalogdealplatform.services.tools;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileTimeCheck {
    public boolean Check(File file){
        long now=System.currentTimeMillis();
        long fileLastModifyTime=file.lastModified();
        if (((now-fileLastModifyTime)/1000)<90){
            return false;
        }
        return true;
    }

    public boolean CheckLong(File file) {
        long now = System.currentTimeMillis();
        long fileLastModifyTime = file.lastModified();
        if (((now - fileLastModifyTime) / 1000) < 60 * 60) {
            return false;
        }
        return true;
    }
}
