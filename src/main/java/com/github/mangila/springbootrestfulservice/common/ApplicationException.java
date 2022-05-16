package com.github.mangila.springbootrestfulservice.common;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}
