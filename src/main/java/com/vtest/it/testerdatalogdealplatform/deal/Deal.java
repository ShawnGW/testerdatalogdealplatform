package com.vtest.it.testerdatalogdealplatform.deal;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class Deal {
    public void datalogDeal(Set<String> pathNeedDeal){
        for (String path : pathNeedDeal) {
            System.out.println(path);
        }
    }
}
