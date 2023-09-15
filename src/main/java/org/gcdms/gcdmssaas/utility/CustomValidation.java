package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Custom validation class
 */
@Slf4j(topic = "const class")
public final class CustomValidation {

    /**
     * @apiNote validate the input is Alphabetical or not
     * @return true if Alphabetical
     */
    public static boolean isAlphabetical( String input) {
        log.debug("const isAlphabetical input string: {}, {}",input,input.matches("[a-zA-Z]+"));
        return input.matches("[a-zA-Z]+");
    }

    /**
     * @apiNote check for input isNonZeroPositiveNumber or not
     */
    public static boolean isNonZeroPositiveNumber(String input) {
        try {
            int number = Integer.parseInt(input);
            log.debug("const isNonZeroPositiveNumber input string: {}, {}",input,number > 0);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @apiNote check if input isPositiveNumber or not
     */
    public static boolean isPositiveNumber(String input) {
        try {
            int number = Integer.parseInt(input);
            log.debug("const isPositiveNumber input string: {}, {}",input,number > 0);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * @apiNote validate input valid email or not
     */
    public static boolean isValidEmail(@NotNull String input) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        log.debug("const isValidEmail input string: {}",input.matches(emailRegex));
        return input.matches(emailRegex);
    }

    /**
     * @apiNote validate input isStringNullOrEmptyOrBlank or not
     */
    public static boolean isStringNullOrEmptyOrBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * @apiNote validate input isArrayNullOrEmpty or not
     */
    public static boolean isArrayNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @apiNote validate input isArrayOfStringsAlphabetic or not
     */
    public static boolean isArrayOfStringsAlphabetic( String @NotNull [] array) {
        for (String str : array)
            if (!isAlphabeticWithDot(str)) {
                return false;
            }
        return true;
    }

    /**
     * @apiNote validate input isAlphabeticWithDot or not
     */
    public static boolean isAlphabeticWithDot(@NotNull String str) {
        return str.matches("[a-zA-Z].+");
    }

    /**
     * @apiNote validate input isAlphabeticWithSpace or not
     */
    public static boolean isAlphabeticWithSpace(@NotNull String str) {
        return str.matches("[a-zA-Z ]+");
    }

    /**
     * @apiNote validate input isValidAge or not
     */
    public static boolean isValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age >= 0 && age <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @apiNote validate input isValidAlphaNumeric or not
     */
    public static boolean isValidAlphaNumeric(@NotNull String str) {
        return str.matches("[a-zA-Z0-9\\-]+");
    }

}
