package com.mortgages.ai.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDecision {
    private String status;
    private String reason;
    private String roi;
}
