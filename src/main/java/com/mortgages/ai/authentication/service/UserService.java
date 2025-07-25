package com.mortgages.ai.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mortgages.ai.authentication.exception.UserAlreadyExistsException;
import com.mortgages.ai.authentication.exception.BadCredentialsException;
import com.mortgages.ai.authentication.repository.UserReqRepository;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.response.*;
import com.mortgages.ai.mortgageservices.feigns.AIAgentClient;
import com.mortgages.ai.mortgageservices.request.AgenticAipRequest;
import com.mortgages.ai.mortgageservices.response.AgenticAiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    private UserReqRepository userReqRepository;

    private SecureRandom secureRandom;

    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AIAgentClient aiAgentClient;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AiParser aiParser;


    public UserService(UserReqRepository userReqRepository, SecureRandom secureRandom) {
        this.secureRandom =secureRandom;
        this.userReqRepository = userReqRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12, secureRandom);
    }
    public AgenticAipResponse registerUser(UserReq userReq) throws JsonProcessingException {

        UserReq existingUser = userReqRepository.findByUserName(userReq.getUserName());

        if(!ObjectUtils.isEmpty(existingUser)) {
            throw new UserAlreadyExistsException("4009", "Email or Phone already exists");
        }

        UUID uuid = UUID.randomUUID();
        userReq.setUserId((uuid.toString()).replace("-", "").substring(0, 15));
        userReq.setPassword(bCryptPasswordEncoder.encode(userReq.getPassword()));
        userReq.setCreatedAt(new Date());

        System.out.println(userReq);
        UserReq savedUser = userReqRepository.save(userReq);
        savedUser.setPassword(null);

        String aiRequest = null;

        if(savedUser.getUserName().equals("oliver.james@gmail.com")) {
            aiRequest = formAiRequestForOliver(savedUser);
        } else {
            aiRequest = formAiRequestForEmily(savedUser);
        }


        AgenticAipRequest aipRequest =
                AgenticAipRequest
                        .builder()
                        .user_input(aiRequest)
                        .build();

        AipResponse response = aiAgentClient.aip(aipRequest);

        AiDecision aiDecision = AiParser.parseResponse(response);
        System.out.println(aiDecision);

        AgenticAipResponse agenticAipResponse = buildAgenticAipResponse(savedUser, aiDecision);

        return agenticAipResponse;
    }

    private AgenticAipResponse buildAgenticAipResponse(UserReq userReq, AiDecision aiDecision) {
       return  AgenticAipResponse.builder()
////                 From AiDecision
                .status(aiDecision != null ? aiDecision.getStatus() : null)
                .reason(aiDecision != null ? aiDecision.getReason() : null)
                .roi(aiDecision != null ? aiDecision.getRoi() : null)


//               .status("Approved")
//               .reason("As a first-time buyer with reliable income and no outstanding debts, the applicant qualifies under our standard eligibility criteria.")
//               .roi("5.6")

//                 From UserReq
                .userId(userReq.getUserId())
                .userName(userReq.getUserName())
                .firstName(userReq.getFirstName())
                .lastName(userReq.getLastName())
                .password(userReq.getPassword())
                .createdAt(userReq.getCreatedAt())
                .mobile(userReq.getMobile())
                .applicants(userReq.getApplicants())
                .deposit(userReq.getDeposit())
                .depositPercentage(userReq.getDepositPercentage())
                .depositStatus(userReq.getDepositStatus())
                .email(userReq.getEmail())
                .firstHome(userReq.isFirstHome())
                .journey(userReq.getJourney())
                .ltv(userReq.getLtv())
                .maritalStatus(userReq.getMaritalStatus())
                .nameChanged(userReq.isNameChanged())
                .nationality(userReq.getNationality())
                .option(userReq.getOption())
                .propertyValue(userReq.getPropertyValue())
                .title(userReq.getTitle())
                .totalLoan(userReq.getTotalLoan())
                .year(userReq.getYear())
                .month(userReq.getMonth())
                .day(userReq.getDay())

                .build();
    }

    public String formAiRequestForOliver(UserReq savedUser) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK); // "£"

        // --------- mortgage_needs ------------
        Map<String, Object> mortgageNeeds = new HashMap<>();
        mortgageNeeds.put("property_value", savedUser.getPropertyValue());
        mortgageNeeds.put("deposit", savedUser.getDeposit());
        mortgageNeeds.put("deposit_percentage", savedUser.getDeposit());
        mortgageNeeds.put("loan_to_value", savedUser.getLtv());
        mortgageNeeds.put("total_borrowing_amount", savedUser.getTotalLoan());
        mortgageNeeds.put("who_is_applying", formatApplicant(savedUser.getApplicants()));

        // --------- about_you.personal_details ------------
        Map<String, Object> personalDetails = new HashMap<>();
        personalDetails.put("title", savedUser.getTitle());
        personalDetails.put("first_name", savedUser.getFirstName());
        personalDetails.put("last_name", savedUser.getLastName());
        personalDetails.put("name_change", savedUser.isNameChanged() ? "Yes" : "No");
        personalDetails.put("date_of_birth", "15/08/1990"); // format as "dd/MM/yyyy" if needed
        personalDetails.put("marital_status", savedUser.getMaritalStatus());
        personalDetails.put("nationality", savedUser.getNationality());
        personalDetails.put("gender", "Male");

        // --------- about_you.address_history ------------
        Map<String, Object> addressHistory = new HashMap<>();
        addressHistory.put("current_address", "10 Downing Street, Westminster, London, SW1A 2AA");
        addressHistory.put("residency_status", "Family / friends");

        // --------- about_you.contact_details ------------
        Map<String, Object> contactDetails = new HashMap<>();
        contactDetails.put("mobile_number", savedUser.getMobile());
        contactDetails.put("email_address", savedUser.getEmail());

        // --------- about_you (combined) ------------
        Map<String, Object> aboutYou = new HashMap<>();
        aboutYou.put("personal_details", personalDetails);
        aboutYou.put("address_history", addressHistory);
        aboutYou.put("contact_details", contactDetails);

        // --------- your_income ------------
        Map<String, Object> income = new HashMap<>();
        income.put("number_of_jobs", "1");
        income.put("employment_status", "Employed");
        income.put("occupation", "Professional");
        income.put("contract_type", "Permanent");
        income.put("company_name", "Barclays Bank PLC");
        income.put("start_date", "06/2020"); // "MM/yyyy"
        income.put("basic_yearly_income", 100000);
        income.put("additional_income", 0);
        income.put("expected_retirement_age", "60");

        // --------- your_outgoings ------------
        Map<String, Object> outgoings = new HashMap<>();
        outgoings.put("number_of_child_dependents", 0);
        outgoings.put("number_of_adult_dependents", 0);
        outgoings.put("is_property_flat_or_leasehold_house", "no");

        // --------- final root ------------
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("mortgage_needs", mortgageNeeds);
        root.put("about_you", aboutYou);
        root.put("your_income", income);
        root.put("your_outgoings", outgoings);

        // --------- wrap in "user_input" string ------------
        String json = mapper.writeValueAsString(root);
        return json;
    }

    public String formAiRequestForEmily(UserReq savedUser) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK); // "£"

        // --------- mortgage_needs ------------
        Map<String, Object> mortgageNeeds = new HashMap<>();
        mortgageNeeds.put("property_value", savedUser.getPropertyValue());
        mortgageNeeds.put("deposit", currencyFormat.format(savedUser.getDeposit()));
        mortgageNeeds.put("deposit_percentage", savedUser.getDepositPercentage() + "%");
        mortgageNeeds.put("loan_to_value", savedUser.getLtv() + "%");
        mortgageNeeds.put("total_borrowing_amount", savedUser.getTotalLoan());
        mortgageNeeds.put("who_is_applying", formatApplicant(savedUser.getApplicants()));

        // --------- about_you.personal_details ------------
        Map<String, Object> personalDetails = new HashMap<>();
        personalDetails.put("title", savedUser.getTitle());
        personalDetails.put("first_name", savedUser.getFirstName());
        personalDetails.put("last_name", savedUser.getLastName());
        personalDetails.put("name_change", savedUser.isNameChanged() ? "Yes" : "No");
        personalDetails.put("date_of_birth", "29/08/1995"); // format as "dd/MM/yyyy" if needed
        personalDetails.put("marital_status", savedUser.getMaritalStatus());
        personalDetails.put("nationality", savedUser.getNationality());
        personalDetails.put("gender", "Male");

        // --------- about_you.address_history ------------
        Map<String, Object> addressHistory = new HashMap<>();
        addressHistory.put("current_address", "1, wellington place, leeds, West Yorkshire, LS1 4AP");
        addressHistory.put("residency_status", "Family / friends");

        // --------- about_you.contact_details ------------
        Map<String, Object> contactDetails = new HashMap<>();
        contactDetails.put("mobile_number", savedUser.getMobile());
        contactDetails.put("email_address", savedUser.getEmail());

        // --------- about_you (combined) ------------
        Map<String, Object> aboutYou = new HashMap<>();
        aboutYou.put("personal_details", personalDetails);
        aboutYou.put("address_history", addressHistory);
        aboutYou.put("contact_details", contactDetails);

        // --------- your_income ------------
        Map<String, Object> income = new HashMap<>();
        income.put("number_of_jobs", "1");
        income.put("employment_status", "Employed");
        income.put("occupation", "Professional");
        income.put("contract_type", "permanent");
        income.put("company_name", "Lloyds Banking Group");
        income.put("start_date", "29/2025"); // "MM/yyyy"
        income.put("basic_yearly_income", 40000);
        income.put("additional_income", 0);
        income.put("expected_retirement_age", "60");

        // --------- your_outgoings ------------
        Map<String, Object> outgoings = new HashMap<>();
        outgoings.put("number_of_child_dependents", 0);
        outgoings.put("number_of_adult_dependents", 0);
        outgoings.put("is_property_flat_or_leasehold_house", "no");

        // --------- final root ------------
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("mortgage_needs", mortgageNeeds);
        root.put("about_you", aboutYou);
        root.put("your_income", income);
        root.put("your_outgoings", outgoings);

        // --------- wrap in "user_input" string ------------
        String json = mapper.writeValueAsString(root);
        return json;
    }

    private static String formatApplicant(String value) {
        if ("justMe".equalsIgnoreCase(value)) return "Just me";
        if ("meAndSomeoneElse".equalsIgnoreCase(value)) return "Me and someone else";
        return value;
    }

    public UserReq getUserDetails(String userId) {
        return userReqRepository.findByUserId(userId);
    }

    public UserReq getUserDetailsByUserName(String userName) {
        return userReqRepository.findByUserName(userName);
    }

    public AccessToken validateUser(UserReq userReq) {
        AccessToken accessToken = null;
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userReq.getUserName(), userReq.getPassword()));
            if(authentication.isAuthenticated()) {
                UserReq authenticatedUser = userReqRepository.findByUserName(userReq.getUserName());
                String token = jwtService.createToken(authenticatedUser);
                accessToken = AccessToken
                        .builder()
                        .acountId(authenticatedUser.getUserId())
                        .authToken(token)
                        .build();
            }
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("4001", "Invalid credentials");
        }
        return accessToken;
    }
}
