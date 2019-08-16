package com.vtest.it.testerdatalogdealplatform.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StartDeal {
    @Autowired
    private Deal deal;
    @Scheduled(fixedDelay = 10000)
    public void deal() {
        deal.datalogDeal(null);
    }
}
