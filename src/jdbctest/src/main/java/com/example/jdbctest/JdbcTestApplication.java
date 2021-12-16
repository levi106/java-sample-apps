package com.example.jdbctest;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

@SpringBootApplication
public class JdbcTestApplication implements CommandLineRunner {
	private static final Logger appLogger = Logger.getLogger("com.example.jdbctest");
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("com.microsoft.sqlserver.jdbc");
		logger.setLevel(Level.FINE);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		appLogger.setLevel(Level.INFO);
		SpringApplication.run(JdbcTestApplication.class, args);
	}

	private void connectDb(String connectionString) {
		try (Connection con = DriverManager.getConnection(connectionString)) {
			Statement stmt = con.createStatement();
			stmt.execute("SELECT 1");
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void connectDb(String user, String password, String serverName, int portNumber, String databaseName) {
		SQLServerDataSource ds = new SQLServerDataSource();

		ds.setUser(user);
		ds.setPassword(password);
		ds.setServerName(serverName);
		ds.setPortNumber(portNumber);
		ds.setDatabaseName(databaseName);

		try (Connection con = ds.getConnection()) {
			Statement stmt = con.createStatement();
			stmt.execute("SELECT 1");
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(String... args) {
		appLogger.info("Start");

		String user = env.getProperty("SERVER_USERNAME");
		String password = env.getProperty("SERVER_PASSWORD");
		String serverName = env.getProperty("SERVER_NAME");
		int portNumber = env.getProperty("PORT", Integer.class, 1433);
		String databaseName = env.getProperty("DATABASE_NAME");
		String connectionString = env.getProperty("CONNECTION_STRING");
		int interval = env.getProperty("INTERVAL", Integer.class, 1000);

		Timer timer = Metrics.timer("my.timer");
		try {
			do {
				timer.record(() -> {
					connectDb(connectionString);
					// connectDb(user, password, serverName, portNumber, databaseName);
				});
				Thread.sleep(interval);
			} while (true);
		} catch (InterruptedException ex) {
			appLogger.info("exit");
		}
	}
}
