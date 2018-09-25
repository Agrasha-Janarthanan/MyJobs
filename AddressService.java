package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class AddressService {

    Address address = new Address();

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

    public int createRecord(String street, String city, long pincode) throws Exception {

        Connection conn = openConnection();
        StringBuilder sb = new StringBuilder();

        if (pincode == 0) {
            throw new RuntimeException("Pincode cannot be null");
        }

        sb.append("INSERT INTO address (street, city, pincode)");
        sb.append("VALUES (?, ?, ?)");
        PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sb.toString());
        statement.setString(1, street);
        statement.setString(2, city);
        statement.setLong(3, pincode);
        int rowsAffected = statement.executeUpdate();
        conn.close();
        return rowsAffected;
    }

    public int updateRecord(String condition) {

        Connection conn = openConnection();
        StringBuilder sb = new StringBuilder();
        int rowsUpdated = 0;

        try {

            sb.append("UPDATE address SET city = 'Chennai' WHERE ");
            sb.append(condition);
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sb.toString());
            rowsUpdated = statement.executeUpdate();
            conn.close();
        } catch (Exception exception) {
            throw new RuntimeException("Cannot update without condition");
        }
        return rowsUpdated;
    }

    public String readRecord(long id) {

        Connection conn = openConnection();
        String resultRecord = null;

        try {

            String query = "SELECT id, street, city, pincode FROM address WHERE id = "+ id;
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            result.next();
            address.setId(result.getLong("id"));
            address.setStreet(result.getString("street"));
            address.setCity(result.getString("city"));
            address.setPincode(result.getLong("pincode"));
            resultRecord = address.getId() + "," + address.getStreet() + "," + address.getCity() + "," + address.getPincode();
            System.out.println(resultRecord);
            conn.close();
        } catch (Exception exception) {
            throw new RuntimeException("ID cannot be empty");
        }
        return resultRecord;
    }

    public List<String> readAllRecords() throws SQLException {

        Connection conn = openConnection();
        StringBuilder sb = new StringBuilder();
        String resultRecord = null;
        List<String> resultRecords = new ArrayList<>();

        try {

            sb.append("SELECT id, street, city, pincode FROM address");
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sb.toString());
            ResultSet result = statement.executeQuery();

            while (result.next()) {

                address.setId(result.getLong("id"));
                address.setStreet(result.getString("street"));
                address.setCity(result.getString("city"));
                address.setPincode(result.getLong("pincode"));
                resultRecord = address.getId() + address.getStreet() + address.getCity() + address.getPincode();
                resultRecords.add(resultRecord);
            }
            conn.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return resultRecords;
    }

    public int deleteRecord(String condition) {

        Connection conn = openConnection();
        StringBuilder sb = new StringBuilder();
        int rowsDeleted = 0;
        try {

            sb.append("DELETE FROM address WHERE ");
            sb.append(condition);
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sb.toString());
            rowsDeleted = statement.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException("Cannot delete without condition");
        }
        return rowsDeleted;
    }

    public static void main(String[] args) throws SQLException {

        AddressService service = new AddressService();
//        service.createRecord("ganesh Street", "Mumbai", 600015);
//        service.readAllRecords();
        service.readRecord(10111);
//        service.updateRecord("street = 'mint street'");
//        service.readAllRecords();
//        service.deleteRecord("city = 'Chennai'");
    }
}
