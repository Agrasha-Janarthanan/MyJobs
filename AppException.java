 package com.objectfrontier.training.service;

import java.util.ArrayDeque;
import java.util.Collection;

public class AppException extends Exception {

    private String message;
    private Exception cause;
    private ArrayDeque<AppException> exceptions = new ArrayDeque<>();
    
    public AppException() {
        super();
    }

    public AppException(Exception cause) {
        super();
        this.cause = cause;
    }
    
    public AppException(String message) {
        super();
        this.message = message;
    }
    
    public AppException(String message, Exception cause) {
        super();
        this.message = message;
        this.cause = cause;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCause(Exception cause) {
        this.cause = cause;
    }


    public Exception getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    public void add(AppException dataBaseException) {
        exceptions.push(dataBaseException);
    }

    public int length() {
        return exceptions.size();
    }

    public AppException get(AppException dataBaseException) {
        return exceptions.pop();
    }

    public Collection<AppException> getAssociateErrorsAsCollection() {
        return exceptions;
    }

}
