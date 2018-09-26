package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;

import com.mysql.jdbc.PreparedStatement;

public class PersonService {

    Person person = new Person();
    AddressService addressService = new AddressService();

    public Connection openConnection() {

        Connection conn = null;
        try {

            String url = "jdbc:mysql://pc1620:3306/agrasha_janarthanan?useSSL=false";

            String userName = "agrasha_janarthanan";
            String password = "demo";

            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return conn;
    }

    public int createRecord(String name, String email, Date birthDate, Time createdDate, long addressId, String street, String city, long pincode) throws Exception {

        Connection conn = openConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO person (name, email, birth_date, created_date, address_id)");
        sb.append("VALUES (?, ?, ?, ?, ?)");
        int rowsAffected = 0;

        try {
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sb.toString());
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setDate(3, birthDate);
            statement.setTime(4, createdDate);
            statement.setLong(5, addressId);
            rowsAffected = statement.executeUpdate();
            addressId = addressService.createRecord(street, city, pincode);
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException("Email already exists");
        }
        return rowsAffected;
    }

    public 
}
