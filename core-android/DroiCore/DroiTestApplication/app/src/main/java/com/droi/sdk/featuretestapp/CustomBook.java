package com.droi.sdk.featuretestapp;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiQueryAnnotation;
import com.droi.sdk.core.DroiReference;


@DroiQueryAnnotation
public class CustomBook extends DroiObject {
    @DroiExpose
    public String name;

    @DroiExpose
    public int price;

    @DroiReference
    public CustomAuthor author;
}
