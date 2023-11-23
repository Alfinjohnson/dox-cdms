package com.dox.cdms.service;

import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.response.GetConfigurationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.dox.cdms.service.imp.ServiceImp.convertMessageToDTO;

@Service
public class KafkaConsumerService {
    @Autowired
    private ConfigurationService configurationService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Value("${get.configuration.consumer.topic}")
    private final String getConfigurationCustomerTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private final String getConfigurationGroupTopic;

    @Value("${kafka.concurrency}")
    private final String kafkaConcurrency;
    @Value("${get.configuration.producer.topic}")
    private final String getConfigurationProducerTopic;
    public KafkaConsumerService(ConfigurationService configurationService,
                                @Value("${get.configuration.consumer.topic}") String getConfigurationCustomerTopic,
                                @Value("${spring.kafka.consumer.group-id}") String getConfigurationGroupTopic,
                                @Value("${kafka.concurrency}") String kafkaConcurrency, @Value("${get.configuration.producer.topic}") String getConfigurationProducerTopic) {
        this.configurationService = configurationService;
        this.getConfigurationCustomerTopic = getConfigurationCustomerTopic;
        this.getConfigurationGroupTopic = getConfigurationGroupTopic;
        this.kafkaConcurrency = kafkaConcurrency;
        this.getConfigurationProducerTopic = getConfigurationProducerTopic;
    }


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * Kafka listener method to receive and process messages.
     *
     * @param message The received message.
     */
    @KafkaListener(topics = "${get.configuration.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "${kafka.concurrency}")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);

        try {
            // Process the received message
            GetConfigurationRequest getConfigurationRequest = convertMessageToDTO(message);
            GetConfigurationResponse getConfigurationResponse = configurationService.getConfiguration(getConfigurationRequest);
            String response = convertMessageToDTO(getConfigurationResponse);

            // Produce the response to a different topic
            kafkaTemplate.send("${get.configuration.producer.topic}", response);
            logger.info("Response message sent: {}", response);
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", e.getMessage(), e);
            // Log or handle the error as needed
        }
    }

}
