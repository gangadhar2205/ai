package com.mortgages.ai.mortgageservices.request;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Table(name = "application_details")
@Component
public class AIPRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // or GenerationType.SEQUENCE for PostgreSQL sequence
        private Long applicationId;

        private String firstName;
        private String lastName;
        private String address;
        private String mobileNumber;
        private String bankAccount;
        private String ifscCode;

        @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private List<Document> documents;
}
