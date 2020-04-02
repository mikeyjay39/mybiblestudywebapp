package com.mybiblestudywebapp.persistenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@EnableCaching
@RefreshScope
public class PersistenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersistenceServiceApplication.class, args);
    }

}
