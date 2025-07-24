package com.mortgages.ai.mortgageservices.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinalResponse {
    private String userId;
    private String userName;
    private String status;
    private String firstName;
    private String lastName;
    private String reason;
}
