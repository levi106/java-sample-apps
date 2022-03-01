package com.example.kvtest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.qos.logback.classic.Level;

@SpringBootApplication
public class KvtestApplication {

	public static void main(String[] args) {
		//com.azure.spring.keyvault.KeyVaultEnvironmentPostProcessorHelper
		//ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("com.azure.spring.keyvault.KeyVaultEnvironmentPostProcessorHelper");
		root.setLevel(Level.TRACE);
		Logger log = LoggerFactory.getLogger(KvtestApplication.class);
		log.debug("abc " + log.getClass());
		SpringApplication.run(KvtestApplication.class, args);
	}
}
