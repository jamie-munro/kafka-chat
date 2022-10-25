package org.munro.kafkachat;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MessageSender {
    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);

    final String topic = "messages";
    Producer<String, String> producer;

    public MessageSender(Properties props) {
        this.producer = new KafkaProducer<>(props);
    }

    public void send(String username, String message) {
        producer.send(new ProducerRecord<>(topic, username, message));
        producer.flush();
        log.info("Produced event to topic {}: key = {} value = {}", topic, username, message);
    }
}
