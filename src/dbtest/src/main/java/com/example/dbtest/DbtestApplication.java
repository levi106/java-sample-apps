package com.example.dbtest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbtestApplication {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("com.microsoft.sqlserver.jdbc");
		logger.setLevel(Level.FINE);
		SpringApplication.run(DbtestApplication.class, args);
	}

}
