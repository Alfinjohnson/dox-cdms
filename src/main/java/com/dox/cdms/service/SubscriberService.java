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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.dox.cdms.service.imp.ServiceImp.dataDTDeterminer;
import static com.dox.cdms.service.imp.ServiceImp.getSubscriberEntity;


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


    public Optional<SubscriberEntity> findSubscribersById(Long subscriberId) {
        return subscriberRepository.findById(subscriberId);
    }

    @Transactional
    public SubscriberEntity updateSubscriber(@NotNull UpdateSubscriberRequest updateSubscriberRequest) {

        Optional<SubscriberEntity> subscriberEntity = findSubscribersById(updateSubscriberRequest.getId());

        if (subscriberEntity.isPresent()){

            String type = updateSubscriberRequest.getDataType();
            SubscriberEntity subscriber = getSubscriberEntity(updateSubscriberRequest, subscriberEntity.get(), type);
            dataDTDeterminer(updateSubscriberRequest.getValue(),type,subscriber);
            return subscriberRepository.save(subscriber);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subscriber id not found");
        }
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subscriber id not found");
    }

    }

    public SubscriberEntity getSubscriberConfig(String name, String subscriber) {
        return subscriberRepository.getSubscriberConfig(name, subscriber);
    }
}
