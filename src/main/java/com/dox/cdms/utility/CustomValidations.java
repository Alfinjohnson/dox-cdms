package com.dox.cdms.utility;

import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.payload.request.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.dox.cdms.utility.AppConst.supportedDataTypes;
import static com.dox.cdms.utility.IsInputCustomValidation.*;


/**
 * Param custom validation class
 */
@Slf4j(topic = "CustomValidations")
public final class CustomValidations {

    /**
     * Validates the input for creating a configuration.
     *
     * @param createConfigurationRequest DTO containing configuration creation information.
     * @throws ResponseStatusException if validation fails.
     */
    public static void createConfigurationValidationMethod(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfigurationValidationMethod: {}", createConfigurationRequest);

        // Validate configuration name
        if (isValidAlphabeticWithNumberAndHyphen(createConfigurationRequest.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");

        // Validate configuration description
        if (isValidDescription(createConfigurationRequest.getDescription()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.");

        // Validate each subscriber in the configuration
        List<CreateConfigurationDataModel> subscribersList = createConfigurationRequest.getSubscribers();
        for (CreateConfigurationDataModel subscriber : subscribersList) {
            String name = subscriber.getName();
            String type = subscriber.getType().toLowerCase(Locale.ROOT);
            Object value = subscriber.getValue();

            // Perform operations on the subscriber data
            log.info("Subscriber Name: {}", name);
            log.info("Subscriber Type: {}", type);
            log.info("Subscriber value: {}", value);

            // Validate subscriber name
            if (isValidAlphabeticWithNumberAndHyphen(name))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber name: '" + name + "'. Input can only contain alphabetic characters, numbers, and underscore.");

            // Validate subscriber data type
            if (isValidDataType(type))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber type: '" + type + "'. Types can only contain " + Arrays.toString(supportedDataTypes));

            // Validate boolean type subscriber value
            if (type.equals("boolean") && !value.equals(true) && !value.equals(false)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber boolean value: " + value);
            }

            // Validate integer type subscriber value
            if ((type.equals("int") || type.equals("integer")) && isValidInteger(value.toString()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber value: " + value);

            // Validate string type subscriber value
            if (type.equals("string") && isValidAlphabeticWithNumberAndHyphen(value.toString()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber value: " + value);
            // Validate string type subscriber value
            if (type.equals("float") && isValidFloat(value.toString()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber value: " + value);
            // Validate string type subscriber value
            if (type.equals("double") && isValidDouble(value.toString()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber value: " + value);
            // Validate string type subscriber value
            if (type.equals("json") && isValidJson(value.toString()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber value: " + value);
        }
    }
    /**
     * Validates the input for updating a configuration.
     *
     * @param updateConfigurationRequest DTO containing configuration update information.
     * @throws ResponseStatusException if validation fails.
     */
    public static void updateConfigurationValidationMethod(UpdateConfigurationRequest updateConfigurationRequest) {
        log.info("updateConfigurationRequest: {}", updateConfigurationRequest);

        // Validate configuration name
        if (isValidAlphabeticWithNumberAndHyphen(updateConfigurationRequest.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + updateConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");

        // Check if description is not empty and validate it
        if (isNull(updateConfigurationRequest.getDescription())) {
            log.info("Description not is empty");
            if (isValidDescription(updateConfigurationRequest.getDescription()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input: '" + updateConfigurationRequest.getDescription() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.");
        }
    }

    /**
     * Validates the input for deleting a configuration.
     *
     * @param deleteConfigurationRequest DTO containing configuration deletion information.
     * @throws ResponseStatusException if validation fails.
     */
    public static void deleteConfigurationValidationMethod(@NotNull DeleteConfigurationRequest deleteConfigurationRequest) {
        // Validate configuration name
        if (isValidAlphabeticWithNumberAndHyphen(deleteConfigurationRequest.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + deleteConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");
    }

    /**
     * Validates the input for retrieving the full configuration.
     *
     * @param configName Name of the configuration.
     * @throws ResponseStatusException if validation fails.
     */
    public static void getFullConfigurationValidationMethod(String configName) {
        // Validate configuration name
        if (isValidAlphabeticWithNumberAndHyphen(configName))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + configName + "'. Input can only contain alphabetic characters, numbers, and hyphens.");
    }

    /**
     * Validates the input for updating a subscriber.
     *
     * @param updateSubscriberRequest DTO containing subscriber update information.
     * @throws ResponseStatusException if validation fails.
     */
    public static void updateSubscriberValidationMethod(@NotNull UpdateSubscriberRequest updateSubscriberRequest) {
        // Validate subscriber name
        if (isValidAlphabeticWithNumberAndHyphen(updateSubscriberRequest.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + updateSubscriberRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");

        // Check if description is not empty and validate it
        if (isNull(updateSubscriberRequest.getDescription())) {
            log.info("Description not is empty");
            if (isValidDescription(updateSubscriberRequest.getDescription()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input: '" + updateSubscriberRequest.getDescription() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.");
        }
    }

    /**
     * Validates the input for retrieving a configuration.
     *
     * @param getConfigurationRequest DTO containing configuration retrieval information.
     * @throws ResponseStatusException if validation fails.
     */
    public static void getConfigurationValidationMethod(@NotNull GetConfigurationRequest getConfigurationRequest) {
        // Validate configuration name
        if (isValidAlphabeticWithNumberAndHyphen(getConfigurationRequest.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + getConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");

        // Validate subscriber name
        if (isValidAlphabeticWithNumberAndHyphen(getConfigurationRequest.getSubscriber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + getConfigurationRequest.getSubscriber() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");
    }
}
