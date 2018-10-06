package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddressService {

    public int create(Address address, Connection conn) throws Exception {

        StringBuilder sb = new StringBuilder();
        int id = 0;

        if(address.street == null || address.street == "") {
            throw new RuntimeException("Street cannot be null or empty");
        }

        if(address.city == null || address.city == "") {
            throw new RuntimeException("City cannot be null or empty");
        }

        if(address.getPostalCode() == 0) {
            throw new RuntimeException("PostalCode cannot be null or empty");
        }

        if(conn == null) {
            throw new RuntimeException("Service not available");
        }

        sb.append("INSERT INTO address (street, city, postal_code)");
        sb.append("VALUES (?, ?, ?)");

        PreparedStatement statement = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, address.street);
        statement.setString(2, address.city);
        statement.setInt   (3, address.postalCode);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if((rs != null) && (rs.next())) {
            id = rs.getInt(1);
        }
        return id;
    }
}
