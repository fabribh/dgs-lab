package com.dgs.dgslab.infraestructure.channel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
@Getter
public class HandlerMessageFromTopic implements MessageHandler {

    private Message<String> messageReceived;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
      log.info("received message='{}'", message);
      messageReceived = (Message<String>) message;
    }
}
