package com.mybiblestudywebapp.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/4/20
 */
public interface PersistenceChannel {

    @Input("persistenceServiceTopic")
    SubscribableChannel persistence();

}
