package com.rockseven.test.repository.model;

public class InvalidDataException extends Exception {
    public InvalidDataException(String errorMessage) {
        super(errorMessage);
    }
}
