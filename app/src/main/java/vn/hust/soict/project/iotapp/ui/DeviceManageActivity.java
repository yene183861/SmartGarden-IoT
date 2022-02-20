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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    String topic = "iot-nhom8-20211/#";
    public static final String TEMPERATURE_TOPIC = "iot-nhom8-20211/dht11/temperature";
    public static final String HUMIDITY_TOPIC = "iot-nhom8-20211/dht11/humidity";
    public static final String LAMP_TOPIC = "iot-nhom8-20211/lamp";
    public static final String SOILMOIST_TOPIC = "iot-nhom8-20211/soil";
    private Area area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);

        area = (Area) getIntent().getSerializableExtra("area");
        initUi();
        connectMQTT();

        Device device = new Device("idArea", "nhiệt độ", "khu 10 cuoi vuon", 1, 0, true);
        Device device1 = new Device("idArea", "do am dat", "khu 10 cuoi vuon", 2, 0, true);
        Device device3 = new Device("idArea", "do am kk", "khu 10 cuoi vuon", 3, 0, true);
        deviceList.add(device);
        deviceList.add(device1);
        deviceList.add(device3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDevice.setLayoutManager(linearLayoutManager);
        adapter = new DeviceListAdapter(this, deviceList, (DeviceListAdapter.ItemClickListener) this);
        //deviceListViewModel.getDeviceList(area.getId());
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
        initListener();
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

    private void initListener() {
        btnAddNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
        controlWatering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgWatering.setImageResource(R.drawable.watering);
                } else {
                    imgWatering.setImageResource(R.drawable.not_watering);
                }
                //Device device2 = new Device("id", "idArea", isChecked, 2);
                //subscribeChannel(WATER_TOPIC);
                //publish(device2, WATER_TOPIC);
            }
        });
        controlLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgLamp.setImageResource(R.drawable.lamp_on);
                } else {
                    imgLamp.setImageResource(R.drawable.lamp_off);
                }
//                Device device2 = new Device("id", "idArea", isChecked, 4);
//                subscribeChannel(LAMP_TOPIC);
//                publish(device2, LAMP_TOPIC);
            }
        });
    }


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
        RadioGroup radioGroupDeviceType = dialogView.findViewById(R.id.radioGroupDeviceType);
        RadioButton radioButtonTemperature = dialogView.findViewById(R.id.radioButtonTemperature);
        RadioButton radioButtonSoilMoisture = dialogView.findViewById(R.id.radioButtonSoilMoisture);
        RadioButton radioButtonAirHumidity = dialogView.findViewById(R.id.radioButtonAirHumidity);

        if (isEdit) {
            addDeviceTitleDialog.setText("Update information device");
            btnCreateDevice.setText("Update");
            enterNameDevice.setText(deviceForEdit.getValue());
            enterPositionDevice.setText(deviceForEdit.getPosition());
            switch (deviceForEdit.getType()) {
                case 1:
                    radioGroupDeviceType.check(R.id.radioButtonTemperature);
                    break;
                case 2:
                    radioGroupDeviceType.check(R.id.radioButtonSoilMoisture);
                    break;
                case 3:
                    radioGroupDeviceType.check(R.id.radioButtonAirHumidity);
                    break;
            }
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
                int type = radioGroupDeviceType.getCheckedRadioButtonId();

                //check fields empty
                if (type == R.id.radioButtonTemperature) {
                    Log.e("radioButtonTemperature", " " + R.id.radioButtonTemperature);
                    type = 1;
                } else if (type == R.id.radioButtonSoilMoisture) {
                    Log.e("radioButtonSoilMoisture", " " + R.id.radioButtonSoilMoisture);
                    type = 2;
                } else {
                    Log.e("radioButtonAirHumidity", " " + R.id.radioButtonAirHumidity);
                    type = 3;
                }

                //check fields empty

                //call view model
                if (isEdit) {
                    deviceForEdit.setName(name);
                    deviceForEdit.setPosition(position);
                    deviceForEdit.setType(type);
                    deviceListViewModel.updateDevice(deviceForEdit);
                } else {
                    //call view model
                    Device device = new Device(area.getId(), name, position, type, 0, false);
                    deviceListViewModel.insertDevice(device);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onEditClick(Device device) {
        deviceForEdit = device;
        showAddDialog(true);
    }

    @Override
    public void onDeleteClick(Device device) {
        deviceListViewModel.deleteDevice(device);
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
                        connectMQTT();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("subscribe", "topic>>" + topic);
                        JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                        Gson gson = new Gson();
                        Device device = gson.fromJson(jsonObject.toString(), Device.class);

                        int n = deviceList.size();
                        if (device instanceof Device) {
                            for (int i = 0; i < n; i++) {
                                int type = deviceList.get(i).getType();
                                if (topicName.compareTo(TEMPERATURE_TOPIC) == 0 && type == 1) {
                                    Log.e("test: ", "TEMPERATURE_TOPIC " + topicName.compareTo(TEMPERATURE_TOPIC) + "type" + type);
                                    deviceList.get(i).setValue(device.getValue());
                                } else if (topicName.compareTo(HUMIDITY_TOPIC) == 0 && type == 2) {
                                    Log.e("test: ", "HUMIDITY_TOPIC " + topicName.compareTo(HUMIDITY_TOPIC) + "type" + type);
                                    deviceList.get(i).setValue(device.getValue());
                                } else {
                                    Log.e("test: ", " WATER_TOPIC" + topicName.compareTo(SOILMOIST_TOPIC) + "type" + type);
                                    deviceList.get(i).setValue(device.getValue());
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
            Toast.makeText(DeviceManageActivity.this, "publish error", Toast.LENGTH_SHORT).show();
        }
    }

}
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


//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e("onStart", "onStart");
//        //bind to the service
//        bindService(new Intent(this, MyService.class),serviceConnection, Context.BIND_AUTO_CREATE);
//    }
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
















