package com.texaconnect.texa.model;

public class SelectedWifiItem {
    String SSID,SecurityType;

    public SelectedWifiItem(String SSID, String securityType) {
        this.SSID = SSID;
        SecurityType = securityType;
    }

    public String getSSID() {
        return SSID;
    }

    public String getSecurityType() {
        return SecurityType;
    }
}
