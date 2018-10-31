package com.texaconnect.texa.interfaces;

import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.Node;

public interface DeviceClickCallback {
    void onDeviceClick(DeviceItem deviceItem);
    void onSwitchClick(Node node);
}
