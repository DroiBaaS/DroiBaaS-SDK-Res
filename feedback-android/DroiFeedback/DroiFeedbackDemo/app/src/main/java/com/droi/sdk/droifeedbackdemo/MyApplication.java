package com.droi.sdk.droifeedbackdemo;

import android.app.Application;
import android.util.Log;

import com.droi.sdk.feedback.DroiFeedback;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogcatFileManager.getInstance().startLogcatManager(this);
        Log.e("MyApplication", "before initialize");
        DroiFeedback.initialize(this);
        Log.e("MyApplication", "after initialize");
    }
}
