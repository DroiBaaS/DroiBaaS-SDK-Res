package com.droi.sdk.featuretestapp;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiFile;
import com.droi.sdk.core.DroiObject;

/**
 * Created by chenpei on 16/7/18.
 */
public class MySongs extends DroiObject {
    @DroiExpose
    public String Name;

    @DroiExpose
    public DroiFile Cover;
}