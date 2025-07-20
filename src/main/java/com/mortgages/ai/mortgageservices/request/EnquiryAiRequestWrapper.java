package com.mortgages.ai.mortgageservices.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryAiRequestWrapper {
    private EnquiryAiRequest enquiryAiRequest;
}
