package com.texaconnect.texa.event;

import java.util.Map;

public class MQTTSubscribeEvent {
    public Map<String,Object> nodeMap;

    public MQTTSubscribeEvent(Map<String, Object> nodeMap) {
        this.nodeMap = nodeMap;
    }
}
