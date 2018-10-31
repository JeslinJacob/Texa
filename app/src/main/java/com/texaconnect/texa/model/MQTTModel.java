package com.texaconnect.texa.model;

import android.databinding.BaseObservable;

public class MQTTModel extends BaseObservable {
    public static final String OFFLINE = "-1";
    public enum State {
        OFFLINE, ON, OFF, ONLOCK, OFFLOCK
    }

}
