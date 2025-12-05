package com.taeyoung.studyhub.studyhub_backend.global.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
