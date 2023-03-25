package com.musala.dronedelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DroneDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroneDeliveryApplication.class, args);
	}

}
