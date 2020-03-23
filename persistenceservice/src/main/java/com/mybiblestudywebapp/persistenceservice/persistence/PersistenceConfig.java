package com.mybiblestudywebapp.persistenceservice.persistence;

import feign.jackson.JacksonDecoder;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/23/20
 */
@Configuration
public class PersistenceConfig {
    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }
}
