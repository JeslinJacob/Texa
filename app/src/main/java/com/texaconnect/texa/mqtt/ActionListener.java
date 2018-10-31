package com.texaconnect.texa.mqtt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.texaconnect.texa.R;
import com.texaconnect.texa.event.MQTTConnected;
import com.texaconnect.texa.mqtt.internal.Connections;
import com.texaconnect.texa.mqtt.model.Subscription;
import com.texaconnect.texa.ui.Notify;
import com.texaconnect.texa.worker.FetchRefreshTokenWorker;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * This Class handles receiving information from the
 * {@link MqttAndroidClient} and updating the {@link Connection} associated with
 * the action
 */
public class ActionListener implements IMqttActionListener {

    private static final String TAG = "ActionListener";
    private static final String activityClass = "org.eclipse.paho.android.sample.activity.MainActivity";

    /**
     * Actions that can be performed Asynchronously <strong>and</strong> associated with a
     * {@link ActionListener} object
     */
    public enum Action {
        /**
         * Connect Action
         **/
        CONNECT,
        /**
         * Disconnect Action
         **/
        DISCONNECT,
        /**
         * Subscribe Action
         **/
        SUBSCRIBE,
        /**
         * Publish Action
         **/
        PUBLISH
    }

    /**
     * The {@link Action} that is associated with this instance of
     * <code>ActionListener</code>
     **/
    private final Action action;
    /**
     * The arguments passed to be used for formatting strings
     **/
    private final String[] additionalArgs;

    private final Connection connection;
    /**
     * Handle of the {@link Connection} this action was being executed on
     **/
    private final String clientHandle;
    /**
     * {@link Context} for performing various operations
     **/
    private final Context context;

    /**
     * Creates a generic action listener for actions performed form any activity
     *
     * @param context        The application context
     * @param action         The action that is being performed
     * @param connection     The connection
     * @param additionalArgs Used for as arguments for string formating
     */
    public ActionListener(Context context, Action action,
                          Connection connection, String... additionalArgs) {
        this.context = context;
        this.action = action;
        this.connection = connection;
        this.clientHandle = connection.handle();
        this.additionalArgs = additionalArgs;
    }

    /**
     * The action associated with this listener has been successful.
     *
     * @param asyncActionToken This argument is not used
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        switch (action) {
            case CONNECT:
//                Notify.toast(context, "Connected", Toast.LENGTH_LONG);

//                connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED);
//                connect();
                EventBus.getDefault().post(new MQTTConnected());

                break;
            case DISCONNECT:
                disconnect();
                break;
            case SUBSCRIBE:
                subscribe();
                break;
            case PUBLISH:
//                publish();
                break;
        }

    }

    /**
     * A publish action has been successfully completed, update connection
     * object associated with the client this action belongs to, then notify the
     * user of success
     */
    private void publish() {

//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        @SuppressLint("StringFormatMatches") String actionTaken = context.getString(R.string.toast_pub_success,
              (Object[]) additionalArgs);
        connection.addAction(actionTaken);
        Notify.toast(context, actionTaken, Toast.LENGTH_SHORT);
        System.out.print("Published");

    }

    /**
     * A addNewSubscription action has been successfully completed, update the connection
     * object associated with the client this action belongs to and then notify
     * the user of success
     */
    private void subscribe() {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String actionTaken = context.getString(R.string.toast_sub_success,
                (Object[]) additionalArgs);
        connection.addAction(actionTaken);
        Notify.toast(context, actionTaken, Toast.LENGTH_SHORT);
        System.out.print(actionTaken);

    }

    /**
     * A disconnection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void disconnect() {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        connection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
        String actionTaken = context.getString(R.string.toast_disconnected);
        connection.addAction(actionTaken);
        Log.i(TAG, connection.handle() + " disconnected.");
        //build intent
        Intent intent = new Intent();
        intent.setClassName(context, activityClass);
        intent.putExtra("handle", clientHandle);

    }

    /**
     * A connection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void connect() {

//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
//        connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED);
        connection.addAction("Client Connected");
        Log.i(TAG, connection.handle() + " connected.");
        try {

            ArrayList<Subscription> subscriptions = connection.getSubscriptions();
            for (Subscription sub : subscriptions) {
                Log.i(TAG, "Auto-subscribing to: " + sub.getTopic() + "@ QoS: " + sub.getQos());
                connection.getClient().subscribe(sub.getTopic(), sub.getQos());
            }
        } catch (MqttException ex){
            Log.e(TAG, "Failed to Auto-Subscribe: " + ex.getMessage());
        }

    }

    /**
     * The action associated with the object was a failure
     *
     * @param token     This argument is not used
     * @param exception The exception which indicates why the action failed
     */
    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        switch (action) {
            case CONNECT:
//                Notify.toast(context, "onFailure "+exception.getMessage(), Toast.LENGTH_LONG);

                connect(exception);
                break;
            case DISCONNECT:
                disconnect(exception);
                break;
            case SUBSCRIBE:
                subscribe(exception);
                break;
            case PUBLISH:
                publish(exception);
                break;
        }

    }

    /**
     * A publish action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void publish(Throwable exception) {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
//        @SuppressLint("StringFormatMatches") String action = context.getString(R.string.toast_pub_failed,
//                (Object[]) additionalArgs);

        String action = additionalArgs[0];
        connection.addAction(action);
        Notify.toast(context, action, Toast.LENGTH_SHORT);
        System.out.print("Publish failed");

    }

    /**
     * A addNewSubscription action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void subscribe(Throwable exception) {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String action = context.getString(R.string.toast_sub_failed,
                (Object[]) additionalArgs);
        connection.addAction(action);
        Notify.toast(context, action, Toast.LENGTH_SHORT);
        System.out.print(action);
    }

    /**
     * A disconnect action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void disconnect(Throwable exception) {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        connection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
        connection.addAction("Disconnect Failed - an error occured");

    }

    /**
     * A connect action was unsuccessful, notify the user and update client history
     *
     * @param exception This argument is not used
     */
    private void connect(Throwable exception) {
//        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        connection.addAction("Client failed to connect");
        if(exception.getMessage().equals("Bad user name or password") &&
                connection.getStatus() != Connection.ConnectionStatus.ERROR){
//            Refresh token and connect;

            System.out.println("Client failed to connect "+exception.getMessage());
            connection.changeConnectionStatus(Connection.ConnectionStatus.ERROR);
            OneTimeWorkRequest compressionWork =
                    new OneTimeWorkRequest.Builder(FetchRefreshTokenWorker.class)
                            .build();
            WorkManager.getInstance().enqueue(compressionWork);

        }
//        Notify.toast(context, "Client failed to connect "+exception.getMessage(), Toast.LENGTH_SHORT);

    }

}