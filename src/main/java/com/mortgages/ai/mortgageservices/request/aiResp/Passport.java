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
public class Passport {
    @Id
    private String applicationId;
    private String full_name;
    private String date_of_birth;
    private String place_of_birth;
    private String nationality;
    private String gender;
    private String passport_number;
    private String issue_date;
    private String expiry_date;
}
