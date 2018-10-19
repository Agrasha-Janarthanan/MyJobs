package com.objectfrontier.training.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Lokesh.
 * @since Sep 24, 2018
 */
public class PersonServiceTest {

    private PersonService personService;
    ConnectionManager personDBConnection;

    @BeforeClass
    private void initService() {
        try {
            personDBConnection = new ConnectionManager("jdbc:mysql://pc1620:3306/lokesh_rajendran",
                                                                          "mysqlCredentials.txt");
            personService = new PersonService(personDBConnection);
        } catch (IOException | SQLException e) {
            new RuntimeException(e);
        }
    }

    @Test (dataProvider = "positiveCase_insert")
    private void insertTest_positive(long expectedResult, Person person) throws SQLException {
        long actualResult = 0;
        try {
            actualResult = personService.insert(person);
            personDBConnection.commit();
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            personDBConnection.rollBack();
            Assert.fail(MessageFormat.format("UnExpected behaviour {0} for input {1}", e, person));
        }
    }

    @Test (dataProvider = "negativeCase_insert")
    private void insertTest_negative(Person person, AppException expectedResult) {
        try {
            personService.insert(person);
            Assert.fail(MessageFormat.format("Expected an Exception for {0}", person));
        } catch (AppException e) {
            Assert.assertEquals(e.getAssociateErrorsAsCollection().toString(),
                                expectedResult.getAssociateErrorsAsCollection().toString());
        }
    }

    @Test (dataProvider = "positiveCase_update")
    private void updateTest_postive(int expectedResult, Person person) {
        int actualResult;
        try {
            actualResult = personService.update(person);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail(MessageFormat.format("UnExpected behaviour {0} for input {1}", e.getMessage(), person));
        }
    }

    @Test (dataProvider = "negativeCase_update")
    private void updateTest_negative(Person person, AppException expectedResult) {
        try {
            personService.update(person);
            Assert.fail(MessageFormat.format("Expected an Exception for {0}", person));
        } catch (AppException e) {
            Assert.assertEquals(e.getMessage(),
                                expectedResult.getMessage());
        }
    }

    @Test (dataProvider = "positiveCase_delete")
    private void deleteTest_postive(int id, int expectedResult) {
        int actualResult;
        try {
            actualResult = personService.delete(id);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail(MessageFormat.format("UnExpected behaviour {0} for input {1}", e, id));
        }
    }

    @Test (dataProvider = "negativeCase_delete")
    private void deleteTest_negative(int id, AppException expectedResult) {
        try {
            personService.delete(id);
            Assert.fail(MessageFormat.format("Expected an Exception for {0}", id));
        } catch (AppException e) {
            Assert.assertEquals(e.getMessage(),
                                expectedResult.getMessage());
        }
    }

    @Test (dataProvider = "negativeCase_read")
    private void readTest_negative(int id, boolean includeAddress, AppException expectedResult) {
        try {
            personService.read(id, includeAddress);
            Assert.fail(MessageFormat.format("Expected an Exception for {0}", id));
        } catch (AppException e) {
            Assert.assertEquals(e.getMessage(),
                                expectedResult.getMessage());
        }
    }

    @Test (dataProvider = "positiveCase_read")
    private void readTest_postive(Person expectedResult, boolean includeAddress, int id) {
        Person actualResult;
        try {
            actualResult = personService.read(id, includeAddress);
            Assert.assertEquals(actualResult.toString(), expectedResult.toString());
        } catch (Exception e) {
            Assert.fail(MessageFormat.format("UnExpected behaviour {0} for input {1}", e, id));
        }
    }

//    @Test (dataProvider = "postiveCase_readAllRecord")
//    private void readAllTest_postive(List<Person> expectedResult) {
//        List<Person> actualResult;
//        try {
//            actualResult = personService.readAll();
//            Assert.assertEquals(actualResult.toString(), expectedResult.toString());
//        } catch (Exception e) {
//            Assert.fail(MessageFormat.format("UnExpected behaviour {0} for readAll", e));
//        }
//    }

