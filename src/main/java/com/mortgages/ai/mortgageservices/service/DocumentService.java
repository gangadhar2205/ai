package com.mortgages.ai.mortgageservices.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.mortgages.ai.mortgageservices.config.DocumentConfig;
import com.mortgages.ai.mortgageservices.controller.DocumentController;
import com.mortgages.ai.mortgageservices.feigns.AIAgentClient;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.UploadRequest;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import com.mortgages.ai.mortgageservices.response.UploadResponse;
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

        }

        for (MultipartFile file : files) {
            // Check file is not empty
            if (file.isEmpty()) {

            }
            // Check file size
            if (file.getSize() > documentConfig.maxFileSizeMb) {

            }
            // Check MIME type
            String mimeType = file.getContentType();
            if (!documentConfig.allowedMimeTypes.contains(mimeType)) {

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
                                .fileContent(file.getBytes())
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
}
