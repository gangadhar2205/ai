package com.mortgages.ai.mortgageservices.repository;

import com.mortgages.ai.mortgageservices.request.aiResp.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankStatementRepository extends JpaRepository<BankStatement, String> {
}
