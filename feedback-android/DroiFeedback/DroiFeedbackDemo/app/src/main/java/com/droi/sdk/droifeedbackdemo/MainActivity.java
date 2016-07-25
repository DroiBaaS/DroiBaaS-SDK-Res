package com.droi.sdk.droifeedbackdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.droi.sdk.core.Core;
import com.droi.sdk.core.FeedbackCoreHelper;
import com.droi.sdk.feedback.DroiFeedback;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    TextView userIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context mContext = this;
        this.setContentView(R.layout.droi_example_activity_main);
        Log.i("FeedbackCoreHelper", FeedbackCoreHelper.getDeviceId(this));
        TextView appId = (TextView) findViewById(R.id.app_id);
        appId.setText(FeedbackCoreHelper.getAppId());
        Button feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DroiFeedback.callFeedback(mContext.getApplicationContext());
            }
        });
        Button userDefined = (Button) findViewById(R.id.user_defined);
        userDefined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DroiFeedback.setTitleBarColor(0xFFFF9900);
                DroiFeedback.setSendButtonColor(0xFFFF9900, 0xFFFF9900);
            }
        });
        Button setDefault = (Button) findViewById(R.id.set_default);
        setDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DroiFeedback.setTitleBarColor(0);
                DroiFeedback.setSendButtonColor(0, 0);
            }
        });
        userIdTextView = (TextView) findViewById(R.id.user_id);
        Button feedbackAfterSetUserId = (Button) findViewById(R.id.feedback_set_userid);
        feedbackAfterSetUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userIdTextView.setText(DroiFeedback.getUserId());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.feedback_set_userid_text));
        builder.setMessage(getString(R.string.feedback_set_userid_text));
        final EditText edittext = new EditText(this);
        builder.setView(edittext);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String userId = edittext.getText().toString();
                if (!userId.equals("")) {
                    DroiFeedback.setUserId(userId);
                }
                userIdTextView.setText(DroiFeedback.getUserId());
            }
        });
        builder.create().show();
    }
}
