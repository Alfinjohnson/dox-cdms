package com.dox.cdms.service.imp;

import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.request.UpdateSubscriberRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.payload.response.GetConfigurationResponse;
import com.dox.cdms.payload.response.GetFullConfigurationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Component
public class ServiceImp {
    private static final Logger logger = LoggerFactory.getLogger(ServiceImp.class);


    /**
     * Maps a subscriber entity to a configuration data model.
     *
     * @param subscriberEntity The subscriber entity to be mapped.
     * @return SubscribersDataModel object.
     */
    public static @NotNull SubscribersDataModel mapSubscriberToConfigurationDataModel(
            @NotNull SubscriberEntity subscriberEntity) {
        SubscribersDataModel subscribersDataModel = new SubscribersDataModel();
        subscribersDataModel.setId(subscriberEntity.getId());
        subscribersDataModel.setName(subscriberEntity.getName());
        subscribersDataModel.setDescription(subscriberEntity.getDescription());
        subscribersDataModel.setEnabled(subscriberEntity.getEnabled());
        subscribersDataModel.setDataType(subscriberEntity.getDataType());
        subscribersDataModel.setValue(ServiceImp.getDTValueMethod(subscriberEntity));
        return subscribersDataModel;
    }

    /**
     * Finds a subscriber by its ID.
     *
     * @param subscriberEntity The subscriber entity to find.
     * @return SubscribersDataModel object representing the found subscriber.
     */
    public @NotNull SubscribersDataModel findSubscribersById(@NotNull SubscriberEntity subscriberEntity) {
        SubscribersDataModel subscribersDataModel = new SubscribersDataModel();
        subscribersDataModel.setId(subscriberEntity.getId());
        subscribersDataModel.setName(subscriberEntity.getName());
        subscribersDataModel.setDescription(subscriberEntity.getDescription());
        subscribersDataModel.setEnabled(subscriberEntity.getEnabled());
        subscribersDataModel.setDataType(subscriberEntity.getDataType());
        subscribersDataModel.setValue(ServiceImp.getDTValueMethod(subscriberEntity));
        return subscribersDataModel;
    }

    /**
     * Builds a full configuration response.
     *
     * @param configurationEntity The configuration entity.
     * @param subscribersList     List of subscribers data models.
     * @return GetFullConfigurationResponse object.
     */
    @NotNull
    public static GetFullConfigurationResponse buildGetFullConfigurationResponse(
            @NotNull ConfigurationEntity configurationEntity, List<SubscribersDataModel> subscribersList) {
        GetFullConfigurationResponse getFullConfigurationResponse = new GetFullConfigurationResponse();
        getFullConfigurationResponse.setId(configurationEntity.getId());
        getFullConfigurationResponse.setName(configurationEntity.getName());
        getFullConfigurationResponse.setDescription(configurationEntity.getDescription());
        getFullConfigurationResponse.setSubscribers(subscribersList);
        getFullConfigurationResponse.setCreatedDateTime(configurationEntity.getCreatedDateTime());
        getFullConfigurationResponse.setModifiedDateTime(configurationEntity.getModifiedDateTime());
        return getFullConfigurationResponse;
    }
    /**
     * Updates a subscriber entity based on the information provided in the update request.
     *
     * @param updateSubscriberRequest The update request containing the new information.
     * @param subscriberEntity        The original subscriber entity to be updated.
     * @param type                    The data type for the subscriber.
     * @return Updated SubscriberEntity object.
     */
    public static @NotNull SubscriberEntity getSubscriberEntity(
            @NotNull UpdateSubscriberRequest updateSubscriberRequest, SubscriberEntity subscriberEntity, String type) {
        if (!updateSubscriberRequest.getName().isEmpty() || !updateSubscriberRequest.getName().isBlank()) {
            subscriberEntity.setName(updateSubscriberRequest.getName());
        }
        if (!updateSubscriberRequest.getDescription().isEmpty() || !updateSubscriberRequest.getDescription().isBlank()) {
            subscriberEntity.setDescription(updateSubscriberRequest.getDescription());
        }
        if (!updateSubscriberRequest.getDataType().isEmpty() || !updateSubscriberRequest.getDataType().isBlank()) {
            subscriberEntity.setDataType(type);
        }
        subscriberEntity.setEnabled(updateSubscriberRequest.getEnabled());
        return subscriberEntity;
    }

