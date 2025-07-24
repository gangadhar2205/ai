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
public class BankStatement {
    @Id
    private String applicationId;
    private String full_name;
    private String account_holder_name;
    private String account_number;
    private String sort_code;
    private String statement_start_Date;
    private String statement_end_date;
    private String transaction_summary;
}
