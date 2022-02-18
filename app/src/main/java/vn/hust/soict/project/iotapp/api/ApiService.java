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

    @GET("/getGardens")
    Call<List<Garden>> getGardenList(@Header("token") String token);

    @GET("/getArea")
    Call<List<Area>> getAreaList(@Header("token") String token,@Query("id") String id);
    @GET("/getDevice")
    Call<List<Device>> getDeviceList(@Header("token") String token,@Query("id") String id);
}

