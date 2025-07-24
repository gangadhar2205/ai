package com.mortgages.ai.mortgageservices.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadAIRequest {
    private String applicationId;
    private String documentType;
    private String documentName;
    private MultipartFile fileContent;
}
