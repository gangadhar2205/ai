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
import com.mortgages.ai.mortgageservices.repository.*;
import com.mortgages.ai.mortgageservices.request.FinalResponse;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.UploadRequest;
import com.mortgages.ai.mortgageservices.request.aiResp.*;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import com.mortgages.ai.mortgageservices.response.UploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DocumentService {

    private final Storage storage;

    private final DocumentConfig documentConfig;

    private final AIAgentClient aiAgentClient;

    @Autowired
    BankStatementRepository bankStatementRepository;

    @Autowired
    DlRepository dlRepository;

    @Autowired
    P60Repository p60Repository;

    @Autowired
    PassportRepository passportRepository;

    @Autowired
    PropertyDataRepository propertyDataRepository;

    @Autowired
    SalarySlipRepository salarySlipRepository;

    @Autowired
    UtilityBillRepository utilityBillRepository;

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
//             Check file is not empty
            if (file.isEmpty()) {
                throw new FileHandlingException("5001", "File is empty");
            }
//             Check file size
            if (file.getSize() > documentConfig.maxFileSizeMb) {
                throw new FileHandlingException("5001", "File size is too large to handle");
            }
            // Check MIME type
            String mimeType = file.getContentType();
            if (!documentConfig.allowedMimeTypes.contains(mimeType)) {
                throw new FileHandlingException("5001", "Incorrect mime type");
            }

//             ---- GCS upload placeholder ----
            boolean uploadStatus = uploadFileToGCS(file, request.getApplicationId(), request.getDocumentType());

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


            }
        }

        uploadResponse = buildUploadResponse(request);

        return uploadResponse;
    }

    private UploadResponse buildUploadResponse(UploadRequest request) {
        return UploadResponse.builder()
                .documentType(request.getDocumentType())
                .status(" Uploaded successfully")
                .build();
    }

    private boolean uploadFileToGCS(MultipartFile file, String applicationId, String documentType) throws IOException {
        log.info("Uploading GCS FIle to Bucket");
        String objectName = applicationId+ "_" + documentType +"_" +file.getOriginalFilename();// You can customize this
        log.info("File Name {}", objectName);
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();

        // Prepare BlobInfo
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(contentType)
                .build();

        log.info("Blob informations set");

        // Upload to GCS
        Blob blob = storage.create(blobInfo, fileContent);
        String fileUrl = String.format("gs://%s/%s", bucketName, objectName);

        log.info("file url {}", fileUrl);

        // Optional: Validate further
        if (blob != null && blob.exists()) {
            return true; // Success
        } else {
            return false; // Something went wrong
        }
    }

    public FinalResponse mortgageSubmit(String userId) throws JsonProcessingException {
       UserReq userReq = userReqRepository.findByUserId(userId);
//       userService.formAiRequest(userReq);
//       if (userReq.getUserName().equals())
        FinalResponse finalResponse = prepareFinalResponse(userReq);

       return finalResponse;
    }

    private FinalResponse prepareFinalResponse(UserReq userReq) {
        return null;
    }
}
