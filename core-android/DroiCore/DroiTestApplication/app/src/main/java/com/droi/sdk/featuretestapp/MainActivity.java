package com.droi.sdk.featuretestapp;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.droi.sdk.DroiError;
import com.droi.sdk.core.DroiCloudCache;
import com.droi.sdk.core.DroiPreference;
import com.droi.sdk.core.DroiRunnable;
import com.droi.sdk.core.DroiTask;
import com.droi.sdk.core.TaskDispatcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentState = (TextView) findViewById(R.id.currentState);
    }

    public void onClickDataTestButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - Data(init)", "Please wait...", true);

        DroiTask task = createDataTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }


    public void onClickACLTestButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - ACL", "Please wait...", true);
        DroiTask task = createACLTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }


    public void onClickClearAllDataButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - Clear ALL DATA", "Please wait...", true);
        DroiTask task = createClearDataTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    public void onClickDroiFileUploadButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - DroiFile Upload", "Please wait...", true);
        DroiTask task = createDroiFileUploadTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    public void onClickDroiFileDownloadButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - DroiFile Download", "Please wait...", true);
        DroiTask task = createDroiFileDownloadTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    public void onClickReferenceButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - DroiReferenceTest", "Please wait...", true);
        DroiTask task = createReferenceTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    public void onClickPreferenceButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - DroiPeferenceTest", "Please wait...", true);
        DroiTask task = createPreferenceTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    public void onClickCloudCacheButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running - DroiCloudCacheTest", "Please wait...", true);
        DroiTask task = createCloudCacheTestTask(dialog);
        task.callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        task.runInBackground("DTA_Background_Thread");
    }

    final private static int TIMES = 10;

    public void onClick10TimesTestButton(View v) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Test Running", "Please wait...", true);

        findViewById(R.id.tentimes_test).setEnabled(false);
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                DroiTask clearTask = createClearDataTestTask(dialog);
                DroiTask dataTask = createDataTestTask(dialog);
                DroiTask referenceTask = createReferenceTestTask(dialog);
                DroiTask aclTask = createACLTestTask(dialog);


                for (int i = 0; i < TIMES; i++) {

                    final String message = String.format("Please wait...%d/%d", i + 1, TIMES);

                    TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setMessage(message);
                        }
                    });

                    clearTask.resetState();

                    TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("Test Running - clear data");
                        }
                    });
                    clearTask.runAndWait("Clear_Data_Background_Thread");
                    if (clearTask.isCancelled()) {
                        cancel();
                        break;
                    }

                    dataTask.resetState();

                    TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("Test Running - Data(init)");
                        }
                    });
                    dataTask.runAndWait("DTA_Background_Thread");
                    if (dataTask.isCancelled()) {
                        cancel();
                        break;
                    }

                    referenceTask.resetState();

                    TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("Test Running - reference DroiObject");
                        }
                    });
                    referenceTask.runAndWait("Reference_Background_Thread");
                    if (referenceTask.isCancelled()) {
                        cancel();
                        break;
                    }

                    aclTask.resetState();

                    TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("Test Running - ACL");
                        }
                    });
                    aclTask.runAndWait("DTA_Background_Thread");
                    if (aclTask.isCancelled()) {
                        cancel();
                        break;
                    }

                    Log.i("XXXXXXXXX", "Count is " + i);
                }
            }
        }).callback(new DroiRunnable() {
            @Override
            public void run() {
                dialog.dismiss();
                findViewById(R.id.tentimes_test).setEnabled(true);
            }
        });
        task.runInBackground("DTA_Background_Thread2");
    }

    private DroiTask createClearDataTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final StringResult res = DroiBaaSTestCases.ClearAllData();
                if (res.res != null && !res.res.isEmpty())
                    cancel();

                DroiBaaSTestCases.checkStringResult(res.res, "clear all data");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (res.res == null || res.res.isEmpty()) {
                            currentState.append("clear all data OK" + "\n");
                        } else {
                            currentState.append("clear all data FAIL:" + res.res + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createDroiFileUploadTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final StringResult res = DroiBaaSTestCases.DroiFileUpload();
                if (res.res != null && !res.res.isEmpty())
                    cancel();

                DroiBaaSTestCases.checkStringResult(res.res, "upload DroiFile");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (res.res == null || res.res.isEmpty()) {
                            currentState.append("upload DroiFile OK" + "\n");
                        } else {
                            currentState.append("upload DroiFile FAIL:" + res.res + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createDroiFileDownloadTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final StringResult res = DroiBaaSTestCases.DroiFileDownload();
                if (res.res != null && !res.res.isEmpty())
                    cancel();

                DroiBaaSTestCases.checkStringResult(res.res, "download DroiFile");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (res.res == null || res.res.isEmpty()) {
                            currentState.append("download DroiFile OK" + "\n");
                        } else {
                            currentState.append("download DroiFile FAIL:" + res.res + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createReferenceTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final StringResult res = DroiBaaSTestCases.DroiReferenceTest();
                if (res.res != null && !res.res.isEmpty())
                    cancel();

                DroiBaaSTestCases.checkStringResult(res.res, "reference test");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (res.res == null || res.res.isEmpty()) {
                            currentState.append("reference test OK" + "\n");
                        } else {
                            currentState.append("reference test FAIL:" + res.res + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createPreferenceTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final boolean key = DroiPreference.instance().getBoolean("key", true);
                //DroiBaaSTestCases.checkStringResult(res.res, "reference test");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (key == false) {
                            currentState.append("Preference test OK" + "\n");
                        } else {
                            currentState.append("Preference test FAIL:" + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createCloudCacheTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                DroiCloudCache.set("keyName", "54678");
                // 由云端取回设定数据
                final DroiError error = new DroiError();
                final String value = DroiCloudCache.get("keyName", error);

                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (value.equals("5678") || error.isOk()) {
                            currentState.append("CloudCache test OK" + "\n");
                        } else {
                            currentState.append("CloudCache test FAIL:" + error.toString() + "\n");
                        }
                    }
                });
            }
        });
        return task;
    }

    private DroiTask createDataTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final Result res = DroiBaaSTestCases.dataInit();
                if (res.res == false)
                    cancel();

                DroiBaaSTestCases.checkResult(res.res, "dataInit");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {

                        if (res.res) {
                            currentState.append("dataInit OK" + "\n");
                            dialog.setTitle("Test Running - Data(create)");
                        } else {
                            currentState.append("dataInit FAIL:" + res.error.toString() + "\n");
                        }
                    }
                });
            }
        })
                .then(new DroiRunnable() {
                    @Override
                    public void run() {
                        final Result res = DroiBaaSTestCases.dataCreate();
                        if (res.res == false)
                            cancel();

                        DroiBaaSTestCases.checkResult(res.res, "dataCreate");
                        TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                            @Override
                            public void run() {
                                if (res.res) {
                                    currentState.append("dataCreate OK" + "\n");
                                    dialog.setTitle("Test Running - Data(read)");
                                } else {
                                    currentState.append("dataCreate FAIL" + res.error.toString() + "\n");
                                }
                            }
                        });
                    }
                })
                .then(new DroiRunnable() {
                    @Override
                    public void run() {
                        final StringResult res = DroiBaaSTestCases.dataRead();
                        if (res.res != null && !res.res.isEmpty())
                            cancel();

                        DroiBaaSTestCases.checkStringResult(res.res, "dataRead");
                        TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                            @Override
                            public void run() {
                                if (res.res == null || res.res.isEmpty()) {
                                    currentState.append("dataRead OK" + "\n");
                                    dialog.setTitle("Test Running - Data(update)");
                                } else {
                                    currentState.append("dataRead FAIL:" + res.res + "\n");
                                }
                            }
                        });
                    }
                })
                .then(new DroiRunnable() {
                    @Override
                    public void run() {
                        final StringResult res = DroiBaaSTestCases.dataUpdate();
                        if (res.res != null && !res.res.isEmpty())
                            cancel();

                        DroiBaaSTestCases.checkStringResult(res.res, "dataUpdate");
                        TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                            @Override
                            public void run() {
                                if (res.res == null || res.res.isEmpty()) {
                                    currentState.append("dataUpdate OK" + "\n");
                                    dialog.setTitle("Test Running - Data(delete)");
                                } else {
                                    currentState.append("dataUpdate FAIL:" + res.res + "\n");
                                }
                            }
                        });
                    }
                })
                .then(new DroiRunnable() {
                    @Override
                    public void run() {
                        final Result res = DroiBaaSTestCases.dataDelete();

                        if (res.res == false)
                            cancel();

                        DroiBaaSTestCases.checkResult(res.res, "dataDelete");
                        TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                            @Override
                            public void run() {
                                if (res.res) {
                                    currentState.append("dataDelete OK" + "\n");
                                } else {
                                    currentState.append("dataDelete FAIL" + res.error.toString() + "\n");
                                }
                            }
                        });

                    }
                });
        return task;
    }

    private DroiTask createACLTestTask(final ProgressDialog dialog) {
        DroiTask task = DroiTask.create(new DroiRunnable() {
            @Override
            public void run() {
                final Result res = DroiBaaSTestCases.aclTest();

                if (res.res == false)
                    cancel();

                DroiBaaSTestCases.checkResult(res.res, "aclTest");
                TaskDispatcher.getDispatcher(TaskDispatcher.MainThreadName).enqueueTask(new Runnable() {
                    @Override
                    public void run() {
                        if (res.res) {
                            currentState.append("ACL TEST OK" + "\n");
                        } else {
                            currentState.append("aclTest FAIL:" + res.error + "\n");
                        }
                    }
                });
            }
        });

        return task;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private TextView currentState;
}
