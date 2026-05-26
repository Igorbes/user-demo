package com.demo.users.exception;

import org.springframework.jdbc.core.SqlReturnType;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException(String msg) {
        super(msg);
    }
}
