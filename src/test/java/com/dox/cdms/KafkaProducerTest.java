package com.dox.cdms;
import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.response.GetConfigurationResponse;import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaProducerTest {

    @Test
    public void testKafkaProducer() {
        // Create an embedded Kafka broker
        EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(1);
        broker.afterPropertiesSet();

        // Setup Kafka producer
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", broker.getBrokersAsString());
        producerProps.put("key.serializer", StringSerializer.class.getName());
        producerProps.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        // Send a message to Kafka
        String topic = "get-configuration";
        String key = "testKey";
        String value = "{\"name\":\"testconfig\",\"subscriber\":\"test2\"}";
        producer.send(new ProducerRecord<>(topic, key, value));
        producer.close();

        // Setup Kafka consumer
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", broker.getBrokersAsString());
        consumerProps.put("group.id", "testGroup");
        consumerProps.put("key.deserializer", StringDeserializer.class.getName());
        consumerProps.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        // Consume the message from Kafka
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

        // Assert that the message was successfully produced
        for (ConsumerRecord<String, String> record : records) {
            assert key.equals(record.key());
            assert value.equals(record.value());
        }

        consumer.close();
        broker.destroy();
    }
}

