package com.cognizant.jfcogni.trainingproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
public class TrainingProjectApplication {


	public static void main(String[] args) {
		SpringApplication.run(TrainingProjectApplication.class, args);
	}

	@Bean
	public HttpClient httpClientBean(){
		return HttpClient.newHttpClient();
	}
}


