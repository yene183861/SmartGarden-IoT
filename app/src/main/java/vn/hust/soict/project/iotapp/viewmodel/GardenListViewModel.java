package vn.hust.soict.project.iotapp.viewmodel;

import android.util.Log;
import android.widget.Toast;

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
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.ui.GardenManageActivity;


public class GardenListViewModel extends ViewModel {
    private MutableLiveData<List<Garden>> gardenListLiveData;
    private List<Garden> gardenList;
    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);


    public GardenListViewModel() {
        gardenListLiveData = new MutableLiveData<>();
        gardenList = new ArrayList<>();
    }
    public List<Garden> getGardenList(){
        Call<List<Garden>> call = apiService.getGardenList(DataLocalManager.getTokenServer());
        call.enqueue(new Callback<List<Garden>>() {
            @Override
            public void onResponse(Call<List<Garden>> call, Response<List<Garden>> response) {
                if(response.code() == 200) {
                    gardenList = response.body();
                    gardenListLiveData.setValue(gardenList);
                    Log.e("getGarden", "success");
                } else {
                    Log.e("getGarden", "failed: " + response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Garden>> call, Throwable t) {
                Log.e("getGarden", "error: " + t);
            }
        });
        return gardenList;
    }

    public MutableLiveData<List<Garden>> getGardenListObserver() {
        return gardenListLiveData;
    }

    public void insertGarden(Garden garden){
        Call<Garden> call = apiService.createGarden(DataLocalManager.getTokenServer(), garden);
        call.enqueue(new Callback<Garden>() {
            @Override
            public void onResponse(Call<Garden> call, Response<Garden> response) {
                if (response.code() == 200) {
                    gardenList.add(garden);
                    gardenListLiveData.setValue(gardenList);
                    Log.e("createGarden", "insert success");
                } else {
                    try {
                        Log.e("createGarden", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("createGarden", "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Garden> call, Throwable t) {
                Log.e("createGarden", "onFailure" + t);
            }
        });
    }
    public void updateGarden(String id, Garden garden){
        gardenListLiveData.setValue(gardenList);
    }
    public void deleteGarden(Garden garden){

    }
}
