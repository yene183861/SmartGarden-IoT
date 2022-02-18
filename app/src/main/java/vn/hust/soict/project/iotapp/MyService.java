package vn.hust.soict.project.iotapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;

public class MyService extends Service {
    public static final int MSG_CONNECT_MQTT = 1;
    private Messenger mMessenger;
    public static final String CHANNEL_ID = "push_notification_id";
    MqttAndroidClient client;
    String topic = "iot_application";

    public class MyHandler extends Handler {
        private Context appContext;
        public MyHandler(Context context) {
            this.appContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_CONNECT_MQTT:
                    connectMQTT();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService", "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mMessenger = new Messenger(new MyHandler(this));
        Log.e("MyService", "onBind");
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("MyService", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectMQTT();
        Log.e("MyService", "onDestroy");
    }

    public void connectMQTT() {
        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Log.e("id client mqtt", DataLocalManager.getClientId());
                    Log.e("connect mqtt", "onSuccess");
                    subscribeChannel(topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e("id", DataLocalManager.getClientId());
                    Log.e("connect mqtt", "onFailure" + exception);

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeChannel(String topicName) {
        try {
            Log.d("mqtt", "mqtt topic name: " + topicName);
            Log.d("mqtt", "client.isConnected(): " + client.isConnected());
            if (client.isConnected()) {
                client.subscribe(topicName, 0);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e("subscribe", "Connection was lost!");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("subscribe", "message>>" + new String(message.getPayload()));
                        Log.d("subscribe", "topic>>" + topic);
                        parseMqttMessage(new String(message.getPayload()));

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.e("subscribe", "Delivery Complete!");
                    }
                });
            }
        } catch (Exception e) {
            Log.d("mqtt", "Error :" + e);
        }
    }

    private void parseMqttMessage(String s) {
        Log.e("message receive", s);
    }

    private void disconnectMQTT() {
        if (client.isConnected()) {
            try {
                IMqttToken disconToken = client.disconnect();
                disconToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // we are now successfully disconnected
                        Log.e("mqtt", "disconnect success");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        Log.e("mqtt", "disconnect failed " + exception);
                         // something went wrong, but probably we are disconnected anyway
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}

