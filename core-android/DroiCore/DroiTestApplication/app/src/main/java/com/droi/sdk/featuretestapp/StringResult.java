package com.droi.sdk.featuretestapp;

import com.droi.sdk.DroiError;

/**
 * Created by chenpei on 16/7/15.
 */
public class StringResult {

    StringResult(String res, DroiError error){
        this.res = res;
        this.error=error;
    }
    String  res;
    DroiError error;
}
