package com.mortgages.ai.mortgageservices.feigns;

import com.mortgages.ai.authentication.response.AipResponse;
import com.mortgages.ai.mortgageservices.config.FeignConfig;
import com.mortgages.ai.mortgageservices.request.EnquiryAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.EnquiryRequest;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aiAgentClient", url = "http://127.0.0.1:9091/chat", configuration = FeignConfig.class)
public interface AIAgentClient {

    @PostMapping("/your-ai-agent-endpoint") // Replace with actual endpoint
    AgenticAiResponse sendFile(@RequestBody UploadAiRequestWrapper aiRequestWrapper);

    @PostMapping("/chat") // Replace with actual endpoint
    AgenticAiResponse makeEnquiry(@RequestBody EnquiryRequest aiRequestWrapper);

    @PostMapping("/aip") // Replace with actual endpoint
    AipResponse aip(@RequestBody String aiRequest);
}
