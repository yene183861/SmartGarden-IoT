package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import vn.hust.soict.project.iotapp.MyService;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.DataReceive;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.viewmodel.DeviceListViewModel;

public class DeviceManageActivity extends AppCompatActivity implements DeviceListAdapter.ItemClickListener {
    private ImageView btnAddNewDevice, imgLamp, imgWatering;
    private TextView tvNoDeviceList;
    private RecyclerView rcvDevice;
    private LinearLayout layoutControl;
    private Switch controlLamp, controlWatering;
    private DeviceListViewModel deviceListViewModel;
    private List<Device> deviceList = new ArrayList<>();
    private DeviceListAdapter adapter;
    private Device deviceForEdit;
    public static final String CHANNEL_ID = "push_notification_id";
    MqttAndroidClient client;
    String topic = "iot_application";
    public static final String TEMPERATURE_TOPIC = "iot/dht11/temperature";
    public static final String HUMIDITY_TOPIC = "iot/dht11/humility";
    public static final String LUX_TOPIC = "iot/bh750/lux";
    public static final String WATER_TOPIC = "iot/role/water";

//    private Messenger messenger = null;
//    private boolean isServiceConnected;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            messenger = new Messenger(service);
//            isServiceConnected = true;
//            //send message connect mqtt
//            sendMessageConnectMQTT();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            messenger = null;
//            isServiceConnected = false;
//        }
//    };

//    private void sendMessageConnectMQTT() {
//        Message message = Message.obtain(null, MyService.MSG_CONNECT_MQTT, 0, 0);
//        try {
//            messenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

//    Area area;
//    public void sayHello(View v) {
//        if (!isServiceConnected) return;
//        // Create and send a message to the service, using a supported 'what' value
//        Message msg = Message.obtain(null, MyService.MSG_CONNECT_MQTT, 0, 0);
//        try {
//            messenger.send(msg);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e("onStart", "onStart");
//        //bind to the service
//        bindService(new Intent(this, MyService.class),serviceConnection, Context.BIND_AUTO_CREATE);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);

        //area = (Area) getIntent().getSerializableExtra("area");
        initUi();
        connectMQTT();

//        Intent serviceIntent = new Intent(context, ServedService.class);
//        context.startService(serviceIntent);
//        context.bindService(serviceIntent, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                //retrieve an instance of the service here from the IBinder returned
//                //from the onBind method to communicate with
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//            }
//        }, Context.BIND_AUTO_CREATE);
        listener();
        Device device = new Device("nhiet do", "khu 10 cuoi vuon", 1, 27, true);
        Device device1 = new Device("do am dat", "khu 10 cuoi vuon", 2, 10, true);
        Device device2 = new Device("anh sang", "khu 10 cuoi vuon", 4, 34, true);
        Device device3 = new Device("do am kk", "khu 10 cuoi vuon", 3, 12, true);
        deviceList.add(device);
        deviceList.add(device1);
        deviceList.add(device2);
        deviceList.add(device3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDevice.setLayoutManager(linearLayoutManager);
        adapter = new DeviceListAdapter(this, deviceList, (DeviceListAdapter.ItemClickListener) this);
        adapter.setDeviceList(deviceList);
        rcvDevice.setAdapter(adapter);
        deviceListViewModel = new ViewModelProvider(this).get(DeviceListViewModel.class);
        deviceListViewModel.getDeviceListObserver().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                if (devices == null) {
                    rcvDevice.setVisibility(View.GONE);
                    tvNoDeviceList.setVisibility(View.VISIBLE);
                } else {
                    deviceList = devices;
                    adapter.setDeviceList(devices);
                    tvNoDeviceList.setVisibility(View.GONE);
                    rcvDevice.setVisibility(View.VISIBLE);
                }

            }
        });
        //deviceListViewModel.getDeviceList(area.getId());
    }

    private void initUi() {
        btnAddNewDevice = findViewById(R.id.btnAddNewDevice);
        rcvDevice = findViewById(R.id.rcvDevice);
        tvNoDeviceList = findViewById(R.id.tvNoDeviceList);
        layoutControl = findViewById(R.id.layoutControl);
        controlLamp = (Switch) findViewById(R.id.controlLamp);
        controlWatering = (Switch) findViewById(R.id.controlWatering);
        imgLamp = findViewById(R.id.imgLamp);
        imgWatering = findViewById(R.id.imgWatering);
    }

    private void listener() {
        btnAddNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
        controlWatering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgWatering.setImageResource(R.drawable.watering);
                } else {
                    imgWatering.setImageResource(R.drawable.not_watering);
                }
                Device device2 = new Device("id", "idUser",  isChecked, 2);
                publish(device2, WATER_TOPIC);
            }
        });
        controlLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgLamp.setImageResource(R.drawable.lamp_on);
                } else {
                    imgLamp.setImageResource(R.drawable.lamp_off);
                }
                Device device2 = new Device("id", "idUser", isChecked, 4);
                publish(device2, LUX_TOPIC);
            }
        });
    }


