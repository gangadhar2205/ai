package com.mortgages.ai.mortgageservices.repository;

import com.mortgages.ai.mortgageservices.request.aiResp.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, String> {
}
