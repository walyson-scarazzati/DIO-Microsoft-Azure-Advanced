package com.azure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication
public class AzureFunctionToDoApplication {

	public static void main(String[] args) {
		ApplicationInsights.attach();
		SpringApplication.run(AzureFunctionToDoApplication.class, args);
	}

}