//    @Override
//    protected void onDestroy() {
//        if (isServiceConnected) {
//            unbindService(serviceConnection);
//            isServiceConnected = false;
//        }
//        super.onDestroy();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(isServiceConnected){
//            unbindService(serviceConnection);
//            isServiceConnected = false;
//        }
//    }


    @Override
    protected void onDestroy() {
        disconnectMQTT();
        Log.e("onDestroy", "onDestroy");
        super.onDestroy();
    }

    private void showAddDialog(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_device, null);
        TextView addDeviceTitleDialog = dialogView.findViewById(R.id.addDeviceTitleDialog);
        EditText enterNameDevice = dialogView.findViewById(R.id.enterNameDevice);
        EditText enterPositionDevice = dialogView.findViewById(R.id.enterPositionDevice);
        TextView btnCreateDevice = dialogView.findViewById(R.id.btnCreateDevice);
        TextView btnCancelDevice = dialogView.findViewById(R.id.btnCancelDevice);

        if (isEdit) {
            addDeviceTitleDialog.setText("Update information device");
            btnCreateDevice.setText("Update");
            enterNameDevice.setText(deviceForEdit.getValue());
            enterPositionDevice.setText(deviceForEdit.getPosition());
        }

        btnCancelDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterNameDevice.getText().toString().trim();
                String position = enterPositionDevice.getText().toString().trim();
//                int area = Integer.parseInt(enterArea.getText().toString().trim());
//                int acreage = Integer.parseInt(enterAcreageGarden.getText().toString().trim());

                //check fields empty

                //call view model
                if (isEdit) {
//                    deviceForEdit.setName(name);
                    deviceForEdit.setPosition(position);
//                    gardenForEdit.setAcreage(acreage);
//                    gardenForEdit.setArea(acreage);
                    deviceListViewModel.updateDevice(deviceForEdit);
                } else {
                    //call view model
//                    Device device = new Device(area.getId(), name, position);
//                    deviceListViewModel.insertDevice(device);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onEditClick(Device device) {

    }

    @Override
    public void onDeleteClick(Device device) {

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
                        JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                        Gson gson = new Gson();
                        DataReceive data = gson.fromJson(jsonObject.toString(), DataReceive.class);
                        int n = deviceList.size();
                        int type;
                        if (data instanceof DataReceive) {
                            for (int i = 0; i < n; i++) {
                                type = deviceList.get(i).getType();
                                switch (type) {
                                    case 1:
                                        deviceList.get(i).setValue(Integer.parseInt(data.getTemperature()));
                                        break;
                                    case 2:
                                        deviceList.get(i).setValue(Integer.parseInt(data.getHumidity_soil()));
                                        break;
                                    case 3:
                                        deviceList.get(i).setValue(Integer.parseInt(data.getHumidity_air()));
                                        break;
                                    default:
                                        deviceList.get(i).setValue(Integer.parseInt(data.getHumidity_air()));
                                }
                            }
                            adapter.setDeviceList(deviceList);
                            rcvDevice.setAdapter(adapter);
                        } else {
                            Log.e("mess receive: ", "error");
                        }
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

    private void publish(Device device, String topic) {
        if (client.isConnected()) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(device);
                MqttMessage message = new MqttMessage(json.getBytes("UTF-8"));
                client.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(DeviceManageActivity.this,"publish error",Toast.LENGTH_SHORT).show();
        }
    }

}


















