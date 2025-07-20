package com.mortgages.ai.mortgageservices.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadAIRequest {
    private String applicationId;
    private String documentType;
    private String documentName;
    private byte[] fileContent;
}
