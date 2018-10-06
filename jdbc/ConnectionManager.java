package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    Connection conn = null;
    public Connection openConnection(String connectionString) {

        try {

            conn = DriverManager.getConnection(connectionString);
            conn.setAutoCommit(false);
        } catch(Exception exception) {
            throw new RuntimeException("Invalid connection string");
        }
        return conn;
    }

    public void releaseConnection() {

        try {

            if(conn != null) {
                conn.close();
            }
        } catch(Exception exception) {
            throw new RuntimeException("Connection is not opened");
        }
    }
}
