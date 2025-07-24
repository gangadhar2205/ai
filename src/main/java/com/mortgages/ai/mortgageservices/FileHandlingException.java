package com.mortgages.ai.mortgageservices;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
public class FileHandlingException extends RuntimeException{
    private String errorCode;
    private String errorMessage;

    public FileHandlingException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
