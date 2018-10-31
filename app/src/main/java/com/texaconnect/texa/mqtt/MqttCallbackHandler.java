/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.texaconnect.texa.mqtt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.texaconnect.texa.R;
import com.texaconnect.texa.event.MQTTConnected;
import com.texaconnect.texa.event.MQTTDisconnected;
import com.texaconnect.texa.event.MQTTMessageEvent;
import com.texaconnect.texa.ui.Notify;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

//import org.eclipse.paho.android.sample.Connection.ConnectionStatus;

/**
 * Handles call backs from the MQTT Client
 *
 */
public class MqttCallbackHandler implements MqttCallbackExtended {

  /** {@link Context} for the application used to format and import external strings**/
  private final Context context;
  /** Client handle to reference the connection that this handler is attached to**/
  private final String clientHandle;

  private static final String TAG = "MqttCallbackHandler";
    private static final String activityClass = "com.texaconnect.texa.ui.MainActivity";

  /**
   * Creates an <code>MqttCallbackHandler</code> object
   * @param context The application's context
   * @param clientHandle The handle to a {@link Connection} object
   */
  public MqttCallbackHandler(Context context, String clientHandle)
  {
    this.context = context;
    this.clientHandle = clientHandle;
  }

  /**
   * @see MqttCallback#connectionLost(Throwable)
   */
  @Override
  public void connectionLost(Throwable cause) {
    if (cause != null) {
      Log.d(TAG, "Connection Lost: " + cause.getMessage());
      /*Connection c = Connections.getInstance(context).getConnection(clientHandle);
      c.addAction("Connection Lost");
      c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);*/

      String message = context.getString(R.string.connection_lost);

//      , c.getId(), c.getHostName()

      //build intent
      Intent intent = new Intent();
      intent.setClassName(context, activityClass);
//      intent.putExtra("handle", clientHandle);

      //notify the user
      Notify.notifcation(context, message, intent, R.string.notifyTitle_connectionLost);

    }
    EventBus.getDefault().post(new MQTTDisconnected());
  }

  /**
   * @see MqttCallback#messageArrived(String, MqttMessage)
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {

    String messageString = context.getString(R.string.messageRecieved, new String(message.getPayload()),topic);
    Log.i(TAG, messageString);
//Get connection object associated with this object
    /*Connection c = Connections.getInstance(context).getConnection(clientHandle);
    c.messageArrived(topic, message);
    //get the string from strings.xml and format
    String messageString = context.getString(R.string.messageRecieved, new String(message.getPayload()), topic+";qos:"+message.getQos()+";retained:"+message.isRetained());

    Log.i(TAG, messageString);

    //update client history
    c.addAction(messageString);*/

    EventBus.getDefault().post(new MQTTMessageEvent(topic, message));

  }

  /**
   * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // Do nothing
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {
      if (reconnect) {
          EventBus.getDefault().post(new MQTTConnected());
      }

  }
}
