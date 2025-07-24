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
public class SalarySlip {
   @Id
   private String applicationId;
   private String full_name;
   private String pay_period_start;
   private String pay_period_end;
   private String net_pay;
   private String gross_pay;
   private String national_insurance_number;
   private String employer_name;
}
