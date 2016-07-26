package com.droi.sdk.featuretestapp;

import android.app.Application;
import android.util.Log;

import com.droi.sdk.core.Core;
import com.droi.sdk.core.DroiObject;


public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Core.initialize(this);

        DroiObject.registerCustomClass(TestData.class);
        DroiObject.registerCustomClass(CustomAuthor.class);
        DroiObject.registerCustomClass(CustomBook.class);
        DroiObject.registerCustomClass(MySongs.class);
    }
}
