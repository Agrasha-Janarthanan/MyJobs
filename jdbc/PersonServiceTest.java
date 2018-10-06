package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PersonServiceTest {

    ConnectionManager connection = new ConnectionManager();
    Connection conn = connection.openConnection("jdbc:mysql://pc1620:3306/agrasha_janarthanan?useSSL=false&user=agrasha_janarthanan&password=demo");
    PersonService personService;

    @BeforeClass
    private void initialize() {
        personService = new PersonService();
    }

    @Test(dataProvider = "testCreatePositiveDP")
    private void testCreatePositive(Person person, Address address, long expectedResult) {
        try {
            long actualResult = personService.create(person, address, conn);
            Assert.assertEquals(actualResult, expectedResult);
            conn.commit();
        } catch (Exception e) {
            Assert.fail("Unexpected exception for the given input.Expected result : " + expectedResult + e);
        }
    }

    @DataProvider
    private Object[][] testCreatePositiveDP() {

        Person person = new Person();
        person.setName("agi");
        person.setEmail("agi@gmail.com");
        person.setBirthDate(Date.valueOf("1996-08-08"));
        person.setAddressId(1);

        Address address = new Address();
        address.setStreet("car street");
        address.setCity("Tiruvannamalai");
        address.setPostalCode(606601);

        Person nextPerson = new Person();
        nextPerson.setName("agrasha");
        nextPerson.setEmail("agrasha@gmail.com");
        nextPerson.setBirthDate(Date.valueOf("1996-08-08"));
        nextPerson.setAddressId(2);

        Address nextAddress = new Address();
        nextAddress.setStreet("temple street");
        nextAddress.setCity("Tiruvannamalai");
        nextAddress.setPostalCode(606601);

        return new Object[][] {
            {person, address, 1},
            {nextAddress, nextAddress, 2}
        };
    }
}
