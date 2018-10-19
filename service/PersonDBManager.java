package com.objectfrontier.training.service;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import sun.util.logging.resources.logging;

public class PersonDBManager implements PersonDAO {

    private ConnectionManager connectionManager;

    public PersonDBManager(ConnectionManager connectionManager) {
        super();
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Person> readAll() throws AppException {

        ArrayList<Person> persons = new ArrayList<>();
        try {
            String fetchALLQuery = MessageFormat.format("{0}{1}{2} {3}", "SELECT person.id, person.name, person.email,person.address_id",
                                            "address.street, address.id, address.city, address.postal_code,",
                                            "person.birth_date, person.created_date ",
                                            "FROM person LEFT JOIN address ON person.address_id = address.id;");
            try (PreparedStatement statement = connectionManager.createStatement(fetchALLQuery)) {
                try (ResultSet resultSet = connectionManager.executeQuery(statement)) {
                    while (resultSet.next()) {
                        Person person = new Person();
                        person.setId(resultSet.getLong(1));
                        person.setName(resultSet.getString(2), resultSet.getString(3));
                        person.setEmail(resultSet.getString(3));
                        Address address = new Address(resultSet.getString(4), resultSet.getString(6),
                                                      resultSet.getLong(7));
                        address.setId(resultSet.getLong(5));
                        person.setAddress(address);
                        person.setBirthDate(resultSet.getDate(8).toLocalDate());
                        person.setCreatedDate(resultSet.getTimestamp(9).toLocalDateTime());
                        persons.add(person);
                    }
                    return persons;
                }
            }
        } catch (SQLException e) {
            throw new AppException("unable to read person record", e);
        }
    }

    @Override
    public Person read(int id) throws AppException {
        try {
            String fetchOneQuery = MessageFormat.format("{0}{1}{2} {3}", "SELECT person.name, person.email,",
                                            "address.street, address.id, address.city, address.postal_code,",
                                            "person.birth_date, person.created_date ",
                                            "FROM address,person WHERE address.id = person.address_id AND person.id = ?;");
            try (PreparedStatement statement = connectionManager.createStatement(fetchOneQuery)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = connectionManager.executeQuery(statement)) {
                    Person person = new Person();
                    while (resultSet.next()) {
                        person.setId(id);
                        person.setName(resultSet.getString(1));
                        person.setEmail(resultSet.getString(2));
                        Address address = new Address(resultSet.getString(3), resultSet.getString(5),
                                                      resultSet.getLong(6));
                        address.setId(resultSet.getLong(4));
                        person.setAddress(address);
                        person.setBirthDate(resultSet.getDate(7).toLocalDate());
                        person.setCreatedDate(resultSet.getTimestamp(8).toLocalDateTime());
                    }
                    return person;
                }
            }
        } catch (SQLException e) {
            throw new AppException("unable to read person record", e);
        }
    }

    @Override
    public int update(Person person) throws AppException {
        long id = person.getId();
        if (person.getAddress() != null) {
            if ((getIdIFPersonHasAddress(id)) != 0) {
                return updateRecord(id, person, true, false);
            } else {
                return updateRecord(id, person, true, true);
            }
        } else {
            return updateRecord(id, person, false, false);
        }
    }

    @Override
    public int delete(int id) throws AppException {
          String deleteQuery ="DELETE FROM person WHERE id = ?;";
          try (PreparedStatement statement = connectionManager.createStatement(deleteQuery)) {
              statement.setLong(1, id);
              return connectionManager.executeUpdate(statement);
          } catch (SQLException e) {
          throw new AppException("unable to delete record", e);
      }
    }

    @Override
    public long insert(Person person) throws AppException {
        Address address = person.getAddress();
        if (address != null) {
            createAddress(address);
        }
        return createPersonRecord(person);
    }

