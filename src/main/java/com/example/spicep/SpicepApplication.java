package com.example.spicep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("com.example.spicep.repository")
@EntityScan("com.example.spicep.entity")
@EnableScheduling
public class SpicepApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpicepApplication.class, args);
	}

}
