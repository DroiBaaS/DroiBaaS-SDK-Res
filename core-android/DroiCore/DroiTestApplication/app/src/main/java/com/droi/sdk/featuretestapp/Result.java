package com.droi.sdk.featuretestapp;

import com.droi.sdk.DroiError;

/**
 * Created by chenpei on 16/7/15.
 */
public class Result {

    Result(Boolean res,DroiError error){
        this.res = res;
        this.error=error;
    }
    Boolean res;
    DroiError error;
}
