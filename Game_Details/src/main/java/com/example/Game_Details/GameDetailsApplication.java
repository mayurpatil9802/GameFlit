package com.example.Game_Details;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.example", "com.example.Game_Details"})
public class GameDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameDetailsApplication.class, args);
		System.out.println("Running.....");
	}

}
