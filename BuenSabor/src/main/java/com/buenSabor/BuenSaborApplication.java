package com.buenSabor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BuenSaborApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuenSaborApplication.class, args);
	}

}
