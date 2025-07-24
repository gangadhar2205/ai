package com.mortgages.ai.mortgageservices.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequest {
    private String applicationId;
    private String documentType;
    private String documentName;
    private String fileContentType;
    private String bucketUrl;
    private String fileType;
    private double fileSize;
    private String userName;
}

