package com.fairy.spareeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpareEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpareEurekaApplication.class, args);
	}

}
