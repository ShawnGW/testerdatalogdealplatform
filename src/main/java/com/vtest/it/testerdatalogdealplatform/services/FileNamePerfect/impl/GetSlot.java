package com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl;

import org.springframework.stereotype.Service;

@Service
public class GetSlot {

    public String get(String waferId){
        try {
            String tempSlot=waferId.substring(waferId.length()-2);
            tempSlot=tempSlot.startsWith("-")?tempSlot.substring(1):tempSlot;
            tempSlot=String.valueOf(Integer.valueOf(tempSlot));
            return tempSlot;
        } catch (Exception e) {
            return null;
        }
    }
}
