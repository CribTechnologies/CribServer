package com.crib.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CribServerApplication {

	public static void main(String[] args) throws IOException {
		FirebaseSetup.getInstance().initialize();
		Argon2Setup.getInstance().initialize();
		JSONWebTokenSetup.getInstance().initialize();

		SpringApplication.run(CribServerApplication.class, args);
	}
}
