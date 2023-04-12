package com.swe443.group4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


// I don't think @EnableDiscoveryClient is necessary
// because it workd without it, seems like it's 
// set to true by default, but still adding it to be sure.
@SpringBootApplication
@EnableDiscoveryClient
public class GroceryhandlingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryhandlingsystemApplication.class, args);
	}

}
