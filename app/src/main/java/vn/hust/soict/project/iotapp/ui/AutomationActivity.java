package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Device;

public class AutomationActivity extends AppCompatActivity {
    private ImageButton startBtn, endBtn;
    private EditText startTime, endTime;
    private Calendar date;
    private ImageView imgWatering, imgLamp;
    private boolean isStart;
    private Button btnSave, btnCancel;
    private Switch controlLamp, controlWatering;
    private EditText garden, area;
    String batdau, ketthuc;
    Device lamp;
    //"6214ab6a148723f43b3fe225"
    Area areaObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automation);
        areaObject = (Area) getIntent().getSerializableExtra("area1");
        startBtn = (ImageButton) findViewById(R.id.btn_startTime);
        endBtn = findViewById(R.id.btn_endTime);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        startBtn.setOnClickListener(v -> {
            showDateTimePicker();
            isStart = true;
        });
        endBtn.setOnClickListener(v -> {
            showDateTimePicker();
            isStart = false;
        });
        imgLamp = findViewById(R.id.imgLamp1);
        imgWatering = findViewById(R.id.imgWatering1);
        controlLamp = (Switch) findViewById(R.id.controlLamp1);
        controlWatering = (Switch) findViewById(R.id.controlWatering1);
        controlLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgLamp.setImageResource(R.drawable.lamp_on);
                } else {
                    imgLamp.setImageResource(R.drawable.lamp_off);

                }
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
            }
        });

        garden = findViewById(R.id.et_chooseGarden);
        garden.setText(areaObject.getGarden());
        area = findViewById(R.id.et_chooseArea);
        area.setText(areaObject.getName());

        btnSave = findViewById(R.id.btn_saveAuto);
        btnSave.setOnClickListener(v -> {
            ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
            Call<Void> call = apiService.schedule(DataLocalManager.getTokenServer(), batdau,
                    ketthuc, controlLamp.isChecked(), lamp.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(AutomationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AutomationActivity.this, DeviceManageActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AutomationActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("schedule", "" + t);
                    Toast.makeText(AutomationActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
//
//            Toast.makeText(AutomationActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(AutomationActivity.this, DeviceManageActivity.class);
//                        startActivity(intent);
        });

        btnCancel = findViewById(R.id.btn_cancelAuto);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AutomationActivity.this, DeviceManageActivity.class);
            startActivity(intent);
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(AutomationActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(AutomationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
//                        date.set(Calendar.SECOND, second);
                        Log.v("TAG_AUTO", "The choosen one " + date.getTime());
                        if (isStart) {
                            startTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth + " " + hourOfDay + ":" + minute);
                            batdau = "0 " + minute + " " + hourOfDay + " " + dayOfMonth + " " + monthOfYear + " *";
                        } else {
                            endTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth + " " + hourOfDay + ":" + minute);
                            ketthuc = "0 " + minute + " " + hourOfDay + " " + dayOfMonth + " " + monthOfYear + " *";
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}