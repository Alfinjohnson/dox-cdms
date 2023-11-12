package com.dox.cdms.service;


import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.repository.SubscriberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @apiNote Subscriber service
 */
@Service
@Slf4j(topic = "SubscriberService")
public class SubscriberService {
    @Autowired
    private final SubscriberRepository subscriberRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    /**
     * constructor for Autowired classes
     * @param subscriberRepository  constructor
     */
    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @NotNull SubscriberEntity createSubscriber(@NotNull CreateConfigurationDataModel configModel) {
        logger.info("createSubscriber: {}",configModel);
        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setName(configModel.getName());
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

}
