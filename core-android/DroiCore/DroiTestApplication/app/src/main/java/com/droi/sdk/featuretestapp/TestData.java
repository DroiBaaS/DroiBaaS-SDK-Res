package com.droi.sdk.featuretestapp;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiQueryAnnotation;

@DroiQueryAnnotation
public class TestData extends DroiObject {
    @DroiExpose
    public String name;

    @DroiExpose
    public int value;

    @DroiExpose
    public String description;
}
