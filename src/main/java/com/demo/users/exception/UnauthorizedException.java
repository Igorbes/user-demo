package com.demo.users.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Access denied");
    }
}
