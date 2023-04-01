package com.swe443.group4.gis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Gis2Application {

	public static void main(String[] args) {
		SpringApplication.run(Gis2Application.class, args);
	}

}