    private long createPersonRecord(Person person) throws AppException {
        String personInsertquery = "INSERT INTO person(name, email, address_id, birth_date) VALUES(?, ?, ?, ?);";
        try (PreparedStatement statement = connectionManager.createStatement(personInsertquery,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(NAME, person.getName());

            statement.setString(EMAIL, person.getEmail());

            if (person.getAddress() != null) {
                statement.setLong(ADDRESS, person.getAddress().getId());
            } else {
                statement.setNull(ADDRESS, Types.BIGINT);
            }

            statement.setDate(BIRTH_DATE, java.sql.Date.valueOf(person.getBirthDate()));
            connectionManager.executeUpdate(statement);
            long personId = 0;

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    personId = resultSet.getInt(1);
                }
            }
            System.out.println(personId);
            return personId;
        } catch (SQLException e) {
            throw new AppException("Unable to create person Record", e);
        }
    }

    public <v> boolean isPresent(String field, v value) throws AppException {
        String getIdQuery = MessageFormat.format("SELECT {0} FROM person", field);
        try (PreparedStatement statement = connectionManager.createStatement(getIdQuery)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (value.equals((v)resultSet.getObject(1))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new AppException("Failed to check email exsistance", e);
        }
    }

    private long createAddress(Address address) throws AppException {
        long addressId = -1;
        if ((addressId = checkAddressExsistance(address)) != -1) {
            address.setId(addressId);
            return addressId;
        } else {
            String addressInsertQuery = "INSERT INTO address(street, city, postal_code) VALUES(?, ?, ?);";
            try (PreparedStatement statement = connectionManager.createStatement(addressInsertQuery,
                    Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(STREET, address.getStreet());
                statement.setString(CITY, address.getCity());
                statement.setLong(POSTAL_CODE, address.getPostalCode());
                connectionManager.executeUpdate(statement);

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    addressId = resultSet.getInt(1);
                }
                address.setId(addressId);
                return addressId;
            } catch (SQLException e) {
                throw new AppException("Unable to create address Record", e);
            }
        }
    }

    private long checkAddressExsistance(Address address) throws AppException {
        long id = 0;
        String getIdQuery = "SELECT id FROM address WHERE city = ? AND street = ? AND postal_code = ?;";

        try (PreparedStatement statement = connectionManager.createStatement(getIdQuery)) {
            statement.setString(1, address.getCity());
            statement.setString(2, address.getStreet());
            statement.setLong(3, address.getPostalCode());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                    return id;
                }
                return -1;
            }
        } catch (SQLException e) {
            throw new AppException("Failed to check address exsistance", e);
        }
    }

    @Override
    public Person read(int id, boolean includeAddress) throws AppException {
        try {
            if (!includeAddress || getIdIFPersonHasAddress(id) == 0) {
                String fetchAllQuery = "SELECT person.name, person.email, person.birth_date, person.created_date "
                        + "FROM person WHERE person.id = ?;";
                try (PreparedStatement statement = connectionManager.createStatement(fetchAllQuery)) {
                    statement.setLong(1, id);
                    try (ResultSet resultSet = connectionManager.executeQuery(statement)) {
                        Person person = new Person();
                        while (resultSet.next()) {
                            person.setId(id);
                            person.setName(resultSet.getString(1));
                            person.setEmail(resultSet.getString(2));
                            person.setBirthDate(resultSet.getDate(3).toLocalDate());
                            person.setCreatedDate(resultSet.getTimestamp(4).toLocalDateTime());
                        }
                        return person;
                    }
                }
            }
        } catch (SQLException e) {
            throw new AppException("Failed to read person record", e);
        }
        return read(id);
    }

    private long getIdIFPersonHasAddress(long id) throws AppException {

        String getIdQuery = "SELECT address_id FROM person WHERE id = ?;";
        try (PreparedStatement statement = connectionManager.createStatement(getIdQuery)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                    return id;
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new AppException("unable to check address record", e);
        }
    }

    private int updateRecord(long id, Person person,boolean doUpdateAddress, boolean doCreateAddress) 
                             throws AppException {

        if (doUpdateAddress) {
            if (doCreateAddress) {
                return updateCreatingAddress(id, person);
            } else {
                return updateWithAddress(id, person);
            }
        } else {
            return updateWithoutAddress(id, person);
        }
    }

    private int updateWithoutAddress(long id, Person person) throws AppException {
        String updateQuery = "UPDATE person "
                + "SET person.name = ?, person.email = ?, person.birth_date = ?, person.address_id = ? "
                + "WHERE person.id = ?;";
        try (PreparedStatement statement = connectionManager.createStatement(updateQuery)) {
            statement.setString(1, person.getName());
            statement.setString(2, person.getEmail());
            statement.setDate(3, new java.sql.Date(person.getBirthDate().toEpochDay()));
            statement.setNull(4, Types.BIGINT);
            statement.setLong(5, id);
            return connectionManager.executeUpdate(statement);
        } catch (SQLException e) {
            throw new AppException("unable to update without address record", e);
        }
    }

    private int updateCreatingAddress(long id, Person person) throws AppException {
        Address address = person.getAddress();
        long newAddressId = createAddress(new Address(address.getStreet(), address.getCity(), address.getPostalCode()));
        String updateQuery = "UPDATE person "
                + "SET person.name = ?, person.email = ?, person.birth_date = ?, person.address_id = ? "
                + "WHERE person.id = ?;";
        try (PreparedStatement statement = connectionManager.createStatement(updateQuery)) {
            statement.setString(1, person.getName());
            statement.setString(2, person.getEmail());
            statement.setDate(3, new java.sql.Date(person.getBirthDate().toEpochDay()));
            statement.setLong(4, newAddressId);
            statement.setLong(5, id);
            return connectionManager.executeUpdate(statement);
        } catch (SQLException e) {
            throw new AppException("unable to update creating a new address record", e);
        }
    }

    private int updateWithAddress(long id, Person person) throws AppException {
        String updateQuery = "UPDATE person, address " + "SET person.name = ?, person.email = ?, person.birth_date = ?,"
                + "address.street = ?, address.city = ?, address.postal_code = ? "
                + "WHERE person.id = ? AND address.id = (SELECT address_id FROM "
                + "(SELECT * FROM person) AS person_alias " + "WHERE id = ?);";
        try (PreparedStatement statement = connectionManager.createStatement(updateQuery)) {
            Address address = person.getAddress();
            statement.setString(1, person.getName());
            statement.setString(2, person.getEmail());
            statement.setDate(3, new java.sql.Date(person.getBirthDate().toEpochDay()));
            statement.setString(4, address.getStreet());
            statement.setString(5, address.getCity());
            statement.setLong(6, address.getPostalCode());
            statement.setLong(7, id);
            statement.setLong(8, id);
            return connectionManager.executeUpdate(statement);
        } catch (SQLException e) {
            throw new AppException("unable to update record", e);
        }
    }

}
