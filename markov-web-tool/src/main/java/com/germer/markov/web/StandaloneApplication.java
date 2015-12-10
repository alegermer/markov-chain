package com.germer.markov.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

/**
 * Default Spring:boot standalone application ready to serve static content
 * through default WebMvc Servlet (files present in main/resources/static).
 * 
 * @author <a href="mailto:alegermer@gmail.com">Alessandro Germer</a>
 */
@SpringBootApplication
public class StandaloneApplication extends WebMvcAutoConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(StandaloneApplication.class, args);
	}

}
