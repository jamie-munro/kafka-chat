package org.munro.kafkachat;

import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    final String topic = "messages";
    Consumer<String, String> consumer;

    ArrayList<Message> buffer;
    int lastRetrievedMessage = 0;

    public MessageListener(Properties props) {
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        buffer = new ArrayList<>();

        //run event handler in new thread
        new Thread(() -> {
            this.handleEvents();
        }).start();
    }

    public ArrayList<Message> getNewMessages() {
        ArrayList<Message> newMessages = new ArrayList<>();

        int lastMsg = this.buffer.size();

        for (int i = this.lastRetrievedMessage; i < lastMsg; i++) {
            newMessages.add(buffer.get(i).clone());
        }

        this.lastRetrievedMessage = lastMsg;
        return newMessages;
    }

    private void handleEvents() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    long timestamp = record.timestamp();

                    log.info("Consumed event from topic {}: key = {} value = {} timestamp = {}", topic, key, value);

                    Message msg = new Message(key, value, timestamp);
                    this.buffer.add(msg);
                }
            }
        } finally {
            consumer.close();
        }
    }
}
