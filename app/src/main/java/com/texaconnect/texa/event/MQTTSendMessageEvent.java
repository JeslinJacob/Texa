package com.texaconnect.texa.event;

public class MQTTSendMessageEvent {
    public String topic;
    public String message;

    public MQTTSendMessageEvent(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
}
