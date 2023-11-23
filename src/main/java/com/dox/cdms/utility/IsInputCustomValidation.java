package com.dox.cdms.utility;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import static com.dox.cdms.utility.AppConst.supportedDataTypes;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * validation class
 */
@Slf4j(topic = "IsInputCustomValidation")
public final class IsInputCustomValidation {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static boolean isValidAlphabeticWithNumberAndHyphen(String input) {
        log.info("isValidAlphabeticWithNumberAndHyphen: {}",input);
        if (input == null || input.isEmpty()) {
            log.info("isValidAlphabeticWithNumberAndHyphen: {} Null or empty",input);
            return true; // Null or empty input.
        }
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') {
                log.info("isValidAlphabeticWithNumberAndHyphen: {} Found a character that is not a letter, digit, or hyphen",input);
                return true; // Found a character that is not a letter, digit, or hyphen.
            }
        }
        log.info("isValidAlphabeticWithNumberAndHyphen: {} All characters in the input are valid",input);
        return false; // All characters in the input are valid.
    }

    public static boolean isValidAlphabeticWithNumberAndUnderScore(String input) {
        log.info("isValidAlphabeticWithNumberAndUnderScore: {}",input);
        if (input == null || input.isEmpty()) {
            log.info("isValidAlphabeticWithNumberAndUnderScore: {} Null or empty.",input);
            return true; // Null or empty input.
        }
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                log.info("isValidAlphabeticWithNumberAndUnderScore: {} Found a character that is not a letter, digit, or underscore.",input);
                return true; // Found a character that is not a letter, digit, or underscore.
            }
        }
        log.info("isValidAlphabeticWithNumberAndUnderScore: {} All characters in the input are valid.",input);
        return false; // All characters in the input are valid.
    }


    public static boolean isValidDataType(String input) {
        log.info("isValidDataType: {}",input);
        // Define the values to match
            // Check if the input matches any of the valid values
        for (String validValue : supportedDataTypes) {
            if (input.equals(validValue)) {
                log.info("isValidDataType: {} All characters in the input are valid.",input);
                return false;
            }
        }
        // If no match is found, return false
        log.info("isValidDataType: {} All characters are not valid.",input);
        return true;
    }

    public static boolean isValidInteger(String input) {
        log.info("isValidInteger: {}",input);
        try {
            // Attempt to parse the input as an integer
            Integer.parseInt(input);
            log.info("isValidInteger: {} All characters in the input are valid.",input);
            return false; // If parsing succeeds, it's a valid integer
        } catch (NumberFormatException e) {
            log.info("isValidInteger: {} All characters are not valid.",input);
            return true; // If parsing fails, it's not a valid integer
        }
    }

    public static boolean isValidFloat(String input) {
        log.info("isValidFloat: {}",input);
        try {
            // Attempt to parse the input as an integer
            Float.parseFloat(input);
            log.info("isValidFloat: {} All characters in the input are valid.",input);
            return false; // If parsing succeeds, it's a valid integer
        } catch (NumberFormatException e) {
            log.info("isValidFloat: {} All characters are not valid.",input);
            return true; // If parsing fails, it's not a valid integer
        }
    }

    public static boolean isValidDouble(String input) {
        log.info("isValidDouble: {}",input);
        try {
            // Attempt to parse the input as an integer
            Double.parseDouble(input);
            log.info("isValidDouble: {} All characters in the input are valid.",input);
            return false; // If parsing succeeds, it's a valid integer
        } catch (NumberFormatException e) {
            log.info("isValidDouble: {} All characters are not valid.",input);
            return true; // If parsing fails, it's not a valid integer
        }
    }

    public static boolean isValidJson(String input) {
        try {
            // Attempt to parse the input as JSON
            JsonNode jsonNode = objectMapper.readTree(input);
            // If parsing succeeds, it's a valid JSON
            return true;
        } catch (Exception e) {
            // If parsing fails, it's not a valid JSON
            return false;
        }
    }
    public static boolean isValidDescription(String description) {
        log.info("isValidDescription: {}",description);
        String regex = "^[a-zA-Z0-9\\s.,!?\\-()\"']+";
        return !description.matches(regex);
    }
    public static boolean isNull(String description) {
        log.info("isNullDescription: {}",description);
        // Check if the description is not null and has a minimum length (e.g., 8 characters)
        if (description == null || description.length() < 10) {
            log.info("isNullDescription: {} Check if the description is not null and has a minimum length (e.g., 8 characters)",description);
            return true;
        }
        else return false;
    }
}
