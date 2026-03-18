package com.parking.common.exception;

import lombok.Getter;

@Getter
public abstract class BaseParkingException extends RuntimeException {
    private final int statusCode; // Simple integer instead of Spring class

    public BaseParkingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}