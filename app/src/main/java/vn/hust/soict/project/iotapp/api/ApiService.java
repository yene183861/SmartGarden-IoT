package vn.hust.soict.project.iotapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Device;
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.model.LoginResponse;
import vn.hust.soict.project.iotapp.model.User;

public interface ApiService {

    @POST("/register")
    Call<User> registerUser(@Body User user);

    @POST("/login")
    Call<LoginResponse> login(@Body User user);

    @GET("/gardens")
    Call<List<Garden>> getGardenList(@Header("token") String token);

    @POST("/gardens/create")
    Call<Garden> createGarden(@Header("token") String token, @Body Garden garden);


    @GET("/areas")
    Call<List<Area>> getAreaList(@Header("token") String token, @Query("gardenId") String id);

    @POST("/areas/create")
    Call<Area> createArea(@Header("token") String token, @Body Area area);

    @GET("/devices")
    Call<List<Device>> getDeviceList(@Header("token") String token, @Query("areaId") String id);

    @POST("/devices/create")
    Call<Device> createDevice(@Header("token") String token, @Body Device device);

    @POST("")
    Call<Void> bind(@Header("token") String token,String id);
}
