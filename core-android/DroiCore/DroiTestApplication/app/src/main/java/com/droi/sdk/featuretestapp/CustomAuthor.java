package com.droi.sdk.featuretestapp;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiQueryAnnotation;

@DroiQueryAnnotation
public class CustomAuthor extends DroiObject {
    @DroiExpose
    public String name;

    @DroiExpose
    public String Country;
}
