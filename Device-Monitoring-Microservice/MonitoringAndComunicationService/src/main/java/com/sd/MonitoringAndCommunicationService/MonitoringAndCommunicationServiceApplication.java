package com.sd.MonitoringAndCommunicationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoringAndCommunicationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitoringAndCommunicationServiceApplication.class, args);
	}

}
