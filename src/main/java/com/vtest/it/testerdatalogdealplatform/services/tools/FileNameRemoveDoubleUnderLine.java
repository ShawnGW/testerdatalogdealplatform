package com.vtest.it.testerdatalogdealplatform.services.tools;

import org.springframework.stereotype.Service;

@Service
public class FileNameRemoveDoubleUnderLine {
    public String remove(String fileName){
        return  fileName.replaceAll("__","_");
    }
}
