package com.mybiblestudywebapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
@EnableCircuitBreaker
@EnableCaching
@RefreshScope
@EnableBinding(Sink.class)
@Slf4j
public class MyBibleStudyWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBibleStudyWebAppApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void subMsg(String msg) {
        log.debug("Received message: {}", msg);
    }
}

// TODO remove this after testing Spring Cloud Config
@RestController
class MessageRestController {

    @Value("${testing}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }


}
