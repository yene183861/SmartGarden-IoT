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
import vn.hust.soict.project.iotapp.model.Garden;


public class GardenListViewModel extends ViewModel {
    private MutableLiveData<List<Garden>> gardenListLiveData;
    private List<Garden> gardenList;
    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);


    public GardenListViewModel() {
        gardenListLiveData = new MutableLiveData<>();
        gardenList = new ArrayList<>();
    }
    public void getGardenList(){
        Call<List<Garden>> call = apiService.getGardenList(DataLocalManager.getTokenServer());
        call.enqueue(new Callback<List<Garden>>() {
            @Override
            public void onResponse(Call<List<Garden>> call, Response<List<Garden>> response) {
                gardenListLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Garden>> call, Throwable t) {
                gardenListLiveData.postValue(null);
            }
        });
    }

    public MutableLiveData<List<Garden>> getGardenListObserver() {
        return gardenListLiveData;
    }

    public void insertGarden(Garden garden){
//        gardenList.add(garden);
//        adapter.setGardenList(gardenList);
//        gardenListLiveData.setValue(gardenList);
    }
    public void updateGarden(String id, Garden garden){
        gardenListLiveData.setValue(gardenList);
    }
    public void deleteGarden(Garden garden){

    }
}
