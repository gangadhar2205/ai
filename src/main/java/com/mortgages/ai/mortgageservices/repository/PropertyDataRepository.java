package com.mortgages.ai.mortgageservices.repository;

import com.mortgages.ai.mortgageservices.request.aiResp.PropertyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyDataRepository extends JpaRepository<PropertyData, String> {
}
