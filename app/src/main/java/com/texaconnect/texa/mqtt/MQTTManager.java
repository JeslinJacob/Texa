package com.texaconnect.texa.mqtt;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.event.LoginEvent;
import com.texaconnect.texa.event.MQTTConnect;
import com.texaconnect.texa.event.MQTTConnected;
import com.texaconnect.texa.event.MQTTDisconnect;
import com.texaconnect.texa.event.MQTTDisconnected;
import com.texaconnect.texa.event.MQTTMessageEvent;
import com.texaconnect.texa.event.MQTTReConnect;
import com.texaconnect.texa.event.MQTTSendMessageEvent;
import com.texaconnect.texa.event.MQTTSubscribeEvent;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.MQTTModel;
import com.texaconnect.texa.model.Node;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

//@Singleton
public class MQTTManager {
    private static final String TAG = "MQTTManager";
    private static final int QOS = 1;
    final String clientid;
    private final Context mContext;
    private String mServerHost = "www.texaconnect.com";
    private int mPort = 8883;
    private Connection mConnection;
    Map<String,Object> nodeMap;
//    @Inject
    public MQTTManager(Application application) {

        Log.d(TAG,"create");
        clientid = "t_android_"+System.currentTimeMillis();
        mContext = application;

        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        Log.d(TAG,"onEvent LoginEvent");
        connect();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MQTTConnect event) {
        Log.d(TAG,"onEvent MQTTConnect");
        connect();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MQTTReConnect event) {
        Log.d(TAG,"onEvent MQTTReConnect "+event.from);
        connect();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MQTTConnected event) {
        Log.d(TAG,"onEvent MQTTConnected");
        mConnection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED);
        setSubscriptions();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MQTTDisconnect event) {
        Log.d(TAG,"onEvent MQTTDisconnect");
        disconnect();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MQTTDisconnected event) {
        Log.d(TAG,"onEvent MQTTDisconnected");

        if (nodeMap != null) {
            for (Map.Entry<String, Object> e : nodeMap.entrySet()) {

                if (e.getValue() instanceof Node) {
                    Node node = (Node) e.getValue();
                    node.setDeviceStatus(MQTTModel.OFFLINE);
//                    node.setNodeOnOff(false);

                }else
                if (e.getValue() instanceof DeviceItem) {
                    DeviceItem deviceItem = (DeviceItem) e.getValue();
                    deviceItem.device.setDeviceStatus(MQTTModel.OFFLINE);
                }
            }
        }

        if(mConnection.getStatus() != Connection.ConnectionStatus.DISCONNECTED){
            connect();
        }else {
            mConnection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
        }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MQTTSubscribeEvent event) {
        Log.d(TAG,"onEvent MQTTSubscribeEvent");
        nodeMap = event.nodeMap;
        setSubscriptions();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
     public void onEvent(MQTTMessageEvent event) {
        Log.d(TAG,"onEvent MQTTMessageEvent "+ new String(event.message.getPayload())+", "+event.topic);

            if (nodeMap.containsKey(event.topic)) {
                if (nodeMap.get(event.topic) instanceof Node) {
                    Node node = (Node) nodeMap.get(event.topic);
                    node.setDeviceStatus(new String(event.message.getPayload()));
                }else if (nodeMap.get(event.topic) instanceof DeviceItem) {
                    DeviceItem deviceItem = (DeviceItem) nodeMap.get(event.topic);
                    String message = new String(event.message.getPayload());
                    deviceItem.device.setDeviceStatus(message);
                    for (Node node:deviceItem.device.nodes) {
                        if (message.equals("1")) {
                            subscribe(node.subscriptionTopic);
                            node.isEnabled = true;
                        }else if (message.equals("0")) {
                            unSubscribe(node.subscriptionTopic);
                            node.setDeviceStatus(MQTTModel.OFFLINE);

                        }
                    }
                }
            }


    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MQTTSendMessageEvent event) {
        Log.d(TAG,"onEvent MQTTSendMessageEvent "+event.topic+", "+event.message+", "+mConnection.isConnected());
        publish(event.topic, event.message, false);

    }

    private void setSubscriptions() {
//        if (mConnection == null || !mConnection.isConnected()||
//                mConnection.isConnecting() || mConnection.getClient().isConnected()) return;

            if (nodeMap != null) {
                for (Map.Entry<String, Object> e : nodeMap.entrySet()) {

                        /*if (e.getValue() instanceof Node) {
                            Node node = (Node) e.getValue();
                            node.isSubscribed = subscribe(node.subscriptionTopic);
                        }else*/
                        if (e.getValue() instanceof DeviceItem) {
                            DeviceItem deviceItem = (DeviceItem) e.getValue();
                            subscribe(deviceItem.device.subscriptionTopic);
                        }
                }
            }
    }

    private boolean subscribe(String topic) {
        try {
            if (mConnection != null && mConnection.getClient().isConnected()) {
                mConnection.getClient().subscribe(topic, QOS);
            }
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean unSubscribe(String topic) {
        try {
//            String[] actionArgs = new String[1];
//            actionArgs[0] = topic;
//            final ActionListener callback = new ActionListener(mContext,
//                    ActionListener.Action.SUBSCRIBE, mConnection, actionArgs);

            if (mConnection != null && mConnection.getClient().isConnected()) {
                mConnection.getClient().unsubscribe(topic);
            }
            return false;
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void publish(String topic, String message, boolean retain){

        try {
            if (mConnection != null && mConnection.getClient().isConnected()) {
                String[] actionArgs = new String[2];
                actionArgs[0] = "Failed to perform the operation";
//            actionArgs[0] = message;
//            actionArgs[1] = topic;
                final ActionListener callback = new ActionListener(mContext,
                        ActionListener.Action.PUBLISH, mConnection, actionArgs);
                mConnection.getClient().publish(topic, message.getBytes(), QOS, retain, null, callback);
            }
        } catch( MqttException ex){
            Log.e(TAG, "Exception occurred during publish: " + ex.getMessage());
        }
    }

    private void connect() {

        if (!TexaApplication.getApp().isLoggedIn() ||
                (mConnection != null &&
                        (mConnection.isConnecting() || mConnection.getClient().isConnected())
                )) {
            return;
        }

        Log.d(TAG,"MQTTManager connect");
        mConnection = Connection.createConnection("texaconnection",
                clientid,mServerHost,mPort,
                mContext,true);

        String[] actionArgs = new String[1];
        actionArgs[0] = mConnection.getId();
        final ActionListener callback = new ActionListener(mContext,
                ActionListener.Action.CONNECT, mConnection, actionArgs);
        try {
//            Connections.getInstance(mContext).addConnection(mConnection);

            mConnection.getClient().setCallback(new MqttCallbackHandler(mContext, mConnection.handle()));
            mConnection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);
//            Log.d(TAG,"MQTTManager connect "+mConnection.getClient().isConnected());

            mConnection.getClient().connect(getMqttConnectOptions(), null, callback);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(),
                    "MqttException occurred", e);
        }catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(),
                    "MqttException occurred", e);
        }
    }

    private void disconnect(){
        try {
            if ((mConnection != null && mConnection.getClient().isConnected()))
            {
                mConnection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
                mConnection.getClient().disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions getMqttConnectOptions() {

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setConnectionTimeout(80);
        mqttConnectOptions.setKeepAliveInterval(200);
        mqttConnectOptions.setUserName(""+TexaApplication.getApp().getUserId());
        String pass = TexaApplication.sAccesToken;
        mqttConnectOptions.setPassword(pass.toCharArray());

        return mqttConnectOptions;
    }

}