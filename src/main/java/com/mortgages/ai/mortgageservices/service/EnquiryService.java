package com.mortgages.ai.mortgageservices.service;

import com.mortgages.ai.mortgageservices.feigns.AIAgentClient;
import com.mortgages.ai.mortgageservices.request.EnquiryAiRequest;
import com.mortgages.ai.mortgageservices.request.EnquiryAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.EnquiryRequest;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import org.springframework.stereotype.Service;

@Service
public class EnquiryService {

    private AIAgentClient aiAgentClient;

    public EnquiryService(AIAgentClient aiAgentClient) {
        this.aiAgentClient = aiAgentClient;
    }

    public AgenticAiResponse makeEnquiry(EnquiryRequest enquiryRequest) {

        EnquiryAiRequestWrapper aiWrapper = EnquiryAiRequestWrapper
                .builder()
                .enquiryAiRequest(
                        EnquiryAiRequest
                                .builder().build()
                )
                .build();

        System.out.println(enquiryRequest);

        AgenticAiResponse aiResponse = aiAgentClient.makeEnquiry(enquiryRequest);
        System.out.println("AI Repise "+aiResponse);
        return aiResponse;
    }
}
