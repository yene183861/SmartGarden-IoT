package vn.hust.soict.project.iotapp.datalocal;

import android.content.Context;

public class DataLocalManager {
    private static final String TOKEN_SERVER = "TOKEN_SERVER";
    private static final String CLIENT_ID = "CLIENT_ID";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setTokenServer(String tokenServer){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(TOKEN_SERVER,tokenServer);
    }
    public static String getTokenServer(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(TOKEN_SERVER);
    }
    public static void setClientId(String id){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(CLIENT_ID,id);
    }
    public static String getClientId(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(CLIENT_ID);
    }
}
