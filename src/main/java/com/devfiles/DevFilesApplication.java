package com.devfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevFilesApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevFilesApplication.class, args);
	}
}
