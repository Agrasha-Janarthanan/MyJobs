package com.objectfrontier.training.java.jdbc;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class AddressServiceTest {

    AddressService addressService;

    @BeforeTest
    private void initialize() {
        addressService = new AddressService();
    }

//    positive test case to insert a record
    @Test(dataProvider = "testCreateRecord_positiveDP")
    private void testCreateRecord_positive(String street, String city, long pincode, int expectedResult) {

        try{
            int actualResult = addressService.createRecord(street, city, pincode);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
        }
    }

    @DataProvider
    private Object[][] testCreateRecord_positiveDP() {

        return new Object[][] {
            {"Mint street", "Madras", 600235, 1},
            {"Ranganathan street", "Madras", 689752, 1}
        };
    }

//    negative test case to insert a record
    @Test(dataProvider = "testCreateRecord_negativeDP")
    private void testCreateRecord_negative(String street, String city, long pincode) {

        try{
            addressService.createRecord(street, city, pincode);
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Pincode cannot be null");
        }
    }

    @DataProvider
    private Object[][] testCreateRecord_negativeDP() {

        return new Object[][] {
            {"Mint street", "Madras", 0},
        };
    }

//    positive case to update a record
    @Test(dataProvider = "testUpdateRecord_positiveDP")
    private void testUpdateRecord_positive(String condition, int expectedResult) {

        try {
            int actualResult = addressService.updateRecord(condition);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
        }
    }

    @DataProvider
    private Object[][] testUpdateRecord_positiveDP() {

        return new Object[][] {
            {"city = 'Madras'", 2},
            {"city = 'Mumbai'", 0}
        };
    }

//    negative case to update a record
    @Test
    private void testUpdateRecord_negative() {

        try {
            addressService.updateRecord("");
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Cannot update without condition");
        }
    }

//    positive case to read a record using id
    @Test(dataProvider = "testReadRecord_positiveDP")
    private void testReadRecord_positive(long id, String expectedResult) {

        try {
            String actualResult = addressService.readRecord(id);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
        }
    }

    @DataProvider
    private Object[][] testReadRecord_positiveDP() {

        return new Object[][] {
            { 10901, "10901,Perumal koil Street,Coimbatore,600133"}
        };
    }

//    negative case to read a record using id
    @Test
    private void testReadRecord_negative() {
        try {
            addressService.readRecord(0);
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "ID cannot be empty");
        }
    }

//  positive case to read all record using id
    @Test(dataProvider = "testReadAllRecord_positiveDP")
    private void testReadAllRecord_positive(long id, String expectedResult) {

      try {
          String actualResult = addressService.readRecord(id);
          Assert.assertEquals(actualResult, expectedResult);
      } catch (Exception e) {
          Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
      }
    }

    @DataProvider
    private Object[][] testReadAllRecord_positiveDP() {

      return new Object[][] {
          { 10901, "10901,Perumal koil Street,Coimbatore,600133"}
      };
    }

//  negative case to read all record using id
    @Test
    private void testReadAllRecord_negative() {
      try {
          addressService.readRecord(0);
          Assert.fail("Expected an exception.");
      } catch (Exception e) {
          Assert.assertEquals(e.getMessage(), "ID cannot be empty");
      }
    }

//    positive case to delete a record
    @Test(dataProvider = "testDeleteRecord_positiveDP")
    private void testDeleteRecord_positive(String condition, int expectedResult) {
        try {
            int actualResult = addressService.deleteRecord(condition);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
        }
    }

    @DataProvider
    private Object[][] testDeleteRecord_positiveDP() {
        return new Object[][] {
            {"city = 'Chennai'", 2},
            {"city = 'Mumbai'", 0}
        };
    }

//    negative case to delete a record
    @Test
    private void testDeleteRecord_negative() {

        try {
            addressService.deleteRecord("");
            Assert.fail("Expected an exception.");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Cannot delete without condition");
        }
    }
}

