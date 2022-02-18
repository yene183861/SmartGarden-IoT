package vn.hust.soict.project.iotapp.datalocal;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context context;

    public MySharedPreferences(Context context){
        this.context = context;
    }

    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES,
              Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getStringValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
