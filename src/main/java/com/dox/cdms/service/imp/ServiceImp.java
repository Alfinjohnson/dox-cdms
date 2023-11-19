package com.dox.cdms.service.imp;

import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.service.ConfigurationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Component
public class ServiceImp {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    public static Object getDTValueMethod(@NotNull SubscriberEntity subscriberEntity) {
        logger.info("getDTValueMethod id: {}, name: {}",subscriberEntity.getId(),subscriberEntity.getName());
        final String type = subscriberEntity.getDataType();
        if (type.equals("boolean")|| type.equals("Boolean")) return subscriberEntity.getBoolean_dt();
        if (type.equals("integer")|| type.equals("int")) return subscriberEntity.getInteger_dt();
        if (type.equals("float_dt")) return subscriberEntity.getFloat_dt();
        if (type.equals("double_dt")) return subscriberEntity.getDouble_dt();
        if (type.equals("json_dt")) return subscriberEntity.getJson_dt();
        logger.info("unable to get subscriber value id: {}, name: {}",subscriberEntity.getId(),subscriberEntity.getName());
        throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unable to get subscriber value id: {}" + subscriberEntity.getId());
    }


    public static @NotNull CreateConfigurationResponse mapConfigDataModelToCreateConfigResponse(@NotNull ConfigurationEntity createdConfig, List<SubscribersDataModel> subscribersDataModelList) {
        CreateConfigurationResponse createConfigurationResponse = new CreateConfigurationResponse();
        createConfigurationResponse.setId(createdConfig.getId());
        createConfigurationResponse.setName(createdConfig.getName());
        createConfigurationResponse.setDescription(createdConfig.getDescription());
        createConfigurationResponse.setEnabled(createdConfig.getEnabled());
        createConfigurationResponse.setSubscribers(subscribersDataModelList);
        createConfigurationResponse.setCreatedDateTime(createdConfig.getCreatedDateTime());
        return createConfigurationResponse;
    }

    public static @NotNull SubscribersDataModel mapSubscriberToConfigurationDataModel(@NotNull SubscriberEntity subscriberEntity) {
        SubscribersDataModel subscribersDataModel = new SubscribersDataModel();
        subscribersDataModel.setId(subscriberEntity.getId());
        subscribersDataModel.setName(subscriberEntity.getName());
        subscribersDataModel.setDescription(subscriberEntity.getDescription());
        subscribersDataModel.setEnabled(subscriberEntity.getEnabled());
        subscribersDataModel.setDataType(subscriberEntity.getDataType());
        subscribersDataModel.setValue(ServiceImp.getDTValueMethod(subscriberEntity));
        return subscribersDataModel;
    }
    public  @NotNull SubscribersDataModel findSubscribersById(@NotNull SubscriberEntity subscriberEntity) {
            SubscribersDataModel subscribersDataModel = new SubscribersDataModel();
            subscribersDataModel.setId(subscriberEntity.getId());
            subscribersDataModel.setName(subscriberEntity.getName());
            subscribersDataModel.setDescription(subscriberEntity.getDescription());
            subscribersDataModel.setEnabled(subscriberEntity.getEnabled());
            subscribersDataModel.setDataType(subscriberEntity.getDataType());
            subscribersDataModel.setValue(ServiceImp.getDTValueMethod(subscriberEntity));
            return subscribersDataModel;

    }
}