package org.gcdms.gcdmssaas.utility;

import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreateConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
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
        Throwable ex = null;
        if (!isValidAlphabeticWithNumberAndHyphen(createConfigurationRequest.getName()))
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Input can only contain alphabetic characters, numbers, and hyphens.", ex);

        if (!isValidDescription(createConfigurationRequest.getDescription()))
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "Invalid character found in input: '" + createConfigurationRequest.getName() + "'. Check if the description contains only alphanumeric characters, spaces, and common punctuation marks.", ex);

        List<CreateConfigurationDataModel> subscribersList = createConfigurationRequest.getSubscribers();
        for (CreateConfigurationDataModel subscriber : subscribersList) {
            String name = subscriber.getName();
            String type = subscriber.getType();
            Object value = subscriber.getValue();
            // Perform operations on the subscriber data
            log.info("Subscriber Name: {}" , name);
            log.info("Subscriber Type: {}" , type);
            log.info("Subscriber value: {}", value);
            if (!isValidAlphabeticWithNumberAndUnderScore(name))
                throw new CustomException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber name: '" + name + "'. Input can only contain alphabetic characters, numbers, and underscore.", ex);
            if (!isValidDataType(type))
                throw new CustomException(HttpStatus.BAD_REQUEST,
                        "Invalid character found in input subscriber type: '" + type + "'. types can only contain "+ Arrays.toString(supportedDataTypes), ex);

            if (type.equals("boolean")){
                if (!value.equals(true) && !value.equals(false)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber boolean value: " + value, ex);
                }
            }
            if (type.equals("int") || type.equals("integer")){
                if (!isValidInteger(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                             "Invalid character found in input subscriber value: " + value, ex);
            }
            if (type.equals("string")){
                if (!isValidAlphabeticWithNumberAndUnderScore(value.toString()))
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Invalid character found in input subscriber value: " + value, ex);
            }

        }
    }

}
