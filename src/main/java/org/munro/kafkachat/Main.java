package org.munro.kafkachat;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static String username;

    public static void main(String[] args) {
        //load Kafka properties
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream("kafka.properties")) {
            props.load(resourceStream);
        }
        catch(IOException ex) {
            log.error("Failed to load Kafka properties file.", ex);
            System.exit(-1);
        }

        do {
            username = JOptionPane.showInputDialog("Please enter a username:", "");

            if (username == null) {
                System.exit(0);
            }
        }
        while (username.equals(""));

        // Add additional properties.
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-chat-"+username);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        MessageListener messageListener = new MessageListener(props);
        MessageSender messageSender = new MessageSender(props);

        Window window = new Window(messageListener, messageSender, username);
        window.setVisible(true);
    }
}