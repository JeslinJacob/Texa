package com.texaconnect.texa.event;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTMessageEvent {
    public String topic;
    public MqttMessage message;

    public MQTTMessageEvent(String topic, MqttMessage message) {
        this.topic = topic;
        this.message = message;
    }
}
