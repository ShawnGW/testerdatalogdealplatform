package com.vtest.it.testerdatalogdealplatform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class TesterdatalogdealplatformApplication {
    private static final Log logger = LogFactory.getLog(TesterdatalogdealplatformApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TesterdatalogdealplatformApplication.class, args);
    }

}
