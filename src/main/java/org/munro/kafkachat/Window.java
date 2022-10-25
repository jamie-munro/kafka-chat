package org.munro.kafkachat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(Window.class);

    MessageListener messageListener;
    MessageSender messageSender;
    String username;
    JScrollPane receivedMessagesPane;
    JTextArea receivedMessagesArea;
    JTextArea sendMessageArea;
    JButton sendButton;


    public Window(MessageListener messageListener, MessageSender messageSender, String username) {
        super("Kafka Chat");

        this.messageListener = messageListener;
        this.messageSender = messageSender;
        this.username = username;

        log.info("Username selected: {}", username);
        log.info("Initialising...");

        this.initialise();

        //run message listener in a new thread
        new Thread(() -> {
            this.listener();
        }).start();
    }

    private void initialise() {
        this.setSize(390, 585);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new FlowLayout());

        receivedMessagesArea = new JTextArea(30, 33);
        receivedMessagesArea.setEditable(false);

        receivedMessagesPane = new JScrollPane(receivedMessagesArea);
        receivedMessagesPane.setHorizontalScrollBar(null);

        this.add(receivedMessagesPane);

        sendMessageArea = new JTextArea(3, 26);
        this.add(sendMessageArea);

        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(70, 50));
        this.add(sendButton);

        sendButton.addActionListener(e -> sendButtonPressedHandler());
    }

    private void sendButtonPressedHandler() {
        String message = this.sendMessageArea.getText();
        this.sendMessageArea.setText("");

        if (!message.isBlank()) {
            this.messageSender.send(username, message);
        }
    }

    private void listener() {
        while(true) {
            for (Message msg : this.messageListener.getNewMessages()) {
                log.info("Received message: {}", msg.toString());
                this.receivedMessagesArea.append(msg.toString() + "\n");
            }
        }
    }
}
