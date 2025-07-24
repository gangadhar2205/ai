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
public class PropertyData {
    @Id
    private String applicationId;
    private String owner_name;
    private String owner_address;
    private String property_type;
    private String valuation_amount;
    private String purchase_date;
    private String property_id_reference;

}
