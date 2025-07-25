package com.mortgages.ai.mortgageservices.controller;

import com.mortgages.ai.mortgageservices.request.FinalResponse;
import com.mortgages.ai.mortgageservices.request.UploadRequest;
import com.mortgages.ai.mortgageservices.response.UploadResponse;
import com.mortgages.ai.mortgageservices.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/user/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> handleFileUpload(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("request") UploadRequest request
            ) throws IOException {
        UploadResponse uploadResponse = documentService.handleFileServices(files, request);
        return ResponseEntity.status(201).body(uploadResponse);
    }

    @GetMapping(value = "/mortgage/submit")
    public ResponseEntity<FinalResponse> mortgageSubmit(
           @RequestParam String userId
    ) throws IOException {
        FinalResponse finalResponse = documentService.mortgageSubmit(userId);
        return ResponseEntity.status(200).body(finalResponse);
    }
}
