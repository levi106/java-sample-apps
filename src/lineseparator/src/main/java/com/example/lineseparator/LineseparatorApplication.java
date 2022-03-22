package com.example.lineseparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LineseparatorApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(LineseparatorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LineseparatorApplication.class, args);
	}

	private String convertStringToHexString(String str) {
		StringBuilder builder = new StringBuilder();

		// char[] chars = str.toCharArray();
		// for (char c : chars) {
		//  String hex = Integer.toHexString(c);
		// 	builder.append(hex);
		// }
		byte[] bytes = str.getBytes();
		for (byte b : bytes) {
		 	builder.append(String.format("%02X", b & 0xff));
		}

		return builder.toString();
	}

	@Override
	public void run(String... args) {
		String separator1 = System.lineSeparator();
		LOG.info("{}", convertStringToHexString(separator1));
		String separator2 = System.getProperty("line.separator");
		LOG.info("{}", convertStringToHexString(separator2));
	}
}
