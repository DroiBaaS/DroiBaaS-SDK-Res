package com.droi.sdk.analyticsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.droi.sdk.analyticsdemo.R;
import com.droi.sdk.analytics.DroiAnalytics;
import com.droi.sdk.analytics.Logger;

/**
 * Created by chenpei on 16/1/5.
 */
public class Activity1 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity2", "onCreate()");
        setContentView(R.layout.droi_example_activity1);
        Button backButton = (Button) findViewById(R.id.crash_button2);
        backButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               finish();
                                           }
                                       }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("Activity1", "onResume()");
        DroiAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i("Activity1", "onPause()");
        DroiAnalytics.onPause(this);
    }
}
