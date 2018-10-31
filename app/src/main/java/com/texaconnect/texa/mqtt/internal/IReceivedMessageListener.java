package com.texaconnect.texa.mqtt.internal;

import com.texaconnect.texa.mqtt.model.ReceivedMessage;

public interface IReceivedMessageListener {

    void onMessageReceived(ReceivedMessage message);
}