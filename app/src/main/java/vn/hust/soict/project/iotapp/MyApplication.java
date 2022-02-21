package vn.hust.soict.project.iotapp;

import android.app.Application;

import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }

}