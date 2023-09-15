package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Application constant class
 */
@Slf4j(topic = "app const class")
public final class AppConst {
    /**
     * return current server time in given format (yyyy-MM-dd HH:mm:ss)
     * @return DateTime
     */
    @NonNull
    public static String getCurrentTime() {
        log.info("app const: getCurrentTime");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.debug("const, getCurrentTime: {}",now.format(formatter));
        return now.format(formatter);
    }
}
