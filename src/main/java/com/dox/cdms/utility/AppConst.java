package com.dox.cdms.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Application constant class.
 */
@Slf4j(topic = "app const class")
public final class AppConst {
    /**
     * Returns the current server time in the given format (yyyy-MM-dd HH:mm:ss).
     *
     * @return A formatted string representing the current DateTime.
     */
    @NonNull
    public static String getCurrentTime() {
        try {
            log.info("AppConst: getCurrentTime");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.debug("AppConst, getCurrentTime: {}", now.format(formatter));
            return now.format(formatter);
        } catch (Exception e) {
            log.error("AppConst: getCurrentTime - Error: {}", e.getMessage());
            throw e; // Rethrow the exception or handle it based on your application's error handling strategy.
        }
    }
    /**
     * Array of supported data types.
     */
    public static final String[] supportedDataTypes = {"string", "boolean", "float", "double", "int", "integer"};
}
