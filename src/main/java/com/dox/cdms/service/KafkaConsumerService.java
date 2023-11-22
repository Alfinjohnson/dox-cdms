package com.dox.cdms.service;

import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.response.GetConfigurationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Autowired
    private final ConfigurationService configurationService;


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

    @KafkaListener(topics = "${get.configuration.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "${kafka.concurrency}")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        GetConfigurationRequest getConfigurationRequest = convertMessageToDTO(message);

        // Process the received message
        GetConfigurationResponse getConfigurationResponse = configurationService.getConfiguration(getConfigurationRequest);
        String response = convertMessageToDTO(getConfigurationResponse);
        System.out.println("response message: " + response);

        // Produce the response to a different topic
        kafkaTemplate.send("${get.configuration.producer.topic}", response);
    }

    private static GetConfigurationRequest convertMessageToDTO(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message,GetConfigurationRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertMessageToDTO(GetConfigurationResponse message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
