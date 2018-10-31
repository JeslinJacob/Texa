package com.example.mqttmanager.internal;

import com.texaconnect.texa.mqtt.ReceivedMessage;

public interface IReceivedMessageListener {

    void onMessageReceived(ReceivedMessage message);
}