package com.disney.app.domain;

public class NoPeriodFoundException extends RuntimeException {
    public NoPeriodFoundException(String message) {
        super(message);
    }
}
