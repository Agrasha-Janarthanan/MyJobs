package com.objectfrontier.training.service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Lokesh.
 * @since Sep 21, 2018
 */
public class Person {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private LocalDate birthDate;
    private LocalDateTime createdDate;


    public Person() {
        super();
    }

    public Person(String firstName, String lastName, String email, Address address, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
    }

    public static LocalDate getDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return MessageFormat.format("{0} {1}", firstName, lastName);
    }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Person [id = %s, firstName = %s, lastName = %s email = %s%n, address = %s%n, birthDate = %s, createdDate = %s]",
                id, firstName, lastName, email, address, birthDate, createdDate);
    }

}
