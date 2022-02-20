package vn.hust.soict.project.iotapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Garden;


public class AreaListViewModel extends ViewModel {
    private MutableLiveData<List<Area>> areaListLiveData;
    private List<Area> areaList;

    public AreaListViewModel() {
        areaListLiveData = new MutableLiveData<>();
        areaList = new ArrayList<>();
//        gardenListLiveData.setValue(gardenList);
    }
    public void getAreaList(String id){
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<List<Area>> call = apiService.getAreaList(DataLocalManager.getTokenServer(), id);
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                areaListLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                areaListLiveData.postValue(null);
            }
        });
    }

    public MutableLiveData<List<Area>> getAreaListObserver() {
        return areaListLiveData;
    }

    public void insertArea(Area area){
        areaList.add(area);
        areaListLiveData.setValue(areaList);
    }
    public void updateArea(Area area){
        areaListLiveData.setValue(areaList);

    }
    public void deleteArea(Area area){
//        areaListLiveData.delete(areaList);
    }
}