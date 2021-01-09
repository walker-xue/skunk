package com.donkeycode.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author walker
 */
@EnableScheduling
@EnableAutoConfiguration
@EnableCaching
@ComponentScan
public class StarUpAdminApp {

    public static void main(String[] args) {
        SpringApplication.run(StarUpAdminApp.class, args);
    }

}
