package com.example.GPU_DATA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class GpuDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpuDataApplication.class, args);
		System.out.println("Running.........");
	}

}
