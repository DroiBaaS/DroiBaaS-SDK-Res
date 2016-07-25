package com.droi.sdk.selfupdatedemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.droi.sdk.core.Core;
import com.droi.sdk.core.SelfUpdateCoreHelper;
import com.droi.sdk.selfupdate.DroiInappUpdateListener;
import com.droi.sdk.selfupdate.DroiInappUpdateResponse;
import com.droi.sdk.selfupdate.DroiUpdate;
import com.droi.sdk.selfupdate.DroiUpdateListener;
import com.droi.sdk.selfupdate.InappUpdateFailCode;
import com.droi.sdk.selfupdate.UpdateStatus;
import com.droi.sdk.selfupdate.DroiUpdateResponse;

import java.io.File;

public class MainActivity extends Activity {

    static Context mContext;
    TextView channel;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.droi_example_activity_main);
        setClick();
        TextView appId = (TextView) findViewById(R.id.app_id);
        appId.setText(SelfUpdateCoreHelper.getAppId());
        channel = (TextView) findViewById(R.id.channel);
        channel.setText(Core.getChannelName(this));
        TextView versionCode = (TextView) findViewById(R.id.version_code);
        versionCode.setText(getAppVersionCode(this));
    }

    public static String getAppVersionCode(Context context) {
        try {
            PackageInfo e = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int appVersionCode = e.versionCode;
            return String.valueOf(appVersionCode);
        } catch (PackageManager.NameNotFoundException var3) {
            return "";
        }
    }

    private void setClick() {
        Button normalUpdate = (Button) findViewById(R.id.normal_ui_upate);
        normalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "");
                channel.setText(Core.getChannelName(mContext));
                DroiUpdate.setDefault();
                DroiUpdate.update(mContext);
            }
        });
        Button customUpdate = (Button) findViewById(R.id.custom_ui_update);
        customUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DroiUpdate.setDefault();
                DroiUpdate.setUpdateAutoPopup(false);
                DroiUpdate.setUpdateListener(new DroiUpdateListener() {

                    @Override
                    public void onUpdateReturned(
                            int updateStatus,
                            DroiUpdateResponse updateInfo) {
                        switch (updateStatus) {
                            case UpdateStatus.NO:
                                Toast.makeText(mContext, R.string.update_status_no, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.YES:
                                Toast.makeText(mContext, R.string.update_status_yes, Toast.LENGTH_SHORT).show();
                                //你可以在此回调处实现自己的更新UI
                                //在用户点击更新时调用下面的函数
                                DroiUpdate.downloadApp(mContext, updateInfo, null);
                                break;
                            case UpdateStatus.ERROR:
                                Toast.makeText(mContext, R.string.update_status_error, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.TIMEOUT:
                                Toast.makeText(mContext, R.string.update_status_timeout, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NON_WIFI:
                                Toast.makeText(mContext, R.string.update_status_nonwifi, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.UPDATING:
                                Toast.makeText(mContext, R.string.update_status_updating, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "");
                channel.setText(Core.getChannelName(mContext));
                DroiUpdate.setUpdateAutoPopup(false);
                DroiUpdate.update(mContext);
            }
        });

        Button silentUpdate = (Button) findViewById(R.id.silent_update);
        silentUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "DROI_CHANNEL2");
                channel.setText("DROI_CHANNEL2");
                DroiUpdate.setDefault();
                DroiUpdate.update(mContext);
            }
        });

        Button forceUpdate = (Button) findViewById(R.id.force_upadte);
        forceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "DROI_CHANNEL3");
                channel.setText("DROI_CHANNEL3");
                DroiUpdate.setDefault();
                DroiUpdate.update(mContext);
            }
        });

        Button manualUpdate = (Button) findViewById(R.id.manual_update);
        manualUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "");
                channel.setText(Core.getChannelName(mContext));
                DroiUpdate.setDefault();
                DroiUpdate.manualUpdate(mContext);
            }
        });

        Button delete = (Button) findViewById(R.id.delet_update_file);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "");
                channel.setText(Core.getChannelName(mContext));
                deleteFile();
                DroiUpdate.setUpdateIgnore(mContext, null);
            }
        });

        Button marketUpdate = (Button) findViewById(R.id.market_upadte);
        marketUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "DROI_CHANNEL4");
                channel.setText("DROI_CHANNEL4");
                DroiUpdate.setDefault();
                DroiUpdate.update(mContext);
            }
        });

        Button inappUpdate = (Button) findViewById(R.id.inapp_update);
        inappUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTestChannel方法只是demo中使用，请勿在生成环境中使用此方法，生成环境中需要在后台配置
                DroiUpdate.setTestChannel(mContext, "DROI_CHANNEL4");
                channel.setText("DROI_CHANNEL4");
                String fileVersion = "1.0";
                String filePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "my.cnf";
                DroiUpdate.inappUpdate(mContext, fileVersion,
                        filePath, new DroiInappUpdateListener() {

                            @Override
                            public void onStart(long l, DroiInappUpdateResponse response) {

                            }

                            @Override
                            public void onProgress(float v) {

                            }

                            @Override
                            public void onFinished(String s, DroiInappUpdateResponse response) {
                                showToastInUiThread(getString(R.string.update_inappupdate_yes));
                            }

                            @Override
                            public void onFailed(int i) {
                                switch (i) {
                                    case InappUpdateFailCode.NO_UPDATE:
                                        showToastInUiThread(getString(R.string.update_inappupdate_no));
                                        break;
                                    case InappUpdateFailCode.DOWNLOAD_FAILED:
                                        showToastInUiThread(getString(R.string.update_inappupdate_download_failed));
                                        break;
                                    case InappUpdateFailCode.REQUEST_FAILED:
                                        showToastInUiThread(getString(R.string.update_inappupdate_request_failed));
                                        break;
                                }
                            }
                        });
            }
        });

        Button deleteInappFile = (Button) findViewById(R.id.delete_inapp_update_file);
        deleteInappFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "my.cnf";
                File file = new File(filePath);
                if (file != null && file.exists()) {
                    if (file.delete()) {
                        Toast.makeText(mContext, "删除完成",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "删除失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "删除完成", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public static void deleteFile() {
        DroiUpdate.setUpdateListener(new DroiUpdateListener() {

            @Override
            public void onUpdateReturned(int statusCode,
                                         DroiUpdateResponse updateResponse) {
                DroiUpdate.setDefault();
                if (statusCode == UpdateStatus.YES) {
                    if (updateResponse != null) {
                        File file = DroiUpdate.getDownloadedFile(mContext,
                                updateResponse);
                        if (file != null) {
                            if (file.delete()) {
                                Toast.makeText(mContext, "删除完成",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "删除失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "删除完成", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
        DroiUpdate.setUpdateAutoPopup(false);
        DroiUpdate.setUpdateOnlyWifi(false);
        DroiUpdate.update(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogcatFileManager.getInstance().stopLogcatManager();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showToastInUiThread(final String stringRes) {

        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(mContext.getApplicationContext(),
                            stringRes, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(stringRes);
                }
                toast.show();
            }
        });
    }
}
