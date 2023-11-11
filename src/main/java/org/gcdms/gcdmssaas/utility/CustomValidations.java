package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreateConfigurationDataModel;
import org.gcdms.gcdmssaas.model.UpdateConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.request.UpdateConfigurationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.gcdms.gcdmssaas.utility.AppConst.supportedDataTypes;
import static org.gcdms.gcdmssaas.utility.IsInputCustomValidation.*;


/**
 * Param custom validation class
 */
@Slf4j(topic = "CustomValidations")
public final class CustomValidations {

    public static void createConfigurationValidationMethod(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfigurationValidationMethod: {}",createConfigurationRequest);
        if (isValidAlphabeticWithNumberAndHyphen(createConfigurationRequest.getName()))
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");

        if (isValidDescription(createConfigurationRequest.getDescription()))
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.");

        List<CreateConfigurationDataModel> subscribersList = createConfigurationRequest.getSubscribers();
        for (CreateConfigurationDataModel subscriber : subscribersList) {
            String name = subscriber.getName();
            String type = subscriber.getType();
            Object value = subscriber.getValue();
            // Perform operations on the subscriber data
            log.info("Subscriber Name: {}" , name);
            log.info("Subscriber Type: {}" , type);
            log.info("Subscriber value: {}", value);
            if (isValidAlphabeticWithNumberAndUnderScore(name))
                throw new CustomException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber name: '" + name + "'. Input can only contain alphabetic characters, numbers, and underscore.");
            if (isValidDataType(type))
                throw new CustomException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber type: '" + type + "'. types can only contain "+ Arrays.toString(supportedDataTypes));

            if (type.equals("boolean")){
                if (!value.equals(true) && !value.equals(false)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber boolean value: " + value);
                }
            }
            if (type.equals("int") || type.equals("integer")){
                if (isValidInteger(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                             "Invalid character found in input subscriber value: " + value);
            }
            if (type.equals("string")){
                if (isValidAlphabeticWithNumberAndUnderScore(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber value: " + value);
            }

        }
    }

    public static void updateConfigurationValidationMethod(UpdateConfigurationRequest updateConfigurationRequest) {
        log.info("updateConfigurationRequest: {}",updateConfigurationRequest);
        if (isValidAlphabeticWithNumberAndHyphen(updateConfigurationRequest.getName()))
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + updateConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.");
        if (isNull(updateConfigurationRequest.getDescription())) {
            log.info("Description is empty");
            if (isValidDescription(updateConfigurationRequest.getDescription()))
                throw new CustomException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input: '" + updateConfigurationRequest.getDescription() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.");
        }
        List<UpdateConfigurationDataModel> subscribersList = updateConfigurationRequest.getSubscribers();
        for (UpdateConfigurationDataModel subscriber : subscribersList) {
            String name = subscriber.getName();
            String type = subscriber.getType();
            Object value = subscriber.getValue();
            // Perform operations on the subscriber data
            log.info("Subscriber Name: {}" , name);
            log.info("Subscriber Type: {}" , type);
            log.info("Subscriber value: {}", value);
            if (isNull(name)){
                log.info("name not empty");
                if (isValidAlphabeticWithNumberAndUnderScore(name))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber name: '" + name + "'. Input can only contain alphabetic characters, numbers, and underscore.");
            }
            if (isNull(type)){
                log.info("name not empty");
                if (isValidDataType(type))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber type: '" + type + "'. types can only contain "+ Arrays.toString(supportedDataTypes));

            }

            if (type.equals("boolean")){
                if (!value.equals(true) && !value.equals(false)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber boolean value: " + value);
                }
            }
            if (type.equals("int") || type.equals("integer")){
                if (isValidInteger(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber value: " + value);
            }
            if (type.equals("string")){
                if (isValidAlphabeticWithNumberAndUnderScore(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber value: " + value);
            }

        }
    }

}
