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
    private MutableLiveData<List<Device>> listLiveData;
    private List<Device> list;
    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);

    public RealDeviceListViewModel() {
        listLiveData = new MutableLiveData<>();
        list = new ArrayList<>();
    }

    public void getRealDevice(int type) {
        Call<List<Device>> call = apiService.getRealDevices(DataLocalManager.getTokenServer(), type);
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.code() == 200) {
                    list = response.body();
                    listLiveData.setValue(list);
                    Log.e("getRealDevice", "success");
                    Log.e("getRealDevice", ""+ list.size());
                } else {
                    Log.e("getRealDevice", "failed: " + response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Log.e("getRealDevice", "error: " + t);
            }
        });
    }

    public MutableLiveData<List<Device>> getListObserver() {
        return listLiveData;
    }

}
