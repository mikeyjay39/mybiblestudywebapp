package com.mybiblestudywebapp.persistenceservice.events;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/4/20
 *//*
@Configuration
public class KafkaConfig {

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.producerCompressionType);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, this.producerClientId);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return props;
    }

    @Bean
    public ProducerFactory<String, Pdid> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Pdid> kafkaTemplate() {
        KafkaTemplate<String, Pdid> kafkaTemplate = new KafkaTemplate<>(producerFactory());

        kafkaTemplate.setDefaultTopic(this.defaultTopicName);

        return kafkaTemplate;
    }
}
*/