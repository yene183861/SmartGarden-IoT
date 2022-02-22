package vn.hust.soict.project.iotapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Device;


public class RealDeviceListViewModel extends ViewModel {
    private MutableLiveData<List<Device>> deviceListLiveData;
    private List<Device> deviceList;
    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);

    public RealDeviceListViewModel() {
        deviceListLiveData = new MutableLiveData<>();
        deviceList = new ArrayList<>();
    }

    public List<Device> getDeviceList(String id) {
        Call<List<Device>> call = apiService.getDeviceList(DataLocalManager.getTokenServer(), id);// Log.e("request", )
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.code() == 200) {
                    deviceList = response.body();
                    deviceListLiveData.setValue(deviceList);
                    Log.e("getDevice", "success");
                    Log.e("getDevice", "" + deviceList.size());
                } else {
                    Log.e("getDevice", "failed: " + response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Log.e("getDevice", "error: " + t);
                deviceList = null;
            }
        });
        return deviceList;
    }

    public MutableLiveData<List<Device>> getDeviceListObserver() {
        return deviceListLiveData;
    }

    public void insertDevice(Device device) {
        Call<Device> call = apiService.createDevice(DataLocalManager.getTokenServer(), device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    Log.e("createDevice", "success");
                    if (device.getType() == 2 || device.getType() == 1 || device.getType() == 5) {
                        //deviceList.add(device);
                        deviceList = getDeviceList(device.getArea());
                    }
                    //deviceListLiveData.setValue(deviceList);
                    //deviceList = getDeviceList(device.getArea());
                } else {
                    try {
                        Log.e("createDevice", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("createDevice", "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.e("createDevice", "onFailure" + t);
            }
        });
    }

    public void updateDevice(Device device) {
        deviceListLiveData.setValue(deviceList);

    }

    public void deleteDevice(Device device) {

    }
}