    @DataProvider (name = "positiveCase_insert")
    private Object[][] insertRecordTest_positiveDP() {
        Address address = new Address("hassan khan street", "Chittoor", 517001);
        return new Object[][] {
            {9, new Person("R.Boovan", "boovanNaiks@gmail.com", address, Person.getDate(1997, 1, 1))},
            {10, new Person("R.Rajendran", "RajendranNaikar2@gmail.com", null, Person.getDate(1997, 1, 1))},
            {11, new Person("R.BoovanClone", "boovanNaiker3S@gmail.com", address, Person.getDate(1997, 1, 1))}
        };
    }

    @DataProvider (name = "negativeCase_insert")
    private Object[][] insertRecordTest_negativeDP() {

        Address address = new Address("hassan khan street", "Chittoor", 517001);
        AppException dataBaseException = new AppException();
        dataBaseException.add(new AppException("Name cannot be Empty"));
        dataBaseException.add(new AppException("Email id already exists"));
        return new Object[][] {
            {new Person(null, "lokeshbalaji68@gmail.com", address, Person.getDate(1997, 1, 1)), dataBaseException},
        };
    }

    @DataProvider (name = "negativeCase_update")
    private Object[][] updateRecordTest_negativeDP() {

        Address address = new Address("hassan khan street", "Chittoor", 517001);
        AppException dataBaseException = new AppException();
        dataBaseException.add(new AppException("Name cannot be Empty"));
        Person person = new Person(null, "lokeshbalaji68@gmail.com", address, Person.getDate(1997, 1, 1));
        person.setId(2);
        return new Object[][] {
            {person, dataBaseException},
        };
    }

    @DataProvider (name = "positiveCase_delete")
    private Object[][] deleteTest_positiveDP() {
        return new Object[][] {
            {5, 1},
        };
    }

    @DataProvider (name = "positiveCase_read")
    private Object[][] readTest_positiveDP() {
        Address address = new Address("Hassan Street", "chittoor", 517001);
        address.setId(1);
        Person personOne = new Person("R.Boovan", "boovanNaiks@gmail.com", address, Person.getDate(1997, 1, 1));
        personOne.setId(6);
        personOne.setCreatedDate(LocalDateTime.of(2018, 9, 24, 16, 39, 49));
        Person personTwo = new Person("R.Rajendran", "RajendranNaikar@gmail.com", null, Person.getDate(1997, 1, 1));
        personTwo.setId(7);
        personTwo.setCreatedDate(LocalDateTime.of(2018, 9, 24, 16, 39, 49));
        return new Object[][] {
            {personOne, true, 6},
            {personTwo, false, 7}
        };
    }

    @DataProvider(name = "positiveCase_update")
    private Object[][] updateTest_positiveDP() {
        Address address = new Address("hassan khan street", "Chittoor", 517001);
        Person personOne = new Person("R.Boovans", "boovanNaiks@gmail.com", address, Person.getDate(1997, 1, 1));
        personOne.setId(7);
        address.setId(1);
        Person personTwo = new Person("R.Rajendrand", "RajendranNaikar2@gmail.com", null, Person.getDate(1997, 1, 1));
        personTwo.setId(8);
        return new Object[][] {
            {7, personOne},
            {8, personTwo}
        };
    }

    @DataProvider(name = "negativeCase_read")
    private Object[][] readTest_negativeDP() {
        return new Object[][] {
            {115, true, new AppException(MessageFormat.format("person with {0} is not in the database", 115))},
            {125, true, new AppException(MessageFormat.format("person with {0} is not in the database", 125))}
        };
    }

    @DataProvider(name = "negativeCase_delete")
    private Object[][] deleteTest_negativeDP() {
        return new Object[][] {
            {115, new AppException(MessageFormat.format("person with {0} is not in the database", 115))},
            {125, new AppException(MessageFormat.format("person with {0} is not in the database", 125))}
        };
    }
    
    @AfterClass
    private void releaseResources() {
        try {
            personDBConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
