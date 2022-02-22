package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotapp.adapter.RealDeviceListAdapter;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.viewmodel.RealDeviceListViewModel;

public class BindActivity extends AppCompatActivity implements RealDeviceListAdapter.ItemClickListener{
    private TextView tvNameDevice, tvAreaDevice, tvNameRealDevice;
    private RecyclerView rcvRealDevice;
    private Button btnCancel, btnSave;
    private Device device, virtualDevice, realDevice;
    private RealDeviceListAdapter adapter;
    private RealDeviceListViewModel listViewModel;
    private List<Device> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        initUi();
        device = (Device) getIntent().getSerializableExtra("device");
        Log.e("device:", "" + device.getName());
        Log.e("device:", "" + device.getArea());
        //getSupportActionBar().setTitle("Bind Device");
        virtualDevice = device;
        Log.e("virtualDevice", virtualDevice.getId());
        tvNameDevice.setText(device.getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvRealDevice.setLayoutManager(linearLayoutManager);
        adapter = new RealDeviceListAdapter(this, list, (RealDeviceListAdapter.ItemClickListener) this);
        rcvRealDevice.setAdapter(adapter);
        //gardenList = gardenListViewModel.getGardenList();
        listViewModel = new ViewModelProvider(this).get(RealDeviceListViewModel.class);
        listViewModel.getRealDevice(device.getType());
        listViewModel.getListObserver().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                if (devices == null) {
                    rcvRealDevice.setVisibility(View.GONE);
                } else {
                    list = devices;
                    adapter.setList(devices);
                    rcvRealDevice.setVisibility(View.VISIBLE);
                }

            }
        });
        //listViewModel.getDeviceList("id");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindActivity.this, DeviceManageActivity.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
                Call<Void> call = apiService.bindDevice(DataLocalManager.getTokenServer(), realDevice.getId(), virtualDevice.getId());
                Log.e("deviceChoose.getId()", ""+ realDevice.getId());
                Log.e("id real", " " + device.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(BindActivity.this, "Ghep noi thanh cong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BindActivity.this, "Ghep noi that bai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("bindDevice", "onFailure" + t);
                        Toast.makeText(BindActivity.this, "Call api failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    private void initUi() {
        tvNameDevice = findViewById(R.id.tv_name_device);
        tvAreaDevice = findViewById(R.id.tv_area_device);
        tvAreaDevice.setVisibility(View.GONE);
        tvNameRealDevice = findViewById(R.id.tv_name_realdevice);
        rcvRealDevice = findViewById(R.id.rcvRealDevice);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
    }

    @Override
    public void onRealClick(Device device) {
        realDevice = device;
        Log.e("deviceChoose", "" +realDevice.getId());
        Log.e("realDevice choose: ", "" +realDevice.getId());
        tvNameRealDevice.setText(device.getName());
    }
}
