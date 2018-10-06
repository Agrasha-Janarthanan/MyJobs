package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PersonService {

    Person person = new Person();
    Address address = new Address();
    AddressService addressService = new AddressService();
    public long create(Person person, Address address, Connection conn) throws Exception {

        long id = 0;
        String selectQuery = "SELECT email FROM person";

        PreparedStatement stmt = conn.prepareStatement(selectQuery);
        ResultSet rs = stmt.executeQuery(selectQuery);

        String email = null;
        ArrayList<String> mails = new ArrayList<>();

        for(int index = 0; rs.isLast(); index++) {
            email = rs.getString(index);
            mails.add(email);
        }

        for(String mail : mails) {
            if(person.email == mail) {
                throw new RuntimeException("Email already exists");
            }
        }

        if(person.name == null || person.name == "") {
            throw new RuntimeException("person name cannot be null or empty");
        }

        if(person.getBirthDate() == null) {
            throw new RuntimeException("Birth date cannot be null");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO person (name, email, birth_date, address_id)");
        stringBuilder.append("VALUES ?, ?, ?, ?");

        PreparedStatement statement = conn.prepareStatement(stringBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, person.name);
        statement.setString(2, person.email);
        statement.setDate(3, person.birthDate);
        long addressId = addressService.create(address, conn);
        statement.setLong(4, addressId);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();

        if((result != null) && (result.next())) {
            id = result.getInt(1);
        }
        return id;
    }
}
