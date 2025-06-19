package com.handicrafts.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class OpenConnectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(OpenConnectionUtil.class);
    private final Environment environment;

    @Autowired
    public OpenConnectionUtil(Environment environment) {
        this.environment = environment;
    }

    public Connection openConnection() {
        try {
            String driverName = environment.getProperty("spring.datasource.driver-class-name");
            String url = environment.getProperty("spring.datasource.url");
            String user = environment.getProperty("spring.datasource.username");
            String password = environment.getProperty("spring.datasource.password");

            logger.info("Connecting to database: {}", url);

            Class.forName(driverName);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found", e);
            return null;
        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            return null;
        }
    }
}
