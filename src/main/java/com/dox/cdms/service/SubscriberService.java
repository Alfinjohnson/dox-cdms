package com.dox.cdms.service;


import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.request.UpdateSubscriberRequest;
import com.dox.cdms.repository.SubscriberRepository;
import com.dox.cdms.service.imp.ServiceImp;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @apiNote Subscriber service
 */
@Service
@Slf4j(topic = "SubscriberService")
public class SubscriberService {
    @Autowired
    private final SubscriberRepository subscriberRepository;

    private final ServiceImp serviceImp;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    public SubscriberService(SubscriberRepository subscriberRepository, ServiceImp serviceImp) {
        this.subscriberRepository = subscriberRepository;
        this.serviceImp = serviceImp;
    }

    @NotNull SubscriberEntity createSubscriber(@NotNull CreateConfigurationDataModel configModel) {
        logger.info("createSubscriber: {}",configModel);
        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setName(configModel.getName());
        subscriberEntity.setDescription(configModel.getDescription());
        subscriberEntity.setDataType(configModel.getType());
        subscriberEntity.setEnabled(true);
        dataDTDeterminer(configModel.getValue(), configModel.getType(),subscriberEntity);
        return subscriberRepository.save(subscriberEntity);
    }

    private void dataDTDeterminer(Object value, @NotNull String type, SubscriberEntity subscriberEntity) {
        if (type.equals("boolean")|| type.equals("Boolean")) subscriberEntity.setBoolean_dt(((boolean) value));
        if (type.equals("integer")|| type.equals("int")) subscriberEntity.setInteger_dt(((Integer) value));
        if (type.equals("float_dt")) subscriberEntity.setFloat_dt(((float) value));
        if (type.equals("double_dt")) subscriberEntity.setDouble_dt(((double) value));
        if (type.equals("json_dt")) subscriberEntity.setJson_dt(((String) value));
    }

    public Optional<SubscriberEntity> findSubscribersById(Long subscriberId) {
        return subscriberRepository.findById(subscriberId);
    }

    public int updateSubscriber(@NotNull UpdateSubscriberRequest updateSubscriberRequest) {
        return subscriberRepository.updateNameAndDescriptionAndEnabledAndBoolean_dtAndString_dtAndDouble_dtAndInteger_dtAndFloat_dtAndJson_dtById(updateSubscriberRequest.getName(),
                updateSubscriberRequest.getDescription(),updateSubscriberRequest.isEnabled(),updateSubscriberRequest.getDataType(),updateSubscriberRequest.getValue(),updateSubscriberRequest.getId() );
    }

    public void deleteSubscriber(Long subscriberId) {
        subscriberRepository.deleteById(subscriberId);
    }

    public SubscribersDataModel getSubscriber(Long subscriberId) {
        Optional<SubscriberEntity> subscriberEntityOptional = findSubscribersById(subscriberId);
        if (subscriberEntityOptional.isPresent()) {
            SubscriberEntity subscriberEntity = subscriberEntityOptional.get();
            return serviceImp.findSubscribersById(subscriberEntity);
        } else {
        // Handle the case where the subscriber with the given ID is not found
        // You might want to throw an exception or return null, depending on your use case
        throw new ConfigurationService.NotFoundException("Subscriber not found with ID: "+subscriberId);
    }

    }
}
