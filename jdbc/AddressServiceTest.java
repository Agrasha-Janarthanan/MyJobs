package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddressServiceTest {

    ConnectionManager connManager = new ConnectionManager();
    Connection conn = connManager.openConnection("jdbc:mysql://pc1620:3306/agrasha_janarthanan?useSSL=false&user=agrasha_janarthanan&password=demo");
    AddressService addressService;

    @BeforeClass
    private void initalize() {
        addressService = new AddressService();
    }

    @Test(dataProvider = "testCreatePositiveDP")
    private void testCreatePositive(Address address, long expectedResult) {
        
        try {
            long actualResult = addressService.create(address, conn);
            Assert.assertEquals(actualResult, expectedResult);
            conn.commit();
        }catch (Exception e) {
            Assert.fail("Unexpected Exception for the given input.Expected result is " + expectedResult + e);
        }
    }
    
    @DataProvider
    private Object[][] testCreatePositiveDP() {
        
        return new Object[][] {
            {"Mint street", "Madras", 600235, 1},
            {"Car street", "Tiruvannamalai", 606601, 2}
        };
    }

    @Test(dataProvider = "testCreateNegativeDP")
    private void testCreateNegative(Address address, Connection conn, String exceptionMessage) {

        try {
            addressService.create(address, conn);
            Assert.fail("Expected an exception");
            conn.rollback();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), exceptionMessage);
        }
    }

    @DataProvider
    private Object[][] testCreateNegativeDP() {

        return new Object[][] {
            {"", "Madras", 600235, conn, "Street cannot be null or empty"},
            {"Car street", "", 606601, conn, "City cannot be null or empty"},
            {"Car street", "Chennai", 0, conn, "PostalCode cannot be null or empty"},
            {"Car street", "Chennai", 606601, null, "Service not available"},
        };
    }
}
