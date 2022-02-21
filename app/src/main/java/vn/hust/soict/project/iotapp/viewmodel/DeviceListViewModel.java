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
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.model.Garden;


public class DeviceListViewModel extends ViewModel {
    private MutableLiveData<List<Device>> deviceListLiveData;
    private List<Device> deviceList;
    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);

    public DeviceListViewModel() {
        deviceListLiveData = new MutableLiveData<>();
        deviceList = new ArrayList<>();
//        gardenListLiveData.setValue(gardenList);
    }
    public List<Device> getDeviceList(String id){
        //ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<List<Device>> call = apiService.getDeviceList(DataLocalManager.getTokenServer(), id);// Log.e("request", )
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if(response.code() == 200) {
                    deviceList = response.body();
                    deviceListLiveData.setValue(deviceList);
                    Log.e("getArea", "success");
                } else {
                    Log.e("getArea", "failed: " + response.code() + " " + response.errorBody());
                }}

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Log.e("getArea", "error: " + t);
                deviceList = null;
            }
        });
        return deviceList;
    }

    public MutableLiveData<List<Device>> getDeviceListObserver() {
        return deviceListLiveData;
    }

    public void insertDevice(Device device){
        Call<Device> call = apiService.createDevice(DataLocalManager.getTokenServer(), device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    deviceList.add(device);
                    deviceListLiveData.setValue(deviceList);
                    Log.e("createArea", "insert success");
                } else {
                    try {
                        Log.e("createArea", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("createArea", "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.e("createArea", "onFailure" + t);
            }
        });
    }
    public void updateDevice(Device device){
        deviceListLiveData.setValue(deviceList);

    }
    public void deleteDevice(Device device){

    }
}
