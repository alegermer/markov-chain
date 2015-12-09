package com.germer.markov.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

@SpringBootApplication
public class StandaloneApplication extends WebMvcAutoConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(StandaloneApplication.class, args);
	}

}
