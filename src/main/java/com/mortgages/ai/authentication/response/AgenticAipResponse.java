package com.mortgages.ai.authentication.response;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgenticAipResponse {

    private String status;
    private String reason;
    private String roi;
    private String userName;
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private Date createdAt;
    private String mobile;
    private String applicants;
    private double deposit;
    private float depositPercentage;
    private String depositStatus;
    private String email;
    private boolean firstHome;
    private String journey;
    private double ltv;
    private String maritalStatus;
    private boolean nameChanged;
    private String nationality;
    private String option;
    private double propertyValue;
    private String title;
    private double totalLoan;
    private int year;
    private int day;
    private String month;
}
