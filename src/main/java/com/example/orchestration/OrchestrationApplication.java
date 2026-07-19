package com.example.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrchestrationApplication {

	// happy flow/roll back dah tak buat -> endpointny saga?simulateError=(boolean)
	// <-- testing, tak pikirin kykny mock 3 gausah dibuat rollback handler karena
	// kalo 1 atau 2 dah error, mock 3 gabakal di exec/commit karena final step juga
	public static void main(String[] args) {
		SpringApplication.run(OrchestrationApplication.class, args);
	}

}
