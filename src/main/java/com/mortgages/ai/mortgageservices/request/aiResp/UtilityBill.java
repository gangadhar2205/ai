package com.mortgages.ai.mortgageservices.request.aiResp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UtilityBill {
    @Id
    private String applicationId;
    private String full_name;
    private String address;
    private String billing_date;
    private String provider_name;
    private String account_number;
    private String bill_amount;
}
