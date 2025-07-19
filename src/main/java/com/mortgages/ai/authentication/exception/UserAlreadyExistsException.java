package com.mortgages.ai.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public UserAlreadyExistsException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
