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

import java.util.Locale;
import java.util.Optional;

import static com.dox.cdms.service.imp.ServiceImp.dataDTDeterminer;
import static com.dox.cdms.service.imp.ServiceImp.getSubscriberEntity;


/**
 * Service class for managing subscribers.
 */
@Service
@Slf4j(topic = "SubscriberService")
public class SubscriberService {
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private final ServiceImp serviceImp;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    public SubscriberService(SubscriberRepository subscriberRepository, ServiceImp serviceImp) {
        this.subscriberRepository = subscriberRepository;
        this.serviceImp = serviceImp;
    }
    /**
     * Create a new subscriber based on the provided configuration model.
     *
     * @param configModel The configuration model for creating a subscriber.
     * @return The created subscriber entity.
     */
    @NotNull
    SubscriberEntity createSubscriber(@NotNull CreateConfigurationDataModel configModel) {
        try {
            logger.info("createSubscriber: {}", configModel);
            SubscriberEntity subscriberEntity = new SubscriberEntity();
            subscriberEntity.setName(configModel.getName().toLowerCase(Locale.ROOT));
            subscriberEntity.setDescription(configModel.getDescription());
            subscriberEntity.setDataType(configModel.getType().toLowerCase(Locale.ROOT));
            subscriberEntity.setEnabled(true);
            dataDTDeterminer(configModel.getValue(), configModel.getType(), subscriberEntity);
            return subscriberRepository.save(subscriberEntity);
        } catch (Exception e) {
            logger.error("Error creating subscriber: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create subscriber");
        }
    }

    /**
     * Find a subscriber by its ID.
     *
     * @param subscriberId The ID of the subscriber.
     * @return An optional containing the subscriber entity if found.
     */
    public Optional<SubscriberEntity> findSubscribersById(Long subscriberId) {
        try {
            logger.info("Finding subscriber by ID: {}", subscriberId);
            return subscriberRepository.findById(subscriberId);
        } catch (Exception e) {
            logger.error("Error finding subscriber by ID: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find subscriber by ID");
        }
    }

    /**
     * Update an existing subscriber based on the provided update request.
     *
     * @param updateSubscriberRequest The update request for the subscriber.
     * @return The updated subscriber entity.
     */
    @Transactional
    public SubscriberEntity updateSubscriber(@NotNull UpdateSubscriberRequest updateSubscriberRequest) {
        try {
            Optional<SubscriberEntity> subscriberEntity = findSubscribersById(updateSubscriberRequest.getId());

            if (subscriberEntity.isPresent()) {
                String type = updateSubscriberRequest.getDataType().toLowerCase(Locale.ROOT);
                SubscriberEntity subscriber = getSubscriberEntity(updateSubscriberRequest, subscriberEntity.get(), type);
                dataDTDeterminer(updateSubscriberRequest.getValue(), type, subscriber);
                return subscriberRepository.save(subscriber);
            } else {
                logger.error("Subscriber ID not found: {}", updateSubscriberRequest.getId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscriber ID not found");
            }
        } catch (Exception e) {
            logger.error("Error updating subscriber: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update subscriber");
        }
    }

    /**
     * Delete a subscriber by its ID.
     *
     * @param subscriberId The ID of the subscriber to delete.
     */
    public void deleteSubscriber(Long subscriberId) {
        try {
            logger.info("Deleting subscriber by ID: {}", subscriberId);
            subscriberRepository.deleteById(subscriberId);
        } catch (Exception e) {
            logger.error("Error deleting subscriber: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete subscriber");
        }
    }

    /**
     * Get a subscriber by ID.
     *
     * @param subscriberId The ID of the subscriber.
     * @return A SubscribersDataModel representing the subscriber.
     */
    public SubscribersDataModel getSubscriber(Long subscriberId) {
        try {
            logger.info("Getting subscriber by ID: {}", subscriberId);
            Optional<SubscriberEntity> subscriberEntityOptional = findSubscribersById(subscriberId);
            if (subscriberEntityOptional.isPresent()) {
                SubscriberEntity subscriberEntity = subscriberEntityOptional.get();
                return serviceImp.findSubscribersById(subscriberEntity);
            } else {
                logger.error("Subscriber ID not found: {}", subscriberId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscriber ID not found");
            }
        } catch (Exception e) {
            logger.error("Error getting subscriber: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get subscriber");
        }
    }

    /**
     * Get a subscriber's configuration by name and subscriber.
     *
     * @param name       The name of the configuration.
     * @param subscriber The name of the subscriber.
     * @return A SubscriberEntity representing the subscriber's configuration.
     */
    public SubscriberEntity getSubscriberConfig(String name, String subscriber) {
        try {
            logger.info("Getting subscriber config. Name: {}, Subscriber: {}", name, subscriber);
            return subscriberRepository.getSubscriberConfig(name, subscriber);
        } catch (Exception e) {
            logger.error("Error getting subscriber config: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get subscriber config");
        }
    }
}
