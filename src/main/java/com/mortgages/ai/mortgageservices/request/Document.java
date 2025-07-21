package com.mortgages.ai.mortgageservices.request;
import jakarta.persistence.*;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "applicationId", nullable = false)
    private AIPRequest application;

    private String docName;
    private String docType;
    private String docStatus;
    private String docDetail; // You can use @Lob for large text or change to JSONB with custom converter
}

