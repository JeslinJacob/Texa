package com.texaconnect.texa.event;

import com.texaconnect.texa.model.SelectedWifiItem;

import java.util.List;

public class wifiListEvent {

    List<SelectedWifiItem> selectedWifiItems;

    public wifiListEvent(List<SelectedWifiItem> selectedWifiItems) {
        this.selectedWifiItems = selectedWifiItems;
    }

    public List<SelectedWifiItem> getSelectedWifiItems() {
        return selectedWifiItems;
    }
}
