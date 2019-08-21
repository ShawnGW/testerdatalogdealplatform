package com.vtest.it.testerdatalogdealplatform.deal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StartDeal {
    @Autowired
    private Deal deal;
    private static final Logger logger = LoggerFactory.getLogger(StartDeal.class);
    @Scheduled(fixedDelay = 10000)
    public void deal() {
        logger.error("start deal");
        deal.datalogDeal(null);
    }
}
