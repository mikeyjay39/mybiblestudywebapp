package com.mybiblestudywebapp.persistenceservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/3/20
 */
@Component
@Slf4j
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class PersistenceSource {

    private final Source source;

    public void sendMessage() {
        log.debug("Sending Kafka message");
        String msg = LocalTime.now().toString();
        source.output().send(MessageBuilder.withPayload("Message from Persistence Service at " + msg)
        .build());
    }
}
