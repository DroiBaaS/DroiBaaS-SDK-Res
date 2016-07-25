package com.droi.sdk.analyticsdemo;

import android.app.Application;
import android.util.Log;

import com.droi.sdk.analytics.DroiAnalytics;
import com.droi.sdk.analytics.Logger;
import com.droi.sdk.analytics.SendPolicy;

/**
 * Created by chenpei on 16/1/5.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyApplication", "before initialize");
        DroiAnalytics.initialize(this);
        Log.e("MyApplication", "after initialize");
        // DroiAnalytics.setCrashReport(true);
        DroiAnalytics.setCrashReportWithLog(true);
        DroiAnalytics.setSandboxMode(true);
        // DroiAnalytics.enableActivityLifecycleCallbacks(this, true);
        // DroiAnalytics.setDefaultSendPolicy(SendPolicy.SCHEDULE);
        // DroiAnalytics.setScheduleConfig(false, 10);
        Logger.i("MyApplication", "onCreate");
    }
}
