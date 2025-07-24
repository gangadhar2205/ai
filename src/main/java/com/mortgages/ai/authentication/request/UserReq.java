package com.mortgages.ai.authentication.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Entity
@Validated
@Component
public class UserReq {

    @NotBlank
    @Column(nullable = false)
    private String userName;

    @Id
    @Column(nullable = false)
    private String userId;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String applicants;

    @Column(nullable = false)
    private double deposit;

    @Column(nullable = false)
    private float depositPercentage;

    @Column(nullable = false)
    private String depositStatus;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean firstHome;

    @Column(nullable = false)
    private String journey;

    @Column(nullable = false)
    private double ltv;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private boolean nameChanged;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String option;

    @Column(nullable = false)
    private double propertyValue;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double totalLoan;

    @Column(nullable = true)
    private int year;

    @Column(nullable = true)
    private int day;

    @Column(nullable = true)
    private String month;
}

