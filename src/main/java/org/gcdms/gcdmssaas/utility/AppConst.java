package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @apiNote application constant class
 */
@Slf4j(topic = "const class")
public final class AppConst {
    /**
     * @apiNote return current server time in given format (yyyy-MM-dd HH:mm:ss)
     * @return DateTime
     */
    @NonNull
    public static String getCurrentTime() {
        log.info("inside const getCurrentTime");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.debug("const, getCurrentTime: {}",now.format(formatter));
        return now.format(formatter);
    }

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
    public static boolean isValidEmail( String input) {
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
    public static boolean isArrayOfStringsAlphabetic( String[] array) {
            for (String str : array)
                if (!isAlphabeticWithDot(str)) {
                    return false;
                }
            return true;
    }

    /**
     * @apiNote validate input isAlphabeticWithDot or not
     */
    public static boolean isAlphabeticWithDot( String str) {
        return str.matches("[a-zA-Z].+");
    }

    /**
     * @apiNote validate input isAlphabeticWithSpace or not
     */
    public static boolean isAlphabeticWithSpace( String str) {
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
    public static boolean isValidAlphaNumeric( String str) {
        return str.matches("[a-zA-Z0-9\\-]+");
    }

}
