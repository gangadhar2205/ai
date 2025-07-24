package com.mortgages.ai.mortgageservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.mortgages.ai.authentication.repository.UserReqRepository;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.service.UserService;
import com.mortgages.ai.mortgageservices.FileHandlingException;
import com.mortgages.ai.mortgageservices.config.DocumentConfig;
import com.mortgages.ai.mortgageservices.controller.DocumentController;
import com.mortgages.ai.mortgageservices.feigns.AIAgentClient;
import com.mortgages.ai.mortgageservices.repository.*;
import com.mortgages.ai.mortgageservices.request.FinalResponse;
import com.mortgages.ai.mortgageservices.request.UploadAIRequest;
import com.mortgages.ai.mortgageservices.request.UploadAiRequestWrapper;
import com.mortgages.ai.mortgageservices.request.UploadRequest;
import com.mortgages.ai.mortgageservices.request.aiResp.*;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import com.mortgages.ai.mortgageservices.response.UploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DocumentService {

    private final Storage storage;

    private final DocumentConfig documentConfig;

    private final AIAgentClient aiAgentClient;

    @Autowired
    BankStatementRepository bankStatementRepository;

    @Autowired
    DlRepository dlRepository;

    @Autowired
    P60Repository p60Repository;

    @Autowired
    PassportRepository passportRepository;

    @Autowired
    PropertyDataRepository propertyDataRepository;

    @Autowired
    SalarySlipRepository salarySlipRepository;

    @Autowired
    UtilityBillRepository utilityBillRepository;

    @Autowired
    UserReqRepository userReqRepository;

    @Autowired
    UserService userService;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public DocumentService(AIAgentClient aiAgentClient, DocumentConfig documentConfig) {
        this.aiAgentClient = aiAgentClient;
        this.documentConfig = documentConfig;
        storage = StorageOptions.getDefaultInstance().getService();
    }


    public UploadResponse handleFileServices(List<MultipartFile> files, UploadRequest request) throws IOException {

        UploadResponse uploadResponse = null;

        // Basic check: any file uploaded?
        if (files == null || files.isEmpty()) {
            throw new FileHandlingException("5001", "File is empty");
        }

        for (MultipartFile file : files) {
            // Check file is not empty
            if (file.isEmpty()) {
                throw new FileHandlingException("5001", "File is empty");
            }
            // Check file size
            if (file.getSize() > documentConfig.maxFileSizeMb) {
                throw new FileHandlingException("5001", "File size is too large to handle");
            }
            // Check MIME type
            String mimeType = file.getContentType();
            if (!documentConfig.allowedMimeTypes.contains(mimeType)) {
                throw new FileHandlingException("5001", "Incorrect mime type");
            }

            // ---- GCS upload placeholder ----
            boolean uploadStatus = uploadFileToGCS(file, request.getApplicationId(), request.getDocumentType());

            if(uploadStatus) {
//               UploadAiRequestWrapper aiRequestWrapper =  UploadAiRequestWrapper.builder()
//                        .aiRequest(UploadAIRequest
//                                .builder()
//                                .applicationId(request.getApplicationId())
//                                .documentType(request.getDocumentType())
//                                .documentName(file.getOriginalFilename())
//                                .fileContent(file)
//                                .build())
//                        .build();

               if(request.getDocumentType().equals("passport")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       Passport passport_oliver = Passport.builder()
                               .full_name("Oliver James Smith")
                               .date_of_birth("15/08/1990")
                               .place_of_birth("London, UK")
                               .nationality("British")
                               .gender("Male")
                               .passport_number("505893142") // valid UK format: 9 digits
                               .issue_date("10/01/2020")
                               .expiry_date("10/01/2030")
                               .build();
                       passportRepository.save(passport_oliver);
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       Passport passport_emily = Passport.builder()
                               .full_name("Emily Charlotte Taylor")
                               .date_of_birth("22/03/1985")
                               .place_of_birth("Manchester, UK")
                               .nationality("British")
                               .gender("Female")
                               .passport_number("721604839") // another valid format
                               .issue_date("05/05/2019")
                               .expiry_date("05/05/2029")
                               .build();

                       passportRepository.save(passport_emily);
                   }

               } else if (request.getDocumentType().equals("dl")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       Dl dl_oliver = Dl.builder()
                               .full_name("Oliver James Smith")
                               .date_of_birth("15/08/1990")
                               .address("10 Downing Street, Westminster, London, SW1A 2AA")
                               .license_number("SMITH908150OJ9IJ")  // Simulated DVLA-style number
                               .issue_date("12/06/2018")
                               .expiry_date("12/06/2028")
                               .endorsements("None")
                               .build();

                       dlRepository.save(dl_oliver);
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                   Dl dl_emily = Dl.builder()
                           .full_name("Emily Charlotte Taylor")
                           .date_of_birth("22/03/1985")
                           .address("25 Deansgate, Manchester, M3 4EN")
                           .license_number("TAYLO853220EC4MV")
                           .issue_date("05/04/2019")
                           .expiry_date("05/04/2029")
                           .endorsements("SP30 x1")
                           .build();
                   dlRepository.save(dl_emily);
                   }

               }  else if (request.getDocumentType().equals("p60")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       P60 p60_oliver = P60.builder()
                               .full_name("Oliver James Smith")
                               .national_insurance_number("AB123456C")        // UK format: 2 letters + 6 digits + 1 letter
                               .tax_year_end("05/04/2024")                    // Standard UK tax year end
                               .employer_name("Barclays Bank PLC")
                               .total_pay("£85,000.00")
                               .tax_paid("£19,300.00")
                               .build();

                       p60Repository.save(p60_oliver);
                   }
                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       P60 p60_emily = P60.builder()
                               .full_name("Emily Charlotte Taylor")
                               .national_insurance_number("CD654321A")
                               .tax_year_end("05/04/2024")
                               .employer_name("British Telecom Ltd")
                               .total_pay("£48,500.00")
                               .tax_paid("£9,200.00")
                               .build();
                       p60Repository.save(p60_emily);
                   }

               }  else if (request.getDocumentType().equals("bank")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       BankStatement statement1 = BankStatement.builder()
                               .full_name("Oliver James Smith")
                               .account_holder_name("Oliver James Smith")
                               .account_number("40392384")
                               .sort_code("20-45-67")
                               .statement_start_Date("01/06/2024")
                               .statement_end_date("30/06/2024")
                               .transaction_summary("Salary credited, 3 utility debits, 1 rent payment")
                               .build();
                       bankStatementRepository.save(statement1);
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       BankStatement statement2 = BankStatement.builder()
                               .full_name("Emily Charlotte Taylor")
                               .account_holder_name("Emily Charlotte Taylor")
                               .account_number("11235813")
                               .sort_code("40-12-34")
                               .statement_start_Date("01/06/2024")
                               .statement_end_date("30/06/2024")
                               .transaction_summary("Freelance deposit, groceries, travel card recharge")
                               .build();
                       bankStatementRepository.save(statement2);
                   }


               }  else if (request.getDocumentType().equals("property")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       PropertyData property1 = PropertyData.builder()
                               .owner_name("Oliver James Smith")
                               .owner_address("10 Downing Street, Westminster, London, SW1A 2AA")
                               .property_type("Detached")
                               .valuation_amount("£850,000")
                               .purchase_date("15/04/2017")
                               .property_id_reference("UKPROP10098723")
                               .build();
                       propertyDataRepository.save(property1);
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       PropertyData property2 = PropertyData.builder()
                               .owner_name("Emily Charlotte Taylor")
                               .owner_address("25 Deansgate, Manchester, M3 4EN")
                               .property_type("Flat")
                               .valuation_amount("£320,000")
                               .purchase_date("22/08/2019")
                               .property_id_reference("UKPROP10123456")
                               .build();
                       propertyDataRepository.save(property2);
                   }

               } else if (request.getDocumentType().equals("utility")) {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       UtilityBill bill_oliver = UtilityBill.builder()
                               .full_name("Oliver James Smith")
                               .address("10 Downing Street, Westminster, London, SW1A 2AA")
                               .billing_date("01/07/2024")
                               .provider_name("British Gas")
                               .account_number("BG202407015678")    // Example: provider + date + random
                               .bill_amount("£95.60")
                               .build();
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       UtilityBill bill_emily = UtilityBill.builder()
                               .full_name("Emily Charlotte Taylor")
                               .address("25 Deansgate, Manchester, M3 4EN")
                               .billing_date("28/06/2024")
                               .provider_name("Thames Water")
                               .account_number("TW202406289001")
                               .bill_amount("£48.75")
                               .build();
                   }


               } else {

                   if ("oliver.james@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       SalarySlip salary_oliver = SalarySlip.builder()
                               .full_name("Oliver James Smith")
                               .pay_period_start("01/06/2024")
                               .pay_period_end("30/06/2024")
                               .net_pay("£5,250.00")
                               .gross_pay("£7,000.00")
                               .national_insurance_number("AB123456C")
                               .employer_name("Barclays Bank PLC")
                               .build();

                       salarySlipRepository.save(salary_oliver);
                   }

                   if ("emily.taylor@gmail.com".equalsIgnoreCase(request.getUserName())) {
                       SalarySlip salary_emily = SalarySlip.builder()
                               .full_name("Emily Charlotte Taylor")
                               .pay_period_start("01/06/2024")
                               .pay_period_end("30/06/2024")
                               .net_pay("£3,800.00")
                               .gross_pay("£5,200.00")
                               .national_insurance_number("CD654321A")
                               .employer_name("British Telecom Ltd")
                               .build();

                       salarySlipRepository.save(salary_emily);
                   }
               }
            }
        }
        return uploadResponse;
    }

    private boolean uploadFileToGCS(MultipartFile file, String applicationId, String documentType) throws IOException {
        log.info("Uploading GCS FIle to Bucket");
        String objectName = applicationId+ "_" + documentType +"_" +file.getOriginalFilename();// You can customize this
        log.info("File Name {}", objectName);
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();

        // Prepare BlobInfo
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(contentType)
                .build();

        log.info("Blob informations set");

        // Upload to GCS
        Blob blob = storage.create(blobInfo, fileContent);
        String fileUrl = String.format("gs://%s/%s", bucketName, objectName);

        log.info("file url {}", fileUrl);

        // Optional: Validate further
        if (blob != null && blob.exists()) {
            return true; // Success
        } else {
            return false; // Something went wrong
        }
    }

    public FinalResponse mortgageSubmit(String userId) throws JsonProcessingException {
       UserReq userReq = userReqRepository.findByUserId(userId);
//       userService.formAiRequest(userReq);
//       if (userReq.getUserName().equals())
       return null;
    }
}
