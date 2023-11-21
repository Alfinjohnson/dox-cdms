package com.dox.cdms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"get-configuration", "get-configuration-response"})
@DirtiesContext
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testKafkaInteraction() {
        // Given
        String testMessage = "{\"name\":\"testconfig\",\"subscriber\":\"test2\"}";

        // When
        kafkaTemplate.send("get-configuration", testMessage);
        // Then
        // Wait for the Kafka consumer to process the message
        waitForConsumer();
    }

    private void waitForConsumer() {
        // Similar to the previous example, you may need to add additional logic
        // to ensure the Kafka consumer has processed the message
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @KafkaListener(topics = "get-configuration-response", groupId = "test-group")
    public void listen(String message) {
        System.out.println(message);
        // This method can be left empty if you don't need to do anything with the message
        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("{\"name\":\"testconfig\",\"subscriber\":\"test2\",\"dataType\":\"boolean\",\"value\":true}"); // replace with your expected response

    }
}
