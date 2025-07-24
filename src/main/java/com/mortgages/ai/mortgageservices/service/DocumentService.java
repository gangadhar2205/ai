package com.mortgages.ai.mortgageservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.mortgages.ai.authentication.repository.UserReqRepository;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.service.UserService;
import com.mortgages.ai.mortgageservices.FileHandlingException;
import com.mortgages.ai.mortgageservices.config.DocumentConfig;
import com.mortgages.ai.mortgageservices.controller.DocumentController;
import com.mortgages.ai.mortgageservices.feigns.AIAgentClient;
import com.mortgages.ai.mortgageservices.request.FinalResponse;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.UploadRequest;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import com.mortgages.ai.mortgageservices.response.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private final Storage storage;

    private final DocumentConfig documentConfig;

    private final AIAgentClient aiAgentClient;

    @Autowired
    UserReqRepository userReqRepository;

    @Autowired
    UserService userService;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public DocumentService(AIAgentClient aiAgentClient, DocumentConfig documentConfig) {
        this.aiAgentClient = aiAgentClient;
        this.documentConfig = documentConfig;
        storage = StorageOptions.getDefaultInstance().getService();
    }


    public UploadResponse handleFileServices(List<MultipartFile> files, UploadRequest request) throws IOException {

        UploadResponse uploadResponse = null;

        // Basic check: any file uploaded?
        if (files == null || files.isEmpty()) {
            throw new FileHandlingException("5001", "File is empty");
        }

        for (MultipartFile file : files) {
            // Check file is not empty
            if (file.isEmpty()) {
                throw new FileHandlingException("5001", "File is empty");
            }
            // Check file size
            if (file.getSize() > documentConfig.maxFileSizeMb) {
                throw new FileHandlingException("5001", "File size is too large to handle");
            }
            // Check MIME type
            String mimeType = file.getContentType();
            if (!documentConfig.allowedMimeTypes.contains(mimeType)) {
                throw new FileHandlingException("5001", "Incorrect mime type");
            }

            // ---- GCS upload placeholder ----
            boolean uploadStatus = uploadFileToGCS(file);

            if(uploadStatus) {
               UploadAiRequestWrapper aiRequestWrapper =  UploadAiRequestWrapper.builder()
                        .aiRequest(UploadAIRequest
                                .builder()
                                .applicationId(request.getApplicationId())
                                .documentType(request.getDocumentType())
                                .documentName(file.getOriginalFilename())
                                .fileContent(file)
                                .build())
                        .build();

                AgenticAiResponse aiResponse = aiAgentClient.sendFile(aiRequestWrapper);
            }
        }
        return uploadResponse;
    }

    private boolean uploadFileToGCS(MultipartFile file) throws IOException {
        String objectName = file.getOriginalFilename(); // You can customize this
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();

        // Prepare BlobInfo
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(contentType)
                .build();

        // Upload to GCS
        Blob blob = storage.create(blobInfo, fileContent);
        String fileUrl = String.format("gs://%s/%s", bucketName, objectName);


        // Optional: Validate further
        if (blob != null && blob.exists()) {
            return true; // Success
        } else {
            return false; // Something went wrong
        }
    }

    public FinalResponse mortgageSubmit(String userId) throws JsonProcessingException {
       UserReq userReq = userReqRepository.findByUserId(userId);
       userService.formAiRequest(userReq);
       return null;
    }
}
