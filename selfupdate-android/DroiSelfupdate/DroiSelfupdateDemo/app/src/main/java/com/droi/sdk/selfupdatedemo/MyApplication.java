package com.droi.sdk.selfupdatedemo;

import android.app.Application;
import android.util.Log;

import com.droi.sdk.selfupdate.DroiUpdate;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //LogcatFileManager.getInstance().startLogcatManager(this);
        Log.e("MyApplication", "before initialize");
        DroiUpdate.initialize(this);
        Log.e("MyApplication", "after initialize");
    }
}
