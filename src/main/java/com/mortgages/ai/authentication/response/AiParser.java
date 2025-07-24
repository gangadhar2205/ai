package com.mortgages.ai.authentication.response;

import org.springframework.stereotype.Component;

@Component
public class AiParser {
    public static AiDecision parseResponse(AipResponse responseObj) {
        String content = responseObj.getResponse();
        String status = extractValue(content, "Status: ", "\\n");
        String reason = extractValue(content, "Reason: ", "\\n");
        String roi = extractValue(content, "ROI: ", "%") + "%";

        return new AiDecision(status, reason, roi);
    }

    private static String extractValue(String source, String start, String endDelimiter) {
        int startIndex = source.indexOf(start);
        if (startIndex == -1) return null;
        startIndex += start.length();

        int endIndex = source.indexOf(endDelimiter, startIndex);
        if (endIndex == -1) return source.substring(startIndex).trim();

        return source.substring(startIndex, endIndex).trim();
    }
}