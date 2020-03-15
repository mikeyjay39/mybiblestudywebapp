package com.mybiblestudywebapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@RefreshScope
public class MyBibleStudyWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBibleStudyWebAppApplication.class, args);
    }

}

@RestController
class MessageRestController {

    @Value("${testing}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
}