    /**
     * Determines and sets the appropriate data type value in the subscriber entity.
     *
     * @param value            The value to be set.
     * @param type             The data type.
     * @param subscriberEntity The subscriber entity to be updated.
     */
    public static void dataDTDeterminer(Object value, @NotNull String type, SubscriberEntity subscriberEntity) {
        if (type.equals("boolean") || type.equals("Boolean")) subscriberEntity.setBoolean_dt((boolean) value);
        if (type.equals("integer") || type.equals("int")) subscriberEntity.setInteger_dt((Integer) value);
        if (type.equals("float_dt")) subscriberEntity.setFloat_dt((float) value);
        if (type.equals("double_dt")) subscriberEntity.setDouble_dt((double) value);
        if (type.equals("json_dt")) subscriberEntity.setJson_dt((String) value);
    }

    /**
     * Retrieves the data type value from the subscriber entity.
     *
     * @param subscriberEntity The subscriber entity from which to retrieve the value.
     * @return Object representing the data type value.
     */
    public static Object getDTValueMethod(@NotNull SubscriberEntity subscriberEntity) {
        logger.info("getDTValueMethod id: {}, name: {}", subscriberEntity.getId(), subscriberEntity.getName());
        final String type = subscriberEntity.getDataType();
        if (type.equals("boolean") || type.equals("Boolean")) return subscriberEntity.getBoolean_dt();
        if (type.equals("integer") || type.equals("int")) return subscriberEntity.getInteger_dt();
        if (type.equals("float_dt")) return subscriberEntity.getFloat_dt();
        if (type.equals("double_dt")) return subscriberEntity.getDouble_dt();
        if (type.equals("json_dt")) return subscriberEntity.getJson_dt();
        logger.info("Unable to get subscriber value id: {}, name: {}", subscriberEntity.getId(), subscriberEntity.getName());
        throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Unable to get subscriber value id: " + subscriberEntity.getId());
    }

    /**
     * Maps a configuration data model to a create configuration response.
     *
     * @param createdConfig           The configuration entity.
     * @param subscribersDataModelList List of subscribers data models.
     * @return CreateConfigurationResponse object.
     */
    public static @NotNull CreateConfigurationResponse mapConfigDataModelToCreateConfigResponse(
            @NotNull ConfigurationEntity createdConfig, List<SubscribersDataModel> subscribersDataModelList) {
        CreateConfigurationResponse createConfigurationResponse = new CreateConfigurationResponse();
        createConfigurationResponse.setId(createdConfig.getId());
        createConfigurationResponse.setName(createdConfig.getName());
        createConfigurationResponse.setDescription(createdConfig.getDescription());
        createConfigurationResponse.setEnabled(createdConfig.getEnabled());
        createConfigurationResponse.setSubscribers(subscribersDataModelList);
        createConfigurationResponse.setCreatedDateTime(createdConfig.getCreatedDateTime());
        return createConfigurationResponse;
    }

    // More methods...

    /**
     * Converts a JSON message to a GetConfigurationRequest object.
     *
     * @param message The JSON message.
     * @return GetConfigurationRequest object.
     */
    public static GetConfigurationRequest convertMessageToDTO(String message) {
        try {
            return new ObjectMapper().readValue(message, GetConfigurationRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message to GetConfigurationRequest", e);
        }
    }

    /**
     * Converts a GetConfigurationResponse object to a JSON message.
     *
     * @param message The GetConfigurationResponse object.
     * @return JSON message.
     */
    public static String convertMessageToDTO(GetConfigurationResponse message) {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message to String", e);
        }
    }
}
