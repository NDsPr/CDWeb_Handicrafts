package com.handicrafts.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OpenConnectionUtil {
    static ResourceBundle bundle = ResourceBundle.getBundle("db");

    public static Connection openConnection() {
        try {
            Class.forName(bundle.getString("driverName"));
            String url = bundle.getString("url");
            String user = bundle.getString("user");
            String password = bundle.getString("password");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }
}
