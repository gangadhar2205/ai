package com.mortgages.ai.mortgageservices.controller;

import com.mortgages.ai.mortgageservices.request.EnquiryRequest;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import com.mortgages.ai.mortgageservices.service.EnquiryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgenticAiResponse> enquireAiAgent(@RequestBody EnquiryRequest enquiryRequest) {
        AgenticAiResponse response = enquiryService.makeEnquiry(enquiryRequest);
        return null;
    }
}
