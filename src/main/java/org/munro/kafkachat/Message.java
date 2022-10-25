package org.munro.kafkachat;

public class Message implements Cloneable {
    private String username;
    private String message;
    private long timestamp;

    public Message(String username, String message, long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public Message clone() {
        return new Message(this.username, this.message, this.timestamp);
    }

    @Override
    public String toString() {
        return this.username + ": " + this.message;
    }
}
