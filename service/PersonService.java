package com.objectfrontier.training.service;

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Lokesh.
 * @since Sep 21, 2018
 */
public class PersonService {

    private ConnectionManager connectionManager;
    private PersonDAO personDBManager;

    public PersonService(Connection connection) {
        this.connectionManager = new ConnectionManager(connection);
        this.personDBManager = new PersonDBManager(connectionManager);
    }

    public PersonService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.personDBManager = new PersonDBManager(connectionManager);
    }

    public long insert(Person person) throws AppException {
        long id = 0;
        validatePerson(person);
        id = personDBManager.insert(person);
        return id;
    }

    public Person read(int id, boolean includeAddress) throws AppException {
        validatePersonId(id);
        return personDBManager.read(id, includeAddress);
    }

    public int delete(int id) throws AppException {
        validatePersonId(id);
        return personDBManager.delete(id);
    }

    public int update(Person person) throws AppException {
        validatePersonId(person.getId());
        validatePerson(person);
        return personDBManager.update(person);
    }

    public List<Person> readAll() throws AppException {
        return personDBManager.readAll();
    }

    private void validatePerson(Person person) throws AppException {
        int throwException = 0;
        AppException dataBaseException = new AppException();
        if (person.getName() == null) {
            dataBaseException.add(new AppException("Name cannot be Empty"));
            throwException++;
        }
        if (person.getBirthDate() == null) {
            dataBaseException.add(new AppException("Birthday field cannot be empty"));
            throwException++;
        }
        if (person.getEmail() == null) {
            dataBaseException.add(new AppException("Email Cannot be Null"));
            throwException++;
        }
        if (personDBManager.isPresent("email", person.getEmail())) {
            dataBaseException.add(new AppException("Email id already exists"));
            throwException++;
        }
        if (throwException > 0) {
            throw dataBaseException;
        }
    }

    public void validatePersonId(long id) throws AppException {
        if (! personDBManager.isPresent("id", id)) {
            throw new AppException(MessageFormat.format("person with {0} is not in the database", id));
        }
    }

}
