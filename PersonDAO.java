package com.objectfrontier.training.service;

import java.util.List;

/**
 * @author Lokesh.
 * @since Sep 27, 2018
 */
public interface PersonDAO {

    List<Person> readAll() throws AppException;
    Person read(int id) throws AppException;
    Person read(int id, boolean includeAddress) throws AppException;
    int update(Person person) throws AppException;
    int delete(int id) throws AppException;
    long insert(Person person) throws AppException;
    <v> boolean isPresent(String field, v value) throws AppException;
}
