package com.mybiblestudywebapp.bibletextservice;

import com.mybiblestudywebapp.bibletextservice.persistence.model.*;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailParseException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class BibleTextServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibleTextServiceApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner init(
            TestamentRepository testamentRepository,
            VersionRepository versionRepository,
            BookRepository bookRepository) {
        return (args) -> {

        };
    }*/


}
