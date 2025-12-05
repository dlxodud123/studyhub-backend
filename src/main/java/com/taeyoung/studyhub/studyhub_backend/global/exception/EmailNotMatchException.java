package com.taeyoung.studyhub.studyhub_backend.global.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailNotMatchException extends AuthenticationException {
    public EmailNotMatchException(String msg) {
        super(msg);
    }
}