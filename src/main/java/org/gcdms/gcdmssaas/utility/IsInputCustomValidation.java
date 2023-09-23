package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;

import static org.gcdms.gcdmssaas.utility.AppConst.supportedDataTypes;

/**
 * validation class
 */
@Slf4j(topic = "IsInputCustomValidation")
public final class IsInputCustomValidation {

    public static boolean isValidAlphabeticWithNumberAndHyphen(String input) {
        log.info("isValidAlphabeticWithNumberAndHyphen: {}",input);
        if (input == null || input.isEmpty()) {
            log.info("isValidAlphabeticWithNumberAndHyphen: {} Null or empty",input);
            return false; // Null or empty input.
        }
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') {
                log.info("isValidAlphabeticWithNumberAndHyphen: {} Found a character that is not a letter, digit, or hyphen",input);
                return false; // Found a character that is not a letter, digit, or hyphen.
            }
        }
        log.info("isValidAlphabeticWithNumberAndHyphen: {} All characters in the input are valid",input);
        return true; // All characters in the input are valid.
    }

    public static boolean isValidAlphabeticWithNumberAndUnderScore(String input) {
        log.info("isValidAlphabeticWithNumberAndUnderScore: {}",input);
        if (input == null || input.isEmpty()) {
            log.info("isValidAlphabeticWithNumberAndUnderScore: {} Null or empty.",input);
            return false; // Null or empty input.
        }
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                log.info("isValidAlphabeticWithNumberAndUnderScore: {} Found a character that is not a letter, digit, or underscore.",input);
                return false; // Found a character that is not a letter, digit, or underscore.
            }
        }
        log.info("isValidAlphabeticWithNumberAndUnderScore: {} All characters in the input are valid.",input);
        return true; // All characters in the input are valid.
    }


    public static boolean isValidDataType(String input) {
        log.info("isValidDataType: {}",input);
        // Define the values to match
            // Check if the input matches any of the valid values
        for (String validValue : supportedDataTypes) {
            if (input.equals(validValue)) {
                log.info("isValidDataType: {} All characters in the input are valid.",input);
                return true;
            }
        }
        // If no match is found, return false
        log.info("isValidDataType: {} All characters are not valid.",input);
        return false;
    }

    public static boolean isValidInteger(String input) {
        log.info("isValidInteger: {}",input);
        try {
            // Attempt to parse the input as an integer
            Integer.parseInt(input);
            log.info("isValidInteger: {} All characters in the input are valid.",input);
            return true; // If parsing succeeds, it's a valid integer
        } catch (NumberFormatException e) {
            log.info("isValidInteger: {} All characters are not valid.",input);
            return false; // If parsing fails, it's not a valid integer
        }
    }

    public static boolean isValidDescription(String description) {
        log.info("isValidDescription: {}",description);
        // Check if the description is not null and has a minimum length (e.g., 8 characters)
        if (description == null || description.length() < 10) {
            log.info("isValidDescription: {} Check if the description is not null and has a minimum length (e.g., 8 characters)",description);
            return false;
        }
        // Check if the description contains only alphanumeric characters, spaces, and common punctuation marks
        String regex = "^[a-zA-Z0-9\\s.,!?\\-()\"']+";
        return description.matches(regex);
    }

}
