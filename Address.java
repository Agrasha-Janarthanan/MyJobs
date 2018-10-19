package com.objectfrontier.training.service;

/**
 * @author Lokesh.
 * @since Sep 21, 2018
 */
public class Address {

    private long id;
    private String street;
    private String city;
    private long postalCode;


    public Address(String street, String city, long postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

    public long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(long postalCode) {
        this.postalCode = postalCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Address [id=%s, street=%s, city=%s, postalCode=%s]", id, street, city, postalCode);
    }

}
