package com.mortgages.ai.authentication.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BadCredentialsException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public BadCredentialsException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
