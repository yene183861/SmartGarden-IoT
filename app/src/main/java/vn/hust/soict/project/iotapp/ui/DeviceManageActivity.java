package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotapp.adapter.GardenListAdapter;
import vn.hust.soict.project.iotapp.adapter.RealDeviceListAdapter;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.DataReceive;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.viewmodel.DeviceListViewModel;
import vn.hust.soict.project.iotapp.viewmodel.GardenListViewModel;
import vn.hust.soict.project.iotapp.viewmodel.RealDeviceListViewModel;

public class DeviceManageActivity extends AppCompatActivity implements DeviceListAdapter.ItemClickListener {
    private ImageView btnAddNewDevice, imgLamp, imgWatering;
    private TextView tvNoDeviceList, txtAreaName, txtAreaPosition, txtAcreage, time;
    private RecyclerView rcvDevice;
    private LinearLayout layoutControl, layoutLamp, layoutPump;
    private Switch controlLamp, controlWatering;
    private DeviceListViewModel deviceListViewModel;
    private List<Device> deviceList = new ArrayList<>();
    List<Device> deviceList1;
    boolean isHaveLamp = false;
    boolean isHavePump = false;
    Device lamp, pump;
    private DeviceListAdapter adapter;
    private Device deviceForEdit;
    public static final String CHANNEL_ID = "push_notification_id";
    MqttAndroidClient client;
    String topic = "iot-nhom8-20211/garden1/area1";
    public static final String TEMPERATURE_TOPIC = "iot-nhom8-20211/garden1/area1/dht11/temperature";
    public static final String HUMIDITY_TOPIC = "iot-nhom8-20211/garden1/area1/dht11/humidity";
    public static final String LAMP_TOPIC = "iot-nhom8-20211/garden1/area1/lamp";
    public static final String SOILMOIST_TOPIC = "iot-nhom8-20211/garden1/area1/soil";
    private Area area;

    private ImageButton btnTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        //getSupportActionBar().setTitle("Manage Device");
        area = (Area) getIntent().getSerializableExtra("area");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //Toobar ???? nh?? ActionBar
        actionBar.setTitle("Manage Device");
        initUi();
        connectMQTT();
        //initHandler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDevice.setLayoutManager(linearLayoutManager);
        deviceListViewModel = new ViewModelProvider(this).get(DeviceListViewModel.class);
        setRcvDevice();
        initListener();

