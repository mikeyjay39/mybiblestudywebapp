package com.mybiblestudywebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyBibleStudyWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBibleStudyWebAppApplication.class, args);
	}

}
