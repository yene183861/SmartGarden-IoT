package vn.hust.soict.project.iotapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.model.Garden;


public class DeviceListViewModel extends ViewModel {
    private MutableLiveData<List<Device>> deviceListLiveData;
    private List<Device> deviceList;

    public DeviceListViewModel() {
        deviceListLiveData = new MutableLiveData<>();
        deviceList = new ArrayList<>();
//        gardenListLiveData.setValue(gardenList);
    }
    public List<Device> getDeviceList(String id){
//        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
//        Call<List<Device>> call = apiService.getDeviceList(DataLocalManager.getTokenServer(), id);
//        call.enqueue(new Callback<List<Device>>() {
//            @Override
//            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
//                deviceListLiveData.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<Device>> call, Throwable t) {
//                deviceListLiveData.postValue(null);
//            }
//        });
        return deviceList;
    }

    public MutableLiveData<List<Device>> getDeviceListObserver() {
        return deviceListLiveData;
    }

    public void insertDevice(Device device){
        deviceList.add(device);
        deviceListLiveData.setValue(deviceList);
    }
    public void updateDevice(Device device){
        deviceListLiveData.setValue(deviceList);

    }
    public void deleteDevice(Device device){

    }
}
