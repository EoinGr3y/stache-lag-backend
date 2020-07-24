package com.rockseven.test.controller;

public class InvalidDataException extends Exception {
    public InvalidDataException(String errorMessage) {
        super(errorMessage);
    }
}
