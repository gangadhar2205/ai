package com.mortgages.ai.mortgageservices.repository;

import com.mortgages.ai.mortgageservices.request.aiResp.SalarySlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalarySlipRepository extends JpaRepository<SalarySlip, String> {
}
