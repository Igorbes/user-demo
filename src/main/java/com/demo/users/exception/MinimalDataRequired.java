package com.demo.users.exception;

public class MinimalDataRequired extends RuntimeException {
    public MinimalDataRequired(String msg) {
        super((msg));
    }
}
