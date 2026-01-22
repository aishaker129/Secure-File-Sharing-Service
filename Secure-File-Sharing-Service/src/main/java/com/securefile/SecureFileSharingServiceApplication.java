package com.securefile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecureFileSharingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureFileSharingServiceApplication.class, args);
	}

}