        btnTimer = findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(v ->{
            Intent intent = new Intent(DeviceManageActivity.this, AutomationActivity.class);
            intent.putExtra("area1", area);
            startActivity(intent);
        });
    }

    private void initUi() {
        btnAddNewDevice = findViewById(R.id.btnAddNewDevice);
        rcvDevice = findViewById(R.id.rcvDevice);
        tvNoDeviceList = findViewById(R.id.tvNoDeviceList);
        layoutControl = findViewById(R.id.layoutControl);
        layoutLamp = findViewById(R.id.layoutLamp);
        layoutPump = findViewById(R.id.layoutPump);
        time = findViewById(R.id.time);
        controlLamp = (Switch) findViewById(R.id.controlLamp);
        controlWatering = (Switch) findViewById(R.id.controlWatering);
        imgLamp = findViewById(R.id.imgLamp);
        imgWatering = findViewById(R.id.imgWatering);
        txtAreaName = findViewById(R.id.txtAreaName);
        txtAreaPosition = findViewById(R.id.txtAreaPosition);
        txtAcreage = findViewById(R.id.txtAcreage);
        txtAreaName.setText("Name area: " + area.getName());
        txtAreaPosition.setText("Position: " + area.getPosition());
        txtAcreage.setText("Acreage: " + String.valueOf(area.getAcreage()) + " (m2)");
        layoutLamp.setVisibility(View.GONE);
        layoutPump.setVisibility(View.GONE);
    }

    private void setRcvDevice() {
        deviceList1 = new ArrayList<>();
        deviceList = deviceListViewModel.getDeviceList(area.getId());
        int size = deviceList.size();
        for (int i = 0; i < size; i++) {
            if (deviceList.get(i).getType() < 3 || deviceList.get(i).getType() == 5) {
                deviceList1.add(deviceList.get(i));
            } else if (deviceList.get(i).getType() == 3) {
                lamp = deviceList.get(i);
                isHaveLamp = deviceList.get(i).isStatus();
                layoutLamp.setVisibility(View.VISIBLE);
                if (isHaveLamp) {
                    imgLamp.setImageResource(R.drawable.lamp_on);
                    controlLamp.setChecked(true);
                }
            } else {
                pump = deviceList.get(i);
                isHavePump = deviceList.get(i).isStatus();
                layoutPump.setVisibility(View.VISIBLE);
                if (isHavePump) {
                    imgWatering.setImageResource(R.drawable.watering);
                    controlWatering.setChecked(true);
                }
            }
        }
        if (deviceList1 == null) {
            rcvDevice.setVisibility(View.GONE);
            tvNoDeviceList.setVisibility(View.VISIBLE);
        } else {
            tvNoDeviceList.setVisibility(View.GONE);
            rcvDevice.setVisibility(View.VISIBLE);
        }
        adapter = new DeviceListAdapter(this, deviceList1, (DeviceListAdapter.ItemClickListener) this);
        adapter.setDeviceList(deviceList1);
        rcvDevice.setAdapter(adapter);
        deviceListViewModel.getDeviceListObserver().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                if (devices == null) {
                    rcvDevice.setVisibility(View.GONE);
                    tvNoDeviceList.setVisibility(View.VISIBLE);
                } else {
                    deviceList = devices;
                    deviceList1.clear();
                    int size = deviceList.size();
                    isHaveLamp = false;
                    isHavePump = false;
                    for (int i = 0; i < size; i++) {
                        if (deviceList.get(i).getType() < 3 || deviceList.get(i).getType() == 5) {
                            deviceList1.add(deviceList.get(i));
                        } else if (deviceList.get(i).getType() == 3) {
                            lamp = deviceList.get(i);
                            isHaveLamp = deviceList.get(i).isStatus();
                            layoutLamp.setVisibility(View.VISIBLE);
                            if (isHaveLamp) {
                                imgLamp.setImageResource(R.drawable.lamp_on);
                                controlLamp.setChecked(true);
                            }
                        } else {
                            pump = deviceList.get(i);
                            isHavePump = deviceList.get(i).isStatus();
                            layoutPump.setVisibility(View.VISIBLE);
                            if (isHavePump) {
                                imgWatering.setImageResource(R.drawable.watering);
                                controlWatering.setChecked(true);
                            }
                        }
                    }
                    adapter.setDeviceList(deviceList1);
                    rcvDevice.setAdapter(adapter);
                    tvNoDeviceList.setVisibility(View.GONE);
                    rcvDevice.setVisibility(View.VISIBLE);
                }
            }
        });

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
                if (isHavePump) {
                    if (isChecked) {
                        imgWatering.setImageResource(R.drawable.watering);
                    } else {
                        imgWatering.setImageResource(R.drawable.not_watering);
                    }
                    int type;
                    if (isChecked) {
                        type = 0;
                    } else type = 1;
                    publish(0, type, SOILMOIST_TOPIC);
                } else {
                    controlWatering.setChecked(false);
                    Toast.makeText(DeviceManageActivity.this, "You must bind pump with real device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        controlLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isHaveLamp) {
                    if (isChecked) {
                        imgLamp.setImageResource(R.drawable.lamp_on);
                    } else {
                        imgLamp.setImageResource(R.drawable.lamp_off);
                    }
                    int type; // 1 l?? off, 0 l?? on
                    if (isChecked) {
                        type = 0;
                    } else type = 1;
                    publish(1, type, LAMP_TOPIC);
                } else {
                    controlLamp.setChecked(false);
                    Toast.makeText(DeviceManageActivity.this, "You must bind lamp with real device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layoutLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lamp.isStatus() == false){
                    Intent intent = new Intent(DeviceManageActivity.this, BindActivity.class);
                    intent.putExtra("device", lamp);
                    startActivity(intent);
                }
            }
        });
        layoutPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pump.isStatus() == false){
                    Intent intent = new Intent(DeviceManageActivity.this, BindActivity.class);
                    intent.putExtra("device", pump);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        disconnectMQTT();
        Log.e("disconnectMQTT", "success");
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
        RadioButton radioButtonLamp = dialogView.findViewById(R.id.radioButtonLamp);
        RadioButton radioButtonPump = dialogView.findViewById(R.id.radioButtonPump);
        enterPositionDevice.setText(area.getPosition());

        if (isEdit) {
            addDeviceTitleDialog.setText("Update information device");
            btnCreateDevice.setText("Update");
            enterNameDevice.setText(deviceForEdit.getName());
            enterPositionDevice.setText(deviceForEdit.getPosition());
            switch (deviceForEdit.getType()) {
                case 2:
                    radioGroupDeviceType.check(R.id.radioButtonSoilMoisture);
                    break;
                case 3:
                    radioGroupDeviceType.check(R.id.radioButtonLamp);
                    break;
                case 4:
                    radioGroupDeviceType.check(R.id.radioButtonPump);
                    break;
                default:
                    radioGroupDeviceType.check(R.id.radioButtonTemperature);

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

                if (type == R.id.radioButtonTemperature) {
                    type = 1;
                } else if (type == R.id.radioButtonSoilMoisture) {
                    type = 2;
                } else if (type == R.id.radioButtonLamp) {
                    type = 3;
                } else {
                    type = 4;
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
                    Device device = new Device(area.getId(), name, area.getPosition(), type, false);
                    deviceList = deviceListViewModel.insertDevice(device);
                    if (type == 1) {
                        Device device1 = new Device(area.getId(), "????? ???m kh??ng kh??", area.getPosition(), 5, false);
                        deviceList = deviceListViewModel.insertDevice(device1);
                    }
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onDeviceClick(Device device) {
        if(device.isStatus() == false){
            Intent intent = new Intent(DeviceManageActivity.this, BindActivity.class);
            intent.putExtra("device", device);
            startActivity(intent);
        }
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
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            time.setText("Time: " + LocalDateTime.now());
                            time.setVisibility(View.VISIBLE);
                            //Log.e("time", "" + LocalDateTime.now());
                        }
                        Log.d("subscribe", "topic>>" + topic);
                        Log.e("message", new String(message.getPayload()));
                        String json = new String(message.getPayload());
                        Gson gson = new Gson(); // kh???i t???o Gson
                        DataReceive dataReceive = gson.fromJson(json, DataReceive.class);

                        int size = deviceList1.size();
                        for (int i = 0; i < size; i++) {
                            int type = deviceList1.get(i).getType();
                            if (type == 1 && deviceList1.get(i).isStatus()) {
                                deviceList1.get(i).setValue(dataReceive.getTemperature());
                            } else if (type == 2 && deviceList1.get(i).isStatus()) {
                                Log.e("Humidity_air", "dataReceive.getHumidity_air()");
                                deviceList1.get(i).setValue(dataReceive.getSoil());
                            } else if(type == 5 && deviceList1.get(i).isStatus()){
                                Log.e("getHumidity_soil", "dataReceive.getHumidity_air()");
                                deviceList1.get(i).setValue(dataReceive.getHumidity());
                            }
                        }
                        adapter.setDeviceList(deviceList1);
                        rcvDevice.setAdapter(adapter);
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

    private void publish(int type, int status, String topic) {
        if (client.isConnected()) {
            try {
                Map<String, Integer> inputMap = new HashMap<String, Integer>();
                if (type == 1) {
                    inputMap.put("Lamp", status);
                } else {
                    inputMap.put("Water", status);
                }
                Gson gson = new Gson();
                String json = gson.toJson(inputMap);
                Log.e("mess publish", "" + json);
                MqttMessage message = new MqttMessage(json.getBytes("UTF-8"));
                client.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            //Toast.makeText(DeviceManageActivity.this, "publish error", Toast.LENGTH_SHORT).show();
            Log.e("error", "publish error");
        }
    }

}


















