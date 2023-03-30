package com.dgs.dgslab.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    private final ApplicationContext applicationContext;

    @Value("${artemis.embedded.topics:test.topic}")
    private String springIntegrationKafkaTopic;

    public HelloController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping
    @Cacheable("hello-world")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/kafka/{message}")
    public String ProduceAndConsumeMessageFromKafka(@PathVariable String message) {
        MessageChannel producingChannel = applicationContext.getBean("producingChannel", MessageChannel.class);
        Map<String, Object> headers =
                Collections.singletonMap(KafkaHeaders.TOPIC, springIntegrationKafkaTopic);
        GenericMessage<String> messageReceived =
                new GenericMessage<>("Hello Spring Integration Kafka! ".concat(message), headers);
        producingChannel.send(messageReceived);
        return "The message was published, See the log on the console!";
    }
}
