package com.mybiblestudywebapp.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/23/20
 */
@Configuration
public class PersistenceClientConfig {

    /*@Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }*/

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

}
