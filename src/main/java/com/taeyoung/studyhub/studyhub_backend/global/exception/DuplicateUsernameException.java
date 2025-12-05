package com.taeyoung.studyhub.studyhub_backend.global.exception;

public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
