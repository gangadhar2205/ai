package com.mortgages.ai.mortgageservices.feigns;

import com.mortgages.ai.mortgageservices.request.EnquiryAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aiAgentClient", url = "http://<AI_AGENT_URL>")
public interface AIAgentClient {
    @PostMapping("/your-ai-agent-endpoint") // Replace with actual endpoint
    AgenticAiResponse sendFile(@RequestBody UploadAiRequestWrapper aiRequestWrapper);

    @PostMapping("/your-ai-agent-endpoint") // Replace with actual endpoint
    AgenticAiResponse makeEnquiry(@RequestBody EnquiryAiRequestWrapper aiRequestWrapper);
}
