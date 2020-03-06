package com.example.longcodeapplication;

public interface MessageListener {
    /**
     * To call this method when new message received and send back
     * @param message Message
     */
    void messageReceived(String message, String SENDER, String DATETIME);
}