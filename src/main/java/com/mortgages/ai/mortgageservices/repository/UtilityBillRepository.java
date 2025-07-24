package com.mortgages.ai.mortgageservices.repository;

import com.mortgages.ai.mortgageservices.request.aiResp.UtilityBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilityBillRepository extends JpaRepository<UtilityBill, String> {
}
