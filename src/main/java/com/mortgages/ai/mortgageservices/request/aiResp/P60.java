package com.mortgages.ai.mortgageservices.request.aiResp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class P60 {
    @Id
    private String applicationId;
    private String full_name;
    private String national_insurance_number;
    private String tax_year_end;
    private String employer_name;
    private String total_pay;
    private String tax_paid;
}
