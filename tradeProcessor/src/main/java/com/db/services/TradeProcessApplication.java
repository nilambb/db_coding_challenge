package com.db.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EntityScan
@EnableScheduling
public class TradeProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeProcessApplication.class, args);
	}

}
